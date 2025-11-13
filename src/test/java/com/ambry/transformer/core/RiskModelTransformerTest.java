package com.ambry.transformer.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RiskModelTransformer Unit Tests")
class RiskModelTransformerTest {

    private TestRiskModelTransformer transformer;
    private Risk mockRisk;
    private Model mockModel;
    private Person mockPerson1;
    private Person mockPerson2;

    @BeforeEach
    void setUp() {
        transformer = new TestRiskModelTransformer();
        mockRisk = mock(Risk.class);
        mockModel = mock(Model.class);
        mockPerson1 = mock(Person.class);
        mockPerson2 = mock(Person.class);
    }

    @Test
    @DisplayName("Should map family with single person")
    void testMapFamilyWithSinglePerson() {
        when(mockRisk.getRequestedModel(any(ModelName.class))).thenReturn(mockModel);
        when(mockModel.getAgeBy()).thenReturn(20);
        when(mockModel.getAgeTo()).thenReturn(80);
        when(mockModel.getChartCode()).thenReturn(1);
        when(mockModel.getInstructions()).thenReturn(new ArrayList<>());
        when(mockRisk.isSaveIncoming()).thenReturn(true);
        when(mockRisk.isConsent()).thenReturn(true);
        when(mockRisk.getTransId()).thenReturn("TRANS001");
        when(mockRisk.getFamilyId()).thenReturn("FAM001");
        when(mockRisk.getFamily()).thenReturn(Arrays.asList(mockPerson1));
        
        when(mockPerson1.getId()).thenReturn(1);
        when(mockPerson1.getMotherId()).thenReturn(0);
        when(mockPerson1.getFatherId()).thenReturn(0);
        when(mockPerson1.getAge()).thenReturn(45);
        when(mockPerson1.getAgeOfDeath()).thenReturn(null);
        when(mockPerson1.getGender()).thenReturn(0);
        when(mockPerson1.getTransId()).thenReturn("TRANS001");
        when(mockPerson1.getTestOrder()).thenReturn(1);
        when(mockPerson1.getTwin()).thenReturn(0);
        when(mockPerson1.getVitalStatus()).thenReturn(BasePerson.VitalStatus.ALIVE);
        when(mockPerson1.getGermline()).thenReturn(new HashMap<>());

        BaseRisk result = transformer.mapFamily(mockRisk);

        assertThat(result).isNotNull();
        // Verify that the result was created and persons were added
        assertThat(transformer.getAddedPersons()).hasSize(1);
    }

    @Test
    @DisplayName("Should map family with multiple persons")
    void testMapFamilyWithMultiplePersons() {
        when(mockRisk.getRequestedModel(any(ModelName.class))).thenReturn(mockModel);
        when(mockModel.getAgeBy()).thenReturn(20);
        when(mockModel.getAgeTo()).thenReturn(80);
        when(mockModel.getChartCode()).thenReturn(1);
        when(mockModel.getInstructions()).thenReturn(new ArrayList<>());
        when(mockRisk.isSaveIncoming()).thenReturn(false);
        when(mockRisk.isConsent()).thenReturn(false);
        when(mockRisk.getTransId()).thenReturn("TRANS002");
        when(mockRisk.getFamilyId()).thenReturn("FAM002");
        when(mockRisk.getFamily()).thenReturn(Arrays.asList(mockPerson1, mockPerson2));
        
        setupMockPerson(mockPerson1, 1, 0, 0, 45);
        setupMockPerson(mockPerson2, 2, 1, 0, 50);

        BaseRisk result = transformer.mapFamily(mockRisk);

        assertThat(result).isNotNull();
        assertThat(transformer.getAddedPersons()).hasSize(2);
    }

    @Test
    @DisplayName("Should map model risk fields with instructions")
    void testMapModelRiskFieldsWithInstructions() {
        Risk risk = mock(Risk.class);
        BaseRisk baseRisk = mock(BaseRisk.class);
        Model model = mock(Model.class);
        
        Instruction instruction1 = mock(Instruction.class);
        when(instruction1.getName()).thenReturn("INST1");
        when(instruction1.getValue()).thenReturn("VALUE1");
        Instruction instruction2 = mock(Instruction.class);
        when(instruction2.getName()).thenReturn("INST2");
        when(instruction2.getValue()).thenReturn("VALUE2");
        when(model.getInstructions()).thenReturn(Arrays.asList(instruction1, instruction2));
        
        when(risk.isSaveIncoming()).thenReturn(true);
        when(risk.isConsent()).thenReturn(true);
        when(risk.getTransId()).thenReturn("TRANS001");
        when(risk.getFamilyId()).thenReturn("FAM001");

        transformer.mapModelRiskFields(risk, baseRisk, model);

        verify(baseRisk).setSaveIncoming(true);
        verify(baseRisk).setConsent(true);
        verify(baseRisk).setTransId("TRANS001");
        verify(baseRisk).setFamilyId("FAM001");
    }

