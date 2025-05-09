package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.entities.ProductoEntity;
import com.example.tbd_lab1.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<TopProductosPorCategoriaResponse> getMostOrderedProductsByCategory() {
        return productoRepository.findMostOrderedProductsByCategory();
    }

    public boolean eliminarProducto(Long id) {
        return productoRepository.deleteProducto(id);
    }

    public ProductoEntity crearProducto(ProductoEntity productoEntity) {
        return productoRepository.saveProducto(productoEntity);
    }
}