package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Parent}.
 */
@DisplayName("Parent Unit Tests")
class ParentTest {

    private Parent parent;

    @BeforeEach
    void setUp() {
        parent = new Parent();
    }

    @Test
    @DisplayName("Should get and set parentID")
    void testParentID() {
        parent.setParentID("PAR001");
        assertThat(parent.getParentID()).isEqualTo("PAR001");
    }

    @Test
    @DisplayName("Should get and set type")
    void testType() {
        parent.setType("Mother");
        assertThat(parent.getType()).isEqualTo("Mother");
    }

    @Test
    @DisplayName("Should get and set parentName")
    void testParentName() {
        parent.setParentName("Jane Doe");
        assertThat(parent.getParentName()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Should get and set parentKey")
    void testParentKey() {
        parent.setParentKey("MOTHER_KEY");
        assertThat(parent.getParentKey()).isEqualTo("MOTHER_KEY");
    }

    @Test
    @DisplayName("Should get and set parentLabel")
    void testParentLabel() {
        parent.setParentLabel("Mother");
        assertThat(parent.getParentLabel()).isEqualTo("Mother");
    }

    @Test
    @DisplayName("Should get and set isUnaffected")
    void testIsUnaffected() {
        parent.setUnaffected(true);
        assertThat(parent.isUnaffected()).isTrue();
        
        parent.setUnaffected(false);
        assertThat(parent.isUnaffected()).isFalse();
    }

    @Test
    @DisplayName("Should get and set parentType")
    void testParentType() {
        parent.setParentType("Biological");
        assertThat(parent.getParentType()).isEqualTo("Biological");
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        parent.setParentID(null);
        assertThat(parent.getParentID()).isNull();
        
        parent.setType(null);
        assertThat(parent.getType()).isNull();
        
        parent.setParentName(null);
        assertThat(parent.getParentName()).isNull();
        
        parent.setParentKey(null);
        assertThat(parent.getParentKey()).isNull();
        
        parent.setParentLabel(null);
        assertThat(parent.getParentLabel()).isNull();
        
        parent.setParentType(null);
        assertThat(parent.getParentType()).isNull();
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        parent.setParentID("");
        assertThat(parent.getParentID()).isEmpty();
        
        parent.setType("");
        assertThat(parent.getType()).isEmpty();
        
        parent.setParentName("");
        assertThat(parent.getParentName()).isEmpty();
    }
}

