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
public class FarmaciaPuntoEntregaLejanaResponse {
    private String nombreFarmacia;
    private String nombreUsuario;
    private double distanciaEntrega;
    private Point ubicacionEntrega;
    private Point ubicacionFarmacia;
}
