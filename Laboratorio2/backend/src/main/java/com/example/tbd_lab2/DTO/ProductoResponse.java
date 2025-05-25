package com.example.tbd_lab2.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Long idProducto;
    private String nombreProducto;
    private float precio;
    private String categoria;
    private boolean requiereReceta;
    private String imageUrl;
}
