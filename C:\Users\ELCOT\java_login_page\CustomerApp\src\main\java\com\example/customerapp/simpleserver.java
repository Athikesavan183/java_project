package com.example.customerapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

/**
 * Single-file version: serves the HTML form and saves it to the database.
 * No separate DAO/Model classes -- everything is here so it's easy to read.
 */
public class SimpleServer {

    // ---- EDIT THESE WITH YOUR OWN DATABASE DETAILS ----
    static final String DB_URL = "jdbc:postgresql://localhost:5432/java";
    static final String DB_USER = "postgres";
    static final String DB_PASSWORD = "Athi@183";
    // ----------------------------------------------------

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Show the form
        server.createContext("/", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            byte[] html = readResource("static/index.html");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(html);
            }
        });

        // Handle form submit -> save to database
        server.createContext("/submit", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> f = parseForm(body);

            String resultHtml;
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 CallableStatement stmt = conn.prepareCall("CALL proc_insertcustomer(?, ?, ?, ?, ?)")) {

                stmt.setString(1, f.get("fullName"));
                stmt.setString(2, f.get("phone"));
                stmt.setString(3, f.get("email"));
                stmt.setString(4, f.get("address"));
                stmt.setString(5, f.get("city"));
               
                stmt.execute();

                resultHtml = "<h2 style='font-family:Arial;color:green;text-align:center'>"
                        + "Customer saved successfully!</h2>"
                        + "<p style='text-align:center'><a href='/'>Add another customer</a></p>";

            } catch (SQLException e) {
                e.printStackTrace();
                resultHtml = "<h2 style='font-family:Arial;color:red;text-align:center'>"
                        + "Error: " + e.getMessage() + "</h2>"
                        + "<p style='text-align:center'><a href='/'>Go back</a></p>";
            }

            byte[] resp = resultHtml.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, resp.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resp);
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Open this in your browser -> http://localhost:8080/");
    }

    // Reads "key=value&key2=value2" style POST body into a Map
    private static Map<String, String> parseForm(String body) {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.isEmpty()) return map;
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        return map;
    }

    private static byte[] readResource(String path) throws IOException {
        try (InputStream in = SimpleServer.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IOException("Could not find " + path + " on classpath");
            return in.readAllBytes();
        }
    }
}
