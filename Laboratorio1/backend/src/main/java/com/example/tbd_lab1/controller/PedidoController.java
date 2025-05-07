package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.services.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/pedido")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pagourgente")
    public ResponseEntity<?> pagourgente() {
        List<PagoMasUsadoUrgenteResponse> pago = pedidoService.pagoMasUsadoUrgente();

        if (pago.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No existen metodo de pago mas urgente "));
        }
        return ResponseEntity.ok(pago);

    }


}
