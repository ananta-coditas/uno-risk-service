package com.genomebrain.backend.service.dto;

import java.time.Instant;
import java.util.List;

public class TestOrderDetails {

    private Long assessmentId;

    private String testOrderId;

    private String patientName;

    private String dateOfBirth;

    private List<String> testCodes;

    private String orderingProviderName;

    private String orderedOn;

    private String orderingPerson;

    private String testName;

    private List<String> primaryTestName;

    private List<String> secondaryTestName;

    private boolean isPWN;

    private String orderingProviderNpi;

    private String dateOfAuthorization;

    private String specimenCollectionDateWarningMessage;

    private List<String> diagnosisCodes;

    public String getSpecimenCollectionDateWarningMessage() {
        return specimenCollectionDateWarningMessage;
    }

    public void setSpecimenCollectionDateWarningMessage(String specimenCollectionDateWarningMessage) {
        this.specimenCollectionDateWarningMessage = specimenCollectionDateWarningMessage;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTestOrderId() {
        return testOrderId;
    }

    public void setTestOrderId(String testOrderId) {
        this.testOrderId = testOrderId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getTestCodes() {
        return testCodes;
    }

    public void setTestCodes(List<String> testCodes) {
        this.testCodes = testCodes;
    }

    public String getOrderingProviderName() {
        return orderingProviderName;
    }

    public void setOrderingProviderName(String orderingProviderName) {
        this.orderingProviderName = orderingProviderName;
    }

    public String getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(String orderedOn) {
        this.orderedOn = orderedOn;
    }

    public String getOrderingPerson() {
        return orderingPerson;
    }

    public void setOrderingPerson(String orderingPerson) {
        this.orderingPerson = orderingPerson;
    }

    public List<String> getPrimaryTestName() {
        return primaryTestName;
    }

    public void setPrimaryTestName(List<String> primaryTestName) {
        this.primaryTestName = primaryTestName;
    }

    public List<String> getSecondaryTestName() {
        return secondaryTestName;
    }

    public void setSecondaryTestName(List<String> secondaryTestName) {
        this.secondaryTestName = secondaryTestName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public boolean isPWN() {
        return isPWN;
    }

    public void setPWN(boolean PWN) {
        isPWN = PWN;
    }

    public String getOrderingProviderNpi() {
        return orderingProviderNpi;
    }

    public void setOrderingProviderNpi(String orderingProviderNpi) {
        this.orderingProviderNpi = orderingProviderNpi;
    }

    public String getDateOfAuthorization() {
        return dateOfAuthorization;
    }

    public void setDateOfAuthorization(String dateOfAuthorization) {
        this.dateOfAuthorization = dateOfAuthorization;
    }

    public List<String> getDiagnosisCodes() {
        return diagnosisCodes;
    }

    public void setDiagnosisCodes(List<String> diagnosisCodes) {
        this.diagnosisCodes = diagnosisCodes;
    }

    @Override
    public String toString() {
        return "TestOrderDetails{" +
                "assessmentId=" + assessmentId +
                ", testOrderId='" + testOrderId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", testCodes=" + testCodes +
                ", orderingProviderName='" + orderingProviderName + '\'' +
                ", orderedOn='" + orderedOn + '\'' +
                ", orderingPerson='" + orderingPerson + '\'' +
                ", testName='" + testName + '\'' +
                ", primaryTestName=" + primaryTestName +
                ", secondaryTestName=" + secondaryTestName +
                ", isPWN=" + isPWN +
                ", orderingProviderNpi=" + orderingProviderNpi +
                ", dateOfAuthorization=" + dateOfAuthorization +
                '}';
    }
}
