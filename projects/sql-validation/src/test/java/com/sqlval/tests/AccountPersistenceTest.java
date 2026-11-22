package com.sqlval.tests;

import com.sqlval.app.AccountApi;
import com.sqlval.support.DbChecks;
import com.sqlval.support.Schema;
import io.restassured.http.ContentType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.fail;

public class AccountPersistenceTest {

    private static final long SEED_ACCOUNT_ID = 1L;
    private static final long NEW_ACCOUNT_ID = 7001L;
    private static final long CLEANUP_ACCOUNT_ID = 7002L;
    private static final Duration DB_TIMEOUT = Duration.ofSeconds(3);

    private AccountApi api;

    @BeforeMethod
    public void setUp() throws IOException {
        Schema.reset();
        api = new AccountApi(300);   // 201 comes back first, INSERT lands 300 ms later
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (api != null) {
            api.close();
        }
    }

    /**
     * DRILL 1 — the second proof.
     * POST an account (createAccount below), then assert the row exists in the DB
     * with status PENDING. Use DbChecks.waitForStatus, not Thread.sleep.
     */
    @Test
    public void createdAccountIsPersisted() {
        fail("TODO: create account " + NEW_ACCOUNT_ID + ", then assert the DB row with DbChecks.waitForStatus");
    }

    /**
     * DRILL 2 — cleanup that does not eat the environment.
     * Create CLEANUP_ACCOUNT_ID, wait for the row, delete it by id, then assert:
     * exactly one row was deleted, the row is gone, and the seed account survived.
     */
    @Test
    public void cleanupRemovesOnlyTheRowYouCreated() {
        fail("TODO: create, wait, DbChecks.deleteById, assert 1 deleted + row gone + seed row " + SEED_ACCOUNT_ID + " still ACTIVE");
    }

    private void createAccount(long id, String owner) {
        given()
                .baseUri(api.baseUri())
                .contentType(ContentType.JSON)
                .body(Map.of("id", id, "owner", owner))
        .when()
                .post("/accounts")
        .then()
                .statusCode(201);
    }
}
