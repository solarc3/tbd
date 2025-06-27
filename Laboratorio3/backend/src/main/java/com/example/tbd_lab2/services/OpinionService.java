package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.farmacia.FarmaciaPromedioDTO;
import com.example.tbd_lab2.DTO.opinion.AgruparHoraResponse;
import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import com.example.tbd_lab2.entities.FarmaciaEntity;
import com.example.tbd_lab2.entities.PedidoEntity;
import com.example.tbd_lab2.repositories.FarmaciaRepository;
import com.example.tbd_lab2.repositories.OpinionesClientesRepository;
import com.example.tbd_lab2.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpinionService {

    @Autowired
    private OpinionesClientesRepository opinionesClientesRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    public OpinionesClientesCollection crearOpinion(OpinionesClientesCollection opinion) {
        opinion.setFecha(LocalDateTime.now());
        return opinionesClientesRepository.save(opinion);
    }

    public List<OpinionesClientesCollection> findAllOpiniones() {
        return opinionesClientesRepository.findAll();
    }

    public List<OpinionesClientesCollection> getOpinionesByIdCliente(Long id){
        return opinionesClientesRepository.findByIdCliente(id);
    }

    public List<OpinionesClientesCollection> getOpinionesByIdPedido(Long id){
        return opinionesClientesRepository.findByIdPedido(id);
    }

    // lab 3
    // promedio de puntuacion por empresa
    public List<FarmaciaPromedioDTO> getPromedioPuntuacionPorFarmacia() {
        List<OpinionesClientesCollection> opiniones = opinionesClientesRepository.findAll();

        if (opiniones.isEmpty()) {
            return List.of();
        }

        List<Long> pedidoIds = opiniones.stream()
                .map(OpinionesClientesCollection::getIdPedido)
                .distinct()
                .toList();

        List<PedidoEntity> pedidos = pedidoIds.stream()
                .map(pedidoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Map<Long, Long> pedidoFarmaciaMap = pedidos.stream()
                .filter(p -> p.getIdFarmacia() != null)
                .collect(Collectors.toMap(PedidoEntity::getIdPedido, PedidoEntity::getIdFarmacia));

        Map<Long, List<Integer>> farmaciaPuntuacionesMap = opiniones.stream()
                .filter(opinion -> pedidoFarmaciaMap.containsKey(opinion.getIdPedido()))
                .collect(Collectors.groupingBy(
                        opinion -> pedidoFarmaciaMap.get(opinion.getIdPedido()),
                        Collectors.mapping(OpinionesClientesCollection::getPuntuacion, Collectors.toList())
                ));

        List<FarmaciaEntity> farmacias = farmaciaRepository.findAll();
        Map<Long, String> farmaciaNombreMap = farmacias.stream()
                .collect(Collectors.toMap(FarmaciaEntity::getIdFarmacia, FarmaciaEntity::getNombreFarmacia));

        return farmaciaPuntuacionesMap.entrySet().stream()
                .map(entry -> {
                    Long farmaciaId = entry.getKey();
                    List<Integer> puntuaciones = entry.getValue();
                    double promedio = puntuaciones.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);
                    String nombreFarmacia = farmaciaNombreMap.getOrDefault(farmaciaId, "Farmacia Desconocida");
                    return new FarmaciaPromedioDTO(nombreFarmacia, promedio);
                })
                .collect(Collectors.toList());
    }

    public AgruparHoraResponse getOpinionesByHoras(){
        List<OpinionesClientesCollection> opiniones = opinionesClientesRepository.findAll();

        Map<Integer, OpinionesClientesCollection> opinionesPorHora = new HashMap<>(Map.of());

        LocalDateTime fecha = LocalDateTime.now();
        for(OpinionesClientesCollection opinion : opiniones){
            fecha = opinion.getFecha();
            int comparacion = fecha.getHour();
            opinionesPorHora.put(comparacion, opinion);
        }
        AgruparHoraResponse response = new AgruparHoraResponse();
        response.setOpiniones(opinionesPorHora);
        return response;
    }
}