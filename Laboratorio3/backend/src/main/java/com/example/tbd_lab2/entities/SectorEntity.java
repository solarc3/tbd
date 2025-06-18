package com.example.tbd_lab2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {
    private Long id;
    private String nombreSector;
    private String area;
}