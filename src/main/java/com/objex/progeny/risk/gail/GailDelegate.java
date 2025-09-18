/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.progeny.risk.gail;

import com.ambrygen.dceg.bcra.consolesample.Helper;
import com.ambrygen.dceg.bcra.consolesample.Risks;
import com.objex.progeny.risk.Utils;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.gail.Risk;
import com.objex.risk.beans.gail.Person;
import com.objex.risk.beans.AffectedType;
import static com.objex.risk.beans.Germline.BRCA1;
import static com.objex.risk.beans.Germline.BRCA2;
import com.objex.risk.beans.gail.Person.Race;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jason
 */
@Service
public class GailDelegate {
     final static Logger LOG = LoggerFactory.getLogger(GailDelegate.class);
    
    public static final String GAIL_VERSION = "Gail (Updated 2011)";
    public static final String MENARCHE_ERROR = "Proband menarche outside range 7 to 39";
    public static final int EMPTY_DEFAULT = 99;
    
    
    public GailResult calculate(Risk risk) {
        GailResult result = new GailResult();
        Person proband = risk.getFamily().get(0);
        result.setVersion(GAIL_VERSION);
        
        //TODO check commented out validations
        int currentAge = defaultEmptyTo(proband.getAge(), EMPTY_DEFAULT);    //this was for values outside our system  BcptConvert.GetCurrentAge(35);
        int menarcheAge = defaultEmptyTo(proband.getMenarcheAge(), EMPTY_DEFAULT);  ; //BcptConvert.GetMenarcheAge("14");
        int firstLiveBirthAge = 0; //  BcptConvert.GetFirstLiveBirthAge("27");
        if (proband.getHasLiveBirth()){
            if (proband.getLiveBirthAge() == null ||  proband.getLiveBirthAge() == 0) {
                firstLiveBirthAge = 99;
            }
            else {
                firstLiveBirthAge = proband.getLiveBirthAge();
            }
        }
        int race = proband.getRace().value; //BcptConvert.GetRace("1");
        
        List<Person> fdrs = Utils.getFirstDegreeRelatives(risk, Person.class);
        
        int affectedFirstDegreeRelCd = getFDRCode(fdrs, proband.getRace()); //BcptConvert.GetFirstDegRelatives("1");
        int hadBiopsy = proband.isPerformedBiopsy() ? 1 : 0; //BcptConvert.GetEverHadBiopsy("1");
        int numBiopsy = EMPTY_DEFAULT;                      //BcptConvert.GetNumberOfBiopsy("1");
        if (proband.getBiopsyCount() != null) { 
            numBiopsy = proband.getBiopsyCount();
        }
        int hyperPlasia = proband.getHasHyperplasia() ? 1 : 0; //getBcptConvert.GetHyperPlasia("1");     
        int predictionAge = currentAge + risk.getAgeBy();
        
        validateData(proband, result, predictionAge, risk); 
        if (result.hasErrors()) {
            return result;
        }

        //Start with 5 year risk, and loop to honor ageBy
        while (predictionAge < risk.getAgeTo()) {
            Double absoluteRisk = riskCalculate(currentAge, predictionAge, menarcheAge, firstLiveBirthAge, hadBiopsy, numBiopsy, hyperPlasia, affectedFirstDegreeRelCd, race);
            addPrediction(result, absoluteRisk, predictionAge);
            predictionAge = predictionAge + risk.getAgeBy();
        }
        // Calculate lifetime risk. this ensures it honors ageTo for lifetime even if it isn't evenly divisible
        Double absoluteRisk = riskCalculate(currentAge, risk.getAgeTo(), menarcheAge, firstLiveBirthAge, hadBiopsy, numBiopsy, hyperPlasia, affectedFirstDegreeRelCd, race);
        addPrediction(result, absoluteRisk, risk.getAgeTo());

        
        
        return result;
    }

    protected void addPrediction(GailResult result, Double absoluteRisk, int predictionAge) {
        Prediction pred = result.addPrediction();
        pred.addRisk("breastRisk", absoluteRisk);
        pred.setAge(predictionAge);
    }

    protected Double riskCalculate(int currentAge, int predictionAge, int menarcheAge, int firstLiveBirthAge, int hadBiopsy, int numBiopsy, int hyperPlasia, int affectedFirstDegreeRelCd, int race) {
        Risks risks = Helper.RiskCalc(0, currentAge, predictionAge, menarcheAge, firstLiveBirthAge, hadBiopsy, numBiopsy, hyperPlasia, affectedFirstDegreeRelCd, race);
        Risks risksPctg = Helper.CalcPercentage(risks.getAbsoluteRisk(), risks.getAverageRisk());
        LOG.debug("This woman (to age " +  predictionAge + "): " + String.format("%.1f", risksPctg.getAbsoluteRisk()));  // absRiskPctg.ToString("F1")
        LOG.debug("Average woman (to age " +  predictionAge + "): " + String.format("%.1f", risksPctg.getAverageRisk())); //avgRiskPctg.ToString("F1")
        return risksPctg.getAbsoluteRisk()/100;
    }

