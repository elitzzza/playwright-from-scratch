package playwright.toolsshop.cucumber.stepDefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;

import java.util.Arrays;

public class PlaywrightCucumberRunConfiguration {

    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    private static final ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    @Before(order = 100)
    public void setUpBrowserContext() {
        System.out.println("Setting up browser context");
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After(order = 100)
    public void closeContext() {
        System.out.println("Closing browser context");
        browserContext.get().close();
    }


    @AfterAll
    public static void tearDown() {
        System.out.println("Tearing down Playwright");
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    public static Page getPage() {
        return page.get();
    }


    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }


}
