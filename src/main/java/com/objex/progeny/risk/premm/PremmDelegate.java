/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.progeny.risk.premm;

import com.objex.progeny.risk.Utils;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.premm.Person;
import static com.objex.risk.beans.premm.Person.MLH1;
import static com.objex.risk.beans.premm.Person.MSH2;
import static com.objex.risk.beans.premm.Person.MSH6;
import static com.objex.risk.beans.premm.Person.PMS2;
import com.objex.risk.beans.premm.Risk;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jason
 */
@Service
public class PremmDelegate {
    final static Logger LOG = LoggerFactory.getLogger(PremmDelegate.class);

    public static final String PREMM_VERSION = "PREMM5 © 2018 DFCI. All Rights Reserved. PREMM® is a registered trademark of Dana-Farber Cancer Institute, Inc.";
    public static final Integer PROB_ENDO_MIN = 31;
    public static final Integer PROB_ENDO_MAX = 67;
    public static final Integer FDR_ENDO_MIN = 28;
    public static final Integer FDR_ENDO_MAX = 69;
    public static final Integer SDR_ENDO_MIN = 34;
    public static final Integer SDR_ENDO_MAX = 70;
    public static final Integer PROB_COLON_MIN = 27;
    public static final Integer PROB_COLON_MAX = 77;
    public static final Integer FDR_COLON_MIN = 28;
    public static final Integer FDR_COLON_MAX = 81;
    public static final Integer SDR_COLON_MIN = 30;
    public static final Integer SDR_COLON_MAX = 85;

     final static int BASE_AGE = 45;
    final static com.objex.risk.beans.premm.Person NONE = new com.objex.risk.beans.premm.Person();
    static{
        NONE.setFatherId(-1);
        NONE.setMotherId(-1);
    }

    public PREMMResult calculate(Risk risk) {
        PREMMResult result = new PREMMResult();
        Person proband = (Person) risk.getProband();
        validateData(proband, result, risk);
        if (result.hasErrors()) {
        	result.setVersion(PREMM_VERSION);
            return result;
        }
        List<Person> firstDegreeRelatives = Utils.getFirstDegreeRelatives(risk, Person.class);
        List<Person> matFDRs = new ArrayList(firstDegreeRelatives);
        List<Person> patFDRs = new ArrayList(firstDegreeRelatives);
        List<Person> parents = Utils.getParents(proband, risk);
        for (Person parent : parents) {
            if (parent.isMother(proband)) {
                patFDRs.remove(parent);
            } else {
                 matFDRs.remove(parent);
            }
        }

        List<Person> secondDegreeRelatives = Utils.getSideOfFamilySecondDegreeRelatives(risk,Person.class,true);
        PREMMResult matResult = calculate(risk,matFDRs,secondDegreeRelatives);
        //PREMMResult matResult = calculate(risk,firstDegreeRelatives,secondDegreeRelatives);

        secondDegreeRelatives = Utils.getSideOfFamilySecondDegreeRelatives(risk,Person.class,false);
        PREMMResult patResult = calculate(risk,patFDRs,secondDegreeRelatives);
        //PREMMResult patResult = calculate(risk,firstDegreeRelatives,secondDegreeRelatives);

        LOG.debug("maternal: " + matResult.getProbabilityCarrier() + " paternal: " + patResult.getProbabilityCarrier());
        result = matResult.getProbabilityCarrier() > patResult.getProbabilityCarrier() ? matResult : patResult;
        roundResultValues(result);
        return result;
    }

    private void roundResultValues(PREMMResult result) {
    	if (result.getProbabilityCarrier() != null && !result.getProbabilityCarrier().isNaN())
    		result.setProbabilityCarrier(BigDecimal.valueOf(result.getProbabilityCarrier()).setScale(3,RoundingMode.HALF_UP).doubleValue());

	}

