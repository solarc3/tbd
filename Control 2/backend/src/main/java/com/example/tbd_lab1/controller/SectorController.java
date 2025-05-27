package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.SectorTareasResponse;
import com.example.tbd_lab1.entities.SectorEntity;
import com.example.tbd_lab1.entities.UserEntity;
import com.example.tbd_lab1.security.services.UserDetailsImpl;
import com.example.tbd_lab1.services.SectorService;
import com.example.tbd_lab1.services.TareaService;
import com.example.tbd_lab1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sector")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
public class SectorController {
    private final SectorService sectorService;
    private final TareaService tareaService;
    private final UserService userService;

    @Autowired
    public SectorController(SectorService sectorService, TareaService tareaService, UserService userService) {
        this.sectorService = sectorService;
        this.tareaService = tareaService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllSectors() {
        List<SectorEntity> sectors = sectorService.getAllSectors();
        return ResponseEntity.status(HttpStatus.OK).body(sectors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSectorById(@PathVariable Long id) {
        Optional<SectorEntity> sector = sectorService.getSectorById(id);
        if (sector.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(sector.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Sector no encontrado con ID: " + id));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSector(@RequestBody SectorEntity sectorEntity) {
        try {
            SectorEntity createdSector = sectorService.createSector(sectorEntity);
            return ResponseEntity.status(HttpStatus.OK).body(createdSector);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al crear sector: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSector(@PathVariable Long id) {
        if (!sectorService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Sector no encontrado con ID: " + id));
        }

        if (!tareaService.getTareasByIdSector(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Hay tareas asociadas al sector con ID: " + id));
        }

        boolean deleted = sectorService.deleteSector(id);
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Sector eliminado correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al eliminar sector"));
        }
    }

    @GetMapping("/tareas-pendientes")
    public ResponseEntity<?> tareasPendientesBySector() {
        List<SectorTareasResponse> sectores = sectorService.tareasBySector();
        if (sectores.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen sectores o tareas pendientes"));
        }
        return ResponseEntity.ok(sectores);
    }

    @GetMapping("/most-completed-near-me/{radiusKm}")
    public ResponseEntity<?> mostCompletedNearMe(@PathVariable BigDecimal radiusKm) {
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

        List<SectorTareasResponse> sectores = sectorService.getByCompletedTasksWithin(
                userDetails.getLocation(), radiusKm);
        return ResponseEntity.ok(sectores);
    }

    @GetMapping("/most-completed-near/{userId}/{radiusKm}")
    public ResponseEntity<?> mostCompletedNearUser(@PathVariable Long userId, @PathVariable BigDecimal radiusKm) {
        Optional<UserEntity> user = userService.getById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No existe el usuario");
        }

        List<SectorTareasResponse> sectores = sectorService.getByCompletedTasksWithin(
                user.get().getLocation(), radiusKm);
        return ResponseEntity.ok(sectores);
    }
}
