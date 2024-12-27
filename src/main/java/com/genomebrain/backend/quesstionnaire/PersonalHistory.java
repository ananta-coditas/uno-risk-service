package com.genomebrain.backend.quesstionnaire;

import java.util.List;

public class PersonalHistory {

    //check if personal history
    private List<QuestionSet> basicQuestions;

    //Cancer Specific questions
    private CancerDetails personalCancerDetails;

    public List<QuestionSet> getBasicQuestions() {
        return basicQuestions;
    }

    public void setBasicQuestions(List<QuestionSet> basicQuestions) {
        this.basicQuestions = basicQuestions;
    }

    public CancerDetails getPersonalCancerDetails() {
        return personalCancerDetails;
    }

    public void setPersonalCancerDetails(CancerDetails personalCancerDetails) {
        this.personalCancerDetails = personalCancerDetails;
    }

    @Override
    public String toString() {
        return "PersonalHistory{" +
            "basicQuestions=" + basicQuestions +
            ", personalCancerDetails=" + personalCancerDetails +
            '}';
    }
}
