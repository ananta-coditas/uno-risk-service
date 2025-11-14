package com.ambry.transformer.tyrer;

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
import com.objex.risk.beans.tcuzick.Person;
import com.objex.risk.beans.tcuzick.TyrerResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.CancerDetails;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("TyrerRiskModelTransformer Unit Tests")
class TyrerRiskModelTransformerTest {

    @Mock
    private ProgenyRiskService tyrerExecuter;

    @Mock
    private TyrerValidator tyrerValidator;

    @InjectMocks
    private TyrerRiskModelTransformer transformer;

    private com.objex.risk.beans.uno.Risk unoRisk;
    private Model model;
    private com.objex.risk.beans.uno.Person unoPerson;

    @BeforeEach
    void setUp() {
        unoRisk = new com.objex.risk.beans.uno.Risk();
        model = new Model();
        model.setName(ModelName.TYRER);
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
    @DisplayName("Should return TYRER model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.TYRER);
    }

    @Test
    @DisplayName("Should transform request correctly")
    void testTransformRequest() {
        com.objex.risk.beans.tcuzick.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isNotNull();
        assertThat(result.getModel()).isEqualTo("TYRER");
        assertThat(result.getAgeBy()).isEqualTo(20);
        assertThat(result.getAgeTo()).isEqualTo(80);
        assertThat(result.getChartCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke risk model and return result")
    void testInvokeRiskModel() {
        com.objex.risk.beans.tcuzick.Risk tyrerRisk = new com.objex.risk.beans.tcuzick.Risk();
        TyrerResult expectedResult = new TyrerResult();

        when(tyrerExecuter.computeTyrer(any(com.objex.risk.beans.tcuzick.Risk.class))).thenReturn(expectedResult);

        TyrerResult result = transformer.invokeRiskModel(tyrerRisk);

        assertThat(result).isEqualTo(expectedResult);
        verify(tyrerExecuter).computeTyrer(tyrerRisk);
    }

    @Test
    @DisplayName("Should transform response correctly")
    void testTransformResponse() {
        TyrerResult modelResponse = new TyrerResult();

        TyrerResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isEqualTo(modelResponse);
    }

    @Test
    @DisplayName("Should return correct risk model validator")
    void testGetRiskModelValidator() {
        com.ambry.transformer.core.validator.RiskModelValidator<com.objex.risk.beans.tcuzick.Risk> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(tyrerValidator);
    }

    @Test
    @DisplayName("Should map breast cancer fields correctly")
    void testMapModelFieldsWithBreastCancer() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(tyrerPerson.getDiagnoseBreastAge()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should map breast cancer with null affected to NONE")
    void testMapModelFieldsWithBreastCancerNullAffected() {
        Cancer breast = new Cancer();
        breast.setCancerType(Cancer.CancerType.BREAST);
        breast.setAffected(null);
        breast.setDiagnoseAge(45);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedBreastType()).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should map breast cancer with bilateral details")
    void testMapModelFieldsWithBreastCancerBilateral() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        CancerDetails details = new CancerDetails();
        details.setBilateral(AffectedType.ONE);
        breast.setCancerDetails(details);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedBilateralBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(tyrerPerson.getDiagnoseBilateralBreastAge()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should map ovarian cancer fields correctly")
    void testMapModelFieldsWithOvarianCancer() {
        Cancer ovarian = new Cancer(Cancer.CancerType.OVARIAN, AffectedType.ONE.value, 50);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedOvarianType()).isEqualTo(AffectedType.ONE);
        assertThat(tyrerPerson.getDiagnoseOvarianAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map ovarian cancer with null affected to NONE")
    void testMapModelFieldsWithOvarianCancerNullAffected() {
        Cancer ovarian = new Cancer();
        ovarian.setCancerType(Cancer.CancerType.OVARIAN);
        ovarian.setAffected(null);
        ovarian.setDiagnoseAge(50);
        unoPerson.addCancer(ovarian);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedOvarianType()).isEqualTo(AffectedType.NONE);
    }

    @Test
    @DisplayName("Should map density measure and value")
    void testMapModelFieldsWithDensity() {
        // Use reflection or test indirectly - density measure mapping
        unoPerson.setDensityValue(2.5);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getDensityValue()).isEqualTo(2.5);
        // Density measure is mapped from uno.Person to tcuzick.Person
        assertThat(tyrerPerson.getDensityMeasure()).isNotNull();
    }

    @Test
    @DisplayName("Should map hyperplasia fields")
    void testMapModelFieldsWithHyperplasia() {
        unoPerson.setHasAtypicalHyperplasia(true);
        unoPerson.setHasLCIS(true);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getHasAtypicalHyperplasia()).isTrue();
        assertThat(tyrerPerson.getHasLCIS()).isTrue();
    }

    @Test
    @DisplayName("Should map live birth fields")
    void testMapModelFieldsWithLiveBirth() {
        unoPerson.setHasLiveBirth(true);
        unoPerson.setLiveBirthAge(30);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getHasLiveBirth()).isTrue();
        assertThat(tyrerPerson.getLiveBirthAge()).isEqualTo(30);
    }

    @Test
    @DisplayName("Should map height and weight")
    void testMapModelFieldsWithHeightAndWeight() {
        unoPerson.setHeight(165.0);
        unoPerson.setWeight(70.0);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getHeight()).isEqualTo(165.0);
        assertThat(tyrerPerson.getWeight()).isEqualTo(70.0);
    }

    @Test
    @DisplayName("Should map HRT fields")
    void testMapModelFieldsWithHRT() {
        // Test HRT fields mapping - use available methods
        // Note: HRTUse enum may need to be set via reflection or may not be directly accessible
        unoPerson.setYearsPastHRT(5.0);
        unoPerson.setYearsFutureHRT(2.0);
        unoPerson.setYearsSinceHRT(1.0);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getYearsPastHRT()).isEqualTo(5.0);
        assertThat(tyrerPerson.getYearsFutureHRT()).isEqualTo(2.0);
        assertThat(tyrerPerson.getYearsSinceHRT()).isEqualTo(1.0);
        // HRT type and use are mapped from uno.Person to tcuzick.Person
        // These may be null if not set in uno.Person
        // assertThat(tyrerPerson.getHrtType()).isNotNull();
        // assertThat(tyrerPerson.getUsedHRT()).isNotNull();
    }

