package com.genomebrain.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link GuideLinesDetails}.
 */
@DisplayName("GuideLinesDetails Unit Tests")
class GuideLinesDetailsTest {

    private GuideLinesDetails guideLinesDetails;

    @BeforeEach
    void setUp() {
        guideLinesDetails = new GuideLinesDetails();
    }

    @Test
    @DisplayName("Should implement Serializable")
    void testImplementsSerializable() {
        assertThat(guideLinesDetails).isInstanceOf(Serializable.class);
    }

    @Test
    @DisplayName("Should have serialVersionUID")
    void testSerialVersionUID() {
        assertThat(GuideLinesDetails.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals("serialVersionUID"));
    }

    @Test
    @DisplayName("Should get and set id")
    void testGetAndSetId() {
        String id = "guideline-001";
        guideLinesDetails.setId(id);

        assertThat(guideLinesDetails.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should handle null id")
    void testNullId() {
        guideLinesDetails.setId(null);
        assertThat(guideLinesDetails.getId()).isNull();
    }

    @Test
    @DisplayName("Should handle empty string id")
    void testEmptyStringId() {
        guideLinesDetails.setId("");
        assertThat(guideLinesDetails.getId()).isEmpty();
    }

    @Test
    @DisplayName("Should get and set tagsList")
    void testGetAndSetTagsList() {
        List<String> tagsList = Arrays.asList("tag1", "tag2", "tag3");
        guideLinesDetails.setTagsList(tagsList);

        assertThat(guideLinesDetails.getTagsList()).isEqualTo(tagsList);
        assertThat(guideLinesDetails.getTagsList()).hasSize(3);
        assertThat(guideLinesDetails.getTagsList()).contains("tag1", "tag2", "tag3");
    }

    @Test
    @DisplayName("Should handle null tagsList")
    void testNullTagsList() {
        guideLinesDetails.setTagsList(null);
        assertThat(guideLinesDetails.getTagsList()).isNull();
    }

    @Test
    @DisplayName("Should handle empty tagsList")
    void testEmptyTagsList() {
        guideLinesDetails.setTagsList(Collections.emptyList());
        assertThat(guideLinesDetails.getTagsList()).isEmpty();
    }

    @Test
    @DisplayName("Should handle tagsList with single element")
    void testSingleElementTagsList() {
        List<String> tagsList = Arrays.asList("singleTag");
        guideLinesDetails.setTagsList(tagsList);

        assertThat(guideLinesDetails.getTagsList()).hasSize(1);
        assertThat(guideLinesDetails.getTagsList()).contains("singleTag");
    }

    @Test
    @DisplayName("Should handle tagsList with duplicate elements")
    void testDuplicateElementsTagsList() {
        List<String> tagsList = Arrays.asList("tag1", "tag1", "tag2");
        guideLinesDetails.setTagsList(tagsList);

        assertThat(guideLinesDetails.getTagsList()).hasSize(3);
        assertThat(guideLinesDetails.getTagsList()).contains("tag1", "tag2");
    }

    @Test
    @DisplayName("Should get and set tagsId")
    void testGetAndSetTagsId() {
        String tagsId = "tags-001";
        guideLinesDetails.setTagsId(tagsId);

        assertThat(guideLinesDetails.getTagsId()).isEqualTo(tagsId);
    }

    @Test
    @DisplayName("Should handle null tagsId")
    void testNullTagsId() {
        guideLinesDetails.setTagsId(null);
        assertThat(guideLinesDetails.getTagsId()).isNull();
    }

    @Test
    @DisplayName("Should get and set guidelineName")
    void testGetAndSetGuidelineName() {
        String guidelineName = "NCCN Guidelines for Breast Cancer";
        guideLinesDetails.setGuidelineName(guidelineName);

        assertThat(guideLinesDetails.getGuidelineName()).isEqualTo(guidelineName);
    }

    @Test
    @DisplayName("Should handle null guidelineName")
    void testNullGuidelineName() {
        guideLinesDetails.setGuidelineName(null);
        assertThat(guideLinesDetails.getGuidelineName()).isNull();
    }

    @Test
    @DisplayName("Should handle empty string guidelineName")
    void testEmptyStringGuidelineName() {
        guideLinesDetails.setGuidelineName("");
        assertThat(guideLinesDetails.getGuidelineName()).isEmpty();
    }

    @Test
    @DisplayName("Should get and set reasoningStatementEnglish")
    void testGetAndSetReasoningStatementEnglish() {
        String reasoningStatement = "Based on family history and genetic testing results...";
        guideLinesDetails.setReasoningStatementEnglish(reasoningStatement);

        assertThat(guideLinesDetails.getReasoningStatementEnglish()).isEqualTo(reasoningStatement);
    }

    @Test
    @DisplayName("Should handle null reasoningStatementEnglish")
    void testNullReasoningStatementEnglish() {
        guideLinesDetails.setReasoningStatementEnglish(null);
        assertThat(guideLinesDetails.getReasoningStatementEnglish()).isNull();
    }

    @Test
    @DisplayName("Should handle long reasoningStatementEnglish")
    void testLongReasoningStatementEnglish() {
        String longStatement = "A".repeat(1000);
        guideLinesDetails.setReasoningStatementEnglish(longStatement);

        assertThat(guideLinesDetails.getReasoningStatementEnglish()).isEqualTo(longStatement);
        assertThat(guideLinesDetails.getReasoningStatementEnglish()).hasSize(1000);
    }

    @Test
    @DisplayName("Should get and set reasoningStatementId")
    void testGetAndSetReasoningStatementId() {
        String reasoningStatementId = "reasoning-001";
        guideLinesDetails.setReasoningStatementId(reasoningStatementId);

        assertThat(guideLinesDetails.getReasoningStatementId()).isEqualTo(reasoningStatementId);
    }

    @Test
    @DisplayName("Should handle null reasoningStatementId")
    void testNullReasoningStatementId() {
        guideLinesDetails.setReasoningStatementId(null);
        assertThat(guideLinesDetails.getReasoningStatementId()).isNull();
    }

    @Test
    @DisplayName("Should get and set associatedGenes")
    void testGetAndSetAssociatedGenes() {
        List<String> associatedGenes = Arrays.asList("BRCA1", "BRCA2", "TP53");
        guideLinesDetails.setAssociatedGenes(associatedGenes);

        assertThat(guideLinesDetails.getAssociatedGenes()).isEqualTo(associatedGenes);
        assertThat(guideLinesDetails.getAssociatedGenes()).hasSize(3);
        assertThat(guideLinesDetails.getAssociatedGenes()).contains("BRCA1", "BRCA2", "TP53");
    }

    @Test
    @DisplayName("Should handle null associatedGenes")
    void testNullAssociatedGenes() {
        guideLinesDetails.setAssociatedGenes(null);
        assertThat(guideLinesDetails.getAssociatedGenes()).isNull();
    }

    @Test
    @DisplayName("Should handle empty associatedGenes")
    void testEmptyAssociatedGenes() {
        guideLinesDetails.setAssociatedGenes(Collections.emptyList());
        assertThat(guideLinesDetails.getAssociatedGenes()).isEmpty();
    }

    @Test
    @DisplayName("Should handle associatedGenes with single gene")
    void testSingleGeneAssociatedGenes() {
        List<String> associatedGenes = Arrays.asList("BRCA1");
        guideLinesDetails.setAssociatedGenes(associatedGenes);

        assertThat(guideLinesDetails.getAssociatedGenes()).hasSize(1);
        assertThat(guideLinesDetails.getAssociatedGenes()).contains("BRCA1");
    }

    @Test
    @DisplayName("Should set all fields")
    void testSetAllFields() {
        String id = "guideline-001";
        List<String> tagsList = Arrays.asList("tag1", "tag2");
        String tagsId = "tags-001";
        String guidelineName = "Test Guidelines";
        String reasoningStatementEnglish = "Test reasoning statement";
        String reasoningStatementId = "reasoning-001";
        List<String> associatedGenes = Arrays.asList("BRCA1", "BRCA2");

        guideLinesDetails.setId(id);
        guideLinesDetails.setTagsList(tagsList);
        guideLinesDetails.setTagsId(tagsId);
        guideLinesDetails.setGuidelineName(guidelineName);
        guideLinesDetails.setReasoningStatementEnglish(reasoningStatementEnglish);
        guideLinesDetails.setReasoningStatementId(reasoningStatementId);
        guideLinesDetails.setAssociatedGenes(associatedGenes);

        assertThat(guideLinesDetails.getId()).isEqualTo(id);
        assertThat(guideLinesDetails.getTagsList()).isEqualTo(tagsList);
        assertThat(guideLinesDetails.getTagsId()).isEqualTo(tagsId);
        assertThat(guideLinesDetails.getGuidelineName()).isEqualTo(guidelineName);
        assertThat(guideLinesDetails.getReasoningStatementEnglish()).isEqualTo(reasoningStatementEnglish);
        assertThat(guideLinesDetails.getReasoningStatementId()).isEqualTo(reasoningStatementId);
        assertThat(guideLinesDetails.getAssociatedGenes()).isEqualTo(associatedGenes);
    }

    @Test
    @DisplayName("Should update mutable lists")
    void testUpdateMutableLists() {
        List<String> tagsList = new ArrayList<>(Arrays.asList("tag1", "tag2"));
        guideLinesDetails.setTagsList(tagsList);

        tagsList.add("tag3");
        assertThat(guideLinesDetails.getTagsList()).hasSize(3);
        assertThat(guideLinesDetails.getTagsList()).contains("tag3");
    }

    @Test
    @DisplayName("Should handle toString method")
    void testToString() {
        guideLinesDetails.setId("test-id");
        guideLinesDetails.setTagsList(Arrays.asList("tag1", "tag2"));
        guideLinesDetails.setTagsId("tags-id");
        guideLinesDetails.setGuidelineName("Test Guidelines");
        guideLinesDetails.setReasoningStatementEnglish("Test reasoning");
        guideLinesDetails.setReasoningStatementId("reasoning-id");
        guideLinesDetails.setAssociatedGenes(Arrays.asList("BRCA1"));

        String toString = guideLinesDetails.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("GuideLinesDetails");
        assertThat(toString).contains("test-id");
        assertThat(toString).contains("Test Guidelines");
    }

    @Test
    @DisplayName("Should handle toString with null fields")
    void testToStringWithNullFields() {
        String toString = guideLinesDetails.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("GuideLinesDetails");
    }

    @Test
    @DisplayName("Should handle toString with empty lists")
    void testToStringWithEmptyLists() {
        guideLinesDetails.setTagsList(Collections.emptyList());
        guideLinesDetails.setAssociatedGenes(Collections.emptyList());

        String toString = guideLinesDetails.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("GuideLinesDetails");
    }

    @Test
    @DisplayName("Should maintain separate instances")
    void testSeparateInstances() {
        GuideLinesDetails details1 = new GuideLinesDetails();
        GuideLinesDetails details2 = new GuideLinesDetails();

        details1.setId("id1");
        details2.setId("id2");

        assertThat(details1.getId()).isEqualTo("id1");
        assertThat(details2.getId()).isEqualTo("id2");
        assertThat(details1.getId()).isNotEqualTo(details2.getId());
    }

    @Test
    @DisplayName("Should handle tagsList with null elements")
    void testTagsListWithNullElements() {
        List<String> tagsList = new ArrayList<>();
        tagsList.add("tag1");
        tagsList.add(null);
        tagsList.add("tag2");

        guideLinesDetails.setTagsList(tagsList);

        assertThat(guideLinesDetails.getTagsList()).hasSize(3);
        assertThat(guideLinesDetails.getTagsList()).contains("tag1", null, "tag2");
    }

    @Test
    @DisplayName("Should handle associatedGenes with null elements")
    void testAssociatedGenesWithNullElements() {
        List<String> associatedGenes = new ArrayList<>();
        associatedGenes.add("BRCA1");
        associatedGenes.add(null);
        associatedGenes.add("BRCA2");

        guideLinesDetails.setAssociatedGenes(associatedGenes);

        assertThat(guideLinesDetails.getAssociatedGenes()).hasSize(3);
        assertThat(guideLinesDetails.getAssociatedGenes()).contains("BRCA1", null, "BRCA2");
    }

    @Test
    @DisplayName("Should handle very long id")
    void testVeryLongId() {
        String longId = "A".repeat(1000);
        guideLinesDetails.setId(longId);

        assertThat(guideLinesDetails.getId()).isEqualTo(longId);
        assertThat(guideLinesDetails.getId()).hasSize(1000);
    }

    @Test
    @DisplayName("Should handle special characters in strings")
    void testSpecialCharactersInStrings() {
        String specialString = "Test@#$%^&*()_+-=[]{}|;':\",./<>?";
        guideLinesDetails.setId(specialString);
        guideLinesDetails.setGuidelineName(specialString);

        assertThat(guideLinesDetails.getId()).isEqualTo(specialString);
        assertThat(guideLinesDetails.getGuidelineName()).isEqualTo(specialString);
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        String unicodeString = "测试指南详情";
        guideLinesDetails.setGuidelineName(unicodeString);
        guideLinesDetails.setReasoningStatementEnglish(unicodeString);

        assertThat(guideLinesDetails.getGuidelineName()).isEqualTo(unicodeString);
        assertThat(guideLinesDetails.getReasoningStatementEnglish()).isEqualTo(unicodeString);
    }
}

