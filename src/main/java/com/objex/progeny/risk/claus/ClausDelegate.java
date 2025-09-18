/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.progeny.risk.claus;

import com.objex.progeny.risk.Utils;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.FIFTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.FOURTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.SEVENTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.SIXTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.THIRTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.TWENTY;
import static com.objex.progeny.risk.claus.TableBuilder.REPORTING_INTERVAL;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.claus.Risk;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jason
 */
@Service
public class ClausDelegate {
    
     final static Logger LOG = LoggerFactory.getLogger(ClausDelegate.class);
    
    public static final String CLAUS_VERSION = "Claus,Risch,Thompson(1994) Claus,Risch,Thompson(1993)";

    static Map<AgeKey,Map<AgeKey,Double>> singleFDRTbl = new HashMap();
    
    static Map<AgeKey,Map<AgeKey,Double>> singleFDROvarianTbl = new HashMap();
    
    static Map<AgeKey,Map<AgeKey,Double>> twoFDROvarianTbl = new HashMap();
    static Map<AgeKey,Map<AgeKey,Double>> singleSDRTbl = new HashMap();
    static Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> twoFDRTbl = new HashMap();
    //one maternal sdr and one paternal sdr
    static Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> singleMaternalSinglePaternalSDRTbl = new HashMap();
    //two sdr either both are  paternal or maternal
    static Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> twoSDRTbl = new HashMap();
    
    //one affected FDR ovarian and one affected FDR  breast
    static Map<AgeKey,Map<AgeKey,Double>> singleFDRandOvarianTbl = new HashMap();
    
    //mother and maternal aunt
    static Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> motherAndMaternalAuntTbl = new HashMap();
    //mother and paternal aunt
    static Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> motherAndPaternalAuntTbl = new HashMap();
    static Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> singleSDRsingleFDRTbl = new HashMap();
    
    protected Comparator<Person> diagnoseAge = new Comparator () {

        @Override
        public int compare(Object o1, Object o2) {
            if (((Person)o1).getDiagnoseBreastAge() >  ((Person)o2).getDiagnoseBreastAge()) {
                return 1;
            } else if (((Person)o1).getDiagnoseBreastAge() <  ((Person)o2).getDiagnoseBreastAge()) {
                return -1;
            }
            return 0;
        }
        
    };
    
    static {
        //initTables();
        TableBuilder.buildTables();
    }
    
    /**
     * this method extrapolates years between current age and next reported value to approximate
     *   the risk value for years in between
     * @param risk
     * @param result
     * @param startRisk
     * @param predictionRisk
     * @param startAge
     * @return 
     */

    private Double buildMultiplePredictions(Risk risk,ClausResult result, Double startRisk, Double predictionRisk, Integer startAge) {
       
        Double ageBy = Double.valueOf(risk.getAgeBy());
        Integer predictionAge = startAge;
        Double partialRiskRatio = risk.getAgeBy() / REPORTING_INTERVAL;
        Double marginalRisk = predictionRisk - startRisk;
        Double partialRisk = startRisk;
       
        while (predictionAge < (startAge + REPORTING_INTERVAL.intValue())) { 
            predictionAge = predictionAge + ageBy.intValue();
            partialRisk = startRisk + (marginalRisk * partialRiskRatio);
            if (predictionAge < risk.getAgeTo()) {
                AgeKey predictionAgeKey = AgeKey.forAge(predictionAge);
                if (predictionAgeKey == AgeKey.UNSUPPORTED) {
                    continue; //don't build prediction for unsupported age.
                }
                buildPrediction(result, partialRisk,predictionAge);
                partialRiskRatio = partialRiskRatio + (risk.getAgeBy() / REPORTING_INTERVAL);
            }
        }
        return partialRisk;
    }
    
    
    /**
     *   EIGHTY(80,89) supporting 80 would mean changing the builder logic to handle variable
     * lifetimes between scenarios.  Have decided we won't do this
     */
  
    
    public enum AgeKey{ TWENTY(20,29),THIRTY(30,39),FOURTY(40,49),FIFTY(50,59),SIXTY(60,69),SEVENTY(70,79), UNSUPPORTED(-1,-1);
            int min;
            int max;
            AgeKey(int min, int max) {
                this.min = min;
                this.max = max;
            }
            public boolean in(int age) {
                return age >= min && age <= max;
            }
            static AgeKey forAge(Integer age) {
                for (AgeKey key : AgeKey.values()) {
                    if (key.in(age)) {
                        return key;
                    }
                }
                return UNSUPPORTED;
            }
            public int getMax() {
                return max;
            }
             public int getMin() {
                return min;
            }

    }
    /**
     * 
     * adjusts maximum age down to highest study age
     * @param diagnoseAge
     * @return 
     */

