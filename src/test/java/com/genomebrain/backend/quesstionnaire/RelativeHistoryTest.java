package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RelativeHistory}.
 */
@DisplayName("RelativeHistory Unit Tests")
class RelativeHistoryTest {

    private RelativeHistory relativeHistory;
    private List<QuestionSet> testQuestionSetList;

    @BeforeEach
    void setUp() {
        relativeHistory = new RelativeHistory();
        testQuestionSetList = new ArrayList<>();
        QuestionSet questionSet = new QuestionSet();
        questionSet.setQuestionText("Test Question");
        questionSet.setAnswerText("Test Answer");
        testQuestionSetList.add(questionSet);
    }

    @Test
    @DisplayName("Should get and set basicQuestions")
    void testBasicQuestions() {
        relativeHistory.setBasicQuestions(testQuestionSetList);
        assertThat(relativeHistory.getBasicQuestions()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Breast")
    void testBreast() {
        relativeHistory.setBreast(testQuestionSetList);
        assertThat(relativeHistory.getBreast()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Ovarian")
    void testOvarian() {
        relativeHistory.setOvarian(testQuestionSetList);
        assertThat(relativeHistory.getOvarian()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set FallopianTube")
    void testFallopianTube() {
        relativeHistory.setFallopianTube(testQuestionSetList);
        assertThat(relativeHistory.getFallopianTube()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Prostate")
    void testProstate() {
        relativeHistory.setProstate(testQuestionSetList);
        assertThat(relativeHistory.getProstate()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Colorectal")
    void testColorectal() {
        relativeHistory.setColorectal(testQuestionSetList);
        assertThat(relativeHistory.getColorectal()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Pancreatic")
    void testPancreatic() {
        relativeHistory.setPancreatic(testQuestionSetList);
        assertThat(relativeHistory.getPancreatic()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Uterine")
    void testUterine() {
        relativeHistory.setUterine(testQuestionSetList);
        assertThat(relativeHistory.getUterine()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Stomach")
    void testStomach() {
        relativeHistory.setStomach(testQuestionSetList);
        assertThat(relativeHistory.getStomach()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set SmallIntestine")
    void testSmallIntestine() {
        relativeHistory.setSmallIntestine(testQuestionSetList);
        assertThat(relativeHistory.getSmallIntestine()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set BiliaryTract")
    void testBiliaryTract() {
        relativeHistory.setBiliaryTract(testQuestionSetList);
        assertThat(relativeHistory.getBiliaryTract()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Kidney")
    void testKidney() {
        relativeHistory.setKidney(testQuestionSetList);
        assertThat(relativeHistory.getKidney()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Ureter")
    void testUreter() {
        relativeHistory.setUreter(testQuestionSetList);
        assertThat(relativeHistory.getUreter()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Brain")
    void testBrain() {
        relativeHistory.setBrain(testQuestionSetList);
        assertThat(relativeHistory.getBrain()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set SebaceousAdenoma")
    void testSebaceousAdenoma() {
        relativeHistory.setSebaceousAdenoma(testQuestionSetList);
        assertThat(relativeHistory.getSebaceousAdenoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Melanoma")
    void testMelanoma() {
        relativeHistory.setMelanoma(testQuestionSetList);
        assertThat(relativeHistory.getMelanoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Skin")
    void testSkin() {
        relativeHistory.setSkin(testQuestionSetList);
        assertThat(relativeHistory.getSkin()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Leukemia")
    void testLeukemia() {
        relativeHistory.setLeukemia(testQuestionSetList);
        assertThat(relativeHistory.getLeukemia()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Lymphoma")
    void testLymphoma() {
        relativeHistory.setLymphoma(testQuestionSetList);
        assertThat(relativeHistory.getLymphoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Gallbladder")
    void testGallbladder() {
        relativeHistory.setGallbladder(testQuestionSetList);
        assertThat(relativeHistory.getGallbladder()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Cervical")
    void testCervical() {
        relativeHistory.setCervical(testQuestionSetList);
        assertThat(relativeHistory.getCervical()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Bladder")
    void testBladder() {
        relativeHistory.setBladder(testQuestionSetList);
        assertThat(relativeHistory.getBladder()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Throat")
    void testThroat() {
        relativeHistory.setThroat(testQuestionSetList);
        assertThat(relativeHistory.getThroat()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Thyroid")
    void testThyroid() {
        relativeHistory.setThyroid(testQuestionSetList);
        assertThat(relativeHistory.getThyroid()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Oral")
    void testOral() {
        relativeHistory.setOral(testQuestionSetList);
        assertThat(relativeHistory.getOral()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Liver")
    void testLiver() {
        relativeHistory.setLiver(testQuestionSetList);
        assertThat(relativeHistory.getLiver()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Testicular")
    void testTesticular() {
        relativeHistory.setTesticular(testQuestionSetList);
        assertThat(relativeHistory.getTesticular()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Paraganglioma")
    void testParaganglioma() {
        relativeHistory.setParaganglioma(testQuestionSetList);
        assertThat(relativeHistory.getParaganglioma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Pheochromocytoma")
    void testPheochromocytoma() {
        relativeHistory.setPheochromocytoma(testQuestionSetList);
        assertThat(relativeHistory.getPheochromocytoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Lung")
    void testLung() {
        relativeHistory.setLung(testQuestionSetList);
        assertThat(relativeHistory.getLung()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set Sarcoma")
    void testSarcoma() {
        relativeHistory.setSarcoma(testQuestionSetList);
        assertThat(relativeHistory.getSarcoma()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set NotListed")
    void testNotListed() {
        relativeHistory.setNotListed(testQuestionSetList);
        assertThat(relativeHistory.getNotListed()).isEqualTo(testQuestionSetList);
    }

    @Test
    @DisplayName("Should get and set relativeDetail")
    void testRelativeDetail() {
        RelativeDetail detail = new RelativeDetail();
        detail.setRelativeID("REL001");
        detail.setRelativeType("Mother");
        relativeHistory.setRelativeDetail(detail);
        assertThat(relativeHistory.getRelativeDetail()).isEqualTo(detail);
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        relativeHistory.setBasicQuestions(null);
        assertThat(relativeHistory.getBasicQuestions()).isNull();
        
        relativeHistory.setBreast(null);
        assertThat(relativeHistory.getBreast()).isNull();
        
        relativeHistory.setRelativeDetail(null);
        assertThat(relativeHistory.getRelativeDetail()).isNull();
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        List<QuestionSet> emptyList = new ArrayList<>();
        relativeHistory.setBasicQuestions(emptyList);
        assertThat(relativeHistory.getBasicQuestions()).isEmpty();
        
        relativeHistory.setBreast(emptyList);
        assertThat(relativeHistory.getBreast()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        relativeHistory.setBasicQuestions(testQuestionSetList);
        relativeHistory.setBreast(testQuestionSetList);
        relativeHistory.setOvarian(testQuestionSetList);
        
        RelativeDetail detail = new RelativeDetail();
        detail.setRelativeID("REL001");
        relativeHistory.setRelativeDetail(detail);
        
        String result = relativeHistory.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("RelativeHistory");
        assertThat(result).contains("basicQuestions");
        assertThat(result).contains("Breast");
        assertThat(result).contains("Ovarian");
        assertThat(result).contains("relativeDetail");
    }

    @Test
    @DisplayName("Should return correct toString with all fields")
    void testToStringWithAllFields() {
        relativeHistory.setBasicQuestions(testQuestionSetList);
        relativeHistory.setBreast(testQuestionSetList);
        relativeHistory.setOvarian(testQuestionSetList);
        relativeHistory.setFallopianTube(testQuestionSetList);
        relativeHistory.setProstate(testQuestionSetList);
        relativeHistory.setColorectal(testQuestionSetList);
        relativeHistory.setPancreatic(testQuestionSetList);
        relativeHistory.setUterine(testQuestionSetList);
        relativeHistory.setStomach(testQuestionSetList);
        relativeHistory.setSmallIntestine(testQuestionSetList);
        relativeHistory.setBiliaryTract(testQuestionSetList);
        relativeHistory.setKidney(testQuestionSetList);
        relativeHistory.setUreter(testQuestionSetList);
        relativeHistory.setBrain(testQuestionSetList);
        relativeHistory.setSebaceousAdenoma(testQuestionSetList);
        relativeHistory.setMelanoma(testQuestionSetList);
        relativeHistory.setSkin(testQuestionSetList);
        relativeHistory.setLeukemia(testQuestionSetList);
        relativeHistory.setLymphoma(testQuestionSetList);
        relativeHistory.setGallbladder(testQuestionSetList);
        relativeHistory.setCervical(testQuestionSetList);
        relativeHistory.setBladder(testQuestionSetList);
        relativeHistory.setThroat(testQuestionSetList);
        relativeHistory.setThyroid(testQuestionSetList);
        relativeHistory.setOral(testQuestionSetList);
        relativeHistory.setLiver(testQuestionSetList);
        relativeHistory.setTesticular(testQuestionSetList);
        relativeHistory.setParaganglioma(testQuestionSetList);
        relativeHistory.setPheochromocytoma(testQuestionSetList);
        relativeHistory.setLung(testQuestionSetList);
        relativeHistory.setSarcoma(testQuestionSetList);
        relativeHistory.setNotListed(testQuestionSetList);
        
        String result = relativeHistory.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("FallopianTube");
        assertThat(result).contains("Prostate");
        assertThat(result).contains("Colorectal");
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

    @Test
    @DisplayName("Should handle multiple question sets")
    void testMultipleQuestionSets() {
        List<QuestionSet> multipleSets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            QuestionSet qs = new QuestionSet();
            qs.setQuestionText("Question " + i);
            qs.setAnswerText("Answer " + i);
            multipleSets.add(qs);
        }
        
        relativeHistory.setBasicQuestions(multipleSets);
        assertThat(relativeHistory.getBasicQuestions()).hasSize(5);
        assertThat(relativeHistory.getBasicQuestions().get(0).getQuestionText()).isEqualTo("Question 0");
    }
}

