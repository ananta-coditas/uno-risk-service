package com.ambry.transformer.premm;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.progeny.risk.premm.PremmDelegate;
import com.objex.progeny.risk.services.ProgenyRiskService;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;


@Service
public class PremmRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.premm.Risk, PREMMResult,PREMMResult>{

    private final ProgenyRiskService premmExecuter;
    
    private final PremmValidator premmValidator;
    
    public PremmRiskModelTransformer(ProgenyRiskService premmExecuter, PremmValidator premmValidator) {
        this.premmExecuter = premmExecuter;
        this.premmValidator = premmValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.PREMM;
    }

    @Override
    public com.objex.risk.beans.premm.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.premm.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public PREMMResult invokeRiskModel(com.objex.risk.beans.premm.Risk premmRisk) {
        return premmExecuter.computePREMM(premmRisk);
    }

    @Override
    public PREMMResult transformResponse(PREMMResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.premm.Risk> getRiskModelValidator() {
        return premmValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.premm.Risk)fam).add((com.objex.risk.beans.premm.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.premm.Person member = (com.objex.risk.beans.premm.Person) basePerson;
		Cancer colon = person.getCancer(CancerType.COLORECTAL);
		if (colon != null) {
			if (colon.getAffected() == null) {
				member.setAffectedColonType(AffectedType.NONE);
			} else {
				member.setAffectedColonType(colon.getAffected());
				member.setDiagnoseColonAge(colon.getDiagnoseAge());
			}
		}
		Cancer endometrial = person.getCancer(CancerType.ENDOMETRIAL);
		if (endometrial != null) {
			if (endometrial.getAffected() == null) {
				member.setAffectedEndometriumType(AffectedType.NONE);
			} else {
				member.setAffectedEndometriumType(endometrial.getAffected());
				member.setDiagnoseEndometriumAge(endometrial.getDiagnoseAge());
			}
		}
		
		Cancer premmRelatedCancer = person.getCancer(CancerType.PREMMRELATED); //TODO can we get specifics?
		if (premmRelatedCancer != null) {
			if (premmRelatedCancer.getAffected() == null) {
				member.setAffectedRelatedType(AffectedType.NONE);
			} else {
				member.setAffectedRelatedType(premmRelatedCancer.getAffected());
			}
		}
		
	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.premm.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.premm.Person();
	}

}
