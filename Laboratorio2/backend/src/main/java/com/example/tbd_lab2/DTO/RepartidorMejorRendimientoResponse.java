package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RepartidorMejorRendimientoResponse {
    Long idRepartidor;
    String nombreRepartidor;
    Long pedidos;
    Double puntuacionPromedio;
    Double indiceRendimiento;
}
