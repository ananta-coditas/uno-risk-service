package com.ambry.transformer.nccn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.genomebrain.backend.service.NCCNExecuter;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Model;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

@ExtendWith(MockitoExtension.class)
@DisplayName("NCCNRiskModelTransformer Unit Tests")
class NCCNRiskModelTransformerTest {

    @Mock
    private NCCNExecuter nccnExecuter;

    @Mock
    private NCCNValidator nccnValidator;

    @Mock
    private NCCNClinicalHistoryMapper nccnClinicalHistoryMapper;

    @InjectMocks
    private NCCNRiskModelTransformer transformer;

    private Risk risk;
    private Model model;
    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        model = new Model();
        model.setName(ModelName.NCCN);
        
        person1 = new Person();
        person1.setId(1);
        person1.setProband(true);
        
        person2 = new Person();
        person2.setId(2);
        person2.setProband(false);
        
        List<Person> family = new LinkedList<>();
        family.add(person1);
        family.add(person2);
        risk.setFamily(family);
    }

    @Test
    @DisplayName("Should return NCCN model name")
    void testGetModelName() {
        ModelName result = transformer.getModelName();

        assertThat(result).isEqualTo(ModelName.NCCN);
    }

    @Test
    @DisplayName("Should transform request with family members")
    void testTransformRequest() {
        ClinicalHistory clinicalHistory1 = new ClinicalHistory();
        ClinicalHistory clinicalHistory2 = new ClinicalHistory();
        
        when(nccnClinicalHistoryMapper.transform(any(Person.class), any(Map.class)))
            .thenReturn(clinicalHistory1, clinicalHistory2);

        NCCNCoreRequest result = transformer.transformRequest(risk, model);

        assertThat(result).isNotNull();
        assertThat(result.getClinicalHistory()).isNotNull();
        assertThat(result.getClinicalHistory()).hasSize(2);
        // Use matchers for both arguments to avoid InvalidUseOfMatchers error
        // Verify it was called twice (once for each person)
        verify(nccnClinicalHistoryMapper, org.mockito.Mockito.times(2))
            .transform(any(Person.class), any(Map.class));
    }

    @Test
    @DisplayName("Should transform request with empty family")
    void testTransformRequestWithEmptyFamily() {
        risk.setFamily(new LinkedList<>());
        // The transformer calls NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber()
        // which uses getFirst() and will throw NoSuchElementException for empty list
        assertThatThrownBy(() -> transformer.transformRequest(risk, model))
            .isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    @DisplayName("Should transform request with single family member")
    void testTransformRequestWithSingleFamilyMember() {
        List<Person> family = new LinkedList<>();
        family.add(person1);
        risk.setFamily(family);
        
        ClinicalHistory clinicalHistory = new ClinicalHistory();
        when(nccnClinicalHistoryMapper.transform(any(Person.class), any(Map.class)))
            .thenReturn(clinicalHistory);

        NCCNCoreRequest result = transformer.transformRequest(risk, model);

        assertThat(result).isNotNull();
        assertThat(result.getClinicalHistory()).hasSize(1);
    }

    @Test
    @DisplayName("Should invoke risk model")
    void testInvokeRiskModel() {
        NCCNCoreRequest request = new NCCNCoreRequest();
        NCCNCoreResponse expectedResponse = new NCCNCoreResponse();
        
        when(nccnExecuter.invokeNCCNRiskModel(request)).thenReturn(expectedResponse);

        NCCNCoreResponse result = transformer.invokeRiskModel(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(nccnExecuter).invokeNCCNRiskModel(request);
    }

    @Test
    @DisplayName("Should transform response")
    void testTransformResponse() {
        NCCNCoreResponse modelResponse = new NCCNCoreResponse();
        modelResponse.setHbocOverall("Yes");
        modelResponse.setHbocVersion("v1.0");
        
        NCCNResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isTrue();
        assertThat(result.getHbocVersion()).isEqualTo("v1.0");
    }

    @Test
    @DisplayName("Should transform response with null values")
    void testTransformResponseWithNullValues() {
        NCCNCoreResponse modelResponse = new NCCNCoreResponse();
        
        NCCNResult result = transformer.transformResponse(modelResponse);

        assertThat(result).isNotNull();
        assertThat(result.getHbocOverall()).isFalse();
        assertThat(result.getLynchOverall()).isFalse();
        assertThat(result.getApsApcOverall()).isFalse();
    }

    @Test
    @DisplayName("Should return validator")
    void testGetRiskModelValidator() {
        RiskModelValidator<NCCNCoreRequest> result = transformer.getRiskModelValidator();

        assertThat(result).isEqualTo(nccnValidator);
    }

    @Test
    @DisplayName("Should transform request with multiple family members")
    void testTransformRequestWithMultipleFamilyMembers() {
        Person person3 = new Person();
        person3.setId(3);
        person3.setProband(false);
        
        List<Person> family = new LinkedList<>();
        family.add(person1);
        family.add(person2);
        family.add(person3);
        risk.setFamily(family);
        
        ClinicalHistory clinicalHistory = new ClinicalHistory();
        when(nccnClinicalHistoryMapper.transform(any(Person.class), any(Map.class)))
            .thenReturn(clinicalHistory);

        NCCNCoreRequest result = transformer.transformRequest(risk, model);

        assertThat(result).isNotNull();
        assertThat(result.getClinicalHistory()).hasSize(3);
    }

    @Test
    @DisplayName("Should transform request with proband not first in list")
    void testTransformRequestWithProbandNotFirst() {
        Person nonProband = new Person();
        nonProband.setId(0);
        nonProband.setProband(false);
        
        List<Person> family = new LinkedList<>();
        family.add(nonProband);
        family.add(person1);
        risk.setFamily(family);
        
        ClinicalHistory clinicalHistory = new ClinicalHistory();
        when(nccnClinicalHistoryMapper.transform(any(Person.class), any(Map.class)))
            .thenReturn(clinicalHistory);

        NCCNCoreRequest result = transformer.transformRequest(risk, model);

        assertThat(result).isNotNull();
        assertThat(result.getClinicalHistory()).hasSize(2);
    }
}

