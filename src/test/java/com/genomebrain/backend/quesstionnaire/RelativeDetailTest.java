package com.genomebrain.backend.quesstionnaire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RelativeDetail}.
 */
@DisplayName("RelativeDetail Unit Tests")
class RelativeDetailTest {

    private RelativeDetail relativeDetail;

    @BeforeEach
    void setUp() {
        relativeDetail = new RelativeDetail();
    }

    @Test
    @DisplayName("Should get and set relativeID")
    void testRelativeID() {
        relativeDetail.setRelativeID("REL001");
        assertThat(relativeDetail.getRelativeID()).isEqualTo("REL001");
    }

    @Test
    @DisplayName("Should get and set relativeType")
    void testRelativeType() {
        relativeDetail.setRelativeType("Mother");
        assertThat(relativeDetail.getRelativeType()).isEqualTo("Mother");
    }

    @Test
    @DisplayName("Should get and set relativeName")
    void testRelativeName() {
        relativeDetail.setRelativeName("Jane Doe");
        assertThat(relativeDetail.getRelativeName()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Should get and set relativeAge")
    void testRelativeAge() {
        relativeDetail.setRelativeAge("65");
        assertThat(relativeDetail.getRelativeAge()).isEqualTo("65");
    }

    @Test
    @DisplayName("Should get and set relativeGender")
    void testRelativeGender() {
        relativeDetail.setRelativeGender("Female");
        assertThat(relativeDetail.getRelativeGender()).isEqualTo("Female");
    }

    @Test
    @DisplayName("Should get and set deceasedStatus")
    void testDeceasedStatus() {
        relativeDetail.setDeceasedStatus("Deceased");
        assertThat(relativeDetail.getDeceasedStatus()).isEqualTo("Deceased");
    }

    @Test
    @DisplayName("Should get and set relativeLabel")
    void testRelativeLabel() {
        relativeDetail.setRelativeLabel("Mother");
        assertThat(relativeDetail.getRelativeLabel()).isEqualTo("Mother");
    }

    @Test
    @DisplayName("Should get and set adopted")
    void testAdopted() {
        relativeDetail.setAdopted(1);
        assertThat(relativeDetail.getAdopted()).isEqualTo(1);
        
        relativeDetail.setAdopted(0);
        assertThat(relativeDetail.getAdopted()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should get and set changeMarkedBy")
    void testChangeMarkedBy() {
        relativeDetail.setChangeMarkedBy("User123");
        assertThat(relativeDetail.getChangeMarkedBy()).isEqualTo("User123");
    }

    @Test
    @DisplayName("Should get and set infertile")
    void testInfertile() {
        relativeDetail.setInfertile("Yes");
        assertThat(relativeDetail.getInfertile()).isEqualTo("Yes");
    }

    @Test
    @DisplayName("Should get and set sab")
    void testSab() {
        relativeDetail.setSab("No");
        assertThat(relativeDetail.getSab()).isEqualTo("No");
    }

    @Test
    @DisplayName("Should get and set noIssue")
    void testNoIssue() {
        relativeDetail.setNoIssue("Yes");
        assertThat(relativeDetail.getNoIssue()).isEqualTo("Yes");
    }

    @Test
    @DisplayName("Should get and set iconText")
    void testIconText() {
        relativeDetail.setIconText("M");
        assertThat(relativeDetail.getIconText()).isEqualTo("M");
    }

    @Test
    @DisplayName("Should get and set isTwin")
    void testIsTwin() {
        relativeDetail.setTwin(true);
        assertThat(relativeDetail.isTwin()).isTrue();
        
        relativeDetail.setTwin(false);
        assertThat(relativeDetail.isTwin()).isFalse();
    }

    @Test
    @DisplayName("Should get and set twinStyle")
    void testTwinStyle() {
        relativeDetail.setTwinStyle("identical");
        assertThat(relativeDetail.getTwinStyle()).isEqualTo("identical");
    }

    @Test
    @DisplayName("Should get and set nodeId")
    void testNodeId() {
        relativeDetail.setNodeId(1);
        assertThat(relativeDetail.getNodeId()).isEqualTo(1);
        
        relativeDetail.setNodeId(100);
        assertThat(relativeDetail.getNodeId()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should get and set connectedToNodeId")
    void testConnectedToNodeId() {
        relativeDetail.setConnectedToNodeId(2);
        assertThat(relativeDetail.getConnectedToNodeId()).isEqualTo(2);
        
        relativeDetail.setConnectedToNodeId(50);
        assertThat(relativeDetail.getConnectedToNodeId()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should get and set relativeUpn")
    void testRelativeUpn() {
        relativeDetail.setRelativeUpn(12345);
        assertThat(relativeDetail.getRelativeUpn()).isEqualTo(12345);
        
        relativeDetail.setRelativeUpn(null);
        assertThat(relativeDetail.getRelativeUpn()).isNull();
    }

    @Test
    @DisplayName("Should get and set knownParentGender")
    void testKnownParentGender() {
        relativeDetail.setKnownParentGender("Male");
        assertThat(relativeDetail.getKnownParentGender()).isEqualTo("Male");
    }

    @Test
    @DisplayName("Should get and set parentUpn")
    void testParentUpn() {
        relativeDetail.setParentUpn(54321);
        assertThat(relativeDetail.getParentUpn()).isEqualTo(54321);
    }

    @Test
    @DisplayName("Should get and set parent")
    void testParent() {
        Parent parent = new Parent();
        parent.setParentID("PAR001");
        parent.setType("Mother");
        relativeDetail.setParent(parent);
        assertThat(relativeDetail.getParent()).isEqualTo(parent);
    }

    @Test
    @DisplayName("Should get and set isUnaffected")
    void testIsUnaffected() {
        relativeDetail.setUnaffected(true);
        assertThat(relativeDetail.isUnaffected()).isTrue();
        
        relativeDetail.setUnaffected(false);
        assertThat(relativeDetail.isUnaffected()).isFalse();
    }

    @Test
    @DisplayName("Should get and set additionalNotes")
    void testAdditionalNotes() {
        relativeDetail.setAdditionalNotes("Some notes here");
        assertThat(relativeDetail.getAdditionalNotes()).isEqualTo("Some notes here");
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        relativeDetail.setRelativeID(null);
        assertThat(relativeDetail.getRelativeID()).isNull();
        
        relativeDetail.setRelativeType(null);
        assertThat(relativeDetail.getRelativeType()).isNull();
        
        relativeDetail.setRelativeName(null);
        assertThat(relativeDetail.getRelativeName()).isNull();
        
        relativeDetail.setRelativeAge(null);
        assertThat(relativeDetail.getRelativeAge()).isNull();
        
        relativeDetail.setRelativeGender(null);
        assertThat(relativeDetail.getRelativeGender()).isNull();
        
        relativeDetail.setDeceasedStatus(null);
        assertThat(relativeDetail.getDeceasedStatus()).isNull();
        
        relativeDetail.setRelativeLabel(null);
        assertThat(relativeDetail.getRelativeLabel()).isNull();
        
        relativeDetail.setChangeMarkedBy(null);
        assertThat(relativeDetail.getChangeMarkedBy()).isNull();
        
        relativeDetail.setInfertile(null);
        assertThat(relativeDetail.getInfertile()).isNull();
        
        relativeDetail.setSab(null);
        assertThat(relativeDetail.getSab()).isNull();
        
        relativeDetail.setNoIssue(null);
        assertThat(relativeDetail.getNoIssue()).isNull();
        
        relativeDetail.setIconText(null);
        assertThat(relativeDetail.getIconText()).isNull();
        
        relativeDetail.setTwinStyle(null);
        assertThat(relativeDetail.getTwinStyle()).isNull();
        
        relativeDetail.setKnownParentGender(null);
        assertThat(relativeDetail.getKnownParentGender()).isNull();
        
        relativeDetail.setParent(null);
        assertThat(relativeDetail.getParent()).isNull();
        
        relativeDetail.setAdditionalNotes(null);
        assertThat(relativeDetail.getAdditionalNotes()).isNull();
    }

    @Test
    @DisplayName("Should return correct toString")
    void testToString() {
        relativeDetail.setRelativeID("REL001");
        relativeDetail.setRelativeType("Mother");
        relativeDetail.setRelativeName("Jane Doe");
        relativeDetail.setRelativeAge("65");
        relativeDetail.setRelativeGender("Female");
        relativeDetail.setDeceasedStatus("Alive");
        relativeDetail.setRelativeLabel("Mother");
        relativeDetail.setAdopted(0);
        relativeDetail.setChangeMarkedBy("User123");
        relativeDetail.setInfertile("No");
        relativeDetail.setSab("No");
        relativeDetail.setNoIssue("No");
        relativeDetail.setIconText("M");
        relativeDetail.setNodeId(1);
        relativeDetail.setConnectedToNodeId(2);
        relativeDetail.setKnownParentGender("Male");
        relativeDetail.setParentUpn(54321);
        
        String result = relativeDetail.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("RelativeDetail");
        assertThat(result).contains("REL001");
        assertThat(result).contains("Mother");
        assertThat(result).contains("Jane Doe");
        assertThat(result).contains("65");
        assertThat(result).contains("Female");
        assertThat(result).contains("Alive");
        assertThat(result).contains("User123");
        assertThat(result).contains("nodeId=1");
        assertThat(result).contains("connectedToNodeId=2");
        assertThat(result).contains("parentUpn=54321");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        relativeDetail.setRelativeID("");
        assertThat(relativeDetail.getRelativeID()).isEmpty();
        
        relativeDetail.setRelativeType("");
        assertThat(relativeDetail.getRelativeType()).isEmpty();
        
        relativeDetail.setRelativeName("");
        assertThat(relativeDetail.getRelativeName()).isEmpty();
    }

    @Test
    @DisplayName("Should handle negative values for adopted")
    void testNegativeAdopted() {
        relativeDetail.setAdopted(-1);
        assertThat(relativeDetail.getAdopted()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle negative values for nodeId")
    void testNegativeNodeId() {
        relativeDetail.setNodeId(-1);
        assertThat(relativeDetail.getNodeId()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle negative values for connectedToNodeId")
    void testNegativeConnectedToNodeId() {
        relativeDetail.setConnectedToNodeId(-1);
        assertThat(relativeDetail.getConnectedToNodeId()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle negative values for parentUpn")
    void testNegativeParentUpn() {
        relativeDetail.setParentUpn(-1);
        assertThat(relativeDetail.getParentUpn()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle toString with null values")
    void testToStringWithNullValues() {
        // Set only some fields to null
        relativeDetail.setRelativeID("REL001");
        relativeDetail.setRelativeType(null);
        relativeDetail.setRelativeName(null);
        relativeDetail.setRelativeAge(null);
        relativeDetail.setRelativeGender(null);
        relativeDetail.setDeceasedStatus(null);
        relativeDetail.setRelativeLabel(null);
        relativeDetail.setChangeMarkedBy(null);
        relativeDetail.setKnownParentGender(null);
        relativeDetail.setParent(null);
        
        String result = relativeDetail.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("RelativeDetail");
        assertThat(result).contains("REL001");
    }

    @Test
    @DisplayName("Should handle toString with all fields set")
    void testToStringWithAllFields() {
        relativeDetail.setRelativeID("REL001");
        relativeDetail.setRelativeType("Mother");
        relativeDetail.setRelativeName("Jane Doe");
        relativeDetail.setRelativeAge("65");
        relativeDetail.setRelativeGender("Female");
        relativeDetail.setDeceasedStatus("Alive");
        relativeDetail.setRelativeLabel("Mother");
        relativeDetail.setAdopted(0);
        relativeDetail.setChangeMarkedBy("User123");
        relativeDetail.setInfertile("No");
        relativeDetail.setSab("No");
        relativeDetail.setNoIssue("No");
        relativeDetail.setIconText("M");
        relativeDetail.setTwin(true);
        relativeDetail.setTwinStyle("identical");
        relativeDetail.setNodeId(1);
        relativeDetail.setConnectedToNodeId(2);
        relativeDetail.setRelativeUpn(12345);
        relativeDetail.setKnownParentGender("Male");
        relativeDetail.setParentUpn(54321);
        
        Parent parent = new Parent();
        parent.setParentID("PAR001");
        relativeDetail.setParent(parent);
        relativeDetail.setUnaffected(false);
        relativeDetail.setAdditionalNotes("Test notes");
        
        String result = relativeDetail.toString();
        
        assertThat(result).isNotNull();
        assertThat(result).contains("RelativeDetail");
    }
}

