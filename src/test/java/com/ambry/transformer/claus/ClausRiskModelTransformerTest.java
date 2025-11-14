package com.ambry.transformer.claus;

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
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ClausRiskModelTransformer Unit Tests")
class ClausRiskModelTransformerTest {

    @Mock
    private ProgenyRiskService clausExecuter;

    @Mock
    private ClausValidator clausValidator;

    @InjectMocks
    private ClausRiskModelTransformer transformer;

    private com.objex.risk.beans.uno.Risk unoRisk;
    private Model model;
    private com.objex.risk.beans.uno.Person unoPerson;

    @BeforeEach
    void setUp() {
        unoRisk = new com.objex.risk.beans.uno.Risk();
        model = new Model();
        model.setName(ModelName.CLAUS);
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
    @DisplayName("Should return CLAUS model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.CLAUS);
    }

    @Test
    @DisplayName("Should transform request correctly")
    void testTransformRequest() {
        com.objex.risk.beans.claus.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isNotNull();
        assertThat(result.getModel()).isEqualTo("CLAUS");
        assertThat(result.getAgeBy()).isEqualTo(20);
        assertThat(result.getAgeTo()).isEqualTo(80);
        assertThat(result.getChartCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke risk model and return result")
    void testInvokeRiskModel() {
        com.objex.risk.beans.claus.Risk clausRisk = new com.objex.risk.beans.claus.Risk();
        ClausResult expectedResult = new ClausResult();

        when(clausExecuter.computeClaus(any(com.objex.risk.beans.claus.Risk.class))).thenReturn(expectedResult);

        ClausResult result = transformer.invokeRiskModel(clausRisk);

        assertThat(result).isEqualTo(expectedResult);
        verify(clausExecuter).computeClaus(clausRisk);
    }

    @Test
    @DisplayName("Should transform response correctly")
    void testTransformResponse() {
        ClausResult modelResponse = new ClausResult();

        ClausResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isEqualTo(modelResponse);
    }

    @Test
    @DisplayName("Should return correct risk model validator")
    void testGetRiskModelValidator() {
        com.ambry.transformer.core.validator.RiskModelValidator<com.objex.risk.beans.claus.Risk> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(clausValidator);
    }

    @Test
    @DisplayName("Should map breast cancer fields correctly")
    void testMapModelFieldsWithBreastCancer() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 50);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseBreastAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map breast cancer with null affected to NONE")
    void testMapModelFieldsWithBreastCancerNullAffected() {
        Cancer breast = new Cancer();
        breast.setCancerType(Cancer.CancerType.BREAST);
        breast.setAffected(null);
        breast.setDiagnoseAge(50);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should map ovarian cancer fields correctly")
    void testMapModelFieldsWithOvarianCancer() {
        Cancer ovarian = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.ONE.value, 45);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseOvarianAge()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should map ovarian cancer with null affected to NONE")
    void testMapModelFieldsWithOvarianCancerNullAffected() {
        Cancer ovarian = new Cancer();
        ovarian.setCancerType(Cancer.CancerType.OVARIAN);
        ovarian.setAffected(null);
        ovarian.setDiagnoseAge(45);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should map both breast and ovarian cancers")
    void testMapModelFieldsWithBothBreastAndOvarianCancers() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 50);
        Cancer ovarian = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.ONE.value, 45);
        unoPerson.addCancer(breast);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseBreastAge()).isEqualTo(50);
        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseOvarianAge()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should create request object correctly")
    void testCreateRequestObject() {
        com.objex.risk.beans.claus.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isInstanceOf(com.objex.risk.beans.claus.Risk.class);
    }

    @Test
    @DisplayName("Should create person object correctly")
    void testCreatePersonObject() {
        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);

        assertThat(clausRisk.getFamily()).isNotEmpty();
        assertThat(clausRisk.getFamily().get(0)).isInstanceOf(Person.class);
    }

    @Test
    @DisplayName("Should add person to risk correctly")
    void testAddPersonToRisk() {
        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);

