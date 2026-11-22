package com.sqlval.support;

import com.sqlval.db.Db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Rebuilds the drill schema before every test. In a real suite you seed once
 * per class and delete only the rows the test created; this reset is safe
 * because the database is a private in-memory instance.
 */
public final class Schema {

    private Schema() {
    }

    public static void reset() {
        try (Connection con = Db.open();
             Statement st = con.createStatement()) {

            st.execute("DROP TABLE IF EXISTS orders");
            st.execute("DROP TABLE IF EXISTS accounts");

            st.execute("""
                    CREATE TABLE accounts
                    (
                        id         BIGINT       NOT NULL,
                        owner      VARCHAR(100) NOT NULL,
                        status     VARCHAR(20)  NOT NULL,
                        created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT pk_accounts PRIMARY KEY (id)
                    )
                    """);

            st.execute("""
                    CREATE TABLE orders
                    (
                        id          BIGINT        NOT NULL,
                        employee_id BIGINT        NULL,
                        amount      DECIMAL(12,2) NOT NULL,
                        status      VARCHAR(20)   NOT NULL,
                        CONSTRAINT pk_orders PRIMARY KEY (id)
                    )
                    """);

            st.execute("INSERT INTO accounts (id, owner, status) VALUES (1, 'seed-user', 'ACTIVE')");

            st.execute("""
                    INSERT INTO orders (id, employee_id, amount, status) VALUES
                        (101, 7,    250.00, 'COMPLETED'),
                        (102, 7,    120.50, 'COMPLETED'),
                        (103, 7,    900.00, 'CANCELLED'),
                        (104, 8,    400.00, 'COMPLETED'),
                        (105, 8,     50.00, 'PENDING'),
                        (106, NULL,  75.00, 'COMPLETED')
                    """);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not reset the drill schema", e);
        }
    }
}
