package com.ambry.transformer.brca;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.brca.Risk;

@Component
public class BRCAProValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk brcaRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.BRCAPRO;
    }

}
