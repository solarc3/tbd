package com.example.tbd_lab1.repositories;

import java.sql.PreparedStatement;
import java.util.Optional;

import com.example.tbd_lab1.entities.CalificacionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CalificacionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CalificacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CalificacionEntity> findById(Long id) {
        String sql = "SELECT id_calificacion, id_detalle_pedido, cliente_id, puntuacion FROM calificacion WHERE id_calificacion = ?";
        try {
            CalificacionEntity calificacion = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CalificacionEntity.class), id);

            return Optional.ofNullable(calificacion);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public CalificacionEntity save(CalificacionEntity calificacion) {
        if (calificacion.getIdCalificacion() == null) {
            String sql = "INSERT INTO calificacion (id_detalle_pedido, cliente_id, puntuacion) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_calificacion"});
                        ps.setLong(1, calificacion.getIdDetallePedido());
                        ps.setLong(2, calificacion.getClienteId());
                        ps.setFloat(3, calificacion.getPuntuacion());
                        return ps;
                    }
                    , keyHolder);
            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                calificacion.setIdCalificacion(generatedId.longValue());
            } else {
                System.err.println("no hay id para reserva...");
                // throw new org.springframework.dao.DataRetrievalFailureException("Failed to retrieve ID for user: " + user.getUsername());
            }
        }
        else {
            String sql = "UPDATE calificacion SET id_detalle_pedido = ?, cliente_id = ?, puntuacion = ? WHERE id_calificacion = ?";
            jdbcTemplate.update(sql,
                    calificacion.getIdDetallePedido(),
                    calificacion.getClienteId(),
                    calificacion.getPuntuacion(),
                    calificacion.getIdCalificacion());
        }
        return calificacion;
    }
    //TODO: get calificaciones by id_repartidores
    // esto es responsabilidad del repositorio de detalle_pedido, se debe llamar un metodo de ese repositorio para eso
    // se quiere implementar el controlador de dado un id_repartidor, ver las reviews de sus pedidos
}
