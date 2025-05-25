package com.example.tbd_lab2.DTO;

import com.example.tbd_lab2.entities.PedidoEntity;
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
    // Basta con estos
    private Long idCliente;
    private Boolean esUrgente;
    private Long idFarmacia;
    private List<ProductoPedidoRequest> productos;

    // Opcionales                                   // Valor default
    private LocalDateTime fechaPedido;              // timestamp actual
    private PedidoEntity.EstadoPedido estadoPedido; // POR_CONFIRMAR
    private Integer monto;                          // total computado en el service
}
