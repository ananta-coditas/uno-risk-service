package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("APIConstants Unit Tests")
class APIConstantsTest {

    @Test
    @DisplayName("Should have non-null polyp related constants")
    void testPolypConstants() {
        assertThat(APIConstants.POLYP_OVERALL).isNotNull();
        assertThat(APIConstants.ADENOMA).isNotNull();
        assertThat(APIConstants.POLYP_TYPE).isNotNull();
        assertThat(APIConstants.EXACT_NUM_OF_POLYPS).isNotNull();
        assertThat(APIConstants.POLYPS_10_TO_19).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null polyp count constants")
    void testPolypCountConstants() {
        assertThat(APIConstants.NUMEROUS).isNotNull();
        assertThat(APIConstants.DOZENS).isNotNull();
        assertThat(APIConstants.CARPETED).isNotNull();
        assertThat(APIConstants.INNUMERABLE).isNotNull();
        assertThat(APIConstants.LOTS).isNotNull();
        assertThat(APIConstants.MANY).isNotNull();
        assertThat(APIConstants.TOO_MANY_TO_COUNT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null CARE API constant")
    void testCAREAPIConstant() {
        assertThat(APIConstants.CARE_API).isNotNull();
        assertThat(APIConstants.CARE_API).isEqualTo("CARE-API");
    }

    @Test
    @DisplayName("Should have non-null relative constants")
    void testRelativeConstants() {
        assertThat(APIConstants.RELATIVE).isNotNull();
        assertThat(APIConstants.RELATIVE_MULTIPLE_PRIMARIES).isNotNull();
        assertThat(APIConstants.RELATIVE_PATHOLOGY).isNotNull();
        assertThat(APIConstants.RELATIVE_BREAST_IPSILATERAL).isNotNull();
        assertThat(APIConstants.RELATIVE_BREAST_ER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null breast cancer constants")
    void testBreastCancerConstants() {
        assertThat(APIConstants.BOTH_BREASTS).isNotNull();
        assertThat(APIConstants.BREAST_IPSILATERAL).isNotNull();
        assertThat(APIConstants.NEGATIVE).isNotNull();
        assertThat(APIConstants.BREAST_ER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null prostate cancer constants")
    void testProstateCancerConstants() {
        assertThat(APIConstants.PROSTATE_GLEASON_SCORE).isNotNull();
        assertThat(APIConstants.HIGHER_THAN_7).isNotNull();
        assertThat(APIConstants.RELATIVE_PROSTATE_GLEASON_SCORE).isNotNull();
        assertThat(APIConstants.RELATIVE_METASTATIC).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null risk group constants")
    void testRiskGroupConstants() {
        assertThat(APIConstants.VERY_HIGH_RISK_GROUP).isNotNull();
        assertThat(APIConstants.GRADE_GROUP_4).isNotNull();
        assertThat(APIConstants.GRADE_GROUP_5).isNotNull();
        assertThat(APIConstants.HIGH_RISK_GROUP).isNotNull();
        
        assertThat(APIConstants.VERY_HIGH_RISK_GROUP).isEqualTo("Very High Risk Group");
        assertThat(APIConstants.GRADE_GROUP_4).isEqualTo("Grade Group 4");
        assertThat(APIConstants.GRADE_GROUP_5).isEqualTo("Grade Group 5");
        assertThat(APIConstants.HIGH_RISK_GROUP).isEqualTo("High Risk Group");
    }

    @Test
    @DisplayName("Should have non-null colorectal constants")
    void testColorectalConstants() {
        assertThat(APIConstants.COLORECTAL_MULTIPLE_PRIMARIES).isNotNull();
        assertThat(APIConstants.RELATIVE_COLORECTAL_MULTIPLE_PRIMARIES).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null cancer type constants")
    void testCancerTypeConstants() {
        assertThat(APIConstants.SEBACEOUS_CARCINOMA).isNotNull();
        assertThat(APIConstants.SEBACEOUS_CARCINOMA_ALT).isNotNull();
        assertThat(APIConstants.SEBACEOUS_ADENOMAS).isNotNull();
        assertThat(APIConstants.FALLOPIAN_TUBE).isNotNull();
        assertThat(APIConstants.PERITONEAL).isNotNull();
        assertThat(APIConstants.FALLOPIAN_TUBE_OR_PERITONEAL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null validation message constants")
    void testValidationMessageConstants() {
        assertThat(APIConstants.PORTAL_ORDER_ID).isNotNull();
        assertThat(APIConstants.PORTAL_PATIENT_ID).isNotNull();
        assertThat(APIConstants.VALID_RELATIONSHIP).isNotNull();
        assertThat(APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST).isNotNull();
        assertThat(APIConstants.VALID_SIDE_OF_FAM).isNotNull();
        assertThat(APIConstants.VALID_SEX_AT_BIRTH).isNotNull();
        assertThat(APIConstants.VALID_CLINICAL_FEATURE_OVERALL).isNotNull();
        assertThat(APIConstants.VALID_CANCER_OVERALL).isNotNull();
        assertThat(APIConstants.VALID_POLYP_OVERALL_PROBAND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null side of family constant")
    void testSideOfFamConstant() {
        assertThat(APIConstants.SIDE_OF_FAM).isNotNull();
        assertThat(APIConstants.RELATIVE_CANCER_FEATURE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null blood and preleukemia constants")
    void testBloodConstants() {
        assertThat(APIConstants.BLOOD).isNotNull();
        assertThat(APIConstants.PRELEUKEMIA).isNotNull();
        assertThat(APIConstants.MYELODYSPLASTIC_SYNDROMES).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null pathology constant")
    void testPathologyConstant() {
        assertThat(APIConstants.PATHOLOGY).isNotNull();
        assertThat(APIConstants.PATHOLOGY).isEqualTo("pathology");
    }

    @Test
    @DisplayName("Should have non-null metastatic constant")
    void testMetastaticConstant() {
        assertThat(APIConstants.METASTATIC).isNotNull();
        assertThat(APIConstants.METASTATIC).isEqualTo("metastatic");
    }

    @Test
    @DisplayName("Should have non-null malignant constant")
    void testMalignantConstant() {
        assertThat(APIConstants.MALIGNANT).isNotNull();
        assertThat(APIConstants.MALIGNANT).isEqualTo("malignant");
    }

    @Test
    @DisplayName("Should have non-null adrenal constant")
    void testAdrenalConstant() {
        assertThat(APIConstants.ADRENAL).isNotNull();
        assertThat(APIConstants.ADRENAL).isEqualTo("Adrenal");
    }

    @Test
    @DisplayName("Should have non-null uterine endometrial constant")
    void testUterineEndometrialConstant() {
        assertThat(APIConstants.UTERINE_ENDOMETRIAL).isNotNull();
        assertThat(APIConstants.UTERINE_ENDOMETRIAL).isEqualTo("Uterine/Endometrial");
    }

    @Test
    @DisplayName("Should have non-null uterine constant")
    void testUterineConstant() {
        assertThat(APIConstants.UTERINE).isNotNull();
        assertThat(APIConstants.UTERINE).isEqualTo("Uterine");
    }

    @Test
    @DisplayName("Should have non-null small intestine constant")
    void testSmallIntestineConstant() {
        assertThat(APIConstants.SMALL_INTESTINE).isNotNull();
        assertThat(APIConstants.SMALL_INTESTINE).isEqualTo("Small Intestine");
    }

    @Test
    @DisplayName("Should have non-null biliary tract constants")
    void testBiliaryTractConstants() {
        assertThat(APIConstants.BILIARY_TRACT_NOS).isNotNull();
        assertThat(APIConstants.BILIARY_TRACT_NOS).isEqualTo("Biliary Tract - NOS");
        assertThat(APIConstants.BILIARY_TRACT).isNotNull();
        assertThat(APIConstants.BILIARY_TRACT).isEqualTo("Biliary Tract");
    }

    @Test
    @DisplayName("Should have non-null fallopian constants")
    void testFallopianConstants() {
        assertThat(APIConstants.FALLOPIAN).isNotNull();
        assertThat(APIConstants.FALLOPIAN).isEqualTo("Fallopian");
    }

    @Test
    @DisplayName("Should verify all constants have correct values")
    void testConstantValues() {
        assertThat(APIConstants.POLYP_OVERALL).isEqualTo("polypOverall");
        assertThat(APIConstants.ADENOMA).isEqualTo("Adenoma");
        assertThat(APIConstants.POLYP_TYPE).isEqualTo("polypType");
        assertThat(APIConstants.EXACT_NUM_OF_POLYPS).isEqualTo("exactNumOfPolyps");
        assertThat(APIConstants.POLYPS_10_TO_19).isEqualTo("10-19");
        assertThat(APIConstants.NUMEROUS).isEqualTo("Numerous");
        assertThat(APIConstants.DOZENS).isEqualTo("Dozens");
        assertThat(APIConstants.CARPETED).isEqualTo("Carpeted");
        assertThat(APIConstants.INNUMERABLE).isEqualTo("Innumerable");
        assertThat(APIConstants.LOTS).isEqualTo("Lots");
        assertThat(APIConstants.MANY).isEqualTo("Many");
        assertThat(APIConstants.TOO_MANY_TO_COUNT).isEqualTo("Too Many To Count");
        assertThat(APIConstants.RELATIVE).isEqualTo("relative");
        assertThat(APIConstants.SEBACEOUS_CARCINOMA).isEqualTo("Sebaceous carcinoma");
        assertThat(APIConstants.SEBACEOUS_CARCINOMA_ALT).isEqualTo("Sebaceous Carcinoma");
        assertThat(APIConstants.SEBACEOUS_ADENOMAS).isEqualTo("Sebaceous adenomas");
        assertThat(APIConstants.BOTH_BREASTS).isEqualTo("Both breasts (bilateral)");
        assertThat(APIConstants.BREAST_IPSILATERAL).isEqualTo("breastIpsilateral");
        assertThat(APIConstants.NEGATIVE).isEqualTo("Negative");
        assertThat(APIConstants.BREAST_ER).isEqualTo("breastEr");
        assertThat(APIConstants.RELATIVE_MULTIPLE_PRIMARIES).isEqualTo("relativeMultiplePrimaries");
        assertThat(APIConstants.RELATIVE_PATHOLOGY).isEqualTo("relativePathology");
        assertThat(APIConstants.RELATIVE_BREAST_IPSILATERAL).isEqualTo("relativeBreastIpsilateral");
        assertThat(APIConstants.RELATIVE_BREAST_ER).isEqualTo("relativeBreastEr");
        assertThat(APIConstants.PROSTATE_GLEASON_SCORE).isEqualTo("prostateGleasonScore");
        assertThat(APIConstants.HIGHER_THAN_7).isEqualTo("Higher than 7");
        assertThat(APIConstants.METASTATIC).isEqualTo("metastatic");
        assertThat(APIConstants.RELATIVE_PROSTATE_GLEASON_SCORE).isEqualTo("relativeProstateGleasonScore");
        assertThat(APIConstants.RELATIVE_METASTATIC).isEqualTo("relativeMetastatic");
        assertThat(APIConstants.MALIGNANT).isEqualTo("malignant");
        assertThat(APIConstants.COLORECTAL_MULTIPLE_PRIMARIES).isEqualTo("colorectalMultiplePrimaries");
        assertThat(APIConstants.RELATIVE_COLORECTAL_MULTIPLE_PRIMARIES).isEqualTo("relativeColorectalMultiplePrimaries");
        assertThat(APIConstants.SIDE_OF_FAM).isEqualTo("sideOfFam");
        assertThat(APIConstants.RELATIVE_CANCER_FEATURE).isEqualTo("relativeCancerFeature");
        assertThat(APIConstants.BLOOD).isEqualTo("Blood");
        assertThat(APIConstants.PRELEUKEMIA).isEqualTo("Preleukemia");
        assertThat(APIConstants.MYELODYSPLASTIC_SYNDROMES).isEqualTo("Myelodysplastic syndromes (MDS)");
        assertThat(APIConstants.FALLOPIAN_TUBE).isEqualTo("Fallopian Tube");
        assertThat(APIConstants.PERITONEAL).isEqualTo("Peritoneal");
        assertThat(APIConstants.FALLOPIAN_TUBE_OR_PERITONEAL).isEqualTo("Fallopian Tube or Peritoneal");
        assertThat(APIConstants.FALLOPIAN).isEqualTo("Fallopian");
        assertThat(APIConstants.ADRENAL).isEqualTo("Adrenal");
        assertThat(APIConstants.UTERINE_ENDOMETRIAL).isEqualTo("Uterine/Endometrial");
        assertThat(APIConstants.UTERINE).isEqualTo("Uterine");
        assertThat(APIConstants.SMALL_INTESTINE).isEqualTo("Small Intestine");
        assertThat(APIConstants.BILIARY_TRACT_NOS).isEqualTo("Biliary Tract - NOS");
        assertThat(APIConstants.BILIARY_TRACT).isEqualTo("Biliary Tract");
    }

    @Test
    @DisplayName("Should have non-null validation message constants with correct values")
    void testValidationMessageConstantValues() {
        assertThat(APIConstants.PORTAL_ORDER_ID).isEqualTo("Please provide portalOrderId");
        assertThat(APIConstants.PORTAL_PATIENT_ID).isEqualTo("Please provide portalPatientId");
        assertThat(APIConstants.VALID_RELATIONSHIP).isEqualTo("Please provide valid Relationship");
        assertThat(APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST).isEqualTo("Please provide valid SexAtBirth for Cousin - 1st");
        assertThat(APIConstants.VALID_SIDE_OF_FAM).isEqualTo("Please provide valid SideOfFam");
        assertThat(APIConstants.VALID_SEX_AT_BIRTH).isEqualTo("Please provide valid SexAtBirth");
        assertThat(APIConstants.VALID_CLINICAL_FEATURE_OVERALL).isEqualTo("Please provide valid ClinicalFeatureOverall");
        assertThat(APIConstants.VALID_CANCER_OVERALL).isEqualTo("Please provide valid CancerOverall");
        assertThat(APIConstants.VALID_POLYP_OVERALL_PROBAND).isEqualTo("Please provide valid PolypOverall for (PROBAND)");
    }
}

