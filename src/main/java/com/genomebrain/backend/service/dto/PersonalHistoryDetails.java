package com.genomebrain.backend.service.dto;

import java.util.List;

public class PersonalHistoryDetails {

    private String age;

    //@NoHoldToTCPropertyMapper(key = "AMBRY096")
    private String weight;

    //@NoHoldToTCPropertyMapper(key = "AMBRY095")
    private String height;

    //Set in guidelineUtil
    private List<String> ethnicity;

    private String ethnicitySide;

    //Set in guidelineUtil
    private String adopted;

    //@NoHoldToTCPropertyMapper(key = "AMBRY094")
    private String ageAtMenarche;

    //@NoHoldToTCPropertyMapper(key = "AMBRY097")
    private String ageAtFirstChild;

    //@NoHoldToTCPropertyMapper(key = "AMBRY098")
    private String menopausalStatus;

    //@NoHoldToTCPropertyMapper(key = "AMBRY099")
    private String ageAtMenopause;

    //@NoHoldToTCPropertyMapper(key = "AMBRY100")
    private String hrtEverUsed;

    //@NoHoldToTCPropertyMapper(key = "AMBRY103")
    private String hrtPlannedUse;

    //@NoHoldToTCPropertyMapper(key = "AMBRY104")
    private String hrtType;

    //@NoHoldToTCPropertyMapper(key = "AMBRY100")
    private String hrtUsedFor;

    //@NoHoldToTCPropertyMapper(key = "AMBRY106")
    private String abnormalBreastBiopsyHx;

    //@NoHoldToTCPropertyMapper(key = "AMBRY105")
    private String abnormalBreastBiopsy;

    //@NoHoldToTCPropertyMapper(key = "AMBRY102")
    private String hrtHowLongHaveTaken;

    //@NoHoldToTCPropertyMapper(key = "AMBRY101")
    private String hrtLastUsed;

    //@NoHoldToTCPropertyMapper(key = "AMBRY109")
    private String hrtHowLongDidTake;

    //@NoHoldToTCPropertyMapper(key = "AMBRY110")
    private String hrtStopTakingHowLongAgo;

    //@NoHoldToTCPropertyMapper(key = "AMBRY103")
    private String hrtIntendedUse;

    //@NoHoldToTCPropertyMapper(key = "AMBRY120")
    private String brcaTesting = "No";

    private boolean showBrcaTesting = false;

    //Set in guidelineUtil
    private String cancerHx;

    //Set in guidelineUtil
    private List<CancerDetailsObject> cancerDetails;

    //Set in guidelineUtil
    private String colorectalPolypHx;

    private String colorectalPolypDetails;

    private String colonoscopy;

    private String newColorectalPolypHx;

    private String isPolypAdenomas;

    private String countOfPolyps;

    private String isDesmoidTumor;

    private String hadGeneticTesting;

    private String resultOfGeneticTesting;

    private String yearOfGeneticTesting;
    
    private String breastDensity;
    
    private String breastDensityMeasurementType;

    private String previousBiopsy;

    private String breastBiopsyResult;

