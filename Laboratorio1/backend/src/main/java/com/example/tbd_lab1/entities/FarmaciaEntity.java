package com.example.tbd_lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FarmaciaEntity {
    private Long idFarmacia;
    private String nombreFarmacia;
    private String direccion;
}
