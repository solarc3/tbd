package com.example.tbd_lab1.repositories;
import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
