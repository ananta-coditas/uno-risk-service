package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;

import com.genomebrain.backend.quesstionnaire.FamilyCancerDetails;
import com.genomebrain.backend.quesstionnaire.PersonalHistory;
import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.quesstionnaire.relative.Father;
import com.genomebrain.backend.quesstionnaire.relative.Mother;
import com.genomebrain.backend.service.GuideLineService;
import com.genomebrain.backend.service.dto.ClinicalSummary;
import com.genomebrain.backend.service.dto.CustomQuestions;
import com.genomebrain.backend.service.dto.FamilyHistory;
import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;
import com.genomebrain.backend.service.dto.GuidelineDetailsDTO;
import com.genomebrain.backend.service.dto.PersonalHistoryTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("GuideLineUtility Unit Tests")
class GuideLineUtilityTest {

    @Mock
    private GuideLineService guideLineService;

    @Mock
    private TagGenerationUtility tagGenerationUtility;

    @Mock
    private LynchReasoningStatementUtility lynchReasoningStatementUtility;

    @Mock
    private HbocReasoningStatementUtility hbocReasoningStatementUtility;

    @Mock
    private GastricReasoningStatementUtility gastricReasoningStatementUtility;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GuideLineUtility guideLineUtility;

    @BeforeEach
    void setUp() throws java.net.ConnectException {
        // Setup default mocks for reasoning statement utilities to avoid NPE
        ReasoningStatementObject defaultLynchRS = new ReasoningStatementObject();
        defaultLynchRS.setReasoningStatementEn(new HashSet<>());
        defaultLynchRS.setReasoningStatementAndTags(new HashSet<>());
        org.mockito.Mockito.lenient().when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(defaultLynchRS);

        ReasoningStatementObject defaultHbocRS = new ReasoningStatementObject();
        defaultHbocRS.setReasoningStatementEn(new HashSet<>());
        defaultHbocRS.setReasoningStatementAndTags(new HashSet<>());
        org.mockito.Mockito.lenient().when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(defaultHbocRS);

        ReasoningStatementObject defaultGastricRS = new ReasoningStatementObject();
        defaultGastricRS.setReasoningStatementEn(new HashSet<>());
        defaultGastricRS.setReasoningStatementAndTags(new HashSet<>());
        org.mockito.Mockito.lenient().when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(defaultGastricRS);
    }

    @Test
    @DisplayName("Should calculate age correctly from date of birth")
    void testCalculateAge() {
        String dob = "01/15/1990";
        int result = guideLineUtility.calculateAge(dob);

        assertThat(result).isGreaterThan(0);
        // Age should be approximately current year - 1990
    }

