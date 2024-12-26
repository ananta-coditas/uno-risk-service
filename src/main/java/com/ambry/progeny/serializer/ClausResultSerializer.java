package com.ambry.progeny.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.claus.ClausResult;

public class ClausResultSerializer extends JsonSerializer<ClausResult> {

    @Override
    public void serialize(ClausResult clausResult, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", "clausResult");
        if (clausResult.getPredictions() != null && !clausResult.getPredictions().isEmpty()) {
            jsonGenerator.writeFieldName("Prediction");
            jsonGenerator.writeStartArray();
            for (Prediction prediction : clausResult.getPredictions()) {
                jsonGenerator.writeObject(prediction);
            }
            jsonGenerator.writeEndArray();
        }

        if (clausResult.getErrors() != null && !clausResult.getErrors().isEmpty()) {
            jsonGenerator.writeFieldName("errors");
            jsonGenerator.writeStartArray();
            for (RiskError error : clausResult.getErrors()) {
                jsonGenerator.writeObject(error);
            }
            jsonGenerator.writeEndArray();
        }

        if (clausResult.getEncodedChart() != null) {
            jsonGenerator.writeStringField("chart", clausResult.getEncodedChart());
        }

        jsonGenerator.writeEndObject();
    }
}
