package com.example.tbd_lab1.services;

import com.example.tbd_lab1.DTO.SectorTareasResponse;
import com.example.tbd_lab1.entities.SectorEntity;
import com.example.tbd_lab1.repositories.SectorRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SectorService {
    private final SectorRepository sectorRepository;

    @Autowired
    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public Optional<SectorEntity> getSectorById(Long id) {
        return sectorRepository.findById(id);
    }

    public List<SectorEntity> getAllSectors() {
        return sectorRepository.findAll();
    }

    public SectorEntity createSector(SectorEntity sector) {
        return sectorRepository.save(sector);
    }

    public SectorEntity updateSector(SectorEntity sector) {
        return sectorRepository.save(sector);
    }

    public boolean existsById(Long id) {
        return sectorRepository.existsById(id);
    }

    public boolean deleteSector(Long id) {
        return sectorRepository.delete(id);
    }

    public List<SectorTareasResponse> getByCompletedTasksWithin(Point location, BigDecimal radius) {
        return sectorRepository.findByCompletedTasksWithin(
                location.getX(),
                location.getY(),
                radius.multiply(BigDecimal.valueOf(1000)));
    }

    public List<SectorTareasResponse> TareasBySector() {
        return sectorRepository.findTareasPendientesBySector();
    }
}
