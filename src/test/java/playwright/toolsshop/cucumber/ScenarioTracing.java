package playwright.toolsshop.cucumber;

import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import playwright.toolsshop.PlaywrightRunConfiguration;
import playwright.toolsshop.cucumber.stepDefinitions.PlaywrightCucumberRunConfiguration;

import java.nio.file.Paths;

public class ScenarioTracing {
    @Before
   public void setupTracing() {
       PlaywrightCucumberRunConfiguration.getBrowserContext().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @After
    public void recordTracing(Scenario scenario) {
        String traceName = scenario.getName().replace(" ", "_").toLowerCase();
        PlaywrightCucumberRunConfiguration.getBrowserContext().tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("/target/traces/trace-" + traceName + ".zip"))
        );
    }
}
