package helpers;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Java interop helper — call from feature files via:
 *   * def DataGenerator = Java.type('helpers.DataGenerator')
 *   * def email = DataGenerator.randomEmail()
 *
 * Interview angle: use Java when the Karate DSL is not enough
 * (crypto, complex IDs, shared utilities already in the org).
 */
public final class DataGenerator {

    private DataGenerator() {
    }

    public static String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    public static String randomName() {
        return "Auto User " + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    public static int randomUserId(int minInclusive, int maxInclusive) {
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }
}
