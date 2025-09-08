package com.genomebrain.backend.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genomebrain.backend.domain.GuideLinesDetails;
import com.genomebrain.backend.repository.custom.CustomGuideLinesDetailsRepository;

@Repository
public class GuideLinesDetailsRepositoryImpl implements CustomGuideLinesDetailsRepository {

    private final Logger log = LoggerFactory.getLogger(GuideLinesDetailsRepositoryImpl.class);

    private String guidelinesDataFilePath = "/home/appuser/risk_model_data/nccn_guide_lines_details.json";

    private final List<GuideLinesDetails> lstGuideLinesDetails;

    public GuideLinesDetailsRepositoryImpl() {
        lstGuideLinesDetails = findAll();
    }

    public List<GuideLinesDetails> findAll() {
        List<GuideLinesDetails> details = new ArrayList<>();
        log.info("Fetching all guidelines data from JSON file");
        try {
            details = loadDataFromJson(guidelinesDataFilePath);
        } catch (Throwable f) {
            log.warn("Fetching all guidelines data from JSON file has failed " + f);
        }
        return details;
    }

    @Override
    public List<GuideLinesDetails> getAllTheGuidelines(List<List<String>> tags) {
        log.info("Query to get all the guidelines");
        List<GuideLinesDetails> guidelines = lstGuideLinesDetails;
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        Integer limit = tags.size();
        if (limit >= 100000) {
            limit = 100000;
        }
        try {
            List<GuideLinesDetails> filteredGuidelines = new ArrayList<>();
            for (GuideLinesDetails guideline : guidelines) {
                for (List<String> tagGroup : tags.subList(0, limit)) {
                    if (guideline.getTagsList().stream().allMatch(tagGroup::contains)) {
                        filteredGuidelines.add(guideline);
                    }
                }
            }
            return filteredGuidelines;
        } catch (Exception e) {
            e.printStackTrace();
            return guidelines;
        }
    }

    private List<GuideLinesDetails> loadDataFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            File file = ResourceUtils.getFile(filePath);
            return objectMapper.readValue(file,
                objectMapper.getTypeFactory().constructCollectionType(List.class, GuideLinesDetails.class));
        } catch (IOException e) {
            log.error("Error reading JSON file from path: {}", filePath, e);
            throw new RuntimeException("Error reading JSON file", e);
        } catch (Exception e) {
            log.error("Error reading JSON file from path: {}", filePath, e);
            throw new RuntimeException("Error reading JSON file", e);
        }
    }
}
