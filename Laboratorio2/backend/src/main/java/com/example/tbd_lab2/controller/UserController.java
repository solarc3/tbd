package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.MessageResponse;
import com.example.tbd_lab2.DTO.auth.UserInfoResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteGastoResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteLejanoDeFarmaciaResponse;
import com.example.tbd_lab2.DTO.cliente.ClienteZonaCoberturaDTO;
import com.example.tbd_lab2.DTO.cliente.TopClienteResponse;
import com.example.tbd_lab2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@GetMapping("/")
	public ResponseEntity<?> getAll() {
		List<UserInfoResponse> users = userService.getAllUsers();
		if (users.isEmpty()) {
			return ResponseEntity.ok().body(new MessageResponse("No hay clientes registrados."));
		}

		return ResponseEntity.ok(users);
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

	// controllers lab 2
	@Operation(summary = "[LAB 2] Obtener zonas de cobertura por ID de Usuario", description = "Implementacion de query 'determinar si un cliente se encuentra dentro de una zona de cobertura'")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Se encontró al usuario y se mostrará la zona en la que se encuentra.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteZonaCoberturaDTO.class))),
			@ApiResponse(responseCode = "404", description = "Coverage zone not found for the client or client does not exist (custom message)",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
	})
	@GetMapping("/zona-cobertura/{id}")
	public ResponseEntity<?> getZonaCoberturaByCliente(@PathVariable Long id) {
		ClienteZonaCoberturaDTO clienteZonaCobertura = userService.getZonaCoberturaByCliente(id);
		if (clienteZonaCobertura == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse("No se encontró zona de cobertura para el cliente con ID: " + id + " o el cliente no existe."));
		}
		return ResponseEntity.ok(clienteZonaCobertura);
	}

	@Operation(summary = "[LAB 2] Obtener clientes más alejados de TODAS las farmacias", description = "Implementación de query 6.- \"Determinar los clientes que están a más de 5km de cualquier empresa o farmacia.\"")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Coleccion de clientes que cumplan esto, además del nombre y distancia [km] hacia su farmacia más cercana.",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteLejanoDeFarmaciaResponse.class)))
	})
	@GetMapping("/mas-lejanos-farmacia/{radiusKm}")
	public ResponseEntity<?> getClientesLejanosDeFarmacia(@PathVariable("radiusKm") @Parameter(
			name = "radiusKm",
			description = "Distancia [km] mínima de separación cliente - farmacia",
			example = "2.5") Double radiusKm) {
		List<ClienteLejanoDeFarmaciaResponse> clientes = userService.getClientesLejanosDeFarmacia(radiusKm);
		return ResponseEntity.ok(clientes);
	}
}
