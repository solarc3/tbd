package com.example.tbd_lab2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FarmaciaRepartidorEntity {
    private Long idFarmaciaRepartidor;
    private Long idFarmacia;
    private Long idRepartidor;
}
