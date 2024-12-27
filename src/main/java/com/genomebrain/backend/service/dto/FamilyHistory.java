package com.genomebrain.backend.service.dto;

import java.util.List;

import com.genomebrain.backend.quesstionnaire.FamilyCancerDetails;
import com.genomebrain.backend.quesstionnaire.QuestionSet;

public class FamilyHistory {

    //check if family history
    private List<QuestionSet> basicQuestions;

    //Cancer Specific questions
    private FamilyCancerDetails familyCancerDetails;

    public List<QuestionSet> getBasicQuestions() {
        return basicQuestions;
    }

    public void setBasicQuestions(List<QuestionSet> basicQuestions) {
        this.basicQuestions = basicQuestions;
    }

    public FamilyCancerDetails getFamilyCancerDetails() {
        return familyCancerDetails;
    }

    public void setFamilyCancerDetails(FamilyCancerDetails familyCancerDetails) {
        this.familyCancerDetails = familyCancerDetails;
    }

    @Override
    public String toString() {
        return "FamilyHistory{" +
            "basicQuestions=" + basicQuestions +
            ", familyCancerDetails=" + familyCancerDetails +
            '}';
    }
}
