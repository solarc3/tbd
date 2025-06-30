package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab2.DTO.pedido.PedidoCruzaZonasResponse;
import com.example.tbd_lab2.DTO.producto.ProductoPedidoRequest;
import com.example.tbd_lab2.DTO.pedido.RegistrarPedidoCompletoRequest;
import com.example.tbd_lab2.entities.PedidoEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.*;

@Repository
public class PedidoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final WKTReader reader = new WKTReader();

    private final RowMapper<PedidoEntity> pedidoRowMapper = (rs, rowNum) -> {
        PedidoEntity pedidoEntity = PedidoEntity.builder()
                .idPedido(rs.getLong("id_pedido"))
                .monto(rs.getInt("monto"))
                .fechaPedido(rs.getTimestamp("fecha_pedido").toLocalDateTime())
                .esUrgente(rs.getBoolean("es_urgente"))
                .estadoPedido(PedidoEntity.EstadoPedido.valueOf(rs.getString("estado_pedido")))
                .idCliente(rs.getObject("id_cliente", Long.class)) // getObject is safer for nullable FKs
                .idFarmacia(rs.getObject("id_farmacia", Long.class))
                .build();

        // handle linestring data
        String wkt = rs.getString("ruta_estimada");
        if (wkt != null && !wkt.isEmpty()) {
            try {
                Geometry geometry = reader.read(wkt);
                pedidoEntity.setRutaEstimada((LineString) geometry);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return pedidoEntity;
    };

    @Autowired
    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PedidoEntity> getPedidos() {
        try {
            String sql = "SELECT id_pedido, monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia, ST_AsText(ruta_estimada) AS ruta_estimada FROM pedido";
            return jdbcTemplate.query(sql, pedidoRowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<PedidoEntity> findById(Long id) {
        try {
            String sql = "SELECT id_pedido, monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia, ST_AsText(ruta_estimada) AS ruta_estimada FROM pedido WHERE id_pedido = ?";
            PedidoEntity pedidoEntity = jdbcTemplate.queryForObject(sql, pedidoRowMapper, id);
            return Optional.ofNullable(pedidoEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<PedidoEntity> findByIdCliente(Long idCliente) {
        String sql = "SELECT id_pedido, monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia, ST_AsText(ruta_estimada) AS ruta_estimada FROM pedido WHERE id_cliente = ?";
        return jdbcTemplate.query(sql, pedidoRowMapper, idCliente);
    }

    public List<PagoMasUsadoUrgenteResponse> findMostUsedPaymentMethodWhenUrgent(){
        try{
            String sql = "SELECT COUNT(detalle_pedido.metodo_pago) AS cantidad_pagos, metodo_pago FROM pedido RIGHT JOIN detalle_pedido ON pedido.id_pedido = detalle_pedido.id_pedido WHERE pedido.es_urgente = 'true'\n" +
                    "GROUP BY detalle_pedido.metodo_pago ORDER BY cantidad_pagos";
            return  jdbcTemplate.query(sql, (rs, rowNum) ->
                    PagoMasUsadoUrgenteResponse.builder()
                            .metodoPago(rs.getString("metodo_pago"))
                            .cantidad(rs.getInt("cantidad_pagos"))
                            .build()
            );
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // [08] Procedimiento Almacenado - Cambiar estado de un pedido con validacion
    public boolean updateState(Long idPedido, String nuevoEstado) {
        try {
            // parámetros del procedimiento
            List<SqlParameter> params = List.of(
                    new SqlParameter("p_id_pedido", Types.BIGINT),
                    new SqlParameter("p_nuevoEstado", Types.VARCHAR)
            );
            jdbcTemplate.call(con -> {
                // castear argumentos explicitamente y ejecutar
                CallableStatement callableStatement = con.prepareCall("CALL cambiar_estado_pedido(?::bigint, ?::estado_pedido)");
                callableStatement.setLong(1, idPedido);
                callableStatement.setString(2, nuevoEstado);
                return callableStatement;
            }, params);
            return true;
        } catch (DataAccessException e) {
            System.err.println("Error al cambiar estado pedido: " + e.getMessage());
            return false;
        }
    }
    public boolean registrarPedidoCompleto(RegistrarPedidoCompletoRequest request) {
        try {
            // desempaquetar lista de dtos
            List<ProductoPedidoRequest> productos = request.getProductos();
            Long[] productosArray = productos.stream()
                    .map(ProductoPedidoRequest::getIdProducto)
                    .toArray(Long[]::new);
            Integer[] cantidadesArray = productos.stream()
                    .map(ProductoPedidoRequest::getCantidad)
                    .toArray(Integer[]::new);
            Boolean[] recetasValidasArray = productos.stream()
                    .map(ProductoPedidoRequest::getRecetaValidada)
                    .toArray(Boolean[]::new);

            List<SqlParameter> params = List.of(
                    new SqlParameter("p_monto", Types.INTEGER),
                    new SqlParameter("p_fecha_pedido", Types.TIMESTAMP),
                    new SqlParameter("p_es_urgente", Types.BOOLEAN),
                    new SqlParameter("p_estado_pedido", Types.VARCHAR),
                    new SqlParameter("p_id_cliente", Types.BIGINT),
                    new SqlParameter("p_id_farmacia", Types.BIGINT),
                    new SqlParameter("p_id_productos", Types.ARRAY),
                    new SqlParameter("p_cantidades", Types.ARRAY),
                    new SqlParameter("p_recetas_validadas", Types.ARRAY)
            );

            jdbcTemplate.call(con -> {
                CallableStatement callableStatement = con.prepareCall("CALL registrar_pedido_completo(?, ?, ?, ?::estado_pedido, ?, ?, ?, ?, ?)");
                callableStatement.setInt(1, request.getMonto());
                callableStatement.setTimestamp(2, java.sql.Timestamp.valueOf(request.getFechaPedido()));
                callableStatement.setBoolean(3, request.getEsUrgente());
                callableStatement.setString(4, request.getEstadoPedido().name());
                callableStatement.setLong(5, request.getIdCliente());
                callableStatement.setLong(6, request.getIdFarmacia());

                callableStatement.setArray(7, con.createArrayOf("BIGINT", productosArray));
                callableStatement.setArray(8, con.createArrayOf("INTEGER", cantidadesArray));
                callableStatement.setArray(9, con.createArrayOf("BOOLEAN", recetasValidasArray));

                return callableStatement;
            }, params);
            return true;
        } catch (DataAccessException e) {
            System.err.println("Error al registrar pedido completo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Queries Laboratorio 2
    // 5. Listar todos los pedidos cuya ruta estimada cruce más de ? zonas de reparto.
    public List<PedidoCruzaZonasResponse> findBySectorIntersection(Integer sectorAmount) {
        String sql = """
                SELECT id_pedido, id_cliente, fecha_pedido,
                    ARRAY_AGG(DISTINCT sectores.nombre_sector) AS nombres_zonas
                FROM pedido
                JOIN sectores ON ST_Intersects(pedido.ruta_estimada,
                                               sectores.area)
                GROUP BY id_pedido, id_cliente
                HAVING COUNT(DISTINCT sectores.id) >= ?;
                """;
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    Array nombresZonas = rs.getArray("nombres_zonas");
                    return PedidoCruzaZonasResponse.builder()
                            .idPedido(rs.getLong("id_pedido"))
                            .idCliente(rs.getLong("id_cliente"))
                            .fechaPedido(rs.getTimestamp("fecha_pedido").toLocalDateTime())
                            .nombresZonas((String[]) nombresZonas.getArray())
                            .build();
                },
                sectorAmount);
    }
}
