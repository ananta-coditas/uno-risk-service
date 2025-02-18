package com.genomebrain.backend.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.genomebrain.backend.quesstionnaire.QuestionSet;

public enum NCCNQuestionMapper {
    cancerFeatureType(Arrays.asList("cancerfeaturetype", "")),
    sexAtBirth(Arrays.asList("What was your sex at birth?", "AMBRY002")),

    cancerOverall(Arrays.asList("Have you ever had and/or do you currently have cancer?", "AMBRY004")),
    ethnicity(Arrays.asList("What is your ancestry?", "AMBRY003")),
    ethnicityOther(Arrays.asList("", "")),

    //CancerDetails
    clinicalfeatureoverall(Arrays.asList("", "")),
    cancerFeature(Arrays.asList("What type of cancer were you diagnosed with?", "AMBRY005")),
    cancerFeatureOther(Arrays.asList("What other type of cancer have you had?", "AMBRY006")),
    DESMOIDS(Arrays.asList("Have you ever been found to have a desmoid tumor?", "AMBRY118")),

    // cancerFeatureOtherQuestion(Arrays.asList("What type of cancer have you had?", "AMBRY005","Other","AMBRY005_016")),
    // cancerfeature(Arrays.asList("What type of cancer were you diagnosed with?", "AMBRY005")),


    // Personal CancerFeature Details
    metastatic(Arrays.asList("Was your prostate cancer metastatic?", "AMBRY018")),
    multiplePrimaries(Arrays.asList("Did you have one breast cancer or more than one primary breast cancer?", "AMBRY007")),
    pathology(Arrays.asList("What type of breast cancer did you have? (select all that apply)", "AMBRY009")),
    pathologyOther(Arrays.asList("", "")),
    breastContralateral(Arrays.asList("Which breast was your cancer in?", "AMBRY010")),
    breastIpsilateral(Arrays.asList("Which breast was your cancer in?", "AMBRY010")),
    breastEr(Arrays.asList("Was your breast cancer \"triple negative\", meaning it was negative for estrogen, progesterone, and HER2 receptors?", "AMBRY011")),
    breastPr(Arrays.asList("Was your breast cancer \"triple negative\", meaning it was negative for estrogen, progesterone, and HER2 receptors?", "AMBRY011")),
    breastHer2(Arrays.asList("Was your breast cancer \"triple negative\", meaning it was negative for estrogen, progesterone, and HER2 receptors?", "AMBRY011")),
    prostateGleasonScore(Arrays.asList("What was the Gleason score of your prostate cancer?", "AMBRY017")),
    colorectalMultiplePrimaries(Arrays.asList("Did you have more than one primary colorectal cancer?", "AMBRY020")),
    relativeColorectalMultiplePrimaries(Arrays.asList("Did this relative have more than one primary colorectal cancer?", "AMBRY067")),
    // prostateRiskRroup(Arrays.asList("What was the Gleason score of your prostate cancer?", "AMBRY017")),
    // prostateGradeGroup(Arrays.asList("What was the Gleason score of your prostate cancer?", "AMBRY017")),
    gastricDiffuse(Arrays.asList("Was your stomach cancer the diffuse type?", "AMBRY186")),


