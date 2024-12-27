package com.genomebrain.backend.service.dto;

/**
 * Class is used to get/set the fields of GleasonScoreObject
 */
public class GleasonScoreObject {

    String gleasonScoreValue;
    String answerKey;

    public String getGleasonScoreValue() {
        return gleasonScoreValue;
    }

    public void setGleasonScoreValue(String gleasonScoreValue) {
        this.gleasonScoreValue = gleasonScoreValue;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
}
