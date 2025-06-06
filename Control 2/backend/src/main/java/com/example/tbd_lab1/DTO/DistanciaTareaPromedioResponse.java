package com.example.tbd_lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistanciaTareaPromedioResponse {
    private Long idUsuario;
    private String nombreUsuario;
    private Double distanciaPromedio;
}
