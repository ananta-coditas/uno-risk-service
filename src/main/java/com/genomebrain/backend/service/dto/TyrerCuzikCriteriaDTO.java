package com.genomebrain.backend.service.dto;

public class TyrerCuzikCriteriaDTO {

    String sexAtBirth;
    Integer age;
    boolean isBreast;
    boolean isBRCAMutation;
    private boolean ashkenaziInheritance = false;
    private boolean showBRCATesting = false;
    private Boolean previousExlcudedDueToBRCAMutation;
    private Boolean previousExlcudedDueToBreastCancer;
    private Boolean hasPreviousHistoryChanged ;

    public String getSexAtBirth() {
        return sexAtBirth;
    }

    public void setSexAtBirth(String sexAtBirth) {
        this.sexAtBirth = sexAtBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isBreast() {
        return isBreast;
    }

    public void setBreast(boolean breast) {
        isBreast = breast;
    }

    public boolean isBRCAMutation() {
        return isBRCAMutation;
    }

    public void setBRCAMutation(boolean BRCAMutation) {
        isBRCAMutation = BRCAMutation;
    }

    public boolean isAshkenaziInheritance() {
        return ashkenaziInheritance;
    }

    public void setAshkenaziInheritance(boolean ashkenaziInheritance) {
        this.ashkenaziInheritance = ashkenaziInheritance;
    }

    public boolean isShowBRCATesting() {
        return showBRCATesting;
    }

    public void setShowBRCATesting(boolean showBRCATesting) {
        this.showBRCATesting = showBRCATesting;
    }

    public Boolean getPreviousExlcudedDueToBRCAMutation() {
        return previousExlcudedDueToBRCAMutation;
    }

    public void setPreviousExlcudedDueToBRCAMutation(Boolean previousExlcudedDueToBRCAMutation) {
        this.previousExlcudedDueToBRCAMutation = previousExlcudedDueToBRCAMutation;
    }

    public Boolean getPreviousExlcudedDueToBreastCancer() {
        return previousExlcudedDueToBreastCancer;
    }

    public void setPreviousExlcudedDueToBreastCancer(Boolean previousExlcudedDueToBreastCancer) {
        this.previousExlcudedDueToBreastCancer = previousExlcudedDueToBreastCancer;
    }

    public Boolean getHasPreviousHistoryChanged() {
        return hasPreviousHistoryChanged;
    }

    public void setHasPreviousHistoryChanged(Boolean hasPreviousHistoryChanged) {
        this.hasPreviousHistoryChanged = hasPreviousHistoryChanged;
    }

    @Override
    public String toString() {
        return "TyrerCuzikCriteriaDTO{" +
                "sexAtBirth='" + sexAtBirth + '\'' +
                ", age=" + age +
                ", isBreast=" + isBreast +
                ", isBRCAMutation=" + isBRCAMutation +
                ", ashkenaziInheritance=" + ashkenaziInheritance +
                ", showBRCATesting=" + showBRCATesting +
                ", previousExlcudedDueToBRCAMutation=" + previousExlcudedDueToBRCAMutation +
                ", previousExlcudedDueToBreastCancer=" + previousExlcudedDueToBreastCancer +
                ", hasPreviousHistoryChanged=" + hasPreviousHistoryChanged +
                '}';
    }
}
