package com.example.tbd_lab2.services;

import com.example.tbd_lab2.collections.NavegacionLog;
import com.example.tbd_lab2.DTO.NavegacionRequest;
import com.example.tbd_lab2.repositories.NavegacionLogRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class NavegacionService {

    private final NavegacionLogRepository navegacionLogRepository;

    public List<NavegacionLog> getAll() {
        return navegacionLogRepository.findAll();
    }

    public void saveLog(Long clientId, NavegacionRequest request) {
        NavegacionLog log = new NavegacionLog();
        log.setClientId(clientId);
        log.setFromUrl(request.getFrom());
        log.setToUrl(request.getTo());
        log.setTimestamp(Instant.now());

        navegacionLogRepository.save(log);
    }
}