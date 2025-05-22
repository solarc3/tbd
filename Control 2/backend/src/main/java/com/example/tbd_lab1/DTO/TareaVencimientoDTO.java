package com.example.tbd_lab1.DTO;

import com.example.tbd_lab1.entities.TareaEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TareaVencimientoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaVencimiento;
    private Long idUsuario;
    private String estado;
    private Long idSector;
    private long horasRestantes;
    private long minutosRestantes;
    private long segundosRestantes;

    public TareaVencimientoDTO(TareaEntity tarea, long horasRestantes, long minutosRestantes, long segundosRestantes) {
        this.id = tarea.getId();
        this.titulo = tarea.getTitulo();
        this.descripcion = tarea.getDescripcion();
        this.fechaVencimiento = tarea.getFechaVencimiento();
        this.idUsuario = tarea.getIdUsuario();
        this.estado = tarea.getEstado();
        this.idSector = tarea.getIdSector();
        this.horasRestantes = horasRestantes;
        this.minutosRestantes = minutosRestantes;
        this.segundosRestantes = segundosRestantes;
    }
}