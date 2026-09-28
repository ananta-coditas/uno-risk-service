package com.ambry.transformer.nccn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.objex.risk.beans.uno.Person;
import com.objex.risk.beans.uno.Risk;

/**
 * Derives, for every family member, the NCCN relationship to the proband and the side of the family
 * it sits on, using the relationship codes accepted by {@code NCCNExecuter} (e.g. {@code MOTHER},
 * {@code HALF_SISTER}, {@code FIRST_COUSIN_MALE}, {@code GREAT_AUNT}).
 * <p>
 * The side of the family is only meaningful for relatives reached through one of the proband's parents
 * (grandparents, aunts/uncles, cousins, half-siblings, ...). First-degree relatives and descendants are
 * reported as {@code OTHER}, as are relatives that cannot be placed.
 */
public class NCCNRiskModelUtils {

    static final String MATERNAL = "MATERNAL";
    static final String PATERNAL = "PATERNAL";
    static final String OTHER = "OTHER";
    static final String PROBAND = "PROBAND";
    static final String UNCLASSIFIED_RELATIVE = "OTHERS";

    private static final int FEMALE = 0;

    private NCCNRiskModelUtils() {
    }

    /**
     * Calculates the relationship details of every member of the family relative to the proband.
     *
     * @param risk the request whose family is described
     * @return relationship details keyed by person id; members that cannot be classified map to {@code OTHERS}
     */
    public static Map<Integer, NCCNPersonRelationShipDetails> calculateRelationShipForAllFamilyMemeber(Risk risk) {
        List<Person> family = risk.getFamily();
        if (family == null || family.isEmpty()) {
            return new HashMap<>();
        }
        Person proband = family.stream().filter(Person::isProband).findFirst().orElse(family.get(0));
        Pedigree pedigree = new Pedigree(family, proband);

        Map<Integer, NCCNPersonRelationShipDetails> relationships = new HashMap<>();
        for (Person person : family) {
            relationships.put(person.getId(), person == proband
                ? new NCCNPersonRelationShipDetails(PROBAND, OTHER)
                : pedigree.classify(person));
        }
        return relationships;
    }

    /** Pedigree lookups relative to a fixed proband. */
    private static final class Pedigree {

        private final Map<Integer, Person> peopleById = new LinkedHashMap<>();
        private final Person proband;

        Pedigree(List<Person> family, Person proband) {
            family.forEach(person -> peopleById.put(person.getId(), person));
            this.proband = proband;
        }

        NCCNPersonRelationShipDetails classify(Person relative) {
            NCCNPersonRelationShipDetails details = classifyFirstDegree(relative);
            if (details == null) {
                details = classifySecondDegree(relative);
            }
            if (details == null) {
                details = classifyThirdDegree(relative);
            }
            return details != null ? details : new NCCNPersonRelationShipDetails(UNCLASSIFIED_RELATIVE, OTHER);
        }

        private NCCNPersonRelationShipDetails classifyFirstDegree(Person relative) {
            if (isParentOf(relative, proband)) {
                return details(relative, "MOTHER", "FATHER", OTHER);
            }
            if (isFullSibling(relative, proband)) {
                return details(relative, "SISTER", "BROTHER", OTHER);
            }
            if (isParentOf(proband, relative)) {
                return details(relative, "DAUGHTER", "SON", OTHER);
            }
            return null;
        }

        private NCCNPersonRelationShipDetails classifySecondDegree(Person relative) {
            for (Person parent : parentsOf(proband)) {
                String side = sideThrough(parent);
                if (isParentOf(relative, parent)) {
                    return details(relative, "GRANDMOTHER", "GRANDFATHER", side);
                }
                if (isFullSibling(relative, parent)) {
                    return details(relative, "AUNT", "UNCLE", side);
                }
            }
            String halfSiblingSide = halfSiblingSide(relative, proband);
            if (halfSiblingSide != null) {
                return details(relative, "HALF_SISTER", "HALF_BROTHER", halfSiblingSide);
            }
            for (Person parent : parentsOf(relative)) {
                if (isFullSibling(parent, proband)) {
                    return details(relative, "NIECE", "NEPHEW", OTHER);
                }
                if (isParentOf(proband, parent)) {
                    return details(relative, "GRANDDAUGHTER", "GRANDSON", OTHER);
                }
            }
            return null;
        }

