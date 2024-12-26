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
import com.objex.risk.beans.tcuzick.Person;
import com.objex.risk.beans.tcuzick.Risk;

public class TyrerDeserializer extends JsonDeserializer<Risk> {

    private static final Logger logger = LoggerFactory.getLogger(TyrerDeserializer.class);

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
        person.setAge(ProgenyDeserializerHelper.getIntValue(node, "age", 0));
        person.setAgeOfDeath(ProgenyDeserializerHelper.getIntValue(node, "ageOfDeath", 0));
        person.setFatherId(ProgenyDeserializerHelper.getIntValue(node, "fatherID", 0));
        person.setGender(ProgenyDeserializerHelper.getIntValue(node, "gender", 0));
        person.setId(ProgenyDeserializerHelper.getIntValue(node, "ID", 0));
        person.setMotherId(ProgenyDeserializerHelper.getIntValue(node, "motherID", 0));
        person.setProband(ProgenyDeserializerHelper.getBooleanValue(node, "proband", false));
        person.setTestOrder(ProgenyDeserializerHelper.getIntValue(node, "testOrder", 0));
        person.setTwin(ProgenyDeserializerHelper.getIntValue(node, "twin", 0));
        person.setVitalStatus(ProgenyDeserializerHelper.parseVitalStatus(node.get("vitalStatus").asText()));
        
        person.setAffectedBilateralBreastType(ProgenyDeserializerHelper.parseAffectedType(node.get("affectedBilateralBreastType").asText()));
        person.setAffectedBreastType(ProgenyDeserializerHelper.parseAffectedType(node.get("affectedBreastType").asText()));
        person.setAffectedOvarianType(ProgenyDeserializerHelper.parseAffectedType(node.get("affectedOvarianType").asText()));

        person.setDensityMeasure(ProgenyDeserializerHelper.parseDensityMeasure(node.get("densityMeasure").asText()));
        person.setDensityValue(node.has("densityValue") ? node.get("densityValue").asDouble(-99.0) : -99.0);
        person.setDiagnoseBilateralBreastAge(ProgenyDeserializerHelper.getIntValue(node, "diagnoseBilateralBreastAge", -99));
        person.setDiagnoseBreastAge(ProgenyDeserializerHelper.getIntValue(node, "diagnoseBreastAge", -99));
        person.setDiagnoseOvarianAge(ProgenyDeserializerHelper.getIntValue(node, "diagnoseOvarianAge", -99));
        person.setEthnic(ProgenyDeserializerHelper.parseEthnic(node.get("ethnic").asText()));
        
        person.setHasAtypicalHyperplasia(ProgenyDeserializerHelper.getBooleanValue(node, "hasAtypicalHyperplasia", false));
        person.setHasLCIS(ProgenyDeserializerHelper.getBooleanValue(node, "hasLCIS", false));
        person.setHasLiveBirth(ProgenyDeserializerHelper.getBooleanValue(node, "hasLiveBirth", false));
        person.setHeight(node.has("height") ? node.get("height").asDouble(-99.0) : -99.0);
        person.setHyperplasia(ProgenyDeserializerHelper.parseHyperplasia(node.get("hyperplasia").asText()));
        person.setLiveBirthAge(ProgenyDeserializerHelper.getIntValue(node, "liveBirthAge", -99));
        person.setMenarcheAge(ProgenyDeserializerHelper.getIntValue(node, "menarcheAge", -99));
        person.setMenopauseAge(ProgenyDeserializerHelper.getIntValue(node, "menopauseAge", -99));
        person.setMenostat(ProgenyDeserializerHelper.parseMenostat(node.get("menostat").asText()));

        person.setPolygenicSNP(node.has("polygenicSNP") ? node.get("polygenicSNP").asDouble(0.0) : 0.0);
        person.setUsedHRT(ProgenyDeserializerHelper.parseUsedHRT(node.get("usedHRT").asText()));
        person.setWeight(node.has("weight") ? node.get("weight").asDouble(-99.0) : -99.0);
        person.setYearsFutureHRT(node.has("yearsFutureHRT") ? node.get("yearsFutureHRT").asDouble(0.0) : 0.0);
        person.setYearsPastHRT(node.has("yearsPastHRT") ? node.get("yearsPastHRT").asDouble(0.0) : 0.0);
        person.setYearsSinceHRT(node.has("yearsSinceHRT") ? node.get("yearsSinceHRT").asDouble(0.0) : 0.0);

        return person;
    }
}
