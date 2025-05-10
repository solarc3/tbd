package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab1.DTO.RankingFarmaciaPedidoResponse;
import com.example.tbd_lab1.entities.CalificacionEntity;
import com.example.tbd_lab1.entities.FarmaciaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FarmaciaRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FarmaciaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<FarmaciaEntity> findById(Long id) {
        String sql = "SELECT id_farmacia, nombre_farmacia, direccion FROM farmacia WHERE id_farmacia = ?";
        try {
            FarmaciaEntity farmacia = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(FarmaciaEntity.class), id);
            return Optional.ofNullable(farmacia);
        }
        catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public List<FarmaciaPedidoFallidoResponse> listFarmaciaByCantPedidoCancelado() {
        try {
            String sql = "Select farmacia.nombre_farmacia,pedido.id_farmacia, count(pedido.estado_pedido) as cant_pedido_fallido FROM pedido RIGHT JOIN farmacia ON pedido.id_farmacia = farmacia.id_farmacia WHERE pedido.estado_pedido = 'CANCELADO'\n" +
                    "group by pedido.id_farmacia, farmacia.nombre_farmacia";
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    FarmaciaPedidoFallidoResponse.builder()
                            .cantidad(rs.getInt("cant_pedido_fallido"))
                            .nombreFarmacia(rs.getString("nombre_farmacia"))
                            .idFarmacia(rs.getLong("id_farmacia")).build());

        } catch (EmptyResultDataAccessException e) {
            System.out.println("No results found: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<RankingFarmaciaPedidoResponse> obtenerFarmaciaRankingPedido(){
        try {
            String sql = "select farmacia.nombre_farmacia as farmacia, count(pedido.id_farmacia) as pedidos_entregados FROM farmacia RIGHT JOIN public.farmacia_repartidor fr on farmacia.id_farmacia = fr.id_farmacia Join pedido  ON farmacia.id_farmacia = pedido.id_farmacia  GROUP BY farmacia.nombre_farmacia ORDER BY pedidos_entregados DESC";
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    RankingFarmaciaPedidoResponse.builder()
                            .nombreFarmacia(rs.getString("farmacia"))
                            .cantPedidosEntregados(rs.getInt("pedidos_entregados")).build());
        }catch (EmptyResultDataAccessException e) {
            System.out.println("No results found: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<FarmaciaEntity> findAll() {
        String sql = "SELECT id_farmacia, nombre_farmacia, direccion FROM farmacia";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FarmaciaEntity.class));
    }
}
