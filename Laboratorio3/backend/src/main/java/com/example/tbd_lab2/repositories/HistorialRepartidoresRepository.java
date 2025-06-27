package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.collections.HistorialRepartidoresCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistorialRepartidoresRepository extends MongoRepository<HistorialRepartidoresCollection, String> {
    Optional<HistorialRepartidoresCollection> findByIdRepartidor(Long idRepartidor);
}