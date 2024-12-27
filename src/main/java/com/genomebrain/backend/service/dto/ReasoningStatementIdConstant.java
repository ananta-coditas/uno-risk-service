package com.genomebrain.backend.service.dto;

public enum ReasoningStatementIdConstant {

    HBOCRS1("HBOCRS1"),
    HBOCRS3("HBOCRS3"),
    HBOCRS25("HBOCRS25"),
    HBOCRS28("HBOCRS28"),
    HBOCRS30("HBOCRS30"),
    HBOCRS31("HBOCRS31"),
    HBOCRS32("HBOCRS32"),
    HBOCRS33("HBOCRS33"),
    HBOCRS34("HBOCRS34"),
    HBOCRS40("HBOCRS40"),
    HBOCRS41("HBOCRS41"),
    HBOCRS42("HBOCRS42"),
    HBOCRS43("HBOCRS43"),
    HBOCRS44("HBOCRS44"),
    HBOCRS49("HBOCRS49"),
    HBOCRS50("HBOCRS50"),
    HBOCRS51("HBOCRS51"),
    HBOCRS52("HBOCRS52"),
    HBOCRS53("HBOCRS53"),
    HBOCRS54("HBOCRS54"),
    HBOCRS55("HBOCRS55"),
    HBOCRS56("HBOCRS56"),
    HBOCRS57("HBOCRS57"),
    HBOCRS58("HBOCRS58"),
    CRCRS1("CRCRS1"),
    CRCRS5("CRCRS5"),
    CRCRS6("CRCRS6"),
    CRCRS7("CRCRS7"),
    CRCRS10("CRCRS10"),
    CRCRS11("CRCRS11"),
    CRCRS12("CRCRS12"),
    CRCRS13("CRCRS13"),
    CRCRS14("CRCRS14"),
    CRCRS15("CRCRS15");


    private final String reasoningStatementId;

    ReasoningStatementIdConstant(String reasoningStatementId) {
        this.reasoningStatementId = reasoningStatementId;
    }

    public String getReasoningStatementId() {
        return this.reasoningStatementId;
    }
}
