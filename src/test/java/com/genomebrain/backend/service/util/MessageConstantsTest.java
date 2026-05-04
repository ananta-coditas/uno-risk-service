package com.genomebrain.backend.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MessageConstants Unit Tests")
class MessageConstantsTest {

    @Test
    @DisplayName("Should allow default constructor for coverage of utility holder class")
    void testDefaultConstructorViaReflection() throws Exception {
        Constructor<MessageConstants> ctor = MessageConstants.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        MessageConstants instance = ctor.newInstance();
        assertThat(instance).isNotNull();
    }

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

    @Test
    @DisplayName("Should have non-null proband related constants")
    void testProbandConstants() {
        assertThat(MessageConstants.PROBAND_VERIFIED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PROBAND_VERIFICATION_FAILED).isNotNull();
        assertThat(MessageConstants.PROBAND_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PROBAND_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PROBAND_DELETED).isNotNull();
        assertThat(MessageConstants.PROBAND_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.INVALID_PROBAND_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null test result related constants")
    void testTestResultConstants() {
        assertThat(MessageConstants.TEST_RESULTS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.TEST_RESULT_TO_DISCLOSE_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.DISCLOSED_TEST_RESULT_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.TEST_RESULT_PENDING_WITH_AP_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.RESULTS_ALREADY_RELEASED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null genetic test related constants")
    void testGeneticTestConstants() {
        assertThat(MessageConstants.GENETIC_TEST_LIST_RETRIEVED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.GENETIC_TEST_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.INVALID_GENETIC_TEST_ID).isNotNull();
        assertThat(MessageConstants.GENETIC_TEST_ORDER_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.GENETIC_TEST_ORDER_IS_PLACED).isNotNull();
        assertThat(MessageConstants.GENETIC_RESULT_HISTORY_RETRIEVED).isNotNull();
        assertThat(MessageConstants.NO_RESULT_HISTORY_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null resource related constants")
    void testResourceConstants() {
        assertThat(MessageConstants.HOMEPAGE_RESOURCES_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.HOMEPAGE_RESOURCES_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.ALL_RESOURCE_TOPICS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.RESOURCE_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.RESOURCE_NOT_ACCESSIBLE_TO_PATIENT).isNotNull();
        assertThat(MessageConstants.RESOURCES_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.RESOURCE_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null FAQ related constants")
    void testFAQConstants() {
        assertThat(MessageConstants.POPULAR_FAQS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.FAQS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_FAQS_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null document related constants")
    void testDocumentConstants() {
        assertThat(MessageConstants.DOCUMENT_UPLOAD_IS_IN_PROGRESS).isNotNull();
        assertThat(MessageConstants.DOCUMENT_SIZE_EXCEEDS).isNotNull();
        assertThat(MessageConstants.INVALID_DOCUMENT_FORMAT).isNotNull();
        assertThat(MessageConstants.DOCUMENT_ALREADY_SENT).isNotNull();
        assertThat(MessageConstants.DOCUMENT_SENT_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null partner related constants")
    void testPartnerConstants() {
        assertThat(MessageConstants.PARTNER_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.DUPLICATE_PARTNER_FOUND).isNotNull();
        assertThat(MessageConstants.PARTNER_INVITE_LINK_GENERATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PARTNER_INVITE_LINK_GENERATION_UNSUCCESSFUL).isNotNull();
        assertThat(MessageConstants.PARTNER_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_PARTNER_FOUND_FOR_PATIENT).isNotNull();
        assertThat(MessageConstants.PARTNER_INVITE_LINK_EXPIRED).isNotNull();
        assertThat(MessageConstants.UNLINKED_PARTNER_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null custom testing decision constants")
    void testCustomTestingDecisionConstants() {
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_DELETED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_ALREADY_EXISTS).isNotNull();
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_DOES_NOT_EXIST).isNotNull();
        assertThat(MessageConstants.CANNOT_ADD_MORE_THAN_5_CUSTOM_TESTING_DECISION).isNotNull();
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_IS_IN_USE_AND_CAN_NOT_BE_DELETED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null filter related constants")
    void testFilterConstants() {
        assertThat(MessageConstants.FILTER_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.FILTER_SAVED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.FILTER_DELETED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.FILTER_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.FILTER_LIMIT_EXCEED).isNotNull();
        assertThat(MessageConstants.FILTER_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.FILTER_DOES_NOT_EXIST).isNotNull();
        assertThat(MessageConstants.FILTER_DOES_NOT_EXIST_FOR_PROVIDER).isNotNull();
        assertThat(MessageConstants.PROVIDE_FILTER_ID).isNotNull();
        assertThat(MessageConstants.FILTER_NOT_PRESENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null questionnaire related constants")
    void testQuestionnaireConstants() {
        assertThat(MessageConstants.QUESTIONNAIRE_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CUSTOM_QUESTIONNAIRE_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ERROR_FETCHING_QUESTIONNAIRE).isNotNull();
        assertThat(MessageConstants.ERROR_PARSING_QUESTIONNAIRE).isNotNull();
        assertThat(MessageConstants.VERSION_IS_EMPTY_OR_NULL_IN_DB).isNotNull();
        assertThat(MessageConstants.APPLICATION_CONFIGURATION_IS_EMPTY_OR_NULL_IN_DB).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null program related constants")
    void testProgramConstants() {
        assertThat(MessageConstants.PROGRAM_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PROGRAM_LIST_SHOULD_NOT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.DISCONTINUED_PROGRAM).isNotNull();
        assertThat(MessageConstants.NO_LONGER_EXIST).isNotNull();
        assertThat(MessageConstants.NO_LONGER_EXIST_PROGRAM).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null XIFIN related constants")
    void testXIFINConstants() {
        assertThat(MessageConstants.XIFIN_PAYORS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.XIFIN_PAYORS_FETCH_UNSUCCESSFUL).isNotNull();
        assertThat(MessageConstants.XIFIN_ESTIMATION_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.XIFIN_ESTIMATION_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null EFAX related constants")
    void testEFAXConstants() {
        assertThat(MessageConstants.EFAX_REQUEST_PROCESSED).isNotNull();
        assertThat(MessageConstants.EFAX_REQUEST_FAILED).isNotNull();
        assertThat(MessageConstants.EFAX_MESSAGE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null CSV related constants")
    void testCSVConstants() {
        assertThat(MessageConstants.FOUND_CSV_LINK).isNotNull();
        assertThat(MessageConstants.CSV_LINK_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.YOUR_DATA_WILL_BE_READY_IN_SOME_TIME).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null GC related constants")
    void testGCConstants() {
        assertThat(MessageConstants.GC_STATUS_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.GC_NOTES_UPLOADED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.GC_NOTES_NOT_UPLOADED).isNotNull();
        assertThat(MessageConstants.NO_GC_NOTES_FOUND).isNotNull();
        assertThat(MessageConstants.POST_TEST_GC_DUPLICATE_ENTRY_WITH_APPOINTMENT_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null PIA related constants")
    void testPIAConstants() {
        assertThat(MessageConstants.PIA_LINK_ALREADY_GENERATED).isNotNull();
        assertThat(MessageConstants.CANNOT_GENERATE_PIA_LINK_FOR_RH_ACCOUNT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null pedigree related constants")
    void testPedigreeConstants() {
        assertThat(MessageConstants.PEDIGREE_IMAGE_URL_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null chatbot related constants")
    void testChatbotConstants() {
        assertThat(MessageConstants.CHATBOT_URL_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CHATBOT_URL_GENERATION_FAILED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null PWN related constants")
    void testPWNConstants() {
        assertThat(MessageConstants.PWN_ORDER_NOT_FOUND_IN_CARE).isNotNull();
        assertThat(MessageConstants.PWN_PREFERENCE_UPDATED).isNotNull();
        assertThat(MessageConstants.INVALID_PWN_PREFERENCE).isNotNull();
        assertThat(MessageConstants.RETRIEVE_PWN_PREFERENCE).isNotNull();
        assertThat(MessageConstants.POST_AMENDED_REPORT_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.POST_AMENDED_REPORT_TO_PWN_FAILED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null diagnosis code related constants")
    void testDiagnosisCodeConstants() {
        assertThat(MessageConstants.DIAGNOSIS_CODES_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.DIAGNOSIS_CODES_FETCHING_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null ordering authorization related constants")
    void testOrderingAuthorizationConstants() {
        assertThat(MessageConstants.ORDERING_AUTHORIZATION_FETCH_LIST_SUCCESS).isNotNull();
        assertThat(MessageConstants.ORDERING_AUTHORIZATION_FETCH_LIST_ERROR).isNotNull();
        assertThat(MessageConstants.INVALID_ORDER_AUTHORIZATION_REQUEST_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null previous testing related constants")
    void testPreviousTestingConstants() {
        assertThat(MessageConstants.PREVIOUS_TESTING_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_PREVIOUS_TESTING_HISTORY_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account setup related constants")
    void testAccountSetupConstants() {
        assertThat(MessageConstants.ACCOUNT_SETUP_COMPLETED).isNotNull();
        assertThat(MessageConstants.ACCOUNT_ASSESSMENT_SETUP_NOT_DONE_MISSING_JSON_CONFIG).isNotNull();
        assertThat(MessageConstants.ACCOUNT_ASSESSMENT_SETUP_NOT_DONE_MISSING_JSON).isNotNull();
        assertThat(MessageConstants.MASTER_JSON_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null admin related constants")
    void testAdminConstants() {
        assertThat(MessageConstants.ADMIN_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ADMIN_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ADMIN_DELETED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null provider related constants")
    void testProviderConstants() {
        assertThat(MessageConstants.ORDERING_PROVIDERS_RETRIEVED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.REFERRING_PROVIDER_LIST_RETRIEVED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.INVALID_PROVIDER_ID).isNotNull();
        assertThat(MessageConstants.INVALID_PROVIDER_PREFERENCE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null TC score related constants")
    void testTCScoreConstants() {
        assertThat(MessageConstants.TC_SCORE_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null unscheduled patient related constants")
    void testUnscheduledPatientConstants() {
        assertThat(MessageConstants.UNSCHEDULED_PATIENT_LIST_RETRIEVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null product related constants")
    void testProductConstants() {
        assertThat(MessageConstants.PRODUCT_LIST_RETRIEVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null article related constants")
    void testArticleConstants() {
        assertThat(MessageConstants.ARTICLE_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_ARTICLE_WITH_GIVEN_ID_EXISTS).isNotNull();
        assertThat(MessageConstants.NO_ARTICLE_WITH_SELECTED_FILTER_IS_ACCESSIBLE_TO_USER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null login pin related constants")
    void testLoginPinConstants() {
        assertThat(MessageConstants.LOGIN_PIN_VERIFIED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.INVALID_PIN).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null appointment follow up related constants")
    void testAppointmentFollowUpConstants() {
        assertThat(MessageConstants.APPOINTMENT_FOLLOW_UP_HISTORY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null status update related constants")
    void testStatusUpdateConstants() {
        assertThat(MessageConstants.STATUS_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.FAILED_TO_UPDATE_STATUS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null SFTP rule related constants")
    void testSFTPRuleConstants() {
        assertThat(MessageConstants.REQUEST_SUCCESSFUL_FOR_SET_ACCOUNT_NOTIFICATION_RULE).isNotNull();
        assertThat(MessageConstants.REQUEST_SUCCESSFUL_FOR_DELETE_ACCOUNT_NOTIFICATION_RULE).isNotNull();
        assertThat(MessageConstants.ERROR_WHILE_REMOVING_RULE).isNotNull();
        assertThat(MessageConstants.RULE_DELETED_FOR_ACCOUNT).isNotNull();
        assertThat(MessageConstants.ERROR_WHILE_REMOVING_TARGET).isNotNull();
        assertThat(MessageConstants.NO_SFTP_RULE_PRESENT_FOR_THE_ACCOUNT).isNotNull();
        assertThat(MessageConstants.SFTP_RULE_ENABLED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.SFTP_RULE_DISABLED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null notification rule related constants")
    void testNotificationRuleConstants() {
        assertThat(MessageConstants.NO_NOTIFICATION_RULE_EXISTS_FOR_THE_ACCOUNT).isNotNull();
        assertThat(MessageConstants.ERROR_WHILE_ENABLING_NOTIFICATION_RULE).isNotNull();
        assertThat(MessageConstants.ERROR_WHILE_DISABLING_NOTIFICATION_RULE).isNotNull();
        assertThat(MessageConstants.NOTIFICATION_RULE_ENABLED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NOTIFICATION_RULE_DISABLED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null progress related constants")
    void testProgressConstants() {
        assertThat(MessageConstants.PROGRESS).isNotNull();
        assertThat(MessageConstants.NO_PROGRESS_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null adhoc holder related constants")
    void testAdhocHolderConstants() {
        assertThat(MessageConstants.UNABLE_TO_DELETE_ADHOC_HOLDER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null ambry account related constants")
    void testAmbryAccountConstants() {
        assertThat(MessageConstants.AMBRY_ACCOUNT_ID_CANNOT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.AMBRY_ACCOUNT_IS_NOT_PRESENT).isNotNull();
        assertThat(MessageConstants.INVALID_AMBRY_ACCOUNT_OR_ACCOUNT_CONFIGURATION_NOT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null education workflow related constants")
    void testEducationWorkflowConstants() {
        assertThat(MessageConstants.EDUCATION_WORKFLOW_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null pizza tracker related constants")
    void testPizzaTrackerConstants() {
        assertThat(MessageConstants.WH_PIZZA_TRACKER_INFORMATION_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.HC_PIZZA_TRACKER_INFORMATION_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null draft related constants")
    void testDraftConstants() {
        assertThat(MessageConstants.DRAFT_SAVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null post GC related constants")
    void testPostGCConstants() {
        assertThat(MessageConstants.NOT_ELIGIBLE_TO_SCHEDULE_POST_GC).isNotNull();
        assertThat(MessageConstants.INITIATED_POST_GC_NOTIFICATION_SENT_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null preference related constants")
    void testPreferenceConstants() {
        assertThat(MessageConstants.PREFERENCE_UPDATED).isNotNull();
        assertThat(MessageConstants.LANG_KEY_UPDATED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null record related constants")
    void testRecordConstants() {
        assertThat(MessageConstants.RECORD_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null email related constants")
    void testEmailConstants() {
        assertThat(MessageConstants.EMAIL_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.EMAIL_IS_ALREADY_IN_USE).isNotNull();
        assertThat(MessageConstants.EMAIL_ALREADY_EXIST).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null cellphone related constants")
    void testCellphoneConstants() {
        assertThat(MessageConstants.CELLPHONE_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.DUPLICATE_PHONE_NUMBER).isNotNull();
        assertThat(MessageConstants.PHONE_NUMBER_SAVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null password related constants")
    void testPasswordConstants() {
        assertThat(MessageConstants.CANNOT_ENTER_SAME_PASSWORD).isNotNull();
        assertThat(MessageConstants.INCORRECT_OLD_PASSWORD).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null pregnancy related constants")
    void testPregnancyConstants() {
        assertThat(MessageConstants.PREGNANCY_STATUS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.PREGNANCY_STATUS_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null personal information related constants")
    void testPersonalInformationConstants() {
        assertThat(MessageConstants.PERSONAL_INFORMATION_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null result status related constants")
    void testResultStatusConstants() {
        assertThat(MessageConstants.YOUR_RESULT_STATUS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null appointment history related constants")
    void testAppointmentHistoryConstants() {
        assertThat(MessageConstants.APPOINTMENTS_HISTORY_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null upcoming appointments related constants")
    void testUpcomingAppointmentsConstants() {
        assertThat(MessageConstants.UPCOMING_APPOINTMENTS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null risk assessment status related constants")
    void testRiskAssessmentStatusConstants() {
        assertThat(MessageConstants.RISK_ASSESSMENT_STATUS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null signup related constants")
    void testSignupConstants() {
        assertThat(MessageConstants.SIGNUP_REQUEST_PROCESSED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.SIGNUP_NOT_COMPLETED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null awaiting results related constants")
    void testAwaitingResultsConstants() {
        assertThat(MessageConstants.AWAITING_RESULTS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null testing eligibility related constants")
    void testTestingEligibilityConstants() {
        assertThat(MessageConstants.NOT_ELIGIBLE_FOR_TESTING).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null order placement related constants")
    void testOrderPlacementConstants() {
        assertThat(MessageConstants.ORDER_IS_ALREADY_PLACED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient journey related constants")
    void testPatientJourneyConstants() {
        assertThat(MessageConstants.PATIENT_JOURNEY_FETCH_SUCCESS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null kit related constants")
    void testKitConstants() {
        assertThat(MessageConstants.KIT_BARCODE_AND_SAMPLE_COLLECTION_DATE_IS_REQUIRED).isNotNull();
        assertThat(MessageConstants.KIT_ACTIVATION_SUCCESSFUL).isNotNull();
        assertThat(MessageConstants.DUPLICATE_KIT_BARCODE).isNotNull();
        assertThat(MessageConstants.KIT_NOT_ACTIVATED).isNotNull();
        assertThat(MessageConstants.KIT_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.KIT_TRACKING_INFO_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null track kit related constants")
    void testTrackKitConstants() {
        assertThat(MessageConstants.TRACK_KIT_PROGRESS_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.TRACK_KIT_DETAILS_NOT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient config related constants")
    void testPatientConfigConstants() {
        assertThat(MessageConstants.PATIENT_CONFIG_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null assessment details related constants")
    void testAssessmentDetailsConstants() {
        assertThat(MessageConstants.ASSESSMENT_DETAILS_RETRIEVED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_DETAILS_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient account related constants")
    void testPatientAccountConstants() {
        assertThat(MessageConstants.PATIENT_ACCOUNT_NUMBER_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.PATIENT_ACCOUNT_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.SECONDARY_PATIENT_ACCOUNT_NUMBER_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.NO_PATIENT_ACCOUNT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient duplicate related constants")
    void testPatientDuplicateConstants() {
        assertThat(MessageConstants.PATIENT_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.DUPLICATE_PATIENT).isNotNull();
        assertThat(MessageConstants.DUPLICATE_PATIENT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null partner exists related constants")
    void testPartnerExistsConstants() {
        assertThat(MessageConstants.PARTNER_EXISTS_FOR_PATIENT).isNotNull();
        assertThat(MessageConstants.PARTNER_EXISTS_FOR_DIFF_PATIENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null existing partner invite related constants")
    void testExistingPartnerInviteConstants() {
        assertThat(MessageConstants.EXISTING_PARTNER_FROM_INVITE_NEW_SECTION).isNotNull();
        assertThat(MessageConstants.EXISTING_PARTNER_WITH_INVALID_DEMOGRAPHICS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null relink partner related constants")
    void testRelinkPartnerConstants() {
        assertThat(MessageConstants.RELINKED_INACTIVE_PARTNER_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.RELINKED_INACTIVE_PARTNER_UNSUCCESSFUL).isNotNull();
        assertThat(MessageConstants.MARKED_PARTNER_INVITATION_PENDING_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null resend invitation related constants")
    void testResendInvitationConstants() {
        assertThat(MessageConstants.RESEND_INVITATION_LINK_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invitation details related constants")
    void testInvitationDetailsConstants() {
        assertThat(MessageConstants.INVITE_DETAIL_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.INVITATION_DETAILS_NOT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null public link related constants")
    void testPublicLinkConstants() {
        assertThat(MessageConstants.PUBLIC_LINK_GENERATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null hierarchy related constants")
    void testHierarchyConstants() {
        assertThat(MessageConstants.HEIRARCHY_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null UUID related constants")
    void testUUIDConstants() {
        assertThat(MessageConstants.INVALID_UUID).isNotNull();
        assertThat(MessageConstants.INVALID_PATIENT_UUID).isNotNull();
        assertThat(MessageConstants.INVALID_ACCOUNT_UUID).isNotNull();
        assertThat(MessageConstants.ACCOUNT_UUID_CAN_NOT_BE_NULL_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null assessment status related constants")
    void testAssessmentStatusConstants() {
        assertThat(MessageConstants.ASSESSMENT_STATUS_IS_NULL).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_NOT_CREATED).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_IS_NOT_COMPLETED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null dashboard status related constants")
    void testDashboardStatusConstants() {
        assertThat(MessageConstants.DASHBOARD_STATUS_FETCHED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null assessment declined related constants")
    void testAssessmentDeclinedConstants() {
        assertThat(MessageConstants.ASSESSMENT_DECLINED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null ROR details related constants")
    void testRORDetailsConstants() {
        assertThat(MessageConstants.ROR_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null results fetched related constants")
    void testResultsFetchedConstants() {
        assertThat(MessageConstants.RESULTS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_RESULT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null logged in user related constants")
    void testLoggedInUserConstants() {
        assertThat(MessageConstants.LOGGED_IN_USER_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.LOGIN_COMPLETED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account configuration related constants")
    void testAccountConfigurationConstants() {
        assertThat(MessageConstants.ACCOUNTCONFIGURATION_NOT_FOUND_FOR_THE_USERS_ACCOUNT).isNotNull();
        assertThat(MessageConstants.ACCOUNT_CONFIG_NOT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null user related constants")
    void testUserConstants() {
        assertThat(MessageConstants.USER_DOES_NOT_EXIST).isNotNull();
        assertThat(MessageConstants.INVALID_USER_PROVIDED).isNotNull();
        assertThat(MessageConstants.INVALID_USER_ROLE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null relative deletion related constants")
    void testRelativeDeletionConstants() {
        assertThat(MessageConstants.NOT_ALLOWED_TO_DELETE_RELATIVE_PRESENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null not a proband related constants")
    void testNotAProbandConstants() {
        assertThat(MessageConstants.NOT_A_PROBAND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null proband update related constants")
    void testProbandUpdateConstants() {
        assertThat(MessageConstants.PROBAND_UPDATED_WITHOUT_PHONE_NUMBER).isNotNull();
        assertThat(MessageConstants.PROBAND_UPDATED_WITHOUT_EMAIL).isNotNull();
        assertThat(MessageConstants.PROBAND_WITH_MOBILE_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.PROBAND_WITH_EMAIL_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.PROBAND_WITH_EMAIL_AND_MOBILE_ALREADY_EXIST).isNotNull();
        assertThat(MessageConstants.PROBAND_ADDED_WITH_PHONE_NUMBER_AND_EMAIL_IS_DUPLICATE).isNotNull();
        assertThat(MessageConstants.PROBAND_ADDED_WITH_EMAIL_AND_PHONE_NUMBER_IS_DUPLICATE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient list count related constants")
    void testPatientListCountConstants() {
        assertThat(MessageConstants.PATIENT_LIST_COUNT_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null bad request related constants")
    void testBadRequestConstants() {
        assertThat(MessageConstants.BAD_REQUEST).isNotNull();
        assertThat(MessageConstants.EMPTY_REQUEST).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null non unique field related constants")
    void testNonUniqueFieldConstants() {
        assertThat(MessageConstants.NON_UNIQUE_FIELD).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null appointment cancellation related constants")
    void testAppointmentCancellationConstants() {
        assertThat(MessageConstants.PATIENT_APPOINTMENT_CANCELLED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.APPOINTMENT_IS_ALREADY_CANCELLED).isNotNull();
        assertThat(MessageConstants.INVALID_APPOINTMENT_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null appointment reschedule related constants")
    void testAppointmentRescheduleConstants() {
        assertThat(MessageConstants.APPOINTMENT_RESCHEDULED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null file related constants")
    void testFileConstants() {
        assertThat(MessageConstants.FILE_REJECTED_PARTIALLY_OR_COMPLETELY).isNotNull();
        assertThat(MessageConstants.FILE_UPLOADED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null gleason score filter related constants")
    void testGleasonScoreFilterConstants() {
        assertThat(MessageConstants.RETURNING_GLEASON_SCORE_FILTER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null custom testing decision ID related constants")
    void testCustomTestingDecisionIDConstants() {
        assertThat(MessageConstants.CUSTOM_TESTING_DECISION_ID_CAN_NOT_BE_NULL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null update order related constants")
    void testUpdateOrderConstants() {
        assertThat(MessageConstants.UPDATED_ORDER_FAILED).isNotNull();
        assertThat(MessageConstants.UPDATED_ORDER_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null post result to PWN related constants")
    void testPostResultToPWNConstants() {
        assertThat(MessageConstants.POST_RESULT_TO_PWN_FAILED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null create order failed related constants")
    void testCreateOrderFailedConstants() {
        assertThat(MessageConstants.CREATE_ORDER_FAILED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null order already created related constants")
    void testOrderAlreadyCreatedConstants() {
        assertThat(MessageConstants.ORDER_ALREADY_CREATED_FOR_THIS_ASSESSMENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null unable to generate order related constants")
    void testUnableToGenerateOrderConstants() {
        assertThat(MessageConstants.UNABLE_TO_GENERATE_ORDER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null test order status updated related constants")
    void testTestOrderStatusUpdatedConstants() {
        assertThat(MessageConstants.TEST_ORDER_STATUS_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null order list count related constants")
    void testOrderListCountConstants() {
        assertThat(MessageConstants.ORDER_LIST_COUNT_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null test order not placed related constants")
    void testTestOrderNotPlacedConstants() {
        assertThat(MessageConstants.TEST_ORDER_NOT_PLACED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid genetic test order ID related constants")
    void testInvalidGeneticTestOrderIDConstants() {
        assertThat(MessageConstants.INVALID_GENETIC_TEST_ORDER_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null cannot update assessment chat bot status related constants")
    void testCannotUpdateAssessmentChatBotStatusConstants() {
        assertThat(MessageConstants.CANNOT_UPDATE_ASSESSMENT_CHAT_BOT_STATUS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null no documents waiting related constants")
    void testNoDocumentsWaitingConstants() {
        assertThat(MessageConstants.NO_DOCUMENTS_WAITING_TO_BE_PROCESSED).isNotNull();
        assertThat(MessageConstants.NO_DOCUMENTS_WAITING_FOR_RESPONSE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid parameter related constants")
    void testInvalidParameterConstants() {
        assertThat(MessageConstants.INVALID_PARAMETER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid input related constants")
    void testInvalidInputConstants() {
        assertThat(MessageConstants.INVALID_INPUT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null error saving specimen info related constants")
    void testErrorSavingSpecimenInfoConstants() {
        assertThat(MessageConstants.ERROR_SAVING_SPECIMEN_INFO).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null city not supported related constants")
    void testCityNotSupportedConstants() {
        assertThat(MessageConstants.CITY_NOT_SUPPORTED_FOR_THE_PROGRAM).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null other info saved related constants")
    void testOtherInfoSavedConstants() {
        assertThat(MessageConstants.OTHER_INFO_SAVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null order ID updated related constants")
    void testOrderIDUpdatedConstants() {
        assertThat(MessageConstants.ORDER_ID_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ERROR_UPDATING_ORDER_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null testing consent changed related constants")
    void testTestingConsentChangedConstants() {
        assertThat(MessageConstants.TESTING_CONSENT_CHANGED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ERROR_CHANGING_TESTING_CONSENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null testing kit info related constants")
    void testTestingKitInfoConstants() {
        assertThat(MessageConstants.TESTING_KIT_INFO_SAVED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.TESTING_KIT_INFO_SAVE_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null signed URL related constants")
    void testSignedURLConstants() {
        assertThat(MessageConstants.SIGNED_URL_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.SIGNED_URL_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null login info updated related constants")
    void testLoginInfoUpdatedConstants() {
        assertThat(MessageConstants.LOGIN_INFO_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.LOGIN_INFO_UPDATE_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid employee password related constants")
    void testInvalidEmployeePasswordConstants() {
        assertThat(MessageConstants.INVALID_EMPLOYEE_PASSWORD).isNotNull();
        assertThat(MessageConstants.INVALID_PATIENT_PASSWORD).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null deactivated activated related constants")
    void testDeactivatedActivatedConstants() {
        assertThat(MessageConstants.DEACTIVATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ACTIVATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.DEACTIVATED_USER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null last assessment risk level related constants")
    void testLastAssessmentRiskLevelConstants() {
        assertThat(MessageConstants.LAST_ASSESSMENT_RISK_LEVEL_NOT_GENERATED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null incorrect login reset password related constants")
    void testIncorrectLoginResetPasswordConstants() {
        assertThat(MessageConstants.INCORRECT_LOGIN_RESET_PASSWORD).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null recent assessment related constants")
    void testRecentAssessmentConstants() {
        assertThat(MessageConstants.RECENT_ASSESSMENT_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.RECENT_ASSESSMENT_FAILED).isNotNull();
        assertThat(MessageConstants.RECENT_ASSESSMENT_NOT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null employee filters related constants")
    void testEmployeeFiltersConstants() {
        assertThat(MessageConstants.EMPLOYEE_FILTERS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null test order list filters related constants")
    void testTestOrderListFiltersConstants() {
        assertThat(MessageConstants.TEST_ORDER_LIST_FILTERS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null work site filter related constants")
    void testWorkSiteFilterConstants() {
        assertThat(MessageConstants.WORK_SITE_FILTER_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null key metrics related constants")
    void testKeyMetricsConstants() {
        assertThat(MessageConstants.KEY_METRICS_RETRIEVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null basic info saved related constants")
    void testBasicInfoSavedConstants() {
        assertThat(MessageConstants.BASIC_INFO_SAVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null contact info saved related constants")
    void testContactInfoSavedConstants() {
        assertThat(MessageConstants.CONTACT_INFO_SAVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null job info saved related constants")
    void testJobInfoSavedConstants() {
        assertThat(MessageConstants.JOB_INFO_SAVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null tests list related constants")
    void testTestsListConstants() {
        assertThat(MessageConstants.TESTS_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.TESTS_LIST_FAILED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null welcome message related constants")
    void testWelcomeMessageConstants() {
        assertThat(MessageConstants.WELCOME_MESSAGE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null employee verified related constants")
    void testEmployeeVerifiedConstants() {
        assertThat(MessageConstants.EMPLOYEE_VERIFIED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null reset password related constants")
    void testResetPasswordConstants() {
        assertThat(MessageConstants.RESET_PASSWORD_REQUEST_MESSAGE).isNotNull();
        assertThat(MessageConstants.RESET_PASSWORD_LINK_SENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null reset key related constants")
    void testResetKeyConstants() {
        assertThat(MessageConstants.RESET_KEY_EXPIRED_ERROR_MESSAGE).isNotNull();
        assertThat(MessageConstants.RESET_KEY_INVALID_ERROR_MESSAGE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account activation link related constants")
    void testAccountActivationLinkConstants() {
        assertThat(MessageConstants.ACCOUNT_ACTIVATION_LINK_RESENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null verification key related constants")
    void testVerificationKeyConstants() {
        assertThat(MessageConstants.INVALID_VERIFICATION_KEY).isNotNull();
        assertThat(MessageConstants.EXPIRED_VERIFICATION_KEY).isNotNull();
        assertThat(MessageConstants.VALID_VERIFICATION_KEY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account list fetched related constants")
    void testAccountListFetchedConstants() {
        assertThat(MessageConstants.ACCOUNT_LIST_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ACCOUNT_LIST_FETCHED_SUCCESSFULLY_FOR_USER).isNotNull();
        assertThat(MessageConstants.FAILED_TO_FETCH_ACCOUNT_LIST_FOR_USER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account updated related constants")
    void testAccountUpdatedConstants() {
        assertThat(MessageConstants.ACCOUNT_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ACCOUNT_DETAILS_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.ACCOUNT_DETAILS_UPDATE_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid account ID related constants")
    void testInvalidAccountIDConstants() {
        assertThat(MessageConstants.INVALID_ACCOUNT_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null duplicate account related constants")
    void testDuplicateAccountConstants() {
        assertThat(MessageConstants.DUPLICATE_ACCOUNT_WITH_SITE).isNotNull();
        assertThat(MessageConstants.DUPLICATE_ACCOUNT_NAME).isNotNull();
        assertThat(MessageConstants.ACCOUNT_ALREADY_EXIST).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invitation link sent related constants")
    void testInvitationLinkSentConstants() {
        assertThat(MessageConstants.INVITATION_LINK_SENT_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.RESEND_INVITATION_LINK_SUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account details fetched related constants")
    void testAccountDetailsFetchedConstants() {
        assertThat(MessageConstants.ACCOUNT_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null primary contact related constants")
    void testPrimaryContactConstants() {
        assertThat(MessageConstants.PRIMARY_CONTACT_EMAIL_CANT_BE_EMPTY).isNotNull();
        assertThat(MessageConstants.PRIMARY_CONTACT_NAME_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null activate assessment at related constants")
    void testActivateAssessmentAtConstants() {
        assertThat(MessageConstants.ACTIVATE_ASSESSMENT_AT_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account logo related constants")
    void testAccountLogoConstants() {
        assertThat(MessageConstants.ACCOUNT_LOGO_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null contacts fetched related constants")
    void testContactsFetchedConstants() {
        assertThat(MessageConstants.CONTACTS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null search by name related constants")
    void testSearchByNameConstants() {
        assertThat(MessageConstants.SEARCH_BY_NAME_SUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null configurations fetched related constants")
    void testConfigurationsFetchedConstants() {
        assertThat(MessageConstants.CONFIGURATIONS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null found multiple users related constants")
    void testFoundMultipleUsersConstants() {
        assertThat(MessageConstants.FOUND_MULTIPLE_USERS_FOR_SAME_CREDENTIALS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null user created related constants")
    void testUserCreatedConstants() {
        assertThat(MessageConstants.USER_CREATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account created related constants")
    void testAccountCreatedConstants() {
        assertThat(MessageConstants.ACCOUNT_CREATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null organisation code related constants")
    void testOrganisationCodeConstants() {
        assertThat(MessageConstants.ORGANISATION_CODE_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null account name related constants")
    void testAccountNameConstants() {
        assertThat(MessageConstants.ACCOUNT_NAME_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null timezone related constants")
    void testTimezoneConstants() {
        assertThat(MessageConstants.TIMEZONE_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null country related constants")
    void testCountryConstants() {
        assertThat(MessageConstants.COUNTRY_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null phone number related constants")
    void testPhoneNumberConstants() {
        assertThat(MessageConstants.PHONE_NUMBER_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null duplicate site name related constants")
    void testDuplicateSiteNameConstants() {
        assertThat(MessageConstants.DUPLICATE_SITE_NAME).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null city state related constants")
    void testCityStateConstants() {
        assertThat(MessageConstants.CITY_STATE_ADDED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.CITY_STATE_ADDITION_UNSUCCESSFUL).isNotNull();
        assertThat(MessageConstants.NO_DATA_FOUND_FOR_CITY_AND_STATE).isNotNull();
        assertThat(MessageConstants.CITY_AND_STATE_ID_CANT_BE_EMPTY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null try clearing browser cache related constants")
    void testTryClearingBrowserCacheConstants() {
        assertThat(MessageConstants.TRY_CLEARING_BROWSER_CACHE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null users list fetched related constants")
    void testUsersListFetchedConstants() {
        assertThat(MessageConstants.USERS_LIST_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid user ID related constants")
    void testInvalidUserIDConstants() {
        assertThat(MessageConstants.INVALID_USER_ID).isNotNull();
        assertThat(MessageConstants.INCORRECT_USER_ID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null user updated related constants")
    void testUserUpdatedConstants() {
        assertThat(MessageConstants.USER_UPDATED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.USER_UPDATE_UNSUCCESSFUL).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null role list fetched related constants")
    void testRoleListFetchedConstants() {
        assertThat(MessageConstants.ROLE_LIST_FETCHED_SUCCESSFULLY_FOR_USER).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null menu fetched related constants")
    void testMenuFetchedConstants() {
        assertThat(MessageConstants.MENU_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null validated URL related constants")
    void testValidatedURLConstants() {
        assertThat(MessageConstants.VALIDATED_URL_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null validation link expired related constants")
    void testValidationLinkExpiredConstants() {
        assertThat(MessageConstants.VALIDATION_LINK_EXPIRED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null no patient found related constants")
    void testNoPatientFoundConstants() {
        assertThat(MessageConstants.NO_PATIENT_FOUND_FOR_ASSESSMENT_UUID).isNotNull();
        assertThat(MessageConstants.NO_PATIENT_FOUND_FOR_PATIENT_UUID).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null assessments not found related constants")
    void testAssessmentsNotFoundConstants() {
        assertThat(MessageConstants.ASSESSMENTS_NOT_FOUND).isNotNull();
        assertThat(MessageConstants.NO_ASSESSMENT_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null no in progress assessments found related constants")
    void testNoInProgressAssessmentsFoundConstants() {
        assertThat(MessageConstants.NO_IN_PROGRESS_ASSESSMENTS_FOUND).isNotNull();
        assertThat(MessageConstants.ASSESSMENT_NOT_IN_PROGRESS).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null email ID or cell phone mandatory related constants")
    void testEmailIDOrCellPhoneMandatoryConstants() {
        assertThat(MessageConstants.EMAIL_ID_OR_CELL_PHONE_IS_MANDATORY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null date of birth mandatory related constants")
    void testDateOfBirthMandatoryConstants() {
        assertThat(MessageConstants.DATE_OF_BIRTH_IS_MANDATORY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null category is required related constants")
    void testCategoryIsRequiredConstants() {
        assertThat(MessageConstants.CATEGORY_IS_REQUIRED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient info updated related constants")
    void testPatientInfoUpdatedConstants() {
        assertThat(MessageConstants.PATIENT_INFO_UPDATED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null duplicate email or cellphone related constants")
    void testDuplicateEmailOrCellphoneConstants() {
        assertThat(MessageConstants.DUPLICATE_EMAIL_OR_CELLPHONE).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null patient basic details fetched related constants")
    void testPatientBasicDetailsFetchedConstants() {
        assertThat(MessageConstants.PATIENT_BASIC_DETAILS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null unauthorised patient related constants")
    void testUnauthorisedPatientConstants() {
        assertThat(MessageConstants.UNAUTHORISED_PATIENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null assessment UUID is required related constants")
    void testAssessmentUUIDIsRequiredConstants() {
        assertThat(MessageConstants.ASSESSMENT_UUID_IS_REQUIRED).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null external document status list fetched related constants")
    void testExternalDocumentStatusListFetchedConstants() {
        assertThat(MessageConstants.EXTERNAL_DOCUMENT_STATUS_LIST_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null CS order configs fetched related constants")
    void testCSOrderConfigsFetchedConstants() {
        assertThat(MessageConstants.CS_ORDER_CONFIGS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null NIPT order configs fetched related constants")
    void testNIPTOrderConfigsFetchedConstants() {
        assertThat(MessageConstants.NIPT_ORDER_CONFIGS_FETCHED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null results uploaded related constants")
    void testResultsUploadedConstants() {
        assertThat(MessageConstants.RESULTS_UPLOADED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null data retrieved related constants")
    void testDataRetrievedConstants() {
        assertThat(MessageConstants.DATA_RETRIEVED_SUCCESSFULLY).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null genetic tests fetched related constants")
    void testGeneticTestsFetchedConstants() {
        assertThat(MessageConstants.GENETIC_TESTS_FETCHED_SUCCESSFULLY).isNotNull();
        assertThat(MessageConstants.NO_GENETIC_TEST_FOUND).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null invalid patient related constants")
    void testInvalidPatientConstants() {
        assertThat(MessageConstants.INVALID_PATIENT).isNotNull();
    }

    @Test
    @DisplayName("Should have non-null no assessment found related constants")
    void testNoAssessmentFoundConstants() {
        assertThat(MessageConstants.NO_ASSESSMENT_FOUND).isNotNull();
    }
}

