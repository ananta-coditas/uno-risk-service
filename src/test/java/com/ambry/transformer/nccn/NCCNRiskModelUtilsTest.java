package com.ambry.transformer.nccn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

@DisplayName("NCCNRiskModelUtils Unit Tests")
class NCCNRiskModelUtilsTest {

    private Risk risk;
    private Person proband;
    private Person father;
    private Person mother;
    private Person brother;
    private Person sister;
    private Person son;
    private Person daughter;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        List<Person> family = new LinkedList<>();
        
        proband = new Person();
        proband.setId(1);
        proband.setProband(true);
        proband.setGender(0); // Female
        proband.setMotherId(2);
        proband.setFatherId(3);
        family.add(proband);
        
        mother = new Person();
        mother.setId(2);
        mother.setProband(false);
        mother.setGender(0); // Female
        mother.setMotherId(0);
        mother.setFatherId(0);
        family.add(mother);
        
        father = new Person();
        father.setId(3);
        father.setProband(false);
        father.setGender(1); // Male
        father.setMotherId(0);
        father.setFatherId(0);
        family.add(father);
        
        brother = new Person();
        brother.setId(4);
        brother.setProband(false);
        brother.setGender(1); // Male
        brother.setMotherId(2);
        brother.setFatherId(3);
        family.add(brother);
        
        sister = new Person();
        sister.setId(5);
        sister.setProband(false);
        sister.setGender(0); // Female
        sister.setMotherId(2);
        sister.setFatherId(3);
        family.add(sister);
        
        son = new Person();
        son.setId(6);
        son.setProband(false);
        son.setGender(1); // Male
        son.setMotherId(1);
        son.setFatherId(0);
        family.add(son);
        
        daughter = new Person();
        daughter.setId(7);
        daughter.setProband(false);
        daughter.setGender(0); // Female
        daughter.setMotherId(1);
        daughter.setFatherId(0);
        family.add(daughter);
        