    private Integer adjustAge(Integer diagnoseAge) {
       if (diagnoseAge > 79) {
           return 79;
       }
       return diagnoseAge;
    }

    private void buildResultForMultiple(Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> selectedMultiCategoryTbl, AgeKey affectedRelativeAge, AgeKey affectedSecondaryRelativeAge,Risk risk, ClausResult result) {
        Person proband = risk.getFamily().get(0);
        AgeKey probandAgeKey = AgeKey.forAge(proband.getAge());
        Map<AgeKey, Map<AgeKey, Double>> ageTable = selectedMultiCategoryTbl.get(probandAgeKey);
        Double currentAgeTableValue = getAgeTableValue(ageTable, affectedRelativeAge, affectedSecondaryRelativeAge);
        Double twentyYearpreceedingValue = currentAgeTableValue;
   
        AgeKey preceedingAgeKey = AgeKey.forAge(proband.getAge() - REPORTING_INTERVAL.intValue());
        Double preceedingTableValue = getAgeTableValue(selectedMultiCategoryTbl.get(preceedingAgeKey), affectedRelativeAge, affectedSecondaryRelativeAge);       
        
        AgeKey predictionAgeKey = AgeKey.forAge(proband.getAge() + REPORTING_INTERVAL.intValue());
        Double startRisk =0d;
        if (predictionAgeKey != AgeKey.UNSUPPORTED) {
            Map<AgeKey, Map<AgeKey, Double>> predictionAgeTable = selectedMultiCategoryTbl.get(predictionAgeKey);
           
            Double predictionAgeValue = getAgeTableValue(predictionAgeTable, affectedRelativeAge, affectedSecondaryRelativeAge);
            Double predictionRisk = (predictionAgeValue - currentAgeTableValue) / (1 - currentAgeTableValue);
            predictionRisk = adjustPredictionForAge(predictionRisk,predictionAgeValue,preceedingTableValue,proband);
            startRisk = buildMultiplePredictions(risk,result, startRisk,predictionRisk,proband.getAge());
        }

        predictionAgeKey = AgeKey.forAge(proband.getAge() + (2 * REPORTING_INTERVAL.intValue())); //support 20 years
        if (predictionAgeKey != AgeKey.UNSUPPORTED) {
            Map<AgeKey, Map<AgeKey, Double>> predictionAgeTable = selectedMultiCategoryTbl.get(predictionAgeKey);
           
            Double predictionAgeValue = getAgeTableValue(predictionAgeTable, affectedRelativeAge, affectedSecondaryRelativeAge);
            Double predictionRisk = (predictionAgeValue - currentAgeTableValue) / (1 - currentAgeTableValue);
              predictionRisk = adjustPredictionForAge(predictionRisk,predictionAgeValue,twentyYearpreceedingValue,proband);
            buildMultiplePredictions(risk,result, startRisk,predictionRisk,(proband.getAge() + REPORTING_INTERVAL.intValue()));
        }
        
        //one more for lifetime(ageTo)
        predictionAgeKey = AgeKey.forAge(risk.getAgeTo());
        if (predictionAgeKey != AgeKey.UNSUPPORTED) {
            Map<AgeKey, Map<AgeKey, Double>> predictionAgeTable = selectedMultiCategoryTbl.get(predictionAgeKey);
           
            Double predictionAgeValue = getAgeTableValue(predictionAgeTable, affectedRelativeAge, affectedSecondaryRelativeAge);
            Double predictionRisk = (predictionAgeValue - currentAgeTableValue) / (1 - currentAgeTableValue);
           
           // Double oldpredictionRisk = adjustPredictionByAgeRatio(predictionRisk, predictionAgeValue,preceedingTableValue,proband);
            predictionRisk = adjustPredictionForAge(predictionRisk,predictionAgeValue,preceedingTableValue,proband);
            //predictedRisk = predictionRisk + offsetAmt; //(riskPerYear * predictionAgeOffset);
            buildPrediction(result, predictionRisk,risk.getAgeTo());
        }
    }

