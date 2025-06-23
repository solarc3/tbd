package com.example.tbd_lab2.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "navegacion_usuarios")
public class NavegacionUsuariosCollection {
    @Id
    private String id;
    private Long idCliente; // Corresponde al ID del cliente en PostgreSQL
    private List<EventoNavegacion> eventos;

    @Data
    public static class EventoNavegacion {
        private String tipo; // "busqueda", "click", "filtro"
        private String valor;
        private Date timestamp;
    }
}
