package com.ambry.progeny.deserializer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson.VitalStatus;

@DisplayName("ProgenyDeserializer Unit Tests")
class ProgenyDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should get int value from JsonNode")
    void testGetIntValue() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{\"age\": 30}");
        int result = deserializer.testGetIntValue(node, "age", 0);
        assertThat(result).isEqualTo(30);
    }

    @Test
    @DisplayName("Should return default value when int field is missing")
    void testGetIntValueMissing() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{}");
        int result = deserializer.testGetIntValue(node, "age", 0);
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should get boolean value from JsonNode")
    void testGetBooleanValue() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{\"proband\": true}");
        boolean result = deserializer.testGetBooleanValue(node, "proband", false);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return default value when boolean field is missing")
    void testGetBooleanValueMissing() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{}");
        boolean result = deserializer.testGetBooleanValue(node, "proband", false);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should get text value from JsonNode")
    void testGetTextValue() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{\"familyId\": \"FAM001\"}");
        String result = deserializer.testGetTextValue(node, "familyId", "");
        assertThat(result).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should return default value when text field is missing")
    void testGetTextValueMissing() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{}");
        String result = deserializer.testGetTextValue(node, "familyId", "");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should parse valid VitalStatus")
    void testParseVitalStatusValid() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        VitalStatus result = deserializer.testParseVitalStatus("ALIVE");
        assertThat(result).isEqualTo(VitalStatus.ALIVE);
    }

    @Test
    @DisplayName("Should parse VitalStatus case-insensitively")
    void testParseVitalStatusCaseInsensitive() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        VitalStatus result = deserializer.testParseVitalStatus("dead");
        assertThat(result).isEqualTo(VitalStatus.DEAD);
    }

    @Test
    @DisplayName("Should return null for null VitalStatus")
    void testParseVitalStatusNull() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        VitalStatus result = deserializer.testParseVitalStatus(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return null for invalid VitalStatus")
    void testParseVitalStatusInvalid() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        VitalStatus result = deserializer.testParseVitalStatus("INVALID_STATUS");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should parse valid AffectedType")
    void testParseAffectedTypeValid() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        AffectedType result = deserializer.testParseAffectedType("NONE");
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should parse AffectedType case-insensitively")
    void testParseAffectedTypeCaseInsensitive() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        AffectedType result = deserializer.testParseAffectedType("none");
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should return null for null AffectedType")
    void testParseAffectedTypeNull() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        AffectedType result = deserializer.testParseAffectedType(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return null for invalid AffectedType")
    void testParseAffectedTypeInvalid() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        AffectedType result = deserializer.testParseAffectedType("INVALID_TYPE");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return default value when int field is not int type")
    void testGetIntValueWrongType() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{\"age\": \"not-a-number\"}");
        int result = deserializer.testGetIntValue(node, "age", 0);
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return default value when boolean field is not boolean type")
    void testGetBooleanValueWrongType() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{\"proband\": \"not-a-boolean\"}");
        boolean result = deserializer.testGetBooleanValue(node, "proband", false);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return default value when text field is not textual type")
    void testGetTextValueWrongType() throws Exception {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        JsonNode node = objectMapper.readTree("{\"familyId\": 123}");
        String result = deserializer.testGetTextValue(node, "familyId", "");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should parse VitalStatus with mixed case")
    void testParseVitalStatusMixedCase() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        VitalStatus result = deserializer.testParseVitalStatus("AlIvE");
        assertThat(result).isEqualTo(VitalStatus.ALIVE);
    }

    @Test
    @DisplayName("Should parse AffectedType with mixed case")
    void testParseAffectedTypeMixedCase() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        AffectedType result = deserializer.testParseAffectedType("NoNe");
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should handle empty string for VitalStatus")
    void testParseVitalStatusEmptyString() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        VitalStatus result = deserializer.testParseVitalStatus("");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle empty string for AffectedType")
    void testParseAffectedTypeEmptyString() {
        TestProgenyDeserializer deserializer = new TestProgenyDeserializer();
        AffectedType result = deserializer.testParseAffectedType("");
        assertThat(result).isNull();
    }

    // Test implementation to access protected methods
    private static class TestProgenyDeserializer extends ProgenyDeserializer<Object> {
        @Override
        public Object deserialize(com.fasterxml.jackson.core.JsonParser p, 
                                   com.fasterxml.jackson.databind.DeserializationContext ctxt) {
            return null;
        }

        public int testGetIntValue(JsonNode node, String fieldName, int defaultValue) {
            return getIntValue(node, fieldName, defaultValue);
        }

        public boolean testGetBooleanValue(JsonNode node, String fieldName, boolean defaultValue) {
            return getBooleanValue(node, fieldName, defaultValue);
        }

        public String testGetTextValue(JsonNode node, String fieldName, String defaultValue) {
            return getTextValue(node, fieldName, defaultValue);
        }

        public VitalStatus testParseVitalStatus(String value) {
            return parseVitalStatus(value);
        }

        public AffectedType testParseAffectedType(String value) {
            return parseAffectedType(value);
        }
    }
}

