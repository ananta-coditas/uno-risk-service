package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.genomebrain.backend.quesstionnaire.relative.*;

/**
 * Unit tests for {@link FamilyCancerDetails}.
 */
@DisplayName("FamilyCancerDetails Unit Tests")
class FamilyCancerDetailsTest {

    private FamilyCancerDetails familyCancerDetails;
    private BasicQuestions basicQuestions;
    private RelativeHistory mother;
    private RelativeHistory father;

    @BeforeEach
    void setUp() {
        familyCancerDetails = new FamilyCancerDetails();
        basicQuestions = new BasicQuestions();
        mother = new Mother();
        father = new Father();
    }

    @Test
    @DisplayName("Should get and set basicQuestions")
    void testBasicQuestions() {
        familyCancerDetails.setBasicQuestions(basicQuestions);
        assertThat(familyCancerDetails.getBasicQuestions()).isEqualTo(basicQuestions);
    }

    @Test
    @DisplayName("Should get and set Mother")
    void testMother() {
        familyCancerDetails.setMother(mother);
        assertThat(familyCancerDetails.getMother()).isEqualTo(mother);
    }

    @Test
    @DisplayName("Should get and set Father")
    void testFather() {
        familyCancerDetails.setFather(father);
        assertThat(familyCancerDetails.getFather()).isEqualTo(father);
    }

    @Test
    @DisplayName("Should get and set Mothers")
    void testMothers() {
        List<Mother> mothers = new ArrayList<>();
        mothers.add(new Mother());
        familyCancerDetails.setMothers(mothers);
        assertThat(familyCancerDetails.getMothers()).isEqualTo(mothers);
    }

    @Test
    @DisplayName("Should get and set Fathers")
    void testFathers() {
        List<Father> fathers = new ArrayList<>();
        fathers.add(new Father());
        familyCancerDetails.setFathers(fathers);
        assertThat(familyCancerDetails.getFathers()).isEqualTo(fathers);
    }

    @Test
    @DisplayName("Should get and set sisters")
    void testSisters() {
        List<Sister> sisters = new ArrayList<>();
        sisters.add(new Sister());
        familyCancerDetails.setSisters(sisters);
        assertThat(familyCancerDetails.getSisters()).isEqualTo(sisters);
    }

    @Test
    @DisplayName("Should get and set Brothers")
    void testBrothers() {
        List<Brother> brothers = new ArrayList<>();
        brothers.add(new Brother());
        familyCancerDetails.setBrothers(brothers);
        assertThat(familyCancerDetails.getBrothers()).isEqualTo(brothers);
    }

    @Test
    @DisplayName("Should get and set Daughters")
    void testDaughters() {
        List<Daughters> daughters = new ArrayList<>();
        daughters.add(new Daughters());
        familyCancerDetails.setDaughters(daughters);
        assertThat(familyCancerDetails.getDaughters()).isEqualTo(daughters);
    }

    @Test
    @DisplayName("Should get and set Sons")
    void testSons() {
        List<Son> sons = new ArrayList<>();
        sons.add(new Son());
        familyCancerDetails.setSons(sons);
        assertThat(familyCancerDetails.getSons()).isEqualTo(sons);
    }

    @Test
    @DisplayName("Should get and set Nieces")
    void testNieces() {
        List<Niece> nieces = new ArrayList<>();
        nieces.add(new Niece());
        familyCancerDetails.setNieces(nieces);
        assertThat(familyCancerDetails.getNieces()).isEqualTo(nieces);
    }

    @Test
    @DisplayName("Should get and set Nephews")
    void testNephews() {
        List<Nephew> nephews = new ArrayList<>();
        nephews.add(new Nephew());
        familyCancerDetails.setNephews(nephews);
        assertThat(familyCancerDetails.getNephews()).isEqualTo(nephews);
    }

    @Test
    @DisplayName("Should get and set HalfSisters")
    void testHalfSisters() {
        List<HalfSister> halfSisters = new ArrayList<>();
        halfSisters.add(new HalfSister());
        familyCancerDetails.setHalfSisters(halfSisters);
        assertThat(familyCancerDetails.getHalfSisters()).isEqualTo(halfSisters);
    }