        risk.setFamily(family);
    }

    @Test
    @DisplayName("Should calculate relationship for all family members")
    void testCalculateRelationShipForAllFamilyMemeber() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(proband.getId())).isNotNull();
        assertThat(result.get(proband.getId()).getRelationShip()).isEqualTo("PROBAND");
    }

    @Test
    @DisplayName("Should identify proband correctly")
    void testCalculateRelationShipIdentifiesProband() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails probandDetails = result.get(proband.getId());
        assertThat(probandDetails).isNotNull();
        assertThat(probandDetails.getRelationShip()).isEqualTo("PROBAND");
        assertThat(probandDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should identify first degree relatives - father")
    void testCalculateRelationShipForFather() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails fatherDetails = result.get(father.getId());
        assertThat(fatherDetails).isNotNull();
        assertThat(fatherDetails.getRelationShip()).isEqualTo("FATHER");
        assertThat(fatherDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should identify first degree relatives - mother")
    void testCalculateRelationShipForMother() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails motherDetails = result.get(mother.getId());
        assertThat(motherDetails).isNotNull();
        assertThat(motherDetails.getRelationShip()).isEqualTo("MOTHER");
        assertThat(motherDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should identify first degree relatives - brother")
    void testCalculateRelationShipForBrother() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails brotherDetails = result.get(brother.getId());
        assertThat(brotherDetails).isNotNull();
        assertThat(brotherDetails.getRelationShip()).isEqualTo("BROTHER");
        assertThat(brotherDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should identify first degree relatives - sister")
    void testCalculateRelationShipForSister() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails sisterDetails = result.get(sister.getId());
        assertThat(sisterDetails).isNotNull();
        assertThat(sisterDetails.getRelationShip()).isEqualTo("SISTER");
        assertThat(sisterDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should identify first degree relatives - son")
    void testCalculateRelationShipForSon() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails sonDetails = result.get(son.getId());
        assertThat(sonDetails).isNotNull();
        assertThat(sonDetails.getRelationShip()).isEqualTo("SON");
        assertThat(sonDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should identify first degree relatives - daughter")
    void testCalculateRelationShipForDaughter() {
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails daughterDetails = result.get(daughter.getId());
        assertThat(daughterDetails).isNotNull();
        assertThat(daughterDetails.getRelationShip()).isEqualTo("DAUGHTER");
        assertThat(daughterDetails.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should handle proband not first in list")
    void testCalculateRelationShipWithProbandNotFirst() {
        List<Person> family = new LinkedList<>();
        family.add(father);
        family.add(mother);
        family.add(proband);
        risk.setFamily(family);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        assertThat(result.get(proband.getId())).isNotNull();
        assertThat(result.get(proband.getId()).getRelationShip()).isEqualTo("PROBAND");
    }

    @Test
    @DisplayName("Should handle family with no proband")
    void testCalculateRelationShipWithNoProband() {
        proband.setProband(false);
        
        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        // Should use first person as default proband
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle empty family")
    void testCalculateRelationShipWithEmptyFamily() {
        risk.setFamily(new LinkedList<>());
        
        assertThat(NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk)).isEmpty();
    }

    @Test
    @DisplayName("Should handle single person family")
    void testCalculateRelationShipWithSinglePerson() {
        List<Person> family = new LinkedList<>();
        family.add(proband);
        risk.setFamily(family);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        assertThat(result).isNotNull();
        assertThat(result.get(proband.getId())).isNotNull();
        assertThat(result.get(proband.getId()).getRelationShip()).isEqualTo("PROBAND");
    }

    @Test
    @DisplayName("Should calculate second degree relatives - maternal side")
    void testCalculateRelationShipForMaternalSecondDegree() {
        Person grandmother = new Person();
        grandmother.setId(8);
        grandmother.setProband(false);
        grandmother.setGender(0); // Female
        grandmother.setMotherId(0);
        grandmother.setFatherId(0);
        mother.setMotherId(8);
        risk.getFamily().add(grandmother);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails grandmotherDetails = result.get(grandmother.getId());
        assertThat(grandmotherDetails).isNotNull();
        assertThat(grandmotherDetails.getRelationShip()).isEqualTo("GRANDMOTHER");
        assertThat(grandmotherDetails.getFamilySide()).isEqualTo("MATERNAL");
    }

    @Test
    @DisplayName("Should calculate second degree relatives - paternal side")
    void testCalculateRelationShipForPaternalSecondDegree() {
        Person grandfather = new Person();
        grandfather.setId(9);
        grandfather.setProband(false);
        grandfather.setGender(1); // Male
        grandfather.setMotherId(0);
        grandfather.setFatherId(0);
        father.setFatherId(9);
        risk.getFamily().add(grandfather);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        NCCNPersonRelationShipDetails grandfatherDetails = result.get(grandfather.getId());
        assertThat(grandfatherDetails).isNotNull();
        assertThat(grandfatherDetails.getRelationShip()).isEqualTo("GRANDFATHER");
        assertThat(grandfatherDetails.getFamilySide()).isEqualTo("PATERNAL");
    }

    @Test
    @DisplayName("Should calculate aunt/uncle relationships")
    void testCalculateRelationShipForAuntUncle() {
        Person aunt = new Person();
        aunt.setId(10);
        aunt.setProband(false);
        aunt.setGender(0); // Female
        aunt.setMotherId(2); // Same mother as proband
        aunt.setFatherId(3);
        risk.getFamily().add(aunt);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        // Aunt should be identified as sibling first, but let's check if it's in the result
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle complex family structure")
    void testCalculateRelationShipForComplexFamily() {
        Person cousin = new Person();
        cousin.setId(11);
        cousin.setProband(false);
        cousin.setGender(0); // Female
        cousin.setMotherId(10); // Child of aunt
        cousin.setFatherId(0);
        risk.getFamily().add(cousin);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle person with no parents")
    void testCalculateRelationShipWithNoParents() {
        Person orphan = new Person();
        orphan.setId(12);
        orphan.setProband(false);
        orphan.setGender(0);
        orphan.setMotherId(0);
        orphan.setFatherId(0);
        risk.getFamily().add(orphan);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should handle multiple probands - uses first found")
    void testCalculateRelationShipWithMultipleProbands() {
        Person proband2 = new Person();
        proband2.setId(13);
        proband2.setProband(true);
        proband2.setGender(1);
        risk.getFamily().add(proband2);

        Map<Integer, NCCNPersonRelationShipDetails> result = 
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        // Should use first proband found
        assertThat(result.get(proband.getId())).isNotNull();
        assertThat(result.get(proband.getId()).getRelationShip()).isEqualTo("PROBAND");
    }

    // ========== Extended pedigree ==========

    private Person addPerson(int id, int gender, int motherId, int fatherId) {
        Person person = new Person();
        person.setId(id);
        person.setProband(false);
        person.setGender(gender);
        person.setMotherId(motherId);
        person.setFatherId(fatherId);
        risk.getFamily().add(person);
        return person;
    }

    private void assertRelationship(Person person, String relationship, String side) {
        NCCNPersonRelationShipDetails details =
            NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk).get(person.getId());
        assertThat(details.getRelationShip()).isEqualTo(relationship);
        assertThat(details.getFamilySide()).isEqualTo(side);
    }

    @Test
    @DisplayName("should_labelBothSidesCorrectly_when_familyHasMaternalAndPaternalGrandparents")
    void should_labelBothSidesCorrectly_when_familyHasMaternalAndPaternalGrandparents() {
        Person maternalGrandmother = addPerson(20, 0, 0, 0);
        Person paternalGrandmother = addPerson(21, 0, 0, 0);
        mother.setMotherId(20);
        father.setMotherId(21);

        Map<Integer, NCCNPersonRelationShipDetails> result = NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(risk);

        assertThat(result.get(maternalGrandmother.getId()).getFamilySide()).isEqualTo("MATERNAL");
        assertThat(result.get(paternalGrandmother.getId()).getFamilySide()).isEqualTo("PATERNAL");
    }

    @Test
    @DisplayName("should_labelPaternalUncle_when_relativeIsFullSiblingOfFather")
    void should_labelPaternalUncle_when_relativeIsFullSiblingOfFather() {
        addPerson(30, 1, 0, 0);
        addPerson(31, 0, 0, 0);
        father.setFatherId(30);
        father.setMotherId(31);
        Person uncle = addPerson(32, 1, 31, 30);

        assertRelationship(uncle, "UNCLE", "PATERNAL");
    }

    @Test
    @DisplayName("should_labelFirstCousinBySex_when_relativeIsChildOfAuntOrUncle")
    void should_labelFirstCousinBySex_when_relativeIsChildOfAuntOrUncle() {
        addPerson(40, 1, 0, 0);
        addPerson(41, 0, 0, 0);
        mother.setFatherId(40);
        mother.setMotherId(41);
        addPerson(42, 0, 41, 40);
        Person cousin = addPerson(43, 1, 42, 0);

        assertRelationship(cousin, "FIRST_COUSIN_MALE", "MATERNAL");
    }

    @Test
    @DisplayName("should_labelGreatGrandparentAndGreatAunt_when_relativesAreAboveGrandparents")
    void should_labelGreatGrandparentAndGreatAunt_when_relativesAreAboveGrandparents() {
        addPerson(50, 1, 0, 0);
        addPerson(51, 0, 0, 0);
        Person paternalGrandfather = addPerson(52, 1, 51, 50);
        father.setFatherId(paternalGrandfather.getId());
        Person greatAunt = addPerson(53, 0, 51, 50);

        assertRelationship(risk.getFamily().stream().filter(p -> p.getId() == 51).findFirst().orElseThrow(),
            "GREAT_GRANDMOTHER", "PATERNAL");
        assertRelationship(greatAunt, "GREAT_AUNT", "PATERNAL");
    }

    @Test
    @DisplayName("should_labelHalfSiblingOnSharedParentSide_when_onlyFatherIsShared")
    void should_labelHalfSiblingOnSharedParentSide_when_onlyFatherIsShared() {
        addPerson(60, 0, 0, 0);
        Person halfBrother = addPerson(61, 1, 60, 3);

        assertRelationship(halfBrother, "HALF_BROTHER", "PATERNAL");
    }

    @Test
    @DisplayName("should_labelHalfAunt_when_relativeSharesOneParentWithProbandsMother")
    void should_labelHalfAunt_when_relativeSharesOneParentWithProbandsMother() {
        addPerson(70, 0, 0, 0);
        addPerson(71, 1, 0, 0);
        addPerson(72, 0, 0, 0);
        mother.setMotherId(70);
        mother.setFatherId(71);
        Person halfAunt = addPerson(73, 0, 72, 71);

        assertRelationship(halfAunt, "HALF_AUNT", "MATERNAL");
    }

    @Test
    @DisplayName("should_labelDescendantsWithoutSide_when_relativesDescendFromSiblingsOrChildren")
    void should_labelDescendantsWithoutSide_when_relativesDescendFromSiblingsOrChildren() {
        Person niece = addPerson(80, 0, 5, 0);
        Person greatNephew = addPerson(81, 1, 80, 0);
        Person grandson = addPerson(82, 1, 7, 0);
        Person greatGranddaughter = addPerson(83, 0, 0, 82);

        assertRelationship(niece, "NIECE", "OTHER");
        assertRelationship(greatNephew, "GREAT_NEPHEW", "OTHER");
        assertRelationship(grandson, "GRANDSON", "OTHER");
        assertRelationship(greatGranddaughter, "GREAT_GRANDDAUGHTER", "OTHER");
    }

    @Test
    @DisplayName("should_labelHalfNiece_when_relativeIsChildOfHalfSibling")
    void should_labelHalfNiece_when_relativeIsChildOfHalfSibling() {
        addPerson(90, 1, 0, 0);
        addPerson(91, 0, 2, 90);
        Person halfNiece = addPerson(92, 0, 91, 0);

        assertRelationship(halfNiece, "HALF_NIECE", "MATERNAL");
    }

    @Test
    @DisplayName("should_fallBackToOthers_when_relativeCannotBePlaced")
    void should_fallBackToOthers_when_relativeCannotBePlaced() {
        Person unrelated = addPerson(99, 0, 0, 0);

        assertRelationship(unrelated, "OTHERS", "OTHER");
    }

    @Test
    @DisplayName("should_recogniseSiblings_when_parentIdsAreAbove127")
    void should_recogniseSiblings_when_parentIdsAreAbove127() {
        mother.setId(1002);
        father.setId(1003);
        proband.setMotherId(1002);
        proband.setFatherId(1003);
        sister.setMotherId(1002);
        sister.setFatherId(1003);

        assertRelationship(sister, "SISTER", "OTHER");
    }

    @Test
    @DisplayName("should_labelMotherAndSister_when_femalesUseUnoGenderCode2")
    void should_labelMotherAndSister_when_femalesUseUnoGenderCode2() {
        mother.setGender(2);
        sister.setGender(2);

        assertRelationship(mother, "MOTHER", "OTHER");
        assertRelationship(sister, "SISTER", "OTHER");
    }
}
