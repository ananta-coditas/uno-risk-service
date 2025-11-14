package com.objex.warehouse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.objex.risk.beans.BaseRisk;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RiskInputs}.
 */
@DisplayName("RiskInputs Unit Tests")
class RiskInputsTest {

    private RiskInputs riskInputs;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        riskInputs = new RiskInputs();
        transaction = new Transaction();
    }

    @Test
    @DisplayName("Should create RiskInputs with default constructor")
    void testDefaultConstructor() {
        assertThat(riskInputs).isNotNull();
        assertThat(riskInputs.getTransId()).isNull();
        assertThat(riskInputs.getMmrRisk()).isNull();
        assertThat(riskInputs.getBrcaRisk()).isNull();
        assertThat(riskInputs.getMelaRisk()).isNull();
        assertThat(riskInputs.getPancRisk()).isNull();
        assertThat(riskInputs.getPremmRisk()).isNull();
        assertThat(riskInputs.getGailRisk()).isNull();
        assertThat(riskInputs.getClausRisk()).isNull();
        assertThat(riskInputs.getTyrerRisk()).isNull();
    }

    @Test
    @DisplayName("Should create RiskInputs with Transaction constructor")
    void testTransactionConstructor() {
        String transId = "TRANS-001";
        transaction.setTransId(transId);
        
        RiskInputs inputs = new RiskInputs(transaction);
        assertThat(inputs).isNotNull();
        assertThat(inputs.getTransId()).isEqualTo(transId);
        assertThat(transaction.getInputs()).isEqualTo(inputs);
    }

    @Test
    @DisplayName("Should set and get transId")
    void testTransId() {
        String transId = "TRANS-123";
        riskInputs.setTransId(transId);
        assertThat(riskInputs.getTransId()).isEqualTo(transId);
    }

    @Test
    @DisplayName("Should set and get MmrRisk")
    void testMmrRisk() {
        com.objex.risk.beans.mmr.Risk mmrRisk = mock(com.objex.risk.beans.mmr.Risk.class);
        riskInputs.setMmrRisk(mmrRisk);
        assertThat(riskInputs.getMmrRisk()).isEqualTo(mmrRisk);
    }

    @Test
    @DisplayName("Should set and get BrcaRisk")
    void testBrcaRisk() {
        com.objex.risk.beans.brca.Risk brcaRisk = mock(com.objex.risk.beans.brca.Risk.class);
        riskInputs.setBrcaRisk(brcaRisk);
        assertThat(riskInputs.getBrcaRisk()).isEqualTo(brcaRisk);
    }

    @Test
    @DisplayName("Should set and get MelaRisk")
    void testMelaRisk() {
        com.objex.risk.beans.mela.Risk melaRisk = mock(com.objex.risk.beans.mela.Risk.class);
        riskInputs.setMelaRisk(melaRisk);
        assertThat(riskInputs.getMelaRisk()).isEqualTo(melaRisk);
    }

    @Test
    @DisplayName("Should set and get PancRisk")
    void testPancRisk() {
        com.objex.risk.beans.panc.Risk pancRisk = mock(com.objex.risk.beans.panc.Risk.class);
        riskInputs.setPancRisk(pancRisk);
        assertThat(riskInputs.getPancRisk()).isEqualTo(pancRisk);
    }

    @Test
    @DisplayName("Should set and get PremmRisk")
    void testPremmRisk() {
        com.objex.risk.beans.premm.Risk premmRisk = mock(com.objex.risk.beans.premm.Risk.class);
        riskInputs.setPremmRisk(premmRisk);
        assertThat(riskInputs.getPremmRisk()).isEqualTo(premmRisk);
    }

    @Test
    @DisplayName("Should set and get GailRisk")
    void testGailRisk() {
        com.objex.risk.beans.gail.Risk gailRisk = mock(com.objex.risk.beans.gail.Risk.class);
        riskInputs.setGailRisk(gailRisk);
        assertThat(riskInputs.getGailRisk()).isEqualTo(gailRisk);
    }

    @Test
    @DisplayName("Should set and get ClausRisk")
    void testClausRisk() {
        com.objex.risk.beans.claus.Risk clausRisk = mock(com.objex.risk.beans.claus.Risk.class);
        riskInputs.setClausRisk(clausRisk);
        assertThat(riskInputs.getClausRisk()).isEqualTo(clausRisk);
    }

    @Test
    @DisplayName("Should set and get TyrerRisk")
    void testTyrerRisk() {
        com.objex.risk.beans.tcuzick.Risk tyrerRisk = mock(com.objex.risk.beans.tcuzick.Risk.class);
        riskInputs.setTyrerRisk(tyrerRisk);
        assertThat(riskInputs.getTyrerRisk()).isEqualTo(tyrerRisk);
    }

    @Test
    @DisplayName("Should return empty list when asList is called with no risks")
    void testAsListEmpty() {
        List<? extends BaseRisk> risks = riskInputs.asList();
        assertThat(risks).isNotNull();
        assertThat(risks).isEmpty();
    }

    @Test
    @DisplayName("Should return list with non-null risks when asList is called")
    void testAsListWithRisks() {
        com.objex.risk.beans.brca.Risk brcaRisk = mock(com.objex.risk.beans.brca.Risk.class);
        com.objex.risk.beans.mmr.Risk mmrRisk = mock(com.objex.risk.beans.mmr.Risk.class);
        com.objex.risk.beans.gail.Risk gailRisk = mock(com.objex.risk.beans.gail.Risk.class);
        
        riskInputs.setBrcaRisk(brcaRisk);
        riskInputs.setMmrRisk(mmrRisk);
        riskInputs.setGailRisk(gailRisk);
        
        List<? extends BaseRisk> risks = riskInputs.asList();
        assertThat(risks).isNotNull();
        assertThat(risks).hasSize(3);
        // Check that all risks are present (using individual checks to avoid wildcard type issues)
        assertThat(risks.stream().anyMatch(r -> r == brcaRisk)).isTrue();
        assertThat(risks.stream().anyMatch(r -> r == mmrRisk)).isTrue();
        assertThat(risks.stream().anyMatch(r -> r == gailRisk)).isTrue();
    }

    @Test
    @DisplayName("Should filter null risks when asList is called")
    void testAsListFiltersNulls() {
        com.objex.risk.beans.brca.Risk brcaRisk = mock(com.objex.risk.beans.brca.Risk.class);
        riskInputs.setBrcaRisk(brcaRisk);
        // mmrRisk, melaRisk, etc. remain null
        
        List<? extends BaseRisk> risks = riskInputs.asList();
        assertThat(risks).isNotNull();
        assertThat(risks).hasSize(1);
        assertThat(risks.get(0)).isEqualTo(brcaRisk);
    }

    @Test
    @DisplayName("Should return zero family size when no risks are set")
    void testGetFamilySizeEmpty() {
        int familySize = riskInputs.getFamilySize();
        assertThat(familySize).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return family size from first non-null risk")
    void testGetFamilySizeWithRisks() {
        com.objex.risk.beans.brca.Risk brcaRisk = mock(com.objex.risk.beans.brca.Risk.class);
        com.objex.risk.beans.mmr.Risk mmrRisk = mock(com.objex.risk.beans.mmr.Risk.class);
        
        @SuppressWarnings("unchecked")
        List<com.objex.risk.beans.brca.Person> family1 = (List<com.objex.risk.beans.brca.Person>) mock(List.class);
        when(family1.size()).thenReturn(3);
        
        @SuppressWarnings("unchecked")
        List<com.objex.risk.beans.mmr.Person> family2 = (List<com.objex.risk.beans.mmr.Person>) mock(List.class);
        when(family2.size()).thenReturn(1);
        
        when(brcaRisk.getFamily()).thenReturn(family1);
        when(mmrRisk.getFamily()).thenReturn(family2);
        
        riskInputs.setBrcaRisk(brcaRisk);
        riskInputs.setMmrRisk(mmrRisk);
        
        int familySize = riskInputs.getFamilySize();
        assertThat(familySize).isEqualTo(3); // Should return size from first risk (brcaRisk)
    }

    @Test
    @DisplayName("Should handle all risk types together")
    void testAllRiskTypes() {
        com.objex.risk.beans.mmr.Risk mmrRisk = mock(com.objex.risk.beans.mmr.Risk.class);
        com.objex.risk.beans.brca.Risk brcaRisk = mock(com.objex.risk.beans.brca.Risk.class);
        com.objex.risk.beans.mela.Risk melaRisk = mock(com.objex.risk.beans.mela.Risk.class);
        com.objex.risk.beans.panc.Risk pancRisk = mock(com.objex.risk.beans.panc.Risk.class);
        com.objex.risk.beans.premm.Risk premmRisk = mock(com.objex.risk.beans.premm.Risk.class);
        com.objex.risk.beans.gail.Risk gailRisk = mock(com.objex.risk.beans.gail.Risk.class);
        com.objex.risk.beans.claus.Risk clausRisk = mock(com.objex.risk.beans.claus.Risk.class);
        com.objex.risk.beans.tcuzick.Risk tyrerRisk = mock(com.objex.risk.beans.tcuzick.Risk.class);
        
        riskInputs.setMmrRisk(mmrRisk);
        riskInputs.setBrcaRisk(brcaRisk);
        riskInputs.setMelaRisk(melaRisk);
        riskInputs.setPancRisk(pancRisk);
        riskInputs.setPremmRisk(premmRisk);
        riskInputs.setGailRisk(gailRisk);
        riskInputs.setClausRisk(clausRisk);
        riskInputs.setTyrerRisk(tyrerRisk);
        riskInputs.setTransId("TRANS-ALL");
        
        assertThat(riskInputs.getMmrRisk()).isEqualTo(mmrRisk);
        assertThat(riskInputs.getBrcaRisk()).isEqualTo(brcaRisk);
        assertThat(riskInputs.getMelaRisk()).isEqualTo(melaRisk);
        assertThat(riskInputs.getPancRisk()).isEqualTo(pancRisk);
        assertThat(riskInputs.getPremmRisk()).isEqualTo(premmRisk);
        assertThat(riskInputs.getGailRisk()).isEqualTo(gailRisk);
        assertThat(riskInputs.getClausRisk()).isEqualTo(clausRisk);
        assertThat(riskInputs.getTyrerRisk()).isEqualTo(tyrerRisk);
        assertThat(riskInputs.getTransId()).isEqualTo("TRANS-ALL");
        
        List<? extends BaseRisk> allRisks = riskInputs.asList();
        assertThat(allRisks).hasSize(8);
    }

    @Test
    @DisplayName("Should handle Transaction constructor setting inputs on transaction")
    void testTransactionConstructorSetsInputs() {
        Transaction trans = new Transaction();
        String transId = "TRANS-002";
        trans.setTransId(transId);
        
        RiskInputs inputs = new RiskInputs(trans);
        
        assertThat(trans.getInputs()).isEqualTo(inputs);
        assertThat(inputs.getTransId()).isEqualTo(transId);
    }
}