	protected PREMMResult calculate(Risk risk, List<Person> firstDegreeRelatives, List<Person> secondDegreeRelatives) {
        PREMMResult result;
        //List<Person> secondDegreeRelatives = Utils.getSecondDegreeRelatives(risk,Person.class);
        Person proband = risk.getFamily().get(0);
        Double colPrev = calcFamColonPrevalence(firstDegreeRelatives, secondDegreeRelatives);
        Double endoPrev = calcFamEndometriumPrevalence(firstDegreeRelatives, secondDegreeRelatives);
        Double relatedPrev = calcFamRelatedCancerPrevalence(firstDegreeRelatives, secondDegreeRelatives);
        result = calculateMutationProbability(proband,colPrev,endoPrev, relatedPrev,firstDegreeRelatives, secondDegreeRelatives);
        result.setVersion(PREMM_VERSION);
        return result;
    }

    public int countAffectedColon(List<com.objex.risk.beans.premm.Person> relatives) {
      int count = 0;
      for (com.objex.risk.beans.premm.Person person : relatives)    {
          if (person.getAffectedColonType() != AffectedType.NONE) {
              count++;
          }
      }
      return count;
    }
    public int countAffectedEndometrium(List<com.objex.risk.beans.premm.Person> relatives) {
       int count = 0;
      for (com.objex.risk.beans.premm.Person person : relatives)    {
          if (person.getAffectedEndometriumType() != AffectedType.NONE) {
              count++;
          }
      }
      return count;
    }

     /**
	 * V8 = (minimum age of CRC in the proband-45) + (minimum age of CRC in a FDR-
     *  45) + (minimum age of CRC in a SDR-45)
	 * @return
	 */

