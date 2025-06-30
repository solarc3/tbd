package com.example.tbd_lab2.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;

@Repository
public class ProductoFarmaciaRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public ProductoFarmaciaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean descontarStock(Long idProducto, Long idFarmacia, Integer cantPedido) {
        try {
            // params del procedimiento
            List<SqlParameter> params = List.of(
                    new SqlParameter("id_productoN", Types.BIGINT),
                    new SqlParameter("id_FarmaciaN", Types.BIGINT),
                    new SqlParameter("cant_pedido", Types.INTEGER)
            );

            jdbcTemplate.call(con -> {
                CallableStatement callableStatement = con.prepareCall("CALL actualizarStock(?, ?, ?)");
                callableStatement.setLong(1, idProducto);
                callableStatement.setLong(2, idFarmacia);
                callableStatement.setInt(3, cantPedido);
                return callableStatement;
            }, params);
            return true;
        } catch (DataAccessException e) {
            System.err.println("Error al descontar stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
