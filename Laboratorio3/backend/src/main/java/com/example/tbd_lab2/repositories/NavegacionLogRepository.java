package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.collections.NavegacionLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NavegacionLogRepository extends MongoRepository<NavegacionLog, String> {

    List<NavegacionLog> findByClientId(Long clientId);
}