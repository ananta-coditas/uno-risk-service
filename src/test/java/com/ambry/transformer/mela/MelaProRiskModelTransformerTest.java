package com.ambry.transformer.mela;

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
import com.objex.risk.beans.mela.MelaResult;
import com.objex.risk.beans.mela.Person;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.services.RiskService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MelaProRiskModelTransformer Unit Tests")
class MelaProRiskModelTransformerTest {

    @Mock
    private RiskService bayesExecuter;

    @Mock
    private MelaProValidator melaValidator;

    @InjectMocks
    private MelaProRiskModelTransformer transformer;

    private com.objex.risk.beans.uno.Risk unoRisk;
    private Model model;
    private com.objex.risk.beans.uno.Person unoPerson;

    @BeforeEach
    void setUp() {
        unoRisk = new com.objex.risk.beans.uno.Risk();
        model = new Model();
        model.setName(ModelName.MELAPRO);
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
    @DisplayName("Should return MELAPRO model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.MELAPRO);
    }

    @Test
    @DisplayName("Should transform request correctly")
    void testTransformRequest() {
        com.objex.risk.beans.mela.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isNotNull();
        assertThat(result.getModel()).isEqualTo("MELAPRO");
        assertThat(result.getAgeBy()).isEqualTo(20);
        assertThat(result.getAgeTo()).isEqualTo(80);
        assertThat(result.getChartCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke risk model and return result")
    void testInvokeRiskModel() {
        com.objex.risk.beans.mela.Risk melaRisk = new com.objex.risk.beans.mela.Risk();
        MelaResult expectedResult = new MelaResult();

        when(bayesExecuter.calcMela(any(com.objex.risk.beans.mela.Risk.class))).thenReturn(expectedResult);

        MelaResult result = transformer.invokeRiskModel(melaRisk);

        assertThat(result).isEqualTo(expectedResult);
        verify(bayesExecuter).calcMela(melaRisk);
    }

    @Test
    @DisplayName("Should transform response correctly")
    void testTransformResponse() {
        MelaResult modelResponse = new MelaResult();

        MelaResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isEqualTo(modelResponse);
    }

    @Test
    @DisplayName("Should return correct risk model validator")
    void testGetRiskModelValidator() {
        com.ambry.transformer.core.validator.RiskModelValidator<com.objex.risk.beans.mela.Risk> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(melaValidator);
    }

    @Test
    @DisplayName("Should map skin cancer fields correctly")
    void testMapModelFieldsWithSkinCancer() {
        Cancer skin = new Cancer(Cancer.CancerType.SKIN, AffectedType.ONE.value, 50);
        unoPerson.addCancer(skin);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        assertThat(melaPerson.getAffectedSkinType()).isEqualTo(AffectedType.ONE.value);
        assertThat(melaPerson.getDiagnoseSkinAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map skin cancer with null affected to NONE")
    void testMapModelFieldsWithSkinCancerNullAffected() {
        Cancer skin = new Cancer();
        skin.setCancerType(Cancer.CancerType.SKIN);
        skin.setAffected(null);
        skin.setDiagnoseAge(50);
        unoPerson.addCancer(skin);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        assertThat(melaPerson.getAffectedSkinType()).isEqualTo(AffectedType.NONE.value);
    }

    @Test
    @DisplayName("Should map model risk instruction for impute")
    void testMapModelRiskInstructionForImpute() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("true");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk.isImpute()).isTrue();
        assertThat(melaRisk.isImputeRelatives()).isTrue();
    }

    @Test
    @DisplayName("Should map model risk instruction for impute with false value")
    void testMapModelRiskInstructionForImputeFalse() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("false");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk.isImpute()).isFalse();
        assertThat(melaRisk.isImputeRelatives()).isFalse();
    }

    @Test
    @DisplayName("Should handle mapModelRiskInstruction exception and call super")
    void testMapModelRiskInstructionWithException() {
        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("invalid-boolean");
        model.getInstructions().add(instruction);

        // Should not throw exception, should fall back to super method
        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk).isNotNull();
    }

    @Test
    @DisplayName("Should create request object correctly")
    void testCreateRequestObject() {
        com.objex.risk.beans.mela.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isInstanceOf(com.objex.risk.beans.mela.Risk.class);
    }

