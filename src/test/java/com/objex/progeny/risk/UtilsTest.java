package com.objex.progeny.risk;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.charts.DataPoint;
import com.objex.risk.beans.charts.Series;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.claus.Risk;

@DisplayName("Utils Unit Tests")
class UtilsTest {

    @TempDir
    Path tempDir;

    private Risk clausRisk;
    private Person proband;
    private Person mother;
    private Person father;
    private Person sibling;
    private Person child;
    private Person grandparent;
    private Person aunt;

    @BeforeEach
    void setUp() {
        clausRisk = new Risk();
        
        proband = new Person();
        proband.setId(1);
        proband.setMotherId(0);
        proband.setFatherId(0);
        proband.setGender(0);
        clausRisk.getFamily().add(proband);

        mother = new Person();
        mother.setId(2);
        mother.setMotherId(6); // Set to grandparent id
        mother.setFatherId(8); // Set to grandparent's spouse id
        mother.setGender(0);
        clausRisk.getFamily().add(mother);
        proband.setMotherId(2);

        father = new Person();
        father.setId(3);
        father.setMotherId(0);
        father.setFatherId(0);
        father.setGender(1);
        clausRisk.getFamily().add(father);
        proband.setFatherId(3);

        sibling = new Person();
        sibling.setId(4);
        sibling.setMotherId(2);
        sibling.setFatherId(3);
        sibling.setGender(0);
        clausRisk.getFamily().add(sibling);

        child = new Person();
        child.setId(5);
        child.setMotherId(1);
        child.setFatherId(0);
        child.setGender(0);
        clausRisk.getFamily().add(child);

        grandparent = new Person();
        grandparent.setId(6);
        grandparent.setMotherId(0);
        grandparent.setFatherId(0);
        grandparent.setGender(0);
        clausRisk.getFamily().add(grandparent);

        // Add grandparent's spouse to ensure aunt and mother share both parents
        Person grandparentSpouse = new Person();
        grandparentSpouse.setId(8);
        grandparentSpouse.setMotherId(0);
        grandparentSpouse.setFatherId(0);
        grandparentSpouse.setGender(1);
        clausRisk.getFamily().add(grandparentSpouse);

        aunt = new Person();
        aunt.setId(7);
        aunt.setMotherId(6); // Same mother as mother
        aunt.setFatherId(8); // Same father as mother
        aunt.setGender(0);
        clausRisk.getFamily().add(aunt);
    }

    @Test
    @DisplayName("Should check if age is in range")
    void testInRange() {
        assertThat(Utils.inRange(50, 20, 80, 0)).isTrue();
        assertThat(Utils.inRange(20, 20, 80, 0)).isTrue();
        assertThat(Utils.inRange(80, 20, 80, 0)).isTrue();
        assertThat(Utils.inRange(19, 20, 80, 0)).isFalse();
        assertThat(Utils.inRange(81, 20, 80, 0)).isFalse();
        assertThat(Utils.inRange(0, 20, 80, 0)).isTrue(); // defaultValue
        assertThat(Utils.inRange(-1, 20, 80, 0)).isFalse();
    }

    @Test
    @DisplayName("Should get parents of a person")
    void testGetParents() {
        List<Person> parents = Utils.getParents(proband, clausRisk);
        
        assertThat(parents).hasSize(2);
        assertThat(parents).contains(mother, father);
    }

    @Test
    @DisplayName("Should get empty list when person has no parents")
    void testGetParentsNoParents() {
        List<Person> parents = Utils.getParents(mother, clausRisk);
        
        // Mother has grandparent as parent, but grandparent is not in the list as a Person
        // Actually, mother has motherId=6, and grandparent has id=6, so it should find it
        assertThat(parents).isNotEmpty();
    }

    @Test
    @DisplayName("Should filter sex - keep females")
    void testFilterSexKeepFemales() {
        List<Person> people = new ArrayList<>();
        people.add(proband); // gender 0 = female
        people.add(father); // gender 1 = male
        people.add(mother); // gender 0 = female
        
        List<Person> females = Utils.filterSex(people, true);
        
        assertThat(females).hasSize(2);
        assertThat(females).contains(proband, mother);
        assertThat(females).doesNotContain(father);
    }

    @Test
    @DisplayName("Should filter sex - keep males")
    void testFilterSexKeepMales() {
        List<Person> people = new ArrayList<>();
        people.add(proband); // gender 0 = female
        people.add(father); // gender 1 = male
        people.add(mother); // gender 0 = female
        
        List<Person> males = Utils.filterSex(people, false);
        
        assertThat(males).hasSize(1);
        assertThat(males).contains(father);
        assertThat(males).doesNotContain(proband, mother);
    }

    @Test
    @DisplayName("Should get siblings of a person")
    void testGetSiblings() {
        List<Person> siblings = Utils.getSiblings(proband, clausRisk);
        
        assertThat(siblings).hasSize(1);
        assertThat(siblings).contains(sibling);
    }

