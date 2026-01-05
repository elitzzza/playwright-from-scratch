package playwright.toolsshop.catalog;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import playwright.examples.AnnotatedTest;
import playwright.toolsshop.ChromeHeadlessOptions;
import playwright.toolsshop.PlaywrightRunConfiguration;
import playwright.toolsshop.TakeFinalScreenshot;
import playwright.toolsshop.catalog.pageobject.NavBar;
import playwright.toolsshop.catalog.pageobject.ProductDetails;
import playwright.toolsshop.catalog.pageobject.ProductList;
import playwright.toolsshop.catalog.pageobject.SearchComponent;
import playwright.toolsshop.shopingcart.pageobjects.CheckOutCart;

import java.util.List;
@Feature( "Product catalog")
@UsePlaywright(ChromeHeadlessOptions.class)
public class SearchForProductTest  implements TakeFinalScreenshot {
    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckOutCart checkoutCart;

    @BeforeEach
    void openHomePage(Page page) {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @BeforeEach
    void setUp(Page page) {
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckOutCart(page);
    }


    @Test
    @DisplayName("Load product page")
    @Feature("Load products")
    void shouldShowProductNames(Page page){
        List<String> productNames = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(productNames).contains("Pliers", "Bolt cutters", "Hammer");
    }

    @Test
    @DisplayName("Search item With page objects")
    @Feature("Search items")
    void searchItemWithPageObjects(Page page) {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("tape");
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains("Sheet Sander", "Belt Sander");
    }

    @Test
    @DisplayName("When there are no matching results")
    @Feature("Search items")
    void whenThereAreNoMatchingResults(Page page) {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("abcde");
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).isEmpty();
        Assertions.assertThat(productList.getSearchCompleteMessage());
    }

    @Test
    @DisplayName("Clear search results")
    @Feature("Search items")
    void clearSearchResults(Page page) {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("saw");
        searchComponent.clearSerach();
        var matchingProducts = productList.getProductNames();

        Assertions.assertThat(matchingProducts).hasSize(9);

    }


}
