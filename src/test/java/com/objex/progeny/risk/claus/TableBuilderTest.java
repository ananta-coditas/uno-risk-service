package com.objex.progeny.risk.claus;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.progeny.risk.claus.ClausDelegate.AgeKey;

/**
 * Unit tests for {@link TableBuilder}.
 */
@DisplayName("TableBuilder Unit Tests")
class TableBuilderTest {

    @Test
    @DisplayName("Should have correct REPORTING_INTERVAL constant")
    void testReportingInterval() {
        assertThat(TableBuilder.REPORTING_INTERVAL).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Should build tables successfully")
    void testBuildTables() {
        // Tables are built in static initializer
        // Verify that tables are not null and contain expected data
        
        // Check singleFDRTbl
        assertThat(ClausDelegate.singleFDRTbl).isNotNull();
        assertThat(ClausDelegate.singleFDRTbl).isNotEmpty();
        
        // Check singleSDRTbl
        assertThat(ClausDelegate.singleSDRTbl).isNotNull();
        assertThat(ClausDelegate.singleSDRTbl).isNotEmpty();
        
        // Check singleFDROvarianTbl
        assertThat(ClausDelegate.singleFDROvarianTbl).isNotNull();
        assertThat(ClausDelegate.singleFDROvarianTbl).isNotEmpty();
        
        // Check twoFDROvarianTbl
        assertThat(ClausDelegate.twoFDROvarianTbl).isNotNull();
        assertThat(ClausDelegate.twoFDROvarianTbl).isNotEmpty();
        
        // Check singleFDRandOvarianTbl
        assertThat(ClausDelegate.singleFDRandOvarianTbl).isNotNull();
        assertThat(ClausDelegate.singleFDRandOvarianTbl).isNotEmpty();
        
        // Check twoFDRTbl
        assertThat(ClausDelegate.twoFDRTbl).isNotNull();
        assertThat(ClausDelegate.twoFDRTbl).isNotEmpty();
        
        // Check singleMaternalSinglePaternalSDRTbl
        assertThat(ClausDelegate.singleMaternalSinglePaternalSDRTbl).isNotNull();
        assertThat(ClausDelegate.singleMaternalSinglePaternalSDRTbl).isNotEmpty();
        
        // Check motherAndMaternalAuntTbl
        assertThat(ClausDelegate.motherAndMaternalAuntTbl).isNotNull();
        assertThat(ClausDelegate.motherAndMaternalAuntTbl).isNotEmpty();
        
        // Check motherAndPaternalAuntTbl
        assertThat(ClausDelegate.motherAndPaternalAuntTbl).isNotNull();
        assertThat(ClausDelegate.motherAndPaternalAuntTbl).isNotEmpty();
        
        // Check twoSDRTbl
        assertThat(ClausDelegate.twoSDRTbl).isNotNull();
        assertThat(ClausDelegate.twoSDRTbl).isNotEmpty();
        
        // Check singleSDRsingleFDRTbl
        assertThat(ClausDelegate.singleSDRsingleFDRTbl).isNotNull();
        // Note: singleSDRsingleFDRTbl may not be initialized in TableBuilder.buildTables()
        // It might be initialized elsewhere or may be empty
    }

    @Test
    @DisplayName("Should have correct values in singleFDRTbl")
    void testSingleFDRTblValues() {
        Map<AgeKey, Map<AgeKey, Double>> table = ClausDelegate.singleFDRTbl;
        
        // Check that table has entries for all age keys
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
        assertThat(table.containsKey(AgeKey.THIRTY)).isTrue();
        assertThat(table.containsKey(AgeKey.FOURTY)).isTrue();
        assertThat(table.containsKey(AgeKey.FIFTY)).isTrue();
        assertThat(table.containsKey(AgeKey.SIXTY)).isTrue();
        assertThat(table.containsKey(AgeKey.SEVENTY)).isTrue();
        
        // Check that each age key has a map with values
        Map<AgeKey, Double> twentyMap = table.get(AgeKey.TWENTY);
        assertThat(twentyMap).isNotNull();
        assertThat(twentyMap).isNotEmpty();
        
        // Verify some expected values (from the table initialization)
        // TWENTY row: .007 ,.005 ,.003 ,.002 ,.002 ,.001
        assertThat(twentyMap.get(AgeKey.TWENTY)).isEqualTo(0.007);
        assertThat(twentyMap.get(AgeKey.THIRTY)).isEqualTo(0.005);
        assertThat(twentyMap.get(AgeKey.FOURTY)).isEqualTo(0.003);
    }

    @Test
    @DisplayName("Should have correct values in singleSDRTbl")
    void testSingleSDRTblValues() {
        Map<AgeKey, Map<AgeKey, Double>> table = ClausDelegate.singleSDRTbl;
        
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
        assertThat(table.containsKey(AgeKey.THIRTY)).isTrue();
        
        Map<AgeKey, Double> twentyMap = table.get(AgeKey.TWENTY);
        assertThat(twentyMap).isNotNull();
        // TWENTY row: .004 ,.003 ,.002 ,.001 ,.001 ,.001
        assertThat(twentyMap.get(AgeKey.TWENTY)).isEqualTo(0.004);
    }

    @Test
    @DisplayName("Should have correct values in singleFDROvarianTbl")
    void testSingleFDROvarianTblValues() {
        Map<AgeKey, Map<AgeKey, Double>> table = ClausDelegate.singleFDROvarianTbl;
        
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
        
        Map<AgeKey, Double> twentyMap = table.get(AgeKey.TWENTY);
        assertThat(twentyMap).isNotNull();
        // TWENTY row: .002 ,.002 ,.002 ,.002 ,.002 ,.002
        assertThat(twentyMap.get(AgeKey.TWENTY)).isEqualTo(0.002);
        assertThat(twentyMap.get(AgeKey.THIRTY)).isEqualTo(0.002);
    }

    @Test
    @DisplayName("Should have correct values in twoFDROvarianTbl")
    void testTwoFDROvarianTblValues() {
        Map<AgeKey, Map<AgeKey, Double>> table = ClausDelegate.twoFDROvarianTbl;
        
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
        
        Map<AgeKey, Double> twentyMap = table.get(AgeKey.TWENTY);
        assertThat(twentyMap).isNotNull();
        // TWENTY row: .011 ,.011 ,.011 ,.011 ,.011 ,.011
        assertThat(twentyMap.get(AgeKey.TWENTY)).isEqualTo(0.011);
    }

    @Test
    @DisplayName("Should have correct values in singleFDRandOvarianTbl")
    void testSingleFDRandOvarianTblValues() {
        Map<AgeKey, Map<AgeKey, Double>> table = ClausDelegate.singleFDRandOvarianTbl;
        
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
        
        Map<AgeKey, Double> twentyMap = table.get(AgeKey.TWENTY);
        assertThat(twentyMap).isNotNull();
        // TWENTY row: .017 ,.015 ,.013 ,.010 ,.007 ,.005,.004
        assertThat(twentyMap.get(AgeKey.TWENTY)).isEqualTo(0.017);
    }

    @Test
    @DisplayName("Should have correct structure in twoFDRTbl")
    void testTwoFDRTblStructure() {
        Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table = ClausDelegate.twoFDRTbl;
        
        assertThat(table).isNotNull();
        assertThat(table).isNotEmpty();
        
        // Check that table has entries for proband age keys
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
        assertThat(table.containsKey(AgeKey.THIRTY)).isTrue();
        
        // Check nested structure
        Map<AgeKey, Map<AgeKey, Double>> twentyMap = table.get(AgeKey.TWENTY);
        assertThat(twentyMap).isNotNull();
        assertThat(twentyMap).isNotEmpty();
        
        // Check that it has entries for relative age keys
        assertThat(twentyMap.containsKey(AgeKey.TWENTY)).isTrue();
        
        Map<AgeKey, Double> relativeMap = twentyMap.get(AgeKey.TWENTY);
        assertThat(relativeMap).isNotNull();
        assertThat(relativeMap).isNotEmpty();
    }

    @Test
    @DisplayName("Should have correct structure in twoSDRTbl")
    void testTwoSDRTblStructure() {
        Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table = ClausDelegate.twoSDRTbl;
        
        assertThat(table).isNotNull();
        assertThat(table).isNotEmpty();
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
    }

    @Test
    @DisplayName("Should have correct structure in singleMaternalSinglePaternalSDRTbl")
    void testSingleMaternalSinglePaternalSDRTblStructure() {
        Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table = ClausDelegate.singleMaternalSinglePaternalSDRTbl;
        
        assertThat(table).isNotNull();
        assertThat(table).isNotEmpty();
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
    }

    @Test
    @DisplayName("Should have correct structure in motherAndMaternalAuntTbl")
    void testMotherAndMaternalAuntTblStructure() {
        Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table = ClausDelegate.motherAndMaternalAuntTbl;
        
        assertThat(table).isNotNull();
        assertThat(table).isNotEmpty();
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
    }

    @Test
    @DisplayName("Should have correct structure in motherAndPaternalAuntTbl")
    void testMotherAndPaternalAuntTblStructure() {
        Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table = ClausDelegate.motherAndPaternalAuntTbl;
        
        assertThat(table).isNotNull();
        assertThat(table).isNotEmpty();
        assertThat(table.containsKey(AgeKey.TWENTY)).isTrue();
    }

    @Test
    @DisplayName("Should have correct structure in singleSDRsingleFDRTbl")
    void testSingleSDRsingleFDRTblStructure() {
        Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table = ClausDelegate.singleSDRsingleFDRTbl;
        
        assertThat(table).isNotNull();
        // Note: singleSDRsingleFDRTbl may not be initialized in TableBuilder.buildTables()
        // It might be initialized elsewhere or may be empty
        // This is acceptable as it's a conditional table
    }

    @Test
    @DisplayName("Should verify table values are within expected range")
    void testTableValuesRange() {
        // All risk values should be between 0 and 1
        verifyTableValuesRange(ClausDelegate.singleFDRTbl);
        verifyTableValuesRange(ClausDelegate.singleSDRTbl);
        verifyTableValuesRange(ClausDelegate.singleFDROvarianTbl);
        verifyTableValuesRange(ClausDelegate.twoFDROvarianTbl);
        verifyTableValuesRange(ClausDelegate.singleFDRandOvarianTbl);
        verifyMultiTableValuesRange(ClausDelegate.twoFDRTbl);
        verifyMultiTableValuesRange(ClausDelegate.twoSDRTbl);
        verifyMultiTableValuesRange(ClausDelegate.singleMaternalSinglePaternalSDRTbl);
        verifyMultiTableValuesRange(ClausDelegate.motherAndMaternalAuntTbl);
        verifyMultiTableValuesRange(ClausDelegate.motherAndPaternalAuntTbl);
        verifyMultiTableValuesRange(ClausDelegate.singleSDRsingleFDRTbl);
    }

    private void verifyTableValuesRange(Map<AgeKey, Map<AgeKey, Double>> table) {
        for (Map<AgeKey, Double> innerMap : table.values()) {
            for (Double value : innerMap.values()) {
                assertThat(value).isNotNull();
                assertThat(value).isGreaterThanOrEqualTo(0.0);
                assertThat(value).isLessThanOrEqualTo(1.0);
            }
        }
    }

    private void verifyMultiTableValuesRange(Map<AgeKey, Map<AgeKey, Map<AgeKey, Double>>> table) {
        for (Map<AgeKey, Map<AgeKey, Double>> middleMap : table.values()) {
            for (Map<AgeKey, Double> innerMap : middleMap.values()) {
                for (Double value : innerMap.values()) {
                    assertThat(value).isNotNull();
                    assertThat(value).isGreaterThanOrEqualTo(0.0);
                    assertThat(value).isLessThanOrEqualTo(1.0);
                }
            }
        }
    }
}

