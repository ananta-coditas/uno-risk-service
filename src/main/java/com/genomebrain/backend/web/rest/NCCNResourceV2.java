package com.genomebrain.backend.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.genomebrain.backend.service.NCCNExecuter;
import com.genomebrain.backend.service.dto.CustomResponseDTO;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;

@RestController
//@RequestMapping("/api/v2/")
public class NCCNResourceV2 {

    private final Logger log = LoggerFactory.getLogger(NCCNResourceV2.class);

    @Autowired
    NCCNExecuter nonCareQuestionnaire;

    private final String SUCCESS = "SUCCESS";

    //@PostMapping("evaluateNCCN")
    public CustomResponseDTO evaluateNCCN(@RequestBody NCCNCoreRequest nonCareQuestionnaireDTO  ) {
        log.info("evaluateNCCN");
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        customResponseDTO= nonCareQuestionnaire.getNonCareQuestionnaireDTO(nonCareQuestionnaireDTO);
        return customResponseDTO;
    }
}
