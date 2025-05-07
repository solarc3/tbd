package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.entities.FarmaciaEntity;
import com.example.tbd_lab1.entities.PedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class PedidoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public List<PagoMasUsadoUrgenteResponse> findMostUsedPaymentMethodWhenUrgent(){
        try{
            String sql = "Select count(detalle_pedido.metodo_pago) as Cantidad_pagos, metodo_pago FROM pedido RIGHT JOIN detalle_pedido ON pedido.id_pedido = detalle_pedido.id_pedido WHERE pedido.es_urgente = 'true'\n" +
                    "GROUP BY detalle_pedido.metodo_pago ORDER BY Cantidad_pagos DESC limit 1";
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
}
