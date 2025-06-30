package com.example.tbd_lab2.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "historial_repartidores")
public class HistorialRepartidoresCollection {
    @Id
    private String id;
    private Long idRepartidor; // Corresponde al ID del repartidor en PostgreSQL
    private List<Ruta> rutas;

    @Data
    public static class Ruta {
        private double lat;
        private double lng;
        private Date timestamp;
    }
}