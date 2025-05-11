package com.example.tbd_lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoResponse {
    private String nombreProducto;
    private Integer cantidad;
    private Boolean recetaValidada;
}
