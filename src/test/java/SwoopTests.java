import com.codeborne.selenide.Configuration;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement;
import java.util.Random;

import io.qameta.allure.Attachment;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeOptions;

import com.codeborne.selenide.Screenshots;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

@Epic("Swoop Tests")
public class SwoopTests {
    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opt = new ChromeOptions();
        opt.setExperimentalOption("debuggerAddress", "localhost:9988");
        WebDriver driver = new ChromeDriver(opt);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
    }


    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        File screenshotFile = Screenshots.takeScreenShotAsFile();
        try {
            return FileUtils.readFileToByteArray(screenshotFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test(priority = 1)
    @Description("Verify login error message")
    @Story("Login")
    public void testLoginErrorMessage() {
        open("https://www.swoop.ge/");

        $(byText("შესვლა")).click();
        $(byName("email")).setValue(generateRandomEmail());
        $(byName("password")).setValue(generateRandomPassword(8));
        $(byText("ავტორიზაცია")).click();

        $(byText("მეილი ან პაროლი არასწორია, თუ დაგავიწყდათ პაროლი,გთხოვთ ისარგებლოთ პაროლის აღდგენის ფუნქციით!"))
                .shouldBe(visible);
        $(byName("password")).shouldHave(value(""));
        takeScreenshot();

    }

    @Test(priority = 2, dependsOnMethods = "testLoginErrorMessage")
    @Description("Verify registration form")
    @Story("Registration")
    public void testRegistrationForm() {
        open("https://www.swoop.ge/");
        $(byText("შესვლა")).click();
        $(byText("რეგისტრაცია")).click();
        $(byText("ფიზიკური პირი")).shouldBe(visible, enabled).click();
        $(byText("რეგისტრაცია")).click();
        $(byId("receive-news")).shouldBe(checked);
        $(byCssSelector(".registration-container .name")).shouldBe(visible);
        takeScreenshot();

    }

    @Test(priority = 3)
    @Description("Verify that the price of each filtered item is within the specified range")
    public void testPriceRangeFilter() {
        open("https://www.swoop.ge/");
        $(byText("დასვენება")).click();
        $(byName("min-price")).setValue("170");
        $(byName("max-price")).setValue("180");
        $(byText("ძებნა")).click();
        $$(".item-list-item .price")
                .stream()
                .map(SelenideElement::getText)
                .map(price -> price.replace("$", ""))
                .mapToInt(Integer::parseInt)
                .forEach(priceValue -> {
                    Assert.assertTrue(priceValue >= 170 && priceValue <= 180);
                    takeScreenshot();
                });
    }


    private String generateRandomEmail() {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder email = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            email.append(randomChar);
        }

        email.append("@example.com");
        return email.toString();
    }


    private String generateRandomPassword(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

}
