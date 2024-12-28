package com.ambry.progeny.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.premm.PREMMResult;

public class PremmResultSerializer extends JsonSerializer<PREMMResult> {

    @Override
    public void serialize(PREMMResult premmResult, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("probabilityCarrier",
                premmResult.getProbabilityCarrier() != null ? premmResult.getProbabilityCarrier() : 0.0);
        jsonGenerator.writeNumberField("probabilityMutationMLH1",
                premmResult.getProbabilityMutationMLH1() != null ? premmResult.getProbabilityMutationMLH1() : 0.0);
        jsonGenerator.writeNumberField("probabilityMutationMSH2",
                premmResult.getProbabilityMutationMSH2() != null ? premmResult.getProbabilityMutationMSH2() : 0.0);
        jsonGenerator.writeNumberField("probabilityMutationMSH6",
                premmResult.getProbabilityMutationMSH6() != null ? premmResult.getProbabilityMutationMSH6() : 0.0);
        jsonGenerator.writeNumberField("probabilityMutationPMS2",
                premmResult.getProbabilityMutationPMS2() != null ? premmResult.getProbabilityMutationPMS2() : 0.0);
        jsonGenerator.writeStringField("version", premmResult.getVersion() != null ? premmResult.getVersion() : "");
        if (premmResult.getPredictions() != null && !premmResult.getPredictions().isEmpty()) {
            jsonGenerator.writeFieldName("Prediction");
            jsonGenerator.writeStartArray();
            for (Prediction prediction : premmResult.getPredictions()) {
                jsonGenerator.writeObject(prediction);
            }
            jsonGenerator.writeEndArray();
        }

        if (premmResult.getErrors() != null && !premmResult.getErrors().isEmpty()) {
            jsonGenerator.writeFieldName("errors");
            jsonGenerator.writeStartArray();
            for (RiskError error : premmResult.getErrors()) {
                jsonGenerator.writeObject(error);
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
