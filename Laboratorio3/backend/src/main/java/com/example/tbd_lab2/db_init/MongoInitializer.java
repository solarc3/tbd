package com.example.tbd_lab2.db_init;

import com.example.tbd_lab2.collections.HistorialRepartidoresCollection;
import com.example.tbd_lab2.collections.LogsPedidosCollection;
import com.example.tbd_lab2.collections.NavegacionLog;
import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import com.example.tbd_lab2.repositories.HistorialRepartidoresRepository;
import com.example.tbd_lab2.repositories.LogsPedidosRepository;
import com.example.tbd_lab2.repositories.NavegacionLogRepository;
import com.example.tbd_lab2.repositories.OpinionesClientesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Component
public class MongoInitializer implements CommandLineRunner {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private final HistorialRepartidoresRepository historialRepartidoresRepository;
    private final LogsPedidosRepository logsPedidosRepository;
    private final NavegacionLogRepository navegacionLogRepository;
    private final OpinionesClientesRepository opinionesClientesRepository;

    public MongoInitializer(@Qualifier("webApplicationContext") ResourceLoader resourceLoader,
                            ObjectMapper objectMapper,
                            HistorialRepartidoresRepository historialRepartidoresRepository,
                            LogsPedidosRepository logsPedidosRepository,
                            NavegacionLogRepository navegacionLogRepository,
                            OpinionesClientesRepository opinionesClientesRepository) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.historialRepartidoresRepository = historialRepartidoresRepository;
        this.logsPedidosRepository = logsPedidosRepository;
        this.navegacionLogRepository = navegacionLogRepository; // <-- CAMBIO
        this.opinionesClientesRepository = opinionesClientesRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() throws IOException {
        // Historial Repartidores
        Resource historialResource = resourceLoader.getResource("classpath:json/historial_repartidores.json");
        try (InputStream inputStream = historialResource.getInputStream()) {
            historialRepartidoresRepository.deleteAll();
            HistorialRepartidoresCollection[] historiales = objectMapper.readValue(inputStream, HistorialRepartidoresCollection[].class);
            historialRepartidoresRepository.saveAll(Arrays.asList(historiales));
        }

        // Logs Pedidos
        Resource logsResource = resourceLoader.getResource("classpath:json/logs_pedidos.json");
        try (InputStream inputStream = logsResource.getInputStream()) {
            logsPedidosRepository.deleteAll();
            LogsPedidosCollection[] logs = objectMapper.readValue(inputStream, LogsPedidosCollection[].class);
            logsPedidosRepository.saveAll(Arrays.asList(logs));
        }

        // Navegacion Usuarios
        Resource navegacionResource = resourceLoader.getResource("classpath:json/navegacion_usuarios.json");
        try (InputStream inputStream = navegacionResource.getInputStream()) {
            navegacionLogRepository.deleteAll();
            NavegacionLog[] navegaciones = objectMapper.readValue(inputStream, NavegacionLog[].class);
            navegacionLogRepository.saveAll(Arrays.asList(navegaciones));
        }


        // Opiniones Clientes
        Resource opinionesResource = resourceLoader.getResource("classpath:json/opiniones_clientes.json");
        try (InputStream inputStream = opinionesResource.getInputStream()) {
            opinionesClientesRepository.deleteAll();
            OpinionesClientesCollection[] opiniones = objectMapper.readValue(inputStream, OpinionesClientesCollection[].class);
            opinionesClientesRepository.saveAll(Arrays.asList(opiniones));
        }
    }
}
