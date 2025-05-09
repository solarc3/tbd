package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.entities.ProductoEntity;
import com.example.tbd_lab1.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //FALTA IMPLEMENTAR ACTUALIZAR

    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@RequestBody ProductoEntity productoEntity) {
        ProductoEntity nuevoProducto = productoService.crearProducto(productoEntity);
        return ResponseEntity.ok(nuevoProducto);
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}