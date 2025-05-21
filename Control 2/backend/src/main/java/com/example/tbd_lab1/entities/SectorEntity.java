package com.example.tbd_lab1.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SectorEntity {
    private Long id;
    private String nombreZona;
    private String area;
}
