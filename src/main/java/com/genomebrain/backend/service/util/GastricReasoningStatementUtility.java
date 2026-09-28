package com.genomebrain.backend.service.util;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.genomebrain.backend.service.dto.ConditionHolder;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.NCCNTagConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementAndTags;
import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementIdConstant;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.TagIdConstant;

@Service
public class GastricReasoningStatementUtility {

    private final Logger log = LoggerFactory.getLogger(GastricReasoningStatementUtility.class);

    /**
     * Check for NCCN gastric logic considering of different individual criteria
     */
    public ReasoningStatementObject getGastricReasoningStatement(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, Set<String> tagsSet) throws ConnectException {
        log.info("Entered into getGastricReasoningStatement method of GastricReasoningStatementUtility ");
        Set<String> gastricReasoningStatement = new HashSet<>();
        Set<ReasoningStatementAndTags> reasoningStatementAndTagsSet = new HashSet<>();
        ReasoningStatementObject reasoningStatementObjects = new ReasoningStatementObject();


        if (familyHistoryDetailsTagsDTOList.size() >= 2) {

            /**
             * Conditions for TagId:
             * CRCTag26
             * CRCTag28
             */
            if (checkForCRCRS(familyHistoryDetailsTagsDTOList, "CRCTag26_A", "CRCTag26_B")) {
                gastricReasoningStatement.add(ReasoningStatementConstants.CRCRS14.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS14.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.CRCTag26.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * CRCTag27
             * CRCTag29
             */
            if (checkForCRCRS(familyHistoryDetailsTagsDTOList, "CRCTag27_A", "CRCTag27_B")) {
                gastricReasoningStatement.add(ReasoningStatementConstants.CRCRS14.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS14.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.CRCTAG27.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

        }

        /**
         * CAR-753 v1.2026 - new CRCCRIT18
         * "≥3 FDR or SDR with gastric cancer regardless of age" on the same side of the family
         * Conditions for TagId:
         * CRCTag33
         */
        if (getValue(familyHistoryDetailsTagsDTOList, "CRCTag33_Mat", 3) || getValue(familyHistoryDetailsTagsDTOList, "CRCTag33_Pat", 3)) {
            gastricReasoningStatement.add(ReasoningStatementConstants.CRCRS18.getValue());
            ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
            reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS18.getReasoningStatementId());
            Set<String> tagId = new HashSet<>();
            tagId.add(TagIdConstant.CRCTag33.getTagId());
            reasoningStatementAndTags.setTagIds(tagId);
            reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
        }

        /**
         * Conditions for TagId:
         * CRCTag25
         */
        if (tagsSet.contains(NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue())) {
            gastricReasoningStatement.add(ReasoningStatementConstants.CRCRS15.getValue());
            ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
            reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.CRCRS15.getReasoningStatementId());
            Set<String> tagId = new HashSet<>();
            tagId.add(TagIdConstant.CRCTag25.getTagId());
            reasoningStatementAndTags.setTagIds(tagId);
            reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
        }

        gastricReasoningStatement.removeIf(String::isEmpty);
        reasoningStatementObjects.setReasoningStatementEn(gastricReasoningStatement);
        reasoningStatementObjects.setReasoningStatementAndTags(reasoningStatementAndTagsSet);
        log.info("Exit into getGastricReasoningStatement method of GastricReasoningStatementUtility ");
        return reasoningStatementObjects;
    }

    private boolean checkForCRCRS(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagIdA, String tagIdB) {
        log.info("Entered into checkForCRCRS method of GastricReasoningStatementUtility ");
        AtomicBoolean confitionFound = new AtomicBoolean(false);
        // 1.getValue method will return 2 distinct relatives with Gastric syndrome cancers against tag array
        if (getValue(familyHistoryDetailsTagsDTOList, tagIdA, 2)
                || checkForGastrichRS(familyHistoryDetailsTagsDTOList, tagIdA)) {
            familyHistoryDetailsTagsDTOList.forEach(x -> {
                //  stored in ConditionHolder Map with key CRCTag15_Pat
                if (x.getTags().stream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdB)).contains(t))) {
                    confitionFound.set(true);
                }
            });
        }
        log.info("Exit from checkForCRCRS method of GastricReasoningStatementUtility ");
        return confitionFound.get();
    }

    private boolean getValue(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId, int count) {
        log.info("Entered into getValue method of GastricReasoningStatementUtility ");
        List<String> derivedSet = new ArrayList<>();
        familyHistoryDetailsTagsDTOList.forEach(x -> derivedSet.add(x.getTags()
                .stream()
                .filter(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagId)).contains(t))
                .findAny().orElse("")
        ));
        derivedSet.removeIf(String::isEmpty);
        log.info("Exit from getValue method of GastricReasoningStatementUtility ");
        return derivedSet.size() >= count;
    }

    /**
     * Check the tags for the relatives with same degree and same cancer
     *
     * @param familyHistoryDetailsTagsDTOList
     * @param conditionKey
     * @return
     */
    private boolean checkForGastrichRS(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String conditionKey) {
        log.info("Entered into checkForGastrichRS method of GastricReasoningStatementUtility ");
        Set<String> tagList = familyHistoryDetailsTagsDTOList.stream().map(FamilyHistoryDetailsTagsDTO::getTags)
                .flatMap(Collection::stream).collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(x -> x.getValue() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        log.info("Exit from checkForGastrichRS method of GastricReasoningStatementUtility ");
        return tagList.parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(conditionKey)).contains(t));
    }
}