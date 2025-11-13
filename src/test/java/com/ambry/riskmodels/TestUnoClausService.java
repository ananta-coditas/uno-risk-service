package com.ambry.riskmodels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.claus.Risk;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.uno.Cancer;
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
 * CLAUS Risk Model Service Tests
 */
@IntegrationTest
@AutoConfigureMockMvc
@ActiveProfiles("testdev")
@DisplayName("CLAUS Risk Model Service Tests")
class TestUnoClausService {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Risk baseFamily;

    /**
     * Mom affected at 22 proband is 39
     */
    protected Risk simpleFDR(int probandAge, int fathAge, int momAge) {
        return simpleFDR(probandAge, fathAge, momAge, 22);
    }

    protected Risk simpleFDR(int probandAge, int fathAge, int momAge, int motherAffectedAge) {
        Risk risk = new Risk();
        risk.setAgeTo(79);
        Person person = new Person();
        person.setId(1);
        person.setFatherId(2);
        person.setMotherId(3);
        person.setGender(0);
        person.setAge(probandAge);
        risk.add(person);
        person = new Person();
        person.setId(2);
        person.setGender(1);
        person.setAffectedBreastType(0);
        person.setAge(fathAge);
        risk.add(person);
        person = new Person();
        person.setId(3);
        if (motherAffectedAge > 0) {
            person.setAffectedBreastType(1);
            person.setDiagnoseBreastAge(motherAffectedAge);
        }
        person.setAge(momAge);
        person.setGender(0);
        risk.add(person);
        return risk;
    }

