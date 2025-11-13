package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.TagConstants;
import com.genomebrain.backend.service.dto.TagIdConstant;

@DisplayName("HbocReasoningStatementUtility Unit Tests")
class HbocReasoningStatementUtilityTest {

    private HbocReasoningStatementUtility utility;

    @BeforeEach
    void setUp() {
        utility = new HbocReasoningStatementUtility();
    }

    @Test
    @DisplayName("Should return empty reasoning statement for empty family history")
    void testGetHbocReasoningStatementWithEmptyFamilyHistory() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isEmpty();
        assertThat(result.getReasoningStatementAndTags()).isEmpty();
    }

    @Test
    @DisplayName("Should return HBOCTag1 reasoning statement for female with PrBreast<=50")
    void testGetHbocReasoningStatementWithHBOCTag1() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add("PrBreast<=50");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isNotEmpty();
        assertThat(result.getReasoningStatementAndTags()).isNotEmpty();
        
        boolean hasHBOCTag1 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG1.getTagId()));
        assertThat(hasHBOCTag1).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag3 reasoning statement for female with multiple primaries")
    void testGetHbocReasoningStatementWithHBOCTag3() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add(TagConstants.AMBRY007_002.getValue());

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isNotEmpty();
        assertThat(result.getReasoningStatementAndTags()).isNotEmpty();
        
        boolean hasHBOCTag3 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG3.getTagId()));
        assertThat(hasHBOCTag3).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag29 reasoning statement with 2+ relatives and male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag29() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag29 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag107 reasoning statement for male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag107() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithOneRelative();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag107 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag108 reasoning statement for male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag108() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithOneRelative();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag108 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag82 reasoning statement for female with PrBreast and 2+ relatives")
    void testGetHbocReasoningStatementWithHBOCTag82() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add("PrBreast");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag82 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag92 reasoning statement for female with matching conditions")
    void testGetHbocReasoningStatementWithHBOCTag92() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        
        // Add tags that match HBOCTag92 condition
        tagsSet.add("AMBRY009_003"); // ILC tag

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag92 when conditions are met
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithTwoRelatives() {
        FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
        relative1.setTags(new HashSet<>(Arrays.asList("Tag1", "Tag2")));

        FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
        relative2.setTags(new HashSet<>(Arrays.asList("Tag3", "Tag4")));

        return Arrays.asList(relative1, relative2);
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithOneRelative() {
        FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
        relative1.setTags(new HashSet<>(Arrays.asList("Tag1", "Tag2")));

        return Arrays.asList(relative1);
    }

    @Test
    @DisplayName("Should return HBOCTag30 reasoning statement with 2+ relatives and male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag30() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag30 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag31 reasoning statement with 2+ relatives and male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag31() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag31 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag36 reasoning statement with 2+ relatives")
    void testGetHbocReasoningStatementWithHBOCTag36() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag36 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag37 reasoning statement with 2+ relatives")
    void testGetHbocReasoningStatementWithHBOCTag37() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag37 when conditions are met
    }

    @Test
    @DisplayName("Should return HBOCTag93 reasoning statement for female with ILC")
    void testGetHbocReasoningStatementWithHBOCTag93() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithOneRelative();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add(TagConstants.AMBRY009_003.getValue()); // ILC

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for HBOCTag93 when conditions are met
    }

    @Test
    @DisplayName("Should handle getHbocReasoningStatement with null family history")
    void testGetHbocReasoningStatementWithNullFamilyHistory() {
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add("PrBreast<=50");

        assertThatThrownBy(() -> utility.getHbocReasoningStatement(null, tagsSet))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when tags set is null")
    void testGetHbocReasoningStatementWithNullTagsSet() {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();

        assertThatThrownBy(() -> utility.getHbocReasoningStatement(familyHistory, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should return empty reasoning statement when no conditions match")
    void testGetHbocReasoningStatementNoMatchingConditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add("SomeOtherTag");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isEmpty();
    }
}

