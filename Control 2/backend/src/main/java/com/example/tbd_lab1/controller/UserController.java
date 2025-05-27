package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.DistanciaTareaPromedioResponse;
import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.entities.UserEntity;
import com.example.tbd_lab1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
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

	@GetMapping("/promedio-distancia-completadas")
	public ResponseEntity<?> distanciaTareasPromedio() {
		List<DistanciaTareaPromedioResponse> usuariosDistancias =
				userService.distanciaPromedioTareasByUser();

		if (usuariosDistancias.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(usuariosDistancias);
	}

	@GetMapping("/promedio-distancia-completadas/{userId}")
	public ResponseEntity<?> distanciaTareasPromedio(@PathVariable Long userId) {
		List<DistanciaTareaPromedioResponse> usuariosDistancias =
				userService.distanciaPromedioTareas(userId);

		return ResponseEntity.ok(usuariosDistancias);
	}
}