    @Test
    @DisplayName("Should get and set HalfBrothers")
    void testHalfBrothers() {
        List<HalfBrother> halfBrothers = new ArrayList<>();
        halfBrothers.add(new HalfBrother());
        familyCancerDetails.setHalfBrothers(halfBrothers);
        assertThat(familyCancerDetails.getHalfBrothers()).isEqualTo(halfBrothers);
    }

    @Test
    @DisplayName("Should get and set GrandMothers")
    void testGrandMothers() {
        List<Grandmother> grandMothers = new ArrayList<>();
        grandMothers.add(new Grandmother());
        familyCancerDetails.setGrandMothers(grandMothers);
        assertThat(familyCancerDetails.getGrandMothers()).isEqualTo(grandMothers);
    }

    @Test
    @DisplayName("Should get and set GrandFathers")
    void testGrandFathers() {
        List<Grandfather> grandFathers = new ArrayList<>();
        grandFathers.add(new Grandfather());
        familyCancerDetails.setGrandFathers(grandFathers);
        assertThat(familyCancerDetails.getGrandFathers()).isEqualTo(grandFathers);
    }

    @Test
    @DisplayName("Should get and set GrandDaughters")
    void testGrandDaughters() {
        List<Granddaughter> grandDaughters = new ArrayList<>();
        grandDaughters.add(new Granddaughter());
        familyCancerDetails.setGrandDaughters(grandDaughters);
        assertThat(familyCancerDetails.getGrandDaughters()).isEqualTo(grandDaughters);
    }

    @Test
    @DisplayName("Should get and set GrandSons")
    void testGrandSons() {
        List<Grandson> grandSons = new ArrayList<>();
        grandSons.add(new Grandson());
        familyCancerDetails.setGrandSons(grandSons);
        assertThat(familyCancerDetails.getGrandSons()).isEqualTo(grandSons);
    }

    @Test
    @DisplayName("Should get and set Aunts")
    void testAunts() {
        List<Aunt> aunts = new ArrayList<>();
        aunts.add(new Aunt());
        familyCancerDetails.setAunts(aunts);
        assertThat(familyCancerDetails.getAunts()).isEqualTo(aunts);
    }

    @Test
    @DisplayName("Should get and set Uncles")
    void testUncles() {
        List<Uncle> uncles = new ArrayList<>();
        uncles.add(new Uncle());
        familyCancerDetails.setUncles(uncles);
        assertThat(familyCancerDetails.getUncles()).isEqualTo(uncles);
    }

    @Test
    @DisplayName("Should get and set FirstCousinsMale")
    void testFirstCousinsMale() {
        List<FirstCousin> firstCousinsMale = new ArrayList<>();
        firstCousinsMale.add(new FirstCousin());
        familyCancerDetails.setFirstCousinsMale(firstCousinsMale);
        assertThat(familyCancerDetails.getFirstCousinsMale()).isEqualTo(firstCousinsMale);
    }

    @Test
    @DisplayName("Should get and set FirstCousinsFemale")
    void testFirstCousinsFemale() {
        List<FirstCousin> firstCousinsFemale = new ArrayList<>();
        firstCousinsFemale.add(new FirstCousin());
        familyCancerDetails.setFirstCousinsFemale(firstCousinsFemale);
        assertThat(familyCancerDetails.getFirstCousinsFemale()).isEqualTo(firstCousinsFemale);
    }

    @Test
    @DisplayName("Should get and set GreatGrandMothers")
    void testGreatGrandMothers() {
        List<GreatGrandmother> greatGrandMothers = new ArrayList<>();
        greatGrandMothers.add(new GreatGrandmother());
        familyCancerDetails.setGreatGrandMothers(greatGrandMothers);
        assertThat(familyCancerDetails.getGreatGrandMothers()).isEqualTo(greatGrandMothers);
    }

    @Test
    @DisplayName("Should get and set GreatGrandFathers")
    void testGreatGrandFathers() {
        List<GreatGrandfather> greatGrandFathers = new ArrayList<>();
        greatGrandFathers.add(new GreatGrandfather());
        familyCancerDetails.setGreatGrandFathers(greatGrandFathers);
        assertThat(familyCancerDetails.getGreatGrandFathers()).isEqualTo(greatGrandFathers);
    }

