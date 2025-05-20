package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.entities.TareaEntity;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class TareaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final WKTReader wktReader = new WKTReader();

    @Autowired
    public TareaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TareaEntity> tareaRowMapper = (rs, rowNum) -> {
        TareaEntity tarea = new TareaEntity();
        tarea.setId(rs.getLong("id"));
        tarea.setTitulo(rs.getString("titulo"));
        tarea.setDescripcion(rs.getString("descripcion"));
        tarea.setFechaVencimiento(rs.getTimestamp("fecha_vencimiento") != null ?
                rs.getTimestamp("fecha_vencimiento").toLocalDateTime() : null);
        tarea.setIdUsuario(rs.getLong("id_usuario"));
        tarea.setEstado(rs.getString("estado"));

        // Handle Point data from PGgeometry
        try {
            Object pgObj = rs.getObject("sector");
            if (pgObj != null) {
                // Convert PostGIS geometry to WKT and then to JTS Point
                String wktText = pgObj.toString();
                if (wktText.startsWith("SRID=")) {
                    wktText = wktText.substring(wktText.indexOf(';') + 1);
                }
                tarea.setSector((Point) wktReader.read(wktText));
            }
        } catch (SQLException | ParseException e) {
            // Log error but continue
            System.err.println("Error parsing Point data: " + e.getMessage());
        }

        return tarea;
    };

    public Optional<TareaEntity> findById(Long id) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, estado, sector FROM tareas WHERE id = ?";
        try {
            List<TareaEntity> results = jdbcTemplate.query(sql, tareaRowMapper, id);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<TareaEntity> findAll() {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, estado, sector FROM tareas";
        return jdbcTemplate.query(sql, tareaRowMapper);
    }

    public List<TareaEntity> findByUsuario(Long idUsuario) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, estado, sector FROM tareas WHERE id_usuario = ?";
        return jdbcTemplate.query(sql, tareaRowMapper, idUsuario);
    }

    public List<TareaEntity> findByEstado(String estado) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, estado, sector FROM tareas WHERE estado = ?";
        return jdbcTemplate.query(sql, tareaRowMapper, estado);
    }

    public TareaEntity save(TareaEntity tareaEntity) {
        if (tareaEntity.getId() == null) {
            String sql = "INSERT INTO tareas (titulo, descripcion, fecha_vencimiento, id_usuario, estado, sector) VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(
                                sql,
                                new String[] { "id" }
                        );
                        ps.setString(1, tareaEntity.getTitulo());
                        ps.setString(2, tareaEntity.getDescripcion());

                        if (tareaEntity.getFechaVencimiento() != null) {
                            ps.setTimestamp(3, Timestamp.valueOf(tareaEntity.getFechaVencimiento()));
                        } else {
                            ps.setNull(3, java.sql.Types.TIMESTAMP);
                        }

                        if (tareaEntity.getIdUsuario() != null) {
                            ps.setLong(4, tareaEntity.getIdUsuario());
                        } else {
                            ps.setNull(4, java.sql.Types.BIGINT);
                        }

                        ps.setString(5, tareaEntity.getEstado());

                        // Handle Point data for sector
                        if (tareaEntity.getSector() != null) {
                            String wktPoint = "SRID=4326;" + tareaEntity.getSector().toText();
                            // Use PostgreSQL's ST_GeomFromText function to convert WKT to geometry
                            ps.setObject(6, connection.createArrayOf("geometry", new Object[]{wktPoint}));
                        } else {
                            ps.setNull(6, java.sql.Types.OTHER);
                        }

                        return ps;
                    },
                    keyHolder
            );

            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                tareaEntity.setId(generatedId.longValue());
            } else {
                System.err.println("No se pudo obtener el ID generado para la tarea: " + tareaEntity.getTitulo());
            }
        } else {
            String sql = "UPDATE tareas SET titulo = ?, descripcion = ?, fecha_vencimiento = ?, id_usuario = ?, estado = ?, sector = ? WHERE id = ?";
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, tareaEntity.getTitulo());
                        ps.setString(2, tareaEntity.getDescripcion());

                        if (tareaEntity.getFechaVencimiento() != null) {
                            ps.setTimestamp(3, Timestamp.valueOf(tareaEntity.getFechaVencimiento()));
                        } else {
                            ps.setNull(3, java.sql.Types.TIMESTAMP);
                        }

                        if (tareaEntity.getIdUsuario() != null) {
                            ps.setLong(4, tareaEntity.getIdUsuario());
                        } else {
                            ps.setNull(4, java.sql.Types.BIGINT);
                        }

                        ps.setString(5, tareaEntity.getEstado());

                        // Handle Point data for sector
                        if (tareaEntity.getSector() != null) {
                            String wktPoint = "SRID=4326;" + tareaEntity.getSector().toText();
                            ps.setObject(6, wktPoint);
                        } else {
                            ps.setNull(6, java.sql.Types.OTHER);
                        }

                        ps.setLong(7, tareaEntity.getId());
                        return ps;
                    }
            );
        }
        return tareaEntity;
    }

    public boolean deleteTarea(Long id) {
        String sql = "DELETE FROM tareas WHERE id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.update(sql, args) == 1;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM tareas WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}