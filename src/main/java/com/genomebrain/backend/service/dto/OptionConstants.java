package com.genomebrain.backend.service.dto;

public enum OptionConstants {

    //Gender
    AMBRY002_001("Male"),
    AMBRY002_002("Female"),


    //Ethnicity
    //Ethnicity Tags
    AMBRY003_001_OLD("African American"),
    AMBRY003_002_OLD("Asian"),
    AMBRY003_003_OLD("Caucasian"),
    AMBRY003_004_OLD("French Canadian/Cajun"),
    AMBRY003_005_OLD("Hispanic"),
    AMBRY003_006_OLD("Jewish (Ashkenazi)"),
    AMBRY003_007_OLD("Jewish (Sephardic, non-Ashkenazi)"),
    AMBRY003_008_OLD("Middle Eastern"),
    AMBRY003_009_OLD("Native American"),
    AMBRY003_010_OLD("Portuguese"),
    AMBRY003_011_OLD("Other, not listed"),
    AMBRY003_012_OLD(Constants.UNKNOWN),

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
    AMBRY003_012(Constants.UNKNOWN),
    AMBRY003_013("Mediterranean"),
    AMBRY003_014("Pacific Islander"),

    //Ashkenazi Ethnicity
    AMBRY113_001("Mother's side"),
    AMBRY113_002("Father's side"),
    AMBRY113_003("Both sides"),

    //Ethnicity sub categories
    //Asian sub categories
    AMBRY003_002_001("Chinese"),
    AMBRY003_002_002("East Asian"),
    AMBRY003_002_003("Filipino"),
    AMBRY003_002_004("Hawaiian"),
    AMBRY003_002_005("Japanese"),
    AMBRY003_002_007("South Asian"),
    AMBRY003_002_008("South-East Asian"),
    //White sub categories
    AMBRY003_003_001("Finnish"),
    AMBRY003_003_002("Northern European"),
    AMBRY003_003_003("Southern European"),

    //TyrerCuzick

    AMBRY094_002(Constants.UNKNOWN),
    AMBRY094_003("Not applicable"),

    AMBRY097_002("Nulliparous"),

    AMBRY098_001("Pre-menopausal"),
    AMBRY098_002("Peri-menopausal"),
    AMBRY098_003("Post-menopausal"),
    AMBRY098_004(Constants.UNKNOWN),

    AMBRY099_002(Constants.UNKNOWN),

    AMBRY100_001("No"),
    AMBRY100_002("Yes"),
    AMBRY100_003(Constants.UNKNOWN),


    AMBRY101_001("Current User"),
    AMBRY101_002("Less than 5 years ago"),
    AMBRY101_003("5 or more years ago"),
    AMBRY101_004(Constants.UNKNOWN),


    AMBRY102_001("1 year"),
    AMBRY102_002("2 years"),
    AMBRY102_003("3 years"),
    AMBRY102_004("4 years"),
    AMBRY102_005("5 years"),
    AMBRY102_006("6 years"),
    AMBRY102_007("More than 6 years"),
    AMBRY102_008(Constants.UNKNOWN),

    AMBRY103_002(Constants.UNKNOWN),

    AMBRY104_001("Estrogen-only"),
    AMBRY104_002("Combined"),
    AMBRY104_003(Constants.UNKNOWN),

    AMBRY105_001("Yes"),
    AMBRY105_002("No / no prior biopsy"),

    AMBRY106_001("Hyperplasia (not atypia)"),
    AMBRY106_002("Atypical hyperplasia"),
    AMBRY106_003("LCIS"),
    AMBRY106_004("Atypical hyperplasia and LCIS"),
    AMBRY106_005("Other"),
    AMBRY106_006(Constants.UNKNOWN),
    AMBRY106_007("Benign (Negative/Normal)"),

    AMBRY107_001("Yes"),
    AMBRY107_002("No"),

    AMBRY108_001("Normal- no mutation found"),
    AMBRY108_002("A BRCA1 mutation was found"),
    AMBRY108_003("A BRCA2 mutation was found"),
    AMBRY108_004(Constants.UNKNOWN),

    AMBRY110_001("Less than one year ago"),
    AMBRY110_002("1 year ago"),
    AMBRY110_003("2 years ago"),
    AMBRY110_004("3 years ago"),
    AMBRY110_005("4 years ago"),
    AMBRY110_006("5 years ago"),
    AMBRY110_007("6 or more years ago"),
    AMBRY110_008(Constants.UNKNOWN),


