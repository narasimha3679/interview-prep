package com.sqlval.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection factory. One place that knows the URL, user and password — the
 * same shape as a real framework's DBUtil, minus the pool.
 */
public final class Db {

    /**
     * DB_CLOSE_DELAY=-1 keeps the in-memory database alive while the JVM runs,
     * so every connection sees the same schema and rows.
     * MODE=MSSQLServer makes H2 accept T-SQL style (TOP, BIT, IDENTITY).
     */
    public static final String URL =
            "jdbc:h2:mem:sqlval;DB_CLOSE_DELAY=-1;MODE=MSSQLServer";

    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private Db() {
    }

    public static Connection open() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
