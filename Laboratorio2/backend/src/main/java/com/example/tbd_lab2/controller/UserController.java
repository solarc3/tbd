package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.MessageResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteGastoResponse;
import com.example.tbd_lab2.DTO.cliente.TopClienteResponse;
import com.example.tbd_lab2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/topuser")
	public ResponseEntity<?> getTopClientBySpending() {
		TopClienteResponse topClienteResponse =
				userService.getClienteWithMostSpending();
		if (topClienteResponse == null) {
			return ResponseEntity.ok()
					.body(new MessageResponse("No hay clientes."));
		}
		return ResponseEntity.ok(topClienteResponse);
	}

	@DeleteMapping("/{id}/eliminar")
	public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
		try {
			userService.eliminarCliente(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.ok()
				.body(new MessageResponse("No se pudo borrar el usuario"));
		}
	}

	@GetMapping("/clientes-gasto")
	public ResponseEntity<?> getAllClientsWithSpending() {
		List<ClienteGastoResponse> clientes =
				userService.getAllClientsWithSpending();

		if (clientes.isEmpty()) {
			return ResponseEntity.ok()
					.body(new MessageResponse("No hay clientes registrados."));
		}

		return ResponseEntity.ok(clientes);
	}
}
