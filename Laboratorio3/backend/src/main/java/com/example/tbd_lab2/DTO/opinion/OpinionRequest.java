package com.example.tbd_lab2.DTO.opinion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OpinionRequest {
    private Long idCliente;
    private Long idPedido;
    private int puntuacion;
    private String comentario;
}
