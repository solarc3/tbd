package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.DetallePedidoResponse;
import com.example.tbd_lab1.entities.DetallePedidoEntity;
import com.example.tbd_lab1.entities.PedidoEntity;
import com.example.tbd_lab1.entities.ProductoPedidoEntity;
import com.example.tbd_lab1.entities.RepartidorEntity;
import com.example.tbd_lab1.repositories.DetallePedidoRepository;
import com.example.tbd_lab1.repositories.ProductoFarmaciaRepository;
import com.example.tbd_lab1.repositories.ProductoPedidoRepository;
import com.example.tbd_lab1.repositories.RepartidorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoService {
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoPedidoRepository productoPedidoRepository;
    private final ProductoFarmaciaRepository productoFarmaciaRepository;
    private final RepartidorRepository repartidorRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, ProductoPedidoRepository productoPedidoRepository, ProductoFarmaciaRepository productoFarmaciaRepository, RepartidorRepository repartidorRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.productoPedidoRepository = productoPedidoRepository;
        this.productoFarmaciaRepository = productoFarmaciaRepository;
        this.repartidorRepository = repartidorRepository;
    }

    public DetallePedidoEntity createDetallePedido(PedidoEntity pedidoEntity, Long idRepartidor, String metodoPago) {
        DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
        Long idPedido = pedidoEntity.getIdPedido();
        detallePedidoEntity.setIdPedido(idPedido);
        // la fecha de entrega la setea el trigger!
        detallePedidoEntity.setIdRepartidor(idRepartidor);
        detallePedidoEntity.setMetodoPago(metodoPago);

        // descontar stock
        List<ProductoPedidoEntity> productos = productoPedidoRepository.findByIdPedido(idPedido);
        productos.forEach(p ->
                    productoFarmaciaRepository.descontarStock(
                            p.getIdProducto(),
                            pedidoEntity.getIdFarmacia(),
                            p.getCantidad())
                    );
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
