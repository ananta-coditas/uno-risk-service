package com.genomebrain.backend.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TyrerCuzickCSDTO {

    private TCDetails tc = new TCDetails();
    private TCDetails biradsA = new TCDetails();
    private TCDetails biradsB = new TCDetails();
    private TCDetails biradsC = new TCDetails();
    private TCDetails biradsD = new TCDetails();

    public TCDetails getTc() {
        return tc;
    }

    public void setTc(TCDetails tc) {
        this.tc = tc;
    }

    public TCDetails getBiradsA() {
        return biradsA;
    }

    public void setBiradsA(TCDetails biradsA) {
        this.biradsA = biradsA;
    }

    public TCDetails getBiradsB() {
        return biradsB;
    }

    public void setBiradsB(TCDetails biradsB) {
        this.biradsB = biradsB;
    }

    public TCDetails getBiradsC() {
        return biradsC;
    }

    public void setBiradsC(TCDetails biradsC) {
        this.biradsC = biradsC;
    }

    public TCDetails getBiradsD() {
        return biradsD;
    }

    public void setBiradsD(TCDetails biradsD) {
        this.biradsD = biradsD;
    }

    public static class TCDetails {
        private String lifeTimeWithMortality;
        private String tenYearsWithMortality;
        private String lifeTimeWithOutMortality;
        private String tenYearsWithOutMortality;

        public TCDetails() {
        }

        public TCDetails(String lifeTimeWithMortality, String lifeTimeWithOutMortality, String tenYearsWithMortality, String tenYearsWithOutMortality) {
            this.lifeTimeWithMortality = lifeTimeWithMortality;
            this.tenYearsWithMortality = tenYearsWithMortality;
            this.lifeTimeWithOutMortality = lifeTimeWithOutMortality;
            this.tenYearsWithOutMortality = tenYearsWithOutMortality;
        }

        public String getLifeTimeWithMortality() {
            return lifeTimeWithMortality;
        }

        public void setLifeTimeWithMortality(String lifeTimeWithMortality) {
            this.lifeTimeWithMortality = lifeTimeWithMortality;
        }

        public String getTenYearsWithMortality() {
            return tenYearsWithMortality;
        }

        public void setTenYearsWithMortality(String tenYearsWithMortality) {
            this.tenYearsWithMortality = tenYearsWithMortality;
        }

        public String getLifeTimeWithOutMortality() {
            return lifeTimeWithOutMortality;
        }

        public void setLifeTimeWithOutMortality(String lifeTimeWithOutMortality) {
            this.lifeTimeWithOutMortality = lifeTimeWithOutMortality;
        }

        public String getTenYearsWithOutMortality() {
            return tenYearsWithOutMortality;
        }

        public void setTenYearsWithOutMortality(String tenYearsWithOutMortality) {
            this.tenYearsWithOutMortality = tenYearsWithOutMortality;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(new TyrerCuzickCSDTO()));
    }

}
