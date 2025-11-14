package com.ambry.transformer.panc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import com.objex.risk.beans.panc.PancResult;
import com.objex.risk.beans.panc.Person;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.services.RiskService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PancProRiskModelTransformer Unit Tests")
class PancProRiskModelTransformerTest {

    @Mock
    private RiskService bayesExecuter;

    @Mock
    private PancProValidator pancValidator;

    @InjectMocks
    private PancProRiskModelTransformer transformer;

    private com.objex.risk.beans.uno.Risk unoRisk;
    private Model model;
    private com.objex.risk.beans.uno.Person unoPerson;

    @BeforeEach
    void setUp() {
        unoRisk = new com.objex.risk.beans.uno.Risk();
        model = new Model();
        model.setName(ModelName.PANCPRO);
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
    @DisplayName("Should return PANCPRO model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.PANCPRO);
    }

    @Test
    @DisplayName("Should transform request correctly")
    void testTransformRequest() {
        com.objex.risk.beans.panc.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isNotNull();
        assertThat(result.getModel()).isEqualTo("PANCPRO");
        assertThat(result.getAgeBy()).isEqualTo(20);
        assertThat(result.getAgeTo()).isEqualTo(80);
        assertThat(result.getChartCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke risk model and return result")
    void testInvokeRiskModel() {
        com.objex.risk.beans.panc.Risk pancRisk = new com.objex.risk.beans.panc.Risk();
        PancResult expectedResult = new PancResult();

        when(bayesExecuter.calcPanc(any(com.objex.risk.beans.panc.Risk.class))).thenReturn(expectedResult);

        PancResult result = transformer.invokeRiskModel(pancRisk);

        assertThat(result).isEqualTo(expectedResult);
        verify(bayesExecuter).calcPanc(pancRisk);
    }

    @Test
    @DisplayName("Should transform response correctly")
    void testTransformResponse() {
        PancResult modelResponse = new PancResult();

        PancResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isEqualTo(modelResponse);
    }

    @Test
    @DisplayName("Should return correct risk model validator")
    void testGetRiskModelValidator() {
        com.ambry.transformer.core.validator.RiskModelValidator<com.objex.risk.beans.panc.Risk> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(pancValidator);
    }

    @Test
    @DisplayName("Should map pancreatic cancer fields correctly")
    void testMapModelFieldsWithPancreaticCancer() {
        Cancer pancreas = new Cancer(Cancer.CancerType.PANCREATIC, AffectedType.ONE.value, 50);
        unoPerson.addCancer(pancreas);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        assertThat(pancPerson.getAffectedPancreasType()).isEqualTo(AffectedType.ONE.value);
        assertThat(pancPerson.getDiagnosePancreasAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map pancreatic cancer with null affected to NONE")
    void testMapModelFieldsWithPancreaticCancerNullAffected() {
        Cancer pancreas = new Cancer();
        pancreas.setCancerType(Cancer.CancerType.PANCREATIC);
        pancreas.setAffected(null);
        pancreas.setDiagnoseAge(50);
        unoPerson.addCancer(pancreas);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        assertThat(pancPerson.getAffectedPancreasType()).isEqualTo(AffectedType.NONE.value);
    }

    @Test
    @DisplayName("Should map model risk instruction for impute")
    void testMapModelRiskInstructionForImpute() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("true");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk.isImpute()).isTrue();
        assertThat(pancRisk.isImputeRelatives()).isTrue();
    }

    @Test
    @DisplayName("Should map model risk instruction for impute with false value")
    void testMapModelRiskInstructionForImputeFalse() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("false");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk.isImpute()).isFalse();
        assertThat(pancRisk.isImputeRelatives()).isFalse();
    }

    @Test
    @DisplayName("Should handle mapModelRiskInstruction exception and call super")
    void testMapModelRiskInstructionWithException() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("invalid-boolean");
        model.getInstructions().add(instruction);

        // Should not throw exception, should fall back to super method
        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk).isNotNull();
    }

    @Test
    @DisplayName("Should create request object correctly")
    void testCreateRequestObject() {
        com.objex.risk.beans.panc.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isInstanceOf(com.objex.risk.beans.panc.Risk.class);
    }

    @Test
    @DisplayName("Should create person object correctly")
    void testCreatePersonObject() {
        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk.getFamily()).isNotEmpty();
        assertThat(pancRisk.getFamily().get(0)).isInstanceOf(Person.class);
    }

    @Test
    @DisplayName("Should add person to risk correctly")
    void testAddPersonToRisk() {
        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk.getFamily()).hasSize(1);
        assertThat(pancRisk.getFamily().get(0)).isNotNull();
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

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk.getFamily()).hasSize(2);
    }

    @Test
    @DisplayName("Should map common risk fields correctly")
    void testMapCommonRiskFields() {
        unoRisk.setSaveIncoming(true);
        unoRisk.setConsent(true);
        unoRisk.setTransId("TRANS001");
        unoRisk.setFamilyId("FAM001");

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk.isSaveIncoming()).isTrue();
        assertThat(pancRisk.isConsent()).isTrue();
        assertThat(pancRisk.getTransId()).isEqualTo("TRANS001");
        assertThat(pancRisk.getFamilyId()).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        unoRisk.getFamily().clear();

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk).isNotNull();
        assertThat(pancRisk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should handle person without pancreatic cancer")
    void testMapModelFieldsWithoutPancreaticCancer() {
        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        assertThat(pancPerson).isNotNull();
        // Should not throw exception even when no pancreatic cancer is present
    }

    @Test
    @DisplayName("Should map all fields together")
    void testMapModelFieldsWithAllFields() {
        Cancer pancreas = new Cancer(Cancer.CancerType.PANCREATIC, AffectedType.ONE.value, 50);
        unoPerson.addCancer(pancreas);

        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("true");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        assertThat(pancPerson.getAffectedPancreasType()).isEqualTo(AffectedType.ONE.value);
        assertThat(pancPerson.getDiagnosePancreasAge()).isEqualTo(50);
        assertThat(pancRisk.isImpute()).isTrue();
        assertThat(pancRisk.isImputeRelatives()).isTrue();
    }

    @Test
    @DisplayName("Should handle other instruction names")
    void testMapModelRiskInstructionWithOtherName() {
        Instruction instruction = new Instruction();
        instruction.setName("otherInstruction");
        instruction.setValue("someValue");
        model.getInstructions().add(instruction);

        // Should not throw exception, should call super method
        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);

        assertThat(pancRisk).isNotNull();
    }

    @Test
    @DisplayName("Should handle pancreatic cancer without diagnose age")
    void testMapModelFieldsWithPancreaticCancerWithoutDiagnoseAge() {
        Cancer pancreas = new Cancer();
        pancreas.setCancerType(Cancer.CancerType.PANCREATIC);
        pancreas.setAffected(AffectedType.ONE.value);
        pancreas.setDiagnoseAge(0);
        unoPerson.addCancer(pancreas);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        assertThat(pancPerson.getAffectedPancreasType()).isEqualTo(AffectedType.ONE.value);
        assertThat(pancPerson.getDiagnosePancreasAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple pancreatic cancers")
    void testMapModelFieldsWithMultiplePancreaticCancers() {
        // Note: Person can have multiple cancers, but getCancer(CancerType.PANCREATIC) returns the first one
        Cancer pancreas1 = new Cancer(Cancer.CancerType.PANCREATIC, AffectedType.ONE.value, 40);
        Cancer pancreas2 = new Cancer(Cancer.CancerType.PANCREATIC, AffectedType.ONE.value, 50);
        unoPerson.addCancer(pancreas1);
        unoPerson.addCancer(pancreas2);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        // Should map the first pancreatic cancer found
        assertThat(pancPerson.getAffectedPancreasType()).isEqualTo(AffectedType.ONE.value);
    }

    @Test
    @DisplayName("Should handle person with other cancer types")
    void testMapModelFieldsWithOtherCancerTypes() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        // Should not map breast cancer, only pancreatic cancer
        assertThat(pancPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle person with both pancreatic and other cancers")
    void testMapModelFieldsWithPancreaticAndOtherCancers() {
        Cancer pancreas = new Cancer(Cancer.CancerType.PANCREATIC, AffectedType.ONE.value, 50);
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        unoPerson.addCancer(pancreas);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.panc.Risk pancRisk = transformer.transformRequest(unoRisk, model);
        Person pancPerson = pancRisk.getFamily().get(0);

        // Should map pancreatic cancer
        assertThat(pancPerson.getAffectedPancreasType()).isEqualTo(AffectedType.ONE.value);
        assertThat(pancPerson.getDiagnosePancreasAge()).isEqualTo(50);
    }
}

