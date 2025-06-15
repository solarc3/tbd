package com.example.tbd_lab2.services;

import com.example.tbd_lab2.entities.SectorEntity;
import com.example.tbd_lab2.repositories.SectorRepository;
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

    public SectorEntity createSector(SectorEntity sector) {
        return sectorRepository.save(sector);
    }


    public Optional<SectorEntity> getSectorById(Long id) {
        return sectorRepository.findById(id);
    }

    public List<SectorEntity> getAllSectores() {
        return sectorRepository.findAll();
    }

    public SectorEntity updateSector(Long id, SectorEntity sectorDetails) {
        // Ensure ID is set for update
        sectorDetails.setId(id);
        return sectorRepository.save(sectorDetails);
    }


}