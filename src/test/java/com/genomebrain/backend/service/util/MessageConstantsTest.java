package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MessageConstants Unit Tests")
class MessageConstantsTest {

    @Test
    @DisplayName("Should have non-null basic status constants")
    void testBasicStatusConstants() {
        assertThat(MessageConstants.SUCCESS).isNotNull();
        assertThat(MessageConstants.FAILURE).isNotNull();
        assertThat(MessageConstants.FORBIDDEN).isNotNull();
        assertThat(MessageConstants.NOT_FOUND).isNotNull();
        assertThat(MessageConstants.UNAUTHORIZED).isNotNull();
        
        assertThat(MessageConstants.SUCCESS).isEqualTo("SUCCESS");
        assertThat(MessageConstants.FAILURE).isEqualTo("FAILURE");
        assertThat(MessageConstants.FORBIDDEN).isEqualTo("FORBIDDEN");
        assertThat(MessageConstants.NOT_FOUND).isEqualTo("NOT FOUND");
        assertThat(MessageConstants.UNAUTHORIZED).isEqualTo("UNAUTHORIZED");
    }

    @Test
    @DisplayName("Should have non-null error message constants")
    void testErrorMessageConstants() {
        assertThat(MessageConstants.INTERNAL_SERVER_ERROR_MESSAGE).isNotNull();
        assertThat(MessageConstants.ACCOUNT_NAME_ERROR_MESSAGE).isNotNull();
        assertThat(MessageConstants.ACCOUNT_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.CONFIGURATION_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.ACCOUNT_BLOCKED_ERROR_MESSAGE).isNotNull();
        assertThat(MessageConstants.INCORRECT_CREDENTIALS_ERROR_MESSAGE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null success message constants")
    void testSuccessMessageConstants() {
        assertThat(MessageConstants.ACCOUNT_INFO_SUCCESS).isNotNull();
        assertThat(MessageConstants.USER_REGISTERED_SUCCESS_MESSAGE).isNotNull();
        assertThat(MessageConstants.USER_AUTHENTICATION_SUCCESS_MESSAGE).isNotNull();
        assertThat(MessageConstants.USER_RECORD_FETCHED_SUCCESS_MESSAGE).isNotNull();
        assertThat(MessageConstants.LOGOUT_SUCCESS_MESSAGE).isNotNull();
        assertThat(MessageConstants.RESET_PASSWORD_SUCCESS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null assessment related constants")
    void testAssessmentConstants() {
        assertThat(MessageConstants.RISK_ASSESSMENT_DETAILS_SUCCESS).isNotNull();
        assertThat(MessageConstants.RISK_ASSESSMENT_DETAILS_FAILED).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_LIST_FAILED).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_CREATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_SUCCESSFULLY_DELETED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null data related constants")
    void testDataConstants() {
        assertThat(MessageConstants.NO_DATA_FOUND).isNotNull();
        assertThat(MessageConstants.DATA_FOUND).isNotNull();
        assertThat(MessageConstants.EMPLOYEE_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.TEST_ORDER_LIST_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null validation constants")
    void testValidationConstants() {
        assertThat(MessageConstants.FIRST_NAME_CANT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.LAST_NAME_CANT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.EMAIL_CANT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.ACCOUNT_ID_CANT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.INVALID_PHONE_NUMBER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient related constants")
    void testPatientConstants() {
        assertThat(MessageConstants.PATIENT_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.INVALID_PATIENT_ID).isNotNull();
        assertThat(MessageConstants.INVALID_PATIENT_UUID).isNotNull();
        assertThat(MessageConstants.PATIENT_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PATIENT_LIST_RETRIEVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null order related constants")
    void testOrderConstants() {
        assertThat(MessageConstants.ORDER_CREATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ORDER_CREATION_FAILED).isNotNull();
        assertThat(MessageConstants.INVALID_ORDER_ID).isNotNull();
        assertThat(MessageConstants.ORDER_CANCELLED).isNotNull();
        assertThat(MessageConstants.ORDER_REPORTED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null appointment related constants")
    void testAppointmentConstants() {
        assertThat(MessageConstants.APPOINTMENT_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_APPOINTMENT_FOUND).isNotNull();
        assertThat(MessageConstants.APPOINTMENT_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.SCHEDULED_APPOINTMENT_SUCCESSFULLY).isNotNull();
    }
}

