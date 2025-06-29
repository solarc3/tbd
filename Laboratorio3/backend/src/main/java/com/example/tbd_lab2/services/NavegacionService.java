package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.NavegacionSinCompraResponse;
import com.example.tbd_lab2.collections.NavegacionLog;
import com.example.tbd_lab2.DTO.NavegacionRequest;
import com.example.tbd_lab2.entities.UserEntity;
import com.example.tbd_lab2.repositories.NavegacionLogRepository;
import com.example.tbd_lab2.repositories.UserRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class NavegacionService {

    private final NavegacionLogRepository navegacionLogRepository;
    private final UserRepository userRepository;

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

    public List<NavegacionSinCompraResponse> getNavegacionSinCompras() {
        return navegacionLogRepository.findNavegacionSinCompraPorSesion()
                .stream()
                .peek(nav -> {
                    UserEntity user = userRepository.findById(nav.getIdCliente()).orElseThrow();
                    String nombreCliente = user.getFirstName() + " " + user.getLastName();
                    nav.setNombreCliente(nombreCliente);
                })
                .toList();
    }
}