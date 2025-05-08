package com.example.tbd_lab1.DTO;

import com.example.tbd_lab1.entities.PedidoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPedidoCompletoRequest {
    // Pedido fields
    private Integer monto;
    private LocalDateTime fechaPedido;
    private Boolean esUrgente;
    private PedidoEntity.EstadoPedido estadoPedido;
    private Long idCliente;
    private Long idFarmacia;

    // DetallePedido fields
    private Long idRepartidor; // Can be null
    private String metodoPago;
    private LocalDateTime fechaEntrega; // Can be null

    // ProductoPedido fields
    private List<Long> idProductos;
}
