package com.ambry.transformer.nccn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.genomebrain.backend.service.dto.CancerFeatureDetails;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.PolypDetails;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Person;

@Component
public class NCCNClinicalHistoryMapper {

    private static final int GENDER_FEMALE = 0;

    private static final int GENDER_MALE = 1;

    private static final Set<Cancer.CancerType> CLINICAL_FEATURE_TYPES = EnumSet.of(
        Cancer.CancerType.DESMOIDS, Cancer.CancerType.SEBACEOUS_ADENOMA, Cancer.CancerType.SEBACEOUS_ADENOMAS);

    public ClinicalHistory transform(Person person,
            Map<Integer, NCCNPersonRelationShipDetails> familyRelativesRelationshipDetails) {
        ClinicalHistory clinicalHistory = new ClinicalHistory();
        NCCNPersonRelationShipDetails nccnPersonRelationShipDetails = familyRelativesRelationshipDetails
                .get(person.getId());
        if (nccnPersonRelationShipDetails == null) {
            nccnPersonRelationShipDetails = new NCCNPersonRelationShipDetails(
                NCCNRiskModelUtils.UNCLASSIFIED_RELATIVE, NCCNRiskModelUtils.OTHER);
        }
        clinicalHistory.setRelationship(nccnPersonRelationShipDetails.getRelationShip());
        clinicalHistory.setSideOfFam(nccnPersonRelationShipDetails.getFamilySide());
        clinicalHistory.setSexAtBirth(getSexAtBirth(person.getGender()));
        clinicalHistory.setEthnicity(List.of(person.getFamilyRace().toString().toUpperCase()));
        clinicalHistory.setEthnicityOther(person.getFamilyRace().toString());
        Person.NCCNEnum polypOverall = person.getPolypOverall() != null ? person.getPolypOverall() : Person.NCCNEnum.NOT_PROVIDED;
        clinicalHistory.setPolypOverall(polypOverall.name());
        if (polypOverall == Person.NCCNEnum.YES && person.getPolypDetails() != null) {
            clinicalHistory.setPolypDetails(getPolypListDetails(person.getPolypDetails()));
        }
        clinicalHistory
                .setCancerOverall((person.getCancers() != null && !person.getCancers().isEmpty()) ? "Yes" : "No");
        clinicalHistory.setClinicalFeatureOverall(hasClinicalFeature(person) ? "Yes" : "No");

        if (person.getCancers() != null) {
            for (Cancer cancer : person.getCancers()) {
                CancerFeatureDetails details = new CancerFeatureDetails();
                details.setCancerFeature(cancer.getCancerType().toString());
                details.setCancerFeatureType("cancer");
                //details.setExactDxAge(String.valueOf(cancer.getDiagnoseAge() + 0.5));
                details.setExactDxAge(String.valueOf(cancer.getDiagnoseAge()));
                if (cancer.getCancerDetails() != null) {
                    details.setMetastatic(cancer.getCancerDetails().getMetastatic());
                    details.setMultiplePrimaries(cancer.getCancerDetails().getMultiplePrimaries());
                    if (cancer.getCancerDetails().getBreastCancerTypes() != null) {
                        List<String> cancerTypes = cancer.getCancerDetails().getBreastCancerTypes().stream().map(Enum::name)
                            .collect(Collectors.toList());
                        details.setBreastCancerTypes(cancerTypes);
                    }
                }
                clinicalHistory.getCancerFeatureDetails().add(details);
            }
        }
        return clinicalHistory;
    }

    private List<PolypDetails> getPolypListDetails(com.objex.risk.beans.uno.PolypDetails polypDetails) {
        List<PolypDetails> corePloypDetails = new ArrayList<>();
        PolypDetails corePolypObject = new PolypDetails();
        corePolypObject.setExactNumOfPolyps(String.valueOf(polypDetails.getNumOfColorectalPolyps()));
        corePolypObject.setOtherNumOfPolyps(polypDetails.getOtherNumOfPolyps());
        corePolypObject.setPolypType(polypDetails.getPolypType());
        corePloypDetails.add(corePolypObject);
        return corePloypDetails;
    }

    /**
     * Maps the UNO gender code (0 = female, 1 = male, as used by the other risk models) to the
     * NCCN sex-at-birth value; any other code is reported as unknown rather than guessed.
     */
    private String getSexAtBirth(Integer gender) {
        if (Objects.equals(gender, GENDER_MALE)) {
            return "male";
        }
        if (Objects.equals(gender, GENDER_FEMALE)) {
            return "female";
        }
        return "unknown";
    }

    /** Desmoid tumours and sebaceous adenomas are NCCN clinical features, reported through UNO as cancer types. */
    private boolean hasClinicalFeature(Person person) {
        return person.getCancers() != null
            && person.getCancers().stream().map(Cancer::getCancerType).anyMatch(CLINICAL_FEATURE_TYPES::contains);
    }
}
