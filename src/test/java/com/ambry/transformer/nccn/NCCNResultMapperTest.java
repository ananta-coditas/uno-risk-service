package com.ambry.transformer.nccn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.objex.risk.beans.nccn.NCCNResult;

@DisplayName("NCCNResultMapper Unit Tests")
class NCCNResultMapperTest {

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with HBOC data")
    void testConvertToWithHbocData() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocVersion("v1.0");
        source.setHbocOverall("Yes");
        source.setHbocCriteriaMet(new HashSet<>(Arrays.asList("Criteria1", "Criteria2")));
        source.setHbocCriteriaMetIds(new HashSet<>(Arrays.asList("ID1", "ID2")));
        source.setDateAssessed("2024-01-01");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getHbocVersion()).isEqualTo("v1.0");
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
        assertThat(result.getHbocCriteriaMet()).containsExactlyInAnyOrder("Criteria1", "Criteria2");
        assertThat(result.getHbocCriteriaMetIds()).containsExactlyInAnyOrder("ID1", "ID2");
        assertThat(result.getDateAssessed()).isEqualTo("2024-01-01");
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with Lynch data")
    void testConvertToWithLynchData() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setCrcVersion("v2.0");
        source.setLynchOverall("Yes");
        source.setLynchCriteriaMet(new HashSet<>(Arrays.asList("LynchCriteria1")));
        source.setLynchCriteriaMetIds(new HashSet<>(Arrays.asList("LynchID1")));
        source.setDateAssessed("2024-01-02");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getCrcVersion()).isEqualTo("v2.0");
        assertThat(result.getLynchOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
        assertThat(result.getLynchCriteriaMet()).containsExactlyInAnyOrder("LynchCriteria1");
        assertThat(result.getLynchCriteriaMetIds()).containsExactlyInAnyOrder("LynchID1");
        assertThat(result.getDateAssessed()).isEqualTo("2024-01-02");
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with APS/APC data")
    void testConvertToWithApsApcData() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setApsApcOverall("Yes");
        source.setApsApcCriteriaMet(new HashSet<>(Arrays.asList("APSCriteria1")));
        source.setApsApcCriteriaMetIds(new HashSet<>(Arrays.asList("APSID1")));
        source.setDateAssessed("2024-01-03");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getApsApcOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
        assertThat(result.getApsApcCriteriaMet()).containsExactlyInAnyOrder("APSCriteria1");
        assertThat(result.getApsApcCriteriaMetIds()).containsExactlyInAnyOrder("APSID1");
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with all data types")
    void testConvertToWithAllData() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocVersion("v1.0");
        source.setCrcVersion("v2.0");
        source.setHbocOverall("Yes");
        source.setLynchOverall("Yes");
        source.setApsApcOverall("Yes");
        source.setHbocCriteriaMet(new HashSet<>(Arrays.asList("HBOC1")));
        source.setLynchCriteriaMet(new HashSet<>(Arrays.asList("Lynch1")));
        source.setApsApcCriteriaMet(new HashSet<>(Arrays.asList("APS1")));
        source.setDateAssessed("2024-01-04");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getLynchOverall()).isTrue();
        assertThat(result.getApsApcOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with empty strings")
    void testConvertToWithEmptyStrings() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocOverall("");
        source.setLynchOverall("");
        source.setApsApcOverall("");
        source.setDateAssessed("2024-01-05");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        // Empty strings don't set boolean values, so they remain false (default)
        assertThat(result.getHbocOverall()).isFalse();
        assertThat(result.getLynchOverall()).isFalse();
        assertThat(result.getApsApcOverall()).isFalse();
        assertThat(result.getNccnCriteriaMet()).isFalse();
        assertThat(result.getDateAssessed()).isEqualTo("2024-01-05");
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with null values")
    void testConvertToWithNullValues() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocOverall(null);
        source.setLynchOverall(null);
        source.setApsApcOverall(null);

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        // Null strings don't set boolean values, so they remain false (default)
        assertThat(result.getHbocOverall()).isFalse();
        assertThat(result.getLynchOverall()).isFalse();
        assertThat(result.getApsApcOverall()).isFalse();
        assertThat(result.getNccnCriteriaMet()).isFalse();
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with empty criteria lists")
    void testConvertToWithEmptyCriteriaLists() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocOverall("Yes");
        source.setHbocCriteriaMet(null);
        source.setHbocCriteriaMetIds(null);

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
        assertThat(result.getHbocCriteriaMet()).isNull();
        assertThat(result.getHbocCriteriaMetIds()).isNull();
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with only versions")
    void testConvertToWithOnlyVersions() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocVersion("v1.0");
        source.setCrcVersion("v2.0");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getHbocVersion()).isEqualTo("v1.0");
        assertThat(result.getCrcVersion()).isEqualTo("v2.0");
        assertThat(result.getHbocOverall()).isFalse();
        assertThat(result.getLynchOverall()).isFalse();
        assertThat(result.getApsApcOverall()).isFalse();
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with HBOC and Lynch")
    void testConvertToWithHbocAndLynch() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocOverall("Yes");
        source.setLynchOverall("Yes");
        source.setHbocCriteriaMet(new HashSet<>(Arrays.asList("HBOC1")));
        source.setLynchCriteriaMet(new HashSet<>(Arrays.asList("Lynch1")));

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getLynchOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with all three types and criteria")
    void testConvertToWithAllTypesAndCriteria() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocOverall("Yes");
        source.setLynchOverall("Yes");
        source.setApsApcOverall("Yes");
        source.setHbocCriteriaMet(new HashSet<>(Arrays.asList("HBOC1", "HBOC2")));
        source.setHbocCriteriaMetIds(new HashSet<>(Arrays.asList("ID1", "ID2")));
        source.setLynchCriteriaMet(new HashSet<>(Arrays.asList("Lynch1")));
        source.setLynchCriteriaMetIds(new HashSet<>(Arrays.asList("LID1")));
        source.setApsApcCriteriaMet(new HashSet<>(Arrays.asList("APS1")));
        source.setApsApcCriteriaMetIds(new HashSet<>(Arrays.asList("AID1")));

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getLynchOverall()).isTrue();
        assertThat(result.getApsApcOverall()).isTrue();
        assertThat(result.getNccnCriteriaMet()).isTrue();
        assertThat(result.getHbocCriteriaMet()).hasSize(2);
        assertThat(result.getLynchCriteriaMet()).hasSize(1);
        assertThat(result.getApsApcCriteriaMet()).hasSize(1);
    }

    @Test
    @DisplayName("Should convert NCCNCoreResponse to NCCNResult with whitespace strings")
    void testConvertToWithWhitespaceStrings() {
        NCCNCoreResponse source = new NCCNCoreResponse();
        source.setHbocOverall("   ");
        source.setLynchOverall("\t");
        source.setApsApcOverall("\n");

        NCCNResult result = NCCNResultMapper.convertTo(source);

        assertThat(result).isNotNull();
        // StringUtils.isEmpty() from logback only returns true for null/empty, not whitespace
        // So whitespace strings like "   " are NOT empty, and the mapper sets them to TRUE
        // The actual behavior: !StringUtils.isEmpty("   ") = true, so boolean is set to TRUE
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getLynchOverall()).isTrue();
        assertThat(result.getApsApcOverall()).isTrue();
    }
}

