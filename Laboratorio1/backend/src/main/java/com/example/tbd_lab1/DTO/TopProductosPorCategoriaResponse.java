package com.example.tbd_lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductosPorCategoriaResponse {
    private String categoria;
    private String nombreProducto;
    private Long cantidadPedidos;
}