package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Optional<PedidoEntity> getById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<PagoMasUsadoUrgenteResponse> pagoMasUsadoUrgente() {
        return pedidoRepository.findMostUsedPaymentMethodWhenUrgent();
    }

    public boolean cambiarEstado(Long idPedido, String nuevoEstado) {
        return pedidoRepository.updateState(idPedido, nuevoEstado);
    }
}
