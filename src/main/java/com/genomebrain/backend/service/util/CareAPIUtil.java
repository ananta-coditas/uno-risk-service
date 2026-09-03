package com.genomebrain.backend.service.util;

import static com.genomebrain.backend.service.util.APIConstants.GRADE_GROUP_4;
import static com.genomebrain.backend.service.util.APIConstants.GRADE_GROUP_5;
import static com.genomebrain.backend.service.util.APIConstants.HIGH_RISK_GROUP;
import static com.genomebrain.backend.service.util.APIConstants.VERY_HIGH_RISK_GROUP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genomebrain.backend.quesstionnaire.CancerDetails;
import com.genomebrain.backend.quesstionnaire.FamilyCancerDetails;
import com.genomebrain.backend.quesstionnaire.Others;
import com.genomebrain.backend.quesstionnaire.PersonalHistory;
import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.quesstionnaire.RelativeHistory;
import com.genomebrain.backend.quesstionnaire.relative.Aunt;
import com.genomebrain.backend.quesstionnaire.relative.Brother;
import com.genomebrain.backend.quesstionnaire.relative.Daughters;
import com.genomebrain.backend.quesstionnaire.relative.Father;
import com.genomebrain.backend.quesstionnaire.relative.FirstCousin;
import com.genomebrain.backend.quesstionnaire.relative.Granddaughter;
import com.genomebrain.backend.quesstionnaire.relative.Grandfather;
import com.genomebrain.backend.quesstionnaire.relative.Grandmother;
import com.genomebrain.backend.quesstionnaire.relative.Grandson;
import com.genomebrain.backend.quesstionnaire.relative.GreatAunt;
import com.genomebrain.backend.quesstionnaire.relative.GreatGranddaughter;
import com.genomebrain.backend.quesstionnaire.relative.GreatGrandfather;
import com.genomebrain.backend.quesstionnaire.relative.GreatGrandmother;
import com.genomebrain.backend.quesstionnaire.relative.GreatGrandson;
import com.genomebrain.backend.quesstionnaire.relative.GreatNephew;
import com.genomebrain.backend.quesstionnaire.relative.GreatNiece;
import com.genomebrain.backend.quesstionnaire.relative.GreatUncle;
import com.genomebrain.backend.quesstionnaire.relative.HalfAunt;
import com.genomebrain.backend.quesstionnaire.relative.HalfBrother;
import com.genomebrain.backend.quesstionnaire.relative.HalfNephew;
import com.genomebrain.backend.quesstionnaire.relative.HalfNiece;
import com.genomebrain.backend.quesstionnaire.relative.HalfSister;
import com.genomebrain.backend.quesstionnaire.relative.HalfUncle;
import com.genomebrain.backend.quesstionnaire.relative.Mother;
import com.genomebrain.backend.quesstionnaire.relative.Nephew;
import com.genomebrain.backend.quesstionnaire.relative.Niece;
import com.genomebrain.backend.quesstionnaire.relative.Sister;
import com.genomebrain.backend.quesstionnaire.relative.Son;
import com.genomebrain.backend.quesstionnaire.relative.Uncle;
import com.genomebrain.backend.service.dto.CancerFeatureDetails;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.ClinicalSummary;
import com.genomebrain.backend.service.dto.FamilyHistory;
import com.genomebrain.backend.service.dto.GuidelineDetailsDTO;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.genomebrain.backend.service.dto.PolypDetails;

import com.objex.risk.beans.uno.Cancer.CancerType;

@Component
public class CareAPIUtil{

    @Autowired
    GuideLineUtility guideLineUtility;

    ClinicalHistory probandClinicalHistory = new ClinicalHistory();
    private final String PROBAND = "Proband";

    public static final String NotListed = "NOT LISTED";
    private final String sexAtBirth = "sexAtBirth";
    private final String Ethnicity = "ethnicity";
    private final String cancerOverall = "cancerOverall";
    private final String cancerFeature = "cancerFeature";
    private final String YES = "YES";
    private final String NO = "NO";
    private final String NOT_PROVIDED = "NOT_PROVIDED";
    private final String multiplePrimaries = "multiplePrimaries";
    private final String HBOCNCCN = "HBOC NCCN met";
    private final String LYNCHNCCN = "Lynch NCCN met";
    private final String APSNCCN = "APS_APC NCCN met";
    private final String MOTHER = "Mother";
    private final String FATHER = "Father";
    private final String SISTER = "Sister";
    private final String BROTHER = "Brother";
    private final String DAUGHTER = "Daughter";
    private final String SON = "Son";
    private final String NIECE = "Niece";
    private final String NEPHEW = "Nephew";
    private final String HALF_SISTER = "Half sister";
    private final String HALF_BROTHER = "Half brother";
    private final String GRANDMOTHER = "Grandmother";
    private final String GRANDFATHER = "Grandfather";
    private final String GRANDDAUGHTER = "Granddaughter";
    private final String GRANDSON = "Grandson";
    private final String AUNT = "Aunt";
    private final String UNCLE = "Uncle";
    private final String FIRST_COUSIN_MALE = "First cousin (male)";
    private final String FIRST_COUSIN_FEMALE = "First cousin (female)";
    private final String GREAT_GRANDMOTHER = "Great grandmother";
    private final String GREAT_GRANDFATHER = "Great grandfather";
    private final String GREAT_AUNT = "Great aunt";
    private final String GREAT_UNCLE = "Great uncle";
    private final String GREAT_NEPHEW = "Great nephew";
    private final String GREAT_NIECE = "Great niece";
    private final String GREAT_GRANDDAUGHTER = "Great granddaughter";
    private final String GREAT_GRANDSON = "Great grandson";
    private final String HALF_AUNT = "Half aunt";
    private final String HALF_UNCLE = "Half uncle";
    private final String HALF_NIECE = "Half niece";
    private final String HALF_NEPHEW = "Half nephew";
    private final String OTHERS = "Others";
    private final String Sebaceous = "Sebaceous carcinoma";
    private final String Sebaceous_C = "Sebaceous Carcinoma";
    private final String Sebaceous_adenomas = "Sebaceous adenomas";
    private final String Sebaceous_Adenomas_Carcinoma = "Sebaceous Adenoma or Carcinoma";
    private final String Fallopian_Tube = "FALLOPIAN TUBE OR PERITONEAL";
    private final String Colon_Rectal = "COLON OR RECTAL";
    private final String Uterine_Endo = "UTERINE (ENDOMETRIAL)";
    private final String Small_intestine = "SMALL INTESTINE (SMALL BOWEL)";
    private final String Biliary_Tract  = "BILIARY TRACT ";
    private final String Ureter_or_Renal_Pelvis = "URETER OR RENAL PELVIS";
    private final String SkinCancerType = "SkinCancerType";
    private final String relative = "RELATIVE_";
    private final String MALE = "male";
    private final String FEMALE = "female";
    private final String Desmoids = "DESMOIDS";
    private final String Cousin = "Cousin - 1st";
    private final String otherDXAgeValue = "48";

    List<String> sideOfFamilyQuestion = Arrays.asList(
            HALF_SISTER, HALF_BROTHER, GRANDMOTHER, GRANDFATHER, AUNT, UNCLE, FIRST_COUSIN_MALE, FIRST_COUSIN_FEMALE,
            GREAT_GRANDMOTHER, GREAT_GRANDFATHER, GREAT_AUNT, GREAT_UNCLE, HALF_AUNT, HALF_UNCLE, HALF_NIECE, HALF_NEPHEW, OTHERS);

