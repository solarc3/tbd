package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.DetallePedidoResponse;
import com.example.tbd_lab1.entities.DetallePedidoEntity;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.entities.RepartidorEntity;
import com.example.tbd_lab1.repositories.DetallePedidoRepository;
import com.example.tbd_lab1.repositories.RepartidorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoService {
    private final DetallePedidoRepository detallePedidoRepository;
    private final RepartidorRepository repartidorRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, RepartidorRepository repartidorRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.repartidorRepository = repartidorRepository;
    }

    public DetallePedidoEntity createDetallePedido(PedidoEntity pedidoEntity, LocalDateTime fechaEntrega, Long idRepartidor, String metodoPago) {
        DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
        detallePedidoEntity.setIdPedido(pedidoEntity.getIdPedido());
        detallePedidoEntity.setFechaEntrega(fechaEntrega);
        detallePedidoEntity.setIdRepartidor(idRepartidor);
        detallePedidoEntity.setMetodoPago(metodoPago);
        return detallePedidoRepository.save(detallePedidoEntity);
    }

    public List<DetallePedidoEntity> getByIdRepartidor(Long id) {
        return detallePedidoRepository.findByIdRepartidor(id);
    }

    public Optional<DetallePedidoEntity> getByIdPedido(Long id) {
        return detallePedidoRepository.findByIdPedido(id);
    }

    public DetallePedidoResponse mapToResponse(DetallePedidoEntity detallePedidoEntity) {
        DetallePedidoResponse detallePedidoResponse = new DetallePedidoResponse();
        Optional<RepartidorEntity> repartidorEntity = repartidorRepository.findById(detallePedidoEntity.getIdRepartidor());
        modelMapper.map(detallePedidoEntity, detallePedidoResponse);

        // setear nombre del repartidor
        if (repartidorEntity.isPresent()) {
            String nombreRepartidor = repartidorEntity.get().getNombreRepartidor();
            detallePedidoResponse.setNombreRepartidor(nombreRepartidor);
        }
        return detallePedidoResponse;
    }
}
