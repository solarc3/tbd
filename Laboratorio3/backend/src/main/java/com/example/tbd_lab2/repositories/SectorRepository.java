package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.entities.SectorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class SectorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<SectorEntity> findAll() {
        String sql = "SELECT id, nombre_sector, ST_AsGeoJSON(area) AS area FROM sectores ORDER BY id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SectorEntity.class));
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
}