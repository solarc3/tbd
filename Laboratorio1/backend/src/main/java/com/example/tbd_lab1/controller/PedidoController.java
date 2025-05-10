package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.*;
import com.example.tbd_lab1.entities.DetallePedidoEntity;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.services.DetallePedidoService;
import com.example.tbd_lab1.services.PedidoService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;

    public PedidoController(PedidoService pedidoService, DetallePedidoService detallePedidoService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getPedidos(){
        List<PedidoEntity> pedidos = pedidoService.getPedidos();
        if(pedidos.isEmpty()){
            return ResponseEntity.ok().body(new MessageResponse("No hay pedidos en el sistema."));
        }
        return ResponseEntity.ok(pedidos);
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

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> getPedidosByCliente(@PathVariable Long idCliente) {
        List<PedidoEntity> pedidos = pedidoService.getByIdCliente(idCliente);

        if (pedidos.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No hay pedidos para este cliente."));
        }

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id_pedido}/detalles")
    public ResponseEntity<?> getDetallePedido(@PathVariable Long id_pedido) {
        Optional<PedidoEntity> pedido = pedidoService.getById(id_pedido);
        if (pedido.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Pedido no encontrado"));
        }
        Optional<DetallePedidoEntity> detallePedidoEntity = detallePedidoService.getByIdPedido(id_pedido);
        if (detallePedidoEntity.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Pedido no completado"));
        }
        return ResponseEntity.ok(detallePedidoService.mapToResponse(detallePedidoEntity.get()));
    }

    @GetMapping("/pagourgente")
    public ResponseEntity<?> pagourgente() {
        List<PagoMasUsadoUrgenteResponse> pago = pedidoService.pagoMasUsadoUrgente();

        if (pago.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen metodo de pago mas urgente "));
        }
        return ResponseEntity.ok(pago);
    }

    @PostMapping("/{id_pedido}/entregar")
    public ResponseEntity<?> entregarPedido(@PathVariable Long id_pedido, @RequestBody DetallePedidoRequest detallePedidoRequest) {
        Optional<PedidoEntity> pedidoEntity = pedidoService.getById(id_pedido);
        if (pedidoEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Pedido no encontrado"));
        }
        detallePedidoService.createDetallePedido(
                pedidoEntity.get(),
                detallePedidoRequest.getIdRepartidor(),
                detallePedidoRequest.getMetodoPago());
        pedidoService.cambiarEstado(id_pedido, "ENTREGADO");
        // volver a recuperarlo de la db para obtener el timestamp generado por el trigger
        return ResponseEntity.ok().body(detallePedidoService.getByIdPedido(id_pedido).get());
    }

    @PostMapping("/cambiarestado")
    public ResponseEntity<?> cambiarEstado(@RequestBody EstadoPedidoRequest estadoPedidoRequest) {
        boolean updated = pedidoService.cambiarEstado(estadoPedidoRequest.getIdPedido(),
                    estadoPedidoRequest.getNuevoEstado());
        if (updated) {
            return ResponseEntity.ok().body(new MessageResponse("Estado cambiado"));
        }
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