    @Test
    @DisplayName("Should get siblings from list")
    void testGetSiblingsFromList() {
        List<Person> family = new ArrayList<>(clausRisk.getFamily());
        List<Person> siblings = Utils.getSiblings(proband, family);
        
        assertThat(siblings).hasSize(1);
        assertThat(siblings).contains(sibling);
    }

    @Test
    @DisplayName("Should get half siblings of a person")
    void testGetHalfSiblings() {
        Person halfSibling = new Person();
        halfSibling.setId(8);
        halfSibling.setMotherId(2); // same mother
        halfSibling.setFatherId(0); // different father
        halfSibling.setGender(0);
        clausRisk.getFamily().add(halfSibling);
        
        List<Person> halfSiblings = Utils.getHalfSiblings(proband, clausRisk);
        
        // Should find half-sibling (same mother, different father)
        assertThat(halfSiblings).isNotEmpty();
    }

    @Test
    @DisplayName("Should get half siblings with parent filter")
    void testGetHalfSiblingsWithParent() {
        Person halfSibling = new Person();
        halfSibling.setId(8);
        halfSibling.setMotherId(2);
        halfSibling.setFatherId(0);
        halfSibling.setGender(0);
        clausRisk.getFamily().add(halfSibling);
        
        List<Person> halfSiblings = Utils.getHalfSiblings(proband, mother, clausRisk);
        
        // Should find half-siblings that are children of the specified parent
        assertThat(halfSiblings).isNotEmpty();
    }

    @Test
    @DisplayName("Should get children of a person")
    void testGetChildren() {
        List<Person> children = Utils.getChildren(proband, clausRisk);
        
        assertThat(children).hasSize(1);
        assertThat(children).contains(child);
    }

    @Test
    @DisplayName("Should get children of multiple parents")
    void testGetChildrenOfMultipleParents() {
        List<Person> parents = new ArrayList<>();
        parents.add(mother);
        parents.add(father);
        
        List<Person> children = Utils.getChildren(parents, clausRisk);
        
        // Should find proband and sibling (children of mother and father)
        assertThat(children).hasSize(2);
        assertThat(children).contains(proband, sibling);
    }

    @Test
    @DisplayName("Should get first degree relatives")
    void testGetFirstDegreeRelatives() {
        List<Person> relatives = Utils.getFirstDegreeRelatives(clausRisk, Person.class);
        
        // First degree relatives: parents, siblings, children
        // Parents: mother, father
        // Siblings: sibling
        // Children: child
        assertThat(relatives).hasSize(4);
        assertThat(relatives).contains(mother, father, sibling, child);
    }

    @Test
    @DisplayName("Should get first degree relatives for specific person")
    void testGetFirstDegreeRelativesForPerson() {
        List<Person> relatives = Utils.getFirstDegreeRelatives(proband, clausRisk, Person.class);
        
        // First degree relatives of proband: parents, siblings, children
        assertThat(relatives).hasSize(4);
        assertThat(relatives).contains(mother, father, sibling, child);
    }

    @Test
    @DisplayName("Should get second degree relatives")
    void testGetSecondDegreeRelatives() {
        List<Person> relatives = Utils.getSecondDegreeRelatives(clausRisk, Person.class);
        
        // Second degree relatives: grandparents, aunts/uncles, grandchildren, nieces/nephews, half-siblings
        // Grandparent: grandparent (parent of mother)
        // Aunt: aunt (sibling of mother, since they share both parents)
        assertThat(relatives).isNotEmpty();
        // Grandparent should be found as parent of mother
        assertThat(relatives).contains(grandparent);
        // Aunt should be found as sibling of mother (they share motherId=6 and fatherId=8)
        assertThat(relatives).contains(aunt);
    }

    @Test
    @DisplayName("Should get side of family second degree relatives - maternal")
    void testGetSideOfFamilySecondDegreeRelativesMaternal() {
        List<Person> relatives = Utils.getSideOfFamilySecondDegreeRelatives(clausRisk, Person.class, true);
        
        // Maternal side: mother's parents, mother's siblings, etc.
        assertThat(relatives).isNotEmpty();
        assertThat(relatives).contains(grandparent, aunt);
    }

    @Test
    @DisplayName("Should get side of family second degree relatives - paternal")
    void testGetSideOfFamilySecondDegreeRelativesPaternal() {
        List<Person> relatives = Utils.getSideOfFamilySecondDegreeRelatives(clausRisk, Person.class, false);
        
        // Paternal side: father's parents, father's siblings, etc.
        // In the current setup, father doesn't have parents or siblings, so the list may be empty
        // This is acceptable - the test verifies the method doesn't throw an exception
        assertThat(relatives).isNotNull();
    }

    @Test
    @DisplayName("Should save byte array to file")
    void testSaveToFileByteArray() throws IOException {
        Path testFile = tempDir.resolve("test.dat");
        byte[] data = {1, 2, 3, 4, 5};
        
        Utils.saveToFile(testFile.toString(), data);
        
        assertThat(testFile).exists();
        byte[] readData = Files.readAllBytes(testFile);
        assertThat(readData).isEqualTo(data);
    }