    protected Double adjustPredictionForAge(Double predictionRisk,Double toAgeTableValue,Double fromeTableValue,  Person proband) {
        AgeKey probandAgeKey = AgeKey.forAge(proband.getAge());
        AgeKey preceedingAgeKey = AgeKey.forAge(proband.getAge() - REPORTING_INTERVAL.intValue());
        if (probandAgeKey.getMax() - proband.getAge() != 0) {  
            Double preceedingIntervalRisk = (toAgeTableValue - fromeTableValue) / (1 - fromeTableValue);
            Double slope = (predictionRisk - preceedingIntervalRisk) /  (probandAgeKey.getMax() - preceedingAgeKey.getMax());
            Double intercept = predictionRisk - (slope * probandAgeKey.getMax());
            predictionRisk = slope * proband.getAge() + intercept;
        }
        return predictionRisk;
    }

    private ClausResult calcMultiAffected(List<Person> affectedBreasts, Risk risk,  Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> multiTbl) {
         ClausResult result = null;
        if (affectedBreasts.size() > 1) {
            AgeKey firstKey = getAffectedAgeKey(affectedBreasts, true);
            AgeKey secondKey = getAffectedAgeKey(affectedBreasts, false);
            if (firstKey != AgeKey.UNSUPPORTED && secondKey != AgeKey.UNSUPPORTED) {
                result = new ClausResult();
                buildResultForMultiple(multiTbl, firstKey, secondKey, risk, result);
            }
        }
        return result;
    }
     private ClausResult calcMultiAffected(Person person,List<Person> affectedBreasts, Risk risk,  Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> multiTbl) {
         ClausResult result = null;
        if (affectedBreasts.size() > 0 && person != null && person.getAffectedBreastType().isAffected()) {
            AgeKey firstKey = AgeKey.forAge(adjustAge(person.getDiagnoseBreastAge()));
            AgeKey secondKey = getAffectedAgeKey(affectedBreasts, true);
            if (firstKey != AgeKey.UNSUPPORTED && secondKey != AgeKey.UNSUPPORTED) {
                result = new ClausResult();
                buildResultForMultiple(multiTbl, firstKey, secondKey, risk, result);
            }
        }
        return result;
    }
     
    private ClausResult calcMultiAffected(List<Person> firstAffecteds, List<Person> secondAffecteds, Risk risk,  Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> multiTbl) {
         ClausResult result = null;
        if (firstAffecteds.size() > 0 && secondAffecteds.size() > 0) {
            AgeKey firstKey = getAffectedAgeKey(firstAffecteds, true);
            AgeKey secondKey = getAffectedAgeKey(secondAffecteds, true);
            if (firstKey != AgeKey.UNSUPPORTED && secondKey != AgeKey.UNSUPPORTED) {
                result = new ClausResult();
                if (firstKey.getMin() <= secondKey.getMin()) {
                     buildResultForMultiple(multiTbl, firstKey, secondKey, risk, result);
                } else {
                    buildResultForMultiple(multiTbl, secondKey, firstKey, risk, result);
                }
            }
        }
        return result;
    }
    

