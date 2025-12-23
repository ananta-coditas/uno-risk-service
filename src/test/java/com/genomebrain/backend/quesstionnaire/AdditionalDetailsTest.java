package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AdditionalDetails}.
 */
@DisplayName("AdditionalDetails Unit Tests")
class AdditionalDetailsTest {

    private AdditionalDetails additionalDetails;

    @BeforeEach
    void setUp() {
        additionalDetails = new AdditionalDetails();
    }

    @Test
    @DisplayName("Should get and set probandAdditionalNotes")
    void testProbandAdditionalNotes() {
        additionalDetails.setProbandAdditionalNotes("Some additional notes");
        assertThat(additionalDetails.getProbandAdditionalNotes()).isEqualTo("Some additional notes");
    }

    @Test
    @DisplayName("Should handle null probandAdditionalNotes")
    void testNullProbandAdditionalNotes() {
        additionalDetails.setProbandAdditionalNotes(null);
        assertThat(additionalDetails.getProbandAdditionalNotes()).isNull();
    }

    @Test
    @DisplayName("Should handle empty string")
    void testEmptyString() {
        additionalDetails.setProbandAdditionalNotes("");
        assertThat(additionalDetails.getProbandAdditionalNotes()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        additionalDetails.setProbandAdditionalNotes("Test notes");
        
        String result = additionalDetails.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("AdditionalDetails");
        assertThat(result).contains("probandAdditionalNotes");
        assertThat(result).contains("Test notes");
    }

    @Test
    @DisplayName("Should return correct toString with null value")
    void testToStringWithNullValue() {
        additionalDetails.setProbandAdditionalNotes(null);
        
        String result = additionalDetails.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("AdditionalDetails");
        assertThat(result).contains("probandAdditionalNotes");
    }
}

