package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.DTO.repartidor.RepartidorInfoResponse;
import com.example.tbd_lab2.DTO.repartidor.RepartidorMejorRendimientoResponse;
import com.example.tbd_lab2.DTO.repartidor.RepartidorTiempoPromedioResponse;
import com.example.tbd_lab2.entities.RepartidorEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class RepartidorRepository {
    private final JdbcTemplate jdbcTemplate;
    private final WKTReader reader = new WKTReader();

    private final RowMapper<RepartidorEntity> repartidorRowMapper = (rs, rowNum) -> {
        RepartidorEntity repartidorEntity = RepartidorEntity.builder()
                .idRepartidor(rs.getLong("id_repartidor"))
                .nombreRepartidor(rs.getString("nombre_repartidor"))
                .fechaContratacion(rs.getDate("fecha_contratacion"))
                .build();

        // handle point data
        String wkt = rs.getString("ubicacion");
        if (wkt != null && !wkt.isEmpty()) {
            try {
                Geometry geom = reader.read(wkt);
                repartidorEntity.setUbicacion((Point) geom);
            } catch (ParseException e) {
                System.out.println("====> Error parsing ubicacion = " + wkt);
                throw new RuntimeException(e);
            }
        }
        return repartidorEntity;
    };


    @Autowired
    public RepartidorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<RepartidorEntity> findById(Long id) {
        try {
            String sql = "SELECT id_repartidor, nombre_repartidor, fecha_contratacion, ST_AsText(ubicacion) AS ubicacion FROM repartidor WHERE id_repartidor = ?";
            RepartidorEntity repartidorEntity = jdbcTemplate.queryForObject(sql, repartidorRowMapper, id);
            return Optional.ofNullable(repartidorEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<RepartidorEntity> findAll() {
        String sql = "SELECT id_repartidor, nombre_repartidor, fecha_contratacion, ST_AsText(ubicacion) AS ubicacion FROM repartidor";
        return jdbcTemplate.query(sql, repartidorRowMapper);
    }

    public List<RepartidorInfoResponse> findAllRepartidorInfo() {
        try {
            String sql = "SELECT repartidor.nombre_repartidor, COUNT(detalle_pedido.id_repartidor) AS cantidad_entregas FROM detalle_pedido INNER JOIN repartidor ON repartidor.id_repartidor=detalle_pedido.id_repartidor GROUP BY nombre_repartidor";

            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    RepartidorInfoResponse.builder().nombre(rs.getString("nombre_repartidor"))
                            .cantPaquetesEntregados(rs.getInt("cantidad_entregas")).build());

        } catch (EmptyResultDataAccessException e) {
            System.out.println("No hay resultados para repartidores por tiempo promedio: "
                    + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
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
