import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.nio.file.Paths;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DigitalUniteFormTest {

    WebDriver driver;

    @BeforeAll
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Automate Digital Unite Webform")
    public void testDigitalUniteWebformWithTodaysDateField() {
        driver.get("https://www.digitalunite.com/practice-webform-learners");

        // Handle cookie popup
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement accept = shortWait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Accept All Cookies')]")
                    )
            );
            accept.click();
        } catch (TimeoutException ignore) {}

        driver.findElement(By.xpath("//input[@id='edit-name']")).sendKeys("John Doe");
        driver.findElement(By.id("edit-number")).sendKeys("01234567890");

        WebElement todaysDateField = driver.findElement(By.xpath("//input[@id='edit-date']"));
        String dateToInput = "12/15/2024";
        todaysDateField.clear();
        todaysDateField.sendKeys(dateToInput);
        System.out.println("Date manually inputted: " + dateToInput);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


        driver.findElement(By.xpath("//input[@id='edit-email']")).sendKeys("john@example.com");
        driver.findElement(By.xpath("//textarea[@id='edit-tell-us-a-bit-about-yourself-']")).sendKeys("This is a test submission.");


        // scroll
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", 0, 400);

        // Upload file
        WebElement fileUpload = driver.findElement(By.xpath("//input[@id='edit-uploadocument-upload']"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileUpload);


        String filePath = Paths.get("src", "test", "resources", "testfile.pdf").toAbsolutePath().toString();
        fileUpload.sendKeys(filePath);


        WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));  // Increased wait time
        try {
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert detected: " + alert.getText());


            alert.accept();
            System.out.println("Alert accepted, continuing the test.");
        } catch (TimeoutException e) {
            System.out.println("No alert present or alert was not triggered within the expected time.");
        }


        WebElement checkbox = driver.findElement(By.xpath("//input[@id='edit-age']"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);


//        wait.until(ExpectedConditions.elementToBeClickable(checkbox));


        if (!checkbox.isSelected()) {
            try {
                checkbox.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            }
        }


        WebElement submitButton = driver.findElement(By.xpath("//input[@id='edit-submit']"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);


//        wait.until(ExpectedConditions.elementToBeClickable(submitButton));


        try {
            submitButton.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        }


        WebElement successMessage = driver.findElement(By.xpath("//h1[normalize-space()='Thank you for your submission!']"));
        Assertions.assertEquals("Thank you for your submission!", successMessage.getText());
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}