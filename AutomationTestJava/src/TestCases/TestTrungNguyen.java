package TestCases;

import org.openqa.selenium.By;

import Models.TestClass;

public class TestTrungNguyen extends TestClass {
	
	public void Login() throws Exception
    {
        Driver.navigate().to("http://27.71.232.114:40002/");
        Thread.sleep(3000);

        //Login
        Driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/div/div[2]/div[1]/input")).sendKeys("admin");
        Driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/div/div[2]/div[2]/input")).sendKeys("Abcd1234");
        Driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/div/div[2]/div[3]/div/label/input")).click();
        Thread.sleep(3000);
        Driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/div/div[2]/div[4]/div[1]/button")).click();
        Thread.sleep(3000);
        AssertAndCapture(Driver.getCurrentUrl(), "http://27.71.232.114:40002/admin");
        Thread.sleep(3000);
    }
}
