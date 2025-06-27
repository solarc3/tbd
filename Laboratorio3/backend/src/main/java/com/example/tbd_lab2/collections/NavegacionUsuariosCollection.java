package com.example.tbd_lab2.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventoNavegacion {
        private String tipo; // "busqueda", "click", "filtro", "compra"
        private String valor;
        private Date timestamp;
    }

    public void addLog(EventoNavegacion evento) {
        eventos.add(evento);
    }
}
