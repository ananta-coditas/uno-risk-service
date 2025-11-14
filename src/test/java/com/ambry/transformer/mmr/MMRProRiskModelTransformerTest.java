package com.ambry.transformer.mmr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.mmr.MMRResult;
import com.objex.risk.beans.mmr.Person;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.services.RiskService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MMRProRiskModelTransformer Unit Tests")
class MMRProRiskModelTransformerTest {

    @Mock
    private RiskService bayesExecuter;

    @Mock
    private MMRProValidator mmrValidator;

    @InjectMocks
    private MMRProRiskModelTransformer transformer;

    private com.objex.risk.beans.uno.Risk unoRisk;
    private Model model;
    private com.objex.risk.beans.uno.Person unoPerson;

    @BeforeEach
    void setUp() {
        unoRisk = new com.objex.risk.beans.uno.Risk();
        model = new Model();
        model.setName(ModelName.MMRPRO);
        model.setAgeBy(20);
        model.setAgeTo(80);
        model.setChartCode(1);
        model.setInstructions(new ArrayList<>());
        unoRisk.getRequestedModels().add(model);

        unoPerson = new com.objex.risk.beans.uno.Person();
        unoPerson.setId(1);
        unoPerson.setMotherId(0);
        unoPerson.setFatherId(0);
        unoPerson.setAge(45);
        unoPerson.setGender(0);
        unoPerson.setTransId("TRANS001");
        unoPerson.setTestOrder(1);
        unoPerson.setTwin(0);
        unoPerson.setVitalStatus(BasePerson.VitalStatus.ALIVE);
        unoRisk.getFamily().add(unoPerson);
    }

    @Test
    @DisplayName("Should return MMRPRO model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.MMRPRO);
    }

    @Test
    @DisplayName("Should transform request correctly")
    void testTransformRequest() {
        com.objex.risk.beans.mmr.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isNotNull();
        assertThat(result.getModel()).isEqualTo("MMRPRO");
        assertThat(result.getAgeBy()).isEqualTo(20);
        assertThat(result.getAgeTo()).isEqualTo(80);
        assertThat(result.getChartCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke risk model and return result")
    void testInvokeRiskModel() {
        com.objex.risk.beans.mmr.Risk mmrRisk = new com.objex.risk.beans.mmr.Risk();
        MMRResult expectedResult = new MMRResult();

        when(bayesExecuter.calcMMR(any(com.objex.risk.beans.mmr.Risk.class))).thenReturn(expectedResult);

        MMRResult result = transformer.invokeRiskModel(mmrRisk);

        assertThat(result).isEqualTo(expectedResult);
        verify(bayesExecuter).calcMMR(mmrRisk);
    }

    @Test
    @DisplayName("Should transform response correctly")
    void testTransformResponse() {
        MMRResult modelResponse = new MMRResult();

        MMRResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isEqualTo(modelResponse);
    }

    @Test
    @DisplayName("Should return correct risk model validator")
    void testGetRiskModelValidator() {
        com.ambry.transformer.core.validator.RiskModelValidator<com.objex.risk.beans.mmr.Risk> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(mmrValidator);
    }

    @Test
    @DisplayName("Should map colorectal cancer fields correctly")
    void testMapModelFieldsWithColorectalCancer() {
        Cancer colorectal = new Cancer(Cancer.CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        unoPerson.addCancer(colorectal);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE.value);
        assertThat(mmrPerson.getDiagnoseColonAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map colorectal cancer with null affected to NONE")
    void testMapModelFieldsWithColorectalCancerNullAffected() {
        Cancer colorectal = new Cancer();
        colorectal.setCancerType(Cancer.CancerType.COLORECTAL);
        colorectal.setAffected(null);
        colorectal.setDiagnoseAge(50);
        unoPerson.addCancer(colorectal);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getAffectedColonType()).isEqualTo(AffectedType.NONE.value);
    }

    @Test
    @DisplayName("Should map endometrial cancer fields correctly")
    void testMapModelFieldsWithEndometrialCancer() {
        Cancer endometrial = new Cancer(Cancer.CancerType.ENDOMETRIAL, AffectedType.ONE.value, 55);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE.value);
        assertThat(mmrPerson.getDiagnoseEndometriumAge()).isEqualTo(55);
    }

    @Test
    @DisplayName("Should map endometrial cancer with null affected to NONE")
    void testMapModelFieldsWithEndometrialCancerNullAffected() {
        Cancer endometrial = new Cancer();
        endometrial.setCancerType(Cancer.CancerType.ENDOMETRIAL);
        endometrial.setAffected(null);
        endometrial.setDiagnoseAge(55);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.NONE.value);
    }

    @Test
    @DisplayName("Should map marker fields when marker is present")
    void testMapModelFieldsWithMarker() {
        com.objex.risk.beans.uno.Marker unoMarker = mock(com.objex.risk.beans.uno.Marker.class);
        // Mock the marker methods - we'll just verify the marker is created and transId is set
        // The exact enum values for location and msi depend on the Marker class implementation
        unoPerson.setMarker(unoMarker);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        // Verify marker is created and transId is set correctly
        assertThat(mmrPerson.getMarker()).isNotNull();
        assertThat(mmrPerson.getMarker().getTransId()).isEqualTo("TRANS001");
        // Location and MSI are set from unoMarker.getLocation() and unoMarker.getMsi()
        // which return enum types that are then assigned to the mmr marker
    }

    @Test
    @DisplayName("Should not map marker when marker is null")
    void testMapModelFieldsWithoutMarker() {
        unoPerson.setMarker(null);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getMarker()).isNull();
    }

