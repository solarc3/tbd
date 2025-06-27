package com.example.tbd_lab2.services;

import com.example.tbd_lab2.DTO.repartidor.*;
import com.example.tbd_lab2.entities.RepartidorEntity;
import com.example.tbd_lab2.repositories.HistorialRepartidoresRepository;
import com.example.tbd_lab2.repositories.RepartidorRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
@Service
public class RepartidorService {
    private final RepartidorRepository repartidorRepository;
    private final MongoTemplate mongoTemplate;
    private final HistorialRepartidoresRepository historialRepartidoresRepository;

    @Autowired
    public RepartidorService(RepartidorRepository repartidorRepository, MongoTemplate mongoTemplate, HistorialRepartidoresRepository historialRepartidoresRepository) {
        this.repartidorRepository = repartidorRepository;
        this.mongoTemplate = mongoTemplate;
        this.historialRepartidoresRepository = historialRepartidoresRepository;
    }

    public List<RepartidorEntity> findAll() {
        return repartidorRepository.findAll();
    }

    public List<RepartidorInfoResponse> findAllRepartidorInfo() {
        return repartidorRepository.findAllRepartidorInfo();
    }

    public List<RepartidorTiempoPromedioResponse> getByAverageDeliveryTime() {
        return repartidorRepository.findAverageDeliveryTime();
    }

    public List<RepartidorMejorRendimientoResponse> getByBestPerformance(Integer top) {
        List<RepartidorMejorRendimientoResponse> reps = repartidorRepository.findByPerformance();
        if (top != null) return reps.subList(0, top);
        return reps;
    }

    public List<RepartidorDistanciaTotalDTO> getRepartidorDistanciaTotal() { return repartidorRepository.findDistanciaTotal(); }


    public List<RepartidorRutasFrecuentesDTO> getRutasFrecuentesUltimos7Dias() {
        // 1. Calcular la fecha de hace 7 días
        LocalDate sevenDaysAgoDate = LocalDate.now().minusDays(7);
        Date sevenDaysAgo = Date.from(sevenDaysAgoDate.atStartOfDay(ZoneOffset.UTC).toInstant());

        // 2. Construir la pipeline de agregación de MongoDB
        Aggregation aggregation = newAggregation(
                // Descomponer el array de rutas en documentos individuales
                unwind("rutas"),
                // Filtrar para obtener solo las rutas de los últimos 7 días
                match(Criteria.where("rutas.timestamp").gte(sevenDaysAgo)),
                // Agrupar por repartidor y por coordenadas de ruta, y contar la frecuencia
                group(Fields.fields("idRepartidor", "rutas.lat", "rutas.lng"))
                        .count().as("frecuencia"),
                // Ordenar por frecuencia de forma descendente
                sort(Sort.Direction.DESC, "frecuencia"),
                // Agrupar de nuevo por repartidor para juntar todas sus rutas frecuentes
                group("idRepartidor")
                        .push(new Document("lat", "$_id.lat")
                                .append("lng", "$_id.lng")
                                .append("frecuencia", "$frecuencia"))
                        .as("rutasFrecuentes")
        );

        // 3. Ejecutar la agregación
        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation, "historial_repartidores", Document.class
        );
        List<Document> mappedResults = results.getMappedResults();

        // 4. Obtener nombres de repartidores desde PostgreSQL
        Map<Long, String> repartidorNombres = repartidorRepository.findAll().stream()
                .collect(Collectors.toMap(RepartidorEntity::getIdRepartidor, RepartidorEntity::getNombreRepartidor));

        // 5. Mapear el resultado final al DTO
        return mappedResults.stream().map(doc -> {
            RepartidorRutasFrecuentesDTO dto = new RepartidorRutasFrecuentesDTO();
            Long idRepartidor = doc.getLong("_id");
            dto.setIdRepartidor(idRepartidor);
            dto.setNombreRepartidor(repartidorNombres.getOrDefault(idRepartidor, "Nombre no encontrado"));

            List<Document> rutasDocs = (List<Document>) doc.get("rutasFrecuentes");
            List<RutaFrecuenteDTO> rutasDto = rutasDocs.stream().map(rutaDoc -> {
                RutaFrecuenteDTO rutaDto = new RutaFrecuenteDTO();
                rutaDto.setLat(rutaDoc.getDouble("lat"));
                rutaDto.setLng(rutaDoc.getDouble("lng"));
                rutaDto.setFrecuencia(rutaDoc.getInteger("frecuencia"));
                return rutaDto;
            }).collect(Collectors.toList());

            dto.setRutasFrecuentes(rutasDto);
            return dto;
        }).collect(Collectors.toList());
    }
}
