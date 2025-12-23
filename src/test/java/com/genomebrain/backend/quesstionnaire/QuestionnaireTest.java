package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.service.dto.FamilyHistory;

/**
 * Unit tests for {@link Questionnaire}.
 */
@DisplayName("Questionnaire Unit Tests")
class QuestionnaireTest {

    private Questionnaire questionnaire;
    private List<QuestionSet> testQuestionSetList;
    private PersonalHistory personalHistory;
    private FamilyHistory familyHistory;
    private AdditionalDetails additionalDetails;

    @BeforeEach
    void setUp() {
        questionnaire = new Questionnaire();
        testQuestionSetList = new ArrayList<>();
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionText("Test Question");
        questionSet.setAnswerText("Test Answer");
        testQuestionSetList.add(questionSet);
        
        personalHistory = new PersonalHistory();
        familyHistory = new FamilyHistory();
        additionalDetails = new AdditionalDetails();
    }

    @Test
    @DisplayName("Should get default source")
    void testDefaultSource() {
        assertThat(questionnaire.getSource()).isEqualTo("Default");
    }

    @Test
    @DisplayName("Should get and set source")
    void testSource() {
        questionnaire.setSource("Custom");
        assertThat(questionnaire.getSource()).isEqualTo("Custom");
    }

    @Test
    @DisplayName("Should get and set assessmentId")
    void testAssessmentId() {
        questionnaire.setAssessmentId("ASSESS001");
        assertThat(questionnaire.getAssessmentId()).isEqualTo("ASSESS001");
    }

    @Test
    @DisplayName("Should get and set demographic")
    void testDemographic() {
        questionnaire.setDemographic(testQuestionSetList);
        assertThat(questionnaire.getDemographic()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set personalHistory")
    void testPersonalHistory() {
        questionnaire.setPersonalHistory(personalHistory);
        assertThat(questionnaire.getPersonalHistory()).isEqualTo(personalHistory);
    }

    @Test
    @DisplayName("Should get and set familyCancer")
    void testFamilyCancer() {
        questionnaire.setFamilyCancer(familyHistory);
        assertThat(questionnaire.getFamilyCancer()).isEqualTo(familyHistory);
    }

    @Test
    @DisplayName("Should get and set tyrerCuzick")
    void testTyrerCuzick() {
        questionnaire.setTyrerCuzick(testQuestionSetList);
        assertThat(questionnaire.getTyrerCuzick()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set customCategories")
    void testCustomCategories() {
        questionnaire.setCustomCategories(testQuestionSetList);
        assertThat(questionnaire.getCustomCategories()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set additionalDetails")
    void testAdditionalDetails() {
        questionnaire.setAdditionalDetails(additionalDetails);
        assertThat(questionnaire.getAdditionalDetails()).isEqualTo(additionalDetails);
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        questionnaire.setAssessmentId(null);
        assertThat(questionnaire.getAssessmentId()).isNull();
        
        questionnaire.setDemographic(null);
        assertThat(questionnaire.getDemographic()).isNull();
        
        questionnaire.setPersonalHistory(null);
        assertThat(questionnaire.getPersonalHistory()).isNull();
        
        questionnaire.setFamilyCancer(null);
        assertThat(questionnaire.getFamilyCancer()).isNull();
        
        questionnaire.setTyrerCuzick(null);
        assertThat(questionnaire.getTyrerCuzick()).isNull();
        
        questionnaire.setCustomCategories(null);
        assertThat(questionnaire.getCustomCategories()).isNull();
        
        questionnaire.setAdditionalDetails(null);
        assertThat(questionnaire.getAdditionalDetails()).isNull();
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        List<QuestionSet> emptyList = new ArrayList<>();
        questionnaire.setDemographic(emptyList);
        assertThat(questionnaire.getDemographic()).isEmpty();
        
        questionnaire.setTyrerCuzick(emptyList);
        assertThat(questionnaire.getTyrerCuzick()).isEmpty();
        
        questionnaire.setCustomCategories(emptyList);
        assertThat(questionnaire.getCustomCategories()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        questionnaire.setAssessmentId("ASSESS001");
        questionnaire.setDemographic(testQuestionSetList);
        questionnaire.setPersonalHistory(personalHistory);
        questionnaire.setFamilyCancer(familyHistory);
        questionnaire.setTyrerCuzick(testQuestionSetList);
        questionnaire.setCustomCategories(testQuestionSetList);
        questionnaire.setAdditionalDetails(additionalDetails);
        
        String result = questionnaire.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("Questionnaire");
        assertThat(result).contains("ASSESS001");
        assertThat(result).contains("demographic");
        assertThat(result).contains("personalHistory");
        assertThat(result).contains("familyCancer");
        assertThat(result).contains("tyrerCuzick");
        assertThat(result).contains("customCategories");
        assertThat(result).contains("additionalDetails");
    }

    @Test
    @DisplayName("Should return correct toString with null values")
    void testToStringWithNullValues() {
        questionnaire.setAssessmentId("ASSESS001");
        questionnaire.setDemographic(null);
        questionnaire.setPersonalHistory(null);
        questionnaire.setFamilyCancer(null);
        questionnaire.setTyrerCuzick(null);
        questionnaire.setCustomCategories(null);
        questionnaire.setAdditionalDetails(null);
        
        String result = questionnaire.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("Questionnaire");
        assertThat(result).contains("ASSESS001");
    }

    @Test
    @DisplayName("Should handle multiple question sets in lists")
    void testMultipleQuestionSets() {
        List<QuestionSet> multipleSets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            QuestionSet qs = new QuestionSet();
            qs.setQuestionText("Question " + i);
            qs.setAnswerText("Answer " + i);
            multipleSets.add(qs);
        }
        
        questionnaire.setDemographic(multipleSets);
        assertThat(questionnaire.getDemographic()).hasSize(5);
        assertThat(questionnaire.getDemographic().get(0).getQuestionText()).isEqualTo("Question 0");
    }
}

