package com.example.tbd_lab2.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