    // FAMILYHISTORY
    relationship(Arrays.asList("Select a relative that has had cancer.", "AMBRY053")),
    sideOfFam(Arrays.asList("On which side of your family is this relative from?", "AMBRY054")),
    relativeCancerOverall(Arrays.asList("Have any of your blood relatives been diagnosed with cancer?", "AMBRY052")),
    relativeCancerFeature(Arrays.asList("What type of cancer was this relative diagnosed with? (Arrays.asList(select all that apply))", "AMBRY055")),
    relativeCancerFeatureOther(Arrays.asList("What other type of cancer was this relative diagnosed with?", "AMBRY056")),
    relativeMetastatic(Arrays.asList("Was this relative's prostate cancer metastatic (Arrays.asList(meaning it spread to other parts of the body))?", "AMBRY066")),
    relativeMultiplePrimaries(Arrays.asList("How many primary breast cancers did this relative have?", "AMBRY057")),
    relativeBreastIpsilateral(Arrays.asList("Did this relative have breast cancer in both breasts (Arrays.asList(also called bilateral breast cancer))?", "AMBRY124")),
    relativeBreastEr(Arrays.asList("Was this relative's breast cancer \"triple negative?\"", "AMBRY059")),
    relativeProstateGleasonScore(Arrays.asList("What was the Gleason score of this relative's prostate cancer?", "AMBRY065")),
    //polyp related details
    polypOverall(Arrays.asList("Have you been found to have colorectal polyps?", "AMBRY051")),
    relativePolypOverall(Arrays.asList("Do you have a blood relative with 10 or more colorectal polyps that were adenomas?", "AMBRY111")),
    relativeTypePolypOverall(Arrays.asList("Which relative(s) has been found to have 10 or more colorectal adenomas?", "AMBRY114")),
    polypType(Arrays.asList("Were the colorectal polyps detected in you adenomas/adenomatous?", "AMBRY116")),
    exactNumOfPolyps(Arrays.asList("How many total colorectal polyps have you had in your lifetime?", "AMBRY117")),
    SkinCancerType(Arrays.asList("What type of skin cancer were you diagnosed with?", "AMBRY121")),
    RELATIVE_SkinCancerType(Arrays.asList("What type of skin cancer was this relative diagnosed with?", "AMBRY123")),
    relativeGastricDiffuse(Arrays.asList("Was this relative's stomach cancer the diffuse type?", "AMBRY187")),



    // NCCN Answer Mapper
    relationshipAnswer(Arrays.asList("MOTHER", "AMBRY053_001", "FATHER", "AMBRY053_002", "SISTER", "AMBRY053_003", "BROTHER", "AMBRY053_004", "DAUGHTER", "AMBRY053_005",
            "SON", "AMBRY053_006", "NIECE", "AMBRY053_007", "NEPHEW", "AMBRY053_008", "HALF_SISTER", "AMBRY053_009", "HALF_BROTHER", "AMBRY053_010", "GRANDMOTHER", "AMBRY053_011", "GRANDFATHER", "AMBRY053_012", "GRANDDAUGHTER", "AMBRY053_013",
            "GRANDSON", "AMBRY053_014", "AUNT", "AMBRY053_015", "UNCLE", "AMBRY053_016", "FIRST_COUSIN_MALE", "AMBRY053_017", "FIRST_COUSIN_FEMALE", "AMBRY053_018", "GREAT_GRANDMOTHER", "AMBRY053_019", "GREAT_GRANDFATHER", "AMBRY053_020", "GREAT_AUNT", "AMBRY053_021", "GREAT_UNCLE", "AMBRY053_022", "GREAT_NIECE", "AMBRY053_024",
            "GREAT_NEPHEW", "AMBRY053_025", "OTHER", "AMBRY053_023", "GREAT_GRANDDAUGHTER", "AMBRY053_026",
            "GREAT_GRANDSON", "AMBRY053_027", "HALF_AUNT", "AMBRY053_028", "HALF_UNCLE", "AMBRY053_029", "HALF_NIECE", "AMBRY053_030", "HALF_NEPHEW", "AMBRY053_031")),
    sideOfFamAnswer(Arrays.asList("MATERNAL", "AMBRY054_001", "PATERNAL", "AMBRY054_002", "OTHER", "AMBRY054_003")),
    sexAtBirthAnswer(Arrays.asList("MALE", "AMBRY002_001", "FEMALE", "AMBRY002_002")),
    ethnicityAnswer(Arrays.asList("African American", "AMBRY003_001", "Asian", "AMBRY003_002", "Caucasian", "AMBRY003_003", "French Canadian/Cajun", "AMBRY003_004",
            "Hispanic/Latino", "AMBRY003_005", "Jewish (Ashkenazi)", "AMBRY003_006","Ashkenazi Jewish", "AMBRY003_006", "Jewish(Ashkenazi)", "AMBRY003_006", "Jewish (Sephardic/non-Ashkenazi)", "AMBRY003_007", "Middle Eastern", "AMBRY003_008",
            "Native American", "AMBRY003_009", "Portuguese", "AMBRY003_010", "Other/not listed", "AMBRY003_011", "I don't know", "AMBRY003_012")),

    cancerOverallAnswer(Arrays.asList("YES", "AMBRY004_001", "NO", "AMBRY004_002")),


