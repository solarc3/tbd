package com.example.tbd_lab2.DTO.repartidor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorDistanciaTotalDTO {
    private Long id;
    private String nombreRepartidor;
    private double distanciaTotalKm;
}
