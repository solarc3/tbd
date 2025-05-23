package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.SectorTareasResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.example.tbd_lab1.entities.SectorEntity;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SectorRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // query 1 wip :)
    public List<SectorEntity> findByCompletedTasksWithin(BigDecimal radius) {
        return List.of();
    }

    public List<SectorEntity> findAll() {
        String sql = "SELECT id, nombre_sector, ST_AsGeoJSON(area) AS area FROM sectores ORDER BY id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SectorEntity.class));
    }

    public List<SectorTareasResponse> findTareasPendientesBySector(){
        String sql = "select sectores.nombre_sector, count(tareas.id_sector) as cantidad_tareas from tareas left join sectores on sectores.id = tareas.id_sector where tareas.estado = 'PENDIENTE' group by sectores.nombre_sector order by cantidad_tareas desc";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> SectorTareasResponse.builder()
                    .cantidad_tareas(rs.getInt("cantidad_tareas"))
                    .nombre_sector(rs.getString("nombre_sector")).build());
        }catch(EmptyResultDataAccessException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<SectorEntity> findById(Long id) {
        String sql = "SELECT id, nombre_sector, ST_AsGeoJSON(area) AS area FROM sectores WHERE id = ?";
        try {
            SectorEntity sectorEntity = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(SectorEntity.class),
                    id
            );
            return Optional.ofNullable(sectorEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public SectorEntity save(SectorEntity sectorEntity) {
        if (sectorEntity.getId() == null) {
            String sql = "INSERT INTO sectores (nombre_sector, area) VALUES (?, ST_GeomFromGeoJSON(?))";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        sql, new String[]{"id"}
                );
                ps.setString(1, sectorEntity.getNombreSector());
                ps.setObject(2, sectorEntity.getArea());
                return ps;
            }, keyHolder);

            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                sectorEntity.setId(generatedId.longValue());
            } else {
                System.err.println(
                        "no hay id para sector " + sectorEntity.getNombreSector()
                );
            }
        } else {
            String sql = "UPDATE sectores SET nombre_sector = ?, area = ST_GeomFromGeoJSON(?) WHERE id = ?";
            jdbcTemplate.update(sql,
                    sectorEntity.getNombreSector(),
                    sectorEntity.getArea(),
                    sectorEntity.getId()
            );
        }
        return sectorEntity;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM sectores WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM sectores WHERE id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.update(sql, args) == 1;
    }
}
