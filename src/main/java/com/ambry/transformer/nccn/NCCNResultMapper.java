package com.ambry.transformer.nccn;

import java.util.HashSet;

import org.springframework.util.CollectionUtils;

import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.objex.risk.beans.nccn.NCCNResult;

public class NCCNResultMapper {

    public static NCCNResult convertTo(NCCNCoreResponse source) {
        NCCNResult target = new NCCNResult();
        target.setHbocVersion(source.getHbocVersion());
        target.setCrcVersion(source.getCrcVersion());
        target.setHbocOverall(source.getHbocOverall());
        if (!CollectionUtils.isEmpty(source.getHbocCriteriaMet())) {
            target.setHbocCriteriaMet(new HashSet<>(source.getHbocCriteriaMet()));
        }
        if (!CollectionUtils.isEmpty(source.getHbocCriteriaMetIds())) {
            target.setHbocCriteriaMetIds(new HashSet<>(source.getHbocCriteriaMetIds()));
        }
        target.setLynchOverall(source.getLynchOverall());
        if (!CollectionUtils.isEmpty(source.getLynchCriteriaMet())) {
            target.setLynchCriteriaMet(new HashSet<>(source.getLynchCriteriaMet()));
        }
        if (!CollectionUtils.isEmpty(source.getLynchCriteriaMetIds())) {
            target.setLynchCriteriaMetIds(new HashSet<>(source.getLynchCriteriaMetIds()));
        }
        target.setApsApcOverall(source.getApsApcOverall());
        if (!CollectionUtils.isEmpty(source.getApsApcCriteriaMet())) {
            target.setApsApcCriteriaMet(new HashSet<>(source.getApsApcCriteriaMet()));
        }
        if (!CollectionUtils.isEmpty(source.getApsApcCriteriaMetIds())) {
            target.setApsApcCriteriaMetIds(new HashSet<>(source.getApsApcCriteriaMetIds()));
        }
        target.setDateAssessed(source.getDateAssessed());
        return target;
    }
}