    private Integer calcFamColonAgeMultiplier(com.objex.risk.beans.premm.Person proband,List<com.objex.risk.beans.premm.Person> fdrs, List<com.objex.risk.beans.premm.Person> sdrs,int affectedFDRCount, int affectedSDRCount, String mutation) {
        int diagnoseAgeProband = BASE_AGE; //leave if unaffected or unknown
        int diagnoseAgeFDR = BASE_AGE;
        int diagnoseAgeSDR = BASE_AGE;
        if (proband.getAffectedColonType() != AffectedType.NONE  && proband.getDiagnoseColonAge() != 0) {
            diagnoseAgeProband = adjustProbandDiagnoseColAge(proband,mutation);
        }
        boolean hasSetFDR = false;
        for (com.objex.risk.beans.premm.Person pers  : fdrs) {
            if (pers.getAffectedColonType() != AffectedType.NONE  && pers.getDiagnoseColonAge() != 0) {
                int adjustedAge = adjustFDRDiagnoseColAge(pers, mutation, affectedFDRCount);
                if (hasSetFDR && adjustedAge < diagnoseAgeFDR) {
                    diagnoseAgeFDR = adjustedAge;
                } else if (!hasSetFDR) {
                    hasSetFDR = true;
                    diagnoseAgeFDR = adjustedAge;
                }
            }
        }

        boolean hasSetSDR = false;
        for (com.objex.risk.beans.premm.Person pers  : sdrs) {
            if (pers.getAffectedColonType() != AffectedType.NONE  && pers.getDiagnoseColonAge() != 0) {
                int adjustedAge = adjustSDRDiagnoseColAge(pers, mutation, affectedSDRCount);
                if (hasSetSDR && adjustedAge < diagnoseAgeSDR) {
                    diagnoseAgeSDR = adjustedAge;
                } else if (!hasSetSDR) {
                    hasSetSDR = true;
                    diagnoseAgeSDR = adjustedAge;
                }
            }
        }
        Integer val = diagnoseAgeProband - BASE_AGE + diagnoseAgeFDR - BASE_AGE + diagnoseAgeSDR - BASE_AGE;
        return val;
    }
    private Integer calcFamEndometriumAgeMultiplier(com.objex.risk.beans.premm.Person proband,List<com.objex.risk.beans.premm.Person> fdrs, List<com.objex.risk.beans.premm.Person> sdrs) {
        int diagnoseAgeProband = BASE_AGE; //leave if unaffected or unknown
        int diagnoseAgeFDR = BASE_AGE;
        int diagnoseAgeSDR = BASE_AGE;
        if (proband.getAffectedEndometriumType() != AffectedType.NONE  && proband.getDiagnoseEndometriumAge() != 0) {
            diagnoseAgeProband = doAdjust(proband.getDiagnoseEndometriumAge(),PROB_ENDO_MIN,PROB_ENDO_MAX);
        }
        boolean hasSetFDR = false;
        for (com.objex.risk.beans.premm.Person pers  : fdrs) {
            if (pers.getAffectedEndometriumType()!= AffectedType.NONE && pers.getDiagnoseEndometriumAge() != 0) {
                Integer age = doAdjust(pers.getDiagnoseEndometriumAge(),FDR_ENDO_MIN,FDR_ENDO_MAX);
                if (hasSetFDR && age < diagnoseAgeFDR) {
                    diagnoseAgeFDR = age;
                } else if (!hasSetFDR) {
                    hasSetFDR = true;
                    diagnoseAgeFDR = age;
                }
            }
        }

        boolean hasSetSDR = false;
        for (com.objex.risk.beans.premm.Person pers  : sdrs) {
            if (pers.getAffectedEndometriumType()!= AffectedType.NONE  && pers.getDiagnoseEndometriumAge() != 0) {
                Integer age = doAdjust(pers.getDiagnoseEndometriumAge(),SDR_ENDO_MIN,SDR_ENDO_MAX);
                if (hasSetSDR && age < diagnoseAgeSDR) {
                    diagnoseAgeSDR = age;
                } else if (!hasSetSDR) {
                    hasSetSDR = true;
                    diagnoseAgeSDR = age;
                }
            }
        }
        Integer val = diagnoseAgeProband - BASE_AGE + diagnoseAgeFDR - BASE_AGE + diagnoseAgeSDR - BASE_AGE;
        return val;
    }
    /**
     * 1*presence of other LS related cancer in FDR (E) + 0.5*presence of other LS
     * Other Lynch-syndrome associated cancers include ovary, stomach, small intestine, urinary tract/kidney, bile ducts, glioblastoma multiforme (brain), sebaceous gland tumors, and pancreas
     * related cancer in SDR (F)
     * @param anyFDROthCa
     * @param anySDR
     * @return
     */
    private Double calcFamRelatedCancerPrevalence(List<com.objex.risk.beans.premm.Person> fdrs, List<com.objex.risk.beans.premm.Person> sdrs) {
        boolean anyFDR = false, anySDR = false;
        for (com.objex.risk.beans.premm.Person person : fdrs)    {
            if (person.getAffectedRelatedType() != AffectedType.NONE) {
                anyFDR = true;
                break;
            }
        }

         for (com.objex.risk.beans.premm.Person person : sdrs)    {
            if (person.getAffectedRelatedType() != AffectedType.NONE) {
                anySDR = true;
                break;
            }
        }

        Double val = 1 * presence(anyFDR)  + .5 * presence(anySDR);
        return val;
    }

/**
 * fdrs firstdegree relatives sdr second degree relatives
 * 1* presence of CRC in 1 FDR (A) + 2* presence of CRC in 2 or more FDR (B) +0.5
 * presence of CRC in 1 SDR (C) +1* presence of CRC in 2 or more SDR (D)
 * @return
 */
   public Double calcFamColonPrevalence(List<com.objex.risk.beans.premm.Person> fdrs, List<com.objex.risk.beans.premm.Person> sdrs) {
       int affectFDR = countAffectedColon(fdrs);
       int affectSDR = countAffectedColon(sdrs);
       boolean singleFDRColCa = affectFDR == 1;
       boolean multiFDRColCa =  affectFDR > 1;
       boolean singleSDRColCa =  affectSDR == 1;
       boolean multiSDRColCa =  affectSDR > 1;

       Double val = 1 * presence(singleFDRColCa) + 2 * presence(multiFDRColCa) + .5 * presence(singleSDRColCa) + 1 * presence(multiSDRColCa);
       return val;
   }
   public int presence(boolean b) {
           int val = b? 1 : 0;
           return val;
   }
   public int adjustProbandDiagnoseColAge(com.objex.risk.beans.premm.Person proband, String mutation) {
       return doAdjust(proband.getDiagnoseColonAge(), PROB_COLON_MIN,PROB_COLON_MAX);
   }

