package runners;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Full suite: everything under features/ except @ignore.
 * Parallel threads are a Sr-level expectation for API suites.
 */
class RegressionRunner {

    @Test
    void regression() {
        Results results = Runner.path("classpath:features")
                .tags("~@ignore")
                .outputCucumberJson(true)
                .parallel(4);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
