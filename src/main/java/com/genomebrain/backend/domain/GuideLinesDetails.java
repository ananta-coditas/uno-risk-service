package com.genomebrain.backend.domain;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * A GuideLinesDetails.
 */
public class GuideLinesDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @JsonProperty("tags")
    private List<String> tagsList;

    @JsonProperty("tag_id")
    private String tagsId;

    @JsonProperty("guideline_name")
    private String guidelineName;

    @JsonProperty("reasoning_statement_en")
    private String reasoningStatementEnglish;

    @JsonProperty("reasoning_statement_id")
    private String reasoningStatementId;

    @JsonProperty("associated_genes")
    private List<String> associatedGenes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public String getGuidelineName() {
        return guidelineName;
    }

    public void setGuidelineName(String guidelineName) {
        this.guidelineName = guidelineName;
    }

    public String getReasoningStatementEnglish() {
        return reasoningStatementEnglish;
    }

    public void setReasoningStatementEnglish(String reasoningStatementEnglish) {
        this.reasoningStatementEnglish = reasoningStatementEnglish;
    }

    public List<String> getAssociatedGenes() {
        return associatedGenes;
    }

    public void setAssociatedGenes(List<String> associatedGenes) {
        this.associatedGenes = associatedGenes;
    }

    public String getTagsId() {
        return tagsId;
    }

    public void setTagsId(String tagsId) {
        this.tagsId = tagsId;
    }

    public String getReasoningStatementId() {
        return reasoningStatementId;
    }

    public void setReasoningStatementId(String reasoningStatementId) {
        this.reasoningStatementId = reasoningStatementId;
    }

    @Override
    public String toString() {
        return "GuideLinesDetails{" +
            "id='" + id + '\'' +
            ", tagsList=" + tagsList +
            ", tagsId='" + tagsId + '\'' +
            ", guidelineName='" + guidelineName + '\'' +
            ", reasoningStatementEnglish='" + reasoningStatementEnglish + '\'' +
            ", reasoningStatementId='" + reasoningStatementId + '\'' +
            ", associatedGenes=" + associatedGenes +
            '}';
    }
}
