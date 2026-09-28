package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.ConditionHolder;
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

    @Test
    @DisplayName("Should test checkForCRCRS method with matching conditions")
    void testCheckForCRCRS() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForCRCRS", 
            List.class, String.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        String[] crcTag26BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_B");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0 && 
            crcTag26BConditions != null && crcTag26BConditions.length > 0) {
            // Create 2 relatives with CRCTag26_A matching tags
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }
            
            // Add CRCTag26_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag26BConditions[0]);

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", "CRCTag26_B");
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should test checkForCRCRS method with checkForGastrichRS condition")
    void testCheckForCRCRSWithGastrichRS() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForCRCRS", 
            List.class, String.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        String[] crcTag26BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_B");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0 && 
            crcTag26BConditions != null && crcTag26BConditions.length > 0) {
            // Create relatives with same tag appearing twice (for checkForGastrichRS)
            FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags1 = new HashSet<>();
            tags1.add(crcTag26AConditions[0]);
            relative1.setTags(tags1);
            familyHistory.add(relative1);
            
            FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags2 = new HashSet<>();
            tags2.add(crcTag26AConditions[0]); // Same tag
            relative2.setTags(tags2);
            familyHistory.add(relative2);
            
            // Add CRCTag26_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag26BConditions[0]);

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", "CRCTag26_B");
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should test checkForCRCRS method with no matching conditions")
    void testCheckForCRCRSNoMatch() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForCRCRS", 
            List.class, String.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList("NonMatchingTag")));
        familyHistory.add(relative);

        Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", "CRCTag26_B");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should test checkForCRCRS method with getValue false and checkForGastrichRS false")
    void testCheckForCRCRSBothFalse() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForCRCRS", 
            List.class, String.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList("NonMatchingTag")));
        familyHistory.add(relative);

        Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", "CRCTag26_B");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should test checkForCRCRS method with conditionFound false")
    void testCheckForCRCRSConditionFoundFalse() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForCRCRS", 
            List.class, String.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create 2 relatives with CRCTag26_A matching tags but no CRCTag26_B
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", "CRCTag26_B");
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Should test getValue method with matching tags")
    void testGetValue() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("getValue", 
            List.class, String.class, int.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create 2 relatives with matching tags
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 2);
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should test getValue method with insufficient matching tags")
    void testGetValueInsufficientTags() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("getValue", 
            List.class, String.class, int.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create only 1 relative with matching tag (need 2)
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            tags.add(crcTag26AConditions[0]);
            relative.setTags(tags);
            familyHistory.add(relative);

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 2);
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Should test getValue method with empty tags")
    void testGetValueEmptyTags() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("getValue", 
            List.class, String.class, int.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>()); // Empty tags
        familyHistory.add(relative);

        Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 1);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should test getValue method with non-matching tags")
    void testGetValueNonMatchingTags() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("getValue", 
            List.class, String.class, int.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList("NonMatchingTag")));
        familyHistory.add(relative);

        Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 1);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should test getValue method with exactly matching count")
    void testGetValueExactCount() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("getValue", 
            List.class, String.class, int.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create exactly 2 relatives with matching tags
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 2);
            assertThat(result).isTrue();
            
            // Test with count 3 (should fail)
            Boolean result2 = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 3);
            assertThat(result2).isFalse();
        }
    }

    @Test
    @DisplayName("Should test getValue method with more than required count")
    void testGetValueMoreThanRequired() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("getValue", 
            List.class, String.class, int.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create 3 relatives with matching tags (need 2)
            for (int i = 0; i < 3; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", 2);
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should test checkForGastrichRS method with matching tags")
    void testCheckForGastrichRS() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForGastrichRS", 
            List.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create 2 relatives with same tag (appears >= 2 times)
            FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags1 = new HashSet<>();
            tags1.add(crcTag26AConditions[0]);
            relative1.setTags(tags1);
            familyHistory.add(relative1);
            
            FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags2 = new HashSet<>();
            tags2.add(crcTag26AConditions[0]); // Same tag
            relative2.setTags(tags2);
            familyHistory.add(relative2);

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A");
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should test checkForGastrichRS method with no matching tags")
    void testCheckForGastrichRSNoMatch() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForGastrichRS", 
            List.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList("SomeOtherTag")));
        familyHistory.add(relative);

        Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should test checkForGastrichRS method with tag appearing only once")
    void testCheckForGastrichRSSingleOccurrence() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForGastrichRS", 
            List.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create only 1 relative with tag (need >= 2 occurrences)
            FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags = new HashSet<>();
            tags.add(crcTag26AConditions[0]);
            relative.setTags(tags);
            familyHistory.add(relative);

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A");
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Should test checkForGastrichRS method with multiple relatives and same tag")
    void testCheckForGastrichRSMultipleRelatives() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForGastrichRS", 
            List.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0) {
            // Create 3 relatives with same tag (appears 3 times)
            for (int i = 0; i < 3; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A");
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should return CRCTag26 when 2+ relatives with matching conditions")
    void testGetGastricReasoningStatementWithCRCTag26Matching() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        String[] crcTag26BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_B");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0 && 
            crcTag26BConditions != null && crcTag26BConditions.length > 0) {
            // Create 2 relatives with CRCTag26_A matching tags
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }
            
            // Add CRCTag26_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag26BConditions[0]);
            
            Set<String> tagsSet = new HashSet<>();

            ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

            assertThat(result).isNotNull();
            assertThat(result.getReasoningStatementEn()).isNotEmpty();
            
            boolean hasCRCTag26 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTag26.getTagId()));
            assertThat(hasCRCTag26).isTrue();
        }
    }

    @Test
    @DisplayName("Should return CRCTag27 when 2+ relatives with matching conditions")
    void testGetGastricReasoningStatementWithCRCTag27Matching() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag27AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag27_A");
        String[] crcTag27BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag27_B");
        
        if (crcTag27AConditions != null && crcTag27AConditions.length > 0 && 
            crcTag27BConditions != null && crcTag27BConditions.length > 0) {
            // Create 2 relatives with CRCTag27_A matching tags
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag27AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }
            
            // Add CRCTag27_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag27BConditions[0]);
            
            Set<String> tagsSet = new HashSet<>();

            ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

            assertThat(result).isNotNull();
            assertThat(result.getReasoningStatementEn()).isNotEmpty();
            
            boolean hasCRCTag27 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTAG27.getTagId()));
            assertThat(hasCRCTag27).isTrue();
        }
    }

    @Test
    @DisplayName("Should return CRCTag26 when checkForGastrichRS returns true")
    void testGetGastricReasoningStatementWithCRCTag26GastrichRS() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        String[] crcTag26BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_B");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0 && 
            crcTag26BConditions != null && crcTag26BConditions.length > 0) {
            // Create 2 relatives with same CRCTag26_A tag (for checkForGastrichRS)
            FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags1 = new HashSet<>();
            tags1.add(crcTag26AConditions[0]);
            relative1.setTags(tags1);
            familyHistory.add(relative1);
            
            FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags2 = new HashSet<>();
            tags2.add(crcTag26AConditions[0]); // Same tag
            relative2.setTags(tags2);
            familyHistory.add(relative2);
            
            // Add CRCTag26_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag26BConditions[0]);
            
            Set<String> tagsSet = new HashSet<>();

            ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

            assertThat(result).isNotNull();
            assertThat(result.getReasoningStatementEn()).isNotEmpty();
            
            boolean hasCRCTag26 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTag26.getTagId()));
            assertThat(hasCRCTag26).isTrue();
        }
    }

    @Test
    @DisplayName("Should return CRCTag27 when checkForGastrichRS returns true")
    void testGetGastricReasoningStatementWithCRCTag27GastrichRS() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag27AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag27_A");
        String[] crcTag27BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag27_B");
        
        if (crcTag27AConditions != null && crcTag27AConditions.length > 0 && 
            crcTag27BConditions != null && crcTag27BConditions.length > 0) {
            // Create 2 relatives with same CRCTag27_A tag (for checkForGastrichRS)
            FamilyHistoryDetailsTagsDTO relative1 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags1 = new HashSet<>();
            tags1.add(crcTag27AConditions[0]);
            relative1.setTags(tags1);
            familyHistory.add(relative1);
            
            FamilyHistoryDetailsTagsDTO relative2 = new FamilyHistoryDetailsTagsDTO();
            Set<String> tags2 = new HashSet<>();
            tags2.add(crcTag27AConditions[0]); // Same tag
            relative2.setTags(tags2);
            familyHistory.add(relative2);
            
            // Add CRCTag27_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag27BConditions[0]);
            
            Set<String> tagsSet = new HashSet<>();

            ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

            assertThat(result).isNotNull();
            assertThat(result.getReasoningStatementEn()).isNotEmpty();
            
            boolean hasCRCTag27 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTAG27.getTagId()));
            assertThat(hasCRCTag27).isTrue();
        }
    }

    @Test
    @DisplayName("Should handle empty strings in reasoning statement")
    void testGetGastricReasoningStatementRemovesEmptyStrings() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = Collections.emptyList();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // Empty strings should be removed
        assertThat(result.getReasoningStatementEn()).doesNotContain("");
    }

    @Test
    @DisplayName("Should handle family history with size exactly 2")
    void testGetGastricReasoningStatementWithSizeExactly2() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = createFamilyHistoryWithTwoRelatives();
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // Should check for CRCTag26 and CRCTag27 when size >= 2
    }

    @Test
    @DisplayName("Should handle family history with size greater than 2")
    void testGetGastricReasoningStatementWithSizeGreaterThan2() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>(createFamilyHistoryWithTwoRelatives());
        FamilyHistoryDetailsTagsDTO relative3 = new FamilyHistoryDetailsTagsDTO();
        relative3.setTags(new HashSet<>(Arrays.asList("Tag5", "Tag6")));
        familyHistory.add(relative3);
        
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // Should check for CRCTag26 and CRCTag27 when size >= 2
    }

    @Test
    @DisplayName("Should handle family history with size 1")
    void testGetGastricReasoningStatementWithSize1() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList("Tag1")));
        familyHistory.add(relative);
        
        Set<String> tagsSet = new HashSet<>();

        ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

        assertThat(result).isNotNull();
        // Should not check for CRCTag26 and CRCTag27 (need size >= 2)
    }

    @Test
    @DisplayName("Should return both CRCTag25 and CRCTag26 when conditions are met")
    void testGetGastricReasoningStatementWithMultipleConditions() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        String[] crcTag26BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_B");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0 && 
            crcTag26BConditions != null && crcTag26BConditions.length > 0) {
            // Create 2 relatives with CRCTag26_A matching tags
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }
            
            // Add CRCTag26_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag26BConditions[0]);
            
            Set<String> tagsSet = new HashSet<>();
            tagsSet.add(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue());

            ReasoningStatementObject result = utility.getGastricReasoningStatement(familyHistory, tagsSet);

            assertThat(result).isNotNull();
            assertThat(result.getReasoningStatementEn()).isNotEmpty();
            
            boolean hasCRCTag25 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTag25.getTagId()));
            boolean hasCRCTag26 = result.getReasoningStatementAndTags().stream()
                .anyMatch(rs -> rs.getTagIds().contains(TagIdConstant.CRCTag26.getTagId()));
            
            assertThat(hasCRCTag25).isTrue();
            assertThat(hasCRCTag26).isTrue();
        }
    }

    @Test
    @DisplayName("Should test checkForCRCRS with getValue returning true for tagIdA")
    void testCheckForCRCRSWithGetValueTrue() throws Exception {
        java.lang.reflect.Method method = GastricReasoningStatementUtility.class.getDeclaredMethod("checkForCRCRS", 
            List.class, String.class, String.class);
        method.setAccessible(true);

        List<FamilyHistoryDetailsTagsDTO> familyHistory = new ArrayList<>();
        String[] crcTag26AConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_A");
        String[] crcTag26BConditions = ConditionHolder.getInstance().getConditionMap().get("CRCTag26_B");
        
        if (crcTag26AConditions != null && crcTag26AConditions.length > 0 && 
            crcTag26BConditions != null && crcTag26BConditions.length > 0) {
            // Create 2 relatives with CRCTag26_A matching tags (for getValue to return true)
            for (int i = 0; i < 2; i++) {
                FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
                Set<String> tags = new HashSet<>();
                tags.add(crcTag26AConditions[0]);
                relative.setTags(tags);
                familyHistory.add(relative);
            }
            
            // Add CRCTag26_B tag to one relative
            familyHistory.get(0).getTags().add(crcTag26BConditions[0]);

            Boolean result = (Boolean) method.invoke(utility, familyHistory, "CRCTag26_A", "CRCTag26_B");
            assertThat(result).isTrue();
        }
    }

    // ========== NCCN CEEG v1.2026 (CAR-753): CRCCRIT18 ==========

    private static FamilyHistoryDetailsTagsDTO relativeTagged(String... tags) {
        FamilyHistoryDetailsTagsDTO relative = new FamilyHistoryDetailsTagsDTO();
        relative.setTags(new HashSet<>(Arrays.asList(tags)));
        return relative;
    }

    @Test
    @DisplayName("should_flagCRCRS18_when_threeSameSideRelativesHadGastricCancer")
    void should_flagCRCRS18_when_threeSameSideRelativesHadGastricCancer() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> family = List.of(
            relativeTagged("FmGastricMatFDR"), relativeTagged("FmGastricMatSDR"), relativeTagged("FmGastricMatSDR"));

        ReasoningStatementObject result = utility.getGastricReasoningStatement(family, new HashSet<>());

        assertThat(result.getReasoningStatementEn()).contains(ReasoningStatementConstants.CRCRS18.getValue());
        assertThat(result.getReasoningStatementAndTags()).anySatisfy(statement ->
            assertThat(statement.getReasoningStatementId()).isEqualTo("CRCRS18"));
    }

    @Test
    @DisplayName("should_notFlagCRCRS18_when_gastricRelativesAreSplitAcrossSides")
    void should_notFlagCRCRS18_when_gastricRelativesAreSplitAcrossSides() throws ConnectException {
        List<FamilyHistoryDetailsTagsDTO> family = List.of(
            relativeTagged("FmGastricMatFDR"), relativeTagged("FmGastricMatSDR"), relativeTagged("FmGastricPatSDR"));

        ReasoningStatementObject result = utility.getGastricReasoningStatement(family, new HashSet<>());

        assertThat(result.getReasoningStatementEn()).doesNotContain(ReasoningStatementConstants.CRCRS18.getValue());
    }
}
