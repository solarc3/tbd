package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface OpinionesClientesRepository extends MongoRepository<OpinionesClientesCollection, String> {
    List<OpinionesClientesCollection> findByIdCliente(Long idCliente);
    List<OpinionesClientesCollection> findByIdPedido(Long idPedido);
}
