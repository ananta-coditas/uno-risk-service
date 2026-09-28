package com.genomebrain.backend.repository.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genomebrain.backend.domain.GuideLinesDetails;
import com.genomebrain.backend.repository.custom.CustomGuideLinesDetailsRepository;

/**
 * Serves the NCCN guideline dataset (formerly the GenomeBrain MongoDB {@code guide_lines_details}
 * collection) from a bundled JSON file. The location defaults to the classpath copy and can be
 * overridden with {@code nccn.guidelines.data-path} (any Spring resource location, e.g. {@code file:/...}).
 */
@Repository
public class GuideLinesDetailsRepositoryImpl implements CustomGuideLinesDetailsRepository {

    private static final int MAX_TAG_GROUPS = 100000;

    private final Logger log = LoggerFactory.getLogger(GuideLinesDetailsRepositoryImpl.class);

    private final ResourceLoader resourceLoader;

    private final String guidelinesDataPath;

    private List<GuideLinesDetails> lstGuideLinesDetails = Collections.emptyList();

    /** Guidelines keyed by their sorted tag combination, for exact-match lookups. */
    private Map<List<String>, List<GuideLinesDetails>> guidelinesByTags = Collections.emptyMap();

    public GuideLinesDetailsRepositoryImpl(
        ResourceLoader resourceLoader,
        @Value("${nccn.guidelines.data-path:classpath:risk_model_data/nccn_guide_lines_details.json}") String guidelinesDataPath
    ) {
        this.resourceLoader = resourceLoader;
        this.guidelinesDataPath = guidelinesDataPath;
    }

    @PostConstruct
    void loadGuidelines() {
        lstGuideLinesDetails = Collections.unmodifiableList(loadDataFromJson(guidelinesDataPath));
        Map<List<String>, List<GuideLinesDetails>> index = new HashMap<>();
        for (GuideLinesDetails guideline : lstGuideLinesDetails) {
            if (guideline.getTagsList() == null || guideline.getTagsList().isEmpty()) {
                continue;
            }
            index.computeIfAbsent(sortedCopy(guideline.getTagsList()), key -> new ArrayList<>()).add(guideline);
        }
        guidelinesByTags = index;
        log.info("Loaded {} NCCN guideline records from {}", lstGuideLinesDetails.size(), guidelinesDataPath);
    }

    @Override
    public List<GuideLinesDetails> findAll() {
        return lstGuideLinesDetails;
    }

    /**
     * Returns the guidelines whose tag combination exactly equals one of the given tag groups,
     * mirroring the original MongoDB query {@code Criteria.where("tagsList").in(tagGroups)}.
     * Each guideline appears at most once.
     */
    @Override
    public List<GuideLinesDetails> getAllTheGuidelines(List<List<String>> tags) {
        log.info("Query to get all the guidelines");
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        int limit = Math.min(tags.size(), MAX_TAG_GROUPS);
        Set<GuideLinesDetails> matched = new LinkedHashSet<>();
        Set<List<String>> seenGroups = new HashSet<>();
        for (List<String> tagGroup : tags.subList(0, limit)) {
            List<String> key = sortedCopy(tagGroup);
            if (seenGroups.add(key)) {
                matched.addAll(guidelinesByTags.getOrDefault(key, Collections.emptyList()));
            }
        }
        return new ArrayList<>(matched);
    }

    private static List<String> sortedCopy(List<String> tags) {
        List<String> sorted = new ArrayList<>(tags);
        Collections.sort(sorted);
        return sorted;
    }

    private List<GuideLinesDetails> loadDataFromJson(String location) {
        Resource resource = resourceLoader.getResource(location);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, GuideLinesDetails.class));
        } catch (IOException e) {
            log.error("Unable to load NCCN guideline data from {}", location, e);
            throw new IllegalStateException("Unable to load NCCN guideline data from " + location, e);
        }
    }
}
