package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.ClienteGastoResponse;
import com.example.tbd_lab1.DTO.TopClienteResponse;
import com.example.tbd_lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public TopClienteResponse getClienteWithMostSpending() {
		return userRepository.findClienteWithMostSpending();
	}

	public boolean eliminarCliente(Long id) {
		return userRepository.deleteCliente(id);
	}

	public List<ClienteGastoResponse> getAllClientsWithSpending() {
		return userRepository.findAllClientsWithSpending();
	}
}
