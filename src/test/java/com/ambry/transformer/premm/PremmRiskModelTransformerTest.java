package com.ambry.transformer.premm;

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

import com.objex.progeny.risk.services.ProgenyRiskService;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PremmRiskModelTransformer Unit Tests")
class PremmRiskModelTransformerTest {

    @Mock
    private ProgenyRiskService premmExecuter;

    @Mock
    private PremmValidator premmValidator;

    @InjectMocks
    private PremmRiskModelTransformer transformer;

    private com.objex.risk.beans.uno.Risk unoRisk;
    private Model model;
    private com.objex.risk.beans.uno.Person unoPerson;

    @BeforeEach
    void setUp() {
        unoRisk = new com.objex.risk.beans.uno.Risk();
        model = new Model();
        model.setName(ModelName.PREMM);
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
    @DisplayName("Should return PREMM model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.PREMM);
    }

    @Test
    @DisplayName("Should transform request correctly")
    void testTransformRequest() {
        com.objex.risk.beans.premm.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isNotNull();
        assertThat(result.getModel()).isEqualTo("PREMM");
        assertThat(result.getAgeBy()).isEqualTo(20);
        assertThat(result.getAgeTo()).isEqualTo(80);
        assertThat(result.getChartCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke risk model and return result")
    void testInvokeRiskModel() {
        com.objex.risk.beans.premm.Risk premmRisk = new com.objex.risk.beans.premm.Risk();
        PREMMResult expectedResult = new PREMMResult();

        when(premmExecuter.computePREMM(any(com.objex.risk.beans.premm.Risk.class))).thenReturn(expectedResult);

        PREMMResult result = transformer.invokeRiskModel(premmRisk);

        assertThat(result).isEqualTo(expectedResult);
        verify(premmExecuter).computePREMM(premmRisk);
    }

    @Test
    @DisplayName("Should transform response correctly")
    void testTransformResponse() {
        PREMMResult modelResponse = new PREMMResult();

        PREMMResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isEqualTo(modelResponse);
    }

    @Test
    @DisplayName("Should return correct risk model validator")
    void testGetRiskModelValidator() {
        com.ambry.transformer.core.validator.RiskModelValidator<com.objex.risk.beans.premm.Risk> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(premmValidator);
    }

    @Test
    @DisplayName("Should map colorectal cancer fields correctly")
    void testMapModelFieldsWithColorectalCancer() {
        Cancer colon = new Cancer(CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        unoPerson.addCancer(colon);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE);
        assertThat(premmPerson.getDiagnoseColonAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map colorectal cancer with null affected to NONE")
    void testMapModelFieldsWithColorectalCancerNullAffected() {
        Cancer colon = new Cancer();
        colon.setCancerType(CancerType.COLORECTAL);
        colon.setAffected(null);
        colon.setDiagnoseAge(50);
        unoPerson.addCancer(colon);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.NONE);
        // When affected is null, diagnose age is not set by the transformer
        // The transformer only sets diagnose age when affected is not null
    }

    @Test
    @DisplayName("Should map endometrial cancer fields correctly")
    void testMapModelFieldsWithEndometrialCancer() {
        Cancer endometrial = new Cancer(CancerType.ENDOMETRIAL, AffectedType.ONE.value, 45);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE);
        assertThat(premmPerson.getDiagnoseEndometriumAge()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should map endometrial cancer with null affected to NONE")
    void testMapModelFieldsWithEndometrialCancerNullAffected() {
        Cancer endometrial = new Cancer();
        endometrial.setCancerType(CancerType.ENDOMETRIAL);
        endometrial.setAffected(null);
        endometrial.setDiagnoseAge(45);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.NONE);
        // When affected is null, diagnose age is not set by the transformer
        // The transformer only sets diagnose age when affected is not null
    }

    @Test
    @DisplayName("Should map PREMM related cancer fields correctly")
    void testMapModelFieldsWithPremmRelatedCancer() {
        Cancer premmRelated = new Cancer(CancerType.PREMMRELATED, AffectedType.ONE.value, 40);
        unoPerson.addCancer(premmRelated);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedRelatedType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should map PREMM related cancer with null affected to NONE")
    void testMapModelFieldsWithPremmRelatedCancerNullAffected() {
        Cancer premmRelated = new Cancer();
        premmRelated.setCancerType(CancerType.PREMMRELATED);
        premmRelated.setAffected(null);
        premmRelated.setDiagnoseAge(40);
        unoPerson.addCancer(premmRelated);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedRelatedType()).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should map all cancer types together")
    void testMapModelFieldsWithAllCancerTypes() {
        Cancer colon = new Cancer(CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        Cancer endometrial = new Cancer(CancerType.ENDOMETRIAL, AffectedType.MULTIPLE.value, 45);
        Cancer premmRelated = new Cancer(CancerType.PREMMRELATED, AffectedType.ONE.value, 40);
        unoPerson.addCancer(colon);
        unoPerson.addCancer(endometrial);
        unoPerson.addCancer(premmRelated);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE);
        assertThat(premmPerson.getDiagnoseColonAge()).isEqualTo(50);
        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.MULTIPLE);
        assertThat(premmPerson.getDiagnoseEndometriumAge()).isEqualTo(45);
        assertThat(premmPerson.getAffectedRelatedType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should handle person without any cancers")
    void testMapModelFieldsWithoutCancers() {
        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson).isNotNull();
        // Should not throw exception even when no cancers are present
    }

    @Test
    @DisplayName("Should create request object correctly")
    void testCreateRequestObject() {
        com.objex.risk.beans.premm.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isInstanceOf(com.objex.risk.beans.premm.Risk.class);
    }

    @Test
    @DisplayName("Should create person object correctly")
    void testCreatePersonObject() {
        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);

        assertThat(premmRisk.getFamily()).isNotEmpty();
        assertThat(premmRisk.getFamily().get(0)).isInstanceOf(com.objex.risk.beans.premm.Person.class);
    }

    @Test
    @DisplayName("Should add person to risk correctly")
    void testAddPersonToRisk() {
        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);

        assertThat(premmRisk.getFamily()).hasSize(1);
        assertThat(premmRisk.getFamily().get(0)).isNotNull();
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

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);

        assertThat(premmRisk.getFamily()).hasSize(2);
    }

    @Test
    @DisplayName("Should map common risk fields correctly")
    void testMapCommonRiskFields() {
        unoRisk.setSaveIncoming(true);
        unoRisk.setConsent(true);
        unoRisk.setTransId("TRANS001");
        unoRisk.setFamilyId("FAM001");

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);

        assertThat(premmRisk.isSaveIncoming()).isTrue();
        assertThat(premmRisk.isConsent()).isTrue();
        assertThat(premmRisk.getTransId()).isEqualTo("TRANS001");
        assertThat(premmRisk.getFamilyId()).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        unoRisk.getFamily().clear();

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);

        assertThat(premmRisk).isNotNull();
        assertThat(premmRisk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should handle colorectal cancer with ONE affected type")
    void testMapModelFieldsWithColorectalCancerOne() {
        Cancer colonOne = new Cancer(CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        unoPerson.addCancer(colonOne);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should handle colorectal cancer with TWO affected type")
    void testMapModelFieldsWithColorectalCancerTwo() {
        Cancer colonTwo = new Cancer(CancerType.COLORECTAL, AffectedType.MULTIPLE.value, 55);
        unoPerson.addCancer(colonTwo);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.MULTIPLE);
    }

    @Test
    @DisplayName("Should handle endometrial cancer with ONE affected type")
    void testMapModelFieldsWithEndometrialCancerOne() {
        Cancer endometrialOne = new Cancer(CancerType.ENDOMETRIAL, AffectedType.ONE.value, 45);
        unoPerson.addCancer(endometrialOne);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should handle endometrial cancer with TWO affected type")
    void testMapModelFieldsWithEndometrialCancerTwo() {
        Cancer endometrialTwo = new Cancer(CancerType.ENDOMETRIAL, AffectedType.MULTIPLE.value, 48);
        unoPerson.addCancer(endometrialTwo);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.MULTIPLE);
    }

    @Test
    @DisplayName("Should handle PREMM related cancer with ONE affected type")
    void testMapModelFieldsWithPremmRelatedCancerOne() {
        Cancer premmRelatedOne = new Cancer(CancerType.PREMMRELATED, AffectedType.ONE.value, 40);
        unoPerson.addCancer(premmRelatedOne);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedRelatedType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should handle PREMM related cancer with TWO affected type")
    void testMapModelFieldsWithPremmRelatedCancerTwo() {
        Cancer premmRelatedTwo = new Cancer(CancerType.PREMMRELATED, AffectedType.MULTIPLE.value, 42);
        unoPerson.addCancer(premmRelatedTwo);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedRelatedType()).isEqualTo(AffectedType.MULTIPLE);
    }

    @Test
    @DisplayName("Should handle colorectal cancer with null diagnose age")
    void testMapModelFieldsWithColorectalCancerNullDiagnoseAge() {
        Cancer colon = new Cancer();
        colon.setCancerType(CancerType.COLORECTAL);
        colon.setAffected(AffectedType.ONE.value);
        colon.setDiagnoseAge(null);
        unoPerson.addCancer(colon);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE);
        // When diagnoseAge is null, it gets set to 0 (primitive int defaults to 0)
        // The transformer passes null through: member.setDiagnoseColonAge(colon.getDiagnoseAge())
        // Since diagnoseColonAge is a primitive int, null gets auto-unboxed to 0
        assertThat(premmPerson.getDiagnoseColonAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle endometrial cancer with null diagnose age")
    void testMapModelFieldsWithEndometrialCancerNullDiagnoseAge() {
        Cancer endometrial = new Cancer();
        endometrial.setCancerType(CancerType.ENDOMETRIAL);
        endometrial.setAffected(AffectedType.ONE.value);
        endometrial.setDiagnoseAge(null);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE);
        // When diagnoseAge is null, it gets set to 0 (primitive int defaults to 0)
        // The transformer passes null through: member.setDiagnoseEndometriumAge(endometrial.getDiagnoseAge())
        // Since diagnoseEndometriumAge is a primitive int, null gets auto-unboxed to 0
        assertThat(premmPerson.getDiagnoseEndometriumAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle person with only colorectal cancer")
    void testMapModelFieldsWithOnlyColorectalCancer() {
        Cancer colon = new Cancer(CancerType.COLORECTAL, AffectedType.ONE.value, 50);
        unoPerson.addCancer(colon);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedColonType()).isEqualTo(AffectedType.ONE);
        assertThat(premmPerson.getDiagnoseColonAge()).isEqualTo(50);
        // Endometrial and PREMM related should not be set
    }

    @Test
    @DisplayName("Should handle person with only endometrial cancer")
    void testMapModelFieldsWithOnlyEndometrialCancer() {
        Cancer endometrial = new Cancer(CancerType.ENDOMETRIAL, AffectedType.ONE.value, 45);
        unoPerson.addCancer(endometrial);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedEndometriumType()).isEqualTo(AffectedType.ONE);
        assertThat(premmPerson.getDiagnoseEndometriumAge()).isEqualTo(45);
        // Colorectal and PREMM related should not be set
    }

    @Test
    @DisplayName("Should handle person with only PREMM related cancer")
    void testMapModelFieldsWithOnlyPremmRelatedCancer() {
        Cancer premmRelated = new Cancer(CancerType.PREMMRELATED, AffectedType.ONE.value, 40);
        unoPerson.addCancer(premmRelated);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);
        com.objex.risk.beans.premm.Person premmPerson = premmRisk.getFamily().get(0);

        assertThat(premmPerson.getAffectedRelatedType()).isEqualTo(AffectedType.ONE);
        // Colorectal and Endometrial should not be set
    }

    @Test
    @DisplayName("Should handle model with instructions")
    void testMapModelFieldsWithInstructions() {
        com.objex.risk.beans.uno.Instruction instruction = new com.objex.risk.beans.uno.Instruction();
        instruction.setName("testInstruction");
        instruction.setValue("testValue");
        model.getInstructions().add(instruction);

        com.objex.risk.beans.premm.Risk premmRisk = transformer.transformRequest(unoRisk, model);

        assertThat(premmRisk).isNotNull();
        // Instructions are logged but not mapped in the base transformer
    }

    @Test
    @DisplayName("Should handle null PREMM result in transformResponse")
    void testTransformResponseWithNull() {
        PREMMResult result = transformer.transformResponse(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle exception from premmExecuter")
    void testInvokeRiskModelWithException() {
        com.objex.risk.beans.premm.Risk premmRisk = new com.objex.risk.beans.premm.Risk();

        when(premmExecuter.computePREMM(any(com.objex.risk.beans.premm.Risk.class)))
            .thenThrow(new RuntimeException("Service error"));

        try {
            transformer.invokeRiskModel(premmRisk);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Service error");
        }

        verify(premmExecuter).computePREMM(premmRisk);
    }
}

