package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NCCNAnswerMapper Unit Tests")
class NCCNAnswerMapperTest {

    @Test
    @DisplayName("Should have sex_at_birth enum value")
    void testSexAtBirthEnumValue() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.sex_at_birth;
        assertThat(mapper).isNotNull();
        assertThat(mapper.name()).isEqualTo("sex_at_birth");
    }

    @Test
    @DisplayName("Should have only one enum value")
    void testEnumValuesCount() {
        NCCNAnswerMapper[] values = NCCNAnswerMapper.values();
        assertThat(values).hasSize(1);
        assertThat(values[0]).isEqualTo(NCCNAnswerMapper.sex_at_birth);
    }

    @Test
    @DisplayName("Should return enum value by name")
    void testValueOf() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.valueOf("sex_at_birth");
        assertThat(mapper).isEqualTo(NCCNAnswerMapper.sex_at_birth);
    }

    @Test
    @DisplayName("Should throw exception for invalid enum name in valueOf")
    void testValueOfInvalidName() {
        assertThatThrownBy(() -> NCCNAnswerMapper.valueOf("INVALID_ENUM"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw exception for null enum name in valueOf")
    void testValueOfNullName() {
        assertThatThrownBy(() -> NCCNAnswerMapper.valueOf(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle case sensitive enum name in valueOf")
    void testValueOfCaseSensitive() {
        assertThatThrownBy(() -> NCCNAnswerMapper.valueOf("SEX_AT_BIRTH"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should verify enum toString returns name")
    void testEnumToString() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.sex_at_birth;
        assertThat(mapper.toString()).isEqualTo("sex_at_birth");
    }

    @Test
    @DisplayName("Should verify enum ordinal")
    void testEnumOrdinal() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.sex_at_birth;
        assertThat(mapper.ordinal()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should verify enum name method")
    void testEnumName() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.sex_at_birth;
        assertThat(mapper.name()).isEqualTo("sex_at_birth");
    }

    @Test
    @DisplayName("Should verify enum getDeclaringClass")
    void testEnumGetDeclaringClass() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.sex_at_birth;
        assertThat(mapper.getDeclaringClass()).isEqualTo(NCCNAnswerMapper.class);
    }

    @Test
    @DisplayName("Should verify enum compareTo")
    void testEnumCompareTo() {
        NCCNAnswerMapper mapper = NCCNAnswerMapper.sex_at_birth;
        assertThat(mapper.compareTo(NCCNAnswerMapper.sex_at_birth)).isEqualTo(0);
    }
}

