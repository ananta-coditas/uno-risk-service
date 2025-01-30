package com.ambry.progeny.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.mmr.MMRResult;

public class MMRResultSerializer  extends JsonSerializer<MMRResult> {

    @Override
    public void serialize(MMRResult mmrResult, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", "mmrResult");
        if (mmrResult.getPredictions() != null && !mmrResult.getPredictions().isEmpty()) {
            jsonGenerator.writeFieldName("Prediction");
            jsonGenerator.writeStartArray();
            for (Prediction prediction : mmrResult.getPredictions()) {
                jsonGenerator.writeObject(prediction);
            }
            jsonGenerator.writeEndArray();
        }

        if (mmrResult.getErrors() != null && !mmrResult.getErrors().isEmpty()) {
            jsonGenerator.writeFieldName("errors");
            jsonGenerator.writeStartArray();
            for (RiskError error : mmrResult.getErrors()) {
                jsonGenerator.writeObject(error);
            }
            jsonGenerator.writeEndArray();
        }

        if (mmrResult.getEncodedChart() != null) {
            jsonGenerator.writeStringField("chart", mmrResult.getEncodedChart());
        }

        jsonGenerator.writeEndObject();
    }
}