    // Note: mapCommonFields is private, so we test it indirectly through mapFamily

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        when(mockRisk.getRequestedModel(any(ModelName.class))).thenReturn(mockModel);
        when(mockModel.getAgeBy()).thenReturn(20);
        when(mockModel.getAgeTo()).thenReturn(80);
        when(mockModel.getChartCode()).thenReturn(1);
        when(mockModel.getInstructions()).thenReturn(new ArrayList<>());
        when(mockRisk.isSaveIncoming()).thenReturn(true);
        when(mockRisk.isConsent()).thenReturn(true);
        when(mockRisk.getTransId()).thenReturn("TRANS001");
        when(mockRisk.getFamilyId()).thenReturn("FAM001");
        when(mockRisk.getFamily()).thenReturn(new ArrayList<>());

        BaseRisk result = transformer.mapFamily(mockRisk);

        assertThat(result).isNotNull();
        assertThat(transformer.getAddedPersons()).isEmpty();
    }

    // Note: mapCommonFields is private, so we test it indirectly through mapFamily

    @Test
    @DisplayName("Should handle empty instructions list")
    void testMapModelRiskFieldsWithEmptyInstructions() {
        Risk risk = mock(Risk.class);
        BaseRisk baseRisk = mock(BaseRisk.class);
        Model model = mock(Model.class);
        
        when(model.getInstructions()).thenReturn(new ArrayList<>());
        when(risk.isSaveIncoming()).thenReturn(false);
        when(risk.isConsent()).thenReturn(false);
        when(risk.getTransId()).thenReturn("TRANS002");
        when(risk.getFamilyId()).thenReturn("FAM002");

        transformer.mapModelRiskFields(risk, baseRisk, model);

        verify(baseRisk).setSaveIncoming(false);
        verify(baseRisk).setConsent(false);
        verify(baseRisk).setTransId("TRANS002");
        verify(baseRisk).setFamilyId("FAM002");
    }

    // Note: mapCommonFields is private, so we test it indirectly through mapFamily

    @Test
    @DisplayName("Should handle null instructions with NPE")
    void testMapModelRiskFieldsWithNullInstructions() {
        Risk risk = mock(Risk.class);
        BaseRisk baseRisk = mock(BaseRisk.class);
        Model model = mock(Model.class);
        
        when(model.getInstructions()).thenReturn(null);
        when(risk.isSaveIncoming()).thenReturn(true);
        when(risk.isConsent()).thenReturn(true);
        when(risk.getTransId()).thenReturn("TRANS001");
        when(risk.getFamilyId()).thenReturn("FAM001");

        // This will throw NPE because mapModelRiskFields iterates over instructions
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> 
            transformer.mapModelRiskFields(risk, baseRisk, model))
            .isInstanceOf(NullPointerException.class);
    }

    private void setupMockPerson(Person person, int id, int motherId, int fatherId, int age) {
        when(person.getId()).thenReturn(id);
        when(person.getMotherId()).thenReturn(motherId);
        when(person.getFatherId()).thenReturn(fatherId);
        when(person.getAge()).thenReturn(age);
        when(person.getAgeOfDeath()).thenReturn(null);
        when(person.getGender()).thenReturn(0);
        when(person.getTransId()).thenReturn("TRANS001");
        when(person.getTestOrder()).thenReturn(1);
        when(person.getTwin()).thenReturn(0);
        when(person.getVitalStatus()).thenReturn(BasePerson.VitalStatus.ALIVE);
        when(person.getGermline()).thenReturn(new HashMap<>());
    }

    // Test implementation of RiskModelTransformer
    private static class TestRiskModelTransformer extends RiskModelTransformer {
        private List<BasePerson> addedPersons = new ArrayList<>();

        @Override
        protected void add(BaseRisk fam, BasePerson basePerson) {
            addedPersons.add(basePerson);
        }

        @Override
        protected void mapModelFields(Person person, BasePerson basePerson) {
            // Test implementation - do nothing
        }

        @Override
        protected BaseRisk createRequestObject() {
            BaseRisk risk = mock(BaseRisk.class);
            when(risk.getModel()).thenReturn("GAIL");
            return risk;
        }

        @Override
        protected BasePerson createPersonObject() {
            BasePerson person = mock(BasePerson.class);
            when(person.getGermline()).thenReturn(new HashMap<>());
            return person;
        }

        public List<BasePerson> getAddedPersons() {
            return addedPersons;
        }
    }
}

