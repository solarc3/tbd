package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.EventoNavegacionRequest;
import com.example.tbd_lab2.DTO.MessageResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaClosestDeliveryResponse;
import com.example.tbd_lab2.collections.NavegacionUsuariosCollection;
import com.example.tbd_lab2.services.NavegacionUsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log")
@AllArgsConstructor
public class NavegacionUsuariosController {
    private final NavegacionUsuariosService navegacionUsuariosService;
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllLogs() {
        return ResponseEntity.ok(navegacionUsuariosService.getAll());
    }

    @Operation(summary = "[LAB 3] registrar navegacion usuario",
            description = "registrar accion del usuario, sean busquedas, clicks (en productos), filtros, compras.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "El registro se guarda correctamente",
                    content=@Content(mediaType = "application/json", schema = @Schema(implementation = EventoNavegacionRequest.class)))
    })
    @PostMapping
    public ResponseEntity<?> logEvent(@RequestBody EventoNavegacionRequest evento) {
        try {
            navegacionUsuariosService.saveLog(evento);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
