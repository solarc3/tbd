package com.example.tbd_lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaCountBySectorDTO {
    private Long idSector;
    private String nombreSector;
    private Long cantidadTareas;
}