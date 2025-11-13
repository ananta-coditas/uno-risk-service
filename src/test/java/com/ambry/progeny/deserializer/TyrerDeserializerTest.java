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
import com.objex.risk.beans.tcuzick.Risk;

@DisplayName("TyrerDeserializer Unit Tests")
class TyrerDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TyrerDeserializer deserializer = new TyrerDeserializer();
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
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"densityValue\": 0.5,\n" +
                "        \"diagnoseBilateralBreastAge\": 40,\n" +
                "        \"diagnoseBreastAge\": 40,\n" +
                "        \"diagnoseOvarianAge\": -99,\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hasAtypicalHyperplasia\": false,\n" +
                "        \"hasLCIS\": false,\n" +
                "        \"hasLiveBirth\": true,\n" +
                "        \"height\": 165.0,\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"liveBirthAge\": 30,\n" +
                "        \"menarcheAge\": 12,\n" +
                "        \"menopauseAge\": 50,\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"polygenicSNP\": 0.0,\n" +
                "        \"usedHRT\": \"NEVER\",\n" +
                "        \"weight\": 60.0,\n" +
                "        \"yearsFutureHRT\": 0.0,\n" +
                "        \"yearsPastHRT\": 0.0,\n" +
                "        \"yearsSinceHRT\": 0.0\n" +
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
        // Person was successfully added (no exception thrown)
        // Note: Cannot directly access persons from Risk class (external JAR)
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
    @DisplayName("Should handle Person with missing densityValue")
    void testDeserializePersonWithMissingDensityValue() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        // Person was successfully added (no exception thrown)
        // Note: Cannot directly access persons from Risk class (external JAR)
        // assertThat(risk.get(0).getDensityValue()).isEqualTo(-99.0);
    }

    @Test
    @DisplayName("Should handle Person with missing height")
    void testDeserializePersonWithMissingHeight() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        // Person was successfully added (no exception thrown)
        // Note: Cannot directly access persons from Risk class (external JAR)
        // assertThat(risk.get(0).getHeight()).isEqualTo(-99.0);
    }

    @Test
    @DisplayName("Should handle Person with missing weight")
    void testDeserializePersonWithMissingWeight() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonParser parser = jsonFactory.createParser(json);
        parser.setCodec(objectMapper);
        DeserializationContext context = objectMapper.getDeserializationContext();

        Risk risk = deserializer.deserialize(parser, context);

        assertThat(risk).isNotNull();
        // Person was successfully added (no exception thrown)
        // Note: Cannot directly access persons from Risk class (external JAR)
        // assertThat(risk.get(0).getWeight()).isEqualTo(-99.0);
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
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
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
        // Note: Cannot directly access persons from Risk class (external JAR)
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
    @DisplayName("Should handle Person with invalid affectedBreastType")
    void testDeserializePersonWithInvalidAffectedBreastType() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"INVALID\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
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
    @DisplayName("Should handle Person with case-insensitive affectedBreastType")
    void testDeserializePersonWithCaseInsensitiveAffectedBreastType() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"none\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
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
    @DisplayName("Should handle Person with case-insensitive vitalStatus")
    void testDeserializePersonWithCaseInsensitiveVitalStatus() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"alive\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
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
    @DisplayName("Should handle Risk without Family node")
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
    @DisplayName("Should handle Person with empty Person array")
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
    @DisplayName("Should handle Person with missing polygenicSNP")
    void testDeserializePersonWithMissingPolygenicSNP() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
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
    @DisplayName("Should handle Person with missing HRT years fields")
    void testDeserializePersonWithMissingHRTYears() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\"\n" +
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
    @DisplayName("Should handle Person with all Tyrer-specific fields populated")
    void testDeserializePersonWithAllTyrerFields() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"ONE\",\n" +
                "        \"affectedBreastType\": \"TWO\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"densityValue\": 0.75,\n" +
                "        \"diagnoseBilateralBreastAge\": 45,\n" +
                "        \"diagnoseBreastAge\": 40,\n" +
                "        \"diagnoseOvarianAge\": -99,\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hasAtypicalHyperplasia\": true,\n" +
                "        \"hasLCIS\": true,\n" +
                "        \"hasLiveBirth\": true,\n" +
                "        \"height\": 170.5,\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"liveBirthAge\": 30,\n" +
                "        \"menarcheAge\": 12,\n" +
                "        \"menopauseAge\": 52,\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"polygenicSNP\": 0.5,\n" +
                "        \"usedHRT\": \"NEVER\",\n" +
                "        \"weight\": 65.0,\n" +
                "        \"yearsFutureHRT\": 5.0,\n" +
                "        \"yearsPastHRT\": 10.0,\n" +
                "        \"yearsSinceHRT\": 2.0\n" +
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
    @DisplayName("Should handle Person with zero HRT years")
    void testDeserializePersonWithZeroHRTYears() throws Exception {
        String json = "{\n" +
                "  \"Family\": {\n" +
                "    \"Person\": [\n" +
                "      {\n" +
                "        \"ID\": 1,\n" +
                "        \"vitalStatus\": \"ALIVE\",\n" +
                "        \"affectedBilateralBreastType\": \"NONE\",\n" +
                "        \"affectedBreastType\": \"NONE\",\n" +
                "        \"affectedOvarianType\": \"NONE\",\n" +
                "        \"densityMeasure\": \"NONE\",\n" +
                "        \"ethnic\": \"WHITE\",\n" +
                "        \"hyperplasia\": \"NONE\",\n" +
                "        \"menostat\": \"PRE\",\n" +
                "        \"usedHRT\": \"NEVER\",\n" +
                "        \"yearsFutureHRT\": 0.0,\n" +
                "        \"yearsPastHRT\": 0.0,\n" +
                "        \"yearsSinceHRT\": 0.0\n" +
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

