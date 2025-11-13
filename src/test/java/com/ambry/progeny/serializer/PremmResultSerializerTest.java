package com.ambry.progeny.serializer;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.premm.PREMMResult;

@ExtendWith(MockitoExtension.class)
@DisplayName("PremmResultSerializer Unit Tests")
class PremmResultSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private PremmResultSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new PremmResultSerializer();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with all fields")
    void testSerializeWithAllFields() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        Prediction prediction = mock(Prediction.class);
        RiskError error = mock(RiskError.class);
        List<Prediction> predictions = Arrays.asList(prediction);
        List<RiskError> errors = Arrays.asList(error);

        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(errors);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeNumberField("probabilityMutationMLH1", 0.3);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH2", 0.2);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH6", 0.1);
        verify(jsonGenerator).writeNumberField("probabilityMutationPMS2", 0.05);
        verify(jsonGenerator).writeStringField("version", "1.0");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(prediction);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeFieldName("errors");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(error);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with null probability values")
    void testSerializeWithNullProbabilities() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        when(result.getProbabilityCarrier()).thenReturn(null);
        when(result.getProbabilityMutationMLH1()).thenReturn(null);
        when(result.getProbabilityMutationMSH2()).thenReturn(null);
        when(result.getProbabilityMutationMSH6()).thenReturn(null);
        when(result.getProbabilityMutationPMS2()).thenReturn(null);
        when(result.getVersion()).thenReturn(null);
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.0);
        verify(jsonGenerator).writeNumberField("probabilityMutationMLH1", 0.0);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH2", 0.0);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH6", 0.0);
        verify(jsonGenerator).writeNumberField("probabilityMutationPMS2", 0.0);
        verify(jsonGenerator).writeStringField("version", "");
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with null predictions")
    void testSerializeWithNullPredictions() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeNumberField("probabilityMutationMLH1", 0.3);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH2", 0.2);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH6", 0.1);
        verify(jsonGenerator).writeNumberField("probabilityMutationPMS2", 0.05);
        verify(jsonGenerator).writeStringField("version", "1.0");
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with empty predictions")
    void testSerializeWithEmptyPredictions() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(Collections.emptyList());
        when(result.getErrors()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with null errors")
    void testSerializeWithNullErrors() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with empty errors")
    void testSerializeWithEmptyErrors() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(Collections.emptyList());

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with multiple predictions")
    void testSerializeWithMultiplePredictions() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        Prediction prediction1 = mock(Prediction.class);
        Prediction prediction2 = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction1, prediction2);

        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(prediction1);
        verify(jsonGenerator).writeObject(prediction2);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with multiple errors")
    void testSerializeWithMultipleErrors() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        RiskError error1 = mock(RiskError.class);
        RiskError error2 = mock(RiskError.class);
        List<RiskError> errors = Arrays.asList(error1, error2);

        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(errors);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeFieldName("errors");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(error1);
        verify(jsonGenerator).writeObject(error2);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PREMMResult with only probability fields")
    void testSerializeWithOnlyProbabilities() throws IOException {
        PREMMResult result = mock(PREMMResult.class);
        when(result.getProbabilityCarrier()).thenReturn(0.5);
        when(result.getProbabilityMutationMLH1()).thenReturn(0.3);
        when(result.getProbabilityMutationMSH2()).thenReturn(0.2);
        when(result.getProbabilityMutationMSH6()).thenReturn(0.1);
        when(result.getProbabilityMutationPMS2()).thenReturn(0.05);
        when(result.getVersion()).thenReturn("1.0");
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("probabilityCarrier", 0.5);
        verify(jsonGenerator).writeNumberField("probabilityMutationMLH1", 0.3);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH2", 0.2);
        verify(jsonGenerator).writeNumberField("probabilityMutationMSH6", 0.1);
        verify(jsonGenerator).writeNumberField("probabilityMutationPMS2", 0.05);
        verify(jsonGenerator).writeStringField("version", "1.0");
        verify(jsonGenerator).writeEndObject();
    }
}

