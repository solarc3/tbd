package com.example.tbd_lab2.entities;

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

public class FarmaciaEntity {
    private Long idFarmacia;
    private String nombreFarmacia;
    private String direccion;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point ubicacion;
}
