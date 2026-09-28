package com.ambry.transformer.nccn;

import java.util.HashSet;

import org.springframework.util.CollectionUtils;

import com.genomebrain.backend.service.dto.NCCNCoreResponse;
import com.objex.risk.beans.nccn.NCCNResult;

import net.logstash.logback.util.StringUtils;

public class NCCNResultMapper {

    public static NCCNResult convertTo(NCCNCoreResponse source) {
        NCCNResult target = new NCCNResult();
        target.setHbocVersion(source.getHbocVersion());
        target.setCrcVersion(source.getCrcVersion());
        if (!StringUtils.isEmpty(source.getHbocOverall())) {
            target.setHbocOverall(Boolean.TRUE);
            target.setNccnCriteriaMet(Boolean.TRUE);
            if (!CollectionUtils.isEmpty(source.getHbocCriteriaMet())) {
                target.setHbocCriteriaMet(new HashSet<>(source.getHbocCriteriaMet()));
            }
            if (!CollectionUtils.isEmpty(source.getHbocCriteriaMetIds())) {
                target.setHbocCriteriaMetIds(new HashSet<>(source.getHbocCriteriaMetIds()));
            }
        }
        if (!StringUtils.isEmpty(source.getLynchOverall())) {
            target.setLynchOverall(Boolean.TRUE);
            target.setNccnCriteriaMet(Boolean.TRUE);
            if (!CollectionUtils.isEmpty(source.getLynchCriteriaMet())) {
                target.setLynchCriteriaMet(new HashSet<>(source.getLynchCriteriaMet()));
            }
            if (!CollectionUtils.isEmpty(source.getLynchCriteriaMetIds())) {
                target.setLynchCriteriaMetIds(new HashSet<>(source.getLynchCriteriaMetIds()));
            }
        }
        if (!StringUtils.isEmpty(source.getApsApcOverall())) {
            target.setApsApcOverall(Boolean.TRUE);
            target.setNccnCriteriaMet(Boolean.TRUE);
            if (!CollectionUtils.isEmpty(source.getApsApcCriteriaMet())) {
                target.setApsApcCriteriaMet(new HashSet<>(source.getApsApcCriteriaMet()));
            }
            if (!CollectionUtils.isEmpty(source.getApsApcCriteriaMetIds())) {
                target.setApsApcCriteriaMetIds(new HashSet<>(source.getApsApcCriteriaMetIds()));
            }
        }
        // CARE reports gastric only through its criteria lists (there is no gastric "overall" flag upstream)
        if (!CollectionUtils.isEmpty(source.getGastricCriteriaMet())) {
            target.setGastricOverall(Boolean.TRUE);
            target.setNccnCriteriaMet(Boolean.TRUE);
            target.setGastricCriteriaMet(new HashSet<>(source.getGastricCriteriaMet()));
            if (!CollectionUtils.isEmpty(source.getGastricCriteriaMetIds())) {
                target.setGastricCriteriaMetIds(new HashSet<>(source.getGastricCriteriaMetIds()));
            }
        }
        target.setDateAssessed(source.getDateAssessed());
        return target;
    }
}
