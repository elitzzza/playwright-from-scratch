package playwright.toolsshop.catalog.pageobject;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolsshop.ScreenshotManager;

public class NavBar {
    private final Page page;
    
    public NavBar(Page page) {
        this.page = page;
    }
@Step("Open shopping cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    ScreenshotManager.takeScreenshot(page,"Open shopping cart");
    }

    @Step("Open home page")
    public void openHomePage() {
        page.navigate("https://www.practicesoftwaretesting.com");
        ScreenshotManager.takeScreenshot(page,"Open home page");
    }
}
