package Models;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Common.Constant;
import Common.GlobalConfig;

public abstract class TestClass {
	public static ExtentReports extent = null;

	public <T> void AssertAndCapture(T input, T output) throws Exception {
		assert input == output;

		if (input == null) {
			if (output != null) {
				Test.log(LogStatus.ERROR, "Assert failed with value '" + input + "' and '" + output + "'");
				throw new Exception("Assert failed with value '" + input + "' # '" + output + "'");
			}
		} else {
			if (!input.equals(output)) {
				Test.log(LogStatus.ERROR, "Assert failed with value '" + input + "' and '" + output + "'");
				AddScreenCaptureFromPath();

				throw new Exception("Assert failed with value '" + input + "' # '" + output + "'");
			} else {
				PassTest("Assert failed with value '" + input + "' and '" + output + "'");
			}

		}

	}

	public <T> void AssertNotAndCapture(T input, T output) throws Exception {
		if (input.equals(output)) {
			Test.log(LogStatus.ERROR, "Assert failed with value '" + input + "' and '" + output + "'");
			AddScreenCaptureFromPath();
			throw new Exception("Assert failed with value '" + input + "' # '" + output + "'");

		} else {
			PassTest("Assert failed with value '" + input + "' and '" + output + "'");
			AddScreenCaptureFromPath();
		}
	}

	public WebDriver Driver = null;
	public ExtentTest Test = null;
	public String DataVersion;
	public String TestName;

	public String KeyWord(String key) throws Exception {
		return GlobalConfig.Instant().TestKeywords.get(DataVersion).get(TestName).get(key);
	}

	public String CommonKeyWord(String key) throws Exception {
		return GlobalConfig.Instant().TestKeywords.get(DataVersion).get("Common").get(key);
	}

	public void AddSystemInfo(String key, String value) {
		// extent.AddSystemInfo(key, value);

	}

	public void AddScreenCaptureFromPath() throws Exception {

		if (Driver != null) {
			TakesScreenshot ts = (TakesScreenshot) Driver;
			File screenshoot = ts.getScreenshotAs(null);
			String path = Constant.ReportPath + "/"
					+ (GlobalConfig.Instant().Config.ReportPath == null ? "" : GlobalConfig.Instant().Config.ReportPath)
					+ "/" + "ScreenShots/";
			File directory = new File(path);
			if (!directory.exists()) {
				directory.mkdir();
			}
			String imageName = UUID.randomUUID().toString() + ".png";
			String localpath = path + imageName;
			// Move image file to new destination
			File savedFile = new File(localpath);
			// Copy file at destination
			try {
				FileUtils.copyFile(screenshoot, savedFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Test.addScreencast((GlobalConfig.Instant().Config.ReportPath == null ? ""
					: GlobalConfig.Instant().Config.ReportPath + "/ScreenShots/" + imageName));
		}
	}

	public void Infor(String message) {
		Test.log(LogStatus.INFO, message);
	}

	public void PassTest(String message) {
		Test.log(LogStatus.PASS, message);
	}

	public void SkipTest(String message) {
		Test.log(LogStatus.SKIP, message);
	}

	public void FailedTest(String message) {
		Test.log(LogStatus.FAIL, message);
	}

	public void WarningTest(String message) {
		Test.log(LogStatus.WARNING, message);
	}
}
