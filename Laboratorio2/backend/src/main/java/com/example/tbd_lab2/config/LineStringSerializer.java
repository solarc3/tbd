package com.example.tbd_lab2.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import java.io.IOException;

public class LineStringSerializer extends JsonSerializer<LineString> {
    @Override
    public void serialize(LineString lineString, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (lineString == null) {
            jsonGenerator.writeNull();
            return;
        }

        jsonGenerator.writeStartArray();
        for (Coordinate coord : lineString.getCoordinates()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("longitude", coord.getX());
            jsonGenerator.writeNumberField("latitude", coord.getY());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
