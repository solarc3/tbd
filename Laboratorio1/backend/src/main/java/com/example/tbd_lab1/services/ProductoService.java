package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.entities.ProductoEntity;
import com.example.tbd_lab1.repositories.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

	@Autowired
	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	public List<ProductoEntity> getAllProductos() {
		return productoRepository.findAll();
	}

	public ProductoEntity getProductoById(Long id) {
		Optional<ProductoEntity> producto = productoRepository.findById(id);
		return producto.orElse(null);
	}

	public List<ProductoEntity> getProductosByCategoria(String categoria) {
		return productoRepository.findByCategory(categoria);
	}

	public List<
		TopProductosPorCategoriaResponse
	> getMostOrderedProductsByCategory() {
		return productoRepository.findMostOrderedProductsByCategory();
	}

	public boolean eliminarProducto(Long id) {
		return productoRepository.deleteProducto(id);
	}

	public ProductoEntity crearProducto(ProductoEntity productoEntity) {
		return productoRepository.saveProducto(productoEntity);
	}

	public ProductoEntity actualizarProducto(
		Long id,
		ProductoEntity productoEntity
	) {
		if (!productoRepository.findById(id).isPresent()) {
			return null;
		}
		productoEntity.setIdProducto(id);
		return productoRepository.saveProducto(productoEntity);
	}
}
