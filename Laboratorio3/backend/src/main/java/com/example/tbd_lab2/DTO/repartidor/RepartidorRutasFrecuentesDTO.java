package com.example.tbd_lab2.DTO.repartidor;

import lombok.Data;
import java.util.List;

@Data
public class RepartidorRutasFrecuentesDTO {
    private Long idRepartidor;
    private String nombreRepartidor;
    private List<RutaFrecuenteDTO> rutasFrecuentes;
}