    protected int doAdjust(Integer age, Integer lowerLimit, Integer upperLimit) {
        int adjusted = age;
        if (age < lowerLimit) {
            adjusted = lowerLimit;
        } else if (age > upperLimit) {
            adjusted = upperLimit;
        }
        return adjusted;
    }

    /**
     * ages of FDRs have a floor and ceiling which varies based on certain criteria
     * @param person
     * @param mutation
     * @param affectedCount
     * @return
     */
   public int adjustFDRDiagnoseColAge(com.objex.risk.beans.premm.Person person, String mutation, int affectedCount) {
       return doAdjust(person.getDiagnoseColonAge(),FDR_COLON_MIN,FDR_COLON_MAX);
   }

   public int adjustSDRDiagnoseColAge(com.objex.risk.beans.premm.Person person, String mutation, int affectedCount) {

        return doAdjust(person.getDiagnoseColonAge(),SDR_COLON_MIN,SDR_COLON_MAX);
   }

    public PREMMResult calculateMutationProbability (com.objex.risk.beans.premm.Person proband,Double prevFamColon, Double prevFamEndometrium, Double prevRelatedCancer, List firstDegreeRelatives, List secondDegreeRelatives) {

        PREMMResult result = new PREMMResult();
        Integer gender = proband.getGender();
        int v1 = presence(proband.getAffectedColonType() == AffectedType.ONE);
        int v2 = presence(proband.getAffectedColonType() == AffectedType.MULTIPLE);
        int v3 = presence(proband.getAffectedEndometriumType().isAffected());
        int v4 = presence(proband.getAffectedRelatedType().isAffected());
        int ageV10 = proband.getAge();  //TODO complicate this

        int countColFDR = countAffectedColon(firstDegreeRelatives);
        int countColSDR = countAffectedColon(secondDegreeRelatives);
        Integer affectedFamAgeEndoMult = calcFamEndometriumAgeMultiplier(proband,firstDegreeRelatives, secondDegreeRelatives);

        //could calculate in a cartesion way.  so nine calcs with mlh1 passed in every time
        Integer affectedFamAgeColonMult =  calcFamColonAgeMultiplier(proband,firstDegreeRelatives, secondDegreeRelatives, countColFDR,countColSDR, MLH1);
        Double lp_MLH1 = calc_lp_MLH1(gender, prevFamColon, prevFamEndometrium,prevRelatedCancer, affectedFamAgeColonMult,affectedFamAgeEndoMult, v1, v2, v3, v4,ageV10);

        affectedFamAgeColonMult =  calcFamColonAgeMultiplier(proband,firstDegreeRelatives, secondDegreeRelatives, countColFDR,countColSDR, MSH2);
        Double lp_MSH2 = calc_lp_MSH2_EPCAM(gender, prevFamColon, prevFamEndometrium,prevRelatedCancer, affectedFamAgeColonMult,affectedFamAgeEndoMult, v1, v2, v3, v4,ageV10);

        affectedFamAgeColonMult =  calcFamColonAgeMultiplier(proband,firstDegreeRelatives, secondDegreeRelatives, countColFDR,countColSDR, MSH6);
        Double lp_MSH6 = calc_lp_MSH6(gender, prevFamColon, prevFamEndometrium,prevRelatedCancer, affectedFamAgeColonMult,affectedFamAgeEndoMult, v1, v2, v3, v4,ageV10);

        affectedFamAgeColonMult =  calcFamColonAgeMultiplier(proband,firstDegreeRelatives, secondDegreeRelatives, countColFDR,countColSDR, PMS2);
        Double lp_PMS2 = calc_lp_PMS2(gender, prevFamColon, prevFamEndometrium,prevRelatedCancer, affectedFamAgeColonMult,affectedFamAgeEndoMult, v1, v2, v3, v4,ageV10);

        Double p_MLH1 =  Math.exp(lp_MLH1) / (1 + Math.exp(lp_MLH1) + Math.exp(lp_MSH2) + Math.exp(lp_MSH6) + Math.exp(lp_PMS2));
        result.setProbabilityMutationMLH1(p_MLH1);

        Double p_MSH2 =  Math.exp(lp_MSH2) / (1 + Math.exp(lp_MLH1) + Math.exp(lp_MSH2) + Math.exp(lp_MSH6) + Math.exp(lp_PMS2));
        result.setProbabilityMutationMSH2(p_MSH2);

        Double p_MSH6 =  Math.exp(lp_MSH6) / (1 + Math.exp(lp_MLH1) + Math.exp(lp_MSH2) + Math.exp(lp_MSH6) + Math.exp(lp_PMS2));
        result.setProbabilityMutationMSH6(p_MSH6);

        Double p_PMS2 =  Math.exp(lp_PMS2) / (1 + Math.exp(lp_MLH1) + Math.exp(lp_MSH2) + Math.exp(lp_MSH6) + Math.exp(lp_PMS2));
        result.setProbabilityMutationPMS2(p_PMS2);

        result.setProbabilityCarrier(p_MLH1 + p_MSH2 + p_MSH6 + result.getProbabilityMutationPMS2());
        return result;
    }
    private Double calc_lp_MLH1(Integer gender, Double prevFamColon,	Double prevFamEndometrium, Double prevRelatedCancer,
                    Integer affectedFamAgeColonMult, Integer affectedFamAgeEndoMult,int v1, int v2, int v3, int v4, int ageV10) {

             Double lp_MLH1 = -5.324907104 + (0.904431436 * gender) + (2.586296612 * v1) + (3.182990883 * v2) + (1.621445382 * v3) + (1.276459168 * v4) + (1.559798783 * prevFamColon) +
                            (0.804447827 * prevFamEndometrium) + (0.396806026 * prevRelatedCancer) + (-0.0556550094 * affectedFamAgeColonMult) + (0.0114821779 * affectedFamAgeEndoMult) + (-0.0475905002 * ageV10);
            return lp_MLH1;
    }
    private Double calc_lp_MSH2_EPCAM(Integer gender, Double prevFamColon,	Double prevFamEndometrium, Double prevRelatedCancer,
                    Integer affectedFamAgeColonMult, Integer affectedFamAgeEndoMult,int v1, int v2, int v3, int v4, int ageV10) {

          Double lp_MSH2_EPCAM = -4.426909972 + (0.937417162 * gender) + (1.798962478 * v1) + (2.593496141 * v2) + (1.923604342 * v3) + (1.584703266 * v4) + (1.33728284 * prevFamColon) +
                            (0.669863277 * prevFamEndometrium) + (0.607397831 * prevRelatedCancer) + (-0.0441301182 * affectedFamAgeColonMult) + (0.0002073389 * affectedFamAgeEndoMult) + (-0.0482478568 * ageV10);
            return lp_MSH2_EPCAM;
    }