    @Test
    @DisplayName("Should get and set GreatAunts")
    void testGreatAunts() {
        List<GreatAunt> greatAunts = new ArrayList<>();
        greatAunts.add(new GreatAunt());
        familyCancerDetails.setGreatAunts(greatAunts);
        assertThat(familyCancerDetails.getGreatAunts()).isEqualTo(greatAunts);
    }

    @Test
    @DisplayName("Should get and set GreatUncles")
    void testGreatUncles() {
        List<GreatUncle> greatUncles = new ArrayList<>();
        greatUncles.add(new GreatUncle());
        familyCancerDetails.setGreatUncles(greatUncles);
        assertThat(familyCancerDetails.getGreatUncles()).isEqualTo(greatUncles);
    }

    @Test
    @DisplayName("Should get and set GreatNephews")
    void testGreatNephews() {
        List<GreatNephew> greatNephews = new ArrayList<>();
        greatNephews.add(new GreatNephew());
        familyCancerDetails.setGreatNephews(greatNephews);
        assertThat(familyCancerDetails.getGreatNephews()).isEqualTo(greatNephews);
    }

    @Test
    @DisplayName("Should get and set GreatNieces")
    void testGreatNieces() {
        List<GreatNiece> greatNieces = new ArrayList<>();
        greatNieces.add(new GreatNiece());
        familyCancerDetails.setGreatNieces(greatNieces);
        assertThat(familyCancerDetails.getGreatNieces()).isEqualTo(greatNieces);
    }

    @Test
    @DisplayName("Should get and set GreatGrandDaughters")
    void testGreatGrandDaughters() {
        List<GreatGranddaughter> greatGrandDaughters = new ArrayList<>();
        greatGrandDaughters.add(new GreatGranddaughter());
        familyCancerDetails.setGreatGrandDaughters(greatGrandDaughters);
        assertThat(familyCancerDetails.getGreatGrandDaughters()).isEqualTo(greatGrandDaughters);
    }

    @Test
    @DisplayName("Should get and set GreatGrandSons")
    void testGreatGrandSons() {
        List<GreatGrandson> greatGrandSons = new ArrayList<>();
        greatGrandSons.add(new GreatGrandson());
        familyCancerDetails.setGreatGrandSons(greatGrandSons);
        assertThat(familyCancerDetails.getGreatGrandSons()).isEqualTo(greatGrandSons);
    }

    @Test
    @DisplayName("Should get and set HalfAunts")
    void testHalfAunts() {
        List<HalfAunt> halfAunts = new ArrayList<>();
        halfAunts.add(new HalfAunt());
        familyCancerDetails.setHalfAunts(halfAunts);
        assertThat(familyCancerDetails.getHalfAunts()).isEqualTo(halfAunts);
    }

    @Test
    @DisplayName("Should get and set HalfUncles")
    void testHalfUncles() {
        List<HalfUncle> halfUncles = new ArrayList<>();
        halfUncles.add(new HalfUncle());
        familyCancerDetails.setHalfUncles(halfUncles);
        assertThat(familyCancerDetails.getHalfUncles()).isEqualTo(halfUncles);
    }

    @Test
    @DisplayName("Should get and set HalfNieces")
    void testHalfNieces() {
        List<HalfNiece> halfNieces = new ArrayList<>();
        halfNieces.add(new HalfNiece());
        familyCancerDetails.setHalfNieces(halfNieces);
        assertThat(familyCancerDetails.getHalfNieces()).isEqualTo(halfNieces);
    }

    @Test
    @DisplayName("Should get and set HalfNephews")
    void testHalfNephews() {
        List<HalfNephew> halfNephews = new ArrayList<>();
        halfNephews.add(new HalfNephew());
        familyCancerDetails.setHalfNephews(halfNephews);
        assertThat(familyCancerDetails.getHalfNephews()).isEqualTo(halfNephews);
    }

