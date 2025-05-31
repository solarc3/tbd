package com.example.tbd_lab2.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaPedido;
    private Boolean esUrgente;
    private EstadoPedido estadoPedido;
    private Long idCliente;
    private Long idFarmacia;
    private Geometry rutaEstimada;
}
