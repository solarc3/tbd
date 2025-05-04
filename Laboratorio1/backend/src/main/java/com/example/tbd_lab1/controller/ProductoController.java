package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/topcategoria")
    public ResponseEntity<?> getTopProductosPorCategoria() {
        List<TopProductosPorCategoriaResponse> topProductos = productoService.getMostOrderedProductsByCategory();

        if (topProductos.isEmpty()) {
            return ResponseEntity.ok().body(new MessageResponse("No hay productos pedidos en el último mes."));
        }

        return ResponseEntity.ok(topProductos);
    }
}