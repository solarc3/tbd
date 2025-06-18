package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.DTO.auth.UserInfoResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteGastoResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteLejanoDeFarmaciaResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteZonaCoberturaDTO;
import com.example.tbd_lab2.DTO.cliente.TopClienteResponse;
import com.example.tbd_lab2.entities.FarmaciaEntity;
import com.example.tbd_lab2.entities.UserEntity;
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

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	// Inicio querys
	// Buscar cliente que haya gastado mas

	public List<UserInfoResponse> getAll() {
		try {
			String sql =
				"SELECT id, username, first_name, last_name, rut, email, ST_AsText(location) AS location FROM users";

			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				UserInfoResponse userInfoResponse = new UserInfoResponse();
				userInfoResponse.setId(rs.getLong("id"));
				userInfoResponse.setUsername(rs.getString("username"));
				userInfoResponse.setFirstName(rs.getString("first_name"));
				userInfoResponse.setLastName(rs.getString("last_name"));
				userInfoResponse.setRut(rs.getString("rut"));
				userInfoResponse.setEmail(rs.getString("email"));
				String wkt = rs.getString("location");
				if (wkt != null) {
					try {
						Geometry geom = reader.read(wkt);
						userInfoResponse.setLocation((Point) geom);
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
				}

				return userInfoResponse;
			});
		}
		catch (EmptyResultDataAccessException e) {
			System.out.println("No se pudieron recuperar los usuarios");
			return null;
		}
	}
	public TopClienteResponse findClienteWithMostSpending() {
		try {
			String sql =
					"SELECT u.id, u.username, SUM(p.monto) as total_gastado " +
							"FROM users u " +
							"JOIN pedido p ON u.id = p.id_cliente " +
							"WHERE p.estado_pedido::text = 'ENTREGADO' " +
							"GROUP BY u.id, u.username " +
							"ORDER BY total_gastado DESC " +
							"LIMIT 1";

			Map<String, Object> result = jdbcTemplate.queryForMap(sql);
            return TopClienteResponse.builder()
                    .id(((Number) result.get("id")).longValue())
                    .username((String) result.get("username"))
                    .totalGastado(
                            ((Number) result.get("total_gastado")).intValue()
                    )
                    .build();

        } catch (EmptyResultDataAccessException e) {
			System.out.println("No results found");
			return null;

		} catch (Exception e) {
			System.err.println("Error con query: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<ClienteGastoResponse> findAllClientsWithSpending() {
		try {
			String sql =
					"SELECT u.id, u.username, u.email, COALESCE(SUM(p.monto), 0) as total_gastado " +
							"FROM users u " +
							"LEFT JOIN pedido p ON u.id = p.id_cliente " +
							"GROUP BY u.id, u.username, u.email " +
							"ORDER BY u.username";

			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				ClienteGastoResponse cliente = new ClienteGastoResponse();
				cliente.setId(rs.getLong("id"));
				cliente.setUsername(rs.getString("username"));
				cliente.setEmail(rs.getString("email"));
				cliente.setTotalGastado(rs.getInt("total_gastado"));
				return cliente;
			});
		} catch (Exception e) {
			System.err.println(
					"Error retrieving clients with spending: " + e.getMessage()
			);
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	// Queries Laboratorio 2
	// 2. Determinar si un cliente se encuentra dentro de una zona de cobertura.
	public ClienteZonaCoberturaDTO findByZonaCobertura(Long id_cliente) {
		try{
			String sql =
				"SELECT u.id, u.first_name, u.last_name, s.nombre_sector, " +
				"ST_Y(ST_Centroid(s.area)) as latitud, ST_X(ST_Centroid(s.area)) as longitud\n" +
				"FROM users as u, sectores as s\n" +
				"WHERE u.id = ? AND st_within(u.location, s.area)";
			return jdbcTemplate.queryForObject(sql, new Object[]{id_cliente}, (rs, rowNum) ->
					new ClienteZonaCoberturaDTO(
							rs.getLong("id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("nombre_sector"),
							rs.getDouble("latitud"),
							rs.getDouble("longitud")
					)
			);
		} catch (EmptyResultDataAccessException e) {
			System.out.println("No se encontró zona de cobertura para el cliente con id: " + id_cliente);
			return null; // O puedes retornar un DTO vacío o lanzar una excepción personalizada
		}
		catch(Exception e){
			System.err.println(
					"Error obteniendo zona de cobertura del usuario... " + e.getMessage()
			);
			e.printStackTrace();
			// Considerar lanzar una excepción personalizada aquí también
			return null;
		}
	}

	// 6. Determinar los clientes que están a más de ?km de cualquier empresa o farmacia
	public List<ClienteLejanoDeFarmaciaResponse> findAllLejanoDeFarmacia(Double radiusMeters) {
		String sql = """
				WITH farmacia_mas_cercana AS (
				    SELECT DISTINCT ON (users.id)
				        users.id AS id_cliente,
				        ST_AsText(users.location) as ubicacion_cliente,
				        farmacia.nombre_farmacia,
				        farmacia.id_farmacia,
				        farmacia.direccion,
				        ST_AsText(farmacia.ubicacion) as ubicacion_farmacia,
				        (ST_Distance(location::geography, farmacia.ubicacion::geography) / 1000) AS distancia_km
				    FROM users
				    CROSS JOIN farmacia
				    ORDER BY id_cliente, distancia_km
				)
				SELECT id, first_name, last_name, ubicacion_cliente, nombre_farmacia,
				       id_farmacia, direccion, ubicacion_farmacia, distancia_km
				FROM users
				INNER JOIN farmacia_mas_cercana ON id = id_cliente
				WHERE NOT ST_Intersects(location, ST_Collect(
				        ARRAY(SELECT ST_Buffer(ubicacion::geography, ?)::geometry FROM farmacia))
				);
				""";
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> {
					String nombreCliente = rs.getString("first_name") + ' ' + rs.getString("last_name");
					ClienteLejanoDeFarmaciaResponse response = ClienteLejanoDeFarmaciaResponse.builder()
							.idCliente(rs.getLong("id"))
							.nombreCliente(nombreCliente)
							.distanciaKm(rs.getDouble("distancia_km"))
							.build();

					String wkt = rs.getString("ubicacion_cliente");
					if (wkt != null) {
						try {
							Geometry geom = reader.read(wkt);
							response.setUbicacionCliente((Point) geom);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
					}

					FarmaciaEntity farmacia = FarmaciaEntity.builder()
						.idFarmacia(rs.getLong("id_farmacia"))
						.nombreFarmacia(rs.getString("nombre_farmacia"))
						.direccion(rs.getString("direccion"))
						.build();

					String wkt2 = rs.getString("ubicacion_farmacia");
					if (wkt2 != null) {
						try {
							Geometry geom = reader.read(wkt2);
							farmacia.setUbicacion((Point) geom);
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
					}
					response.setFarmacia(farmacia);
					return response;
				},
				radiusMeters);

	}
}