    cancerFeatureAnswer(Arrays.asList("BREAST", "AMBRY005_001", "OVARIAN", "AMBRY005_002", "FALLOPIAN TUBE OR PERITONEAL", "AMBRY005_003", "PROSTATE", "AMBRY005_004",
            "COLORECTAL", "AMBRY005_005", "PANCREATIC", "AMBRY005_006", "UTERINE (ENDOMETRIAL)", "AMBRY005_007", "STOMACH", "AMBRY005_008", "SMALL INTESTINE (SMALL BOWEL)", "AMBRY005_009",
            "BILIARY TRACT", "AMBRY005_010", "KIDNEY", "AMBRY005_011", "URETER OR RENAL PELVIS", "AMBRY005_012", "BRAIN", "AMBRY005_013", "SKIN", "AMBRY005_015", "OTHER", "AMBRY005_016")),
    cancerFeatureAnswerOther(Arrays.asList("LEUKEMIA", "AMBRY006_001", "LYMPHOMA", "AMBRY006_002", "GALLBLADDER", "AMBRY006_003", "CERVICAL", "AMBRY006_004", "BLADDER", "AMBRY006_005",
            "THROAT", "AMBRY006_006", "THYROID", "AMBRY006_007", "ORAL", "AMBRY006_008", "LIVER", "AMBRY006_009", "TESTICULAR", "AMBRY006_010", "PARAGANGLIOMA", "AMBRY006_011", "PHEOCHROMOCYTOMA", "AMBRY006_012",
            "LUNG", "AMBRY006_013", "SARCOMA", "AMBRY006_014", "UNKNOWN/OTHER CANCER NOT LISTED HERE", "AMBRY006_015")
    ),

    // cancerFeatureOtherRelativeQuestion(Arrays.asList("What type of cancer was this relative diagnosed with?", "AMBRY055","Other","AMBRY055_016")),

