package com.genomebrain.backend.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

public class CancerDetailsObject {

    private String cancer;
    private String age;
    private Map<String,String> additionalInformation;

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
                .append("cancer",cancer)
                .append("age",age)
                .append("additionalInformation",additionalInformation)
                .toString();
    }
}
