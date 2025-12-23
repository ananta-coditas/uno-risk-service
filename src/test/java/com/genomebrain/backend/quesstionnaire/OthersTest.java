package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.RelativeDetail;

/**
 * Unit tests for {@link Others}.
 */
@DisplayName("Others Unit Tests")
class OthersTest {

    @Test
    @DisplayName("Should create Others instance")
    void testOthers() {
        Others others = new Others();
        assertThat(others).isNotNull();
        assertThat(others).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should use RelativeHistory methods")
    void testOthersCanUseRelativeHistoryMethods() {
        Others others = new Others();
        List<QuestionSet> questionSets = new ArrayList<>();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionText("Test");
        questionSets.add(qs);
        
        others.setBasicQuestions(questionSets);
        others.setBreast(questionSets);
        
        RelativeDetail detail = new RelativeDetail();
        detail.setRelativeID("REL001");
        others.setRelativeDetail(detail);
        
        assertThat(others.getBasicQuestions()).isEqualTo(questionSets);
        assertThat(others.getBreast()).isEqualTo(questionSets);
        assertThat(others.getRelativeDetail()).isEqualTo(detail);
    }
}

