package com.ambry.transformer.premm;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.premm.Risk;

@Component
public class PremmValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk premmRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.PREMM;
    }

}
