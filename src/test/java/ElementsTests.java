import org.testng.Assert;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.*;

public class ElementsTests {
    @Test
    public void selectRadioButtonTest() {
        open("https://demoqa.com/");
        $("#menu-item-144").click();
        $("#item-1").click();
        $("#impressiveRadio").click();
        String selectedText = $("#radio-button-result").getText();
        Assert.assertEquals(selectedText, "You have selected Impressive");
    }

    @Test(dependsOnMethods = "selectRadioButtonTest")
    public void clickButtonsTest() {
        open("https://demoqa.com/");
        $("#menu-item-144").click();
        $("#item-0").click();
        $("#dynamicClickMessage").click();
        String dynamicClickText = $("#dynamicClickMessage").getText();
        Assert.assertEquals(dynamicClickText, "You have done a dynamic click");
        $("#rightClickBtn").contextClick();
        String rightClickText = $("#rightClickMessage").getText();
        Assert.assertEquals(rightClickText, "You have done a right click");
    }
}
