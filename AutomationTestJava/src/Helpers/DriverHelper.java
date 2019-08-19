package Helpers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import Common.DriverType;

public class DriverHelper {
	public static WebDriver Create(DriverType type) throws Exception
    {
        switch(type)
        {
            case ChromeDriver:
            	System.setProperty("webdriver.chrome.driver","./chromedriver.exe");
                return new ChromeDriver();
//            case FireFoxDriver:
//                Encoding.RegisterProvider(CodePagesEncodingProvider.Instance);
//                FirefoxOptions opt = new FirefoxOptions
//                {
//                    Profile = new FirefoxProfile(Directory.GetCurrentDirectory())
//                };
//                var service = FirefoxDriverService.CreateDefaultService(Directory.GetCurrentDirectory(), "geckodriver.exe");
//                return new FirefoxDriver(service, opt, TimeSpan.FromMinutes(1));
            case IEDriver:
                InternetExplorerOptions opts = new InternetExplorerOptions();
                opts.ignoreZoomSettings();
                opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                opts.introduceFlakinessByIgnoringSecurityDomains();
                var ieDriver = new InternetExplorerDriver(opts);
                return ieDriver;
            default:
                throw new Exception("Wrong driver type");
        }
    }
}
