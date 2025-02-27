package com.ambry.transformer.tyrer;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.tcuzick.Risk;

@Component
public class TyrerValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk tyrerRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.TYRER;
    }

}