        assertThat(clausRisk.getFamily()).hasSize(1);
        assertThat(clausRisk.getFamily().get(0)).isNotNull();
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

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);

        assertThat(clausRisk.getFamily()).hasSize(2);
    }

    @Test
    @DisplayName("Should map common risk fields correctly")
    void testMapCommonRiskFields() {
        unoRisk.setSaveIncoming(true);
        unoRisk.setConsent(true);
        unoRisk.setTransId("TRANS001");
        unoRisk.setFamilyId("FAM001");

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);

        assertThat(clausRisk.isSaveIncoming()).isTrue();
        assertThat(clausRisk.isConsent()).isTrue();
        assertThat(clausRisk.getTransId()).isEqualTo("TRANS001");
        assertThat(clausRisk.getFamilyId()).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        unoRisk.getFamily().clear();

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);

        assertThat(clausRisk).isNotNull();
        assertThat(clausRisk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should handle person without breast or ovarian cancer")
    void testMapModelFieldsWithoutCancers() {
        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson).isNotNull();
        // Should not throw exception even when no cancers are present
    }

    @Test
    @DisplayName("Should handle person with other cancer types")
    void testMapModelFieldsWithOtherCancerTypes() {
        Cancer skin = new Cancer(Cancer.CancerType.SKIN, AffectedType.ONE.value, 40);
        unoPerson.addCancer(skin);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        // Should not map skin cancer, only breast and ovarian
        assertThat(clausPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle breast cancer without diagnose age")
    void testMapModelFieldsWithBreastCancerWithoutDiagnoseAge() {
        Cancer breast = new Cancer();
        breast.setCancerType(Cancer.CancerType.BREAST);
        breast.setAffected(AffectedType.ONE.value);
        breast.setDiagnoseAge(0);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseBreastAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle ovarian cancer without diagnose age")
    void testMapModelFieldsWithOvarianCancerWithoutDiagnoseAge() {
        Cancer ovarian = new Cancer();
        ovarian.setCancerType(Cancer.CancerType.OVARIAN);
        ovarian.setAffected(AffectedType.ONE.value);
        ovarian.setDiagnoseAge(0);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseOvarianAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should map all fields together")
    void testMapModelFieldsWithAllFields() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 50);
        Cancer ovarian = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.ONE.value, 45);
        unoPerson.addCancer(breast);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseBreastAge()).isEqualTo(50);
        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.ONE);
        assertThat(clausPerson.getDiagnoseOvarianAge()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should handle multiple breast cancers")
    void testMapModelFieldsWithMultipleBreastCancers() {
        // Note: Person can have multiple cancers, but getCancer(CancerType.BREAST) returns the first one
        Cancer breast1 = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 40);
        Cancer breast2 = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 50);
        unoPerson.addCancer(breast1);
        unoPerson.addCancer(breast2);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        // Should map the first breast cancer found
        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should handle multiple ovarian cancers")
    void testMapModelFieldsWithMultipleOvarianCancers() {
        // Note: Person can have multiple cancers, but getCancer(CancerType.OVARIAN) returns the first one
        Cancer ovarian1 = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.ONE.value, 40);
        Cancer ovarian2 = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.ONE.value, 50);
        unoPerson.addCancer(ovarian1);
        unoPerson.addCancer(ovarian2);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        // Should map the first ovarian cancer found
        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.ONE);
    }

    @Test
    @DisplayName("Should handle breast cancer with NONE AffectedType")
    void testMapModelFieldsWithBreastCancerNoneAffectedType() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.NONE.value, 50);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedBreastType()).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should handle ovarian cancer with NONE AffectedType")
    void testMapModelFieldsWithOvarianCancerNoneAffectedType() {
        Cancer ovarian = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.NONE.value, 45);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.claus.Risk clausRisk = transformer.transformRequest(unoRisk, model);
        Person clausPerson = clausRisk.getFamily().get(0);

        assertThat(clausPerson.getAffectedOvarianType()).isEqualTo(AffectedType.NONE);
    }
}

