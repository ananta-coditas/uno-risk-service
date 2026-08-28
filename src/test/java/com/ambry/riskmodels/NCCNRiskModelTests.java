package com.ambry.riskmodels;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import com.ambry.IntegrationTest;

@IntegrationTest
@AutoConfigureMockMvc
@ActiveProfiles("testdev") 
public class NCCNRiskModelTests {
    
    @Autowired
    private MockMvc mockMvc;
    
   // @Test
    @DisplayName("CRCTagOne\" Case1 \"Consider a male patient with: Personal History: Colorectal polyps - (Yes) Adenomas-(Yes) Total colorectal polyps - (10-19) Family History: No Family history of any cancers")
    void testCRCCRIT1Case1() throws Exception {
        String payLoad = readPayloadData("classpath:test-input-data/nccn/CRCCRIT1_case1.json");
        mockMvc.perform(post("/api/risk/models/calculate").content(payLoad).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results.NCCN.nccnCriteriaMet").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.apsApcOverall").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.apsApcCriteriaMetIds[0]").value("CRCRS1"));
    }
    
   // @Test
    @DisplayName("CRCTagOne\" Case1 \"Consider a female patient with: Personal History: Colorectal polyps - (Yes) Adenomas-(Yes) Total colorectal polyps - (10-19), Family History: No Family history of any cancers, Result: NCCN Met with CRCRS1")
    void testCRCCRIT1Case2() throws Exception {
        String payLoad = readPayloadData("classpath:test-input-data/nccn/CRCCRIT1_case2.json");
        mockMvc.perform(post("/api/risk/models/calculate").content(payLoad).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results.NCCN.nccnCriteriaMet").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.apsApcOverall").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.apsApcCriteriaMetIds[0]").value("CRCRS1"));
    }
   
    //@Test
    @DisplayName("CRCCRIT2 CRCTag2 Case1 \"Consider a male patient with: Personal History: No Personal history of any cancers, Family History: Mother with 10 or more colorectal polyps\", result: NCCN not met")
    void testCRCCRIT2Case1() throws Exception {
        String payLoad = readPayloadData("classpath:test-input-data/nccn/CRCCRIT2_case1.json");
        mockMvc.perform(post("/api/risk/models/calculate").content(payLoad).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results.NCCN.nccnCriteriaMet").value(Boolean.FALSE));
    }
    
    //@Test
    @DisplayName("CRCCRIT3 \"CRCTagThree\" Case1 Consider a male patient with age = 47 Personal History: Colon or Rectal cancer Family History: No Family history of any cancers, Result: NCCN met with CRCRS3")
    void testCRCCRIT3Case1() throws Exception {
        String payLoad = readPayloadData("classpath:test-input-data/nccn/CRCCRIT3_case1.json");
        mockMvc.perform(post("/api/risk/models/calculate").content(payLoad).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results.NCCN.nccnCriteriaMet").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.lynchCriteriaMetIds[0]").value("CRCRS5"));
    }
    
    //@Test
    @DisplayName("HBOCCRIT30 HBOCTag95 Case1 Consider a female patient with: Personal History: No cancer Family History: Mother breast (55y) Brother breast (55y)\"\r\n"
            + "")
    void testHBOCCRIT39Case1() throws Exception {
        String payLoad = readPayloadData("classpath:test-input-data/nccn/HBOCCCRIT30_case1.json");
        mockMvc.perform(post("/api/risk/models/calculate").content(payLoad).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results.NCCN.nccnCriteriaMet").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.hbocOverall").value(Boolean.TRUE))
        .andExpect(jsonPath("$.results.NCCN.hbocCriteriaMetIds").value(hasItem("HBOCRS30")));
    }
    
    private String readPayloadData(String filePath) throws IOException {
        java.io.File file = ResourceUtils.getFile(filePath);
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
    }

}
