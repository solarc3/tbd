package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.collections.LogsPedidosCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LogsPedidosRepository extends MongoRepository<LogsPedidosCollection, String> {
    Optional<LogsPedidosCollection> findByIdPedido(Long idPedido);
}
