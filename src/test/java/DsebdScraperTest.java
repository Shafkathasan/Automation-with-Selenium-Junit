import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DsebdScraperTest  {

    WebDriver driver;
    String url = "https://dsebd.org/latest_share_price_scroll_by_value.php";
    String filePath = Paths.get("src", "test", "resources", "dse_data.txt").toAbsolutePath().toString();

    @BeforeAll
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(url);
    }

    @Test
    void scrapeDSE() throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        List<WebElement> tableContainer = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy((By.className("table-responsive"))));
        WebElement table = tableContainer.get(0); //locating table

        FileWriter writer = new FileWriter(filePath);

        scrapeHeaders(wait, writer);

        scrapeRows(writer, table);

        writer.close();
        System.out.println("Table data scraped and saved to: " + filePath);
    }

    void scrapeHeaders(WebDriverWait wait, FileWriter writer) throws IOException {
        List<WebElement> headerContainer = wait.until(ExpectedConditions.
                presenceOfAllElementsLocatedBy(By.className("floatThead-container"))); //headers
        List<WebElement> headers = headerContainer.get(0).findElements(By.tagName("table"));
        for (WebElement header : headers) {
            writer.write(header.getText() + "\t");
            System.out.print(header.getText() + "   \t");

        }
        writer.write("\n");
        System.out.println();

    }

    void scrapeRows(FileWriter writer, WebElement table) throws IOException {
        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr")); //rows

        int rowNum = 1;

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            int cellNum = 1;
            for (WebElement cell : cells) {
                String cellText = cell.getText().trim();
                writer.write(cellText + "\t");
                System.out.print(cellText + "\t");
                cellNum++;
            }
            writer.write("\n");
            System.out.println();
            rowNum++;
        }

    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}