package com.genomebrain.backend.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClinicalSummary {

    private String clinicalSummaryVersion;

    private Boolean relativeVariantOutsideCancerNextPanel;

    private String familyHistoryOfGeneticTesting;

    private String program = "hc";

    private boolean isOldVersion;

    private String logo;

    private String pdfName;

    private String clientName;

    private String address;

    private String option;

    private String account;

    private String organisationCode;

    private String patientId;

    private String languageKey;

    private boolean nccnCriteriaMet;

    private boolean accountLevelTCActive;

    private String tyrerCuzickScore;

    private boolean isHeme;

    private String tyrerCuzickScoreInPercentage;

    private Demographics demographics;

    //this is needed only to get the optionKey for Clinic_Summary
    boolean isTyrerCusicCalculated;

    boolean isExcludedDueToBRCA;

    boolean isExcludedDueToBRCAMutation;

    boolean isExcludedDueToAge;

    boolean isPrDesmoid;

    boolean isPrPolyps;

    boolean isPersonalFamilyHxChanges;

    private String nccnVersion;

    private String lynchNccnVersion;

    private String csDisclaimerContent;

    boolean isPrAdPolyps;

    boolean isPrPolypsCountGTTen;

    private String amendedContent ;

    public String getAmendedContent() {
        return amendedContent;
    }

    public void setAmendedContent(String amendedContent) {
        this.amendedContent = amendedContent;
    }

    private PersonalHistoryDetails personalHistoryDetails;

    private List<FamilyHistoryDetails> familyHistoryDetailsList;

    private List<FamilyHistoryDetails> familyHistoryForFDR;

    private List<FamilyHistoryDetails> familyHistoryForMaternalRelatives;

    private List<FamilyHistoryDetails> familyHistoryForPaternalRelatives;

    private String familyHistoryOfColorectalPolyps;

    private List<String> relativeWithPolypsHistory;

    private Set<String> riskAssessmentNccn;

    private boolean isHBOC;

    private boolean isLYNCH;

    private Set<String> riskAssessmentNccnForHBOC;

    private Set<String> riskAssessmentNccnForLYNCH;

    private Set<String> riskAssessmentNccnForAPS;

    private Set<String> riskAssessmentNccnForGastric;

    private TestOrderDetails testOrder;

    private boolean showTCScoreHeader;

    private Long accountId;

    private boolean assessmentUpdated;

    private String assessmentUpdatedInfo;

    private boolean amendedReport;

    private String tyrerCuzickScoreWithBiradA;

    private String tyrerCuzickScoreWithBiradB;

    private String tyrerCuzickScoreWithBiradC;

    private String tyrerCuzickScoreWithBiradD;

    private boolean isInsuranceEnabled;

    private boolean isInsuranceCriteriaMet;

    private boolean isPinkRibbon;

    private String insuranceType;

    private String familyHistoryOfCancer;

    private boolean isPersonalHistory;

    private boolean isFamilyHistory;

    private String pedigreeURL;

    private boolean returningPatient;

    private String previousReportDate;

    private String previousReportLink;

    private boolean previousTestingByCare;

    private Boolean showSummary = true;

    private Boolean renameNCCNSummaryHeader = false;

    private List<PreviousRiskAssessmentDetails> previousRiskAssessmentDetails;

    private List<CustomQuestions> customQuestions;

    private List<ConsentQuestion> consentQuestions;

    private String accountAliasName;

    private String externalAppointmentId;

    private boolean isBiradsEnabled;

    private boolean tcWithMortalityEnabled;

    private boolean tcWithOutMortalityEnabled;

    private TyrerCuzickCSDTO tyrerCuzickCSDTO;

    private boolean breastDensityUpdated;

    private Boolean isResultBack= Boolean.FALSE;

    private String brcaGeneInResult;

    private Boolean otherGeneInResultPositive =Boolean.FALSE;

    private Boolean enableAskTCQuestions = Boolean.FALSE;

    private Boolean hasBreastCancer = Boolean.FALSE;


    public String getClinicalSummaryVersion() {
        return clinicalSummaryVersion;
    }

    public void setClinicalSummaryVersion(String clinicalSummaryVersion) {
        this.clinicalSummaryVersion = clinicalSummaryVersion;
    }

    public Boolean isRelativeVariantOutsideCancerNextPanel() {
        return relativeVariantOutsideCancerNextPanel;
    }

    public void setRelativeVariantOutsideCancerNextPanel(Boolean relativeVariantOutsideCancerNextPanel) {
        this.relativeVariantOutsideCancerNextPanel = relativeVariantOutsideCancerNextPanel;
    }

    public String getFamilyHistoryOfGeneticTesting() {
        return familyHistoryOfGeneticTesting;
    }

    public void setFamilyHistoryOfGeneticTesting(String familyHistoryOfGeneticTesting) {
        this.familyHistoryOfGeneticTesting = familyHistoryOfGeneticTesting;
    }

    public boolean isOldVersion() {
        return isOldVersion;
    }

    public void setOldVersion(boolean oldVersion) {
        isOldVersion = oldVersion;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isNccnCriteriaMet() {
        return nccnCriteriaMet;
    }

    public void setNccnCriteriaMet(boolean nccnCriteriaMet) {
        this.nccnCriteriaMet = nccnCriteriaMet;
    }

    public String getTyrerCuzickScore() {
        return tyrerCuzickScore;
    }

    public void setTyrerCuzickScore(String tyrerCuzickScore) {
        this.tyrerCuzickScore = tyrerCuzickScore;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public List<FamilyHistoryDetails> getFamilyHistoryForFDR() {
        return familyHistoryForFDR;
    }

    public void setFamilyHistoryForFDR(List<FamilyHistoryDetails> familyHistoryForFDR) {
        this.familyHistoryForFDR = familyHistoryForFDR;
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

    public String getLynchNccnVersion() {
        return lynchNccnVersion;
    }

    public void setLynchNccnVersion(String lynchNccnVersion) {
        this.lynchNccnVersion = lynchNccnVersion;
    }

    public String getCsDisclaimerContent() {
        return csDisclaimerContent;
    }

    public void setCsDisclaimerContent(String csDisclaimerContent) {
        this.csDisclaimerContent = csDisclaimerContent;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public PersonalHistoryDetails getPersonalHistoryDetails() {
        return personalHistoryDetails;
    }

    public void setPersonalHistoryDetails(PersonalHistoryDetails personalHistoryDetails) {
        this.personalHistoryDetails = personalHistoryDetails;
    }

    public TestOrderDetails getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(TestOrderDetails testOrder) {
        this.testOrder = testOrder;
    }

    public String getTyrerCuzickScoreInPercentage() {
        return tyrerCuzickScoreInPercentage;
    }

    public void setTyrerCuzickScoreInPercentage(String tyrerCuzickScoreInPercentage) {
        this.tyrerCuzickScoreInPercentage = tyrerCuzickScoreInPercentage;
    }

    public boolean isHeme() {
        return isHeme;
    }

    public void setHeme(boolean heme) {
        isHeme = heme;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOrganisationCode() {
        return organisationCode;
    }

    public void setOrganisationCode(String organisationCode) {
        this.organisationCode = organisationCode;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFamilyHistoryOfColorectalPolyps() {
        return familyHistoryOfColorectalPolyps;
    }

    public void setFamilyHistoryOfColorectalPolyps(String familyHistoryOfColorectalPolyps) {
        this.familyHistoryOfColorectalPolyps = familyHistoryOfColorectalPolyps;
    }

    public boolean isTyrerCusicCalculated() {
        return isTyrerCusicCalculated;
    }

    public void setTyrerCusicCalculated(boolean tyrerCusicCalculated) {
        isTyrerCusicCalculated = tyrerCusicCalculated;
    }

    public boolean isExcludedDueToBRCA() {
        return isExcludedDueToBRCA;
    }

    public void setExcludedDueToBRCA(boolean excludedDueToBRCA) {
        isExcludedDueToBRCA = excludedDueToBRCA;
    }

    public boolean isExcludedDueToAge() {
        return isExcludedDueToAge;
    }

    public void setExcludedDueToAge(boolean excludedDueToAge) {
        isExcludedDueToAge = excludedDueToAge;
    }

    public boolean isAccountLevelTCActive() {
        return accountLevelTCActive;
    }

    public void setAccountLevelTCActive(boolean accountLevelTCActive) {
        this.accountLevelTCActive = accountLevelTCActive;
    }

    public boolean isShowTCScoreHeader() {
        return showTCScoreHeader;
    }

    public void setShowTCScoreHeader(boolean showTCScoreHeader) {
        this.showTCScoreHeader = showTCScoreHeader;
    }

    public boolean isExcludedDueToBRCAMutation() {
        return isExcludedDueToBRCAMutation;
    }

    public void setExcludedDueToBRCAMutation(boolean excludedDueToBRCAMutation) {
        isExcludedDueToBRCAMutation = excludedDueToBRCAMutation;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean isAssessmentUpdated() {
        return assessmentUpdated;
    }

    public void setAssessmentUpdated(boolean assessmentUpdated) {
        this.assessmentUpdated = assessmentUpdated;
    }

    public String getAssessmentUpdatedInfo() {
        return assessmentUpdatedInfo;
    }

    public void setAssessmentUpdatedInfo(String assessmentUpdatedInfo) {
        this.assessmentUpdatedInfo = assessmentUpdatedInfo;
    }

    public boolean isAmendedReport() {
        return amendedReport;
    }

    public void setAmendedReport(boolean amendedReport) {
        this.amendedReport = amendedReport;
    }

    public String getTyrerCuzickScoreWithBiradA() {
        return tyrerCuzickScoreWithBiradA;
    }

    public void setTyrerCuzickScoreWithBiradA(String tyrerCuzickScoreWithBiradA) {
        this.tyrerCuzickScoreWithBiradA = tyrerCuzickScoreWithBiradA;
    }

    public String getTyrerCuzickScoreWithBiradB() {
        return tyrerCuzickScoreWithBiradB;
    }

    public void setTyrerCuzickScoreWithBiradB(String tyrerCuzickScoreWithBiradB) {
        this.tyrerCuzickScoreWithBiradB = tyrerCuzickScoreWithBiradB;
    }

    public String getTyrerCuzickScoreWithBiradC() {
        return tyrerCuzickScoreWithBiradC;
    }

    public void setTyrerCuzickScoreWithBiradC(String tyrerCuzickScoreWithBiradC) {
        this.tyrerCuzickScoreWithBiradC = tyrerCuzickScoreWithBiradC;
    }

    public String getTyrerCuzickScoreWithBiradD() {
        return tyrerCuzickScoreWithBiradD;
    }

    public void setTyrerCuzickScoreWithBiradD(String tyrerCuzickScoreWithBiradD) {
        this.tyrerCuzickScoreWithBiradD = tyrerCuzickScoreWithBiradD;
    }

    public boolean isInsuranceEnabled() {
        return isInsuranceEnabled;
    }

    public void setInsuranceEnabled(boolean insuranceEnabled) {
        isInsuranceEnabled = insuranceEnabled;
    }

    public boolean isInsuranceCriteriaMet() {
        return isInsuranceCriteriaMet;
    }

    public void setInsuranceCriteriaMet(boolean insuranceCriteriaMet) {
        isInsuranceCriteriaMet = insuranceCriteriaMet;
    }

    public boolean isPinkRibbon() {
        return isPinkRibbon;
    }

    public void setPinkRibbon(boolean pinkRibbon) {
        isPinkRibbon = pinkRibbon;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public boolean isHBOC() {
        return isHBOC;
    }

    public void setHBOC(boolean HBOC) {
        isHBOC = HBOC;
    }

    public boolean isLYNCH() {
        return isLYNCH;
    }

    public void setLYNCH(boolean LYNCH) {
        isLYNCH = LYNCH;
    }

    public Set<String> getRiskAssessmentNccnForAPS() {
        return riskAssessmentNccnForAPS;
    }

    public void setRiskAssessmentNccnForAPS(Set<String> riskAssessmentNccnForAPS) {
        this.riskAssessmentNccnForAPS = riskAssessmentNccnForAPS;
    }

    public Set<String> getRiskAssessmentNccnForGastric() {
        return riskAssessmentNccnForGastric;
    }

    public void setRiskAssessmentNccnForGastric(Set<String> riskAssessmentNccnForGastric) {
        this.riskAssessmentNccnForGastric = riskAssessmentNccnForGastric;
    }

    public List<FamilyHistoryDetails> getFamilyHistoryForMaternalRelatives() {
        return familyHistoryForMaternalRelatives;
    }

    public void setFamilyHistoryForMaternalRelatives(List<FamilyHistoryDetails> familyHistoryForMaternalRelatives) {
        this.familyHistoryForMaternalRelatives = familyHistoryForMaternalRelatives;
    }

    public List<FamilyHistoryDetails> getFamilyHistoryForPaternalRelatives() {
        return familyHistoryForPaternalRelatives;
    }

    public void setFamilyHistoryForPaternalRelatives(List<FamilyHistoryDetails> familyHistoryForPaternalRelatives) {
        this.familyHistoryForPaternalRelatives = familyHistoryForPaternalRelatives;
    }

    public String getFamilyHistoryOfCancer() {
        return familyHistoryOfCancer;
    }

    public void setFamilyHistoryOfCancer(String familyHistoryOfCancer) {
        this.familyHistoryOfCancer = familyHistoryOfCancer;
    }

    public List<FamilyHistoryDetails> getFamilyHistoryDetailsList() {
        return familyHistoryDetailsList;
    }

    public void setFamilyHistoryDetailsList(List<FamilyHistoryDetails> familyHistoryDetailsList) {
        this.familyHistoryDetailsList = familyHistoryDetailsList;
    }

    public Set<String> getRiskAssessmentNccn() {
        return riskAssessmentNccn;
    }

    public void setRiskAssessmentNccn(Set<String> riskAssessmentNccn) {
        this.riskAssessmentNccn = riskAssessmentNccn;
    }

    public boolean isPersonalHistory() {
        return isPersonalHistory;
    }

    public void setPersonalHistory(boolean personalHistory) {
        isPersonalHistory = personalHistory;
    }

    public boolean isFamilyHistory() {
        return isFamilyHistory;
    }

    public void setFamilyHistory(boolean familyHistory) {
        isFamilyHistory = familyHistory;
    }

    public String getPedigreeURL() {
        return pedigreeURL;
    }

    public void setPedigreeURL(String pedigreeURL) {
        this.pedigreeURL = pedigreeURL;
    }

    public boolean isReturningPatient() {
        return returningPatient;
    }

    public void setReturningPatient(boolean returningPatient) {
        this.returningPatient = returningPatient;
    }

    public String getPreviousReportDate() {
        return previousReportDate;
    }

    public void setPreviousReportDate(String previousReportDate) {
        this.previousReportDate = previousReportDate;
    }

    public String getPreviousReportLink() {
        return previousReportLink;
    }

    public void setPreviousReportLink(String previousReportLink) {
        this.previousReportLink = previousReportLink;
    }

    public List<PreviousRiskAssessmentDetails> getPreviousRiskAssessmentDetails() {
        return previousRiskAssessmentDetails;
    }

    public void setPreviousRiskAssessmentDetails(List<PreviousRiskAssessmentDetails> previousRiskAssessmentDetails) {
        this.previousRiskAssessmentDetails = previousRiskAssessmentDetails;
    }

    public List<String> getRelativeWithPolypsHistory() {
        return relativeWithPolypsHistory;
    }

    public void setRelativeWithPolypsHistory(List<String> relativeWithPolypsHistory) {
        this.relativeWithPolypsHistory = relativeWithPolypsHistory;
    }

    public boolean isPreviousTestingByCare() {
        return previousTestingByCare;
    }

    public void setPreviousTestingByCare(boolean previousTestingByCare) {
        this.previousTestingByCare = previousTestingByCare;
    }

    public List<CustomQuestions> getCustomQuestions() {
        return customQuestions;
    }

    public void setCustomQuestions(List<CustomQuestions> customQuestions) {
        this.customQuestions = customQuestions;
    }

    public Boolean getShowSummary() {
        return showSummary;
    }

    public void setShowSummary(Boolean showSummary) {
        this.showSummary = showSummary;
    }

    public Boolean getRenameNCCNSummaryHeader() {
        return renameNCCNSummaryHeader;
    }

    public void setRenameNCCNSummaryHeader(Boolean renameNCCNSummaryHeader) {
        this.renameNCCNSummaryHeader = renameNCCNSummaryHeader;
    }

    public boolean isPrDesmoid() {
        return isPrDesmoid;
    }

    public void setPrDesmoid(boolean prDesmoid) {
        isPrDesmoid = prDesmoid;
    }

    public boolean isPrPolyps() {
        return isPrPolyps;
    }

    public void setPrPolyps(boolean prPolyps) {
        isPrPolyps = prPolyps;
    }

    public boolean isPersonalFamilyHxChanges() {
        return isPersonalFamilyHxChanges;
    }

    public void setPersonalFamilyHxChanges(boolean personalFamilyHxChanges) {
        isPersonalFamilyHxChanges = personalFamilyHxChanges;
    }

    public String getNccnVersion() {
        return nccnVersion;
    }

    public void setNccnVersion(String nccnVersion) {
        this.nccnVersion = nccnVersion;
    }

    public boolean isPrAdPolyps() {
        return isPrAdPolyps;
    }

    public void setPrAdPolyps(boolean prAdPolyps) {
        isPrAdPolyps = prAdPolyps;
    }

    public boolean isPrPolypsCountGTTen() {
        return isPrPolypsCountGTTen;
    }

    public void setPrPolypsCountGTTen(boolean prPolypsCountGTTen) {
        isPrPolypsCountGTTen = prPolypsCountGTTen;
    }

    public List<ConsentQuestion> getConsentQuestions() {
        return consentQuestions;
    }

    public void setConsentQuestions(List<ConsentQuestion> consentQuestions) {
        this.consentQuestions = consentQuestions;
    }

    public String getAccountAliasName() {
        return accountAliasName;
    }

    public void setAccountAliasName(String accountAliasName) {
        this.accountAliasName = accountAliasName;
    }

    public String getExternalAppointmentId() {
        return externalAppointmentId;
    }

    public void setExternalAppointmentId(String externalAppointmentId) {
        this.externalAppointmentId = externalAppointmentId;
    }

    public boolean getIsBiradsEnabled() {
        return this.isBiradsEnabled;
    }

    public void setIsBiradsEnabled(boolean biradsEnabled) {
        this.isBiradsEnabled = biradsEnabled;
    }

    public boolean isTcWithMortalityEnabled() {
        return tcWithMortalityEnabled;
    }

    public void setTcWithMortalityEnabled(boolean tcWithMortalityEnabled) {
        this.tcWithMortalityEnabled = tcWithMortalityEnabled;
    }

    public boolean isTcWithOutMortalityEnabled() {
        return tcWithOutMortalityEnabled;
    }

    public void setTcWithOutMortalityEnabled(boolean tcWithOutMortalityEnabled) {
        this.tcWithOutMortalityEnabled = tcWithOutMortalityEnabled;
    }

    public TyrerCuzickCSDTO getTyrerCuzickCSDTO() {
        return tyrerCuzickCSDTO;
    }

    public void setTyrerCuzickCSDTO(TyrerCuzickCSDTO tyrerCuzickCSDTO) {
        this.tyrerCuzickCSDTO = tyrerCuzickCSDTO;
    }

    public boolean isBreastDensityUpdated() {
        return breastDensityUpdated;
    }

    public void setBreastDensityUpdated(boolean breastDensityUpdated) {
        this.breastDensityUpdated = breastDensityUpdated;
    }

    public Boolean getIsResultBack() {
        return isResultBack;
    }

    public void setIsResultBack(Boolean resultBack) {
        isResultBack = resultBack;
    }

    public String getBrcaGeneInResult() {
        return brcaGeneInResult;
    }

    public void setBrcaGeneInResult(String brcaGeneInResult) {
        this.brcaGeneInResult = brcaGeneInResult;
    }

    public Boolean getOtherGeneInResultPositive() {
        return otherGeneInResultPositive;
    }

    public void setOtherGeneInResultPositive(Boolean otherGeneInResultPositive) {
        this.otherGeneInResultPositive = otherGeneInResultPositive;
    }

    public Boolean getEnableAskTCQuestions() {
        return enableAskTCQuestions;
    }

    public void setEnableAskTCQuestions(Boolean enableAskTCQuestions) {
        this.enableAskTCQuestions = enableAskTCQuestions;
    }

    public Boolean getHasBreastCancer() {
        return hasBreastCancer;
    }

    public void setHasBreastCancer(Boolean hasBreastCancer) {
        this.hasBreastCancer = hasBreastCancer;
    }
}
