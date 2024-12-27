package com.genomebrain.backend.service.util;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.genomebrain.backend.service.dto.ConditionHolder;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementAndTags;
import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementIdConstant;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.TagIdConstant;

@Service
public class LynchReasoningStatementUtility {

    /**
     * Check for NCCN Lynch logic considering of different individual criteria
     */
    public ReasoningStatementObject getLynchReasoningStatement(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, Set<String> tagsSet) throws ConnectException {
        Set<String> lynchReasoningStatement = new HashSet<>();
        Set<ReasoningStatementAndTags> reasoningStatementAndTagsSet = new HashSet<>();
        ReasoningStatementObject reasoningStatementObjects = new ReasoningStatementObject();


        /**
         * Conditions for TagId:
         * CRCTag22
         * CRCTag21
         */
        if (familyHistoryDetailsTagsDTOList.size() >= 3) {
            lynchReasoningStatement.add(checkForCRCRS11(familyHistoryDetailsTagsDTOList, "CRCTag21"));
            if (!StringUtils.isEmpty(checkForCRCRS11(familyHistoryDetailsTagsDTOList, "CRCTag21"))) {
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS11.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.CRCTag21.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }
            lynchReasoningStatement.add(checkForCRCRS11(familyHistoryDetailsTagsDTOList, "CRCTag22"));
            if (!StringUtils.isEmpty(checkForCRCRS11(familyHistoryDetailsTagsDTOList, "CRCTag22"))) {
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS11.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.CRCTag22.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }
        }

        if (familyHistoryDetailsTagsDTOList.size() >= 2) {

            /**
             * Conditions for TagId:
             * CRCTag19
             * CRCTag20
             */
            lynchReasoningStatement.add(checkForCRCRS10(familyHistoryDetailsTagsDTOList, "CRCTag19_A", "CRCTag19_B"));
            if (!StringUtils.isEmpty(checkForCRCRS10(familyHistoryDetailsTagsDTOList, "CRCTag19_A", "CRCTag19_B"))) {
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS10.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.CRCTag19.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            lynchReasoningStatement.add(checkForCRCRS10(familyHistoryDetailsTagsDTOList, "CRCTag20_A", "CRCTag20_B"));
            if (!StringUtils.isEmpty(checkForCRCRS10(familyHistoryDetailsTagsDTOList, "CRCTag20_A", "CRCTag20_B"))) {
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS10.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.CRCTag20.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }


            /**
             * Conditions for TagId:
             * CRCTag15
             * CRCTag13
             */
            String CRCTag15Pat = checkForCRCRS7(familyHistoryDetailsTagsDTOList, "CRCTag15_Pat");
            if (!StringUtils.isEmpty(CRCTag15Pat) && !Collections.disjoint(tagsSet, Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("CRCTag12")))) {
                lynchReasoningStatement.add(CRCTag15Pat);
                if (!StringUtils.isEmpty(CRCTag15Pat)) {
                    ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                    reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS7.getReasoningStatementId());
                    Set<String> tagId = new HashSet<>();
                    tagId.add(TagIdConstant.CRCTag15.getTagId());
                    reasoningStatementAndTags.setTagIds(tagId);
                    reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
                }
            }
            String CRCTag15Mat = checkForCRCRS7(familyHistoryDetailsTagsDTOList, "CRCTag15_Mat");
            if (!StringUtils.isEmpty(CRCTag15Mat) && !Collections.disjoint(tagsSet, Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("CRCTag12")))) {
                lynchReasoningStatement.add(CRCTag15Mat);
                if (!StringUtils.isEmpty(CRCTag15Mat)) {
                    ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                    reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS7.getReasoningStatementId());
                    Set<String> tagId = new HashSet<>();
                    tagId.add(TagIdConstant.CRCTag15.getTagId());
                    reasoningStatementAndTags.setTagIds(tagId);
                    reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
                }
            }

            /**
             * Conditions for TagId:
             * CRCTag12
             * CRCTag14
             */
            boolean crcTag12Pat = checkForLynchRS(familyHistoryDetailsTagsDTOList, "CRCTag15_Pat");
            if (crcTag12Pat && !Collections.disjoint(tagsSet, Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("CRCTag12")))) {
                lynchReasoningStatement.add(ReasoningStatementConstants.CRCRS7.getValue());
                if (!StringUtils.isEmpty(crcTag12Pat)) {
                    ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                    reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS7.getReasoningStatementId());
                    Set<String> tagId = new HashSet<>();
                    tagId.add(TagIdConstant.CRCTag12.getTagId());
                    reasoningStatementAndTags.setTagIds(tagId);
                }
            }
            boolean crcTag12Mat = checkForLynchRS(familyHistoryDetailsTagsDTOList, "CRCTag15_Mat");
            if (crcTag12Mat && !Collections.disjoint(tagsSet, Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("CRCTag12")))) {
                lynchReasoningStatement.add(ReasoningStatementConstants.CRCRS7.getValue());
                if (!StringUtils.isEmpty(crcTag12Mat)) {
                    ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                    reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS7.getReasoningStatementId());
                    Set<String> tagId = new HashSet<>();
                    tagId.add(TagIdConstant.CRCTag12.getTagId());
                    reasoningStatementAndTags.setTagIds(tagId);
                }
            }
        }

        /**
         * Conditions for TagId:
         * CRCTag5
         */
        if (tagsSet.contains("PrColon>=2Dx") || getValue(tagsSet, "CRCTag12", 2)) {
            lynchReasoningStatement.add(ReasoningStatementConstants.CRCRS5.getValue());
            ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
            reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS5.getReasoningStatementId());
            Set<String> tagId = new HashSet<>();
            tagId.add(TagIdConstant.CRCTAG5.getTagId());
            reasoningStatementAndTags.setTagIds(tagId);
            reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
        }

        /**
         * Conditions for TagId:
         * CRCTag8
         * CRCTag9
         */
        if (!Collections.disjoint(tagsSet, Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("CRCTag12"))) &&
                getValue(familyHistoryDetailsTagsDTOList, "CRCTag8",1)) {
            lynchReasoningStatement.add(ReasoningStatementConstants.CRCRS6.getValue());
            ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
            reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS6.getReasoningStatementId());
            Set<String> tagId = new HashSet<>();
            tagId.add(TagIdConstant.CRCTAG8.getTagId());
            reasoningStatementAndTags.setTagIds(tagId);
            reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
        }

        /**
         * Conditions for TagId:
         * CRCTag24
         */
        if (!Collections.disjoint(tagsSet, Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("CRCTag24")))) {
            lynchReasoningStatement.add(ReasoningStatementConstants.CRCRS13.getValue());
            ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
            reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS13.getReasoningStatementId());
            Set<String> tagId = new HashSet<>();
            tagId.add(TagIdConstant.CRCTAG24.getTagId());
            reasoningStatementAndTags.setTagIds(tagId);
            reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
        }

        lynchReasoningStatement.removeIf(String::isEmpty);
        reasoningStatementObjects.setReasoningStatementEn(lynchReasoningStatement);
        reasoningStatementObjects.setReasoningStatementAndTags(reasoningStatementAndTagsSet);
        return reasoningStatementObjects;
    }


    private String checkForCRCRS11(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 3) ? ReasoningStatementConstants.CRCRS11.getValue() : "";
    }

    private String checkForCRCRS7(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 2) ? ReasoningStatementConstants.CRCRS7.getValue() : "";
    }

    private String checkForCRCRS10(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagIdA, String tagIdB) {

        AtomicBoolean confitionFound = new AtomicBoolean(false);
        // 1.getValue method will return 2 distinct relatives with lynch syndrome cancers against tag array
        if (getValue(familyHistoryDetailsTagsDTOList, tagIdB, 2)
                || checkForLynchRS(familyHistoryDetailsTagsDTOList, tagIdB)) {
            familyHistoryDetailsTagsDTOList.forEach(x -> {
                //  stored in ConditionHolder Map with key CRCTag15_Pat
                if (x.getTags().stream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdA)).contains(t))) {
                    confitionFound.set(true);
                }
            });
        }

        return confitionFound.get() ? ReasoningStatementConstants.CRCRS10.getValue() : "";
    }

    private boolean getValue(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId, int count) {
        List<String> derivedSet = new ArrayList<>();
        familyHistoryDetailsTagsDTOList.forEach(x -> derivedSet.add(x.getTags()
                .stream()
                .filter(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagId)).contains(t))
                .findAny().orElse("")
        ));
        derivedSet.removeIf(String::isEmpty);
        return derivedSet.size() >= count;
    }


    /**
     * Check the tags for the relatives with same degree and same cancer
     *
     * @param familyHistoryDetailsTagsDTOList
     * @param conditionKey
     * @return
     */
    private boolean checkForLynchRS(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String conditionKey) {
        Set<String> tagList = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getTags)
                .flatMap(Collection::stream).collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(x -> x.getValue() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return tagList.parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(conditionKey)).contains(t));
    }

    private boolean getValue(Set<String> tagSet, String tagId, int count) {
        return tagSet.stream().
                filter(Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagId))::contains)
                .distinct()
                .limit(count)
                .count() >= count;
    }

}

