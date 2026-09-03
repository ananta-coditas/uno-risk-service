package com.genomebrain.backend.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.genomebrain.backend.quesstionnaire.CancerDetails;
import com.genomebrain.backend.quesstionnaire.FamilyCancerDetails;
import com.genomebrain.backend.quesstionnaire.PersonalHistory;
import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.quesstionnaire.RelativeHistory;
import com.genomebrain.backend.service.dto.CancerDetailsDTO;
import com.genomebrain.backend.service.dto.CancerDetailsObject;
import com.genomebrain.backend.service.dto.ClinicalSummary;
import com.genomebrain.backend.service.dto.DocumentType;
import com.genomebrain.backend.service.dto.FamilyHistoryDetails;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.GleasonScoreObject;
import com.genomebrain.backend.service.dto.NCCNTagConstants;
import com.genomebrain.backend.service.dto.OptionConstants;
import com.genomebrain.backend.service.dto.PersonalHistoryDetails;
import com.genomebrain.backend.service.dto.PersonalHistoryTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.RelativeConstants;
import com.genomebrain.backend.service.dto.TagConstants;

/**
 * Utility class containing helper methods to generate NCCN Tags.
 */
@Service
public class TagGenerationUtility {

    public static final String PRIOR_BIOPSY_QUESTION = "AMBRY180";
    public static final String BIOPSY_RESULT = "AMBRY106";

    public static final String I_DONT_KNOW = "I don't know";

    public final static String UNKNOWN = "Unknown";

    private final Logger log = LoggerFactory.getLogger(TagGenerationUtility.class);

    @Autowired
    private EthnicityUtil ethnicityUtil;

