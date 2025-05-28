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
            // cargar 'resources/zonas.sql'
            Path zonePath = Paths.get(getClass().getClassLoader().getResource("zonas.sql").toURI());
            String sql_zona = new String(Files.readAllBytes(zonePath));
            jdbcTemplate.execute(sql_zona);
            // cargar 'resources/datos.sql'
            Path dataPath = Paths.get(getClass().getClassLoader().getResource("datos.sql").toURI());
            String sql_data = new String(Files.readAllBytes(dataPath));
            jdbcTemplate.execute(sql_data);


        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error al inicializar procedimientos almacenados: ", e);
        }
    }
}
