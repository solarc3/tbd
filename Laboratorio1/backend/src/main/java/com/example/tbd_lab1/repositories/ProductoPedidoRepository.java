package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.entities.ProductoPedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductoPedidoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductoPedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductoPedidoEntity> findByIdPedido(Long idPedido) {
        String sql = "SELECT id_producto_pedido, id_pedido, id_producto, cantidad, receta_validada FROM producto_pedido WHERE id_pedido = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductoPedidoEntity.class), idPedido);
    }
}
