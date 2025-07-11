package com.example.tbd_lab2.DTO.farmacia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingFarmaciaPedidoResponse {
    String nombreFarmacia;
    int cantPedidosEntregados;
}
