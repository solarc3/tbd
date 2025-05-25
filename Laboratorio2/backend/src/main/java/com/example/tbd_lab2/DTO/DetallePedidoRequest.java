package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoRequest {
    private Long idRepartidor;
    private String metodoPago;
}
