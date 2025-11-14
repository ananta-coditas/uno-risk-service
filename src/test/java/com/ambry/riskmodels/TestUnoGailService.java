package com.ambry.riskmodels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.gail.Person;
import com.objex.risk.beans.gail.Risk;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ambry.IntegrationTest;

/**
 * GAIL Risk Model Service Tests
 * The example family in this project does not match the one in FHQ due to the age difference
 * of 90 to use the tool
 */
@IntegrationTest
@AutoConfigureMockMvc
@ActiveProfiles("testdev")
@DisplayName("GAIL Risk Model Service Tests")
class TestUnoGailService {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Risk baseFamily;

    //@Test
    @DisplayName("Test combined base family risk calculation for GAIL model")
    void testCombinedBaseFamily() throws Exception {
        String predBr = "0.003";
        String lastpredBr = "0.11340920";

        com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        fam.setAgeTo(90);
        com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
        proband.setAge(35);
        proband.setMenarcheAge(13);
        proband.setRace(Person.Race.White);
        fam.getFamily().add(proband);
        fam.setSaveIncoming(true);

        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(90);
        model.setName(ModelName.GAIL);
        model.setChartCode(1);
        fam.getRequestedModels().add(model);

        MvcResult result = mockMvc.perform(post("/api/risk/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fam))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CombinedResult combresult = objectMapper.readValue(result.getResponse().getContentAsString(), CombinedResult.class);
        GailResult gailResult = (GailResult) combresult.getResults().get(ModelName.GAIL);

        assertNotNull(gailResult);
        assertNotNull(gailResult.getPredictions());
        assertFalse(gailResult.getPredictions().isEmpty());
        assertEquals(predBr, fmt(gailResult.getPredictions().get(0).getResult("breastRisk")));
        assertEquals(lastpredBr, fmt(gailResult.getPredictions().get(gailResult.getPredictions().size() - 1).getResult("breastRisk"), 10));
    }
    
   // @Test
    @DisplayName("Test GAIL model with different age ranges")
    void testGailWithDifferentAgeRanges() throws Exception {
        com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        fam.setAgeTo(80);
        com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
        proband.setAge(40);
        proband.setMenarcheAge(13);
        proband.setRace(Person.Race.White);
        fam.getFamily().add(proband);

        Model model = new Model();
        model.setAgeBy(10);
        model.setAgeTo(80);
        model.setName(ModelName.GAIL);
        model.setChartCode(1);
        fam.getRequestedModels().add(model);

        MvcResult result = mockMvc.perform(post("/api/risk/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fam))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CombinedResult combresult = objectMapper.readValue(result.getResponse().getContentAsString(), CombinedResult.class);
        GailResult gailResult = (GailResult) combresult.getResults().get(ModelName.GAIL);

        assertNotNull(gailResult);
        assertNotNull(gailResult.getPredictions());
        assertFalse(gailResult.getPredictions().isEmpty());
    }

    String fmt(Double numb) {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(3, RoundingMode.HALF_UP);
        return a_dec.toPlainString();
    }

    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(5, risk.getAgeBy());
        assertEquals(21, risk.getFamily().size());
    }

    String fmt(Double numb, int len) {
        if (numb != null && numb.toString().length() > len) {
            return numb.toString().substring(0, len);
        }
        return numb != null ? numb.toString() : "";
    }

    @BeforeEach
    void setUp() {
        try {
            baseFamily = Utils.createRiskFromFile("test/exampleGailFamily.xml", Risk.class);
            Person prob = baseFamily.getFamily().get(0);
            prob.setRace(Person.Race.White);
        } catch (JAXBException ex) {
            throw new RuntimeException("Failed to load test data", ex);
        }
    }

    Risk copyBase() {
        try {
            return (Risk) baseFamily.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Failed to clone base family", ex);
        }
    }
}
