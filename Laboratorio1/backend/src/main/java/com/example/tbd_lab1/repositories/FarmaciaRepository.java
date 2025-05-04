package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.entities.CalificacionEntity;
import com.example.tbd_lab1.entities.FarmaciaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

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
}
