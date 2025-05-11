package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.RepartidorInfoResponse;
import com.example.tbd_lab1.DTO.RepartidorMejorRendimientoResponse;
import com.example.tbd_lab1.DTO.RepartidorTiempoPromedioResponse;
import com.example.tbd_lab1.repositories.RepartidorRepository;
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
    public List<RepartidorInfoResponse> findAll() {
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
}
