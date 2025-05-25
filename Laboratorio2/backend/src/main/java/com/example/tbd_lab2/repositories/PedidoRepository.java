package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab2.DTO.ProductoPedidoRequest;
import com.example.tbd_lab2.DTO.RegistrarPedidoCompletoRequest;
import com.example.tbd_lab2.entities.PedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.*;

@Repository
public class PedidoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PedidoEntity> getPedidos() {
        try {
            String sql = "SELECT * FROM pedido";
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    PedidoEntity.builder()
                            .idPedido(rs.getLong("id_pedido"))
                            .monto(rs.getInt("monto"))
                            .fechaPedido(rs.getTimestamp("fecha_pedido").toLocalDateTime())
                            .esUrgente(rs.getBoolean("es_urgente"))
                            .estadoPedido(PedidoEntity.EstadoPedido.valueOf(rs.getString("estado_pedido")))
                            .idCliente(rs.getObject("id_cliente", Long.class))
                            .idFarmacia(rs.getObject("id_farmacia", Long.class))
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<PedidoEntity> findById(Long id) {
        try {
            String sql = "SELECT id_pedido, monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia FROM pedido WHERE id_pedido = ?";
            PedidoEntity pedidoEntity = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PedidoEntity.class), id);
            return Optional.ofNullable(pedidoEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<PedidoEntity> findByIdCliente(Long idCliente) {
        String sql = "SELECT id_pedido, monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia FROM pedido WHERE id_cliente = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PedidoEntity.class), idCliente);
    }

    public List<PagoMasUsadoUrgenteResponse> findMostUsedPaymentMethodWhenUrgent(){
        try{
            String sql = "Select count(detalle_pedido.metodo_pago) as Cantidad_pagos, metodo_pago FROM pedido RIGHT JOIN detalle_pedido ON pedido.id_pedido = detalle_pedido.id_pedido WHERE pedido.es_urgente = 'true'\n" +
                    "GROUP BY detalle_pedido.metodo_pago ORDER BY Cantidad_pagos";
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
}
