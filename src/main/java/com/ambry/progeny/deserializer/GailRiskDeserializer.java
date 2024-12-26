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
import com.objex.risk.beans.gail.Person;
import com.objex.risk.beans.gail.Person.Race;
import com.objex.risk.beans.gail.Risk;

public class GailRiskDeserializer extends JsonDeserializer<Risk> {

    private static final Logger logger = LoggerFactory.getLogger(GailRiskDeserializer.class);

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

    private Person deserializePerson(JsonNode node) {
        Person person = new Person();
        person.setAge(ProgenyDeserializerHelper.getIntValue(node, "age", 0));
        person.setAgeOfDeath(ProgenyDeserializerHelper.getIntValue(node, "ageOfDeath", 0));
        person.setFatherId(ProgenyDeserializerHelper.getIntValue(node, "fatherID", 0));
        person.setMotherId(ProgenyDeserializerHelper.getIntValue(node, "motherID", 0));
        person.setGender(ProgenyDeserializerHelper.getIntValue(node, "gender", 0));
        person.setId(ProgenyDeserializerHelper.getIntValue(node, "ID", -1));
        person.setTestOrder(ProgenyDeserializerHelper.getIntValue(node, "testOrder", 0));
        person.setTwin(ProgenyDeserializerHelper.getIntValue(node, "twin", 0));
        person.setProband(ProgenyDeserializerHelper.getBooleanValue(node, "proband", false));

        person.setRace(parseRace(ProgenyDeserializerHelper.getTextValue(node, "race", null)));

        person.setVitalStatus(parseVitalStatus(ProgenyDeserializerHelper.getTextValue(node, "vitalStatus", null)));

        person.setAffectedBreastType(ProgenyDeserializerHelper.parseAffectedType(ProgenyDeserializerHelper.getTextValue(node, "affectedBreastType", null)));

        person.setBiopsyCount(ProgenyDeserializerHelper.getIntValue(node, "biopsyCount", 0));
        person.setHasDCIS(ProgenyDeserializerHelper.getBooleanValue(node, "hasDCIS", false));
        person.setHasHyperplasia(ProgenyDeserializerHelper.getBooleanValue(node, "hasHyperplasia", false));
        person.setHasLCIS(ProgenyDeserializerHelper.getBooleanValue(node, "hasLCIS", false));
        person.setHasLiveBirth(ProgenyDeserializerHelper.getBooleanValue(node, "hasLiveBirth", false));
        person.setHasMantleRadiation(ProgenyDeserializerHelper.getBooleanValue(node, "hasMantleRadiation", false));
        person.setLiveBirthAge(ProgenyDeserializerHelper.getIntValue(node, "liveBirthAge", 0));
        person.setMenarcheAge(ProgenyDeserializerHelper.getIntValue(node, "menarcheAge", 0));
        person.setPerformedBiopsy(ProgenyDeserializerHelper.getBooleanValue(node, "performedBiopsy", false));

        return person;
    }

    private Race parseRace(String value) {
        if (value == null) return Race.Unsupported;
        try {
            return Race.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid race value: {}", value);
            return Race.Unsupported;
        }
    }

    private BasePerson.VitalStatus parseVitalStatus(String value) {
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
}