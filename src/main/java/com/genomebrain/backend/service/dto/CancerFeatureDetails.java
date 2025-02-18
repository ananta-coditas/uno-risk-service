package com.genomebrain.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CancerFeatureDetails {

    @JsonProperty("cancer_feature")
    private String cancerFeature;

    @JsonProperty("cancer_feature_type")
    private String cancerFeatureType;

    @JsonProperty("exact_dx_age")
    private String exactDxAge;

    @JsonProperty("min_dx_age")
    private String minDxAge;

    @JsonProperty("max_dx_age")
    private String maxDxAge;

    @JsonProperty("other_dx_age")
    private String otherDxAge;

    @JsonProperty("metastatic")
    private String metastatic;

    @JsonProperty("multiple_primaries")
    private String multiplePrimaries;

    @JsonProperty("pathology")
    private List<String> breastCancerTypes;

    @JsonProperty("pathology_other")
    private String pathologyOther;

    @JsonProperty("breast_bilateral")
    private String breastBilateral;

    @JsonProperty("breast_contralateral")
    private String breastContralateral;

    @JsonProperty("breast_ipsilateral")
    private String breastIpsilateral;

    @JsonProperty("breast_er")
    private String breastEr;

    @JsonProperty("breast_pr")
    private String breastPr;

    @JsonProperty("breast_her2")
    private String breastHer2;

    @JsonProperty("prostate_gleason_score")
    private String prostateGleasonScore;

    @JsonProperty("prostate_risk_group")
    private String prostateRiskGroup;

    @JsonProperty("prostate_grade_group")
    private String prostateGradeGroup;

    public String getCancerFeature() {
        return cancerFeature;
    }

    public void setCancerFeature(String cancerFeature) {
        this.cancerFeature = cancerFeature;
    }

    public String getCancerFeatureType() {
        return cancerFeatureType;
    }

    public void setCancerFeatureType(String cancerFeatureType) {
        this.cancerFeatureType = cancerFeatureType;
    }

    public String getExactDxAge() {
        return exactDxAge;
    }

    public void setExactDxAge(String exactDxAge) {
        this.exactDxAge = exactDxAge;
    }

    public String getMinDxAge() {
        return minDxAge;
    }

    public void setMinDxAge(String minDxAge) {
        this.minDxAge = minDxAge;
    }

    public String getMaxDxAge() {
        return maxDxAge;
    }

    public void setMaxDxAge(String maxDxAge) {
        this.maxDxAge = maxDxAge;
    }

    public String getOtherDxAge() {
        return otherDxAge;
    }

    public void setOtherDxAge(String otherDxAge) {
        this.otherDxAge = otherDxAge;
    }

    public String getMetastatic() {
        return metastatic;
    }

    public void setMetastatic(String metastatic) {
        this.metastatic = metastatic;
    }

    public String getMultiplePrimaries() {
        return multiplePrimaries;
    }

    public void setMultiplePrimaries(String multiplePrimaries) {
        this.multiplePrimaries = multiplePrimaries;
    }

    public List<String> getBreastCancerTypes() {
        return breastCancerTypes;
    }

    public void setBreastCancerTypes(List<String> pathology) {
        this.breastCancerTypes = pathology;
    }

    public String getPathologyOther() {
        return pathologyOther;
    }

    public void setPathologyOther(String pathologyOther) {
        this.pathologyOther = pathologyOther;
    }

    public String getBreastBilateral() {
        return breastBilateral;
    }

    public void setBreastBilateral(String breastBilateral) {
        this.breastBilateral = breastBilateral;
    }

    public String getBreastContralateral() {
        return breastContralateral;
    }

    public void setBreastContralateral(String breastContralateral) {
        this.breastContralateral = breastContralateral;
    }

    public String getBreastIpsilateral() {
        return breastIpsilateral;
    }

    public void setBreastIpsilateral(String breastIpsilateral) {
        this.breastIpsilateral = breastIpsilateral;
    }

    public String getBreastEr() {
        return breastEr;
    }

    public void setBreastEr(String breastEr) {
        this.breastEr = breastEr;
    }

    public String getBreastPr() {
        return breastPr;
    }

    public void setBreastPr(String breastPr) {
        this.breastPr = breastPr;
    }

    public String getBreastHer2() {
        return breastHer2;
    }

    public void setBreastHer2(String breastHer2) {
        this.breastHer2 = breastHer2;
    }

    public String getProstateGleasonScore() {
        return prostateGleasonScore;
    }

    public void setProstateGleasonScore(String prostateGleasonScore) {
        this.prostateGleasonScore = prostateGleasonScore;
    }

    public String getProstateRiskGroup() {
        return prostateRiskGroup;
    }

    public void setProstateRiskGroup(String prostateRiskGroup) {
        this.prostateRiskGroup = prostateRiskGroup;
    }

    public String getProstateGradeGroup() {
        return prostateGradeGroup;
    }

    public void setProstateGradeGroup(String prostateGradeGroup) {
        this.prostateGradeGroup = prostateGradeGroup;
    }

    @Override
    public String toString() {
        return "CancerFeatureDetails{" +
                "cancerFeature='" + cancerFeature + '\'' +
                ", cancerFeatureType='" + cancerFeatureType + '\'' +
                ", exactDxAge='" + exactDxAge + '\'' +
                ", minDxAge='" + minDxAge + '\'' +
                ", maxDxAge='" + maxDxAge + '\'' +
                ", otherDxAge='" + otherDxAge + '\'' +
                ", metastatic='" + metastatic + '\'' +
                ", multiplePrimaries='" + multiplePrimaries + '\'' +
                ", pathology=" + breastCancerTypes +
                ", pathologyOther='" + pathologyOther + '\'' +
                ", breastContralateral='" + breastContralateral + '\'' +
                ", breastIpsilateral='" + breastIpsilateral + '\'' +
                ", breastEr='" + breastEr + '\'' +
                ", breastPr='" + breastPr + '\'' +
                ", breastHer2='" + breastHer2 + '\'' +
                ", prostateGleasonScore='" + prostateGleasonScore + '\'' +
                ", prostateRiskGroup='" + prostateRiskGroup + '\'' +
                ", prostateGradeGroup='" + prostateGradeGroup + '\'' +
                '}';
    }
}
