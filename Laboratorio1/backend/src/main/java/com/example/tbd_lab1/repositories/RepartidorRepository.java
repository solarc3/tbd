package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.RepartidorMejorRendimientoResponse;
import com.example.tbd_lab1.DTO.RepartidorTiempoPromedioResponse;
import com.example.tbd_lab1.entities.RepartidorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RepartidorRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RepartidorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<RepartidorEntity> findById(Long id) {
        try {
            String sql = "SELECT id_repartidor, nombre_repartidor, fecha_contratacion FROM repartidor WHERE id_repartidor = ?";
            RepartidorEntity repartidorEntity = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(RepartidorEntity.class), id);
            return Optional.ofNullable(repartidorEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<RepartidorTiempoPromedioResponse> findAverageDeliveryTime() {
        try {
            String sql =
                    "SELECT r.id_repartidor, r.nombre_repartidor, " +
                        "AVG(EXTRACT(EPOCH FROM dp.fecha_entrega - p.fecha_pedido) / 3600) " +
                        "AS promedio_horas " +
                    "FROM repartidor AS r " +
                    "JOIN detalle_pedido AS dp ON dp.id_repartidor = r.id_repartidor " +
                    "JOIN pedido p ON p.id_pedido = dp.id_pedido " +
                    "GROUP BY r.id_repartidor";

            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    RepartidorTiempoPromedioResponse.builder()
                            .idRepartidor(rs.getLong("id_repartidor"))
                            .nombreRepartidor(rs.getString("nombre_repartidor"))
                            .promedioHoras(rs.getDouble("promedio_horas"))
                            .build());
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No hay resultados para repartidores por tiempo promedio: "
                    + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<RepartidorMejorRendimientoResponse> findByPerformance() {
        try {
            String sql =
                    "WITH rendimiento AS " +
                        "(SELECT r.id_repartidor, r.nombre_repartidor, COUNT(dp.id_pedido) AS pedidos, " +
                        "COALESCE(AVG(c.puntuacion),0) AS puntuacion_promedio " +
                        "FROM repartidor r " +
                        "JOIN detalle_pedido dp ON dp.id_repartidor = r.id_repartidor " +
                        "LEFT JOIN calificacion c ON c.id_detalle_pedido = dp.id_detalle_pedido " +
                        "GROUP BY r.id_repartidor) " +
                    "SELECT id_repartidor, nombre_repartidor, pedidos, puntuacion_promedio, " +
                            "(pedidos * puntuacion_promedio) AS indice_rendimiento " +
                    "FROM rendimiento " +
                    "ORDER BY indice_rendimiento DESC";

            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    RepartidorMejorRendimientoResponse.builder()
                            .idRepartidor(rs.getLong("id_repartidor"))
                            .nombreRepartidor(rs.getString("nombre_repartidor"))
                            .pedidos(rs.getLong("pedidos"))
                            .puntuacionPromedio(rs.getDouble("puntuacion_promedio"))
                            .indiceRendimiento(rs.getDouble("indice_rendimiento"))
                            .build());
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No hay resultados para mejores repartidores: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
