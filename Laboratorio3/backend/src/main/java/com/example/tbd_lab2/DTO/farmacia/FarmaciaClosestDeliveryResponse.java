package com.example.tbd_lab2.DTO.farmacia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FarmaciaClosestDeliveryResponse {
    private String nombreUsuario;
    private String nombreFarmacia;
    private Point ubicacionFarmacia;
    private Point ubicacionUsuario;
    private double distanciaEntrega;
}
