package com.ambry.progeny.deserializer;

import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.claus.Risk;

public class ClausDeserializer extends JsonDeserializer<Risk> {

    private static final Logger logger = LoggerFactory.getLogger(ClausDeserializer.class);

    @Override
    public Risk deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode node = mapper.readTree(jp);

            Risk risk = new Risk();
            risk.setAgeBy(getIntValue(node, "ageBy", 0));
            risk.setAgeTo(getIntValue(node, "ageTo", 0));
            risk.setConsent(getBooleanValue(node, "consent", false));
            risk.setFamilyId(getTextValue(node, "familyId", ""));
            risk.setSaveIncoming(getBooleanValue(node, "saveIncoming", false));

            if (node.has("Family") && node.get("Family").has("Person")) {
                JsonNode personListNode = node.get("Family").get("Person");
                if (personListNode.isArray()) {
                    Iterator<JsonNode> personIterator = personListNode.elements();
                    while (personIterator.hasNext()) {
                        JsonNode personNode = personIterator.next();
                        Person person = deserializePerson(personNode);
                        risk.add(person);
                    }
                }
            }
            return risk;
        } catch (Exception e) {
            logger.error("Error deserializing Risk object: {}", e.getMessage(), e);
            throw new IOException("Failed to deserialize Risk object.", e);
        }
    }

    private Person deserializePerson(JsonNode node) {
        Person person = new Person();
        person.setAge(getIntValue(node, "age", 0));
        person.setAgeOfDeath(getIntValue(node, "ageOfDeath", 0));
        person.setFatherId(getIntValue(node, "fatherID", 0));
        person.setMotherId(getIntValue(node, "motherID", 0));
        person.setGender(getIntValue(node, "gender", 0));
        person.setId(getIntValue(node, "ID", -1));
        person.setTestOrder(getIntValue(node, "testOrder", 0));
        person.setTwin(getIntValue(node, "twin", 0));
        person.setProband(getBooleanValue(node, "proband", false));

        String vitalStatus = getTextValue(node, "vitalStatus", null);
        if (vitalStatus != null) {
            person.setVitalStatus(parseVitalStatus(vitalStatus));
        }

        String affectedBreastType = getTextValue(node, "affectedBreastType", null);
        if (affectedBreastType != null) {
            person.setAffectedBreastType(parseAffectedType(affectedBreastType));
        }

        person.setDiagnoseBreastAge(getIntValue(node, "diagnoseBreastAge", 0));
        person.setAffectedOvarianType(getIntValue(node, "affectedOvarianType", 0));
        person.setDiagnoseOvarianAge(getIntValue(node, "diagnoseOvarianAge", 0));
        return person;
    }

    private BasePerson.VitalStatus parseVitalStatus(String value) {
        try {
            return BasePerson.VitalStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid vitalStatus value: {}", value);
            return null;
        }
    }

    private AffectedType parseAffectedType(String value) {
        try {
            return AffectedType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }

    private int getIntValue(JsonNode node, String fieldName, int defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isInt() ? node.get(fieldName).asInt() : defaultValue;
    }

    private boolean getBooleanValue(JsonNode node, String fieldName, boolean defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isBoolean() ? node.get(fieldName).asBoolean() : defaultValue;
    }

    private String getTextValue(JsonNode node, String fieldName, String defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isTextual() ? node.get(fieldName).asText() : defaultValue;
    }
}