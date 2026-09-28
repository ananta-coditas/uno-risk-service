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
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.uno.Person;

@Component
public class NCCNClinicalHistoryMapper {

    private static final int GENDER_MALE = 1;

    private static final String MULTIPLE_PRIMARIES_YES = "YES";

    /**
     * UNO cancer types that other risk models use for a cancer the NCCN logic knows under another name
     * (e.g. MMRPro/PREMM send endometrial cancer as ENDOMETRIAL; NCCN handles it as Uterine).
     */
    private static final Map<Cancer.CancerType, Cancer.CancerType> NCCN_CANCER_TYPE_ALIASES = Map.of(
        Cancer.CancerType.ENDOMETRIAL, Cancer.CancerType.Uterine,
        Cancer.CancerType.Fallopian, Cancer.CancerType.Fallopian_Tube,
        Cancer.CancerType.Biliary, Cancer.CancerType.BILIARY_TRACT);

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
                details.setCancerFeature(toNccnCancerType(cancer.getCancerType()).toString());
                details.setCancerFeatureType("cancer");
                //details.setExactDxAge(String.valueOf(cancer.getDiagnoseAge() + 0.5));
                details.setExactDxAge(String.valueOf(cancer.getDiagnoseAge()));
                if (cancer.getCancerDetails() != null) {
                    details.setMetastatic(cancer.getCancerDetails().getMetastatic());
                    details.setMultiplePrimaries(cancer.getCancerDetails().getMultiplePrimaries());
                    if (cancer.getCancerType() == Cancer.CancerType.BREAST && hasSecondBreastPrimary(cancer.getCancerDetails())) {
                        details.setBreastBilateral("Yes");
                        // NCCN counts a bilateral breast cancer as a second primary; this answer drives HBOCRS27
                        if (details.getMultiplePrimaries() == null || details.getMultiplePrimaries().isBlank()) {
                            details.setMultiplePrimaries(MULTIPLE_PRIMARIES_YES);
                        }
                    }
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
     * Maps the UNO gender code to the NCCN sex-at-birth value. UNO sends 1 for male and 2 for female;
     * like the other risk models, anything other than 1 (including the legacy 0) is treated as female.
     */
    private String getSexAtBirth(Integer gender) {
        return Objects.equals(gender, GENDER_MALE) ? "male" : "female";
    }

    /**
     * A bilateral or contralateral breast cancer is a second breast primary, which NCCN asks about as
     * "both breasts" (HBOCRS27 for relatives).
     */
    private static boolean hasSecondBreastPrimary(com.objex.risk.beans.uno.CancerDetails cancerDetails) {
        return isAffected(cancerDetails.getBilateral()) || isAffected(cancerDetails.getContralateral());
    }

    private static boolean isAffected(AffectedType affectedType) {
        return affectedType != null && affectedType != AffectedType.NONE;
    }

    private static Cancer.CancerType toNccnCancerType(Cancer.CancerType cancerType) {
        return NCCN_CANCER_TYPE_ALIASES.getOrDefault(cancerType, cancerType);
    }

    /** Desmoid tumours and sebaceous adenomas are NCCN clinical features, reported through UNO as cancer types. */
    private boolean hasClinicalFeature(Person person) {
        return person.getCancers() != null
            && person.getCancers().stream().map(Cancer::getCancerType).anyMatch(CLINICAL_FEATURE_TYPES::contains);
    }
}
