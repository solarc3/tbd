package com.example.tbd_lab2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {

    public enum EstadoPedido {
        POR_CONFIRMAR,
        CONFIRMADO,
        ENTREGADO,
        CANCELADO
    }

    private Long idPedido;
    private Integer monto;
    private LocalDateTime fechaPedido;
    private Boolean esUrgente;
    private EstadoPedido estadoPedido;
    private Long idCliente;
    private Long idFarmacia;
}