    @Test
    @DisplayName("Should get and set Others")
    void testOthers() {
        List<Others> others = new ArrayList<>();
        others.add(new Others());
        familyCancerDetails.setOthers(others);
        assertThat(familyCancerDetails.getOthers()).isEqualTo(others);
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        familyCancerDetails.setBasicQuestions(null);
        assertThat(familyCancerDetails.getBasicQuestions()).isNull();
        
        familyCancerDetails.setMother(null);
        assertThat(familyCancerDetails.getMother()).isNull();
        
        familyCancerDetails.setFather(null);
        assertThat(familyCancerDetails.getFather()).isNull();
        
        familyCancerDetails.setMothers(null);
        assertThat(familyCancerDetails.getMothers()).isNull();
        
        familyCancerDetails.setSisters(null);
        assertThat(familyCancerDetails.getSisters()).isNull();
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        List<Sister> emptyList = new ArrayList<>();
        familyCancerDetails.setSisters(emptyList);
        assertThat(familyCancerDetails.getSisters()).isEmpty();
        
        List<Brother> emptyBrothers = new ArrayList<>();
        familyCancerDetails.setBrothers(emptyBrothers);
        assertThat(familyCancerDetails.getBrothers()).isEmpty();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        familyCancerDetails.setBasicQuestions(basicQuestions);
        familyCancerDetails.setMother(mother);
        familyCancerDetails.setFather(father);
        
        List<Sister> sisters = new ArrayList<>();
        sisters.add(new Sister());
        familyCancerDetails.setSisters(sisters);
        
        List<Brother> brothers = new ArrayList<>();
        brothers.add(new Brother());
        familyCancerDetails.setBrothers(brothers);
        
        String result = familyCancerDetails.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("FamilyCancerDetails");
        assertThat(result).contains("basicQuestions");
        assertThat(result).contains("Mother");
        assertThat(result).contains("Father");
        assertThat(result).contains("sisters");
        assertThat(result).contains("Brothers");
    }

