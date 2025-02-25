package com.ambry.transformer.claus;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.progeny.risk.claus.ClausDelegate;
import com.objex.progeny.risk.services.ProgenyRiskService;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;


@Service
public class ClausRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.claus.Risk, ClausResult, ClausResult>{

    private final ProgenyRiskService clausExecuter;
    
    private final ClausValidator clausValidator;
    
    public ClausRiskModelTransformer(ProgenyRiskService clausExecuter, ClausValidator clausValidator) {
        this.clausExecuter = clausExecuter;
        this.clausValidator = clausValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.CLAUS;
    }

    @Override
    public com.objex.risk.beans.claus.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.claus.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public ClausResult invokeRiskModel(com.objex.risk.beans.claus.Risk clausRisk) {
        return clausExecuter.computeClaus(clausRisk);
    }

    @Override
    public ClausResult transformResponse(ClausResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.claus.Risk> getRiskModelValidator() {
        return clausValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.claus.Risk)fam).add((com.objex.risk.beans.claus.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.claus.Person member = (com.objex.risk.beans.claus.Person) basePerson;
		Cancer breast = person.getCancer(CancerType.BREAST);
		if (breast != null) {
			if (breast.getAffected() == null) {
				member.setAffectedBreastType(AffectedType.NONE);
			} else {
				member.setAffectedBreastType(breast.getAffected());
				member.setDiagnoseBreastAge(breast.getDiagnoseAge());
			}
		}
		Cancer ovarian = person.getCancer(CancerType.OVARIAN);
		if (ovarian != null) {
			if (ovarian.getAffected() == null) {
				member.setAffectedOvarianType(AffectedType.NONE);
			} else {
				member.setAffectedOvarianType(ovarian.getAffected());
				member.setDiagnoseOvarianAge(ovarian.getDiagnoseAge());
			}
		}
		
	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.claus.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.claus.Person();
	}

}
