package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.entities.ProductoEntity;
import com.example.tbd_lab1.services.ProductoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	private final ProductoService productoService;

	@Autowired
	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllProductos() {
		List<ProductoEntity> productos = productoService.getAllProductos();

		if (productos.isEmpty()) {
			return ResponseEntity.ok()
				.body(new MessageResponse("No hay productos disponibles."));
		}

		return ResponseEntity.ok(productos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductoById(@PathVariable Long id) {
		ProductoEntity producto = productoService.getProductoById(id);
		if (producto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new MessageResponse("Producto no encontrado con id: " + id)
			);
		}
		return ResponseEntity.ok(producto);
	}

	@GetMapping("/categoria/{categoria}")
	public ResponseEntity<?> getProductosByCategoria(
		@PathVariable String categoria
	) {
		List<ProductoEntity> productos =
			productoService.getProductosByCategoria(categoria);

		if (productos.isEmpty()) {
			return ResponseEntity.ok()
				.body(
					new MessageResponse(
						"No hay productos disponibles en la categoría: " +
						categoria
					)
				);
		}

		return ResponseEntity.ok(productos);
	}

	@GetMapping("/topcategoria")
	public ResponseEntity<?> getTopProductosPorCategoria() {
		List<TopProductosPorCategoriaResponse> topProductos =
			productoService.getMostOrderedProductsByCategory();

		if (topProductos.isEmpty()) {
			return ResponseEntity.ok()
				.body(
					new MessageResponse(
						"No hay productos pedidos en el último mes."
					)
				);
		}

		return ResponseEntity.ok(topProductos);
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crearProducto(
		@RequestBody ProductoEntity productoEntity
	) {
		ProductoEntity nuevoProducto = productoService.crearProducto(
			productoEntity
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarProducto(
		@PathVariable Long id,
		@RequestBody ProductoEntity productoEntity
	) {
		ProductoEntity productoActualizado = productoService.actualizarProducto(
			id,
			productoEntity
		);

		if (productoActualizado == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new MessageResponse("Producto no encontrado con id: " + id)
			);
		}

		return ResponseEntity.ok(productoActualizado);
	}

	@DeleteMapping("/{id}/eliminar")
	public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
		boolean eliminado = productoService.eliminarProducto(id);

		if (!eliminado) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new MessageResponse("Producto no encontrado con id: " + id)
			);
		}

		return ResponseEntity.ok(
			new MessageResponse("Producto eliminado correctamente")
		);
	}

	@GetMapping("/farmacia/{idFarmacia}")
	public ResponseEntity<?> getProductosByFarmaciaId(
		@PathVariable Long idFarmacia
	) {
		List<ProductoEntity> productos =
			productoService.getProductosByFarmaciaId(idFarmacia);

		if (productos.isEmpty()) {
			return ResponseEntity.ok()
				.body(
					new MessageResponse(
						"No hay productos disponibles en la farmacia con ID: " +
						idFarmacia
					)
				);
		}

		return ResponseEntity.ok(productos);
	}
}
