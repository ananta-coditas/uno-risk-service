package com.genomebrain.backend.web.rest;

import com.genomebrain.backend.service.GuideLineService;
import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

/**
 * REST controller for managing guideline requests.
 */
@RestController
//@RequestMapping("/api")
public class GuidelineResource {

    @Autowired
    GuideLineService guideLineService;

    private final Logger log = LoggerFactory.getLogger(GuidelineResource.class);

    /**
     * To get recommended guidelines
     */
    //@PostMapping("/get-recommended-guidelines")
    public ResponseEntity<GenomeBrainResponseDTO> getRecommendedTests(HttpServletRequest request, @RequestBody String tags) {
        log.debug("REST request to get recommendedTests for corresponding user details");
        HttpSession session = request.getSession();
        boolean authorized = false;
        if (session != null) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.contains("APIKey")) {
                log.info("Authenticating request");
                String apikey = authorization.substring("APIKey".length() + 1);
                if ("GenomeBrain@123".equalsIgnoreCase(apikey)) {
                    authorized = true;
                }
            }
        }
        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        if (authorized) {
            genomeBrainResponseDTO = guideLineService.getGuidelinesForTags(tags);
        } else {
            log.error("Unauthorized access");
            genomeBrainResponseDTO.setStatus("Failure");
            genomeBrainResponseDTO.setMessage("Unauthorized access");
        }
        return ResponseEntity.ok().body(genomeBrainResponseDTO);
    }
}

