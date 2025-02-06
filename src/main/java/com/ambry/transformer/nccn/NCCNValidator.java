package com.ambry.transformer.nccn;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.objex.risk.beans.uno.Model.ModelName;

@Component
public class NCCNValidator extends RiskModelValidator<NCCNCoreRequest>{

    @Override
    public void validate(NCCNCoreRequest request, ValidationErrors validationErros) {
        
    }

    @Override
    public ModelName getModelName() {
        return ModelName.NCCN;
    }

}
