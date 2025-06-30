package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.pedido.PedidoCruzaZonasResponse;
import com.example.tbd_lab2.DTO.repartidor.*;
import com.example.tbd_lab2.entities.RepartidorEntity;
import com.example.tbd_lab2.services.RepartidorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // documentación de swagger para equipo front
    @Operation(summary = "Calcular la distancia total recorrida por un repartidor en el último mes.", description = "Obtiene la distancia total recorrida en kilómetros por cada repartidor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se muestran todos los repartidores junto con su distancia en kilómetros.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RepartidorDistanciaTotalDTO.class)))
    })
    @GetMapping("/distancia-total")
    public ResponseEntity<?> getByDistanciaTotal() {
        List<RepartidorDistanciaTotalDTO> distanciasTotalDTOS = repartidorService.getRepartidorDistanciaTotal();
        if (distanciasTotalDTOS.isEmpty()) {
            return ResponseEntity.ok().body("No hay datos de distancia total para los repartidores.");
        }
        return ResponseEntity.ok(distanciasTotalDTOS);
    }

    @Operation(summary = "[LAB 3] Analizar las rutas más frecuentes de repartidores en los últimos 7 días.", description = "Implementación de consulta 4: Agrupa por repartidor y por medio de agregate va entregando la frecuencia por punto, solo toma en cuenta la parte entera de la coordenada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Para cada repartidor que esté siendo logueado, se entrega la ruta cómo un cjto de puntos con su timestamp (sampleo) y la frecuencia de la dicha ruta.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RepartidorRutasFrecuentesDTO.class)))
    })
    @GetMapping("/rutas-frecuentes")
    public ResponseEntity<List<RepartidorRutasFrecuentesDTO>> getRutasFrecuentes() {
        List<RepartidorRutasFrecuentesDTO> rutas = repartidorService.getRutasFrecuentesUltimos7Dias();
        return ResponseEntity.ok(rutas);
    }
}
