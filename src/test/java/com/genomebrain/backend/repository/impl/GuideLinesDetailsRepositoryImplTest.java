package com.genomebrain.backend.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.DefaultResourceLoader;

import com.genomebrain.backend.domain.GuideLinesDetails;

@DisplayName("GuideLinesDetailsRepositoryImpl Unit Tests")
class GuideLinesDetailsRepositoryImplTest {

    private static final String DATASET = """
        [
          {"tags": ["Female", "PrBreast<=50"], "tag_id": "HBOCTag1", "guideline_name": "NCCN HBOC",
           "reasoning_statement_en": "breast <=50", "reasoning_statement_id": "HBOCRS1", "_class": "ignored"},
          {"tags": ["Female"], "guideline_name": "NCCN HBOC", "reasoning_statement_en": "female only"},
          {"tags": ["FmColon<50MatFDR", "PrColon"], "guideline_name": "NCCN LYNCH", "reasoning_statement_en": "lynch"},
          {"tags": [], "guideline_name": "NCCN APS", "reasoning_statement_en": "no tags"}
        ]
        """;

    @TempDir
    Path tempDir;

    private GuideLinesDetailsRepositoryImpl repositoryFor(String json) throws IOException {
        Path file = tempDir.resolve("guidelines.json");
        Files.writeString(file, json, StandardCharsets.UTF_8);
        GuideLinesDetailsRepositoryImpl repository =
            new GuideLinesDetailsRepositoryImpl(new DefaultResourceLoader(), file.toUri().toString());
        repository.loadGuidelines();
        return repository;
    }

    @Nested
    @DisplayName("loading")
    class Loading {

        @Test
        @DisplayName("should_loadAllRecords_when_dataFileIsValid")
        void should_loadAllRecords_when_dataFileIsValid() throws IOException {
            GuideLinesDetailsRepositoryImpl repository = repositoryFor(DATASET);

            assertThat(repository.findAll()).hasSize(4);
        }

        @Test
        @DisplayName("should_loadOnlyIdentifiedRecords_when_usingBundledDataset")
        void should_loadOnlyIdentifiedRecords_when_usingBundledDataset() {
            GuideLinesDetailsRepositoryImpl repository = new GuideLinesDetailsRepositoryImpl(
                new DefaultResourceLoader(), "classpath:risk_model_data/nccn_guide_lines_details.json");

            repository.loadGuidelines();

            // Every bundled record must carry criteria IDs, otherwise it surfaces as statement text with no ID
            assertThat(repository.findAll()).isNotEmpty().allSatisfy(guideline -> {
                assertThat(guideline.getTagsId()).isNotBlank();
                assertThat(guideline.getReasoningStatementId()).isNotBlank();
            });
        }

        @Test
        @DisplayName("should_failStartup_when_dataFileIsMissing")
        void should_failStartup_when_dataFileIsMissing() {
            GuideLinesDetailsRepositoryImpl repository = new GuideLinesDetailsRepositoryImpl(
                new DefaultResourceLoader(), tempDir.resolve("missing.json").toUri().toString());

            assertThatThrownBy(repository::loadGuidelines)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("missing.json");
        }
    }

    @Nested
    @DisplayName("getAllTheGuidelines")
    class GetAllTheGuidelines {

        @Test
        @DisplayName("should_matchOnlyExactTagCombinations_when_groupIsASupersetOfAGuideline")
        void should_matchOnlyExactTagCombinations_when_groupIsASupersetOfAGuideline() throws IOException {
            GuideLinesDetailsRepositoryImpl repository = repositoryFor(DATASET);

            List<GuideLinesDetails> result = repository.getAllTheGuidelines(List.of(List.of("Female", "PrBreast<=50")));

            assertThat(result).extracting(GuideLinesDetails::getReasoningStatementEnglish)
                .containsExactly("breast <=50");
        }

        @Test
        @DisplayName("should_returnEachGuidelineOnce_when_severalGroupsMatchIt")
        void should_returnEachGuidelineOnce_when_severalGroupsMatchIt() throws IOException {
            GuideLinesDetailsRepositoryImpl repository = repositoryFor(DATASET);

            List<GuideLinesDetails> result = repository.getAllTheGuidelines(List.of(
                List.of("Female"), List.of("Female"), List.of("Female", "PrBreast<=50")));

            assertThat(result).extracting(GuideLinesDetails::getReasoningStatementEnglish)
                .containsExactlyInAnyOrder("female only", "breast <=50");
        }

        @Test
        @DisplayName("should_matchRegardlessOfTagOrder_when_groupIsUnsorted")
        void should_matchRegardlessOfTagOrder_when_groupIsUnsorted() throws IOException {
            GuideLinesDetailsRepositoryImpl repository = repositoryFor(DATASET);

            List<GuideLinesDetails> result = repository.getAllTheGuidelines(List.of(List.of("PrColon", "FmColon<50MatFDR")));

            assertThat(result).extracting(GuideLinesDetails::getGuidelineName).containsExactly("NCCN LYNCH");
        }

        @Test
        @DisplayName("should_neverMatchGuidelineWithoutTags_when_anyGroupIsGiven")
        void should_neverMatchGuidelineWithoutTags_when_anyGroupIsGiven() throws IOException {
            GuideLinesDetailsRepositoryImpl repository = repositoryFor(DATASET);

            List<GuideLinesDetails> result = repository.getAllTheGuidelines(List.of(List.of("Male")));

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should_returnEmpty_when_tagGroupsAreNullOrEmpty")
        void should_returnEmpty_when_tagGroupsAreNullOrEmpty() throws IOException {
            GuideLinesDetailsRepositoryImpl repository = repositoryFor(DATASET);

            assertThat(repository.getAllTheGuidelines(null)).isEmpty();
            assertThat(repository.getAllTheGuidelines(List.of())).isEmpty();
        }
    }
}
