package com.ambry.transformer.mela;

import org.springframework.stereotype.Component;

import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.mela.Risk;

@Component
public class MelaProValidator extends RiskModelValidator<Risk>{

    @Override
    public void validate(Risk melaRisk, ValidationErrors validationErros) {
    }

    @Override
    public ModelName getModelName() {
        return ModelName.MELAPRO;
    }

}
