package com.genomebrain.backend.service.dto;

public enum Program {

    ONCOLOGY("_HC_", "HC", "Hereditary Cancer", "HC"),
    CARE_FOR_PHARMA("_Pharma_", "Pharma", "Pharma", "Pharma"),
    WOMENES_HEALTH("_WH_", "WH", "Reproductive Health", "WH"),
    WOMENES_HEALTH_NIPT("_WH_NIPT_", "WH_NIPT", "Reproductive Health", "WH"),
    WOMENES_HEALTH_CS("_WH_CS_", "WH_CS", "Reproductive Health", "WH");

    private final String[] values;

    Program(String... values) {
        this.values = values;
    }

    //The value in this format is stored in the DB
    public String getProgram() {
        return values[0];
    }

    //The value in this format is returned to UI
    public String getProgramResponse() {
        return values[1];
    }

    public String getProgramDisplayName() {
        return values[2];
    }

    //The value in this format is used to form a map of main program as Key and subprogram as Value
    // (Ex: WH_NIPT => {"WH": ["NIPT"]}, WH => {"WH": ["NIPT", "CS"]})
    public String getProgramKey() {
        return values[3];
    }

    public static Program getPrograms(final String docType) {
        for (Program program : Program.values()) {
            if (docType.equalsIgnoreCase(program.toString())) {
                return program;
            }
        }
        return null;
    }

    public static Program getProgramByValue(final String value) {
        for (Program program : Program.values()) {
            if (value.equalsIgnoreCase(program.getProgram())) {
                return program;
            }
        }
        return null;
    }

    public static Program getProgramByResponse(final String response) {
        for (Program program : Program.values()) {
            if (response.equalsIgnoreCase(program.getProgramResponse())) {
                return program;
            }
        }
        return null;
    }
}
