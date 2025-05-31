package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.DTO.farmacia.FarmaciaClosestDeliveryResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPuntoEntregaLejanaResponse;
import com.example.tbd_lab2.DTO.farmacia.RankingFarmaciaPedidoResponse;
import com.example.tbd_lab2.entities.FarmaciaEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FarmaciaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final WKTReader reader = new WKTReader();

    private final RowMapper<FarmaciaEntity> farmaciaRowMapper = (rs, rowNum) -> {
        FarmaciaEntity farmaciaEntity = FarmaciaEntity.builder()
                .idFarmacia(rs.getLong("id_farmacia"))
                .nombreFarmacia(rs.getString("nombre_farmacia"))
                .direccion(rs.getString("direccion"))
                .build();

        // handle point data
        String wkt = rs.getString("ubicacion");
        if (wkt != null && !wkt.isEmpty()) {
            try {
                Geometry geom = reader.read(wkt);
                farmaciaEntity.setUbicacion((Point) geom);
            } catch (ParseException e) {
                System.out.println("====> Error parsing ubicacion = " + wkt);
                throw new RuntimeException(e);
            }
        }
        return farmaciaEntity;
    };

    @Autowired
    public FarmaciaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<FarmaciaEntity> findById(Long id) {
        String sql = "SELECT id_farmacia, nombre_farmacia, direccion, ST_AsText(ubicacion) AS ubicacion FROM farmacia WHERE id_farmacia = ?";
        try {
            FarmaciaEntity farmacia = jdbcTemplate.queryForObject(sql, farmaciaRowMapper, id);
            return Optional.ofNullable(farmacia);
        }
        catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }
    public List<FarmaciaClosestDeliveryResponse> listUsersFarmaciaClosestDelivery(Long id){
        try {
            String sql = "SELECT concat(usuarios.first_name,' ',usuarios.last_name) AS nombre_usuario ,st_distance(usuarios.location::geography,farm.ubicacion::geography) AS distancia FROM (SELECT pedido.id_farmacia, pedido.id_cliente FROM pedido WHERE pedido.id_farmacia = ?) AS usuariofarmacia INNER JOIN users AS usuarios ON usuarios.id = usuariofarmacia.id_cliente INNER JOIN farmacia AS farm ON farm.id_farmacia = usuariofarmacia.id_farmacia group by usuarios.location, farm.ubicacion,nombre_usuario ORDER BY distancia ASC";
                    return jdbcTemplate.query(sql,(rs,rowNum )-> FarmaciaClosestDeliveryResponse.builder()
                            .distanciaEntrega(rs.getDouble("distancia"))
                            .nombreUsuario(rs.getString("nombre_usuario"))
                            .build(),id);
            }catch(EmptyResultDataAccessException e){
                return new ArrayList<>();
            }catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
    }
    public List<FarmaciaPuntoEntregaLejanaResponse> listbyFarmaciaFurthestPoint(){
        try {
            String sql = "select nombre_farmacia, max(distancia) as entrega_lejana from (SELECT farm.id_farmacia ,farm.nombre_farmacia, concat(usuarios.first_name,' ',usuarios.last_name) AS nombre_usuario ,st_distance(usuarios.location::geography,farm.ubicacion::geography) AS distancia FROM (SELECT pedido.id_farmacia, pedido.id_cliente FROM pedido) AS usuariofarmacia INNER JOIN users AS usuarios ON usuarios.id = usuariofarmacia.id_cliente INNER JOIN farmacia AS farm ON farm.id_farmacia = usuariofarmacia.id_farmacia group by farm.id_farmacia, farm.nombre_farmacia ,usuarios.location, farm.ubicacion,nombre_usuario ORDER BY id_farmacia, distancia DESC) as distacia_entregas group by nombre_farmacia";
            return jdbcTemplate.query(sql,(rs,RowNum)-> FarmaciaPuntoEntregaLejanaResponse.builder()
                    .distanciaEntrega(rs.getDouble("entrega_lejana"))
                    .nombreFarmacia(rs.getString("nombre_farmacia"))
                    .build());
        } catch(EmptyResultDataAccessException e){
            return new ArrayList<>();
        }catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
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
        String sql = "SELECT id_farmacia, nombre_farmacia, direccion, ST_AsText(ubicacion) AS ubicacion FROM farmacia";
        return jdbcTemplate.query(sql, farmaciaRowMapper);
    }
}
