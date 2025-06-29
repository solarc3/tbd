package com.example.tbd_lab2.DTO;

import com.example.tbd_lab2.collections.NavegacionLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NavegacionSinCompraResponse {
    private Long idCliente;
    private String nombreCliente;
    private List<NavegacionLog> historial;
}
