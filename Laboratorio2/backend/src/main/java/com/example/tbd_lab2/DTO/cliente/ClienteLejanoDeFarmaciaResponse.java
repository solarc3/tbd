package com.example.tbd_lab2.DTO.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteLejanoDeFarmaciaResponse {
    private Long idCliente;
    private String nombreCliente;
    private String nombreFarmacia;
    private Double distanciaKm;
}
