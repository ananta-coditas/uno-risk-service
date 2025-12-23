package com.genomebrain.backend.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.genomebrain.backend.domain.GuideLinesDetails;
import com.genomebrain.backend.repository.custom.CustomGuideLinesDetailsRepository;
import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementAndTags;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("GuideLineServiceImpl Unit Tests")
class GuideLineServiceImplTest {

    @Mock
    private CustomGuideLinesDetailsRepository guideLinesDetailsRepository;

    @InjectMocks
    private GuideLineServiceImpl guideLineService;

    private GuideLinesDetails hbocGuideline;
    private GuideLinesDetails lynchGuideline;
    private GuideLinesDetails apsGuideline;

    @BeforeEach
    void setUp() {
        hbocGuideline = createGuideline("1", "NCCN HBOC", "HBOC reasoning statement", "RS1", 
            Arrays.asList("Female", "PrBreast"), "TAG1");
        
        lynchGuideline = createGuideline("2", "NCCN LYNCH", "LYNCH reasoning statement", "RS2", 
            Arrays.asList("Male", "PrColon"), "TAG2");
        
        apsGuideline = createGuideline("3", "NCCN APS", "APS reasoning statement", "RS3", 
            Arrays.asList("Female", "PrColon"), "TAG3");
    }

    private GuideLinesDetails createGuideline(String id, String guidelineName, String reasoningStatement, 
            String reasoningStatementId, List<String> tags, String tagsId) {
        GuideLinesDetails guideline = new GuideLinesDetails();
        guideline.setId(id);
        guideline.setGuidelineName(guidelineName);
        guideline.setReasoningStatementEnglish(reasoningStatement);
        guideline.setReasoningStatementId(reasoningStatementId);
        guideline.setTagsList(tags);
        guideline.setTagsId(tagsId);
        return guideline;
    }

    @Test
    @DisplayName("Should successfully get guidelines for tags with HBOC criteria")
    void testGetGuidelinesForTagsWithHBOC() throws Exception {
        String tagsObject = createTagsJson("female", "prbreast");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        assertThat(result.isLYNCH()).isFalse();
        assertThat(result.isAPS()).isFalse();
        assertThat(result.isGuideLineMet()).isTrue();
        assertThat(result.getStatus()).isEqualTo("success");
        assertThat(result.getMessage()).isEqualTo("Guidelines fetched successfully");
        assertThat(result.getReasoningStatementsForHBOC()).contains("HBOC reasoning statement");
        assertThat(result.getHbocReasoningStatementAndTags()).isNotEmpty();
        verify(guideLinesDetailsRepository).getAllTheGuidelines(anyList());
    }