    protected void validateData(Person proband, GailResult result,int firstPredictionAge, Risk risk) {            
        if (proband.getAge() != 0 && risk.getAgeTo() < firstPredictionAge) {
            result.addError(new RiskError(proband.getId(),"Proband age is too high","ageTooHighForModelSettings"));
        }
        if (proband.getGender() == 1) {
            result.addError(new RiskError(proband.getId(),"Proband can not be male","probandCannotBeMale"
            ));
        }
        if (proband.getAge() < 35) {
            result.addError(new RiskError(proband.getId(),"Proband Must be at least 35","probandMustBeAtLeastThirtyFive"));
        }
        if (proband.getAffectedBreastType().isAffected()) {
             result.addError(new RiskError(proband.getId(),"Unsupported Cancer for Proband","unsupportedCancerForProband"));
        }
        if (proband.getHasLCIS()) {
            result.addError(new RiskError(proband.getId(),"Unsupported LCIS for Proband","unsupportedLCISForProband"));
        }
        if (proband.getHasDCIS()) {
            result.addError(new RiskError(proband.getId(),"Unsupported DCIS for Proband","unsupportedDCISForProband"));
        }
        if (proband.getHasMantleRadiation()) {
             result.addError(new RiskError(proband.getId(),"Unsupported Radiation for Proband","unsupportedRadiationForProband"));
        }
        if (proband.hasGermline() && (proband.getGermline(BRCA1) == TestResult.POSITIVE || proband.getGermline(BRCA2) == TestResult.POSITIVE)) { 
              result.addError(new RiskError(proband.getId(),"Unsupported Positive test","unsupportedPositiveTest"));
        }
        if (proband.getMenarcheAge() > 0 && proband.getMenarcheAge() < 7) {
            result.addError(new RiskError(proband.getId(),"Menarche Below Seven","menarcheBelowSeven"));
        }
        if (proband.getMenarcheAge() > 39) {
            result.addError(new RiskError(proband.getId(),"Menarche Above Thirty Nine","menarcheAboveThirtyNine"));
        }
        if (proband.getRace() == null) {
             result.addError(new RiskError(proband.getId(),"Missing Race for Proband","missingRace"));
        }
        else if (proband.getRace() == Race.Unsupported) {
             result.addError(new RiskError(proband.getId(),"Missing sub Race for Proband","missingSubRace"));
        } 
        if (proband.getHasLiveBirth() && proband.getLiveBirthAge() != null) {
           if (proband.getLiveBirthAge() < 10) {
               result.addError(new RiskError(proband.getId(),"Live Birth Age below 10","liveBirthAgeBelowTen"));
            } else if (proband.getLiveBirthAge() > 55) {
               result.addError(new RiskError(proband.getId(),"Live Birth Age above 55","liveBirthAgeAboveFiftyFive"));
            }   
        }
        if (proband.getLiveBirthAge() != null && proband.getLiveBirthAge() > 0) {
            if (!proband.getHasLiveBirth()) {
                result.addError(new RiskError(proband.getId(),"Missing Live Birth Age","missingLiveBirth"));
            }
        }
        if (proband.isPerformedBiopsy() && (proband.getBiopsyCount() == 0)) {
            result.addError(new RiskError(proband.getId(),"No Biopsy Count for Biopsy","noBiopsyCountForBiopsy"));
       }
        if (proband.getBiopsyCount() > 0 && !proband.isPerformedBiopsy()) {
            result.addError(new RiskError(proband.getId(),"Biopsy Count with no Biopsy","biopsyCountWithNoBiopsy"));
        }
         
    }

    protected int getFDRCode(List<Person> fdrs, Race race) {
        int affectedCode = 0;
        for (Person person : fdrs) {
            if (person.getAffectedBreastType() != AffectedType.NONE) {
                affectedCode++;
            }
        }
        if (affectedCode >= 2 && race.value >= 7) {
            affectedCode = 1;
        } else if (affectedCode > 2) {
            affectedCode = 2;
        }
        return affectedCode;
    }
    public Integer defaultEmptyTo(Integer age, int defaultVal) {
        if (age == null || age == 0) {
            return defaultVal;
        }
        return age;
    }
   
    
}
