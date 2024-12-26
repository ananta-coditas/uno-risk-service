package com.ambry.progeny.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.gail.GailResult;

public class GailResultSerializer extends JsonSerializer<GailResult> {

    @Override
    public void serialize(GailResult gailResult, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", "gailResult");
        if (gailResult.getPredictions() != null && !gailResult.getPredictions().isEmpty()) {
            jsonGenerator.writeFieldName("Prediction");
            jsonGenerator.writeStartArray();
            for (Prediction prediction : gailResult.getPredictions()) {
                jsonGenerator.writeObject(prediction);
            }
            jsonGenerator.writeEndArray();
        }

        if (gailResult.getErrors() != null && !gailResult.getErrors().isEmpty()) {
            jsonGenerator.writeFieldName("errors");
            jsonGenerator.writeStartArray();
            for (RiskError error : gailResult.getErrors()) {
                jsonGenerator.writeObject(error);
            }
            jsonGenerator.writeEndArray();
        }

        if (gailResult.getEncodedChart() != null) {
            jsonGenerator.writeStringField("chart", gailResult.getEncodedChart());
        }

        jsonGenerator.writeEndObject();
    }
}
