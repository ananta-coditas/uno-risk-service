package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.objex.risk.beans.uno.Cancer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.quesstionnaire.QuestionSet;

@DisplayName("NCCNQuestionMapper Unit Tests")
class NCCNQuestionMapperTest {

    @Test
    @DisplayName("Should return QuestionSet for valid question key and answer value")
    void testGetQuestionSetWithValidInput() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSet("sexAtBirth", "MALE");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).isEqualTo("What was your sex at birth?");
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY002");
        assertThat(result.getAnswerText()).isEqualTo("MALE");
        assertThat(result.getAnswerKey()).isEqualTo("AMBRY002_001");
    }

    @Test
    @DisplayName("Should return empty QuestionSet for invalid question key")
    void testGetQuestionSetWithInvalidQuestionKey() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSet("INVALID_KEY", "MALE");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).isNull();
        assertThat(result.getQuestionKey()).isNull();
    }

    @Test
    @DisplayName("Should return QuestionSet for cancerOverall question")
    void testGetQuestionSetForCancerOverall() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSet("cancerOverall", "YES");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY004");
    }

    @Test
    @DisplayName("Should return QuestionSet for cancerFeature with standard cancer type")
    void testGetQuestionSetForCancerFeatureStandard() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "BREAST");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should normalize Gastric to Stomach in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureGastric() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "Gastric");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should normalize Uterine to Uterine (Endometrial) in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureUterine() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "Uterine");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should handle Other cancer type in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureOther() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "LEUKEMIA");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY006");
    }

    @Test
    @DisplayName("Should return QuestionSet for age at diagnosis with valid age")
    void testGetQuestionSetForAgeAtDiagnosisWithValidAge() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", "45");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("How old were you when you were first diagnosed");
        assertThat(result.getAnswerText()).isEqualTo("45");
        assertThat(result.getAnswerKey()).isEqualTo("AMBRY008_001");
    }

    @Test
    @DisplayName("Should return empty answer text for invalid age format")
    void testGetQuestionSetForAgeAtDiagnosisWithInvalidAge() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", "invalid");

        assertThat(result).isNotNull();
        assertThat(result.getAnswerText()).isEmpty();
    }

    @Test
    @DisplayName("Should return QuestionSet for multiple answers")
    void testGetQuestionSetForMultipleAnswer() {
        List<String> answers = Arrays.asList("MOTHER", "FATHER");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("relationship", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY053");
        assertThat(result.getAnswerText()).contains("MOTHER");
        assertThat(result.getAnswerText()).contains("FATHER");
        assertThat(result.getAnswerKey()).contains("AMBRY053_001");
        assertThat(result.getAnswerKey()).contains("AMBRY053_002");
    }

    @Test
    @DisplayName("Should handle empty answer list in getQuestionSetForMultipleAnswer")
    void testGetQuestionSetForMultipleAnswerWithEmptyList() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("relationship", Collections.emptyList());

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY053");
        assertThat(result.getAnswerText()).isEmpty();
        assertThat(result.getAnswerKey()).isEmpty();
    }

    @Test
    @DisplayName("Should return true for present enum value")
    void testIsPresentWithValidEnum() {
        boolean result = NCCNQuestionMapper.isPresent("sexAtBirth");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false for invalid enum value")
    void testIsPresentWithInvalidEnum() {
        boolean result = NCCNQuestionMapper.isPresent("INVALID_ENUM");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false for null enum value")
    void testIsPresentWithNull() {
        boolean result = NCCNQuestionMapper.isPresent(null);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return QuestionSet for OVARIAN cancer type")
    void testGetQuestionSetForAgeAtDiagnosisOvarian() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("OVARIAN", "50");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("ovarian cancer");
        assertThat(result.getAnswerText()).isEqualTo("50");
    }

    @Test
    @DisplayName("Should return QuestionSet for PROSTATE cancer type")
    void testGetQuestionSetForAgeAtDiagnosisProstate() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("PROSTATE", "65");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("prostate cancer");
        assertThat(result.getAnswerText()).isEqualTo("65");
    }

    @Test
    @DisplayName("Should return QuestionSet for COLORECTAL cancer type")
    void testGetQuestionSetForAgeAtDiagnosisColorectal() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("COLORECTAL", "55");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("colorectal cancer");
        assertThat(result.getAnswerText()).isEqualTo("55");
    }

    @Test
    @DisplayName("Should return QuestionSet for PANCREATIC cancer type")
    void testGetQuestionSetForAgeAtDiagnosisPancreatic() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("PANCREATIC", "60");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("pancreatic cancer");
        assertThat(result.getAnswerText()).isEqualTo("60");
    }

    @Test
    @DisplayName("Should return QuestionSet for UTERINE cancer type")
    void testGetQuestionSetForAgeAtDiagnosisUterine() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("UTERINE", "45");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("uterine cancer");
        assertThat(result.getAnswerText()).isEqualTo("45");
    }

    @Test
    @DisplayName("Should return QuestionSet for GASTRIC cancer type")
    void testGetQuestionSetForAgeAtDiagnosisGastric() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("GASTRIC", "58");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).contains("stomach cancer");
        assertThat(result.getAnswerText()).isEqualTo("58");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForMultipleAnswer with null values in list")
    void testGetQuestionSetForMultipleAnswerWithNullValues() {
        List<String> answers = Arrays.asList("MOTHER", null, "FATHER");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("relationship", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY053");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForMultipleAnswer with invalid answer values")
    void testGetQuestionSetForMultipleAnswerWithInvalidValues() {
        List<String> answers = Arrays.asList("INVALID_RELATIVE");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("relationship", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY053");
        assertThat(result.getAnswerText()).isEmpty();
    }

    @Test
    @DisplayName("Should return QuestionSet for ethnicity with multiple answers")
    void testGetQuestionSetForMultipleAnswerEthnicity() {
        List<String> answers = Arrays.asList("AFRICANAMERICAN", "CAUCASIAN");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("ethnicity", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY003");
        assertThat(result.getAnswerText()).contains("AFRICANAMERICAN");
        assertThat(result.getAnswerText()).contains("CAUCASIAN");
    }

    @Test
    @DisplayName("Should return QuestionSet for pathology with multiple answers")
    void testGetQuestionSetForMultipleAnswerPathology() {
        List<String> answers = Arrays.asList("DCIS", "IDC");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("pathology", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY009");
    }

    @Test
    @DisplayName("Should return true for all valid enum values in isPresent")
    void testIsPresentWithAllValidEnums() {
        assertThat(NCCNQuestionMapper.isPresent("sexAtBirth")).isTrue();
        assertThat(NCCNQuestionMapper.isPresent("cancerOverall")).isTrue();
        assertThat(NCCNQuestionMapper.isPresent("ethnicity")).isTrue();
        assertThat(NCCNQuestionMapper.isPresent("cancerFeature")).isTrue();
        assertThat(NCCNQuestionMapper.isPresent("relationship")).isTrue();
    }

    @Test
    @DisplayName("Should normalize SmallIntestine to Small Intestine (Small Bowel) in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureSmallIntestine() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "SmallIntestine");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should normalize Fallopian to Fallopian tube or Peritoneal in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureFallopian() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "Fallopian");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should normalize Biliary to Biliary Tract in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureBiliary() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", Cancer.CancerType.BILIARY_TRACT.name());

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should normalize Ureter to Ureter or Renal Pelvis in getQuestionSetForCancerFeature")
    void testGetQuestionSetForCancerFeatureUreter() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "Ureter");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should return empty QuestionSet for invalid cancer name in getQuestionSetForAgeAtDiagnosis")
    void testGetQuestionSetForAgeAtDiagnosisWithInvalidCancerName() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("INVALID_CANCER", "45");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).isNull();
    }

    @Test
    @DisplayName("Should return QuestionSet for all cancer types in getQuestionSetForAgeAtDiagnosis")
    void testGetQuestionSetForAgeAtDiagnosisAllCancerTypes() {
        String[] cancerTypes = {"BREAST", "OVARIAN", "FALLOPIAN", "PROSTATE", "COLORECTAL",
                               "PANCREATIC", "UTERINE", "GASTRIC", "SMALLINTESTINE", "BILIARY_TRACT",
                               "KIDNEY", "URETER", "BRAIN", "SKIN", "MELANOMA", "OTHER",
                               "LEUKEMIA", "LYMPHOMA", "GALLBLADDER", "CERVICAL", "BLADDER",
                               "THROAT", "THYROID", "ORAL", "LIVER", "TESTICULAR",
                               "PARAGANGLIOMA", "PHEOCHROMOCYTOMA", "LUNG", "SARCOMA"};

        for (String cancerType : cancerTypes) {
            QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(cancerType, "50");
            System.out.println(cancerType);
            assertThat(result).isNotNull();
            assertThat(result.getQuestionText()).isNotNull();
        }
    }

    @Test
    @DisplayName("Should handle getQuestionSetForMultipleAnswer with single answer")
    void testGetQuestionSetForMultipleAnswerWithSingleAnswer() {
        List<String> answers = Arrays.asList("MOTHER");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("relationship", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY053");
        assertThat(result.getAnswerText()).contains("MOTHER");
        assertThat(result.getAnswerKey()).contains("AMBRY053_001");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForMultipleAnswer with many answers")
    void testGetQuestionSetForMultipleAnswerWithManyAnswers() {
        List<String> answers = Arrays.asList("MOTHER", "FATHER", "SISTER", "BROTHER");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("relationship", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY053");
        assertThat(result.getAnswerText()).contains("MOTHER");
        assertThat(result.getAnswerText()).contains("FATHER");
        assertThat(result.getAnswerText()).contains("SISTER");
        assertThat(result.getAnswerText()).contains("BROTHER");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForMultipleAnswer with invalid question key")
    void testGetQuestionSetForMultipleAnswerWithInvalidQuestionKey() {
        List<String> answers = Arrays.asList("MOTHER");
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForMultipleAnswer("INVALID_KEY", answers);

        assertThat(result).isNotNull();
        assertThat(result.getQuestionText()).isNull();
    }

    @Test
    @DisplayName("Should handle getQuestionSet with null answer value")
    void testGetQuestionSetWithNullAnswerValue() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSet("sexAtBirth", null);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should throw NullPointerException when getQuestionSetForCancerFeature receives null answer value")
    void testGetQuestionSetForCancerFeatureWithNullAnswerValue() {
        assertThatThrownBy(() -> NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when getQuestionSetForAgeAtDiagnosis receives null age value")
    void testGetQuestionSetForAgeAtDiagnosisWithNullAgeValue() {
        assertThatThrownBy(() -> NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle getQuestionSetForAgeAtDiagnosis with empty age value")
    void testGetQuestionSetForAgeAtDiagnosisWithEmptyAgeValue() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", "");

        assertThat(result).isNotNull();
        assertThat(result.getAnswerText()).isEmpty();
    }

    @Test
    @DisplayName("Should handle getQuestionSetForAgeAtDiagnosis with zero age")
    void testGetQuestionSetForAgeAtDiagnosisWithZeroAge() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", "0");

        assertThat(result).isNotNull();
        assertThat(result.getAnswerText()).isEqualTo("0");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForAgeAtDiagnosis with negative age")
    void testGetQuestionSetForAgeAtDiagnosisWithNegativeAge() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", "-10");

        assertThat(result).isNotNull();
        assertThat(result.getAnswerText()).isEmpty();
    }

    @Test
    @DisplayName("Should handle getQuestionSetForAgeAtDiagnosis with very large age")
    void testGetQuestionSetForAgeAtDiagnosisWithVeryLargeAge() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis("BREAST", "150");

        assertThat(result).isNotNull();
        assertThat(result.getAnswerText()).isEqualTo("150");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForCancerFeature with case insensitive answer")
    void testGetQuestionSetForCancerFeatureCaseInsensitive() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "breast");

        assertThat(result).isNotNull();
        assertThat(result.getQuestionKey()).isEqualTo("AMBRY005");
    }

    @Test
    @DisplayName("Should handle getQuestionSetForCancerFeature with Other cancer type not in main list")
    void testGetQuestionSetForCancerFeatureOtherNotInMainList() {
        QuestionSet result = NCCNQuestionMapper.getQuestionSetForCancerFeature("cancerFeature", "UNKNOWN_CANCER");

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should verify enum values are accessible")
    void testEnumValues() {
        NCCNQuestionMapper[] values = NCCNQuestionMapper.values();
        assertThat(values).isNotEmpty();
    }

    @Test
    @DisplayName("Should verify enum valueOf with valid name")
    void testValueOfValidName() {
        NCCNQuestionMapper mapper = NCCNQuestionMapper.valueOf("sexAtBirth");
        assertThat(mapper).isNotNull();
        assertThat(mapper).isEqualTo(NCCNQuestionMapper.sexAtBirth);
    }

    @Test
    @DisplayName("Should throw exception for invalid enum name in valueOf")
    void testValueOfInvalidName() {
        assertThatThrownBy(() -> NCCNQuestionMapper.valueOf("INVALID_ENUM"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should verify enum getParameters method")
    void testGetParameters() {
        NCCNQuestionMapper mapper = NCCNQuestionMapper.sexAtBirth;
        List<String> parameters = mapper.getParameters();
        assertThat(parameters).isNotNull();
        assertThat(parameters).isNotEmpty();
    }
}

