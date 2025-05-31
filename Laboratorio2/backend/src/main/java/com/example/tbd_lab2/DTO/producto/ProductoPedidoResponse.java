package com.example.tbd_lab2.DTO.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoResponse {
    private String nombreProducto;
    private Integer cantidad;
    private Boolean recetaValidada;
}
