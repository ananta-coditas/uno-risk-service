package com.genomebrain.backend.service.dto;

import java.util.Arrays;

public enum Ethnicity {
    // Old Ethnicity
    AMBRY003_001_OLD("African American"),
    AMBRY003_002_OLD("Asian"),
    AMBRY003_003_OLD("Caucasian"),
    AMBRY003_004_OLD("French Canadian/Cajun"),
    AMBRY003_005_OLD("Hispanic/Latino"),
    AMBRY003_006_OLD("Jewish (Ashkenazi)"),
    AMBRY003_007_OLD("Jewish (Sephardic, non-Ashkenazi)"),
    AMBRY003_008_OLD("Middle Eastern"),
    AMBRY003_009_OLD("Native American"),
    AMBRY003_010_OLD("Portuguese"),
    AMBRY003_011_OLD("Other/not listed"),
    // TODO: confirm value to be unknown or I don't know
    AMBRY003_012_OLD("Unknown"),

    // New Ethnicity
    AMBRY003_001("Black/African American"),
    AMBRY003_002("Asian"),
    AMBRY003_003("White"),
    AMBRY003_004("French Canadian/Cajun"),
    AMBRY003_005("Hispanic/Latino"),
    AMBRY003_006("Ashkenazi Jewish"),
    AMBRY003_007("Jewish (Sephardic, non-Ashkenazi)"),
    AMBRY003_008("Middle Eastern"),
    AMBRY003_009("Native American"),
    AMBRY003_010("Portuguese"),
    AMBRY003_011("Other"),
    AMBRY003_012("Unknown"),
    AMBRY003_013("Mediterranean"),
    AMBRY003_014("Pacific Islander"),
    // Ethnicity sub categories
    // Asian sub categories
    AMBRY003_002_001("Chinese"),
    AMBRY003_002_002("East Asian"),
    AMBRY003_002_003("Filipino"),
    AMBRY003_002_004("Hawaiian"),
    AMBRY003_002_005("Japanese"),
    AMBRY003_002_007("South Asian"),
    AMBRY003_002_008("South-East Asian"),
    // White sub categories
    AMBRY003_003_001("Finnish"),
    AMBRY003_003_002("Northern European"),
    AMBRY003_003_003("Southern European");

    private final String value;

    Ethnicity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValueForName(String name) {
        for (Ethnicity ethnicity : values()) {
            if (ethnicity.name().equalsIgnoreCase(name)) {
                return ethnicity.value;
            }
        }
        throw new IllegalArgumentException("No Ethnicity mapping found for constant:" + name);
    }

    public static Ethnicity ofValue (String value) {
        return Arrays.stream(values()).filter(e -> !e.name().endsWith("_OLD") && e.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}
