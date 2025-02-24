package com.ambry.transformer.nccn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objex.progeny.risk.Utils;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

public class NCCNRiskModelUtils {

    final static BasePerson NONE = new BasePerson() {

        @Override
        protected List<String> germLines() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    static {
        NONE.setFatherId(-1);
        NONE.setMotherId(-1);
    }

    public static Map<Integer, NCCNPersonRelationShipDetails> calculateRelationShipForAllFamilyMemeber(Risk risk) {
        Person proband = null;
        Map<Integer, NCCNPersonRelationShipDetails> relativesIdsAndRelationShip = new HashMap<>();
        for (Person person : risk.getFamily()) {
            if (person.isProband()) {
                proband = person;
                relativesIdsAndRelationShip.put(proband.getId(), new NCCNPersonRelationShipDetails("PROBAND", "OTHER"));
                break;
            }
        }
        
        // Calculate for the First Degree Relatives
        relativesIdsAndRelationShip.putAll(calculateRelationShipForFirstDegreeRelatives(risk, proband));
        // Calculate for the second Degree Relatives for meternal side
        relativesIdsAndRelationShip.putAll(calculateRelationShipForSecondDegreeRelatives(risk, proband, true));
        // Calculate for the First Degree Relatives
        relativesIdsAndRelationShip.putAll(calculateRelationShipForSecondDegreeRelatives(risk, proband, false));
        // For remaining Relatives like ..FIRST_COUSIN_MALE & FEMALE, GREAT_GRAND parents, GREAT_AUNT, GREAT_UNCLE, GREAT_NIECE, GREAT_NEPHEW, 
        // GREAT_GRANDDAUGHTER, GREAT_GRANDSON,HALF_AUNT,HALF_UNCLE,HALF_NIECE,HALF_NEPHEW,OTHER
        return relativesIdsAndRelationShip;
    }

    private static Map<Integer, NCCNPersonRelationShipDetails> calculateRelationShipForFirstDegreeRelatives(Risk risk, Person proband) {
        Map<Integer, NCCNPersonRelationShipDetails> relativesIdsAndRelationShip = new HashMap<>();
        for (BasePerson relative : risk.getFamily()) {
            if (proband == relative) {
                continue;
            }
            if (relative.isFather(proband)) {
                relativesIdsAndRelationShip.put(relative.getId(), new NCCNPersonRelationShipDetails("FATHER", "OTHER"));
            }
            if (relative.isMother(proband)) {
                relativesIdsAndRelationShip.put(relative.getId(), new NCCNPersonRelationShipDetails("MOTHER", "OTHER"));
            }
            if (relative.isSibling(proband)) {
                relativesIdsAndRelationShip.put(relative.getId(), new NCCNPersonRelationShipDetails(relative.getGender() == 0 ? "SISTER" : "BROTHER", "OTHER"));
            }
            if (relative.isChild(proband)) {
                relativesIdsAndRelationShip.put(relative.getId(), new NCCNPersonRelationShipDetails(relative.getGender() == 0 ? "DAUGHTER" : "SON", "OTHER"));
            }
        }
        return relativesIdsAndRelationShip;
    }

    private static Map<Integer, NCCNPersonRelationShipDetails> calculateRelationShipForSecondDegreeRelatives(Risk risk, Person proband,
            boolean maternal) {
        Map<Integer, NCCNPersonRelationShipDetails> relativesIdsAndRelationShip = new HashMap<>();
        BasePerson father = NONE;
        BasePerson mother = NONE;
        BasePerson parent = NONE;
        Integer parentId = 0;
        ArrayList<Integer> children = new ArrayList();
        ArrayList<Integer> siblings = new ArrayList();
        List<BasePerson> relatives = new ArrayList();
        for (BasePerson person : risk.getFamily()) {
            if (proband == person) {
                continue;
            }
            if (person.isMother(proband)) {
                mother = person;
            } else if (person.isFather(proband)) {
                father = person;
            } else if (proband.isParent(person)) {
                children.add(person.getId());
            } else if (proband.isSibling(person)) {
                siblings.add(person.getId());
            }
        }

        parent = (maternal ? mother : father);
        if (parent != null) {
            parentId = parent.getId();
        }

        for (BasePerson person : risk.getFamily()) {
            if (proband == person) {
                continue;
            }
            if (person.isParent(parent)) {
                relativesIdsAndRelationShip.put(person.getId(), new NCCNPersonRelationShipDetails(person.getGender() == 0 ? "GRANDMOTHER" : "GRANDFATHER", "MATERNAL"));
            } else if (person.isSibling(parent)) {
                relativesIdsAndRelationShip.put(person.getId(), new NCCNPersonRelationShipDetails(person.getGender() == 0 ? "AUNT" : "UNCLE", "MATERNAL"));
            } else if (children.contains(person.getMotherId()) || children.contains(person.getFatherId())) {
                relativesIdsAndRelationShip.put(person.getId(), new NCCNPersonRelationShipDetails(person.getGender() == 0 ? "GRANDDAUGHTER" : "GRANDSON", "MATERNAL"));
            } else if (siblings.contains(person.getMotherId()) || siblings.contains(person.getFatherId())) {
                relativesIdsAndRelationShip.put(person.getId(), new NCCNPersonRelationShipDetails(person.getGender() == 0 ? "NIECE" : "NEPHEW", "MATERNAL"));
            } else if (!siblings.contains(person.getId()) && ((parentId != 0 && parentId.equals(person.getMotherId()))
                    || (parentId.equals(person.getFatherId())))) {
                relativesIdsAndRelationShip.put(person.getId(), new NCCNPersonRelationShipDetails(person.getGender() == 0 ? "HALF_SISTER" : "HALF_BROTHER", "MATERNAL"));
            }
        }
        return relativesIdsAndRelationShip;
    }
}

class NCCNPersonRelationShipDetails {
    String relationShip;
    String familySide;

    public NCCNPersonRelationShipDetails(String relationShip, String familySide) {
        super();
        this.relationShip = relationShip;
        this.familySide = familySide;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public String getFamilySide() {
        return familySide;
    }
}
