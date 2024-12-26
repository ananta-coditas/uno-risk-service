package com.ambry.progeny.deserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.claus.Risk;

public abstract class ProgenyDeserializer<T> extends JsonDeserializer<T> {

    private static final Logger logger = LoggerFactory.getLogger(ProgenyDeserializer.class);

    public ProgenyDeserializer() {
        super();
    }

    int getIntValue(JsonNode node, String fieldName, int defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isInt() ? node.get(fieldName).asInt() : defaultValue;
    }

    boolean getBooleanValue(JsonNode node, String fieldName, boolean defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isBoolean() ? node.get(fieldName).asBoolean() : defaultValue;
    }

    String getTextValue(JsonNode node, String fieldName, String defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isTextual() ? node.get(fieldName).asText() : defaultValue;
    }
    
    BasePerson.VitalStatus parseVitalStatus(String value) {
        if (value == null) return null;
        switch (value.toUpperCase()) {
            case "ALIVE":
                return BasePerson.VitalStatus.ALIVE;
            case "DEAD":
                return BasePerson.VitalStatus.DEAD;
            default:
                logger.warn("Invalid vitalStatus value: {}", value);
                return null;
        }
    }

    AffectedType parseAffectedType(String value) {
        if (value == null) return null;
        try {
            return AffectedType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }
}