package com.example.tbd_lab2.services;

import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import com.example.tbd_lab2.repositories.OpinionesClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OpinionService {

    @Autowired
    private OpinionesClientesRepository opinionesClientesRepository;

    public OpinionesClientesCollection crearOpinion(OpinionesClientesCollection opinion) {
        opinion.setFecha(new Date());
        return opinionesClientesRepository.save(opinion);
    }

    public List<OpinionesClientesCollection> findAllOpiniones() {
        return opinionesClientesRepository.findAll();
    }

    public List<OpinionesClientesCollection> getOpinionesByIdCliente(Long id){
        return opinionesClientesRepository.findByIdCliente(id);
    }

    public List<OpinionesClientesCollection> getOpinionesByIdPedido(Long id){
        return opinionesClientesRepository.findByIdPedido(id);
    }

}