package com.genomebrain.backend.service.dto;

public enum ReasoningStatementConstants {

    HBOCRS1("Personal history of breast cancer diagnosed at age ≤50y"),

    HBOCRS3("Personal history of breast cancer at any age and another synchronous or metachronous breast cancer at any age"),

    HBOCRS25("Personal history of prostate cancer and ≥2 close blood relatives with breast and/or prostate cancer at any age on the same side of the family"),

    HBOCRS27("Family history of ≥1 first- or second-degree blood relative with breast cancer and another synchronous or metachronous breast cancer at any age"),

    HBOCRS28("Family history of breast cancer diagnosed at 46-50y in a first- or second- degree blood relative and ≥1 close blood relative with breast, ovarian (including fallopian tube or peritoneal cancer), pancreatic, or prostate cancer at any age on the same side of the family"),

    HBOCRS30("Family history of breast cancer diagnosed at any age in a first- or second- degree blood relative and ≥1 close blood relative with breast cancer diagnosed ≤50y on the same side of the family or with male breast cancer at any age"),

    HBOCRS31("Family history of breast cancer diagnosed at any age in a first- or second- degree blood relative and ≥1 close blood relative with ovarian cancer (including fallopian tube or peritoneal cancer) at any age on the same side of the family"),

    HBOCRS32("Family history of breast cancer diagnosed at any age in a first- or second- degree blood relative and ≥1 close blood relative with pancreatic cancer at any age on the same side of the family"),

    HBOCRS33("Family history of breast cancer diagnosed at any age in a first- or second- degree blood relative and ≥1 close blood relative with metastatic or high-grade prostate cancer at any age on the same side of the family"),

    HBOCRS34("Family history of breast cancer diagnosed at any age in a first- or second- degree blood relative with ≥3 total diagnoses of breast cancer on the same side of the family"),

    HBOCRS40("Family history of prostate cancer at any age in a first-degree blood relative and ≥1 close blood relative with breast cancer ≤50y on the same side of the family"),

    HBOCRS41("Family history of prostate cancer at any age in a first-degree blood relative and ≥1 close blood relative with ovarian cancer (including fallopian tube or peritoneal cancer) at any age on the same side of the family"),

    HBOCRS43("Family history of prostate cancer in a first-degree blood relative and ≥2 close blood relatives with breast and/or prostate cancer at any age on the same side of the family"),

    HBOCRS44("Family history of prostate cancer at any age in a first-degree blood relative and ≥1 close blood relative with metastatic or high-risk prostate cancer at any age on the same side of the family"),

    HBOCRS42("Family history of prostate cancer at any age in a first-degree blood relative and ≥1 close blood relative with pancreatic cancer at any age on the same side of the family"),

    HBOCRS48("Family history of prostate cancer in a first- or second- degree blood relative and ≥2 close blood relatives (including proband) with breast and/or prostate cancer at any age on the same side of the family"),

    HBOCRS49("Personal history of breast cancer diagnosed at any age and ≥2 close blood relatives with breast and/or prostate cancer at any age on the same side of the family"),

    HBOCRS50("Family history of breast cancer diagnosed at any age in a first- or second-degree blood relative and ≥2 close blood relatives with breast and/or prostate cancer at any age on the same side of the family"),

    HBOCRS51("Personal history of lobular breast cancer and diffuse gastric cancer diagnosed at any age"),

    HBOCRS52("Personal history of lobular breast cancer and ≥1 close blood relative with diffuse gastric cancer"),

    HBOCRS53("Personal history of breast cancer and ≥1 close blood relative with male breast cancer"),

    HBOCRS54("Family history of breast cancer in a first- or second-degree blood relative and ≥1 close blood relative with male breast cancer"),

    HBOCRS55("Personal history of prostate cancer and ≥1 close blood relative with triple negative breast cancer at any age"),

    HBOCRS56("Personal history of prostate cancer and ≥1 close blood relative with male breast cancer at any age"),

    HBOCRS57("Family history of prostate cancer at any age in a first-degree blood relative and ≥1 close blood relative with triple negative breast cancer at any age on the same side of the family"),

    HBOCRS58("Family history of prostate cancer at any age in a first-degree blood relative and ≥1 close blood relative with male breast cancer at any age on the same side of the family"),

    CRCRS1("Personal history of ≥10 adenomatous colorectal polyps"),

    CRCRS5("Personal history of a Lynch syndrome-related cancer at any age and another synchronous or metachronous Lynch syndrome-related cancer at any age"),

    CRCRS6("Personal history of a Lynch syndrome-related cancer at any age and ≥1 first- or second- degree blood relative with Lynch syndrome-related cancer diagnosed <50y"),

    CRCRS7("Personal history of a Lynch syndrome-related cancer at any age and ≥2 first- or second- degree blood relatives with Lynch syndrome-related cancers at any age on the same side of the family"),

    CRCRS9("Family history of ≥1 first-degree blood relative with colorectal or endometrial cancer and another synchronous or metachronous Lynch syndrome-related cancer at any age"),

    CRCRS10("Family history of ≥2 first- or second- degree blood relatives with Lynch syndrome-related cancers on the same side of the family, with at least 1 diagnosed <50y"),

    CRCRS11("Family history of ≥3 first- or second- degree blood relatives with Lynch syndrome-related cancers at any age on the same side of the family"),

    CRCRS12("Personal history of desmoid tumor"),

    CRCRS13 ("Personal history of a Lynch syndrome-related cancer <50y") ,

    CRCRS14("Family history of ≥2 first- or second- degree blood relatives with gastric cancer, with at least one diagnosed ≤50y or at least one confirmed as diffuse gastric cancer"),

    CRCRS15("Personal history of diffuse gastric cancer at any age");

    private String value;

    public String getValue() {
        return this.value;
    }

    ReasoningStatementConstants(String value) {
        this.value = value;
    }
}

