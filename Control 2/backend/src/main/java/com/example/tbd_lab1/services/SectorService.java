package com.example.tbd_lab1.services;

import com.example.tbd_lab1.entities.SectorEntity;
import com.example.tbd_lab1.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
