package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.EventoNavegacionRequest;
import com.example.tbd_lab2.collections.NavegacionUsuariosCollection;
import com.example.tbd_lab2.repositories.NavegacionUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NavegacionUsuariosService {
    @Autowired
    private NavegacionUsuariosRepository navegacionUsuariosRepository;

    public List<NavegacionUsuariosCollection> getAll() {
        return navegacionUsuariosRepository.findAll();
    }

    public void saveLog(EventoNavegacionRequest eventoDto) {
        Optional<NavegacionUsuariosCollection> navegacionUsuario = navegacionUsuariosRepository.findByIdCliente(eventoDto.getIdCliente());

        if (navegacionUsuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        NavegacionUsuariosCollection.EventoNavegacion evento = NavegacionUsuariosCollection.EventoNavegacion.builder()
                .tipo(eventoDto.getTipo())
                .valor(eventoDto.getValor())
                .timestamp(eventoDto.getTimestamp())
                .build();

        NavegacionUsuariosCollection navegacionUsuarios = navegacionUsuario.get();
        navegacionUsuarios.addLog(evento);
        navegacionUsuariosRepository.save(navegacionUsuarios);
    }

}
