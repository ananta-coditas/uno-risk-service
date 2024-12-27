package com.genomebrain.backend.service.dto;

import java.util.List;
import java.util.Set;

public class PreviousRiskAssessmentDetails {

    private String dateOfAssessment;
    private boolean nccnCriteriaMet;
    private String tcScore;
    private List<String> reasoningStatements;
    private String nccnVersion;
    private Set<String> riskAssessmentNccnForHBOC;

    private Set<String> riskAssessmentNccnForLYNCH;

    private Set<String> riskAssessmentNccnForAPS;

    private TyrerCuzickCSDTO.TCDetails tcDetails;

    private boolean isBreastDensityEnabled;

    public String getDateOfAssessment() {
        return dateOfAssessment;
    }

    public void setDateOfAssessment(String dateOfAssessment) {
        this.dateOfAssessment = dateOfAssessment;
    }

    public boolean isNccnCriteriaMet() {
        return nccnCriteriaMet;
    }

    public void setNccnCriteriaMet(boolean nccnCriteriaMet) {
        this.nccnCriteriaMet = nccnCriteriaMet;
    }

    public String getTcScore() {
        return tcScore;
    }

    public void setTcScore(String tcScore) {
        this.tcScore = tcScore;
    }

    public List<String> getReasoningStatements() {
        return reasoningStatements;
    }

    public void setReasoningStatements(List<String> reasoningStatements) {
        this.reasoningStatements = reasoningStatements;
    }

    public String getNccnVersion() {
        return nccnVersion;
    }

    public void setNccnVersion(String nccnVersion) {
        this.nccnVersion = nccnVersion;
    }

    public Set<String> getRiskAssessmentNccnForHBOC() {
        return riskAssessmentNccnForHBOC;
    }

    public void setRiskAssessmentNccnForHBOC(Set<String> riskAssessmentNccnForHBOC) {
        this.riskAssessmentNccnForHBOC = riskAssessmentNccnForHBOC;
    }

    public Set<String> getRiskAssessmentNccnForLYNCH() {
        return riskAssessmentNccnForLYNCH;
    }

    public void setRiskAssessmentNccnForLYNCH(Set<String> riskAssessmentNccnForLYNCH) {
        this.riskAssessmentNccnForLYNCH = riskAssessmentNccnForLYNCH;
    }

    public Set<String> getRiskAssessmentNccnForAPS() {
        return riskAssessmentNccnForAPS;
    }

    public void setRiskAssessmentNccnForAPS(Set<String> riskAssessmentNccnForAPS) {
        this.riskAssessmentNccnForAPS = riskAssessmentNccnForAPS;
    }

    public TyrerCuzickCSDTO.TCDetails getTcDetails() {
        return tcDetails;
    }

    public void setTcDetails(TyrerCuzickCSDTO.TCDetails tcDetails) {
        this.tcDetails = tcDetails;
    }

    public boolean isBreastDensityEnabled() {
        return isBreastDensityEnabled;
    }

    public void setBreastDensityEnabled(boolean breastDensityEnabled) {
        isBreastDensityEnabled = breastDensityEnabled;
    }
}
