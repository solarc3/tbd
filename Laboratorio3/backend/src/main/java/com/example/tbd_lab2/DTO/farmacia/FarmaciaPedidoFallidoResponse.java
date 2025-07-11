package com.example.tbd_lab2.DTO.farmacia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmaciaPedidoFallidoResponse {
    private Long idFarmacia;
    private String nombreFarmacia;
    int cantidad;
}
