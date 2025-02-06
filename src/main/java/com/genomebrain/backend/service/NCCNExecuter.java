package com.genomebrain.backend.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.CustomResponseDTO;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.util.APIConstants;
import com.genomebrain.backend.service.util.CareAPIUtil;
import com.genomebrain.backend.service.util.MessageConstants;
import com.genomebrain.backend.web.rest.util.StatusCode;

@Service
public class NCCNExecuter{

    @Autowired
    CareAPIUtil careAPIUtil;

    private final Logger log = LoggerFactory.getLogger(NCCNExecuter.class);

    private final String VERSION = "V1";

    List <String> relationShip = Arrays.asList("PROBAND", "MOTHER", "FATHER", "SISTER", "BROTHER", "DAUGHTER", "SON", "NIECE", "NEPHEW", "HALF SISTER", "HALF BROTHER", "GRANDMOTHER", "GRANDFATHER", "GRANDDAUGHTER", "GRANDSON", "AUNT", "UNCLE", "COUSIN - 1ST", "GR GRANDMOTHER", "GR GRANDFATHER", "G AUNT", "G UNCLE", "G NIECE", "G NEPHEW", "G GRANDDAUGHTER", "GR GRANDSON", "HALF AUNT", "HALF UNCLE", "HALF NIECE", "HALF NEPHEW", "OTHER");
    List <String> sideOfFamily = Arrays.asList("PROBAND", "MATERNAL", "PATERNAL", "OTHER");
    List <String> sexATBirth = Arrays.asList("MALE", "FEMALE", "UNKNOWN");
    List <String> inputOptions = Arrays.asList("YES", "NO", "NOT PROVIDED");
    public final  String cousin ="COUSIN - 1ST";
    public final  String PROBAND ="PROBAND";

    /**
     * Method to parse given request json to Non CARE Questionnaire DTO
     *
     * @param nonCareQuestionnaireDTO
     * @return
     */
    public NCCNCoreResponse invokeNCCNRiskModel(NCCNCoreRequest nonCareQuestionnaireDTO) {
        log.info("Entered NonCareQuestionnaireImpl in getNonCareQuestionnaireDTO : request to get NCCN criteria ");

        NCCNCoreResponse careapiResponseDTO = new NCCNCoreResponse();
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        if (!ObjectUtils.isEmpty(nonCareQuestionnaireDTO)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            customResponseDTO = getDataValidation(nonCareQuestionnaireDTO);
            if (!customResponseDTO.getStatus().equalsIgnoreCase(MessageConstants.FAILURE)) {
                try {
                    careapiResponseDTO = careAPIUtil.getQuestionObject(nonCareQuestionnaireDTO);
                    customResponseDTO.setData(careapiResponseDTO);
                } catch (Exception e) {
                    log.error("Exception in getNonCareQuestionnaireDTO method of NonCareQuestionnaireImpl with cause: {}", e);
                }
            }
        }
        log.info("Exit NonCareQuestionnaireImpl from getNonCareQuestionnaireDTO : request to get NCCN criteria ");
        return null;
    }
    
    /**
     * Method to parse given request json to Non CARE Questionnaire DTO
     *
     * @param nonCareQuestionnaireDTO
     * @return
     */
    public CustomResponseDTO getNonCareQuestionnaireDTO(NCCNCoreRequest nonCareQuestionnaireDTO) {
        log.info("Entered NonCareQuestionnaireImpl in getNonCareQuestionnaireDTO : request to get NCCN criteria ");

        NCCNCoreResponse careapiResponseDTO = new NCCNCoreResponse();
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        if (!ObjectUtils.isEmpty(nonCareQuestionnaireDTO)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            customResponseDTO = getDataValidation(nonCareQuestionnaireDTO);
            if (!customResponseDTO.getStatus().equalsIgnoreCase(MessageConstants.FAILURE)) {
                try {
                    careapiResponseDTO = careAPIUtil.getQuestionObject(nonCareQuestionnaireDTO);
                    customResponseDTO.setData(careapiResponseDTO);
                } catch (Exception e) {
                    log.error("Exception in getNonCareQuestionnaireDTO method of NonCareQuestionnaireImpl with cause: {}", e);
                    return new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), "Request failed", null);
                }
            }
        }
        log.info("Exit NonCareQuestionnaireImpl from getNonCareQuestionnaireDTO : request to get NCCN criteria ");
        return customResponseDTO;
    }

    public CareAPIUtil getCareAPIUtil() {
        return careAPIUtil;
    }

    public List<String> getRelationShip() {
        return relationShip;
    }

    public List<String> getSideOfFamily() {
        return sideOfFamily;
    }

    public List<String> getSexATBirth() {
        return sexATBirth;
    }

    public String getPROBAND() {
        return PROBAND;
    }

    public CustomResponseDTO getDataValidation(NCCNCoreRequest nonCareQuestionnaireDTO) {
        log.info("Entered NonCareQuestionnaireImpl in getDataValidation : validate the data for care api nccn and tc score generation ");

        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        customResponseDTO = new CustomResponseDTO(MessageConstants.SUCCESS, StatusCode.SUCCESS.getstatusCode(), "", null);

        List<ClinicalHistory> clinicalHistories = nonCareQuestionnaireDTO.getClinicalHistory();
        for (ClinicalHistory clinicalHistory : clinicalHistories) {
            if (clinicalHistory.getRelationship() == null || clinicalHistory.getRelationship().isEmpty() || !relationShip.contains(clinicalHistory.getRelationship().toUpperCase()) ) {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_RELATIONSHIP, null);
            }
            if(clinicalHistory.getRelationship().equalsIgnoreCase(cousin) && (clinicalHistory.getSexAtBirth()==null || StringUtils.isEmpty(clinicalHistory.getSexAtBirth()) || !sexATBirth.contains(clinicalHistory.getSexAtBirth().toUpperCase())))
            {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST, null);
            }
            if (clinicalHistory.getSideOfFam() == null || clinicalHistory.getSideOfFam().isEmpty() || !sideOfFamily.contains(clinicalHistory.getSideOfFam().toUpperCase()) ) {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_SIDE_OF_FAM, null);
            }
            if (!(clinicalHistory.getSexAtBirth() == null) && !clinicalHistory.getSexAtBirth().isEmpty() && !sexATBirth.contains(clinicalHistory.getSexAtBirth().toUpperCase()) ) {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_SEX_AT_BIRTH, null);
            }
            if (clinicalHistory.getClinicalFeatureOverall() == null || clinicalHistory.getClinicalFeatureOverall().isEmpty() || !inputOptions.contains(clinicalHistory.getClinicalFeatureOverall().toUpperCase())) {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_CLINICAL_FEATURE_OVERALL, null);
            }
            if (clinicalHistory.getCancerOverall() == null || clinicalHistory.getCancerOverall().isEmpty() || !inputOptions.contains(clinicalHistory.getCancerOverall().toUpperCase())) {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_CANCER_OVERALL, null);
            }
            if (clinicalHistory.getRelationship().equalsIgnoreCase(PROBAND) && (!(clinicalHistory.getPolypOverall() == null) && !clinicalHistory.getPolypOverall().isEmpty() && !inputOptions.contains(clinicalHistory.getPolypOverall().toUpperCase()))) {
                return customResponseDTO = new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), APIConstants.VALID_POLYP_OVERALL_PROBAND, null);
            }
        }
        log.info("Entered NonCareQuestionnaireImpl in getDataValidation : validate the data for care api nccn and tc score generation ");
        return customResponseDTO;
    }
}
