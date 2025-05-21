package com.example.tbd_lab1.repositories;

import com.example.tbd_lab1.entities.UserEntity;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Optional<UserEntity> findById(Long id) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, refresh_token, refresh_token_expiration FROM users WHERE id = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					new BeanPropertyRowMapper<>(UserEntity.class),
					id
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<UserEntity> findByUsername(String username) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, refresh_token, refresh_token_expiration FROM users WHERE username = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					new BeanPropertyRowMapper<>(UserEntity.class),
					username
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<UserEntity> findByEmail(String email) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, refresh_token, refresh_token_expiration FROM users WHERE email = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					new BeanPropertyRowMapper<>(UserEntity.class),
					email
			);
			return Optional.ofNullable(userEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<UserEntity> findByRefreshToken(String refreshToken) {
		String sql =
				"SELECT id, username, first_name, last_name, rut, password, email, refresh_token, refresh_token_expiration FROM users WHERE refresh_token = ?";
		try {
			UserEntity userEntity = jdbcTemplate.queryForObject(
					sql,
					new BeanPropertyRowMapper<>(UserEntity.class),
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
					"INSERT INTO users (username, first_name, last_name, rut, password, email, refresh_token, refresh_token_expiration) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
						ps.setString(7, userEntity.getRefreshToken());
						ps.setObject(8, userEntity.getLocation());
						if (userEntity.getRefreshTokenExpiration() != null) {
							ps.setLong(8, userEntity.getRefreshTokenExpiration());
						} else {
							ps.setNull(8, java.sql.Types.BIGINT);
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
						"no hay id luego intentando recuperar al usuario " +
								userEntity.getUsername()
				);
				// throw new org.springframework.dao.DataRetrievalFailureException("Failed to retrieve ID for user: " + user.getUsername());
			}
		} else {
			String sql =
					"UPDATE users SET username = ?, first_name = ?, last_name = ?, rut = ?, password = ?, email = ?, refresh_token = ?, refresh_token_expiration = ? WHERE id = ?";
			jdbcTemplate.update(
					sql,
					userEntity.getUsername(),
					userEntity.getFirstName(),
					userEntity.getLastName(),
					userEntity.getRut(),
					userEntity.getPassword(),
					userEntity.getEmail(),
					userEntity.getRefreshToken(),
					userEntity.getRefreshTokenExpiration(),
					userEntity.getId()
			);
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