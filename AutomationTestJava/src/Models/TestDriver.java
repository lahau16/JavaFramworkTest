package Models;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;

import org.openqa.selenium.WebDriver;

import Common.ConsoleType;
import Common.Constant;
import Common.GlobalConfig;
import Helpers.CommonHelper;
import Helpers.DriverHelper;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestDriver {
	public void Run() throws IOException {
		CommonHelper.WriteConsole("Reading Config...");

		// Read Config
		GlobalConfig config = null;
		try {
			config = GlobalConfig.Instant();
		} catch (Exception ex) {
			CommonHelper.WriteConsole("Can't read Config. Exception: " + ex.getMessage(), ConsoleType.Error);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			br.readLine();
			return;
		}

		CommonHelper.WriteConsole("Setting up report test...");
		String pathReport = config.Config.OutputTestFile == null
				? (config.Config.ReportPath == null ? "" : config.Config.ReportPath) + Constant.TestReportFile
				: (config.Config.ReportPath == null ? "" : config.Config.ReportPath) + config.Config.OutputTestFile;
		TestClass.extent = new ExtentReports(pathReport, true);
		TestClass.extent.loadConfig(new File("./extentReportConfig.xml"));

		
		var cPros = CommonConfig.class.getDeclaredFields();
		for(var i = 0; i < cPros.length; i++) {
			try {
				TestClass.extent.addSystemInfo(cPros[i].getName(), (String) cPros[i].get(config));
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		 

		CommonHelper.WriteConsole("Test Running...");
		config.TestCases.forEach((testCase) -> {
			ExtentTest testReport = null;
			if (!testCase.IsEnabled) {
				testReport = TestClass.extent.startTest(testCase.Name, testCase.Description);
				testReport.log(LogStatus.SKIP, "'{testCase.Name}' had been skip by User");
				return;
			}
			try {
				Class<?> c = Class.forName(testCase.AssemblyName + "." + testCase.Class);
				if (c != null) {
					var methodTest = c.getMethod(testCase.Method);
					if (methodTest != null) {
						Constructor<?> ctor= c.getConstructor();
						Object instance = ctor.newInstance();

						var test = c.getSuperclass().getDeclaredField("Test");
						var driverPro = c.getSuperclass().getDeclaredField("Driver");
						var versionPro = c.getSuperclass().getDeclaredField("DataVersion");
						var testNamePro = c.getSuperclass().getDeclaredField("TestName");
						try {
							testNamePro.set(instance, testCase.Name);
							versionPro.set(instance, testCase.DataVerion);
							test.set(instance, TestClass.extent.startTest(testCase.Name, testCase.Description));
							var driver = DriverHelper.Create(GlobalConfig.Instant().Config.getDriverType());
							driverPro.set(instance, driver);

							methodTest.invoke(instance);
							driver = (WebDriver)driverPro.get(instance);
							if (driver != null) {
								driver.close();
								driver.quit();
							}
							CommonHelper.WriteConsole("Test Name {testCase.Name} has been successfull!");
							return;
						} catch (Exception ex) {
							var failedTestMethod = c.getSuperclass().getDeclaredMethod("FailedTest");
							failedTestMethod.invoke(instance, new Object[] {
									ex.getMessage() });
							var driver = driverPro.get(instance);
							if (driver != null) {
								((WebDriver) driver).close();
								((WebDriver) driver).quit();
							}
							CommonHelper.WriteConsole(
									"TestName " + testCase.Name + " failed with exception: " + ex.getMessage(),
									ConsoleType.Error);
							return;
						}
					}
				}
			} catch(Exception ex) {
				testReport = TestClass.extent.startTest(testCase.Name, testCase.Description);
				testReport.log(LogStatus.ERROR, "TestName " + testCase.Name + " failed with exception: Can't load this testcase");
				CommonHelper.WriteConsole("TestName " + testCase.Name + " failed with exception: Can't load this testcase",
						ConsoleType.Error);
				return;
			}
		});

		CommonHelper.WriteConsole("Plush report test...");
		TestClass.extent.flush();

		CommonHelper.WriteConsole("Test Completed");
		// Console.ReadLine();
	}
}
