package com.ambry.transformer.nccn;

import java.util.HashSet;

import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.objex.risk.beans.nccn.NCCNResult;

public class NCCNResultMapper {

    public static NCCNResult convertTo(NCCNCoreResponse source) {
        NCCNResult target = new NCCNResult();
        target.setHbocVersion(source.getHbocVersion());
        target.setCrcVersion(source.getCrcVersion());
        target.setHbocOverall(source.getHbocOverall());
        target.setHbocCriteriaMet(new HashSet<>(source.getHbocCriteriaMet()));
        target.setHbocCriteriaMetIds(new HashSet<>(source.getHbocCriteriaMetIds()));
        target.setLynchOverall(source.getLynchOverall());
        target.setLynchCriteriaMet(new HashSet<>(source.getLynchCriteriaMet()));
        target.setLynchCriteriaMetIds(new HashSet<>(source.getLynchCriteriaMetIds()));
        target.setApsApcOverall(source.getApsApcOverall());
        target.setApsApcCriteriaMet(new HashSet<>(source.getApsApcCriteriaMet()));
        target.setApsApcCriteriaMetIds(new HashSet<>(source.getApsApcCriteriaMetIds()));
        target.setDateAssessed(source.getDateAssessed());
        return target;
    }
}
