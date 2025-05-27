package com.example.tbd_lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaCercanaResponse {
    private Long idTarea;
    private String tituloTarea;
    private String descripcionTarea;
    private Timestamp fechaVencimiento;
    private String estadoTarea;
    private Long idSector;
    private String nombreSector;
    private Double distanciaAlSectorMetros;
}