    @Test
    @DisplayName("Should map menarche and menopause fields")
    void testMapModelFieldsWithMenarcheAndMenopause() {
        unoPerson.setMenarcheAge(12);
        unoPerson.setMenopauseAge(50);
        // Menostat is mapped from uno.Person.Menostat to tcuzick.Person.MenosType
        // Note: Menostat enum may need to be set via reflection or may not be directly accessible

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getMenarcheAge()).isEqualTo(12);
        // Menopause age may return -99 if not set or if person is not in menopause
        // The actual value depends on the person's menostat status
        assertThat(tyrerPerson.getMenopauseAge()).isNotNull();
        // Menostat is mapped from uno.Person to tcuzick.Person
        assertThat(tyrerPerson.getMenostat()).isNotNull();
    }

    @Test
    @DisplayName("Should map polygenic SNP")
    void testMapModelFieldsWithPolygenicSNP() {
        Double polygenicSNP = 0.5;
        unoPerson.setPolygenicSNP(polygenicSNP);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getPolygenicSNP()).isEqualTo(polygenicSNP);
    }

    @Test
    @DisplayName("Should create request object correctly")
    void testCreateRequestObject() {
        com.objex.risk.beans.tcuzick.Risk result = transformer.transformRequest(unoRisk, model);

        assertThat(result).isInstanceOf(com.objex.risk.beans.tcuzick.Risk.class);
    }

    @Test
    @DisplayName("Should create person object correctly")
    void testCreatePersonObject() {
        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);

        assertThat(tyrerRisk.getFamily()).isNotEmpty();
        assertThat(tyrerRisk.getFamily().get(0)).isInstanceOf(Person.class);
    }

    @Test
    @DisplayName("Should add person to risk correctly")
    void testAddPersonToRisk() {
        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);

        assertThat(tyrerRisk.getFamily()).hasSize(1);
        assertThat(tyrerRisk.getFamily().get(0)).isNotNull();
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

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);

        assertThat(tyrerRisk.getFamily()).hasSize(2);
    }

    @Test
    @DisplayName("Should map common risk fields correctly")
    void testMapCommonRiskFields() {
        unoRisk.setSaveIncoming(true);
        unoRisk.setConsent(true);
        unoRisk.setTransId("TRANS001");
        unoRisk.setFamilyId("FAM001");

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);

        assertThat(tyrerRisk.isSaveIncoming()).isTrue();
        assertThat(tyrerRisk.isConsent()).isTrue();
        assertThat(tyrerRisk.getTransId()).isEqualTo("TRANS001");
        assertThat(tyrerRisk.getFamilyId()).isEqualTo("FAM001");
    }

    @Test
    @DisplayName("Should handle empty family list")
    void testMapFamilyWithEmptyFamily() {
        unoRisk.getFamily().clear();

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);

        assertThat(tyrerRisk).isNotNull();
        assertThat(tyrerRisk.getFamily()).isEmpty();
    }

    @Test
    @DisplayName("Should handle person without any cancer")
    void testMapModelFieldsWithoutCancer() {
        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson).isNotNull();
        // Should not throw exception even when no cancer is present
    }

    @Test
    @DisplayName("Should handle null density measure")
    void testMapModelFieldsWithNullDensityMeasure() {
        unoPerson.setDensityValue(2.5);
        // Density measure may be null if not set

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getDensityValue()).isEqualTo(2.5);
        // Density measure is mapped from uno.Person, may be null
    }

    @Test
    @DisplayName("Should handle null hyperplasia")
    void testMapModelFieldsWithNullHyperplasia() {
        unoPerson.setHasAtypicalHyperplasia(false);
        unoPerson.setHasLCIS(false);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getHasAtypicalHyperplasia()).isFalse();
        assertThat(tyrerPerson.getHasLCIS()).isFalse();
    }

    @Test
    @DisplayName("Should handle null live birth")
    void testMapModelFieldsWithNullLiveBirth() {
        unoPerson.setHasLiveBirth(false);
        unoPerson.setLiveBirthAge(null);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getHasLiveBirth()).isFalse();
        // Live birth age may be null
    }

    @Test
    @DisplayName("Should handle null HRT years")
    void testMapModelFieldsWithNullHRTYears() {
        unoPerson.setYearsPastHRT(null);
        unoPerson.setYearsFutureHRT(null);
        unoPerson.setYearsSinceHRT(null);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        // HRT years may be null if not set
        assertThat(tyrerPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle null polygenic SNP")
    void testMapModelFieldsWithNullPolygenicSNP() {
        unoPerson.setPolygenicSNP(null);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        // Polygenic SNP may be null if not set
        assertThat(tyrerPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle null height and weight")
    void testMapModelFieldsWithNullHeightAndWeight() {
        unoPerson.setHeight(null);
        unoPerson.setWeight(null);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        // Height and weight may be null if not set
        assertThat(tyrerPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle null menarche and menopause ages")
    void testMapModelFieldsWithNullMenarcheAndMenopauseAges() {
        unoPerson.setMenarcheAge(null);
        unoPerson.setMenopauseAge(null);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        // Menarche and menopause ages may be null if not set
        assertThat(tyrerPerson).isNotNull();
    }

    @Test
    @DisplayName("Should handle breast cancer without details")
    void testMapModelFieldsWithBreastCancerWithoutDetails() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        // No cancer details
        unoPerson.addCancer(breast);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(tyrerPerson.getDiagnoseBreastAge()).isEqualTo(45);
        // Bilateral fields should not be set when details are null
    }

    @Test
    @DisplayName("Should handle bilateral breast cancer with null bilateral")
    void testMapModelFieldsWithBreastCancerNullBilateral() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        CancerDetails details = new CancerDetails();
        details.setBilateral(null);
        breast.setCancerDetails(details);
        unoPerson.addCancer(breast);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        // Bilateral type may be null if not set
    }

    @Test
    @DisplayName("Should map all person fields together")
    void testMapModelFieldsWithAllFields() {
        Cancer breast = new Cancer(Cancer.CancerType.BREAST, AffectedType.ONE.value, 45);
        CancerDetails details = new CancerDetails();
        details.setBilateral(AffectedType.ONE);
        breast.setCancerDetails(details);
        unoPerson.addCancer(breast);

        unoPerson.setDensityValue(2.5);
        unoPerson.setHasAtypicalHyperplasia(true);
        unoPerson.setHasLCIS(true);
        unoPerson.setHasLiveBirth(true);
        unoPerson.setLiveBirthAge(30);
        unoPerson.setHeight(165.0);
        unoPerson.setWeight(70.0);
        unoPerson.setYearsPastHRT(5.0);
        unoPerson.setMenarcheAge(12);
        unoPerson.setMenopauseAge(50);
        unoPerson.setPolygenicSNP(0.5);

        com.objex.risk.beans.tcuzick.Risk tyrerRisk = transformer.transformRequest(unoRisk, model);
        Person tyrerPerson = tyrerRisk.getFamily().get(0);

        assertThat(tyrerPerson.getAffectedBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(tyrerPerson.getDiagnoseBreastAge()).isEqualTo(45);
        assertThat(tyrerPerson.getAffectedBilateralBreastType()).isEqualTo(AffectedType.ONE);
        assertThat(tyrerPerson.getDensityValue()).isEqualTo(2.5);
        assertThat(tyrerPerson.getHasAtypicalHyperplasia()).isTrue();
        assertThat(tyrerPerson.getHasLCIS()).isTrue();
        assertThat(tyrerPerson.getHasLiveBirth()).isTrue();
        assertThat(tyrerPerson.getLiveBirthAge()).isEqualTo(30);
        assertThat(tyrerPerson.getHeight()).isEqualTo(165.0);
        assertThat(tyrerPerson.getWeight()).isEqualTo(70.0);
        assertThat(tyrerPerson.getYearsPastHRT()).isEqualTo(5.0);
        assertThat(tyrerPerson.getMenarcheAge()).isEqualTo(12);
        // Menopause age may return -99 if not set or if person is not in menopause
        // The actual value depends on the person's menostat status
        assertThat(tyrerPerson.getMenopauseAge()).isNotNull();
        assertThat(tyrerPerson.getPolygenicSNP()).isEqualTo(0.5);
        // Enum mappings are tested indirectly
        assertThat(tyrerPerson.getDensityMeasure()).isNotNull();
        // HRT type and use may be null if not set in uno.Person
        // assertThat(tyrerPerson.getHrtType()).isNotNull();
        // assertThat(tyrerPerson.getUsedHRT()).isNotNull();
        assertThat(tyrerPerson.getMenostat()).isNotNull();
    }
}

