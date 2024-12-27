package com.genomebrain.backend.service.dto;

import java.util.List;

public class CustomQuestions {

    private String categoryName;
    private List<ValueLabelDTO> questionAnswers;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ValueLabelDTO> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<ValueLabelDTO> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}
