package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.RepartidorInfoResponse;
import com.example.tbd_lab2.DTO.RepartidorMejorRendimientoResponse;
import com.example.tbd_lab2.DTO.RepartidorTiempoPromedioResponse;
import com.example.tbd_lab2.entities.RepartidorEntity;
import com.example.tbd_lab2.services.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repartidor")
public class RepartidorController {
    private final RepartidorService repartidorService;

    @Autowired
    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllRepartidores() {
        List<RepartidorEntity> repartidores = repartidorService.findAll();
        return ResponseEntity.ok(repartidores);
    }

    @GetMapping("/tpromedio")
    public ResponseEntity<?> getByAvgTime() {
        List<RepartidorTiempoPromedioResponse> reps = repartidorService.getByAverageDeliveryTime();
        return ResponseEntity.ok(reps);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getByInfoRepartidor() {
        List<RepartidorInfoResponse> repartidores = repartidorService.findAllRepartidorInfo();
        return ResponseEntity.ok(repartidores);
    }

    @GetMapping("/rendimiento")
    public ResponseEntity<?> getByTopPerformance(@RequestParam(required = false) Integer n) {
        // obtener los n mejores -> query del enunciado es top 3
        List<RepartidorMejorRendimientoResponse> reps = repartidorService.getByBestPerformance(n);
        return ResponseEntity.ok(reps);
    }
}
