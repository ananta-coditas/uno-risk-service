package com.ambry.progeny.serializer;

import static org.mockito.ArgumentMatchers.anyString;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("PredictionSerializer Unit Tests")
class PredictionSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private PredictionSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new PredictionSerializer();
    }

    @Test
    @DisplayName("Should serialize Prediction with age and results")
    void testSerializeWithAgeAndResults() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1", "result2");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult("result1")).thenReturn(0.5);
        when(prediction.getResult("result2")).thenReturn(0.7);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject(); // Main object + result objects
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        verify(jsonGenerator).writeStringField("name", "result2");
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with null age")
    void testSerializeWithNullAge() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(null);
        when(prediction.getNames()).thenReturn(Collections.emptyList());

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 0);
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with null names")
    void testSerializeWithNullNames() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(25);
        when(prediction.getNames()).thenReturn(null);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 25);
        verify(jsonGenerator).writeEndObject();
        verify(jsonGenerator, never()).writeFieldName("result");
    }

    @Test
    @DisplayName("Should serialize Prediction with empty names")
    void testSerializeWithEmptyNames() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(25);
        when(prediction.getNames()).thenReturn(Collections.emptyList());

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 25);
        verify(jsonGenerator).writeEndObject();
        verify(jsonGenerator, never()).writeFieldName("result");
    }

    @Test
    @DisplayName("Should handle NullPointerException when getting result")
    void testSerializeWithNullPointerException() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult("result1")).thenThrow(new NullPointerException());

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject(); // Main object + result object
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        // NullPointerException should be caught and ignored - value field should not be written
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize all result names")
    void testSerializeAllResultNames() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1", "result2", "result3");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult(anyString())).thenReturn(0.5);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject(); // Main object + result objects
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        verify(jsonGenerator).writeStringField("name", "result2");
        verify(jsonGenerator).writeStringField("name", "result3");
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with zero age")
    void testSerializeWithZeroAge() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(0);
        when(prediction.getNames()).thenReturn(Collections.emptyList());

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 0);
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with negative age")
    void testSerializeWithNegativeAge() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(-1);
        when(prediction.getNames()).thenReturn(Collections.emptyList());

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", -1);
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with large age")
    void testSerializeWithLargeAge() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(120);
        when(prediction.getNames()).thenReturn(Collections.emptyList());

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 120);
        verify(jsonGenerator).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with single result")
    void testSerializeWithSingleResult() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult("result1")).thenReturn(0.75);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        verify(jsonGenerator).writeNumberField("value", 0.75);
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with result value zero")
    void testSerializeWithResultValueZero() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult("result1")).thenReturn(0.0);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        verify(jsonGenerator).writeNumberField("value", 0.0);
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with result value one")
    void testSerializeWithResultValueOne() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult("result1")).thenReturn(1.0);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        verify(jsonGenerator).writeNumberField("value", 1.0);
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }

    @Test
    @DisplayName("Should serialize Prediction with mixed result values and NullPointerException")
    void testSerializeWithMixedResultsAndNullPointerException() throws IOException {
        Prediction prediction = mock(Prediction.class);
        when(prediction.getAge()).thenReturn(30);
        List<String> names = Arrays.asList("result1", "result2", "result3");
        when(prediction.getNames()).thenReturn(names);
        when(prediction.getResult("result1")).thenReturn(0.5);
        when(prediction.getResult("result2")).thenThrow(new NullPointerException());
        when(prediction.getResult("result3")).thenReturn(0.7);

        serializer.serialize(prediction, jsonGenerator, serializerProvider);

        verify(jsonGenerator, atLeast(1)).writeStartObject();
        verify(jsonGenerator).writeNumberField("age", 30);
        verify(jsonGenerator).writeFieldName("result");
        verify(jsonGenerator).writeStartArray();
        verify(jsonGenerator).writeStringField("name", "result1");
        verify(jsonGenerator).writeNumberField("value", 0.5);
        verify(jsonGenerator).writeStringField("name", "result2");
        // result2 value should not be written due to NullPointerException
        verify(jsonGenerator).writeStringField("name", "result3");
        verify(jsonGenerator).writeNumberField("value", 0.7);
        verify(jsonGenerator, atLeast(1)).writeEndObject();
    }
}

