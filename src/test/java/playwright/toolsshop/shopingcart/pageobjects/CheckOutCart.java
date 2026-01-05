package playwright.toolsshop.shopingcart.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolsshop.ScreenshotManager;
import playwright.toolsshop.domain.CartLineItems;

import java.util.List;

public class CheckOutCart {

    private final Page page;

    public CheckOutCart(Page page) {
        this.page = page;
    }

    @Step("Get cart line items")
    public List<CartLineItems> getLineItems(){
        page.getByTestId("proceed-1").waitFor();
        return  page.locator("tbody tr")
                .all()
                .stream()
                .map(row ->{
                    String title = trimmed(row.getByTestId("product-title").innerText());
                    int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                    double price = Double.parseDouble(price(row.getByTestId("product-price").innerText()));
                    double linePrice = Double.parseDouble(price(row.getByTestId("line-price").innerText()));
                    return new CartLineItems(title, quantity, price, linePrice);
                })
                .toList();


    }

    private String trimmed(String value) {
        return value.strip().replace("\u00A0", "");
    }

    private String price(String value){
        return value.replace("$", "");
    }
}
