package com.example.tbd_lab2.DTO.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCruzaZonasResponse {
    private Long idPedido;
    private Long idCliente;
    private String nombreCliente;
    private LocalDateTime fechaPedido;
    private String[] nombresZonas;
}
