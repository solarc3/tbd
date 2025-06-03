package com.example.tbd_lab2.controller;

import com.example.tbd_lab2.DTO.*;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaClosestDeliveryResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPuntoEntregaLejanaResponse;
import com.example.tbd_lab2.DTO.farmacia.RankingFarmaciaPedidoResponse;
import com.example.tbd_lab2.entities.FarmaciaEntity;
import com.example.tbd_lab2.services.FarmaciaService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	//Query 1 lab 2
	@Operation(summary = "Obtener la distancia de la entrega mas cercana a la farmacia y el usuario de quien hizo el pedido", description = "Query donde se entrega el id de la farmacia donde se desea saber los 5 pedidos mas cercanos que han realizado ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description="Se devuelven la distancia y usuarios de los cuales estan las 5 (o menos) entregas mas cercanas",
					content=@Content(mediaType = "application/json",schema = @Schema(implementation = FarmaciaClosestDeliveryResponse.class))),
			@ApiResponse(responseCode = "200",description = "No se encontraron usuarios y/o farmacias para realizar la query",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
	})
	@GetMapping("/entregas-cercanas/{id_farmacia}")
	public ResponseEntity<?> entregasCercana(@PathVariable Long id_farmacia) {
		List<FarmaciaClosestDeliveryResponse> usuarios = farmaciaService.getFarmaciasClosestDeliveryPoint(id_farmacia);
		if (usuarios.isEmpty()) {
			return ResponseEntity.ok().body(new MessageResponse("No existen usuarios a quienes les hicieron entregas"));
		}
		return ResponseEntity.ok(usuarios);
	}

	//Query 4 lab 2
	@Operation(summary = "Obtener la entrega mas lejana a la farmacia y el respectivo nombre de a cual pertenece", description = "Query donde se entrega la distancia del punto mas lejano de entrega de la farmacia, y el nombre del cual esta asociada la distancia")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description="Se devuelven la distancia y el nombre de la farmacia",
					content=@Content(mediaType = "application/json",schema = @Schema(implementation = FarmaciaPuntoEntregaLejanaResponse.class))),
			@ApiResponse(responseCode = "200",description = "No se encontraron pedidos y/o farmacias para realizar la query",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
	})
	@GetMapping("/entregas-mas-lejanas")
	public ResponseEntity<?> entregasMasLejanas() {
		List<FarmaciaPuntoEntregaLejanaResponse> farmacias = farmaciaService.getFarmaciasFarthestDeliveryPoint();
		if (farmacias.isEmpty()) {
			return ResponseEntity.ok().body(new MessageResponse("No existen farmacias con entregas"));
		}
		return ResponseEntity.ok(farmacias);
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
