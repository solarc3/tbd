package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.*;
import com.example.tbd_lab2.DTO.cliente.ClienteLejanoDeFarmaciaResponse;
import com.example.tbd_lab2.DTO.pedido.*;
import com.example.tbd_lab2.entities.DetallePedidoEntity;
import com.example.tbd_lab2.entities.PedidoEntity;
import com.example.tbd_lab2.services.DetallePedidoService;
import com.example.tbd_lab2.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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

    @GetMapping("/{id_pedido}/productos")
    public ResponseEntity<?> getProductosPedido(@PathVariable Long id_pedido) {
        Optional<PedidoEntity> pedido = pedidoService.getById(id_pedido);
        if (pedido.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Pedido no encontrado"));
        }
        return ResponseEntity.ok(pedidoService.getProducts(id_pedido));
    }

    @GetMapping("/pagourgente")
    public ResponseEntity<?> pagourgente() {
        List<PagoMasUsadoUrgenteResponse> pago = pedidoService.pagoMasUsadoUrgente();

        if (pago.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen metodo de pago mas urgente "));
        }
        return ResponseEntity.ok(pago);
    }


    @Operation(summary = "[LAB 3] Obtener los pedidos cuyas actualizaciones hayan sido mas de 3 o más y en menos de 10 minutos", description = "Implementación de query 3.- \"Contar cuántos pedidos tienen más de 3 cambios de estado en menos de 10 minutos.\" ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los logs del pedido en logsPedido, y el pedido en sí en pedidos, ambos comparten su index, es decir, el log en la posición 0 pertenece al pedido en el index 0 de pedidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogsCambioPedidosResponse.class)))
    })
    @GetMapping("/logs/cambios-rapidos")
    public ResponseEntity<?> getPedidosCambiosRapidos() {
        LogsCambioPedidosResponse logs = pedidoService.getLogsPedidosByChangesHours();

        return ResponseEntity.ok().body(Objects.requireNonNullElseGet(logs, () -> new MessageResponse("No existen opiniones...")));
    }

    @PostMapping("/{id_pedido}/entregar")
    public ResponseEntity<?> entregarPedido(@PathVariable Long id_pedido, @RequestBody DetallePedidoRequest detallePedidoRequest) {
        Optional<PedidoEntity> pedidoEntity = pedidoService.getById(id_pedido);
        if (pedidoEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Pedido no encontrado"));
        }
        if (detallePedidoService.getByIdPedido(id_pedido).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Pedido ya entregado"));
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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

    @Operation(summary = "[LAB 2] Obtener los pedidos cuya ruta estimada cruce más de N sectores", description = "Implementación de query 5.- \"Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.\" En este caso sectorAmount = 3")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos que abarquen el minimo de sectores, con info acotada- solo id, fecha, id y nombre del cliente; y un array con los nombres de los sectores por donde pasa su ruta estimada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoCruzaZonasResponse.class)))
    })
    @GetMapping("/cruza-zonas-reparto/{sectorAmount}")
    public ResponseEntity<?> cruzaZonasReparto(@PathVariable("sectorAmount") @Parameter(
            name = "sectorAmount",
            description = "Cantidad mínima de sectores",
            example = "3") Integer sectorAmount) {
        List<PedidoCruzaZonasResponse> pedidos = pedidoService.getBySectorIntersection(sectorAmount);
        return ResponseEntity.ok(pedidos);
    }
}