    private Double calc_lp_MSH6(Integer gender, Double prevFamColon,	Double prevFamEndometrium, Double prevRelatedCancer,
                    Integer affectedFamAgeColonMult, Integer affectedFamAgeEndoMult,int v1, int v2, int v3, int v4, int ageV10) {

           		Double lp_MSH6 = -4.6749607 + (0.8156045 * gender) + (1.26494 * v1) + (-53.205025 * v2) + (1.7592287 * v3) + (0.5375488 * v4)
                     + (0.5453779 * prevFamColon) + (0.9232568 * prevFamEndometrium) + (0.3130166 * prevRelatedCancer)
                     + (-0.00946418 * affectedFamAgeColonMult) + (0.03435143 * affectedFamAgeEndoMult) + (-0.03629359 * ageV10);
		return lp_MSH6;
    }

    private Double calc_lp_PMS2(Integer gender, Double prevFamColonV5,	Double prevFamEndometriumV6, Double prevRelatedCancerV7,
                    Integer affectedFamAgeColonMultV8, Integer affectedFamAgeEndoMultV9,int v1, int v2, int v3, int v4, int currentAgeV10) {

     		Double lp_PMS2 = -4.913053672 + (0.293782393 * gender) + (0.989418119 * v1) + (-0.35391041 * v2) + (0.739108785 * v3) + (0.395308696 * v4)
                     + (-0.001777441 * prevFamColonV5) + (-0.426102907 * prevFamEndometriumV6) + (-0.105259809 * prevRelatedCancerV7)
                     + (-0.0085921222 * affectedFamAgeColonMultV8) + (0.0007508214 * affectedFamAgeEndoMultV9) + (-0.0073868337 * currentAgeV10);
		return lp_PMS2;
    }
    /**
     * 1* presence of EC in 1 FDR (A) + 2* presence of EC in 2 or more FDR (B) + 0.5*
 * presence of EC in 1 SDR (C) + 1* presence of EC in 2 or more SDR (D)
     * @return
     */

