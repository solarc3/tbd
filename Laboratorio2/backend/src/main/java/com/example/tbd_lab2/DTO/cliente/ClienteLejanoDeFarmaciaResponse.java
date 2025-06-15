package com.example.tbd_lab2.DTO.cliente;

import com.example.tbd_lab2.entities.FarmaciaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteLejanoDeFarmaciaResponse {
    private Long idCliente;
    private String nombreCliente;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point ubicacionCliente;

    private FarmaciaEntity farmacia;
    private Double distanciaKm;
}
