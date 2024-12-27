package com.genomebrain.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClinicalHistory {
    @JsonProperty("relationship")
    private String relationship;

    @JsonProperty("side_of_fam")
    private String sideOfFam;

    @JsonProperty("sex_at_birth")
    private String sexAtBirth;

    @JsonProperty("ethnicity")
    private List<String> ethnicity;

    @JsonProperty("ethnicity_other")
    private String ethnicityOther;

    @JsonProperty("cancer_overall")
    private String cancerOverall;

    @JsonProperty("clinical_feature_overall")
    private String clinicalFeatureOverall;

    @JsonProperty("cancer_feature_details")
    private List<CancerFeatureDetails> cancerFeatureDetails;

    @JsonProperty("polyp_overall")
    private String polypOverall;

    @JsonProperty("polyp_details")
    private List<PolypDetails> polypDetails;

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSideOfFam() {
        return sideOfFam;
    }

    public void setSideOfFam(String sideOfFam) {
        this.sideOfFam = sideOfFam;
    }

    public String getSexAtBirth() {
        return sexAtBirth;
    }

    public void setSexAtBirth(String sexAtBirth) {
        this.sexAtBirth = sexAtBirth;
    }

    public List<String> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(List<String> ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getEthnicityOther() {
        return ethnicityOther;
    }

    public void setEthnicityOther(String ethnicityOther) {
        this.ethnicityOther = ethnicityOther;
    }

    public String getCancerOverall() {
        return cancerOverall;
    }

    public void setCancerOverall(String cancerOverall) {
        this.cancerOverall = cancerOverall;
    }

    public String getClinicalFeatureOverall() {
        return clinicalFeatureOverall;
    }

    public void setClinicalFeatureOverall(String clinicalFeatureOverall) {
        this.clinicalFeatureOverall = clinicalFeatureOverall;
    }

    public List<CancerFeatureDetails> getCancerFeatureDetails() {
        return cancerFeatureDetails;
    }

    public void setCancerFeatureDetails(List<CancerFeatureDetails> cancerFeatureDetails) {
        this.cancerFeatureDetails = cancerFeatureDetails;
    }

    public String getPolypOverall() {
        return polypOverall;
    }

    public void setPolypOverall(String polypOverall) {
        this.polypOverall = polypOverall;
    }

    public List<PolypDetails> getPolypDetails() {
        return polypDetails;
    }

    public void setPolypDetails(List<PolypDetails> polypDetails) {
        this.polypDetails = polypDetails;
    }

    @Override
    public String toString() {
        return "ClinicalHistory{" +
                "relationship='" + relationship + '\'' +
                ", sideOfFam='" + sideOfFam + '\'' +
                ", sexAtBirth='" + sexAtBirth + '\'' +
                ", ethnicity=" + ethnicity +
                ", ethnicityOther='" + ethnicityOther + '\'' +
                ", cancerOverall='" + cancerOverall + '\'' +
                ", clinicalFeatureOverall='" + clinicalFeatureOverall + '\'' +
                ", cancerFeatureDetails=" + cancerFeatureDetails +
                ", polypOverall='" + polypOverall + '\'' +
                ", polypDetails=" + polypDetails +
                '}';
    }
}
