package playwright.toolsshop.catalog.pageobject;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolsshop.domain.ProductSummary;

import java.util.List;

public class ProductList {

    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    public List<ProductSummary> getProductSummaries() {
        return page.locator(".card").all()
                .stream()
                .map(productCard ->{
                  String productName = productCard.getByTestId("product-name").innerText().strip();
                  String productPrice = productCard.getByTestId("product-price").innerText().strip();
                  return new ProductSummary(productName, productPrice);
                }).toList();
    }

    @Step("Get product names")
    public List<String> getProductNames() {
        page.getByTestId("product-name").first().waitFor();
        return page.getByTestId("product-name").allInnerTexts();

    }

    @Step("View product details")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();

    }

    @Step("Get search complete message")
    public String getSearchCompleteMessage() {
       return  page.getByTestId("search_completed").innerText();
    }
}
