package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.RepartidorMejorRendimientoResponse;
import com.example.tbd_lab1.DTO.RepartidorTiempoPromedioResponse;
import com.example.tbd_lab1.services.RepartidorService;
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

    @GetMapping("/tpromedio")
    public ResponseEntity<?> getByAvgTime() {
        List<RepartidorTiempoPromedioResponse> reps = repartidorService.getByAverageDeliveryTime();
        return ResponseEntity.ok(reps);
    }

    @GetMapping("/rendimiento")
    public ResponseEntity<?> getByTopPerformance(@RequestParam(required = false) Integer n) {
        // obtener los n mejores -> query del enunciado es top 3
        List<RepartidorMejorRendimientoResponse> reps = repartidorService.getByBestPerformance(n);
        return ResponseEntity.ok(reps);
    }
}
