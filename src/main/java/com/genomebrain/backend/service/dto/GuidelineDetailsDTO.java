package com.genomebrain.backend.service.dto;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GuidelineDetailsDTO {

    ClinicalSummary clinicalSummary;
    TyrerCuzikCriteriaDTO tyrerCuzikCriteriaDTO;
    Set<String> tagList;
    boolean isFamilyHistory;
    boolean isPersonalHistory;
    boolean isHBOC;
    boolean isLYNCH;
    boolean isAPS;
    boolean isGastric;
    String reasoningStatementAndTagsSet;

    public ClinicalSummary getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(ClinicalSummary clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public TyrerCuzikCriteriaDTO getTyrerCuzikCriteriaDTO() {
        return tyrerCuzikCriteriaDTO;
    }

    public void setTyrerCuzikCriteriaDTO(TyrerCuzikCriteriaDTO tyrerCuzikCriteriaDTO) {
        this.tyrerCuzikCriteriaDTO = tyrerCuzikCriteriaDTO;
    }

    public Set<String> getTagList() {
        return tagList;
    }

    public void setTagList(Set<String> tagList) {
        this.tagList = tagList;
    }

    public boolean isFamilyHistory() {
        return isFamilyHistory;
    }

    public void setFamilyHistory(boolean familyHistory) {
        isFamilyHistory = familyHistory;
    }

    public boolean isPersonalHistory() {
        return isPersonalHistory;
    }

    public void setPersonalHistory(boolean personalHistory) {
        isPersonalHistory = personalHistory;
    }

    public boolean isHBOC() {
        return isHBOC;
    }

    public void setHBOC(boolean HBOC) {
        isHBOC = HBOC;
    }

    public boolean isLYNCH() {
        return isLYNCH;
    }

    public void setLYNCH(boolean LYNCH) {
        isLYNCH = LYNCH;
    }

    public boolean isAPS() {
        return isAPS;
    }

    public void setAPS(boolean APS) {
        isAPS = APS;
    }

    public boolean isGastric() {
        return isGastric;
    }

    public void setGastric(boolean gastric) {
        isGastric = gastric;
    }

    public String getReasoningStatementAndTagsSet() {
        return reasoningStatementAndTagsSet;
    }

    public void setReasoningStatementAndTagsSet(String reasoningStatementAndTagsSet) {
        this.reasoningStatementAndTagsSet = reasoningStatementAndTagsSet;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clinicalSummary", clinicalSummary)
                .append("tyrerCuzikCriteriaDTO", tyrerCuzikCriteriaDTO)
                .append("tagList", tagList)
                .append("isFamilyHistory", isFamilyHistory)
                .append("isPersonalHistory", isPersonalHistory)
                .append("isHBOC", isHBOC)
                .append("isLYNCH", isLYNCH)
                .append("isAPS", isAPS)
                .append("reasoningStatementAndTagsSet",reasoningStatementAndTagsSet)
                .toString();
    }
}
