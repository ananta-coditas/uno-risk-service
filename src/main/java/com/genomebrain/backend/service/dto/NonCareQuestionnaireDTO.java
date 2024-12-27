package com.genomebrain.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NonCareQuestionnaireDTO {

     @JsonProperty("clinical_history")
     private List<ClinicalHistory> clinicalHistory;

    public List<ClinicalHistory> getClinicalHistory() {
        return clinicalHistory;
    }

    public void setClinicalHistory(List<ClinicalHistory> clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    @Override
    public String toString() {
        return "NonCareQuestionnaireDTO{" +
               ", clinicalHistory=" + clinicalHistory +
                '}';
    }
}
