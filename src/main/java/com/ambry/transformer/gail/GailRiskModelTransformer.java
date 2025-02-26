package com.ambry.transformer.gail;

import org.springframework.stereotype.Service;

import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.RiskModelTransformer;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.objex.progeny.risk.gail.GailDelegate;
import com.objex.progeny.risk.services.ProgenyRiskService;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.nccn.NCCNResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;


@Service
public class GailRiskModelTransformer extends RiskModelTransformer implements RiskModelExecutor<com.objex.risk.beans.gail.Risk, GailResult, GailResult>{

    private final ProgenyRiskService gailExecuter;
    
    private final GailValidator gailValidator;
    
    public GailRiskModelTransformer(ProgenyRiskService gailExecuter, GailValidator gailValidator) {
        this.gailExecuter = gailExecuter;
        this.gailValidator = gailValidator;
    }
    
    @Override
    public ModelName getModelName() {
        return ModelName.GAIL;
    }

    @Override
    public com.objex.risk.beans.gail.Risk transformRequest(Risk commonRequest, Model model) {
        return (com.objex.risk.beans.gail.Risk) super.mapFamily(commonRequest);
    }

    @Override
    public GailResult invokeRiskModel(com.objex.risk.beans.gail.Risk gailRisk) {
        return gailExecuter.computeGail(gailRisk);
    }

    @Override
    public GailResult transformResponse(GailResult modelResponse) {
        return modelResponse;
    }

    @Override
    public RiskModelValidator<com.objex.risk.beans.gail.Risk> getRiskModelValidator() {
        return gailValidator;
    }

	@Override
	protected void add(BaseRisk fam, BasePerson basePerson) {
		((com.objex.risk.beans.gail.Risk)fam).add((com.objex.risk.beans.gail.Person)basePerson);
		
	}

	@Override
	protected void mapModelFields(Person person, BasePerson basePerson) {
		com.objex.risk.beans.gail.Person member = (com.objex.risk.beans.gail.Person) basePerson;
		Cancer breast = person.getCancer(CancerType.BREAST);
		if (breast != null) {
			if (breast.getAffected() == null) {
				member.setAffectedBreastType(AffectedType.NONE);
			} else {
				member.setAffectedBreastType(breast.getAffected());
			}
		}
		member.setBiopsyCount(person.getBiopsyCount());
		member.setHasDCIS(person.isHasDCIS());
		member.setHasHyperplasia(person.isHasHyperplasia());
		member.setHasLCIS(person.isHasLCIS());
		member.setHasLiveBirth(person.getHasLiveBirth());
		member.setHasMantleRadiation(person.isHasMantleRadiation());
		member.setLiveBirthAge(person.getLiveBirthAge());
		member.setHasMantleRadiation(person.isHasMantleRadiation());
		member.setMenarcheAge(person.getMenarcheAge());
		member.setPerformedBiopsy(person.getBiopsyCount() > 0);
		member.setRace(person.getRace());
	}

	@Override
	protected BaseRisk createRequestObject() {
		return new com.objex.risk.beans.gail.Risk();
	}

	@Override
	protected BasePerson createPersonObject() {
		return new com.objex.risk.beans.gail.Person();
	}

}
