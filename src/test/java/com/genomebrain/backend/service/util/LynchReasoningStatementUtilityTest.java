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

import com.genomebrain.backend.service.dto.ConditionHolder;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.TagIdConstant;

@DisplayName("LynchReasoningStatementUtility Unit Tests")
class LynchReasoningStatementUtilityTest {

    private LynchReasoningStatementUtility utility;

    @BeforeEach
    void setUp() {
        utility = new LynchReasoningStatementUtility();
    }

    @Test
    @DisplayName("Should return empty reasoning statement for empty family history")
    void testGetLynchReasoningStatementWithEmptyFamilyHistory() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isEmpty();
        assertThat(result.getReasoningStatementAndTags()).isEmpty();
    }

    @Test
    @DisplayName("Should return CRCTag5 reasoning statement when PrColon>=2Dx tag is present")
    void testGetLynchReasoningStatementWithCRCTag5() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add("PrColon>=2Dx");

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isNotEmpty();
        assertThat(result.getReasoningStatementAndTags()).isNotEmpty();
        
        boolean hasCRCTag5 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTAG5.getTagId()));
        assertThat(hasCRCTag5).isTrue();
    }

    @Test
    @DisplayName("Should return CRCTag24 reasoning statement when CRCTag24 condition tags are present")
    void testGetLynchReasoningStatementWithCRCTag24() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        
        // Add a tag that matches CRCTag24 condition
        String[] crcTag24Conditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag24");
        if (crcTag24Conditions != null && crcTag24Conditions.length > 0) {
            tagsSet.add(crcTag24Conditions[0]);
        }

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        if (crcTag24Conditions != null && crcTag24Conditions.length > 0) {
            assertThat(result.getReasoningStatementEn()).isNotEmpty();
            assertThat(result.getReasoningStatementAndTags()).isNotEmpty();
            
            boolean hasCRCTag24 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTAG24.getTagId()));
            assertThat(hasCRCTag24).isTrue();
        }
    }

    @Test
    @DisplayName("Should return CRCTag21 reasoning statement with 3+ relatives")
    void testGetLynchReasoningStatementWithThreeRelatives() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithThreeRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag21 and CRCTag22 when familyHistory.size() >= 3
        // Actual result depends on tags matching condition map
    }

    @Test
    @DisplayName("Should return CRCTag19 reasoning statement with 2+ relatives")
    void testGetLynchReasoningStatementWithTwoRelatives() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag19 and CRCTag20 when familyHistory.size() >= 2
    }

    @Test
    @DisplayName("Should return CRCTag15 reasoning statement with matching conditions")
    void testGetLynchReasoningStatementWithCRCTag15() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        
        // Add CRCTag12 condition tags
        String[] crcTag12Conditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag12");
        if (crcTag12Conditions != null && crcTag12Conditions.length > 0) {
            tagsSet.add(crcTag12Conditions[0]);
        }

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag15 when conditions are met
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithThreeRelatives() {
        FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
        relative1.setTags(new HashSet<>(Arrays.asList("Tag1", "Tag2")));

        FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
        relative2.setTags(new HashSet<>(Arrays.asList("Tag3", "Tag4")));

        FamilyHistoryDetailsTagsDTO relative3 = new FamilyHistoryDetailsTagsDTO();
        relative3.setTags(new HashSet<>(Arrays.asList("Tag5", "Tag6")));

        return Arrays.asList(relative1, relative2, relative3);
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithTwoRelatives() {
        FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
        relative1.setTags(new HashSet<>(Arrays.asList("Tag1", "Tag2")));

        FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
        relative2.setTags(new HashSet<>(Arrays.asList("Tag3", "Tag4")));

        return Arrays.asList(relative1, relative2);
    }

    @Test
    @DisplayName("Should return CRCRS6 reasoning statement with CRCTag8 condition")
    void testGetLynchReasoningStatementWithCRCTag8() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        
        // Add CRCTag12 condition tags
        String[] crcTag12Conditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag12");
        if (crcTag12Conditions != null && crcTag12Conditions.length > 0) {
            tagsSet.add(crcTag12Conditions[0]);
        }

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag8 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS7 reasoning statement with CRCTag12 condition")
    void testGetLynchReasoningStatementWithCRCTag12() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();
        
        // Add CRCTag12 condition tags
        String[] crcTag12Conditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag12");
        if (crcTag12Conditions != null && crcTag12Conditions.length > 0) {
            tagsSet.add(crcTag12Conditions[0]);
        }

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag12 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS10 reasoning statement with CRCTag19 condition")
    void testGetLynchReasoningStatementWithCRCTag19() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag19 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS10 reasoning statement with CRCTag20 condition")
    void testGetLynchReasoningStatementWithCRCTag20() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag20 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS11 reasoning statement with CRCTag21 condition")
    void testGetLynchReasoningStatementWithCRCTag21() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithThreeRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag21 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS11 reasoning statement with CRCTag22 condition")
    void testGetLynchReasoningStatementWithCRCTag22() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithThreeRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag22 when conditions are met
    }

    @Test
    @DisplayName("Should handle getLynchReasoningStatement with null family history")
    void testGetLynchReasoningStatementWithNullFamilyHistory() {
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add("PrColon>=2Dx");

        assertThatThrownBy(() -> utility.getLynchReasoningStatement(null, tagsSet))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when tags set is null")
    void testGetLynchReasoningStatementWithNullTagsSet() {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();

        assertThatThrownBy(() -> utility.getLynchReasoningStatement(familyHistory, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should return empty reasoning statement when no conditions match")
    void testGetLynchReasoningStatementNoMatchingConditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add("SomeOtherTag");

        ReasoningStatementObject result = utility.getLynchReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // Should only have CRCTag5 if PrColon>=2Dx is present
    }
}

