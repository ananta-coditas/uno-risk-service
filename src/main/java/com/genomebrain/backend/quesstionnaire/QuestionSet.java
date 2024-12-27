package com.genomebrain.backend.quesstionnaire;

public class QuestionSet {

    private String QuestionText;
    private String QuestionKey;
    private String AnswerText;
    private String AnswerKey;
    private String VartagName;
    private String category;
    private String previewLabel;
    private String previewLabel_ES;

    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        this.QuestionText = questionText;
    }

    public String getQuestionKey() {
        return QuestionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.QuestionKey = questionKey;
    }

    public String getAnswerText() {
        return AnswerText;
    }

    public void setAnswerText(String answerText) {
        this.AnswerText = answerText;
    }

    public String getAnswerKey() {
        return AnswerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.AnswerKey = answerKey;
    }

    public String getVartagName() {
        return VartagName;
    }

    public void setVartagName(String vartagName) {
        this.VartagName = vartagName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPreviewLabel() {
        return previewLabel;
    }

    public void setPreviewLabel(String previewLabel) {
        this.previewLabel = previewLabel;
    }

    public String getPreviewLabel_ES() {
        return previewLabel_ES;
    }

    public void setPreviewLabel_ES(String previewLabel_ES) {
        this.previewLabel_ES = previewLabel_ES;
    }
}
