package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoResponse {
    private String nombreRepartidor;
    private String metodoPago;
    private LocalDateTime fechaEntrega;
}
