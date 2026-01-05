package playwright.toolsshop.shopingcart;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.junit.UsePlaywright;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import playwright.examples.AnnotatedTest;
import playwright.toolsshop.ChromeHeadlessOptions;
import playwright.toolsshop.PlaywrightRunConfiguration;
import playwright.toolsshop.ScreenshotManager;
import playwright.toolsshop.TakeFinalScreenshot;
import playwright.toolsshop.catalog.pageobject.NavBar;
import playwright.toolsshop.catalog.pageobject.ProductDetails;
import playwright.toolsshop.catalog.pageobject.ProductList;
import playwright.toolsshop.catalog.pageobject.SearchComponent;
import playwright.toolsshop.domain.CartLineItems;
import playwright.toolsshop.shopingcart.pageobjects.CheckOutCart;

import java.nio.file.Paths;
import java.util.List;

@Feature( "Add to cart")
@UsePlaywright(ChromeHeadlessOptions.class)
public class AddToCartTest implements TakeFinalScreenshot {
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

    @BeforeEach
    void setupTrace(BrowserContext browserContext) {
        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

    }


    @AfterEach
    void recordTrace(TestInfo testInfo, BrowserContext browserContext) {
        String traceName = testInfo.getDisplayName().replace(" ", "_").toLowerCase();
        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("/target/traces/trace-" + traceName + ".zip"))
        );
    }

    @Test
    @DisplayName("Add to cart with page objects")
    @Story( "Add to cart")
    void addToCartWithPageObjects() {

        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Slip Joint Pliers");

        productDetails.increaseQuantityBy(1);
        productDetails.addToCart();

        navBar.openCart();
        List<CartLineItems> cartLineItems = checkoutCart.getLineItems();

        Assertions.assertThat(cartLineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Slip Joint Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(1);
                    Assertions.assertThat(item.totalPrice()).isEqualTo(item.quantity() * item.price());
                });
    }

    @Test
    @DisplayName("Checking out multiple items")
    @Story( "Checkout items")
    void checkingOutMultipleItems() {
        navBar.openHomePage();
        productList.viewProductDetails("Bolt cutters");
        productDetails.increaseQuantityBy(1);
        productDetails.addToCart();

        navBar.openHomePage();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItems> cartLineItems = checkoutCart.getLineItems();
        Assertions.assertThat(cartLineItems).hasSize(2);

        List<String> productNames = cartLineItems.stream().map(CartLineItems::title).toList();
        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        Assertions.assertThat(cartLineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(1);
                    Assertions.assertThat(item.totalPrice()).isGreaterThan(0.0);
                    Assertions.assertThat(item.totalPrice()).isEqualTo(item.quantity() * item.price());
                });
    }
}
