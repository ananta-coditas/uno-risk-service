package com.genomebrain.backend.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.genomebrain.backend.domain.GuideLinesDetails;
import com.genomebrain.backend.repository.custom.CustomGuideLinesDetailsRepository;
import com.genomebrain.backend.service.GuideLineService;
import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementAndTags;

@Service
public class GuideLineServiceImpl implements GuideLineService {

    private final Logger log = LoggerFactory.getLogger(GuideLineServiceImpl.class);

    private static final int minCountTagInCombination = 1;

    private static final int maxCountTagInCombination = 5;


    @Autowired
    CustomGuideLinesDetailsRepository guideLinesDetailsRepository;

    @Override
    public GenomeBrainResponseDTO getGuidelinesForTags(String tagsObject) {
        log.info("Method to get guidelines for tags: {}", tagsObject);
        GenomeBrainResponseDTO genomeBrainResponseDTO = new GenomeBrainResponseDTO();
        try {
            boolean isHBOC = false;
            boolean isLYNCH = false;
            boolean isAPS = false;
            JSONObject jsonObject = new JSONObject(tagsObject);
            JSONArray jsonArray = jsonObject.getJSONArray("tags");
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                tags.add(jsonArray.getString(i));
            }
            List<String> tagsList = new ArrayList<>();
            for (String tag : tags) {
                tagsList.add(StringUtils.capitalize(tag));
            }

            List<GuideLinesDetails> guidelinesDetailsResult = new ArrayList<>();
            if (tagsList.size() <= 50) {
                List<List<String>> sortedTagList = getTagsSorted(tagsList);
                guidelinesDetailsResult = guideLinesDetailsRepository.getAllTheGuidelines(sortedTagList);
            } else {
                List<GuideLinesDetails> guidelinesDetailsResults = new ArrayList<>();
                List<GuideLinesDetails> guideLinesDetailsList = guideLinesDetailsRepository.findAll();
                guideLinesDetailsList.parallelStream().forEach(x -> {
                    if (tagsList.containsAll(x.getTagsList())) {
                        guidelinesDetailsResults.add(x);
                    }
                });
                guidelinesDetailsResult.addAll(guidelinesDetailsResults);
            }
            if (!CollectionUtils.isEmpty(guidelinesDetailsResult)) {
                log.info("Guideline met for the given combination");
                genomeBrainResponseDTO.setGuideLineMet(true);
            }
            Set<String> reasoningStatementsForHBOC = new HashSet<>();
            Set<String> reasoningStatementsForLYNCH = new HashSet<>();
            Set<String> reasoningStatementsForAPS = new HashSet<>();
            Set<ReasoningStatementAndTags> hbocReasoningStatementAndTagsSet = new HashSet<>();
            Set<ReasoningStatementAndTags> lynchReasoningStatementAndTagsSet = new HashSet<>();
            Set<ReasoningStatementAndTags> apsReasoningStatementAndTagsSet = new HashSet<>();

            for (GuideLinesDetails guideLinesDetails : guidelinesDetailsResult) {
                if (!StringUtils.isEmpty(guideLinesDetails.getReasoningStatementEnglish())) {
                    if (guideLinesDetails.getGuidelineName().contains("NCCN HBOC")) {
                        reasoningStatementsForHBOC.add(guideLinesDetails.getReasoningStatementEnglish());

                        //Saving TagId and ReasoningStatementId in assessment table
                        ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                        reasoningStatementAndTags.setReasoningStatementId(guideLinesDetails.getReasoningStatementId());
                        Set<String> tagid = new HashSet<>();
                        tagid.add(guideLinesDetails.getTagsId());
                        reasoningStatementAndTags.setTagIds(tagid);
                        hbocReasoningStatementAndTagsSet.add(reasoningStatementAndTags);
                    }
                    if (guideLinesDetails.getGuidelineName().contains("NCCN LYNCH")) {
                        reasoningStatementsForLYNCH.add(guideLinesDetails.getReasoningStatementEnglish());

                        //Saving TagId and ReasoningStatementId in assessment table
                        ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                        reasoningStatementAndTags.setReasoningStatementId(guideLinesDetails.getReasoningStatementId());
                        Set<String> tagid = new HashSet<>();
                        tagid.add(guideLinesDetails.getTagsId());
                        reasoningStatementAndTags.setTagIds(tagid);
                        lynchReasoningStatementAndTagsSet.add(reasoningStatementAndTags);
                    }
                    if (guideLinesDetails.getGuidelineName().contains("NCCN APS")) {
                        reasoningStatementsForAPS.add(guideLinesDetails.getReasoningStatementEnglish());

                        //Saving TagId and ReasoningStatementId in assessment table
                        ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                        reasoningStatementAndTags.setReasoningStatementId(guideLinesDetails.getReasoningStatementId());
                        Set<String> tagid = new HashSet<>();
                        tagid.add(guideLinesDetails.getTagsId());
                        reasoningStatementAndTags.setTagIds(tagid);
                        apsReasoningStatementAndTagsSet.add(reasoningStatementAndTags);
                    }
                }
            }
            Set<String> guideLineName = guidelinesDetailsResult.stream().map(GuideLinesDetails::getGuidelineName).collect(Collectors.toSet());
            if (guideLineName.contains("NCCN HBOC")) {
                log.info("NCCN HBOC criteria met");
                isHBOC = true;
            }
            if (guideLineName.contains("NCCN LYNCH")) {
                log.info("NCCN LYNCH criteria met");
                isLYNCH = true;
            }
            if (guideLineName.contains("NCCN APS")) {
                log.info("NCCN APS criteria met");
                isAPS = true;
            }
            genomeBrainResponseDTO.setHBOC(isHBOC);
            genomeBrainResponseDTO.setLYNCH(isLYNCH);
            genomeBrainResponseDTO.setAPS(isAPS);
            genomeBrainResponseDTO.setReasoningStatementsForHBOC(reasoningStatementsForHBOC);
            genomeBrainResponseDTO.setReasoningStatementsForLYNCH(reasoningStatementsForLYNCH);
            genomeBrainResponseDTO.setReasoningStatementsForAPS(reasoningStatementsForAPS);
            genomeBrainResponseDTO.setHbocReasoningStatementAndTags(hbocReasoningStatementAndTagsSet);
            genomeBrainResponseDTO.setLynchReasoningStatementAndTags(lynchReasoningStatementAndTagsSet);
            genomeBrainResponseDTO.setApsReasoningStatementAndTags(apsReasoningStatementAndTagsSet);
            genomeBrainResponseDTO.setMessage("Guidelines fetched successfully");
            genomeBrainResponseDTO.setStatus("success");
        } catch (Exception e) {
            log.error("Error while fetching the guidelines {}", e.getMessage());
            genomeBrainResponseDTO.setMessage("failure");
            genomeBrainResponseDTO.setStatus(e.getMessage());
        }
        return genomeBrainResponseDTO;
    }

    private List<List<String>> getTagsSorted(List<String> tags) {
        List<List<String>> tagsList = new LinkedList<>();
        IntStream range = IntStream.rangeClosed(minCountTagInCombination, maxCountTagInCombination);
        range.forEach(x -> {
            tagsList.addAll(combination(tags, x));
        });
        List<List<String>> sortedTagList = new LinkedList<>();
        for (List<String> list : tagsList) {
            Collections.sort(list);
            sortedTagList.add(list);
        }
        return sortedTagList;
    }

    private static <T> List<List<T>> combination(List<T> values, int size) {
        if (0 == size) {
            return Collections.singletonList(Collections.<T>emptyList());
        }
        if (values.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> combination = new LinkedList<>();
        T actual = values.iterator().next();
        List<T> subSet = new LinkedList<>(values);
        subSet.remove(actual);
        List<List<T>> subSetCombination = combination(subSet, size - 1);

        for (List<T> set : subSetCombination) {
            List<T> newSet = new LinkedList<>(set);
            newSet.add(0, actual);
            combination.add(newSet);
        }
        combination.addAll(combination(subSet, size));
        return combination;
    }

    public static void main(String agrs[]) {
        String[] tags = {"Female", "PrBreast", "FmProstateGL7MatSDR"};
        Arrays.sort(tags);
        System.out.println(Arrays.toString(tags));
    }
}
