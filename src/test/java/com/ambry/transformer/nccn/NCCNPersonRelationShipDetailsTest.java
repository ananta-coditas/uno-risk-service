package com.ambry.transformer.nccn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NCCNPersonRelationShipDetails Unit Tests")
class NCCNPersonRelationShipDetailsTest {

    @Test
    @DisplayName("Should create NCCNPersonRelationShipDetails with relationship and family side")
    void testConstructor() {
        NCCNPersonRelationShipDetails details = new NCCNPersonRelationShipDetails("MOTHER", "MATERNAL");

        assertThat(details).isNotNull();
        assertThat(details.getRelationShip()).isEqualTo("MOTHER");
        assertThat(details.getFamilySide()).isEqualTo("MATERNAL");
    }

    @Test
    @DisplayName("Should get relationship correctly")
    void testGetRelationShip() {
        NCCNPersonRelationShipDetails details = new NCCNPersonRelationShipDetails("FATHER", "PATERNAL");

        assertThat(details.getRelationShip()).isEqualTo("FATHER");
    }

    @Test
    @DisplayName("Should get family side correctly")
    void testGetFamilySide() {
        NCCNPersonRelationShipDetails details = new NCCNPersonRelationShipDetails("SISTER", "OTHER");

        assertThat(details.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should handle different relationship types")
    void testDifferentRelationshipTypes() {
        NCCNPersonRelationShipDetails proband = new NCCNPersonRelationShipDetails("PROBAND", "OTHER");
        NCCNPersonRelationShipDetails brother = new NCCNPersonRelationShipDetails("BROTHER", "OTHER");
        NCCNPersonRelationShipDetails aunt = new NCCNPersonRelationShipDetails("AUNT", "MATERNAL");
        NCCNPersonRelationShipDetails uncle = new NCCNPersonRelationShipDetails("UNCLE", "PATERNAL");

        assertThat(proband.getRelationShip()).isEqualTo("PROBAND");
        assertThat(brother.getRelationShip()).isEqualTo("BROTHER");
        assertThat(aunt.getRelationShip()).isEqualTo("AUNT");
        assertThat(uncle.getRelationShip()).isEqualTo("UNCLE");
    }

    @Test
    @DisplayName("Should handle different family sides")
    void testDifferentFamilySides() {
        NCCNPersonRelationShipDetails maternal = new NCCNPersonRelationShipDetails("GRANDMOTHER", "MATERNAL");
        NCCNPersonRelationShipDetails paternal = new NCCNPersonRelationShipDetails("GRANDFATHER", "PATERNAL");
        NCCNPersonRelationShipDetails other = new NCCNPersonRelationShipDetails("OTHER", "OTHER");

        assertThat(maternal.getFamilySide()).isEqualTo("MATERNAL");
        assertThat(paternal.getFamilySide()).isEqualTo("PATERNAL");
        assertThat(other.getFamilySide()).isEqualTo("OTHER");
    }

    @Test
    @DisplayName("Should handle null values")
    void testWithNullValues() {
        NCCNPersonRelationShipDetails details = new NCCNPersonRelationShipDetails(null, null);

        assertThat(details.getRelationShip()).isNull();
        assertThat(details.getFamilySide()).isNull();
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testWithEmptyStrings() {
        NCCNPersonRelationShipDetails details = new NCCNPersonRelationShipDetails("", "");

        assertThat(details.getRelationShip()).isEqualTo("");
        assertThat(details.getFamilySide()).isEqualTo("");
    }
}

