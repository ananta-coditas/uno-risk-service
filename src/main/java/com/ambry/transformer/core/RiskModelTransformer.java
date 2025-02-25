package com.ambry.transformer.core;

import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

/**
 * THis type does the basic mapping of the core value objects
 */

public abstract class RiskModelTransformer {
	protected BaseRisk mapFamily(Risk commonRisk) {
		BaseRisk fam = createRequestObject();
		fam.setSaveIncoming(commonRisk.isSaveIncoming());
		fam.setConsent(commonRisk.isConsent());
		fam.setTransId(commonRisk.getTransId());
		fam.setFamilyId(commonRisk.getFamilyId());
		for (Person person : commonRisk.getFamily())
		{
			BasePerson basePerson = createPersonObject();
			mapCommonFields(person,basePerson);
			mapModelFields(person,basePerson);
			add(fam,basePerson);
		}
		
		return fam;
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
