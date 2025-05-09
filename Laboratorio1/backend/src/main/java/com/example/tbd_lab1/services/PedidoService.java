package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.DTO.RegistrarPedidoCompletoRequest;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<PedidoEntity> getByIdCliente(Long idCliente) {
        return pedidoRepository.findByIdCliente(idCliente);
    }

    public List<PagoMasUsadoUrgenteResponse> pagoMasUsadoUrgente() {
        return pedidoRepository.findMostUsedPaymentMethodWhenUrgent();
    }

    public boolean cambiarEstado(Long idPedido, String nuevoEstado) {
        return pedidoRepository.updateState(idPedido, nuevoEstado);
    }
    public boolean registrarPedidoCompleto(RegistrarPedidoCompletoRequest request) {
        if (request.getEstadoPedido() == null) {
            request.setEstadoPedido(PedidoEntity.EstadoPedido.POR_CONFIRMAR);
        }
        if (request.getFechaPedido() == null) {
            request.setFechaPedido(LocalDateTime.now());
        }
        return pedidoRepository.registrarPedidoCompleto(request);
    }
}