    private ClausResult calcSingleBreastProb(List<Person> affectedBreasts, Risk risk, Map<AgeKey,Map<AgeKey,Double>> singleTbl) {
        ClausResult result = null;
        if (!affectedBreasts.isEmpty()) {
            AgeKey key = getAffectedAgeKey(affectedBreasts, true);
            if (key != AgeKey.UNSUPPORTED) {
                result = new ClausResult();
                buildResultForSingle(singleTbl,  key, risk, result);
            }
        }
        return result;
    }
    /**
     * handle both exclusive ovarian scenarios
     * @param affectedOvs
     * @param risk
     * @return 
     */
     private ClausResult calcOvarianProb(List<Person> affectedOvs, Risk risk) {
        ClausResult result = null;
        if (affectedOvs.size() > 1) {
            result = new ClausResult();
            buildResultForSingle(twoFDROvarianTbl, AgeKey.THIRTY, risk, result);
        } else if (affectedOvs.size() == 1) {
            result = new ClausResult();
            buildResultForSingle(singleFDROvarianTbl, AgeKey.THIRTY, risk, result);   
        }
        return result;
    }
       private ClausResult calcOvAndBreastProb(List<Person> affectedOvs, List<Person> affectedBreasts, Risk risk) {
        ClausResult result = null;
        if (affectedOvs.isEmpty() || affectedBreasts.isEmpty()) {
           //do nothing
        } else  {
            AgeKey key = getAffectedAgeKey(affectedBreasts, true);
            if (key != AgeKey.UNSUPPORTED) {
                result = new ClausResult();
                buildResultForSingle(singleFDRandOvarianTbl, key, risk, result);
            }
        }
        return result;
    }
     
    
    /*
     protected Double adjustPredictionByAgeRatio(Double predictionRisk,Double toAgeTableValue,Double fromAgeTableValue,  Person proband) {
        AgeKey probandAgeKey = AgeKey.forAge(proband.getAge());
        int predictionAgeOffset = probandAgeKey.getMax() - proband.getAge();
        Double ageOffsetRatio = (predictionAgeOffset / REPORTING_INTERVAL);
        if (probandAgeKey.getMax() - proband.getAge() != 0) {  
            Double preceedingIntervalRisk = (toAgeTableValue - fromAgeTableValue) / (1 - fromAgeTableValue);
             
             predictionRisk = predictionRisk - (predictionRisk -  preceedingIntervalRisk) * ageOffsetRatio;
        }
        return predictionRisk;
    } */
     
    /*
     protected Double adjustPredictionByLowValue(Double predictionRisk,Double toAgeTableValue,Double fromAgeTableValue,  Person proband) {
        AgeKey probandAgeKey = AgeKey.forAge(proband.getAge() - 10);
        int predictionAgeOffset = proband.getAge() - probandAgeKey.getMax();
        Double ageOffsetRatio = (predictionAgeOffset / REPORTING_INTERVAL);
        if (probandAgeKey.getMax() - proband.getAge() != 0) {  
            Double preceedingIntervalRisk = (toAgeTableValue - fromAgeTableValue) / (1 - fromAgeTableValue);
            Double oldRiskPerYear = preceedingIntervalRisk / REPORTING_INTERVAL;
            //Double riskPerYear = predictionRisk  / REPORTING_INTERVAL;
            //Double riskIncreasePerYear = riskPerYear - oldRiskPerYear; //deriv
             
             predictionRisk =  preceedingIntervalRisk + (((preceedingIntervalRisk / 2) - fromAgeTableValue) * ageOffsetRatio);
        }
        return predictionRisk;
     }
    */
     /*
      protected Double adjustPredictionBySplitAgeRatio(Double predictionRisk,Double toAgeTableValue,Double fromAgeTableValue,  Person proband) {
        AgeKey probandAgeKey = AgeKey.forAge(proband.getAge());
        int predictionAgeOffset = probandAgeKey.getMax() - proband.getAge();
        Double ageOffsetRatio = (predictionAgeOffset / REPORTING_INTERVAL);
        if (probandAgeKey.getMax() - proband.getAge() != 0) {  
            Double preceedingIntervalRisk = (toAgeTableValue - fromAgeTableValue) / (1 - fromAgeTableValue);
            Double oldRiskPerYear = preceedingIntervalRisk / REPORTING_INTERVAL;
            Double riskPerYear = predictionRisk  / REPORTING_INTERVAL;
            Double riskIncreasePerYear = riskPerYear - oldRiskPerYear; //deriv
             
             predictionRisk = predictionRisk - (predictionRisk -  preceedingIntervalRisk) * ageOffsetRatio;
        }
        return predictionRisk;
    }
*/
    
