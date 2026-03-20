package com.ambry.transformer.tyrer;

import com.ambry.transformer.core.RiskModelExecutor;
import com.objex.progeny.risk.services.ProgenyRiskService;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.tcuzick.TyrerResult;
import com.objex.risk.beans.uno.Model.ModelName;
import org.springframework.stereotype.Service;


@Service
public class CompetingMortalityTransformer extends TyrerRiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.tcuzick.Risk, TyrerResult, TyrerResult>{


    public CompetingMortalityTransformer(ProgenyRiskService tyrerExecuter, TyrerValidator tyrerValidator) {
        super(tyrerExecuter, tyrerValidator);
    }

    /**
     * this sets competing mortality to true and disregards the instructions.
     * @param risk
     * @param name
     * @param value
     */
    protected void mapModelRiskInstruction(BaseRisk risk, String name, String value) {
        super.mapModelRiskInstruction(risk, name, value);
        ((com.objex.risk.beans.tcuzick.Risk)risk).setCompetingMortality(true); //can't override
    }

    @Override
    public ModelName getModelName() {
        return ModelName.TYRER_COMPETING_MORTALITY;
    }
}
