package com.ambry.transformer.brca;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.brca.BRCAResult;
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
public class BRCAProRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.brca.Risk, BRCAResult, BRCAResult>{

    private final RiskService bayesExecuter;
    
    private final BRCAProValidator brcaValidator;
    
    public BRCAProRiskModelTransformer(RiskService bayesExecuter, BRCAProValidator brcaValidator) {
        this.bayesExecuter = bayesExecuter;
        this.brcaValidator = brcaValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.BRCAPRO;
    }

    @Override
    public com.objex.risk.beans.brca.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.brca.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public BRCAResult invokeRiskModel(com.objex.risk.beans.brca.Risk brcaRisk)  {
        return bayesExecuter.calcBRCA(brcaRisk);
    }

    @Override
    public BRCAResult transformResponse(BRCAResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.brca.Risk> getRiskModelValidator() {
        return brcaValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.brca.Risk)fam).add((com.objex.risk.beans.brca.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.brca.Person member = (com.objex.risk.beans.brca.Person) basePerson;
		Cancer breast = person.getCancer(CancerType.BREAST);
		if (breast != null) {
			if (breast.getAffected() == null) {
				member.setAffectedBreastType(AffectedType.NONE.value);
			} else {
				member.setAffectedBreastType(breast.getAffected());
				member.setDiagnoseBreastAge(breast.getDiagnoseAge());
			}
			CancerDetails details = breast.getCancerDetails();
			if (details != null)
			{
			    member.setDiagnoseBreastContralateralAge(details.getContralateralAge());
			    if (details.getContralateral() != null && details.getContralateral().value > 0) {
			    	member.setAffectedBreastType(AffectedType.MULTIPLE.value);
			    }
			}
		}
		Cancer ovarian = person.getCancer(CancerType.OVARIAN);
		if (ovarian != null) {
			if (ovarian.getAffected() == null) {
				member.setAffectedOvarianType(AffectedType.NONE.value);
			} else {
				member.setAffectedOvarianType(ovarian.getAffected());
				member.setDiagnoseOvarianAge(ovarian.getDiagnoseAge());
			}
		}
		member.setAgeMasectomy(person.getAgeMasectomy());
		member.setAgeOophorectomy(person.getAgeOophorectomy());
	    //member.setEthnic(person.getEthnic());
	    member.setPerformedMasectomy(person.isPerformedMasectomy());
	    member.setPerformedOophorectomy(person.isPerformedOophorectomy());
		//member.setMarker();

	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.brca.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.brca.Person();
	}
	
	protected void mapModelRiskInstruction(BaseRisk risk, String name, String value) {
		try {
			if ("impute".equals(name)) {
				((com.objex.risk.beans.brca.Risk)risk).setImpute(Boolean.parseBoolean(value));
				((com.objex.risk.beans.brca.Risk)risk).setImputeRelatives(Boolean.parseBoolean(value));
				return;
			}
		}
		catch (Exception e) {
			super.mapModelRiskInstruction(risk, name, value);
		}
		super.mapModelRiskInstruction(risk, name, value);
	}

}
