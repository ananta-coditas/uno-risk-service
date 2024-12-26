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
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.premm.Person;
import com.objex.risk.beans.premm.Risk;

public class PremmDeserializer extends JsonDeserializer<Risk> {

    private static final Logger logger = LoggerFactory.getLogger(PremmDeserializer.class);

    @Override
    public Risk deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode node = mapper.readTree(jp);

            Risk risk = new Risk();
            risk.setAgeBy(ProgenyDeserializerHelper.getIntValue(node, "ageBy", 0));
            risk.setAgeTo(ProgenyDeserializerHelper.getIntValue(node, "ageTo", 0));
            risk.setConsent(ProgenyDeserializerHelper.getBooleanValue(node, "consent", false));
            risk.setFamilyId(ProgenyDeserializerHelper.getTextValue(node, "familyId", ""));
            risk.setSaveIncoming(ProgenyDeserializerHelper.getBooleanValue(node, "saveIncoming", false));

            if (node.has("Family")) {
                JsonNode familyNode = node.get("Family");
                if (familyNode.has("Person")) {
                    JsonNode personListNode = familyNode.get("Person");
                    if (personListNode.isArray()) {
                        Iterator<JsonNode> personIterator = personListNode.elements();
                        while (personIterator.hasNext()) {
                            JsonNode personNode = personIterator.next();
                            Person person = deserializePerson(personNode);
                            risk.add(person);
                        }
                    }
                }
            }
            return risk;
        } catch (Exception e) {
            logger.error("Error deserializing Risk object: {}", e.getMessage(), e);
            throw new IOException("Failed to deserialize Risk object.", e);
        }
    }

    private Person deserializePerson(JsonNode personNode) {
        Person person = new Person();
        person.setAge(ProgenyDeserializerHelper.getIntValue(personNode, "age", 0));
        person.setAgeOfDeath(ProgenyDeserializerHelper.getIntValue(personNode, "ageOfDeath", 0));
        person.setFatherId(ProgenyDeserializerHelper.getIntValue(personNode, "fatherID", 0));
        person.setGender(ProgenyDeserializerHelper.getIntValue(personNode, "gender", 0));
        person.setId(ProgenyDeserializerHelper.getIntValue(personNode, "ID", 0));
        person.setMotherId(ProgenyDeserializerHelper.getIntValue(personNode, "motherID", 0));
        person.setProband(ProgenyDeserializerHelper.getBooleanValue(personNode, "proband", false));
        person.setTestOrder(ProgenyDeserializerHelper.getIntValue(personNode, "testOrder", 0));
        person.setTwin(ProgenyDeserializerHelper.getIntValue(personNode, "twin", 0));
        String vitalStatus = ProgenyDeserializerHelper.getTextValue(personNode, "vitalStatus", null);
        if (vitalStatus != null) {
            person.setVitalStatus(parseVitalStatus(vitalStatus));
        }
        person.setAffectedColonType(ProgenyDeserializerHelper.parseAffectedType(
                ProgenyDeserializerHelper.getTextValue(personNode, "affectedColonType", "NONE")));
        person.setAffectedEndometriumType(ProgenyDeserializerHelper.parseAffectedType(
                ProgenyDeserializerHelper.getTextValue(personNode, "affectedEndometriumType", "NONE")));
        person.setAffectedRelatedType(ProgenyDeserializerHelper.parseAffectedType(
                ProgenyDeserializerHelper.getTextValue(personNode, "affectedRelatedType", "NONE")));

        person.setDiagnoseColonAge(ProgenyDeserializerHelper.getIntValue(personNode, "diagnoseColonAge", 0));
        person.setDiagnoseEndometriumAge(
                ProgenyDeserializerHelper.getIntValue(personNode, "diagnoseEndometriumAge", 0));
        return person;
    }

    private BasePerson.VitalStatus parseVitalStatus(String value) {
        if (value == null)
            return null;
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
}
