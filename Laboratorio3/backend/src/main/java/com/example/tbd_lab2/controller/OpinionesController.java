package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.MessageResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPromedioDTO;
import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import com.example.tbd_lab2.services.OpinionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/opiniones")
public class OpinionesController {
    private final OpinionService opinionService;

    public OpinionesController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll() {
        List<OpinionesClientesCollection> opiniones = opinionService.findAllOpiniones();
        if(opiniones.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen opiniones en el sistema"));
        }
        return ResponseEntity.ok().body(opiniones);
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> findByIdCliente(@PathVariable Long id) {
        List<OpinionesClientesCollection> opiniones = opinionService.getOpinionesByIdCliente(id);
        if (opiniones.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen opiniones para este usuario"));
        }
        return ResponseEntity.ok().body(opiniones);
    }

    // @GetMapping("/Opi")

    @GetMapping("/farmacia/promedio")
    public ResponseEntity<?> getPromedioPuntuacionPorFarmacia() {
        List<FarmaciaPromedioDTO> promedios = opinionService.getPromedioPuntuacionPorFarmacia();
        if (promedios.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No hay suficientes datos para calcular promedios por farmacia."));
        }
        return ResponseEntity.ok().body(promedios);
    }

}