    //Personal History Questions
    //Prostate Cancer
    AMBRY017_001("7 or less"),
    AMBRY017_002("Higher than 7"),
    AMBRY017_003(Constants.UNKNOWN),
    AMBRY018_001("Yes"),
    AMBRY018_002("No"),
    AMBRY018_003(Constants.UNKNOWN),
    AMBRY019_001("Yes"),
    AMBRY019_002("No"),
    AMBRY019_003(Constants.UNKNOWN),

    AMBRY180_001("Yes"),

    AMBRY180_002("No"),

    AMBRY180_003("I don't know"),

    //Brain cancer
    AMBRY031_001("Yes"),
    AMBRY031_002("No"),
    AMBRY031_003(Constants.UNKNOWN),


    //Breast cancer
    AMBRY007_001("No"),
    AMBRY007_002("Yes"),
    AMBRY009_001("DCIS"),
    AMBRY009_002("IDC"),
    AMBRY009_003("ILC"),
    AMBRY009_004("Other"),
    AMBRY009_005(Constants.UNKNOWN),
    AMBRY010_001("Right breast"),
    AMBRY010_002("Left breast"),
    AMBRY010_003("Both breasts (bilateral)"),
    AMBRY011_001("Yes"),
    AMBRY011_002("No"),
    AMBRY011_003(Constants.UNKNOWN),

    //Leukemia cancer
    AMBRY035_001("Yes"),
    AMBRY035_002("No"),
    AMBRY035_003(Constants.UNKNOWN),
    AMBRY036_001("Yes"),
    AMBRY036_002("No"),
    AMBRY036_003(Constants.UNKNOWN),
    AMBRY037_001("Yes"),
    AMBRY037_002("No"),
    AMBRY037_003(Constants.UNKNOWN),

    //Lymphoma cancer


    //Ovarian Cancer
    AMBRY014_001("Yes"),
    AMBRY014_002("No"),
    AMBRY014_003(Constants.UNKNOWN),

    //Colon cancer

    AMBRY020_001("No"),
    AMBRY020_002("Yes"),

    AMBRY022_001("Cecum"),
    AMBRY022_002("Ascending colon"),
    AMBRY022_003("Hepatic flexure"),
    AMBRY022_004("Transverse colon"),
    AMBRY022_005("Splenic flexure"),
    AMBRY022_006("Descending colon"),
    AMBRY022_007("Sigmoid colon"),
    AMBRY022_008("Rectosigmoid junction"),
    AMBRY022_009("Rectum"),
    AMBRY022_010("Overlapping sites of colon"),
    AMBRY022_011("Other area"),
    AMBRY022_012(Constants.UNKNOWN),

    //Family Hisory

    //Breast cancer
    AMBRY057_001("No"),
    AMBRY057_002("Yes"),
    AMBRY057_003(Constants.UNKNOWN),
    AMBRY124_001("Yes"),
    AMBRY124_002("No"),
    AMBRY124_003(Constants.UNKNOWN),
    AMBRY059_001("Yes"),
    AMBRY059_002("No"),
    AMBRY059_003(Constants.UNKNOWN),

    //Prostate Cancer
    AMBRY065_001_previous("Less than 7"),
    AMBRY065_002_previous("7 or higher"),
    AMBRY065_003_previous(Constants.UNKNOWN),
    AMBRY065_001("7 or less"),
    AMBRY065_002("Higher than 7"),
    AMBRY065_003(Constants.UNKNOWN),
    AMBRY066_001("Yes"),
    AMBRY066_002("No"),
    AMBRY066_003(Constants.UNKNOWN),

    //Colon cancer
    AMBRY067_001("No"),
    AMBRY067_002("Yes"),
    AMBRY067_003(Constants.UNKNOWN),

    //Ovarian Cancer
    AMBRY062_001("Yes"),
    AMBRY062_002("No"),
    AMBRY062_003(Constants.UNKNOWN),

    //Skin cancer
    AMBRY121_001("Sebaceous Adenoma or Carcinoma"),
    AMBRY121_002("Melanoma"),
    AMBRY121_003("Basal cell"),
    AMBRY121_004("Squamous cell"),
    AMBRY121_005("Other"),
    AMBRY121_006(Constants.UNKNOWN),


    AMBRY123_001("Sebaceous Adenoma or Carcinoma"),
    AMBRY123_002("Melanoma"),
    AMBRY123_003("Basal cell"),
    AMBRY123_004("Squamous cell"),
    AMBRY123_005("Other"),
    AMBRY123_006(Constants.UNKNOWN),

    //Personal Polyp History
    AMBRY051_001("Yes"),
    AMBRY051_002("No"),
    AMBRY051_003(Constants.UNKNOWN),

    AMBRY125_001("Yes"),
    AMBRY125_002("No"),
    AMBRY125_003(Constants.UNKNOWN),

