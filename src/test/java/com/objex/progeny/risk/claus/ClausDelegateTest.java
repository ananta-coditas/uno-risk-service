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

    @Test
    @DisplayName("Should test adjustPredictionForAge method")
    void testAdjustPredictionForAge() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("adjustPredictionForAge", 
            Double.class, Double.class, Double.class, Person.class);
        method.setAccessible(true);

        Person proband = new Person();
        proband.setAge(35); // THIRTY age key

        Double result = (Double) method.invoke(clausDelegate, 0.1, 0.2, 0.15, proband);
        
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should test adjustPredictionForAge when probandAgeKey.getMax() - proband.getAge() == 0")
    void testAdjustPredictionForAgeAtMaxAge() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("adjustPredictionForAge", 
            Double.class, Double.class, Double.class, Person.class);
        method.setAccessible(true);

        Person proband = new Person();
        proband.setAge(39); // THIRTY age key max

        Double result = (Double) method.invoke(clausDelegate, 0.1, 0.2, 0.15, proband);
        
        // Should return original predictionRisk when at max age
        assertThat(result).isEqualTo(0.1);
    }

    @Test
    @DisplayName("Should test getAgeTableValue method")
    void testGetAgeTableValue() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getAgeTableValue", 
            java.util.Map.class, ClausDelegate.AgeKey.class, ClausDelegate.AgeKey.class);
        method.setAccessible(true);

        java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>> table = 
            new java.util.HashMap<>();
        java.util.Map<ClausDelegate.AgeKey, Double> innerTable = new java.util.HashMap<>();
        innerTable.put(ClausDelegate.AgeKey.FOURTY, 0.05);
        table.put(ClausDelegate.AgeKey.THIRTY, innerTable);

        Double result = (Double) method.invoke(clausDelegate, table, 
            ClausDelegate.AgeKey.THIRTY, ClausDelegate.AgeKey.FOURTY);
        
        assertThat(result).isEqualTo(0.05);
    }

    @Test
    @DisplayName("Should test high method with newRes higher than current")
    void testHighWithNewResHigher() throws Exception {
        ClausResult newRes = new ClausResult();
        ClausResult current = new ClausResult();
        
        com.objex.risk.beans.Prediction pred1 = newRes.addPrediction();
        pred1.setAge(50);
        pred1.addRisk("breastRisk", 0.15);
        
        com.objex.risk.beans.Prediction pred2 = current.addPrediction();
        pred2.setAge(50);
        pred2.addRisk("breastRisk", 0.10);

        ClausResult result = clausDelegate.high(newRes, current);
        
        assertThat(result).isEqualTo(newRes);
    }

    @Test
    @DisplayName("Should test high method with current higher than newRes")
    void testHighWithCurrentHigher() throws Exception {
        ClausResult newRes = new ClausResult();
        ClausResult current = new ClausResult();
        
        com.objex.risk.beans.Prediction pred1 = newRes.addPrediction();
        pred1.setAge(50);
        pred1.addRisk("breastRisk", 0.10);
        
        com.objex.risk.beans.Prediction pred2 = current.addPrediction();
        pred2.setAge(50);
        pred2.addRisk("breastRisk", 0.15);

        ClausResult result = clausDelegate.high(newRes, current);
        
        assertThat(result).isEqualTo(current);
    }

    @Test
    @DisplayName("Should test high method with null current")
    void testHighWithNullCurrent() throws Exception {
        ClausResult newRes = new ClausResult();
        
        com.objex.risk.beans.Prediction pred1 = newRes.addPrediction();
        pred1.setAge(50);
        pred1.addRisk("breastRisk", 0.10);

        ClausResult result = clausDelegate.high(newRes, null);
        
        assertThat(result).isEqualTo(newRes);
    }

    @Test
    @DisplayName("Should test high method with null newRes")
    void testHighWithNullNewRes() throws Exception {
        ClausResult current = new ClausResult();
        
        com.objex.risk.beans.Prediction pred2 = current.addPrediction();
        pred2.setAge(50);
        pred2.addRisk("breastRisk", 0.10);

        ClausResult result = clausDelegate.high(null, current);
        
        assertThat(result).isEqualTo(current);
    }

    @Test
    @DisplayName("Should test high method with newRes having null breastRisk")
    void testHighWithNewResNullBreastRisk() throws Exception {
        ClausResult newRes = new ClausResult();
        ClausResult current = new ClausResult();
        
        com.objex.risk.beans.Prediction pred1 = newRes.addPrediction();
        pred1.setAge(50);
        // Don't add breastRisk
        
        com.objex.risk.beans.Prediction pred2 = current.addPrediction();
        pred2.setAge(50);
        pred2.addRisk("breastRisk", 0.10);

        ClausResult result = clausDelegate.high(newRes, current);
        
        // Should return current when newRes has null breastRisk
        assertThat(result).isEqualTo(current);
    }

    @Test
    @DisplayName("Should test high method with current having null breastRisk")
    void testHighWithCurrentNullBreastRisk() throws Exception {
        ClausResult newRes = new ClausResult();
        ClausResult current = new ClausResult();
        
        com.objex.risk.beans.Prediction pred1 = newRes.addPrediction();
        pred1.setAge(50);
        pred1.addRisk("breastRisk", 0.10);
        
        com.objex.risk.beans.Prediction pred2 = current.addPrediction();
        pred2.setAge(50);
        // Don't add breastRisk

        ClausResult result = clausDelegate.high(newRes, current);
        
        // Should return newRes when current has null breastRisk
        assertThat(result).isEqualTo(newRes);
    }

    @Test
    @DisplayName("Should test getAffectedAgeKey with firstAffected true")
    void testGetAffectedAgeKeyFirstAffected() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getAffectedAgeKey", 
            List.class, boolean.class);
        method.setAccessible(true);

        List<Person> affectedFDRs = new ArrayList<>();
        Person person = new Person();
        person.setDiagnoseBreastAge(45);
        affectedFDRs.add(person);

        ClausDelegate.AgeKey result = (ClausDelegate.AgeKey) method.invoke(clausDelegate, affectedFDRs, true);
        
        assertThat(result).isEqualTo(ClausDelegate.AgeKey.FOURTY);
    }

    @Test
    @DisplayName("Should test getAffectedAgeKey with firstAffected false")
    void testGetAffectedAgeKeySecondAffected() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getAffectedAgeKey", 
            List.class, boolean.class);
        method.setAccessible(true);

        List<Person> affectedFDRs = new ArrayList<>();
        Person person1 = new Person();
        person1.setDiagnoseBreastAge(45);
        affectedFDRs.add(person1);
        Person person2 = new Person();
        person2.setDiagnoseBreastAge(55);
        affectedFDRs.add(person2);

        ClausDelegate.AgeKey result = (ClausDelegate.AgeKey) method.invoke(clausDelegate, affectedFDRs, false);
        
        assertThat(result).isEqualTo(ClausDelegate.AgeKey.FIFTY);
    }

    @Test
    @DisplayName("Should test getAffectedAgeKey with empty list")
    void testGetAffectedAgeKeyEmptyList() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getAffectedAgeKey", 
            List.class, boolean.class);
        method.setAccessible(true);

        List<Person> affectedFDRs = new ArrayList<>();

        ClausDelegate.AgeKey result = (ClausDelegate.AgeKey) method.invoke(clausDelegate, affectedFDRs, true);
        
        assertThat(result).isEqualTo(ClausDelegate.AgeKey.UNSUPPORTED);
    }

    @Test
    @DisplayName("Should test getAffectedAgeKey with index out of bounds")
    void testGetAffectedAgeKeyIndexOutOfBounds() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getAffectedAgeKey", 
            List.class, boolean.class);
        method.setAccessible(true);

        List<Person> affectedFDRs = new ArrayList<>();
        Person person = new Person();
        person.setDiagnoseBreastAge(45);
        affectedFDRs.add(person);

        // Request second affected (index 1) but only one exists
        ClausDelegate.AgeKey result = (ClausDelegate.AgeKey) method.invoke(clausDelegate, affectedFDRs, false);
        
        assertThat(result).isEqualTo(ClausDelegate.AgeKey.UNSUPPORTED);
    }

    @Test
    @DisplayName("Should test getOrderedAffectedBreast method")
    void testGetOrderedAffectedBreast() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getOrderedAffectedBreast", 
            List.class);
        method.setAccessible(true);

        List<Person> relatives = new ArrayList<>();
        Person person1 = new Person();
        person1.setAffectedBreastType(AffectedType.ONE);
        person1.setDiagnoseBreastAge(50);
        relatives.add(person1);
        
        Person person2 = new Person();
        person2.setAffectedBreastType(AffectedType.ONE);
        person2.setDiagnoseBreastAge(40);
        relatives.add(person2);
        
        Person person3 = new Person();
        person3.setAffectedBreastType(AffectedType.NONE);
        relatives.add(person3);

        @SuppressWarnings("unchecked")
        List<Person> result = (List<Person>) method.invoke(clausDelegate, relatives);
        
        assertThat(result).hasSize(2);
        // Should be sorted by diagnoseBreastAge
        assertThat(result.get(0).getDiagnoseBreastAge()).isEqualTo(40);
        assertThat(result.get(1).getDiagnoseBreastAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should test getOrderedAffectedOvarian method")
    void testGetOrderedAffectedOvarian() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getOrderedAffectedOvarian", 
            List.class);
        method.setAccessible(true);

        List<Person> relatives = new ArrayList<>();
        Person person1 = new Person();
        person1.setAffectedOvarianType(AffectedType.ONE);
        person1.setDiagnoseOvarianAge(50);
        person1.setDiagnoseBreastAge(50); // Comparator uses diagnoseBreastAge
        relatives.add(person1);
        
        Person person2 = new Person();
        person2.setAffectedOvarianType(AffectedType.ONE);
        person2.setDiagnoseOvarianAge(40);
        person2.setDiagnoseBreastAge(40); // Comparator uses diagnoseBreastAge
        relatives.add(person2);
        
        Person person3 = new Person();
        person3.setAffectedOvarianType(AffectedType.NONE);
        relatives.add(person3);

        @SuppressWarnings("unchecked")
        List<Person> result = (List<Person>) method.invoke(clausDelegate, relatives);
        
        assertThat(result).hasSize(2);
        // Should be sorted by diagnoseBreastAge (comparator uses diagnoseBreastAge, not diagnoseOvarianAge)
        assertThat(result.get(0).getDiagnoseBreastAge()).isEqualTo(40);
        assertThat(result.get(1).getDiagnoseBreastAge()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should test buildPrediction with two parameters")
    void testBuildPredictionTwoParams() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("buildPrediction", 
            ClausResult.class, Double.class, int.class);
        method.setAccessible(true);

        ClausResult result = new ClausResult();
        method.invoke(clausDelegate, result, 0.15, 50);
        
        assertThat(result.getPredictions()).hasSize(1);
        assertThat(result.getPredictions().get(0).getAge()).isEqualTo(50);
        assertThat(result.getPredictions().get(0).getResult("breastRisk")).isEqualTo(0.15);
    }

    @Test
    @DisplayName("Should test buildPrediction with five parameters")
    void testBuildPredictionFiveParams() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("buildPrediction", 
            ClausResult.class, Double.class, Double.class, int.class, Double.class);
        method.setAccessible(true);

        ClausResult result = new ClausResult();
        method.invoke(clausDelegate, result, 0.2, 0.1, 50, 0.5);
        
        assertThat(result.getPredictions()).hasSize(1);
        assertThat(result.getPredictions().get(0).getAge()).isEqualTo(50);
        Double risk = result.getPredictions().get(0).getResult("breastRisk");
        assertThat(risk).isNotNull();
    }

    @Test
    @DisplayName("Should test buildPrediction with null risk")
    void testBuildPredictionWithNullRisk() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("buildPrediction", 
            ClausResult.class, Double.class, int.class);
        method.setAccessible(true);

        ClausResult result = new ClausResult();
        method.invoke(clausDelegate, result, null, 50);
        
        assertThat(result.getPredictions()).hasSize(1);
        assertThat(result.getPredictions().get(0).getAge()).isEqualTo(50);
        assertThat(result.getPredictions().get(0).getResult("breastRisk")).isNull();
    }

    @Test
    @DisplayName("Should calculate with two affected SDR breast (maternal)")
    void testCalculateWithTwoAffectedSDRMaternal() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            Person mother = new Person();
            mother.setId(2);
            mother.setGender(0);
            parents.add(mother);
            
            Person father = new Person();
            father.setId(3);
            father.setGender(1);
            parents.add(father);
            
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();
            
            Person affectedSDR1 = new Person();
            affectedSDR1.setId(4);
            affectedSDR1.setAffectedBreastType(AffectedType.ONE);
            affectedSDR1.setDiagnoseBreastAge(50);
            sdrs.add(affectedSDR1);
            
            Person affectedSDR2 = new Person();
            affectedSDR2.setId(5);
            affectedSDR2.setAffectedBreastType(AffectedType.ONE);
            affectedSDR2.setDiagnoseBreastAge(55);
            sdrs.add(affectedSDR2);

            List<Person> matRels = new ArrayList<>();
            matRels.add(affectedSDR1);
            matRels.add(affectedSDR2);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(mother), any(Risk.class), eq(Person.class)))
                    .thenReturn(matRels);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(father), any(Risk.class), eq(Person.class)))
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
    @DisplayName("Should calculate with one maternal and one paternal SDR")
    void testCalculateWithMaternalAndPaternalSDR() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            Person mother = new Person();
            mother.setId(2);
            mother.setGender(0);
            parents.add(mother);
            
            Person father = new Person();
            father.setId(3);
            father.setGender(1);
            parents.add(father);
            
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();
            
            Person affectedMatSDR = new Person();
            affectedMatSDR.setId(4);
            affectedMatSDR.setAffectedBreastType(AffectedType.ONE);
            affectedMatSDR.setDiagnoseBreastAge(50);
            sdrs.add(affectedMatSDR);
            
            Person affectedPatSDR = new Person();
            affectedPatSDR.setId(5);
            affectedPatSDR.setAffectedBreastType(AffectedType.ONE);
            affectedPatSDR.setDiagnoseBreastAge(55);
            sdrs.add(affectedPatSDR);

            List<Person> matRels = new ArrayList<>();
            matRels.add(affectedMatSDR);
            List<Person> patRels = new ArrayList<>();
            patRels.add(affectedPatSDR);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(mother), any(Risk.class), eq(Person.class)))
                    .thenReturn(matRels);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(father), any(Risk.class), eq(Person.class)))
                    .thenReturn(patRels);
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
    @DisplayName("Should calculate with mother and maternal aunt")
    void testCalculateWithMotherAndMaternalAunt() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            Person mother = new Person();
            mother.setId(2);
            mother.setGender(0);
            mother.setAffectedBreastType(AffectedType.ONE);
            mother.setDiagnoseBreastAge(50);
            parents.add(mother);
            
            Person father = new Person();
            father.setId(3);
            father.setGender(1);
            parents.add(father);
            
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();
            
            Person maternalAunt = new Person();
            maternalAunt.setId(4);
            maternalAunt.setAffectedBreastType(AffectedType.ONE);
            maternalAunt.setDiagnoseBreastAge(55);
            maternalAunt.setGender(0);
            sdrs.add(maternalAunt);

            List<Person> matRels = new ArrayList<>();
            matRels.add(maternalAunt);
            List<Person> matAunts = new ArrayList<>();
            matAunts.add(maternalAunt);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(mother), any(Risk.class), eq(Person.class)))
                    .thenReturn(matRels);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(father), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(eq(mother), eq(matRels)))
                    .thenReturn(matAunts);
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(eq(matAunts), eq(true)))
                    .thenReturn(matAunts);
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should calculate with mother and paternal aunt")
    void testCalculateWithMotherAndPaternalAunt() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            List<Person> parents = new ArrayList<>();
            Person mother = new Person();
            mother.setId(2);
            mother.setGender(0);
            mother.setAffectedBreastType(AffectedType.ONE);
            mother.setDiagnoseBreastAge(50);
            parents.add(mother);
            
            Person father = new Person();
            father.setId(3);
            father.setGender(1);
            parents.add(father);
            
            List<Person> fdrs = new ArrayList<>();
            List<Person> sdrs = new ArrayList<>();
            
            Person paternalAunt = new Person();
            paternalAunt.setId(4);
            paternalAunt.setAffectedBreastType(AffectedType.ONE);
            paternalAunt.setDiagnoseBreastAge(55);
            paternalAunt.setGender(0);
            sdrs.add(paternalAunt);

            List<Person> patRels = new ArrayList<>();
            patRels.add(paternalAunt);
            List<Person> patAunts = new ArrayList<>();
            patAunts.add(paternalAunt);

            mockedUtils.when(() -> Utils.getParents(any(Person.class), any(Risk.class)))
                    .thenReturn(parents);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(fdrs);
            mockedUtils.when(() -> Utils.getSecondDegreeRelatives(any(Risk.class), eq(Person.class)))
                    .thenReturn(sdrs);
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(mother), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(father), any(Risk.class), eq(Person.class)))
                    .thenReturn(patRels);
            mockedUtils.when(() -> Utils.getSiblings(eq(father), eq(patRels)))
                    .thenReturn(patAunts);
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(eq(patAunts), eq(true)))
                    .thenReturn(patAunts);
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            ClausResult result = clausDelegate.calculate(risk);

            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo(ClausDelegate.CLAUS_VERSION);
            assertThat(result.hasErrors()).isFalse();
        }
    }

    @Test
    @DisplayName("Should test getSingleSelectTable with various scenarios")
    void testGetSingleSelectTable() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getSingleSelectTable", 
            int.class, int.class, int.class);
        method.setAccessible(true);

        // Test: affectedSDRs >= 0 && affectedFDRs == 1 && affectedOvaraianFDRs == 1
        @SuppressWarnings("unchecked")
        java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>> result1 = 
            (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>>) 
            method.invoke(clausDelegate, 0, 1, 1);
        assertThat(result1).isNotNull();

        // Test: affectedSDRs >= 0 && affectedFDRs == 1 && affectedOvaraianFDRs != 1
        @SuppressWarnings("unchecked")
        java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>> result2 = 
            (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>>) 
            method.invoke(clausDelegate, 0, 1, 0);
        assertThat(result2).isNotNull();

        // Test: affectedSDRs == 1 && affectedFDRs == 0
        @SuppressWarnings("unchecked")
        java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>> result3 = 
            (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>>) 
            method.invoke(clausDelegate, 1, 0, 0);
        assertThat(result3).isNotNull();

        // Test: affectedSDRs == 0 && affectedFDRs == 0 && affectedOvaraianFDRs == 1
        @SuppressWarnings("unchecked")
        java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>> result4 = 
            (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>>) 
            method.invoke(clausDelegate, 0, 0, 1);
        assertThat(result4).isNotNull();

        // Test: affectedSDRs == 0 && affectedFDRs == 0 && affectedOvaraianFDRs >= 2
        @SuppressWarnings("unchecked")
        java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>> result5 = 
            (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, Double>>) 
            method.invoke(clausDelegate, 0, 0, 2);
        assertThat(result5).isNotNull();
    }

    @Test
    @DisplayName("Should test getMultiSelectTable with various scenarios")
    void testGetMultiSelectTable() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("getMultiSelectTable", 
            int.class, int.class, Person.class, Person.class, Risk.class);
        method.setAccessible(true);

        Person mother = new Person();
        mother.setId(2);
        mother.setGender(0);
        mother.setAffectedBreastType(AffectedType.NONE);
        
        Person father = new Person();
        father.setId(3);
        father.setGender(1);

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(any(Person.class), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.getSiblings(any(Person.class), any(List.class)))
                    .thenReturn(new ArrayList<>());
            mockedUtils.when(() -> Utils.filterSex(any(List.class), any(Boolean.class)))
                    .thenReturn(new ArrayList<>());

            // Test: sdrCount == 0 && fdrCount >= 2
            @SuppressWarnings("unchecked")
            java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, 
                java.util.Map<ClausDelegate.AgeKey, Double>>> result1 = 
                (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, 
                    java.util.Map<ClausDelegate.AgeKey, Double>>>) 
                method.invoke(clausDelegate, 0, 2, mother, father, risk);
            assertThat(result1).isNotNull();

            // Test: sdrCount >= 2 && fdrCount == 0 && affectedMatRels.size() >= 1 && affectedPatRels.size() >= 1
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(mother), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<Person>() {{ add(new Person()); }});
            mockedUtils.when(() -> Utils.getFirstDegreeRelatives(eq(father), any(Risk.class), eq(Person.class)))
                    .thenReturn(new ArrayList<Person>() {{ add(new Person()); }});
            
            @SuppressWarnings("unchecked")
            java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, 
                java.util.Map<ClausDelegate.AgeKey, Double>>> result2 = 
                (java.util.Map<ClausDelegate.AgeKey, java.util.Map<ClausDelegate.AgeKey, 
                    java.util.Map<ClausDelegate.AgeKey, Double>>>) 
                method.invoke(clausDelegate, 2, 0, mother, father, risk);
            assertThat(result2).isNotNull();
        }
    }

    @Test
    @DisplayName("Should test AgeKey boundary values")
    void testAgeKeyBoundaryValues() {
        assertThat(ClausDelegate.AgeKey.forAge(19)).isEqualTo(ClausDelegate.AgeKey.UNSUPPORTED);
        assertThat(ClausDelegate.AgeKey.forAge(20)).isEqualTo(ClausDelegate.AgeKey.TWENTY);
        assertThat(ClausDelegate.AgeKey.forAge(29)).isEqualTo(ClausDelegate.AgeKey.TWENTY);
        assertThat(ClausDelegate.AgeKey.forAge(30)).isEqualTo(ClausDelegate.AgeKey.THIRTY);
        assertThat(ClausDelegate.AgeKey.forAge(39)).isEqualTo(ClausDelegate.AgeKey.THIRTY);
        assertThat(ClausDelegate.AgeKey.forAge(40)).isEqualTo(ClausDelegate.AgeKey.FOURTY);
        assertThat(ClausDelegate.AgeKey.forAge(49)).isEqualTo(ClausDelegate.AgeKey.FOURTY);
        assertThat(ClausDelegate.AgeKey.forAge(50)).isEqualTo(ClausDelegate.AgeKey.FIFTY);
        assertThat(ClausDelegate.AgeKey.forAge(59)).isEqualTo(ClausDelegate.AgeKey.FIFTY);
        assertThat(ClausDelegate.AgeKey.forAge(60)).isEqualTo(ClausDelegate.AgeKey.SIXTY);
        assertThat(ClausDelegate.AgeKey.forAge(69)).isEqualTo(ClausDelegate.AgeKey.SIXTY);
        assertThat(ClausDelegate.AgeKey.forAge(70)).isEqualTo(ClausDelegate.AgeKey.SEVENTY);
        assertThat(ClausDelegate.AgeKey.forAge(79)).isEqualTo(ClausDelegate.AgeKey.SEVENTY);
        assertThat(ClausDelegate.AgeKey.forAge(80)).isEqualTo(ClausDelegate.AgeKey.UNSUPPORTED);
    }

    @Test
    @DisplayName("Should test adjustAge with various values")
    void testAdjustAgeVariousValues() throws Exception {
        java.lang.reflect.Method method = ClausDelegate.class.getDeclaredMethod("adjustAge", Integer.class);
        method.setAccessible(true);

        assertThat(method.invoke(clausDelegate, 50)).isEqualTo(50);
        assertThat(method.invoke(clausDelegate, 79)).isEqualTo(79);
        assertThat(method.invoke(clausDelegate, 80)).isEqualTo(79);
        assertThat(method.invoke(clausDelegate, 90)).isEqualTo(79);
        assertThat(method.invoke(clausDelegate, 100)).isEqualTo(79);
        assertThat(method.invoke(clausDelegate, 20)).isEqualTo(20);
    }
}

