package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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



}