    // cancerfeaturetypeanswer(),
    DESMOIDSAnswer(Arrays.asList("YES", "AMBRY118_001","NO","AMBRY118_002","NOT_PROVIDED","AMBRY118_003")),
    colorectalMultiplePrimariesAnswer(Arrays.asList("YES", "AMBRY020_002", "NO", "AMBRY020_001")),
    metastaticAnswer(Arrays.asList("YES", "AMBRY018_001", "NO", "AMBRY018_002", "NOT_PROVIDED", "AMBRY018_003")),
    multiplePrimariesAnswer(Arrays.asList("NO", "AMBRY007_001", "YES", "AMBRY007_002")), //need to check and discuss for option
    pathologyAnswer(Arrays.asList("DCIS", "AMBRY009_001", "IDC", "AMBRY009_002", "ILC", "AMBRY009_003", "OTHER", "AMBRY009_004", "UNKNOWN", "AMBRY009_005")),
    breastIpsilateralAnswer(Arrays.asList("Both breasts (bilateral)", "AMBRY010_003")),
    breastErAnswer(Arrays.asList("YES", "AMBRY011_001", "NO", "AMBRY011_002", "NOT_PROVIDED", "AMBRY011_003")),
    breastPrAnswer(Arrays.asList("")),// Need to discuss
    breastHer2Answer(Arrays.asList("")),
    prostateGleasonScoreAnswer(Arrays.asList("7_OR_LESS", "AMBRY017_001", "HIGHER_THAN_7", "AMBRY017_002", "NOT_PROVIDED", "AMBRY017_003")),
    ProstateRiskGroupAnswer(Arrays.asList()),  // Need to check
    prostateGradeGroupAnswer(Arrays.asList()),  // Need to check
    polypOverallAnswer(Arrays.asList("YES", "AMBRY051_001", "NO", "AMBRY051_002", "NOT_PROVIDED", "AMBRY051_003")),
    relativePolypOverallAnswer(Arrays.asList("Yes","AMBRY111_001","No", "AMBRY111_002", "I don't know", "AMBRY111_003")),
    relativeTypePolypOverallAnswer(Arrays.asList("Mother","AMBRY114_001","Father", "AMBRY114_002", "Sister", "AMBRY114_003", "Brother", "AMBRY114_004", "Daughter",
            "AMBRY114_005", "Son", "AMBRY114_006", "Niece", "AMBRY114_007", "Nephew", "AMBRY114_008", "Granddaughter", "AMBRY114_009", "Grandson", "AMBRY114_010",
            "Half sister", "AMBRY114_011", "Half brother", "AMBRY114_012", "Grandmother", "AMBRY114_013", "Grandfather", "AMBRY114_014", "Aunt", "AMBRY114_015",
            "Uncle", "AMBRY114_016", "First cousin (male)", "AMBRY114_017", "First cousin (female)", "AMBRY114_018", "Great grandmother", "AMBRY114_019",
            "Great grandfather", "AMBRY114_020","Great aunt", "AMBRY114_021", "Great uncle", "AMBRY114_022", "Other", "AMBRY114_023")),
    polypTypeAnswer(Arrays.asList("Yes", "AMBRY116_001", "No", "AMBRY116_002", "I don't know", "AMBRY116_003")),// NEED to check
    exactNumOfPolypsAnswer(Arrays.asList("1-4", "AMBRY117_001", "5-9", "AMBRY117_002", "10-19", "AMBRY117_003", "20 or more", "AMBRY117_005", "I don't know", "AMBRY117_004")),
    gastricDiffuseAnswer(Arrays.asList("Yes", "AMBRY186_001", "No", "AMBRY186_002","I don't know", "AMBRY186_03")),
    relativeGastricDiffuseAnswer(Arrays.asList("Yes", "AMBRY187_001", "No", "AMBRY187_002", "I don't know", "AMBRY187_003")),
    relativeColorectalMultiplePrimariesAnswer(Arrays.asList("YES", "AMBRY067_002", "NO", "AMBRY067_001")),
    relativeBreastIpsilateralAnswer(Arrays.asList("Both breasts (bilateral)", "AMBRY124_001")),
    relativeMetastaticAnswer(Arrays.asList("yes", "AMBRY066_001", "no", "AMBRY066_002", "I don't know", "AMBRY066_003")),
    relativeMultiplePrimariesAnswer(Arrays.asList("No", "AMBRY057_001", "Yes", "AMBRY057_002")), //need to check and discuss for option
    relativePathologyAnswer(Arrays.asList("DCIS", "AMBRY009_001", "IDC", "AMBRY009_002", "ILC", "AMBRY009_003", "Other", "AMBRY009_004", "I don't know", "AMBRY009_005")),
    relativeBreastErAnswer(Arrays.asList("YES", "AMBRY059_001", "NO", "AMBRY059_002", "NOT_PROVIDED", "AMBRY059_003")),
    relativeProstateGleasonScoreAnswer(Arrays.asList("7 or less", "AMBRY065_001", "Higher than 7", "AMBRY065_002", "I don't know", "AMBRY065_003")),
    relativeCancerFeatureAnswer(Arrays.asList("BREAST", "AMBRY055_001", "OVARIAN", "AMBRY055_002", "FALLOPIAN TUBE OR PERITONEAL", "AMBRY055_003", "PROSTATE",
            "AMBRY055_004", "COLORECTAL", "AMBRY055_005", "PANCREATIC", "AMBRY055_006", "UTERINE (ENDOMETRIAL)", "AMBRY055_007", "STOMACH", "AMBRY055_008", "SMALL INTESTINE (SMALL BOWEL)", "AMBRY055_009",
            "BILIARY TRACT", "AMBRY055_010", "KIDNEY", "AMBRY055_011", "URETER OR RENAL PELVIS", "AMBRY055_012", "BRAIN", "AMBRY055_013", "SKIN", "AMBRY055_015", "OTHER", "AMBRY055_016")),
    relativeCancerFeatureAnswerOther(Arrays.asList("LEUKEMIA", "AMBRY056_001", "LYMPHOMA", "AMBRY056_002", "GALLBLADDER", "AMBRY056_003", "CERVICAL", "AMBRY056_004", "BLADDER", "AMBRY056_005",
            "THROAT", "AMBRY056_006", "THYROID", "AMBRY056_007", "ORAL", "AMBRY056_008", "LIVER", "AMBRY056_009", "TESTICULAR", "AMBRY056_010",
            "PARAGANGLIOMA", "AMBRY056_011", "PHEOCHROMOCYTOMA", "AMBRY056_012", "LUNG", "AMBRY056_013", "SARCOMA", "AMBRY056_014", "UNKNOWN/OTHER CANCER NOT LISTED HERE", "AMBRY056_015")),
    relativeCancerOverallAnswer(Arrays.asList("YES", "AMBRY052_001", "NO", "AMBRY052_002", "NOT_PROVIDED", "AMBRY052_003", "I am adopted", "AMBRY052_004")),
    SkinCancerTypeAnswer(Arrays.asList("Sebaceous Adenoma or Carcinoma", "AMBRY121_001")),
    RELATIVE_SkinCancerTypeAnswer(Arrays.asList("Sebaceous Adenoma or Carcinoma", "AMBRY123_001")),
//Age of  diagnosis

