package com.genomebrain.backend.quesstionnaire;

import java.util.List;

import com.genomebrain.backend.service.dto.FamilyHistory;

public class Questionnaire {

    String assessmentId;
    //Q.How old are you?
    //Q.What was your sex at birth?
    //Q.What is your ethnic ancestry
    List<QuestionSet> demographic;

    //Personal history related questions
    PersonalHistory personalHistory;

    //Family history related questions
    FamilyHistory familyCancer;

    //Tyrer cusick questions
    List<QuestionSet> tyrerCuzick;

    List<QuestionSet> customCategories;

    String source = "Default";

    AdditionalDetails additionalDetails;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public List<QuestionSet> getDemographic() {
        return demographic;
    }

    public void setDemographic(List<QuestionSet> demographic) {
        this.demographic = demographic;
    }

    public PersonalHistory getPersonalHistory() {
        return personalHistory;
    }

    public void setPersonalHistory(PersonalHistory personalHistory) {
        this.personalHistory = personalHistory;
    }

    public FamilyHistory getFamilyCancer() {
        return familyCancer;
    }

    public void setFamilyCancer(FamilyHistory familyCancer) {
        this.familyCancer = familyCancer;
    }

    public List<QuestionSet> getTyrerCuzick() {
        return tyrerCuzick;
    }

    public void setTyrerCuzick(List<QuestionSet> tyrerCuzick) {
        this.tyrerCuzick = tyrerCuzick;
    }


    public List<QuestionSet> getCustomCategories() {
        return customCategories;
    }

    public void setCustomCategories(List<QuestionSet> customCategories) {
        this.customCategories = customCategories;
    }
    public AdditionalDetails getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(AdditionalDetails additionalDetails) {
        this.additionalDetails = additionalDetails;
    }


    @Override
    public String toString() {
        return "Questionnaire{" +
                "assessmentId='" + assessmentId + '\'' +
                ", demographic=" + demographic +
                ", personalHistory=" + personalHistory +
                ", familyCancer=" + familyCancer +
                ", tyrerCuzick=" + tyrerCuzick +
                ", customCategories=" + customCategories +
                "additionalDetails=" + additionalDetails +
                '}';
    }
}