    public ClausResult calculate(Risk risk) {
        ClausResult result = null;
        ClausResult errorResult = new ClausResult();
        Person proband = risk.getFamily().get(0);
        validateData(risk.getFamily().get(0), errorResult, risk); 
        if (errorResult.hasErrors()) {
            errorResult.setVersion(CLAUS_VERSION); 
             return errorResult;
         }
        List<Person> parents = Utils.getParents(proband, risk);
        Person mother = null;
        Person father = null;
        for (Person parent : parents) {
            if (parent.isMother(proband)) {
                mother = parent;
            } else {
                father = parent;
            }
        }
        
        List<Person> affectedMatRels = getOrderedAffectedBreast(Utils.getFirstDegreeRelatives(mother, risk, Person.class));
        List<Person> affectedPatRels = getOrderedAffectedBreast(Utils.getFirstDegreeRelatives(father, risk, Person.class));
        List<Person> affectedMatAunts = Utils.filterSex(Utils.getSiblings(mother, affectedMatRels), true);
        List<Person> affectedPatAunts = Utils.filterSex(Utils.getSiblings(father, affectedPatRels), true);
        List<Person> fdrs = Utils.getFirstDegreeRelatives(risk, Person.class);
        List<Person> sdrs = Utils.getSecondDegreeRelatives(risk, Person.class);
        List<Person> affectedBreastFDRs = getOrderedAffectedBreast(fdrs);
        List<Person> affectedBreastSDRs = getOrderedAffectedBreast(sdrs);
        List<Person> affectedOvarianFDRs = getOrderedAffectedOvarian(fdrs);
          
        result = high(calcSingleBreastProb(affectedBreastFDRs, risk, singleFDRTbl),result); //single fdr
        result = high(calcSingleBreastProb(affectedBreastSDRs, risk, singleSDRTbl),result); //single sdr
        result = high(calcOvarianProb(affectedOvarianFDRs, risk),result); // single and multi fdr ovarian
        result = high(calcOvAndBreastProb(affectedOvarianFDRs, affectedBreastFDRs, risk),result); //one breast /one ovarian
        
        result = high(calcMultiAffected(affectedBreastFDRs, risk, twoFDRTbl),result); //two fdr
        result = high(calcMultiAffected(affectedMatRels, risk, twoSDRTbl),result); //two sdr maternal
        result = high(calcMultiAffected(affectedPatRels, risk, twoSDRTbl),result); //two sdr paternal
        result = high(calcMultiAffected(affectedMatRels, affectedPatRels,risk, singleMaternalSinglePaternalSDRTbl),result); //one paternal one maternal sdr
        result = high(calcMultiAffected(mother,affectedMatAunts, risk,motherAndMaternalAuntTbl),result); //mother and maternal aunt
        result = high(calcMultiAffected(mother,affectedPatAunts, risk,motherAndPaternalAuntTbl),result); //mother and paternal aunt
        if (result == null) {
            LOG.info("No scenario found");
            result = new ClausResult(); 
            buildPrediction(result, null,proband.getAge());
            buildPrediction(result, null,risk.getAgeTo());
        }
        
        /* TODO remove when stable
        Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>>  selectedMultiCategoryTbl = getMultiSelectTable(affectedBreastSDRs.size(), affectedBreastFDRs.size(),mother,father, risk);
        Map<AgeKey,Map<AgeKey,Double>> selectedSingleCategoryTbl = getSingleSelectTable(affectedBreastSDRs.size(), affectedBreastFDRs.size(),affectedOvarianFDRs.size());
        
        AgeKey affectedFirstRelativeAge = getAffectedAgeKey(affectedBreastFDRs, affectedBreastSDRs, affectedOvarianFDRs,true);
        AgeKey affectedSecondaryRelativeAge = getAffectedAgeKey(affectedBreastFDRs, affectedBreastSDRs, affectedOvarianFDRs, false);
        boolean unsupported = false;
        if (AgeKey.forAge(proband.getAge()) == AgeKey.UNSUPPORTED || affectedFirstRelativeAge == AgeKey.UNSUPPORTED) {
            unsupported = true; //result.addError(new RiskError("Unsupported Age for Proband or Affected Relative"));
            LOG.info("Unsupported Age for Proband or Affected Relative");
        } 
        
        if (unsupported || selectedSingleCategoryTbl.isEmpty() && selectedMultiCategoryTbl.isEmpty()) {
            LOG.info("No scenario found");
            buildPrediction(result, null,proband.getAge());
            buildPrediction(result, null,risk.getAgeTo());
        }
        else if (selectedMultiCategoryTbl.isEmpty()) { //bolth could have values since we expanded defs
            buildResultForSingle(selectedSingleCategoryTbl,  affectedFirstRelativeAge, risk, result);
        } else {
            buildResultForMultiple(selectedMultiCategoryTbl, affectedFirstRelativeAge, affectedSecondaryRelativeAge, risk, result);
        }
        */
        result.setVersion(CLAUS_VERSION);  
        return result;
    }