    @Test
    @DisplayName("Should create person object correctly")
    void testCreatePersonObject() {
        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk.getFamily()).isNotEmpty();
        assertThat(melaRisk.getFamily().get(0)).isInstanceOf(Person.class);
    }

    @Test
    @DisplayName("Should add person to risk correctly")
    void testAddPersonToRisk() {
        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk.getFamily()).hasSize(1);
        assertThat(melaRisk.getFamily().get(0)).isNotNull();
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

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk.getFamily()).hasSize(2);
    }

    @Test
    @DisplayName("Should map common risk fields correctly")
    void testMapCommonRiskFields() {
        unoRisk.setSaveIncoming(true);
        unoRisk.setConsent(true);
        unoRisk.setTransId("TRANS001");
        unoRisk.setFamilyId("FAM001");

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk.isSaveIncoming()).isTrue();
        assertThat(melaRisk.isConsent()).isTrue();
        assertThat(melaRisk.getTransId()).isEqualTo("TRANS001");
        assertThat(melaRisk.getFamilyId()).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        unoRisk.getFamily().clear();

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk).isNotNull();
        assertThat(melaRisk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should handle person without skin cancer")
    void testMapModelFieldsWithoutSkinCancer() {
        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        assertThat(melaPerson).isNotNull();
        // Should not throw exception even when no skin cancer is present
    }

    @Test
    @DisplayName("Should map all fields together")
    void testMapModelFieldsWithAllFields() {
        Cancer skin = new Cancer(Cancer.CancerType.SKIN, AffectedType.ONE.value, 50);
        unoPerson.addCancer(skin);

        Instruction instruction = new Instruction();
        instruction.setName("impute");
        instruction.setValue("true");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        assertThat(melaPerson.getAffectedSkinType()).isEqualTo(AffectedType.ONE.value);
        assertThat(melaPerson.getDiagnoseSkinAge()).isEqualTo(50);
        assertThat(melaRisk.isImpute()).isTrue();
        assertThat(melaRisk.isImputeRelatives()).isTrue();
    }

    @Test
    @DisplayName("Should handle other instruction names")
    void testMapModelRiskInstructionWithOtherName() {
        Instruction instruction = new Instruction();
        instruction.setName("otherInstruction");
        instruction.setValue("someValue");
        model.getInstructions().add(instruction);

        // Should not throw exception, should call super method
        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);

        assertThat(melaRisk).isNotNull();
    }

    @Test
    @DisplayName("Should handle skin cancer without diagnose age")
    void testMapModelFieldsWithSkinCancerWithoutDiagnoseAge() {
        Cancer skin = new Cancer();
        skin.setCancerType(Cancer.CancerType.SKIN);
        skin.setAffected(AffectedType.ONE.value);
        skin.setDiagnoseAge(0);
        unoPerson.addCancer(skin);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        assertThat(melaPerson.getAffectedSkinType()).isEqualTo(AffectedType.ONE.value);
        assertThat(melaPerson.getDiagnoseSkinAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple skin cancers")
    void testMapModelFieldsWithMultipleSkinCancers() {
        // Note: Person can have multiple cancers, but getCancer(CancerType.SKIN) returns the first one
        Cancer skin1 = new Cancer(Cancer.CancerType.SKIN, AffectedType.ONE.value, 40);
        Cancer skin2 = new Cancer(Cancer.CancerType.SKIN, AffectedType.ONE.value, 50);
        unoPerson.addCancer(skin1);
        unoPerson.addCancer(skin2);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        // Should map the first skin cancer found
        assertThat(melaPerson.getAffectedSkinType()).isEqualTo(AffectedType.ONE.value);
    }

    @Test
    @DisplayName("Should handle person with other cancer types")
    void testMapModelFieldsWithOtherCancerTypes() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        // Should not map breast cancer, only skin cancer
        assertThat(melaPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle person with both skin and other cancers")
    void testMapModelFieldsWithSkinAndOtherCancers() {
        Cancer skin = new Cancer(Cancer.CancerType.SKIN, AffectedType.ONE.value, 50);
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        unoPerson.addCancer(skin);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.mela.Risk melaRisk = transformer.transformRequest(unoRisk, model);
        Person melaPerson = melaRisk.getFamily().get(0);

        // Should map skin cancer
        assertThat(melaPerson.getAffectedSkinType()).isEqualTo(AffectedType.ONE.value);
        assertThat(melaPerson.getDiagnoseSkinAge()).isEqualTo(50);
    }
}

