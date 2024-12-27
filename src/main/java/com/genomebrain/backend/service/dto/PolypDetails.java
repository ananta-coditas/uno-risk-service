package com.genomebrain.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PolypDetails {
    @JsonProperty("polyp_type")
    private String polypType;

    @JsonProperty("exact_num_of_polyps")
    private String exactNumOfPolyps;

    @JsonProperty("min_num_of_polyps")
    private String minNumOfPolyps;

    @JsonProperty("max_num_of_polyps")
    private String maxNumOfPolyps;

    @JsonProperty("other_num_of_polyps")
    private String otherNumOfPolyps;

    public String getPolypType() {
        return polypType;
    }

    public void setPolypType(String polypType) {
        this.polypType = polypType;
    }

    public String getExactNumOfPolyps() {
        return exactNumOfPolyps;
    }

    public void setExactNumOfPolyps(String exactNumOfPolyps) {
        this.exactNumOfPolyps = exactNumOfPolyps;
    }

    public String getMinNumOfPolyps() {
        return minNumOfPolyps;
    }

    public void setMinNumOfPolyps(String minNumOfPolyps) {
        this.minNumOfPolyps = minNumOfPolyps;
    }

    public String getMaxNumOfPolyps() {
        return maxNumOfPolyps;
    }

    public void setMaxNumOfPolyps(String maxNumOfPolyps) {
        this.maxNumOfPolyps = maxNumOfPolyps;
    }

    public String getOtherNumOfPolyps() {
        return otherNumOfPolyps;
    }

    public void setOtherNumOfPolyps(String otherNumOfPolyps) {
        this.otherNumOfPolyps = otherNumOfPolyps;
    }

    @Override
    public String toString() {
        return "PolypDetails{" +
                "polypType='" + polypType + '\'' +
                ", exactNumOfPolyps='" + exactNumOfPolyps + '\'' +
                ", minNumOfPolyps='" + minNumOfPolyps + '\'' +
                ", maxNumOfPolyps='" + maxNumOfPolyps + '\'' +
                ", otherNumOfPolyps='" + otherNumOfPolyps + '\'' +
                '}';
    }
}
