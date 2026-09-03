package com.genomebrain.backend.quesstionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RelativeDetail {

    private String relativeID;
    private String relativeType;
    private String relativeName;
    private String relativeAge;
    private String relativeGender;
    private String deceasedStatus;
    private String relativeLabel;
    private int adopted;
    private String changeMarkedBy;
    private String infertile;
    private String sab;
    private String noIssue;
    private String iconText;
    private boolean isTwin;
    private String twinStyle;
    private int nodeId;
    private int connectedToNodeId;
    private Integer relativeUpn;
    private String knownParentGender;
    private int parentUpn;
    @JsonProperty("isUnaffected")
    private boolean unaffected;
    private Parent parent;
    private String additionalNotes;
    private String relativeGeneList;

    public String getRelativeType() {
        return relativeType;
    }

    public void setRelativeType(String relativeType) {
        this.relativeType = relativeType;
    }

    public String getRelativeName() {
        return relativeName;
    }

    public void setRelativeName(String relativeName) {
        this.relativeName = relativeName;
    }

    public String getRelativeLabel() {
        return relativeLabel;
    }

    public void setRelativeLabel(String relativeLabel) {
        this.relativeLabel = relativeLabel;
    }

    public String getRelativeID() {
        return relativeID;
    }

    public void setRelativeID(String relativeID) {
        this.relativeID = relativeID;
    }

    public String getRelativeAge() {
        return relativeAge;
    }

    public void setRelativeAge(String relativeAge) {
        this.relativeAge = relativeAge;
    }

    public String getRelativeGender() {
        return relativeGender;
    }

    public void setRelativeGender(String relativeGender) {
        this.relativeGender = relativeGender;
    }

    public String getDeceasedStatus() {
        return deceasedStatus;
    }

    public void setDeceasedStatus(String deceasedStatus) {
        this.deceasedStatus = deceasedStatus;
    }

    public int getAdopted() {
        return adopted;
    }

    public void setAdopted(int adopted) {
        this.adopted = adopted;
    }

    public String getChangeMarkedBy() {
        return changeMarkedBy;
    }

    public void setChangeMarkedBy(String changeMarkedBy) {
        this.changeMarkedBy = changeMarkedBy;
    }

    public String getInfertile() {
        return infertile;
    }

    public void setInfertile(String infertile) {
        this.infertile = infertile;
    }

    public String getSab() {
        return sab;
    }

    public void setSab(String sab) {
        this.sab = sab;
    }

    public String getNoIssue() {
        return noIssue;
    }

    public void setNoIssue(String noIssue) {
        this.noIssue = noIssue;
    }

    public String getIconText() {
        return iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public boolean isTwin() {
        return isTwin;
    }

    public void setTwin(boolean twin) {
        isTwin = twin;
    }

    public String getTwinStyle() {
        return twinStyle;
    }

    public void setTwinStyle(String twinStyle) {
        this.twinStyle = twinStyle;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getConnectedToNodeId() {
        return connectedToNodeId;
    }

    public void setConnectedToNodeId(int connectedToNodeId) {
        this.connectedToNodeId = connectedToNodeId;
    }

    public Integer getRelativeUpn() {
        return relativeUpn;
    }

    public void setRelativeUpn(Integer relativeUpn) {
        this.relativeUpn = relativeUpn;
    }

    public String getKnownParentGender() {
        return knownParentGender;
    }

    public void setKnownParentGender(String knownParentGender) {
        this.knownParentGender = knownParentGender;
    }

    public int getParentUpn(){
        return parentUpn;
    }

    public void setParentUpn(int parentUpn){
        this.parentUpn = parentUpn;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public boolean isUnaffected() {
        return unaffected;
    }

    public void setUnaffected(boolean unaffected) {
        this.unaffected = unaffected;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getRelativeGeneList() {
        return relativeGeneList;
    }

    public void setRelativeGeneList(String relativeGeneList) {
        this.relativeGeneList = relativeGeneList;
    }

    @Override
    public String toString() {
        return "RelativeDetail{" +
                "relativeID='" + relativeID + '\'' +
                ", relativeType='" + relativeType + '\'' +
                ", relativeName='" + relativeName + '\'' +
                ", relativeAge='" + relativeAge + '\'' +
                ", gender='" + relativeGender + '\'' +
                ", deceasedStatus='" + deceasedStatus + '\'' +
                ", relativeLabel='" + relativeLabel + '\'' +
                ", adopted='" + adopted + '\'' +
                ", changeMarkedBy='" + changeMarkedBy + '\'' +
                ", infertile='" + infertile + '\'' +
                ", sab='" + sab + '\'' +
                ", noIssue='" + noIssue + '\'' +
                ", iconText='" + iconText + '\'' +
                ", nodeId=" + nodeId +
                ", connectedToNodeId=" + connectedToNodeId +
                ", knownParentGender=" + knownParentGender +
                ", parentUpn=" + parentUpn +
                '}';
    }
}