    @Test
    @DisplayName("Should return 0 for invalid date format")
    void testCalculateAgeInvalidFormat() {
        String dob = "invalid-date";
        int result = guideLineUtility.calculateAge(dob);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return 0 for empty date")
    void testCalculateAgeEmpty() {
        String dob = "";
        int result = guideLineUtility.calculateAge(dob);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return 0 for null date")
    void testCalculateAgeNull() {
        int result = guideLineUtility.calculateAge(null);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return custom questions list")
    void testGetCustomQuestions() {
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY140");
        questionSet.setAnswerKey("AMBRY140_001");
        questionSet.setCategory("Category1");
        List<QuestionSet> mammogramCategory = Arrays.asList(questionSet);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should return empty list for empty custom questions")
    void testGetCustomQuestionsEmpty() {
        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(Collections.emptyList());

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should group custom questions by category")
    void testGetCustomQuestionsGroupedByCategory() {
        QuestionSet qs1 = new QuestionSet();
        qs1.setQuestionKey("AMBRY140");
        qs1.setAnswerKey("AMBRY140_001");
        qs1.setAnswerText("Answer1");
        qs1.setCategory("Category1");

        QuestionSet qs2 = new QuestionSet();
        qs2.setQuestionKey("AMBRY141");
        qs2.setAnswerKey("AMBRY141_001");
        qs2.setAnswerText("Answer2");
        qs2.setCategory("Category1");

        QuestionSet qs3 = new QuestionSet();
        qs3.setQuestionKey("AMBRY142");
        qs3.setAnswerKey("AMBRY142_001");
        qs3.setAnswerText("Answer3");
        qs3.setCategory("Category2");

        List<QuestionSet> mammogramCategory = Arrays.asList(qs1, qs2, qs3);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");
        when(messageSource.getMessage(eq("AMBRY140_001.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Answer 1");
        when(messageSource.getMessage(eq("AMBRY141_001.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Answer 2");
        when(messageSource.getMessage(eq("AMBRY142_001.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Answer 3");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // Two categories
    }

    @Test
    @DisplayName("Should return GuidelineDetailsDTO with clinical summary and tags")
    void testGetClinicalSummaryAndTags() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should throw ConnectException when genomebrain service fails")
    void testGetClinicalSummaryAndTagsServiceFailure() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("FAILURE");
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        assertThatThrownBy(() -> guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false))
            .isInstanceOf(ConnectException.class)
            .hasMessageContaining("Unable to connect to server");
    }

    @Test
    @DisplayName("Should handle null questionnaire")
    void testGetClinicalSummaryAndTagsNullQuestionnaire() {
        assertThatThrownBy(() -> guideLineUtility.getClinicalSummaryAndTags(
            null, false, null, "v1", false, false))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should calculate age for date with slash separator")
    void testCalculateAgeWithSlash() {
        String dob = "12/31/1985";
        int result = guideLineUtility.calculateAge(dob);

        assertThat(result).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle custom questions with weight details")
    void testGetCustomQuestionsWithWeightDetails() {
        QuestionSet weightQs = new QuestionSet();
        weightQs.setQuestionKey("AMBRY160");
        weightQs.setAnswerKey("AMBRY160_001");
        weightQs.setCategory("Weight");
        QuestionSet weightValueQs = new QuestionSet();
        weightValueQs.setQuestionKey("AMBRY161");
        weightValueQs.setAnswerKey("AMBRY161_001");
        weightValueQs.setAnswerText("150");
        weightValueQs.setCategory("Weight");
        List<QuestionSet> mammogramCategory = Arrays.asList(weightQs, weightValueQs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with personal history")
    void testGetClinicalSummaryAndTagsWithPersonalHistory() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        PersonalHistory personalHistory = new PersonalHistory();
        QuestionSet cancerQs = new QuestionSet();
        cancerQs.setQuestionKey("AMBRY004");
        cancerQs.setAnswerKey("AMBRY004_001");
        personalHistory.setBasicQuestions(Arrays.asList(cancerQs));
        questionnaire.setPersonalHistory(personalHistory);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>(Arrays.asList("PrBreast")));
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(true);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        // Mock reasoning statement utilities to return non-null objects with non-null collections
        ReasoningStatementObject lynchRS = new ReasoningStatementObject();
        lynchRS.setReasoningStatementEn(new HashSet<>());
        lynchRS.setReasoningStatementAndTags(new HashSet<>());
        when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(lynchRS);

        ReasoningStatementObject hbocRS = new ReasoningStatementObject();
        hbocRS.setReasoningStatementEn(new HashSet<>());
        hbocRS.setReasoningStatementAndTags(new HashSet<>());
        when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(hbocRS);

        ReasoningStatementObject gastricRS = new ReasoningStatementObject();
        gastricRS.setReasoningStatementEn(new HashSet<>());
        gastricRS.setReasoningStatementAndTags(new HashSet<>());
        when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(gastricRS);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isPersonalHistory()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with family history")
    void testGetClinicalSummaryAndTagsWithFamilyHistory() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        questionnaire.setFamilyCancer(familyHistory);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(true);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isFamilyHistory()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with CARE-API source")
    void testGetClinicalSummaryAndTagsWithCAREAPISource() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("CARE-API");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with reasoning statements")
    void testGetClinicalSummaryAndTagsWithReasoningStatements() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>(Arrays.asList("PrBreast")));
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(true);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        genomeBrainResponseDTO.setReasoningStatementsForHBOC(new HashSet<>(Arrays.asList("HBOCRS1")));
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        ReasoningStatementObject lynchRS = new ReasoningStatementObject();
        lynchRS.setReasoningStatementEn(new HashSet<>());
        lynchRS.setReasoningStatementAndTags(new HashSet<>());
        when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(lynchRS);

        ReasoningStatementObject hbocRS = new ReasoningStatementObject();
        hbocRS.setReasoningStatementEn(new HashSet<>(Arrays.asList("HBOCRS1")));
        hbocRS.setReasoningStatementAndTags(new HashSet<>());
        when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(hbocRS);

        ReasoningStatementObject gastricRS = new ReasoningStatementObject();
        gastricRS.setReasoningStatementEn(new HashSet<>());
        gastricRS.setReasoningStatementAndTags(new HashSet<>());
        when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(gastricRS);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with breast procedure details")
    void testGetCustomQuestionsWithBreastProcedure() {
        QuestionSet procedureQs = new QuestionSet();
        procedureQs.setQuestionKey("AMBRY149");
        procedureQs.setAnswerText("Lumpectomy, Mastectomy");
        procedureQs.setCategory("Breast Procedures");
        QuestionSet procedureDateQs = new QuestionSet();
        procedureDateQs.setQuestionKey("AMBRY150");
        procedureDateQs.setAnswerKey("AMBRY150_001");
        procedureDateQs.setAnswerText("01-2020");
        procedureDateQs.setCategory("Breast Procedures");
        List<QuestionSet> mammogramCategory = Arrays.asList(procedureQs, procedureDateQs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("History of Breast Procedure(s)");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with AMBRY147_001 has {ClinicName} placeholder")
    void testGetCustomQuestionsWithAMBRY147_001() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY147");
        qs.setAnswerKey("AMBRY147_001");
        qs.setCategory("Category1");
        qs.setAnswerText("Test Answer");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        // Mock messageSource to return string with {ClinicName} placeholder
        when(messageSource.getMessage(eq("AMBRY147.previewLabel"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Preview Label");
        when(messageSource.getMessage(eq("AMBRY147_001.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Text with {ClinicName} placeholder");

        // The code calls text.replace("{ClinicName}", null) which throws NPE
        assertThatThrownBy(() -> guideLineUtility.getCustomQuestions(mammogramCategory))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with comma separated answer keys")
    void testGetCustomQuestionsWithCommaSeparatedAnswerKeys() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY140");
        qs.setAnswerKey("AMBRY140_001,AMBRY140_002");
        qs.setCategory("Category1");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");
        when(messageSource.getMessage(eq("AMBRY140_001.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Answer 1");
        when(messageSource.getMessage(eq("AMBRY140_002.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("Answer 2");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with maternal relatives grouping")
    void testGetClinicalSummaryAndTagsWithMaternalRelatives() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Mother mother = new Mother();
        QuestionSet maternalQs = new QuestionSet();
        maternalQs.setQuestionKey("AMBRY054");
        maternalQs.setAnswerKey("AMBRY054_001");
        mother.setBasicQuestions(Arrays.asList(maternalQs));
        familyCancerDetails.setMothers(Arrays.asList(mother));
        familyHistory.setFamilyCancerDetails(familyCancerDetails);
        questionnaire.setFamilyCancer(familyHistory);
        
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());
        
        com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO familyHistoryDTO = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO();
        com.genomebrain.backend.service.dto.FamilyHistoryDetails familyHistoryDetails = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetails();
        familyHistoryDetails.setRelativeSide("Mat");
        familyHistoryDetails.setFamilyMember("Mother");
        familyHistoryDetails.setCancerDetails(new ArrayList<>());
        familyHistoryDTO.setFamilyHistoryDetails(familyHistoryDetails);
        familyHistoryDTO.setTags(new HashSet<>());
        familyHistoryDTO.setLynchReasoningStatements(new HashSet<>());
        familyHistoryDTO.setHbocReasoningStatements(new HashSet<>());
        when(tagGenerationUtility.getFamilyCancerTags(any(), anyString()))
            .thenReturn(Arrays.asList(familyHistoryDTO));

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isFamilyHistory()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with paternal relatives grouping")
    void testGetClinicalSummaryAndTagsWithPaternalRelatives() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Father father = new Father();
        QuestionSet paternalQs = new QuestionSet();
        paternalQs.setQuestionKey("AMBRY054");
        paternalQs.setAnswerKey("AMBRY054_002");
        father.setBasicQuestions(Arrays.asList(paternalQs));
        familyCancerDetails.setFathers(Arrays.asList(father));
        familyHistory.setFamilyCancerDetails(familyCancerDetails);
        questionnaire.setFamilyCancer(familyHistory);
        
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());
        
        com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO familyHistoryDTO = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO();
        com.genomebrain.backend.service.dto.FamilyHistoryDetails familyHistoryDetails = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetails();
        familyHistoryDetails.setRelativeSide("Pat");
        familyHistoryDetails.setFamilyMember("Father");
        familyHistoryDetails.setCancerDetails(new ArrayList<>());
        familyHistoryDTO.setFamilyHistoryDetails(familyHistoryDetails);
        familyHistoryDTO.setTags(new HashSet<>());
        familyHistoryDTO.setLynchReasoningStatements(new HashSet<>());
        familyHistoryDTO.setHbocReasoningStatements(new HashSet<>());
        when(tagGenerationUtility.getFamilyCancerTags(any(), anyString()))
            .thenReturn(Arrays.asList(familyHistoryDTO));

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isFamilyHistory()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with grandparent relatives for grouping")
    void testGetClinicalSummaryAndTagsWithGrandparentRelatives() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Grandmother grandmother = 
            new com.genomebrain.backend.quesstionnaire.relative.Grandmother();
        QuestionSet maternalQs = new QuestionSet();
        maternalQs.setQuestionKey("AMBRY054");
        maternalQs.setAnswerKey("AMBRY054_001");
        grandmother.setBasicQuestions(Arrays.asList(maternalQs));
        familyCancerDetails.setGrandMothers(Arrays.asList(grandmother));
        familyHistory.setFamilyCancerDetails(familyCancerDetails);
        questionnaire.setFamilyCancer(familyHistory);
        
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());
        
        com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO familyHistoryDTO = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO();
        com.genomebrain.backend.service.dto.FamilyHistoryDetails familyHistoryDetails = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetails();
        familyHistoryDetails.setRelativeSide("Mat");
        familyHistoryDetails.setFamilyMember("Grandmother");
        familyHistoryDetails.setCancerDetails(new ArrayList<>());
        familyHistoryDTO.setFamilyHistoryDetails(familyHistoryDetails);
        familyHistoryDTO.setTags(new HashSet<>());
        familyHistoryDTO.setLynchReasoningStatements(new HashSet<>());
        familyHistoryDTO.setHbocReasoningStatements(new HashSet<>());
        when(tagGenerationUtility.getFamilyCancerTags(any(), anyString()))
            .thenReturn(Arrays.asList(familyHistoryDTO));

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isFamilyHistory()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with multiple relatives for tag counting")
    void testGetClinicalSummaryAndTagsWithMultipleRelativesForTagCounting() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Mother mother1 = new Mother();
        mother1.setBasicQuestions(Collections.emptyList());
        Mother mother2 = new Mother();
        mother2.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setMothers(Arrays.asList(mother1, mother2));
        familyHistory.setFamilyCancerDetails(familyCancerDetails);
        questionnaire.setFamilyCancer(familyHistory);
        
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());
        
        com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO familyHistoryDTO1 = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO();
        com.genomebrain.backend.service.dto.FamilyHistoryDetails familyHistoryDetails1 = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetails();
        familyHistoryDetails1.setRelativeSide("Mat");
        familyHistoryDetails1.setFamilyMember("Mother");
        com.genomebrain.backend.service.dto.CancerDetailsObject cancerDetails1 = 
            new com.genomebrain.backend.service.dto.CancerDetailsObject();
        cancerDetails1.setCancer("Breast");
        familyHistoryDetails1.setCancerDetails(Arrays.asList(cancerDetails1));
        familyHistoryDTO1.setFamilyHistoryDetails(familyHistoryDetails1);
        familyHistoryDTO1.setTags(new HashSet<>());
        familyHistoryDTO1.setLynchReasoningStatements(new HashSet<>());
        familyHistoryDTO1.setHbocReasoningStatements(new HashSet<>());
        
        com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO familyHistoryDTO2 = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO();
        com.genomebrain.backend.service.dto.FamilyHistoryDetails familyHistoryDetails2 = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetails();
        familyHistoryDetails2.setRelativeSide("Mat");
        familyHistoryDetails2.setFamilyMember("Mother");
        com.genomebrain.backend.service.dto.CancerDetailsObject cancerDetails2 = 
            new com.genomebrain.backend.service.dto.CancerDetailsObject();
        cancerDetails2.setCancer("Breast");
        familyHistoryDetails2.setCancerDetails(Arrays.asList(cancerDetails2));
        familyHistoryDTO2.setFamilyHistoryDetails(familyHistoryDetails2);
        familyHistoryDTO2.setTags(new HashSet<>());
        familyHistoryDTO2.setLynchReasoningStatements(new HashSet<>());
        familyHistoryDTO2.setHbocReasoningStatements(new HashSet<>());
        
        when(tagGenerationUtility.getFamilyCancerTags(any(), anyString()))
            .thenReturn(Arrays.asList(familyHistoryDTO1, familyHistoryDTO2));

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isFamilyHistory()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with no family history cancer")
    void testGetClinicalSummaryAndTagsWithNoFamilyHistoryCancer() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_002");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        questionnaire.setFamilyCancer(familyHistory);
        
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummaryWithPersonalHistory = new ClinicalSummary();
        clinicalSummaryWithPersonalHistory.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummaryWithPersonalHistory);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isFamilyHistory()).isFalse();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with Gleason score consideration")
    void testGetClinicalSummaryAndTagsWithGleasonScore() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        PersonalHistory personalHistory = new PersonalHistory();
        QuestionSet cancerQs = new QuestionSet();
        cancerQs.setQuestionKey("AMBRY004");
        cancerQs.setAnswerKey("AMBRY004_001");
        personalHistory.setBasicQuestions(Arrays.asList(cancerQs));
        questionnaire.setPersonalHistory(personalHistory);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), eq(true), eq(true), eq(8L), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, true, 8L, "v1", false, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with adopted status Unknown")
    void testGetClinicalSummaryAndTagsWithAdoptedUnknown() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_003");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        questionnaire.setFamilyCancer(familyHistory);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with adopted status Unknown (adopted)")
    void testGetClinicalSummaryAndTagsWithAdoptedUnknownAdopted() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_004");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        questionnaire.setFamilyCancer(familyHistory);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with gastric reasoning statements")
    void testGetClinicalSummaryAndTagsWithGastricReasoningStatements() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>(Arrays.asList("PrGastric")));
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(true);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        ReasoningStatementObject lynchRS = new ReasoningStatementObject();
        lynchRS.setReasoningStatementEn(new HashSet<>());
        when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(lynchRS);

        ReasoningStatementObject hbocRS = new ReasoningStatementObject();
        hbocRS.setReasoningStatementEn(new HashSet<>());
        when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(hbocRS);

        ReasoningStatementObject gastricRS = new ReasoningStatementObject();
        gastricRS.setReasoningStatementEn(new HashSet<>(Arrays.asList("CRCRS14")));
        gastricRS.setReasoningStatementAndTags(new HashSet<>());
        when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(gastricRS);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isGastric()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with APS reasoning statements")
    void testGetClinicalSummaryAndTagsWithAPSReasoningStatements() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(true);
        genomeBrainResponseDTO.setGastric(false);
        genomeBrainResponseDTO.setReasoningStatementsForAPS(new HashSet<>(Arrays.asList("APRS1")));
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        ReasoningStatementObject lynchRS = new ReasoningStatementObject();
        lynchRS.setReasoningStatementEn(new HashSet<>());
        lynchRS.setReasoningStatementAndTags(new HashSet<>());
        when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(lynchRS);

        ReasoningStatementObject hbocRS = new ReasoningStatementObject();
        hbocRS.setReasoningStatementEn(new HashSet<>());
        hbocRS.setReasoningStatementAndTags(new HashSet<>());
        when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(hbocRS);

        ReasoningStatementObject gastricRS = new ReasoningStatementObject();
        gastricRS.setReasoningStatementEn(new HashSet<>());
        gastricRS.setReasoningStatementAndTags(new HashSet<>());
        when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(gastricRS);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isAPS()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with HBOCRS27 reasoning statement tag")
    void testGetClinicalSummaryAndTagsWithHBOCRS27() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(true);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        ReasoningStatementObject lynchRS = new ReasoningStatementObject();
        lynchRS.setReasoningStatementEn(new HashSet<>());
        when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(lynchRS);

        ReasoningStatementObject hbocRS = new ReasoningStatementObject();
        hbocRS.setReasoningStatementEn(new HashSet<>(Arrays.asList("HBOCRS27")));
        hbocRS.setReasoningStatementAndTags(new HashSet<>());
        when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(hbocRS);

        ReasoningStatementObject gastricRS = new ReasoningStatementObject();
        gastricRS.setReasoningStatementEn(new HashSet<>());
        when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(gastricRS);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with CRCRS9 reasoning statement tag")
    void testGetClinicalSummaryAndTagsWithCRCRS9() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        questionnaire.setFamilyCancer(familyHistory);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());
        
        com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO familyHistoryDTO = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO();
        com.genomebrain.backend.service.dto.FamilyHistoryDetails familyHistoryDetails = 
            new com.genomebrain.backend.service.dto.FamilyHistoryDetails();
        familyHistoryDetails.setRelativeSide("Mat");
        familyHistoryDetails.setFamilyMember("Mother");
        familyHistoryDetails.setCancerDetails(new ArrayList<>());
        familyHistoryDTO.setFamilyHistoryDetails(familyHistoryDetails);
        familyHistoryDTO.setTags(new HashSet<>());
        familyHistoryDTO.setLynchReasoningStatements(new HashSet<>());
        familyHistoryDTO.setHbocReasoningStatements(new HashSet<>());
        when(tagGenerationUtility.getFamilyCancerTags(any(), anyString()))
            .thenReturn(Arrays.asList(familyHistoryDTO));

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(true);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        ReasoningStatementObject lynchRS = new ReasoningStatementObject();
        lynchRS.setReasoningStatementEn(new HashSet<>(Arrays.asList("CRCRS9")));
        lynchRS.setReasoningStatementAndTags(new HashSet<>());
        when(lynchReasoningStatementUtility.getLynchReasoningStatement(any(), any()))
            .thenReturn(lynchRS);

        ReasoningStatementObject hbocRS = new ReasoningStatementObject();
        hbocRS.setReasoningStatementEn(new HashSet<>());
        when(hbocReasoningStatementUtility.getHbocReasoningStatement(any(), any()))
            .thenReturn(hbocRS);

        ReasoningStatementObject gastricRS = new ReasoningStatementObject();
        gastricRS.setReasoningStatementEn(new HashSet<>());
        when(gastricReasoningStatementUtility.getGastricReasoningStatement(any(), any()))
            .thenReturn(gastricRS);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.isLYNCH()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with Ashkenazi tag")
    void testGetClinicalSummaryAndTagsWithAshkenazi() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>(Arrays.asList("Ashkenazi")));
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.getTyrerCuzikCriteriaDTO()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with male breast cancer")
    void testGetClinicalSummaryAndTagsWithMaleBreastCancer() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        PersonalHistory personalHistory = new PersonalHistory();
        QuestionSet cancerQs = new QuestionSet();
        cancerQs.setQuestionKey("AMBRY004");
        cancerQs.setAnswerKey("AMBRY004_001");
        personalHistory.setBasicQuestions(Arrays.asList(cancerQs));
        questionnaire.setPersonalHistory(personalHistory);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_002");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>(Arrays.asList("PrBreast")));
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with heme tag")
    void testGetClinicalSummaryAndTagsWithHeme() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>(Arrays.asList("PrHeme")));
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.getClinicalSummary().isHeme()).isTrue();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with custom questions")
    void testGetClinicalSummaryAndTagsWithCustomQuestions() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));
        
        QuestionSet customQs = new QuestionSet();
        customQs.setQuestionKey("AMBRY140");
        customQs.setAnswerKey("AMBRY140_001");
        customQs.setCategory("Category1");
        questionnaire.setCustomCategories(Arrays.asList(customQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
        assertThat(result.getClinicalSummary().getCustomQuestions()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with null genomeBrainResponseDTO")
    void testGetClinicalSummaryAndTagsWithNullGenomeBrainResponse() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(null);

        assertThatThrownBy(() -> guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false))
            .isInstanceOf(ConnectException.class)
            .hasMessageContaining("Unable to connect to server");
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with AMBRY141_001 answer key with dash")
    void testGetCustomQuestionsWithAMBRY141_001Dash() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY141");
        qs.setAnswerKey("AMBRY141_001");
        qs.setAnswerText("Test-Label");
        qs.setCategory("Category1");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");
        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
            .thenReturn("Test-Label");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with AMBRY147_002 answer key with dash")
    void testGetCustomQuestionsWithAMBRY147_002Dash() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY147");
        qs.setAnswerKey("AMBRY147_002");
        qs.setAnswerText("Test-Label");
        qs.setCategory("Category1");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");
        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
            .thenReturn("Test-Label");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with AMBRY148 breast procedure")
    void testGetCustomQuestionsWithAMBRY148() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY148");
        qs.setAnswerKey("AMBRY148_001");
        qs.setCategory("Breast Procedures");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("History of Breast Procedure(s)");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with AMBRY151_001 breast procedure date")
    void testGetCustomQuestionsWithAMBRY151_001() {
        QuestionSet procedureQs = new QuestionSet();
        procedureQs.setQuestionKey("AMBRY149");
        procedureQs.setAnswerText("Lumpectomy");
        procedureQs.setCategory("Breast Procedures");
        QuestionSet procedureDateQs = new QuestionSet();
        procedureDateQs.setQuestionKey("AMBRY151");
        procedureDateQs.setAnswerKey("AMBRY151_001");
        procedureDateQs.setAnswerText("01-2020");
        procedureDateQs.setCategory("Breast Procedures");
        List<QuestionSet> mammogramCategory = Arrays.asList(procedureQs, procedureDateQs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("History of Breast Procedure(s)");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with AMBRY152_001 breast procedure date")
    void testGetCustomQuestionsWithAMBRY152_001() {
        QuestionSet procedureQs = new QuestionSet();
        procedureQs.setQuestionKey("AMBRY149");
        procedureQs.setAnswerText("Mastectomy");
        procedureQs.setCategory("Breast Procedures");
        QuestionSet procedureDateQs = new QuestionSet();
        procedureDateQs.setQuestionKey("AMBRY152");
        procedureDateQs.setAnswerKey("AMBRY152_001");
        procedureDateQs.setAnswerText("02/2020");
        procedureDateQs.setCategory("Breast Procedures");
        List<QuestionSet> mammogramCategory = Arrays.asList(procedureQs, procedureDateQs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("History of Breast Procedure(s)");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with exception in CustomQuestionConstants")
    void testGetCustomQuestionsWithException() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY140");
        qs.setAnswerKey("INVALID_KEY");
        qs.setCategory("Category1");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("Test Label");
        when(messageSource.getMessage(eq("INVALID_KEY.answerText"), any(), eq(Locale.ENGLISH)))
            .thenReturn("1");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getCustomQuestions with answerText when answer equals 1")
    void testGetCustomQuestionsWithAnswerTextEquals1() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY140");
        qs.setAnswerKey("INVALID_KEY");
        qs.setAnswerText("Custom Answer");
        qs.setCategory("Category1");
        List<QuestionSet> mammogramCategory = Arrays.asList(qs);

        when(messageSource.getMessage(anyString(), any(), eq(Locale.ENGLISH)))
            .thenReturn("1");

        List<CustomQuestions> result = guideLineUtility.getCustomQuestions(mammogramCategory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should calculate age for date with dash separator")
    void testCalculateAgeWithDash() {
        String dob = "01-15-1990";
        int result = guideLineUtility.calculateAge(dob);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle calculateAge with future date")
    void testCalculateAgeWithFutureDate() {
        String dob = "01/15/2050";
        int result = guideLineUtility.calculateAge(dob);

        assertThat(result).isLessThan(0);
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with old version")
    void testGetClinicalSummaryAndTagsWithOldVersion() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", true, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with new ethnicity mapping")
    void testGetClinicalSummaryAndTagsWithNewEthnicityMapping() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getClinicalSummaryAndTags with family history of colorectal polyps")
    void testGetClinicalSummaryAndTagsWithFamilyHistoryOfPolyps() throws ConnectException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setSource("Default");
        questionnaire.setPersonalHistory(null);
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet familyCancerQs = new QuestionSet();
        familyCancerQs.setQuestionKey("AMBRY052");
        familyCancerQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(familyCancerQs));
        questionnaire.setFamilyCancer(familyHistory);
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY002");
        demographicQs.setAnswerKey("AMBRY002_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        personalHistoryTagsDTO.setTags(new HashSet<>());
        com.genomebrain.backend.service.dto.PersonalHistoryDetails personalHistoryDetails = new com.genomebrain.backend.service.dto.PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.getPersonalHistoryDetails(any(), any(), anyBoolean(), anyBoolean(), any(), anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(personalHistoryTagsDTO);
        
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        clinicalSummary.setFamilyHistoryOfColorectalPolyps("Yes");
        clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
        when(tagGenerationUtility.checkFamilyColorectalPolypsHistory(any(), any()))
            .thenReturn(clinicalSummary);
        when(tagGenerationUtility.getTagsForSelectedOptions(any())).thenReturn(new HashSet<>());

        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        genomeBrainResponseDTO.setStatus("SUCCESS");
        genomeBrainResponseDTO.setHBOC(false);
        genomeBrainResponseDTO.setLYNCH(false);
        genomeBrainResponseDTO.setAPS(false);
        genomeBrainResponseDTO.setGastric(false);
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(genomeBrainResponseDTO);

        GuidelineDetailsDTO result = guideLineUtility.getClinicalSummaryAndTags(
            questionnaire, false, null, "v1", false, false);

        assertThat(result).isNotNull();
    }
}

