package com.objex.progeny.risk.claus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.objex.progeny.risk.Utils;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.claus.Risk;

/**
 * Unit tests for {@link ClausDelegate}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClausDelegate Unit Tests")
@SuppressWarnings("unchecked")
class ClausDelegateTest {

    @InjectMocks
    private ClausDelegate clausDelegate;

    private Risk risk;
    private Person proband;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        risk.setAgeBy(5);
        risk.setAgeTo(80);

        proband = new Person();
        proband.setId(1);
        proband.setAge(40);
        proband.setGender(0); // Female
        proband.setAffectedBreastType(AffectedType.NONE);
        proband.setAffectedOvarianType(AffectedType.NONE);

        risk.getFamily().add(proband);
    }

    @Test
    @DisplayName("Should return correct version")
    void testGetVersion() {
        assertThat(ClausDelegate.CLAUS_VERSION).isEqualTo("Claus,Risch,Thompson(1994) Claus,Risch,Thompson(1993)");
    }

    @Test
    @DisplayName("Should calculate risk successfully with valid input and no affected relatives")
    void testCalculateWithValidInputNoAffectedRelatives() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.getPredictions()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Should return error when proband age is too high for model settings")
    void testCalculateWithAgeTooHigh() {
        proband.setAge(85);
        risk.setAgeTo(80);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "ageTooHighForModelSettings".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when proband is male")
    void testCalculateWithMaleProband() {
        proband.setGender(1); // Male

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "probandCannotBeMale".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when proband age is less than 29")
    void testCalculateWithAgeLessThan29() {
        proband.setAge(25);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "probandMustBeAtLeastTwentyNine".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when proband age is over 79")
    void testCalculateWithAgeOver79() {
        proband.setAge(80);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "probandAgeOverSeventyNine".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when proband has affected breast")
    void testCalculateWithAffectedBreast() {
        proband.setAffectedBreastType(AffectedType.ONE);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "unsupportedCancerForProband".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when proband has affected ovarian")
    void testCalculateWithAffectedOvarian() {
        proband.setAffectedOvarianType(AffectedType.ONE);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "unsupportedCancerForProband".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when member has affected ovarian but missing age")
    void testValidateMemberDataWithOvarianMissingAge() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedOvarianType(AffectedType.ONE);
        member.setDiagnoseOvarianAge(0);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "missingAgeOvarian".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when member has affected breast age under 20")
    void testValidateMemberDataWithBreastAgeUnder20() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedBreastType(AffectedType.ONE);
        member.setDiagnoseBreastAge(18);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "affectedBreastAgeUnderTwenty".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should return error when member has affected breast but missing age")
    void testValidateMemberDataWithBreastMissingAge() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedBreastType(AffectedType.ONE);
        member.setDiagnoseBreastAge(0);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "missingAgeBreast".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should test AgeKey enum forAge method")
    void testAgeKeyForAge() {
        assertThat(ClausDelegate.AgeKey.forAge(25)).isEqualTo(ClausDelegate.AgeKey.TWENTY);
        assertThat(ClausDelegate.AgeKey.forAge(35)).isEqualTo(ClausDelegate.AgeKey.THIRTY);
        assertThat(ClausDelegate.AgeKey.forAge(45)).isEqualTo(ClausDelegate.AgeKey.FOURTY);
        assertThat(ClausDelegate.AgeKey.forAge(55)).isEqualTo(ClausDelegate.AgeKey.FIFTY);
        assertThat(ClausDelegate.AgeKey.forAge(65)).isEqualTo(ClausDelegate.AgeKey.SIXTY);
        assertThat(ClausDelegate.AgeKey.forAge(75)).isEqualTo(ClausDelegate.AgeKey.SEVENTY);
        assertThat(ClausDelegate.AgeKey.forAge(15)).isEqualTo(ClausDelegate.AgeKey.UNSUPPORTED);
        assertThat(ClausDelegate.AgeKey.forAge(85)).isEqualTo(ClausDelegate.AgeKey.UNSUPPORTED);
    }

    @Test
    @DisplayName("Should test AgeKey enum in method")
    void testAgeKeyIn() {
        assertThat(ClausDelegate.AgeKey.TWENTY.in(25)).isTrue();
        assertThat(ClausDelegate.AgeKey.TWENTY.in(20)).isTrue();
        assertThat(ClausDelegate.AgeKey.TWENTY.in(29)).isTrue();
        assertThat(ClausDelegate.AgeKey.TWENTY.in(30)).isFalse();
        assertThat(ClausDelegate.AgeKey.THIRTY.in(35)).isTrue();
        assertThat(ClausDelegate.AgeKey.THIRTY.in(39)).isTrue();
    }

    @Test
    @DisplayName("Should test AgeKey enum getMin and getMax methods")
    void testAgeKeyGetMinMax() {
        assertThat(ClausDelegate.AgeKey.TWENTY.getMin()).isEqualTo(20);
        assertThat(ClausDelegate.AgeKey.TWENTY.getMax()).isEqualTo(29);
        assertThat(ClausDelegate.AgeKey.THIRTY.getMin()).isEqualTo(30);
        assertThat(ClausDelegate.AgeKey.THIRTY.getMax()).isEqualTo(39);
        assertThat(ClausDelegate.AgeKey.SEVENTY.getMin()).isEqualTo(70);
        assertThat(ClausDelegate.AgeKey.SEVENTY.getMax()).isEqualTo(79);
    }

    @Test
    @DisplayName("Should test adjustAge method")
    void testAdjustAge() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("adjustAge", Integer.class);
        method.setAccessible(true);

        assertThat(method.invoke(clausDelegate, 50)).isEqualTo(50);
        assertThat(method.invoke(clausDelegate, 79)).isEqualTo(79);
        assertThat(method.invoke(clausDelegate, 80)).isEqualTo(79);
        assertThat(method.invoke(clausDelegate, 100)).isEqualTo(79);
    }

    @Test
    @DisplayName("Should calculate with single affected FDR breast")
    void testCalculateWithSingleAffectedFDRBreast() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            Person affectedFDR = new Person();
            affectedFDR.setId(2);
            affectedFDR.setAffectedBreastType(AffectedType.ONE);
            affectedFDR.setDiagnoseBreastAge(45);
            affectedFDR.setMotherId(0);
            affectedFDR.setFatherId(1);
            fdrs.add(affectedFDR);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should calculate with single affected SDR breast")
    void testCalculateWithSingleAffectedSDRBreast() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            Person affectedSDR = new Person();
            affectedSDR.setId(2);
            affectedSDR.setAffectedBreastType(AffectedType.ONE);
            affectedSDR.setDiagnoseBreastAge(50);
            affectedSDR.setMotherId(0);
            affectedSDR.setFatherId(0);
            sdrs.add(affectedSDR);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should calculate with single affected FDR ovarian")
    void testCalculateWithSingleAffectedFDROvarian() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            Person affectedFDR = new Person();
            affectedFDR.setId(2);
            affectedFDR.setAffectedOvarianType(AffectedType.ONE);
            affectedFDR.setDiagnoseOvarianAge(50);
            affectedFDR.setMotherId(0);
            affectedFDR.setFatherId(1);
            fdrs.add(affectedFDR);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should calculate with two affected FDR breast")
    void testCalculateWithTwoAffectedFDRBreast() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            Person affectedFDR1 = new Person();
            affectedFDR1.setId(2);
            affectedFDR1.setAffectedBreastType(AffectedType.ONE);
            affectedFDR1.setDiagnoseBreastAge(40);
            affectedFDR1.setMotherId(0);
            affectedFDR1.setFatherId(1);
            fdrs.add(affectedFDR1);

            Person affectedFDR2 = new Person();
            affectedFDR2.setId(3);
            affectedFDR2.setAffectedBreastType(AffectedType.ONE);
            affectedFDR2.setDiagnoseBreastAge(50);
            affectedFDR2.setMotherId(0);
            affectedFDR2.setFatherId(1);
            fdrs.add(affectedFDR2);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should calculate with one breast and one ovarian FDR")
    void testCalculateWithOneBreastAndOneOvarianFDR() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            Person affectedBreastFDR = new Person();
            affectedBreastFDR.setId(2);
            affectedBreastFDR.setAffectedBreastType(AffectedType.ONE);
            affectedBreastFDR.setDiagnoseBreastAge(45);
            affectedBreastFDR.setAffectedOvarianType(AffectedType.NONE);
            affectedBreastFDR.setMotherId(0);
            affectedBreastFDR.setFatherId(1);
            fdrs.add(affectedBreastFDR);

            Person affectedOvarianFDR = new Person();
            affectedOvarianFDR.setId(3);
            affectedOvarianFDR.setAffectedBreastType(AffectedType.NONE);
            affectedOvarianFDR.setAffectedOvarianType(AffectedType.ONE);
            affectedOvarianFDR.setDiagnoseOvarianAge(50);
            affectedOvarianFDR.setMotherId(0);
            affectedOvarianFDR.setFatherId(1);
            fdrs.add(affectedOvarianFDR);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should handle proband age at boundary values")
    void testCalculateWithBoundaryAges() {
        // Test age 29 (minimum)
        proband.setAge(29);
        ClausResult result = clausDelegate.calculate(risk);
        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();

        // Test age 79 (maximum)
        proband.setAge(79);
        result = clausDelegate.calculate(risk);
        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should handle multiple validation errors")
    void testCalculateWithMultipleValidationErrors() {
        proband.setAge(25); // Less than 29
        proband.setGender(1); // Male
        proband.setAffectedBreastType(AffectedType.ONE);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Should test diagnoseAge comparator")
    void testDiagnoseAgeComparator() throws Exception {
        java.lang.reflect.Field field = ClausDelegate.class.getDeclaredField("diagnoseAge");
        field.setAccessible(true);
        java.util.Comparator<Person> comparator = (java.util.Comparator<Person>) field.get(clausDelegate);

        Person person1 = new Person();
        person1.setDiagnoseBreastAge(40);
        Person person2 = new Person();
        person2.setDiagnoseBreastAge(50);

        assertThat(comparator.compare(person1, person2)).isLessThan(0);
        assertThat(comparator.compare(person2, person1)).isGreaterThan(0);
        assertThat(comparator.compare(person1, person1)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should calculate with two affected FDR ovarian")
    void testCalculateWithTwoAffectedFDROvarian() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();

            Person affectedOvarianFDR1 = new Person();
            affectedOvarianFDR1.setId(2);
            affectedOvarianFDR1.setAffectedOvarianType(AffectedType.ONE);
            affectedOvarianFDR1.setDiagnoseOvarianAge(50);
            affectedOvarianFDR1.setAffectedBreastType(AffectedType.NONE);
            affectedOvarianFDR1.setMotherId(0);
            affectedOvarianFDR1.setFatherId(1);
            fdrs.add(affectedOvarianFDR1);

            Person affectedOvarianFDR2 = new Person();
            affectedOvarianFDR2.setId(3);
            affectedOvarianFDR2.setAffectedOvarianType(AffectedType.ONE);
            affectedOvarianFDR2.setDiagnoseOvarianAge(55);
            affectedOvarianFDR2.setAffectedBreastType(AffectedType.NONE);
            affectedOvarianFDR2.setMotherId(0);
            affectedOvarianFDR2.setFatherId(1);
            fdrs.add(affectedOvarianFDR2);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should handle member with valid affected breast age")
    void testValidateMemberDataWithValidBreastAge() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedBreastType(AffectedType.ONE);
        member.setDiagnoseBreastAge(45);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        // Should not have missingAgeBreast error
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "missingAgeBreast".equals(err.getCode()))).isFalse();
    }

    @Test
    @DisplayName("Should handle member with valid affected ovarian age")
    void testValidateMemberDataWithValidOvarianAge() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedOvarianType(AffectedType.ONE);
        member.setDiagnoseOvarianAge(50);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        // Should not have missingAgeOvarian error
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "missingAgeOvarian".equals(err.getCode()))).isFalse();
    }

    @Test
    @DisplayName("Should handle member with affected breast age exactly 20")
    void testValidateMemberDataWithBreastAgeExactly20() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedBreastType(AffectedType.ONE);
        member.setDiagnoseBreastAge(20);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        // Age 20 is valid (>= 20)
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "affectedBreastAgeUnderTwenty".equals(err.getCode()))).isFalse();
    }

    @Test
    @DisplayName("Should handle member with affected breast age 19")
    void testValidateMemberDataWithBreastAge19() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedBreastType(AffectedType.ONE);
        member.setDiagnoseBreastAge(19);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "affectedBreastAgeUnderTwenty".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should handle proband age exactly 29")
    void testCalculateWithAgeExactly29() {
        proband.setAge(29);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should handle proband age exactly 79")
    void testCalculateWithAgeExactly79() {
        proband.setAge(79);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Should handle proband age 28")
    void testCalculateWithAge28() {
        proband.setAge(28);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "probandMustBeAtLeastTwentyNine".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should handle proband age 80")
    void testCalculateWithAge80() {
        proband.setAge(80);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "probandAgeOverSeventyNine".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should handle member with affected breast age 0 when not affected")
    void testValidateMemberDataWithBreastAge0NotAffected() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedBreastType(AffectedType.NONE);
        member.setDiagnoseBreastAge(0);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        // Should not have missingAgeBreast error when not affected
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "missingAgeBreast".equals(err.getCode()))).isFalse();
    }

    @Test
    @DisplayName("Should handle member with affected ovarian age 0 when not affected")
    void testValidateMemberDataWithOvarianAge0NotAffected() {
        Person member = new Person();
        member.setId(2);
        member.setAffectedOvarianType(AffectedType.NONE);
        member.setDiagnoseOvarianAge(0);
        risk.getFamily().add(member);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        // Should not have missingAgeOvarian error when not affected
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "missingAgeOvarian".equals(err.getCode()))).isFalse();
    }

    @Test
    @DisplayName("Should handle age exactly at ageTo boundary")
    void testCalculateWithAgeAtAgeTo() {
        proband.setAge(80);
        risk.setAgeTo(80);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "ageTooHighForModelSettings".equals(err.getCode()))).isTrue();
    }

    @Test
    @DisplayName("Should handle age one less than ageTo")
    void testCalculateWithAgeOneLessThanAgeTo() {
        proband.setAge(79);
        risk.setAgeTo(80);

        ClausResult result = clausDelegate.calculate(risk);

        assertThat(result).isNotNull();
        // Should not have ageTooHighForModelSettings error
        assertThat(result.getErrors().stream()
                .anyMatch(err -> "ageTooHighForModelSettings".equals(err.getCode()))).isFalse();
    }
}

