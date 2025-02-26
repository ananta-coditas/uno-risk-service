package com.ambry.transformer.gail;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.gail.Risk;

@Component
public class GailValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk clausRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.GAIL;
    }

}
