package com.ambry.transformer.claus;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.claus.Risk;

@Component
public class ClausValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk clausRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.CLAUS;
    }

}
