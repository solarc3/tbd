package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.collections.NavegacionUsuariosCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NavegacionUsuariosRepository extends MongoRepository<NavegacionUsuariosCollection, String> {
    Optional<NavegacionUsuariosCollection> findByIdCliente(Long idCliente);
}