    protected void buildResultForSingle(Map<AgeKey, Map<AgeKey, Double>> selectedSingleCategoryTbl,  AgeKey affectedRelativeAge,  Risk risk, ClausResult result) {
        Person proband = risk.getFamily().get(0);
        AgeKey probandAgeKey = AgeKey.forAge(proband.getAge());
        AgeKey preceedingAgeKey = AgeKey.forAge(proband.getAge() - REPORTING_INTERVAL.intValue());
        
        Double currentAgeValue = getAgeTableValue(selectedSingleCategoryTbl, probandAgeKey, affectedRelativeAge);
        Double preceedingTableValue = 0d;
        
        if (preceedingAgeKey != AgeKey.UNSUPPORTED) {
            preceedingTableValue = getAgeTableValue(selectedSingleCategoryTbl,preceedingAgeKey, affectedRelativeAge);
        }
      
        int predictionAge = proband.getAge() + REPORTING_INTERVAL.intValue();
        int ageOffset = probandAgeKey.getMax() - proband.getAge();
        Double ageRatio = 1 - ageOffset / REPORTING_INTERVAL;
       
        Double ageByRatio = risk.getAgeBy() / REPORTING_INTERVAL;
              
        while (predictionAge < risk.getAgeTo()) {
                //Double currentRatio =  ageByRatio;
                AgeKey predictionAgeKey = AgeKey.forAge(predictionAge);
                if (predictionAgeKey == AgeKey.UNSUPPORTED) {
                    break;
                }
                Double predictionAgeValue = getAgeTableValue(selectedSingleCategoryTbl, predictionAgeKey, affectedRelativeAge);
                
                while (ageByRatio <= 1) {
                     buildPrediction(result, predictionAgeValue, currentAgeValue, (int) (proband.getAge() + (ageByRatio * REPORTING_INTERVAL)), ageRatio * ageByRatio);
                     //currentRatio = currentRatio + ageByRatio;
                     ageByRatio = ageByRatio + (risk.getAgeBy() / REPORTING_INTERVAL);
                }
                predictionAge = predictionAge + REPORTING_INTERVAL.intValue();
                
        }
        
        
        //one more for age to for us it would never hit this
        AgeKey predictionAgeKey = AgeKey.forAge(risk.getAgeTo());
       
        if (predictionAgeKey != AgeKey.UNSUPPORTED) {
            Double predictionAgeValue = getAgeTableValue(selectedSingleCategoryTbl, predictionAgeKey, affectedRelativeAge);
            Double predictionRisk = (predictionAgeValue - currentAgeValue) / (1 - currentAgeValue);
            predictionRisk = adjustPredictionForAge(predictionRisk,predictionAgeValue,preceedingTableValue,proband);
            buildPrediction(result, predictionRisk,risk.getAgeTo());
        }
    }
    
     private AgeKey getAffectedAgeKey(List<Person> affectedFDRs, boolean firstAffected) {
        int index = firstAffected ? 0: 1;
        AgeKey resp = AgeKey.UNSUPPORTED;
        if (affectedFDRs.size() > index) {
            resp = AgeKey.forAge(adjustAge(affectedFDRs.get(index).getDiagnoseBreastAge()));
       }  //else if (affectedOvaianFDRs.size() > 0)  { /* this is because age doesn't matter for ovarian of fdr */
         //   resp = AgeKey.THIRTY;
        //}
       return resp;
    }

