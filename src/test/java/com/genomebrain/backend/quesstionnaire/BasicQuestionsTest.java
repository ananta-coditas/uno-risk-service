package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BasicQuestions}.
 */
@DisplayName("BasicQuestions Unit Tests")
class BasicQuestionsTest {

    private BasicQuestions basicQuestions;
    private List<QuestionSet> testQuestionSetList;

    @BeforeEach
    void setUp() {
        basicQuestions = new BasicQuestions();
        testQuestionSetList = new ArrayList<>();
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionText("Test Question");
        questionSet.setAnswerText("Test Answer");
        testQuestionSetList.add(questionSet);
    }

    @Test
    @DisplayName("Should get and set Relatives")
    void testRelatives() {
        basicQuestions.setRelatives(testQuestionSetList);
        assertThat(basicQuestions.getRelatives()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should handle null Relatives")
    void testNullRelatives() {
        basicQuestions.setRelatives(null);
        assertThat(basicQuestions.getRelatives()).isNull();
    }

    @Test
    @DisplayName("Should handle empty Relatives list")
    void testEmptyRelatives() {
        List<QuestionSet> emptyList = new ArrayList<>();
        basicQuestions.setRelatives(emptyList);
        assertThat(basicQuestions.getRelatives()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        basicQuestions.setRelatives(testQuestionSetList);
        
        String result = basicQuestions.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("BasicQuestions");
        assertThat(result).contains("Relatives");
    }

    @Test
    @DisplayName("Should return correct toString with null Relatives")
    void testToStringWithNullRelatives() {
        basicQuestions.setRelatives(null);
        
        String result = basicQuestions.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("BasicQuestions");
        assertThat(result).contains("Relatives");
    }

    @Test
    @DisplayName("Should handle multiple question sets")
    void testMultipleQuestionSets() {
        List<QuestionSet> multipleSets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            QuestionSet qs = new QuestionSet();
            qs.setQuestionText("Question " + i);
            qs.setAnswerText("Answer " + i);
            multipleSets.add(qs);
        }
        
        basicQuestions.setRelatives(multipleSets);
        assertThat(basicQuestions.getRelatives()).hasSize(5);
        assertThat(basicQuestions.getRelatives().get(0).getQuestionText()).isEqualTo("Question 0");
    }
}

