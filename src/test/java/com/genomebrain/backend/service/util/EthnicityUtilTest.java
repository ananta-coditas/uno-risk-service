package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.service.dto.DocumentType;
import com.genomebrain.backend.service.dto.Ethnicity;
import com.genomebrain.backend.service.dto.OptionConstants;
import com.genomebrain.backend.service.dto.PersonalHistoryDetails;
import com.genomebrain.backend.service.dto.TagConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("EthnicityUtil Unit Tests")
class EthnicityUtilTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EthnicityUtil ethnicityUtil;

    private Locale locale;
    private Questionnaire questionnaire;

    @BeforeEach
    void setUp() {
        locale = Locale.US;
        questionnaire = new Questionnaire();
        questionnaire.setDemographic(new ArrayList<>());
    }

    @Test
    @DisplayName("Should return ethnicity list with subcategories for Asian ethnicity using new mapping")
    void testGetEthnicityListWithSubcategoryAsianWithSubcategories() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_002", "AMBRY003_002_002", "AMBRY003_002_007");
        
        when(messageSource.getMessage(eq("AMBRY003_002.answerText"), any(), eq(locale))).thenReturn("Asian");
        when(messageSource.getMessage(eq("AMBRY003_002_002.answerText"), any(), eq(locale))).thenReturn("East Asian");
        when(messageSource.getMessage(eq("AMBRY003_002_007.answerText"), any(), eq(locale))).thenReturn("South Asian");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).contains("Asian");
        assertThat(result.get(0)).contains("East Asian");
        assertThat(result.get(0)).contains("South Asian");
    }

    @Test
    @DisplayName("Should return ethnicity list without subcategories when useNewMapping is false")
    void testGetEthnicityListWithSubcategoryWithoutNewMapping() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_001");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, false);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(Ethnicity.AMBRY003_001_OLD.getValue());
    }

    @Test
    @DisplayName("Should return ethnicity list for CLINICAL_SUMMARY document type")
    void testGetEthnicityListWithSubcategoryClinicalSummary() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_001");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, true);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(Ethnicity.AMBRY003_001.getValue());
    }

    @Test
    @DisplayName("Should return empty list for empty ethnicity constants")
    void testGetEthnicityListWithSubcategoryEmptyList() {
        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            Collections.emptyList(), locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should apply maternal side tag for Ashkenazi Jewish ethnicity")
    void testApplyAshkenaziTagAndSideMaternal() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006.getValue());
        
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY113");
        questionSet.setAnswerKey("AMBRY113_001");
        questionnaire.getDemographic().add(questionSet);

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).contains(TagConstants.AMBRY113_001.getValue());
        assertThat(personalHistoryDetails.getEthnicitySide()).isEqualTo(OptionConstants.AMBRY113_001.getValue());
    }

    @Test
    @DisplayName("Should apply paternal side tag for Ashkenazi Jewish ethnicity")
    void testApplyAshkenaziTagAndSidePaternal() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006.getValue());
        
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY113");
        questionSet.setAnswerKey("AMBRY113_002");
        questionnaire.getDemographic().add(questionSet);

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).contains(TagConstants.AMBRY113_002.getValue());
        assertThat(personalHistoryDetails.getEthnicitySide()).isEqualTo(OptionConstants.AMBRY113_002.getValue());
    }

    @Test
    @DisplayName("Should apply both sides tag for Ashkenazi Jewish ethnicity")
    void testApplyAshkenaziTagAndSideBothSides() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006.getValue());
        
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY113");
        questionSet.setAnswerKey("AMBRY113_003");
        questionnaire.getDemographic().add(questionSet);

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).contains(TagConstants.AMBRY113_003.getValue());
        assertThat(personalHistoryDetails.getEthnicitySide()).isEqualTo(OptionConstants.AMBRY113_003.getValue());
    }

    @Test
    @DisplayName("Should not apply tags when Ashkenazi Jewish ethnicity is not selected")
    void testApplyAshkenaziTagAndSideNotSelected() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_001.getValue());

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).isEmpty();
        assertThat(personalHistoryDetails.getEthnicitySide()).isNull();
    }

    @Test
    @DisplayName("Should map African American ethnicity")
    void testMapToMirthAPEthnicityAfricanAmerican() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("African American");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_001.getValue());
    }

    @Test
    @DisplayName("Should map Caucasian ethnicity")
    void testMapToMirthAPEthnicityCaucasian() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Caucasian");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_003.getValue());
    }

    @Test
    @DisplayName("Should map Ashkenazi Jewish ethnicity")
    void testMapToMirthAPEthnicityAshkenaziJewish() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Ashkenazi Jewish");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_006.getValue());
    }

    @Test
    @DisplayName("Should map Asian ethnicity")
    void testMapToMirthAPEthnicityAsian() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Asian");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_002.getValue());
    }

    @Test
    @DisplayName("Should map East Asian ethnicity")
    void testMapToMirthAPEthnicityEastAsian() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("East Asian");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_002_002.getValue());
    }

    @Test
    @DisplayName("Should map South Asian ethnicity")
    void testMapToMirthAPEthnicitySouthAsian() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("South Asian");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_002_007.getValue());
    }

    @Test
    @DisplayName("Should map French Canadian ethnicity")
    void testMapToMirthAPEthnicityFrenchCanadian() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("French Canadian");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_004.getValue());
    }

    @Test
    @DisplayName("Should map Hispanic ethnicity")
    void testMapToMirthAPEthnicityHispanic() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Hispanic");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_005.getValue());
    }

    @Test
    @DisplayName("Should map Mediterranean ethnicity")
    void testMapToMirthAPEthnicityMediterranean() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Mediterranean");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_013.getValue());
    }

    @Test
    @DisplayName("Should map Middle Eastern ethnicity")
    void testMapToMirthAPEthnicityMiddleEastern() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Middle Eastern");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_008.getValue());
    }

    @Test
    @DisplayName("Should map Portuguese ethnicity")
    void testMapToMirthAPEthnicityPortuguese() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Portuguese");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_010.getValue());
    }

    @Test
    @DisplayName("Should map Native American ethnicity")
    void testMapToMirthAPEthnicityNativeAmerican() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Native American");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_009.getValue());
    }

    @Test
    @DisplayName("Should map Pacific Islander ethnicity")
    void testMapToMirthAPEthnicityPacificIslander() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Pacific Islander");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_014.getValue());
    }

    @Test
    @DisplayName("Should map Northern European ethnicity")
    void testMapToMirthAPEthnicityNorthernEuropean() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Northern European");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_003_002.getValue());
    }

    @Test
    @DisplayName("Should map Southern European ethnicity")
    void testMapToMirthAPEthnicitySouthernEuropean() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Southern European");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_003_003.getValue());
    }

    @Test
    @DisplayName("Should map Finnish ethnicity")
    void testMapToMirthAPEthnicityFinnish() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Finnish");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_003_001.getValue());
    }

    @Test
    @DisplayName("Should return Other for unknown ethnicity")
    void testMapToMirthAPEthnicityUnknown() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("Unknown Ethnicity");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_011.getValue());
    }

    @Test
    @DisplayName("Should return empty string for empty ethnicity")
    void testMapToMirthAPEthnicityEmpty() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return null for null ethnicity")
    void testMapToMirthAPEthnicityNull() {
        String result = ethnicityUtil.mapToMirthAPEthnicity(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return Other for ethnicity with whitespace (no trimming in implementation)")
    void testMapToMirthAPEthnicityWithWhitespace() {
        // Note: mapToMirthAPEthnicity does not trim whitespace, so whitespace causes default case
        String result = ethnicityUtil.mapToMirthAPEthnicity("  Caucasian  ");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_011.getValue()); // Returns "Other" for unmatched case
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with exception in messageSource")
    void testGetEthnicityListWithSubcategoryWithException() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_002", "AMBRY003_002_002");
        
        when(messageSource.getMessage(anyString(), any(), eq(locale)))
            .thenThrow(new RuntimeException("Message source error"));

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotNull();
        // Should return empty list or handle exception gracefully
    }

    @Test
    @DisplayName("Should handle applyAshkenaziTagAndSide with missing question set")
    void testApplyAshkenaziTagAndSideMissingQuestionSet() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006.getValue());
        
        // Questionnaire without AMBRY113 question
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDemographic(new ArrayList<>());

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).isEmpty();
        assertThat(personalHistoryDetails.getEthnicitySide()).isNull();
    }

    @Test
    @DisplayName("Should throw NullPointerException when questionnaire is null")
    void testApplyAshkenaziTagAndSideNullQuestionnaire() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006.getValue());

        assertThatThrownBy(() -> ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with invalid constant")
    void testGetEthnicityListWithSubcategoryInvalidConstant() {
        List<String> ethnicityConstants = Arrays.asList("INVALID_CONSTANT");
        
        // Invalid constant is treated as subcategory, but has no parent ethnicity
        // This causes ethnicities to be null, which is handled in catch block
        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty(); // Returns empty list when exception occurs
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with mixed valid and invalid constants")
    void testGetEthnicityListWithSubcategoryMixedConstants() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_001", "INVALID_CONSTANT", "AMBRY003_002");
        
        // Mock messageSource for valid constants
        when(messageSource.getMessage(eq("AMBRY003_001.answerText"), any(), eq(locale)))
            .thenReturn("Black/African American");
        when(messageSource.getMessage(eq("AMBRY003_002.answerText"), any(), eq(locale)))
            .thenReturn("Asian");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        // Should contain the valid ethnicity values
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with White ethnicity and subcategories")
    void testGetEthnicityListWithSubcategoryWhiteWithSubcategories() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_003", "AMBRY003_003_001", "AMBRY003_003_002");
        
        when(messageSource.getMessage(eq("AMBRY003_003.answerText"), any(), eq(locale))).thenReturn("White");
        when(messageSource.getMessage(eq("AMBRY003_003_001.answerText"), any(), eq(locale))).thenReturn("Finnish");
        when(messageSource.getMessage(eq("AMBRY003_003_002.answerText"), any(), eq(locale))).thenReturn("Northern European");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).contains("White");
    }

    @Test
    @DisplayName("Should return Other for ethnicity with whitespace (no trimming in implementation)")
    void testMapToMirthAPEthnicityTrimmed() {
        // Note: mapToMirthAPEthnicity does not trim whitespace
        String result1 = ethnicityUtil.mapToMirthAPEthnicity("  Asian  ");
        String result2 = ethnicityUtil.mapToMirthAPEthnicity("Asian");
        
        // Without trimming, whitespace causes default case
        assertThat(result1).isEqualTo(Ethnicity.AMBRY003_011.getValue()); // Returns "Other"
        assertThat(result2).isEqualTo(Ethnicity.AMBRY003_002.getValue()); // Exact match works
    }

    @Test
    @DisplayName("Should handle South-East Asian ethnicity mapping")
    void testMapToMirthAPEthnicitySouthEastAsian() {
        String result = ethnicityUtil.mapToMirthAPEthnicity("South-East Asian");
        assertThat(result).isEqualTo(Ethnicity.AMBRY003_002_008.getValue());
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with null list")
    void testGetEthnicityListWithSubcategoryNullList() {
        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            null, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with old mapping for CLINICAL_SUMMARY")
    void testGetEthnicityListWithSubcategoryOldMappingClinicalSummary() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_001");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, false);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(Ethnicity.AMBRY003_001_OLD.getValue());
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with multiple ethnicities without subcategories")
    void testGetEthnicityListWithSubcategoryMultipleEthnicities() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_001", "AMBRY003_004", "AMBRY003_005");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, true);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(Ethnicity.AMBRY003_001.getValue());
        assertThat(result).contains(Ethnicity.AMBRY003_004.getValue());
        assertThat(result).contains(Ethnicity.AMBRY003_005.getValue());
    }

    @Test
    @DisplayName("Should handle applyAshkenaziTagAndSide with old Ashkenazi Jewish ethnicity value")
    void testApplyAshkenaziTagAndSideOldEthnicityValue() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006_OLD.getValue());
        
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY113");
        questionSet.setAnswerKey("AMBRY113_001");
        questionnaire.getDemographic().add(questionSet);

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).contains(TagConstants.AMBRY113_001.getValue());
        assertThat(personalHistoryDetails.getEthnicitySide()).isEqualTo(OptionConstants.AMBRY113_001.getValue());
    }

    @Test
    @DisplayName("Should handle applyAshkenaziTagAndSide with null demographic")
    void testApplyAshkenaziTagAndSideNullDemographic() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Arrays.asList(Ethnicity.AMBRY003_006.getValue());
        
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDemographic(null);

        assertThatThrownBy(() -> ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle applyAshkenaziTagAndSide with empty ethnicity list")
    void testApplyAshkenaziTagAndSideEmptyEthnicityList() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        Set<String> tagSet = new HashSet<>();
        List<String> ethnicityList = Collections.emptyList();

        ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagSet, ethnicityList, questionnaire);

        assertThat(tagSet).isEmpty();
        assertThat(personalHistoryDetails.getEthnicitySide()).isNull();
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with Asian ethnicity without subcategories when useNewMapping is false")
    void testGetEthnicityListWithSubcategoryAsianWithoutSubcategoriesOldMapping() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_002");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, false);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(Ethnicity.AMBRY003_002_OLD.getValue());
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with White ethnicity without subcategories when useNewMapping is false")
    void testGetEthnicityListWithSubcategoryWhiteWithoutSubcategoriesOldMapping() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_003");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, false);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(Ethnicity.AMBRY003_003_OLD.getValue());
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with subcategories but useNewMapping is false")
    void testGetEthnicityListWithSubcategoryWithSubcategoriesButOldMapping() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_002", "AMBRY003_002_002");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.CLINICAL_SUMMARY, false);

        assertThat(result).isNotEmpty();
        // Should return old mapping without subcategories
        assertThat(result.get(0)).isEqualTo(Ethnicity.AMBRY003_002_OLD.getValue());
    }

    @Test
    @DisplayName("Should handle mapToMirthAPEthnicity with case sensitivity")
    void testMapToMirthAPEthnicityCaseSensitivity() {
        // Switch statement is case-sensitive
        String result1 = ethnicityUtil.mapToMirthAPEthnicity("caucasian");
        String result2 = ethnicityUtil.mapToMirthAPEthnicity("ASIAN");
        
        assertThat(result1).isEqualTo(Ethnicity.AMBRY003_011.getValue()); // Default case
        assertThat(result2).isEqualTo(Ethnicity.AMBRY003_011.getValue()); // Default case
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with ASSESSMENT_TRANSCRIPT document type")
    void testGetEthnicityListWithSubcategoryAssessmentTranscript() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_001");
        
        when(messageSource.getMessage(eq("AMBRY003_001.answerText"), any(), eq(locale)))
            .thenReturn("Black/African American");

        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo("Black/African American");
    }

    @Test
    @DisplayName("Should handle getEthnicityListWithSubcategory with only subcategories")
    void testGetEthnicityListWithSubcategoryOnlySubcategories() {
        List<String> ethnicityConstants = Arrays.asList("AMBRY003_002_001", "AMBRY003_002_002");
        
        // Only subcategories, no parent ethnicity
        List<String> result = ethnicityUtil.getEthnicityListWithSubcategory(
            ethnicityConstants, locale, DocumentType.ASSESSMENT_TRANSCRIPT, true);

        assertThat(result).isNotNull();
        // Should handle gracefully - subcategories without parent are treated as invalid
    }
}

