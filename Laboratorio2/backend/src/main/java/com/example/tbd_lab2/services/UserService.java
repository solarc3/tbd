package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.cliente.ClienteGastoResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteZonaCoberturaDTO;
import com.example.tbd_lab2.DTO.cliente.TopClienteResponse;
import com.example.tbd_lab2.repositories.UserRepository;
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

	// servicios lab 2
	public ClienteZonaCoberturaDTO getZonaCoberturaByCliente(Long id_cliente) {
		return userRepository.findByZonaCobertura(id_cliente);
	}}
