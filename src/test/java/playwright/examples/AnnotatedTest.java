package playwright.examples;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;


import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.*;
import playwright.toolsshop.ChromeHeadlessOptions;

import java.util.Arrays;
import java.util.List;

@UsePlaywright(ChromeHeadlessOptions.class)
public class AnnotatedTest {

    public static class MyOptions implements OptionsFactory {

        @Override
        public Options getOptions() {

            return new Options()
                    .setHeadless(false)
                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
                    );

        }
    }

    @BeforeEach
    void beforeEach(Page page) {
        page.navigate("https://www.practicesoftwaretesting.com");
        page.waitForSelector("[data-test=product-name]");
    }

    @Test
    void shouldShowProductNames(Page page) {
//        page.navigate("https://www.practicesoftwaretesting.com");
        page.waitForSelector(".card-title",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE)
        );
        List<String> productNames = page.locator(".card-title").allInnerTexts();
        org.assertj.core.api.Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters");
    }

    @Test
    void shouldShowProductImages(Page page) {
        List<String> allImages = page.locator(".card-img-top").all()
                .stream()
                .map(img -> img.getAttribute("alt"))
                .toList();
        org.assertj.core.api.Assertions.assertThat(allImages).contains("Pliers", "Bolt Cutters");
    }


    @Test
    void shouldShowThePageTitle(Page page) {
        page.navigate("https://www.practicesoftwaretesting.com");
        String title = page.title();

        Assertions.assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword(Page page) {
        page.navigate("https://www.practicesoftwaretesting.com");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResults > 0);

    }

    @Nested
    class AutomaticWaits {
        @BeforeEach
        void beforeEach(Page page) {
            page.navigate("https://www.practicesoftwaretesting.com");
        }

        @Test
        @DisplayName("Should wait for the check boxes to appear before click")
        void shouldWaitForTheCheckBoxes(Page page) {
            var hammerFilter = page.getByLabel("Hammer");
            hammerFilter.click();
            assertThat(hammerFilter).isChecked();

        }


        @Test
        @DisplayName("Should filter products by category")
        void shouldFilterProductsByCategory(Page page) {
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

            page.waitForSelector("[data-test=co2-rating-badge]");

            List<String> filteredProducts = page.getByTestId("product-name").allInnerTexts();
            org.assertj.core.api.Assertions.assertThat(filteredProducts).contains("Sheet Sander", "Belt Sander");

        }

        @Test
        @DisplayName("Should filter products by category")
        void updateShopingCartItems(Page page) {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

//        page.waitForCondition(() -> page.locator("#lblCartCount").textContent().equals("1"));

            page.waitForSelector("#lblCartCount:has-text('1')");

        }

        @Test
        @DisplayName("Sort by descending price")
        void sortByDescendingPrice(Page page) {

//        Sort by descending price
            page.waitForResponse("**/products?page**", () -> {
                page.getByTestId("sort").selectOption("Price: High - Low");
            });

//        Find all prices on the page
            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(AutomaticWaits::extractPrice)
                    .toList();
        }

        private static double extractPrice(String price) {
            return Double.parseDouble(price.replace("$", ""));
        }
    }


}