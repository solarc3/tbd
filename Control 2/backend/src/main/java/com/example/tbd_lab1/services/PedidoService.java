package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab1.DTO.ProductoPedidoResponse;
import com.example.tbd_lab1.DTO.RegistrarPedidoCompletoRequest;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.entities.ProductoPedidoEntity;
import com.example.tbd_lab1.repositories.PedidoRepository;
import com.example.tbd_lab1.repositories.ProductoPedidoRepository;
import com.example.tbd_lab1.repositories.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoPedidoRepository productoPedidoRepository;
    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;

    public PedidoService(PedidoRepository pedidoRepository, ProductoPedidoRepository productoPedidoRepository, ProductoRepository productoRepository, ModelMapper modelMapper) {
        this.pedidoRepository = pedidoRepository;
        this.productoPedidoRepository = productoPedidoRepository;
        this.productoRepository = productoRepository;
        this.modelMapper = modelMapper;
    }

    public List<PedidoEntity> getPedidos() {return pedidoRepository.getPedidos(); }

    public Optional<PedidoEntity> getById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<PedidoEntity> getByIdCliente(Long idCliente) {
        return pedidoRepository.findByIdCliente(idCliente);
    }

    public List<ProductoPedidoResponse> getProducts(Long id) {
        List<ProductoPedidoEntity> productoPedidoEntities = productoPedidoRepository.findByIdPedido(id);
        return productoPedidoEntities.stream()
                .map(pp -> {
                    ProductoPedidoResponse ppr = modelMapper.map(pp, ProductoPedidoResponse.class);
                    ppr.setNombreProducto(productoRepository.findById(pp.getIdProducto()).get().getNombreProducto());
                    return ppr;
                }).toList();
    }

    public List<PagoMasUsadoUrgenteResponse> pagoMasUsadoUrgente() {
        return pedidoRepository.findMostUsedPaymentMethodWhenUrgent();
    }

    public boolean cambiarEstado(Long idPedido, String nuevoEstado) {
        return pedidoRepository.updateState(idPedido, nuevoEstado);
    }
    public boolean registrarPedidoCompleto(RegistrarPedidoCompletoRequest request) {
        if (request.getIdCliente() == null || request.getIdFarmacia() == null || request.getEsUrgente() == null)
            return false;

        if (request.getEstadoPedido() == null) {
            request.setEstadoPedido(PedidoEntity.EstadoPedido.POR_CONFIRMAR);
        }
        if (request.getFechaPedido() == null) {
            request.setFechaPedido(LocalDateTime.now());
        }

        // computar monto
        if (request.getMonto() == null) {
            Integer monto = request.getProductos().stream()
                    .map(pp ->
                            productoRepository.findById(pp.getIdProducto())
                                    .orElseThrow(IllegalArgumentException::new)
                                    .getPrecio())
                    .reduce(0, Integer::sum);
            request.setMonto(monto);
        }
        return pedidoRepository.registrarPedidoCompleto(request);
    }
}
