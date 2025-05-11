package com.example.tbd_lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionEntity {
    private Long idCalificacion;
    private Long idDetallePedido;
    private Long clienteId;
    private float puntuacion;
}