    private AgeKey getAffectedAgeKey(List<Person> affectedFDRs, List<Person> affectedSDRs,  List<Person> affectedOvaianFDRs, boolean firstAffected) {
        int index = firstAffected ? 0: 1;
        AgeKey resp = AgeKey.UNSUPPORTED;
        if (!affectedSDRs.isEmpty() && !affectedFDRs.isEmpty()) {
           AgeKey tempFDR = AgeKey.forAge(adjustAge(affectedFDRs.get(0).getDiagnoseBreastAge()));
           AgeKey tempSDR = AgeKey.forAge(adjustAge(affectedSDRs.get(0).getDiagnoseBreastAge()));
           if (firstAffected) {
              resp =  tempFDR.getMax() <= tempSDR.getMax()? tempFDR : tempSDR;
           } else {
              resp =  tempFDR.getMax() > tempSDR.getMax()? tempFDR : tempSDR;
           }
        }
        if (affectedFDRs.size() > index) {
            resp = AgeKey.forAge(adjustAge(affectedFDRs.get(index).getDiagnoseBreastAge()));
        } else if (affectedSDRs.size() > index) {
             resp = AgeKey.forAge(adjustAge(affectedSDRs.get(index).getDiagnoseBreastAge()));
        }  else if (affectedOvaianFDRs.size() > 0)  { /* this is because age doesn't matter for ovarian of fdr */
            resp = AgeKey.THIRTY;
        }
       return resp;
    }

    protected Map<AgeKey, Map<AgeKey, Double>> getSingleSelectTable(int affectedSDRs, int affectedFDRs, int affectedOvaraianFDRs) {
        //make this conditional what happens when more than two, what happens if none of these are met?
        Map<AgeKey,Map<AgeKey,Double>> selectedSingleCategoryTbl = new HashMap();
        if (affectedSDRs >= 0 && affectedFDRs == 1) { //changed to gt.  this may not get used if a scenario matches from sdrs
            if (affectedOvaraianFDRs == 1) {
                selectedSingleCategoryTbl = singleFDRandOvarianTbl;
            } else {
                selectedSingleCategoryTbl = singleFDRTbl;
            }
        } else if (affectedSDRs == 1 && affectedFDRs == 0) {
            selectedSingleCategoryTbl = singleSDRTbl;
        } else if (affectedSDRs == 0 && affectedFDRs == 0) {
            if (affectedOvaraianFDRs == 1) {
                selectedSingleCategoryTbl = singleFDROvarianTbl;
            } else if (affectedOvaraianFDRs >= 2) { // gt added here to include more scenarios
                selectedSingleCategoryTbl = twoFDROvarianTbl;
            }
        }
        return selectedSingleCategoryTbl;
    }
    
     protected  Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> getMultiSelectTable(int sdrCount,  int fdrCount, Person mother, Person father,Risk risk) {
        List<Person> affectedMatRels = getOrderedAffectedBreast(Utils.getFirstDegreeRelatives(mother, risk, Person.class));
        List<Person> affectedPatRels = getOrderedAffectedBreast(Utils.getFirstDegreeRelatives(father, risk, Person.class));
        List<Person> affectedMatAunts = Utils.filterSex(Utils.getSiblings(mother, affectedMatRels), true);
        List<Person> affectedPatAunts = Utils.filterSex(Utils.getSiblings(father, affectedPatRels), true);
       
        Map<AgeKey,Map<AgeKey,Map<AgeKey,Double>>> selectedMultiCategoryTbl = new HashMap();
        if (sdrCount == 0 && fdrCount >= 2) { //adjusted to gt
            selectedMultiCategoryTbl = twoFDRTbl;
        } else if (sdrCount >= 2 && fdrCount == 0) {  //adjust to gt
            if (affectedMatRels.size() >= 1 && affectedPatRels.size() >= 1) {
                selectedMultiCategoryTbl = singleMaternalSinglePaternalSDRTbl; 
            } else {
                selectedMultiCategoryTbl = twoSDRTbl;
            }
        } else if (sdrCount >= 1 && fdrCount >= 1) { //adjusted to gt
             if (mother.getAffectedBreastType().isAffected() && affectedMatAunts.size() >= 1) {
                 selectedMultiCategoryTbl = motherAndMaternalAuntTbl;
             } else if (mother.getAffectedBreastType().isAffected() && affectedPatAunts.size() >= 1) {
                     selectedMultiCategoryTbl = motherAndPaternalAuntTbl;
             } else {
                  selectedMultiCategoryTbl = singleSDRsingleFDRTbl;
             }
        }
        return selectedMultiCategoryTbl;
    }

