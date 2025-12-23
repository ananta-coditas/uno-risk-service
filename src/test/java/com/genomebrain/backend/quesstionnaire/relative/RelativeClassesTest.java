package com.genomebrain.backend.quesstionnaire.relative;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.RelativeDetail;
import com.genomebrain.backend.quesstionnaire.RelativeHistory;

/**
 * Unit tests for relative classes that extend RelativeHistory.
 */
@DisplayName("Relative Classes Unit Tests")
class RelativeClassesTest {

    @Test
    @DisplayName("Should create Mother instance")
    void testMother() {
        Mother mother = new Mother();
        assertThat(mother).isNotNull();
        assertThat(mother).isInstanceOf(RelativeHistory.class);
        
        // Test that it can use RelativeHistory methods
        List<QuestionSet> questionSets = new ArrayList<>();
        mother.setBasicQuestions(questionSets);
        assertThat(mother.getBasicQuestions()).isEqualTo(questionSets);
    }

    @Test
    @DisplayName("Should create Father instance")
    void testFather() {
        Father father = new Father();
        assertThat(father).isNotNull();
        assertThat(father).isInstanceOf(RelativeHistory.class);
        
        RelativeDetail detail = new RelativeDetail();
        father.setRelativeDetail(detail);
        assertThat(father.getRelativeDetail()).isEqualTo(detail);
    }

    @Test
    @DisplayName("Should create Brother instance")
    void testBrother() {
        Brother brother = new Brother();
        assertThat(brother).isNotNull();
        assertThat(brother).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Sister instance")
    void testSister() {
        Sister sister = new Sister();
        assertThat(sister).isNotNull();
        assertThat(sister).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Son instance")
    void testSon() {
        Son son = new Son();
        assertThat(son).isNotNull();
        assertThat(son).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Daughters instance")
    void testDaughters() {
        Daughters daughters = new Daughters();
        assertThat(daughters).isNotNull();
        assertThat(daughters).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Grandmother instance")
    void testGrandmother() {
        Grandmother grandmother = new Grandmother();
        assertThat(grandmother).isNotNull();
        assertThat(grandmother).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Grandfather instance")
    void testGrandfather() {
        Grandfather grandfather = new Grandfather();
        assertThat(grandfather).isNotNull();
        assertThat(grandfather).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Aunt instance")
    void testAunt() {
        Aunt aunt = new Aunt();
        assertThat(aunt).isNotNull();
        assertThat(aunt).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Uncle instance")
    void testUncle() {
        Uncle uncle = new Uncle();
        assertThat(uncle).isNotNull();
        assertThat(uncle).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Nephew instance")
    void testNephew() {
        Nephew nephew = new Nephew();
        assertThat(nephew).isNotNull();
        assertThat(nephew).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Niece instance")
    void testNiece() {
        Niece niece = new Niece();
        assertThat(niece).isNotNull();
        assertThat(niece).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Grandson instance")
    void testGrandson() {
        Grandson grandson = new Grandson();
        assertThat(grandson).isNotNull();
        assertThat(grandson).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create Granddaughter instance")
    void testGranddaughter() {
        Granddaughter granddaughter = new Granddaughter();
        assertThat(granddaughter).isNotNull();
        assertThat(granddaughter).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create FirstCousin instance")
    void testFirstCousin() {
        FirstCousin firstCousin = new FirstCousin();
        assertThat(firstCousin).isNotNull();
        assertThat(firstCousin).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create HalfBrother instance")
    void testHalfBrother() {
        HalfBrother halfBrother = new HalfBrother();
        assertThat(halfBrother).isNotNull();
        assertThat(halfBrother).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create HalfSister instance")
    void testHalfSister() {
        HalfSister halfSister = new HalfSister();
        assertThat(halfSister).isNotNull();
        assertThat(halfSister).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create HalfAunt instance")
    void testHalfAunt() {
        HalfAunt halfAunt = new HalfAunt();
        assertThat(halfAunt).isNotNull();
        assertThat(halfAunt).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create HalfUncle instance")
    void testHalfUncle() {
        HalfUncle halfUncle = new HalfUncle();
        assertThat(halfUncle).isNotNull();
        assertThat(halfUncle).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create HalfNephew instance")
    void testHalfNephew() {
        HalfNephew halfNephew = new HalfNephew();
        assertThat(halfNephew).isNotNull();
        assertThat(halfNephew).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create HalfNiece instance")
    void testHalfNiece() {
        HalfNiece halfNiece = new HalfNiece();
        assertThat(halfNiece).isNotNull();
        assertThat(halfNiece).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatGrandmother instance")
    void testGreatGrandmother() {
        GreatGrandmother greatGrandmother = new GreatGrandmother();
        assertThat(greatGrandmother).isNotNull();
        assertThat(greatGrandmother).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatGrandfather instance")
    void testGreatGrandfather() {
        GreatGrandfather greatGrandfather = new GreatGrandfather();
        assertThat(greatGrandfather).isNotNull();
        assertThat(greatGrandfather).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatAunt instance")
    void testGreatAunt() {
        GreatAunt greatAunt = new GreatAunt();
        assertThat(greatAunt).isNotNull();
        assertThat(greatAunt).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatUncle instance")
    void testGreatUncle() {
        GreatUncle greatUncle = new GreatUncle();
        assertThat(greatUncle).isNotNull();
        assertThat(greatUncle).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatGrandson instance")
    void testGreatGrandson() {
        GreatGrandson greatGrandson = new GreatGrandson();
        assertThat(greatGrandson).isNotNull();
        assertThat(greatGrandson).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatGranddaughter instance")
    void testGreatGranddaughter() {
        GreatGranddaughter greatGranddaughter = new GreatGranddaughter();
        assertThat(greatGranddaughter).isNotNull();
        assertThat(greatGranddaughter).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatNephew instance")
    void testGreatNephew() {
        GreatNephew greatNephew = new GreatNephew();
        assertThat(greatNephew).isNotNull();
        assertThat(greatNephew).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should create GreatNiece instance")
    void testGreatNiece() {
        GreatNiece greatNiece = new GreatNiece();
        assertThat(greatNiece).isNotNull();
        assertThat(greatNiece).isInstanceOf(RelativeHistory.class);
    }

    @Test
    @DisplayName("Should test that relative classes can use RelativeHistory methods")
    void testRelativeClassesCanUseRelativeHistoryMethods() {
        Mother mother = new Mother();
        List<QuestionSet> questionSets = new ArrayList<>();
        QuestionSet qs = new QuestionSet();
        qs.setQuestionText("Test");
        questionSets.add(qs);
        
        mother.setBasicQuestions(questionSets);
        mother.setBreast(questionSets);
        mother.setOvarian(questionSets);
        
        RelativeDetail detail = new RelativeDetail();
        detail.setRelativeID("REL001");
        mother.setRelativeDetail(detail);
        
        assertThat(mother.getBasicQuestions()).isEqualTo(questionSets);
        assertThat(mother.getBreast()).isEqualTo(questionSets);
        assertThat(mother.getOvarian()).isEqualTo(questionSets);
        assertThat(mother.getRelativeDetail()).isEqualTo(detail);
        assertThat(mother.getRelativeDetail().getRelativeID()).isEqualTo("REL001");
    }
}

