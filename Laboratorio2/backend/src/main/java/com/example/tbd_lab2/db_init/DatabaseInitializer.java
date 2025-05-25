package com.example.tbd_lab2.db_init;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initProcedures() {
        try {
            // cargar 'resources/procedures.sql'
            Path procedurePath = Paths.get(getClass().getClassLoader().getResource("procedures.sql").toURI());
            String sql = new String(Files.readAllBytes(procedurePath));
            jdbcTemplate.execute(sql);

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error al inicializar procedimientos almacenados: ", e);
        }
    }
}
