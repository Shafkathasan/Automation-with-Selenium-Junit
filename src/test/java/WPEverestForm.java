import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WPEverestForm {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String url = "https://demo.wpeverest.com/user-registration/guest-registration-form/";

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
        driver.get(url);
    }

    @Test
    @DisplayName("Fill and submit Guest Registration form")
    void fillAndSubmitRegistrationForm() {
        // 1. First Name & Last Name
        driver.findElement(By.id("first_name")).sendKeys("Raju");
        driver.findElement(By.id("last_name")).sendKeys("Mia");

        // 2. Randomized Email
        randomEmail();

        // 3. Gender
        driver.findElement(By.id("radio_1665627729_Male")).click();

        // 4. Password (required by WP Everest form)
        driver.findElement(By.id("user_pass")).sendKeys("0qwertY1234@");

        // 5. Date of Birth via flatpickr
        datePicker();

        // 6. Nationality
        driver.findElement(By.id("country_1665629257")).sendKeys("Bangladesh");

        // 7. Phone and Emergency Contact
        numberPicker();

        // 8. Country (dropdown – Bangladesh)
        driver.findElement(By.id("input_box_1665629217")).sendKeys("Bangladesh");

        // 9. Terms & Conditions
        driver.findElement(By.id("privacy_policy_1665633140")).click();

        // 10. Submit
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 11. Assert success message
        String message = wait
                .until(d -> d.findElement(By.cssSelector("div#ur-submit-message-node ul")).getText());
        assertTrue(message.contains("User successfully registered."),
                "Expected registration success message, got: " + message);
    }

    private void randomEmail() {
        WebElement email = driver.findElement(By.id("user_email"));
        int n = (int)(Math.random() * 1000);
        email.sendKeys("testuser" + n + "@example.com");
    }

    private void numberPicker() {
        // scroll so phone fields are in view
        scroll(0, 400);

        // Phone
        List<WebElement> phones = driver.findElements(By.id("phone_1665627880"));
        phones.get(1).sendKeys("01234567890");

        // Emergency Contact
        List<WebElement> ephones = driver.findElements(By.id("phone_1665627865"));
        ephones.get(1).sendKeys("09876543210");
    }

    private void datePicker() {
        WebElement dobInput = driver.findElement(By.cssSelector("input.flatpickr-input"));
        // open the flatpickr calendar
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click',{bubbles:true}));",
                dobInput
        );
        // wait for calendar to open and click today
        wait.until(ExpectedConditions.attributeContains(dobInput, "class", "active"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flatpickr-calendar.open")));
        WebElement today = driver.findElement(By.cssSelector("span.flatpickr-day.today"));
        today.click();
    }

    private void scroll(int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
