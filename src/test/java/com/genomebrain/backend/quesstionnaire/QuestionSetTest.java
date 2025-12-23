package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link QuestionSet}.
 */
@DisplayName("QuestionSet Unit Tests")
class QuestionSetTest {

    private QuestionSet questionSet;

    @BeforeEach
    void setUp() {
        questionSet = new QuestionSet();
    }

    @Test
    @DisplayName("Should get and set QuestionText")
    void testQuestionText() {
        questionSet.setQuestionText("What is your age?");
        assertThat(questionSet.getQuestionText()).isEqualTo("What is your age?");
    }

    @Test
    @DisplayName("Should get and set QuestionKey")
    void testQuestionKey() {
        questionSet.setQuestionKey("AGE");
        assertThat(questionSet.getQuestionKey()).isEqualTo("AGE");
    }

    @Test
    @DisplayName("Should get and set AnswerText")
    void testAnswerText() {
        questionSet.setAnswerText("40");
        assertThat(questionSet.getAnswerText()).isEqualTo("40");
    }

    @Test
    @DisplayName("Should get and set AnswerKey")
    void testAnswerKey() {
        questionSet.setAnswerKey("AGE_40");
        assertThat(questionSet.getAnswerKey()).isEqualTo("AGE_40");
    }

    @Test
    @DisplayName("Should get and set VartagName")
    void testVartagName() {
        questionSet.setVartagName("age");
        assertThat(questionSet.getVartagName()).isEqualTo("age");
    }

    @Test
    @DisplayName("Should get and set category")
    void testCategory() {
        questionSet.setCategory("Demographic");
        assertThat(questionSet.getCategory()).isEqualTo("Demographic");
    }

    @Test
    @DisplayName("Should get and set previewLabel")
    void testPreviewLabel() {
        questionSet.setPreviewLabel("Age: 40");
        assertThat(questionSet.getPreviewLabel()).isEqualTo("Age: 40");
    }

    @Test
    @DisplayName("Should get and set previewLabel_ES")
    void testPreviewLabelES() {
        questionSet.setPreviewLabel_ES("Edad: 40");
        assertThat(questionSet.getPreviewLabel_ES()).isEqualTo("Edad: 40");
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        questionSet.setQuestionText(null);
        assertThat(questionSet.getQuestionText()).isNull();
        
        questionSet.setQuestionKey(null);
        assertThat(questionSet.getQuestionKey()).isNull();
        
        questionSet.setAnswerText(null);
        assertThat(questionSet.getAnswerText()).isNull();
        
        questionSet.setAnswerKey(null);
        assertThat(questionSet.getAnswerKey()).isNull();
        
        questionSet.setVartagName(null);
        assertThat(questionSet.getVartagName()).isNull();
        
        questionSet.setCategory(null);
        assertThat(questionSet.getCategory()).isNull();
        
        questionSet.setPreviewLabel(null);
        assertThat(questionSet.getPreviewLabel()).isNull();
        
        questionSet.setPreviewLabel_ES(null);
        assertThat(questionSet.getPreviewLabel_ES()).isNull();
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        questionSet.setQuestionText("");
        assertThat(questionSet.getQuestionText()).isEmpty();
        
        questionSet.setQuestionKey("");
        assertThat(questionSet.getQuestionKey()).isEmpty();
        
        questionSet.setAnswerText("");
        assertThat(questionSet.getAnswerText()).isEmpty();
    }

    @Test
    @DisplayName("Should handle all fields set")
    void testAllFieldsSet() {
        questionSet.setQuestionText("What is your age?");
        questionSet.setQuestionKey("AGE");
        questionSet.setAnswerText("40");
        questionSet.setAnswerKey("AGE_40");
        questionSet.setVartagName("age");
        questionSet.setCategory("Demographic");
        questionSet.setPreviewLabel("Age: 40");
        questionSet.setPreviewLabel_ES("Edad: 40");
        
        assertThat(questionSet.getQuestionText()).isEqualTo("What is your age?");
        assertThat(questionSet.getQuestionKey()).isEqualTo("AGE");
        assertThat(questionSet.getAnswerText()).isEqualTo("40");
        assertThat(questionSet.getAnswerKey()).isEqualTo("AGE_40");
        assertThat(questionSet.getVartagName()).isEqualTo("age");
        assertThat(questionSet.getCategory()).isEqualTo("Demographic");
        assertThat(questionSet.getPreviewLabel()).isEqualTo("Age: 40");
        assertThat(questionSet.getPreviewLabel_ES()).isEqualTo("Edad: 40");
    }
}

