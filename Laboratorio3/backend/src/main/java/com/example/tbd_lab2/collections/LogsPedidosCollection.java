package com.example.tbd_lab2.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "logs_pedidos")
public class LogsPedidosCollection {
    @Id
    private String id;
    private Long idPedido; // Corresponde al ID del pedido en PostgreSQL
    private List<Evento> eventos;

    @Data
    public static class Evento {
        private String estado;
        private LocalDateTime timestamp;
    }
}
