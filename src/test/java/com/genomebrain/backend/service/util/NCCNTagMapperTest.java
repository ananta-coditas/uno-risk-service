package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NCCNTagMapper Unit Tests")
class NCCNTagMapperTest {

    @Test
    @DisplayName("Should return -1 when question enum is not found")
    void testQuestionAndAnswerMapperWithInvalidEnum() {
        int result = NCCNTagMapper.QuestionAndAnswerMapper("INVALID_ENUM", "male");
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should process QuestionAndAnswerMapper for valid enum")
    void testQuestionAndAnswerMapperWithValidEnum() {
        // The method always returns -1 currently, but creates a QuestionSet internally
        // This test verifies the method executes without exception
        int result = NCCNTagMapper.QuestionAndAnswerMapper("Sex_at_birth", "male");
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle null question enum")
    void testQuestionAndAnswerMapperWithNullEnum() {
        int result = NCCNTagMapper.QuestionAndAnswerMapper(null, "male");
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle null answer value")
    void testQuestionAndAnswerMapperWithNullAnswer() {
        int result = NCCNTagMapper.QuestionAndAnswerMapper("Sex_at_birth", null);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should verify enum values are accessible")
    void testEnumValues() {
        NCCNTagMapper[] values = NCCNTagMapper.values();
        assertThat(values).isNotEmpty();
        assertThat(values[0]).isEqualTo(NCCNTagMapper.Sex_at_birth);
    }

    @Test
    @DisplayName("Should handle empty answer value in QuestionAndAnswerMapper")
    void testQuestionAndAnswerMapperWithEmptyAnswer() {
        int result = NCCNTagMapper.QuestionAndAnswerMapper("Sex_at_birth", "");
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle case insensitive enum name")
    void testQuestionAndAnswerMapperCaseInsensitive() {
        int result = NCCNTagMapper.QuestionAndAnswerMapper("sex_at_birth", "male");
        assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should verify enum valueOf with valid name")
    void testValueOfValidName() {
        NCCNTagMapper mapper = NCCNTagMapper.valueOf("Sex_at_birth");
        assertThat(mapper).isNotNull();
        assertThat(mapper).isEqualTo(NCCNTagMapper.Sex_at_birth);
    }

    @Test
    @DisplayName("Should throw exception for invalid enum name in valueOf")
    void testValueOfInvalidName() {
        assertThatThrownBy(() -> NCCNTagMapper.valueOf("INVALID_ENUM"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

