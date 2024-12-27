package com.genomebrain.backend.service.dto;

public enum TagConstants {

    //Cancer
    BREAST("Breast"),
    OVARIAN("Ovarian"),
    FALLOPIAN("Fallopian tube or peritoneal"),
    PROSTATE("Prostate"),
    COLON("Colon or Rectal"),
    PANCREATIC("Pancreatic"),
    UTERINE("Uterine (Endometrial)"),
    GASTRIC("Stomach"),
    SMALL_INTESTINE("Small Intestine (Small Bowel)"),
    BILIARY_TRACT("Biliary Tract"),
    KIDNEY("Kidney"),
    URETER("Ureter or Renal pelvis"),
    BRAIN("Brain"),
    SEBACEOUS_ADENOMA("Sebaceous Adenoma or Carcinoma (Rare skin tumor or cancer)"),
    MELANOMA("Melanoma"),
    OTHER("Other"),
    LEUKEMIA("Leukemia"),
    LYMPHOMA("Lymphoma"),
    GALLBLADDER("Gallbladder"),
    CERVICAL("Cervical"),
    BLADDER("Bladder"),
    THROAT("Throat"),
    THYROID("Thyroid"),
    ORAL("Oral"),
    LIVER("Liver"),
    TESTICULAR("Testicular"),
    PARAGANGLIOMA("Paraganglioma"),
    PHEOCHROMOCYTOMA("Pheochromocytoma"),
    LUNG("Lung"),
    SARCOMA("Sarcoma (bone or soft tissue)"),

    //Gender
    FEMALE("Female"),
    MALE("Male"),

    //Ethnicity Tags
    AMBRY003_001("AfricanAmer"),
    AMBRY003_002("Asian"),
    AMBRY003_003("Caucasian"),
    AMBRY003_004("FrenchCa"),
    AMBRY003_005("Hispanic"),
    AMBRY003_006("Ashkenazi"),
    AMBRY003_007("Sephardic"),
    AMBRY003_008("MEastern"),
    AMBRY003_009("NativeAm"),
    AMBRY003_010("Portugese"),
    AMBRY003_011("AncestryOther"),
    AMBRY003_012("AncestryUnknown"),

    AMBRY111_001("FmPolyp>10"),

    //Asheknazi ethnicity
    AMBRY113_001("AshkenaziMat"),
    AMBRY113_002("AshkenaziPat"),
    AMBRY113_003("AshkenaziBoth"),


    //Personal Polyps history tags
    AMBRY051_001("PrPolyp"),
    AMBRY116_001("PrAdPolyps"),
    AMBRY116_002("PrNonAdPolyps"),
    AMBRY117_003("PrPolyp>10Dx"),
    AMBRY117_005("PrPolyp>20Dx"),
    AMBRY118_001("PrDesmoid"),
    AMBRY119_001("PrGeneTest"),
    AMBRY120_001("PrGeneTestNeg"),
    AMBRY120_002("PrGeneTestPos"),
    AMBRY120_003("PrGeneTestPos"),


    //Personal History Tags
    AMBRY007_002("PrBreastBilat"),
    AMBRY009_001("PrBreastDCIS"),
    AMBRY009_002("PrBreastIDC"),
    AMBRY009_003("PrBreastILC"),
    AMBRY009_004("PrBreastOtherType"),
    AMBRY009_005("PrBreastIDK"),
    AMBRY010_003("PrBreastBilat"),
    AMBRY011_001("PrBreastTrpNeg"),
    AMBRY014_001("PrOvarianEpith"),
    AMBRY014_003("PrOvarianEpith"),
    AMBRY017_002("PrProstateGL7"),
    AMBRY018_001("PrProstateMeta"),
    AMBRY019_001("PrProstateIntra"),
    AMBRY020_002("PrColon>=2Dx"),
    AMBRY022_001(""),
    AMBRY022_002(""),
    AMBRY022_003(""),
    AMBRY022_004(""),
    AMBRY022_005(""),
    AMBRY022_006(""),
    AMBRY022_007(""),
    AMBRY022_008(""),
    AMBRY022_009(""),
    AMBRY022_010(""),
    AMBRY022_011(""),
    AMBRY022_012(""),
    AMBRY031_001("PrBrainMalig"),
    AMBRY031_003("PrBrainMalig"),
    AMBRY035_001(Constants.PR_HEME),
    AMBRY036_001(Constants.PR_HEME),
    AMBRY036_003(Constants.PR_HEME),
    AMBRY037_002(Constants.PR_HEME),

    //Skin CAncer tags
    AMBRY121_001("PrSebAdenoCar"),
    AMBRY121_002("PrMelanoma"),
    AMBRY121_003("PrBasal"),
    AMBRY121_004("PrSquamous"),
    AMBRY121_005("PrSkinOther"),

    AMBRY123_001("FmSebAdenoCar"),
    AMBRY123_002("FmMelanoma"),
    AMBRY123_003("FmBasal"),
    AMBRY123_004("FmSquamous"),
    AMBRY123_005("FmSkinOther"),


    //Family History Tags
    AMBRY057_002("FmBreastBilat"),
    AMBRY124_001("TCFmBreastBilat"),
    AMBRY058_001(""),
    AMBRY067_002("FmColon>=2Dx"),
    AMBRY059_001("FmBreastTrpNeg"),
    AMBRY062_001("FmOvarianEpith"),
    AMBRY062_003("FmOvarianEpith"),
    AMBRY065_002("FmProstateGL7"),
    AMBRY066_001("FmProstateMeta"),


    //Relative Side
    AMBRY054_001("Mother's side"),
    AMBRY054_002("Father's side");


    private static class Constants {
        public static final String PR_HEME = "PrHeme";
    }

    private String value;

    public String getValue() {
        return this.value;
    }

    TagConstants(String value) {
        this.value = value;
    }
}

