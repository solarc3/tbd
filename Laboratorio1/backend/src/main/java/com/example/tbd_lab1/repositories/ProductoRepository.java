package com.example.tbd_lab1.repositories;
import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.entities.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public ProductoEntity saveProducto(ProductoEntity producto) {
        if (producto.getIdProducto() == null) {
            String sql = "INSERT INTO producto (nombre_producto, precio,categoria,requiere_receta) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, producto.getNombreProducto());
                ps.setFloat(2, producto.getPrecio());
                ps.setString(3, producto.getCategoria());
                ps.setBoolean(4, producto.isRequiereReceta());
                return ps;
            }, keyHolder);
            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                producto.setIdProducto(generatedId.longValue());
            } else {
                System.err.println("No se pudo agregar el id del producto");
            }
        }else{
            String sql = "UPDATE producto SET nombre_producto = ?, precio = ?, categoria = ?, requiere_receta = ? WHERE id_producto = ?";
            jdbcTemplate.update(sql, producto.getNombreProducto(),
                    producto.getPrecio(),producto.getCategoria(),producto.isRequiereReceta(),producto.isRequiereReceta());
        }
        return producto;
    }

    public boolean deleteProducto(Long id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        Object[] args = new Object[] {id};
        return jdbcTemplate.update(sql, args) == 0;
    }

    public List<TopProductosPorCategoriaResponse> findMostOrderedProductsByCategory() {
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
}