    BREAST(Arrays.asList("Breast", "AMBRY008_001", "How old were you when you were first diagnosed with breast cancer?", "AMBRY008")),
    OVARIAN(Arrays.asList("Ovarian", "AMBRY013_001", "How old were you when you were first diagnosed with ovarian cancer?", "AMBRY013")),
    FALLOPIAN(Arrays.asList("Fallopian tube or peritoneal", "AMBRY015_001", "How old were you when you were first diagnosed with fallopian tube or peritoneal cancer?", "AMBRY015")),
    PROSTATE(Arrays.asList("Prostate", "AMBRY016_001", "How old were you when you were first diagnosed with prostate cancer?", "AMBRY016")),
    COLORECTAL(Arrays.asList("Colorectal", "AMBRY021_001", "How old were you when you were first diagnosed with colorectal cancer?", "AMBRY021")),
    PANCREATIC(Arrays.asList("Pancreatic", "AMBRY023_001", "How old were you when you were first diagnosed with pancreatic cancer?", "AMBRY023")),
    UTERINE(Arrays.asList("Uterine (Endometrial)", "AMBRY024_001", "How old were you when you were first diagnosed with uterine cancer?", "AMBRY024")),
    GASTRIC(Arrays.asList("Stomach", "AMBRY025_001", "How old were you when you were first diagnosed with stomach cancer?", "AMBRY025")),
    SMALLINTESTINE(Arrays.asList("Small Intestine (Small Bowel)", "AMBRY026_001", "How old were you when you were first diagnosed with cancer of the small intestine?", "AMBRY026")),
    BILIARY(Arrays.asList("Biliary Tract", "AMBRY027_001", "How old were you when you were first diagnosed with biliary tract cancer?", "AMBRY027")),
    KIDNEY(Arrays.asList("Kidney", "AMBRY028_001", "How old were you when you were first diagnosed with kidney cancer?", "AMBRY028")),
    URETER(Arrays.asList("Ureter or Renal Pelvis", "AMBRY029_001", "How old were you when you were first diagnosed with cancer of the ureter or renal pelvis?", "AMBRY029")),
    BRAIN(Arrays.asList("Brain", "AMBRY030_001", "How old were you when you were first diagnosed with brain cancer?", "AMBRY030")),
    SKIN(Arrays.asList("Sebaceous Adenoma or Carcinoma (Rare skin tumor or cancer)", "AMBRY032_001", "How old were you when you were first diagnosed with sebaceous adenoma or carcinoma?", "AMBRY032")),
    MELANOMA(Arrays.asList("Melanoma", "AMBRY033_001", "How old were you when you were first diagnosed with melanoma?", "AMBRY033")),
    OTHER(Arrays.asList("Other", "AMBRY050_001", "How old were you when you were first diagnosed with another type of cancer not listed here?", "AMBRY050")),
    LEUKEMIA(Arrays.asList("Leukemia", "AMBRY035_001", "How old were you when you were first diagnosed with leukemia or lymphoma?", "AMBRY035")),
    LYMPHOMA(Arrays.asList("Lymphoma", "AMBRY034_001", "How old were you when you were first diagnosed with leukemia or lymphoma?", "AMBRY034")),
    GALLBLADDER(Arrays.asList("Gallbladder", "AMBRY038_001", "How old were you when you were first diagnosed with gallbladder cancer?", "AMBRY038")),
    CERVICAL(Arrays.asList("Cervical", "AMBRY039_001", "How old were you when you were first diagnosed with cervical cancer?", "AMBRY039")),
    BLADDER(Arrays.asList("Bladder", "AMBRY040_001", "How old were you when you were first diagnosed with bladder cancer?", "AMBRY040")),
    THROAT(Arrays.asList("Throat", "AMBRY041_001", "How old were you when you were first diagnosed with throat cancer?", "AMBRY041")),
    THYROID(Arrays.asList("Thyroid", "AMBRY042_001", "How old were you when you were first diagnosed with thyroid cancer?", "AMBRY042")),
    ORAL(Arrays.asList("Oral", "AMBRY043_001", "How old were you when you were first diagnosed with oral cancer?", "AMBRY043")),
    LIVER(Arrays.asList("Liver", "AMBRY044_001", "How old were you when you were first diagnosed with liver cancer?", "AMBRY044")),
    TESTICULAR(Arrays.asList("Testicular", "AMBRY045_001", "How old were you when you were first diagnosed with testicular cancer?", "AMBRY045")),
    PARAGANGLIOMA(Arrays.asList("Paraganglioma", "AMBRY046_001", "How old were you when you were first diagnosed with a paraganglioma?", "AMBRY046")),
    PHEOCHROMOCYTOMA(Arrays.asList("Pheochromocytoma", "AMBRY047_001", "How old were you when you were first diagnosed with a pheochromocytoma?", "AMBRY047")),
    LUNG(Arrays.asList("Lung", "AMBRY048_001", "How old were you when you were first diagnosed with lung cancer?", "AMBRY048")),
    SARCOMA(Arrays.asList("Sarcoma (bone or soft tissue)", "AMBRY049_001", "How old were you when you were first diagnosed with sarcoma?", "AMBRY049")),


