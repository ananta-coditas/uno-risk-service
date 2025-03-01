package com.ambry.transformer.mmr;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.mmr.Risk;

@Component
public class MMRProValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk mmrRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.MMRPRO;
    }

}
