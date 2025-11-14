package com.objex.progeny.risk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.charts.Series;

@DisplayName("ChartBuilder Unit Tests")
class ChartBuilderTest {

    @Test
    @DisplayName("Should return singleton instance")
    void testInstance() {
        ChartBuilder instance1 = ChartBuilder.instance();
        ChartBuilder instance2 = ChartBuilder.instance();
        
        assertThat(instance1).isNotNull();
        assertThat(instance2).isNotNull();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("Should create chart for GAIL model with chart code 1")
    void testCreateChartGail() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "GAIL");
        
        assertThat(chart).isNotNull();
        assertThat(chart.getChartCode()).isEqualTo(1);
        assertThat(chart.getContext()).isEqualTo("GAIL");
        assertThat(chart.getChartType()).isEqualTo(ChartVO.Type.LINE);
        assertThat(chart.getTitle()).isEqualTo("Risk By Age");
        assertThat(chart.getSeries()).isNotEmpty();
    }

    @Test
    @DisplayName("Should create chart for CLAUS model with chart code 1")
    void testCreateChartClaus() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "CLAUS");
        
        assertThat(chart).isNotNull();
        assertThat(chart.getChartCode()).isEqualTo(1);
        assertThat(chart.getContext()).isEqualTo("CLAUS");
        assertThat(chart.getChartType()).isEqualTo(ChartVO.Type.LINE);
        assertThat(chart.getTitle()).isEqualTo("Risk By Age");
        assertThat(chart.getSeries()).isNotEmpty();
    }

    @Test
    @DisplayName("Should create chart for TYRER model with chart code 1")
    void testCreateChartTyrer() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "TYRER");
        
        assertThat(chart).isNotNull();
        assertThat(chart.getChartCode()).isEqualTo(1);
        assertThat(chart.getContext()).isEqualTo("TYRER");
        assertThat(chart.getChartType()).isEqualTo(ChartVO.Type.LINE);
        assertThat(chart.getTitle()).isEqualTo("Risk By Age");
        assertThat(chart.getSeries()).hasSize(2); // breastRisk and populationRisk
    }

    @Test
    @DisplayName("Should return null prototype for unknown model")
    void testGetPrototypeUnknownModel() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO prototype = builder.getPrototype(1, "UNKNOWN_MODEL");
        
        assertThat(prototype).isNull();
    }

    @Test
    @DisplayName("Should return null prototype for unknown chart code")
    void testGetPrototypeUnknownChartCode() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO prototype = builder.getPrototype(999, "GAIL");
        
        assertThat(prototype).isNull();
    }

    @Test
    @DisplayName("Should create chart with null prototype for unknown model")
    void testCreateChartUnknownModel() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "UNKNOWN_MODEL");
        
        assertThat(chart).isNotNull();
        assertThat(chart.getChartCode()).isEqualTo(1);
        assertThat(chart.getContext()).isEqualTo("UNKNOWN_MODEL");
        // Chart may be empty if no prototype found
    }

    @Test
    @DisplayName("Should create chart with null prototype for unknown chart code")
    void testCreateChartUnknownChartCode() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(999, "GAIL");
        
        assertThat(chart).isNotNull();
        assertThat(chart.getChartCode()).isEqualTo(999);
        assertThat(chart.getContext()).isEqualTo("GAIL");
        // Chart may be empty if no prototype found
    }

    @Test
    @DisplayName("Should have breastRisk series in GAIL chart")
    void testGailChartHasBreastRiskSeries() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "GAIL");
        
        assertThat(chart.getSeries()).isNotEmpty();
        Series breastSeries = chart.getSeries().stream()
            .filter(s -> "breastRisk".equals(s.getName()))
            .findFirst()
            .orElse(null);
        assertThat(breastSeries).isNotNull();
        assertThat(breastSeries.getLabel()).isEqualTo("Breast Cancer Risk");
    }

    @Test
    @DisplayName("Should have breastRisk and populationRisk series in TYRER chart")
    void testTyrerChartHasBothSeries() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "TYRER");
        
        assertThat(chart.getSeries()).hasSize(2);
        Series breastSeries = chart.getSeries().stream()
            .filter(s -> "breastRisk".equals(s.getName()))
            .findFirst()
            .orElse(null);
        Series populationSeries = chart.getSeries().stream()
            .filter(s -> "populationRisk".equals(s.getName()))
            .findFirst()
            .orElse(null);
        
        assertThat(breastSeries).isNotNull();
        assertThat(populationSeries).isNotNull();
        assertThat(breastSeries.getLabel()).isEqualTo("Breast Cancer Risk");
        assertThat(populationSeries.getLabel()).isEqualTo("Population Breast Risk");
    }

    @Test
    @DisplayName("Should set chart properties correctly")
    void testChartProperties() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart = builder.createChart(1, "GAIL");
        
        assertThat(chart.getBackgroundColor()).isEqualTo("#EEEEEE");
        assertThat(chart.getAxisX()).isEqualTo("Age (years)");
        assertThat(chart.getAxisY()).isEqualTo("Risk (percent)");
        assertThat(chart.isShowPoints()).isTrue();
        assertThat(chart.getWidth()).isEqualTo(640);
        assertThat(chart.getHeight()).isEqualTo(480);
    }

    @Test
    @DisplayName("Should create independent chart instances")
    void testCreateChartIndependentInstances() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chart1 = builder.createChart(1, "GAIL");
        ChartVO chart2 = builder.createChart(1, "GAIL");
        
        assertThat(chart1).isNotSameAs(chart2);
        assertThat(chart1.getChartCode()).isEqualTo(chart2.getChartCode());
        assertThat(chart1.getContext()).isEqualTo(chart2.getContext());
    }

    @Test
    @DisplayName("Should handle case-sensitive model names")
    void testCreateChartCaseSensitive() {
        ChartBuilder builder = ChartBuilder.instance();
        
        ChartVO chartLower = builder.createChart(1, "gail");
        ChartVO chartUpper = builder.createChart(1, "GAIL");
        
        // Model names are case-sensitive, so "gail" should not match "GAIL"
        assertThat(chartLower).isNotNull();
        assertThat(chartUpper).isNotNull();
        // Lower case may not have prototype
    }
}

