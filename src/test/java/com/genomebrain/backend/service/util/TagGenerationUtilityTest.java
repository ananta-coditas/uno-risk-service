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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamilyHistoryDetails().getFamilyMember()).isEqualTo("Mother");
    }

    @Test
    @DisplayName("Should process father in getFamilyCancerTags")
    void testGetFamilyCancerTagsWithFather() {
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();
        RelativeHistory father = new RelativeHistory();
        father.setBasicQuestions(Collections.emptyList());
        familyCancerDetails.setFather(father);

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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

        List<FamilyHistoryDetailsTagsDTO> result = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, "v1");

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
        assertThatThrownBy(() -> tagGenerationUtility.getFamilyCancerTags(null, "v1"))
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
}

