package com.example.tbd_lab2.DTO.opinion;

import com.example.tbd_lab2.collections.OpinionesClientesCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AgruparHoraResponse {

    private LinkedHashMap<Integer, OpinionesClientesCollection> opiniones;

    public void putOpinion(int opinionId, OpinionesClientesCollection opinion) {
        opiniones.put(opinionId, opinion);
    }
}
