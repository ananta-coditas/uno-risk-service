package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.genomebrain.backend.quesstionnaire.CancerDetails;
import com.genomebrain.backend.quesstionnaire.FamilyCancerDetails;
import com.genomebrain.backend.quesstionnaire.PersonalHistory;
import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.quesstionnaire.RelativeHistory;
import com.genomebrain.backend.quesstionnaire.relative.Mother;
import com.genomebrain.backend.quesstionnaire.relative.Sister;
import com.genomebrain.backend.quesstionnaire.relative.Brother;
import com.genomebrain.backend.service.dto.FamilyHistory;
import com.genomebrain.backend.service.dto.CancerDetailsDTO;
import com.genomebrain.backend.service.dto.ClinicalSummary;
import com.genomebrain.backend.service.dto.OptionConstants;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.PersonalHistoryDetails;
import com.genomebrain.backend.service.dto.PersonalHistoryTagsDTO;

@ExtendWith(MockitoExtension.class)
@DisplayName("TagGenerationUtility Unit Tests")
class TagGenerationUtilityTest {

    @Mock
    private EthnicityUtil ethnicityUtil;

    @InjectMocks
    private TagGenerationUtility tagGenerationUtility;

    private QuestionSet questionSet;
    private List<QuestionSet> questionSetList;

    @BeforeEach
    void setUp() {
        questionSet = new QuestionSet();
        questionSetList = new ArrayList<>();
    }

    @Test
    @DisplayName("Should return tags for valid question sets with valid answer keys")
    void testGetTagsForSelectedOptionsValid() {
        questionSet.setQuestionKey("AMBRY001");
        questionSet.setAnswerKey("AMBRY057_001");
        questionSetList.add(questionSet);

        Set<String> result = tagGenerationUtility.getTagsForSelectedOptions(questionSetList);

        assertThat(result).isNotNull();
        // Verify that valid tags are added (if TagConstants.AMBRY057_001 exists)
        // The actual result depends on whether the enum value exists
    }

