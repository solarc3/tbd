package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.repartidor.RepartidorDistanciaTotalDTO;
import com.example.tbd_lab2.DTO.repartidor.RepartidorInfoResponse;
import com.example.tbd_lab2.DTO.repartidor.RepartidorMejorRendimientoResponse;
import com.example.tbd_lab2.DTO.repartidor.RepartidorTiempoPromedioResponse;
import com.example.tbd_lab2.entities.RepartidorEntity;
import com.example.tbd_lab2.repositories.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepartidorService {
    private final RepartidorRepository repartidorRepository;

    @Autowired
    public RepartidorService(RepartidorRepository repartidorRepository) {
        this.repartidorRepository = repartidorRepository;
    }

    public List<RepartidorEntity> findAll() {
        return repartidorRepository.findAll();
    }

    public List<RepartidorInfoResponse> findAllRepartidorInfo() {
        return repartidorRepository.findAllRepartidorInfo();
    }

    public List<RepartidorTiempoPromedioResponse> getByAverageDeliveryTime() {
        return repartidorRepository.findAverageDeliveryTime();
    }

    public List<RepartidorMejorRendimientoResponse> getByBestPerformance(Integer top) {
        List<RepartidorMejorRendimientoResponse> reps = repartidorRepository.findByPerformance();
        if (top != null) return reps.subList(0, top);
        return reps;
    }

    public List<RepartidorDistanciaTotalDTO> getRepartidorDistanciaTotal() { return repartidorRepository.findDistanciaTotal(); }
}
