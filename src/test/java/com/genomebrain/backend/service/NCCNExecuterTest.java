package com.genomebrain.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.CustomResponseDTO;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.genomebrain.backend.service.util.APIConstants;
import com.genomebrain.backend.service.util.CareAPIUtil;
import com.genomebrain.backend.service.util.MessageConstants;
import com.genomebrain.backend.web.rest.util.StatusCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("NCCNExecuter Unit Tests")
class NCCNExecuterTest {

    @Mock
    private CareAPIUtil careAPIUtil;

    @InjectMocks
    private NCCNExecuter nccnExecuter;

    private NCCNCoreRequest request;
    private ClinicalHistory clinicalHistory;
    private NCCNCoreResponse nccnResponse;

    @BeforeEach
    void setUp() {
        request = new NCCNCoreRequest();
        clinicalHistory = new ClinicalHistory();
        clinicalHistory.setRelationship("PROBAND");
        clinicalHistory.setSideOfFam("MATERNAL");
        clinicalHistory.setSexAtBirth("MALE");
        clinicalHistory.setCancerOverall("YES");
        clinicalHistory.setPolypOverall("NO");

        List<ClinicalHistory> clinicalHistories = new ArrayList<>();
        clinicalHistories.add(clinicalHistory);
        request.setClinicalHistory(clinicalHistories);

        nccnResponse = new NCCNCoreResponse();
        nccnResponse.setHbocOverall("MET");
        nccnResponse.setLynchOverall("NOT_MET");
    }

    // ========== invokeNCCNRiskModel Tests ==========

    @Test
    @DisplayName("Should return empty response when request is null")
    void testInvokeNCCNRiskModelWithNullRequest() {
        NCCNCoreResponse result = nccnExecuter.invokeNCCNRiskModel(null);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(NCCNCoreResponse.class);
        verify(careAPIUtil, never()).getQuestionObject(any());
    }

    @Test
    @DisplayName("Should return response when validation fails")
    void testInvokeNCCNRiskModelWithValidationFailure() {
        clinicalHistory.setRelationship(null); // Invalid relationship

        NCCNCoreResponse result = nccnExecuter.invokeNCCNRiskModel(request);

        assertThat(result).isNotNull();
        verify(careAPIUtil, never()).getQuestionObject(any());
    }

    @Test
    @DisplayName("Should return response when validation succeeds and API call succeeds")
    void testInvokeNCCNRiskModelWithSuccess() {
        when(careAPIUtil.getQuestionObject(any(NCCNCoreRequest.class))).thenReturn(nccnResponse);

        NCCNCoreResponse result = nccnExecuter.invokeNCCNRiskModel(request);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isEqualTo("MET");
        verify(careAPIUtil).getQuestionObject(request);
    }

    @Test
    @DisplayName("Should handle exception from CareAPIUtil gracefully")
    void testInvokeNCCNRiskModelWithException() {
        when(careAPIUtil.getQuestionObject(any(NCCNCoreRequest.class)))
            .thenThrow(new RuntimeException("API Error"));

        NCCNCoreResponse result = nccnExecuter.invokeNCCNRiskModel(request);

        assertThat(result).isNotNull();
        verify(careAPIUtil).getQuestionObject(request);
    }

    @Test
    @DisplayName("Should return response when request has empty clinical history list")
    void testInvokeNCCNRiskModelWithEmptyRequest() {
        NCCNCoreRequest emptyRequest = new NCCNCoreRequest();
        emptyRequest.setClinicalHistory(new ArrayList<>());
        
        // When clinical history is empty, validation passes (no items to validate)
        // and API is called
        when(careAPIUtil.getQuestionObject(any(NCCNCoreRequest.class))).thenReturn(nccnResponse);

        NCCNCoreResponse result = nccnExecuter.invokeNCCNRiskModel(emptyRequest);

        assertThat(result).isNotNull();
        // Empty list means validation passes, so API is called
        verify(careAPIUtil).getQuestionObject(emptyRequest);
    }

    // ========== getNonCareQuestionnaireDTO Tests ==========

    @Test
    @DisplayName("Should return success response when request is null")
    void testGetNonCareQuestionnaireDTOWithNullRequest() {
        CustomResponseDTO result = nccnExecuter.getNonCareQuestionnaireDTO(null);

        assertThat(result).isNotNull();
        verify(careAPIUtil, never()).getQuestionObject(any());
    }

