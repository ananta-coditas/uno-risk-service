package com.ambry.transformer.core;

import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * THis type does the basic mapping of the core value objects
 */

public abstract class RiskModelTransformer {
	private static final Logger LOG = LoggerFactory.getLogger(RiskModelTransformer.class);
	protected BaseRisk mapFamily(Risk commonRisk) {
		BaseRisk fam = createRequestObject();
		ModelName modelName = ModelName.valueOf(fam.getModel());
		Model model = commonRisk.getRequestedModel(modelName);
		fam.setAgeBy(model.getAgeBy());
		fam.setAgeTo(model.getAgeTo());
		fam.setChartCode(model.getChartCode());
		mapModelRiskFields(commonRisk,fam,model);
		for (Person person : commonRisk.getFamily())
		{
			BasePerson basePerson = createPersonObject();
			mapCommonFields(person,basePerson);
			mapModelFields(person,basePerson);
			add(fam,basePerson);
		}
		
		return fam;
	}
    /**
     * this setups the base fields from model and risk and should be overridden for model specific fields
     * @param commonRisk
     * @param fam
     * @param model
     */
	protected void mapModelRiskFields(Risk commonRisk, BaseRisk fam, Model model) {
		fam.setSaveIncoming(commonRisk.isSaveIncoming());
		fam.setConsent(commonRisk.isConsent());
		fam.setTransId(commonRisk.getTransId());
		fam.setFamilyId(commonRisk.getFamilyId());
		for (Instruction ins : model.getInstructions()) {
			mapModelRiskInstruction(fam,ins.getName(), ins.getValue());
		}
	}

	protected void mapModelRiskInstruction(BaseRisk fam, String name, String value) {
	    LOG.info("Instruction " + "name:" + name + " value" + value + " may not be mapped correctly");   
	
	}
	protected abstract void add(BaseRisk fam, BasePerson basePerson);

	protected abstract void mapModelFields(Person person, BasePerson basePerson);
    /**
     * maps values from unoPerson to modelPerson
     * @param person
     * @param basePerson
     */
	private void mapCommonFields(Person person, BasePerson basePerson) {
		basePerson.setId(person.getId());
		basePerson.setMotherId(person.getMotherId());
		basePerson.setFatherId(person.getFatherId());
		basePerson.setAge(person.getAge());
		basePerson.setAgeOfDeath(person.getAgeOfDeath());
		basePerson.setGender(person.getGender());
		basePerson.setTransId(person.getTransId());
		basePerson.setTestOrder(person.getTestOrder());
		basePerson.setTwin(person.getTwin());
		basePerson.setVitalStatus(person.getVitalStatus());
		for (String germLine : person.getGermline().keySet()) {
			if (basePerson.getGermline().containsKey(germLine))
				basePerson.setGermline(germLine, person.getGermline(germLine));
		}
	}
    /**
     * creates appropriate model specific request
     * @return
     */
	protected abstract BaseRisk createRequestObject();
	/**
	 * creates appropriate model specific person
	 * @return
	 */
	protected abstract BasePerson createPersonObject();
		
}
