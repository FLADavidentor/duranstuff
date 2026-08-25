package com.example.duranstuff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DuranstuffApplication {

    public static void main(String[] args) {
        ensureDatabase("167.86.74.77", "5433", "duranstuff", "dev", "dev");
        SpringApplication.run(DuranstuffApplication.class, args);
    }

    private static void ensureDatabase(String host, String port, String db, String user, String pass) {
        String adminUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
        try (var c = java.sql.DriverManager.getConnection(adminUrl, user, pass);
             var st = c.createStatement()) {
            var rs = st.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + db + "'");
            if (!rs.next()) {
                st.executeUpdate("CREATE DATABASE \"" + db + "\"");
                System.out.println("[devdb] created database " + db);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not ensure database " + db, e);
        }
    }
}