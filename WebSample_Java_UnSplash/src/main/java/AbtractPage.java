import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbtractPage {
    public static WebDriver driver;
    WebDriverWait waitExplicit;
    Actions action;
    long longTimeout = 30;
    String downloadFilepath = System.getProperty("user.dir") + "/DownloadedFile";
    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
    ChromeOptions options = new ChromeOptions();

    public AbtractPage() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        options.setExperimentalOption("prefs", chromePrefs);
        if(driver == null) {
            driver = new ChromeDriver(options);
        }
        waitExplicit = new WebDriverWait(driver, longTimeout);
        action = new Actions(driver);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver web) {
        driver = web;
    }

    public void gotoUrl(String pageURL) {
        driver.get(pageURL);
    }

    public void waitToElementPresence(String locator) {
        waitExplicit.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
    }

    public void clickOnElement(String xpath) {
        waitToElementPresence(xpath);
        driver.findElement(By.xpath(xpath)).click();
    }

    public void sendTextToElement(String xpath, String value) {
        waitToElementPresence(xpath);
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(value);
    }

    public void hoverOnElement(String xpath) {
        waitToElementPresence(xpath);
        action.moveToElement(driver.findElement(By.xpath(xpath)))
                .perform();
        sleepInSecond(2);
    }

    public String getColorValue(String xpath) {
        waitToElementPresence(xpath);
        return driver.findElement(By.xpath(xpath)).getCssValue("background-color");
    }

    public String getTextValue(String xpath) {
        waitToElementPresence(xpath);
        return driver.findElement(By.xpath(xpath)).getText();
    }

    public void sleepInSecond(long numberInSecond) {
        try {
            Thread.sleep(numberInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isElementDisplay(String xpath) {
        waitToElementPresence(xpath);
        return driver.findElement(By.xpath(xpath)).isDisplayed();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getLinkText(String xpath) {
        waitToElementPresence(xpath);
        return driver.findElement(By.xpath(xpath)).getAttribute("href");
    }

    public List<String> getDownloadedName() {
        List<String> listDownloadedImange = new ArrayList<String>();
        File[] names = new File(downloadFilepath).listFiles();
        for (File file : names) {

            listDownloadedImange.add(file.getName());
        }
        return listDownloadedImange;
    }

    public void CleanDownloadDirectory() {
        File[] files = new File(downloadFilepath).listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    public void clickCoordinate(int x, int y) {
        action.moveByOffset(x, y).click().perform();
    }
}
