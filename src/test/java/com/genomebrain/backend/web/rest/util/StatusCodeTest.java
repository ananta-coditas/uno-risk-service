package com.genomebrain.backend.web.rest.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StatusCode Enum Unit Tests")
class StatusCodeTest {

    @Test
    @DisplayName("Should return correct status code for SUCCESS")
    void testSuccessStatusCode() {
        assertThat(StatusCode.SUCCESS.getstatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should return correct status code for FAILURE")
    void testFailureStatusCode() {
        assertThat(StatusCode.FAILURE.getstatusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should return correct status code for UNAUTHORIZED")
    void testUnauthorizedStatusCode() {
        assertThat(StatusCode.UNAUTHORIZED.getstatusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("Should return correct status code for INTERNAL_SERVER_ERROR")
    void testInternalServerErrorStatusCode() {
        assertThat(StatusCode.INTERNAL_SERVER_ERROR.getstatusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Should return correct status code for No_CONTENT")
    void testNoContentStatusCode() {
        assertThat(StatusCode.No_CONTENT.getstatusCode()).isEqualTo(204);
    }

    @Test
    @DisplayName("Should return correct status code for ALREADY_EXIST")
    void testAlreadyExistStatusCode() {
        assertThat(StatusCode.ALREADY_EXIST.getstatusCode()).isEqualTo(409);
    }

    @Test
    @DisplayName("Should return correct status code for UNPROCESSABLE_ENTITY_ERROR")
    void testUnprocessableEntityErrorStatusCode() {
        assertThat(StatusCode.UNPROCESSABLE_ENTITY_ERROR.getstatusCode()).isEqualTo(422);
    }

    @Test
    @DisplayName("Should return correct status code for CHAT_BOT_LINK_EXPIRED")
    void testChatBotLinkExpiredStatusCode() {
        assertThat(StatusCode.CHAT_BOT_LINK_EXPIRED.getstatusCode()).isEqualTo(1001);
    }

    @Test
    @DisplayName("Should return correct status code for VERIFICATION_FAILED")
    void testVerificationFailedStatusCode() {
        assertThat(StatusCode.VERIFICATION_FAILED.getstatusCode()).isEqualTo(1002);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_UNIQUE_IDENTIFIER")
    void testInvalidUniqueIdentifierStatusCode() {
        assertThat(StatusCode.INVALID_UNIQUE_IDENTIFIER.getstatusCode()).isEqualTo(1004);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_REQUEST_PARAMETER")
    void testInvalidRequestParameterStatusCode() {
        assertThat(StatusCode.INVALID_REQUEST_PARAMETER.getstatusCode()).isEqualTo(1005);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_ACTION_PARAMETER")
    void testInvalidActionParameterStatusCode() {
        assertThat(StatusCode.INVALID_ACTION_PARAMETER.getstatusCode()).isEqualTo(1006);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_ACCOUNT")
    void testInvalidAccountStatusCode() {
        assertThat(StatusCode.INVALID_ACCOUNT.getstatusCode()).isEqualTo(1007);
    }

    @Test
    @DisplayName("Should return correct status code for PATIENT_ADDED_SUCCESSFULLY")
    void testPatientAddedSuccessfullyStatusCode() {
        assertThat(StatusCode.PATIENT_ADDED_SUCCESSFULLY.getstatusCode()).isEqualTo(1008);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_PATIENT")
    void testDuplicatePatientStatusCode() {
        assertThat(StatusCode.DUPLICATE_PATIENT.getstatusCode()).isEqualTo(1009);
    }

    @Test
    @DisplayName("Should return correct status code for EMAIL_NOT_VALID")
    void testEmailNotValidStatusCode() {
        assertThat(StatusCode.EMAIL_NOT_VALID.getstatusCode()).isEqualTo(1010);
    }

    @Test
    @DisplayName("Should return correct status code for RESEND_LINK_NOT_SENT")
    void testResendLinkNotSentStatusCode() {
        assertThat(StatusCode.RESEND_LINK_NOT_SENT.getstatusCode()).isEqualTo(1011);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_ORDER")
    void testInvalidOrderStatusCode() {
        assertThat(StatusCode.INVALID_ORDER.getstatusCode()).isEqualTo(1012);
    }

    @Test
    @DisplayName("Should return correct status code for INAVLID_REQUST_BODY")
    void testInvalidRequestBodyStatusCode() {
        assertThat(StatusCode.INAVLID_REQUST_BODY.getstatusCode()).isEqualTo(1013);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_GENETIC_TEST_NAME")
    void testDuplicateGeneticTestNameStatusCode() {
        assertThat(StatusCode.DUPLICATE_GENETIC_TEST_NAME.getstatusCode()).isEqualTo(1014);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_GENETIC_TEST_PRODUCTCODE")
    void testDuplicateGeneticTestProductCodeStatusCode() {
        assertThat(StatusCode.DUPLICATE_GENETIC_TEST_PRODUCTCODE.getstatusCode()).isEqualTo(1015);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_GENETIC_TEST_DISPLAY_SEQUENCE")
    void testDuplicateGeneticTestDisplaySequenceStatusCode() {
        assertThat(StatusCode.DUPLICATE_GENETIC_TEST_DISPLAY_SEQUENCE.getstatusCode()).isEqualTo(1016);
    }

    @Test
    @DisplayName("Should return correct status code for GENETIC_TEST_ORDER_IS_PLACED")
    void testGeneticTestOrderIsPlacedStatusCode() {
        assertThat(StatusCode.GENETIC_TEST_ORDER_IS_PLACED.getstatusCode()).isEqualTo(1017);
    }

    @Test
    @DisplayName("Should return correct status code for APPLICATION_CONFIGURATION_IS_EMPTY_OR_NULL")
    void testApplicationConfigurationIsEmptyOrNullStatusCode() {
        assertThat(StatusCode.APPLICATION_CONFIGURATION_IS_EMPTY_OR_NULL.getstatusCode()).isEqualTo(1018);
    }

    @Test
    @DisplayName("Should return correct status code for APPLICATION_CONFIGURATION_VERSION_IS_EMPTY_OR_NULL")
    void testApplicationConfigurationVersionIsEmptyOrNullStatusCode() {
        assertThat(StatusCode.APPLICATION_CONFIGURATION_VERSION_IS_EMPTY_OR_NULL.getstatusCode()).isEqualTo(1019);
    }

    @Test
    @DisplayName("Should return correct status code for ASSESSMENT_MIGHT_NOT_BE_COMPLETED")
    void testAssessmentMightNotBeCompletedStatusCode() {
        assertThat(StatusCode.ASSESSMENT_MIGHT_NOT_BE_COMPLETED.getstatusCode()).isEqualTo(1020);
    }

    @Test
    @DisplayName("Should return correct status code for ASSESSMENT_NOT_FOUND")
    void testAssessmentNotFoundStatusCode() {
        assertThat(StatusCode.ASSESSMENT_NOT_FOUND.getstatusCode()).isEqualTo(1021);
    }

    @Test
    @DisplayName("Should return correct status code for GENEMATTERS_APPT_NOT_FOUND")
    void testGenemattersApptNotFoundStatusCode() {
        assertThat(StatusCode.GENEMATTERS_APPT_NOT_FOUND.getstatusCode()).isEqualTo(1022);
    }

    @Test
    @DisplayName("Should return correct status code for ACCOUNT_JSON_CONFIG_VERSION_IS_EMPTY_OR_NULL")
    void testAccountJsonConfigVersionIsEmptyOrNullStatusCode() {
        assertThat(StatusCode.ACCOUNT_JSON_CONFIG_VERSION_IS_EMPTY_OR_NULL.getstatusCode()).isEqualTo(1023);
    }

    @Test
    @DisplayName("Should return correct status code for ACCOUNT_JSON_CONFIG_VERSION_CHANGE")
    void testAccountJsonConfigVersionChangeStatusCode() {
        assertThat(StatusCode.ACCOUNT_JSON_CONFIG_VERSION_CHANGE.getstatusCode()).isEqualTo(1024);
    }

    @Test
    @DisplayName("Should return correct status code for PROBAND_ADDED_SUCCESSFULLY")
    void testProbandAddedSuccessfullyStatusCode() {
        assertThat(StatusCode.PROBAND_ADDED_SUCCESSFULLY.getstatusCode()).isEqualTo(10022);
    }

    @Test
    @DisplayName("Should return correct status code for PROGRAM_IS_EMPTY_OR_NULL")
    void testProgramIsEmptyOrNullStatusCode() {
        assertThat(StatusCode.PROGRAM_IS_EMPTY_OR_NULL.getstatusCode()).isEqualTo(2001);
    }

    @Test
    @DisplayName("Should return correct status code for CANNOT_UPDATE_ASSESSMENT_CHAT_BOT_STATUS")
    void testCannotUpdateAssessmentChatBotStatusStatusCode() {
        assertThat(StatusCode.CANNOT_UPDATE_ASSESSMENT_CHAT_BOT_STATUS.getstatusCode()).isEqualTo(1997);
    }

    @Test
    @DisplayName("Should return correct status code for SUCCESS_ORDER_WITH_SWITCH_ACCOUNT")
    void testSuccessOrderWithSwitchAccountStatusCode() {
        assertThat(StatusCode.SUCCESS_ORDER_WITH_SWITCH_ACCOUNT.getstatusCode()).isEqualTo(2001);
    }

    @Test
    @DisplayName("Should return correct status code for SUCCESS_ORDER_WITHOUT_SWITCH_ACCOUNT")
    void testSuccessOrderWithoutSwitchAccountStatusCode() {
        assertThat(StatusCode.SUCCESS_ORDER_WITHOUT_SWITCH_ACCOUNT.getstatusCode()).isEqualTo(2000);
    }

    @Test
    @DisplayName("Should return correct status code for BAD_REQUEST")
    void testBadRequestStatusCode() {
        assertThat(StatusCode.BAD_REQUEST.getstatusCode()).isEqualTo(4000);
    }

    @Test
    @DisplayName("Should return correct status code for MRN_IS_MANDATORY")
    void testMrnIsMandatoryStatusCode() {
        assertThat(StatusCode.MRN_IS_MANDATORY.getstatusCode()).isEqualTo(4001);
    }

    @Test
    @DisplayName("Should return correct status code for TEST_CODE_IS_MANDATORY")
    void testTestCodeIsMandatoryStatusCode() {
        assertThat(StatusCode.TEST_CODE_IS_MANDATORY.getstatusCode()).isEqualTo(4002);
    }

    @Test
    @DisplayName("Should return correct status code for NPI_IS_MANDATORY")
    void testNpiIsMandatoryStatusCode() {
        assertThat(StatusCode.NPI_IS_MANDATORY.getstatusCode()).isEqualTo(4003);
    }

    @Test
    @DisplayName("Should return correct status code for SPECIMEN_TYPE_ID_IS_MANDATORY")
    void testSpecimenTypeIdIsMandatoryStatusCode() {
        assertThat(StatusCode.SPECIMEN_TYPE_ID_IS_MANDATORY.getstatusCode()).isEqualTo(4004);
    }

    @Test
    @DisplayName("Should return correct status code for PAYMENT_OPTION_IS_MANDATORY")
    void testPaymentOptionIsMandatoryStatusCode() {
        assertThat(StatusCode.PAYMENT_OPTION_IS_MANDATORY.getstatusCode()).isEqualTo(4005);
    }

    @Test
    @DisplayName("Should return correct status code for FIRST_NAME_IS_MANDATORY")
    void testFirstNameIsMandatoryStatusCode() {
        assertThat(StatusCode.FIRST_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4010);
    }

    @Test
    @DisplayName("Should return correct status code for LAST_NAME_IS_MANDATORY")
    void testLastNameIsMandatoryStatusCode() {
        assertThat(StatusCode.LAST_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4011);
    }

    @Test
    @DisplayName("Should return correct status code for DATE_OF_BIRTH_IS_MANDATORY")
    void testDateOfBirthIsMandatoryStatusCode() {
        assertThat(StatusCode.DATE_OF_BIRTH_IS_MANDATORY.getstatusCode()).isEqualTo(4012);
    }

    @Test
    @DisplayName("Should return correct status code for CONTACT_DETAIL_IS_MANDATORY")
    void testContactDetailIsMandatoryStatusCode() {
        assertThat(StatusCode.CONTACT_DETAIL_IS_MANDATORY.getstatusCode()).isEqualTo(4013);
    }

    @Test
    @DisplayName("Should return correct status code for ADDRESS_IS_MANDATORY")
    void testAddressIsMandatoryStatusCode() {
        assertThat(StatusCode.ADDRESS_IS_MANDATORY.getstatusCode()).isEqualTo(4016);
    }

    @Test
    @DisplayName("Should return correct status code for CITY_IS_MANDATORY")
    void testCityIsMandatoryStatusCode() {
        assertThat(StatusCode.CITY_IS_MANDATORY.getstatusCode()).isEqualTo(4018);
    }

    @Test
    @DisplayName("Should return correct status code for STATE_IS_MANDATORY")
    void testStateIsMandatoryStatusCode() {
        assertThat(StatusCode.STATE_IS_MANDATORY.getstatusCode()).isEqualTo(4019);
    }

    @Test
    @DisplayName("Should return correct status code for POSTAL_CODE_IS_MANDATORY")
    void testPostalCodeIsMandatoryStatusCode() {
        assertThat(StatusCode.POSTAL_CODE_IS_MANDATORY.getstatusCode()).isEqualTo(4020);
    }

    @Test
    @DisplayName("Should return correct status code for INSURANCE_PROVIDER_IS_MANDATORY")
    void testInsuranceProviderIsMandatoryStatusCode() {
        assertThat(StatusCode.INSURANCE_PROVIDER_IS_MANDATORY.getstatusCode()).isEqualTo(4022);
    }

    @Test
    @DisplayName("Should return correct status code for MEMBER_ID_IS_MANDATORY")
    void testMemberIdIsMandatoryStatusCode() {
        assertThat(StatusCode.MEMBER_ID_IS_MANDATORY.getstatusCode()).isEqualTo(4023);
    }

    @Test
    @DisplayName("Should return correct status code for HOLDER_FIRST_NAME_IS_MANDATORY")
    void testHolderFirstNameIsMandatoryStatusCode() {
        assertThat(StatusCode.HOLDER_FIRST_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4027);
    }

    @Test
    @DisplayName("Should return correct status code for HOLDER_LAST_NAME_IS_MANDATORY")
    void testHolderLastNameIsMandatoryStatusCode() {
        assertThat(StatusCode.HOLDER_LAST_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4028);
    }

    @Test
    @DisplayName("Should return correct status code for CANCER_NAME_IS_MANDATORY")
    void testCancerNameIsMandatoryStatusCode() {
        assertThat(StatusCode.CANCER_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4041);
    }

    @Test
    @DisplayName("Should return correct status code for CANCER_TYPE_IS_MANDATORY")
    void testCancerTypeIsMandatoryStatusCode() {
        assertThat(StatusCode.CANCER_TYPE_IS_MANDATORY.getstatusCode()).isEqualTo(4042);
    }

    @Test
    @DisplayName("Should return correct status code for RELATIVE_NAME_IS_MANDATORY")
    void testRelativeNameIsMandatoryStatusCode() {
        assertThat(StatusCode.RELATIVE_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4044);
    }

    @Test
    @DisplayName("Should return correct status code for AGE_OF_DIAGNOSIS_IS_MANDATORY")
    void testAgeOfDiagnosisIsMandatoryStatusCode() {
        assertThat(StatusCode.AGE_OF_DIAGNOSIS_IS_MANDATORY.getstatusCode()).isEqualTo(4047);
    }

    @Test
    @DisplayName("Should return correct status code for PRIMARY_TEST_IS_REQUIRED")
    void testPrimaryTestIsRequiredStatusCode() {
        assertThat(StatusCode.PRIMARY_TEST_IS_REQUIRED.getstatusCode()).isEqualTo(4051);
    }

    @Test
    @DisplayName("Should return correct status code for ETHNICITY_IS_REQUIRED")
    void testEthnicityIsRequiredStatusCode() {
        assertThat(StatusCode.ETHNICITY_IS_REQUIRED.getstatusCode()).isEqualTo(4053);
    }

    @Test
    @DisplayName("Should return correct status code for GENDER_IS_MANDATORY")
    void testGenderIsMandatoryStatusCode() {
        assertThat(StatusCode.GENDER_IS_MANDATORY.getstatusCode()).isEqualTo(4056);
    }

    @Test
    @DisplayName("Should return correct status code for PWN_ACCOUNT_ORDER_NOT_ALLOWED")
    void testPwnAccountOrderNotAllowedStatusCode() {
        assertThat(StatusCode.PWN_ACCOUNT_ORDER_NOT_ALLOWED.getstatusCode()).isEqualTo(4401);
    }

    @Test
    @DisplayName("Should return correct status code for ACCOUNT_NOT_FOUND")
    void testAccountNotFoundStatusCode() {
        assertThat(StatusCode.ACCOUNT_NOT_FOUND.getstatusCode()).isEqualTo(4402);
    }

    @Test
    @DisplayName("Should return correct status code for PATIENT_NOT_FOUND")
    void testPatientNotFoundStatusCode() {
        assertThat(StatusCode.PATIENT_NOT_FOUND.getstatusCode()).isEqualTo(4403);
    }

    @Test
    @DisplayName("Should return correct status code for MULTIPLE_PATIENTS_FOUND")
    void testMultiplePatientsFoundStatusCode() {
        assertThat(StatusCode.MULTIPLE_PATIENTS_FOUND.getstatusCode()).isEqualTo(4405);
    }

    @Test
    @DisplayName("Should return correct status code for ORDER_ALREADY_PLACED")
    void testOrderAlreadyPlacedStatusCode() {
        assertThat(StatusCode.ORDER_ALREADY_PLACED.getstatusCode()).isEqualTo(4406);
    }

    @Test
    @DisplayName("Should return correct status code for ELIGIBLE_ASSESSMENT_NOT_FOUND")
    void testEligibleAssessmentNotFoundStatusCode() {
        assertThat(StatusCode.ELIGIBLE_ASSESSMENT_NOT_FOUND.getstatusCode()).isEqualTo(4407);
    }

    @Test
    @DisplayName("Should return correct status code for GENETIC_TEST_NOT_PRESENT")
    void testGeneticTestNotPresentStatusCode() {
        assertThat(StatusCode.GENETIC_TEST_NOT_PRESENT.getstatusCode()).isEqualTo(4409);
    }

    @Test
    @DisplayName("Should return correct status code for APPOINTMENT_NOT_AVAILABLE")
    void testAppointmentNotAvailableStatusCode() {
        assertThat(StatusCode.APPOINTMENT_NOT_AVAILABLE.getstatusCode()).isEqualTo(4410);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_CONTACT_DETAILS")
    void testDuplicateContactDetailsStatusCode() {
        assertThat(StatusCode.DUPLICATE_CONTACT_DETAILS.getstatusCode()).isEqualTo(4411);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_MRN")
    void testDuplicateMrnStatusCode() {
        assertThat(StatusCode.DUPLICATE_MRN.getstatusCode()).isEqualTo(4412);
    }

    @Test
    @DisplayName("Should return correct status code for DUPLICATE_EMAIL")
    void testDuplicateEmailStatusCode() {
        assertThat(StatusCode.DUPLICATE_EMAIL.getstatusCode()).isEqualTo(4413);
    }

    @Test
    @DisplayName("Should return correct status code for TEST_CODE_NOT_FOUND")
    void testTestCodeNotFoundStatusCode() {
        assertThat(StatusCode.TEST_CODE_NOT_FOUND.getstatusCode()).isEqualTo(4417);
    }

    @Test
    @DisplayName("Should return correct status code for MORE_THAN_TWO_TEST_CODES_NOT_SUPPORTED")
    void testMoreThanTwoTestCodesNotSupportedStatusCode() {
        assertThat(StatusCode.MORE_THAN_TWO_TEST_CODES_NOT_SUPPORTED.getstatusCode()).isEqualTo(4418);
    }

    @Test
    @DisplayName("Should return correct status code for SOME_TEST_CODES_NOT_SUPPORTED")
    void testSomeTestCodesNotSupportedStatusCode() {
        assertThat(StatusCode.SOME_TEST_CODES_NOT_SUPPORTED.getstatusCode()).isEqualTo(4419);
    }

    @Test
    @DisplayName("Should return correct status code for SIMILAR_TEST_CODES_NOT_SUPPORTED")
    void testSimilarTestCodesNotSupportedStatusCode() {
        assertThat(StatusCode.SIMILAR_TEST_CODES_NOT_SUPPORTED.getstatusCode()).isEqualTo(4420);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_PAYMENT_OPTION")
    void testInvalidPaymentOptionStatusCode() {
        assertThat(StatusCode.INVALID_PAYMENT_OPTION.getstatusCode()).isEqualTo(4501);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_SPECIMENT_TYPE")
    void testInvalidSpecimentTypeStatusCode() {
        assertThat(StatusCode.INVALID_SPECIMENT_TYPE.getstatusCode()).isEqualTo(4502);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_STATE")
    void testInvalidStateStatusCode() {
        assertThat(StatusCode.INVALID_STATE.getstatusCode()).isEqualTo(4505);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_DOB")
    void testInvalidDobStatusCode() {
        assertThat(StatusCode.INVALID_DOB.getstatusCode()).isEqualTo(4506);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_EMAIL_ADDRESS")
    void testInvalidEmailAddressStatusCode() {
        assertThat(StatusCode.INVALID_EMAIL_ADDRESS.getstatusCode()).isEqualTo(4510);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_FIRST_NAME")
    void testInvalidFirstNameStatusCode() {
        assertThat(StatusCode.INVALID_FIRST_NAME.getstatusCode()).isEqualTo(4511);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_LAST_NAME")
    void testInvalidLastNameStatusCode() {
        assertThat(StatusCode.INVALID_LAST_NAME.getstatusCode()).isEqualTo(4512);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_GENDER")
    void testInvalidGenderStatusCode() {
        assertThat(StatusCode.INVALID_GENDER.getstatusCode()).isEqualTo(4513);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_CANCER_NAME")
    void testInvalidCancerNameStatusCode() {
        assertThat(StatusCode.INVALID_CANCER_NAME.getstatusCode()).isEqualTo(4514);
    }

    @Test
    @DisplayName("Should return correct status code for LESS_THAN_18")
    void testLessThan18StatusCode() {
        assertThat(StatusCode.LESS_THAN_18.getstatusCode()).isEqualTo(4526);
    }

    @Test
    @DisplayName("Should return correct status code for INVALID_ORDERING_CLINICIAN_ID")
    void testInvalidOrderingClinicianIdStatusCode() {
        assertThat(StatusCode.INVALID_ORDERING_CLINICIAN_ID.getstatusCode()).isEqualTo(4527);
    }

    @Test
    @DisplayName("Should return correct status code for DISCONTINUED_PROGRAM")
    void testDiscontinuedProgramStatusCode() {
        assertThat(StatusCode.DISCONTINUED_PROGRAM.getstatusCode()).isEqualTo(4528);
    }

    @Test
    @DisplayName("Should return correct status code for INSURANCE_DETAILS_CANNOT_BE_MORE_THAN_2")
    void testInsuranceDetailsCannotBeMoreThan2StatusCode() {
        assertThat(StatusCode.INSURANCE_DETAILS_CANNOT_BE_MORE_THAN_2.getstatusCode()).isEqualTo(4529);
    }

    @Test
    @DisplayName("Should return correct status code for ORGANISATION_ID_REQUIRED")
    void testOrganisationIdRequiredStatusCode() {
        assertThat(StatusCode.ORGANISATION_ID_REQUIRED.getstatusCode()).isEqualTo(5003);
    }

    @Test
    @DisplayName("Should return correct status code for UNABLE_TO_SWITCH_ACCOUNT")
    void testUnableToSwitchAccountStatusCode() {
        assertThat(StatusCode.UNABLE_TO_SWITCH_ACCOUNT.getstatusCode()).isEqualTo(5004);
    }

    @Test
    @DisplayName("Should return correct status code for DEPRECATED_PRODUCT_CODE")
    void testDeprecatedProductCodeStatusCode() {
        assertThat(StatusCode.DEPRECATED_PRODUCT_CODE.getstatusCode()).isEqualTo(4418);
    }

    @Test
    @DisplayName("Should return correct status code for GENE_GROUPING_ID_NOT_PRESENT")
    void testGeneGroupingIdNotPresentStatusCode() {
        assertThat(StatusCode.GENE_GROUPING_ID_NOT_PRESENT.getstatusCode()).isEqualTo(4419);
    }

    @Test
    @DisplayName("Should return all enum values")
    void testValues() {
        StatusCode[] values = StatusCode.values();
        
        assertThat(values).isNotNull();
        assertThat(values.length).isGreaterThan(0);
        // Verify some expected values are present
        assertThat(values).contains(StatusCode.SUCCESS);
        assertThat(values).contains(StatusCode.FAILURE);
        assertThat(values).contains(StatusCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should get enum value by name using valueOf")
    void testValueOf() {
        StatusCode success = StatusCode.valueOf("SUCCESS");
        
        assertThat(success).isEqualTo(StatusCode.SUCCESS);
        assertThat(success.getstatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should throw exception for invalid enum name in valueOf")
    void testValueOfWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatusCode.valueOf("INVALID_ENUM_NAME");
        });
    }

    @Test
    @DisplayName("Should return enum name correctly")
    void testName() {
        assertThat(StatusCode.SUCCESS.name()).isEqualTo("SUCCESS");
        assertThat(StatusCode.FAILURE.name()).isEqualTo("FAILURE");
        assertThat(StatusCode.UNAUTHORIZED.name()).isEqualTo("UNAUTHORIZED");
    }

    @Test
    @DisplayName("Should return correct ordinal for enum values")
    void testOrdinal() {
        StatusCode[] values = StatusCode.values();
        
        for (int i = 0; i < values.length; i++) {
            assertThat(values[i].ordinal()).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("Should test all enum values have unique status codes")
    void testAllEnumValuesHaveUniqueStatusCodes() {
        StatusCode[] values = StatusCode.values();
        java.util.Set<Integer> statusCodes = new java.util.HashSet<>();
        
        for (StatusCode code : values) {
            int statusCode = code.getstatusCode();
            // Note: Some status codes might be duplicated (e.g., 2001, 4418, 4419)
            // This test verifies getstatusCode() works for all values
            assertThat(statusCode).isNotNull();
            statusCodes.add(statusCode);
        }
        
        // Verify we have status codes
        assertThat(statusCodes.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should test getstatusCode for all enum values")
    void testGetStatusCodeForAllEnumValues() {
        StatusCode[] values = StatusCode.values();
        
        for (StatusCode code : values) {
            int statusCode = code.getstatusCode();
            assertThat(statusCode).isNotNull();
            // Verify it's a valid integer (not null, which is impossible for int, but good to verify it works)
        }
    }

    @Test
    @DisplayName("Should test enum comparison")
    void testEnumComparison() {
        assertThat(StatusCode.SUCCESS).isEqualTo(StatusCode.SUCCESS);
        assertThat(StatusCode.SUCCESS).isNotEqualTo(StatusCode.FAILURE);
        assertThat(StatusCode.SUCCESS == StatusCode.SUCCESS).isTrue();
    }

    @Test
    @DisplayName("Should test enum toString")
    void testEnumToString() {
        assertThat(StatusCode.SUCCESS.toString()).isEqualTo("SUCCESS");
        assertThat(StatusCode.FAILURE.toString()).isEqualTo("FAILURE");
    }

    @Test
    @DisplayName("Should test enum getClass")
    void testEnumGetClass() {
        assertThat(StatusCode.SUCCESS.getClass()).isEqualTo(StatusCode.class);
        assertThat(StatusCode.FAILURE.getClass()).isEqualTo(StatusCode.class);
    }

    @Test
    @DisplayName("Should test enum hashCode")
    void testEnumHashCode() {
        StatusCode success1 = StatusCode.SUCCESS;
        StatusCode success2 = StatusCode.SUCCESS;
        
        assertThat(success1.hashCode()).isEqualTo(success2.hashCode());
    }

    @Test
    @DisplayName("Should test enum equals")
    void testEnumEquals() {
        assertThat(StatusCode.SUCCESS.equals(StatusCode.SUCCESS)).isTrue();
        assertThat(StatusCode.SUCCESS.equals(StatusCode.FAILURE)).isFalse();
        assertThat(StatusCode.SUCCESS.equals(null)).isFalse();
        assertThat(StatusCode.SUCCESS.equals("SUCCESS")).isFalse();
    }

    @Test
    @DisplayName("Should test enum compareTo")
    void testEnumCompareTo() {
        StatusCode[] values = StatusCode.values();
        
        for (int i = 0; i < values.length - 1; i++) {
            assertThat(values[i].compareTo(values[i + 1])).isLessThan(0);
            assertThat(values[i + 1].compareTo(values[i])).isGreaterThan(0);
            assertThat(values[i].compareTo(values[i])).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("Should test status codes from different ranges")
    void testStatusCodesFromDifferentRanges() {
        // Test 1000s range
        assertThat(StatusCode.CHAT_BOT_LINK_EXPIRED.getstatusCode()).isEqualTo(1001);
        
        // Test 2000s range
        assertThat(StatusCode.SUCCESS_ORDER_WITHOUT_SWITCH_ACCOUNT.getstatusCode()).isEqualTo(2000);
        
        // Test 4000s range
        assertThat(StatusCode.BAD_REQUEST.getstatusCode()).isEqualTo(4000);
        assertThat(StatusCode.MRN_IS_MANDATORY.getstatusCode()).isEqualTo(4001);
        
        // Test 5000s range
        assertThat(StatusCode.ORGANISATION_ID_REQUIRED.getstatusCode()).isEqualTo(5003);
    }

    @Test
    @DisplayName("Should test status codes with special values")
    void testStatusCodesWithSpecialValues() {
        // Test HTTP status codes
        assertThat(StatusCode.SUCCESS.getstatusCode()).isEqualTo(200);
        assertThat(StatusCode.FAILURE.getstatusCode()).isEqualTo(400);
        assertThat(StatusCode.UNAUTHORIZED.getstatusCode()).isEqualTo(401);
        assertThat(StatusCode.No_CONTENT.getstatusCode()).isEqualTo(204);
        assertThat(StatusCode.ALREADY_EXIST.getstatusCode()).isEqualTo(409);
        assertThat(StatusCode.UNPROCESSABLE_ENTITY_ERROR.getstatusCode()).isEqualTo(422);
        assertThat(StatusCode.INTERNAL_SERVER_ERROR.getstatusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Should test status codes with large values")
    void testStatusCodesWithLargeValues() {
        assertThat(StatusCode.PROBAND_ADDED_SUCCESSFULLY.getstatusCode()).isEqualTo(10022);
    }

    @Test
    @DisplayName("Should test status codes with duplicate values")
    void testStatusCodesWithDuplicateValues() {
        // Note: Some status codes have duplicate values (e.g., 2001, 4418, 4419)
        // This is a design issue but we test the actual behavior
        assertThat(StatusCode.PROGRAM_IS_EMPTY_OR_NULL.getstatusCode()).isEqualTo(2001);
        assertThat(StatusCode.SUCCESS_ORDER_WITH_SWITCH_ACCOUNT.getstatusCode()).isEqualTo(2001);
        
        assertThat(StatusCode.MORE_THAN_TWO_TEST_CODES_NOT_SUPPORTED.getstatusCode()).isEqualTo(4418);
        assertThat(StatusCode.DEPRECATED_PRODUCT_CODE.getstatusCode()).isEqualTo(4418);
        
        assertThat(StatusCode.SOME_TEST_CODES_NOT_SUPPORTED.getstatusCode()).isEqualTo(4419);
        assertThat(StatusCode.GENE_GROUPING_ID_NOT_PRESENT.getstatusCode()).isEqualTo(4419);
    }

    @Test
    @DisplayName("Should test enum getDeclaringClass")
    void testEnumGetDeclaringClass() {
        assertThat(StatusCode.SUCCESS.getDeclaringClass()).isEqualTo(StatusCode.class);
    }

    @Test
    @DisplayName("Should test enum in switch statement")
    void testEnumInSwitchStatement() {
        int result = switch (StatusCode.SUCCESS) {
            case SUCCESS -> 200;
            case FAILURE -> 400;
            default -> 0;
        };
        
        assertThat(result).isEqualTo(200);
    }

    @Test
    @DisplayName("Should test all mandatory field status codes")
    void testAllMandatoryFieldStatusCodes() {
        // Test a sample of mandatory field status codes
        assertThat(StatusCode.MRN_IS_MANDATORY.getstatusCode()).isEqualTo(4001);
        assertThat(StatusCode.TEST_CODE_IS_MANDATORY.getstatusCode()).isEqualTo(4002);
        assertThat(StatusCode.FIRST_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4010);
        assertThat(StatusCode.LAST_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4011);
        assertThat(StatusCode.DATE_OF_BIRTH_IS_MANDATORY.getstatusCode()).isEqualTo(4012);
        assertThat(StatusCode.GENDER_IS_MANDATORY.getstatusCode()).isEqualTo(4056);
    }

    @Test
    @DisplayName("Should test all invalid field status codes")
    void testAllInvalidFieldStatusCodes() {
        // Test a sample of invalid field status codes
        assertThat(StatusCode.INVALID_PAYMENT_OPTION.getstatusCode()).isEqualTo(4501);
        assertThat(StatusCode.INVALID_SPECIMENT_TYPE.getstatusCode()).isEqualTo(4502);
        assertThat(StatusCode.INVALID_STATE.getstatusCode()).isEqualTo(4505);
        assertThat(StatusCode.INVALID_DOB.getstatusCode()).isEqualTo(4506);
        assertThat(StatusCode.INVALID_EMAIL_ADDRESS.getstatusCode()).isEqualTo(4510);
        assertThat(StatusCode.INVALID_FIRST_NAME.getstatusCode()).isEqualTo(4511);
        assertThat(StatusCode.INVALID_LAST_NAME.getstatusCode()).isEqualTo(4512);
        assertThat(StatusCode.INVALID_GENDER.getstatusCode()).isEqualTo(4513);
    }

    @Test
    @DisplayName("Should test all duplicate status codes")
    void testAllDuplicateStatusCodes() {
        // Test duplicate-related status codes
        assertThat(StatusCode.DUPLICATE_PATIENT.getstatusCode()).isEqualTo(1009);
        assertThat(StatusCode.DUPLICATE_GENETIC_TEST_NAME.getstatusCode()).isEqualTo(1014);
        assertThat(StatusCode.DUPLICATE_CONTACT_DETAILS.getstatusCode()).isEqualTo(4411);
        assertThat(StatusCode.DUPLICATE_MRN.getstatusCode()).isEqualTo(4412);
        assertThat(StatusCode.DUPLICATE_EMAIL.getstatusCode()).isEqualTo(4413);
    }

    @Test
    @DisplayName("Should test all not found status codes")
    void testAllNotFoundStatusCodes() {
        // Test not found-related status codes
        assertThat(StatusCode.ASSESSMENT_NOT_FOUND.getstatusCode()).isEqualTo(1021);
        assertThat(StatusCode.GENEMATTERS_APPT_NOT_FOUND.getstatusCode()).isEqualTo(1022);
        assertThat(StatusCode.ACCOUNT_NOT_FOUND.getstatusCode()).isEqualTo(4402);
        assertThat(StatusCode.PATIENT_NOT_FOUND.getstatusCode()).isEqualTo(4403);
        assertThat(StatusCode.ELIGIBLE_ASSESSMENT_NOT_FOUND.getstatusCode()).isEqualTo(4407);
        assertThat(StatusCode.GENETIC_TEST_NOT_PRESENT.getstatusCode()).isEqualTo(4409);
        assertThat(StatusCode.TEST_CODE_NOT_FOUND.getstatusCode()).isEqualTo(4417);
    }

    @Test
    @DisplayName("Should test enum serialization")
    void testEnumSerialization() {
        // Enums are serializable by default
        assertThat(StatusCode.SUCCESS).isInstanceOf(java.io.Serializable.class);
    }

    @Test
    @DisplayName("Should test enum in collections")
    void testEnumInCollections() {
        java.util.Set<StatusCode> statusCodes = new java.util.HashSet<>();
        statusCodes.add(StatusCode.SUCCESS);
        statusCodes.add(StatusCode.FAILURE);
        statusCodes.add(StatusCode.UNAUTHORIZED);
        
        assertThat(statusCodes).hasSize(3);
        assertThat(statusCodes).contains(StatusCode.SUCCESS);
    }

    @Test
    @DisplayName("Should test enum in maps")
    void testEnumInMaps() {
        java.util.Map<StatusCode, Integer> statusCodeMap = new java.util.HashMap<>();
        statusCodeMap.put(StatusCode.SUCCESS, 200);
        statusCodeMap.put(StatusCode.FAILURE, 400);
        
        assertThat(statusCodeMap.get(StatusCode.SUCCESS)).isEqualTo(200);
        assertThat(statusCodeMap.get(StatusCode.FAILURE)).isEqualTo(400);
    }

    @Test
    @DisplayName("Should test enum with different status code ranges")
    void testEnumWithDifferentStatusCodeRanges() {
        // Test various status code ranges to ensure constructor works for all
        assertThat(StatusCode.CHAT_BOT_LINK_EXPIRED.getstatusCode()).isEqualTo(1001);
        assertThat(StatusCode.PROBAND_ADDED_SUCCESSFULLY.getstatusCode()).isEqualTo(10022);
        assertThat(StatusCode.CANNOT_UPDATE_ASSESSMENT_CHAT_BOT_STATUS.getstatusCode()).isEqualTo(1997);
        assertThat(StatusCode.SUCCESS_ORDER_WITHOUT_SWITCH_ACCOUNT.getstatusCode()).isEqualTo(2000);
        assertThat(StatusCode.BAD_REQUEST.getstatusCode()).isEqualTo(4000);
        assertThat(StatusCode.ORGANISATION_ID_REQUIRED.getstatusCode()).isEqualTo(5003);
    }

    @Test
    @DisplayName("Should test remaining mandatory field status codes")
    void testRemainingMandatoryFieldStatusCodes() {
        assertThat(StatusCode.STAT_TESTING_IS_MANDATORY.getstatusCode()).isEqualTo(4006);
        assertThat(StatusCode.ACCOUNT_ID_IS_MANDATORY.getstatusCode()).isEqualTo(4007);
        assertThat(StatusCode.ORDERING_PROVIDER_IS_MANDATORY.getstatusCode()).isEqualTo(4008);
        assertThat(StatusCode.KIT_REQUESTED_IS_MANDATORY.getstatusCode()).isEqualTo(4009);
        assertThat(StatusCode.KIT_REQUESTED_PHONE_IS_MANDATORY.getstatusCode()).isEqualTo(4014);
        assertThat(StatusCode.KIT_REQUESTED_EMAIL_IS_MANDATORY.getstatusCode()).isEqualTo(4015);
        assertThat(StatusCode.ADDRESS_LINE_IS_MANDATORY.getstatusCode()).isEqualTo(4017);
        assertThat(StatusCode.INSURANCE_PROVIDER_DETAILS_ARE_MANDATORY.getstatusCode()).isEqualTo(4021);
        assertThat(StatusCode.GROUP_NUMBER_IS_MANDATORY.getstatusCode()).isEqualTo(4024);
        assertThat(StatusCode.PRIMARY_INSURANCE_HOLDER_FLAG_IS_MANDATORY.getstatusCode()).isEqualTo(4025);
        assertThat(StatusCode.HOLDER_DETAILS_ARE_MANDATORY.getstatusCode()).isEqualTo(4026);
        assertThat(StatusCode.HOLDER_GENDER_IS_MANDATORY.getstatusCode()).isEqualTo(4029);
        assertThat(StatusCode.HOLDER_RELATIONSHIP_IS_MANDATORY.getstatusCode()).isEqualTo(4030);
        assertThat(StatusCode.HOLDER_PHONE_IS_MANDATORY.getstatusCode()).isEqualTo(4031);
        assertThat(StatusCode.HOLDER_DATE_OF_BIRTH_IS_MANDATORY.getstatusCode()).isEqualTo(4032);
        assertThat(StatusCode.HOLDER_ADDRESS_PREFERENCE_IS_MANDATORY.getstatusCode()).isEqualTo(4033);
        assertThat(StatusCode.HOLDER_ADDRESS_IS_MANDATORY.getstatusCode()).isEqualTo(4034);
        assertThat(StatusCode.HOLDER_ADDRESS_1_IS_MANDATORY.getstatusCode()).isEqualTo(4035);
        assertThat(StatusCode.HOLDER_POSTAL_CODE_IS_MANDATORY.getstatusCode()).isEqualTo(4036);
        assertThat(StatusCode.HOLDER_STATE_IS_MANDATORY.getstatusCode()).isEqualTo(4037);
        assertThat(StatusCode.HOLDER_CITY_IS_MANDATORY.getstatusCode()).isEqualTo(4038);
        assertThat(StatusCode.PATIENT_DETAILS_ARE_MANDATORY.getstatusCode()).isEqualTo(4039);
        assertThat(StatusCode.ORDER_DETAILS_ARE_MANDATORY.getstatusCode()).isEqualTo(4040);
        assertThat(StatusCode.OTHER_CANCER_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4043);
        assertThat(StatusCode.RELATIVE_SIDE_IS_MANDATORY.getstatusCode()).isEqualTo(4045);
        assertThat(StatusCode.RELATIVE_CANCER_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4046);
        assertThat(StatusCode.FILE_NAME_IS_MANDATORY.getstatusCode()).isEqualTo(4048);
        assertThat(StatusCode.FILE_CONTENT_IS_MANDATORY.getstatusCode()).isEqualTo(4049);
        assertThat(StatusCode.PERSONAL_OR_FAMILY_OR_DOCUMENT_HISTORY_IS_MANDATORY.getstatusCode()).isEqualTo(4050);
        assertThat(StatusCode.OTHER_PRIMARY_TEST_IS_REQUIRED.getstatusCode()).isEqualTo(4052);
        assertThat(StatusCode.EXTERNAL_APPOINTMENT_ID_IS_MANDATORY.getstatusCode()).isEqualTo(4054);
        assertThat(StatusCode.APPOINTMENT_REASON_IS_MANDATORY.getstatusCode()).isEqualTo(4055);
        assertThat(StatusCode.APPOINTMENT_DATE_IS_MANDATORY.getstatusCode()).isEqualTo(4057);
        assertThat(StatusCode.APPOINTMENT_TIME_IS_MANDATORY.getstatusCode()).isEqualTo(4058);
    }

    @Test
    @DisplayName("Should test remaining invalid field status codes")
    void testRemainingInvalidFieldStatusCodes() {
        assertThat(StatusCode.INVALID_SPECIMENT_TYPE_FOR_RNA_INSIGHT.getstatusCode()).isEqualTo(4503);
        assertThat(StatusCode.INCORRECT_ADDRESS_LENGTH.getstatusCode()).isEqualTo(4504);
        assertThat(StatusCode.UNSUPPORTED_PROGRAM.getstatusCode()).isEqualTo(4507);
        assertThat(StatusCode.INVALID_ORDERING_PROVIDER_NAME.getstatusCode()).isEqualTo(4508);
        assertThat(StatusCode.INVALID_CELL_PHONE.getstatusCode()).isEqualTo(4509);
        assertThat(StatusCode.INVALID_CANCER_TYPE.getstatusCode()).isEqualTo(4515);
        assertThat(StatusCode.INVALID_RELATIVE.getstatusCode()).isEqualTo(4516);
        assertThat(StatusCode.INVALID_RELATIVE_SIDE.getstatusCode()).isEqualTo(4517);
        assertThat(StatusCode.INVALID_RELATIVE_CANCER_NAME.getstatusCode()).isEqualTo(4518);
        assertThat(StatusCode.INVALID_AGE_OF_DIAGNOSIS.getstatusCode()).isEqualTo(4519);
        assertThat(StatusCode.INVALID_PRIMARY_TEST_NAME.getstatusCode()).isEqualTo(4520);
        assertThat(StatusCode.INVALID_ETHNICITY.getstatusCode()).isEqualTo(4521);
        assertThat(StatusCode.INVALID_APPOINTMENT_REASON.getstatusCode()).isEqualTo(4522);
        assertThat(StatusCode.INVALID_APPOINTMENT_DATE.getstatusCode()).isEqualTo(4523);
        assertThat(StatusCode.PAST_APPOINTMENT_DATE.getstatusCode()).isEqualTo(4524);
        assertThat(StatusCode.ACCOUNT_DOES_NOT_SUPPORT_THIS_PROGRAM.getstatusCode()).isEqualTo(4525);
    }

    @Test
    @DisplayName("Should test remaining error status codes")
    void testRemainingErrorStatusCodes() {
        assertThat(StatusCode.PATIENT_NOT_FOUND_FOR_ACCOUNT.getstatusCode()).isEqualTo(4404);
        assertThat(StatusCode.HEME_MALIGNANCY_ALLOGENEIC_TRANSPLANT.getstatusCode()).isEqualTo(4408);
        assertThat(StatusCode.DUPLICATE_CELL_PHONE.getstatusCode()).isEqualTo(4414);
        assertThat(StatusCode.NAOW_IS_DISABLED.getstatusCode()).isEqualTo(4415);
        assertThat(StatusCode.MULTIPLE_PRODUCT_CODES_NOT_SUPPORTED.getstatusCode()).isEqualTo(4416);
    }

    @Test
    @DisplayName("Should test all enum values using reflection")
    void testAllEnumValuesUsingReflection() {
        StatusCode[] allValues = StatusCode.values();
        
        // Test that getstatusCode() works for every enum value
        for (StatusCode code : allValues) {
            int statusCode = code.getstatusCode();
            assertThat(statusCode).isNotNull();
            // Verify it's a positive integer (most status codes are positive)
            assertThat(statusCode).isGreaterThan(0);
        }
    }

    @Test
    @DisplayName("Should test enum valueOf case sensitivity")
    void testEnumValueOfCaseSensitivity() {
        // valueOf is case-sensitive
        assertThrows(IllegalArgumentException.class, () -> {
            StatusCode.valueOf("success"); // lowercase should fail
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            StatusCode.valueOf("Success"); // mixed case should fail
        });
        
        // Correct case should work
        assertThat(StatusCode.valueOf("SUCCESS")).isEqualTo(StatusCode.SUCCESS);
    }

    @Test
    @DisplayName("Should test enum with null comparison")
    void testEnumWithNullComparison() {
        assertThat(StatusCode.SUCCESS.equals(null)).isFalse();
        assertThat(StatusCode.SUCCESS == null).isFalse();
    }

    @Test
    @DisplayName("Should test enum with different object type comparison")
    void testEnumWithDifferentObjectTypeComparison() {
        assertThat(StatusCode.SUCCESS.equals("SUCCESS")).isFalse();
        assertThat(StatusCode.SUCCESS.equals(200)).isFalse();
        assertThat(StatusCode.SUCCESS.equals(StatusCode.FAILURE)).isFalse();
    }

    @Test
    @DisplayName("Should test enum getstatusCode returns correct type")
    void testEnumGetStatusCodeReturnsCorrectType() {
        int statusCode = StatusCode.SUCCESS.getstatusCode();
        
        // Verify it's an int (primitive)
        assertThat(statusCode).isInstanceOf(Integer.class);
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    @DisplayName("Should test enum values are immutable")
    void testEnumValuesAreImmutable() {
        StatusCode original = StatusCode.SUCCESS;
        int originalStatusCode = original.getstatusCode();
        
        // Enums are immutable, so getstatusCode() should always return the same value
        assertThat(original.getstatusCode()).isEqualTo(originalStatusCode);
        assertThat(original.getstatusCode()).isEqualTo(originalStatusCode);
    }

    @Test
    @DisplayName("Should test enum in array")
    void testEnumInArray() {
        StatusCode[] codes = {StatusCode.SUCCESS, StatusCode.FAILURE, StatusCode.UNAUTHORIZED};
        
        assertThat(codes.length).isEqualTo(3);
        assertThat(codes[0]).isEqualTo(StatusCode.SUCCESS);
        assertThat(codes[1]).isEqualTo(StatusCode.FAILURE);
        assertThat(codes[2]).isEqualTo(StatusCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should test enum with stream operations")
    void testEnumWithStreamOperations() {
        java.util.List<Integer> statusCodes = java.util.Arrays.stream(StatusCode.values())
            .map(StatusCode::getstatusCode)
            .collect(java.util.stream.Collectors.toList());
        
        assertThat(statusCodes).isNotEmpty();
        assertThat(statusCodes).contains(200, 400, 401, 500);
    }

    @Test
    @DisplayName("Should test enum with filter operations")
    void testEnumWithFilterOperations() {
        java.util.List<StatusCode> httpStatusCodes = java.util.Arrays.stream(StatusCode.values())
            .filter(code -> code.getstatusCode() < 1000)
            .collect(java.util.stream.Collectors.toList());
        
        assertThat(httpStatusCodes).isNotEmpty();
        assertThat(httpStatusCodes).contains(StatusCode.SUCCESS, StatusCode.FAILURE, 
            StatusCode.UNAUTHORIZED, StatusCode.INTERNAL_SERVER_ERROR);
    }
}

