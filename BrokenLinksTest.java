import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrokenLinksTest {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // Counters
        int totalLinks = 0;
        int validLinksCount = 0;
        int brokenLinksCount = 0;
        int errorLinksCount = 0;

        try {
            // 3️⃣ Open the website (make sure URL is reachable)
            String siteURL = "https://www.google.com"; // <-- Valid site
            System.out.println("Opening website: " + siteURL);
            driver.get(siteURL);

            // Maximize window
            driver.manage().window().maximize();

            // 4️⃣ Get all links on the page
            List<WebElement> links = driver.findElements(By.tagName("a"));
            totalLinks = links.size();
            System.out.println("\nTotal links found: " + totalLinks + "\n");

            // 5️⃣ Check each link
            for (WebElement link : links) {
                String url = link.getAttribute("href");

                if (url == null || url.isEmpty()) {
                    System.out.println("⚠ Skipping empty or missing URL for link: " + link.getText());
                    continue;
                }

                try {
                    HttpURLConnection connection = (HttpURLConnection) (new URL(url).openConnection());
                    connection.setRequestMethod("HEAD");
                    connection.setConnectTimeout(3000); // 3 seconds timeout
                    connection.connect();

                    int responseCode = connection.getResponseCode();

                    if (responseCode >= 400) {
                        System.out.println("❌ Broken link: " + url + " | Code: " + responseCode);
                        brokenLinksCount++;
                    } else {
                        System.out.println("✅ Valid link: " + url + " | Code: " + responseCode);
                        validLinksCount++;
                    }

                } catch (Exception e) {
                    System.out.println("⚠ Error checking link: " + url + " | Exception: " + e.getMessage());
                    errorLinksCount++;
                }
            }

        } finally {
            // 6️⃣ Close browser
            driver.quit();
        }

        // 7️⃣ Summary report
        System.out.println("\n==============================");
        System.out.println("       TEST SUMMARY REPORT    ");
        System.out.println("==============================");
        System.out.println("Total Links Found:   " + totalLinks);
        System.out.println("✅ Valid Links:       " + validLinksCount);
        System.out.println("❌ Broken Links:      " + brokenLinksCount);
        System.out.println("⚠ Error Links:       " + errorLinksCount);
        System.out.println("==============================");
        System.out.println("Test Completed Successfully!");
    }
    }

