package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.MessageResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPromedioDTO;
import com.example.tbd_lab2.DTO.opinion.AgruparHoraResponse;
import com.example.tbd_lab2.DTO.pedido.LogsCambioPedidosResponse;
import com.example.tbd_lab2.DTO.pedido.PedidoCruzaZonasResponse;
import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import com.example.tbd_lab2.services.OpinionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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

    @Operation(summary = "Crear una nueva opinión para un pedido", description = "Crea y guarda una nueva opinión de un cliente sobre un pedido específico en MongoDB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La opinión fue creada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OpinionesClientesCollection.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la opinión inválidos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<?> createOpinion(@RequestBody OpinionesClientesCollection opinion) {
        if (opinion.getPuntuacion() < 1 || opinion.getPuntuacion() > 5) {
            return ResponseEntity.badRequest().body(new MessageResponse("La puntuación debe estar entre 1 y 5."));
        }
        OpinionesClientesCollection nuevaOpinion = opinionService.crearOpinion(opinion);
        return ResponseEntity.ok(nuevaOpinion);
    }

    @Operation(summary = "[LAB 3] Obtener las opiniones de los pedidos entregados separados por las horas en cuando fueron entregados", description = "Implementación de query 6.- \"Agrupar opiniones por hora del día para analizar patrones de satisfacción.\" ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Un map, su llave es la hora de la entrega de todos los pedidos dentro de esta, todo lo que entrega este" +
                    "response es un hashmap que tiene las opiniones divididos por horas, y las horas son las llaves para acceder a las opiniones mismas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgruparHoraResponse.class)))
    })
    @GetMapping("/opiniones-por-hora")
    public ResponseEntity<?> getOpinionesPorHora() {
        AgruparHoraResponse opiniones = opinionService.getOpinionesByHoras();
        return ResponseEntity.ok().body(Objects.requireNonNullElseGet(opiniones, () -> new MessageResponse("No existen opiniones...")));

    }

    @Operation(summary = "[LAB 3] Obtener el promedio de puntuación por empresa o farmacia.", description = "Implementación de consulta 1: Tomar del log el id de los pedidos y hacer un 'JOIN' con la tabla de farmcia, luego de manera funcional se va mapeando y con la función average() se computa el promedio x log.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Para cada farmacia, se le asocia un promedio, notar que solo se entrega el nombre de la farmacia; si es necesario se puede modificar para obtener el objeto farmacia.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FarmaciaPromedioDTO.class)))
    })
    @GetMapping("/farmacia/promedio")
    public ResponseEntity<?> getPromedioPuntuacionPorFarmacia() {
        List<FarmaciaPromedioDTO> promedios = opinionService.getPromedioPuntuacionPorFarmacia();
        if (promedios.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No hay suficientes datos para calcular promedios por farmacia."));
        }
        return ResponseEntity.ok().body(promedios);
    }


}
