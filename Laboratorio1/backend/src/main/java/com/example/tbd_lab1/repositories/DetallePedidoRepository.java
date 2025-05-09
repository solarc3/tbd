package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.entities.DetallePedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class DetallePedidoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DetallePedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<DetallePedidoEntity> findById(Long id) {
        String sql = "SELECT * FROM detalle_pedido WHERE id_detalle_pedido = ?";
        try {
            DetallePedidoEntity detallePedidoEntity = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DetallePedidoEntity.class), id);
            return Optional.ofNullable(detallePedidoEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<DetallePedidoEntity> findByIdPedido(Long id) {
        String sql = "SELECT * FROM detalle_pedido WHERE id_pedido = ?";
        try {
            DetallePedidoEntity detallePedidoEntity = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DetallePedidoEntity.class), id);
            return Optional.ofNullable(detallePedidoEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<DetallePedidoEntity> findByIdRepartidor(Long id) {
        String sql = "SELECT * FROM detalle_pedido WHERE id_repartidor = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DetallePedidoEntity.class), id);
    }

    public DetallePedidoEntity save(DetallePedidoEntity detallePedidoEntity) {
        // create
        if (detallePedidoEntity.getIdDetallePedido() == null) {
            String sql = "INSERT INTO detalle_pedido(id_pedido, id_repartidor, metodo_pago, fecha_entrega) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
                ps.setLong(1, detallePedidoEntity.getIdPedido());
                ps.setLong(2, detallePedidoEntity.getIdRepartidor());
                ps.setString(3, detallePedidoEntity.getMetodoPago());
                ps.setTimestamp(4, Timestamp.valueOf(detallePedidoEntity.getFechaEntrega()));
                return ps;
            }, keyHolder);

            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                detallePedidoEntity.setIdDetallePedido(generatedId.longValue());
            } else {
                System.err.println("Error al generar id de pedido completo: " + detallePedidoEntity.getIdPedido());
            }

        } else {
            // update
            String sql = "UPDATE detalle_pedido SET id_pedido = ?, id_repartidor = ?, metodo_pago = ?, fecha_entrega = ? WHERE id_detalle_pedido = ?";
            jdbcTemplate.update(sql,
                    detallePedidoEntity.getIdPedido(),
                    detallePedidoEntity.getIdRepartidor(),
                    detallePedidoEntity.getMetodoPago(),
                    detallePedidoEntity.getFechaEntrega(),
                    detallePedidoEntity.getIdDetallePedido());
        }
        return detallePedidoEntity;
    }
}
