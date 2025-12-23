package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PersonalHistory}.
 */
@DisplayName("PersonalHistory Unit Tests")
class PersonalHistoryTest {

    private PersonalHistory personalHistory;
    private List<QuestionSet> testQuestionSetList;
    private CancerDetails cancerDetails;

    @BeforeEach
    void setUp() {
        personalHistory = new PersonalHistory();
        testQuestionSetList = new ArrayList<>();
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionText("Test Question");
        questionSet.setAnswerText("Test Answer");
        testQuestionSetList.add(questionSet);
        
        cancerDetails = new CancerDetails();
    }

    @Test
    @DisplayName("Should get and set basicQuestions")
    void testBasicQuestions() {
        personalHistory.setBasicQuestions(testQuestionSetList);
        assertThat(personalHistory.getBasicQuestions()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set personalCancerDetails")
    void testPersonalCancerDetails() {
        personalHistory.setPersonalCancerDetails(cancerDetails);
        assertThat(personalHistory.getPersonalCancerDetails()).isEqualTo(cancerDetails);
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        personalHistory.setBasicQuestions(null);
        assertThat(personalHistory.getBasicQuestions()).isNull();
        
        personalHistory.setPersonalCancerDetails(null);
        assertThat(personalHistory.getPersonalCancerDetails()).isNull();
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        List<QuestionSet> emptyList = new ArrayList<>();
        personalHistory.setBasicQuestions(emptyList);
        assertThat(personalHistory.getBasicQuestions()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        personalHistory.setBasicQuestions(testQuestionSetList);
        personalHistory.setPersonalCancerDetails(cancerDetails);
        
        String result = personalHistory.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("PersonalHistory");
        assertThat(result).contains("basicQuestions");
        assertThat(result).contains("personalCancerDetails");
    }

    @Test
    @DisplayName("Should return correct toString with null values")
    void testToStringWithNullValues() {
        personalHistory.setBasicQuestions(null);
        personalHistory.setPersonalCancerDetails(null);
        
        String result = personalHistory.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("PersonalHistory");
    }
}

