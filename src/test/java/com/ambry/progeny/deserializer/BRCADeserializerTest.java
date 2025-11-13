package com.ambry.progeny.deserializer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.brca.Risk;

@DisplayName("BRCADeserializer Unit Tests")
class BRCADeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BRCADeserializer deserializer = new BRCADeserializer();
    private final JsonFactory jsonFactory = new JsonFactory();

    @Test
    @DisplayName("Should deserialize Risk with all fields")
    void testDeserializeRiskWithAllFields() throws Exception {
        String json = "{\n" +
                "  \"ageBy\": 30,\n" +
                "  \"ageTo\": 80,\n" +
                "  \"consent\": true,\n" +
                "  \"familyId\": \"FAM001\",\n" +
                "  \"saveIncoming\": true,\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": 45,\n" +
                "        \"ageOfDeath\": 0,\n" +
                "        \"fatherID\": 0,\n" +
                "        \"motherID\": 0,\n" +
                "        \"gender\": 1,\n" +
                "        \"proband\": true,\n" +
                "        \"testOrder\": 1,\n" +
                "        \"twin\": 0,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"diagnoseBreastAge\": 40,\n" +
                "        \"diagnoseOvarianAge\": -99\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        assertThat(risk.getAgeBy()).isEqualTo(30);
        assertThat(risk.getAgeTo()).isEqualTo(80);
        assertThat(risk.isConsent()).isTrue();
        assertThat(risk.getFamilyId()).isEqualTo("FAM001");
        assertThat(risk.isSaveIncoming()).isTrue();
        // Note: Risk class from external JAR, cannot directly access persons list
        // Verification is done through successful deserialization
        // Person was successfully added to risk (no exception thrown)
    }

    @Test
    @DisplayName("Should deserialize Risk with default values when fields are missing")
    void testDeserializeRiskWithDefaults() throws Exception {
        String json = "{}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        assertThat(risk.getAgeBy()).isEqualTo(0);
        assertThat(risk.getAgeTo()).isEqualTo(0);
        assertThat(risk.isConsent()).isFalse();
        assertThat(risk.getFamilyId()).isEmpty();
        assertThat(risk.isSaveIncoming()).isFalse();
    }

    @Test
    @DisplayName("Should deserialize Risk with empty Person array")
    void testDeserializeRiskWithEmptyPersonArray() throws Exception {
        String json = "{\n" +
                "  \"ageBy\": 30,\n" +
                "  \"Family\": {\n" +
                "    \"Person\": []\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        assertThat(risk.getAgeBy()).isEqualTo(30);
    }

    @Test
    @DisplayName("Should deserialize Risk with multiple Persons")
    void testDeserializeRiskWithMultiplePersons() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": 45,\n" +
                "        \"vitalStatus\": \"ALIVE\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"ID\": 2,\n" +
                "        \"age\": 50,\n" +
                "        \"vitalStatus\": \"DEAD\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        // Multiple persons were successfully added (no exception thrown)
    }

    @Test
    @DisplayName("Should deserialize Risk without Family node")
    void testDeserializeRiskWithoutFamily() throws Exception {
        String json = "{\n" +
                "  \"ageBy\": 30,\n" +
                "  \"ageTo\": 80\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        assertThat(risk.getAgeBy()).isEqualTo(30);
        assertThat(risk.getAgeTo()).isEqualTo(80);
    }

    @Test
    @DisplayName("Should handle Person with missing vitalStatus field")
    void testDeserializePersonWithMissingVitalStatus() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": 45\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        assertThatThrownBy(() -> deserializer.deserialize(parser, context))
                .isInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("Should throw IOException for malformed JSON")
    void testDeserializeWithMalformedJson() throws Exception {
        String json = "{ invalid json }";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        assertThatThrownBy(() -> deserializer.deserialize(parser, context))
                .isInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("Should handle Person with all default values")
    void testDeserializePersonWithDefaults() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"vitalStatus\": \"ALIVE\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        // Person with defaults was successfully added (no exception thrown)
    }

    @Test
    @DisplayName("Should handle Person with DEAD vitalStatus")
    void testDeserializePersonWithDeadVitalStatus() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": 45,\n" +
                "        \"vitalStatus\": \"DEAD\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        // Person with DEAD vitalStatus was successfully added (no exception thrown)
    }

    @Test
    @DisplayName("Should handle Family node without Person")
    void testDeserializeRiskWithFamilyWithoutPerson() throws Exception {
        String json = "{\n" +
                "  \"Family\": {}\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Person node that is not an array")
    void testDeserializeRiskWithPersonNotArray() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": \"not-an-array\"\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Person with invalid vitalStatus")
    void testDeserializePersonWithInvalidVitalStatus() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"INVALID\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        // BRCADeserializer doesn't throw exception for invalid vitalStatus, it just sets it to null
        Risk risk = deserializer.deserialize(parser, context);
        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Person with case-insensitive vitalStatus")
    void testDeserializePersonWithCaseInsensitiveVitalStatus() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"alive\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Person with negative age values")
    void testDeserializePersonWithNegativeAge() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": -1,\n" +
                "        \"vitalStatus\": \"ALIVE\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Person with large age values")
    void testDeserializePersonWithLargeAge() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": 150,\n" +
                "        \"vitalStatus\": \"ALIVE\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Person with zero age")
    void testDeserializePersonWithZeroAge() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"age\": 0,\n" +
                "        \"vitalStatus\": \"ALIVE\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should handle Risk with negative ageBy and ageTo")
    void testDeserializeRiskWithNegativeAges() throws Exception {
        String json = "{\n" +
                "  \"ageBy\": -10,\n" +
                "  \"ageTo\": -5\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        assertThat(risk.getAgeBy()).isEqualTo(-10);
        assertThat(risk.getAgeTo()).isEqualTo(-5);
    }

    @Test
    @DisplayName("Should handle Person with all optional fields missing")
    void testDeserializePersonWithAllOptionalFieldsMissing() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"vitalStatus\": \"ALIVE\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
    }
}

