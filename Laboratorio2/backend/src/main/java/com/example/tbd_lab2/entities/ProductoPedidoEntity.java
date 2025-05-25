package com.example.tbd_lab2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoEntity {
    Long idProductoPedido;
    Long idProducto;
    Long idPedido;
    Integer cantidad;
    Boolean recetaValidada;
}
