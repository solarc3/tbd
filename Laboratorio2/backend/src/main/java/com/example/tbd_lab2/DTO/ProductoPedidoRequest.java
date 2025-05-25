package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoRequest {
    private Long idProducto;
    private Integer cantidad;
    private Boolean recetaValidada;
}
