package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.DTO.producto.TopProductosPorCategoriaResponse;
import com.example.tbd_lab2.entities.ProductoEntity;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoRepository {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ProductoRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ProductoEntity saveProducto(ProductoEntity producto) {
		if (producto.getIdProducto() == null) {
			String sql =
				"INSERT INTO producto (nombre_producto, precio,categoria,requiere_receta) VALUES (?, ?, ?, ?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(
						sql,
						new String[] { "id" }
					);
					ps.setString(1, producto.getNombreProducto());
					ps.setFloat(2, producto.getPrecio());
					ps.setString(3, producto.getCategoria());
					ps.setBoolean(4, producto.isRequiereReceta());
					return ps;
				},
				keyHolder
			);
			Number generatedId = keyHolder.getKey();
			if (generatedId != null) {
				producto.setIdProducto(generatedId.longValue());
			} else {
				System.err.println("No se pudo agregar el id del producto");
			}
		} else {
			String sql =
				"UPDATE producto SET nombre_producto = ?, precio = ?, categoria = ?, requiere_receta = ? WHERE id_producto = ?";
			jdbcTemplate.update(
				sql,
				producto.getNombreProducto(),
				producto.getPrecio(),
				producto.getCategoria(),
				producto.isRequiereReceta(),
				producto.isRequiereReceta()
			);
		}
		return producto;
	}

	public List<ProductoEntity> findByCategory(String category) {
		String sql =
			"SELECT id_producto, nombre_producto, precio, categoria, requiere_receta, image_url as imageUrl FROM producto WHERE LOWER(categoria) = LOWER(?)";
		try {
			return jdbcTemplate.query(
				sql,
				new BeanPropertyRowMapper<>(ProductoEntity.class),
				category
			);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			System.err.println(
				"Error fetching products for category " +
				category +
				": " +
				e.getMessage()
			);
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public boolean deleteProducto(Long id) {
		String sql = "DELETE FROM producto WHERE id_producto = ?";
		Object[] args = new Object[] { id };
		return jdbcTemplate.update(sql, args) == 0;
	}

	public List<
		TopProductosPorCategoriaResponse
	> findMostOrderedProductsByCategory() {
		try {
			String sql =
				"WITH ranked_products AS (" +
				"    SELECT " +
				"        p.categoria, " +
				"        p.nombre_producto, " +
				"        COUNT(pp.id_producto) AS cantidad_pedidos, " +
				"        RANK() OVER (PARTITION BY p.categoria ORDER BY COUNT(pp.id_producto) DESC) AS rank " +
				"    FROM " +
				"        producto p " +
				"    JOIN producto_pedido pp ON p.id_producto = pp.id_producto " +
				"    JOIN pedido pe ON pp.id_pedido = pe.id_pedido " +
				"    WHERE " +
				"        pe.fecha_pedido >= (CURRENT_DATE - INTERVAL '1 month') " +
				"        AND pe.estado_pedido::text = 'ENTREGADO' " +
				"    GROUP BY " +
				"        p.categoria, p.nombre_producto " +
				") " +
				"SELECT " +
				"    categoria, " +
				"    nombre_producto, " +
				"    cantidad_pedidos " +
				"FROM " +
				"    ranked_products " +
				"WHERE " +
				"    rank = 1 " +
				"ORDER BY " +
				"    categoria";

			return jdbcTemplate.query(sql, (rs, rowNum) ->
				TopProductosPorCategoriaResponse.builder()
					.categoria(rs.getString("categoria"))
					.nombreProducto(rs.getString("nombre_producto"))
					.cantidadPedidos(rs.getLong("cantidad_pedidos"))
					.build()
			);
		} catch (EmptyResultDataAccessException e) {
			System.out.println("No results found: " + e.getMessage());
			return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<ProductoEntity> findAll() {
		String sql =
			"SELECT id_producto, nombre_producto, precio, categoria, requiere_receta, image_url as imageUrl FROM producto";
		try {
			return jdbcTemplate.query(
				sql,
				new BeanPropertyRowMapper<>(ProductoEntity.class)
			);
		} catch (EmptyResultDataAccessException e) {
			System.out.println("No products found: " + e.getMessage());
			return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public Optional<ProductoEntity> findById(Long id) {
		String sql =
			"SELECT id_producto, nombre_producto, precio, categoria, requiere_receta, image_url as imageUrl FROM producto WHERE id_producto = ?";
		try {
			ProductoEntity producto = jdbcTemplate.queryForObject(
				sql,
				new BeanPropertyRowMapper<>(ProductoEntity.class),
				id
			);
			return Optional.ofNullable(producto);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (Exception e) {
			System.err.println(
				"Error fetching product with id " + id + ": " + e.getMessage()
			);
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public List<ProductoEntity> findByFarmaciaId(Long idFarmacia) {
		String sql =
			"SELECT " +
			"  p.id_producto      AS idProducto, " +
			"  p.nombre_producto  AS nombreProducto, " +
			"  p.precio, " +
			"  p.categoria, " +
			"  p.requiere_receta  AS requiereReceta, " +
			"  p.image_url        AS imageUrl " +
			"FROM producto p " +
			"JOIN producto_farmacia pf ON p.id_producto = pf.id_producto " +
			"WHERE pf.id_farmacia = ? " +
			"  AND pf.stock_producto > 0";

		try {
			return jdbcTemplate.query(
				sql,
				new BeanPropertyRowMapper<>(ProductoEntity.class),
				idFarmacia
			);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			System.err.println(
				"Error fetching products for farmacia " +
				idFarmacia +
				": " +
				e.getMessage()
			);
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
