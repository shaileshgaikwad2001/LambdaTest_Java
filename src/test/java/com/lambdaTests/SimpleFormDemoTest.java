package com.lambdaTests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SimpleFormDemoTest {

    String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
    String accesskey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");

    public static RemoteWebDriver driver;
    public static String Status="failed";
    String gridURL = "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    public void setUp(Method m, ITestContext ctx) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("version", "latest");
        capabilities.setCapability("platform", "win10"); // If this cap isn't specified, it will just get any available one.
        capabilities.setCapability("build", this.getClass().getSimpleName());
        capabilities.setCapability("name", m.getName() + " - " + this.getClass().getName());
        capabilities.setCapability("plugin", "git-testng");
        capabilities.setCapability("network", true);
        capabilities.setCapability("visual", true);
        capabilities.setCapability("video", true);
        capabilities.setCapability("console", true);
        capabilities.setCapability("terminal", true);

        String[] Tags = new String[] { "Feature", "Falcon", "Severe" };
        capabilities.setCapability("tags", Tags);

        try {
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
        } catch (MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSimpleFormDemo() {
        ArrayList<String> exceptionCapture = new ArrayList<>();
        try {
            driver.get("https://www.lambdatest.com/selenium-playground/");

            driver.findElement(By.linkText("Simple Form Demo")).click();

            String expUrl = "https://www.lambdatest.com/selenium-playground/simple-form-demo";
            Assert.assertEquals(driver.getCurrentUrl(), expUrl);

            String message = "Welcome to LambdaTest";
            driver.findElement(By.id("user-message")).sendKeys(message);
            driver.findElement(By.xpath("//*[@id=\"showInput\"]")).click();
            String actualMessage = driver.findElement(By.cssSelector("#message")).getText();
            Assert.assertEquals(actualMessage, message);
            Status = "passed";
        } catch (AssertionError e) {
            Status = "failed";
            exceptionCapture.add(e.getMessage());
            ((JavascriptExecutor) driver).executeScript("lambda-exceptions", exceptionCapture);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.executeScript("lambda-status=" + Status);
            driver.quit();
        }
    }
}
