package com.ambry.transformer.nccn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.util.APIConstants;
import com.objex.risk.beans.uno.Model.ModelName;

/**
 * Validates the NCCN clinical histories against the values the CARE NCCN logic understands.
 */
@Component
public class NCCNValidator extends RiskModelValidator<NCCNCoreRequest> {

    static final List<String> RELATIONSHIPS = List.of("PROBAND", "MOTHER", "FATHER", "SISTER", "BROTHER", "DAUGHTER",
        "SON", "NIECE", "NEPHEW", "HALF_SISTER", "HALF_BROTHER", "GRANDMOTHER", "GRANDFATHER", "GRANDDAUGHTER",
        "GRANDSON", "AUNT", "UNCLE", "FIRST_COUSIN_MALE", "FIRST_COUSIN_FEMALE", "GREAT_GRANDMOTHER",
        "GREAT_GRANDFATHER", "GREAT_AUNT", "GREAT_UNCLE", "GREAT_NEPHEW", "GREAT_NIECE", "GREAT_GRANDDAUGHTER",
        "GREAT_GRANDSON", "HALF_AUNT", "HALF_UNCLE", "HALF_NIECE", "HALF_NEPHEW", "OTHERS");

    static final List<String> SIDES_OF_FAMILY = List.of("MATERNAL", "PATERNAL", "OTHER");

    static final List<String> SEXES_AT_BIRTH = List.of("MALE", "FEMALE", "UNKNOWN");

    static final List<String> INPUT_OPTIONS = List.of("YES", "NO", "NOT_PROVIDED");

    private static final String FIRST_COUSIN = "FIRST_COUSIN";

    private static final String PROBAND = "PROBAND";

    private static final String CLINICAL_HISTORY_FIELD = "clinicalHistory";

    @Override
    public void validate(NCCNCoreRequest request, ValidationErrors validationErros) {
        if (validationErros == null) {
            return;
        }
        findViolations(request).forEach(message -> validationErros.rejectValue(CLINICAL_HISTORY_FIELD, message));
    }

    /**
     * Lists the validation failures of the request, at most one per clinical history (the first failing
     * check, in the order CARE applied them), in the order the clinical histories appear.
     *
     * @param request the request to check; may be null
     * @return the failure messages, empty when the request is valid
     */
    public List<String> findViolations(NCCNCoreRequest request) {
        List<String> violations = new ArrayList<>();
        if (request == null || request.getClinicalHistory() == null) {
            return violations;
        }
        for (ClinicalHistory clinicalHistory : request.getClinicalHistory()) {
            String violation = findViolation(clinicalHistory);
            if (violation != null) {
                violations.add(violation);
            }
        }
        return violations;
    }

    private String findViolation(ClinicalHistory clinicalHistory) {
        String relationship = clinicalHistory.getRelationship();
        if (!isAllowed(relationship, RELATIONSHIPS)) {
            return APIConstants.VALID_RELATIONSHIP;
        }
        if (relationship.contains(FIRST_COUSIN) && !isAllowed(clinicalHistory.getSexAtBirth(), SEXES_AT_BIRTH)) {
            return APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST;
        }
        if (!isAllowed(clinicalHistory.getSideOfFam(), SIDES_OF_FAMILY)) {
            return APIConstants.VALID_SIDE_OF_FAM;
        }
        if (StringUtils.hasLength(clinicalHistory.getSexAtBirth()) && !isAllowed(clinicalHistory.getSexAtBirth(), SEXES_AT_BIRTH)) {
            return APIConstants.VALID_SEX_AT_BIRTH;
        }
        if (!isAllowed(clinicalHistory.getCancerOverall(), INPUT_OPTIONS)) {
            return APIConstants.VALID_CANCER_OVERALL;
        }
        if (PROBAND.equalsIgnoreCase(relationship) && StringUtils.hasLength(clinicalHistory.getPolypOverall())
            && !isAllowed(clinicalHistory.getPolypOverall(), INPUT_OPTIONS)) {
            return APIConstants.VALID_POLYP_OVERALL_PROBAND;
        }
        return null;
    }

    private static boolean isAllowed(String value, List<String> allowedValues) {
        return StringUtils.hasLength(value) && allowedValues.contains(value.toUpperCase());
    }

    @Override
    public ModelName getModelName() {
        return ModelName.NCCN;
    }

}
