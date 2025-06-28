package com.example.tbd_lab2.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "navegacion_logs")
@Data
public class NavegacionLog {
    @Id
    private String id;

    private Long clientId;

    private String fromUrl;

    private String toUrl;

    private Instant timestamp;
}