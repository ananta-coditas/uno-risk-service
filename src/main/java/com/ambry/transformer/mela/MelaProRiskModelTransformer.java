package com.ambry.transformer.mela;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.mela.MelaResult;
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
public class MelaProRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.mela.Risk, MelaResult, MelaResult>{

    private final RiskService bayesExecuter;
    
    private final MelaProValidator melaValidator;
    
    public MelaProRiskModelTransformer(RiskService bayesExecuter, MelaProValidator melaValidator) {
        this.bayesExecuter = bayesExecuter;
        this.melaValidator = melaValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.MELAPRO;
    }

    @Override
    public com.objex.risk.beans.mela.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.mela.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public MelaResult invokeRiskModel(com.objex.risk.beans.mela.Risk melaRisk)  {
        return bayesExecuter.calcMela(melaRisk);
    }

    @Override
    public MelaResult transformResponse(MelaResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.mela.Risk> getRiskModelValidator() {
        return melaValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.mela.Risk)fam).add((com.objex.risk.beans.mela.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.mela.Person member = (com.objex.risk.beans.mela.Person) basePerson;
		Cancer melanoma = person.getCancer(CancerType.SKIN);
		if (melanoma != null) {
			if (melanoma.getAffected() == null) {
				member.setAffectedSkinType(AffectedType.NONE.value);
			} else {
				member.setAffectedSkinType(melanoma.getAffected());
				member.setDiagnoseSkinAge(melanoma.getDiagnoseAge());
			}
		}


	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.mela.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.mela.Person();
	}
	
		protected void mapModelRiskInstruction(BaseRisk risk, String name, String value) {
		try {
			if ("impute".equals(name)) {
				((com.objex.risk.beans.mela.Risk)risk).setImpute(Boolean.parseBoolean(value));
				((com.objex.risk.beans.mela.Risk)risk).setImputeRelatives(Boolean.parseBoolean(value));
				return;
			}
		}
		catch (Exception e) {
			super.mapModelRiskInstruction(risk, name, value);
		}
		super.mapModelRiskInstruction(risk, name, value);
	}

}
