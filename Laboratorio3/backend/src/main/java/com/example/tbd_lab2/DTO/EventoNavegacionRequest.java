package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoNavegacionRequest {
    private Long idCliente;
    private String tipo; // "busqueda", "click", "filtro", "compra"
    private String valor;
    private Date timestamp;
}
