package com.genomebrain.backend.service.util;

import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genomebrain.backend.quesstionnaire.FamilyCancerDetails;
import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.quesstionnaire.RelativeDetail;
import com.genomebrain.backend.service.GuideLineService;
import com.genomebrain.backend.service.dto.CancerDetailsObject;
import com.genomebrain.backend.service.dto.ClinicalSummary;
import com.genomebrain.backend.service.dto.CustomQuestionConstants;
import com.genomebrain.backend.service.dto.CustomQuestions;
import com.genomebrain.backend.service.dto.Demographics;
import com.genomebrain.backend.service.dto.FamilyHistory;
import com.genomebrain.backend.service.dto.FamilyHistoryDetails;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;
import com.genomebrain.backend.service.dto.GuidelineDetailsDTO;
import com.genomebrain.backend.service.dto.OptionConstants;
import com.genomebrain.backend.service.dto.PersonalHistoryDetails;
import com.genomebrain.backend.service.dto.PersonalHistoryTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementAndTags;
import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.RelativeConstants;
import com.genomebrain.backend.service.dto.TagConstants;
import com.genomebrain.backend.service.dto.TyrerCuzikCriteriaDTO;
import com.genomebrain.backend.service.dto.ValueLabelDTO;

@Service
public class GuideLineUtility {

    private final Logger log = LoggerFactory.getLogger(GuideLineUtility.class);

    private static final String MOTHER = "Mother";
    private static final String FATHER = "Father";
    private static final String CARE_API = "CARE-API";

    @Autowired
    GuideLineService guideLineService;
    
    @Autowired
    TagGenerationUtility tagGenerationUtility;

    @Autowired
    LynchReasoningStatementUtility lynchReasoningStatementUtility;

    @Autowired
    HbocReasoningStatementUtility hbocReasoningStatementUtility;

    @Autowired
    GastricReasoningStatementUtility gastricReasoningStatementUtility;

    @Autowired
    private MessageSource messageSource;

