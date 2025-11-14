package com.objex.warehouse;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link TransType}.
 */
@DisplayName("TransType Unit Tests")
class TransTypeTest {

    @Test
    @DisplayName("Should have RISK enum value")
    void testRiskEnumValue() {
        assertThat(TransType.RISK).isNotNull();
    }

    @Test
    @DisplayName("Should be able to use RISK value")
    void testUseRiskValue() {
        TransType type = TransType.RISK;
        assertThat(type).isEqualTo(TransType.RISK);
    }

    @Test
    @DisplayName("Should return correct string representation")
    void testToString() {
        assertThat(TransType.RISK.toString()).isEqualTo("RISK");
    }

    @Test
    @DisplayName("Should be usable in switch statements")
    void testSwitchStatement() {
        TransType type = TransType.RISK;
        String result = switch (type) {
            case RISK -> "Risk Transaction";
        };
        assertThat(result).isEqualTo("Risk Transaction");
    }

    @Test
    @DisplayName("Should compare enum values correctly")
    void testEnumComparison() {
        TransType type1 = TransType.RISK;
        TransType type2 = TransType.RISK;
        assertThat(type1).isEqualTo(type2);
        assertThat(type1 == type2).isTrue();
    }
}

