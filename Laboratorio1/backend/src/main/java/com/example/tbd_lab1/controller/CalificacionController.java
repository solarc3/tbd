package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.CalificacionResponse;
import com.example.tbd_lab1.DTO.MessageResponse;
import com.example.tbd_lab1.entities.CalificacionEntity;
import com.example.tbd_lab1.repositories.CalificacionRepository;
import com.example.tbd_lab1.security.services.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/calificacion")
public class CalificacionController {

    @Autowired
    CalificacionRepository calificacionRepository;
    ModelMapper modelMapper;

    @GetMapping("{id_calificacion}")
    public ResponseEntity<Object> getCalification(@PathVariable Long id_calification, @AuthenticationPrincipal UserDetailsImpl currentUser){
        //recuperar id del cliente que hizo la calificacion en base al repositorio para luego comparar si en verdad el q lo solicita hizo esa calificacion
        Long currentUserId = currentUser.getId();
        Optional<CalificacionEntity> calification = calificacionRepository.findById(id_calification);
        if (calification.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Rip Bozo PACKWATCH"));
        }

        if(!calification.get().getClienteId().equals(currentUserId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        CalificacionResponse dto = modelMapper.map(calification, CalificacionResponse.class);
        //DTO Mapper aca
        return ResponseEntity.ok(dto);

    }

}