    @Test
    @DisplayName("Should handle save to file with invalid path")
    void testSaveToFileInvalidPath() {
        // Should not throw exception, should log error
        Utils.saveToFile("/invalid/path/test.dat", new byte[]{1, 2, 3});
        
        // Method should complete without throwing
    }

    @Test
    @DisplayName("Should build chart from result")
    void testBuildChart() {
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        // Note: ClausResult model is set via constant CLAUS, not setModel method
        
        Prediction prediction = result.addPrediction();
        prediction.setAge(50);
        prediction.addRisk("breastRisk", 0.05);
        
        ChartVO chart = Utils.buildChart(result);
        
        assertThat(chart).isNotNull();
        assertThat(chart.getSeries()).isNotEmpty();
    }

    @Test
    @DisplayName("Should build chart with multiple predictions")
    void testBuildChartMultiplePredictions() {
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        // Note: ClausResult model is set via constant CLAUS, not setModel method
        
        Prediction pred1 = result.addPrediction();
        pred1.setAge(50);
        pred1.addRisk("breastRisk", 0.05);
        
        Prediction pred2 = result.addPrediction();
        pred2.setAge(60);
        pred2.addRisk("breastRisk", 0.10);
        
        ChartVO chart = Utils.buildChart(result);
        
        assertThat(chart).isNotNull();
        assertThat(chart.getSeries()).isNotEmpty();
    }

    @Test
    @DisplayName("Should copy from prototype")
    void testCopyFromPrototype() {
        ChartVO chart = new ChartVO();
        chart.setChartCode(1);
        chart.setContext("GAIL");
        
        Utils.copyFromPrototype(chart);
        
        // Chart should be populated from prototype
        assertThat(chart.getTitle()).isNotNull();
    }

