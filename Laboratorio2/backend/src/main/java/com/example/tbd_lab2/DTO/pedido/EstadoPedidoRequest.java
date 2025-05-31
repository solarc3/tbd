package com.example.tbd_lab2.DTO.pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedidoRequest {
    private Long idPedido;
    private String nuevoEstado;
}
