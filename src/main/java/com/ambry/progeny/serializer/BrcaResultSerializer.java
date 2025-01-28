package com.ambry.progeny.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.brca.BRCAResult;

public class BrcaResultSerializer  extends JsonSerializer<BRCAResult> {

    @Override
    public void serialize(BRCAResult brcaResult, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", "brcaResult");
        if (brcaResult.getPredictions() != null && !brcaResult.getPredictions().isEmpty()) {
            jsonGenerator.writeFieldName("Prediction");
            jsonGenerator.writeStartArray();
            for (Prediction prediction : brcaResult.getPredictions()) {
                jsonGenerator.writeObject(prediction);
            }
            jsonGenerator.writeEndArray();
        }

        if (brcaResult.getErrors() != null && !brcaResult.getErrors().isEmpty()) {
            jsonGenerator.writeFieldName("errors");
            jsonGenerator.writeStartArray();
            for (RiskError error : brcaResult.getErrors()) {
                jsonGenerator.writeObject(error);
            }
            jsonGenerator.writeEndArray();
        }

        if (brcaResult.getEncodedChart() != null) {
            jsonGenerator.writeStringField("chart", brcaResult.getEncodedChart());
        }

        jsonGenerator.writeEndObject();
    }
}

