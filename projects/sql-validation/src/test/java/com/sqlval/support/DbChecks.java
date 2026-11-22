package com.sqlval.support;

import com.sqlval.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;

/**
 * The DB half of the test: read what the API claims it wrote.
 *
 * DRILL — implement findStatus and deleteById. waitForStatus is given: it is the
 * polling loop you would otherwise hand-write in every async test.
 */
public final class DbChecks {

    private static final long POLL_INTERVAL_MILLIS = 100;

    private DbChecks() {
    }

    /**
     * One row by primary key, or empty when the row is not there.
     *
     * TODO: DriverManager.getConnection -> PreparedStatement("SELECT status FROM accounts WHERE id = ?")
     *       -> setLong(1, accountId) -> executeQuery -> rs.next() -> Optional.of(rs.getString("status"))
     *       -> Optional.empty() when there is no row. try-with-resources on Connection, PreparedStatement,
     *       ResultSet. Wrap SQLException in IllegalStateException.
     */
    public static Optional<String> findStatus(long accountId) {
        throw new UnsupportedOperationException("TODO: implement findStatus with JDBC");
    }

    /**
     * Polls until the row appears or the timeout expires. Async writers mean the
     * row is usually absent for the first few reads.
     */
    public static Optional<String> waitForStatus(long accountId, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();

        while (System.nanoTime() < deadline) {
            Optional<String> status = findStatus(accountId);
            if (status.isPresent()) {
                return status;
            }
            sleep(POLL_INTERVAL_MILLIS);
        }
        return findStatus(accountId);
    }

    /**
     * Deletes one row and returns how many rows the database actually changed.
     *
     * TODO: same six steps, "DELETE FROM accounts WHERE id = ?", return ps.executeUpdate().
     *       No WHERE clause means the whole table: never.
     */
    public static int deleteById(long accountId) {
        throw new UnsupportedOperationException("TODO: implement deleteById with JDBC");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while polling the database", e);
        }
    }
}
