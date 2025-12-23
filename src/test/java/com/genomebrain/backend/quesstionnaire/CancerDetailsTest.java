package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CancerDetails}.
 */
@DisplayName("CancerDetails Unit Tests")
class CancerDetailsTest {

    private CancerDetails cancerDetails;
    private List<QuestionSet> testQuestionSetList;

    @BeforeEach
    void setUp() {
        cancerDetails = new CancerDetails();
        testQuestionSetList = new ArrayList<>();
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionText("Test Question");
        questionSet.setAnswerText("Test Answer");
        testQuestionSetList.add(questionSet);
    }

    @Test
    @DisplayName("Should get and set Breast")
    void testBreast() {
        cancerDetails.setBreast(testQuestionSetList);
        assertThat(cancerDetails.getBreast()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Ovarian")
    void testOvarian() {
        cancerDetails.setOvarian(testQuestionSetList);
        assertThat(cancerDetails.getOvarian()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set FallopianTube")
    void testFallopianTube() {
        cancerDetails.setFallopianTube(testQuestionSetList);
        assertThat(cancerDetails.getFallopianTube()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Prostate")
    void testProstate() {
        cancerDetails.setProstate(testQuestionSetList);
        assertThat(cancerDetails.getProstate()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Colorectal")
    void testColorectal() {
        cancerDetails.setColorectal(testQuestionSetList);
        assertThat(cancerDetails.getColorectal()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Pancreatic")
    void testPancreatic() {
        cancerDetails.setPancreatic(testQuestionSetList);
        assertThat(cancerDetails.getPancreatic()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Uterine")
    void testUterine() {
        cancerDetails.setUterine(testQuestionSetList);
        assertThat(cancerDetails.getUterine()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Stomach")
    void testStomach() {
        cancerDetails.setStomach(testQuestionSetList);
        assertThat(cancerDetails.getStomach()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set SmallIntestine")
    void testSmallIntestine() {
        cancerDetails.setSmallIntestine(testQuestionSetList);
        assertThat(cancerDetails.getSmallIntestine()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set BiliaryTract")
    void testBiliaryTract() {
        cancerDetails.setBiliaryTract(testQuestionSetList);
        assertThat(cancerDetails.getBiliaryTract()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Kidney")
    void testKidney() {
        cancerDetails.setKidney(testQuestionSetList);
        assertThat(cancerDetails.getKidney()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Ureter")
    void testUreter() {
        cancerDetails.setUreter(testQuestionSetList);
        assertThat(cancerDetails.getUreter()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Brain")
    void testBrain() {
        cancerDetails.setBrain(testQuestionSetList);
        assertThat(cancerDetails.getBrain()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set SebaceousAdenoma")
    void testSebaceousAdenoma() {
        cancerDetails.setSebaceousAdenoma(testQuestionSetList);
        assertThat(cancerDetails.getSebaceousAdenoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Melanoma")
    void testMelanoma() {
        cancerDetails.setMelanoma(testQuestionSetList);
        assertThat(cancerDetails.getMelanoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Skin")
    void testSkin() {
        cancerDetails.setSkin(testQuestionSetList);
        assertThat(cancerDetails.getSkin()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Leukemia")
    void testLeukemia() {
        cancerDetails.setLeukemia(testQuestionSetList);
        assertThat(cancerDetails.getLeukemia()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Lymphoma")
    void testLymphoma() {
        cancerDetails.setLymphoma(testQuestionSetList);
        assertThat(cancerDetails.getLymphoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Gallbladder")
    void testGallbladder() {
        cancerDetails.setGallbladder(testQuestionSetList);
        assertThat(cancerDetails.getGallbladder()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Cervical")
    void testCervical() {
        cancerDetails.setCervical(testQuestionSetList);
        assertThat(cancerDetails.getCervical()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Bladder")
    void testBladder() {
        cancerDetails.setBladder(testQuestionSetList);
        assertThat(cancerDetails.getBladder()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Throat")
    void testThroat() {
        cancerDetails.setThroat(testQuestionSetList);
        assertThat(cancerDetails.getThroat()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Thyroid")
    void testThyroid() {
        cancerDetails.setThyroid(testQuestionSetList);
        assertThat(cancerDetails.getThyroid()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Oral")
    void testOral() {
        cancerDetails.setOral(testQuestionSetList);
        assertThat(cancerDetails.getOral()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Liver")
    void testLiver() {
        cancerDetails.setLiver(testQuestionSetList);
        assertThat(cancerDetails.getLiver()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Testicular")
    void testTesticular() {
        cancerDetails.setTesticular(testQuestionSetList);
        assertThat(cancerDetails.getTesticular()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Paraganglioma")
    void testParaganglioma() {
        cancerDetails.setParaganglioma(testQuestionSetList);
        assertThat(cancerDetails.getParaganglioma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Pheochromocytoma")
    void testPheochromocytoma() {
        cancerDetails.setPheochromocytoma(testQuestionSetList);
        assertThat(cancerDetails.getPheochromocytoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Lung")
    void testLung() {
        cancerDetails.setLung(testQuestionSetList);
        assertThat(cancerDetails.getLung()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Sarcoma")
    void testSarcoma() {
        cancerDetails.setSarcoma(testQuestionSetList);
        assertThat(cancerDetails.getSarcoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set NotListed")
    void testNotListed() {
        cancerDetails.setNotListed(testQuestionSetList);
        assertThat(cancerDetails.getNotListed()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        cancerDetails.setBreast(null);
        assertThat(cancerDetails.getBreast()).isNull();
        
        cancerDetails.setOvarian(null);
        assertThat(cancerDetails.getOvarian()).isNull();
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        List<QuestionSet> emptyList = new ArrayList<>();
        cancerDetails.setBreast(emptyList);
        assertThat(cancerDetails.getBreast()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        cancerDetails.setBreast(testQuestionSetList);
        cancerDetails.setOvarian(testQuestionSetList);
        cancerDetails.setColorectal(testQuestionSetList);
        
        String result = cancerDetails.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("CancerDetails");
        assertThat(result).contains("Breast");
        assertThat(result).contains("Ovarian");
        assertThat(result).contains("Colorectal");
    }

    @Test
    @DisplayName("Should return correct toString with all fields")
    void testToStringWithAllFields() {
        cancerDetails.setBreast(testQuestionSetList);
        cancerDetails.setOvarian(testQuestionSetList);
        cancerDetails.setFallopianTube(testQuestionSetList);
        cancerDetails.setProstate(testQuestionSetList);
        cancerDetails.setColorectal(testQuestionSetList);
        cancerDetails.setPancreatic(testQuestionSetList);
        cancerDetails.setUterine(testQuestionSetList);
        cancerDetails.setStomach(testQuestionSetList);
        cancerDetails.setSmallIntestine(testQuestionSetList);
        cancerDetails.setBiliaryTract(testQuestionSetList);
        cancerDetails.setKidney(testQuestionSetList);
        cancerDetails.setUreter(testQuestionSetList);
        cancerDetails.setBrain(testQuestionSetList);
        cancerDetails.setSebaceousAdenoma(testQuestionSetList);
        cancerDetails.setMelanoma(testQuestionSetList);
        cancerDetails.setSkin(testQuestionSetList);
        cancerDetails.setLeukemia(testQuestionSetList);
        cancerDetails.setLymphoma(testQuestionSetList);
        cancerDetails.setGallbladder(testQuestionSetList);
        cancerDetails.setCervical(testQuestionSetList);
        cancerDetails.setBladder(testQuestionSetList);
        cancerDetails.setThroat(testQuestionSetList);
        cancerDetails.setThyroid(testQuestionSetList);
        cancerDetails.setOral(testQuestionSetList);
        cancerDetails.setLiver(testQuestionSetList);
        cancerDetails.setTesticular(testQuestionSetList);
        cancerDetails.setParaganglioma(testQuestionSetList);
        cancerDetails.setPheochromocytoma(testQuestionSetList);
        cancerDetails.setLung(testQuestionSetList);
        cancerDetails.setSarcoma(testQuestionSetList);
        cancerDetails.setNotListed(testQuestionSetList);
        
        String result = cancerDetails.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("FallopianTube");
        assertThat(result).contains("Prostate");
        assertThat(result).contains("Pancreatic");
        assertThat(result).contains("Uterine");
        assertThat(result).contains("Stomach");
        assertThat(result).contains("SmallIntestine");
        assertThat(result).contains("BiliaryTract");
        assertThat(result).contains("Kidney");
        assertThat(result).contains("Ureter");
        assertThat(result).contains("Brain");
        assertThat(result).contains("SebaceousAdenoma");
        assertThat(result).contains("Melanoma");
        assertThat(result).contains("Skin");
        assertThat(result).contains("Leukemia");
        assertThat(result).contains("Lymphoma");
        assertThat(result).contains("Gallbladder");
        assertThat(result).contains("Cervical");
        assertThat(result).contains("Bladder");
        assertThat(result).contains("Throat");
        assertThat(result).contains("Thyroid");
        assertThat(result).contains("Oral");
        assertThat(result).contains("Liver");
        assertThat(result).contains("Testicular");
        assertThat(result).contains("Paraganglioma");
        assertThat(result).contains("Pheochromocytoma");
        assertThat(result).contains("Lung");
        assertThat(result).contains("Sarcoma");
        assertThat(result).contains("NotListed");
    }
}

