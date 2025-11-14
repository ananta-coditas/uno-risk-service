package com.genomebrain.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AbstractAuditingEntity}.
 * Since AbstractAuditingEntity is abstract, we create a concrete test implementation.
 */
@DisplayName("AbstractAuditingEntity Unit Tests")
class AbstractAuditingEntityTest {

    private TestAuditingEntity entity;

    /**
     * Concrete test implementation of AbstractAuditingEntity for testing purposes.
     */
    private static class TestAuditingEntity extends AbstractAuditingEntity {
        private static final long serialVersionUID = 1L;
    }

    @BeforeEach
    void setUp() {
        entity = new TestAuditingEntity();
    }

    @Test
    @DisplayName("Should implement Serializable")
    void testImplementsSerializable() {
        assertThat(entity).isInstanceOf(Serializable.class);
    }

    @Test
    @DisplayName("Should have serialVersionUID")
    void testSerialVersionUID() {
        assertThat(AbstractAuditingEntity.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals("serialVersionUID"));
    }

    @Test
    @DisplayName("Should get and set createdBy")
    void testGetAndSetCreatedBy() {
        String createdBy = "testUser";
        entity.setCreatedBy(createdBy);

        assertThat(entity.getCreatedBy()).isEqualTo(createdBy);
    }

    @Test
    @DisplayName("Should handle null createdBy")
    void testNullCreatedBy() {
        entity.setCreatedBy(null);
        assertThat(entity.getCreatedBy()).isNull();
    }

    @Test
    @DisplayName("Should get and set createdDate")
    void testGetAndSetCreatedDate() {
        Instant createdDate = Instant.now();
        entity.setCreatedDate(createdDate);

        assertThat(entity.getCreatedDate()).isEqualTo(createdDate);
    }

    @Test
    @DisplayName("Should handle null createdDate")
    void testNullCreatedDate() {
        entity.setCreatedDate(null);
        assertThat(entity.getCreatedDate()).isNull();
    }

    @Test
    @DisplayName("Should initialize with default createdDate")
    void testDefaultCreatedDate() {
        TestAuditingEntity newEntity = new TestAuditingEntity();
        assertThat(newEntity.getCreatedDate()).isNotNull();
        assertThat(newEntity.getCreatedDate()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should get and set lastModifiedBy")
    void testGetAndSetLastModifiedBy() {
        String lastModifiedBy = "modifiedUser";
        entity.setLastModifiedBy(lastModifiedBy);

        assertThat(entity.getLastModifiedBy()).isEqualTo(lastModifiedBy);
    }

    @Test
    @DisplayName("Should handle null lastModifiedBy")
    void testNullLastModifiedBy() {
        entity.setLastModifiedBy(null);
        assertThat(entity.getLastModifiedBy()).isNull();
    }

    @Test
    @DisplayName("Should get and set lastModifiedDate")
    void testGetAndSetLastModifiedDate() {
        Instant lastModifiedDate = Instant.now();
        entity.setLastModifiedDate(lastModifiedDate);

        assertThat(entity.getLastModifiedDate()).isEqualTo(lastModifiedDate);
    }

    @Test
    @DisplayName("Should handle null lastModifiedDate")
    void testNullLastModifiedDate() {
        entity.setLastModifiedDate(null);
        assertThat(entity.getLastModifiedDate()).isNull();
    }

    @Test
    @DisplayName("Should initialize with default lastModifiedDate")
    void testDefaultLastModifiedDate() {
        TestAuditingEntity newEntity = new TestAuditingEntity();
        assertThat(newEntity.getLastModifiedDate()).isNotNull();
        assertThat(newEntity.getLastModifiedDate()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should set all auditing fields")
    void testSetAllAuditingFields() {
        String createdBy = "creator";
        Instant createdDate = Instant.parse("2023-01-01T00:00:00Z");
        String lastModifiedBy = "modifier";
        Instant lastModifiedDate = Instant.parse("2023-01-02T00:00:00Z");

        entity.setCreatedBy(createdBy);
        entity.setCreatedDate(createdDate);
        entity.setLastModifiedBy(lastModifiedBy);
        entity.setLastModifiedDate(lastModifiedDate);

        assertThat(entity.getCreatedBy()).isEqualTo(createdBy);
        assertThat(entity.getCreatedDate()).isEqualTo(createdDate);
        assertThat(entity.getLastModifiedBy()).isEqualTo(lastModifiedBy);
        assertThat(entity.getLastModifiedDate()).isEqualTo(lastModifiedDate);
    }

    @Test
    @DisplayName("Should handle empty string for createdBy")
    void testEmptyStringCreatedBy() {
        entity.setCreatedBy("");
        assertThat(entity.getCreatedBy()).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty string for lastModifiedBy")
    void testEmptyStringLastModifiedBy() {
        entity.setLastModifiedBy("");
        assertThat(entity.getLastModifiedBy()).isEmpty();
    }

    @Test
    @DisplayName("Should update lastModifiedDate independently of createdDate")
    void testIndependentDateUpdates() {
        Instant createdDate = Instant.parse("2023-01-01T00:00:00Z");
        Instant lastModifiedDate = Instant.parse("2023-12-31T23:59:59Z");

        entity.setCreatedDate(createdDate);
        entity.setLastModifiedDate(lastModifiedDate);

        assertThat(entity.getCreatedDate()).isEqualTo(createdDate);
        assertThat(entity.getLastModifiedDate()).isEqualTo(lastModifiedDate);
        assertThat(entity.getLastModifiedDate()).isAfter(entity.getCreatedDate());
    }

    @Test
    @DisplayName("Should handle future dates")
    void testFutureDates() {
        Instant futureDate = Instant.now().plusSeconds(3600);
        entity.setCreatedDate(futureDate);
        entity.setLastModifiedDate(futureDate);

        assertThat(entity.getCreatedDate()).isEqualTo(futureDate);
        assertThat(entity.getLastModifiedDate()).isEqualTo(futureDate);
    }

    @Test
    @DisplayName("Should handle past dates")
    void testPastDates() {
        Instant pastDate = Instant.parse("2000-01-01T00:00:00Z");
        entity.setCreatedDate(pastDate);
        entity.setLastModifiedDate(pastDate);

        assertThat(entity.getCreatedDate()).isEqualTo(pastDate);
        assertThat(entity.getLastModifiedDate()).isEqualTo(pastDate);
    }

    @Test
    @DisplayName("Should maintain separate instances")
    void testSeparateInstances() {
        TestAuditingEntity entity1 = new TestAuditingEntity();
        TestAuditingEntity entity2 = new TestAuditingEntity();

        entity1.setCreatedBy("user1");
        entity2.setCreatedBy("user2");

        assertThat(entity1.getCreatedBy()).isEqualTo("user1");
        assertThat(entity2.getCreatedBy()).isEqualTo("user2");
        assertThat(entity1.getCreatedBy()).isNotEqualTo(entity2.getCreatedBy());
    }
}

