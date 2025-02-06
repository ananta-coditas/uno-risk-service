package com.genomebrain.backend.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NCCNCoreRequest {

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