    RELATIVE_BREAST(Arrays.asList("Breast", "AMBRY058_001", "How old was this relative when he or she was first diagnosed with breast cancer?", "AMBRY058")),
    RELATIVE_OVARIAN(Arrays.asList("Ovarian", "AMBRY061_001", "How old was this relative when she was first diagnosed with ovarian cancer?", "AMBRY061")),
    RELATIVE_FALLOPIAN(Arrays.asList("Fallopian tube or peritoneal", "AMBRY063_001", "How old was this relative when he or she was first diagnosed with fallopian tube or peritoneal cancer?", "AMBRY063")),
    RELATIVE_PROSTATE(Arrays.asList("Prostate", "AMBRY064_001", "How old was this relative when he was first diagnosed with prostate cancer?", "AMBRY064")),
    RELATIVE_COLORECTAL(Arrays.asList("Colorectal", "AMBRY068_001", "How old was this relative when he or she was first diagnosed with colorectal cancer?", "AMBRY068")),
    RELATIVE_PANCREATIC(Arrays.asList("Pancreatic", "AMBRY069_001", "How old was this relative when he or she was first diagnosed with pancreatic cancer?", "AMBRY069")),
    RELATIVE_UTERINE(Arrays.asList("Uterine (Endometrial)", "AMBRY070_001", "How old was this relative when he or she was first diagnosed with uterine cancer?", "AMBRY070")),
    RELATIVE_GASTRIC(Arrays.asList("Stomach", "AMBRY071_001", "How old was this relative when he or she was first diagnosed with stomach cancer?", "AMBRY071")),
    RELATIVE_SMALLINTESTINE(Arrays.asList("Small Intestine (Small Bowel)", "AMBRY072_001", "How old was this relative when he or she was first diagnosed with cancer of the small intestine?", "AMBRY072")),
    RELATIVE_BILIARY(Arrays.asList("Biliary Tract", "AMBRY073_001", "How old was this relative when he or she was first diagnosed with biliary tract cancer?", "AMBRY073")),
    RELATIVE_KIDNEY(Arrays.asList("Kidney", "AMBRY074_001", "How old was this relative when he or she was first diagnosed with kidney cancer?", "AMBRY074")),
    RELATIVE_URETER(Arrays.asList("Ureter or Renal Pelvis", "AMBRY075_001", "How old was this relative when he or she was first diagnosed with cancer of the ureter or renal pelvis?", "AMBRY075")),
    RELATIVE_BRAIN(Arrays.asList("Brain", "AMBRY076_001", "How old was this relative when he or she was first diagnosed with brain cancer?", "AMBRY076")),
    RELATIVE_SKIN(Arrays.asList("Sebaceous Adenoma or Carcinoma (Rare skin tumor or cancer)", "AMBRY077_001", "How old was this relative when he or she was first diagnosed with sebaceous adenoma or carcinoma?", "AMBRY077")),
    RELATIVE_MELANOMA(Arrays.asList("Melanoma", "AMBRY078_001", "How old was this relative when he or she was first diagnosed with melanoma?", "AMBRY078")),
    RELATIVE_OTHER(Arrays.asList("Other", "AMBRY093_001", "How old was this relative when he or she was first diagnosed with this other type of cancer not listed here?", "AMBRY093")),
    RELATIVE_LEUKEMIA(Arrays.asList("Leukemia", "AMBRY079_001", "How old was this relative when he or she was first diagnosed with leukemia?", "AMBRY079")),
    RELATIVE_LYMPHOMA(Arrays.asList("Lymphoma", "AMBRY080_001", "How old was this relative when he or she was first diagnosed with lymphoma?", "AMBRY080")),
    RELATIVE_GALLBLADDER(Arrays.asList("Gallbladder", "AMBRY081_001", "How old was this relative when he or she was first diagnosed with gallbladder cancer?", "AMBRY081")),
    RELATIVE_CERVICAL(Arrays.asList("Cervical", "AMBRY082_001", "How old was this relative when he or she was first diagnosed with cervical cancer?", "AMBRY082")),
    RELATIVE_BLADDER(Arrays.asList("Bladder", "AMBRY083_001", "How old was this relative when he or she was first diagnosed with bladder cancer?", "AMBRY083")),
    RELATIVE_THROAT(Arrays.asList("Throat", "AMBRY084_001", "How old was this relative when he or she was first diagnosed with throat cancer?", "AMBRY084")),
    RELATIVE_THYROID(Arrays.asList("Thyroid", "AMBRY085_001", "How old was this relative when he or she was first diagnosed with thyroid cancer?", "AMBRY085")),
    RELATIVE_ORAL(Arrays.asList("Oral", "AMBRY086_001", "How old was this relative when he or she was first diagnosed with oral cancer?", "AMBRY086")),
    RELATIVE_LIVER(Arrays.asList("Liver", "AMBRY087_001", "How old was this relative when he or she was first diagnosed with liver cancer?", "AMBRY087")),
    RELATIVE_TESTICULAR(Arrays.asList("Testicular", "AMBRY088_001", "How old was this relative when he or she was first diagnosed with testicular cancer?", "AMBRY088")),
    RELATIVE_PARAGANGLIOMA(Arrays.asList("Paraganglioma", "AMBRY089_001", "How old was this relative when he or she was first diagnosed with a paraganglioma?", "AMBRY089")),
    RELATIVE_PHEOCHROMOCYTOMA(Arrays.asList("Pheochromocytoma", "AMBRY090_001", "How old was this relative when he or she was first diagnosed with a pheochromocytoma?", "AMBRY090")),
    RELATIVE_LUNG(Arrays.asList("Lung", "AMBRY091_001", "How old was this relative when he or she was first diagnosed with lung cancer?", "AMBRY091")),
    RELATIVE_SARCOMA(Arrays.asList("Sarcoma (bone or soft tissue)", "AMBRY092_001", "How old was this relative when he or she was first diagnosed with sarcoma?", "AMBRY092"));