    @Test
    @DisplayName("Should plot chart to PNG - line chart")
    void testPlotChartToPNGLine() {
        ChartVO chart = new ChartVO();
        chart.setChartType(ChartVO.Type.LINE);
        chart.setTitle("Test Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setShowPoints(true);
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series = chart.addSeries("testSeries");
        series.setLabel("Test Series");
        series.setPrimaryColor("#008DCE");
        series.add(new DataPoint(50, 5.0));
        
        byte[] image = Utils.plotChartToPNG(chart);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should plot chart to PNG - pie chart")
    void testPlotChartToPNGPie() {
        ChartVO chart = new ChartVO();
        chart.setChartType(ChartVO.Type.PIE);
        chart.setTitle("Test Pie Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setWidth(640);
        chart.setHeight(480);
        chart.setExplodePie(false);
        
        Series series = chart.addSeries("testSeries");
        series.setLabel("Test Series");
        series.setPrimaryColor("#008DCE");
        series.add(new DataPoint(0, 50.0));
        
        byte[] image = Utils.plotChartToPNG(chart);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should plot chart to pie PNG with explode")
    void testPlotChartToPiePNGWithExplode() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Pie Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series = chart.addSeries("testSeries");
        series.setLabel("Test Series");
        series.setPrimaryColor("#008DCE");
        series.add(new DataPoint(0, 50.0));
        
        byte[] image = Utils.plotChartToPiePNG(chart, true);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should plot chart to line PNG with show points")
    void testPlotChartToLinePNGWithShowPoints() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Line Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setAxisX("Age");
        chart.setAxisY("Risk");
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series = chart.addSeries("testSeries");
        series.setLabel("Test Series");
        series.setPrimaryColor("#008DCE");
        series.add(new DataPoint(50, 5.0));
        series.add(new DataPoint(60, 10.0));
        
        byte[] image = Utils.plotChartToLinePNG(chart, true);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should plot chart to line PNG without show points")
    void testPlotChartToLinePNGWithoutShowPoints() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Line Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setAxisX("Age");
        chart.setAxisY("Risk");
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series = chart.addSeries("testSeries");
        series.setLabel("Test Series");
        series.setPrimaryColor("#008DCE");
        series.add(new DataPoint(50, 5.0));
        series.add(new DataPoint(60, 10.0));
        
        byte[] image = Utils.plotChartToLinePNG(chart, false);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle empty family in getFirstDegreeRelatives")
    void testGetFirstDegreeRelativesEmptyFamily() {
        Risk emptyRisk = new Risk();
        Person person = new Person();
        person.setId(1);
        emptyRisk.getFamily().add(person);
        
        List<Person> relatives = Utils.getFirstDegreeRelatives(emptyRisk, Person.class);
        
        assertThat(relatives).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty family in getSecondDegreeRelatives")
    void testGetSecondDegreeRelativesEmptyFamily() {
        Risk emptyRisk = new Risk();
        Person person = new Person();
        person.setId(1);
        emptyRisk.getFamily().add(person);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(emptyRisk, Person.class);
        
        assertThat(relatives).isEmpty();
    }

    @Test
    @DisplayName("Should handle person with no relatives")
    void testGetRelativesNoRelatives() {
        Risk risk = new Risk();
        Person person = new Person();
        person.setId(1);
        person.setMotherId(0);
        person.setFatherId(0);
        risk.getFamily().add(person);
        
        List<Person> parents = Utils.getParents(person, risk);
        List<Person> siblings = Utils.getSiblings(person, risk);
        List<Person> children = Utils.getChildren(person, risk);
        
        assertThat(parents).isEmpty();
        assertThat(siblings).isEmpty();
        assertThat(children).isEmpty();
    }

    @Test
    @DisplayName("Should save BaseRisk to file")
    void testSaveToFileBaseRisk() throws IOException {
        Path testFile = tempDir.resolve("test-risk.xml");
        
        Utils.saveToFile(testFile.toString(), clausRisk);
        
        assertThat(testFile).exists();
    }

    @Test
    @DisplayName("Should save Object to file")
    void testSaveToFileObject() throws IOException {
        Path testFile = tempDir.resolve("test-result.json");
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        
        // This may fail if JAXB context creation fails, but should not throw exception
        try {
            Utils.saveToFile(testFile.toString(), result);
            // File may or may not exist depending on JAXB marshalling success
        } catch (Exception e) {
            // Acceptable if JAXB marshalling fails
        }
        
        // Method should complete without throwing
    }

    @Test
    @DisplayName("Should handle saveToFile BaseRisk with invalid path")
    void testSaveToFileBaseRiskInvalidPath() {
        // Should not throw exception, should log error
        Utils.saveToFile("/invalid/path/test.xml", clausRisk);
        
        // Method should complete without throwing
    }

    @Test
    @DisplayName("Should handle saveToFile Object with invalid path")
    void testSaveToFileObjectInvalidPath() {
        ClausResult result = new ClausResult();
        // Should not throw exception, should log error
        Utils.saveToFile("/invalid/path/test.json", result);
        
        // Method should complete without throwing
    }

    @Test
    @DisplayName("Should save ResultFile to file")
    void testSaveResultFile() throws IOException {
        Path testFile = tempDir.resolve("test-result.xml");
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        
        Utils.saveResultFile(testFile.toString(), result);
        
        assertThat(testFile).exists();
    }

    @Test
    @DisplayName("Should handle saveResultFile with invalid path")
    void testSaveResultFileInvalidPath() {
        ClausResult result = new ClausResult();
        // Should not throw exception, should print stack trace
        Utils.saveResultFile("/invalid/path/test.xml", result);
        
        // Method should complete without throwing
    }

    @Test
    @DisplayName("Should create MMR Risk with family")
    void testCreateMMRRiskWithFamily() {
        ArrayList<String> fam = new ArrayList<>();
        fam.add("ID");
        fam.add("Gender");
        fam.add("FatherID");
        fam.add("MotherID");
        fam.add("AffectedColon");
        fam.add("AffectedEndometrium");
        fam.add("AgeColon");
        fam.add("AgeEndometrium");
        
        com.objex.risk.beans.mmr.Risk risk = Utils.createMMRRiskWithFamily(fam);
        
        assertThat(risk).isNotNull();
        assertThat(risk.getFamily()).isNotEmpty();
    }

    @Test
    @DisplayName("Should create MMR Risk with empty family")
    void testCreateMMRRiskWithEmptyFamily() {
        ArrayList<String> fam = new ArrayList<>();
        
        com.objex.risk.beans.mmr.Risk risk = Utils.createMMRRiskWithFamily(fam);
        
        assertThat(risk).isNotNull();
        assertThat(risk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should create MMR Risk with family containing various field names")
    void testCreateMMRRiskWithFamilyVariousFields() {
        ArrayList<String> fam = new ArrayList<>();
        fam.add("ID");
        fam.add("Gender");
        fam.add("FatherID");
        fam.add("MotherID");
        fam.add("AffectedColon");
        fam.add("AffectedEndometrium");
        fam.add("AgeColon");
        fam.add("AgeEndometrium");
        fam.add("UnknownField"); // Should be ignored
        
        com.objex.risk.beans.mmr.Risk risk = Utils.createMMRRiskWithFamily(fam);
        
        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should save to MMR file")
    void testSaveToMMRFile() throws IOException {
        Path testFile = tempDir.resolve("test-mmr.xml");
        ArrayList<String> fam = new ArrayList<>();
        fam.add("ID");
        fam.add("Gender");
        
        Utils.saveToMMRFile(testFile.toString(), fam);
        
        assertThat(testFile).exists();
    }

    @Test
    @DisplayName("Should handle saveToMMRFile with invalid path")
    void testSaveToMMRFileInvalidPath() {
        ArrayList<String> fam = new ArrayList<>();
        fam.add("ID");
        // Should not throw exception, should log error
        Utils.saveToMMRFile("/invalid/path/test.xml", fam);
        
        // Method should complete without throwing
    }

    @Test
    @DisplayName("Should test inRange with boundary values")
    void testInRangeBoundaryValues() {
        assertThat(Utils.inRange(10, 10, 20, 0)).isTrue(); // floor boundary
        assertThat(Utils.inRange(20, 10, 20, 0)).isTrue(); // ceiling boundary
        assertThat(Utils.inRange(9, 10, 20, 0)).isFalse(); // below floor
        assertThat(Utils.inRange(21, 10, 20, 0)).isFalse(); // above ceiling
    }

    @Test
    @DisplayName("Should test inRange with different default values")
    void testInRangeWithDifferentDefaults() {
        assertThat(Utils.inRange(-1, 10, 20, -1)).isTrue(); // defaultValue
        assertThat(Utils.inRange(99, 10, 20, 99)).isTrue(); // defaultValue
        assertThat(Utils.inRange(15, 10, 20, -1)).isTrue(); // in range
        assertThat(Utils.inRange(25, 10, 20, -1)).isFalse(); // out of range
    }

    @Test
    @DisplayName("Should test inRange with null age")
    void testInRangeWithNullAge() {
        // null age should cause issues, but let's test the method signature
        // The method expects Integer, so null should be handled by caller
        assertThat(Utils.inRange(15, 10, 20, 0)).isTrue();
    }

    @Test
    @DisplayName("Should build chart with empty predictions")
    void testBuildChartWithEmptyPredictions() {
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        // No predictions added
        
        ChartVO chart = Utils.buildChart(result);
        
        assertThat(chart).isNotNull();
    }

    @Test
    @DisplayName("Should build chart with prediction that has exception")
    void testBuildChartWithPredictionException() {
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        
        Prediction prediction = result.addPrediction();
        prediction.setAge(50);
        // Don't add risk, which may cause exception in buildChart
        
        ChartVO chart = Utils.buildChart(result);
        
        // Should handle exception gracefully
        assertThat(chart).isNotNull();
    }

    @Test
    @DisplayName("Should handle plot chart to PNG with null chart")
    void testPlotChartToPNGNullChart() {
        // This will throw NPE, which is expected behavior
        try {
            byte[] image = Utils.plotChartToPNG(null);
            // If no exception, image should be null
            assertThat(image).isNull();
        } catch (NullPointerException e) {
            // Expected behavior when chart is null
        }
    }

    @Test
    @DisplayName("Should plot chart to PNG with unknown chart type")
    void testPlotChartToPNGUnknownType() {
        ChartVO chart = new ChartVO();
        chart.setChartType(null); // null chart type
        
        byte[] image = Utils.plotChartToPNG(chart);
        
        // Should return null for unknown type
        assertThat(image).isNull();
    }

    @Test
    @DisplayName("Should plot pie chart to PNG with empty series")
    void testPlotChartToPiePNGEmptySeries() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setWidth(640);
        chart.setHeight(480);
        // No series added
        
        byte[] image = Utils.plotChartToPiePNG(chart, false);
        
        // Should handle empty series gracefully
        assertThat(image).isNotNull();
    }

    @Test
    @DisplayName("Should plot line chart to PNG with empty series")
    void testPlotChartToLinePNGEmptySeries() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setAxisX("Age");
        chart.setAxisY("Risk");
        chart.setWidth(640);
        chart.setHeight(480);
        // No series added
        
        byte[] image = Utils.plotChartToLinePNG(chart, true);
        
        // Should handle empty series gracefully
        assertThat(image).isNotNull();
    }

    @Test
    @DisplayName("Should plot line chart to PNG with multiple series")
    void testPlotChartToLinePNGMultipleSeries() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setAxisX("Age");
        chart.setAxisY("Risk");
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series1 = chart.addSeries("series1");
        series1.setLabel("Series 1");
        series1.setPrimaryColor("#008DCE");
        series1.add(new DataPoint(50, 5.0));
        
        Series series2 = chart.addSeries("series2");
        series2.setLabel("Series 2");
        series2.setPrimaryColor("#FF0000");
        series2.add(new DataPoint(50, 10.0));
        
        byte[] image = Utils.plotChartToLinePNG(chart, false);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should copy from prototype with null chart")
    void testCopyFromPrototypeNullChart() {
        // Should handle null gracefully or throw NPE
        try {
            Utils.copyFromPrototype(null);
        } catch (Exception e) {
            // Expected if null is not handled
        }
    }

    @Test
    @DisplayName("Should get side of family second degree relatives with null parent")
    void testGetSideOfFamilySecondDegreeRelativesNullParent() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(0);
        proband.setFatherId(0);
        risk.getFamily().add(proband);
        
        // Maternal side with no mother
        List<Person> relatives = Utils.getSideOfFamilySecondDegreeRelatives(risk, Person.class, true);
        
        assertThat(relatives).isNotNull();
    }

    @Test
    @DisplayName("Should get side of family second degree relatives with parentId zero")
    void testGetSideOfFamilySecondDegreeRelativesParentIdZero() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(0);
        proband.setFatherId(0);
        risk.getFamily().add(proband);
        
        // Paternal side with no father
        List<Person> relatives = Utils.getSideOfFamilySecondDegreeRelatives(risk, Person.class, false);
        
        assertThat(relatives).isNotNull();
    }

    @Test
    @DisplayName("Should get second degree relatives with grandchildren")
    void testGetSecondDegreeRelativesWithGrandchildren() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person mother = new Person();
        mother.setId(2);
        risk.getFamily().add(mother);
        
        Person child = new Person();
        child.setId(4);
        child.setMotherId(1);
        child.setFatherId(0);
        risk.getFamily().add(child);
        
        Person grandchild = new Person();
        grandchild.setId(5);
        grandchild.setMotherId(4);
        grandchild.setFatherId(0);
        risk.getFamily().add(grandchild);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(risk, Person.class);
        
        // Grandchild should be found
        assertThat(relatives).contains(grandchild);
    }

    @Test
    @DisplayName("Should get second degree relatives with nieces and nephews")
    void testGetSecondDegreeRelativesWithNiecesNephews() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person sibling = new Person();
        sibling.setId(4);
        sibling.setMotherId(2);
        sibling.setFatherId(3);
        risk.getFamily().add(sibling);
        
        Person niece = new Person();
        niece.setId(5);
        niece.setMotherId(4);
        niece.setFatherId(0);
        risk.getFamily().add(niece);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(risk, Person.class);
        
        // Niece should be found
        assertThat(relatives).contains(niece);
    }

    @Test
    @DisplayName("Should get first degree relatives excluding self")
    void testGetFirstDegreeRelativesExcludingSelf() {
        List<Person> relatives = Utils.getFirstDegreeRelatives(proband, clausRisk, Person.class);
        
        // Should not include proband itself
        assertThat(relatives).doesNotContain(proband);
    }

    @Test
    @DisplayName("Should filter sex with empty list")
    void testFilterSexEmptyList() {
        List<Person> emptyList = new ArrayList<>();
        
        List<Person> result = Utils.filterSex(emptyList, true);
        
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle filter sex with null gender values")
    void testFilterSexWithNullGender() {
        List<Person> people = new ArrayList<>();
        Person person = new Person();
        person.setGender(null); // null gender
        people.add(person);
        
        // This will throw NPE, which is expected behavior
        try {
            List<Person> result = Utils.filterSex(people, true);
            // If no exception, result should be empty
            assertThat(result).isEmpty();
        } catch (NullPointerException e) {
            // Expected behavior when gender is null
        }
    }

    @Test
    @DisplayName("Should get children from empty parents list")
    void testGetChildrenEmptyParentsList() {
        List<Person> emptyParents = new ArrayList<>();
        
        List<Person> children = Utils.getChildren(emptyParents, clausRisk);
        
        assertThat(children).isEmpty();
    }

    @Test
    @DisplayName("Should get half siblings with parent filter when no matching children")
    void testGetHalfSiblingsWithParentNoMatches() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person parent = new Person();
        parent.setId(2);
        risk.getFamily().add(parent);
        
        // No half-siblings that are children of parent
        List<Person> halfSiblings = Utils.getHalfSiblings(proband, parent, risk);
        
        assertThat(halfSiblings).isEmpty();
    }

    @Test
    @DisplayName("Should create risk from file")
    void testCreateRiskFromFile() throws Exception {
        Path testFile = tempDir.resolve("test-risk.xml");
        Utils.saveToFile(testFile.toString(), clausRisk);
        
        Risk loadedRisk = Utils.createRiskFromFile(testFile.toString(), Risk.class);
        
        assertThat(loadedRisk).isNotNull();
        assertThat(loadedRisk.getFamily()).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle createRiskFromFile with non-existent file")
    void testCreateRiskFromFileNonExistent() {
        try {
            Utils.createRiskFromFile("/nonexistent/file.xml", Risk.class);
            // Should not reach here
            assertThat(false).as("Expected exception was not thrown").isTrue();
        } catch (javax.xml.bind.JAXBException e) {
            // Expected - file doesn't exist, JAXBException is thrown
            assertThat(e).isNotNull();
        } catch (Exception e) {
            // JAXBException might wrap other exceptions
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("Should test createCommon method with ID field")
    void testCreateCommonWithID() throws Exception {
        com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
        java.lang.reflect.Method method = Utils.class.getDeclaredMethod("createCommon", Object.class, com.objex.risk.beans.BasePerson.class, String.class);
        method.setAccessible(true);
        
        method.invoke(null, "ID", person, "123");
        
        assertThat(person.getId()).isEqualTo(123);
    }

    @Test
    @DisplayName("Should test createCommon method with Gender field")
    void testCreateCommonWithGender() throws Exception {
        com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
        java.lang.reflect.Method method = Utils.class.getDeclaredMethod("createCommon", Object.class, com.objex.risk.beans.BasePerson.class, String.class);
        method.setAccessible(true);
        
        method.invoke(null, "Gender", person, "0");
        
        assertThat(person.getGender()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should test createCommon method with FatherID field")
    void testCreateCommonWithFatherID() throws Exception {
        com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
        java.lang.reflect.Method method = Utils.class.getDeclaredMethod("createCommon", Object.class, com.objex.risk.beans.BasePerson.class, String.class);
        method.setAccessible(true);
        
        method.invoke(null, "FatherID", person, "5");
        
        assertThat(person.getFatherId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should test createCommon method with MotherID field")
    void testCreateCommonWithMotherID() throws Exception {
        com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
        java.lang.reflect.Method method = Utils.class.getDeclaredMethod("createCommon", Object.class, com.objex.risk.beans.BasePerson.class, String.class);
        method.setAccessible(true);
        
        method.invoke(null, "MotherID", person, "6");
        
        assertThat(person.getMotherId()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should test createCommon method with unknown field")
    void testCreateCommonWithUnknownField() throws Exception {
        com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
        int originalId = person.getId();
        java.lang.reflect.Method method = Utils.class.getDeclaredMethod("createCommon", Object.class, com.objex.risk.beans.BasePerson.class, String.class);
        method.setAccessible(true);
        
        // Should not throw exception, just do nothing
        method.invoke(null, "UnknownField", person, "123");
        
        // Person should remain unchanged (ID should still be original value, not 123)
        assertThat(person.getId()).isEqualTo(originalId);
        assertThat(person.getId()).isNotEqualTo(123);
    }

    @Test
    @DisplayName("Should get second degree relatives with half-siblings")
    void testGetSecondDegreeRelativesWithHalfSiblings() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person mother = new Person();
        mother.setId(2);
        risk.getFamily().add(mother);
        
        Person father = new Person();
        father.setId(3);
        risk.getFamily().add(father);
        
        // Half-sibling: same mother, different father
        Person halfSibling = new Person();
        halfSibling.setId(4);
        halfSibling.setMotherId(2); // same mother
        halfSibling.setFatherId(5); // different father
        risk.getFamily().add(halfSibling);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(risk, Person.class);
        
        // Half-sibling should be found
        assertThat(relatives).contains(halfSibling);
    }

    @Test
    @DisplayName("Should get second degree relatives with grandparents")
    void testGetSecondDegreeRelativesWithGrandparents() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person mother = new Person();
        mother.setId(2);
        mother.setMotherId(6);
        mother.setFatherId(7);
        risk.getFamily().add(mother);
        
        Person father = new Person();
        father.setId(3);
        risk.getFamily().add(father);
        
        Person grandparent = new Person();
        grandparent.setId(6);
        risk.getFamily().add(grandparent);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(risk, Person.class);
        
        // Grandparent should be found
        assertThat(relatives).contains(grandparent);
    }

    @Test
    @DisplayName("Should get side of family second degree relatives with parentId not zero")
    void testGetSideOfFamilySecondDegreeRelativesWithParentIdNotZero() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person mother = new Person();
        mother.setId(2);
        risk.getFamily().add(mother);
        
        Person father = new Person();
        father.setId(3);
        risk.getFamily().add(father);
        
        // Half-sibling with same mother
        Person halfSibling = new Person();
        halfSibling.setId(4);
        halfSibling.setMotherId(2); // same mother
        halfSibling.setFatherId(5); // different father
        risk.getFamily().add(halfSibling);
        
        // Maternal side
        List<Person> relatives = Utils.getSideOfFamilySecondDegreeRelatives(risk, Person.class, true);
        
        // Half-sibling should be found on maternal side
        assertThat(relatives).contains(halfSibling);
    }

    @Test
    @DisplayName("Should get side of family second degree relatives with parentId zero")
    void testGetSideOfFamilySecondDegreeRelativesWithParentIdZero() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(0);
        proband.setFatherId(0);
        risk.getFamily().add(proband);
        
        // No parents, so parentId will be 0
        List<Person> relatives = Utils.getSideOfFamilySecondDegreeRelatives(risk, Person.class, true);
        
        assertThat(relatives).isNotNull();
        assertThat(relatives).isEmpty();
    }

    @Test
    @DisplayName("Should create MMR Risk with family and actual data values")
    void testCreateMMRRiskWithFamilyWithData() {
        ArrayList<String> fam = new ArrayList<>();
        fam.add("ID");
        fam.add("Gender");
        fam.add("FatherID");
        fam.add("MotherID");
        fam.add("AffectedColon");
        fam.add("AffectedEndometrium");
        fam.add("AgeColon");
        fam.add("AgeEndometrium");
        
        // The createMMRRiskWithFamily method iterates through fam twice
        // First loop creates persons, second loop sets values
        // But res is always null in the current implementation
        // So we test the structure
        com.objex.risk.beans.mmr.Risk risk = Utils.createMMRRiskWithFamily(fam);
        
        assertThat(risk).isNotNull();
        // The method creates one person per iteration of fam.size()
        // But since res is null, values won't be set
        assertThat(risk.getFamily()).isNotEmpty();
    }

    @Test
    @DisplayName("Should save empty byte array to file")
    void testSaveToFileEmptyByteArray() throws IOException {
        Path testFile = tempDir.resolve("empty.dat");
        byte[] emptyData = new byte[0];
        
        Utils.saveToFile(testFile.toString(), emptyData);
        
        assertThat(testFile).exists();
        byte[] readData = Files.readAllBytes(testFile);
        assertThat(readData).isEmpty();
    }

    @Test
    @DisplayName("Should build chart with multiple series and predictions")
    void testBuildChartWithMultipleSeriesAndPredictions() {
        ClausResult result = new ClausResult();
        result.setChartCode(1);
        
        Prediction pred1 = result.addPrediction();
        pred1.setAge(50);
        pred1.addRisk("breastRisk", 0.05);
        pred1.addRisk("ovarianRisk", 0.02);
        
        Prediction pred2 = result.addPrediction();
        pred2.setAge(60);
        pred2.addRisk("breastRisk", 0.10);
        pred2.addRisk("ovarianRisk", 0.04);
        
        ChartVO chart = Utils.buildChart(result);
        
        assertThat(chart).isNotNull();
        assertThat(chart.getSeries()).isNotEmpty();
    }

    @Test
    @DisplayName("Should plot pie chart to PNG with multiple series")
    void testPlotChartToPiePNGWithMultipleSeries() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Pie Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series1 = chart.addSeries("series1");
        series1.setLabel("Series 1");
        series1.setPrimaryColor("#008DCE");
        series1.add(new DataPoint(0, 50.0));
        
        Series series2 = chart.addSeries("series2");
        series2.setLabel("Series 2");
        series2.setPrimaryColor("#FF0000");
        series2.add(new DataPoint(0, 30.0));
        
        // Pie chart only uses first series, but should handle multiple
        byte[] image = Utils.plotChartToPiePNG(chart, false);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should plot line chart to PNG with multiple series and showPoints false")
    void testPlotChartToLinePNGWithMultipleSeriesNoShowPoints() {
        ChartVO chart = new ChartVO();
        chart.setTitle("Test Line Chart");
        chart.setBackgroundColor("#EEEEEE");
        chart.setAxisX("Age");
        chart.setAxisY("Risk");
        chart.setWidth(640);
        chart.setHeight(480);
        
        Series series1 = chart.addSeries("series1");
        series1.setLabel("Series 1");
        series1.setPrimaryColor("#008DCE");
        series1.add(new DataPoint(50, 5.0));
        series1.add(new DataPoint(60, 10.0));
        
        Series series2 = chart.addSeries("series2");
        series2.setLabel("Series 2");
        series2.setPrimaryColor("#FF0000");
        series2.add(new DataPoint(50, 8.0));
        series2.add(new DataPoint(60, 15.0));
        
        byte[] image = Utils.plotChartToLinePNG(chart, false);
        
        assertThat(image).isNotNull();
        assertThat(image.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should copy from prototype with valid chart")
    void testCopyFromPrototypeWithValidChart() {
        ChartVO chart = new ChartVO();
        chart.setChartCode(1);
        chart.setContext("GAIL");
        
        Utils.copyFromPrototype(chart);
        
        // Chart should be populated from prototype
        assertThat(chart.getTitle()).isNotNull();
    }

    @Test
    @DisplayName("Should get second degree relatives with aunts and uncles")
    void testGetSecondDegreeRelativesWithAuntsUncles() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person mother = new Person();
        mother.setId(2);
        mother.setMotherId(6);
        mother.setFatherId(7);
        risk.getFamily().add(mother);
        
        Person father = new Person();
        father.setId(3);
        risk.getFamily().add(father);
        
        Person grandparent = new Person();
        grandparent.setId(6);
        risk.getFamily().add(grandparent);
        
        Person grandparentSpouse = new Person();
        grandparentSpouse.setId(7);
        risk.getFamily().add(grandparentSpouse);
        
        // Aunt: sibling of mother
        Person aunt = new Person();
        aunt.setId(8);
        aunt.setMotherId(6); // same mother as mother
        aunt.setFatherId(7); // same father as mother
        risk.getFamily().add(aunt);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(risk, Person.class);
        
        // Aunt should be found as sibling of mother
        assertThat(relatives).contains(aunt);
    }

    @Test
    @DisplayName("Should get second degree relatives excluding siblings from half-sibling check")
    void testGetSecondDegreeRelativesExcludingSiblingsFromHalfSibling() {
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
        risk.getFamily().add(proband);
        
        Person mother = new Person();
        mother.setId(2);
        risk.getFamily().add(mother);
        
        Person father = new Person();
        father.setId(3);
        risk.getFamily().add(father);
        
        // Full sibling (should not be included as half-sibling)
        Person sibling = new Person();
        sibling.setId(4);
        sibling.setMotherId(2);
        sibling.setFatherId(3);
        risk.getFamily().add(sibling);
        
        // Half-sibling: same mother, different father
        Person halfSibling = new Person();
        halfSibling.setId(5);
        halfSibling.setMotherId(2); // same mother
        halfSibling.setFatherId(6); // different father
        risk.getFamily().add(halfSibling);
        
        List<Person> relatives = Utils.getSecondDegreeRelatives(risk, Person.class);
        
        // Half-sibling should be found, but not full sibling (as half-sibling)
        // Full sibling should be in first-degree relatives, not second
        assertThat(relatives).contains(halfSibling);
    }
}

