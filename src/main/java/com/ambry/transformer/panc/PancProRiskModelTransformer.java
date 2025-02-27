package com.ambry.transformer.panc;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.panc.PancResult;
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
public class PancProRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.panc.Risk, PancResult, PancResult>{

    private final RiskService bayesExecuter;
    
    private final PancProValidator pancValidator;
    
    public PancProRiskModelTransformer(RiskService bayesExecuter, PancProValidator pancValidator) {
        this.bayesExecuter = bayesExecuter;
        this.pancValidator = pancValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.PANCPRO;
    }

    @Override
    public com.objex.risk.beans.panc.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.panc.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public PancResult invokeRiskModel(com.objex.risk.beans.panc.Risk pancRisk)  {
        return bayesExecuter.calcPanc(pancRisk);
    }

    @Override
    public PancResult transformResponse(PancResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.panc.Risk> getRiskModelValidator() {
        return pancValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.panc.Risk)fam).add((com.objex.risk.beans.panc.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.panc.Person member = (com.objex.risk.beans.panc.Person) basePerson;
		Cancer pancreas = person.getCancer(CancerType.PANCREATIC);
		if (pancreas != null) {
			if (pancreas.getAffected() == null) {
				member.setAffectedPancreasType(AffectedType.NONE.value);
			} else {
				member.setAffectedPancreasType(pancreas.getAffected());
				member.setDiagnosePancreasAge(pancreas.getDiagnoseAge());
			}
		}


	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.panc.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.panc.Person();
		
	}
	
	protected void mapModelRiskInstruction(BaseRisk risk, String name, String value) {
		try {
			if ("impute".equals(name)) {
				((com.objex.risk.beans.panc.Risk)risk).setImpute(Boolean.parseBoolean(value));
				((com.objex.risk.beans.panc.Risk)risk).setImputeRelatives(Boolean.parseBoolean(value));
				return;
			}
		}
		catch (Exception e) {
			super.mapModelRiskInstruction(risk, name, value);
		}
		super.mapModelRiskInstruction(risk, name, value);
	}


}
