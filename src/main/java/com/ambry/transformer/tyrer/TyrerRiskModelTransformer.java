package com.ambry.transformer.tyrer;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.progeny.risk.services.ProgenyRiskService;
import com.objex.progeny.risk.tycuz.TyrerDelegate;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.tcuzick.TyrerResult;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.CancerDetails;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;


@Service
public class TyrerRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.tcuzick.Risk, TyrerResult, TyrerResult>{

    private final ProgenyRiskService tyrerExecuter;
    
    private final TyrerValidator tyrerValidator;
    
    public TyrerRiskModelTransformer(ProgenyRiskService tyrerExecuter, TyrerValidator tyrerValidator) {
        this.tyrerExecuter = tyrerExecuter;
        this.tyrerValidator = tyrerValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.TYRER;
    }

    @Override
    public com.objex.risk.beans.tcuzick.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.tcuzick.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public TyrerResult invokeRiskModel(com.objex.risk.beans.tcuzick.Risk tyrerRisk)  {
        return tyrerExecuter.computeTyrer(tyrerRisk);
    }

    @Override
    public TyrerResult transformResponse(TyrerResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.tcuzick.Risk> getRiskModelValidator() {
        return tyrerValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.tcuzick.Risk)fam).add((com.objex.risk.beans.tcuzick.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.tcuzick.Person member = (com.objex.risk.beans.tcuzick.Person) basePerson;
		Cancer breast = person.getCancer(CancerType.BREAST);
		if (breast != null) {
			if (breast.getAffected() == null) {
				member.setAffectedBreastType(AffectedType.NONE);
			} else {
				member.setAffectedBreastType(breast.getAffected());
				member.setDiagnoseBreastAge(breast.getDiagnoseAge());
			}
			CancerDetails details = breast.getCancerDetails();
			if (details != null)
			{
				member.setAffectedBilateralBreastType(details.getBilateral());
				member.setDiagnoseBilateralBreastAge(breast.getDiagnoseAge());
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
		
		member.setDensityMeasure(person.getDensityMeasure());
		member.setDensityValue(person.getDensityValue());
		//if (person.getEthnic() == com.objex.risk.beans.brca.Person.Ethnic.AJ) {
		//	member.setEthnic(com.objex.risk.beans.tcuzick.Person.Ethnic.AJ);
		//} else {
		//	member.setEthnic(com.objex.risk.beans.tcuzick.Person.Ethnic.nonAJ);
		//}
		member.setHasAtypicalHyperplasia(person.isHasAtypicalHyperplasia());
		member.setHasLCIS(person.isHasLCIS());
		member.setHasLiveBirth(person.getHasLiveBirth());
		member.setHeight(person.getHeight());
		member.setHrtType(person.getHrtType());
		member.setHyperplasia(member.getHyperplasia());
		member.setLiveBirthAge(person.getLiveBirthAge());
		member.setMenarcheAge(person.getMenarcheAge());
		member.setMenopauseAge(person.getMenopauseAge());
		member.setMenostat(person.getMenostat());
		member.setPolygenicSNP(person.getPolygenicSNP());
		member.setUsedHRT(person.getUsedHRT());
		member.setWeight(person.getWeight());
		member.setYearsFutureHRT(person.getYearsFutureHRT());
		member.setYearsPastHRT(person.getYearsPastHRT());
		member.setYearsSinceHRT(person.getYearsSinceHRT());
	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.tcuzick.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.tcuzick.Person();
	}

}