    List<String> otherDXAge = Arrays.asList("PRENATAL", "BIRTH", "NEWBORN", "INFANCY", "CHILDHOOD", "TEENS", "EARLY TEENS", "MID TEENS", "LATE TEENS", "EARLY", "YOUNG", "PREMENOPAUSAL");
    List<String> apsRSIds = Arrays.asList("CRCRS1","CRCRS2","CRCRS12");
    List<String> lynchRSIds = Arrays.asList("CRCRS3", "CRCRS4", "CRCRS5", "CRCRS6", "CRCRS7", "CRCRS8", "CRCRS9", "CRCRS10", "CRCRS11","CRCRS13");
    public NCCNCoreResponse getQuestionObject(NCCNCoreRequest nonCareQuestionnaireDTO) {
        NCCNCoreResponse careapiResponseDTO = new NCCNCoreResponse();
        List<QuestionSet> demographic = new ArrayList<>();
        PersonalHistory personalHistory = new PersonalHistory();
        List<QuestionSet> basicQuestions = new ArrayList<>();
        CancerDetails personalCancerDetails = new CancerDetails();
        FamilyHistory familyHistory = new FamilyHistory();
        List<ClinicalHistory> clinicalHistories = new ArrayList<>();
        QuestionSet questionSet;
        Questionnaire questionnaire = new Questionnaire();
        if (!ObjectUtils.isEmpty(nonCareQuestionnaireDTO.getClinicalHistory()))
            clinicalHistories = nonCareQuestionnaireDTO.getClinicalHistory();
        if (!clinicalHistories.isEmpty()) {
            List<ClinicalHistory> familyClinicalHistory = getFamilyCancerStatus(clinicalHistories, familyHistory);
            if (!(probandClinicalHistory.getRelationship() == null)) {
                if (probandClinicalHistory.getSexAtBirth() != null) {
                    questionSet = NCCNQuestionMapper.getQuestionSet(sexAtBirth, probandClinicalHistory.getSexAtBirth().toUpperCase());
                    demographic.add(questionSet);
                }
                if (probandClinicalHistory.getEthnicity().size() > 0) {
                    List<String> ethnicity = probandClinicalHistory.getEthnicity();
                    if (!StringUtils.isEmpty(probandClinicalHistory.getEthnicityOther())) {
                        //ethnicity.add(probandClinicalHistory.getEthnicityOther());
                    }
                    questionSet = NCCNQuestionMapper.getQuestionSetForMultipleAnswer(Ethnicity, ethnicity);
                    demographic.add(questionSet);
                }
                if (!StringUtils.isEmpty(probandClinicalHistory.getCancerOverall())) {
                    if (probandClinicalHistory.getCancerOverall().equalsIgnoreCase(YES)) {
                        questionSet = NCCNQuestionMapper.getQuestionSet(cancerOverall, YES);
                        basicQuestions.add(questionSet);
                    }
                    if (probandClinicalHistory.getCancerOverall().equalsIgnoreCase(NO)) {
                        questionSet = NCCNQuestionMapper.getQuestionSet(cancerOverall, NO);
                        basicQuestions.add(questionSet);
                    }
                    if (probandClinicalHistory.getCancerOverall().equalsIgnoreCase(NOT_PROVIDED)) {
                        questionSet = NCCNQuestionMapper.getQuestionSet(cancerOverall, NO);
                        basicQuestions.add(questionSet);
                    }
                }
                List<CancerFeatureDetails> cancerFeatureDetailsList = new ArrayList<>();
                for (CancerFeatureDetails cancerFeatureDetails : probandClinicalHistory.getCancerFeatureDetails()) {
                    if (cancerFeatureDetails.getCancerFeature().equalsIgnoreCase(Desmoids)) {
                        questionSet = NCCNQuestionMapper.getQuestionSet(Desmoids, YES);
                        basicQuestions.add(questionSet);
                    } else {
                        questionSet = NCCNQuestionMapper.getQuestionSetForCancerFeature(cancerFeature, cancerFeatureDetails.getCancerFeature());
                        if (questionSet.getAnswerKey() != null) {
                            basicQuestions.add(questionSet);
                        }
                        cancerFeatureDetailsList.add(cancerFeatureDetails);
                    }
                }
                getCancerObject(cancerFeatureDetailsList, personalCancerDetails, true, null);

                if (!StringUtils.isEmpty(probandClinicalHistory.getPolypOverall())) {
                    String polypDetail = probandClinicalHistory.getPolypOverall();
                    if (probandClinicalHistory.getPolypOverall().equalsIgnoreCase(YES))
                        polypDetail = YES;
                    if (probandClinicalHistory.getPolypOverall().equalsIgnoreCase(NO))
                        polypDetail = NO;
                    if (probandClinicalHistory.getPolypOverall().equalsIgnoreCase(NOT_PROVIDED))
                        polypDetail = NO;

                    questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.POLYP_OVERALL, polypDetail);
                    basicQuestions.add(questionSet);

                    if (polypDetail.equalsIgnoreCase(YES)) {
                        List<PolypDetails> polypDetails = probandClinicalHistory.getPolypDetails();
                        int polypNumber;
                        for (PolypDetails polypData : polypDetails) {
                            if (!StringUtils.isEmpty(polypData.getPolypType()) && polypData.getPolypType().contains(APIConstants.ADENOMA)) {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.POLYP_TYPE, YES);
                                basicQuestions.add(questionSet);
                            } else if (!StringUtils.isEmpty(polypData.getPolypType())) {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.POLYP_TYPE, NO);
                                basicQuestions.add(questionSet);
                            }

                            if (!StringUtils.isEmpty(polypData.getExactNumOfPolyps())) {
                                polypNumber = Integer.parseInt(polypData.getExactNumOfPolyps());
                                if (polypNumber > 9) {
                                    questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.EXACT_NUM_OF_POLYPS, APIConstants.POLYPS_10_TO_19);
                                    basicQuestions.add(questionSet);
                                    break;
                                }

                            } else if (!StringUtils.isEmpty(polypData.getMinNumOfPolyps())) {
                                polypNumber = Integer.parseInt(polypData.getMinNumOfPolyps());
                                if (polypNumber > 9) {
                                    questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.EXACT_NUM_OF_POLYPS, APIConstants.POLYPS_10_TO_19);
                                    basicQuestions.add(questionSet);
                                    break;
                                }

                            } else if (!StringUtils.isEmpty(polypData.getOtherNumOfPolyps())) {
                                List otherNumberOfPolyps = new ArrayList();
                                otherNumberOfPolyps.add(APIConstants.NUMEROUS);
                                otherNumberOfPolyps.add(APIConstants.DOZENS);
                                otherNumberOfPolyps.add(APIConstants.CARPETED);
                                otherNumberOfPolyps.add(APIConstants.INNUMERABLE);
                                otherNumberOfPolyps.add(APIConstants.LOTS);
                                otherNumberOfPolyps.add(APIConstants.MANY);
                                otherNumberOfPolyps.add(APIConstants.TOO_MANY_TO_COUNT);
                                if (otherNumberOfPolyps.contains(polypData.getOtherNumOfPolyps().trim())) {
                                    questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.EXACT_NUM_OF_POLYPS, APIConstants.POLYPS_10_TO_19);
                                    basicQuestions.add(questionSet);
                                    break;
                                }
                            }


                        }
                    }

                }

                personalHistory.setBasicQuestions(basicQuestions);
                personalHistory.setPersonalCancerDetails(personalCancerDetails);

                questionnaire.setDemographic(demographic);
            }
            if (familyClinicalHistory.size() > 0) {
                getFamilyCancerDetails(familyClinicalHistory, familyHistory);
                List<QuestionSet> familyBasicQuestions = new ArrayList<>();
                familyClinicalHistory.forEach(clinicalHistory -> {
                    if (!StringUtils.isEmpty(clinicalHistory.getPolypOverall())) {
                        String polypDetail = clinicalHistory.getPolypOverall();
                        if (clinicalHistory.getPolypOverall().equalsIgnoreCase(YES)) {
                            polypDetail = YES;
                        }
                        if (clinicalHistory.getPolypOverall().equalsIgnoreCase(NO)) {
                            polypDetail = NO;
                        }
                        if (clinicalHistory.getPolypOverall().equalsIgnoreCase(NOT_PROVIDED)) {
                            polypDetail = NO;
                        }
                        if (clinicalHistory.getPolypOverall().isEmpty())
                            polypDetail = NO;

                        QuestionSet relativePolypOverallQuestionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.R_POLYP_OVERALL, polypDetail);
                        familyBasicQuestions.add(relativePolypOverallQuestionSet);
                        if (polypDetail.equalsIgnoreCase(YES)) {
                            QuestionSet relativeTypePolypOverallQuestionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.R_TYPE_POLYP_OVERALL, clinicalHistory.getRelationship().equalsIgnoreCase("Other")
                                    ? "Other"
                                    : getRelationship(clinicalHistory.getRelationship()));
                            familyBasicQuestions.add(relativeTypePolypOverallQuestionSet);
                        }
                    }
                });
                familyHistory.setBasicQuestions(familyBasicQuestions);
            }
            if (!(personalHistory.getBasicQuestions() == null))
                questionnaire.setPersonalHistory(personalHistory);
            if (!ObjectUtils.isEmpty(familyHistory)) {
                questionnaire.setFamilyCancer(familyHistory);
            }
            try {
                questionnaire.setSource(APIConstants.CARE_API);
                GuidelineDetailsDTO guidelineDetailsDTO = guideLineUtility.getClinicalSummaryAndTags(questionnaire, false, null, null, false,false);
                if (!ObjectUtils.isEmpty(guidelineDetailsDTO)) {
                    careapiResponseDTO = CAREAPIResponseMapper(guidelineDetailsDTO, nonCareQuestionnaireDTO);
                }
            } catch (Exception e) {
                System.out.println("Error in getClinicalSummaryAndTags ");
                e.printStackTrace();
            }
        }
        return careapiResponseDTO;
    }

    public static String getRelationship(String relative) {
        switch (relative.toUpperCase()) {
            case "MOTHER":
                return "Mother";
            case "FATHER":
                return "Father";
            case "SISTER":
                return "Sister";
            case "BROTHER":
                return "Brother";
            case "DAUGHTER":
                return "Daughter";
            case "SON":
                return "Son";
            case "NIECE":
                return "Niece";
            case "NEPHEW":
                return "Nephew";
            case "HALF SISTER":
                return "Half sister";
            case "HALF BROTHER":
                return "Half brother";
            case "GRANDMOTHER":
                return "Grandmother";
            case "GRANDFATHER":
                return "Grandfather";
            case "GRANDDAUGHTER":
                return "Granddaughter";
            case "GRANDSON":
                return "Grandson";
            case "AUNT":
                return "Aunt";
            case "UNCLE":
                return "Uncle";
            case "COUSIN - 1ST":
                return "First cousin (male)";
            case "GR GRANDMOTHER":
                return "Great grandmother";
            case "GR GRANDFATHER":
                return "Great grandfather";
            case "G AUNT":
                return "Great aunt";
            case "G UNCLE":
                return "Great uncle";
            case "G NIECE":
                return "Great niece";
            case "G NEPHEW":
                return "Great nephew";
            case "G GRANDDAUGHTER":
                return "Great granddaughter";
            case "GR GRANDSON":
                return "Great grandson";
            case "HALF AUNT":
                return "Half aunt";
            case "HALF UNCLE":
                return "Half uncle";
            case "HALF NIECE":
                return "Half niece";
            case "HALF NEPHEW":
                return "Half nephew";
            case "OTHER":
                return "Others";
            default:
                return "";
        }
    }

    public void getCancerObject(List<CancerFeatureDetails> cancerFeatureDetailsList, CancerDetails personalCancer, Boolean isProband, RelativeHistory relativeHistory) {
        List<QuestionSet> personalCancerQuestions = new ArrayList<>();
        List<QuestionSet> relativeCancerQuestions = new ArrayList<>();
        for (CancerFeatureDetails cancerFeatureDetails : cancerFeatureDetailsList) {
            cancerFeatureDetails.setCancerFeature(cancerFeatureDetails.getCancerFeature());
            if(cancerFeatureDetails.getCancerFeature().equalsIgnoreCase("Sebaceous Carcinoma") || cancerFeatureDetails.getCancerFeature().equalsIgnoreCase("Sebaceous adenomas"))
            {
                cancerFeatureDetails.setCancerFeature(CancerType.SKIN.name());
                cancerFeatureDetails.setBreastCancerTypes(Collections.singletonList(Sebaceous_C));
            }
            QuestionSet questionSet = new QuestionSet();
            String relativeCancerName = "";
            String ageAtDiagnosis = "";
            String cancerName = cancerFeatureDetails.getCancerFeature();
            Boolean isSkinCancer = cancerName.equalsIgnoreCase(CancerType.SKIN.name()) ? Boolean.TRUE : Boolean.FALSE;
            if (isProband && !isSkinCancer) {
                if ((!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) && (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature()))) {
                    if (!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()))
                        ageAtDiagnosis = cancerFeatureDetails.getExactDxAge();
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()))
                        ageAtDiagnosis = cancerFeatureDetails.getMaxDxAge();
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge()))
                    {
                        ageAtDiagnosis = cancerFeatureDetails.getMinDxAge();
                    }
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()))
                    {
                        String otherAge = cancerFeatureDetails.getOtherDxAge().trim().toUpperCase();
                        if(otherDXAge.contains(otherAge))
                        {
                            ageAtDiagnosis = otherDXAgeValue;
                        }
                    }
                    questionSet = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(cancerFeatureDetails.getCancerFeature(), ageAtDiagnosis);
                    personalCancerQuestions.add(questionSet);
                }
            } else if (!isSkinCancer) {

                if ((!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) && (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature()))) {
                    relativeCancerName = relative + cancerFeatureDetails.getCancerFeature();
                    if (!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()))
                        ageAtDiagnosis = cancerFeatureDetails.getExactDxAge();
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()))
                        ageAtDiagnosis = cancerFeatureDetails.getMaxDxAge();
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge()))
                    {
                        ageAtDiagnosis = cancerFeatureDetails.getMinDxAge();
                    }
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()))
                    {
                        String otherAge = cancerFeatureDetails.getOtherDxAge().trim().toUpperCase();
                        if(otherDXAge.contains(otherAge))
                        {
                            ageAtDiagnosis = otherDXAgeValue;
                        }
                    }
                    questionSet = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(relativeCancerName, ageAtDiagnosis);
                    relativeCancerQuestions.add(questionSet);
                }
            }
            if (cancerName.equals("Unknown/Other cancer not listed here")) {
                if ((!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) && (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature()))) {
                    if (!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()))
                        ageAtDiagnosis = cancerFeatureDetails.getExactDxAge();
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()))
                        ageAtDiagnosis = cancerFeatureDetails.getMaxDxAge();
                    else if (!StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) {
                        ageAtDiagnosis = cancerFeatureDetails.getMinDxAge();
                    } else if (!StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge())) {
                        String otherAge = cancerFeatureDetails.getOtherDxAge().trim().toUpperCase();
                        if (otherDXAge.contains(otherAge)) {
                            ageAtDiagnosis = otherDXAgeValue;
                        }
                    }
                    questionSet = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(cancerFeatureDetails.getCancerFeature(), ageAtDiagnosis);
                    personalCancerQuestions.add(questionSet);
                }
                return;
            }
            switch (CancerType.valueOf(cancerName)) {
                case CancerType.BREAST:
                    if (isProband) {
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMultiplePrimaries()) && cancerFeatureDetails.getMultiplePrimaries().equalsIgnoreCase(YES)) {
                            questionSet = NCCNQuestionMapper.getQuestionSet(multiplePrimaries, YES);
                            personalCancerQuestions.add(questionSet);
                        }
                        if (cancerFeatureDetails.getBreastCancerTypes() != null) {
                            if (!cancerFeatureDetails.getBreastCancerTypes().isEmpty()) {
                                List<String> pathology = cancerFeatureDetails.getBreastCancerTypes();
                                if (!StringUtils.isEmpty(cancerFeatureDetails.getPathologyOther())) {
                                    pathology.add(cancerFeatureDetails.getPathologyOther());
                                }
                                questionSet = NCCNQuestionMapper.getQuestionSetForMultipleAnswer(APIConstants.PATHOLOGY, pathology);
                                personalCancerQuestions.add(questionSet);
                            }
                        }
                        if ((!StringUtils.isEmpty(cancerFeatureDetails.getBreastBilateral()) && cancerFeatureDetails.getBreastBilateral().equalsIgnoreCase("Yes")) || (!StringUtils.isEmpty(cancerFeatureDetails.getBreastIpsilateral()) && cancerFeatureDetails.getBreastIpsilateral().equalsIgnoreCase("Yes"))) {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.BREAST_IPSILATERAL, APIConstants.BOTH_BREASTS);
                            personalCancerQuestions.add(questionSet);
                        }
                        if ((!StringUtils.isEmpty(cancerFeatureDetails.getBreastEr())) || (!StringUtils.isEmpty(cancerFeatureDetails.getBreastPr())) || (!StringUtils.isEmpty(cancerFeatureDetails.getBreastHer2()))) {
                            if ((!StringUtils.isEmpty(cancerFeatureDetails.getBreastEr())) && (!StringUtils.isEmpty(cancerFeatureDetails.getBreastPr()))
                                    && (!StringUtils.isEmpty(cancerFeatureDetails.getBreastHer2())) && ((cancerFeatureDetails.getBreastEr().equalsIgnoreCase(APIConstants.NEGATIVE)) &&
                                    (cancerFeatureDetails.getBreastPr().equalsIgnoreCase(APIConstants.NEGATIVE)) && (cancerFeatureDetails.getBreastHer2().equalsIgnoreCase(APIConstants.NEGATIVE)))) {


                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.BREAST_ER, YES);
                                personalCancerQuestions.add(questionSet);
                            } else {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.BREAST_ER, NO);
                                personalCancerQuestions.add(questionSet);
                            }
                        }
                        else {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.BREAST_ER, NOT_PROVIDED);
                            relativeCancerQuestions.add(questionSet);
                        }
                        personalCancer.setBreast(personalCancerQuestions);
                    } else {

                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMultiplePrimaries()) && cancerFeatureDetails.getMultiplePrimaries().equalsIgnoreCase(YES)) {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_MULTIPLE_PRIMARIES, YES);
                            relativeCancerQuestions.add(questionSet);
                        }
