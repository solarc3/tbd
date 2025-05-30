package com.example.tbd_lab2.services;

import com.example.tbd_lab2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean eliminarCliente(Long id) {
		return userRepository.deleteCliente(id);
	}
}
