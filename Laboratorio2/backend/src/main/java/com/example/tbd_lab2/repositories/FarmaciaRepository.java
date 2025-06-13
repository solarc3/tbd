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
        WKTReader reader = new WKTReader(); // Initialize WKTReader
        try {
            String sql = "WITH FarmaciaDistancias AS (\n" +
                    "    SELECT\n" +
                    "        farm.nombre_farmacia,\n" +
                    "        CONCAT(usuarios.first_name, ' ', usuarios.last_name) AS nombre_usuario,\n" +
                    "        ST_Distance(usuarios.location::geography, farm.ubicacion::geography) AS distancia,\n" +
                    // Request geometry as WKT
                    "        ST_AsText(farm.ubicacion) AS ubicacion_farmacia_wkt,\n" +
                    "        ST_AsText(usuarios.location) AS ubicacion_usuario_wkt,\n" +
                    "        ROW_NUMBER() OVER (PARTITION BY farm.id_farmacia ORDER BY ST_Distance(usuarios.location::geography, farm.ubicacion::geography) DESC) as rn\n" +
                    "    FROM\n" +
                    "        pedido\n" +
                    "            INNER JOIN\n" +
                    "        users AS usuarios ON usuarios.id = pedido.id_cliente\n" +
                    "            INNER JOIN\n" +
                    "        farmacia AS farm ON farm.id_farmacia = pedido.id_farmacia\n" +
                    ")\n" +
                    "SELECT\n" +
                    "    nombre_farmacia,\n" +
                    "    nombre_usuario,\n" +
                    "    distancia AS entrega_lejana,\n" +
                    // Select the WKT strings
                    "    ubicacion_farmacia_wkt,\n" +
                    "    ubicacion_usuario_wkt\n" +
                    "FROM\n" +
                    "    FarmaciaDistancias\n" +
                    "WHERE\n" +
                    "    rn = 1\n" +
                    "ORDER BY\n" +
                    "    entrega_lejana DESC;";
            return jdbcTemplate.query(sql,(rs,RowNum)-> {
                Point ubicacionFarmaciaPoint = null;
                String farmaciaWkt = rs.getString("ubicacion_farmacia_wkt");
                if (farmaciaWkt != null && !farmaciaWkt.isEmpty()) {
                    try {
                        Geometry farmaciaGeom = reader.read(farmaciaWkt);
                        if (farmaciaGeom instanceof Point) {
                            ubicacionFarmaciaPoint = (Point) farmaciaGeom;
                        }
                    } catch (ParseException e) {
                        // Log or handle parsing error, e.g., throw a runtime exception or return null
                        System.err.println("Error parsing ubicacion_farmacia_wkt: " + farmaciaWkt + " - " + e.getMessage());
                    }
                }

                Point ubicacionUsuarioPoint = null;
                String usuarioWkt = rs.getString("ubicacion_usuario_wkt");
                if (usuarioWkt != null && !usuarioWkt.isEmpty()) {
                    try {
                        Geometry usuarioGeom = reader.read(usuarioWkt);
                        if (usuarioGeom instanceof Point) {
                            ubicacionUsuarioPoint = (Point) usuarioGeom;
                        }
                    } catch (ParseException e) {
                        // Log or handle parsing error
                        System.err.println("Error parsing ubicacion_usuario_wkt: " + usuarioWkt + " - " + e.getMessage());
                    }
                }

                return FarmaciaPuntoEntregaLejanaResponse.builder()
                        .distanciaEntrega(rs.getDouble("entrega_lejana"))
                        .nombreFarmacia(rs.getString("nombre_farmacia"))
                        .ubicacionEntrega(ubicacionUsuarioPoint)
                        .ubicacionFarmacia(ubicacionFarmaciaPoint)
                        .nombreUsuario(rs.getString("nombre_usuario")).build();
            });
        } catch(EmptyResultDataAccessException e){
            return new ArrayList<>();
        }catch (Exception e) { // Catching general Exception can hide specific issues.
            // Consider more specific catches if possible.
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
