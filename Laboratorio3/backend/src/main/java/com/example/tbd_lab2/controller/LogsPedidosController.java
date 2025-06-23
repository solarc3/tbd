package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.collections.LogsPedidosCollection;
import com.example.tbd_lab2.services.LogsPedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/logs")
public class LogsPedidosController {
    @Autowired private LogsPedidosService logsPedidosService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getLogsByPedidoId(@PathVariable Long id) {
        Optional<LogsPedidosCollection> logs = logsPedidosService.getByIdPedido(id);
        if (logs.isPresent()) {
            return new ResponseEntity<>(logs.get(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
