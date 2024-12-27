package com.genomebrain.backend.service.dto;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GenomeBrainResponseDTO {

    String status;
    String message;
    boolean isHBOC;
    boolean isLYNCH;
    boolean isAPS;
    boolean isGastric;
    boolean isGuideLineMet;
    Set<String> reasoningStatementsForHBOC;
    Set<String> reasoningStatementsForLYNCH;
    Set<String> reasoningStatementsForAPS;
    Set<ReasoningStatementAndTags> hbocReasoningStatementAndTags;
    Set<ReasoningStatementAndTags> lynchReasoningStatementAndTags;
    Set<ReasoningStatementAndTags> apsReasoningStatementAndTags;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public boolean isGastric() {
        return isGastric;
    }

    public void setGastric(boolean gastric) {
        isGastric = gastric;
    }

    public boolean isGuideLineMet() {
        return isGuideLineMet;
    }

    public void setGuideLineMet(boolean guideLineMet) {
        isGuideLineMet = guideLineMet;
    }

    public Set<String> getReasoningStatementsForHBOC() {
        return reasoningStatementsForHBOC;
    }

    public void setReasoningStatementsForHBOC(Set<String> reasoningStatementsForHBOC) {
        this.reasoningStatementsForHBOC = reasoningStatementsForHBOC;
    }

    public Set<String> getReasoningStatementsForLYNCH() {
        return reasoningStatementsForLYNCH;
    }

    public void setReasoningStatementsForLYNCH(Set<String> reasoningStatementsForLYNCH) {
        this.reasoningStatementsForLYNCH = reasoningStatementsForLYNCH;
    }

    public Set<String> getReasoningStatementsForAPS() {
        return reasoningStatementsForAPS;
    }

    public void setReasoningStatementsForAPS(Set<String> reasoningStatementsForAPS) {
        this.reasoningStatementsForAPS = reasoningStatementsForAPS;
    }

    public boolean isAPS() {
        return isAPS;
    }

    public void setAPS(boolean APS) {
        isAPS = APS;
    }

    public Set<ReasoningStatementAndTags> getHbocReasoningStatementAndTags() {
        return hbocReasoningStatementAndTags;
    }

    public void setHbocReasoningStatementAndTags(Set<ReasoningStatementAndTags> hbocReasoningStatementAndTags) {
        this.hbocReasoningStatementAndTags = hbocReasoningStatementAndTags;
    }

    public Set<ReasoningStatementAndTags> getLynchReasoningStatementAndTags() {
        return lynchReasoningStatementAndTags;
    }

    public void setLynchReasoningStatementAndTags(Set<ReasoningStatementAndTags> lynchReasoningStatementAndTags) {
        this.lynchReasoningStatementAndTags = lynchReasoningStatementAndTags;
    }

    public Set<ReasoningStatementAndTags> getApsReasoningStatementAndTags() {
        return apsReasoningStatementAndTags;
    }

    public void setApsReasoningStatementAndTags(Set<ReasoningStatementAndTags> apsReasoningStatementAndTags) {
        this.apsReasoningStatementAndTags = apsReasoningStatementAndTags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("message", message)
                .append("isHBOC", isHBOC)
                .append("isLYNCH", isLYNCH)
                .append("isAPS", isAPS)
                .append("isGuideLineMet", isGuideLineMet)
                .append("reasoningStatementsForHBOC", reasoningStatementsForHBOC)
                .append("reasoningStatementsForLYNCH", reasoningStatementsForLYNCH)
                .append("reasoningStatementsForAPS", reasoningStatementsForAPS)
                .toString();
    }
}
