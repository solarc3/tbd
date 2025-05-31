package com.example.tbd_lab2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorEntity {
    private Long idRepartidor;
    private String nombreRepartidor;
    private Date fechaContratacion;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point ubicacion;
}
