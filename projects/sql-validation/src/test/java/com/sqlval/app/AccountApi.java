package com.sqlval.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqlval.db.Db;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A stand-in for the real service under test: a tiny HTTP API backed by H2.
 *
 * It deliberately behaves like an async service: POST returns 201 immediately
 * and the INSERT lands a few hundred milliseconds later. That is what makes
 * "assert the status code, then assert the row" a real skill instead of a
 * formality.
 */
public final class AccountApi implements AutoCloseable {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final HttpServer server;
    private final ScheduledExecutorService writer = Executors.newSingleThreadScheduledExecutor();
    private final long writeDelayMillis;

    public AccountApi(long writeDelayMillis) throws IOException {
        this.writeDelayMillis = writeDelayMillis;
        this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        this.server.createContext("/accounts", this::handle);
        this.server.setExecutor(Executors.newFixedThreadPool(2));
        this.server.start();
    }

    public String baseUri() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    private void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("POST".equals(method) && "/accounts".equals(path)) {
                create(exchange);
            } else {
                send(exchange, 404, "{\"error\":\"not found\"}");
            }
        } catch (Exception e) {
            send(exchange, 500, "{\"error\":\"server error\"}");
        } finally {
            exchange.close();
        }
    }

    private void create(HttpExchange exchange) throws IOException {
        JsonNode body = JSON.readTree(exchange.getRequestBody());
        long id = body.path("id").asLong();
        String owner = body.path("owner").asText();

        if (id <= 0 || owner.isBlank()) {
            send(exchange, 400, "{\"error\":\"id and owner are required\"}");
            return;
        }

        String payload = "{\"id\":" + id + ",\"owner\":\"" + owner + "\",\"status\":\"PENDING\"}";
        send(exchange, 201, payload);

        // The write happens after the response — eventual consistency on purpose.
        writer.schedule(() -> insert(id, owner), writeDelayMillis, TimeUnit.MILLISECONDS);
    }

    private void insert(long id, String owner) {
        String sql = "INSERT INTO accounts (id, owner, status) VALUES (?, ?, ?)";
        try (Connection con = Db.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setString(2, owner);
            ps.setString(3, "PENDING");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Insert failed for account " + id, e);
        }
    }

    private static void send(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    @Override
    public void close() {
        writer.shutdownNow();
        server.stop(0);
    }
}