    private List<String> parameters = new ArrayList<>(Arrays.asList());

    private List<String> values;

    NCCNQuestionMapper(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public static QuestionSet getQuestionSet(String questionKey, String answerValue) {
        String answerKeyRequest = questionKey + "Answer";

        QuestionSet questionSet = new QuestionSet();
        if (isPresent(questionKey) && isPresent(answerKeyRequest)) {
            List<String> ss = NCCNQuestionMapper.valueOf(questionKey).parameters;
            List<String> ans = NCCNQuestionMapper.valueOf(answerKeyRequest).parameters;
            questionSet.setQuestionText(ss.get(0));
            questionSet.setQuestionKey(ss.get(1));
            if (ans.contains(answerValue)) {
                int i = ans.indexOf(answerValue);
                questionSet.setAnswerText(ans.get(i));
                questionSet.setAnswerKey(ans.get(i + 1));
            }
            System.out.println(questionSet);
        }
        return questionSet;
    }

    public static QuestionSet getQuestionSetForCancerFeature(String questionKey, String answerValue) {
        if (answerValue.equalsIgnoreCase("Gastric"))
            answerValue = "Stomach";
        if (answerValue.equalsIgnoreCase("Uterine"))
            answerValue = "Uterine (Endometrial)";
        if (answerValue.equalsIgnoreCase("SmallIntestine"))
            answerValue = "Small Intestine (Small Bowel)";
        if (answerValue.equalsIgnoreCase("Fallopian"))
            answerValue = "Fallopian tube or Peritoneal";
        if (answerValue.equalsIgnoreCase("Biliary"))
            answerValue = "Biliary Tract";
        if (answerValue.equalsIgnoreCase("Ureter"))
            answerValue = "Ureter or Renal Pelvis";

        answerValue = answerValue.toUpperCase();
        String answerKeyRequest = questionKey + "Answer";
        List<String> question = NCCNQuestionMapper.valueOf(questionKey).parameters;
        List<String> answer = NCCNQuestionMapper.valueOf(answerKeyRequest).parameters;
        QuestionSet questionSet = new QuestionSet();
        if (isPresent(questionKey) && isPresent(answerKeyRequest)) {
            question = NCCNQuestionMapper.valueOf(questionKey).parameters;
            answer = NCCNQuestionMapper.valueOf(answerKeyRequest).parameters;
            questionSet.setQuestionText(question.get(0));
            questionSet.setQuestionKey(question.get(1));
            if (answer.contains(answerValue)) {
                int i = answer.indexOf(answerValue);
                questionSet.setAnswerText(answer.get(i));
                questionSet.setAnswerKey(answer.get(i + 1));
            } else {
                questionKey = questionKey + "Other";
                answerKeyRequest = answerKeyRequest + "Other";
                question = NCCNQuestionMapper.valueOf(questionKey).parameters;
                answer = NCCNQuestionMapper.valueOf(answerKeyRequest).parameters;
                questionSet.setQuestionText(question.get(0));
                questionSet.setQuestionKey(question.get(1));
                if (answer.contains(answerValue)) {
                    int i = answer.indexOf(answerValue);
                    questionSet.setAnswerText(answer.get(i));
                    questionSet.setAnswerKey(answer.get(i + 1));
                }
            }
            System.out.println(questionSet);
        }
        return questionSet;
    }

    public static QuestionSet getQuestionSetForAgeAtDiagnosis(String cancerName, String ageValue) {
        QuestionSet questionSet = new QuestionSet();
        if (isPresent(cancerName)) {
            List<String> diagnosisResult = NCCNQuestionMapper.valueOf(cancerName).parameters;
            questionSet.setQuestionText(diagnosisResult.get(2));
            questionSet.setQuestionKey(diagnosisResult.get(3));
            if (ageValue.matches("\\d+")) {
                questionSet.setAnswerText(ageValue);
            } else {
                questionSet.setAnswerText("");
            }
            questionSet.setAnswerKey(diagnosisResult.get(1));
        }
        return questionSet;
    }

    public static QuestionSet getQuestionSetForMultipleAnswer(String questionKey, List<String> answerValueList) {
        String answerKeyRequest = questionKey + "Answer";

        QuestionSet questionSet = new QuestionSet();
        if (isPresent(questionKey) && isPresent(answerKeyRequest)) {
            List<String> questionParameters = NCCNQuestionMapper.valueOf(questionKey).parameters;
            List<String> answerParameters = NCCNQuestionMapper.valueOf(answerKeyRequest).parameters;
            questionSet.setQuestionText(questionParameters.get(0));
            questionSet.setQuestionKey(questionParameters.get(1));
            StringBuilder answerKeys = new StringBuilder();
            StringBuilder answerValues = new StringBuilder();
            for (String answer : answerValueList) {

                if (answerParameters.contains(answer)) {
                    int i = answerParameters.indexOf(answer);
                    answerValues.append(answerParameters.get(i) + ",");
                    answerKeys.append(answerParameters.get(i + 1) + ",");

                }
            }
            if (answerValues.length() > 0 && answerValues.charAt(answerValues.length() - 1) == ',') {
                answerValues.deleteCharAt(answerValues.length() - 1);
            }
            if (answerKeys.length() > 0 && answerKeys.charAt(answerKeys.length() - 1) == ',') {
                answerKeys.deleteCharAt(answerKeys.length() - 1);
            }
            questionSet.setAnswerText(String.valueOf(answerValues));
            questionSet.setAnswerKey(String.valueOf(answerKeys));
        }
        return questionSet;
    }


    public static boolean isPresent(String question) {
        try {
            valueOf(question);

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
