package com.genomebrain.backend.service.dto;

public enum CustomQuestionConstants {

    AMBRY126_001("Yes"),
    AMBRY126_002("No"),
//    AMBRY127_002("Unknown"),
    AMBRY127_003("Not applicable"),
    AMBRY128_001("Yes"),
    AMBRY128_002("No"),
//    AMBRY129_002("Unknown"),
    AMBRY129_003("Not applicable"),
    AMBRY130_001("Yes"),
    AMBRY130_002("No"),
    AMBRY131_002("Left"),
    AMBRY131_001("Right"),
    AMBRY131_003("Both"),
    AMBRY132_002("Unknown"),
    AMBRY132_003("Not applicable"),
    AMBRY133_001("Yes"),
    AMBRY133_002("No"),
    AMBRY135_001("Yes"),
    AMBRY135_002("No"),
    AMBRY137_002("Left"),
    AMBRY137_001("Right"),
    AMBRY137_003("Both"),
    AMBRY138_001("I am currently taking oral contraceptives"),
    AMBRY138_002("I have taken oral contraceptives in the past"),
    AMBRY138_003("I have never taken oral contraceptives"),
    CARQ1_001("Yes"),
    CARQ1_002("No"),
    CARQ2_002("I don't know"),
    CARQ2_003("Not applicable"),
    CARQ3_001("Yes"),
    CARQ3_002("No"),
    AMBRY154_002("None"),
    AMBRY158_001("Bilateral"),
    AMBRY158_002("R ovary only"),
    AMBRY158_003("L ovary only"),
    AMBRY150_001("Lumpectomy - "),
    AMBRY151_001("Mastectomy - "),
    AMBRY152_001("Breast reduction and/or lift - ");



    private String value;

    public String getValue() {
        return this.value;
    }

    CustomQuestionConstants(String value) {
        this.value = value;
    }
}

