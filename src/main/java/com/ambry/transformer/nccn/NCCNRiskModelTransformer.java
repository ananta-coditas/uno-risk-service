package com.ambry.transformer.nccn;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.genomebrain.backend.service.NCCNExecuter;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Risk;

@Service
public class NCCNRiskModelTransformer implements RiskModelExecutor<NCCNCoreRequest, NCCNCoreResponse, NCCNResult>{

    private final NCCNExecuter nccnExecuter;
    
    private final NCCNValidator nccnValidator;
    
    public NCCNRiskModelTransformer(NCCNExecuter nccnExecuter, NCCNValidator nccnValidator) {
        this.nccnExecuter = nccnExecuter;
        this.nccnValidator = nccnValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.NCCN;
    }

    @Override
    public NCCNCoreRequest transformRequest(Risk commonRequest, Model model) {
        return null;
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