    protected void buildPrediction(ClausResult result, Double predictionAgeValue, Double currentAgeValue, int predictionAge, Double ageByTotalRatio) {
        Prediction pred = result.addPrediction();
        Double predictedRisk = (predictionAgeValue - currentAgeValue) / (1 - currentAgeValue);
        pred.addRisk("breastRisk", predictedRisk * ageByTotalRatio);
        pred.setAge(predictionAge);
    }
    
    protected void buildPrediction(ClausResult result, Double predictedRisk, int predictionAge) {
        Prediction pred = result.addPrediction();
        pred.addRisk("breastRisk", predictedRisk);
        pred.setAge(predictionAge);
    }

    private List<Person> getOrderedAffectedBreast(List<Person> relatives) {
        List<Person> affecteds = new ArrayList();
        for (Person relative : relatives) {
            if (relative.getAffectedBreastType() != AffectedType.NONE) {
               affecteds.add(relative);
            }
        }
        Collections.sort(affecteds, diagnoseAge);
        return affecteds;
    }
    
     private List<Person> getOrderedAffectedOvarian(List<Person> relatives) {
        List<Person> affecteds = new ArrayList();
        for (Person relative : relatives) {
            if (relative.getAffectedOvarianType() != AffectedType.NONE) {
               affecteds.add(relative);
            }
        }
        Collections.sort(affecteds, diagnoseAge);
        return affecteds;
    }

   
    public Double getAgeTableValue(Map<AgeKey,Map<AgeKey,Double>> affectedCategoryTable ,AgeKey probandKey, AgeKey affectedRelativeAgeKey) {
        return affectedCategoryTable.get(probandKey).get(affectedRelativeAgeKey);
    }
    public ClausResult high(ClausResult newRes, ClausResult current) {
        if (newRes != null &&  newRes.getPredictions().get(newRes.getPredictions().size() - 1).getResult("breastRisk") != null) {
            Double newProb = newRes.getPredictions().get(newRes.getPredictions().size() - 1).getResult("breastRisk");
            if (current == null || current.getPredictions().get(current.getPredictions().size() - 1).getResult("breastRisk") == null 
                    || newProb > current.getPredictions().get(current.getPredictions().size() - 1).getResult("breastRisk")) {
                return newRes;
            }
        }
        return current;
    }
    /**
   * validate the incoming risk family data
   * @param proband
   * @param result
   * @param risk 
   */ 
  protected void validateData(Person proband, ClausResult result, Risk risk) {  

      if (proband.getAge() >= risk.getAgeTo()) {
          result.addError(new RiskError(proband.getId(),"Age Too High for Model Settings","ageTooHighForModelSettings"));
      }
      if (proband.getGender() == 1) {
          result.addError(new RiskError(proband.getId(),"Proband Cannot Be Male","probandCannotBeMale"));
      }
      if (proband.getAge() < 29) {
              result.addError(new RiskError(proband.getId(),"Proband Must be at least 29","probandMustBeAtLeastTwentyNine"));
      }
      if (proband.getAge() > 79) {
           result.addError(new RiskError(proband.getId(),"Proband age over 79","probandAgeOverSeventyNine"));
      }
      if (proband.getAffectedBreastType().isAffected() || proband.getAffectedOvarianType().isAffected()) {
            result.addError(new RiskError(proband.getId(),"Unsupported Cancer for Proband","unsupportedCancerForProband"));
      }
      
      for (Person member : risk.getFamily()) {
             validateMemberData(member, result);
      }
   
  }
  
    /**
   * 
   * @param member
   * @param result
   * @param risk 
   */
  private void validateMemberData(Person member, ClausResult result) {
          if (member.getAffectedOvarianType().isAffected() && member.getDiagnoseOvarianAge() == 0) {
              result.addError(new RiskError(member.getId(),"Missing Age Ovarian","missingAgeOvarian"));
          }
              
          if (member.getAffectedBreastType().isAffected() && member.getDiagnoseBreastAge() < 20  && member.getDiagnoseBreastAge() != 0) {
              result.addError(new RiskError(member.getId(),"Affected Breast Age Under 20","affectedBreastAgeUnderTwenty"));
          }
          if (member.getAffectedBreastType().isAffected() && member.getDiagnoseBreastAge() == 0) {
              result.addError(new RiskError(member.getId(),"Missing Age Breast","missingAgeBreast"));
          }
  }
}
