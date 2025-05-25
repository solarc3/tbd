package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.TareaCercanaDTO;
import com.example.tbd_lab1.DTO.TareaCountBySectorDTO;
import com.example.tbd_lab1.DTO.TareaVencimientoDTO;
import com.example.tbd_lab1.entities.TareaEntity;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.sql.PreparedStatement;
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
        tarea.setIdSector(rs.getLong("id_sector"));


        if (rs.getObject("id_usuario") == null) {
            tarea.setIdUsuario(null);
        } else {
            tarea.setIdUsuario(rs.getLong("id_usuario"));
        }
        tarea.setEstado(rs.getString("estado"));

        if (rs.getObject("id_sector") == null) {
            tarea.setIdSector(null);
        } else {
            tarea.setIdSector(rs.getLong("id_sector"));
        }
        return tarea;
    };

    public Optional<TareaEntity> findById(Long id) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado FROM tareas WHERE id = ?";
        try {
            List<TareaEntity> results = jdbcTemplate.query(sql, tareaRowMapper, id);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<TareaEntity> findAll() {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado FROM tareas";
        return jdbcTemplate.query(sql, tareaRowMapper);
    }

    public List<TareaEntity> findByUsuario(Long idUsuario) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado FROM tareas WHERE id_usuario = ?";
        return jdbcTemplate.query(sql, tareaRowMapper, idUsuario);
    }

    public List<TareaEntity> findByEstado(String estado) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado FROM tareas WHERE estado = ?";
        return jdbcTemplate.query(sql, tareaRowMapper, estado);
    }

    public List<TareaEntity> findByIdSector(Long idSector) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado FROM tareas WHERE id_sector = ?";
        return jdbcTemplate.query(sql, tareaRowMapper, idSector);
    }

    // Apartado de notificaciones
    public List<TareaVencimientoDTO> findTareasPorVencerHoyByUsuario(Long idUsuario) {
        String sql = "SELECT id, titulo, descripcion, fecha_vencimiento, id_usuario, estado, id_sector " +
                "FROM tareas " +
                "WHERE id_usuario = ? AND DATE(fecha_vencimiento) = CURRENT_DATE";

        List<TareaEntity> tareas = jdbcTemplate.query(sql, tareaRowMapper, idUsuario);
        List<TareaVencimientoDTO> resultado = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (TareaEntity tarea : tareas) {
            long horas = 0;
            long minutos = 0;
            long segundos = 0;

            // Si tiene fecha de vencimiento
            if (tarea.getFechaVencimiento() != null) {
                // Y si es hoy LocalDateTime.now(), entonces
                if (tarea.getFechaVencimiento().isAfter(ahora)) {
                    Duration duracion = Duration.between(ahora, tarea.getFechaVencimiento());
                    horas = duracion.toHours();
                    minutos = duracion.toMinutesPart();
                    segundos = duracion.toSecondsPart();
                }

            }
            // queda marcado cómo notificación
            resultado.add(new TareaVencimientoDTO(tarea, horas, minutos, segundos));
        }
        return resultado;
    }

    public TareaEntity save(TareaEntity tareaEntity) {
        if (tareaEntity.getId() == null) {
            String sql = "INSERT INTO tareas (titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado) VALUES (?, ?, ?, ?, ?, ?)";
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

                        ps.setString(6, tareaEntity.getEstado());

                        // Handle Point data for sector
                        if (tareaEntity.getIdSector() != null) {
                            ps.setLong(5, tareaEntity.getIdSector());
                        } else {
                            ps.setNull(5, java.sql.Types.OTHER);
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
            String sql = "UPDATE tareas SET titulo = ?, descripcion = ?, fecha_vencimiento = ?, id_usuario = ?, id_sector = ?, estado = ? WHERE id = ?";
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

                        ps.setString(6, tareaEntity.getEstado());

                        // Handle Point data for sector
                        if (tareaEntity.getIdSector() != null) {
                            ps.setLong(5, tareaEntity.getIdSector());
                        } else {
                            ps.setNull(5, java.sql.Types.OTHER);
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

    // Inicio queries
    // 1.- Obtener tareas por sector dado un usuario
    public List<TareaCountBySectorDTO> countTareasByUsuarioAndSector(Long idUsuario) {
        String sql = "SELECT s.id AS id_sector, s.nombre, COUNT(t.id) AS cantidad_tareas " +
                "FROM tareas t " +
                "JOIN sector s ON t.id_sector = s.id " +
                "WHERE t.id_usuario = ? " +
                "GROUP BY s.id, s.nombre " +
                "ORDER BY s.nombre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TareaCountBySectorDTO dto = new TareaCountBySectorDTO();
            dto.setIdSector(rs.getLong("id_sector"));
            dto.setNombreSector(rs.getString("nombre"));
            dto.setCantidadTareas(rs.getLong("cantidad_tareas"));
            return dto;
        }, idUsuario);
    }

    // 2.-
    public List<TareaCercanaDTO> findTareaPendienteMasCercana(Long idUsuario) {
        String sql = "SELECT " +
                "    t.id AS id_tarea, " +
                "    t.titulo AS titulo_tarea, " +
                "    t.descripcion AS descripcion_tarea, " +
                "    t.fecha_vencimiento, " +
                "    t.estado AS estado_tarea, " +
                "    s.id AS id_sector, " +
                "    s.nombre, " +
                "    ST_Distance(u.location::geography, s.area::geography) AS distancia_al_sector_metros " +
                "FROM " +
                "    tareas t " +
                "INNER JOIN " +
                "    sector s ON t.id_sector = s.id " +
                "INNER JOIN " +
                "    users u ON t.id_usuario = u.id " +
                "WHERE " +
                "    t.id_usuario = ? " +
                "    AND t.estado = 'PENDIENTE' " +
                "    AND u.location IS NOT NULL " +
                "    AND s.area IS NOT NULL " +
                "ORDER BY " +
                "    distancia_al_sector_metros ASC, t.fecha_vencimiento ASC " +
                "LIMIT 1;";

        List<TareaCercanaDTO> results = jdbcTemplate.query(sql, (rs, rowNum) -> new TareaCercanaDTO(
                rs.getLong("id_tarea"),
                rs.getString("titulo_tarea"),
                rs.getString("descripcion_tarea"),
                rs.getTimestamp("fecha_vencimiento"),
                rs.getString("estado_tarea"),
                rs.getLong("id_sector"),
                rs.getString("nombre"),
                rs.getDouble("distancia_al_sector_metros")
        ), idUsuario);
        return results;
        // return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}