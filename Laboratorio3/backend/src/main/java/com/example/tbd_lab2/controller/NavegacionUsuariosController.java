package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.NavegacionRequest;
import com.example.tbd_lab2.services.NavegacionService;
import com.example.tbd_lab2.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/navegacion")
@AllArgsConstructor
public class NavegacionUsuariosController {
    private final NavegacionService navegacionService;
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllLogs() {
        return ResponseEntity.ok(navegacionService.getAll());
    }

    @GetMapping("/sin-compra")
    public ResponseEntity<?> getAllLogsWithNoPurchase() {
        return ResponseEntity.ok(navegacionService.getNavegacionSinCompras());
    }

    @Operation(summary = "[LAB 3] registrar navegacion usuario",
            description = "registrar accion del usuario, sean busquedas, clicks (en productos), filtros, compras.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "El registro se guarda correctamente"
            )
    })
    @PostMapping
    public ResponseEntity<?> logEvent(@RequestBody NavegacionRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long clientId = userDetails.getId();

            navegacionService.saveLog(clientId, request);

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al procesar el registro de navegación.");
        }
    }
}
