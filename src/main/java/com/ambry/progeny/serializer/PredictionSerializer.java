package com.ambry.progeny.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;

public class PredictionSerializer extends JsonSerializer<Prediction> {

    @Override
    public void serialize(Prediction prediction, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("age", prediction.getAge() != null ? prediction.getAge() : 0);

        if (prediction.getNames() != null && !prediction.getNames().isEmpty()) {
            jsonGenerator.writeFieldName("result");
            jsonGenerator.writeStartArray();
            for (String name : prediction.getNames()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("name", name);
                try {
                    jsonGenerator.writeNumberField("value", prediction.getResult(name));
                } catch (NullPointerException exc) {

                }
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