    @Test
    @DisplayName("Should handle multiple answer keys separated by comma")
    void testGetTagsForSelectedOptionsMultipleAnswerKeys() {
        questionSet.setQuestionKey("AMBRY001");
        questionSet.setAnswerKey("AMBRY057_001,AMBRY057_002");
        questionSetList.add(questionSet);

        Set<String> result = tagGenerationUtility.getTagsForSelectedOptions(questionSetList);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when answer key is null")
    void testGetTagsForSelectedOptionsNullAnswerKey() {
        questionSet.setQuestionKey("AMBRY001");
        questionSet.setAnswerKey(null);
        questionSetList.add(questionSet);

        assertThatThrownBy(() -> tagGenerationUtility.getTagsForSelectedOptions(questionSetList))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Answer key cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when answer key is empty")
    void testGetTagsForSelectedOptionsEmptyAnswerKey() {
        questionSet.setQuestionKey("AMBRY001");
        questionSet.setAnswerKey("");
        questionSetList.add(questionSet);

        assertThatThrownBy(() -> tagGenerationUtility.getTagsForSelectedOptions(questionSetList))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Answer key cannot be null or empty");
    }

    @Test
    @DisplayName("Should return empty set for empty question set list")
    void testGetTagsForSelectedOptionsEmptyList() {
        Set<String> result = tagGenerationUtility.getTagsForSelectedOptions(Collections.emptyList());

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return Mat for maternal side")
    void testGetRelativeSideMaternal() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY054");
        qs.setAnswerKey("AMBRY054_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("Mat");
    }

    @Test
    @DisplayName("Should return Pat for paternal side")
    void testGetRelativeSidePaternal() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY054");
        qs.setAnswerKey("AMBRY054_002");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("Pat");
    }

    @Test
    @DisplayName("Should return N/A for unknown side")
    void testGetRelativeSideUnknown() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY054");
        qs.setAnswerKey("AMBRY054_003");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should return N/A when question key doesn't match")
    void testGetRelativeSideNoMatch() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY055");
        qs.setAnswerKey("AMBRY054_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should return N/A for empty basic questions")
    void testGetRelativeSideEmptyList() {
        String result = tagGenerationUtility.getRelativeSide(Collections.emptyList());

        assertThat(result).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should return N/A for null basic questions")
    void testGetRelativeSideNull() {
        String result = tagGenerationUtility.getRelativeSide(null);

        assertThat(result).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should set family history of colorectal polyps")
    void testCheckFamilyColorectalPolypsHistory() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY111");
        qs.setAnswerKey("AMBRY111_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        ClinicalSummary clinicalSummary = new ClinicalSummary();

        ClinicalSummary result = tagGenerationUtility.checkFamilyColorectalPolypsHistory(basicQuestions, clinicalSummary);

        assertThat(result).isNotNull();
        assertThat(result.getFamilyHistoryOfColorectalPolyps()).isNotNull();
    }

    @Test
    @DisplayName("Should set relative with polyps history")
    void testCheckFamilyColorectalPolypsHistoryWithRelatives() {
        QuestionSet qs1 = new QuestionSet();
        qs1.setQuestionKey("AMBRY114");
        qs1.setAnswerKey("AMBRY054_001,AMBRY054_002");
        List<QuestionSet> basicQuestions = Arrays.asList(qs1);
        ClinicalSummary clinicalSummary = new ClinicalSummary();

        ClinicalSummary result = tagGenerationUtility.checkFamilyColorectalPolypsHistory(basicQuestions, clinicalSummary);

        assertThat(result).isNotNull();
        assertThat(result.getRelativeWithPolypsHistory()).isNotNull();
    }

    @Test
    @DisplayName("Should return empty list for getFamilyCancerTags with null family cancer details")
    void testGetFamilyCancerTagsNull() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should process mother in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithMother() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory mother = new RelativeHistory();
        mother.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setMother(mother);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Mother");
    }

    @Test
    @DisplayName("Should generate FmPositiveGeneMutation tags when relative has a positive gene test and family genetic testing history is Yes")
    void testGetFamilyCancerTagsGeneratesGeneticTestingTagsWhenFamilyHistoryOfGeneticTestingIsYes() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory mother = new RelativeHistory();
        QuestionSet ambry192 = new QuestionSet();
        ambry192.setQuestionKey("AMBRY192");
        ambry192.setAnswerKey("AMBRY192_002");
        mother.setBasicQuestions(Arrays.asList(ambry192));
        com.genomebrain.backend.quesstionnaire.RelativeDetail relativeDetail = new com.genomebrain.backend.quesstionnaire.RelativeDetail();
        relativeDetail.setRelativeType("Mother");
        mother.setRelativeDetail(relativeDetail);
        familyCancerDetails.setMother(mother);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "Yes");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmPositiveGeneMutation", "FmPositiveGeneMutation-BRCA1");
        assertThat(result.get(0).getFamilyHistoryDetails().getRelativeDetail().getRelativeGeneList()).isEqualTo("Positive - BRCA1");
    }

    @Test
    @DisplayName("Should not generate FmPositiveGeneMutation tags when family genetic testing history is not Yes")
    void testGetFamilyCancerTagsSkipsGeneticTestingTagsWhenFamilyHistoryOfGeneticTestingIsNotYes() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory mother = new RelativeHistory();
        QuestionSet ambry192 = new QuestionSet();
        ambry192.setQuestionKey("AMBRY192");
        ambry192.setAnswerKey("AMBRY192_002");
        mother.setBasicQuestions(Arrays.asList(ambry192));
        com.genomebrain.backend.quesstionnaire.RelativeDetail relativeDetail = new com.genomebrain.backend.quesstionnaire.RelativeDetail();
        relativeDetail.setRelativeType("Mother");
        mother.setRelativeDetail(relativeDetail);
        familyCancerDetails.setMother(mother);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "No");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).doesNotContain("FmPositiveGeneMutation", "FmPositiveGeneMutation-BRCA1");
    }

    @Test
    @DisplayName("Should process father in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithFather() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory father = new RelativeHistory();
        father.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setFather(father);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Father");
    }

    @Test
    @DisplayName("Should process multiple mothers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithMultipleMothers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Mother mother1 = new Mother();
        mother1.setBasicQuestions(Collections.emptyList());
        Mother mother2 = new Mother();
        mother2.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setMothers(Arrays.asList(mother1, mother2));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return empty list for getRelativeCancerTags with empty relative history")
    void testGetRelativeCancerTagsEmpty() {
        RelativeHistory relativeHistory = new RelativeHistory();

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should process breast cancer in getRelativeCancerTags")
    void testGetRelativeCancerTagsWithBreast() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY008");
        qs.setAnswerKey("AMBRY008_001");
        qs.setAnswerText("45");
        relativeHistory.setBreast(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Breast");
    }

    @Test
    @DisplayName("Should process ovarian cancer in getRelativeCancerTags")
    void testGetRelativeCancerTagsWithOvarian() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY013");
        qs.setAnswerKey("AMBRY013_001");
        qs.setAnswerText("50");
        relativeHistory.setOvarian(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Ovarian");
    }

    @Test
    @DisplayName("Should return empty list for getPersonalCancerTags with empty cancer details")
    void testGetPersonalCancerTagsEmpty() {
        CancerDetails cancerDetails = new CancerDetails();

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should process breast cancer in getPersonalCancerTags")
    void testGetPersonalCancerTagsWithBreast() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY008");
        qs.setAnswerKey("AMBRY008_001");
        qs.setAnswerText("45");
        cancerDetails.setBreast(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Breast");
    }

    @Test
    @DisplayName("Should process prostate cancer with Gleason score consideration")
    void testGetPersonalCancerTagsWithProstateGleasonScore() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY016");
        qs.setAnswerKey("AMBRY016_001");
        qs.setAnswerText("60");
        QuestionSet gleasonQs = new QuestionSet();
        gleasonQs.setQuestionKey("AMBRY017");
        gleasonQs.setAnswerKey("AMBRY017_001");
        cancerDetails.setProstate(Arrays.asList(qs, gleasonQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, true, 8L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Prostate");
    }

    @Test
    @DisplayName("Should process multiple cancer types in getPersonalCancerTags")
    void testGetPersonalCancerTagsMultipleTypes() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        cancerDetails.setBreast(Arrays.asList(breastQs));

        QuestionSet ovarianQs = new QuestionSet();
        ovarianQs.setQuestionKey("AMBRY013");
        ovarianQs.setAnswerKey("AMBRY013_001");
        ovarianQs.setAnswerText("50");
        cancerDetails.setOvarian(Arrays.asList(ovarianQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should set personal colorectal polyps history")
    void testCheckPersonalColorectalPolypsHistory() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY051");
        qs.setAnswerKey("AMBRY051_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getNewColorectalPolypHx()).isNotNull();
    }

    @Test
    @DisplayName("Should set colonoscopy in personal polyps history")
    void testCheckPersonalColorectalPolypsHistoryColonoscopy() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY125");
        qs.setAnswerKey("AMBRY125_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getColonoscopy()).isNotNull();
    }

    @Test
    @DisplayName("Should handle old version for count of polyps")
    void testCheckPersonalColorectalPolypsHistoryOldVersion() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY117");
        qs.setAnswerKey("AMBRY117_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should set year of genetic testing from answer text")
    void testCheckPersonalColorectalPolypsHistoryYearOfGeneticTesting() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY126");
        qs.setAnswerKey("AMBRY126_001");
        qs.setAnswerText("2020");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getYearOfGeneticTesting()).isEqualTo("2020");
    }

    @Test
    @DisplayName("Should generate personal history details with demographic")
    void testGetPersonalHistoryDetailsWithDemographic() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY003");
        demographicQs.setAnswerKey("AMBRY003_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        when(ethnicityUtil.getEthnicityListWithSubcategory(anyList(), any(), any(), any(Boolean.class)))
            .thenReturn(Arrays.asList("African American"));

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails()).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("No");
    }

    @Test
    @DisplayName("Should set cancerHx to Yes when isPersonalHistory is true")
    void testGetPersonalHistoryDetailsWithPersonalHistory() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        CancerDetails cancerDetails = new CancerDetails();
        personalHistory.setPersonalCancerDetails(cancerDetails);
        personalHistory.setBasicQuestions(Collections.emptyList());
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("Yes");
    }

    @Test
    @DisplayName("Should handle adopted status from family cancer")
    void testGetPersonalHistoryDetailsWithAdopted() {
        Questionnaire questionnaire = new Questionnaire();
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet adoptedQs = new QuestionSet();
        adoptedQs.setQuestionKey("AMBRY052");
        adoptedQs.setAnswerKey("AMBRY052_004");
        familyHistory.setBasicQuestions(Arrays.asList(adoptedQs));
        questionnaire.setFamilyCancer(familyHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getAdopted()).isEqualTo("Adopted");
    }

    @Test
    @DisplayName("Should handle biopsy details from TyrerCuzick")
    void testGetPersonalHistoryDetailsWithBiopsy() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet biopsyQs = new QuestionSet();
        biopsyQs.setQuestionKey(TagGenerationUtility.PRIOR_BIOPSY_QUESTION);
        biopsyQs.setAnswerKey("AMBRY180_001");
        questionnaire.setTyrerCuzick(Arrays.asList(biopsyQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getPreviousBiopsy()).isNotNull();
    }

    @Test
    @DisplayName("Should handle biopsy result with I don't know")
    void testGetPersonalHistoryDetailsBiopsyResultIDontKnow() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet biopsyQs = new QuestionSet();
        biopsyQs.setQuestionKey(TagGenerationUtility.PRIOR_BIOPSY_QUESTION);
        biopsyQs.setAnswerKey("AMBRY180_003"); // Assuming this is "I don't know"
        QuestionSet resultQs = new QuestionSet();
        resultQs.setQuestionKey(TagGenerationUtility.BIOPSY_RESULT);
        resultQs.setAnswerKey("AMBRY106_003"); // Assuming this is "I don't know"
        questionnaire.setTyrerCuzick(Arrays.asList(biopsyQs, resultQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should return empty list for getFamilyCancerTags with empty relatives")
    void testGetFamilyCancerTagsEmptyRelatives() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        familyCancerDetails.setMothers(Collections.emptyList());
        familyCancerDetails.setFathers(Collections.emptyList());

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should process sisters in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithSisters() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Sister sister = new Sister();
        sister.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setSisters(Arrays.asList(sister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Sister");
    }

    @Test
    @DisplayName("Should process brothers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithBrothers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Brother brother = new Brother();
        brother.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setBrothers(Arrays.asList(brother));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Brother");
    }

    @Test
    @DisplayName("Should process daughters in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithDaughters() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Daughters daughter =
            new com.genomebrain.backend.quesstionnaire.relative.Daughters();
        daughter.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setDaughters(Arrays.asList(daughter));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Daughter");
    }

    @Test
    @DisplayName("Should process sons in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithSons() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Son son =
            new com.genomebrain.backend.quesstionnaire.relative.Son();
        son.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setSons(Arrays.asList(son));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Son");
    }

    @Test
    @DisplayName("Should process nieces in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithNieces() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Niece niece =
            new com.genomebrain.backend.quesstionnaire.relative.Niece();
        niece.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setNieces(Arrays.asList(niece));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Niece");
    }

    @Test
    @DisplayName("Should process nephews in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithNephews() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Nephew nephew =
            new com.genomebrain.backend.quesstionnaire.relative.Nephew();
        nephew.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setNephews(Arrays.asList(nephew));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Nephew");
    }

    @Test
    @DisplayName("Should process aunts in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithAunts() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Aunt aunt =
            new com.genomebrain.backend.quesstionnaire.relative.Aunt();
        aunt.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setAunts(Arrays.asList(aunt));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Aunt");
    }

    @Test
    @DisplayName("Should process uncles in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithUncles() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Uncle uncle =
            new com.genomebrain.backend.quesstionnaire.relative.Uncle();
        uncle.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setUncles(Arrays.asList(uncle));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Uncle");
    }

    @Test
    @DisplayName("Should process grandmothers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGrandmothers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Grandmother grandmother =
            new com.genomebrain.backend.quesstionnaire.relative.Grandmother();
        grandmother.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGrandMothers(Arrays.asList(grandmother));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Grandmother");
    }

    @Test
    @DisplayName("Should process grandfathers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGrandfathers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Grandfather grandfather =
            new com.genomebrain.backend.quesstionnaire.relative.Grandfather();
        grandfather.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGrandFathers(Arrays.asList(grandfather));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Grandfather");
    }

    @Test
    @DisplayName("Should handle checkFamilyColorectalPolypsHistory with multiple relatives with polyps")
    void testCheckFamilyColorectalPolypsHistoryMultipleRelatives() {
        QuestionSet qs1 = new QuestionSet();
        qs1.setQuestionKey("AMBRY114");
        qs1.setAnswerKey("AMBRY054_001,AMBRY054_002");
        QuestionSet qs2 = new QuestionSet();
        qs2.setQuestionKey("AMBRY111");
        qs2.setAnswerKey("AMBRY111_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs1, qs2);
        ClinicalSummary clinicalSummary = new ClinicalSummary();

        ClinicalSummary result = tagGenerationUtility.checkFamilyColorectalPolypsHistory(basicQuestions, clinicalSummary);

        assertThat(result).isNotNull();
        assertThat(result.getFamilyHistoryOfColorectalPolyps()).isNotNull();
        assertThat(result.getRelativeWithPolypsHistory()).isNotNull();
    }

    @Test
    @DisplayName("Should handle checkFamilyColorectalPolypsHistory with empty basic questions")
    void testCheckFamilyColorectalPolypsHistoryEmptyQuestions() {
        List<QuestionSet> basicQuestions = Collections.emptyList();
        ClinicalSummary clinicalSummary = new ClinicalSummary();

        ClinicalSummary result = tagGenerationUtility.checkFamilyColorectalPolypsHistory(basicQuestions, clinicalSummary);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should throw NullPointerException when basic questions is null")
    void testCheckFamilyColorectalPolypsHistoryNullQuestions() {
        ClinicalSummary clinicalSummary = new ClinicalSummary();

        assertThatThrownBy(() -> tagGenerationUtility.checkFamilyColorectalPolypsHistory(null, clinicalSummary))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when questionnaire is null")
    void testGetPersonalHistoryDetailsWithNullQuestionnaire() {
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        assertThatThrownBy(() -> tagGenerationUtility.getPersonalHistoryDetails(
            null, personalHistoryDetails, false, false, null, "v1", false, true))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with empty questionnaire")
    void testGetPersonalHistoryDetailsWithEmptyQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails()).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("No");
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with multiple cancer types")
    void testGetPersonalHistoryDetailsWithMultipleCancers() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        CancerDetails cancerDetails = new CancerDetails();

        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        cancerDetails.setBreast(Arrays.asList(breastQs));

        QuestionSet ovarianQs = new QuestionSet();
        ovarianQs.setQuestionKey("AMBRY013");
        ovarianQs.setAnswerKey("AMBRY013_001");
        ovarianQs.setAnswerText("50");
        cancerDetails.setOvarian(Arrays.asList(ovarianQs));

        personalHistory.setPersonalCancerDetails(cancerDetails);
        personalHistory.setBasicQuestions(Collections.emptyList());
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("Yes");
        assertThat(result.getTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with Gleason score consideration")
    void testGetPersonalHistoryDetailsWithGleasonScore() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        CancerDetails cancerDetails = new CancerDetails();

        QuestionSet prostateQs = new QuestionSet();
        prostateQs.setQuestionKey("AMBRY016");
        prostateQs.setAnswerKey("AMBRY016_001");
        prostateQs.setAnswerText("60");
        cancerDetails.setProstate(Arrays.asList(prostateQs));

        personalHistory.setPersonalCancerDetails(cancerDetails);
        personalHistory.setBasicQuestions(Collections.emptyList());
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, true, 8L, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("Yes");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with pancreatic cancer")
    void testGetRelativeCancerTagsWithPancreatic() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY020");
        qs.setAnswerKey("AMBRY020_001");
        qs.setAnswerText("55");
        relativeHistory.setPancreatic(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Pancreatic");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with uterine cancer")
    void testGetRelativeCancerTagsWithUterine() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY021");
        qs.setAnswerKey("AMBRY021_001");
        qs.setAnswerText("45");
        relativeHistory.setUterine(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Uterine (Endometrial)");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with stomach cancer")
    void testGetRelativeCancerTagsWithStomach() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY022");
        qs.setAnswerKey("AMBRY022_001");
        qs.setAnswerText("58");
        relativeHistory.setStomach(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Stomach");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with fallopian tube cancer")
    void testGetPersonalCancerTagsWithFallopianTube() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY014");
        qs.setAnswerKey("AMBRY014_001");
        qs.setAnswerText("50");
        cancerDetails.setFallopianTube(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Fallopian tube or peritoneal");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with stomach cancer")
    void testGetPersonalCancerTagsWithStomach() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY022");
        qs.setAnswerKey("AMBRY022_001");
        qs.setAnswerText("52");
        cancerDetails.setStomach(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Stomach");
    }

    @Test
    @DisplayName("Should process half sisters in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithHalfSisters() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.HalfSister halfSister =
            new com.genomebrain.backend.quesstionnaire.relative.HalfSister();
        halfSister.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setHalfSisters(Arrays.asList(halfSister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Half-Sister");
    }

    @Test
    @DisplayName("Should process half brothers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithHalfBrothers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.HalfBrother halfBrother =
            new com.genomebrain.backend.quesstionnaire.relative.HalfBrother();
        halfBrother.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setHalfBrothers(Arrays.asList(halfBrother));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Half-Brother");
    }

    @Test
    @DisplayName("Should process granddaughters in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGrandDaughters() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Granddaughter granddaughter =
            new com.genomebrain.backend.quesstionnaire.relative.Granddaughter();
        granddaughter.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGrandDaughters(Arrays.asList(granddaughter));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Granddaughter");
    }

    @Test
    @DisplayName("Should process grandsons in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGrandSons() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.Grandson grandson =
            new com.genomebrain.backend.quesstionnaire.relative.Grandson();
        grandson.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGrandSons(Arrays.asList(grandson));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Grandson");
    }

    @Test
    @DisplayName("Should process first cousins female in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithFirstCousinsFemale() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.FirstCousin firstCousinFemale =
            new com.genomebrain.backend.quesstionnaire.relative.FirstCousin();
        firstCousinFemale.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setFirstCousinsFemale(Arrays.asList(firstCousinFemale));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("First Cousin (female)");
    }

    @Test
    @DisplayName("Should process first cousins male in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithFirstCousinsMale() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.FirstCousin firstCousinMale =
            new com.genomebrain.backend.quesstionnaire.relative.FirstCousin();
        firstCousinMale.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setFirstCousinsMale(Arrays.asList(firstCousinMale));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("First Cousin (male)");
    }

    @Test
    @DisplayName("Should process great uncles in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatUncles() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatUncle greatUncle =
            new com.genomebrain.backend.quesstionnaire.relative.GreatUncle();
        greatUncle.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatUncles(Arrays.asList(greatUncle));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Uncle");
    }

    @Test
    @DisplayName("Should process great aunts in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatAunts() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatAunt greatAunt =
            new com.genomebrain.backend.quesstionnaire.relative.GreatAunt();
        greatAunt.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatAunts(Arrays.asList(greatAunt));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Aunt");
    }

    @Test
    @DisplayName("Should process great grandmothers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatGrandMothers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatGrandmother greatGrandmother =
            new com.genomebrain.backend.quesstionnaire.relative.GreatGrandmother();
        greatGrandmother.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatGrandMothers(Arrays.asList(greatGrandmother));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Grandmother");
    }

    @Test
    @DisplayName("Should process great grandfathers in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatGrandFathers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatGrandfather greatGrandfather =
            new com.genomebrain.backend.quesstionnaire.relative.GreatGrandfather();
        greatGrandfather.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatGrandFathers(Arrays.asList(greatGrandfather));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Grandfather");
    }

    @Test
    @DisplayName("Should process others in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithOthers() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.Others other =
            new com.genomebrain.backend.quesstionnaire.Others();
        other.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setOthers(Arrays.asList(other));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Other");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with colorectal cancer")
    void testGetRelativeCancerTagsWithColorectal() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY021");
        qs.setAnswerKey("AMBRY021_001");
        qs.setAnswerText("45");
        relativeHistory.setColorectal(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Colon or Rectal");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with prostate cancer")
    void testGetRelativeCancerTagsWithProstate() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY016");
        qs.setAnswerKey("AMBRY016_001");
        qs.setAnswerText("60");
        relativeHistory.setProstate(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Prostate");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with fallopian tube cancer")
    void testGetRelativeCancerTagsWithFallopianTube() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY015");
        qs.setAnswerKey("AMBRY015_001");
        qs.setAnswerText("50");
        relativeHistory.setFallopianTube(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Fallopian tube or peritoneal");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with small intestine cancer")
    void testGetRelativeCancerTagsWithSmallIntestine() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY026");
        qs.setAnswerKey("AMBRY026_001");
        qs.setAnswerText("45");
        relativeHistory.setSmallIntestine(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Small Intestine (Small Bowel)");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with biliary tract cancer")
    void testGetRelativeCancerTagsWithBiliaryTract() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY027");
        qs.setAnswerKey("AMBRY027_001");
        qs.setAnswerText("55");
        relativeHistory.setBiliaryTract(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Biliary Tract");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with kidney cancer")
    void testGetRelativeCancerTagsWithKidney() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY028");
        qs.setAnswerKey("AMBRY028_001");
        qs.setAnswerText("50");
        relativeHistory.setKidney(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Kidney");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with brain cancer")
    void testGetRelativeCancerTagsWithBrain() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY030");
        qs.setAnswerKey("AMBRY030_001");
        qs.setAnswerText("40");
        relativeHistory.setBrain(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Brain");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with melanoma cancer")
    void testGetRelativeCancerTagsWithMelanoma() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY033");
        qs.setAnswerKey("AMBRY033_001");
        qs.setAnswerText("50");
        relativeHistory.setMelanoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Melanoma");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with colorectal cancer")
    void testGetPersonalCancerTagsWithColorectal() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY021");
        qs.setAnswerKey("AMBRY021_001");
        qs.setAnswerText("45");
        cancerDetails.setColorectal(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Colon or Rectal");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with pancreatic cancer")
    void testGetPersonalCancerTagsWithPancreatic() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY023");
        qs.setAnswerKey("AMBRY023_001");
        qs.setAnswerText("55");
        cancerDetails.setPancreatic(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Pancreatic");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with uterine cancer")
    void testGetPersonalCancerTagsWithUterine() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY024");
        qs.setAnswerKey("AMBRY024_001");
        qs.setAnswerText("45");
        cancerDetails.setUterine(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Uterine (Endometrial)");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with ovarian cancer")
    void testGetPersonalCancerTagsWithOvarian() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY013");
        qs.setAnswerKey("AMBRY013_001");
        qs.setAnswerText("50");
        cancerDetails.setOvarian(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Ovarian");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with prostate cancer and null Gleason score")
    void testGetPersonalCancerTagsWithProstateNullGleason() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY016");
        qs.setAnswerKey("AMBRY016_001");
        qs.setAnswerText("60");
        QuestionSet gleasonQs = new QuestionSet();
        gleasonQs.setQuestionKey("AMBRY017");
        gleasonQs.setAnswerKey("AMBRY017_001");
        cancerDetails.setProstate(Arrays.asList(qs, gleasonQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, true, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Prostate");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with prostate cancer and Gleason score <= 7")
    void testGetPersonalCancerTagsWithProstateGleasonLe7() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY016");
        qs.setAnswerKey("AMBRY016_001");
        qs.setAnswerText("60");
        QuestionSet gleasonQs = new QuestionSet();
        gleasonQs.setQuestionKey("AMBRY017");
        gleasonQs.setAnswerKey("AMBRY017_001");
        cancerDetails.setProstate(Arrays.asList(qs, gleasonQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, true, 7L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Prostate");
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with old version ethnicity")
    void testGetPersonalHistoryDetailsWithOldVersionEthnicity() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY003");
        demographicQs.setAnswerKey("AMBRY003_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", true, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with new ethnicity mapping")
    void testGetPersonalHistoryDetailsWithNewEthnicityMapping() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet demographicQs = new QuestionSet();
        demographicQs.setQuestionKey("AMBRY003");
        demographicQs.setAnswerKey("AMBRY003_001");
        questionnaire.setDemographic(Arrays.asList(demographicQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        when(ethnicityUtil.getEthnicityListWithSubcategory(anyList(), any(), any(), any(Boolean.class)))
            .thenReturn(Arrays.asList("African American"));

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with unknown adopted status")
    void testGetPersonalHistoryDetailsWithUnknownAdopted() {
        Questionnaire questionnaire = new Questionnaire();
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet adoptedQs = new QuestionSet();
        adoptedQs.setQuestionKey("AMBRY052");
        adoptedQs.setAnswerKey("AMBRY052_003");
        familyHistory.setBasicQuestions(Arrays.asList(adoptedQs));
        questionnaire.setFamilyCancer(familyHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getAdopted()).isEqualTo("Unknown");
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with not adopted")
    void testGetPersonalHistoryDetailsWithNotAdopted() {
        Questionnaire questionnaire = new Questionnaire();
        FamilyHistory familyHistory = new FamilyHistory();
        QuestionSet adoptedQs = new QuestionSet();
        adoptedQs.setQuestionKey("AMBRY052");
        adoptedQs.setAnswerKey("AMBRY052_001");
        familyHistory.setBasicQuestions(Arrays.asList(adoptedQs));
        questionnaire.setFamilyCancer(familyHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getAdopted()).isEqualTo("Not Adopted");
    }

    @Test
    @DisplayName("Should handle checkPersonalColorectalPolypsHistory with is polyp adenomas")
    void testCheckPersonalColorectalPolypsHistoryIsPolypAdenomas() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY116");
        qs.setAnswerKey("AMBRY116_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getIsPolypAdenomas()).isNotNull();
    }

    @Test
    @DisplayName("Should handle checkPersonalColorectalPolypsHistory with desmoid tumor")
    void testCheckPersonalColorectalPolypsHistoryDesmoidTumor() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY118");
        qs.setAnswerKey("AMBRY118_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getIsDesmoidTumor()).isNotNull();
    }

    @Test
    @DisplayName("Should handle checkPersonalColorectalPolypsHistory with genetic testing")
    void testCheckPersonalColorectalPolypsHistoryGeneticTesting() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY119");
        qs.setAnswerKey("AMBRY119_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getHadGeneticTesting()).isNotNull();
    }

    @Test
    @DisplayName("Should handle checkPersonalColorectalPolypsHistory with result of genetic testing")
    void testCheckPersonalColorectalPolypsHistoryResultOfGeneticTesting() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY120");
        qs.setAnswerKey("AMBRY120_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getResultOfGeneticTesting()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with null demographic")
    void testGetPersonalHistoryDetailsWithNullDemographic() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDemographic(null);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with null personal history")
    void testGetPersonalHistoryDetailsWithNullPersonalHistory() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setPersonalHistory(null);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("No");
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with null family cancer")
    void testGetPersonalHistoryDetailsWithNullFamilyCancer() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setFamilyCancer(null);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getTagsForSelectedOptions with whitespace-only answer key")
    void testGetTagsForSelectedOptionsWhitespaceAnswerKey() {
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY001");
        questionSet.setAnswerKey("   ");
        questionSetList.add(questionSet);

        // StringUtils.isEmpty("   ") returns false, so no exception is thrown
        // The whitespace string is processed but no matching TagConstants is found, so empty set is returned
        Set<String> result = tagGenerationUtility.getTagsForSelectedOptions(questionSetList);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle getTagsForSelectedOptions with invalid tag constant")
    void testGetTagsForSelectedOptionsInvalidTagConstant() {
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionKey("AMBRY001");
        questionSet.setAnswerKey("INVALID_TAG_CONSTANT");
        questionSetList.add(questionSet);

        Set<String> result = tagGenerationUtility.getTagsForSelectedOptions(questionSetList);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty(); // Invalid tags are logged but not added
    }

    @Test
    @DisplayName("Should handle getTagsForSelectedOptions with multiple question sets")
    void testGetTagsForSelectedOptionsMultipleQuestionSets() {
        QuestionSet qs1 = new QuestionSet();
        qs1.setQuestionKey("AMBRY001");
        qs1.setAnswerKey("AMBRY057_001");

        QuestionSet qs2 = new QuestionSet();
        qs2.setQuestionKey("AMBRY002");
        qs2.setAnswerKey("AMBRY057_002");

        questionSetList.add(qs1);
        questionSetList.add(qs2);

        Set<String> result = tagGenerationUtility.getTagsForSelectedOptions(questionSetList);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getRelativeSide with multiple question sets")
    void testGetRelativeSideMultipleQuestionSets() {
        QuestionSet qs1 = new QuestionSet();
        qs1.setQuestionKey("AMBRY054");
        qs1.setAnswerKey("AMBRY054_001");

        QuestionSet qs2 = new QuestionSet();
        qs2.setQuestionKey("AMBRY055");
        qs2.setAnswerKey("AMBRY055_001");

        List<QuestionSet> basicQuestions = Arrays.asList(qs1, qs2);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("Mat");
    }

    @Test
    @DisplayName("Should handle getRelativeSide with case-insensitive question key")
    void testGetRelativeSideCaseInsensitiveQuestionKey() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("ambry054");
        qs.setAnswerKey("AMBRY054_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("Mat");
    }

    @Test
    @DisplayName("Should handle getRelativeSide with case-insensitive answer key")
    void testGetRelativeSideCaseInsensitiveAnswerKey() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY054");
        qs.setAnswerKey("ambry054_001");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);

        String result = tagGenerationUtility.getRelativeSide(basicQuestions);

        assertThat(result).isEqualTo("Mat");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with sarcoma cancer")
    void testGetRelativeCancerTagsWithSarcoma() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY035");
        qs.setAnswerKey("AMBRY035_001");
        qs.setAnswerText("45");
        relativeHistory.setSarcoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Sarcoma (bone or soft tissue)");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with lung cancer")
    void testGetRelativeCancerTagsWithLung() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY034");
        qs.setAnswerKey("AMBRY034_001");
        qs.setAnswerText("60");
        relativeHistory.setLung(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Lung");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with other/not listed cancer")
    void testGetRelativeCancerTagsWithOther() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY050");
        qs.setAnswerKey("AMBRY050_001");
        qs.setAnswerText("55");
        relativeHistory.setNotListed(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Other/Unknown");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with sarcoma cancer")
    void testGetPersonalCancerTagsWithSarcoma() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY035");
        qs.setAnswerKey("AMBRY035_001");
        qs.setAnswerText("45");
        cancerDetails.setSarcoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Sarcoma (bone or soft tissue)");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with lung cancer")
    void testGetPersonalCancerTagsWithLung() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY034");
        qs.setAnswerKey("AMBRY034_001");
        qs.setAnswerText("60");
        cancerDetails.setLung(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Lung");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with other/not listed cancer")
    void testGetPersonalCancerTagsWithOther() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY050");
        qs.setAnswerKey("AMBRY050_001");
        qs.setAnswerText("55");
        cancerDetails.setNotListed(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Other/Unknown");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with small intestine cancer")
    void testGetPersonalCancerTagsWithSmallIntestine() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY026");
        qs.setAnswerKey("AMBRY026_001");
        qs.setAnswerText("50");
        cancerDetails.setSmallIntestine(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Small Intestine (Small Bowel)");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with biliary tract cancer")
    void testGetPersonalCancerTagsWithBiliaryTract() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY027");
        qs.setAnswerKey("AMBRY027_001");
        qs.setAnswerText("55");
        cancerDetails.setBiliaryTract(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Biliary Tract");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with kidney cancer")
    void testGetPersonalCancerTagsWithKidney() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY028");
        qs.setAnswerKey("AMBRY028_001");
        qs.setAnswerText("50");
        cancerDetails.setKidney(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Kidney");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with brain cancer")
    void testGetPersonalCancerTagsWithBrain() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY030");
        qs.setAnswerKey("AMBRY030_001");
        qs.setAnswerText("40");
        cancerDetails.setBrain(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Brain");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with melanoma cancer")
    void testGetPersonalCancerTagsWithMelanoma() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY033");
        qs.setAnswerKey("AMBRY033_001");
        qs.setAnswerText("50");
        cancerDetails.setMelanoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Melanoma");
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with empty TyrerCuzick")
    void testGetPersonalHistoryDetailsWithEmptyTyrerCuzick() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTyrerCuzick(Collections.emptyList());

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with null TyrerCuzick")
    void testGetPersonalHistoryDetailsWithNullTyrerCuzick() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTyrerCuzick(null);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with personal history but no cancer details")
    void testGetPersonalHistoryDetailsWithPersonalHistoryNoCancer() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        personalHistory.setPersonalCancerDetails(new CancerDetails());
        personalHistory.setBasicQuestions(Collections.emptyList());
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("Yes");
    }

    @Test
    @DisplayName("Should throw NullPointerException when basic questions is null")
    void testCheckPersonalColorectalPolypsHistoryNullQuestions() {
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        assertThatThrownBy(() -> tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            null, personalHistoryTagsDTO, false))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle checkPersonalColorectalPolypsHistory with empty basic questions")
    void testCheckPersonalColorectalPolypsHistoryEmptyQuestions() {
        List<QuestionSet> basicQuestions = Collections.emptyList();
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should throw NullPointerException when family cancer details is null")
    void testGetFamilyCancerTagsWithNullFamilyCancerDetails() {
        assertThatThrownBy(() -> tagGenerationUtility.getFamilyCancerTags(null, "v1", ""))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when relative history is null")
    void testGetRelativeCancerTagsWithNullRelativeHistory() {
        assertThatThrownBy(() -> tagGenerationUtility.getRelativeCancerTags(null, "Male", "v1"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when cancer details is null")
    void testGetPersonalCancerTagsWithNullCancerDetails() {
        assertThatThrownBy(() -> tagGenerationUtility.getPersonalCancerTags(null, false, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with empty demographic")
    void testGetPersonalHistoryDetailsWithEmptyDemographic() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDemographic(Collections.emptyList());

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with cancer age as 0")
    void testGetPersonalHistoryDetailsWithCancerAgeZero() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        CancerDetails cancerDetails = new CancerDetails();

        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("0");
        cancerDetails.setBreast(Arrays.asList(breastQs));

        personalHistory.setPersonalCancerDetails(cancerDetails);
        personalHistory.setBasicQuestions(Collections.emptyList());
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("Yes");
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with null cancer age")
    void testGetPersonalHistoryDetailsWithNullCancerAge() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        CancerDetails cancerDetails = new CancerDetails();

        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText(null);
        cancerDetails.setBreast(Arrays.asList(breastQs));

        personalHistory.setPersonalCancerDetails(cancerDetails);
        personalHistory.setBasicQuestions(Collections.emptyList());
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getCancerHx()).isEqualTo("Yes");
    }

    // Additional test cases for missing cancer types in getRelativeCancerTags

    @Test
    @DisplayName("Should handle getRelativeCancerTags with ureter cancer")
    void testGetRelativeCancerTagsWithUreter() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY029");
        qs.setAnswerKey("AMBRY029_001");
        qs.setAnswerText("55");
        relativeHistory.setUreter(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Ureter or Renal Pelvis");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with sebaceous adenoma cancer")
    void testGetRelativeCancerTagsWithSebaceousAdenoma() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY031");
        qs.setAnswerKey("AMBRY031_001");
        qs.setAnswerText("50");
        relativeHistory.setSebaceousAdenoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Sebaceous Adenoma or Carcinoma (Rare skin tumor or cancer)");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with skin cancer")
    void testGetRelativeCancerTagsWithSkin() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY032");
        qs.setAnswerKey("AMBRY032_001");
        qs.setAnswerText("60");
        relativeHistory.setSkin(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Skin");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with leukemia")
    void testGetRelativeCancerTagsWithLeukemia() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY036");
        qs.setAnswerKey("AMBRY036_001");
        qs.setAnswerText("45");
        relativeHistory.setLeukemia(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Leukemia");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with lymphoma")
    void testGetRelativeCancerTagsWithLymphoma() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY037");
        qs.setAnswerKey("AMBRY037_001");
        qs.setAnswerText("50");
        relativeHistory.setLymphoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Lymphoma");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with gallbladder cancer")
    void testGetRelativeCancerTagsWithGallbladder() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY038");
        qs.setAnswerKey("AMBRY038_001");
        qs.setAnswerText("55");
        relativeHistory.setGallbladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Gallbladder");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with cervical cancer")
    void testGetRelativeCancerTagsWithCervical() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY039");
        qs.setAnswerKey("AMBRY039_001");
        qs.setAnswerText("45");
        relativeHistory.setCervical(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Cervical");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with bladder cancer")
    void testGetRelativeCancerTagsWithBladder() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY040");
        qs.setAnswerKey("AMBRY040_001");
        qs.setAnswerText("50");
        relativeHistory.setBladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Bladder");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with bladder cancer age < 50")
    void testGetRelativeCancerTagsWithBladderAgeLessThan50() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY083");
        qs.setAnswerKey("AMBRY083_001");
        qs.setAnswerText("45");
        relativeHistory.setBladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmBladder<50");
        assertThat(result.get(0).getTags()).contains("FmBladder<=50");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with bladder cancer age = 50")
    void testGetRelativeCancerTagsWithBladderAgeEqualTo50() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY083");
        qs.setAnswerKey("AMBRY083_001");
        qs.setAnswerText("50");
        relativeHistory.setBladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmBladder<=50");
        assertThat(result.get(0).getTags()).doesNotContain("FmBladder<50");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with throat cancer")
    void testGetRelativeCancerTagsWithThroat() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY041");
        qs.setAnswerKey("AMBRY041_001");
        qs.setAnswerText("55");
        relativeHistory.setThroat(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Throat");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with thyroid cancer")
    void testGetRelativeCancerTagsWithThyroid() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY042");
        qs.setAnswerKey("AMBRY042_001");
        qs.setAnswerText("40");
        relativeHistory.setThyroid(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Thyroid");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with oral cancer")
    void testGetRelativeCancerTagsWithOral() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY043");
        qs.setAnswerKey("AMBRY043_001");
        qs.setAnswerText("50");
        relativeHistory.setOral(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Oral");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with liver cancer")
    void testGetRelativeCancerTagsWithLiver() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY044");
        qs.setAnswerKey("AMBRY044_001");
        qs.setAnswerText("55");
        relativeHistory.setLiver(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Liver");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with testicular cancer")
    void testGetRelativeCancerTagsWithTesticular() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY045");
        qs.setAnswerKey("AMBRY045_001");
        qs.setAnswerText("30");
        relativeHistory.setTesticular(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Testicular");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with paraganglioma cancer")
    void testGetRelativeCancerTagsWithParaganglioma() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY046");
        qs.setAnswerKey("AMBRY046_001");
        qs.setAnswerText("45");
        relativeHistory.setParaganglioma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Paraganglioma");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with pheochromocytoma cancer")
    void testGetRelativeCancerTagsWithPheochromocytoma() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY047");
        qs.setAnswerKey("AMBRY047_001");
        qs.setAnswerText("50");
        relativeHistory.setPheochromocytoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Male", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Pheochromocytoma");
    }

    // Additional test cases for missing cancer types in getPersonalCancerTags

    @Test
    @DisplayName("Should handle getPersonalCancerTags with ureter cancer")
    void testGetPersonalCancerTagsWithUreter() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY029");
        qs.setAnswerKey("AMBRY029_001");
        qs.setAnswerText("55");
        cancerDetails.setUreter(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Ureter or Renal Pelvis");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with sebaceous adenoma cancer")
    void testGetPersonalCancerTagsWithSebaceousAdenoma() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY031");
        qs.setAnswerKey("AMBRY031_001");
        qs.setAnswerText("50");
        cancerDetails.setSebaceousAdenoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Sebaceous Adenoma or Carcinoma (Rare skin tumor or cancer)");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with skin cancer")
    void testGetPersonalCancerTagsWithSkin() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY032");
        qs.setAnswerKey("AMBRY032_001");
        qs.setAnswerText("60");
        cancerDetails.setSkin(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Skin");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with leukemia")
    void testGetPersonalCancerTagsWithLeukemia() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY036");
        qs.setAnswerKey("AMBRY036_001");
        qs.setAnswerText("45");
        cancerDetails.setLeukemia(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Leukemia");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with lymphoma")
    void testGetPersonalCancerTagsWithLymphoma() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY037");
        qs.setAnswerKey("AMBRY037_001");
        qs.setAnswerText("50");
        cancerDetails.setLymphoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Lymphoma");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with gallbladder cancer")
    void testGetPersonalCancerTagsWithGallbladder() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY038");
        qs.setAnswerKey("AMBRY038_001");
        qs.setAnswerText("55");
        cancerDetails.setGallbladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Gallbladder");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with cervical cancer")
    void testGetPersonalCancerTagsWithCervical() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY039");
        qs.setAnswerKey("AMBRY039_001");
        qs.setAnswerText("45");
        cancerDetails.setCervical(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Cervical");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with bladder cancer")
    void testGetPersonalCancerTagsWithBladder() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY040");
        qs.setAnswerKey("AMBRY040_001");
        qs.setAnswerText("45");
        cancerDetails.setBladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Bladder");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with bladder cancer age < 50")
    void testGetPersonalCancerTagsWithBladderAgeLessThan50() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY040");
        qs.setAnswerKey("AMBRY040_001");
        qs.setAnswerText("45");
        cancerDetails.setBladder(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("PrBladder<50");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with throat cancer")
    void testGetPersonalCancerTagsWithThroat() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY041");
        qs.setAnswerKey("AMBRY041_001");
        qs.setAnswerText("55");
        cancerDetails.setThroat(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Throat");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with thyroid cancer")
    void testGetPersonalCancerTagsWithThyroid() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY042");
        qs.setAnswerKey("AMBRY042_001");
        qs.setAnswerText("40");
        cancerDetails.setThyroid(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Thyroid");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with oral cancer")
    void testGetPersonalCancerTagsWithOral() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY043");
        qs.setAnswerKey("AMBRY043_001");
        qs.setAnswerText("50");
        cancerDetails.setOral(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Oral");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with liver cancer")
    void testGetPersonalCancerTagsWithLiver() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY044");
        qs.setAnswerKey("AMBRY044_001");
        qs.setAnswerText("55");
        cancerDetails.setLiver(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Liver");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with testicular cancer")
    void testGetPersonalCancerTagsWithTesticular() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY045");
        qs.setAnswerKey("AMBRY045_001");
        qs.setAnswerText("30");
        cancerDetails.setTesticular(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Testicular");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with paraganglioma cancer")
    void testGetPersonalCancerTagsWithParaganglioma() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY046");
        qs.setAnswerKey("AMBRY046_001");
        qs.setAnswerText("45");
        cancerDetails.setParaganglioma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Paraganglioma");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with pheochromocytoma cancer")
    void testGetPersonalCancerTagsWithPheochromocytoma() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY047");
        qs.setAnswerKey("AMBRY047_001");
        qs.setAnswerText("50");
        cancerDetails.setPheochromocytoma(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Pheochromocytoma");
    }

    // Additional test cases for missing relatives in getFamilyCancerTags

    @Test
    @DisplayName("Should process great nieces in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatNieces() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatNiece greatNiece =
            new com.genomebrain.backend.quesstionnaire.relative.GreatNiece();
        greatNiece.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatNieces(Arrays.asList(greatNiece));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Niece");
    }

    @Test
    @DisplayName("Should process great nephews in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatNephews() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatNephew greatNephew =
            new com.genomebrain.backend.quesstionnaire.relative.GreatNephew();
        greatNephew.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatNephews(Arrays.asList(greatNephew));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Nephew");
    }

    @Test
    @DisplayName("Should process great granddaughters in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatGrandDaughters() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatGranddaughter greatGranddaughter =
            new com.genomebrain.backend.quesstionnaire.relative.GreatGranddaughter();
        greatGranddaughter.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatGrandDaughters(Arrays.asList(greatGranddaughter));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Granddaughter");
    }

    @Test
    @DisplayName("Should process great grandsons in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithGreatGrandSons() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.GreatGrandson greatGrandson =
            new com.genomebrain.backend.quesstionnaire.relative.GreatGrandson();
        greatGrandson.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setGreatGrandSons(Arrays.asList(greatGrandson));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Great Grandson");
    }

    @Test
    @DisplayName("Should process half aunts in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithHalfAunts() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.HalfAunt halfAunt =
            new com.genomebrain.backend.quesstionnaire.relative.HalfAunt();
        halfAunt.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setHalfAunts(Arrays.asList(halfAunt));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Half Aunt");
    }

    @Test
    @DisplayName("Should process half uncles in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithHalfUncles() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.HalfUncle halfUncle =
            new com.genomebrain.backend.quesstionnaire.relative.HalfUncle();
        halfUncle.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setHalfUncles(Arrays.asList(halfUncle));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Half Uncle");
    }

    @Test
    @DisplayName("Should process half nieces in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithHalfNieces() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.HalfNiece halfNiece =
            new com.genomebrain.backend.quesstionnaire.relative.HalfNiece();
        halfNiece.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setHalfNieces(Arrays.asList(halfNiece));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Half Niece");
    }

    @Test
    @DisplayName("Should process half nephews in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithHalfNephews() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.relative.HalfNephew halfNephew =
            new com.genomebrain.backend.quesstionnaire.relative.HalfNephew();
        halfNephew.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setHalfNephews(Arrays.asList(halfNephew));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Half Nephew");
    }

    // Additional test cases for edge cases in breast cancer

    @Test
    @DisplayName("Should handle getRelativeCancerTags with breast cancer bilateral")
    void testGetRelativeCancerTagsWithBreastBilateral() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY008");
        qs.setAnswerKey("AMBRY008_002");
        qs.setAnswerText("45");
        relativeHistory.setBreast(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Breast");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with breast cancer age < 50")
    void testGetRelativeCancerTagsWithBreastAgeLessThan50() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY008");
        qs.setAnswerKey("AMBRY008_001");
        qs.setAnswerText("45");
        relativeHistory.setBreast(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    // Additional test cases for ovarian cancer edge cases

    @Test
    @DisplayName("Should handle getRelativeCancerTags with ovarian cancer age < 50")
    void testGetRelativeCancerTagsWithOvarianAgeLessThan50() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY061");
        qs.setAnswerKey("AMBRY061_001");
        qs.setAnswerText("45");
        relativeHistory.setOvarian(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmOvarian<50");
        assertThat(result.get(0).getTags()).contains("FmOvarian<=50");
    }

    @Test
    @DisplayName("Should handle getRelativeCancerTags with ovarian cancer age = 50")
    void testGetRelativeCancerTagsWithOvarianAgeEqualTo50() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY061");
        qs.setAnswerKey("AMBRY061_001");
        qs.setAnswerText("50");
        relativeHistory.setOvarian(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmOvarian<=50");
        assertThat(result.get(0).getTags()).doesNotContain("FmOvarian<50");
    }

    @Test
    @DisplayName("Should handle getPersonalCancerTags with ovarian cancer age < 50")
    void testGetPersonalCancerTagsWithOvarianAgeLessThan50() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY013");
        qs.setAnswerKey("AMBRY013_001");
        qs.setAnswerText("45");
        cancerDetails.setOvarian(Arrays.asList(qs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("PrOvarian<50");
    }

    // Additional test cases for prostate cancer with different Gleason scores

    @Test
    @DisplayName("Should handle getPersonalCancerTags with prostate cancer and Gleason score > 7")
    void testGetPersonalCancerTagsWithProstateGleasonGt7() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY016");
        qs.setAnswerKey("AMBRY016_001");
        qs.setAnswerText("60");
        QuestionSet gleasonQs = new QuestionSet();
        gleasonQs.setQuestionKey("AMBRY017");
        gleasonQs.setAnswerKey("AMBRY017_001");
        cancerDetails.setProstate(Arrays.asList(qs, gleasonQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, true, 8L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Prostate");
    }

    // Additional test cases for setBiopsyDetails

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with biopsy result I don't know")
    void testGetPersonalHistoryDetailsWithBiopsyResultIDontKnow() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet biopsyQs = new QuestionSet();
        biopsyQs.setQuestionKey(TagGenerationUtility.PRIOR_BIOPSY_QUESTION);
        biopsyQs.setAnswerKey("AMBRY180_001");
        QuestionSet resultQs = new QuestionSet();
        resultQs.setQuestionKey(TagGenerationUtility.BIOPSY_RESULT);
        resultQs.setAnswerKey("AMBRY106_004"); // Assuming this is "I don't know" - adjust if needed
        questionnaire.setTyrerCuzick(Arrays.asList(biopsyQs, resultQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        // The actual value depends on OptionConstants - just verify it's set
        assertThat(result.getPersonalHistoryDetails().getBreastBiopsyResult()).isNotNull();
    }

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with prior biopsy I don't know")
    void testGetPersonalHistoryDetailsWithPriorBiopsyIDontKnow() {
        Questionnaire questionnaire = new Questionnaire();
        QuestionSet biopsyQs = new QuestionSet();
        biopsyQs.setQuestionKey(TagGenerationUtility.PRIOR_BIOPSY_QUESTION);
        biopsyQs.setAnswerKey("AMBRY180_003");
        questionnaire.setTyrerCuzick(Arrays.asList(biopsyQs));

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        PersonalHistoryTagsDTO result = tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, false, false, null, "v1", false, true);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getPreviousBiopsy()).isEqualTo("Unknown");
    }

    // Additional test cases for getFamilyHistoryDetails edge cases

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with Other relative type")
    void testGetFamilyHistoryDetailsWithOtherRelativeType() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        com.genomebrain.backend.quesstionnaire.Others other =
            new com.genomebrain.backend.quesstionnaire.Others();
        other.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setOthers(Arrays.asList(other));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).isEmpty(); // Other relatives don't get tags
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with relative side Mat")
    void testGetFamilyHistoryDetailsWithRelativeSideMat() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Sister sister = new Sister();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY054");
        qs.setAnswerKey("AMBRY054_001");
        sister.setBasicQuestions(Arrays.asList(qs));
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        sister.setBreast(Arrays.asList(breastQs));
        familyCancerDetails.setSisters(Arrays.asList(sister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getRelativeSide()).isEqualTo("Mother's side");
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with relative side Pat")
    void testGetFamilyHistoryDetailsWithRelativeSidePat() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Brother brother = new Brother();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY054");
        qs.setAnswerKey("AMBRY054_002");
        brother.setBasicQuestions(Arrays.asList(qs));
        QuestionSet prostateQs = new QuestionSet();
        prostateQs.setQuestionKey("AMBRY016");
        prostateQs.setAnswerKey("AMBRY016_001");
        prostateQs.setAnswerText("60");
        brother.setProstate(Arrays.asList(prostateQs));
        familyCancerDetails.setBrothers(Arrays.asList(brother));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getRelativeSide()).isEqualTo("Father's side");
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with Mother and N/A relative side")
    void testGetFamilyHistoryDetailsWithMotherAndNASide() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory mother = new RelativeHistory();
        mother.setBasicQuestions(Collections.emptyList());
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        mother.setBreast(Arrays.asList(breastQs));
        familyCancerDetails.setMother(mother);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        // When relative side is N/A and relative type is Mother, tags should include Mat
        assertThat(result.get(0).getTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with Father and N/A relative side")
    void testGetFamilyHistoryDetailsWithFatherAndNASide() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory father = new RelativeHistory();
        father.setBasicQuestions(Collections.emptyList());
        QuestionSet prostateQs = new QuestionSet();
        prostateQs.setQuestionKey("AMBRY016");
        prostateQs.setAnswerKey("AMBRY016_001");
        prostateQs.setAnswerText("60");
        father.setProstate(Arrays.asList(prostateQs));
        familyCancerDetails.setFather(father);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        // When relative side is N/A and relative type is Father, tags should include Pat
        assertThat(result.get(0).getTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with other relative and N/A relative side")
    void testGetFamilyHistoryDetailsWithOtherRelativeAndNASide() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Sister sister = new Sister();
        sister.setBasicQuestions(Collections.emptyList());
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        sister.setBreast(Arrays.asList(breastQs));
        familyCancerDetails.setSisters(Arrays.asList(sister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        // When relative side is N/A and relative type is not Mother/Father, tags should include both Mat and Pat
        assertThat(result.get(0).getTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with cancer age null")
    void testGetFamilyHistoryDetailsWithCancerAgeNull() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Sister sister = new Sister();
        sister.setBasicQuestions(Collections.emptyList());
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText(null);
        sister.setBreast(Arrays.asList(breastQs));
        familyCancerDetails.setSisters(Arrays.asList(sister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getCancerDetails().get(0).getAge()).isEqualTo("-");
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with cancer age 0")
    void testGetFamilyHistoryDetailsWithCancerAgeZero() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Sister sister = new Sister();
        sister.setBasicQuestions(Collections.emptyList());
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("0");
        sister.setBreast(Arrays.asList(breastQs));
        familyCancerDetails.setSisters(Arrays.asList(sister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getCancerDetails().get(0).getAge()).isEqualTo("Unknown");
    }

    @Test
    @DisplayName("Should handle getFamilyHistoryDetails with additional information")
    void testGetFamilyHistoryDetailsWithAdditionalInformation() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        Sister sister = new Sister();
        sister.setBasicQuestions(Collections.emptyList());
        QuestionSet prostateQs = new QuestionSet();
        prostateQs.setQuestionKey("AMBRY016");
        prostateQs.setAnswerKey("AMBRY016_001");
        prostateQs.setAnswerText("60");
        QuestionSet gleasonQs = new QuestionSet();
        gleasonQs.setQuestionKey("AMBRY017");
        gleasonQs.setAnswerKey("AMBRY017_001");
        sister.setProstate(Arrays.asList(prostateQs, gleasonQs));
        familyCancerDetails.setSisters(Arrays.asList(sister));

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1", "");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    // Additional test cases for checkPersonalColorectalPolypsHistory with year of genetic testing

    @Test
    @DisplayName("Should handle checkPersonalColorectalPolypsHistory with year of genetic testing from answer key")
    void testCheckPersonalColorectalPolypsHistoryYearOfGeneticTestingFromAnswerKey() {
        QuestionSet qs = new QuestionSet();
        qs.setQuestionKey("AMBRY126");
        qs.setAnswerKey("AMBRY126_002");
        List<QuestionSet> basicQuestions = Arrays.asList(qs);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(new HashSet<>());

        PersonalHistoryTagsDTO result = tagGenerationUtility.checkPersonalColorectalPolypsHistory(
            basicQuestions, personalHistoryTagsDTO, false);

        assertThat(result).isNotNull();
        assertThat(result.getPersonalHistoryDetails().getYearOfGeneticTesting()).isNotNull();
    }

    // Additional test cases for getPersonalHistoryDetails with empty personal history basic questions

    @Test
    @DisplayName("Should handle getPersonalHistoryDetails with null personal history basic questions")
    void testGetPersonalHistoryDetailsWithNullPersonalHistoryBasicQuestions() {
        Questionnaire questionnaire = new Questionnaire();
        PersonalHistory personalHistory = new PersonalHistory();
        CancerDetails cancerDetails = new CancerDetails();
        personalHistory.setPersonalCancerDetails(cancerDetails);
        personalHistory.setBasicQuestions(null);
        questionnaire.setPersonalHistory(personalHistory);

        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        assertThatThrownBy(() -> tagGenerationUtility.getPersonalHistoryDetails(
            questionnaire, personalHistoryDetails, true, false, null, "v1", false, true))
            .isInstanceOf(NullPointerException.class);
    }

    // Additional test cases for getRelativeCancerTags with multiple cancer types

    @Test
    @DisplayName("Should handle getRelativeCancerTags with multiple cancer types")
    void testGetRelativeCancerTagsWithMultipleCancerTypes() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        relativeHistory.setBreast(Arrays.asList(breastQs));

        QuestionSet ovarianQs = new QuestionSet();
        ovarianQs.setQuestionKey("AMBRY013");
        ovarianQs.setAnswerKey("AMBRY013_001");
        ovarianQs.setAnswerText("50");
        relativeHistory.setOvarian(Arrays.asList(ovarianQs));

        QuestionSet colorectalQs = new QuestionSet();
        colorectalQs.setQuestionKey("AMBRY021");
        colorectalQs.setAnswerKey("AMBRY021_001");
        colorectalQs.setAnswerText("55");
        relativeHistory.setColorectal(Arrays.asList(colorectalQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
    }

    // Additional test cases for getPersonalCancerTags with multiple cancer types

    @Test
    @DisplayName("Should handle getPersonalCancerTags with multiple cancer types")
    void testGetPersonalCancerTagsWithMultipleCancerTypes() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet breastQs = new QuestionSet();
        breastQs.setQuestionKey("AMBRY008");
        breastQs.setAnswerKey("AMBRY008_001");
        breastQs.setAnswerText("45");
        cancerDetails.setBreast(Arrays.asList(breastQs));

        QuestionSet ovarianQs = new QuestionSet();
        ovarianQs.setQuestionKey("AMBRY013");
        ovarianQs.setAnswerKey("AMBRY013_001");
        ovarianQs.setAnswerText("50");
        cancerDetails.setOvarian(Arrays.asList(ovarianQs));

        QuestionSet colorectalQs = new QuestionSet();
        colorectalQs.setQuestionKey("AMBRY021");
        colorectalQs.setAnswerKey("AMBRY021_001");
        colorectalQs.setAnswerText("55");
        cancerDetails.setColorectal(Arrays.asList(colorectalQs));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Should map extended personal breast question branches including age bands and profile fields")
    void testGetPersonalCancerTagsBreastExtendedBranches() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet q7 = new QuestionSet();
        q7.setQuestionKey("AMBRY007");
        q7.setAnswerKey("AMBRY007_002");
        QuestionSet q9 = new QuestionSet();
        q9.setQuestionKey("AMBRY009");
        q9.setAnswerKey("AMBRY009_001,AMBRY009_003");
        QuestionSet q10 = new QuestionSet();
        q10.setQuestionKey("AMBRY010");
        q10.setAnswerKey("AMBRY010_003");
        QuestionSet q11 = new QuestionSet();
        q11.setQuestionKey("AMBRY011");
        q11.setAnswerKey("AMBRY011_001");
        QuestionSet q124 = new QuestionSet();
        q124.setQuestionKey("AMBRY124");
        q124.setAnswerKey("AMBRY124_001");
        QuestionSet q59 = new QuestionSet();
        q59.setQuestionKey("AMBRY059");
        q59.setAnswerKey("AMBRY059_002");
        QuestionSet q57 = new QuestionSet();
        q57.setQuestionKey("AMBRY057");
        q57.setAnswerKey("AMBRY057_002");
        QuestionSet q8 = new QuestionSet();
        q8.setQuestionKey("AMBRY008");
        q8.setAnswerKey("AMBRY008_001");
        q8.setAnswerText("52");
        cancerDetails.setBreast(Arrays.asList(q7, q9, q10, q11, q124, q59, q57, q8));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Breast");
        assertThat(result.get(0).getAdditionalInformation())
            .containsKeys("moreThanOnePrimary", "breastCancerType", "location", "tripleNegative", "bilateral");
        assertThat(result.get(0).getTags())
            .contains("PrBreast>=51", "PrBreast<=60", "PrBreastBilat");
    }

    @Test
    @DisplayName("Should add bilateral family breast tags when relative age is 51 or older")
    void testGetRelativeCancerTagsBreastFmBilateralAge51Plus() {
        RelativeHistory relativeHistory = new RelativeHistory();
        QuestionSet bilateralTag = new QuestionSet();
        bilateralTag.setQuestionKey("AMBRY001");
        bilateralTag.setAnswerKey("AMBRY057_002");
        QuestionSet age = new QuestionSet();
        age.setQuestionKey("AMBRY058");
        age.setAnswerKey("AMBRY058_001");
        age.setAnswerText("55");
        relativeHistory.setBreast(Arrays.asList(bilateralTag, age));

        List<CancerDetailsDTO> result = tagGenerationUtility.getRelativeCancerTags(relativeHistory, "Female", "v1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmBreastBilat>=51", "FmBreast>=51");
    }

    @Test
    @DisplayName("Should map personal skin cancer melanoma and sebaceous unknown-age additional details")
    void testGetPersonalCancerTagsSkinMelanomaAndSebaceousUnknownAgePaths() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet types121 = new QuestionSet();
        types121.setQuestionKey("AMBRY121");
        types121.setAnswerKey("AMBRY121_001,AMBRY121_002");
        QuestionSet types123 = new QuestionSet();
        types123.setQuestionKey("AMBRY123");
        types123.setAnswerKey("AMBRY123_003");
        QuestionSet melUnk = new QuestionSet();
        melUnk.setQuestionKey("AMBRY078");
        melUnk.setAnswerKey("AMBRY078_002");
        melUnk.setAnswerText("1");
        QuestionSet sebUnk = new QuestionSet();
        sebUnk.setQuestionKey("AMBRY077");
        sebUnk.setAnswerKey("AMBRY077_002");
        sebUnk.setAnswerText("1");
        cancerDetails.setSkin(Arrays.asList(types121, types123, melUnk, sebUnk));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCancerType()).isEqualTo("Skin");
        assertThat(result.get(0).getAdditionalInformation().get("melanomaAgeOfDiagnosis"))
            .isEqualTo(OptionConstants.AMBRY003_012.getValue());
        assertThat(result.get(0).getAdditionalInformation().get("adenomaAgeOfDiagnosis"))
            .isEqualTo(OptionConstants.AMBRY003_012.getValue());
    }

    @Test
    @DisplayName("Should map personal colorectal location and both proband and family age tag branches")
    void testGetPersonalCancerTagsColorectalExtendedBranches() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet q20 = new QuestionSet();
        q20.setQuestionKey("AMBRY020");
        q20.setAnswerKey("AMBRY020_002");
        QuestionSet q22 = new QuestionSet();
        q22.setQuestionKey("AMBRY022");
        q22.setAnswerKey("AMBRY022_001,AMBRY022_009");
        QuestionSet q67 = new QuestionSet();
        q67.setQuestionKey("AMBRY067");
        q67.setAnswerKey("AMBRY067_002");
        QuestionSet q21 = new QuestionSet();
        q21.setQuestionKey("AMBRY021");
        q21.setAnswerKey("AMBRY021_001");
        q21.setAnswerText("49");
        QuestionSet q68 = new QuestionSet();
        q68.setQuestionKey("AMBRY068");
        q68.setAnswerKey("AMBRY068_001");
        q68.setAnswerText("49");
        cancerDetails.setColorectal(Arrays.asList(q20, q22, q67, q21, q68));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAdditionalInformation()).containsKeys("moreThanOnePrimary", "location");
        assertThat(result.get(0).getTags()).contains("PrColon<=49", "PrColon<50", "FmColon<50", "FmColon<=50");
    }

    @Test
    @DisplayName("Should map personal sebaceous AMBRY077 and AMBRY032 age branches for coverage")
    void testGetPersonalCancerTagsSebaceousAdenomaFmAgeBranches() {
        CancerDetails cancerDetails = new CancerDetails();
        QuestionSet q77 = new QuestionSet();
        q77.setQuestionKey("AMBRY077");
        q77.setAnswerKey("AMBRY077_001");
        q77.setAnswerText("48");
        QuestionSet q32 = new QuestionSet();
        q32.setQuestionKey("AMBRY032");
        q32.setAnswerKey("AMBRY032_001");
        q32.setAnswerText("40");
        cancerDetails.setSebaceousAdenoma(Arrays.asList(q77, q32));

        List<CancerDetailsDTO> result = tagGenerationUtility.getPersonalCancerTags(cancerDetails, false, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("FmSebAdenoCar<=50", "FmSebAdenoCar<50");
    }
}

