package playwright.examples;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;


public class SimpleTest {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox")));
        browserContext = browser.newContext();
    }

    @BeforeEach
    public void beforeEach() {
        page = browserContext.newPage();
        page.navigate("https://www.practicesoftwaretesting.com");
    }

    @AfterAll
    public static void teardown() {
        browser.close();
        playwright.close();
    }


    public static class MyOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setLaunchOptions(new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList("--no-sandbox"))
                    );
        }
    }

    @Test
    void shouldShowThePageTitle() {
        page.navigate("https://www.practicesoftwaretesting.com");
        String title = page.title();

        Assertions.assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword() {
        page.navigate("https://www.practicesoftwaretesting.com");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResults > 0);
    }

    @Nested
    class MakingApiCalls {
        record Product(String name, double price) {
        }

        private static APIRequestContext requestContext;

        @BeforeAll
        public static void setupRequestContext() {
            requestContext = playwright.request().newContext(
                    new APIRequest.NewContextOptions()
                            .setBaseURL("https://api.practicesoftwaretesting.com")
                            .setExtraHTTPHeaders(new HashMap<>() {{
                                put("Accept", "application/json");
                            }})
            );
        }

        @DisplayName("Check presence of known products")
        @ParameterizedTest(name="Checking product {0}")
        @MethodSource("products")
        void checkKnownProducts(Product product){
            page.fill("[placeholder=Search]", product.name);
//            OR
//            page.getByPlaceholder("Search").fill(product.name);
//
//            Check that the product appears with the correct name and price

            Locator productCard = page.locator(".card").filter(
                    new Locator.FilterOptions()
                            .setHasText(product.name)
                            .setHasText(Double.toString(product.price))
            );
            org.assertj.core.api.Assertions.assertThat(productCard.isVisible());
        }


        static Stream<Product> products() {

            APIResponse response = requestContext.get("/products?page=2");
            org.assertj.core.api.Assertions.assertThat(response.status()).isEqualTo(200);

            JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);
            JsonArray data = jsonObject.getAsJsonArray("data");

            return data.asList().stream()
                    .map(jsonElement -> {
                        JsonObject productJson = jsonElement.getAsJsonObject();
                        return new Product(
                                productJson.get("name").getAsString(),
                                productJson.get("price").getAsDouble());
                    });

        }
    }
}
