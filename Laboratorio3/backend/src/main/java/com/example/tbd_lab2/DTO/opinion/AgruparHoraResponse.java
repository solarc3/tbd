package com.example.tbd_lab2.DTO.opinion;

import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class AgruparHoraResponse {

    private Map<Integer, OpinionesClientesCollection> opiniones;

}
