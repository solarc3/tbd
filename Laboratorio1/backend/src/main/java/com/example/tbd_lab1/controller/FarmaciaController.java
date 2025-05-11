package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.DTO.RankingFarmaciaPedidoResponse;
import com.example.tbd_lab1.DTO.TopProductosPorCategoriaResponse;
import com.example.tbd_lab1.entities.FarmaciaEntity;
import com.example.tbd_lab1.repositories.FarmaciaRepository;
import com.example.tbd_lab1.services.FarmaciaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farmacia")
public class FarmaciaController {

	private final FarmaciaService farmaciaService;

	@Autowired
	public FarmaciaController(FarmaciaService farmaciaService) {
		this.farmaciaService = farmaciaService;
	}

	@GetMapping("/pedidofallado")
	public ResponseEntity<?> failedPedido() {
		List<FarmaciaPedidoFallidoResponse> farmacias =
			farmaciaService.getFarmaciasWithMostCancelledPedidos();

		if (farmacias.isEmpty()) {
			return ResponseEntity.ok()
				.body(
					new MessageResponse(
						"No existen farmacias con pedidos cancelados"
					)
				);
		}
		return ResponseEntity.ok(farmacias);
	}

	@GetMapping("/ranking")
	public ResponseEntity<?> ranking() {
		List<RankingFarmaciaPedidoResponse> ranking =
			farmaciaService.rankingFarmaciaPedidos();
		if (ranking.isEmpty()) {
			return ResponseEntity.ok()
				.body(new MessageResponse("No es posible generar un Ranking"));
		}
		return ResponseEntity.ok(ranking);
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllFarmacias() {
		List<FarmaciaEntity> farmacias = farmaciaService.getAllFarmacias();

		if (farmacias.isEmpty()) {
			return ResponseEntity.ok()
				.body(new MessageResponse("No hay farmacias disponibles."));
		}

		return ResponseEntity.ok(farmacias);
	}
}
