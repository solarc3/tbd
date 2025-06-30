package com.example.tbd_lab2.DTO.pedido;

import com.example.tbd_lab2.collections.LogsPedidosCollection;
import com.example.tbd_lab2.entities.PedidoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogsCambioPedidosResponse {
    private List<LogsPedidosCollection> logsPedidos; // Match your variable name
    private List<PedidoEntity> pedidos;
}