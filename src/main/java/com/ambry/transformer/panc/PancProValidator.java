package com.ambry.transformer.panc;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.panc.Risk;

@Component
public class PancProValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk mmrRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.PANCPRO;
    }

}
