package com.genomebrain.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class is used to get and set
 * the fields of WHQuestionAnswerRequestDTO
 * for question answer of WH
 */
public class WHQuestionAnswerRequestDTO {
    @JsonProperty("QuestionText")
    String questionText;

    @JsonProperty("QuestionKey")
    String questionKey;

    @JsonProperty("AnswerText")
    String answerText;

    @JsonProperty("AnswerKey")
    String answerKey;

    @JsonProperty("PreviewLabel")
    String previewLabel;

    @JsonProperty("ShowMore")
    String showMore;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

	public String getPreviewLabel() {
		return previewLabel;
	}

	public void setPreviewLabel(String previewLabel) {
		this.previewLabel = previewLabel;
	}

	public String getShowMore() {
		return showMore;
	}

	public void setShowMore(String showMore) {
		this.showMore = showMore;
	}

}
