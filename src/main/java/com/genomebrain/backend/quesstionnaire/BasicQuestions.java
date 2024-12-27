package com.genomebrain.backend.quesstionnaire;

import java.util.List;

public class BasicQuestions {

    List<QuestionSet> Relatives;

    public List<QuestionSet> getRelatives() {
        return Relatives;
    }

    public void setRelatives(List<QuestionSet> relatives) {
        Relatives = relatives;
    }

    @Override
    public String toString() {
        return "BasicQuestions{" +
                "Relatives=" + Relatives +
                '}';
    }
}
