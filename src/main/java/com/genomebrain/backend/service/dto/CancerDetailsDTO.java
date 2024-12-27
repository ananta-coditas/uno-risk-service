package com.genomebrain.backend.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;
import java.util.Set;

public class CancerDetailsDTO {

    String cancerType;
    Integer ageOnSet;
    Set<String> tags;
    private Map<String,String> additionalInformation;

    public String getCancerType() {
        return cancerType;
    }

    public void setCancerType(String cancerType) {
        this.cancerType = cancerType;
    }

    public Integer getAgeOnSet() {
        return ageOnSet;
    }

    public void setAgeOnSet(Integer ageOnSet) {
        this.ageOnSet = ageOnSet;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, String> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cancerType", cancerType)
                .append("ageOnSet", ageOnSet)
                .append("tags", tags)
                .toString();
    }
}
