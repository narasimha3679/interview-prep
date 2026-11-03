package runners;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Fast CI gate: only scenarios tagged @smoke.
 * Analogous to Cucumber tags = "@smoke" in @CucumberOptions.
 */
class SmokeRunner {

    @Test
    void smoke() {
        Results results = Runner.path("classpath:features")
                .tags("@smoke")
                .outputCucumberJson(true)
                .parallel(2);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
