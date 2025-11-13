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
import com.objex.risk.beans.panc.PancResult;

@ExtendWith(MockitoExtension.class)
@DisplayName("PancResultSerializer Unit Tests")
class PancResultSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private PancResultSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new PancResultSerializer();
    }

    @Test
    @DisplayName("Should serialize PancResult with all fields")
    void testSerializeWithAllFields() throws IOException {
        PancResult result = mock(PancResult.class);
        Prediction prediction = mock(Prediction.class);
        RiskError error = mock(RiskError.class);
        List<Prediction> predictions = Arrays.asList(prediction);
        List<RiskError> errors = Arrays.asList(error);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(errors);
        when(result.getEncodedChart()).thenReturn("encodedChartData");

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(prediction);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeFieldName("errors");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(error);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeStringField("chart", "encodedChartData");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with null predictions")
    void testSerializeWithNullPredictions() throws IOException {
        PancResult result = mock(PancResult.class);
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator, never()).writeStringField(org.mockito.ArgumentMatchers.eq("chart"), org.mockito.ArgumentMatchers.anyString());
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with empty predictions")
    void testSerializeWithEmptyPredictions() throws IOException {
        PancResult result = mock(PancResult.class);
        when(result.getPredictions()).thenReturn(Collections.emptyList());
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with null errors")
    void testSerializeWithNullErrors() throws IOException {
        PancResult result = mock(PancResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with empty errors")
    void testSerializeWithEmptyErrors() throws IOException {
        PancResult result = mock(PancResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(Collections.emptyList());
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with null chart")
    void testSerializeWithNullChart() throws IOException {
        PancResult result = mock(PancResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator, never()).writeStringField(org.mockito.ArgumentMatchers.eq("chart"), org.mockito.ArgumentMatchers.anyString());
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with multiple predictions")
    void testSerializeWithMultiplePredictions() throws IOException {
        PancResult result = mock(PancResult.class);
        Prediction prediction1 = mock(Prediction.class);
        Prediction prediction2 = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction1, prediction2);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(prediction1);
        verify(jsonGenerator).writeObject(prediction2);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with multiple errors")
    void testSerializeWithMultipleErrors() throws IOException {
        PancResult result = mock(PancResult.class);
        RiskError error1 = mock(RiskError.class);
        RiskError error2 = mock(RiskError.class);
        List<RiskError> errors = Arrays.asList(error1, error2);

        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(errors);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator).writeFieldName("errors");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(error1);
        verify(jsonGenerator).writeObject(error2);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize PancResult with only type field")
    void testSerializeWithOnlyType() throws IOException {
        PancResult result = mock(PancResult.class);
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "pancResult");
        verify(jsonGenerator).writeEndObject();
    }
}

