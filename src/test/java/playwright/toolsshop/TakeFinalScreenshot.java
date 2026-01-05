package playwright.toolsshop;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;

public interface TakeFinalScreenshot {
    @AfterEach
    default void takeScreenshot(Page page){
        System.out.println("Taking screenshot after test");
        ScreenshotManager.takeScreenshot(page,"the screenShot");
    };
}