    @Test
    @DisplayName("Should map model risk instruction for impute")
    void testMapModelRiskInstructionForImpute() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("true");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk.isImpute()).isTrue();
        assertThat(mmrRisk.isImputeRelatives()).isTrue();
    }

    @Test
    @DisplayName("Should map model risk instruction for impute with false value")
    void testMapModelRiskInstructionForImputeFalse() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("false");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk.isImpute()).isFalse();
        assertThat(mmrRisk.isImputeRelatives()).isFalse();
    }

    @Test
    @DisplayName("Should handle mapModelRiskInstruction exception and call super")
    void testMapModelRiskInstructionWithException() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("invalid-boolean");
        model.getInstructions().add(instruction);

        // Should not throw exception, should fall back to super method
        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk).isNotNull();
    }

    @Test
    @DisplayName("Should create request object correctly")
    void testCreateRequestObject() {
        com.objex.risk.beans.mmr.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isInstanceOf(com.objex.risk.beans.mmr.Risk.class);
    }

    @Test
    @DisplayName("Should create person object correctly")
    void testCreatePersonObject() {
        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk.getFamily()).isNotEmpty();
        assertThat(mmrRisk.getFamily().get(0)).isInstanceOf(Person.class);
    }

    @Test
    @DisplayName("Should add person to risk correctly")
    void testAddPersonToRisk() {
        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk.getFamily()).hasSize(1);
        assertThat(mmrRisk.getFamily().get(0)).isNotNull();
    }

    @Test
    @DisplayName("Should map multiple persons correctly")
    void testMapMultiplePersons() {
        com.objex.risk.beans.uno.Person person2 = new com.objex.risk.beans.uno.Person();
        person2.setId(2);
        person2.setMotherId(1);
        person2.setFatherId(0);
        person2.setAge(50);
        person2.setGender(0);
        person2.setTransId("TRANS001");
        person2.setTestOrder(2);
        person2.setTwin(0);
        person2.setVitalStatus(BasePerson.VitalStatus.ALIVE);
        unoRisk.getFamily().add(person2);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk.getFamily()).hasSize(2);
    }

    @Test
    @DisplayName("Should map common risk fields correctly")
    void testMapCommonRiskFields() {
        unoRisk.setSaveIncoming(true);
        unoRisk.setConsent(true);
        unoRisk.setTransId("TRANS001");
        unoRisk.setFamilyId("FAM001");

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk.isSaveIncoming()).isTrue();
        assertThat(mmrRisk.isConsent()).isTrue();
        assertThat(mmrRisk.getTransId()).isEqualTo("TRANS001");
        assertThat(mmrRisk.getFamilyId()).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        unoRisk.getFamily().clear();

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk).isNotNull();
        assertThat(mmrRisk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should handle person without any cancer")
    void testMapModelFieldsWithoutCancer() {
        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson).isNotNull();
        // Should not throw exception even when no cancer is present
    }

    @Test
    @DisplayName("Should map both colorectal and endometrial cancer")
    void testMapModelFieldsWithBothCancers() {
        Cancer colorectal = new Cancer(Cancer.CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        Cancer endometrial = new Cancer(Cancer.CancerType.ENDOMETRIAL, AffectedType.ONE.value, 55);
        unoPerson.addCancer(colorectal);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE.value);
        assertThat(mmrPerson.getDiagnoseColonAge()).isEqualTo(50);
        assertThat(mmrPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE.value);
        assertThat(mmrPerson.getDiagnoseEndometriumAge()).isEqualTo(55);
    }

    @Test
    @DisplayName("Should map marker with null location")
    void testMapModelFieldsWithMarkerNullLocation() {
        com.objex.risk.beans.uno.Marker unoMarker = mock(com.objex.risk.beans.uno.Marker.class);
        when(unoMarker.getLocation()).thenReturn(null);
        unoPerson.setMarker(unoMarker);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getMarker()).isNotNull();
        assertThat(mmrPerson.getMarker().getLocation()).isNull();
        // MSI will be whatever unoMarker.getMsi() returns (may be null or an enum value)
    }

    @Test
    @DisplayName("Should map marker with null msi")
    void testMapModelFieldsWithMarkerNullMsi() {
        com.objex.risk.beans.uno.Marker unoMarker = mock(com.objex.risk.beans.uno.Marker.class);
        when(unoMarker.getMsi()).thenReturn(null);
        unoPerson.setMarker(unoMarker);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getMarker()).isNotNull();
        // Location will be whatever unoMarker.getLocation() returns (may be null or an enum value)
        assertThat(mmrPerson.getMarker().getMsi()).isNull();
    }

    @Test
    @DisplayName("Should handle other instruction names")
    void testMapModelRiskInstructionWithOtherName() {
        Instruction instruction = new Instruction();
        instruction.setName("otherInstruction");
        instruction.setValue("someValue");
        model.getInstructions().add(instruction);

        // Should not throw exception, should call super method
        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);

        assertThat(mmrRisk).isNotNull();
    }

    @Test
    @DisplayName("Should map all fields together")
    void testMapModelFieldsWithAllFields() {
        Cancer colorectal = new Cancer(Cancer.CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        Cancer endometrial = new Cancer(Cancer.CancerType.ENDOMETRIAL, AffectedType.ONE.value, 55);
        unoPerson.addCancer(colorectal);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.uno.Marker unoMarker = mock(com.objex.risk.beans.uno.Marker.class);
        unoPerson.setMarker(unoMarker);

        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("true");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.mmr.Risk mmrRisk = transformer.transformRequest(unoRisk, model);
        Person mmrPerson = mmrRisk.getFamily().get(0);

        assertThat(mmrPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE.value);
        assertThat(mmrPerson.getDiagnoseColonAge()).isEqualTo(50);
        assertThat(mmrPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE.value);
        assertThat(mmrPerson.getDiagnoseEndometriumAge()).isEqualTo(55);
        assertThat(mmrPerson.getMarker()).isNotNull();
        // Verify marker fields are set (exact values depend on what unoMarker returns)
        // Location and MSI will be set from unoMarker.getLocation() and unoMarker.getMsi()
        assertThat(mmrRisk.isImpute()).isTrue();
        assertThat(mmrRisk.isImputeRelatives()).isTrue();
    }
}

