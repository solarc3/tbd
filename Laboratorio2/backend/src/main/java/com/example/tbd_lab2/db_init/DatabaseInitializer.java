package com.example.tbd_lab2.db_init;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Component
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initProcedures() {
        try {
            List<String> filenames = List.of(
                    "procedures.sql", "zonas.sql", "datos.sql"
            );

            // Ejecutar scripts SQL en orden.
            // schema.sql lo ejecuta Spring automaticamente.
            for (String filename : filenames) {
                Path path = Paths.get(Objects.requireNonNull(
                        getClass().getClassLoader().getResource(filename)).toURI());
                String sql = new String(Files.readAllBytes(path));
                jdbcTemplate.execute(sql);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error al inicializar scripts SQL: ", e);
        }
    }
}
