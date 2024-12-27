package com.genomebrain.backend.service.dto;

public enum RelativeConstants {

    //Sides
    MATERNAL("Mother's side"),
    PATERNAL("Father's side"),
    BOTH_SIDES("Both sides"),

    //Relative
    SELF("Self"),
    MOTHER("Mother"),
    FATHER("Father"),
    SISTER("Sister"),
    BROTHER("Brother"),
    UNKNOWN_SISTER_BROTHER("Unknown sister/brother"),
    DAUGHTER("Daughter"),
    SON("Son"),
    UNKNOWN_DAUGHTER_SON("Unknown daughter/son"),
    NIECE("Niece"),
    NEPHEW("Nephew"),
    GRANDFATHER("Grandfather"),
    GRANDMOTHER("Grandmother"),
    GRANDDAUGHTER("Granddaughter"),
    GRANDSON("Grandson"),
    AUNT("Aunt"),
    UNCLE("Uncle"),
    UNKNOWN_AUNT_UNCLE("Unknown aunt/uncle"),
    FIRST_COUSIN_FEMALE("First Cousin (female)"),
    FIRST_COUSIN_MALE("First Cousin (male)"),
    HALF_BROTHER("Half-Brother"),
    HALF_SISTER("Half-Sister"),
    GREAT_UNCLE("Great Uncle"),
    GREAT_AUNT("Great Aunt"),
    GREAT_GRANDMOTHER("Great Grandmother"),
    GREAT_GRANDFATHER("Great Grandfather"),
    UNKNOWN_MAT_GRANDPARENTS("Unknown MatGreatParents"),
    UNKNOWN_PAT_GRANDPARENTS("Unknown PatGreatParents"),
    UNKNOWN_FATHER("Unknown FATHER"),
    UNKNOWN_MOTHER("Unknown MOTHER"),
    GREAT_NIECE("Great Niece"),
    GREAT_NEPHEW("Great Nephew"),
    UNKNOWN_NIECE_NEPHEW("Unknown niece/nephew"),

    GREAT_GRANDDAUGHTER("Great Granddaughter"),
    GREAT_GRANDSON("Great Grandson"),
    HALF_AUNT("Half Aunt"),
    HALF_UNCLE("Half Uncle"),
    HALF_NIECE("Half Niece"),
    HALF_NEPHEW("Half Nephew"),

    UNKNOWN_GRANDSON_GRANDDAUGHTER("Unknown grandson/granddaughter"),
    UNKNOWN_HALF_BROTHER_HALF_SISTER("Unknown half brother/sister"),


    SPOUSE("Spouse"),

    OTHER("Other"),

    PROBAND("Proband");

    private String value;

    public String getValue() {
        return this.value;
    }

    RelativeConstants(String value) {
        this.value = value;
    }
}

