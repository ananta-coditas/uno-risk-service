package com.genomebrain.backend.service.dto;

import java.util.Set;

public class ReasoningStatementAndTags {

    String reasoningStatementId;
    Set<String> tagIds;

    public String getReasoningStatementId() {
        return reasoningStatementId;
    }

    public void setReasoningStatementId(String reasoningStatementId) {
        this.reasoningStatementId = reasoningStatementId;
    }

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
    }
}
