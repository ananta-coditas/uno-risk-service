package com.ambry.transformer.nccn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.util.APIConstants;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("NCCNValidator Unit Tests")
class NCCNValidatorTest {

    private NCCNValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NCCNValidator();
    }

    @Test
    @DisplayName("Should return NCCN model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.NCCN);
    }

    @Test
    @DisplayName("Should validate without errors for valid request")
    void testValidateWithValidRequest() {
        NCCNCoreRequest request = new NCCNCoreRequest();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(request, validationErrors);

        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should validate with null request")
    void testValidateWithNullRequest() {
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(null, validationErrors);

        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should validate with null validation errors")
    void testValidateWithNullValidationErrors() {
        NCCNCoreRequest request = new NCCNCoreRequest();

        // Should not throw exception
        validator.validate(request, null);
    }

    private static ClinicalHistory validHistory() {
        ClinicalHistory clinicalHistory = new ClinicalHistory();
        clinicalHistory.setRelationship("MOTHER");
        clinicalHistory.setSideOfFam("OTHER");
        clinicalHistory.setSexAtBirth("female");
        clinicalHistory.setCancerOverall("Yes");
        return clinicalHistory;
    }

    private static NCCNCoreRequest requestWith(ClinicalHistory... histories) {
        NCCNCoreRequest request = new NCCNCoreRequest();
        request.setClinicalHistory(new ArrayList<>(List.of(histories)));
        return request;
    }

    @Test
    @DisplayName("should_acceptHistory_when_allValuesAreAllowed")
    void should_acceptHistory_when_allValuesAreAllowed() {
        assertThat(validator.findViolations(requestWith(validHistory()))).isEmpty();
    }

    @Test
    @DisplayName("should_rejectRelationship_when_valueIsNotAnNccnRelationship")
    void should_rejectRelationship_when_valueIsNotAnNccnRelationship() {
        ClinicalHistory history = validHistory();
        history.setRelationship("OTHER");

        assertThat(validator.findViolations(requestWith(history))).containsExactly(APIConstants.VALID_RELATIONSHIP);
    }

    @Test
    @DisplayName("should_rejectCousin_when_sexAtBirthIsMissing")
    void should_rejectCousin_when_sexAtBirthIsMissing() {
        ClinicalHistory history = validHistory();
        history.setRelationship("FIRST_COUSIN_MALE");
        history.setSexAtBirth(null);

        assertThat(validator.findViolations(requestWith(history))).containsExactly(APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST);
    }

    @Test
    @DisplayName("should_rejectSideOfFamily_when_valueIsUnknown")
    void should_rejectSideOfFamily_when_valueIsUnknown() {
        ClinicalHistory history = validHistory();
        history.setSideOfFam("BOTH");

        assertThat(validator.findViolations(requestWith(history))).containsExactly(APIConstants.VALID_SIDE_OF_FAM);
    }

    @Test
    @DisplayName("should_rejectProbandPolyps_when_polypOverallIsNotAnInputOption")
    void should_rejectProbandPolyps_when_polypOverallIsNotAnInputOption() {
        ClinicalHistory history = validHistory();
        history.setRelationship("PROBAND");
        history.setPolypOverall("MAYBE");

        assertThat(validator.findViolations(requestWith(history))).containsExactly(APIConstants.VALID_POLYP_OVERALL_PROBAND);
    }

    @Test
    @DisplayName("should_reportOneViolationPerHistory_when_severalHistoriesAreInvalid")
    void should_reportOneViolationPerHistory_when_severalHistoriesAreInvalid() {
        ClinicalHistory badRelationship = validHistory();
        badRelationship.setRelationship(null);
        badRelationship.setCancerOverall(null);
        ClinicalHistory badCancerOverall = validHistory();
        badCancerOverall.setCancerOverall("SOMETIMES");
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(requestWith(badRelationship, validHistory(), badCancerOverall), validationErrors);

        assertThat(validationErrors.getErrors()).hasSize(2);
        assertThat(validator.findViolations(requestWith(badRelationship, validHistory(), badCancerOverall)))
            .containsExactly(APIConstants.VALID_RELATIONSHIP, APIConstants.VALID_CANCER_OVERALL);
    }

    @Test
    @DisplayName("should_exposeCareAllowLists_when_inspectingSupportedValues")
    void should_exposeCareAllowLists_when_inspectingSupportedValues() {
        assertThat(NCCNValidator.RELATIONSHIPS).contains("PROBAND", "MOTHER", "FATHER", "OTHERS").doesNotContain("OTHER");
        assertThat(NCCNValidator.SIDES_OF_FAMILY).containsExactly("MATERNAL", "PATERNAL", "OTHER");
        assertThat(NCCNValidator.SEXES_AT_BIRTH).containsExactly("MALE", "FEMALE", "UNKNOWN");
    }
}
