package com.example.tbd_lab2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEntity {
    private Long idProducto;
    private String nombreProducto;
    private Integer precio;
    private String categoria;
    private boolean requiereReceta;
    private String imageUrl;
}
