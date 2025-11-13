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
}

