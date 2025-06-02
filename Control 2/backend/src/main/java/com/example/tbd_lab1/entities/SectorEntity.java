package com.example.tbd_lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SectorEntity {
    private Long id;
    private String nombreSector;
    private String area; // Representación GeoJSON
}
