package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.FarmaciaPedidoFallidoResponse;
import com.example.tbd_lab1.DTO.RankingFarmaciaPedidoResponse;
import com.example.tbd_lab1.entities.FarmaciaEntity;
import com.example.tbd_lab1.repositories.FarmaciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<RankingFarmaciaPedidoResponse> rankingFarmaciaPedidos() {
        return farmaciaRepository.obtenerFarmaciaRankingPedido();
    }
}
