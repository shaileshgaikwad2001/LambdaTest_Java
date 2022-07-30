package com.lambdaTests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;

public class InputFormSubmitTest {

    String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
    String accesskey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");

    public static RemoteWebDriver driver;
    public static String Status="failed";
    String gridURL = "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    public void setUp(Method m, ITestContext ctx) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Safari");
        capabilities.setCapability("version", "latest");
        capabilities.setCapability("platform", "MacOS Catalina"); // If this cap isn't specified, it will just get any available one.
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
    public void testInputFormSubmit() {
        ArrayList<String> exceptionCapture = new ArrayList<>();
        try {
            driver.get("https://www.lambdatest.com/selenium-playground/");

            driver.findElement(By.linkText("Input Form Submit")).click();

            WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Submit']")));

            driver.findElement(By.xpath("//button[text()='Submit']")).click();

            String message = driver.findElement(By.name("name")).getAttribute("validationMessage");
            Assert.assertEquals(message,"Fill out this field");
            driver.findElement(By.id("name")).sendKeys("Shailesh");
            driver.findElement(By.id("inputEmail4")).sendKeys("Shaileshgaikwad20@gmail.com");
            driver.findElement(By.id("inputPassword4")).sendKeys("Test1234");
            driver.findElement(By.id("company")).sendKeys("Visa");
            driver.findElement(By.id("websitename")).sendKeys("www.Visa.com");
            driver.findElement(By.id("websitename")).sendKeys("www.Visa.com");
            Select select = new Select(driver.findElement(By.name("country")));
            select.selectByVisibleText("United States");
            driver.findElement(By.id("inputCity")).sendKeys("Pune");
            driver.findElement(By.id("inputAddress1")).sendKeys("Wakad");
            driver.findElement(By.id("inputAddress2")).sendKeys("Kaspate Vasti Road");
            driver.findElement(By.id("inputState")).sendKeys("Maharashtra");
            driver.findElement(By.id("inputZip")).sendKeys("411057");
            driver.findElement(By.xpath("//button[text()='Submit']")).click();
            Assert.assertEquals(driver.findElement(By.xpath("//form/following-sibling::p")).getText(),"Thanks for contacting us, we will get back to you shortly.");
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