    /**
     * Get family cancer history details into
     * list of FamilyHistoryDetailsTagsDTO from
     * FamilyCancerDetails
     *
     * @param familyCancerDetails
     * @return list of FamilyHistoryDetailsTagsDTO
     */
    public List<FamilyHistoryDetailsTagsDTO> getFamilyCancerTags(FamilyCancerDetails familyCancerDetails, String version, String familyHistoryOfGeneticTesting) {
        boolean isFamilyGeneticTesting = familyHistoryOfGeneticTesting != null && familyHistoryOfGeneticTesting.equalsIgnoreCase("Yes");
        //log.info("Entered into getFamilyCancerTags method of TagGenerationUtility with familyCancerDetails");
        List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsList = new ArrayList<>();
        if (familyCancerDetails.getMother() != null) {
            familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.MOTHER.getValue(), "FDR", familyCancerDetails.getMother(), TagConstants.FEMALE.getValue(), 1, version, isFamilyGeneticTesting));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getMothers())) {
            familyCancerDetails.getMothers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.MOTHER.getValue(), "FDR", relative, TagConstants.FEMALE.getValue(), 1, version, isFamilyGeneticTesting)));
        }
        if (familyCancerDetails.getFather() != null) {
            familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.FATHER.getValue(), "FDR", familyCancerDetails.getFather(), TagConstants.MALE.getValue(), 2, version, isFamilyGeneticTesting));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getFathers())) {
            familyCancerDetails.getFathers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.FATHER.getValue(), "FDR", relative, TagConstants.MALE.getValue(), 1, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getSisters())) {
            familyCancerDetails.getSisters().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.SISTER.getValue(), "FDR", relative, TagConstants.FEMALE.getValue(), 3, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getBrothers())) {
            familyCancerDetails.getBrothers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.BROTHER.getValue(), "FDR", relative, TagConstants.MALE.getValue(), 4, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getDaughters())) {
            familyCancerDetails.getDaughters().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.DAUGHTER.getValue(), "FDR", relative, TagConstants.FEMALE.getValue(), 5, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getSons())) {
            familyCancerDetails.getSons().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.SON.getValue(), "FDR", relative, TagConstants.MALE.getValue(), 6, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getNieces())) {
            familyCancerDetails.getNieces().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.NIECE.getValue(), "SDR", relative, TagConstants.FEMALE.getValue(), 7, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getNephews())) {
            familyCancerDetails.getNephews().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.NEPHEW.getValue(), "SDR", relative, TagConstants.MALE.getValue(), 8, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getHalfSisters())) {
            familyCancerDetails.getHalfSisters().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.HALF_SISTER.getValue(), "SDR", relative, TagConstants.FEMALE.getValue(), 9, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getHalfBrothers())) {
            familyCancerDetails.getHalfBrothers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.HALF_BROTHER.getValue(), "SDR", relative, TagConstants.MALE.getValue(), 10, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGrandDaughters())) {
            familyCancerDetails.getGrandDaughters().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GRANDDAUGHTER.getValue(), "SDR", relative, TagConstants.FEMALE.getValue(), 11, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGrandSons())) {
            familyCancerDetails.getGrandSons().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GRANDSON.getValue(), "SDR", relative, TagConstants.MALE.getValue(), 12, version, isFamilyGeneticTesting)));
        }
        //Maternal or Paternal relatives
        if (!CollectionUtils.isEmpty(familyCancerDetails.getAunts())) {
            familyCancerDetails.getAunts().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.AUNT.getValue(), "SDR", relative, TagConstants.FEMALE.getValue(), 1, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getUncles())) {
            familyCancerDetails.getUncles().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.UNCLE.getValue(), "SDR", relative, TagConstants.MALE.getValue(), 2, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getFirstCousinsFemale())) {
            familyCancerDetails.getFirstCousinsFemale().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.FIRST_COUSIN_FEMALE.getValue(), "TDR", relative, TagConstants.FEMALE.getValue(), 3, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getFirstCousinsMale())) {
            familyCancerDetails.getFirstCousinsMale().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.FIRST_COUSIN_MALE.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 4, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGrandMothers())) {
            familyCancerDetails.getGrandMothers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GRANDMOTHER.getValue(), "SDR", relative, TagConstants.FEMALE.getValue(), 5, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGrandFathers())) {
            familyCancerDetails.getGrandFathers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GRANDFATHER.getValue(), "SDR", relative, TagConstants.MALE.getValue(), 6, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatUncles())) {
            familyCancerDetails.getGreatUncles().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_UNCLE.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 7, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatAunts())) {
            familyCancerDetails.getGreatAunts().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_AUNT.getValue(), "TDR", relative, TagConstants.FEMALE.getValue(), 8, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatGrandMothers())) {
            familyCancerDetails.getGreatGrandMothers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_GRANDMOTHER.getValue(), "TDR", relative, TagConstants.FEMALE.getValue(), 9, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatGrandFathers())) {
            familyCancerDetails.getGreatGrandFathers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_GRANDFATHER.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 10, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatNieces())) {
            familyCancerDetails.getGreatNieces().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_NIECE.getValue(), "TDR", relative,TagConstants.FEMALE.getValue() , 11, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatNephews())) {
            familyCancerDetails.getGreatNephews().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_NEPHEW.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 12, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatGrandDaughters())) {
            familyCancerDetails.getGreatGrandDaughters().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_GRANDDAUGHTER.getValue(), "TDR", relative, TagConstants.FEMALE.getValue(), 13, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getGreatGrandSons())) {
            familyCancerDetails.getGreatGrandSons().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.GREAT_GRANDSON.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 14, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getHalfAunts())) {
            familyCancerDetails.getHalfAunts().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.HALF_AUNT.getValue(), "TDR", relative, TagConstants.FEMALE.getValue(), 15, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getHalfUncles())) {
            familyCancerDetails.getHalfUncles().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.HALF_UNCLE.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 16, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getHalfNieces())) {
            familyCancerDetails.getHalfNieces().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.HALF_NIECE.getValue(), "TDR", relative,TagConstants.FEMALE.getValue() , 17, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getHalfNephews())) {
            familyCancerDetails.getHalfNephews().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.HALF_NEPHEW.getValue(), "TDR", relative, TagConstants.MALE.getValue(), 18, version, isFamilyGeneticTesting)));
        }
        if (!CollectionUtils.isEmpty(familyCancerDetails.getOthers())) {
            familyCancerDetails.getOthers().forEach(relative -> familyHistoryDetailsList.add(getFamilyHistoryDetails(RelativeConstants.OTHER.getValue(), "", relative, "N/A", 19, version, isFamilyGeneticTesting)));
        }
        //log.info("Exit from getFamilyCancerTags method of TagGenerationUtility with familyCancerDetails");
        return familyHistoryDetailsList;
    }

    /**
     * get the relative cancer tags from
     * cancerDetails with sex and cancerType(FM)
     *
     * @param cancerDetails
     * @param sex
     * @return list of CancerDetailsDTO
     */
    public List<CancerDetailsDTO> getRelativeCancerTags(RelativeHistory cancerDetails, String sex, String version) {
        //log.info("Entered into getRelativeCancerTags method of TagGenerationUtility with sex: {}, cancerType: Fm", sex);
        List<CancerDetailsDTO> cancerDetailsDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cancerDetails.getBreast())) {
            cancerDetailsDTOList.add(getBreastCancerTags(cancerDetails.getBreast(), "Fm", sex));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getOvarian())) {
            cancerDetailsDTOList.add(getOvarianCancerTags(cancerDetails.getOvarian(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getFallopianTube())) {
            cancerDetailsDTOList.add(getFallopianTubeTags(cancerDetails.getFallopianTube(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getProstate())) {
            cancerDetailsDTOList.add(getProstateCancerTags(cancerDetails.getProstate(), "Fm", false, null));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getColorectal())) {
            cancerDetailsDTOList.add(getColorectalCancerTags(cancerDetails.getColorectal(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getPancreatic())) {
            cancerDetailsDTOList.add(getPancreaticCancerTags(cancerDetails.getPancreatic(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getUterine())) {
            cancerDetailsDTOList.add(getUterineOrEndometrialCancerTags(cancerDetails.getUterine(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getStomach())) {
            cancerDetailsDTOList.add(getStomachCancerTags(cancerDetails.getStomach(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSmallIntestine())) {
            cancerDetailsDTOList.add(getSmallIntestineCancerTags(cancerDetails.getSmallIntestine(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getBiliaryTract())) {
            cancerDetailsDTOList.add(getBiliaryTractCancerTags(cancerDetails.getBiliaryTract(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getKidney())) {
            cancerDetailsDTOList.add(getKidneyCancerTag(cancerDetails.getKidney(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getUreter())) {
            cancerDetailsDTOList.add(getUreterCancerTag(cancerDetails.getUreter(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getBrain())) {
            cancerDetailsDTOList.add(getBrainCancerTag(cancerDetails.getBrain(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSebaceousAdenoma())) {
            cancerDetailsDTOList.add(getSebaceousAdenomaOrCarcinomaCancerTag(cancerDetails.getSebaceousAdenoma(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getMelanoma())) {
            cancerDetailsDTOList.add(getMelanomaCancerTag(cancerDetails.getMelanoma(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSkin())) {
            cancerDetailsDTOList.add(getSkinCancerTag(cancerDetails.getSkin(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLeukemia())) {
            cancerDetailsDTOList.add(getLeukemiaTag(cancerDetails.getLeukemia(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLymphoma())) {
            cancerDetailsDTOList.add(getLymphomaTag(cancerDetails.getLymphoma(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getGallbladder())) {
            cancerDetailsDTOList.add(getGallbladderTag(cancerDetails.getGallbladder(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getCervical())) {
            cancerDetailsDTOList.add(getCervicalCancerTag(cancerDetails.getCervical(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getBladder())) {
            cancerDetailsDTOList.add(getBladderCancerTag(cancerDetails.getBladder(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getThroat())) {
            cancerDetailsDTOList.add(getThroatCancerTag(cancerDetails.getThroat(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getThyroid())) {
            cancerDetailsDTOList.add(getThyroidCancerTag(cancerDetails.getThyroid(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getOral())) {
            cancerDetailsDTOList.add(getOralCancerTag(cancerDetails.getOral(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLiver())) {
            cancerDetailsDTOList.add(getLiverCancerTag(cancerDetails.getLiver(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getTesticular())) {
            cancerDetailsDTOList.add(getTesticularCancerTag(cancerDetails.getTesticular(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getParaganglioma())) {
            cancerDetailsDTOList.add(getParagangliomaCancerTag(cancerDetails.getParaganglioma(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getPheochromocytoma())) {
            cancerDetailsDTOList.add(getPheochromocytomaCancerTag(cancerDetails.getPheochromocytoma(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLung())) {
            cancerDetailsDTOList.add(getLungCancerTag(cancerDetails.getLung(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSarcoma())) {
            cancerDetailsDTOList.add(getSarcomaCancerTag(cancerDetails.getSarcoma(), "Fm"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getNotListed())) {
            cancerDetailsDTOList.add(getOtherCancerTag(cancerDetails.getNotListed(), "Fm"));
        }
        //log.info("Exit from getRelativeCancerTags method of TagGenerationUtility");
        return cancerDetailsDTOList;
    }


    /**
     * Get personal cancer tags with
     * cancer type: Pr and gleasonScoreValue
     * and considerGleasonScore from CancerDetails
     *
     * @param cancerDetails
     * @param considerGleasonScore
     * @param gleasonScoreValue
     * @return list CancerDetailsDTO
     */
    public List<CancerDetailsDTO> getPersonalCancerTags(CancerDetails cancerDetails, boolean considerGleasonScore, Long gleasonScoreValue) {
        //log.info("Entered into getPersonalCancerTags method of TagGenerationUtility with considerGleasonScore: {}, gleasonScoreValue: {} cancerType: Pr", considerGleasonScore, gleasonScoreValue);

        List<CancerDetailsDTO> cancerDetailsDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cancerDetails.getBreast())) {
            cancerDetailsDTOList.add(getBreastCancerTags(cancerDetails.getBreast(), "Pr", ""));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getOvarian())) {
            cancerDetailsDTOList.add(getOvarianCancerTags(cancerDetails.getOvarian(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getFallopianTube())) {
            cancerDetailsDTOList.add(getFallopianTubeTags(cancerDetails.getFallopianTube(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getProstate())) {
            cancerDetailsDTOList.add(getProstateCancerTags(cancerDetails.getProstate(), "Pr", considerGleasonScore, gleasonScoreValue));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getColorectal())) {
            cancerDetailsDTOList.add(getColorectalCancerTags(cancerDetails.getColorectal(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getPancreatic())) {
            cancerDetailsDTOList.add(getPancreaticCancerTags(cancerDetails.getPancreatic(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getUterine())) {
            cancerDetailsDTOList.add(getUterineOrEndometrialCancerTags(cancerDetails.getUterine(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getStomach())) {
            cancerDetailsDTOList.add(getStomachCancerTags(cancerDetails.getStomach(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSmallIntestine())) {
            cancerDetailsDTOList.add(getSmallIntestineCancerTags(cancerDetails.getSmallIntestine(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getBiliaryTract())) {
            cancerDetailsDTOList.add(getBiliaryTractCancerTags(cancerDetails.getBiliaryTract(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getKidney())) {
            cancerDetailsDTOList.add(getKidneyCancerTag(cancerDetails.getKidney(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getUreter())) {
            cancerDetailsDTOList.add(getUreterCancerTag(cancerDetails.getUreter(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getBrain())) {
            cancerDetailsDTOList.add(getBrainCancerTag(cancerDetails.getBrain(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSebaceousAdenoma())) {
            cancerDetailsDTOList.add(getSebaceousAdenomaOrCarcinomaCancerTag(cancerDetails.getSebaceousAdenoma(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getMelanoma())) {
            cancerDetailsDTOList.add(getMelanomaCancerTag(cancerDetails.getMelanoma(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSkin())) {
            cancerDetailsDTOList.add(getSkinCancerTag(cancerDetails.getSkin(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLeukemia())) {
            cancerDetailsDTOList.add(getLeukemiaTag(cancerDetails.getLeukemia(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLymphoma())) {
            cancerDetailsDTOList.add(getLymphomaTag(cancerDetails.getLymphoma(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getGallbladder())) {
            cancerDetailsDTOList.add(getGallbladderTag(cancerDetails.getGallbladder(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getCervical())) {
            cancerDetailsDTOList.add(getCervicalCancerTag(cancerDetails.getCervical(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getBladder())) {
            cancerDetailsDTOList.add(getBladderCancerTag(cancerDetails.getBladder(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getThroat())) {
            cancerDetailsDTOList.add(getThroatCancerTag(cancerDetails.getThroat(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getThyroid())) {
            cancerDetailsDTOList.add(getThyroidCancerTag(cancerDetails.getThyroid(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getOral())) {
            cancerDetailsDTOList.add(getOralCancerTag(cancerDetails.getOral(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLiver())) {
            cancerDetailsDTOList.add(getLiverCancerTag(cancerDetails.getLiver(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getTesticular())) {
            cancerDetailsDTOList.add(getTesticularCancerTag(cancerDetails.getTesticular(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getParaganglioma())) {
            cancerDetailsDTOList.add(getParagangliomaCancerTag(cancerDetails.getParaganglioma(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getPheochromocytoma())) {
            cancerDetailsDTOList.add(getPheochromocytomaCancerTag(cancerDetails.getPheochromocytoma(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getLung())) {
            cancerDetailsDTOList.add(getLungCancerTag(cancerDetails.getLung(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getSarcoma())) {
            cancerDetailsDTOList.add(getSarcomaCancerTag(cancerDetails.getSarcoma(), "Pr"));
        }
        if (!CollectionUtils.isEmpty(cancerDetails.getNotListed())) {
            cancerDetailsDTOList.add(getOtherCancerTag(cancerDetails.getNotListed(), "Pr"));
        }
        //log.info("Exit from getPersonalCancerTags method of TagGenerationUtility");
        return cancerDetailsDTOList;
    }

    /**
     * generate the family history details
     * with passed parameter and convert it to
     * FamilyHistoryDetailsTagsDTO and return it
     *
     * @param relativeType
     * @param relativeDegree
     * @param relativeHistory
     * @param sex
     * @param sequence
     * @return FamilyHistoryDetailsTagsDTO
     */
    private FamilyHistoryDetailsTagsDTO getFamilyHistoryDetails(String relativeType, String relativeDegree, RelativeHistory relativeHistory, String sex, int sequence, String version, Boolean relativeHistoryOfGeneticTesting) {
        //log.debug("Entered into getFamilyHistoryDetails method of TagGenerationUtility with relativeType: {}, relativeDegree: {} sex: {}, sequence: {}", relativeType, relativeDegree, sex, sequence);

        FamilyHistoryDetailsTagsDTO familyHistoryDetailsTagsDTO = new FamilyHistoryDetailsTagsDTO();
        FamilyHistoryDetails familyHistoryDetails = new FamilyHistoryDetails();
        Set<String> tagsSet = new HashSet<>();
        familyHistoryDetails.setFamilyMember(relativeType);
        familyHistoryDetails.setSex(sex);
        familyHistoryDetails.setSequence(sequence);
        familyHistoryDetails.setRelativeDegree(relativeDegree);
        String relativeSide = getRelativeSide(relativeHistory.getBasicQuestions());
        if ("Mat".equalsIgnoreCase(relativeSide)) {
            familyHistoryDetails.setRelativeSide("Mother's side");
        } else if ("Pat".equalsIgnoreCase(relativeSide)) {
            familyHistoryDetails.setRelativeSide("Father's side");
        } else {
            familyHistoryDetails.setRelativeSide("N/A");
        }
        List<CancerDetailsDTO> familyCancerDetailsDTOList = getRelativeCancerTags(relativeHistory, sex, version);
        List<CancerDetailsObject> cancerDetailsList = new ArrayList<>();
        familyCancerDetailsDTOList.forEach(cancerDetailsDTO -> {
            CancerDetailsObject cancerDetails = new CancerDetailsObject();
            if (cancerDetailsDTO.getAdditionalInformation() != null && !cancerDetailsDTO.getAdditionalInformation().isEmpty()) {
                cancerDetails.setAdditionalInformation(cancerDetailsDTO.getAdditionalInformation());
            }
            cancerDetails.setCancer(cancerDetailsDTO.getCancerType());

            if (cancerDetailsDTO.getAgeOnSet() == null) {
                cancerDetails.setAge("-");
            } else if (cancerDetailsDTO.getAgeOnSet() == 0) {
                cancerDetails.setAge("Unknown");
            } else {
                cancerDetails.setAge(cancerDetailsDTO.getAgeOnSet().toString());
            }
            cancerDetailsList.add(cancerDetails);
        });
        familyHistoryDetails.setCancerDetails(cancerDetailsList);
        //setting relativeDetailsObject
        if (relativeHistory != null)
            familyHistoryDetails.setRelativeDetail(relativeHistory.getRelativeDetail());
        if (!familyHistoryDetails.getFamilyMember().equalsIgnoreCase(RelativeConstants.OTHER.getValue())) {
            Set<String> familyCancerTags = familyCancerDetailsDTOList.stream().map(CancerDetailsDTO::getTags).flatMap(Collection::stream).collect(Collectors.toSet());
            for (String tag : familyCancerTags) {
                if (!"N/A".equalsIgnoreCase(relativeSide)) {
                    tagsSet.add(tag + relativeSide + relativeDegree);
                } else {
                    if ("Mother".equalsIgnoreCase(relativeType)) {
                        tagsSet.add(tag + "Mat" + relativeDegree);
                    } else if ("Father".equalsIgnoreCase(relativeType)) {
                        tagsSet.add(tag + "Pat" + relativeDegree);
                    } else {
                        tagsSet.add(tag + "Mat" + relativeDegree);
                        tagsSet.add(tag + "Pat" + relativeDegree);
                    }
                }
            }
        }
        // Generate genetic testing tags for this specific relative and add to tagsSet
        Set<String> geneticTestingTags = new HashSet<>();
        // Check if genetic testing history flag is enabled
        if (Boolean.TRUE.equals(relativeHistoryOfGeneticTesting)) {
            processRelativeGeneticTesting(relativeHistory, relativeType, geneticTestingTags);
        }
        if (!geneticTestingTags.isEmpty()) {
            tagsSet.addAll(geneticTestingTags);
        }

        familyHistoryDetailsTagsDTO.setFamilyHistoryDetails(familyHistoryDetails);
        familyHistoryDetailsTagsDTO.setTags(tagsSet);
        familyHistoryDetailsTagsDTO.setHbocReasoningStatements(checkForSameRelativeHbocGuidelineDetails(tagsSet));
        familyHistoryDetailsTagsDTO.setLynchReasoningStatements(checkForSameRelativeLynchGuidelineDetails(tagsSet));
        //log.debug("Exit from getFamilyHistoryDetails method of TagGenerationUtility");
        return familyHistoryDetailsTagsDTO;
    }

    /**
     * Process genetic testing data for a single relative and generate tags.
     * Checks for AMBRY191 and AMBRY192 questions which indicate positive genetic test results with gene information.
     *
     * @param relativeHistory    The relative's history containing genetic testing data
     * @param relativeType       The type of relative (e.g., "Mother", "Father", "Sister")
     * @param geneticTestingTags The set to add generated tags to
     */
    private void processRelativeGeneticTesting(RelativeHistory relativeHistory, String relativeType, Set<String> geneticTestingTags) {
        if (relativeHistory == null || relativeHistory.getRelativeDetail() == null) {
            log.debug("Skipping genetic testing tag generation: relativeHistory or relativeDetail is null");
            return;
        }

        // Also check relativeDetail.getRelativeType() to be extra safe
        String relativeDetailType = relativeHistory.getRelativeDetail().getRelativeType();
        if (relativeDetailType != null && RelativeConstants.OTHER.getValue().equalsIgnoreCase(relativeDetailType)) {
            log.debug("Skipping genetic testing tag generation for non-blood relative from relativeDetail: {}", relativeDetailType);
            return;
        }

        // Check AMBRY191 first - if it exists and answer is not AMBRY191_001 (No/Unknown), use its answerText
        QuestionSet ambry191Question = findQuestion(relativeHistory, "AMBRY191");
        if (ambry191Question != null) {
            String ambry191AnswerKey = ambry191Question.getAnswerKey();
            // If AMBRY191 exists and answer is not AMBRY191_001 (typically "No" or "Unknown"), use it
            if (StringUtils.isNotEmpty(ambry191AnswerKey)
                    && !"AMBRY191_001".equalsIgnoreCase(ambry191AnswerKey)) {

                String answerText = null;

                switch (ambry191AnswerKey.toUpperCase()) {
                    case "AMBRY191_002":
                        answerText = "Negative";
                        break;

                    case "AMBRY191_003":
                        answerText = "VUS";
                        break;
                    case "AMBRY191_004":
                        answerText = "Unknown";
                        break;

                    default:
                        log.debug("Unhandled AMBRY191 answer key: {}", ambry191AnswerKey);
                }

                if (StringUtils.isNotEmpty(answerText)) {
                    relativeHistory.getRelativeDetail().setRelativeGeneList(answerText);
                    log.debug("Using AMBRY191 answerText for relative gene list: {}", answerText);
                }}
        }

        // Look for AMBRY192 in basicQuestions
        // AMBRY192 question indicates the relative has a positive genetic test result
        QuestionSet ambry192Question = findQuestion(relativeHistory, "AMBRY192");
        if (ambry192Question == null) {
            log.debug("AMBRY192 question not found for relative: {}", relativeType);
            return;
        }


        if (ambry192Question.getAnswerKey().equalsIgnoreCase("AMBRY192_056")) {
            relativeHistory.getRelativeDetail()
                    .setRelativeGeneList("Positive - " + "Other");
        } else if (ambry192Question.getAnswerKey().equalsIgnoreCase("AMBRY192_001")) {
            relativeHistory.getRelativeDetail()
                    .setRelativeGeneList("Positive - " + "Unknown");
        } else {
            // Extract genes from the question
            Set<String> genes = extractGenesFromQuestion(ambry192Question);

            // Set gene list as comma-separated string (better format than toString())
            if (!genes.isEmpty()) {
                String geneList = String.join(", ", genes);
                relativeHistory.getRelativeDetail()
                        .setRelativeGeneList("Positive - " + geneList);
                log.debug("Extracted genes for relative {}: {}", relativeType, geneList);
            }


            // Generate tags if genes were found
            if (!genes.isEmpty() && !RelativeConstants.OTHER.getValue().equalsIgnoreCase(relativeType) ) {
                String baseTag = NCCNTagConstants.FM_POSITIVE_GENE_MUTATION.getValue();

                // Generate specific tags for each gene: FmPositiveGeneMutation-GENE_NAME
                for (String gene : genes) {
                    String geneUpper = gene.toUpperCase();
                    String specificTag = baseTag + "-" + geneUpper;
                    geneticTestingTags.add(specificTag);
                    log.debug("Generated genetic testing tag: {} for relative: {}, gene: {}",
                            specificTag, relativeType, gene);
                }

                // Add generic tag: FmPositiveGeneMutation (indicates any family member has pathogenic gene)
                geneticTestingTags.add(baseTag);
            } else {
                log.debug("No valid genes found for relative: {}", relativeType);
            }
        }
    }

    /**
     * Finds a question by QuestionKey from the relative history's basic questions.
     *
     * @param relativeHistory the relative history to search
     * @param questionKeyToFind the QuestionKey (e.g., "AMBRY192", "AMBRY191")
     * @return the QuestionSet matching the key, or null if not found
     */
    private QuestionSet findQuestion(RelativeHistory relativeHistory, String questionKeyToFind) {

        if (relativeHistory == null
                || CollectionUtils.isEmpty(relativeHistory.getBasicQuestions())
                || questionKeyToFind == null) {
            return null;
        }

        for (QuestionSet questionSet : relativeHistory.getBasicQuestions()) {
            String key = questionSet.getQuestionKey();
            if (key != null && key.equalsIgnoreCase(questionKeyToFind)) {
                return questionSet;
            }
        }

        return null;
    }


    /**
     * Extracts gene names from the AMBRY192 question.
     * First tries to parse from answerKey using OptionConstants,
     * then falls back to answerText if needed.
     *
     * @param questionSet the AMBRY192 question set
     * @return a set of unique gene names
     */
    public Set<String> extractGenesFromQuestion(QuestionSet questionSet) {
        Set<String> genes = new LinkedHashSet<>(); // Use LinkedHashSet to preserve order and avoid duplicates

        if (questionSet == null) {
            log.warn("Cannot extract genes: questionSet is null");
            return genes;
        }

        String answerKey = questionSet.getAnswerKey();
        String answerText = questionSet.getAnswerText();

        // Skip if answerKey is AMBRY192_001 (I don't know/Unknown)
        if (StringUtils.isEmpty(answerKey) || "AMBRY192_001".equalsIgnoreCase(answerKey)) {
            // Try answerText as fallback if answerKey is empty or unknown
            if (StringUtils.isNotEmpty(answerText)) {
                extractGenesFromText(answerText, genes);
            }
            return genes;
        }

        // Extract genes from answerKey (comma-separated answer keys like "AMBRY192_002,AMBRY192_003")
        boolean foundAnyGene = extractGenesFromAnswerKey(answerKey, genes);

        // If no genes found from answerKey, try answerText as fallback
        if (!foundAnyGene && StringUtils.isNotEmpty(answerText)) {
            extractGenesFromText(answerText, genes);
        }

        return genes;
    }

    /**
     * Extracts gene names from comma-separated answer keys using OptionConstants.
     *
     * @param answerKey comma-separated answer keys
     * @param genes set to add extracted genes to
     * @return true if at least one gene was successfully extracted
     */
    private boolean extractGenesFromAnswerKey(String answerKey, Set<String> genes) {
        if (StringUtils.isEmpty(answerKey)) {
            return false;
        }

        boolean foundAny = false;
        String[] answerKeyArray = answerKey.split(",");

        for (String key : answerKeyArray) {
            String trimmedKey = key.trim();
            if (StringUtils.isEmpty(trimmedKey)) {
                continue;
            }

            try {
                // Use OptionConstants to get gene name from answer key
                OptionConstants option = OptionConstants.valueOf(trimmedKey.toUpperCase());
                String geneName = option.getValue();

                // Skip "Unknown" and "Other" values
                if (StringUtils.isNotEmpty(geneName) &&
                    !UNKNOWN.equalsIgnoreCase(geneName) ) {
                    genes.add(geneName);
                    foundAny = true;
                }
            } catch (IllegalArgumentException e) {
                // OptionConstants doesn't have this key
                log.debug("OptionConstants not found for key: {}", trimmedKey);
            }
        }

        return foundAny;
    }

    /**
     * Extracts gene names from comma-separated text.
     *
     * @param answerText comma-separated gene names
     * @param genes set to add extracted genes to
     */
    private void extractGenesFromText(String answerText, Set<String> genes) {
        if (StringUtils.isEmpty(answerText)) {
            return;
        }

        String[] geneArray = answerText.split(",");
        for (String gene : geneArray) {
            String trimmedGene = gene.trim();
            if (StringUtils.isNotEmpty(trimmedGene) &&
                !UNKNOWN.equalsIgnoreCase(trimmedGene)) {
                genes.add(trimmedGene);
            }
        }
    }

    /**
     * Retrieve the answerText from OptionConstants enum
     * based on answerkey
     *
     * @param answerKey
     * @return value present againg OptionConstants enum
     */
    private String getSelectedOption(String answerKey) {
        log.debug("Entered into getSelectedOption method of TagGenerationUtility with answerKey: {}", answerKey);
        String answerText = "";
        try {
            if (OptionConstants.valueOf(answerKey) != null) {
                answerText = OptionConstants.valueOf(answerKey).getValue();
            }
        } catch (Exception e) {
            log.error("Exception in getSelectedOption method of TagGenerationUtility msg: No value found for answer key: {}", answerKey);
        }
        log.debug("Exit from getSelectedOption method of TagGenerationUtility with answerText: {}", answerText);
        return answerText;
    }

    /**
     * Get Tags for selected Option from
     * QuestionSet list
     *
     * @param questionSetList
     * @return tagsSet
     */
    public Set<String> getTagsForSelectedOptions(List<QuestionSet> questionSetList) {
        //log.debug("Entered into getTagsForSelectedOptions method of TagGenerationUtility with list of QuestionSet:");
        List<String> optionsSelected = new ArrayList<>();
        Set<String> tagsSet = new HashSet<>();
        for (QuestionSet questionSet : questionSetList) {
            if(StringUtils.isEmpty(questionSet.getAnswerKey())) {
                throw new IllegalArgumentException("Answer key cannot be null or empty "+questionSet.getQuestionKey());
            }
            optionsSelected.addAll(Stream.of(questionSet.getAnswerKey().split(",", -1)).collect(Collectors.toList()));
        }
        optionsSelected.forEach(x -> {
            try {
                if (TagConstants.valueOf(x) != null) {
                    tagsSet.add(TagConstants.valueOf(x).getValue());
                }
            } catch (Exception e) {
                log.error("Exception in getTagsForSelectedOptions method of TagGenerationUtility: No tag for option: {}", x);
            }
        });
        tagsSet.removeAll(Arrays.asList("", null));
        //log.debug("Exit from getTagsForSelectedOptions method of TagGenerationUtility with tagsSet:");
        return tagsSet;
    }

    /**
     * get the side of relative from basicQuestions
     *
     * @param basicQuestions
     * @return relative side
     */
    public String getRelativeSide(List<QuestionSet> basicQuestions) {
        //log.debug("Entered into getRelativeSide method of TagGenerationUtility with list of basicQuestions(QuestionSet):");
        String relativeSide = "N/A";
        if (!CollectionUtils.isEmpty(basicQuestions)) {
            for (QuestionSet questionSet : basicQuestions) {
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY054")) {
                    if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY054_001")) {
                        relativeSide = "Mat";
                    } else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY054_002")) {
                        relativeSide = "Pat";
                    } else {
                        relativeSide = "N/A";
                    }
                }
            }
        }
        //log.debug("Exit from getRelativeSide method of TagGenerationUtility with relativeSide: {}", relativeSide);
        return relativeSide;
    }

    /**
     * get the Other cancer tag based on
     * cancer type
     *
     * @param other
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getOtherCancerTag(List<QuestionSet> other, String cancerType) {
        //log.debug("Entered into getOtherCancerTag method of TagGenerationUtility to personal other cancer tag generation with list of QuestionSet, cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Other/Unknown");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "OtherUnspec");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : other) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY050_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY050") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY093_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY093") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getOtherCancerTag method of TagGenerationUtility cancerDetailsDTO:");
        return cancerDetailsDTO;
    }

    /**
     * Method to get sarcoma cancer tags for personal and family history
     *
     * @param sarcoma
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getSarcomaCancerTag(List<QuestionSet> sarcoma, String cancerType) {
        //log.debug("Entered into getSarcomaCancerTag method of TagGenerationUtility toPersonal sarcoma cancer tag generation with list of sarcoma(QuestionSet), cancerType: {}:", cancerType);
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Sarcoma (bone or soft tissue)");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Sarcoma");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : sarcoma) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY049_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY049") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY092_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY092") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getSarcomaCancerTag method of TagGenerationUtility cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get lung cancer tags for personal and family history
     *
     * @param lung       is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getLungCancerTag(List<QuestionSet> lung, String cancerType) {
        //log.debug("Entered into getLungCancerTag method of TagGenerationUtility to personal lung cancer tag generation with list of sarcoma(QuestionSet), cancerType: {}:", cancerType);
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Lung");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Lung");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : lung) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY048_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY048") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY091_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY091") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getLungCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get Pheochromocytoma cancer tags for personal and family history
     *
     * @param pheochromocytoma is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getPheochromocytomaCancerTag(List<QuestionSet> pheochromocytoma, String cancerType) {
        //log.debug("Entered into getPheochromocytomaCancerTag method of TagGenerationUtility to Personal pheochromocytoma cancer tag generation with list of pheochromocytoma(QuestionSet), cancerType: {}:", cancerType);
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Pheochromocytoma");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Pheochromocytoma");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : pheochromocytoma) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY047_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY047") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY090_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY090") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getPheochromocytomaCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get Paraganglioma cancer tags for personal and family history
     *
     * @param paraganglioma is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getParagangliomaCancerTag(List<QuestionSet> paraganglioma, String cancerType) {
        //log.debug("Entered into getParagangliomaCancerTag method of TagGenerationUtility to Personal paraganglioma cancer tag generation with list of paraganglioma(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Paraganglioma");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Paraganglioma");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : paraganglioma) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY046_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY046") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY089_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY089") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getParagangliomaCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get testicular cancer tags for personal and family history
     *
     * @param testicular
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getTesticularCancerTag(List<QuestionSet> testicular, String cancerType) {
        //log.debug("Entered into getParagangliomaCancerTag method of TagGenerationUtility toPersonal testicular cancer tag generation with list of testicular(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Testicular");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Testicular");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : testicular) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY045_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY045") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY088_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY088") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getTesticularCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get liver cancer tags for personal and family history
     *
     * @param liver      is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getLiverCancerTag(List<QuestionSet> liver, String cancerType) {
        //log.debug("Entered into getLiverCancerTag method of TagGenerationUtility to Personal liver cancer tag generation with list of liver(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Liver");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Liver");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : liver) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY044_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY044") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY087_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY087") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getLiverCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get oral cancer tags for personal and family history
     *
     * @param oral       is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getOralCancerTag(List<QuestionSet> oral, String cancerType) {
        //log.debug("Entered into getOralCancerTag method of TagGenerationUtility to Personal oral cancer tag generation with list of oral(QuestionSet), cancerType: {}:", cancerType);
        Set<String> tagsSet = new HashSet<>();
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Oral");
        tagsSet.add(cancerType + "Oral");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : oral) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY043_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY043") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY086_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY086") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getOralCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get thyroid cancer tags for personal and family history
     *
     * @param thryoid    is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getThyroidCancerTag(List<QuestionSet> thryoid, String cancerType) {
        //log.debug("Entered into getThyroidCancerTag method of TagGenerationUtility to Personal thyroid cancer tag generation with list of thyroid(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Thyroid");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Thyroid");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : thryoid) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY042_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY042") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY085_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY085") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getThyroidCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get throat cancer tags for personal and family history
     *
     * @param throat     is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getThroatCancerTag(List<QuestionSet> throat, String cancerType) {
        //log.debug("Entered into getThroatCancerTag method of TagGenerationUtility to Personal thyroid cancer tag generation with list of throat(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Throat");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Throat");

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : throat) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY041_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY041") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY084_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY084") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getThroatCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get bladder tags for personal and family history
     *
     * @param bladder    is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getBladderCancerTag(List<QuestionSet> bladder, String cancerType) {
        //log.debug("Entered into getBladderCancerTag method of TagGenerationUtility to Personal bladder cancer tag generation with list of throat(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Bladder");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Bladder");

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : bladder) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY040_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_BLADDER_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY040") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY083_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmBladder<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmBladder<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY083") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getBladderCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get cervical cancer tags for personal and family history
     *
     * @param cervical   is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getCervicalCancerTag(List<QuestionSet> cervical, String cancerType) {
        //log.debug("Entered into getCervicalCancerTag method of TagGenerationUtility to Personal cervical cancer tag generation with list of cervical(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Cervical");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Cervical");

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : cervical) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY039_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY039") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY082_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY082") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getCervicalCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get gallbladder tags for personal and family history
     *
     * @param gallbladder is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getGallbladderTag(List<QuestionSet> gallbladder, String cancerType) {
        //log.debug("Entered into getGallbladderTag method of TagGenerationUtility to Personal Gallbladder cancer tag generation with list of gallbladder(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Gallbladder");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Gallbladder");

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : gallbladder) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY038_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY038") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY081_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY081") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getCervicalCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get leukemia tags for personal and family history
     *
     * @param leukemia   is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getLeukemiaTag(List<QuestionSet> leukemia, String cancerType) {
        //log.debug("Entered into getLeukemiaTag method of TagGenerationUtility to Personal Leukemia Or Lymphoma cancer tag generation with list of leukemia(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Leukemia");
        Map<String, String> additionalDetails = new HashMap<>();

        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Leukemia");
        tagsSet.addAll(getTagsForSelectedOptions(leukemia));

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : leukemia) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY035")) {
                additionalDetails.put("diagnosisOfCLLOrCML", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY036")) {
                additionalDetails.put("hxOfAllogeneicTransplant", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY037")) {
                additionalDetails.put("cancerFreeOrRemission", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY034_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY034") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY079_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY079") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getLeukemiaTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get lymphoma tags for personal and family history
     *
     * @param lymphoma   is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getLymphomaTag(List<QuestionSet> lymphoma, String cancerType) {
        //log.debug("Entered into getLymphomaTag method of TagGenerationUtility to Personal Leukemia Or Lymphoma cancer tag generation with list of lymphoma(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        Map<String, String> additionalDetails = new HashMap<>();
        cancerDetailsDTO.setCancerType("Lymphoma");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Lymphoma");
        tagsSet.addAll(getTagsForSelectedOptions(lymphoma));

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : lymphoma) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY036")) {
                additionalDetails.put("hxOfAllogeneicTransplant", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY037")) {
                additionalDetails.put("cancerFreeOrRemission", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY034_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY034") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY080_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY080") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getLymphomaTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get sebaceousAdenoma or carcinoma cancer tags for personal and family history
     *
     * @param sebaceousAdenomaOrCarcinoma is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getSebaceousAdenomaOrCarcinomaCancerTag(List<QuestionSet> sebaceousAdenomaOrCarcinoma, String cancerType) {
        //log.debug("Entered into getSebaceousAdenomaOrCarcinomaCancerTag method of TagGenerationUtility to Personal Sebaceous Adenoma Or Carcinoma cancer tag generation with list of sebaceousAdenomaOrCarcinoma(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Sebaceous Adenoma or Carcinoma (Rare skin tumor or cancer)");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "SebAdenoCar");

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : sebaceousAdenomaOrCarcinoma) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY032_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_SEB_ADENO_CAR_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY032") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY077_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmSebAdenoCar<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmSebAdenoCar<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY077") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getSebaceousAdenomaOrCarcinomaCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get skin cancer tags for personal and family history
     *
     * @param skin       is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getSkinCancerTag(List<QuestionSet> skin, String cancerType) {
        //log.debug("Entered into getSkinCancerTag method of TagGenerationUtility to  skin cancer tag generation with list of skin(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Skin");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Skin");
        tagsSet.addAll(getTagsForSelectedOptions(skin));
        Map<String, String> additionalDetails = new HashMap<>();

        Integer melanomaAgeOfDiagnosis = null;
        Integer adenomaAgeOfDiagnosis = null;

        for (QuestionSet questionSet : skin) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY121")) {
                List<String> answerKey = Stream.of(questionSet.getAnswerKey().split(",", -1)).collect(Collectors.toList());
                String answerText = answerKey.stream().map(x -> getSelectedOption(x)).collect(Collectors.joining(", "));
                additionalDetails.put("typeOfSkinCancer", answerText);
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY123")) {
                List<String> answerKey = Stream.of(questionSet.getAnswerKey().split(",", -1)).collect(Collectors.toList());
                String answerText = answerKey.stream().map(x -> getSelectedOption(x)).collect(Collectors.joining(", "));
                additionalDetails.put("typeOfSkinCancer", answerText);
            }

            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY078_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                melanomaAgeOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            }else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY078_002") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                melanomaAgeOfDiagnosis = 0;
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY078") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                melanomaAgeOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY033_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                melanomaAgeOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY033") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                melanomaAgeOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY032_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                adenomaAgeOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (adenomaAgeOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_SEB_ADENO_CAR_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY032") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                adenomaAgeOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY077_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                adenomaAgeOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (adenomaAgeOfDiagnosis <= 50) {
                    tagsSet.add("FmSebAdenoCar<=50");
                }
                if (adenomaAgeOfDiagnosis < 50) {
                    tagsSet.add("FmSebAdenoCar<50");
                }
            }else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY077_002") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                adenomaAgeOfDiagnosis = 0;
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY077") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                adenomaAgeOfDiagnosis = null;
            }
        }
        if( melanomaAgeOfDiagnosis!=null && melanomaAgeOfDiagnosis == 0) {
            additionalDetails.put("melanomaAgeOfDiagnosis", OptionConstants.AMBRY003_012.getValue());
        }else if( melanomaAgeOfDiagnosis!=null){
            additionalDetails.put("melanomaAgeOfDiagnosis", String.valueOf(melanomaAgeOfDiagnosis));
        }
        if(adenomaAgeOfDiagnosis!=null && adenomaAgeOfDiagnosis == 0){
            additionalDetails.put("adenomaAgeOfDiagnosis", OptionConstants.AMBRY003_012.getValue() );
        }else if(adenomaAgeOfDiagnosis!=null){
            additionalDetails.put("adenomaAgeOfDiagnosis", String.valueOf(adenomaAgeOfDiagnosis));
        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getSkinCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get melanoma cancer tags for personal and family history
     *
     * @param melanoma   is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getMelanomaCancerTag(List<QuestionSet> melanoma, String cancerType) {
        //log.debug("Entered into getMelanomaCancerTag method of TagGenerationUtility to Personal melanoma cancer tag generation with list of melanoma(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Melanoma");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Melanoma");

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : melanoma) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY033_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY033") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY078_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY078") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getMelanomaCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method  to get Brain cancer tags for personal and family history
     *
     * @param brain      is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getBrainCancerTag(List<QuestionSet> brain, String cancerType) {
        //log.debug("Entered into getBrainCancerTag method of TagGenerationUtility to Personal brain cancer tag generation with list of brain(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Brain");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Brain");
        tagsSet.addAll(getTagsForSelectedOptions(brain));

        Map<String, String> additionalDetails = new HashMap<>();

        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : brain) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY031")) {
                additionalDetails.put("malignant", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY030_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_BRAIN_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY030") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY076_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmBrain<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmBrain<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY076") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getBrainCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get Ureter cancer tags for personal and family history
     *
     * @param ureterRenalPelvis is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getUreterCancerTag(List<QuestionSet> ureterRenalPelvis, String cancerType) {
        //log.debug("Entered into getUreterCancerTag method of TagGenerationUtility to Personal ureter cancer tag generation with list of ureterRenalPelvis(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Ureter or Renal Pelvis");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Ureter");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : ureterRenalPelvis) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY029_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_URETER_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY029") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY075_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmUreter<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmUreter<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY075") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getUreterCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get kidney cancer tags for personal and family history
     *
     * @param kidney     is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getKidneyCancerTag(List<QuestionSet> kidney, String cancerType) {
        //log.debug("Entered into getKidneyCancerTag method of TagGenerationUtility to Personal kidney cancer tag generation with list of kidney(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Kidney");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Kidney");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : kidney) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY028_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY028") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY074_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY074") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getKidneyCancerTag method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get biliary tract cancer tags for personal and family history
     *
     * @param biliaryTract is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getBiliaryTractCancerTags(List<QuestionSet> biliaryTract, String cancerType) {
        //log.debug("Entered into getBiliaryTractCancerTags method of TagGenerationUtility to Personal biliary tract cancer tag generation with list of biliaryTract(QuestionSet), cancerType: {}:", cancerType);
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Biliary Tract");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Biliary");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : biliaryTract) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY027_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_BILIARY_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY027") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY073_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmBiliary<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmBiliary<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY073") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getBiliaryTractCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }


    /**
     * Method to get small intestine cancer tags for personal and family history
     *
     * @param smallIntestineOrSmallBowel is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getSmallIntestineCancerTags(List<QuestionSet> smallIntestineOrSmallBowel, String cancerType) {
        //log.debug("Entered into getSmallIntestineCancerTags method of TagGenerationUtility to Personal small intestine cancer tag generation with list of smallIntestineOrSmallBowel(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Small Intestine (Small Bowel)");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "SmBowel");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : smallIntestineOrSmallBowel) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY026_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_SMALL_BOWEL_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY026") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY072_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmSmBowel<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmSmBowel<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY072") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getSmallIntestineCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get stomach cancer tags for personal and family history
     *
     * @param stomach    is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getStomachCancerTags(List<QuestionSet> stomach, String cancerType) {
        log.info("Entered into getStomachCancerTags method of TagGenerationUtility to Personal stomach cancer tag generation with list of stomach(QuestionSet)");
        Map<String, String> additionalDetails = new HashMap<>();
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType("Stomach");
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Gastric");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : stomach) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY025_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_GASTRIC_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY025") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY071_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add(NCCNTagConstants.FM_GASTRIC_LTE_50.getValue());
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmGastric<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY071") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY186_001")) {
                tagsSet.add(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue());
                additionalDetails.put("diffuseType", "Yes");
            } else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY186_002")) {
                additionalDetails.put("diffuseType", "No");
            } else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY186_003")) {
                additionalDetails.put("diffuseType", "Unknown");
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY187_001")) {
                tagsSet.add(NCCNTagConstants.FM_GASTRIC_DIFFUSE.getValue());
                additionalDetails.put("diffuseType", "Yes");
            } else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY187_002")) {
                additionalDetails.put("diffuseType", "No");
            } else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY187_003")) {
                additionalDetails.put("diffuseType", "Unknown");
            }

        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        log.info("Exit from getStomachCancerTags method of TagGenerationUtility");
        return cancerDetailsDTO;
    }

    /**
     * Method to get uterine cancer tags for personal and family history
     *
     * @param uterineOrEndometrial is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getUterineOrEndometrialCancerTags(List<QuestionSet> uterineOrEndometrial, String cancerType) {
        //log.debug("Entered into getUterineOrEndometrialCancerTags method of TagGenerationUtility to Personal uterine or endometrial cancer tag generation with list of uterineOrEndometrial(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType(TagConstants.UTERINE.getValue());
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Endometrial");
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : uterineOrEndometrial) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY024_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("PrEndometrial<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_ENDOMETRIAL_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY024") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY070_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmEndometrial<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmEndometrial<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY070") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);

        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getUterineOrEndometrialCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get pancreatic cancer tags for personal and family history
     *
     * @param pancreatic is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getPancreaticCancerTags(List<QuestionSet> pancreatic, String cancerType) {
        //log.debug("Entered into getPancreaticCancerTags method of TagGenerationUtility to Personal pancreatic cancer tag generation with list of pancreatic(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType(TagConstants.PANCREATIC.getValue());
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + TagConstants.PANCREATIC.getValue());
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : pancreatic) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY023_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_PANCREATIC_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY023") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY069_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmPancreatic<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmPancreatic<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY069") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getPancreaticCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get colorectal cancer tags for personal and family
     *
     * @param colonOrRectal is list of QuestionSet
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getColorectalCancerTags(List<QuestionSet> colonOrRectal, String cancerType) {
        //log.debug("Entered into getColorectalCancerTags method of TagGenerationUtility to Personal colon/rectal cancer tag generation with list of colonOrRectal(QuestionSet), cancerType: {}:", cancerType);

        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        Map<String, String> additionalDetails = new HashMap<>();
        cancerDetailsDTO.setCancerType(TagConstants.COLON.getValue());
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "Colon");
        tagsSet.addAll(getTagsForSelectedOptions(colonOrRectal));
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : colonOrRectal) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY020")) {
                additionalDetails.put("moreThanOnePrimary", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY022")) {
                List<String> answerKey = Stream.of(questionSet.getAnswerKey().split(",", -1)).collect(Collectors.toList());
                String answerText = answerKey.stream().map(x -> getSelectedOption(x)).collect(Collectors.joining(", "));
                additionalDetails.put("location", answerText);
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY067")) {
                additionalDetails.put("moreThanOnePrimary", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY021_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 49) {
                    tagsSet.add("PrColon<=49");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_COLON_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY021") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY068_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmColon<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmColon<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY068") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getColorectalCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get breast cancer tags for personal or family history
     *
     * @param breast     is list of QuestionSet
     * @param cancerType
     * @param sex
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getBreastCancerTags(List<QuestionSet> breast, String cancerType, String sex) {
        //log.debug("Entered into getBreastCancerTags method of TagGenerationUtility to Personal breast cancer tag generation with list of breast(QuestionSet), cancerType: {}:", cancerType);
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType(TagConstants.BREAST.getValue());
        Map<String, String> additionalDetails = new HashMap<>();
        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + TagConstants.BREAST.getValue());
        tagsSet.add(cancerType + TagConstants.BREAST.getValue() + sex);
        tagsSet.addAll(getTagsForSelectedOptions(breast));
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : breast) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY007")) {
                additionalDetails.put("moreThanOnePrimary", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY009")) {
                List<String> answerKey = Stream.of(questionSet.getAnswerKey().split(",", -1)).collect(Collectors.toList());
                String answerText = answerKey.stream().map(x -> getSelectedOption(x)).collect(Collectors.joining(", "));
                additionalDetails.put("breastCancerType", answerText);
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY010")) {
                additionalDetails.put("location", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY011")) {
                additionalDetails.put("tripleNegative", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY057")) {
                additionalDetails.put("moreThanOnePrimary", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY124")) {
                additionalDetails.put("bilateral", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY059")) {
                additionalDetails.put("tripleNegative", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY008_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 45) {
                    tagsSet.add("PrBreast<=45");
                }
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("PrBreast<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("PrBreast<50");
                }
                if (ageOfDiagnosis >= 51) {
                    tagsSet.add("PrBreast>=51");
                }
                if (ageOfDiagnosis >= 46 && ageOfDiagnosis <= 50) {
                    tagsSet.add("PrBreast46-50");
                }
                if (ageOfDiagnosis <= 60) {
                    tagsSet.add("PrBreast<=60");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY008") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY058_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmBreast<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmBreast<50");
                }
                if (ageOfDiagnosis >= 51) {
                    tagsSet.add("FmBreast>=51");
                    if (tagsSet.contains(TagConstants.AMBRY057_002.getValue())) {
                        tagsSet.add("FmBreastBilat>=51");
                    }
                }
                if (ageOfDiagnosis <= 45) {
                    tagsSet.add("FmBreast<=45");
                }
                if (ageOfDiagnosis >= 46 && ageOfDiagnosis <= 50) {
                    tagsSet.add("FmBreast46-50");
                }
                if (ageOfDiagnosis <= 60) {
                    tagsSet.add("FmBreast<=60");
                }
            } else if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY058_002")) {
                ageOfDiagnosis = 0;
                tagsSet.add("FmBreastUnknownAge");
                if (tagsSet.contains(TagConstants.AMBRY057_002.getValue())) {
                    tagsSet.add("FmBreastBilatUnknownAge");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY058") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getBreastCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get ovarian cancer tags for personal and family history
     *
     * @param ovarian
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getOvarianCancerTags(List<QuestionSet> ovarian, String cancerType) {
        //log.debug("Entered into getOvarianCancerTags method of TagGenerationUtility to Personal ovarian cancer tag generation with list of ovarian(QuestionSet), cancerType: {}:", cancerType);

        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + TagConstants.OVARIAN.getValue());
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType(TagConstants.OVARIAN.getValue());
        if (cancerType.equalsIgnoreCase("Pr")) {
            tagsSet.add(TagConstants.AMBRY014_001.getValue());
        } else if (cancerType.equalsIgnoreCase("Fm")) {
            tagsSet.add(TagConstants.AMBRY062_001.getValue());
        }

        tagsSet.addAll(getTagsForSelectedOptions(ovarian));
        Map<String, String> additionalDetails = new HashMap<>();
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : ovarian) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY014")) {
                additionalDetails.put("epithelialType", getSelectedOption(questionSet.getAnswerKey()));
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY014_002")) {
                    tagsSet.remove(TagConstants.AMBRY014_001.getValue());
                }
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY062")) {
                if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY062_002")) {
                    tagsSet.remove(TagConstants.AMBRY062_001.getValue());
                }
                additionalDetails.put("epithelialType", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY013_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis < 50) {
                    tagsSet.add(NCCNTagConstants.PR_OVARIAN_LT_50.getValue());
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY013") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY061_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
                if (ageOfDiagnosis <= 50) {
                    tagsSet.add("FmOvarian<=50");
                }
                if (ageOfDiagnosis < 50) {
                    tagsSet.add("FmOvarian<50");
                }
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY061") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getOvarianCancerTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get Fallopian Tube cancer tags for personal and family history
     *
     * @param fallopianTubeOrPeritoneal
     * @param cancerType
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getFallopianTubeTags(List<QuestionSet> fallopianTubeOrPeritoneal, String cancerType) {
        //log.debug("Entered into getOvarianCancerTags method of TagGenerationUtility to Personal fallopianTube Or Peritoneal cancer tag generation with list of fallopianTubeOrPeritoneal(QuestionSet), cancerType: {}:", cancerType);

        Set<String> tagsSet = new HashSet<>();
        tagsSet.add(cancerType + "FalloPeritoneal");
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType(TagConstants.FALLOPIAN.getValue());
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : fallopianTubeOrPeritoneal) {
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY015_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY015") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY063_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY063") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getFallopianTubeTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to get prostate cancer tags for personal and family history
     *
     * @param prostate             is list of QuestionSet
     * @param cancerType
     * @param considerGleasonScore
     * @param gleasonScoreValue
     * @return CancerDetailsDTO
     */
    private CancerDetailsDTO getProstateCancerTags(List<QuestionSet> prostate, String cancerType, boolean considerGleasonScore, Long gleasonScoreValue) {
        //log.debug("Entered into getOvarianCancerTags method of TagGenerationUtility to Personal prostate cancer tag generation with list of prostate(QuestionSet), cancerType: {}:", cancerType);
        Set<String> tagsSet = new HashSet<>();
        Map<String, String> additionalDetails = new HashMap<>();

        tagsSet.add(cancerType + TagConstants.PROSTATE.getValue());
        CancerDetailsDTO cancerDetailsDTO = new CancerDetailsDTO();
        cancerDetailsDTO.setCancerType(TagConstants.PROSTATE.getValue());
        Integer ageOfDiagnosis = 0;
        for (QuestionSet questionSet : prostate) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY017")) {
                GleasonScoreObject gleasonScoreObject = getGleasonScore(questionSet, considerGleasonScore, gleasonScoreValue);
                if (gleasonScoreObject != null) {
                    additionalDetails.put("gleasonScore", gleasonScoreObject.getGleasonScoreValue());
                    if (!StringUtils.isEmpty(gleasonScoreObject.getAnswerKey())) {
                        questionSet.setAnswerKey(gleasonScoreObject.getAnswerKey());
                    }
                } else {
                    additionalDetails.put("gleasonScore", getSelectedOption(questionSet.getAnswerKey()));
                }
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY018")) {
                additionalDetails.put("metastatic", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY065")) {
                additionalDetails.put("gleasonScore", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY066")) {
                additionalDetails.put("metastatic", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY019")) {
                additionalDetails.put("intraductal", getSelectedOption(questionSet.getAnswerKey()));
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY016_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY016") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
            if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY064_001") && !StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = Integer.valueOf(questionSet.getAnswerText());
            } else if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY064") && StringUtils.isEmpty(questionSet.getAnswerText())) {
                ageOfDiagnosis = null;
            }
        }
        tagsSet.addAll(getTagsForSelectedOptions(prostate));
        cancerDetailsDTO.setAdditionalInformation(additionalDetails);
        cancerDetailsDTO.setAgeOnSet(ageOfDiagnosis);
        cancerDetailsDTO.setTags(tagsSet);
        //log.debug("Exit from getFallopianTubeTags method of TagGenerationUtility with cancerDetailsDTO: {}", cancerDetailsDTO);
        return cancerDetailsDTO;
    }

    /**
     * Method to check the polyps history
     *
     * @param basicQuestions
     * @return
     */
    public ClinicalSummary checkFamilyColorectalPolypsHistory(List<QuestionSet> basicQuestions, ClinicalSummary clinicalSummary) {
        log.info("Entered into checkColorectalPolypsHistory method of TagGenerationUtility with basicQuestions(QuestionSet)");
        for (QuestionSet questionSet : basicQuestions) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY111")) {
                clinicalSummary.setFamilyHistoryOfColorectalPolyps(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY114") && !StringUtils.isEmpty(questionSet.getAnswerKey())) {
                List<String> answerKey = Stream.of(questionSet.getAnswerKey().split(",", -1)).collect(Collectors.toList());
                List<String> answerText = answerKey.stream().map(x -> getSelectedOption(x)).collect(Collectors.toList());
                clinicalSummary.setRelativeWithPolypsHistory(answerText);
            }
        }
        log.info("Exit from checkFamilyColorectalPolypsHistory method of TagGenerationUtility with historyOfPolyps");
        return clinicalSummary;
    }

    /**
     * Method to get the personal polyps history
     *
     * @param basicQuestions
     * @return
     */
    public PersonalHistoryTagsDTO checkPersonalColorectalPolypsHistory
    (List<QuestionSet> basicQuestions, PersonalHistoryTagsDTO personalHistoryTagsDTO, boolean isOldVersion) {
        //log.info("Entered into checkColorectalPolypsHistory method of TagGenerationUtility with basicQuestions(QuestionSet)");
        PersonalHistoryDetails personalHistoryDetails = personalHistoryTagsDTO.getPersonalHistoryDetails();
        Set<String> tagSet = personalHistoryTagsDTO.getTags();
        tagSet.addAll(getTagsForSelectedOptions(basicQuestions));
        personalHistoryTagsDTO.setTags(tagSet);
        for (QuestionSet questionSet : basicQuestions) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY051")) {
                personalHistoryDetails.setNewColorectalPolypHx(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY125")) {
                personalHistoryDetails.setColonoscopy(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY116")) {
                personalHistoryDetails.setIsPolypAdenomas(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY117")) {
                if (isOldVersion) {
                    personalHistoryDetails.setCountOfPolyps(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase() + "_OLD").getValue());
                } else {
                    personalHistoryDetails.setCountOfPolyps(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
                }
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY118")) {
                personalHistoryDetails.setIsDesmoidTumor(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY119")) {
                personalHistoryDetails.setHadGeneticTesting(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY120")) {
                personalHistoryDetails.setResultOfGeneticTesting(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY126")) {
                if (questionSet.getAnswerKey().equalsIgnoreCase("AMBRY126_002")) {
                    personalHistoryDetails.setYearOfGeneticTesting(OptionConstants.valueOf(questionSet.getAnswerKey().toUpperCase()).getValue());
                } else {
                    personalHistoryDetails.setYearOfGeneticTesting(questionSet.getAnswerText());
                }
            }
        }
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        //log.info("Exit from checkColorectalPolypsHistory method of TagGenerationUtility with historyOfPolyps");
        return personalHistoryTagsDTO;
    }


    /**
     * Generate the PersonalHistoryTagDto with given inputs
     *
     * @param questionnaire
     * @param personalHistoryDetails
     * @param isPersonalHistory
     * @param considerGleasonScore
     * @param gleasonScoreValue
     * @return PersonalHistoryTagsDTO
     */
    public PersonalHistoryTagsDTO getPersonalHistoryDetails(Questionnaire questionnaire, PersonalHistoryDetails
            personalHistoryDetails, boolean isPersonalHistory, boolean considerGleasonScore, Long gleasonScoreValue, String assessmentVersion, boolean isOldVersion, boolean useNewEthnicityMapping) {
        //log.info("Entered into getPersonalHistoryDetails method of TagGenerationUtility with questionnaire, personalHistoryDetails, isPersonalHistory: {}, considerGleasonScore: {}, gleasonScoreValue: {}", isPersonalHistory, considerGleasonScore, gleasonScoreValue);
        PersonalHistoryTagsDTO personalHistoryTagsDTO = new PersonalHistoryTagsDTO();
        Set<String> tagsSet = new HashSet<>();
        if (questionnaire.getDemographic() != null) {
            List<String> ethnicityList = getEthnicityList(questionnaire.getDemographic(), isOldVersion, useNewEthnicityMapping);
            personalHistoryDetails.setEthnicity(ethnicityList);
            ethnicityUtil.applyAshkenaziTagAndSide(personalHistoryDetails, tagsSet, ethnicityList, questionnaire);
            tagsSet.addAll(getEthnicityTags(ethnicityList));
            tagsSet.addAll(getTagsForSelectedOptions(questionnaire.getDemographic()));
        }
        if (!CollectionUtils.isEmpty(questionnaire.getTyrerCuzick()))
            setBiopsyDetails(personalHistoryDetails, questionnaire.getTyrerCuzick());

        if (questionnaire.getPersonalHistory() != null) {
            PersonalHistory personalHistory = questionnaire.getPersonalHistory();
            List<CancerDetailsObject> cancerDetailsList = new ArrayList<>();
            List<CancerDetailsDTO> personalCancerDetailsDTOList = new ArrayList<>();
            if (isPersonalHistory) {
                CancerDetails personalCancerDetails = personalHistory.getPersonalCancerDetails();
                personalCancerDetailsDTOList = getPersonalCancerTags(personalCancerDetails, considerGleasonScore, gleasonScoreValue);
                personalCancerDetailsDTOList.forEach(cancerDetailsDTO -> {
                    CancerDetailsObject cancerDetails = new CancerDetailsObject();
                    cancerDetails.setCancer(cancerDetailsDTO.getCancerType());

                    if (cancerDetailsDTO.getAgeOnSet() == null) {
                        cancerDetails.setAge("-");
                    } else if (cancerDetailsDTO.getAgeOnSet() == 0) {
                        cancerDetails.setAge("Unknown");
                    } else {
                        cancerDetails.setAge(cancerDetailsDTO.getAgeOnSet().toString());
                    }
                    if (cancerDetailsDTO.getAdditionalInformation() != null && !cancerDetailsDTO.getAdditionalInformation().isEmpty()) {
                        cancerDetails.setAdditionalInformation(cancerDetailsDTO.getAdditionalInformation());
                    }
                    cancerDetailsList.add(cancerDetails);
                });
            }
            personalHistoryDetails.setCancerDetails(cancerDetailsList);
            tagsSet.addAll(personalCancerDetailsDTOList.stream().map(CancerDetailsDTO::getTags).flatMap(Collection::stream).collect(Collectors.toSet()));
        }
        if (questionnaire.getFamilyCancer() != null) {
            personalHistoryDetails.setAdopted(checkIfAdopted(questionnaire.getFamilyCancer().getBasicQuestions()));
        }
        if (isPersonalHistory) {
            personalHistoryDetails.setCancerHx("Yes");
        } else {
            personalHistoryDetails.setCancerHx("No");
        }
        personalHistoryTagsDTO.setPersonalHistoryDetails(personalHistoryDetails);
        personalHistoryTagsDTO.setTags(tagsSet);
        if (questionnaire.getPersonalHistory() != null) {
            personalHistoryTagsDTO = checkPersonalColorectalPolypsHistory(questionnaire.getPersonalHistory().getBasicQuestions(), personalHistoryTagsDTO, isOldVersion);
        }
        //log.info("Exit into getPersonalHistoryDetails method of TagGenerationUtility with personalHistoryTagsDTO");
        return personalHistoryTagsDTO;
    }

    private void setBiopsyDetails(PersonalHistoryDetails personalHistoryDetails, List<QuestionSet> questionList) {
        Map<String, String> filteredQuestion = questionList
                .stream()
                .filter(question -> PRIOR_BIOPSY_QUESTION.equals(question.getQuestionKey()) || BIOPSY_RESULT.equals(question.getQuestionKey()))
                .collect(Collectors.toMap(QuestionSet::getQuestionKey, QuestionSet::getAnswerKey));

        if (filteredQuestion.containsKey(PRIOR_BIOPSY_QUESTION)) {
            if(OptionConstants.valueOf(filteredQuestion.get(PRIOR_BIOPSY_QUESTION)).getValue().equalsIgnoreCase(I_DONT_KNOW)) {
                personalHistoryDetails.setPreviousBiopsy(UNKNOWN);
            } else {
                personalHistoryDetails.setPreviousBiopsy(OptionConstants.valueOf(filteredQuestion.get(PRIOR_BIOPSY_QUESTION)).getValue());
            }

            if (filteredQuestion.containsKey(BIOPSY_RESULT)) {
                // if AMBRY180 (prior breast biopsy) question is present
                // we will display breast biopsy result (new question) on CS and do not show abnormal biopsy hx (old question)
                // we need to nullify the abnormal biopsy hx (old question) to hide it on CS
                if (OptionConstants.valueOf(filteredQuestion.get(BIOPSY_RESULT)).getValue().equalsIgnoreCase(I_DONT_KNOW)) {
                    personalHistoryDetails.setBreastBiopsyResult(UNKNOWN);
                } else {
                    personalHistoryDetails.setBreastBiopsyResult(OptionConstants.valueOf(filteredQuestion.get(BIOPSY_RESULT)).getValue());
                }
                personalHistoryDetails.setAbnormalBreastBiopsyHx(null);
            }
        }

    }
    /**
     * Method to get check if patient is adopted or not
     *
     * @param basicQuestions
     * @return adopted
     */
    private String checkIfAdopted(List<QuestionSet> basicQuestions) {
        //log.debug("Entered into checkIfAdopted method of TagGenerationUtility with basicQuestions(QuestionSet)");
        String adopted = "Not Adopted";
        for (QuestionSet questionSet : basicQuestions) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY052") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY052_003")) {
                adopted = "Unknown";
            }
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY052") && questionSet.getAnswerKey().equalsIgnoreCase("AMBRY052_004")) {
                adopted = "Adopted";
            }
        }
        //log.debug("Exit from checkIfAdopted method of TagGenerationUtility with adopted: {}", adopted);
        return adopted;
    }

    /**
     * Method to get ethnicity values selected by the user
     *
     * @param demographic
     * @return list of ethnicity selected
     */
    private List<String> getEthnicityList(List<QuestionSet> demographic, boolean isOldVersion, boolean useNewEthnicityMapping) {
        //log.debug("Entered into getEthnicityList method of TagGenerationUtility with demographic(QuestionSet)");
        List<String> ethnicityList = new ArrayList<>();
        for (QuestionSet questionSet : demographic) {
            if (questionSet.getQuestionKey().equalsIgnoreCase("AMBRY003")) {
                if (!StringUtils.isEmpty(questionSet.getAnswerKey())) {
                    List<String> answerKeyList = Stream.of(questionSet.getAnswerKey().split(",", -1))
                            .collect(Collectors.toList());
                    if (isOldVersion) {
                        for (String answerKey : answerKeyList) {
                            ethnicityList.add(getSelectedOption(answerKey+"_OLD"));
                        }
                    } else {
                        ethnicityList = ethnicityUtil.getEthnicityListWithSubcategory(answerKeyList, null, DocumentType.CLINICAL_SUMMARY, useNewEthnicityMapping);
                    }
                }
            }
        }
        //log.debug("Exit from checkIfAdopted method of TagGenerationUtility with ethnicityList");
        return ethnicityList;
    }

    /**
     * Method to get tags for the selected ethnicity values
     *
     * @param ethnicity
     * @return list of tags for ethnicity
     */
    private List<String> getEthnicityTags(List<String> ethnicity) {
        //log.debug("Entered into getEthnicityTags method of TagGenerationUtility with ethnicity: {}", ethnicity);
        List<String> tagsSet = new ArrayList<>();
        ethnicity.forEach(x -> {
            try {
                if (TagConstants.valueOf(x) != null) {

                    tagsSet.add(TagConstants.valueOf(x).getValue());
                }
            } catch (Exception e) {
                log.error("No constant for value: {} ", x);
            }
        });
        tagsSet.removeAll(Arrays.asList("", null));
        //log.debug("Exit into getEthnicityTags method of TagGenerationUtility with tagsSet: {}", tagsSet);
        return tagsSet;
    }

    /**
     * Method to get the LYNCH guideline and corresponding reasoning statements when the same relative satisfied certain conditions
     *
     * @param tags
     * @return lynchReasoningStatements
     */
    private Set<String> checkForSameRelativeLynchGuidelineDetails(Set<String> tags) {
        Set<String> lynchReasoningStatements = new HashSet<>();

        if (!CollectionUtils.isEmpty(tags)) {
            List<String> lynchSameRelativeTagsForMatA = new ArrayList<>();
            lynchSameRelativeTagsForMatA.add("FmColonMatFDR");
            lynchSameRelativeTagsForMatA.add("FmEndometrialMatFDR");

            List<String> lynchSameRelativeTagsForMatB = new ArrayList<>();
            lynchSameRelativeTagsForMatB.add("FmBiliaryMatFDR");
            lynchSameRelativeTagsForMatB.add("FmBrainMatFDR");
            lynchSameRelativeTagsForMatB.add("FmGastricMatFDR");
            lynchSameRelativeTagsForMatB.add("FmOvarianMatFDR");
            lynchSameRelativeTagsForMatB.add("FmPancreaticMatFDR");
            lynchSameRelativeTagsForMatB.add("FmSebAdenoCarMatFDR");
            lynchSameRelativeTagsForMatB.add("FmSmBowelMatFDR");
            lynchSameRelativeTagsForMatB.add("FmUreterMatFDR");
            lynchSameRelativeTagsForMatB.add("FmBladderMatFDR");

            if (tags.contains("FmColon>=2DxMatFDR") || (!Collections.disjoint(tags, lynchSameRelativeTagsForMatA) && !Collections.disjoint(tags, lynchSameRelativeTagsForMatB)) || tags.containsAll(lynchSameRelativeTagsForMatA)) {
                lynchReasoningStatements.add(ReasoningStatementConstants.CRCRS9.getValue());
            }


            List<String> lynchSameRelativeTagsForPatA = new ArrayList<>();
            lynchSameRelativeTagsForPatA.add("FmColonPatFDR");
            lynchSameRelativeTagsForPatA.add("FmEndometrialPatFDR");

            List<String> lynchSameRelativeTagsForPatB = new ArrayList<>();
            lynchSameRelativeTagsForPatB.add("FmBiliaryPatFDR");
            lynchSameRelativeTagsForPatB.add("FmBrainPatFDR");
            lynchSameRelativeTagsForPatB.add("FmGastricPatFDR");
            lynchSameRelativeTagsForPatB.add("FmOvarianPatFDR");
            lynchSameRelativeTagsForPatB.add("FmPancreaticPatFDR");
            lynchSameRelativeTagsForPatB.add("FmSebAdenoCarPatFDR");
            lynchSameRelativeTagsForPatB.add("FmSmBowelPatFDR");
            lynchSameRelativeTagsForPatB.add("FmUreterPatFDR");
            lynchSameRelativeTagsForPatB.add("FmBladderPatFDR");

            if (tags.contains("FmColon>=2DxPatFDR") || (!Collections.disjoint(tags, lynchSameRelativeTagsForPatA) && !Collections.disjoint(tags, lynchSameRelativeTagsForPatB)) || tags.containsAll(lynchSameRelativeTagsForPatA)) {
                lynchReasoningStatements.add(ReasoningStatementConstants.CRCRS9.getValue());
            }
        }
        return lynchReasoningStatements;
    }

    /**
     * Method to get the HBOC guideline and corresponding reasoning statements when the same relative satisfied certain conditions
     *
     * @param tags
     * @return hbocReasoningStatements
     */
    private Set<String> checkForSameRelativeHbocGuidelineDetails(Set<String> tags) {
        //log.debug("Entered into checkForSameRelativeHbocGuidelineDetails method of TagGenerationUtility with tags: {}", tags);
        Set<String> hbocReasoningStatements = new HashSet<>();

        //For HBOCTag33
        List<String> hbocSameRelativeTagsMatTag33 = new ArrayList<>();
        hbocSameRelativeTagsMatTag33.add("FmBreastMatFDR");
        hbocSameRelativeTagsMatTag33.add("FmBreastBilatMatFDR");
        hbocSameRelativeTagsMatTag33.add("FmBreastBilatMatSDR");
        hbocSameRelativeTagsMatTag33.add("FmBreastMatSDR");
        List<String> commonHbocTagsMatTag33 = new ArrayList<>(hbocSameRelativeTagsMatTag33);
        commonHbocTagsMatTag33.retainAll(tags);
        if (commonHbocTagsMatTag33.size() >= 2) {
            hbocReasoningStatements.add(ReasoningStatementConstants.HBOCRS27.getValue());
        }

        List<String> hbocSameRelativeTagsPatTag33 = new ArrayList<>();
        hbocSameRelativeTagsPatTag33.add("FmBreastPatFDR");
        hbocSameRelativeTagsPatTag33.add("FmBreastBilatPatFDR");
        hbocSameRelativeTagsPatTag33.add("FmBreastBilatPatSDR");
        hbocSameRelativeTagsPatTag33.add("FmBreastPatSDR");
        List<String> commonHbocTagsPatTag33 = new ArrayList<>(hbocSameRelativeTagsPatTag33);
        commonHbocTagsPatTag33.retainAll(tags);
        if (commonHbocTagsPatTag33.size() >= 2) {
            hbocReasoningStatements.add(ReasoningStatementConstants.HBOCRS27.getValue());
        }


        //log.debug("Exit into checkForSameRelativeHbocGuidelineDetails method of TagGenerationUtility with hbocReasoningStatements: {}", hbocReasoningStatements);
        return hbocReasoningStatements;
    }

    private GleasonScoreObject getGleasonScore(QuestionSet questionSet, boolean considerGleasonScore, Long
            gleasonScoreValue) {
        //log.info("Getting the gleason score value for the version: {}", assessmentVersion);
        GleasonScoreObject gleasonScoreObject = new GleasonScoreObject();
        Integer version = null;
        if (considerGleasonScore) {
            //log.info("Account level configuration for consider gleason score: true");
            //log.info("Gleason score value stored in assessment table:{}", gleasonScoreValue);
            if (gleasonScoreValue != null && gleasonScoreValue > 7) {
                gleasonScoreObject.setGleasonScoreValue(OptionConstants.AMBRY017_002.getValue());
                gleasonScoreObject.setAnswerKey("AMBRY017_002");
            } else if (gleasonScoreValue != null && gleasonScoreValue <= 7) {
                gleasonScoreObject.setGleasonScoreValue(OptionConstants.AMBRY017_001.getValue());
                gleasonScoreObject.setAnswerKey("AMBRY017_001");
            } else {
                //log.info("Gleason score value is null");
                gleasonScoreObject.setGleasonScoreValue(getSelectedOption(questionSet.getAnswerKey()));
            }
        } else {
            //log.info("Account level configuration for consider gleason score: false");
            gleasonScoreObject.setGleasonScoreValue(getSelectedOption(questionSet.getAnswerKey()));

        }
        return gleasonScoreObject;
    }
}