    /**
     * generate the clinical summary and taglist from questionnaire
     * and added it in GuidelineDetailsDTO and return GuidelineDetailsDTO
     *
     * @param questionnaire
     * @param considerGleasonScore
     * @param gleasonScoreValue
     * @return GuidelineDetailsDTO with clinical summary and taglist
     */
    public GuidelineDetailsDTO getClinicalSummaryAndTags(Questionnaire questionnaire, boolean considerGleasonScore, Long gleasonScoreValue, String version, boolean isOldVersion, boolean useNewEthnicityMapping) throws ConnectException {
        String source = questionnaire.getSource();
        log.info("Entered into getClinicalSummaryAndTags method of GuideLineUtility : generate the clinical summary and taglist from questionnaire with gleasonScoreValue : {}, considerGleasonScore : {}", gleasonScoreValue, considerGleasonScore);
        GuidelineDetailsDTO guidelineDetailsDTO = new GuidelineDetailsDTO();
        ClinicalSummary clinicalSummary = new ClinicalSummary();
        Set<String> tagsList = new HashSet<>();
        Set<String> lynchReasoningStatements = new HashSet<>();
        Set<String> hbocReasoningStatements = new HashSet<>();
        Set<String> gastricReasoningStatements = new HashSet<>();
        boolean isPersonalHistory = false;
        boolean isFamilyHistory = false;
        String personalHistoryOfColorectalPolyps = "";
        String familyHistoryOfColorectalPolyps = "";
        PersonalHistoryDetails personalHistoryDetails = new PersonalHistoryDetails();

        List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList = new ArrayList<>();
        List<FamilyHistoryDetailsTagsDTO> groupedFamilyHistoryDetails = new ArrayList<>();

        if (questionnaire != null) {
            if (questionnaire.getPersonalHistory() != null) {
                isPersonalHistory = checkIfCancerHistory(questionnaire.getPersonalHistory().getBasicQuestions());
            }
            PersonalHistoryTagsDTO personalHistoryDetailsDTO = tagGenerationUtility.getPersonalHistoryDetails(
                    questionnaire, personalHistoryDetails, isPersonalHistory, considerGleasonScore, gleasonScoreValue, version, isOldVersion, useNewEthnicityMapping);
            personalHistoryDetails = personalHistoryDetailsDTO.getPersonalHistoryDetails();
            tagsList.addAll(personalHistoryDetailsDTO.getTags());
            clinicalSummary.setPersonalHistoryDetails(personalHistoryDetails);
            clinicalSummary.setPersonalHistory(isPersonalHistory);


            if (questionnaire.getFamilyCancer() != null) {
                isFamilyHistory = checkIfCancerHistory(questionnaire.getFamilyCancer().getBasicQuestions());
                clinicalSummary = tagGenerationUtility.checkFamilyColorectalPolypsHistory(questionnaire.getFamilyCancer().getBasicQuestions(), clinicalSummary);
                clinicalSummary.setFamilyHistoryOfColorectalPolyps(familyHistoryOfColorectalPolyps);
                if ("Yes".equalsIgnoreCase(familyHistoryOfColorectalPolyps)) {
                    tagsList.add("FmPolyp>10");
                }
                clinicalSummary.setFamilyHistory(isFamilyHistory);
                clinicalSummary = tagGenerationUtility.checkFamilyColorectalPolypsHistory(questionnaire.getFamilyCancer().getBasicQuestions(), clinicalSummary);
                tagsList.addAll(tagGenerationUtility.getTagsForSelectedOptions(questionnaire.getFamilyCancer().getBasicQuestions()));
            }

            //Getting family Details for clinical summary
            if (isFamilyHistory) {
                log.info("Into getClinicalSummaryAndTags method of GuideLineUtility : family details");

                FamilyHistory familyHistory = questionnaire.getFamilyCancer();
                FamilyCancerDetails familyCancerDetails = familyHistory.getFamilyCancerDetails();
                List<FamilyHistoryDetailsTagsDTO> unfamilyHistoryDetailsTagsDTOList = tagGenerationUtility.getFamilyCancerTags(familyCancerDetails, version);
                familyHistoryDetailsTagsDTOList = getCategroirzedFamilyDetailsWithTags(unfamilyHistoryDetailsTagsDTOList);
                lynchReasoningStatements = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getLynchReasoningStatements).flatMap(Collection::stream).collect(Collectors.toSet());
                hbocReasoningStatements = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getHbocReasoningStatements).flatMap(Collection::stream).collect(Collectors.toSet());
                List<FamilyHistoryDetails> familyHistoryDetailsList = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).collect(Collectors.toList());
                tagsList.addAll(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getTags).flatMap(Collection::stream).collect(Collectors.toList()));
                clinicalSummary.setFamilyHistoryForFDR(familyHistoryDetailsList.stream().filter(x -> x.getRelativeSide().equalsIgnoreCase("N/A")
                        || x.getFamilyMember().equalsIgnoreCase(RelativeConstants.HALF_SISTER.getValue()) || x.getFamilyMember().equalsIgnoreCase(RelativeConstants.HALF_BROTHER.getValue())).collect(Collectors.toList()));
                clinicalSummary.setFamilyHistoryForMaternalRelatives(familyHistoryDetailsList.stream().filter(x -> x.getRelativeSide().equalsIgnoreCase(RelativeConstants.MATERNAL.getValue())
                        && !x.getFamilyMember().equalsIgnoreCase(RelativeConstants.HALF_SISTER.getValue()) && !x.getFamilyMember().equalsIgnoreCase(RelativeConstants.HALF_BROTHER.getValue())).collect(Collectors.toList()));
                clinicalSummary.setFamilyHistoryForPaternalRelatives(familyHistoryDetailsList.stream().filter(x -> x.getRelativeSide().equalsIgnoreCase(RelativeConstants.PATERNAL.getValue())
                        && !x.getFamilyMember().equalsIgnoreCase(RelativeConstants.HALF_SISTER.getValue()) && !x.getFamilyMember().equalsIgnoreCase(RelativeConstants.HALF_BROTHER.getValue())).collect(Collectors.toList()));

                List<FamilyHistoryDetails> familyHistoryOtherRelativeDetailsList = new ArrayList<>();
                familyHistoryOtherRelativeDetailsList.addAll(familyHistoryDetailsList.stream().filter(x -> x.getFamilyMember().equalsIgnoreCase(RelativeConstants.OTHER.getValue())).collect(Collectors.toList()));
                familyHistoryDetailsList.removeIf(x -> x.getFamilyMember().equalsIgnoreCase(RelativeConstants.OTHER.getValue()));


                groupedFamilyHistoryDetails = getGroupedFamilyHistoryDetails(familyHistoryDetailsTagsDTOList);
                groupedFamilyHistoryDetails.removeIf(x -> x.getFamilyHistoryDetails().getFamilyMember().equalsIgnoreCase(RelativeConstants.OTHER.getValue()));


                tagsList.addAll(getRelativeCountTags(groupedFamilyHistoryDetails.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).collect(Collectors.toList()), TagConstants.AMBRY054_001.getValue()));
                tagsList.addAll(getRelativeCountTags(groupedFamilyHistoryDetails.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).collect(Collectors.toList()), TagConstants.AMBRY054_002.getValue()));

                if (!CollectionUtils.isEmpty(familyHistoryOtherRelativeDetailsList)) {
                    familyHistoryDetailsList.addAll(familyHistoryOtherRelativeDetailsList);
                }
            }

            clinicalSummary.setFamilyHistoryOfCancer(checkFamilyHistoryCancer(questionnaire));
            if (!StringUtils.isEmpty(clinicalSummary.getFamilyHistoryOfCancer())
                    && (clinicalSummary.getFamilyHistoryOfCancer().equalsIgnoreCase("Unknown (adopted)") ||
                    clinicalSummary.getFamilyHistoryOfCancer().equalsIgnoreCase("Unknown"))) {
                tagsList.add("FmNull");
            }

            if (!CollectionUtils.isEmpty(questionnaire.getCustomCategories()) && !source.equalsIgnoreCase(CARE_API)) {
                clinicalSummary.setCustomQuestions(getCustomQuestions(questionnaire.getCustomCategories()));
            }
            if (questionnaire.getDemographic() != null) {
                String gender = getGenderFromDemographics(questionnaire.getDemographic());
                TyrerCuzikCriteriaDTO tyrerCuzikCriteriaDTO = new TyrerCuzikCriteriaDTO();
                tagsList.add(gender);
                if (tagsList.contains("PrBreast") && gender.equalsIgnoreCase("Male")) {
                    tagsList.add("PrBreastMale");
                }
                if (tagsList.contains("Ashkenazi")) {
                    tyrerCuzikCriteriaDTO.setAshkenaziInheritance(true);
                }
                tyrerCuzikCriteriaDTO.setSexAtBirth(gender);
                if (tyrerCuzikCriteriaDTO.getAge() != null) {
                    clinicalSummary.getPersonalHistoryDetails().setAge(tyrerCuzikCriteriaDTO.getAge().toString());
                }
                clinicalSummary.getPersonalHistoryDetails().setShowBrcaTesting(tyrerCuzikCriteriaDTO.isShowBRCATesting());
                Demographics demographics = new Demographics();
                demographics.setSex(gender);
                clinicalSummary.setDemographics(demographics);
                guidelineDetailsDTO.setTyrerCuzikCriteriaDTO(tyrerCuzikCriteriaDTO);
            }

        }
        Set<ReasoningStatementAndTags> reasoningStatementAndTags = new HashSet<>();

        if (!CollectionUtils.isEmpty(groupedFamilyHistoryDetails) || !CollectionUtils.isEmpty(tagsList)) {
            log.info("Into getClinicalSummaryAndTags method of GuideLineUtility : reasoning statements");

            ReasoningStatementObject lynchReasoningStatementObject = lynchReasoningStatementUtility.getLynchReasoningStatement(groupedFamilyHistoryDetails, tagsList);
            if (!CollectionUtils.isEmpty(lynchReasoningStatementObject.getReasoningStatementEn())) {
                lynchReasoningStatements.addAll(lynchReasoningStatementObject.getReasoningStatementEn());
            }
            if (!CollectionUtils.isEmpty(lynchReasoningStatementObject.getReasoningStatementEn())) {
                reasoningStatementAndTags.addAll(lynchReasoningStatementObject.getReasoningStatementAndTags());
            }

            ReasoningStatementObject hbocReasoningStatementObject = hbocReasoningStatementUtility.getHbocReasoningStatement(groupedFamilyHistoryDetails, tagsList);
            if (!CollectionUtils.isEmpty(hbocReasoningStatementObject.getReasoningStatementEn())) {
                hbocReasoningStatements.addAll(hbocReasoningStatementObject.getReasoningStatementEn());
            }
            if (!CollectionUtils.isEmpty(hbocReasoningStatementObject.getReasoningStatementEn())) {
                reasoningStatementAndTags.addAll(hbocReasoningStatementObject.getReasoningStatementAndTags());
            }

            ReasoningStatementObject gastricReasoningStatementObject = gastricReasoningStatementUtility.getGastricReasoningStatement(groupedFamilyHistoryDetails, tagsList);
            if (!CollectionUtils.isEmpty(gastricReasoningStatementObject.getReasoningStatementEn())) {
                gastricReasoningStatements.addAll(gastricReasoningStatementObject.getReasoningStatementEn());
            }
            if (!CollectionUtils.isEmpty(gastricReasoningStatementObject.getReasoningStatementEn())) {
                reasoningStatementAndTags.addAll(gastricReasoningStatementObject.getReasoningStatementAndTags());
            }
        }
        GenomeBrainResponseDTO genomeBrainResponseDTO = getGuidelines(tagsList);


        if (genomeBrainResponseDTO != null && !genomeBrainResponseDTO.getStatus().equalsIgnoreCase(MessageConstants.FAILURE)) {
            log.info("Into getClinicalSummaryAndTags method of GuideLineUtility : generate response dto");
            Set<String> reasoningStatementsForHBOC = new HashSet<>();
            Set<String> reasoningStatementsForLYNCH = new HashSet<>();
            Set<String> reasoningStatementsForAPS = new HashSet<>();
            Set<String> reasoningStatementsForGastric = new HashSet<>();


            if (!CollectionUtils.isEmpty(lynchReasoningStatements)) {
                reasoningStatementsForLYNCH.addAll(lynchReasoningStatements);
                genomeBrainResponseDTO.setLYNCH(true);
                clinicalSummary.setLYNCH(true);
                genomeBrainResponseDTO.setGuideLineMet(true);
            }

            if (!CollectionUtils.isEmpty(genomeBrainResponseDTO.getReasoningStatementsForLYNCH())) {
                clinicalSummary.setLYNCH(true);
                reasoningStatementsForLYNCH.addAll(genomeBrainResponseDTO.getReasoningStatementsForLYNCH());
            }

            if (!CollectionUtils.isEmpty(genomeBrainResponseDTO.getReasoningStatementsForAPS())) {
                clinicalSummary.setLYNCH(true);
                reasoningStatementsForAPS.addAll(genomeBrainResponseDTO.getReasoningStatementsForAPS());
            }

            if (!CollectionUtils.isEmpty(hbocReasoningStatements)) {
                reasoningStatementsForHBOC.addAll(hbocReasoningStatements);
                genomeBrainResponseDTO.setHBOC(true);
                clinicalSummary.setHBOC(true);
                genomeBrainResponseDTO.setGuideLineMet(true);
            }

            // Process Gastric reasoning statements
            if (!CollectionUtils.isEmpty(gastricReasoningStatements)) {
                reasoningStatementsForGastric.addAll(gastricReasoningStatements);
                genomeBrainResponseDTO.setGastric(true);
                clinicalSummary.setLYNCH(true);
                genomeBrainResponseDTO.setGuideLineMet(true);
            }


            if (!CollectionUtils.isEmpty(genomeBrainResponseDTO.getReasoningStatementsForHBOC())) {
                clinicalSummary.setHBOC(true);
                reasoningStatementsForHBOC.addAll(genomeBrainResponseDTO.getReasoningStatementsForHBOC());
            }

            if (!CollectionUtils.isEmpty(genomeBrainResponseDTO.getHbocReasoningStatementAndTags())) {
                reasoningStatementAndTags.addAll(genomeBrainResponseDTO.getHbocReasoningStatementAndTags());
            }
            if (!CollectionUtils.isEmpty(genomeBrainResponseDTO.getLynchReasoningStatementAndTags())) {
                reasoningStatementAndTags.addAll(genomeBrainResponseDTO.getLynchReasoningStatementAndTags());
            }
            if (!CollectionUtils.isEmpty(genomeBrainResponseDTO.getApsReasoningStatementAndTags())) {
                reasoningStatementAndTags.addAll(genomeBrainResponseDTO.getApsReasoningStatementAndTags());
            }

            clinicalSummary.setRiskAssessmentNccnForLYNCH(reasoningStatementsForLYNCH);
            clinicalSummary.setRiskAssessmentNccnForHBOC(reasoningStatementsForHBOC);
            clinicalSummary.setRiskAssessmentNccnForAPS(reasoningStatementsForAPS);
            clinicalSummary.setRiskAssessmentNccnForGastric(reasoningStatementsForGastric);

            if(!CollectionUtils.isEmpty(clinicalSummary.getRiskAssessmentNccnForHBOC()) &&
                    clinicalSummary.getRiskAssessmentNccnForHBOC().contains(ReasoningStatementConstants.HBOCRS27.getValue())){
                ReasoningStatementAndTags reasoningStatementAndTagsHBOC = new ReasoningStatementAndTags();
                reasoningStatementAndTagsHBOC.setReasoningStatementId("HBOCRS27");
                Set<String> hashSet = new HashSet<>();
                hashSet.add("HBOCTag33");
                reasoningStatementAndTagsHBOC.setTagIds(hashSet);
                reasoningStatementAndTags.add(reasoningStatementAndTagsHBOC);
            }

            if(!CollectionUtils.isEmpty(clinicalSummary.getRiskAssessmentNccnForLYNCH()) &&
                    clinicalSummary.getRiskAssessmentNccnForLYNCH().contains(ReasoningStatementConstants.CRCRS9.getValue())){
                ReasoningStatementAndTags reasoningStatementAndTagsLYNCH = new ReasoningStatementAndTags();
                reasoningStatementAndTagsLYNCH.setReasoningStatementId("CRCRS9");
                Set<String> hashSet = new HashSet<>();
                hashSet.add("CRCTag17");
                hashSet.add("CRCTag18");
                reasoningStatementAndTagsLYNCH.setTagIds(hashSet);
                reasoningStatementAndTags.add(reasoningStatementAndTagsLYNCH);
            }

            guidelineDetailsDTO.setHBOC(genomeBrainResponseDTO.isHBOC());
            guidelineDetailsDTO.setLYNCH(genomeBrainResponseDTO.isLYNCH());
            guidelineDetailsDTO.setAPS(genomeBrainResponseDTO.isAPS());
            guidelineDetailsDTO.setGastric(genomeBrainResponseDTO.isGastric());

            clinicalSummary.setNccnCriteriaMet(genomeBrainResponseDTO.isGuideLineMet());

            boolean isPrHeme = tagsList.contains("PrHeme");
            clinicalSummary.setHeme(isPrHeme);
            guidelineDetailsDTO.setClinicalSummary(clinicalSummary);
            guidelineDetailsDTO.setTagList(tagsList);
            ObjectMapper objectMapper = new ObjectMapper();
            String reasoningStatementAndTagsvalue = null;
            try {
                reasoningStatementAndTagsvalue = objectMapper.writeValueAsString(getReasoningStatementAndTags(reasoningStatementAndTags));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (!StringUtils.isEmpty(reasoningStatementAndTagsvalue)) {
                guidelineDetailsDTO.setReasoningStatementAndTagsSet(reasoningStatementAndTagsvalue);
            }
            guidelineDetailsDTO.setPersonalHistory(isPersonalHistory);
            guidelineDetailsDTO.setFamilyHistory(isFamilyHistory);


            //log.info("Exit from getClinicalSummaryAndTags method of GuideLineUtility with guidelineDetailsDTO: {}", guidelineDetailsDTO);
        } else {
            log.error("**** Error While Connecting To Genomebrain ********");
            throw new ConnectException("Unable to connect to server");
        }
        log.info("Exit from getClinicalSummaryAndTags method of GuideLineUtility : generate the clinical summary and taglist from questionnaire");

        return guidelineDetailsDTO;
    }

    private List<FamilyHistoryDetailsTagsDTO> getGroupedFamilyHistoryDetails(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsList) {
        List<FamilyHistoryDetailsTagsDTO> groupedFamilyHistoryDetails = new ArrayList<>();
        Map<String, List<FamilyHistoryDetailsTagsDTO>> familyDetailsMap = familyHistoryDetailsList.stream()
                .collect(Collectors.groupingBy(x -> x.getFamilyHistoryDetails().getRelativeSide()));

        familyDetailsMap.forEach(
                (k, v) -> {
                    if (k.equalsIgnoreCase(RelativeConstants.MATERNAL.getValue())) {
                        groupedFamilyHistoryDetails.addAll(groupGrandParentOrGrearGrandParents(v, RelativeConstants.MATERNAL.getValue()));
                    } else if (k.equalsIgnoreCase(RelativeConstants.PATERNAL.getValue())) {
                        groupedFamilyHistoryDetails.addAll(groupGrandParentOrGrearGrandParents(v, RelativeConstants.PATERNAL.getValue()));
                    } else
                        groupedFamilyHistoryDetails.addAll(v);
                }
        );
        return groupedFamilyHistoryDetails;
    }

    private List<FamilyHistoryDetailsTagsDTO> groupGrandParentOrGrearGrandParents(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsList, String relativeSide) {
        List<FamilyHistoryDetailsTagsDTO> groupedFamilyHistoryDetails = new ArrayList<>();
        List<FamilyHistoryDetailsTagsDTO> groupedGrandParentFamilyHistoryDetails = new ArrayList<>();
        List<FamilyHistoryDetailsTagsDTO> groupedGreatGrandParentFamilyHistoryDetails = new ArrayList<>();
        Map<String, List<FamilyHistoryDetailsTagsDTO>> familyDetailsMap = familyHistoryDetailsList.stream()
                .collect(Collectors.groupingBy(x -> x.getFamilyHistoryDetails().getFamilyMember()));

        familyDetailsMap.forEach(
                (k, v) -> {
                    if (k.equalsIgnoreCase(RelativeConstants.GRANDMOTHER.getValue()) || k.equalsIgnoreCase(RelativeConstants.GRANDFATHER.getValue())) {
                        groupedGrandParentFamilyHistoryDetails.addAll(v);
                    } else if (k.equalsIgnoreCase(RelativeConstants.GREAT_GRANDMOTHER.getValue()) || k.equalsIgnoreCase(RelativeConstants.GREAT_GRANDFATHER.getValue())) {
                        groupedGreatGrandParentFamilyHistoryDetails.addAll(v);
                    } else
                        groupedFamilyHistoryDetails.addAll(v);
                }
        );
        if (!CollectionUtils.isEmpty(groupedGrandParentFamilyHistoryDetails)) {
            groupedFamilyHistoryDetails.add(getGrandParentOrGreatGrandParentObject("Grandparent", groupedGrandParentFamilyHistoryDetails, relativeSide, "SDR"));
        }
        if (!CollectionUtils.isEmpty(groupedGreatGrandParentFamilyHistoryDetails)) {
            groupedFamilyHistoryDetails.add(getGrandParentOrGreatGrandParentObject("Great Grandparent", groupedGreatGrandParentFamilyHistoryDetails, relativeSide, "TDR"));
        }

        return groupedFamilyHistoryDetails;
    }

    /**
     * get the realative cancer tags from familyHistoryDetailsList with degree and relativeSide
     *
     * @param familyHistoryDetailsList
     * @param relativeSide
     * @return taglist
     */
    private Set<String> getRelativeCountTags(List<FamilyHistoryDetails> familyHistoryDetailsList, String relativeSide) {
        log.info("Entered into getRelativeCountTags method of GuideLineUtility : get the realative tags from familyHistoryDetailsList with degree and relativeSide : {}", relativeSide);
        Set<String> tagsSet = new HashSet<>();
        tagsSet.addAll(getRelativeCountTagsWithDegree(familyHistoryDetailsList, relativeSide, null, ""));
        List<String> firstDegreeRelative = new ArrayList<>();
        firstDegreeRelative.add(RelativeConstants.MOTHER.getValue());
        firstDegreeRelative.add(RelativeConstants.FATHER.getValue());
        firstDegreeRelative.add(RelativeConstants.SISTER.getValue());
        firstDegreeRelative.add(RelativeConstants.BROTHER.getValue());
        firstDegreeRelative.add(RelativeConstants.DAUGHTER.getValue());
        firstDegreeRelative.add(RelativeConstants.SON.getValue());
        tagsSet.addAll(getRelativeCountTagsWithDegree(familyHistoryDetailsList, relativeSide, firstDegreeRelative, "FDR"));
        List<String> secondDegreeRelative = new ArrayList<>();
        secondDegreeRelative.add(RelativeConstants.NIECE.getValue());
        secondDegreeRelative.add(RelativeConstants.NEPHEW.getValue());
        secondDegreeRelative.add(RelativeConstants.HALF_SISTER.getValue());
        secondDegreeRelative.add(RelativeConstants.HALF_BROTHER.getValue());
        secondDegreeRelative.add(RelativeConstants.GRANDMOTHER.getValue());
        secondDegreeRelative.add(RelativeConstants.GRANDFATHER.getValue());
        secondDegreeRelative.add(RelativeConstants.GRANDDAUGHTER.getValue());
        secondDegreeRelative.add(RelativeConstants.GRANDSON.getValue());
        secondDegreeRelative.add(RelativeConstants.UNCLE.getValue());
        secondDegreeRelative.add(RelativeConstants.AUNT.getValue());
        tagsSet.addAll(getRelativeCountTagsWithDegree(familyHistoryDetailsList, relativeSide, secondDegreeRelative, "SDR"));
        log.info("Exit from getRelativeCountTags method of GuideLineUtility : get the realative tags from familyHistoryDetailsList with degree and relativeSide with tagSet : {}", tagsSet);
        return tagsSet;
    }



    /**
     * Method to get gender from the demographcis question set
     *
     * @param demographic
     * @return gender
     */
    private String getGenderFromDemographics(List<QuestionSet> demographic) {
        //log.debug("Entered into getGenderFromDemographics method of GuideLineUtility to get tyrerCuzick criteria with demographic is QuestionSet");
        String gender = "";
        for (QuestionSet questionSet : demographic) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY002")) {
                try {
                    if (OptionConstants.valueOf(questionSet.getAnswerKey()) != null) {
                        gender = OptionConstants.valueOf(questionSet.getAnswerKey()).getValue();
                    }
                } catch (Exception e) {
                    log.error("No answer key found for the gender: {} cause: ", questionSet.getAnswerKey(),e);
                }
            }
        }
        //log.debug("Exit from getGenderFromDemographics method of GuideLineUtility with gender: {}", gender);
        return gender;
    }


    /**
     * Method to check if user has personal or family cancer history
     *
     * @param basicQuestions
     * @return true if cancer history present else false
     */
    private boolean checkIfCancerHistory(List<QuestionSet> basicQuestions) {
        //log.debug("Entered into checkIfCancerHistory method of GuideLineUtility to get tyrerCuzick criteria with basicQuestions( QuestionSet)");
        boolean isPresent = false;
        if (!CollectionUtils.isEmpty(basicQuestions)) {
            for (QuestionSet questionSet : basicQuestions) {
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY004") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY004_001")) {
                    isPresent = true;
                }
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY052") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY052_001")) {
                    isPresent = true;
                }
            }
        }
        //log.debug("Exit from checkIfCancerHistory method of GuideLineUtility to get tyrerCuzick criteria with isPresent: {}", isPresent);
        return isPresent;
    }

    /**
     * Method to connect to genomebrain server and get the response
     *
     * @param tagsList
     * @return genomeBrainResponseDTO
     */
    private GenomeBrainResponseDTO getGuidelines(Set<String> tagsList)  throws  ConnectException {
        log.info("Entered into getGuidelines method of GuideLineUtility : connect to genomebrain server and get the response");
        GenomeBrainResponseDTO genomeBrainResponseDTO = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tags", tagsList);
        genomeBrainResponseDTO = guideLineService.getGuidelinesForTags(jsonObject.toString());
        log.info("Exit from getGuidelines method of GuideLineUtility : connect to genomebrain server and get the response");
        return genomeBrainResponseDTO;
    }

    /**
     * Method converts response from genomebrain to genomeBrainResponseDTO
     */
    private GenomeBrainResponseDTO getGenomeBrainResponseDTO(String jsonObjectResponse) {
        //log.debug("Entered into getGenomeBrainResponseDTO method of GuideLineUtility jsonObjectResponse: {}", jsonObjectResponse);
        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        try {
            genomeBrainResponseDTO = new ObjectMapper().readValue(jsonObjectResponse, GenomeBrainResponseDTO.class);
        } catch (IOException e) {
            log.error("Exception while getting node response: ", e);
            genomeBrainResponseDTO.setStatus(MessageConstants.FAILURE);
            genomeBrainResponseDTO.setMessage(e.getMessage());
        }
        //log.debug("Exit from getGenomeBrainResponseDTO method of GuideLineUtility genomeBrainResponseDTO: {}", genomeBrainResponseDTO);
        return genomeBrainResponseDTO;
    }

    /**
     * Method to get relative counts with degree from the family history details
     */
    private Set<String> getRelativeCountTagsWithDegree(List<FamilyHistoryDetails> familyHistoryDetailsList, String relativeSide, List<String> relatives, String degree) {
        log.info("Entered into getRelativeCountTagsWithDegree method of GuideLineUtility : get relative counts with degree from the family history details");

        Set<String> tagsSet = new HashSet<>();
        String side = "Pat";
        if (relativeSide.equalsIgnoreCase(TagConstants.AMBRY054_001.getValue())) {
            side = "Mat";
        }
        for (FamilyHistoryDetails familyHistoryDetails : familyHistoryDetailsList) {
            if (familyHistoryDetails.getFamilyMember().equalsIgnoreCase(FATHER)) {
                familyHistoryDetails.setRelativeSide(TagConstants.AMBRY054_002.getValue());
            }
            if (familyHistoryDetails.getFamilyMember().equalsIgnoreCase(MOTHER)) {
                familyHistoryDetails.setRelativeSide(TagConstants.AMBRY054_001.getValue());
            }
        }
        List<String> nonUniqueCancerList;
        if (StringUtils.isEmpty(degree)) {
            nonUniqueCancerList = familyHistoryDetailsList.stream().filter(x -> x.getRelativeSide().equalsIgnoreCase(relativeSide)
                    || x.getRelativeSide().equalsIgnoreCase("N/A")).map(FamilyHistoryDetails::getCancerDetails)
                    .flatMap(cancerDetailsObjects -> cancerDetailsObjects.stream().map(CancerDetailsObject::getCancer)).collect(Collectors.toList());
        } else {
            nonUniqueCancerList = familyHistoryDetailsList.stream().filter(x -> relatives.contains(x.getFamilyMember()) && (x.getRelativeSide().equalsIgnoreCase(relativeSide)
                    || x.getRelativeSide().equalsIgnoreCase("N/A"))).map(FamilyHistoryDetails::getCancerDetails)
                    .flatMap(cancerDetailsObjects -> cancerDetailsObjects.stream().map(CancerDetailsObject::getCancer)).collect(Collectors.toList());
        }

        nonUniqueCancerList.replaceAll(String::toLowerCase);


        Map<String, Integer> relativeCancer = new HashMap<>();
        relativeCancer.put("Breast", Collections.frequency(nonUniqueCancerList, TagConstants.BREAST.getValue().toLowerCase()));
        relativeCancer.put("Prostate", Collections.frequency(nonUniqueCancerList, TagConstants.PROSTATE.getValue().toLowerCase()));
        relativeCancer.put("Ovarian", Collections.frequency(nonUniqueCancerList, TagConstants.OVARIAN.getValue().toLowerCase()));
        relativeCancer.put("Pancreatic", Collections.frequency(nonUniqueCancerList, TagConstants.PANCREATIC.getValue().toLowerCase()));
        relativeCancer.put("Colon", Collections.frequency(nonUniqueCancerList, TagConstants.COLON.getValue().toLowerCase()));
        relativeCancer.put("Endometrial", Collections.frequency(nonUniqueCancerList, TagConstants.UTERINE.getValue().toLowerCase()));
        relativeCancer.put("Gastric", Collections.frequency(nonUniqueCancerList, TagConstants.GASTRIC.getValue().toLowerCase()));
        relativeCancer.put("Brain", Collections.frequency(nonUniqueCancerList, TagConstants.BRAIN.getValue().toLowerCase()));
        relativeCancer.put("SmBowel", Collections.frequency(nonUniqueCancerList, TagConstants.SMALL_INTESTINE.getValue().toLowerCase()));
        relativeCancer.put("Biliary", Collections.frequency(nonUniqueCancerList, TagConstants.BILIARY_TRACT.getValue().toLowerCase()));
        relativeCancer.put("Ureter", Collections.frequency(nonUniqueCancerList, TagConstants.URETER.getValue().toLowerCase()));
        relativeCancer.put("SebAdenoCar", Collections.frequency(nonUniqueCancerList, TagConstants.SEBACEOUS_ADENOMA.getValue().toLowerCase()));
        relativeCancer.put("Bladder", Collections.frequency(nonUniqueCancerList, TagConstants.BLADDER.getValue().toLowerCase()));

        for (Map.Entry<String, Integer> relativeMap : relativeCancer.entrySet()) {
            if (relativeMap.getValue() >= 1) {
                tagsSet.add("Fm" + relativeMap.getKey() + ">=1Ppl" + side + degree);
            }
            if (relativeMap.getValue() >= 2) {
                tagsSet.add("Fm" + relativeMap.getKey() + ">=2Ppl" + side + degree);
            }
            if (relativeMap.getValue() >= 3) {
                tagsSet.add("Fm" + relativeMap.getKey() + ">=3Ppl" + side + degree);
            }
        }

        for (FamilyHistoryDetails familyHistoryDetails : familyHistoryDetailsList) {
            if (familyHistoryDetails.getFamilyMember().equalsIgnoreCase(FATHER)) {
                familyHistoryDetails.setRelativeSide("N/A");
            }
            if (familyHistoryDetails.getFamilyMember().equalsIgnoreCase(MOTHER)) {
                familyHistoryDetails.setRelativeSide("N/A");
            }
        }
        log.info("Exit from getRelativeCountTagsWithDegree method of GuideLineUtility : get relative counts with degree from the family history details");
        return tagsSet;
    }

    /**
     * Method to get check if patient has family cancer history or not or is adopted
     */
    private String checkFamilyHistoryCancer(Questionnaire questionnaire) {
        //log.debug("Entered into checkFamilyHistoryCancer method of GuideLineUtility  with basicQuestions");
        String familyHistoryCaner = "No";
        if (questionnaire.getFamilyCancer() != null && !CollectionUtils.isEmpty(questionnaire.getFamilyCancer().getBasicQuestions())) {
            for (QuestionSet questionSet : questionnaire.getFamilyCancer().getBasicQuestions()) {
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY052") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY052_001")) {
                    familyHistoryCaner = "Yes";
                }
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY052") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY052_003")) {
                    familyHistoryCaner = "Unknown";
                }
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY052") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY052_004")) {
                    familyHistoryCaner = "Unknown (adopted)";
                }
            }
        }
        //log.debug("Exit from checkFamilyHistoryCancer method of GuideLineUtility  with familyHistoryCaner: {}", familyHistoryCaner);
        return familyHistoryCaner;
    }

    public int calculateAge(String dob) {
        //log.info("Entered into calculateAge method of GuidelineUtility to calculate age for dob: {} ", dob);
        int age = 0;
        if (!StringUtils.isEmpty(dob)) {
            try {

                String pattern = "MM/dd/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date date = simpleDateFormat.parse(dob);

                Date today = new Date();
                DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                int d1 = Integer.parseInt(formatter.format(date));
                int d2 = Integer.parseInt(formatter.format(today));
                age = (d2 - d1) / 10000;

            } catch (ParseException e) {
                log.error("ParseException : in calculateAge method of GuidelineUtility: {}", e.getMessage());

            }
        }
        //log.info("Exit from calculateAge method of GuidelineUtility with: {} ", age);
        return age;
    }

    public List<CustomQuestions> getCustomQuestions(List<QuestionSet> mammogramCategory) {
        log.info("Entered into getCustomQuestions method of GuideLineUtility : get custom questions");
        Locale locale;

        locale = Locale.ENGLISH;

        //log.info("Method to get the custom questions details from the questionnaire");
        List<CustomQuestions> customQuestionsList = new ArrayList<>();
//        Map<String, List<QuestionSet>> customQuestionSubList =
//                mammogramCategory.stream().collect(Collectors.groupingBy(QuestionSet::getCategory));

        // Need to use linked hashmap to maintain order of category in question set
        Map<String,List<QuestionSet>> customQuestionSubList = mammogramCategory.stream().collect(Collectors.groupingBy(
                QuestionSet::getCategory,
                LinkedHashMap::new, Collectors.toList()));

        boolean breatProcedureDetailsAdded = false;

        for (Map.Entry<String, List<QuestionSet>> entry : customQuestionSubList.entrySet()) {
            CustomQuestions customQuestions = new CustomQuestions();
            customQuestions.setCategoryName(entry.getKey());
            List<ValueLabelDTO> valueLabelDTOList = new ArrayList<>();
            List<String> weightDetails = new ArrayList<>();
            List<String> breatProcedureDetails = new ArrayList<>();
            String selectedBreastProcedure="";

//            Remove sorting logic for custom questions as question sequence is not same for all accounts. So, sorting based on question key will not work.
//            entry.getValue().sort((h1, h2) -> Integer.valueOf(Integer.parseInt(h1.getQuestionKey().replaceAll("[^\\d]", ""))).compareTo(Integer.valueOf(Integer.parseInt(h2.getQuestionKey().replaceAll("[^\\d]", "")))));
            List<QuestionSet> sortedList = entry.getValue();
            for (QuestionSet x : sortedList) {
                ValueLabelDTO valueLabelDTO = new ValueLabelDTO();
                //es will have english label always as per motion point changes
                x.setPreviewLabel(messageSource.getMessage(x.getQuestionKey() + ".previewLabel", null, locale));
                valueLabelDTO.setLabel(x.getPreviewLabel());
                if(x.getPreviewLabel().equalsIgnoreCase("History of Breast Procedure(s)") && !x.getQuestionKey().equalsIgnoreCase("AMBRY148")){
                    if(x.getQuestionKey().equalsIgnoreCase("AMBRY149")){
                        selectedBreastProcedure = x.getAnswerText();
                        valueLabelDTO.setValue(null);
                    }else if (x.getAnswerKey().equalsIgnoreCase("AMBRY150_001") || x.getAnswerKey().equalsIgnoreCase("AMBRY151_001") ||
                            x.getAnswerKey().equalsIgnoreCase("AMBRY152_001")){
                        String procedureDate = getProcedureDate(x.getAnswerText());
                        try {
                            if (CustomQuestionConstants.valueOf(x.getAnswerKey()) != null)
                                breatProcedureDetails.add(CustomQuestionConstants.valueOf(x.getAnswerKey()).getValue().concat(procedureDate));
                        } catch (Exception e) {
                            log.error("Exception in getCustomQuestions of GuideLineUtility with cause : {}", e);
                        }
                        if(breatProcedureDetails.size() == (Arrays.asList(selectedBreastProcedure.split("\\s*,\\s*")).size())){
                            valueLabelDTO.setValue(breatProcedureDetails.stream().collect(Collectors.joining(", ")));
                        }
                    }
                }
                else if (x.getAnswerKey().equalsIgnoreCase("AMBRY160_001") || x.getAnswerKey().equalsIgnoreCase("AMBRY160_002")) {
                    weightDetails.add(messageSource.getMessage(x.getAnswerKey() + ".answerText", null, locale));
                    continue;
                } else if (x.getAnswerKey().equalsIgnoreCase("AMBRY161_001")) {
                    weightDetails.add(x.getAnswerText());
                    if(!CollectionUtils.isEmpty(weightDetails)){
                        valueLabelDTO.setValue(weightDetails.stream().collect(Collectors.joining(", ")));
                    }
                }
                //Changes to handle format of custom questions for HCA.
                else if (x.getAnswerKey().equalsIgnoreCase("AMBRY147_001")) {
                    String text = messageSource.getMessage(x.getAnswerKey() + ".answerText", null, locale);
                    valueLabelDTO.setValue(text.replace("{ClinicName}", null));
                }
                else if (x.getAnswerKey().equalsIgnoreCase("AMBRY141_001")|| x.getAnswerKey().equalsIgnoreCase("AMBRY147_002")){
                    Locale patientLang = getLocaleFromPatientLanguage();
                    if(x.getAnswerText().contains("-")){
                       String enteredText = x.getAnswerText().replaceAll(messageSource.getMessage(x.getAnswerKey() + ".answerText", null, patientLang),"").replaceAll("-","");
                        String answer = messageSource.getMessage(x.getAnswerKey() + ".answerText", null, locale).concat(" -").concat(enteredText);
                        valueLabelDTO.setValue(answer);
                    }
                }else {
                    try {
                        if (CustomQuestionConstants.valueOf(x.getAnswerKey()) != null) {
                            valueLabelDTO.setValue(CustomQuestionConstants.valueOf(x.getAnswerKey()).getValue());
                        }
                    } catch (Exception e) {
                        log.error("Exception in getCustomQuestions while Setting the answer text for answer key: {} with cause: ", x.getAnswerKey(),e);
                        if(x.getAnswerKey().contains(",")){
                            List<String> listOfAnswers = new ArrayList<>();
                            List<String> items = Arrays.asList(x.getAnswerKey().split("\\s*,\\s*"));
                            for (String str : items) {
                                listOfAnswers.add(messageSource.getMessage(str + ".answerText", null, locale));
                            }
                            valueLabelDTO.setValue(String.join(", ", listOfAnswers));
                        }else {
                            String answer = messageSource.getMessage(x.getAnswerKey() + ".answerText", null, locale);
                            if(answer.equalsIgnoreCase("1")){
                        valueLabelDTO.setValue(x.getAnswerText());
                            }else{
                                valueLabelDTO.setValue(answer);
                            }
                        }

                    }
                }
                if(valueLabelDTO.getValue()!=null){
                    valueLabelDTOList.add(valueLabelDTO);
                }
            }
            customQuestions.setQuestionAnswers(valueLabelDTOList);
            customQuestionsList.add(customQuestions);
        }

        log.info("Exit from getCustomQuestions method of GuideLineUtility : get custom questions");
        return customQuestionsList;
    }

    private String getProcedureDate(String dateFormat){
        String splitChar="";
        if(dateFormat.contains("-")){
            splitChar =  "-";
        }else if(dateFormat.contains("/")){
            splitChar =  "/";
        }
        String[] arrOfStr = dateFormat.split(splitChar);
        String procedureDate="";
        if(arrOfStr.length!=0){
            procedureDate = arrOfStr[1]+", "+arrOfStr[0];
        }
        return procedureDate;
    }


    private List<FamilyHistoryDetailsTagsDTO> getCategroirzedFamilyDetailsWithTags(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList) {
        log.info("Entered into getCategroirzedFamilyDetailsWithTags method of GuideLineUtility : get family details with tags");
        List<FamilyHistoryDetailsTagsDTO> categroirzedFamilyDetailsWithTags = new ArrayList<>();
        Map<String, List<FamilyHistoryDetailsTagsDTO>> familyDetailsMap = familyHistoryDetailsTagsDTOList.stream()
                .collect(Collectors.groupingBy(x -> x.getFamilyHistoryDetails().getFamilyMember())
                );

        familyDetailsMap.forEach(
                (k, v) -> {
                    if (k.equalsIgnoreCase(RelativeConstants.MOTHER.getValue())) {
                        categroirzedFamilyDetailsWithTags.add(getFamilyHistoryDetailsDTO(RelativeConstants.MOTHER.getValue(), 1, TagConstants.FEMALE.getValue(), v, "N/A", "FDR"));
                    } else if (k.equalsIgnoreCase(RelativeConstants.FATHER.getValue())) {
                        categroirzedFamilyDetailsWithTags.add(getFamilyHistoryDetailsDTO(RelativeConstants.FATHER.getValue(), 2, TagConstants.MALE.getValue(), v, "N/A", "FDR"));
                    } else if (k.equalsIgnoreCase(RelativeConstants.GRANDMOTHER.getValue())) {
                        categroirzedFamilyDetailsWithTags.addAll(getGrandParents(RelativeConstants.GRANDMOTHER.getValue(), v));
                    } else if (k.equalsIgnoreCase(RelativeConstants.GRANDFATHER.getValue())) {
                        categroirzedFamilyDetailsWithTags.addAll(getGrandParents(RelativeConstants.GRANDFATHER.getValue(), v));
                    } else {
                        categroirzedFamilyDetailsWithTags.addAll(v);
                    }
                }
        );
        log.info("Exit from getCategroirzedFamilyDetailsWithTags method of GuideLineUtility : get family details with tags");
        return categroirzedFamilyDetailsWithTags;

    }

    private FamilyHistoryDetailsTagsDTO getFamilyHistoryDetailsDTO(String relative, Integer sequence, String geneder, List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String relativeSide, String relativeDegree) {
        FamilyHistoryDetailsTagsDTO familyHistoryDetailsTagsDTO = new FamilyHistoryDetailsTagsDTO();
        FamilyHistoryDetails familyHistoryDetails = new FamilyHistoryDetails();
        familyHistoryDetails.setFamilyMember(relative);
        familyHistoryDetails.setSequence(sequence);
        familyHistoryDetails.setRelativeSide(relativeSide);
        familyHistoryDetails.setSex(geneder);
        familyHistoryDetails.setRelativeDegree(relativeDegree);
        List<CancerDetailsObject> cancerDetails = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).map(FamilyHistoryDetails::getCancerDetails).flatMap(Collection::stream).collect(Collectors.toList());
        familyHistoryDetails.setCancerDetails(cancerDetails);
        RelativeDetail relativeDetail = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).
                map(FamilyHistoryDetails::getRelativeDetail).filter(Objects::nonNull).findFirst().orElse(null);
        familyHistoryDetails.setRelativeDetail(relativeDetail);
        familyHistoryDetailsTagsDTO.setTags(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getTags).flatMap(Collection::stream).collect(Collectors.toSet()));
        familyHistoryDetailsTagsDTO.setLynchReasoningStatements(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getLynchReasoningStatements).flatMap(Collection::stream).collect(Collectors.toSet()));
        familyHistoryDetailsTagsDTO.setHbocReasoningStatements(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getHbocReasoningStatements).flatMap(Collection::stream).collect(Collectors.toSet()));
        familyHistoryDetailsTagsDTO.setFamilyHistoryDetails(familyHistoryDetails);
        return familyHistoryDetailsTagsDTO;
    }


    private FamilyHistoryDetailsTagsDTO getGrandParentOrGreatGrandParentObject(String relative, List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String relativeSide, String relativeDegree) {
        FamilyHistoryDetailsTagsDTO familyHistoryDetailsTagsDTO = new FamilyHistoryDetailsTagsDTO();
        FamilyHistoryDetails familyHistoryDetails = new FamilyHistoryDetails();
        familyHistoryDetails.setFamilyMember(relative);
        familyHistoryDetails.setRelativeSide(relativeSide);
        familyHistoryDetails.setRelativeDegree(relativeDegree);
        Set<CancerDetailsObject> cancerDetails = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).map(FamilyHistoryDetails::getCancerDetails)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(CancerDetailsObject::getCancer))));
        List<CancerDetailsObject> cancerDetailsObjectList = new ArrayList<>(cancerDetails);
        familyHistoryDetails.setCancerDetails(cancerDetailsObjectList);
        RelativeDetail relativeDetail = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getFamilyHistoryDetails).
                map(FamilyHistoryDetails::getRelativeDetail).filter(Objects::nonNull).findFirst().orElse(null);
        familyHistoryDetails.setRelativeDetail(relativeDetail);
        familyHistoryDetailsTagsDTO.setTags(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getTags).flatMap(Collection::stream).collect(Collectors.toSet()));
        familyHistoryDetailsTagsDTO.setLynchReasoningStatements(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getLynchReasoningStatements).flatMap(Collection::stream).collect(Collectors.toSet()));
        familyHistoryDetailsTagsDTO.setHbocReasoningStatements(familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getHbocReasoningStatements).flatMap(Collection::stream).collect(Collectors.toSet()));
        familyHistoryDetailsTagsDTO.setFamilyHistoryDetails(familyHistoryDetails);
        return familyHistoryDetailsTagsDTO;
    }


    private List<FamilyHistoryDetailsTagsDTO> getGrandParents(String relative, List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList) {
        List<FamilyHistoryDetailsTagsDTO> categroirzedFamilyDetailsWithTags = new ArrayList<>();
        Map<String, List<FamilyHistoryDetailsTagsDTO>> familyDetailsMap = familyHistoryDetailsTagsDTOList.stream()
                .collect(Collectors.groupingBy(x -> x.getFamilyHistoryDetails().getRelativeSide())
                );
        familyDetailsMap.forEach(
                (k, v) -> {
                    if (k.equalsIgnoreCase("Mother's side")) {
                        categroirzedFamilyDetailsWithTags.add(getFamilyHistoryDetailsDTO(relative, 5, TagConstants.FEMALE.getValue(), v, k, "SDR"));
                    } else if (k.equalsIgnoreCase("Father's side")) {
                        categroirzedFamilyDetailsWithTags.add(getFamilyHistoryDetailsDTO(relative, 6, TagConstants.MALE.getValue(), v, k, "SDR"));
                    }
                }
        );
        return categroirzedFamilyDetailsWithTags;
    }

    private Set<ReasoningStatementAndTags> getReasoningStatementAndTags(Set<ReasoningStatementAndTags> reasoningStatementAndTags) {
        log.info("Entered into getReasoningStatementAndTags method of GuideLineUtility : get reasoning statements and tags");
        Map<String, Set<String>> map = new HashMap<>();

        reasoningStatementAndTags.forEach(reasoningStatement -> {
            Set set = null;
            if (map.containsKey(reasoningStatement.getReasoningStatementId())) {
                set = map.get(reasoningStatement.getReasoningStatementId());
                set.addAll(reasoningStatement.getTagIds());
            } else {
                set = new HashSet();
                set.addAll(reasoningStatement.getTagIds());
            }
            map.put(reasoningStatement.getReasoningStatementId(), set);
        });
        Set<ReasoningStatementAndTags> reasoningStatementAndTagsSet = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            ReasoningStatementAndTags reasoningStatementAndTags1 = new ReasoningStatementAndTags();
            reasoningStatementAndTags1.setReasoningStatementId(entry.getKey());
            reasoningStatementAndTags1.setTagIds(entry.getValue());
            reasoningStatementAndTagsSet.add(reasoningStatementAndTags1);
        }
        return reasoningStatementAndTagsSet;
    }

    private Locale getLocaleFromPatientLanguage() {
       return Locale.ENGLISH;
    }

}

