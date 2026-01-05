package playwright.toolsshop.catalog.pageobject;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolsshop.ScreenshotManager;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchComponent {
    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    @Step("Search for product by keyword")
    public void searchBy(String keyword) {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8).replace("+", "%20");
        page.waitForResponse("**/products/search?q=" + encodedKeyword, () -> {
            page.locator("#search-query").fill(keyword);
            page.getByTestId("search-submit").click();
        });
    }
@Step("Clear search")
    public void clearSerach() {
        page.waitForResponse("**/products**", () -> {
            page.getByTestId("search-reset").click();
        });

    }

    public void filterByCategory(String category) {
        page.waitForResponse("**/products**by_category=**", () -> {
            page.getByLabel(category).click();
        });
    }

    public void sortBy(String sortingCriterion) {
        page.waitForResponse("**/products?page=**", () -> {
            page.getByTestId("sort").selectOption(sortingCriterion);
        });
    }
}
