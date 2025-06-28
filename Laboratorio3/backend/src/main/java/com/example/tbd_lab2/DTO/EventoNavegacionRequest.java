package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoNavegacionRequest {
    private Long id;
    private Long clientId;
    private String tipo; // "busqueda", "click", "filtro", "compra"
    private String valor;
    private String direccion;
    private String from;
    private String to;
    private Date timestamp;
}
