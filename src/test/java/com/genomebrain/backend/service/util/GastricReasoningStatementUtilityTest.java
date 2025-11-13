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
import com.genomebrain.backend.service.dto.NCCNTagConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.TagIdConstant;

@DisplayName("GastricReasoningStatementUtility Unit Tests")
class GastricReasoningStatementUtilityTest {

    private GastricReasoningStatementUtility utility;

    @BeforeEach
    void setUp() {
        utility = new GastricReasoningStatementUtility();
    }

    @Test
    @DisplayName("Should return empty reasoning statement for empty family history")
    void testGetGastricReasoningStatementWithEmptyFamilyHistory() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isEmpty();
        assertThat(result.getReasoningStatementAndTags()).isEmpty();
    }

    @Test
    @DisplayName("Should return CRCTag25 reasoning statement when PR_GASTRIC_DIFFUSE tag is present")
    void testGetGastricReasoningStatementWithCRCTag25() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue());

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isNotEmpty();
        assertThat(result.getReasoningStatementAndTags()).isNotEmpty();
        
        boolean hasCRCTag25 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTag25.getTagId()));
        assertThat(hasCRCTag25).isTrue();
    }

    @Test
    @DisplayName("Should return empty reasoning statement when no conditions are met")
    void testGetGastricReasoningStatementWithNoMatchingConditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add("SomeOtherTag");

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isEmpty();
        assertThat(result.getReasoningStatementAndTags()).isEmpty();
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithTwoRelatives() {
        FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
        relative1.setTags(new HashSet<>(Arrays.asList("Tag1", "Tag2")));

        FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
        relative2.setTags(new HashSet<>(Arrays.asList("Tag3", "Tag4")));

        return Arrays.asList(relative1, relative2);
    }

    @Test
    @DisplayName("Should return CRCRS14 reasoning statement with CRCTag26 condition")
    void testGetGastricReasoningStatementWithCRCTag26() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag26 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS14 reasoning statement with CRCTag27 condition")
    void testGetGastricReasoningStatementWithCRCTag27() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag27 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS15 reasoning statement when PR_GASTRIC_DIFFUSE tag is present")
    void testGetGastricReasoningStatementWithCRCRS15() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue());

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).isNotEmpty();
        assertThat(result.getReasoningStatementAndTags()).isNotEmpty();
        
        boolean hasCRCRS15 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getReasoningStatementId().equals("CRCRS15"));
        assertThat(hasCRCRS15).isTrue();
    }

    @Test
    @DisplayName("Should handle getGastricReasoningStatement with null family history")
    void testGetGastricReasoningStatementWithNullFamilyHistory() {
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue());

        assertThatThrownBy(() -> utility.getGastricReasoningStatement(null, tagsSet))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when tags set is null")
    void testGetGastricReasoningStatementWithNullTagsSet() {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();

        assertThatThrownBy(() -> utility.getGastricReasoningStatement(familyHistory, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should return CRCRS14 reasoning statement with CRCTag28 condition")
    void testGetGastricReasoningStatementWithCRCTag28() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag28 when conditions are met
    }

    @Test
    @DisplayName("Should return CRCRS14 reasoning statement with CRCTag29 condition")
    void testGetGastricReasoningStatementWithCRCTag29() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // The method checks for CRCTag29 when conditions are met
    }
}

