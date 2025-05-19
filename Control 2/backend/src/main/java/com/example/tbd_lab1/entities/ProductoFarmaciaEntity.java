package com.example.tbd_lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoFarmaciaEntity {
    private Long idProductoFarmacia;
    private Long idProducto;
    private Long idFarmacia;
    private Long stockProducto;
}
