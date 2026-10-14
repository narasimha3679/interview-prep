package com.framework.hooks;

import com.framework.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scenario lifecycle: clear shared state before each scenario;
 * on failure, attach response details to the Cucumber report.
 */
public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        TestContext.reset();
        LOG.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            Response response = TestContext.get().getResponse();
            if (response != null) {
                String attachment = "Status: " + response.getStatusCode()
                        + System.lineSeparator()
                        + "Body:" + System.lineSeparator()
                        + response.asPrettyString();
                scenario.attach(attachment.getBytes(), "text/plain", "last-api-response");
            }
        }
        LOG.info("Finished scenario: {} | status={}", scenario.getName(), scenario.getStatus());
        TestContext.reset();
    }
}