        private NCCNPersonRelationShipDetails classifyThirdDegree(Person relative) {
            for (Person parent : parentsOf(proband)) {
                String side = sideThrough(parent);
                if (halfSiblingSide(relative, parent) != null) {
                    return details(relative, "HALF_AUNT", "HALF_UNCLE", side);
                }
                for (Person grandparent : parentsOf(parent)) {
                    if (isParentOf(relative, grandparent)) {
                        return details(relative, "GREAT_GRANDMOTHER", "GREAT_GRANDFATHER", side);
                    }
                    if (isFullSibling(relative, grandparent)) {
                        return details(relative, "GREAT_AUNT", "GREAT_UNCLE", side);
                    }
                }
                for (Person relativeParent : parentsOf(relative)) {
                    if (isFullSibling(relativeParent, parent)) {
                        return details(relative, "FIRST_COUSIN_FEMALE", "FIRST_COUSIN_MALE", side);
                    }
                }
            }
            for (Person relativeParent : parentsOf(relative)) {
                String halfSiblingSide = halfSiblingSide(relativeParent, proband);
                if (halfSiblingSide != null) {
                    return details(relative, "HALF_NIECE", "HALF_NEPHEW", halfSiblingSide);
                }
                for (Person grandparent : parentsOf(relativeParent)) {
                    if (isFullSibling(grandparent, proband)) {
                        return details(relative, "GREAT_NIECE", "GREAT_NEPHEW", OTHER);
                    }
                    if (isParentOf(proband, grandparent)) {
                        return details(relative, "GREAT_GRANDDAUGHTER", "GREAT_GRANDSON", OTHER);
                    }
                }
            }
            return null;
        }

        /** Side of the family reached through the given parent of the proband. */
        private String sideThrough(Person probandParent) {
            return isSameId(probandParent.getId(), proband.getMotherId()) ? MATERNAL : PATERNAL;
        }

        /**
         * Returns the side of the family two people are half-siblings on (the side of their one shared
         * parent, relative to the proband), or null when they are not half-siblings.
         */
        private String halfSiblingSide(Person first, Person second) {
            if (first == second) {
                return null;
            }
            boolean sameMother = isKnownAndSame(first.getMotherId(), second.getMotherId());
            boolean sameFather = isKnownAndSame(first.getFatherId(), second.getFatherId());
            if (sameMother == sameFather) {
                return null;
            }
            if (second != proband) {
                return OTHER;
            }
            return sameMother ? MATERNAL : PATERNAL;
        }

        private List<Person> parentsOf(Person person) {
            List<Person> parents = new ArrayList<>(2);
            addIfPresent(parents, person.getMotherId());
            addIfPresent(parents, person.getFatherId());
            return parents;
        }

        private void addIfPresent(List<Person> people, Integer id) {
            Person person = id != null ? peopleById.get(id) : null;
            if (person != null) {
                people.add(person);
            }
        }
    }

    private static NCCNPersonRelationShipDetails details(Person relative, String femaleTerm, String maleTerm, String side) {
        return new NCCNPersonRelationShipDetails(isFemale(relative) ? femaleTerm : maleTerm, side);
    }

    private static boolean isFemale(Person person) {
        return Objects.equals(person.getGender(), FEMALE);
    }

    private static boolean isParentOf(Person parent, Person child) {
        return isSameId(parent.getId(), child.getMotherId()) || isSameId(parent.getId(), child.getFatherId());
    }

    private static boolean isFullSibling(Person first, Person second) {
        return first != second
            && isKnownAndSame(first.getMotherId(), second.getMotherId())
            && isKnownAndSame(first.getFatherId(), second.getFatherId());
    }

    private static boolean isKnownAndSame(Integer firstId, Integer secondId) {
        return firstId != null && firstId > 0 && firstId.equals(secondId);
    }

    private static boolean isSameId(Integer id, Integer otherId) {
        return id != null && id > 0 && id.equals(otherId);
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
