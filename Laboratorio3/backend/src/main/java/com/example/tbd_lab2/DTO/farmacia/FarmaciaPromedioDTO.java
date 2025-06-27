package com.example.tbd_lab2.DTO.farmacia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmaciaPromedioDTO {
    private String nombreFarmacia;
    private double promedioPuntuacion;
}
