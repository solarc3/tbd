package com.example.tbd_lab2.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "opiniones_clientes")
public class OpinionesClientesCollection {
    @Id
    private String id;
    private String comentarios;
    private int puntuacion;
    private LocalDateTime fecha;
    private Long idCliente;
    private Long idPedido;
}
