package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.entities.TareaEntity;
import com.example.tbd_lab1.services.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarea")
@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping("/")
    public ResponseEntity<List<TareaEntity>> getAllTareas() {
        List<TareaEntity> tareas = tareaService.getAllTareas();
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTareaById(@PathVariable Long id) {
        Optional<TareaEntity> tarea = tareaService.getTareaById(id);
        if (tarea.isPresent()) {
            return ResponseEntity.ok(tarea.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Tarea no encontrada con ID: " + id));
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<TareaEntity>> getTareasByUsuario(@PathVariable Long idUsuario) {
        List<TareaEntity> tareas = tareaService.getTareasByUsuario(idUsuario);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TareaEntity>> getTareasByEstado(@PathVariable String estado) {
        List<TareaEntity> tareas = tareaService.getTareasByEstado(estado);
        return ResponseEntity.ok(tareas);
    }

    @PostMapping("/")
    public ResponseEntity<?> createTarea(@RequestBody TareaEntity tareaEntity) {
        try {
            TareaEntity createdTarea = tareaService.createTarea(tareaEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTarea);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al crear la tarea: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTarea(@PathVariable Long id, @RequestBody TareaEntity tareaEntity) {
        if (!tareaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Tarea no encontrada con ID: " + id));
        }

        tareaEntity.setId(id);
        TareaEntity updatedTarea = tareaService.updateTarea(tareaEntity);
        return ResponseEntity.ok(updatedTarea);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTarea(@PathVariable Long id) {
        if (!tareaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Tarea no encontrada con ID: " + id));
        }

        boolean deleted = tareaService.deleteTarea(id);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Tarea eliminada correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al eliminar la tarea"));
        }
    }
}