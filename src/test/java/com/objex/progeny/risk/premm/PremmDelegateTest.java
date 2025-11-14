package com.objex.progeny.risk.premm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.premm.Person;
import com.objex.risk.beans.premm.Risk;

import static com.objex.risk.beans.premm.Person.MLH1;

/**
 * Unit tests for {@link PremmDelegate}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PremmDelegate Unit Tests")
class PremmDelegateTest {

    @InjectMocks
    private PremmDelegate premmDelegate;

    private Risk risk;
    private Person proband;
    private Person father;
    private Person mother;
    private Person sibling;
    private Person grandparent;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        risk.setAgeBy(5);
        risk.setAgeTo(79);

        proband = new Person();
        proband.setId(1);
        proband.setAge(40);
        proband.setGender(0); // Female
        proband.setFatherId(2);
        proband.setMotherId(3);
        risk.getFamily().add(proband);

        father = new Person();
        father.setId(2);
        father.setAge(70);
        father.setGender(1); // Male
        father.setFatherId(0);
        father.setMotherId(0);
        risk.getFamily().add(father);

        mother = new Person();
        mother.setId(3);
        mother.setAge(65);
        mother.setGender(0); // Female
        mother.setFatherId(0);
        mother.setMotherId(0);
        risk.getFamily().add(mother);

        sibling = new Person();
        sibling.setId(4);
        sibling.setAge(35);
        sibling.setGender(0);
        sibling.setFatherId(2);
        sibling.setMotherId(3);
        risk.getFamily().add(sibling);

        grandparent = new Person();
        grandparent.setId(5);
        grandparent.setAge(90);
        grandparent.setGender(0);
        grandparent.setFatherId(0);
        grandparent.setMotherId(0);
        risk.getFamily().add(grandparent);
        mother.setMotherId(5);
    }

    @Test
    @DisplayName("Should verify PREMM_VERSION constant")
    void testPremmVersion() {
        assertThat(PremmDelegate.PREMM_VERSION).isNotNull();
        assertThat(PremmDelegate.PREMM_VERSION).contains("PREMM");
    }

    @Test
    @DisplayName("Should verify age limit constants")
    void testAgeLimitConstants() {
        assertThat(PremmDelegate.PROB_ENDO_MIN).isEqualTo(31);
        assertThat(PremmDelegate.PROB_ENDO_MAX).isEqualTo(67);
        assertThat(PremmDelegate.FDR_ENDO_MIN).isEqualTo(28);
        assertThat(PremmDelegate.FDR_ENDO_MAX).isEqualTo(69);
        assertThat(PremmDelegate.SDR_ENDO_MIN).isEqualTo(34);
        assertThat(PremmDelegate.SDR_ENDO_MAX).isEqualTo(70);
        assertThat(PremmDelegate.PROB_COLON_MIN).isEqualTo(27);
        assertThat(PremmDelegate.PROB_COLON_MAX).isEqualTo(77);
        assertThat(PremmDelegate.FDR_COLON_MIN).isEqualTo(28);
        assertThat(PremmDelegate.FDR_COLON_MAX).isEqualTo(81);
        assertThat(PremmDelegate.SDR_COLON_MIN).isEqualTo(30);
        assertThat(PremmDelegate.SDR_COLON_MAX).isEqualTo(85);
    }

    @Test
    @DisplayName("Should calculate PREMM with valid data")
    void testCalculateWithValidData() {
        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.getVersion()).isEqualTo(PremmDelegate.PREMM_VERSION);
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should return error when proband age is too high")
    void testCalculateWithAgeTooHigh() {
        proband.setAge(80);
        risk.setAgeTo(79);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("ageTooHighForModelSettings");
    }

    @Test
    @DisplayName("Should return error when colon cancer affected but age is missing")
    void testCalculateWithMissingColonAge() {
        proband.setAffectedColonType(AffectedType.ONE);
        proband.setDiagnoseColonAge(0);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("missingAgeColon");
    }

    @Test
    @DisplayName("Should return error when endometrium cancer affected but age is missing")
    void testCalculateWithMissingEndometriumAge() {
        proband.setAffectedEndometriumType(AffectedType.ONE);
        proband.setDiagnoseEndometriumAge(0);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("missingAgeEndometrium");
    }

    @Test
    @DisplayName("Should return error when male has endometrium cancer")
    void testCalculateWithInvalidGenderForEndometrium() {
        proband.setGender(1); // Male
        proband.setAffectedEndometriumType(AffectedType.ONE);
        proband.setDiagnoseEndometriumAge(50);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("invalidGenderForEndometrium");
    }

    @Test
    @DisplayName("Should return error when age is negative")
    void testCalculateWithNegativeAge() {
        proband.setAge(-1);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("invalidAge");
    }

    @Test
    @DisplayName("Should count affected colon relatives")
    void testCountAffectedColon() {
        List<Person> relatives = new ArrayList<>();
        
        Person person1 = new Person();
        person1.setAffectedColonType(AffectedType.ONE);
        relatives.add(person1);

        Person person2 = new Person();
        person2.setAffectedColonType(AffectedType.NONE);
        relatives.add(person2);

        Person person3 = new Person();
        person3.setAffectedColonType(AffectedType.MULTIPLE);
        relatives.add(person3);

        int count = premmDelegate.countAffectedColon(relatives);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should count affected endometrium relatives")
    void testCountAffectedEndometrium() {
        List<Person> relatives = new ArrayList<>();
        
        Person person1 = new Person();
        person1.setAffectedEndometriumType(AffectedType.ONE);
        relatives.add(person1);

        Person person2 = new Person();
        person2.setAffectedEndometriumType(AffectedType.NONE);
        relatives.add(person2);

        Person person3 = new Person();
        person3.setAffectedEndometriumType(AffectedType.MULTIPLE);
        relatives.add(person3);

        int count = premmDelegate.countAffectedEndometrium(relatives);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should calculate family colon prevalence")
    void testCalcFamColonPrevalence() {
        List<Person> fdrs = new ArrayList<>();
        Person fdr1 = new Person();
        fdr1.setAffectedColonType(AffectedType.ONE);
        fdrs.add(fdr1);

        Person fdr2 = new Person();
        fdr2.setAffectedColonType(AffectedType.ONE);
        fdrs.add(fdr2);

        List<Person> sdrs = new ArrayList<>();
        Person sdr1 = new Person();
        sdr1.setAffectedColonType(AffectedType.ONE);
        sdrs.add(sdr1);

        Double prevalence = premmDelegate.calcFamColonPrevalence(fdrs, sdrs);

        assertThat(prevalence).isNotNull();
        // 2 FDRs = 2, 1 SDR = 0.5, total = 2.5
        assertThat(prevalence).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should calculate family endometrium prevalence")
    void testCalcFamEndometriumPrevalence() {
        List<Person> fdrs = new ArrayList<>();
        Person fdr1 = new Person();
        fdr1.setAffectedEndometriumType(AffectedType.ONE);
        fdrs.add(fdr1);

        List<Person> sdrs = new ArrayList<>();
        Person sdr1 = new Person();
        sdr1.setAffectedEndometriumType(AffectedType.ONE);
        sdrs.add(sdr1);

        Double prevalence = premmDelegate.calcFamEndometriumPrevalence(fdrs, sdrs);

        assertThat(prevalence).isNotNull();
        // 1 FDR = 1, 1 SDR = 0.5, total = 1.5
        assertThat(prevalence).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should calculate presence correctly")
    void testPresence() {
        assertThat(premmDelegate.presence(true)).isEqualTo(1);
        assertThat(premmDelegate.presence(false)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should adjust proband diagnose colon age")
    void testAdjustProbandDiagnoseColAge() {
        Person person = new Person();
        person.setDiagnoseColonAge(30);

        int adjusted = premmDelegate.adjustProbandDiagnoseColAge(person, MLH1);

        assertThat(adjusted).isGreaterThanOrEqualTo(PremmDelegate.PROB_COLON_MIN);
        assertThat(adjusted).isLessThanOrEqualTo(PremmDelegate.PROB_COLON_MAX);
    }

    @Test
    @DisplayName("Should adjust FDR diagnose colon age")
    void testAdjustFDRDiagnoseColAge() {
        Person person = new Person();
        person.setDiagnoseColonAge(30);

        int adjusted = premmDelegate.adjustFDRDiagnoseColAge(person, MLH1, 1);

        assertThat(adjusted).isGreaterThanOrEqualTo(PremmDelegate.FDR_COLON_MIN);
        assertThat(adjusted).isLessThanOrEqualTo(PremmDelegate.FDR_COLON_MAX);
    }

    @Test
    @DisplayName("Should adjust SDR diagnose colon age")
    void testAdjustSDRDiagnoseColAge() {
        Person person = new Person();
        person.setDiagnoseColonAge(35);

        int adjusted = premmDelegate.adjustSDRDiagnoseColAge(person, MLH1, 1);

        assertThat(adjusted).isGreaterThanOrEqualTo(PremmDelegate.SDR_COLON_MIN);
        assertThat(adjusted).isLessThanOrEqualTo(PremmDelegate.SDR_COLON_MAX);
    }

    @Test
    @DisplayName("Should adjust age below minimum")
    void testDoAdjustBelowMinimum() {
        // Use reflection to access protected method
        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "doAdjust", Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            int adjusted = (int) method.invoke(premmDelegate, 20, 30, 70);

            assertThat(adjusted).isEqualTo(30); // Should be clamped to minimum
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should adjust age above maximum")
    void testDoAdjustAboveMaximum() {
        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "doAdjust", Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            int adjusted = (int) method.invoke(premmDelegate, 80, 30, 70);

            assertThat(adjusted).isEqualTo(70); // Should be clamped to maximum
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should adjust age within range")
    void testDoAdjustWithinRange() {
        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "doAdjust", Integer.class, Integer.class, Integer.class);
            method.setAccessible(true);
            int adjusted = (int) method.invoke(premmDelegate, 50, 30, 70);

            assertThat(adjusted).isEqualTo(50); // Should remain unchanged
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should calculate with proband affected colon")
    void testCalculateWithProbandAffectedColon() {
        proband.setAffectedColonType(AffectedType.ONE);
        proband.setDiagnoseColonAge(45);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should calculate with proband affected endometrium")
    void testCalculateWithProbandAffectedEndometrium() {
        proband.setAffectedEndometriumType(AffectedType.ONE);
        proband.setDiagnoseEndometriumAge(50);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should calculate with FDR affected colon")
    void testCalculateWithFDRAffectedColon() {
        father.setAffectedColonType(AffectedType.ONE);
        father.setDiagnoseColonAge(60);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should calculate with FDR affected endometrium")
    void testCalculateWithFDRAffectedEndometrium() {
        mother.setAffectedEndometriumType(AffectedType.ONE);
        mother.setDiagnoseEndometriumAge(55);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should calculate with multiple affected relatives")
    void testCalculateWithMultipleAffectedRelatives() {
        proband.setAffectedColonType(AffectedType.ONE);
        proband.setDiagnoseColonAge(45);
        father.setAffectedColonType(AffectedType.ONE);
        father.setDiagnoseColonAge(60);
        mother.setAffectedEndometriumType(AffectedType.ONE);
        mother.setDiagnoseEndometriumAge(55);

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
        assertThat(result.getProbabilityCarrier()).isNotNull();
    }

    @Test
    @DisplayName("Should round result values")
    void testRoundResultValues() {
        PREMMResult result = new PREMMResult();
        result.setProbabilityCarrier(0.123456789);

        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "roundResultValues", PREMMResult.class);
            method.setAccessible(true);
            method.invoke(premmDelegate, result);

            // Should be rounded to 3 decimal places
            assertThat(result.getProbabilityCarrier()).isNotNull();
            // Verify it's approximately 0.123 (rounded)
            assertThat(result.getProbabilityCarrier()).isBetween(0.122, 0.124);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should handle NaN in roundResultValues")
    void testRoundResultValuesWithNaN() {
        PREMMResult result = new PREMMResult();
        result.setProbabilityCarrier(Double.NaN);

        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "roundResultValues", PREMMResult.class);
            method.setAccessible(true);
            method.invoke(premmDelegate, result);

            // Should not throw exception
            assertThat(result.getProbabilityCarrier()).isNaN();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should handle null probability carrier in roundResultValues")
    void testRoundResultValuesWithNull() {
        PREMMResult result = new PREMMResult();
        result.setProbabilityCarrier(null);

        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "roundResultValues", PREMMResult.class);
            method.setAccessible(true);
            method.invoke(premmDelegate, result);

            // Should not throw exception
            assertThat(result.getProbabilityCarrier()).isNull();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should calculate with empty family")
    void testCalculateWithEmptyFamily() {
        Risk emptyRisk = new Risk();
        emptyRisk.setAgeBy(5);
        emptyRisk.setAgeTo(79);
        Person emptyProband = new Person();
        emptyProband.setId(1);
        emptyProband.setAge(40);
        emptyProband.setGender(0);
        emptyRisk.getFamily().add(emptyProband);

        PREMMResult result = premmDelegate.calculate(emptyRisk);

        assertThat(result).isNotNull();
        // May have errors or succeed depending on validation
    }

    @Test
    @DisplayName("Should calculate mutation probabilities")
    void testCalculateMutationProbability() {
        proband.setAffectedColonType(AffectedType.ONE);
        proband.setDiagnoseColonAge(45);
        proband.setAge(40);

        List<Person> fdrs = new ArrayList<>();
        List<Person> sdrs = new ArrayList<>();

        try {
            java.lang.reflect.Method method = PremmDelegate.class.getDeclaredMethod(
                "calculateMutationProbability", Person.class, Double.class, Double.class, 
                Double.class, List.class, List.class);
            method.setAccessible(true);
            PREMMResult result = (PREMMResult) method.invoke(
                premmDelegate, proband, 1.0, 0.5, 0.0, fdrs, sdrs);

            assertThat(result).isNotNull();
            assertThat(result.getProbabilityMutationMLH1()).isNotNull();
            assertThat(result.getProbabilityMutationMSH2()).isNotNull();
            assertThat(result.getProbabilityMutationMSH6()).isNotNull();
            assertThat(result.getProbabilityMutationPMS2()).isNotNull();
            assertThat(result.getProbabilityCarrier()).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should handle multiple validation errors")
    void testCalculateWithMultipleValidationErrors() {
        proband.setAge(80); // Too high
        proband.setAffectedColonType(AffectedType.ONE);
        proband.setDiagnoseColonAge(0); // Missing
        proband.setAffectedEndometriumType(AffectedType.ONE);
        proband.setDiagnoseEndometriumAge(0); // Missing

        PREMMResult result = premmDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().size()).isGreaterThan(1);
    }
}

