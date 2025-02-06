package com.genomebrain.backend.service.dto;

import java.util.Set;

public class NCCNCoreResponse {
    private String portalOrderId;
    private String portalPatientId;
    private String careAssessmentVersion;
    private String hbocVersion;
    private String crcVersion;
    private String hbocOverall;
    private Set<String> hbocCriteriaMet;
    private Set<String> hbocCriteriaMetIds;
    private String lynchOverall;
    private Set<String> lynchCriteriaMet;
    private Set<String> lynchCriteriaMetIds;
    private String apsApcOverall;
    private Set<String> apsApcCriteriaMet;
    private Set<String> apsApcCriteriaMetIds;
    private String dateAssessed;

    public String getPortalOrderId() {
        return portalOrderId;
    }

    public void setPortalOrderId(String portalOrderId) {
        this.portalOrderId = portalOrderId;
    }

    public String getPortalPatientId() {
        return portalPatientId;
    }

    public void setPortalPatientId(String portalPatientId) {
        this.portalPatientId = portalPatientId;
    }

    public String getCareAssessmentVersion() {
        return careAssessmentVersion;
    }

    public void setCareAssessmentVersion(String careAssessmentVersion) {
        this.careAssessmentVersion = careAssessmentVersion;
    }

    public String getHbocVersion() {
        return hbocVersion;
    }

    public void setHbocVersion(String hbocVersion) {
        this.hbocVersion = hbocVersion;
    }

    public String getCrcVersion() {
        return crcVersion;
    }

    public void setCrcVersion(String crcVersion) {
        this.crcVersion = crcVersion;
    }

    public String getHbocOverall() {
        return hbocOverall;
    }

    public void setHbocOverall(String hbocOverall) {
        this.hbocOverall = hbocOverall;
    }

    public Set<String> getHbocCriteriaMet() {
        return hbocCriteriaMet;
    }

    public void setHbocCriteriaMet(Set<String> hbocCriteriaMet) {
        this.hbocCriteriaMet = hbocCriteriaMet;
    }

    public String getLynchOverall() {
        return lynchOverall;
    }

    public void setLynchOverall(String lynchOverall) {
        this.lynchOverall = lynchOverall;
    }

    public Set<String> getLynchCriteriaMet() {
        return lynchCriteriaMet;
    }

    public void setLynchCriteriaMet(Set<String> lynchCriteriaMet) {
        this.lynchCriteriaMet = lynchCriteriaMet;
    }

    public String getApsApcOverall() {
        return apsApcOverall;
    }

    public void setApsApcOverall(String apsApcOverall) {
        this.apsApcOverall = apsApcOverall;
    }

    public Set<String> getApsApcCriteriaMet() {
        return apsApcCriteriaMet;
    }

    public void setApsApcCriteriaMet(Set<String> apsApcCriteriaMet) {
        this.apsApcCriteriaMet = apsApcCriteriaMet;
    }

    public String getDateAssessed() {
        return dateAssessed;
    }

    public void setDateAssessed(String dateAssessed) {
        this.dateAssessed = dateAssessed;
    }

    public Set<String> getHbocCriteriaMetIds() {
        return hbocCriteriaMetIds;
    }

    public void setHbocCriteriaMetIds(Set<String> hbocCriteriaMetIds) {
        this.hbocCriteriaMetIds = hbocCriteriaMetIds;
    }

    public Set<String> getLynchCriteriaMetIds() {
        return lynchCriteriaMetIds;
    }

    public void setLynchCriteriaMetIds(Set<String> lynchCriteriaMetIds) {
        this.lynchCriteriaMetIds = lynchCriteriaMetIds;
    }

    public Set<String> getApsApcCriteriaMetIds() {
        return apsApcCriteriaMetIds;
    }

    public void setApsApcCriteriaMetIds(Set<String> apsApcCriteriaMetIds) {
        this.apsApcCriteriaMetIds = apsApcCriteriaMetIds;
    }

    @Override
    public String toString() {
        return "CAREAPIResponseDTO{" +
                "portalOrderId='" + portalOrderId + '\'' +
                ", portalPatientId='" + portalPatientId + '\'' +
                ", careAssessmentVersion='" + careAssessmentVersion + '\'' +
                ", hbocVersion='" + hbocVersion + '\'' +
                ", crcVersion='" + crcVersion + '\'' +
                ", hbocOverall='" + hbocOverall + '\'' +
                ", hbocCriteriaMet=" + hbocCriteriaMet +
                ", hbocCriteriaMetIds=" + hbocCriteriaMetIds +
                ", lynchOverall='" + lynchOverall + '\'' +
                ", lynchCriteriaMet=" + lynchCriteriaMet +
                ", lynchCriteriaMetIds=" + lynchCriteriaMetIds +
                ", apsApcOverall='" + apsApcOverall + '\'' +
                ", apsApcCriteriaMet=" + apsApcCriteriaMet +
                ", apsApcCriteriaMetIds=" + apsApcCriteriaMetIds +
                ", dateAssessed='" + dateAssessed + '\'' +
                '}';
    }
}
