package com.example.tbd_lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaCountBySectorResponse {
    private Long idUsuario;
    private Long idSector;
    private String nombreSector;
    private Long cantidadTareas;
}