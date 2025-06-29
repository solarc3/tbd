package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.PagoMasUsadoUrgenteResponse;
import com.example.tbd_lab2.DTO.pedido.LogsCambioPedidosResponse;
import com.example.tbd_lab2.DTO.pedido.PedidoCruzaZonasResponse;
import com.example.tbd_lab2.DTO.producto.ProductoPedidoResponse;
import com.example.tbd_lab2.DTO.pedido.RegistrarPedidoCompletoRequest;
import com.example.tbd_lab2.collections.LogsPedidosCollection;
import com.example.tbd_lab2.entities.PedidoEntity;
import com.example.tbd_lab2.entities.ProductoPedidoEntity;
import com.example.tbd_lab2.entities.UserEntity;
import com.example.tbd_lab2.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoPedidoRepository productoPedidoRepository;
    private final ProductoRepository productoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final LogsPedidosRepository logsPedidosRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoPedidoRepository productoPedidoRepository, ProductoRepository productoRepository, UserRepository userRepository, ModelMapper modelMapper, LogsPedidosRepository logsPedidosRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoPedidoRepository = productoPedidoRepository;
        this.productoRepository = productoRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.logsPedidosRepository = logsPedidosRepository;
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

    public void cambiarEstadoMongo(Long idPedido, String nuevoEstado) {
        if( logsPedidosRepository.findByIdPedido(idPedido).isPresent()){
            LogsPedidosCollection log = logsPedidosRepository.findByIdPedido(idPedido).get();
            LogsPedidosCollection.Evento evento = new LogsPedidosCollection.Evento();
            evento.setEstado(nuevoEstado);
            evento.setTimestamp(LocalDateTime.now());
            log.getEventos().add(evento);
            logsPedidosRepository.save(log);
        }else{
            LogsPedidosCollection log = new LogsPedidosCollection();
            log.setIdPedido(idPedido);
            LogsPedidosCollection.Evento evento = new LogsPedidosCollection.Evento();
            List<LogsPedidosCollection.Evento> eventos = new ArrayList<>();
            evento.setEstado(nuevoEstado);
            evento.setTimestamp(LocalDateTime.now());
            eventos.add(evento);
            log.setEventos(eventos);
            logsPedidosRepository.save(log);
        }
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

    public List<PedidoCruzaZonasResponse> getBySectorIntersection(Integer sectorAmount) {
        List<PedidoCruzaZonasResponse> pedidos = pedidoRepository.findBySectorIntersection(sectorAmount);
        return pedidos.stream()
                // generar nombre completo del cliente
                .peek(p -> {
                    UserEntity client = userRepository.findById(p.getIdCliente()).orElseThrow(IllegalArgumentException::new);
                    String name = client.getFirstName() + " " + client.getLastName();
                    p.setNombreCliente(name);
                }).toList();
    }

    public LogsCambioPedidosResponse getLogsPedidosByChangesHours() {
        List<LogsPedidosCollection> logsPedidos = logsPedidosRepository.findAll();
        List<LogsPedidosCollection> logsCambios = new ArrayList<>();
        List<PedidoEntity> pedidos = new ArrayList<>();

        for (LogsPedidosCollection logs : logsPedidos) {
            List<LogsPedidosCollection.Evento> eventos = logs.getEventos();
            int tamano = eventos.size();

            if (tamano < 3) continue;

            // Check each window of 3 consecutive events
            for (int i = 0; i <= tamano - 3; i++) {
                LogsPedidosCollection.Evento primerEvento = eventos.get(i);
                LogsPedidosCollection.Evento ultimoEvento = eventos.get(i + 2);

                // Calculate minutes between first and third event
                long diferencia = java.time.Duration.between(
                        primerEvento.getTimestamp(),
                        ultimoEvento.getTimestamp()
                ).toMinutes();

                if (diferencia < 10) {
                    logsCambios.add(logs);
                    pedidos.add(pedidoRepository.findById(logs.getIdPedido()).get());
                    break;
                }
            }
        }

        LogsCambioPedidosResponse response = new LogsCambioPedidosResponse();
        response.setLogsPedidos(logsCambios);
        response.setPedidos(pedidos);
        return response;
    }
}
