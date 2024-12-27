package com.genomebrain.backend.service.dto;

import java.util.HashSet;
import java.util.Set;

public class FamilyHistoryDetailsTagsDTO {

    Set<String> tags;
    Set<String> lynchReasoningStatements = new HashSet<>();
    Set<String> hbocReasoningStatements = new HashSet<>();
    FamilyHistoryDetails familyHistoryDetails;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public FamilyHistoryDetails getFamilyHistoryDetails() {
        return familyHistoryDetails;
    }

    public void setFamilyHistoryDetails(FamilyHistoryDetails familyHistoryDetails) {
        this.familyHistoryDetails = familyHistoryDetails;
    }

    public Set<String> getLynchReasoningStatements() {
        return lynchReasoningStatements;
    }

    public void setLynchReasoningStatements(Set<String> lynchReasoningStatements) {
        this.lynchReasoningStatements = lynchReasoningStatements;
    }

    public Set<String> getHbocReasoningStatements() {
        return hbocReasoningStatements;
    }

    public void setHbocReasoningStatements(Set<String> hbocReasoningStatements) {
        this.hbocReasoningStatements = hbocReasoningStatements;
    }
}
