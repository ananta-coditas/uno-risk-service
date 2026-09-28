package com.genomebrain.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ambry.transformer.nccn.NCCNValidator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.genomebrain.backend.service.dto.CustomResponseDTO;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.util.CareAPIUtil;
import com.genomebrain.backend.service.util.MessageConstants;
import com.genomebrain.backend.web.rest.util.StatusCode;

@Service
public class NCCNExecuter{

    @Autowired
    CareAPIUtil careAPIUtil;

    @Autowired
    NCCNValidator nccnValidator;

    private final Logger log = LoggerFactory.getLogger(NCCNExecuter.class);

    private final String VERSION = "V1";

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
        return careapiResponseDTO;
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

    /**
     * Validates the clinical histories of the request; the checks themselves live in {@link NCCNValidator}.
     *
     * @param nonCareQuestionnaireDTO the request to validate
     * @return a success response, or a failure response carrying the first validation message
     */
    public CustomResponseDTO getDataValidation(NCCNCoreRequest nonCareQuestionnaireDTO) {
        List<String> violations = nccnValidator.findViolations(nonCareQuestionnaireDTO);
        if (!violations.isEmpty()) {
            return new CustomResponseDTO(MessageConstants.FAILURE, StatusCode.FAILURE.getstatusCode(), violations.get(0), null);
        }
        return new CustomResponseDTO(MessageConstants.SUCCESS, StatusCode.SUCCESS.getstatusCode(), "", null);
    }
}
