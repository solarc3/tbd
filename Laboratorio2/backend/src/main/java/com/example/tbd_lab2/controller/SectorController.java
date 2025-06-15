package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.entities.SectorEntity;
import com.example.tbd_lab2.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sectores")
public class SectorController {

    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @PostMapping
    public ResponseEntity<SectorEntity> createSector(@RequestBody SectorEntity sector) {
        // Asumiendo que sector entity puede ser deserializado cn geomtry (si usamos geojson)
        // tmb podemos chantarle un dto
        // try {
        //     SectorEntity createdSector = sectorService.createSectorFromWKT(sectorDto.getNombreSector(), sectorDto.getAreaWkt());
        //     return ResponseEntity.status(HttpStatus.CREATED).body(createdSector);
        // } catch (ParseException e) {
        //     return ResponseEntity.badRequest().build(); // Or more specific error
        // }
        return ResponseEntity.status(HttpStatus.CREATED).body(sectorService.createSector(sector));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorEntity> getSectorById(@PathVariable Long id) {
        return sectorService.getSectorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<SectorEntity>> getAllSectores() {
        return ResponseEntity.ok(sectorService.getAllSectores());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectorEntity> updateSector(@PathVariable Long id, @RequestBody SectorEntity sectorDetails) {
        SectorEntity updatedSector = sectorService.updateSector(id, sectorDetails);
        if (updatedSector != null) {
            return ResponseEntity.ok(updatedSector);
        }
        return ResponseEntity.notFound().build();
    }
}