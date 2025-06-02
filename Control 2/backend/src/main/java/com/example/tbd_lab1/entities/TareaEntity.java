package com.example.tbd_lab1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TareaEntity {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaVencimiento;
    private Long idUsuario;
    private String estado;
    private Long idSector;
    private String nombreSector;
}