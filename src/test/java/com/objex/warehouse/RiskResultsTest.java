package com.objex.warehouse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.brca.BRCAResult;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.mela.MelaResult;
import com.objex.risk.beans.mmr.MMRResult;
import com.objex.risk.beans.panc.PancResult;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.tcuzick.TyrerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RiskResults}.
 */
@DisplayName("RiskResults Unit Tests")
class RiskResultsTest {

    private RiskResults riskResults;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        riskResults = new RiskResults();
        transaction = new Transaction();
    }

    @Test
    @DisplayName("Should create RiskResults with default constructor")
    void testDefaultConstructor() {
        assertThat(riskResults).isNotNull();
        assertThat(riskResults.getTransId()).isNull();
        assertThat(riskResults.getPedImage()).isNull();
        assertThat(riskResults.getMMRResult()).isNull();
        assertThat(riskResults.getBrcaResult()).isNull();
        assertThat(riskResults.getMelaResult()).isNull();
        assertThat(riskResults.getPancResult()).isNull();
        assertThat(riskResults.getPremmResult()).isNull();
        assertThat(riskResults.getGailResult()).isNull();
        assertThat(riskResults.getClausResult()).isNull();
        assertThat(riskResults.getTyrerResult()).isNull();
    }

    @Test
    @DisplayName("Should create RiskResults with Transaction constructor")
    void testTransactionConstructor() {
        String transId = "TRANS-001";
        transaction.setTransId(transId);
        
        RiskResults results = new RiskResults(transaction);
        assertThat(results).isNotNull();
        assertThat(results.getTransId()).isEqualTo(transId);
    }

    @Test
    @DisplayName("Should set and get transId")
    void testTransId() {
        String transId = "TRANS-123";
        riskResults.setTransId(transId);
        assertThat(riskResults.getTransId()).isEqualTo(transId);
    }

    @Test
    @DisplayName("Should set and get pedImage")
    void testPedImage() {
        byte[] pedImage = new byte[] { 1, 2, 3, 4, 5 };
        riskResults.setPedImage(pedImage);
        assertThat(riskResults.getPedImage()).isEqualTo(pedImage);
    }

    @Test
    @DisplayName("Should set and get MMRResult")
    void testMMRResult() {
        MMRResult mmrResult = mock(MMRResult.class);
        riskResults.setMMRResult(mmrResult);
        assertThat(riskResults.getMMRResult()).isEqualTo(mmrResult);
    }

    @Test
    @DisplayName("Should set and get BRCAResult")
    void testBRCAResult() {
        BRCAResult brcaResult = mock(BRCAResult.class);
        riskResults.setBrcaResult(brcaResult);
        assertThat(riskResults.getBrcaResult()).isEqualTo(brcaResult);
    }

    @Test
    @DisplayName("Should set and get MelaResult")
    void testMelaResult() {
        MelaResult melaResult = mock(MelaResult.class);
        riskResults.setMelaResult(melaResult);
        assertThat(riskResults.getMelaResult()).isEqualTo(melaResult);
    }

    @Test
    @DisplayName("Should set and get PancResult")
    void testPancResult() {
        PancResult pancResult = mock(PancResult.class);
        riskResults.setPancResult(pancResult);
        assertThat(riskResults.getPancResult()).isEqualTo(pancResult);
    }

    @Test
    @DisplayName("Should set and get PREMMResult")
    void testPREMMResult() {
        PREMMResult premmResult = mock(PREMMResult.class);
        riskResults.setPremmResult(premmResult);
        assertThat(riskResults.getPremmResult()).isEqualTo(premmResult);
    }

    @Test
    @DisplayName("Should set and get GailResult")
    void testGailResult() {
        GailResult gailResult = mock(GailResult.class);
        riskResults.setGailResult(gailResult);
        assertThat(riskResults.getGailResult()).isEqualTo(gailResult);
    }

    @Test
    @DisplayName("Should set and get ClausResult")
    void testClausResult() {
        ClausResult clausResult = mock(ClausResult.class);
        riskResults.setClausResult(clausResult);
        assertThat(riskResults.getClausResult()).isEqualTo(clausResult);
    }

    @Test
    @DisplayName("Should set and get TyrerResult")
    void testTyrerResult() {
        TyrerResult tyrerResult = mock(TyrerResult.class);
        riskResults.setTyrerResult(tyrerResult);
        assertThat(riskResults.getTyrerResult()).isEqualTo(tyrerResult);
    }

    @Test
    @DisplayName("Should return empty list when asList is called with no results")
    void testAsListEmpty() {
        List<? extends BaseResult> results = riskResults.asList();
        assertThat(results).isNotNull();
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should return list with non-null results when asList is called")
    void testAsListWithResults() {
        BRCAResult brcaResult = mock(BRCAResult.class);
        MMRResult mmrResult = mock(MMRResult.class);
        GailResult gailResult = mock(GailResult.class);
        
        riskResults.setBrcaResult(brcaResult);
        riskResults.setMMRResult(mmrResult);
        riskResults.setGailResult(gailResult);
        
        List<? extends BaseResult> results = riskResults.asList();
        assertThat(results).isNotNull();
        assertThat(results).hasSize(3);
        // Check that all results are present (using individual checks to avoid wildcard type issues)
        assertThat(results.stream().anyMatch(r -> r == brcaResult)).isTrue();
        assertThat(results.stream().anyMatch(r -> r == mmrResult)).isTrue();
        assertThat(results.stream().anyMatch(r -> r == gailResult)).isTrue();
    }

    @Test
    @DisplayName("Should filter null results when asList is called")
    void testAsListFiltersNulls() {
        BRCAResult brcaResult = mock(BRCAResult.class);
        riskResults.setBrcaResult(brcaResult);
        // mmrResult, melaResult, etc. remain null
        
        List<? extends BaseResult> results = riskResults.asList();
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(brcaResult);
    }

    @Test
    @DisplayName("Should return empty set when getVersions is called with no results")
    void testGetVersionsEmpty() {
        Set<String> versions = riskResults.getVersions();
        assertThat(versions).isNotNull();
        assertThat(versions).isEmpty();
    }

    @Test
    @DisplayName("Should return set of versions when getVersions is called with results")
    void testGetVersionsWithResults() {
        BRCAResult brcaResult = mock(BRCAResult.class);
        MMRResult mmrResult = mock(MMRResult.class);
        GailResult gailResult = mock(GailResult.class);
        
        when(brcaResult.getVersion()).thenReturn("2.1");
        when(mmrResult.getVersion()).thenReturn("1.0");
        when(gailResult.getVersion()).thenReturn("2.1");
        
        riskResults.setBrcaResult(brcaResult);
        riskResults.setMMRResult(mmrResult);
        riskResults.setGailResult(gailResult);
        
        Set<String> versions = riskResults.getVersions();
        assertThat(versions).isNotNull();
        assertThat(versions).hasSize(2); // "2.1" appears twice but Set should deduplicate
        assertThat(versions).contains("2.1", "1.0");
    }

    @Test
    @DisplayName("Should filter results by supported models")
    void testGetResultsWithSupportedModels() throws Exception {
        BRCAResult brcaResult = mock(BRCAResult.class);
        MMRResult mmrResult = mock(MMRResult.class);
        GailResult gailResult = mock(GailResult.class);
        
        when(brcaResult.getModel()).thenReturn("BRCA");
        when(mmrResult.getModel()).thenReturn("MMR");
        when(gailResult.getModel()).thenReturn("GAIL");
        
        riskResults.setBrcaResult(brcaResult);
        riskResults.setMMRResult(mmrResult);
        riskResults.setGailResult(gailResult);
        
        List<String> supportedModels = new ArrayList<>();
        supportedModels.add("BRCA");
        supportedModels.add("GAIL");
        
        // Use reflection to access package-private method
        java.lang.reflect.Method method = RiskResults.class.getDeclaredMethod("getResults", List.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<? extends BaseResult> results = (List<? extends BaseResult>) method.invoke(riskResults, supportedModels);
        
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);
        // Check that filtered results contain only BRCA and GAIL
        assertThat(results.stream().anyMatch(r -> r == brcaResult)).isTrue();
        assertThat(results.stream().anyMatch(r -> r == gailResult)).isTrue();
        assertThat(results.stream().anyMatch(r -> r == mmrResult)).isFalse();
    }

    @Test
    @DisplayName("Should handle all result types together")
    void testAllResultTypes() {
        BRCAResult brcaResult = mock(BRCAResult.class);
        MMRResult mmrResult = mock(MMRResult.class);
        MelaResult melaResult = mock(MelaResult.class);
        PancResult pancResult = mock(PancResult.class);
        PREMMResult premmResult = mock(PREMMResult.class);
        GailResult gailResult = mock(GailResult.class);
        ClausResult clausResult = mock(ClausResult.class);
        TyrerResult tyrerResult = mock(TyrerResult.class);
        byte[] pedImage = new byte[] { 1, 2, 3 };
        
        riskResults.setBrcaResult(brcaResult);
        riskResults.setMMRResult(mmrResult);
        riskResults.setMelaResult(melaResult);
        riskResults.setPancResult(pancResult);
        riskResults.setPremmResult(premmResult);
        riskResults.setGailResult(gailResult);
        riskResults.setClausResult(clausResult);
        riskResults.setTyrerResult(tyrerResult);
        riskResults.setPedImage(pedImage);
        riskResults.setTransId("TRANS-ALL");
        
        assertThat(riskResults.getBrcaResult()).isEqualTo(brcaResult);
        assertThat(riskResults.getMMRResult()).isEqualTo(mmrResult);
        assertThat(riskResults.getMelaResult()).isEqualTo(melaResult);
        assertThat(riskResults.getPancResult()).isEqualTo(pancResult);
        assertThat(riskResults.getPremmResult()).isEqualTo(premmResult);
        assertThat(riskResults.getGailResult()).isEqualTo(gailResult);
        assertThat(riskResults.getClausResult()).isEqualTo(clausResult);
        assertThat(riskResults.getTyrerResult()).isEqualTo(tyrerResult);
        assertThat(riskResults.getPedImage()).isEqualTo(pedImage);
        assertThat(riskResults.getTransId()).isEqualTo("TRANS-ALL");
        
        List<? extends BaseResult> allResults = riskResults.asList();
        // Note: asList() only includes 7 result types (excludes clausResult)
        assertThat(allResults).hasSize(7);
    }
}

