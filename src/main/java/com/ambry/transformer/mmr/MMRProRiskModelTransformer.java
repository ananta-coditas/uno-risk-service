package com.ambry.transformer.mmr;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.mmr.MMRResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.CancerDetails;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;
import com.objex.risk.services.RiskService;


@Service
public class MMRProRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.mmr.Risk, MMRResult, MMRResult>{

    private final RiskService bayesExecuter;
    
    private final MMRProValidator mmrValidator;
    
    public MMRProRiskModelTransformer(RiskService bayesExecuter, MMRProValidator mmrValidator) {
        this.bayesExecuter = bayesExecuter;
        this.mmrValidator = mmrValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.MMRPRO;
    }

    @Override
    public com.objex.risk.beans.mmr.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.mmr.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public MMRResult invokeRiskModel(com.objex.risk.beans.mmr.Risk mmrRisk)  {
        return bayesExecuter.calcMMR(mmrRisk);
    }

    @Override
    public MMRResult transformResponse(MMRResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.mmr.Risk> getRiskModelValidator() {
        return mmrValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.mmr.Risk)fam).add((com.objex.risk.beans.mmr.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.mmr.Person member = (com.objex.risk.beans.mmr.Person) basePerson;
		Cancer colon = person.getCancer(CancerType.COLORECTAL);
		if (colon != null) {
			if (colon.getAffected() == null) {
				member.setAffectedColonType(AffectedType.NONE.value);
			} else {
				member.setAffectedColonType(colon.getAffected());
				member.setDiagnoseColonAge(colon.getDiagnoseAge());
			}
		}
		Cancer endometrium = person.getCancer(CancerType.ENDOMETRIAL);
		if (endometrium != null) {
			if (endometrium.getAffected() == null) {
				member.setAffectedEndometriumType(AffectedType.NONE.value);
			} else {
				member.setAffectedEndometriumType(endometrium.getAffected());
				member.setDiagnoseEndometriumAge(endometrium.getDiagnoseAge());
			}
		}
		member.setMarker(null); //TODO

	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.mmr.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.mmr.Person();
	}
	protected void mapModelRiskInstruction(BaseRisk risk, String name, String value) {
		try {
			if ("impute".equals(name)) {
				((com.objex.risk.beans.mmr.Risk)risk).setImpute(Boolean.parseBoolean(value));
				((com.objex.risk.beans.mmr.Risk)risk).setImputeRelatives(Boolean.parseBoolean(value));
				return;
			}
		}
		catch (Exception e) {
			super.mapModelRiskInstruction(risk, name, value);
		}
		super.mapModelRiskInstruction(risk, name, value);
	}

}
