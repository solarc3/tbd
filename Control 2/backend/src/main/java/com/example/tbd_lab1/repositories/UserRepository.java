package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.DTO.DistanciaTareaPromedioResponse;
import com.example.tbd_lab1.entities.UserEntity;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.geom.Point;

import java.sql.Types;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;
	private final WKTReader reader = new WKTReader();

	private final RowMapper<UserEntity> userRowMapper = (rs, rowNum) -> {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(rs.getLong("id"));
		userEntity.setUsername(rs.getString("username"));
		userEntity.setFirstName(rs.getString("first_name"));
		userEntity.setLastName(rs.getString("last_name"));
		userEntity.setRut(rs.getString("rut"));
		userEntity.setEmail(rs.getString("email"));
		userEntity.setPassword(rs.getString("password"));
		userEntity.setRefreshToken(rs.getString("refresh_token"));
		userEntity.setRefreshTokenExpiration(rs.getLong("refresh_token_expiration"));

		// handle point data
		String wkt = rs.getString("location");
		if (wkt != null) {
            try {
                Geometry geom = reader.read(wkt);
				userEntity.setLocation((Point) geom);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

		return userEntity;
	};

	@Autowired
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Optional<UserEntity> findById(Long id) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, ST_AsText(location) AS location, refresh_token, refresh_token_expiration FROM users WHERE id = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					userRowMapper,
					id
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<UserEntity> findByUsername(String username) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, ST_AsText(location) AS location, refresh_token, refresh_token_expiration FROM users WHERE username = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					userRowMapper,
					username
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<UserEntity> findByEmail(String email) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, ST_AsText(location) AS location, refresh_token, refresh_token_expiration FROM users WHERE email = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					userRowMapper,
					email
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<UserEntity> findByRefreshToken(String refreshToken) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, ST_AsText(location) AS location, refresh_token, refresh_token_expiration FROM users WHERE refresh_token = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					userRowMapper,
					refreshToken
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public boolean existsByUsername(String username) {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
		// varargs: queryForObject(sql, requiredType, args...)
		Integer count = jdbcTemplate.queryForObject(
				sql,
				Integer.class,
				username
		);
		return count != null && count > 0;
	}

	// 4.- Promedio distancia tareas completadas ~ ubicación usuario
	public List<DistanciaTareaPromedioResponse> findDistanciaTareaPromedio() {
		try {
			String sql = """
			SELECT users.id,
			       CONCAT(users.first_name, ' ', users.last_name) AS nombre,
			       AVG(ST_Distance(sectores.area::geography, users.location::geography)) AS promedio_tareas
			FROM (SELECT id_usuario, id_sector
			      FROM tareas WHERE tareas.estado = 'COMPLETADA') AS tareas_completadas
			    INNER JOIN sectores ON id_sector = sectores.id
			    INNER JOIN users ON users.id = id_usuario
			GROUP BY users.id
			""";
			return jdbcTemplate.query(sql,
					(rs, rowNum) -> DistanciaTareaPromedioResponse.builder()
							.idUsuario(rs.getLong("id"))
							.distanciaPromedio(rs.getDouble("promedio_tareas"))
							.nombreUsuario(rs.getString("nombre")).build());

		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();

		} catch (Exception e) {
			System.err.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public boolean existsByEmail(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
		return count != null && count > 0;
	}

	public boolean existsByRut(String rut) {
		String sql = "SELECT COUNT(*) FROM users WHERE rut = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, rut);
		return count != null && count > 0;
	}

	public UserEntity save(UserEntity userEntity) {
		if (userEntity.getId() == null) {
			String sql =
					"INSERT INTO users (username, first_name, last_name, rut, password, email, location, refresh_token, refresh_token_expiration) VALUES (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326), ?, ?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(
					connection -> {
						PreparedStatement ps = connection.prepareStatement(
								sql,
								new String[] { "id" }
						);
						ps.setString(1, userEntity.getUsername());
						ps.setString(2, userEntity.getFirstName());
						ps.setString(3, userEntity.getLastName());
						ps.setString(4, userEntity.getRut());
						ps.setString(5, userEntity.getPassword());
						ps.setString(6, userEntity.getEmail());

						Point location = userEntity.getLocation();
						if (location != null) {
							// Pasasr como string WKT 
							ps.setString(7, location.toText()); 
						} else {
							ps.setNull(7, Types.VARCHAR); 
						}

						ps.setString(8, userEntity.getRefreshToken());
						if (userEntity.getRefreshTokenExpiration() != null) {
							ps.setLong(9, userEntity.getRefreshTokenExpiration());
						} else {
							ps.setNull(9, java.sql.Types.BIGINT);
						}
						return ps;
					},
					keyHolder
			);
			Number generatedId = keyHolder.getKey();
			if (generatedId != null) {
				userEntity.setId(generatedId.longValue());
			} else {
				System.err.println(
						"No se pudo generar ID para el usuario: " +
								userEntity.getUsername()
				);
			}
		} else {
			String sql =
					"UPDATE users SET username = ?, first_name = ?, last_name = ?, rut = ?, password = ?, email = ?, refresh_token = ?, refresh_token_expiration = ?, location = ST_GeomFromText(?, 4326) WHERE id = ?";

			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, userEntity.getUsername());
				ps.setString(2, userEntity.getFirstName());
				ps.setString(3, userEntity.getLastName());
				ps.setString(4, userEntity.getRut());
				ps.setString(5, userEntity.getPassword());
				ps.setString(6, userEntity.getEmail());
				ps.setString(7, userEntity.getRefreshToken());
				if (userEntity.getRefreshTokenExpiration() != null) {
					ps.setLong(8, userEntity.getRefreshTokenExpiration());
				} else {
					ps.setNull(8, Types.BIGINT);
				}

				Point location = userEntity.getLocation();
				if (location != null) {
					ps.setString(9, location.toText()); 
				} else {
					ps.setNull(9, Types.VARCHAR); 
				}
				ps.setLong(10, userEntity.getId());
				return ps;
			});
		}
		return userEntity;
	}

	public int updateRefreshToken(
			Long userId,
			String refreshToken,
			Long expiration
	) {
		String sql =
				"UPDATE users SET refresh_token = ?, refresh_token_expiration = ? WHERE id = ?";
		return jdbcTemplate.update(sql, refreshToken, expiration, userId);
	}

	public int clearRefreshToken(Long userId) {
		String sql =
				"UPDATE users SET refresh_token = NULL, refresh_token_expiration = NULL WHERE id = ?";
		return jdbcTemplate.update(sql, userId);
	}
	public boolean deleteCliente(Long id) {
		String sql = "DELETE FROM users WHERE id = ?";
		Object[] args = new Object[] { id };
		return jdbcTemplate.update(sql, args) == 1;
	}

}