//                        if (!(cancerFeatureDetails.getPathology() == null)) {
//                            if (cancerFeatureDetails.getPathology().size() > 0) {
//                                List<String> pathology = cancerFeatureDetails.getPathology();
//                                if (!StringUtils.isEmpty(cancerFeatureDetails.getPathologyOther())) {
//                                    pathology.add(cancerFeatureDetails.getPathologyOther());
//                                }
//                                questionSet = NCCNQuestionMapper.getQuestionSetForMultipleAnswer(APIConstants.RELATIVE_PATHOLOGY, pathology);
//                                relativeCancerQuestions.add(questionSet);
//                            }
//                        }
                        if ((!StringUtils.isEmpty(cancerFeatureDetails.getBreastBilateral()) && cancerFeatureDetails.getBreastBilateral().equalsIgnoreCase("Yes")) || (!StringUtils.isEmpty(cancerFeatureDetails.getBreastIpsilateral()) && cancerFeatureDetails.getBreastIpsilateral().equalsIgnoreCase("Yes"))) {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_BREAST_IPSILATERAL, APIConstants.BOTH_BREASTS);
                            relativeCancerQuestions.add(questionSet);
                        }
                        if ((!StringUtils.isEmpty(cancerFeatureDetails.getBreastEr())) || (!StringUtils.isEmpty(cancerFeatureDetails.getBreastPr())) || (!StringUtils.isEmpty(cancerFeatureDetails.getBreastHer2()))) {
                            if ((!StringUtils.isEmpty(cancerFeatureDetails.getBreastEr())) && (!StringUtils.isEmpty(cancerFeatureDetails.getBreastPr()))
                                    && (!StringUtils.isEmpty(cancerFeatureDetails.getBreastHer2())) && ((cancerFeatureDetails.getBreastEr().equalsIgnoreCase(APIConstants.NEGATIVE)) &&
                                    (cancerFeatureDetails.getBreastPr().equalsIgnoreCase(APIConstants.NEGATIVE)) && (cancerFeatureDetails.getBreastHer2().equalsIgnoreCase(APIConstants.NEGATIVE)))) {


                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_BREAST_ER, YES);
                                relativeCancerQuestions.add(questionSet);
                            } else {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_BREAST_ER, NO);
                                relativeCancerQuestions.add(questionSet);
                            }
                        }
                        else {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_BREAST_ER, NOT_PROVIDED);
                            relativeCancerQuestions.add(questionSet);
                        }
                        relativeHistory.setBreast(relativeCancerQuestions);
                    }
                    break;
                case CancerType.PROSTATE:
                    if (isProband) {

                        if ((!StringUtils.isEmpty(cancerFeatureDetails.getProstateGleasonScore()) || !StringUtils.isEmpty(cancerFeatureDetails.getProstateGradeGroup()) || !StringUtils.isEmpty(cancerFeatureDetails.getProstateRiskGroup()))) {
                            if (!StringUtils.isEmpty(cancerFeatureDetails.getProstateGleasonScore())) {
                                int gleasonScore = Integer.parseInt(cancerFeatureDetails.getProstateGleasonScore());
                                if (gleasonScore > 7) {
                                    questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.PROSTATE_GLEASON_SCORE, APIConstants.HIGHER_THAN_7);
                                    personalCancerQuestions.add(questionSet);
                                }
                            }
                            if (!StringUtils.isEmpty(cancerFeatureDetails.getProstateGradeGroup()) && (cancerFeatureDetails.getProstateGradeGroup().equalsIgnoreCase(GRADE_GROUP_4) || cancerFeatureDetails.getProstateGradeGroup().equalsIgnoreCase(GRADE_GROUP_5))) {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.PROSTATE_GLEASON_SCORE, APIConstants.HIGHER_THAN_7);
                                personalCancerQuestions.add(questionSet);
                            }
                            if (!StringUtils.isEmpty(cancerFeatureDetails.getProstateRiskGroup()) && ((cancerFeatureDetails.getProstateRiskGroup().equalsIgnoreCase(VERY_HIGH_RISK_GROUP)) || (cancerFeatureDetails.getProstateRiskGroup().equalsIgnoreCase(HIGH_RISK_GROUP)))) {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.PROSTATE_GLEASON_SCORE, APIConstants.HIGHER_THAN_7);
                                personalCancerQuestions.add(questionSet);
                            }

                        }
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMetastatic())) {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.METASTATIC, cancerFeatureDetails.getMetastatic());
                            personalCancerQuestions.add(questionSet);
                        }
                        personalCancer.setProstate(personalCancerQuestions);
                    } else {
                        if ((!StringUtils.isEmpty(cancerFeatureDetails.getProstateGleasonScore()) || !StringUtils.isEmpty(cancerFeatureDetails.getProstateGradeGroup()) || !StringUtils.isEmpty(cancerFeatureDetails.getProstateRiskGroup()))) {
                            if (!StringUtils.isEmpty(cancerFeatureDetails.getProstateGleasonScore())) {
                                int gleasonScore = Integer.parseInt(cancerFeatureDetails.getProstateGleasonScore());
                                if (gleasonScore > 7) {
                                    questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_PROSTATE_GLEASON_SCORE, APIConstants.HIGHER_THAN_7);
                                    relativeCancerQuestions.add(questionSet);
                                }
                            }
                            if (!StringUtils.isEmpty(cancerFeatureDetails.getProstateGradeGroup()) && (cancerFeatureDetails.getProstateGradeGroup().equalsIgnoreCase(GRADE_GROUP_4) || cancerFeatureDetails.getProstateGradeGroup().equalsIgnoreCase(GRADE_GROUP_5))) {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_PROSTATE_GLEASON_SCORE, APIConstants.HIGHER_THAN_7);
                                relativeCancerQuestions.add(questionSet);
                            }
                            if (!StringUtils.isEmpty(cancerFeatureDetails.getProstateRiskGroup()) && ((cancerFeatureDetails.getProstateRiskGroup().equalsIgnoreCase(VERY_HIGH_RISK_GROUP)) || (cancerFeatureDetails.getProstateRiskGroup().equalsIgnoreCase(HIGH_RISK_GROUP)))) {
                                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_PROSTATE_GLEASON_SCORE, APIConstants.HIGHER_THAN_7);
                                relativeCancerQuestions.add(questionSet);
                            }
                        }
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMetastatic())) {
                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_METASTATIC, cancerFeatureDetails.getMetastatic());
                            relativeCancerQuestions.add(questionSet);
                        }
                        relativeHistory.setProstate(relativeCancerQuestions);
                    }
                    break;
                case CancerType.BRAIN:
                    if (isProband) {
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMultiplePrimaries())) {

                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.MALIGNANT, cancerFeatureDetails.getMultiplePrimaries()); //Intraductal  Need to check
                            personalCancerQuestions.add(questionSet);
                        }
                        personalCancer.setBrain(personalCancerQuestions);
                    } else
                        relativeHistory.setBrain(relativeCancerQuestions);
                    break;

                case CancerType.OVARIAN:
                    if (isProband)
                        personalCancer.setOvarian(personalCancerQuestions);
                    else
                        relativeHistory.setOvarian(relativeCancerQuestions);
                    break;
                case CancerType.Fallopian_Tube:
                    if (isProband)
                        personalCancer.setFallopianTube(personalCancerQuestions);
                    else
                        relativeHistory.setFallopianTube(relativeCancerQuestions);
                    break;
                case CancerType.COLORECTAL:
                    if (isProband) {
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMultiplePrimaries())) {

                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.COLORECTAL_MULTIPLE_PRIMARIES, cancerFeatureDetails.getMultiplePrimaries().toUpperCase()); //Intraductal  Need to check
                            personalCancerQuestions.add(questionSet);
                        }
                        personalCancer.setColorectal(personalCancerQuestions);
                    } else {
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getMultiplePrimaries())) {

                            questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.RELATIVE_COLORECTAL_MULTIPLE_PRIMARIES, cancerFeatureDetails.getMultiplePrimaries().toUpperCase()); //Intraductal  Need to check
                            relativeCancerQuestions.add(questionSet);
                        }
                        relativeHistory.setColorectal(relativeCancerQuestions);
                    }
                    break;
                case CancerType.PANCREATIC:
                    if (isProband)
                        personalCancer.setPancreatic(personalCancerQuestions);
                    else
                        relativeHistory.setPancreatic(relativeCancerQuestions);
                    break;
                case CancerType.Uterine:
                    if (isProband)
                        personalCancer.setUterine(personalCancerQuestions);
                    else
                        relativeHistory.setUterine(relativeCancerQuestions);
                    break;
                case CancerType.Gastric:
                    if (isProband) {
                        List<String> pathology = new ArrayList<>();
                        pathology = getPathology(cancerFeatureDetails, pathology);
                        diffuseExtraction(pathology, questionSet, APIConstants.GASTRIC_DIFFUSE, personalCancerQuestions);
                        personalCancer.setStomach(personalCancerQuestions);
                    } else {
                        List<String> pathology = new ArrayList<>();
                        pathology = getPathology(cancerFeatureDetails, pathology);
                        diffuseExtraction(pathology, questionSet, APIConstants.RELATIVE_GASTRIC_DIFFUSE, relativeCancerQuestions);
                        relativeHistory.setStomach(relativeCancerQuestions);
                    }
                    break;
                case CancerType.SmallIntestine:
                    if (isProband)
                        personalCancer.setSmallIntestine(personalCancerQuestions);
                    else
                        relativeHistory.setSmallIntestine(relativeCancerQuestions);
                    break;
                case CancerType.BILIARY_TRACT:
                    if (isProband)
                        personalCancer.setBiliaryTract(personalCancerQuestions);
                    else
                        relativeHistory.setBiliaryTract(relativeCancerQuestions);
                    break;
                case CancerType.KIDNEY:
                    if (isProband)
                        personalCancer.setKidney(personalCancerQuestions);
                    else
                        relativeHistory.setKidney(relativeCancerQuestions);
                    break;
                case CancerType.Ureter:

                    if (isProband)
                        personalCancer.setUreter(personalCancerQuestions);
                    else
                        relativeHistory.setUreter(relativeCancerQuestions);
                    break;
                case CancerType.SKIN:
                    if ((cancerFeatureDetails.getBreastCancerTypes().contains(Sebaceous) ||  cancerFeatureDetails.getBreastCancerTypes().contains(Sebaceous_C))) {
                        if (isProband) {
                            if ((!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) && (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature()))) {
                                if (!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()))
                                    ageAtDiagnosis = cancerFeatureDetails.getExactDxAge();
                                else if (!StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()))
                                    ageAtDiagnosis = cancerFeatureDetails.getMaxDxAge();
                                else if (!StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge()))
                                {
                                    ageAtDiagnosis = cancerFeatureDetails.getMinDxAge();
                                }
                                else if (!StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()))
                                {
                                    String otherAge = cancerFeatureDetails.getOtherDxAge().trim().toUpperCase();
                                    if(otherDXAge.contains(otherAge))
                                    {
                                        ageAtDiagnosis = otherDXAgeValue;
                                    }
                                }
                                questionSet = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(cancerFeatureDetails.getCancerFeature(), ageAtDiagnosis);
                                personalCancerQuestions.add(questionSet);
                            }
                            questionSet = NCCNQuestionMapper.getQuestionSet(SkinCancerType, Sebaceous_Adenomas_Carcinoma);
                            personalCancerQuestions.add(questionSet);

                            personalCancer.setSkin(personalCancerQuestions);
                        } else {
                            if ((!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge())|| !StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) && (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature()))) {
                                relativeCancerName = relative + cancerFeatureDetails.getCancerFeature();
                                if (!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()))
                                    ageAtDiagnosis = cancerFeatureDetails.getExactDxAge();
                                else if (!StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()))
                                    ageAtDiagnosis = cancerFeatureDetails.getMaxDxAge();
                                else if (!StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge()))
                                {
                                    ageAtDiagnosis = cancerFeatureDetails.getMinDxAge();
                                }
                                else if (!StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()))
                                {
                                    String otherAge = cancerFeatureDetails.getOtherDxAge().trim().toUpperCase();
                                    if(otherDXAge.contains(otherAge))
                                    {
                                        ageAtDiagnosis = otherDXAgeValue;
                                    }
                                }
                                questionSet = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(relativeCancerName, ageAtDiagnosis);
                                relativeCancerQuestions.add(questionSet);
                            }
                            questionSet = NCCNQuestionMapper.getQuestionSet(relative + SkinCancerType, Sebaceous_Adenomas_Carcinoma);
                            relativeCancerQuestions.add(questionSet);
                            relativeHistory.setSkin(relativeCancerQuestions);
                        }
                    }
                    break;

                case CancerType.LEUKEMIA:

                    if (isProband)
                        personalCancer.setLeukemia(personalCancerQuestions);
                    else
                        relativeHistory.setLeukemia(relativeCancerQuestions);
                    break;
                case CancerType.LYMPHOMA:

                    if (isProband)
                        personalCancer.setLymphoma(personalCancerQuestions);
                    else
                        relativeHistory.setLymphoma(relativeCancerQuestions);
                    break;
                case CancerType.GALLBLADDER:

                    if (isProband)
                        personalCancer.setGallbladder(personalCancerQuestions);
                    else
                        relativeHistory.setGallbladder(relativeCancerQuestions);
                    break;
                case CancerType.CERVICAL:
                    if (isProband)
                        personalCancer.setCervical(personalCancerQuestions);
                    else
                        relativeHistory.setCervical(relativeCancerQuestions);
                    break;
                case CancerType.BLADDER:
                    if (isProband)
                        personalCancer.setBladder(personalCancerQuestions);
                    else
                        relativeHistory.setBladder(relativeCancerQuestions);
                    break;
                case CancerType.THROAT:
                    if (isProband)
                        personalCancer.setThroat(personalCancerQuestions);
                    else
                        relativeHistory.setThroat(relativeCancerQuestions);
                    break;
                case CancerType.THYROID:
                    if (isProband)
                        personalCancer.setThyroid(personalCancerQuestions);
                    else
                        relativeHistory.setThyroid(relativeCancerQuestions);
                    break;
                case CancerType.ORAL:
                    if (isProband)
                        personalCancer.setOral(personalCancerQuestions);
                    else
                        relativeHistory.setOral(relativeCancerQuestions);
                    break;
                case CancerType.LIVER:
                    if (isProband)
                        personalCancer.setLiver(personalCancerQuestions);
                    else
                        relativeHistory.setLiver(relativeCancerQuestions);
                    break;
                case CancerType.TESTICULAR:
                    if (isProband)
                        personalCancer.setTesticular(personalCancerQuestions);
                    else
                        relativeHistory.setTesticular(relativeCancerQuestions);
                    break;
                case CancerType.PARAGANGLIOMA:
                    if (isProband)
                        personalCancer.setParaganglioma(personalCancerQuestions);
                    else
                        relativeHistory.setParaganglioma(relativeCancerQuestions);
                    break;
                case CancerType.PHEOCHROMOCYTOMA:
                    if (isProband)
                        personalCancer.setPheochromocytoma(personalCancerQuestions);
                    else
                        relativeHistory.setPheochromocytoma(relativeCancerQuestions);
                    break;
                case CancerType.LUNG:
                    if (isProband)
                        personalCancer.setLung(personalCancerQuestions);
                    else
                        relativeHistory.setLung(relativeCancerQuestions);
                    break;
                case CancerType.SARCOMA:
                    if (isProband) {
                        personalCancer.setSarcoma(personalCancerQuestions);
                    } else {
                        relativeHistory.setSarcoma(relativeCancerQuestions);
                    }
                    break;
               /* case "Unknown/Other cancer not listed here":
                    if ((!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()) || !StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge())|| !StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge())) && (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature()))) {
                        if (!StringUtils.isEmpty(cancerFeatureDetails.getExactDxAge()))
                            ageAtDiagnosis = cancerFeatureDetails.getExactDxAge();
                        else if (!StringUtils.isEmpty(cancerFeatureDetails.getMaxDxAge()))
                            ageAtDiagnosis = cancerFeatureDetails.getMaxDxAge();
                        else if (!StringUtils.isEmpty(cancerFeatureDetails.getMinDxAge()))
                        {
                            ageAtDiagnosis = cancerFeatureDetails.getMinDxAge();
                        }
                        else if (!StringUtils.isEmpty(cancerFeatureDetails.getOtherDxAge()))
                        {
                            String otherAge = cancerFeatureDetails.getOtherDxAge().trim().toUpperCase();
                            if(otherDXAge.contains(otherAge))
                            {
                                ageAtDiagnosis = otherDXAgeValue;
                            }
                        }
                        questionSet = NCCNQuestionMapper.getQuestionSetForAgeAtDiagnosis(cancerFeatureDetails.getCancerFeature(), ageAtDiagnosis);
                        personalCancerQuestions.add(questionSet);
                    }
                    break; */
                default:
                    System.out.println("Invalid cancer type");
                    break;
            }

        }
    }

    private static List<String> getPathology(CancerFeatureDetails cancerFeatureDetails, List<String> pathology) {
        if (cancerFeatureDetails.getBreastCancerTypes() != null) {
            if (cancerFeatureDetails.getBreastCancerTypes().size() > 0) {
                pathology = cancerFeatureDetails.getBreastCancerTypes();
                if (!StringUtils.isEmpty(cancerFeatureDetails.getPathologyOther())) {
                    pathology.add(cancerFeatureDetails.getPathologyOther());
                }
            }
        }
        return pathology;
    }

    private static void diffuseExtraction(List<String> pathology, QuestionSet questionSet, String gastricDiffuse, List<QuestionSet> personalCancerQuestions) {
        List<String> diffuse = getDiffuse(pathology);
        questionSet = NCCNQuestionMapper.getQuestionSetForMultipleAnswer(gastricDiffuse, diffuse);
        personalCancerQuestions.add(questionSet);
    }

    private static List<String> getDiffuse(List<String> pathology) {
        if (pathology == null || pathology.isEmpty()) {
            pathology.add("No");
            return pathology;
        }
        boolean containsDiffuse = pathology.stream().anyMatch(value -> value != null && value.toLowerCase().contains("diffuse"));

        if (containsDiffuse) {
            pathology = pathology.stream()
                    .filter(java.util.Objects::nonNull)
                    .map(value -> value.toLowerCase().contains("diffuse") ? "Yes" : value)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            pathology.add("No");
            pathology = pathology.stream()
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return pathology;
    }


    public NCCNCoreResponse CAREAPIResponseMapper(GuidelineDetailsDTO guidelineDetailsDTO, NCCNCoreRequest nonCareQuestionnaireDTO) throws Exception {
        NCCNCoreResponse careapiResponseDTO = new NCCNCoreResponse();

        ClinicalSummary clinicalSummary = guidelineDetailsDTO.getClinicalSummary();
        List<String> inputList = parseJson(guidelineDetailsDTO.getReasoningStatementAndTagsSet());
        if (guidelineDetailsDTO.isHBOC()) {
            careapiResponseDTO.setHbocOverall(HBOCNCCN);
            careapiResponseDTO.setHbocCriteriaMet(clinicalSummary.getRiskAssessmentNccnForHBOC());
        if (!ObjectUtils.isEmpty(careapiResponseDTO.getHbocCriteriaMet()) && !inputList.isEmpty()) {
            careapiResponseDTO.setHbocCriteriaMetIds(filterByPrefix(inputList, "HBOC"));
        }}

        if (guidelineDetailsDTO.isLYNCH()) {
            careapiResponseDTO.setLynchCriteriaMet(clinicalSummary.getRiskAssessmentNccnForLYNCH());
            careapiResponseDTO.setLynchOverall(LYNCHNCCN);
            if (!ObjectUtils.isEmpty(careapiResponseDTO.getLynchOverall())) {
                careapiResponseDTO.setLynchCriteriaMetIds(findAvailableRSId(lynchRSIds, filterByPrefix(inputList, "CRC")));
            }
        }
        if (guidelineDetailsDTO.isAPS()) {
            careapiResponseDTO.setApsApcCriteriaMet(clinicalSummary.getRiskAssessmentNccnForAPS());
            careapiResponseDTO.setApsApcOverall(APSNCCN);
            careapiResponseDTO.setApsApcCriteriaMetIds(findAvailableRSId(apsRSIds, filterByPrefix(inputList, "CRC")));
        }
        return careapiResponseDTO;
    }
    public void getFamilyCancerDetails(List<ClinicalHistory> clinicalHistoryList, FamilyHistory familyHistory) {
        List<Father> fathers = new ArrayList<>();
        List<FirstCousin> firstCousinsMale = new ArrayList<>();
        List<FirstCousin> firstCousinsFemale = new ArrayList<>();
        List<Grandfather> grandFathers = new ArrayList<>();
        List<Mother> mothers = new ArrayList<>();
        List<GreatUncle> greatUncles = new ArrayList<>();
        List<HalfAunt> halfAunts = new ArrayList<>();
        List<Sister> sisters = new ArrayList<>();
        List<Brother> brothers = new ArrayList<>();
        List<Daughters> daughters = new ArrayList<>();
        List<Son> sons = new ArrayList<>();
        List<Niece> nieces = new ArrayList<>();
        List<Nephew> nephews = new ArrayList<>();
        List<GreatAunt> greatAunts = new ArrayList<>();
        List<HalfSister> halfSisters = new ArrayList<>();
        List<GreatGrandfather> greatGrandFathers = new ArrayList<>();
        List<HalfUncle> halfUncles = new ArrayList<>();
        List<HalfBrother> halfBrothers = new ArrayList<>();
        List<GreatNiece> greatNieces = new ArrayList<>();
        List<Grandmother> grandMothers = new ArrayList<>();
        List<Others> others = new ArrayList<>();
        List<Granddaughter> grandDaughters = new ArrayList<>();
        List<GreatGrandmother> greatGrandMothers = new ArrayList<>();
        List<Grandson> grandSons = new ArrayList<>();
        List<HalfNephew> halfNephews = new ArrayList<>();
        List<Aunt> aunts = new ArrayList<>();
        List<GreatNephew> greatNephews = new ArrayList<>();
        List<HalfNiece> halfNieces = new ArrayList<>();
        List<GreatGrandson> greatGrandsons = new ArrayList<>();
        List<GreatGranddaughter> greatGranddaughters = new ArrayList<>();
        List<Uncle> uncles = new ArrayList<>();
        FamilyCancerDetails familyCancerDetails = new FamilyCancerDetails();

        for (ClinicalHistory clinicalHistory : clinicalHistoryList) {
            String relationShip = clinicalHistory.getRelationship();
            QuestionSet questionSet = new QuestionSet();

            List<CancerFeatureDetails> cancerFeatureDetailsList = clinicalHistory.getCancerFeatureDetails();
            List<QuestionSet> basicRelativeQuestion = new ArrayList<>();

            if (!StringUtils.isEmpty(clinicalHistory.getSideOfFam()) && sideOfFamilyQuestion.contains(relationShip)) {
                questionSet = NCCNQuestionMapper.getQuestionSet(APIConstants.SIDE_OF_FAM, clinicalHistory.getSideOfFam().trim().toUpperCase());
                basicRelativeQuestion.add(questionSet);
            }
            for (CancerFeatureDetails cancerFeatureDetails : cancerFeatureDetailsList) {
                if (!StringUtils.isEmpty(cancerFeatureDetails.getCancerFeature())) {
                    questionSet = NCCNQuestionMapper.getQuestionSetForCancerFeature(APIConstants.RELATIVE_CANCER_FEATURE, cancerFeatureDetails.getCancerFeature());
                    if (questionSet.getAnswerKey() != null) {
                        basicRelativeQuestion.add(questionSet);
                    }
                }
            }

            switch (relationShip) {
                case MOTHER:
                    Mother mother = new Mother();
                    mother.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, mother);
                    mothers.add(mother);
                    familyCancerDetails.setMothers(mothers);
                    break;

                case FATHER:
                    Father father = new Father();
                    father.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, father);
                    fathers.add(father);
                    familyCancerDetails.setFathers(fathers);
                    break;

                case SISTER:
                    Sister sister = new Sister();
                    sister.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, sister);
                    sisters.add(sister);
                    familyCancerDetails.setSisters(sisters);
                    break;

                case BROTHER:
                    Brother brother = new Brother();
                    brother.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, brother);
                    brothers.add(brother);
                    familyCancerDetails.setBrothers(brothers);
                    break;

                case DAUGHTER:
                    Daughters daughter = new Daughters();
                    daughter.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, daughter);
                    daughters.add(daughter);
                    familyCancerDetails.setDaughters(daughters);
                    break;

                case SON:
                    Son son = new Son();
                    son.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, son);
                    sons.add(son);
                    familyCancerDetails.setSons(sons);
                    break;

                case NIECE:
                    Niece niece = new Niece();
                    niece.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, niece);
                    nieces.add(niece);
                    familyCancerDetails.setNieces(nieces);
                    break;

                case NEPHEW:
                    Nephew nephew = new Nephew();
                    nephew.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, nephew);
                    nephews.add(nephew);
                    familyCancerDetails.setNephews(nephews);
                    break;

                case HALF_SISTER:
                    HalfSister halfSister = new HalfSister();
                    halfSister.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, halfSister);
                    halfSisters.add(halfSister);
                    familyCancerDetails.setHalfSisters(halfSisters);
                    break;

                case HALF_BROTHER:
                    HalfBrother halfBrother = new HalfBrother();
                    halfBrother.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, halfBrother);
                    halfBrothers.add(halfBrother);
                    familyCancerDetails.setHalfBrothers(halfBrothers);
                    break;

                case GRANDMOTHER:
                    Grandmother grandmother = new Grandmother();
                    grandmother.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, grandmother);
                    grandMothers.add(grandmother);
                    familyCancerDetails.setGrandMothers(grandMothers);
                    break;

                case GRANDFATHER:
                    Grandfather grandfather = new Grandfather();
                    grandfather.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, grandfather);
                    grandFathers.add(grandfather);
                    familyCancerDetails.setGrandFathers(grandFathers);
                    break;

                case GRANDDAUGHTER:
                    Granddaughter granddaughter = new Granddaughter();
                    granddaughter.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, granddaughter);
                    grandDaughters.add(granddaughter);
                    familyCancerDetails.setGrandDaughters(grandDaughters);
                    break;

                case GRANDSON:
                    Grandson grandson = new Grandson();
                    grandson.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, grandson);
                    grandSons.add(grandson);
                    familyCancerDetails.setGrandSons(grandSons);
                    break;

                case AUNT:
                    Aunt aunt = new Aunt();
                    aunt.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, aunt);
                    aunts.add(aunt);
                    familyCancerDetails.setAunts(aunts);
                    break;

                case UNCLE:
                    Uncle uncle = new Uncle();
                    uncle.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, uncle);
                    uncles.add(uncle);
                    familyCancerDetails.setUncles(uncles);
                    break;

                case FIRST_COUSIN_MALE:
                    FirstCousin firstCousinMale = new FirstCousin();
                    firstCousinMale.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, firstCousinMale);
                    firstCousinsMale.add(firstCousinMale);
                    familyCancerDetails.setFirstCousinsMale(firstCousinsMale);
                    break;

                case FIRST_COUSIN_FEMALE:
                    FirstCousin firstCousinFemale = new FirstCousin();
                    firstCousinFemale.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, firstCousinFemale);
                    firstCousinsFemale.add(firstCousinFemale);
                    familyCancerDetails.setFirstCousinsFemale(firstCousinsFemale);
                    break;

                case GREAT_GRANDMOTHER:
                    GreatGrandmother greatGrandmother = new GreatGrandmother();
                    greatGrandmother.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatGrandmother);
                    greatGrandMothers.add(greatGrandmother);
                    familyCancerDetails.setGreatGrandMothers(greatGrandMothers);
                    break;

                case GREAT_GRANDFATHER:
                    GreatGrandfather greatGrandfather = new GreatGrandfather();
                    greatGrandfather.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatGrandfather);
                    greatGrandFathers.add(greatGrandfather);
                    familyCancerDetails.setGreatGrandFathers(greatGrandFathers);
                    break;

                case GREAT_AUNT:
                    GreatAunt greatAunt = new GreatAunt();
                    greatAunt.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatAunt);
                    greatAunts.add(greatAunt);
                    familyCancerDetails.setGreatAunts(greatAunts);
                    break;

                case GREAT_UNCLE:
                    GreatUncle greatUncle = new GreatUncle();
                    greatUncle.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatUncle);
                    greatUncles.add(greatUncle);
                    familyCancerDetails.setGreatUncles(greatUncles);
                    break;

                case GREAT_NEPHEW:
                    GreatNephew greatNephew = new GreatNephew();
                    greatNephew.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatNephew);
                    greatNephews.add(greatNephew);
                    familyCancerDetails.setGreatNephews(greatNephews);
                    break;

                case GREAT_NIECE:
                    GreatNiece greatNiece = new GreatNiece();
                    greatNiece.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatNiece);
                    greatNieces.add(greatNiece);
                    familyCancerDetails.setGreatNieces(greatNieces);
                    break;

                case GREAT_GRANDDAUGHTER:
                    GreatGranddaughter greatGranddaughter = new GreatGranddaughter();
                    greatGranddaughter.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatGranddaughter);
                    greatGranddaughters.add(greatGranddaughter);
                    familyCancerDetails.setGreatGrandDaughters(greatGranddaughters);
                    break;

                case GREAT_GRANDSON:
                    GreatGrandson greatGrandson = new GreatGrandson();
                    greatGrandson.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, greatGrandson);
                    greatGrandsons.add(greatGrandson);
                    familyCancerDetails.setGreatGrandSons(greatGrandsons);
                    break;

                case HALF_AUNT:
                    HalfAunt halfAunt = new HalfAunt();
                    halfAunt.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, halfAunt);
                    halfAunts.add(halfAunt);
                    familyCancerDetails.setHalfAunts(halfAunts);
                    break;

                case HALF_UNCLE:
                    HalfUncle halfUncle = new HalfUncle();
                    halfUncle.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, halfUncle);
                    halfUncles.add(halfUncle);
                    familyCancerDetails.setHalfUncles(halfUncles);
                    break;

                case HALF_NIECE:
                    HalfNiece halfNiece = new HalfNiece();
                    getCancerObject(cancerFeatureDetailsList, null, false, halfNiece);
                    halfNieces.add(halfNiece);
                    familyCancerDetails.setHalfNieces(halfNieces);
                    break;

                case HALF_NEPHEW:
                    HalfNephew halfNephew = new HalfNephew();
                    halfNephew.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, halfNephew);
                    halfNephews.add(halfNephew);
                    familyCancerDetails.setHalfNephews(halfNephews);
                    break;

                case OTHERS:
                    Others other = new Others();
                    other.setBasicQuestions(basicRelativeQuestion);
                    getCancerObject(cancerFeatureDetailsList, null, false, other);
                    others.add(other);
                    familyCancerDetails.setOthers(others);
                    break;

                default:
            }


            familyHistory.setFamilyCancerDetails(familyCancerDetails);
        }
        if (!ObjectUtils.isEmpty(familyCancerDetails))
            familyHistory.setFamilyCancerDetails(familyCancerDetails);

    }

    public HashMap<String, String> relationConversionMap() {
        HashMap<String, String> conversionMap = new HashMap<>();
        conversionMap.put("MOTHER", MOTHER);
        conversionMap.put("FATHER", FATHER);
        conversionMap.put("SISTER", SISTER);
        conversionMap.put("BROTHER", BROTHER);
        conversionMap.put("DAUGHTER", DAUGHTER);
        conversionMap.put("SON", SON);
        conversionMap.put("NIECE", NIECE);
        conversionMap.put("NEPHEW", NEPHEW);
        conversionMap.put("HALF_SISTER", HALF_SISTER);
        conversionMap.put("HALF_BROTHER", HALF_BROTHER);
        conversionMap.put("GRANDMOTHER", GRANDMOTHER);
        conversionMap.put("GRANDFATHER", GRANDFATHER);
        conversionMap.put("GRANDDAUGHTER", GRANDDAUGHTER);
        conversionMap.put("GRANDSON", GRANDSON);
        conversionMap.put("AUNT", AUNT);
        conversionMap.put("UNCLE", UNCLE);
        conversionMap.put("FIRST_COUSIN_MALE", FIRST_COUSIN_MALE);
        conversionMap.put("FIRST_COUSIN_FEMALE", FIRST_COUSIN_FEMALE);
        conversionMap.put("GREAT_GRANDMOTHER", GREAT_GRANDMOTHER);
        conversionMap.put("GREAT_GRANDFATHER", GREAT_GRANDFATHER);
        conversionMap.put("GREAT_AUNT", GREAT_AUNT);
        conversionMap.put("GREAT_UNCLE", GREAT_UNCLE);
        conversionMap.put("GREAT_NIECE", GREAT_NIECE);
        conversionMap.put("GREAT_NEPHEW", GREAT_NEPHEW);
        conversionMap.put("GREAT_GRANDDAUGHTER", GREAT_GRANDDAUGHTER);
        conversionMap.put("GREAT_GRANDSON", GREAT_GRANDSON);
        conversionMap.put("HALF_AUNT", HALF_AUNT);
        conversionMap.put("HALF_UNCLE", HALF_UNCLE);
        conversionMap.put("HALF_NIECE", HALF_NIECE);
        conversionMap.put("HALF_NEPHEW", HALF_NEPHEW);
        conversionMap.put("OTHER", OTHERS);
        return conversionMap;
    }

    public List<ClinicalHistory> getFamilyCancerStatus(List<ClinicalHistory> clinicalHistories, FamilyHistory familyHistory) {
        List<ClinicalHistory> familyClinicalHistoryList = new ArrayList<>();
        String questionSetName;
        Boolean isPersonalHistory = false;


        for (ClinicalHistory clinicalHistory : clinicalHistories) {
            String relationShip = clinicalHistory.getRelationship().trim().toUpperCase();
            List<CancerFeatureDetails> featureDetails = clinicalHistory.getCancerFeatureDetails();
            if (PROBAND.equalsIgnoreCase(relationShip)) {
                probandClinicalHistory = clinicalHistory;
                isPersonalHistory = true;
            } else if (!featureDetails.isEmpty()) {
                if (relationShip.equalsIgnoreCase(Cousin)) {
                    if (clinicalHistory.getSexAtBirth().equalsIgnoreCase(MALE))
                        relationShip = FIRST_COUSIN_MALE;
                    if (clinicalHistory.getSexAtBirth().equalsIgnoreCase(FEMALE))
                        relationShip = FIRST_COUSIN_FEMALE;
                }
                relationShip = relationConversionMap().get(relationShip);
                clinicalHistory.setRelationship(relationShip);
                familyClinicalHistoryList.add(clinicalHistory);
            }
        }

        if (!isPersonalHistory)
            probandClinicalHistory = new ClinicalHistory();

        questionSetName = familyClinicalHistoryList.isEmpty() ? NO : YES;
        familyHistory.setBasicQuestions(Collections.singletonList(NCCNQuestionMapper.getQuestionSet("relativeCancerOverall", questionSetName)));

        return familyClinicalHistoryList;
    }

    public GuideLineUtility getGuideLineUtility() {
        return guideLineUtility;
    }

    public List<String> getSideOfFamilyQuestion() {
        return sideOfFamilyQuestion;
    }

    public static List<String> parseJson(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        return jsonNode.findValuesAsText("reasoningStatementId");
    }

    public static Set<String> filterByPrefix(List<String> inputList, String prefix) {
        return inputList.stream()
                .filter(statementId -> statementId != null && statementId.startsWith(prefix))
                .collect(Collectors.toSet());
    }

    public static Set<String> findAvailableRSId(List<String> allMatchingIds, Set<String> patientRSIds) {
        return allMatchingIds.stream()
                .filter(patientRSIds::contains)
                .collect(Collectors.toSet());
    }

}