    AMBRY116_001("Yes"),
    AMBRY116_002("No"),
    AMBRY116_003(Constants.UNKNOWN),

    AMBRY117_001("1-4"),
    AMBRY117_002("5-9"),
    AMBRY117_003("10-19"),
    AMBRY117_005("20 or more"),
    AMBRY117_004(Constants.UNKNOWN),


    AMBRY117_001_OLD("1-4"),
    AMBRY117_002_OLD("5-9"),
    AMBRY117_003_OLD("10 or more"),
    AMBRY117_004_OLD(Constants.UNKNOWN),

    AMBRY118_001("Yes"),
    AMBRY118_002("No"),
    AMBRY118_003(Constants.UNKNOWN),

    AMBRY119_001("Yes"),
    AMBRY119_002("No"),
    AMBRY119_003(Constants.UNKNOWN),

    AMBRY120_001("Negative (no mutations identified)"),
    AMBRY120_002("Positive - BRCA1"),
    AMBRY120_003("Positive - BRCA2"),
    AMBRY120_004("Positive (different gene, not BRCA1/2)"),
    AMBRY120_005(Constants.UNKNOWN),


    AMBRY120_001_OLD("Normal (no mutation was found)"),
    AMBRY120_002_OLD("A mutation was found"),
    AMBRY120_003_OLD(Constants.UNKNOWN),

    AMBRY126_002(Constants.UNKNOWN),

    //family history of Polyps
    AMBRY111_001("Yes"),
    AMBRY111_002("No"),
    AMBRY111_003(Constants.UNKNOWN),

    AMBRY114_001("Mother"),
    AMBRY114_002("Father"),
    AMBRY114_003("Sister"),
    AMBRY114_004("Brother"),
    AMBRY114_005("Daughter"),
    AMBRY114_006("Son"),
    AMBRY114_007("Niece"),
    AMBRY114_008("Nephew "),
    AMBRY114_009("Granddaughter "),
    AMBRY114_010("Grandson"),
    AMBRY114_011("Half-sister (Mother’s side)"),
    AMBRY114_012("Half-brother (Mother’s side)"),
    AMBRY114_013("Grandmother (Mother’s side)"),
    AMBRY114_014("Grandfather (Mother’s side)"),
    AMBRY114_015("Aunt (Mother’s side)"),
    AMBRY114_016("Uncle (Mother’s side)"),
    AMBRY114_017("First cousin (male) (Mother’s side)"),
    AMBRY114_018("First cousin (female) (Mother’s side)"),
    AMBRY114_019("Great grandmother (Mother’s side)"),
    AMBRY114_020("Great grandfather (Mother’s side)"),
    AMBRY114_021("Great aunt (Mother’s side)"),
    AMBRY114_022("Great uncle (Mother’s side)"),
    AMBRY114_023("Other (Mother’s side)"),
    AMBRY114_024("Half-sister (Father’s side)"),
    AMBRY114_025("Half-brother (Father’s side)"),
    AMBRY114_026("Grandmother (Father’s side)"),
    AMBRY114_027("Grandfather (Father’s side)"),
    AMBRY114_028("Aunt (Father’s side)"),
    AMBRY114_029("Uncle (Father’s side)"),
    AMBRY114_030("First cousin (male) (Father’s side)"),
    AMBRY114_031("First cousin (female) (Father’s side)"),
    AMBRY114_032("Great grandmother (Father’s side)"),
    AMBRY114_033("Great grandfather (Father’s side)"),
    AMBRY114_034("Great aunt (Father’s side)"),
    AMBRY114_035("Great uncle (Father’s side)"),
    AMBRY114_036("Other (Father’s side)"),
    AMBRY114_037("Great niece"),
    AMBRY114_038("Great nephew"),
    AMBRY114_039("Great granddaughter"),
    AMBRY114_040("Great grandson"),
    AMBRY114_041("Half aunt (Mother's side)"),
    AMBRY114_042("Half uncle (Mother's side)"),
    AMBRY114_043("Half niece (Mother's side)"),
    AMBRY114_044("Half nephew (Mother's side)"),
    AMBRY114_045("Half aunt (Father’s side)"),
    AMBRY114_046("Half uncle (Father’s side)"),
    AMBRY114_047("Half niece (Father’s side)"),
    AMBRY114_048("Half nephew (Father’s side)"),



    AMBRY115_001("Mother's side"),
    AMBRY115_002("Father's side");


    private static class Constants {
        public static final String UNKNOWN = "Unknown";
    }

    private String value;

    public String getValue() {
        return this.value;
    }

    OptionConstants(String value) {
        this.value = value;
    }
}

