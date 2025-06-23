package com.example.tbd_lab2.services;

import com.example.tbd_lab2.collections.LogsPedidosCollection;
import com.example.tbd_lab2.repositories.LogsPedidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogsPedidosService {
    @Autowired
    private LogsPedidosRepository logsPedidosRepository;

    public Optional<LogsPedidosCollection> getByIdPedido(Long idPedido) {
        return logsPedidosRepository.findByIdPedido(idPedido);
    }
}
