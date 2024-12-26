package com.ambry.progeny.deserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson.VitalStatus;
import com.objex.risk.beans.tcuzick.Person.DensityMeasure;
import com.objex.risk.beans.tcuzick.Person.Ethnic;
import com.objex.risk.beans.tcuzick.Person.HRTUse;
import com.objex.risk.beans.tcuzick.Person.HyperplasiaType;
import com.objex.risk.beans.tcuzick.Person.MenosType;

public class ProgenyDeserializerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ProgenyDeserializerHelper.class);

    public static AffectedType parseAffectedType(String value) {
        if (value == null)
            return null;
        try {
            return AffectedType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }

    public static int getIntValue(JsonNode node, String fieldName, int defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isInt() ? node.get(fieldName).asInt() : defaultValue;
    }

    public static boolean getBooleanValue(JsonNode node, String fieldName, boolean defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isBoolean() ? node.get(fieldName).asBoolean() : defaultValue;
    }

    public static String getTextValue(JsonNode node, String fieldName, String defaultValue) {
        return node.has(fieldName) && node.get(fieldName).isTextual() ? node.get(fieldName).asText() : defaultValue;
    }
    
    public static VitalStatus parseVitalStatus(String value) {
        if (value == null)
            return null;
        try {
            return VitalStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }
    
    public static DensityMeasure parseDensityMeasure(String value) {
        if (value == null)
            return null;
        try {
            return DensityMeasure.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }

    public static Ethnic parseEthnic(String value) {
        if (value == null)
            return null;
        try {
            return Ethnic.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }

    public static HyperplasiaType parseHyperplasia(String value) {
        if (value == null)
            return null;
        try {
            return HyperplasiaType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }

    public static MenosType parseMenostat(String value) {
        if (value == null)
            return null;
        try {
            return MenosType.valueOf(value);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }

    public static HRTUse parseUsedHRT(String value) {
        if (value == null)
            return null;
        try {
            return HRTUse.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid affectedBreastType value: {}", value);
            return null;
        }
    }
}
