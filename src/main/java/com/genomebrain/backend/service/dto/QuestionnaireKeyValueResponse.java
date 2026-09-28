package com.genomebrain.backend.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.genomebrain.backend.quesstionnaire.AdditionalDetails;

import java.util.Map;

public class QuestionnaireKeyValueResponse {
    private Long assessmentId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> demographic;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> personalHistory;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> tyrerCuzick;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> customCategories;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> familyCancer;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("AdditionalDetails")
    private AdditionalDetails additionalDetails;

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Map<String, Object> getDemographic() {
        return demographic;
    }

    public void setDemographic(Map<String, Object> demographic) {
        this.demographic = demographic;
    }

    public Map<String, Object> getPersonalHistory() {
        return personalHistory;
    }

    public void setPersonalHistory(Map<String, Object> personalHistory) {
        this.personalHistory = personalHistory;
    }

    public Map<String, Object> getTyrerCuzick() {
        return tyrerCuzick;
    }

    public void setTyrerCuzick(Map<String, Object> tyrerCuzick) {
        this.tyrerCuzick = tyrerCuzick;
    }

    public Map<String, Object> getCustomCategories() {
        return customCategories;
    }

    public void setCustomCategories(Map<String, Object> customCategories) {
        this.customCategories = customCategories;
    }

    public Map<String, Object> getFamilyCancer() {
        return familyCancer;
    }

    public void setFamilyCancer(Map<String, Object> familyCancer) {
        this.familyCancer = familyCancer;
    }

    public AdditionalDetails getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(AdditionalDetails additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}