    @Test
    @DisplayName("Should return failure response when validation fails")
    void testGetNonCareQuestionnaireDTOWithValidationFailure() {
        clinicalHistory.setRelationship("INVALID_RELATIONSHIP");

        CustomResponseDTO result = nccnExecuter.getNonCareQuestionnaireDTO(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getStatusCode()).isEqualTo(StatusCode.FAILURE.getstatusCode());
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_RELATIONSHIP);
        verify(careAPIUtil, never()).getQuestionObject(any());
    }

    @Test
    @DisplayName("Should return success response when validation succeeds and API call succeeds")
    void testGetNonCareQuestionnaireDTOWithSuccess() {
        when(careAPIUtil.getQuestionObject(any(NCCNCoreRequest.class))).thenReturn(nccnResponse);

        CustomResponseDTO result = nccnExecuter.getNonCareQuestionnaireDTO(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
        assertThat(result.getStatusCode()).isEqualTo(StatusCode.SUCCESS.getstatusCode());
        assertThat(result.getData()).isEqualTo(nccnResponse);
        verify(careAPIUtil).getQuestionObject(request);
    }

    @Test
    @DisplayName("Should return failure response when API call throws exception")
    void testGetNonCareQuestionnaireDTOWithException() {
        when(careAPIUtil.getQuestionObject(any(NCCNCoreRequest.class)))
            .thenThrow(new RuntimeException("API Error"));

        CustomResponseDTO result = nccnExecuter.getNonCareQuestionnaireDTO(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getStatusCode()).isEqualTo(StatusCode.FAILURE.getstatusCode());
        assertThat(result.getMessage()).isEqualTo("Request failed");
        verify(careAPIUtil).getQuestionObject(request);
    }

    @Test
    @DisplayName("Should return success response when request has empty clinical history list")
    void testGetNonCareQuestionnaireDTOWithEmptyRequest() {
        NCCNCoreRequest emptyRequest = new NCCNCoreRequest();
        emptyRequest.setClinicalHistory(new ArrayList<>());
        
        // When clinical history is empty, validation passes (no items to validate)
        // and API is called
        when(careAPIUtil.getQuestionObject(any(NCCNCoreRequest.class))).thenReturn(nccnResponse);

        CustomResponseDTO result = nccnExecuter.getNonCareQuestionnaireDTO(emptyRequest);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
        // Empty list means validation passes, so API is called
        verify(careAPIUtil).getQuestionObject(emptyRequest);
    }

    // ========== getDataValidation Tests ==========

    @Test
    @DisplayName("Should return success when all validations pass")
    void testGetDataValidationWithValidData() {
        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
        assertThat(result.getStatusCode()).isEqualTo(StatusCode.SUCCESS.getstatusCode());
    }

    @Test
    @DisplayName("Should return failure when relationship is null")
    void testGetDataValidationWithNullRelationship() {
        clinicalHistory.setRelationship(null);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getStatusCode()).isEqualTo(StatusCode.FAILURE.getstatusCode());
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_RELATIONSHIP);
    }

    @Test
    @DisplayName("Should return failure when relationship is empty")
    void testGetDataValidationWithEmptyRelationship() {
        clinicalHistory.setRelationship("");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_RELATIONSHIP);
    }

    @Test
    @DisplayName("Should return failure when relationship is invalid")
    void testGetDataValidationWithInvalidRelationship() {
        clinicalHistory.setRelationship("INVALID_RELATIONSHIP");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_RELATIONSHIP);
    }

    @Test
    @DisplayName("Should return failure when relationship contains FIRST_COUSIN and sex at birth is null")
    void testGetDataValidationWithCousinAndNullSexAtBirth() {
        clinicalHistory.setRelationship("FIRST_COUSIN_MALE");
        clinicalHistory.setSexAtBirth(null);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST);
    }

    @Test
    @DisplayName("Should return failure when relationship contains FIRST_COUSIN and sex at birth is empty")
    void testGetDataValidationWithCousinAndEmptySexAtBirth() {
        clinicalHistory.setRelationship("FIRST_COUSIN_FEMALE");
        clinicalHistory.setSexAtBirth("");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST);
    }

    @Test
    @DisplayName("Should return failure when relationship contains FIRST_COUSIN and sex at birth is invalid")
    void testGetDataValidationWithCousinAndInvalidSexAtBirth() {
        clinicalHistory.setRelationship("FIRST_COUSIN_MALE");
        clinicalHistory.setSexAtBirth("INVALID");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SEX_AT_BIRTH_COUSIN_1ST);
    }

    @Test
    @DisplayName("Should return success when relationship contains FIRST_COUSIN and sex at birth is valid")
    void testGetDataValidationWithCousinAndValidSexAtBirth() {
        clinicalHistory.setRelationship("FIRST_COUSIN_MALE");
        clinicalHistory.setSexAtBirth("MALE");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return failure when side of family is null")
    void testGetDataValidationWithNullSideOfFamily() {
        clinicalHistory.setSideOfFam(null);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SIDE_OF_FAM);
    }

    @Test
    @DisplayName("Should return failure when side of family is empty")
    void testGetDataValidationWithEmptySideOfFamily() {
        clinicalHistory.setSideOfFam("");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SIDE_OF_FAM);
    }

    @Test
    @DisplayName("Should return failure when side of family is invalid")
    void testGetDataValidationWithInvalidSideOfFamily() {
        clinicalHistory.setSideOfFam("INVALID_SIDE");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SIDE_OF_FAM);
    }

    @Test
    @DisplayName("Should return failure when sex at birth is invalid (not null/empty)")
    void testGetDataValidationWithInvalidSexAtBirth() {
        clinicalHistory.setSexAtBirth("INVALID_SEX");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SEX_AT_BIRTH);
    }

    @Test
    @DisplayName("Should return success when sex at birth is null")
    void testGetDataValidationWithNullSexAtBirth() {
        clinicalHistory.setSexAtBirth(null);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return success when sex at birth is empty")
    void testGetDataValidationWithEmptySexAtBirth() {
        clinicalHistory.setSexAtBirth("");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return failure when cancer overall is null")
    void testGetDataValidationWithNullCancerOverall() {
        clinicalHistory.setCancerOverall(null);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_CANCER_OVERALL);
    }

    @Test
    @DisplayName("Should return failure when cancer overall is empty")
    void testGetDataValidationWithEmptyCancerOverall() {
        clinicalHistory.setCancerOverall("");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_CANCER_OVERALL);
    }

    @Test
    @DisplayName("Should return failure when cancer overall is invalid")
    void testGetDataValidationWithInvalidCancerOverall() {
        clinicalHistory.setCancerOverall("INVALID");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_CANCER_OVERALL);
    }

    @Test
    @DisplayName("Should return success when cancer overall is YES")
    void testGetDataValidationWithYesCancerOverall() {
        clinicalHistory.setCancerOverall("YES");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return success when cancer overall is NO")
    void testGetDataValidationWithNoCancerOverall() {
        clinicalHistory.setCancerOverall("NO");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return success when cancer overall is NOT_PROVIDED")
    void testGetDataValidationWithNotProvidedCancerOverall() {
        clinicalHistory.setCancerOverall("NOT_PROVIDED");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return failure when proband has invalid polyp overall")
    void testGetDataValidationWithProbandAndInvalidPolypOverall() {
        clinicalHistory.setRelationship("PROBAND");
        clinicalHistory.setPolypOverall("INVALID");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_POLYP_OVERALL_PROBAND);
    }

    @Test
    @DisplayName("Should return success when proband has valid polyp overall")
    void testGetDataValidationWithProbandAndValidPolypOverall() {
        clinicalHistory.setRelationship("PROBAND");
        clinicalHistory.setPolypOverall("YES");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return success when proband has null polyp overall")
    void testGetDataValidationWithProbandAndNullPolypOverall() {
        clinicalHistory.setRelationship("PROBAND");
        clinicalHistory.setPolypOverall(null);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return success when proband has empty polyp overall")
    void testGetDataValidationWithProbandAndEmptyPolypOverall() {
        clinicalHistory.setRelationship("PROBAND");
        clinicalHistory.setPolypOverall("");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return success when non-proband has invalid polyp overall")
    void testGetDataValidationWithNonProbandAndInvalidPolypOverall() {
        clinicalHistory.setRelationship("MOTHER");
        clinicalHistory.setPolypOverall("INVALID");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should return failure when first clinical history is invalid")
    void testGetDataValidationWithMultipleHistoriesFirstInvalid() {
        ClinicalHistory history1 = new ClinicalHistory();
        history1.setRelationship("INVALID");
        history1.setSideOfFam("MATERNAL");
        history1.setCancerOverall("YES");

        ClinicalHistory history2 = new ClinicalHistory();
        history2.setRelationship("MOTHER");
        history2.setSideOfFam("PATERNAL");
        history2.setSexAtBirth("FEMALE");
        history2.setCancerOverall("NO");

        List<ClinicalHistory> histories = new ArrayList<>();
        histories.add(history1);
        histories.add(history2);
        request.setClinicalHistory(histories);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_RELATIONSHIP);
    }

    @Test
    @DisplayName("Should return failure when second clinical history is invalid")
    void testGetDataValidationWithMultipleHistoriesSecondInvalid() {
        ClinicalHistory history1 = new ClinicalHistory();
        history1.setRelationship("PROBAND");
        history1.setSideOfFam("MATERNAL");
        history1.setSexAtBirth("MALE");
        history1.setCancerOverall("YES");

        ClinicalHistory history2 = new ClinicalHistory();
        history2.setRelationship("MOTHER");
        history2.setSideOfFam("INVALID");
        history2.setSexAtBirth("FEMALE");
        history2.setCancerOverall("NO");

        List<ClinicalHistory> histories = new ArrayList<>();
        histories.add(history1);
        histories.add(history2);
        request.setClinicalHistory(histories);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.FAILURE);
        assertThat(result.getMessage()).isEqualTo(APIConstants.VALID_SIDE_OF_FAM);
    }

    @Test
    @DisplayName("Should return success when all clinical histories are valid")
    void testGetDataValidationWithMultipleHistoriesAllValid() {
        ClinicalHistory history1 = new ClinicalHistory();
        history1.setRelationship("PROBAND");
        history1.setSideOfFam("MATERNAL");
        history1.setSexAtBirth("MALE");
        history1.setCancerOverall("YES");
        history1.setPolypOverall("NO");

        ClinicalHistory history2 = new ClinicalHistory();
        history2.setRelationship("MOTHER");
        history2.setSideOfFam("PATERNAL");
        history2.setSexAtBirth("FEMALE");
        history2.setCancerOverall("NO");

        List<ClinicalHistory> histories = new ArrayList<>();
        histories.add(history1);
        histories.add(history2);
        request.setClinicalHistory(histories);

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should handle case-insensitive relationship validation")
    void testGetDataValidationWithCaseInsensitiveRelationship() {
        clinicalHistory.setRelationship("proband");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should handle case-insensitive side of family validation")
    void testGetDataValidationWithCaseInsensitiveSideOfFamily() {
        clinicalHistory.setSideOfFam("maternal");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should handle case-insensitive sex at birth validation")
    void testGetDataValidationWithCaseInsensitiveSexAtBirth() {
        clinicalHistory.setSexAtBirth("male");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should handle case-insensitive cancer overall validation")
    void testGetDataValidationWithCaseInsensitiveCancerOverall() {
        clinicalHistory.setCancerOverall("yes");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should handle case-insensitive polyp overall validation for proband")
    void testGetDataValidationWithCaseInsensitivePolypOverall() {
        clinicalHistory.setRelationship("PROBAND");
        clinicalHistory.setPolypOverall("yes");

        CustomResponseDTO result = nccnExecuter.getDataValidation(request);

        assertThat(result.getStatus()).isEqualTo(MessageConstants.SUCCESS);
    }

    @Test
    @DisplayName("Should validate all relationship types")
    void testGetDataValidationWithAllRelationshipTypes() {
        String[] relationships = {"PROBAND", "MOTHER", "FATHER", "SISTER", "BROTHER", 
                                 "DAUGHTER", "SON", "NIECE", "NEPHEW", "HALF_SISTER", 
                                 "HALF_BROTHER", "GRANDMOTHER", "GRANDFATHER", 
                                 "GRANDDAUGHTER", "GRANDSON", "AUNT", "UNCLE", 
                                 "FIRST_COUSIN_MALE", "FIRST_COUSIN_FEMALE", 
                                 "GREAT_GRANDMOTHER", "GREAT_GRANDFATHER", "GREAT_AUNT", 
                                 "GREAT_UNCLE", "GREAT_NEPHEW", "GREAT_NIECE", 
                                 "GREAT_GRANDDAUGHTER", "GREAT_GRANDSON", "HALF_AUNT", 
                                 "HALF_UNCLE", "HALF_NIECE", "HALF_NEPHEW", "OTHERS"};

        for (String relationship : relationships) {
            clinicalHistory.setRelationship(relationship);
            if (relationship.contains("FIRST_COUSIN")) {
                clinicalHistory.setSexAtBirth("MALE");
            }
            CustomResponseDTO result = nccnExecuter.getDataValidation(request);
            assertThat(result.getStatus())
                .as("Relationship %s should be valid", relationship)
                .isEqualTo(MessageConstants.SUCCESS);
        }
    }

    // ========== Getter Methods Tests ==========

    @Test
    @DisplayName("Should return CareAPIUtil")
    void testGetCareAPIUtil() {
        CareAPIUtil result = nccnExecuter.getCareAPIUtil();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(careAPIUtil);
    }

    @Test
    @DisplayName("Should return relationship list")
    void testGetRelationShip() {
        List<String> result = nccnExecuter.getRelationShip();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).contains("PROBAND", "MOTHER", "FATHER");
    }

    @Test
    @DisplayName("Should return side of family list")
    void testGetSideOfFamily() {
        List<String> result = nccnExecuter.getSideOfFamily();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).contains("MATERNAL", "PATERNAL", "OTHER");
    }

    @Test
    @DisplayName("Should return sex at birth list")
    void testGetSexATBirth() {
        List<String> result = nccnExecuter.getSexATBirth();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).contains("MALE", "FEMALE", "UNKNOWN");
    }

    @Test
    @DisplayName("Should return PROBAND constant")
    void testGetPROBAND() {
        String result = nccnExecuter.getPROBAND();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("PROBAND");
    }
}

