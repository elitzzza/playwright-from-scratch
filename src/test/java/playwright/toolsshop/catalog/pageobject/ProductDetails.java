package playwright.toolsshop.catalog.pageobject;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolsshop.ScreenshotManager;

public class ProductDetails {
    private final Page page;

    public ProductDetails(Page page) {
        this.page = page;
    }

    @Step("Increase quantity by {quantity}")
    public void increaseQuantityBy(int quantity) {
        for (int i = 1; i < quantity; i++) {
            page.getByTestId("increase-quantity").click();
        }

    }

    @Step("Add product to cart")
    public void addToCart() {
        page.waitForResponse(response -> response
                        .url().contains("/carts") && response.request().method().equals("POST"),
                () -> page.getByText("Add to cart").click());
        page.locator("#toast-container").waitFor();


    }
}
