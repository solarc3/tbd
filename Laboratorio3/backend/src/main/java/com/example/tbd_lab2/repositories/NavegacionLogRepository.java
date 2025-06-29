package com.example.tbd_lab2.repositories;

import com.example.tbd_lab2.collections.NavegacionLog;
import com.example.tbd_lab2.DTO.NavegacionSinCompraResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import java.util.List;

public interface NavegacionLogRepository extends MongoRepository<NavegacionLog, String> {

    List<NavegacionLog> findByClientId(Long clientId);

    @Aggregation(pipeline = {
            // agrupar por sesion diaria x cliente.
            """
            { $group: {
                    _id: {
                    cliente: '$clientId',
                    fecha: { $dateToString: { format: '%Y-%m-%d', date: { $toDate: '$timestamp' } } }
                    },
                    historial: { $push: '$$ROOT' },
                    realizoCompraEnSesion: { $max: { $cond: [ { $eq: ['$toUrl', '/purchase/success'] }, 1, 0 ] } }
                    } }
            """,

            // filtrar por sesiones donde no se realizo una compra
            "{ $match: { realizoCompraEnSesion: 0 } }",

            // output
            "{ $project: { idCliente: '$_id.cliente', historial: '$historial', _id: 0 } }"
    })
    List<NavegacionSinCompraResponse> findNavegacionSinCompraPorSesion();
}