    private String abnormalBreastBiopsyResult;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<String> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(List<String> ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getAdopted() {
        return adopted;
    }

    public void setAdopted(String adopted) {
        this.adopted = adopted;
    }

    public String getAgeAtMenarche() {
        return ageAtMenarche;
    }

    public void setAgeAtMenarche(String ageAtMenarche) {
        this.ageAtMenarche = ageAtMenarche;
    }

    public String getAgeAtFirstChild() {
        return ageAtFirstChild;
    }

    public void setAgeAtFirstChild(String ageAtFirstChild) {
        this.ageAtFirstChild = ageAtFirstChild;
    }

    public String getMenopausalStatus() {
        return menopausalStatus;
    }

    public void setMenopausalStatus(String menopausalStatus) {
        this.menopausalStatus = menopausalStatus;
    }

    public String getAgeAtMenopause() {
        return ageAtMenopause;
    }

    public void setAgeAtMenopause(String ageAtMenopause) {
        this.ageAtMenopause = ageAtMenopause;
    }

    public String getHrtEverUsed() {
        return hrtEverUsed;
    }

    public void setHrtEverUsed(String hrtEverUsed) {
        this.hrtEverUsed = hrtEverUsed;
    }

    public String getHrtPlannedUse() {
        return hrtPlannedUse;
    }

    public void setHrtPlannedUse(String hrtPlannedUse) {
        this.hrtPlannedUse = hrtPlannedUse;
    }

    public String getHrtType() {
        return hrtType;
    }

    public void setHrtType(String hrtType) {
        this.hrtType = hrtType;
    }

    public String getHrtUsedFor() {
        return hrtUsedFor;
    }

    public void setHrtUsedFor(String hrtUsedFor) {
        this.hrtUsedFor = hrtUsedFor;
    }

    public String getHrtLastUsed() {
        return hrtLastUsed;
    }

    public void setHrtLastUsed(String hrtLastUsed) {
        this.hrtLastUsed = hrtLastUsed;
    }

    public String getAbnormalBreastBiopsyHx() {
        return abnormalBreastBiopsyHx;
    }

    public void setAbnormalBreastBiopsyHx(String abnormalBreastBiopsyHx) {
        this.abnormalBreastBiopsyHx = abnormalBreastBiopsyHx;
    }

    public String getBrcaTesting() {
        return brcaTesting;
    }

    public void setBrcaTesting(String brcaTesting) {
        this.brcaTesting = brcaTesting;
    }

    public String getCancerHx() {
        return cancerHx;
    }

    public void setCancerHx(String cancerHx) {
        this.cancerHx = cancerHx;
    }

    public List<CancerDetailsObject> getCancerDetails() {
        return cancerDetails;
    }

    public void setCancerDetails(List<CancerDetailsObject> cancerDetails) {
        this.cancerDetails = cancerDetails;
    }

    public String getColorectalPolypHx() {
        return colorectalPolypHx;
    }

    public void setColorectalPolypHx(String colorectalPolypHx) {
        this.colorectalPolypHx = colorectalPolypHx;
    }

    public String getNewColorectalPolypHx() {
        return newColorectalPolypHx;
    }

    public void setNewColorectalPolypHx(String newColorectalPolypHx) {
        this.newColorectalPolypHx = newColorectalPolypHx;
    }

    public String getHrtHowLongHaveTaken() {
        return hrtHowLongHaveTaken;
    }

    public void setHrtHowLongHaveTaken(String hrtHowLongHaveTaken) {
        this.hrtHowLongHaveTaken = hrtHowLongHaveTaken;
    }

    public String getHrtHowLongDidTake() {
        return hrtHowLongDidTake;
    }

    public void setHrtHowLongDidTake(String hrtHowLongDidTake) {
        this.hrtHowLongDidTake = hrtHowLongDidTake;
    }

    public String getHrtStopTakingHowLongAgo() {
        return hrtStopTakingHowLongAgo;
    }

    public void setHrtStopTakingHowLongAgo(String hrtStopTakingHowLongAgo) {
        this.hrtStopTakingHowLongAgo = hrtStopTakingHowLongAgo;
    }

    public String getHrtIntendedUse() {
        return hrtIntendedUse;
    }

    public void setHrtIntendedUse(String hrtIntendedUse) {
        this.hrtIntendedUse = hrtIntendedUse;
    }

    public String getAbnormalBreastBiopsy() {
        return abnormalBreastBiopsy;
    }

    public void setAbnormalBreastBiopsy(String abnormalBreastBiopsy) {
        this.abnormalBreastBiopsy = abnormalBreastBiopsy;
    }

    public String getColonoscopy() {
        return colonoscopy;
    }

    public void setColonoscopy(String colonoscopy) {
        this.colonoscopy = colonoscopy;
    }

    public String getEthnicitySide() {
        return ethnicitySide;
    }

    public void setEthnicitySide(String ethnicitySide) {
        this.ethnicitySide = ethnicitySide;
    }

    public String getIsPolypAdenomas() {
        return isPolypAdenomas;
    }

    public void setIsPolypAdenomas(String isPolypAdenomas) {
        this.isPolypAdenomas = isPolypAdenomas;
    }

    public String getCountOfPolyps() {
        return countOfPolyps;
    }

    public void setCountOfPolyps(String countOfPolyps) {
        this.countOfPolyps = countOfPolyps;
    }

    public String getIsDesmoidTumor() {
        return isDesmoidTumor;
    }

    public void setIsDesmoidTumor(String isDesmoidTumor) {
        this.isDesmoidTumor = isDesmoidTumor;
    }

    public String getHadGeneticTesting() {
        return hadGeneticTesting;
    }

    public void setHadGeneticTesting(String hadGeneticTesting) {
        this.hadGeneticTesting = hadGeneticTesting;
    }

    public String getResultOfGeneticTesting() {
        return resultOfGeneticTesting;
    }

    public void setResultOfGeneticTesting(String resultOfGeneticTesting) {
        this.resultOfGeneticTesting = resultOfGeneticTesting;
    }

    public String getColorectalPolypDetails() {
        return colorectalPolypDetails;
    }

    public void setColorectalPolypDetails(String colorectalPolypDetails) {
        this.colorectalPolypDetails = colorectalPolypDetails;
    }

    public boolean isShowBrcaTesting() {
        return showBrcaTesting;
    }

    public void setShowBrcaTesting(boolean showBrcaTesting) {
        this.showBrcaTesting = showBrcaTesting;
    }

	public String getBreastDensity() {
		return breastDensity;
	}

	public void setBreastDensity(String breastDensity) {
		this.breastDensity = breastDensity;
	}

	public String getBreastDensityMeasurementType() {
		return breastDensityMeasurementType;
	}

	public void setBreastDensityMeasurementType(String breastDensityMeasurementType) {
		this.breastDensityMeasurementType = breastDensityMeasurementType;
	}

    public String getYearOfGeneticTesting() {
        return yearOfGeneticTesting;
    }

    public void setYearOfGeneticTesting(String yearOfGeneticTesting) {
        this.yearOfGeneticTesting = yearOfGeneticTesting;
    }

    public String getPreviousBiopsy() {
        return previousBiopsy;
    }

    public void setPreviousBiopsy(String previousBiopsy) {
        this.previousBiopsy = previousBiopsy;
    }

    public String getBreastBiopsyResult() {
        return breastBiopsyResult;
    }

    public void setBreastBiopsyResult(String breastBiopsyResult) {
        this.breastBiopsyResult = breastBiopsyResult;
    }

    public String getAbnormalBreastBiopsyResult() {
        return abnormalBreastBiopsyResult;
    }

    public void setAbnormalBreastBiopsyResult(String abnormalBreastBiopsyResult) {
        this.abnormalBreastBiopsyResult = abnormalBreastBiopsyResult;
    }
}
