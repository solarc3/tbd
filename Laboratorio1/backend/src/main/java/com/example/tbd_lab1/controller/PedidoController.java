package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.EstadoPedidoRequest;
import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.DTO.RegistrarPedidoCompletoRequest;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.services.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id_pedido}")
    public ResponseEntity<?> getPedido(@PathVariable Long id_pedido) {
        // ruta de prueba mas que nada, si se deja, faltaria checkear id_cliente con userdetails
        Optional<PedidoEntity> pedido = pedidoService.getById(id_pedido);
        if (pedido.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Pedido no encontrado"));
        }
        return ResponseEntity.ok(pedido.get());
    }

    @GetMapping("/pagourgente")
    public ResponseEntity<?> pagourgente() {
        List<PagoMasUsadoUrgenteResponse> pago = pedidoService.pagoMasUsadoUrgente();

        if (pago.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen metodo de pago mas urgente "));
        }
        return ResponseEntity.ok(pago);
    }

    @PostMapping("/cambiarestado")
    public ResponseEntity<?> cambiarEstado(@RequestBody EstadoPedidoRequest estadoPedidoRequest) {
        boolean updated = pedidoService.cambiarEstado(estadoPedidoRequest.getIdPedido(),
                    estadoPedidoRequest.getNuevoEstado());
        if (updated)
            return ResponseEntity.ok().body(new MessageResponse("Estado cambiado"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("Error al cambiar estado"));
    }

    @PostMapping("/registrarcompleto")
    public ResponseEntity<?> registrarPedidoCompleto(@RequestBody RegistrarPedidoCompletoRequest request) {
        boolean success = pedidoService.registrarPedidoCompleto(request);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Pedido registrado exitosamente."));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Error al registrar el pedido completo."));
    }
}
