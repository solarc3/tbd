package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.farmacia.FarmaciaClosestDeliveryResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab2.DTO.farmacia.FarmaciaPuntoEntregaLejanaResponse;
import com.example.tbd_lab2.DTO.farmacia.RankingFarmaciaPedidoResponse;
import com.example.tbd_lab2.entities.FarmaciaEntity;
import com.example.tbd_lab2.repositories.FarmaciaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FarmaciaService {

	private final FarmaciaRepository farmaciaRepository;

	@Autowired
	public FarmaciaService(FarmaciaRepository farmaciaRepository) {
		this.farmaciaRepository = farmaciaRepository;
	}

	public List<FarmaciaPedidoFallidoResponse> getFarmaciasWithMostCancelledPedidos() {
		return farmaciaRepository.listFarmaciaByCantPedidoCancelado();
	}
	public List<FarmaciaPuntoEntregaLejanaResponse> getFarmaciasFarthestDeliveryPoint(){
		return farmaciaRepository.listbyFarmaciaFurthestPoint();
	}
	public List<FarmaciaClosestDeliveryResponse>  getFarmaciasClosestDeliveryPoint(Long id){
		return farmaciaRepository.listUsersFarmaciaClosestDelivery(id);
	}

	public List<RankingFarmaciaPedidoResponse> rankingFarmaciaPedidos() {
		return farmaciaRepository.obtenerFarmaciaRankingPedido();
	}

	public List<FarmaciaEntity> getAllFarmacias() {
		return farmaciaRepository.findAll();
	}
}