    public Double calcFamEndometriumPrevalence(List<com.objex.risk.beans.premm.Person> fdrs, List<com.objex.risk.beans.premm.Person> sdrs) {
        int affectFDR = countAffectedEndometrium(fdrs);
        int affectSDR = countAffectedEndometrium(sdrs);
        boolean singleFDREndoCa = affectFDR == 1;
        boolean multiFDREndoCa =  affectFDR > 1;
        boolean singleSDREndoCa =  affectSDR == 1;
        boolean multiSDREndoCa =  affectSDR > 1;

            Double val = 1 * presence(singleFDREndoCa) + 2 * presence(multiFDREndoCa) + .5 * presence(singleSDREndoCa) + 1 * presence(multiSDREndoCa);
            return val;
    }
    /* validate the incoming risk family data for Premm
     * @param proband
     * @param result
     * @param risk
     */
    protected void validateData(Person proband, PREMMResult result, Risk risk) {

	    if (proband.getAge() >= risk.getAgeTo()) {
	        result.addError(new RiskError(proband.getId(),"Proband age is too high","ageTooHighForModelSettings"));
	    }

	    for (Person member : risk.getFamily()) {
	    	validateMemberData(member, result);
	    }
}
/**
 * Member validations for MMR
 * @param member
 * @param result
 */
private void validateMemberData(Person member, PREMMResult result) {
    if (member.getAffectedColonType() != AffectedType.NONE && member.getDiagnoseColonAge() == 0) {
        result.addError(new RiskError(member.getId(),"Missing Age Colon","missingAgeColon"));
    }

    if (member.getAffectedEndometriumType() !=  AffectedType.NONE  && member.getDiagnoseEndometriumAge() == 0) {
        result.addError(new RiskError(member.getId(),"Missing Age Endometrium","missingAgeEndometrium"));
    }
    if (member.getGender() == 1 && member.getAffectedEndometriumType()  !=  AffectedType.NONE) {
        result.addError(new RiskError(member.getId(),"Invalid Gender for Endometrium","invalidGenderForEndometrium"));
    }
    if (member.getAge() < 0) {
        result.addError(new RiskError(member.getId(),"Invalid Age","invalidAge"));
    }
}

}