    @Test
    @DisplayName("Should return correct toString with all fields")
    void testToStringWithAllFields() {
        familyCancerDetails.setBasicQuestions(basicQuestions);
        familyCancerDetails.setMother(mother);
        familyCancerDetails.setFather(father);
        
        List<Mother> mothers = new ArrayList<>();
        mothers.add(new Mother());
        familyCancerDetails.setMothers(mothers);
        
        List<Father> fathers = new ArrayList<>();
        fathers.add(new Father());
        familyCancerDetails.setFathers(fathers);
        
        List<Sister> sisters = new ArrayList<>();
        sisters.add(new Sister());
        familyCancerDetails.setSisters(sisters);
        
        List<Brother> brothers = new ArrayList<>();
        brothers.add(new Brother());
        familyCancerDetails.setBrothers(brothers);
        
        List<Daughters> daughters = new ArrayList<>();
        daughters.add(new Daughters());
        familyCancerDetails.setDaughters(daughters);
        
        List<Son> sons = new ArrayList<>();
        sons.add(new Son());
        familyCancerDetails.setSons(sons);
        
        List<Niece> nieces = new ArrayList<>();
        nieces.add(new Niece());
        familyCancerDetails.setNieces(nieces);
        
        List<Nephew> nephews = new ArrayList<>();
        nephews.add(new Nephew());
        familyCancerDetails.setNephews(nephews);
        
        List<HalfSister> halfSisters = new ArrayList<>();
        halfSisters.add(new HalfSister());
        familyCancerDetails.setHalfSisters(halfSisters);
        
        List<HalfBrother> halfBrothers = new ArrayList<>();
        halfBrothers.add(new HalfBrother());
        familyCancerDetails.setHalfBrothers(halfBrothers);
        
        List<Grandmother> grandMothers = new ArrayList<>();
        grandMothers.add(new Grandmother());
        familyCancerDetails.setGrandMothers(grandMothers);
        
        List<Grandfather> grandFathers = new ArrayList<>();
        grandFathers.add(new Grandfather());
        familyCancerDetails.setGrandFathers(grandFathers);
        
        List<Granddaughter> grandDaughters = new ArrayList<>();
        grandDaughters.add(new Granddaughter());
        familyCancerDetails.setGrandDaughters(grandDaughters);
        
        List<Grandson> grandSons = new ArrayList<>();
        grandSons.add(new Grandson());
        familyCancerDetails.setGrandSons(grandSons);
        
        List<Aunt> aunts = new ArrayList<>();
        aunts.add(new Aunt());
        familyCancerDetails.setAunts(aunts);
        
        List<Uncle> uncles = new ArrayList<>();
        uncles.add(new Uncle());
        familyCancerDetails.setUncles(uncles);
        
        List<FirstCousin> firstCousinsMale = new ArrayList<>();
        firstCousinsMale.add(new FirstCousin());
        familyCancerDetails.setFirstCousinsMale(firstCousinsMale);
        
        List<FirstCousin> firstCousinsFemale = new ArrayList<>();
        firstCousinsFemale.add(new FirstCousin());
        familyCancerDetails.setFirstCousinsFemale(firstCousinsFemale);
        
        List<GreatGrandmother> greatGrandMothers = new ArrayList<>();
        greatGrandMothers.add(new GreatGrandmother());
        familyCancerDetails.setGreatGrandMothers(greatGrandMothers);
        
        List<GreatGrandfather> greatGrandFathers = new ArrayList<>();
        greatGrandFathers.add(new GreatGrandfather());
        familyCancerDetails.setGreatGrandFathers(greatGrandFathers);
        
        List<GreatAunt> greatAunts = new ArrayList<>();
        greatAunts.add(new GreatAunt());
        familyCancerDetails.setGreatAunts(greatAunts);
        
        List<GreatUncle> greatUncles = new ArrayList<>();
        greatUncles.add(new GreatUncle());
        familyCancerDetails.setGreatUncles(greatUncles);
        
        List<GreatNephew> greatNephews = new ArrayList<>();
        greatNephews.add(new GreatNephew());
        familyCancerDetails.setGreatNephews(greatNephews);
        
        List<GreatNiece> greatNieces = new ArrayList<>();
        greatNieces.add(new GreatNiece());
        familyCancerDetails.setGreatNieces(greatNieces);
        
        List<GreatGranddaughter> greatGrandDaughters = new ArrayList<>();
        greatGrandDaughters.add(new GreatGranddaughter());
        familyCancerDetails.setGreatGrandDaughters(greatGrandDaughters);
        
        List<GreatGrandson> greatGrandSons = new ArrayList<>();
        greatGrandSons.add(new GreatGrandson());
        familyCancerDetails.setGreatGrandSons(greatGrandSons);
        
        List<HalfAunt> halfAunts = new ArrayList<>();
        halfAunts.add(new HalfAunt());
        familyCancerDetails.setHalfAunts(halfAunts);
        
        List<HalfUncle> halfUncles = new ArrayList<>();
        halfUncles.add(new HalfUncle());
        familyCancerDetails.setHalfUncles(halfUncles);
        
        List<HalfNiece> halfNieces = new ArrayList<>();
        halfNieces.add(new HalfNiece());
        familyCancerDetails.setHalfNieces(halfNieces);
        
        List<HalfNephew> halfNephews = new ArrayList<>();
        halfNephews.add(new HalfNephew());
        familyCancerDetails.setHalfNephews(halfNephews);
        
        List<Others> others = new ArrayList<>();
        others.add(new Others());
        familyCancerDetails.setOthers(others);
        
        String result = familyCancerDetails.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("FamilyCancerDetails");
        assertThat(result).contains("mothers");
        assertThat(result).contains("fathers");
        assertThat(result).contains("sisters");
        assertThat(result).contains("Brothers");
        assertThat(result).contains("Daughters");
        assertThat(result).contains("Sons");
        assertThat(result).contains("Nieces");
        assertThat(result).contains("Nephews");
        assertThat(result).contains("HalfSisters");
        assertThat(result).contains("HalfBrothers");
        assertThat(result).contains("GrandMothers");
        assertThat(result).contains("GrandFathers");
        assertThat(result).contains("GrandDaughters");
        assertThat(result).contains("GrandSons");
        assertThat(result).contains("Aunts");
        assertThat(result).contains("Uncles");
        assertThat(result).contains("FirstCousinsMale");
        assertThat(result).contains("FirstCousinsFemale");
        assertThat(result).contains("GreatGrandMothers");
        assertThat(result).contains("GreatGrandFathers");
        assertThat(result).contains("GreatAunts");
        assertThat(result).contains("GreatUncles");
        assertThat(result).contains("Others");
    }
}

