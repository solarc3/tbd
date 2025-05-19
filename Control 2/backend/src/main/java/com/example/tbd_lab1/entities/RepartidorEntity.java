package com.example.tbd_lab1.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorEntity {
    private Long idRepartidor;
    private String nombreRepartidor;
    private Date fechaContratacion;
}
