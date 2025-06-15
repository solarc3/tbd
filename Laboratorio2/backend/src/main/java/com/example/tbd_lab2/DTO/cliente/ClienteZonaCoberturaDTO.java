package com.example.tbd_lab2.DTO.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteZonaCoberturaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreSector;
    private Double latitud;
    private Double longitud;
}