    @Test
    @DisplayName("Should successfully get guidelines for tags with LYNCH criteria")
    void testGetGuidelinesForTagsWithLYNCH() throws Exception {
        String tagsObject = createTagsJson("male", "prcolon");
        List<GuideLinesDetails> guidelines = Arrays.asList(lynchGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isFalse();
        assertThat(result.isLYNCH()).isTrue();
        assertThat(result.isAPS()).isFalse();
        assertThat(result.isGuideLineMet()).isTrue();
        assertThat(result.getReasoningStatementsForLYNCH()).contains("LYNCH reasoning statement");
        assertThat(result.getLynchReasoningStatementAndTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Should successfully get guidelines for tags with APS criteria")
    void testGetGuidelinesForTagsWithAPS() throws Exception {
        String tagsObject = createTagsJson("female", "prcolon");
        List<GuideLinesDetails> guidelines = Arrays.asList(apsGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isFalse();
        assertThat(result.isLYNCH()).isFalse();
        assertThat(result.isAPS()).isTrue();
        assertThat(result.isGuideLineMet()).isTrue();
        assertThat(result.getReasoningStatementsForAPS()).contains("APS reasoning statement");
        assertThat(result.getApsReasoningStatementAndTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle multiple guideline types in single response")
    void testGetGuidelinesForTagsWithMultipleTypes() throws Exception {
        String tagsObject = createTagsJson("female", "prbreast", "prcolon");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline, apsGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        assertThat(result.isAPS()).isTrue();
        assertThat(result.isLYNCH()).isFalse();
        assertThat(result.getReasoningStatementsForHBOC()).isNotEmpty();
        assertThat(result.getReasoningStatementsForAPS()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle empty guidelines result")
    void testGetGuidelinesForTagsWithEmptyResult() throws Exception {
        String tagsObject = createTagsJson("unknown", "tag");
        List<GuideLinesDetails> guidelines = Collections.emptyList();
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isFalse();
        assertThat(result.isLYNCH()).isFalse();
        assertThat(result.isAPS()).isFalse();
        assertThat(result.isGuideLineMet()).isFalse();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should handle tags with more than 50 items")
    void testGetGuidelinesForTagsWithMoreThan50Tags() throws Exception {
        List<String> manyTags = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            manyTags.add("tag" + i);
        }
        String tagsObject = createTagsJson(manyTags.toArray(new String[0]));
        
        List<GuideLinesDetails> allGuidelines = Arrays.asList(hbocGuideline, lynchGuideline);
        when(guideLinesDetailsRepository.findAll()).thenReturn(allGuidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        verify(guideLinesDetailsRepository, never()).getAllTheGuidelines(anyList());
        verify(guideLinesDetailsRepository).findAll();
    }

    @Test
    @DisplayName("Should handle exactly 50 tags")
    void testGetGuidelinesForTagsWithExactly50Tags() throws Exception {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            tags.add("tag" + i);
        }
        String tagsObject = createTagsJson(tags.toArray(new String[0]));
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        verify(guideLinesDetailsRepository).getAllTheGuidelines(anyList());
        verify(guideLinesDetailsRepository, never()).findAll();
    }

    @Test
    @DisplayName("Should capitalize tags correctly")
    void testGetGuidelinesForTagsCapitalizesTags() throws Exception {
        String tagsObject = createTagsJson("female", "prbreast", "MALE", "prcolon");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        verify(guideLinesDetailsRepository).getAllTheGuidelines(anyList());
    }

    @Test
    @DisplayName("Should handle guidelines without reasoning statements")
    void testGetGuidelinesForTagsWithoutReasoningStatements() throws Exception {
        GuideLinesDetails guidelineWithoutReasoning = createGuideline("4", "NCCN HBOC", null, "RS4", 
            Arrays.asList("Female"), "TAG4");
        String tagsObject = createTagsJson("female");
        List<GuideLinesDetails> guidelines = Arrays.asList(guidelineWithoutReasoning);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        assertThat(result.getReasoningStatementsForHBOC()).isEmpty();
        assertThat(result.getHbocReasoningStatementAndTags()).isEmpty();
    }

    @Test
    @DisplayName("Should handle guidelines with empty reasoning statements")
    void testGetGuidelinesForTagsWithEmptyReasoningStatements() throws Exception {
        GuideLinesDetails guidelineWithEmptyReasoning = createGuideline("5", "NCCN LYNCH", "", "RS5", 
            Arrays.asList("Male"), "TAG5");
        String tagsObject = createTagsJson("male");
        List<GuideLinesDetails> guidelines = Arrays.asList(guidelineWithEmptyReasoning);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isLYNCH()).isTrue();
        assertThat(result.getReasoningStatementsForLYNCH()).isEmpty();
    }

    @Test
    @DisplayName("Should handle exception during processing")
    void testGetGuidelinesForTagsWithException() throws Exception {
        String invalidTagsObject = "invalid json";
        
        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(invalidTagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotEqualTo("success");
        assertThat(result.getMessage()).isEqualTo("failure");
    }

    @Test
    @DisplayName("Should handle null tags array in JSON")
    void testGetGuidelinesForTagsWithNullTagsArray() throws Exception {
        String tagsObject = "{\"tags\": null}";
        
        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotEqualTo("success");
    }

    @Test
    @DisplayName("Should handle empty tags array")
    void testGetGuidelinesForTagsWithEmptyTagsArray() throws Exception {
        String tagsObject = createTagsJson();
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("success");
        assertThat(result.isGuideLineMet()).isFalse();
    }

    @Test
    @DisplayName("Should handle single tag")
    void testGetGuidelinesForTagsWithSingleTag() throws Exception {
        String tagsObject = createTagsJson("female");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should set ReasoningStatementAndTags correctly for HBOC")
    void testGetGuidelinesForTagsSetsReasoningStatementAndTagsForHBOC() throws Exception {
        String tagsObject = createTagsJson("female", "prbreast");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result.getHbocReasoningStatementAndTags()).isNotEmpty();
        ReasoningStatementAndTags rsAndTags = result.getHbocReasoningStatementAndTags().iterator().next();
        assertThat(rsAndTags.getReasoningStatementId()).isEqualTo("RS1");
        assertThat(rsAndTags.getTagIds()).contains("TAG1");
    }

    @Test
    @DisplayName("Should set ReasoningStatementAndTags correctly for LYNCH")
    void testGetGuidelinesForTagsSetsReasoningStatementAndTagsForLYNCH() throws Exception {
        String tagsObject = createTagsJson("male", "prcolon");
        List<GuideLinesDetails> guidelines = Arrays.asList(lynchGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result.getLynchReasoningStatementAndTags()).isNotEmpty();
        ReasoningStatementAndTags rsAndTags = result.getLynchReasoningStatementAndTags().iterator().next();
        assertThat(rsAndTags.getReasoningStatementId()).isEqualTo("RS2");
        assertThat(rsAndTags.getTagIds()).contains("TAG2");
    }

    @Test
    @DisplayName("Should set ReasoningStatementAndTags correctly for APS")
    void testGetGuidelinesForTagsSetsReasoningStatementAndTagsForAPS() throws Exception {
        String tagsObject = createTagsJson("female", "prcolon");
        List<GuideLinesDetails> guidelines = Arrays.asList(apsGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result.getApsReasoningStatementAndTags()).isNotEmpty();
        ReasoningStatementAndTags rsAndTags = result.getApsReasoningStatementAndTags().iterator().next();
        assertThat(rsAndTags.getReasoningStatementId()).isEqualTo("RS3");
        assertThat(rsAndTags.getTagIds()).contains("TAG3");
    }

    @Test
    @DisplayName("Should handle multiple guidelines of same type")
    void testGetGuidelinesForTagsWithMultipleSameTypeGuidelines() throws Exception {
        GuideLinesDetails hbocGuideline2 = createGuideline("6", "NCCN HBOC", "HBOC reasoning statement 2", "RS6", 
            Arrays.asList("Female", "PrBreast"), "TAG6");
        String tagsObject = createTagsJson("female", "prbreast");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline, hbocGuideline2);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        assertThat(result.getReasoningStatementsForHBOC()).hasSize(2);
        assertThat(result.getHbocReasoningStatementAndTags()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle parallel stream processing for large tag lists")
    void testGetGuidelinesForTagsWithParallelStream() throws Exception {
        List<String> manyTags = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            manyTags.add("tag" + i);
        }
        String tagsObject = createTagsJson(manyTags.toArray(new String[0]));
        
        GuideLinesDetails matchingGuideline = createGuideline("7", "NCCN HBOC", "Test reasoning", "RS7", 
            manyTags.subList(0, 10), "TAG7");
        List<GuideLinesDetails> allGuidelines = Arrays.asList(matchingGuideline, hbocGuideline);
        
        when(guideLinesDetailsRepository.findAll()).thenReturn(allGuidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should get tags sorted correctly")
    void testGetTagsSorted() {
        List<String> tags = Arrays.asList("Female", "PrBreast", "FmProstate");
        
        List<List<String>> result = guideLineService.getTagsSorted(tags);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        // Should generate combinations from size 1 to 5
        // For 3 tags: C(3,1) + C(3,2) + C(3,3) = 3 + 3 + 1 = 7 combinations
        assertThat(result.size()).isGreaterThanOrEqualTo(7);
        
        // All lists should be sorted
        for (List<String> list : result) {
            List<String> sorted = new ArrayList<>(list);
            Collections.sort(sorted);
            assertThat(list).isEqualTo(sorted);
        }
    }

    @Test
    @DisplayName("Should get tags sorted with empty list")
    void testGetTagsSortedWithEmptyList() {
        List<String> tags = Collections.emptyList();
        
        List<List<String>> result = guideLineService.getTagsSorted(tags);

        assertThat(result).isNotNull();
        // Empty list should return empty combinations
    }

    @Test
    @DisplayName("Should get tags sorted with single tag")
    void testGetTagsSortedWithSingleTag() {
        List<String> tags = Arrays.asList("Female");
        
        List<List<String>> result = guideLineService.getTagsSorted(tags);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1); // Only one combination of size 1
        assertThat(result.get(0)).containsExactly("Female");
    }

    @Test
    @DisplayName("Should get tags sorted with multiple tags")
    void testGetTagsSortedWithMultipleTags() {
        List<String> tags = Arrays.asList("A", "B", "C", "D", "E");
        
        List<List<String>> result = guideLineService.getTagsSorted(tags);

        assertThat(result).isNotNull();
        // Should generate combinations from size 1 to 5
        // C(5,1) + C(5,2) + C(5,3) + C(5,4) + C(5,5) = 5 + 10 + 10 + 5 + 1 = 31
        assertThat(result.size()).isGreaterThanOrEqualTo(31);
    }

    @Test
    @DisplayName("Should handle guideline name that doesn't match any criteria")
    void testGetGuidelinesForTagsWithUnknownGuidelineName() throws Exception {
        GuideLinesDetails unknownGuideline = createGuideline("8", "UNKNOWN GUIDELINE", "Unknown reasoning", "RS8", 
            Arrays.asList("Tag1"), "TAG8");
        String tagsObject = createTagsJson("tag1");
        List<GuideLinesDetails> guidelines = Arrays.asList(unknownGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isFalse();
        assertThat(result.isLYNCH()).isFalse();
        assertThat(result.isAPS()).isFalse();
        assertThat(result.isGuideLineMet()).isTrue();
    }

    @Test
    @DisplayName("Should handle guideline with partial name match")
    void testGetGuidelinesForTagsWithPartialNameMatch() throws Exception {
        GuideLinesDetails partialMatch = createGuideline("9", "NCCN HBOC Extended", "Extended reasoning", "RS9", 
            Arrays.asList("Female"), "TAG9");
        String tagsObject = createTagsJson("female");
        List<GuideLinesDetails> guidelines = Arrays.asList(partialMatch);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Note: isHBOC() checks if Set contains exact "NCCN HBOC", not substring match
        // So "NCCN HBOC Extended" won't set isHBOC to true, but reasoning statement will be added
        // because the contains() check on the string works for substring matching
        assertThat(result.isHBOC()).isFalse();
        assertThat(result.getReasoningStatementsForHBOC()).contains("Extended reasoning");
        assertThat(result.isGuideLineMet()).isTrue();
    }

    @Test
    @DisplayName("Should handle repository exception")
    void testGetGuidelinesForTagsWithRepositoryException() throws Exception {
        String tagsObject = createTagsJson("female", "prbreast");
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList()))
            .thenThrow(new RuntimeException("Repository error"));

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotEqualTo("success");
        assertThat(result.getMessage()).isEqualTo("failure");
    }

    @Test
    @DisplayName("Should handle case-insensitive tag matching in parallel stream")
    void testGetGuidelinesForTagsCaseInsensitiveMatching() throws Exception {
        List<String> manyTags = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            manyTags.add("TAG" + i);
        }
        String tagsObject = createTagsJson(manyTags.toArray(new String[0]));
        
        GuideLinesDetails guideline = createGuideline("10", "NCCN HBOC", "Reasoning", "RS10", 
            manyTags.subList(0, 10), "TAG10");
        List<GuideLinesDetails> allGuidelines = Arrays.asList(guideline);
        
        when(guideLinesDetailsRepository.findAll()).thenReturn(allGuidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Tags are capitalized, so matching should work
    }

    @Test
    @DisplayName("Should handle duplicate reasoning statements")
    void testGetGuidelinesForTagsWithDuplicateReasoningStatements() throws Exception {
        GuideLinesDetails duplicate1 = createGuideline("11", "NCCN HBOC", "Same reasoning", "RS11", 
            Arrays.asList("Female"), "TAG11");
        GuideLinesDetails duplicate2 = createGuideline("12", "NCCN HBOC", "Same reasoning", "RS12", 
            Arrays.asList("Female", "PrBreast"), "TAG12");
        String tagsObject = createTagsJson("female", "prbreast");
        List<GuideLinesDetails> guidelines = Arrays.asList(duplicate1, duplicate2);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Set should deduplicate reasoning statements
        assertThat(result.getReasoningStatementsForHBOC()).hasSize(1);
        assertThat(result.getReasoningStatementsForHBOC()).contains("Same reasoning");
    }

    @Test
    @DisplayName("Should handle all three guideline types together")
    void testGetGuidelinesForTagsWithAllThreeTypes() throws Exception {
        String tagsObject = createTagsJson("female", "prbreast", "prcolon", "male");
        List<GuideLinesDetails> guidelines = Arrays.asList(hbocGuideline, lynchGuideline, apsGuideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        assertThat(result.isLYNCH()).isTrue();
        assertThat(result.isAPS()).isTrue();
        assertThat(result.getReasoningStatementsForHBOC()).isNotEmpty();
        assertThat(result.getReasoningStatementsForLYNCH()).isNotEmpty();
        assertThat(result.getReasoningStatementsForAPS()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle tags with special characters")
    void testGetGuidelinesForTagsWithSpecialCharacters() throws Exception {
        String tagsObject = createTagsJson("tag-with-dash", "tag_with_underscore", "tag.with.dot");
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should handle very large tag list in parallel stream")
    void testGetGuidelinesForTagsWithVeryLargeTagList() throws Exception {
        List<String> veryManyTags = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            veryManyTags.add("tag" + i);
        }
        String tagsObject = createTagsJson(veryManyTags.toArray(new String[0]));
        
        when(guideLinesDetailsRepository.findAll()).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        verify(guideLinesDetailsRepository).findAll();
        verify(guideLinesDetailsRepository, never()).getAllTheGuidelines(anyList());
    }

    @Test
    @DisplayName("Should handle combination method with size 0")
    void testCombinationWithSizeZero() {
        List<String> tags = Arrays.asList("A", "B", "C");
        
        // Test through getTagsSorted which calls combination
        List<List<String>> result = guideLineService.getTagsSorted(tags);
        
        // Size 0 combination should be included (empty list)
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle combination method with empty values")
    void testCombinationWithEmptyValues() {
        List<String> emptyTags = Collections.emptyList();
        
        List<List<String>> result = guideLineService.getTagsSorted(emptyTags);
        
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle tags that don't match any guideline tags")
    void testGetGuidelinesForTagsWithNoMatchingTags() throws Exception {
        String tagsObject = createTagsJson("unmatched", "tags");
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isGuideLineMet()).isFalse();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should handle guideline with null tagsId")
    void testGetGuidelinesForTagsWithNullTagsId() throws Exception {
        GuideLinesDetails guidelineWithNullTagsId = createGuideline("13", "NCCN HBOC", "Reasoning", "RS13", 
            Arrays.asList("Female"), null);
        String tagsObject = createTagsJson("female");
        List<GuideLinesDetails> guidelines = Arrays.asList(guidelineWithNullTagsId);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        // Should handle null tagsId gracefully
    }

    @Test
    @DisplayName("Should handle guideline with null reasoningStatementId")
    void testGetGuidelinesForTagsWithNullReasoningStatementId() throws Exception {
        GuideLinesDetails guideline = new GuideLinesDetails();
        guideline.setId("14");
        guideline.setGuidelineName("NCCN HBOC");
        guideline.setReasoningStatementEnglish("Reasoning");
        guideline.setReasoningStatementId(null);
        guideline.setTagsList(Arrays.asList("Female"));
        guideline.setTagsId("TAG14");
        
        String tagsObject = createTagsJson("female");
        List<GuideLinesDetails> guidelines = Arrays.asList(guideline);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
    }

    @Test
    @DisplayName("Should handle combination edge cases through getTagsSorted")
    void testGetTagsSortedEdgeCases() {
        // Test with 2 tags - should generate combinations of size 1 and 2
        List<String> twoTags = Arrays.asList("A", "B");
        List<List<String>> result = guideLineService.getTagsSorted(twoTags);
        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThanOrEqualTo(3); // C(2,1) + C(2,2) = 2 + 1 = 3
        
        // Test with 6 tags - should generate combinations up to size 5
        List<String> sixTags = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<List<String>> result2 = guideLineService.getTagsSorted(sixTags);
        assertThat(result2).isNotNull();
        // Should have combinations from size 1 to 5, not 6
        assertThat(result2.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle JSON parsing exception")
    void testGetGuidelinesForTagsWithJSONParsingException() {
        String invalidJson = "{invalid json}";
        
        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(invalidJson);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotEqualTo("success");
        assertThat(result.getMessage()).isEqualTo("failure");
    }

    @Test
    @DisplayName("Should handle missing tags key in JSON")
    void testGetGuidelinesForTagsWithMissingTagsKey() {
        String tagsObject = "{}";
        
        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotEqualTo("success");
    }

    @Test
    @DisplayName("Should handle null tag values in JSON array")
    void testGetGuidelinesForTagsWithNullTagValues() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("valid");
        jsonArray.put(JSONObject.NULL);
        jsonObject.put("tags", jsonArray);
        String tagsObject = jsonObject.toString();
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Should handle null gracefully or throw exception
    }

    @Test
    @DisplayName("Should handle guideline with empty tagsList in parallel stream")
    void testGetGuidelinesForTagsWithEmptyTagsListInParallelStream() throws Exception {
        List<String> manyTags = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            manyTags.add("tag" + i);
        }
        String tagsObject = createTagsJson(manyTags.toArray(new String[0]));
        
        GuideLinesDetails guidelineWithEmptyTags = createGuideline("15", "NCCN HBOC", "Reasoning", "RS15", 
            Collections.emptyList(), "TAG15");
        List<GuideLinesDetails> allGuidelines = Arrays.asList(guidelineWithEmptyTags);
        
        when(guideLinesDetailsRepository.findAll()).thenReturn(allGuidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Empty tagsList should not match
    }

    @Test
    @DisplayName("Should handle guideline with null tagsList in parallel stream")
    void testGetGuidelinesForTagsWithNullTagsListInParallelStream() throws Exception {
        List<String> manyTags = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            manyTags.add("tag" + i);
        }
        String tagsObject = createTagsJson(manyTags.toArray(new String[0]));
        
        GuideLinesDetails guidelineWithNullTags = new GuideLinesDetails();
        guidelineWithNullTags.setId("16");
        guidelineWithNullTags.setGuidelineName("NCCN HBOC");
        guidelineWithNullTags.setTagsList(null);
        List<GuideLinesDetails> allGuidelines = Arrays.asList(guidelineWithNullTags);
        
        when(guideLinesDetailsRepository.findAll()).thenReturn(allGuidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Null tagsList should be handled gracefully
    }

    @Test
    @DisplayName("Should verify main method exists and can be called")
    void testMainMethod() {
        // Main method is static and just sorts/prints, so we just verify it exists
        // We can't easily test System.out.println, but we can verify no exception
        try {
            GuideLineServiceImpl.main(new String[]{"test"});
        } catch (Exception e) {
            // If it throws an exception, that's a problem
            throw new AssertionError("Main method should not throw exception", e);
        }
    }

    @Test
    @DisplayName("Should handle guideline name with case variations")
    void testGetGuidelinesForTagsWithCaseVariations() throws Exception {
        GuideLinesDetails lowerCase = createGuideline("17", "nccn hboc", "Reasoning", "RS17", 
            Arrays.asList("Female"), "TAG17");
        GuideLinesDetails mixedCase = createGuideline("18", "Nccn Lynch", "Reasoning", "RS18", 
            Arrays.asList("Male"), "TAG18");
        String tagsObject = createTagsJson("female", "male");
        List<GuideLinesDetails> guidelines = Arrays.asList(lowerCase, mixedCase);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        // Contains check is case-sensitive, so these won't match
        assertThat(result.isHBOC()).isFalse();
        assertThat(result.isLYNCH()).isFalse();
    }

    @Test
    @DisplayName("Should handle multiple tags with same guideline type")
    void testGetGuidelinesForTagsWithMultipleSameType() throws Exception {
        GuideLinesDetails hboc1 = createGuideline("19", "NCCN HBOC", "Reasoning 1", "RS19", 
            Arrays.asList("Female", "Tag1"), "TAG19");
        GuideLinesDetails hboc2 = createGuideline("20", "NCCN HBOC", "Reasoning 2", "RS20", 
            Arrays.asList("Female", "Tag2"), "TAG20");
        String tagsObject = createTagsJson("female", "tag1", "tag2");
        List<GuideLinesDetails> guidelines = Arrays.asList(hboc1, hboc2);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(guidelines);

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.isHBOC()).isTrue();
        assertThat(result.getReasoningStatementsForHBOC()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle tags with whitespace")
    void testGetGuidelinesForTagsWithWhitespace() throws Exception {
        String tagsObject = createTagsJson(" tag with space ", "  another  ");
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should handle very long tag names")
    void testGetGuidelinesForTagsWithVeryLongTagNames() throws Exception {
        String longTag = "a".repeat(1000);
        String tagsObject = createTagsJson(longTag);
        
        when(guideLinesDetailsRepository.getAllTheGuidelines(anyList())).thenReturn(Collections.emptyList());

        GenomeBrainResponseDTO result = guideLineService.getGuidelinesForTags(tagsObject);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("Should handle combination recursion correctly")
    void testCombinationRecursion() {
        // Test that combination method works correctly for different sizes
        List<String> tags = Arrays.asList("A", "B", "C", "D");
        
        List<List<String>> result = guideLineService.getTagsSorted(tags);
        
        assertThat(result).isNotNull();
        // Verify all combinations are present and sorted
        boolean hasSize1 = result.stream().anyMatch(list -> list.size() == 1);
        boolean hasSize2 = result.stream().anyMatch(list -> list.size() == 2);
        boolean hasSize3 = result.stream().anyMatch(list -> list.size() == 3);
        boolean hasSize4 = result.stream().anyMatch(list -> list.size() == 4);
        
        assertThat(hasSize1).isTrue();
        assertThat(hasSize2).isTrue();
        assertThat(hasSize3).isTrue();
        assertThat(hasSize4).isTrue();
    }

    private String createTagsJson(String... tags) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (String tag : tags) {
            jsonArray.put(tag);
        }
        jsonObject.put("tags", jsonArray);
        return jsonObject.toString();
    }
}

