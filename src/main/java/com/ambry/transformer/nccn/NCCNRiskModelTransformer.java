package com.ambry.transformer.nccn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.genomebrain.backend.service.NCCNExecuter;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

@Service
public class NCCNRiskModelTransformer implements RiskModelExecutor<NCCNCoreRequest, NCCNCoreResponse, NCCNResult>{

    private final NCCNExecuter nccnExecuter;
    private final NCCNClinicalHistoryMapper nccnClinicalHistoryMapper;
    private final NCCNValidator nccnValidator;
    
    public NCCNRiskModelTransformer(NCCNExecuter nccnExecuter, NCCNValidator nccnValidator, NCCNClinicalHistoryMapper nccnClinicalHistoryMapper) {
        this.nccnExecuter = nccnExecuter;
        this.nccnValidator = nccnValidator;
        this.nccnClinicalHistoryMapper = nccnClinicalHistoryMapper;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.NCCN;
    }

    @Override
    public NCCNCoreRequest transformRequest(Risk commonRequest, Model model) {
        NCCNCoreRequest nccnCoreRequest = new NCCNCoreRequest();
        List<ClinicalHistory> clinicalHistories = new ArrayList<>();
        Map<Integer, NCCNPersonRelationShipDetails> familyRelativesRelationshipDetails =  NCCNRiskModelUtils.calculateRelationShipForAllFamilyMemeber(commonRequest);
        for(Person person : commonRequest.getFamily()) {
            ClinicalHistory clinicalHistory = nccnClinicalHistoryMapper.transform(person, familyRelativesRelationshipDetails);
            clinicalHistories.add(clinicalHistory);
        }
        nccnCoreRequest.setClinicalHistory(clinicalHistories);
        return nccnCoreRequest;
    }

    @Override
    public NCCNCoreResponse invokeRiskModel(NCCNCoreRequest nccnCoreRequest) {
        return nccnExecuter.invokeNCCNRiskModel(nccnCoreRequest);
    }

    @Override
    public NCCNResult transformResponse(NCCNCoreResponse modelResponse) {
        return NCCNResultMapper.convertTo(modelResponse);
    }

    @Override
    public RiskModelValidator<NCCNCoreRequest> getRiskModelValidator() {
        return nccnValidator;
    }
}
