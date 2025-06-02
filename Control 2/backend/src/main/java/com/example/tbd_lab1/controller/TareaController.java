package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.TareaCercanaResponse;
import com.example.tbd_lab1.DTO.TareaCountBySectorResponse;
import com.example.tbd_lab1.DTO.TareaVencimientoResponse;
import com.example.tbd_lab1.entities.TareaEntity;
import com.example.tbd_lab1.security.services.UserDetailsImpl;
import com.example.tbd_lab1.services.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarea")
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
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new MessageResponse("No authenticated")
                                                                      );
        }

        // recuperar ubicacion del usuario logeado
        UserDetailsImpl userDetails =
            (UserDetailsImpl) authentication.getPrincipal();

        if (!tareaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Tarea no encontrada con ID: " + id));
        }

        //tareaEntity.setId(id);
        tareaEntity.setIdUsuario(userDetails.getId());
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

    // Respuesta notificaciones
    @GetMapping("/due/usuario/{idUsuario}")
    public ResponseEntity<List<TareaVencimientoResponse>> getTareasDueTodayByUsuario(@PathVariable Long idUsuario) {
        List<TareaVencimientoResponse> tareas = tareaService.getTareasPorVencerHoy(idUsuario);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/usuario/{idUsuario}/count-by-sector")
    public ResponseEntity<List<TareaCountBySectorResponse>> getTareaCountByUsuarioAndSector(@PathVariable Long idUsuario) {
        List<TareaCountBySectorResponse> counts = tareaService.getTareaCountByUsuarioAndSector(idUsuario);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/count-by-sector")
    public ResponseEntity<List<TareaCountBySectorResponse>> getTareaCountForEachUsuarioBySector() {
        List<TareaCountBySectorResponse> counts = tareaService.getTareaCountForEachUsuarioBySector();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/usuario/{idUsuario}/mas-cercana")
    public ResponseEntity<List<TareaCercanaResponse>> getTareaPendienteMasCercana(@PathVariable Long idUsuario) {
        List<TareaCercanaResponse> tareaOpt = tareaService.getTareaPendienteMasCercana(idUsuario);
        return ResponseEntity.ok(tareaOpt);
    }
}