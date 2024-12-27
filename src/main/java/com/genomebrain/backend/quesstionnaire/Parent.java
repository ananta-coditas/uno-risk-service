package com.genomebrain.backend.quesstionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parent {

    private String parentID;
    private String type;
    private String parentName;
    private String parentKey;
    private String parentLabel;
    @JsonProperty("isUnaffected")
    private boolean unaffected;
    private String parentType;

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    public boolean isUnaffected() {
        return unaffected;
    }

    public void setUnaffected(boolean unaffected) {
        this.unaffected = unaffected;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}