    /**
     * Aunt affected at 22
     */
    protected Risk affectedAuntSDR(int probandAge, int fathAge, int momAge) {
        Risk risk = new Risk();
        risk.setAgeTo(79);
        Person person = new Person();
        person.setId(1);
        person.setFatherId(2);
        person.setMotherId(3);
        person.setGender(0);
        person.setAge(probandAge);
        risk.add(person);

        person = new Person();
        person.setId(2);
        person.setGender(1);
        person.setAffectedBreastType(0);
        person.setAge(fathAge);
        person.setFatherId(4);
        person.setMotherId(5);
        risk.add(person);

        person = new Person();
        person.setId(3);
        person.setAge(momAge);
        person.setGender(0);
        risk.add(person);

        person = new Person();
        person.setId(4);
        person.setAge(81);
        person.setGender(0);
        risk.add(person);

        person = new Person();
        person.setId(5);
        person.setAge(82);
        person.setGender(0);
        risk.add(person);

        person = new Person();
        person.setId(6);
        person.setGender(0);
        person.setAffectedBreastType(1);
        person.setDiagnoseBreastAge(22);
        person.setAge(fathAge + 2);
        person.setFatherId(4);
        person.setMotherId(5);
        risk.add(person);

        return risk;
    }

    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(5, risk.getAgeBy());
        assertEquals(21, risk.getFamily().size());
    }

    String fmt(Double numb) {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(3, RoundingMode.HALF_UP);
        return a_dec.toPlainString();
    }

    @BeforeEach
    void setUp() {
        try {
            baseFamily = Utils.createRiskFromFile("test/exampleClausFamily.xml", Risk.class);
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

    @Test
    @DisplayName("Test combined base family risk calculation for CLAUS model")
    void testCombinedBaseFamily() throws Exception {
        String predBr = "0.004";
        com.objex.risk.beans.uno.Risk risk = simpleFDRCombined(29, 62, 60);

        risk.getFamily().get(2).getCancer(Cancer.CancerType.BREAST).setAffected(AffectedType.NONE.value);

        com.objex.risk.beans.uno.Person person = new com.objex.risk.beans.uno.Person();
        person.setId(4);
        Cancer ovarian = new Cancer();
        ovarian.setCancerType(Cancer.CancerType.OVARIAN);
        ovarian.setAffected(1);
        ovarian.setDiagnoseAge(29);
        person.getCancers().add(ovarian);
        person.setAge(42);
        person.setGender(0);
        person.setFatherId(2);
        person.setMotherId(3);
        risk.add(person);

        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(79);
        model.setName(ModelName.CLAUS);
        model.setChartCode(1);
        risk.getRequestedModels().add(model);

        MvcResult result = mockMvc.perform(post("/api/risk/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CombinedResult combresult = objectMapper.readValue(result.getResponse().getContentAsString(), CombinedResult.class);
        ClausResult clausResult = (ClausResult) combresult.getResults().get(ModelName.CLAUS);

        assertNotNull(clausResult);
        assertNotNull(clausResult.getPredictions());
        assertFalse(clausResult.getPredictions().isEmpty());
        assertEquals(predBr, fmt(clausResult.getPredictions().get(0).getResult("breastRisk")));
        
        // Verify last prediction exists
        int lastIndex = clausResult.getPredictions().size() - 1;
        assertNotNull(clausResult.getPredictions().get(lastIndex));
    }

    private com.objex.risk.beans.uno.Risk simpleFDRCombined(int probandAge, int fathAge, int momAge) {
        return simpleFDRCombined(probandAge, fathAge, momAge, 22);
    }

    private com.objex.risk.beans.uno.Risk simpleFDRCombined(int probandAge, int fathAge, int momAge, int motherAffectedAge) {
        com.objex.risk.beans.uno.Risk risk = new com.objex.risk.beans.uno.Risk();
        risk.setAgeTo(79);

        com.objex.risk.beans.uno.Person person = new com.objex.risk.beans.uno.Person();
        person.setId(1);
        person.setFatherId(2);
        person.setMotherId(3);
        person.setGender(0);
        person.setAge(probandAge);
        risk.add(person);
        person = new com.objex.risk.beans.uno.Person();
        person.setId(2);
        person.setGender(1);
        person.setAge(fathAge);
        risk.add(person);
        person = new com.objex.risk.beans.uno.Person();
        person.setId(3);
        if (motherAffectedAge > 0) {
            Cancer cancer = new Cancer();
            cancer.setCancerType(Cancer.CancerType.BREAST);
            cancer.setAffected(1);
            cancer.setDiagnoseAge(motherAffectedAge);
        }
        person.setAge(momAge);
        person.setGender(0);
        risk.add(person);
        return risk;
    }
    
    @Test
    @DisplayName("Test CLAUS model with simple first-degree relative")
    void testSimpleFDR() throws Exception {
        com.objex.risk.beans.uno.Risk risk = simpleFDRCombined(29, 62, 60, 22);

        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(79);
        model.setName(ModelName.CLAUS);
        model.setChartCode(1);
        risk.getRequestedModels().add(model);

        MvcResult result = mockMvc.perform(post("/api/risk/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CombinedResult combresult = objectMapper.readValue(result.getResponse().getContentAsString(), CombinedResult.class);
        ClausResult clausResult = (ClausResult) combresult.getResults().get(ModelName.CLAUS);

        assertNotNull(clausResult);
        assertNotNull(clausResult.getPredictions());
        assertFalse(clausResult.getPredictions().isEmpty());
    }
    
    @Test
    @DisplayName("Test CLAUS model with affected aunt")
    void testAffectedAunt() throws Exception {
        com.objex.risk.beans.uno.Risk risk = new com.objex.risk.beans.uno.Risk();
        risk.setAgeTo(79);

        com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
        proband.setId(1);
        proband.setFatherId(2);
        proband.setMotherId(3);
        proband.setGender(0);
        proband.setAge(29);
        risk.add(proband);

        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(79);
        model.setName(ModelName.CLAUS);
        model.setChartCode(1);
        risk.getRequestedModels().add(model);

        MvcResult result = mockMvc.perform(post("/api/risk/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CombinedResult combresult = objectMapper.readValue(result.getResponse().getContentAsString(), CombinedResult.class);
        ClausResult clausResult = (ClausResult) combresult.getResults().get(ModelName.CLAUS);

        assertNotNull(clausResult);
    }
}
