package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.DistanciaTareaPromedioResponse;
import com.example.tbd_lab1.entities.UserEntity;
import com.example.tbd_lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<UserEntity> getById(Long id) {
		return userRepository.findById(id);
	}

	public boolean eliminarCliente(Long id) {
		return userRepository.deleteCliente(id);
	}

	public List<DistanciaTareaPromedioResponse> distanciaPromedioTareasByUser() {
		return userRepository.findDistanciaTareaPromedio();
	}

	public List<DistanciaTareaPromedioResponse> distanciaPromedioTareas(Long id) {
		List<DistanciaTareaPromedioResponse> usuariosDistancias = userRepository.findDistanciaTareaPromedio();
		return usuariosDistancias.stream()
				.filter(u -> Objects.equals(u.getIdUsuario(), id))
				.toList();
	}
}
