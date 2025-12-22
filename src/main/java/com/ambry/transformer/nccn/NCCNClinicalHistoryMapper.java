package com.ambry.transformer.nccn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.genomebrain.backend.service.dto.CancerFeatureDetails;
import com.genomebrain.backend.service.dto.ClinicalHistory;
import com.genomebrain.backend.service.dto.PolypDetails;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Person;

@Component
public class NCCNClinicalHistoryMapper {

    public ClinicalHistory transform(Person person,
            Map<Integer, NCCNPersonRelationShipDetails> familyRelativesRelationshipDetails) {
        ClinicalHistory clinicalHistory = new ClinicalHistory();
        NCCNPersonRelationShipDetails nccnPersonRelationShipDetails = familyRelativesRelationshipDetails
                .get(person.getId());
        if (nccnPersonRelationShipDetails == null) {
            nccnPersonRelationShipDetails = new NCCNPersonRelationShipDetails("OTHER", "OTHER");
        }
        clinicalHistory.setRelationship(nccnPersonRelationShipDetails.getRelationShip());
        clinicalHistory.setSideOfFam(nccnPersonRelationShipDetails.getFamilySide());
        clinicalHistory.setSexAtBirth(getSexAtBirth(person.getGender()));
        clinicalHistory.setEthnicity(List.of(person.getFamilyRace().toString().toUpperCase()));
        clinicalHistory.setEthnicityOther(person.getFamilyRace().toString());
        clinicalHistory.setPolypOverall(person.getPolypOverall().toString());
        if(clinicalHistory.getPolypOverall().equals("YES")) {
            clinicalHistory.setPolypDetails(getPolypListDetails(person.getPolypDetails()));
        }
        clinicalHistory
                .setCancerOverall((person.getCancers() != null && !person.getCancers().isEmpty()) ? "Yes" : "No");
        clinicalHistory.setClinicalFeatureOverall("Not Provided");

        if (person.getCancers() != null) {
            for (Cancer cancer : person.getCancers()) {
                CancerFeatureDetails details = new CancerFeatureDetails();
                details.setCancerFeature(cancer.getCancerType().toString());
                details.setCancerFeatureType("cancer");
                details.setExactDxAge(String.valueOf(cancer.getDiagnoseAge() + 0.5));
                if (cancer.getCancerDetails() != null) {
                    details.setMetastatic(cancer.getCancerDetails().getMetastatic().toLowerCase());
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

    private String getSexAtBirth(int gender) {
        return (gender == 1) ? "male" : "female";
    }
}
