package com.example.tbd_lab2.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoMasUsadoUrgenteResponse {
    String metodoPago;
    int cantidad;
}
