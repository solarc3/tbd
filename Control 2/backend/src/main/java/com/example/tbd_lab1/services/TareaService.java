package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.TareaCercanaResponse;
import com.example.tbd_lab1.DTO.TareaCountBySectorResponse;
import com.example.tbd_lab1.DTO.TareaVencimientoResponse;
import com.example.tbd_lab1.entities.TareaEntity;
import com.example.tbd_lab1.repositories.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    @Autowired
    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<TareaEntity> getAllTareas() {
        return tareaRepository.findAll();
    }

    public Optional<TareaEntity> getTareaById(Long id) {
        return tareaRepository.findById(id);
    }

    public List<TareaEntity> getTareasByUsuario(Long idUsuario) {
        return tareaRepository.findByUsuario(idUsuario);
    }

    public List<TareaEntity> getTareasByEstado(String estado) {
        return tareaRepository.findByEstado(estado);
    }

    public List<TareaEntity> getTareasByIdSector(Long idSector) {
        return tareaRepository.findByIdSector(idSector);
    }

    public List<TareaVencimientoResponse> getTareasPorVencerHoy(Long idUsuario) { return tareaRepository.findTareasPorVencerHoyByUsuario(idUsuario); }

    public TareaEntity createTarea(TareaEntity tareaEntity) {
        return tareaRepository.save(tareaEntity);
    }

    public TareaEntity updateTarea(TareaEntity tareaEntity) {
        return tareaRepository.save(tareaEntity);
    }

    public boolean deleteTarea(Long id) {
        return tareaRepository.deleteTarea(id);
    }

    public boolean existsById(Long id) {
        return tareaRepository.existsById(id);
    }

    public List<TareaCountBySectorResponse> getTareaCountByUsuarioAndSector(Long idUsuario) {
        return tareaRepository.countTareasByUsuarioAndSector(idUsuario);
    }

    public List<TareaCountBySectorResponse> getTareaCountForEachUsuarioBySector() {
        return tareaRepository.countTareasForEachUsuarioBySector();
    }

    public List<TareaCercanaResponse> getTareaPendienteMasCercana(Long idUsuario) {
        return tareaRepository.findTareaPendienteMasCercana(idUsuario);
    }
}