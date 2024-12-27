package com.genomebrain.backend.service.dto;

import java.util.Set;

public class ReasoningStatementObject {
    private Set<String> reasoningStatementEn;
    private Set<ReasoningStatementAndTags> reasoningStatementAndTags;

    public Set<String> getReasoningStatementEn() {
        return reasoningStatementEn;
    }

    public void setReasoningStatementEn(Set<String> reasoningStatementEn) {
        this.reasoningStatementEn = reasoningStatementEn;
    }

    public Set<ReasoningStatementAndTags> getReasoningStatementAndTags() {
        return reasoningStatementAndTags;
    }

    public void setReasoningStatementAndTags(Set<ReasoningStatementAndTags> reasoningStatementAndTags) {
        this.reasoningStatementAndTags = reasoningStatementAndTags;
    }
}
