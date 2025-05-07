package com.example.tbd_lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DetallePedidoEntity {
    private Long idDetallePedido;
    // llave foranea
    private Long idPedido;
    // llave foranea
    private Long idRepartidor;
    private String metodoPago;
    private LocalDateTime fechaEntrega;
}
