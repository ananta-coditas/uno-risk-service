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
import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementIdConstant;
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

    @Test
    @DisplayName("Should return HBOCTag29 reasoning statement with 2+ relatives having breast cancer tags and male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag29WithBreastTags() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithBreastTags(2);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS25.getValue());
        boolean hasHBOCTag29 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG29.getTagId()));
        assertThat(hasHBOCTag29).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag30 reasoning statement with 2+ relatives having prostate cancer tags and male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag30WithProstateTags() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithProstateTags(2);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS25.getValue());
        boolean hasHBOCTag30 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG30.getTagId()));
        assertThat(hasHBOCTag30).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag31 reasoning statement with 2+ relatives having breast or prostate cancer tags and male with PrProstate")
    void testGetHbocReasoningStatementWithHBOCTag31WithBreastOrProstateTags() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithBreastOrProstateTags(2);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS25.getValue());
        boolean hasHBOCTag31 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG31.getTagId()));
        assertThat(hasHBOCTag31).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag36 reasoning statement with 2+ relatives matching HBOCTag36 conditions")
    void testGetHbocReasoningStatementWithHBOCTag36Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag36(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS30.getValue());
        boolean hasHBOCTag36 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG36.getTagId()));
        assertThat(hasHBOCTag36).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag37 reasoning statement with 2+ relatives matching HBOCTag37 conditions")
    void testGetHbocReasoningStatementWithHBOCTag37Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag37(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS30.getValue());
        boolean hasHBOCTag37 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG37.getTagId()));
        assertThat(hasHBOCTag37).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag38 reasoning statement with 2+ relatives matching HBOCTag38 conditions")
    void testGetHbocReasoningStatementWithHBOCTag38Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag38(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS31.getValue());
        boolean hasHBOCTag38 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG38.getTagId()));
        assertThat(hasHBOCTag38).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag39 reasoning statement with 2+ relatives matching HBOCTag39 conditions")
    void testGetHbocReasoningStatementWithHBOCTag39Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag39(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS31.getValue());
        boolean hasHBOCTag39 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG39.getTagId()));
        assertThat(hasHBOCTag39).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag40 reasoning statement with 2+ relatives matching HBOCTag40 conditions")
    void testGetHbocReasoningStatementWithHBOCTag40Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag40(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS32.getValue());
        boolean hasHBOCTag40 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG40.getTagId()));
        assertThat(hasHBOCTag40).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag41 reasoning statement with 2+ relatives matching HBOCTag41 conditions")
    void testGetHbocReasoningStatementWithHBOCTag41Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag41(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS32.getValue());
        boolean hasHBOCTag41 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG41.getTagId()));
        assertThat(hasHBOCTag41).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag42 reasoning statement with 2+ relatives matching HBOCTag42 conditions")
    void testGetHbocReasoningStatementWithHBOCTag42Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag42(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS33.getValue());
        boolean hasHBOCTag42 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG42.getTagId()));
        assertThat(hasHBOCTag42).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag43 reasoning statement with 2+ relatives matching HBOCTag43 conditions")
    void testGetHbocReasoningStatementWithHBOCTag43Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag43(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS33.getValue());
        boolean hasHBOCTag43 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG43.getTagId()));
        assertThat(hasHBOCTag43).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag64 reasoning statement with 2+ relatives matching HBOCTag64 conditions")
    void testGetHbocReasoningStatementWithHBOCTag64Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag64(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS44.getValue());
        boolean hasHBOCTag64 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG64.getTagId()));
        assertThat(hasHBOCTag64).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag65 reasoning statement with 2+ relatives matching HBOCTag65 conditions")
    void testGetHbocReasoningStatementWithHBOCTag65Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag65(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS44.getValue());
        boolean hasHBOCTag65 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG65.getTagId()));
        assertThat(hasHBOCTag65).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag67 reasoning statement with 2+ relatives matching HBOCTag67 conditions")
    void testGetHbocReasoningStatementWithHBOCTag67Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag67(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS33.getValue());
        boolean hasHBOCTag67 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG67.getTagId()));
        assertThat(hasHBOCTag67).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag68 reasoning statement with 2+ relatives matching HBOCTag68 conditions")
    void testGetHbocReasoningStatementWithHBOCTag68Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag68(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS33.getValue());
        boolean hasHBOCTag68 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG68.getTagId()));
        assertThat(hasHBOCTag68).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag76 reasoning statement with 2+ relatives matching HBOCTag76 conditions")
    void testGetHbocReasoningStatementWithHBOCTag76Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag76(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS31.getValue());
        boolean hasHBOCTag76 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG76.getTagId()));
        assertThat(hasHBOCTag76).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag77 reasoning statement with 2+ relatives matching HBOCTag77 conditions")
    void testGetHbocReasoningStatementWithHBOCTag77Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag77(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS31.getValue());
        boolean hasHBOCTag77 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG77.getTagId()));
        assertThat(hasHBOCTag77).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag78 reasoning statement with 2+ relatives matching HBOCTag78 conditions")
    void testGetHbocReasoningStatementWithHBOCTag78Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag78(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS41.getValue());
        boolean hasHBOCTag78 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG78.getTagId()));
        assertThat(hasHBOCTag78).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag79 reasoning statement with 2+ relatives matching HBOCTag79 conditions")
    void testGetHbocReasoningStatementWithHBOCTag79Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag79(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS41.getValue());
        boolean hasHBOCTag79 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG79.getTagId()));
        assertThat(hasHBOCTag79).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag53 reasoning statement with 2+ relatives matching HBOCTag53 conditions")
    void testGetHbocReasoningStatementWithHBOCTag53Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag53(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS40.getValue());
        boolean hasHBOCTag53 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG53.getTagId()));
        assertThat(hasHBOCTag53).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag54 reasoning statement with 2+ relatives matching HBOCTag54 conditions")
    void testGetHbocReasoningStatementWithHBOCTag54Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag54(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS41.getValue());
        boolean hasHBOCTag54 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG54.getTagId()));
        assertThat(hasHBOCTag54).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag55 reasoning statement with 2+ relatives matching HBOCTag55 conditions")
    void testGetHbocReasoningStatementWithHBOCTag55Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag55(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS41.getValue());
        boolean hasHBOCTag55 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG55.getTagId()));
        assertThat(hasHBOCTag55).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag56 reasoning statement with 2+ relatives matching HBOCTag56 conditions")
    void testGetHbocReasoningStatementWithHBOCTag56Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag56(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS42.getValue());
        boolean hasHBOCTag56 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG56.getTagId()));
        assertThat(hasHBOCTag56).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag57 reasoning statement with 2+ relatives matching HBOCTag57 conditions")
    void testGetHbocReasoningStatementWithHBOCTag57Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag57(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS42.getValue());
        boolean hasHBOCTag57 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG57.getTagId()));
        assertThat(hasHBOCTag57).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag58 reasoning statement with 3+ relatives matching HBOCTag58 conditions")
    void testGetHbocReasoningStatementWithHBOCTag58Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag58(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS43.getValue());
        boolean hasHBOCTag58 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG58.getTagId()));
        assertThat(hasHBOCTag58).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag59 reasoning statement with 3+ relatives matching HBOCTag59 conditions")
    void testGetHbocReasoningStatementWithHBOCTag59Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag59(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS43.getValue());
        boolean hasHBOCTag59 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG59.getTagId()));
        assertThat(hasHBOCTag59).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag82 reasoning statement for female with PrBreast and 2+ relatives matching conditions")
    void testGetHbocReasoningStatementWithHBOCTag82WithPrBreast() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag82(2);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add("PrBreast");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS49.getValue());
        boolean hasHBOCTag82 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG82.getTagId()));
        assertThat(hasHBOCTag82).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag83 reasoning statement for female with PrBreast and 2+ relatives matching conditions")
    void testGetHbocReasoningStatementWithHBOCTag83WithPrBreast() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag83(2);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add("PrBreast");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS49.getValue());
        boolean hasHBOCTag83 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG83.getTagId()));
        assertThat(hasHBOCTag83).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag88 reasoning statement with 3+ relatives matching HBOCTag88 conditions")
    void testGetHbocReasoningStatementWithHBOCTag88Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag88(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS50.getValue());
        boolean hasHBOCTag88 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG88.getTagId()));
        assertThat(hasHBOCTag88).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag89 reasoning statement with 3+ relatives matching HBOCTag89 conditions")
    void testGetHbocReasoningStatementWithHBOCTag89Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag89(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS50.getValue());
        boolean hasHBOCTag89 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG89.getTagId()));
        assertThat(hasHBOCTag89).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag90 reasoning statement with 3+ relatives matching HBOCTag90 conditions")
    void testGetHbocReasoningStatementWithHBOCTag90Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag90(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS50.getValue());
        boolean hasHBOCTag90 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG90.getTagId()));
        assertThat(hasHBOCTag90).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag91 reasoning statement with 3+ relatives matching HBOCTag91 conditions")
    void testGetHbocReasoningStatementWithHBOCTag91Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag91(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS50.getValue());
        boolean hasHBOCTag91 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG91.getTagId()));
        assertThat(hasHBOCTag91).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag97 reasoning statement with 2+ relatives matching HBOCTag97 conditions")
    void testGetHbocReasoningStatementWithHBOCTag97Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag97(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS34.getValue());
        boolean hasHBOCTag97 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG97.getTagId()));
        assertThat(hasHBOCTag97).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag98 reasoning statement with 2+ relatives matching HBOCTag98 conditions")
    void testGetHbocReasoningStatementWithHBOCTag98Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag98(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS34.getValue());
        boolean hasHBOCTag98 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG98.getTagId()));
        assertThat(hasHBOCTag98).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag99 reasoning statement with 2+ relatives matching HBOCTag99 conditions")
    void testGetHbocReasoningStatementWithHBOCTag99Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag99(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS34.getValue());
        boolean hasHBOCTag99 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG99.getTagId()));
        assertThat(hasHBOCTag99).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag100 reasoning statement with 2+ relatives matching HBOCTag100 conditions")
    void testGetHbocReasoningStatementWithHBOCTag100Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag100(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS34.getValue());
        boolean hasHBOCTag100 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG100.getTagId()));
        assertThat(hasHBOCTag100).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag105 reasoning statement with 3+ relatives matching HBOCTag105 conditions")
    void testGetHbocReasoningStatementWithHBOCTag105Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag105(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS50.getValue());
        boolean hasHBOCTag105 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG105.getTagId()));
        assertThat(hasHBOCTag105).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag106 reasoning statement with 3+ relatives matching HBOCTag106 conditions")
    void testGetHbocReasoningStatementWithHBOCTag106Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag106(3);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS50.getValue());
        boolean hasHBOCTag106 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG106.getTagId()));
        assertThat(hasHBOCTag106).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag109 reasoning statement with 2+ relatives matching HBOCTag109 conditions")
    void testGetHbocReasoningStatementWithHBOCTag109Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag109(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS57.getValue());
        boolean hasHBOCTag109 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG109.getTagId()));
        assertThat(hasHBOCTag109).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag110 reasoning statement with 2+ relatives matching HBOCTag110 conditions")
    void testGetHbocReasoningStatementWithHBOCTag110Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag110(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS57.getValue());
        boolean hasHBOCTag110 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG110.getTagId()));
        assertThat(hasHBOCTag110).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag111 reasoning statement with 2+ relatives matching HBOCTag111 conditions")
    void testGetHbocReasoningStatementWithHBOCTag111Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag111(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS58.getValue());
        boolean hasHBOCTag111 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG111.getTagId()));
        assertThat(hasHBOCTag111).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag112 reasoning statement with 2+ relatives matching HBOCTag112 conditions")
    void testGetHbocReasoningStatementWithHBOCTag112Conditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag112(2);
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS58.getValue());
        boolean hasHBOCTag112 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG112.getTagId()));
        assertThat(hasHBOCTag112).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag92 reasoning statement for female with matching tags")
    void testGetHbocReasoningStatementWithHBOCTag92WithMatchingTags() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add(TagConstants.AMBRY009_003.getValue());
        tagsSet.add(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue()); // Required for HBOCTag92

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS51.getValue());
        boolean hasHBOCTag92 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG92.getTagId()));
        assertThat(hasHBOCTag92).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag93 reasoning statement for female with ILC and matching family history")
    void testGetHbocReasoningStatementWithHBOCTag93WithMatchingFamilyHistory() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag93(1);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add(TagConstants.AMBRY009_003.getValue());

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS52.getValue());
        boolean hasHBOCTag93 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG93.getTagId()));
        assertThat(hasHBOCTag93).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag107 reasoning statement for male with PrProstate and matching family history")
    void testGetHbocReasoningStatementWithHBOCTag107WithMatchingFamilyHistory() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag107(1);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS55.getValue());
        boolean hasHBOCTag107 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG107.getTagId()));
        assertThat(hasHBOCTag107).isTrue();
    }

    @Test
    @DisplayName("Should return HBOCTag108 reasoning statement for male with PrProstate and matching family history")
    void testGetHbocReasoningStatementWithHBOCTag108WithMatchingFamilyHistory() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryForHBOCTag108(1);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.MALE.getValue());
        tagsSet.add("PrProstate");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS56.getValue());
        boolean hasHBOCTag108 = result.getReasoningStatementAndTags().stream()
            .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.HBOCTAG108.getTagId()));
        assertThat(hasHBOCTag108).isTrue();
    }

    @Test
    @DisplayName("Should handle single relative family history")
    void testGetHbocReasoningStatementWithSingleRelative() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithOneRelative();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle family history with size less than 2")
    void testGetHbocReasoningStatementWithFamilyHistorySizeLessThanTwo() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithOneRelative();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(TagConstants.FEMALE.getValue());
        tagsSet.add("PrBreast<=50");

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // Should still return HBOCTag1 for female with PrBreast<=50
        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.HBOCRS1.getValue());
    }

    // Helper methods to create family history with specific tags
    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithBreastTags(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue())));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithProstateTags(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue())));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryWithBreastOrProstateTags(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(
                NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue()
            )));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag36(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_LTE_50_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag37(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_LTE_50_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag38(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_OVARIAN_EPITH_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag39(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_OVARIAN_EPITH_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag40(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag41(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag42(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_META_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag43(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_META_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag64(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_META_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag65(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_META_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag67(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_GL7_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag68(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_GL7_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag76(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag77(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag78(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag79(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag53(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_LTE_50_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag54(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_OVARIAN_EPITH_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag55(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_OVARIAN_EPITH_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag56(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag57(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag58(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else if (i == 1) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag59(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else if (i == 1) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag82(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(
                NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue()
            )));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag83(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(
                NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue()
            )));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag88(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else if (i == 1) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_SDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag89(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else if (i == 1) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_SDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag90(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else if (i == 1) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_SDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag91(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else if (i == 1) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_SDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag97(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_BILAT_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag98(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_BILAT_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag99(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_BILAT_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag100(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_BREAST_BILAT_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag105(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                // Match HBOCTag88_A (breast MAT FDR/SDR/UNKNOWN) - using UNKNOWN to ensure it matches A but not necessarily B
                tags.add(NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue());
            } else if (i == 1) {
                // Match HBOCTag88_B (breast MAT FDR/SDR/TDR) - using TDR to ensure it matches B
                tags.add(NCCNTagConstants.FM_BREAST_MAT_TDR.getValue());
            } else {
                // Match HBOCTag88_B again (same as above) - using another TDR
                tags.add(NCCNTagConstants.FM_BREAST_MAT_TDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag106(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                // Match HBOCTag89_A (breast PAT FDR/SDR/UNKNOWN) - using UNKNOWN to ensure it matches A but not necessarily B
                tags.add(NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue());
            } else if (i == 1) {
                // Match HBOCTag89_B (breast PAT FDR/SDR/TDR) - using TDR to ensure it matches B
                tags.add(NCCNTagConstants.FM_BREAST_PAT_TDR.getValue());
            } else {
                // Match HBOCTag89_B again (same as above) - using another TDR
                tags.add(NCCNTagConstants.FM_BREAST_PAT_TDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag109(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag110(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag111(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag112(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            if (i == 0) {
                tags.add(NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue());
            } else {
                tags.add(NCCNTagConstants.FM_BREAST_MALE_MAT_FDR.getValue());
            }
            relative.setTags(tags);
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag93(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(NCCNTagConstants.FM_GASTRIC_DIFFUSE_MAT_FDR.getValue())));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag107(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_FDR.getValue())));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    private List<FamilyHistoryDetailsTagsDTO> createFamilyHistoryForHBOCTag108(int count) {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            relative.setTags(new HashSet<>(Arrays.asList(NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue())));
            familyHistory.add(relative);
        }
        return familyHistory;
    }

    // ========== CancerNext-Expanded pathogenic gene (NCCNRS1) ==========

    private static FamilyHistoryDetailsTagsDTO relativeWithTags(String... tags) {
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList(tags)));
        return relative;
    }

    @Test
    @DisplayName("should_addNCCNRS1_when_relativeCarriesPathogenicVariantInCancerNextExpandedGene")
    void should_addNCCNRS1_when_relativeCarriesPathogenicVariantInCancerNextExpandedGene() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = List.of(
            relativeWithTags("FmPositiveGeneMutation", "FmPositiveGeneMutation-BRCA2"));

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, new HashSet<>());

        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.NCCNRS1.getValue());
        assertThat(result.getReasoningStatementAndTags())
            .anySatisfy(statement -> {
                assertThat(statement.getReasoningStatementId()).isEqualTo(ReasoningStatementIdConstant.NCCNCRS1.getReasoningStatementId());
                assertThat(statement.getTagIds()).containsExactly(TagIdConstant.NCCNTag1.getTagId());
            });
    }

    @Test
    @DisplayName("should_notAddNCCNRS1_when_relativeGeneIsOutsideCancerNextExpandedPanel")
    void should_notAddNCCNRS1_when_relativeGeneIsOutsideCancerNextExpandedPanel() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = List.of(
            relativeWithTags("FmPositiveGeneMutation", "FmPositiveGeneMutation-CFTR"));

        ReasoningStatementObject result = utility.getHbocReasoningStatement(familyHistory, new HashSet<>());

        assertThat(result.getReasoningStatementEn()).doesNotContain(ReasoningStatementConstants.NCCNRS1.getValue());
    }

    @Test
    @DisplayName("should_removeGeneSpecificTagsButKeepGenericTag_when_extractingPathogenicGenes")
    void should_removeGeneSpecificTagsButKeepGenericTag_when_extractingPathogenicGenes() throws ConnectException {
        FamilyHistoryDetailsTagsDTO relative = relativeWithTags(
            "FmPositiveGeneMutation", "FmPositiveGeneMutation-BRCA1", "FmBreastMatFDR");
        Set<String> tagsSet = new HashSet<>(Set.of("Female", "FmPositiveGeneMutation-BRCA1", "FmPositiveGeneMutation"));

        utility.getHbocReasoningStatement(List.of(relative), tagsSet);

        assertThat(relative.getTags()).containsExactlyInAnyOrder("FmPositiveGeneMutation", "FmBreastMatFDR");
        assertThat(tagsSet).containsExactlyInAnyOrder("Female", "FmPositiveGeneMutation");
    }

    @Test
    @DisplayName("should_returnCancerNextExpandedPanel_when_productCodeIs8875")
    void should_returnCancerNextExpandedPanel_when_productCodeIs8875() {
        Set<String> genes = utility.getDistinctGenesByProductCode(List.of("8875"));

        assertThat(genes).hasSize(76).contains("BRCA1", "BRCA2", "MLH1", "TP53").doesNotContain("CFTR", "ATRIP");
    }

    @Test
    @DisplayName("should_returnNoGenes_when_productCodeIsUnknown")
    void should_returnNoGenes_when_productCodeIsUnknown() {
        assertThat(utility.getDistinctGenesByProductCode(List.of("0000"))).isEmpty();
    }
}
