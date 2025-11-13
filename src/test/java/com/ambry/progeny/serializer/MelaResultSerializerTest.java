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
import com.objex.risk.beans.mela.MelaResult;

@ExtendWith(MockitoExtension.class)
@DisplayName("MelaResultSerializer Unit Tests")
class MelaResultSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private MelaResultSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new MelaResultSerializer();
    }

    @Test
    @DisplayName("Should serialize MelaResult with all fields")
    void testSerializeWithAllFields() throws IOException {
        MelaResult result = mock(MelaResult.class);
        Prediction prediction = mock(Prediction.class);
        RiskError error = mock(RiskError.class);
        List<Prediction> predictions = Arrays.asList(prediction);
        List<RiskError> errors = Arrays.asList(error);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(errors);
        when(result.getEncodedChart()).thenReturn("encodedChartData");

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
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
    @DisplayName("Should serialize MelaResult with null predictions")
    void testSerializeWithNullPredictions() throws IOException {
        MelaResult result = mock(MelaResult.class);
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator, never()).writeStringField(org.mockito.ArgumentMatchers.eq("chart"), org.mockito.ArgumentMatchers.anyString());
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with empty predictions")
    void testSerializeWithEmptyPredictions() throws IOException {
        MelaResult result = mock(MelaResult.class);
        when(result.getPredictions()).thenReturn(Collections.emptyList());
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator, never()).writeFieldName("Prediction");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with null errors")
    void testSerializeWithNullErrors() throws IOException {
        MelaResult result = mock(MelaResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with empty errors")
    void testSerializeWithEmptyErrors() throws IOException {
        MelaResult result = mock(MelaResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(Collections.emptyList());
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, never()).writeFieldName("errors");
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with null chart")
    void testSerializeWithNullChart() throws IOException {
        MelaResult result = mock(MelaResult.class);
        Prediction prediction = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator, never()).writeStringField(org.mockito.ArgumentMatchers.eq("chart"), org.mockito.ArgumentMatchers.anyString());
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with multiple predictions")
    void testSerializeWithMultiplePredictions() throws IOException {
        MelaResult result = mock(MelaResult.class);
        Prediction prediction1 = mock(Prediction.class);
        Prediction prediction2 = mock(Prediction.class);
        List<Prediction> predictions = Arrays.asList(prediction1, prediction2);

        when(result.getPredictions()).thenReturn(predictions);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator).writeFieldName("Prediction");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(prediction1);
        verify(jsonGenerator).writeObject(prediction2);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with multiple errors")
    void testSerializeWithMultipleErrors() throws IOException {
        MelaResult result = mock(MelaResult.class);
        RiskError error1 = mock(RiskError.class);
        RiskError error2 = mock(RiskError.class);
        List<RiskError> errors = Arrays.asList(error1, error2);

        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(errors);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator).writeFieldName("errors");
        verify(jsonGenerator, atLeast(1)).writeStartArray();
        verify(jsonGenerator).writeObject(error1);
        verify(jsonGenerator).writeObject(error2);
        verify(jsonGenerator, atLeast(1)).writeEndArray();
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize MelaResult with only type field")
    void testSerializeWithOnlyType() throws IOException {
        MelaResult result = mock(MelaResult.class);
        when(result.getPredictions()).thenReturn(null);
        when(result.getErrors()).thenReturn(null);
        when(result.getEncodedChart()).thenReturn(null);

        serializer.serialize(result, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "melaResult");
        verify(jsonGenerator).writeEndObject();
    }
}

