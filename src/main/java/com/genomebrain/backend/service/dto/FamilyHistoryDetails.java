package com.genomebrain.backend.service.dto;

import java.util.List;

import com.genomebrain.backend.quesstionnaire.RelativeDetail;

public class FamilyHistoryDetails {

    private String familyMember;

    private String sex;

    private String relativeSide;

    private String relativeDegree;

    private int sequence;

    private List<CancerDetailsObject> cancerDetails;

    private RelativeDetail relativeDetail;

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRelativeSide() {
        return relativeSide;
    }

    public void setRelativeSide(String relativeSide) {
        this.relativeSide = relativeSide;
    }

    public String getRelativeDegree() {
        return relativeDegree;
    }

    public void setRelativeDegree(String relativeDegree) {
        this.relativeDegree = relativeDegree;
    }

    public List<CancerDetailsObject> getCancerDetails() {
        return cancerDetails;
    }

    public void setCancerDetails(List<CancerDetailsObject> cancerDetails) {
        this.cancerDetails = cancerDetails;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public RelativeDetail getRelativeDetail() {
        return relativeDetail;
    }

    public void setRelativeDetail(RelativeDetail relativeDetail) {
        this.relativeDetail = relativeDetail;
    }
}
