package com.genomebrain.backend.service.util;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.genomebrain.backend.service.dto.ConditionHolder;
import com.genomebrain.backend.service.dto.FamilyHistoryDetailsTagsDTO;
import com.genomebrain.backend.service.dto.ReasoningStatementAndTags;
import com.genomebrain.backend.service.dto.ReasoningStatementConstants;
import com.genomebrain.backend.service.dto.ReasoningStatementIdConstant;
import com.genomebrain.backend.service.dto.ReasoningStatementObject;
import com.genomebrain.backend.service.dto.TagConstants;
import com.genomebrain.backend.service.dto.TagIdConstant;

@Service
public class HbocReasoningStatementUtility {

    private final Logger log = LoggerFactory.getLogger(HbocReasoningStatementUtility.class);

    /**
     * Check for NCCN HBOC logic considering of different individual criteria
     */
    public ReasoningStatementObject getHbocReasoningStatement(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, Set<String> tagsSet) throws ConnectException {
        log.info("Entered into getHbocReasoningStatement method of HbocReasoningStatementUtility : Check for NCCN HBOC logic considering of different individual criteria");
        Set<String> hbocReasoningStatement = new HashSet<>();
        Set<ReasoningStatementAndTags> reasoningStatementAndTagsSet = new HashSet<>();
        ReasoningStatementObject reasoningStatementObjects = new ReasoningStatementObject();


        if (familyHistoryDetailsTagsDTOList.size() >= 2) {
            log.info("Into getHbocReasoningStatement method of HbocReasoningStatementUtility : familyHistoryDetailsTagsDTOList more than 1");

            /**
             * Conditions for TagId:
             * HBOCTag29
             */
            String hbocTag29Pat = checkForHBOCTag29Or30(familyHistoryDetailsTagsDTOList, "HBOCTag29_Pat");
            String hbocTag29Mat = checkForHBOCTag29Or30(familyHistoryDetailsTagsDTOList, "HBOCTag29_Mat");

            if ((!StringUtils.isEmpty(hbocTag29Pat) || !StringUtils.isEmpty(hbocTag29Mat)) && tagsSet.contains("Male") && tagsSet.contains("PrProstate")) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS25.getValue());

                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS25.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG29.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);


            }

            /**
             * Conditions for TagId:
             * HBOCTag30
             */
            String hbocTag30Pat = checkForHBOCTag29Or30(familyHistoryDetailsTagsDTOList, "HBOCTag30_Pat");
            String hbocTag30Mat = checkForHBOCTag29Or30(familyHistoryDetailsTagsDTOList, "HBOCTag30_Mat");

            if ((!StringUtils.isEmpty(hbocTag30Pat) || !StringUtils.isEmpty(hbocTag30Mat)) && tagsSet.contains("Male") && tagsSet.contains("PrProstate")) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS25.getValue());

                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS25.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG30.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag31
             */
            String hbocTag31Pat = checkForHBOCTag31(familyHistoryDetailsTagsDTOList, "HBOCTag31_Pat");
            String hbocTag31Mat = checkForHBOCTag31(familyHistoryDetailsTagsDTOList, "HBOCTag31_Mat");

            if ((!StringUtils.isEmpty(hbocTag31Mat) || !StringUtils.isEmpty(hbocTag31Pat)) && tagsSet.contains("Male") && tagsSet.contains("PrProstate")) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS25.getValue());

                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS25.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG31.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag36
             * HBOCTag37
             */
            Boolean hbocTag36 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag36_A", "HBOCTag36_B", 2);
            Boolean hbocTag37 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag37_A", "HBOCTag37_B", 2);

            if ((hbocTag36 != null && Boolean.TRUE.equals(hbocTag36)) || (hbocTag37 != null && Boolean.TRUE.equals(hbocTag37))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS30.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS30.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag36)) {
                    tagId.add(TagIdConstant.HBOCTAG36.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag37)) {
                    tagId.add(TagIdConstant.HBOCTAG37.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag38
             * HBOCTag39
             */
            Boolean hbocTag38 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag38_A", "HBOCTag38_B", 2);
            Boolean hbocTag39 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag39_A", "HBOCTag39_B", 2);

            if ((hbocTag38 != null && Boolean.TRUE.equals(hbocTag38)) || (hbocTag39 != null && Boolean.TRUE.equals(hbocTag39))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS31.getValue());

                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS31.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag38)) {
                    tagId.add(TagIdConstant.HBOCTAG38.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag39)) {
                    tagId.add(TagIdConstant.HBOCTAG39.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);

            }


            /**
             * Conditions for TagId:
             * HBOCTag40
             * HBOCTag41
             */
            Boolean hbocTag40 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag40_A", "HBOCTag40_B", 2);
            Boolean hbocTag41 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag41_A", "HBOCTag41_B", 2);

            if ((hbocTag40 != null && Boolean.TRUE.equals(hbocTag40)) || (hbocTag41 != null && Boolean.TRUE.equals(hbocTag41))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS32.getValue());

                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS32.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag40)) {
                    tagId.add(TagIdConstant.HBOCTAG40.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag41)) {
                    tagId.add(TagIdConstant.HBOCTAG41.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag42
             * HBOCTag43
             */
            Boolean hbocTag42 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag42_A", "HBOCTag42_B", 2);
            Boolean hbocTag43 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag43_A", "HBOCTag43_B", 2);

            if ((hbocTag42 != null && Boolean.TRUE.equals(hbocTag42)) || (hbocTag43 != null && Boolean.TRUE.equals(hbocTag43))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS33.getValue());

                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS33.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag42)) {
                    tagId.add(TagIdConstant.HBOCTAG42.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag43)) {
                    tagId.add(TagIdConstant.HBOCTAG43.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag64
             * HBOCTag67
             */
            Boolean hbocTag64 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag64_A", "HBOCTag64_B", 2);
            Boolean hbocTag65 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag65_A", "HBOCTag65_B", 2);

            if ((hbocTag64 != null && Boolean.TRUE.equals(hbocTag64)) || (hbocTag65 != null && Boolean.TRUE.equals(hbocTag65))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS44.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS44.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag64)) {
                    tagId.add(TagIdConstant.HBOCTAG64.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag65)) {
                    tagId.add(TagIdConstant.HBOCTAG65.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag67
             * HBOCTag68
             */
            Boolean hbocTag67 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag67_A", "HBOCTag67_B", 2);
            Boolean hbocTag68 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag68_A", "HBOCTag68_B", 2);

            if ((hbocTag67 != null && Boolean.TRUE.equals(hbocTag67)) || (hbocTag68 != null && Boolean.TRUE.equals(hbocTag68))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS33.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS33.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag67)) {
                    tagId.add(TagIdConstant.HBOCTAG67.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag68)) {
                    tagId.add(TagIdConstant.HBOCTAG68.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag76
             * HBOCTag77
             */
            Boolean hbocTag76 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag76_A", "HBOCTag76_B", 2);
            Boolean hbocTag77 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag77_A", "HBOCTag77_B", 2);

            if ((hbocTag76 != null && Boolean.TRUE.equals(hbocTag76)) || (hbocTag77 != null && Boolean.TRUE.equals(hbocTag77))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS31.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS31.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag76)) {
                    tagId.add(TagIdConstant.HBOCTAG76.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag77)) {
                    tagId.add(TagIdConstant.HBOCTAG77.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag78
             * HBOCTag79
             */
            Boolean hbocTag78 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag78_A", "HBOCTag78_B", 2);
            Boolean hbocTag79 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag79_A", "HBOCTag79_B", 2);

            if ((hbocTag78 != null && Boolean.TRUE.equals(hbocTag78)) || (hbocTag79 != null && Boolean.TRUE.equals(hbocTag79))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS41.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS41.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag78)) {
                    tagId.add(TagIdConstant.HBOCTAG78.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag79)) {
                    tagId.add(TagIdConstant.HBOCTAG79.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag53
             */
            Boolean hbocTag53Mat = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag53_Mat_A", "HBOCTag53_Mat_B", 2);
            Boolean hbocTag53Pat = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag53_Pat_A", "HBOCTag53_Pat_B", 2);

            if ((hbocTag53Mat != null && Boolean.TRUE.equals(hbocTag53Mat)) || (hbocTag53Pat != null && Boolean.TRUE.equals(hbocTag53Pat))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS40.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS40.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG53.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag54
             * HBOCTag55
             */
            Boolean hbocTag54 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag54_A", "HBOCTag54_B", 2);
            Boolean hbocTag55 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag55_A", "HBOCTag55_B", 2);

            if ((hbocTag54 != null && Boolean.TRUE.equals(hbocTag54)) || (hbocTag55 != null && Boolean.TRUE.equals(hbocTag55))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS41.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS41.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag54)) {
                    tagId.add(TagIdConstant.HBOCTAG54.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag55)) {
                    tagId.add(TagIdConstant.HBOCTAG55.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag56
             * HBOCTag57
             */
            Boolean hbocTag56 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag56_A", "HBOCTag56_B", 2);
            Boolean hbocTag57 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag57_A", "HBOCTag57_B", 2);

            if ((hbocTag56 != null && Boolean.TRUE.equals(hbocTag56)) || (hbocTag57 != null && Boolean.TRUE.equals(hbocTag57))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS42.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS42.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag56)) {
                    tagId.add(TagIdConstant.HBOCTAG56.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag57)) {
                    tagId.add(TagIdConstant.HBOCTAG57.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag59
             * HBOCTag58
             */
            Boolean hbocTag58 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag58_A", "HBOCTag58_B", 3);
            Boolean hbocTag59 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag59_A", "HBOCTag59_B", 3);

            if (Boolean.TRUE.equals(hbocTag58) || Boolean.TRUE.equals(hbocTag59)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS43.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS43.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag58)) {
                    tagId.add(TagIdConstant.HBOCTAG58.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag59)) {
                    tagId.add(TagIdConstant.HBOCTAG59.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag82
             * HBOCTag83
             * HBOCTag84
             * HBOCTag85  //todo
             * HBOCTag86
             * HBOCTag87
             */
            Boolean hbocTag82 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag82_A", "HBOCTag82_B", 2);
            Boolean hbocTag83 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag83_A", "HBOCTag83_B", 2);

            if (tagsSet.contains(TagConstants.FEMALE.getValue()) && tagsSet.contains("PrBreast")
                    && (Boolean.TRUE.equals(hbocTag82) || Boolean.TRUE.equals(hbocTag83))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS49.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS49.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag82)) {
                    tagId.add(TagIdConstant.HBOCTAG82.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag83)) {
                    tagId.add(TagIdConstant.HBOCTAG83.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }


            /**
             * Conditions for TagId:
             * HBOCTag88
             * HBOCTag89
             * HBOCTag90
             * HBOCTag91
             * HBOCTag105
             * HBOCTag106
             */

            Boolean hbocTag88 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag88_A", "HBOCTag88_C", "HBOCTag88_C", 3);
            Boolean hbocTag89 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag89_A", "HBOCTag89_C", "HBOCTag89_C", 3);

            if (Boolean.TRUE.equals(hbocTag88) || Boolean.TRUE.equals(hbocTag89)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS50.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS50.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag88)) {
                    tagId.add(TagIdConstant.HBOCTAG88.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag89)) {
                    tagId.add(TagIdConstant.HBOCTAG89.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            Boolean hbocTag105 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag88_A", "HBOCTag88_B", "HBOCTag88_B", 3);
            Boolean hbocTag106 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag89_A", "HBOCTag89_B", "HBOCTag89_B", 3);

            if (Boolean.TRUE.equals(hbocTag105) || Boolean.TRUE.equals(hbocTag106)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS50.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS50.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag105)) {
                    tagId.add(TagIdConstant.HBOCTAG105.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag106)) {
                    tagId.add(TagIdConstant.HBOCTAG106.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            Boolean hbocTag90 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag88_A", "HBOCTag88_B", "HBOCTag88_C", 3);
            Boolean hbocTag91 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag89_A", "HBOCTag89_B", "HBOCTag89_C", 3);

            if (Boolean.TRUE.equals(hbocTag90) || Boolean.TRUE.equals(hbocTag91)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS50.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS50.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag90)) {
                    tagId.add(TagIdConstant.HBOCTAG90.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag91)) {
                    tagId.add(TagIdConstant.HBOCTAG91.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag97
             * HBOCTag98
             * HBOCTag99
             * HBOCTag100
             */
            Boolean hbocTag97 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag97_A", "HBOCTag97_B", 2);
            Boolean hbocTag98 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag98_A", "HBOCTag98_B", 2);

            if (Boolean.TRUE.equals(hbocTag97) || Boolean.TRUE.equals(hbocTag98)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS34.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS34.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag97)) {
                    tagId.add(TagIdConstant.HBOCTAG97.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag98)) {
                    tagId.add(TagIdConstant.HBOCTAG98.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            Boolean hbocTag99 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag99_A", "HBOCTag99_B", 2);
            Boolean hbocTag100 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag100_A", "HBOCTag100_B", 2);

            if (Boolean.TRUE.equals(hbocTag99) || Boolean.TRUE.equals(hbocTag100)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS34.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS34.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag99)) {
                    tagId.add(TagIdConstant.HBOCTAG99.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag100)) {
                    tagId.add(TagIdConstant.HBOCTAG100.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }


            /**
             * Conditions for TagId:
             * HBOCTag109
             * HBOCTag110
             */
            Boolean hbocTag109 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag109_A", "HBOCTag109_B", 2);
            Boolean hbocTag110 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag110_A", "HBOCTag110_B", 2);

            if (Boolean.TRUE.equals(hbocTag109) || Boolean.TRUE.equals(hbocTag110)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS57.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS57.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag109)) {
                    tagId.add(TagIdConstant.HBOCTAG109.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag110)) {
                    tagId.add(TagIdConstant.HBOCTAG110.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }


            /**
             * Conditions for TagId:
             * HBOCTag111
             * HBOCTag112
             */
            Boolean hbocTag111 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag111_A", "HBOCTag111_B", 2);
            Boolean hbocTag112 = checkForHBOCRS(familyHistoryDetailsTagsDTOList, "HBOCTag112_A", "HBOCTag112_B", 2);

            if (Boolean.TRUE.equals(hbocTag111) || Boolean.TRUE.equals(hbocTag112)) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS58.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS58.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                if (Boolean.TRUE.equals(hbocTag111)) {
                    tagId.add(TagIdConstant.HBOCTAG111.getTagId());
                }
                if (Boolean.TRUE.equals(hbocTag112)) {
                    tagId.add(TagIdConstant.HBOCTAG112.getTagId());
                }
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

        }
        if (tagsSet.contains(TagConstants.FEMALE.getValue())) {
            /**
             * Conditions for TagId:
             * HBOCTag1
             */
            if (tagsSet.contains("PrBreast<=50")) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS1.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS1.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG1.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag3
             */
            if (tagsSet.contains(TagConstants.AMBRY007_002.getValue())) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS3.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS3.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG3.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }


            /**
             * Conditions for TagId:
             * HBOCTag92
             */
            if (tagsSet.containsAll(Arrays.asList(ConditionHolder.getInstance().getConditionMap().get("HBOCTag92")))) {
                hbocReasoningStatement.add(ReasoningStatementConstants.HBOCRS51.getValue());
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS51.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG92.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);

                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            /**
             * Conditions for TagId:
             * HBOCTag93
             */
            if (tagsSet.contains(TagConstants.AMBRY009_003.getValue())) {
                hbocReasoningStatement.add(checkForHBOCTag93(familyHistoryDetailsTagsDTOList, "HBOCTag93_B"));
                if (!StringUtils.isEmpty(checkForHBOCTag93(familyHistoryDetailsTagsDTOList, "HBOCTag93_B"))) {
                    ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                    reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS52.getReasoningStatementId());
                    Set<String> tagId = new HashSet<>();
                    tagId.add(TagIdConstant.HBOCTAG93.getTagId());
                    reasoningStatementAndTags.setTagIds(tagId);

                    reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
                }

            }

        }

        if (tagsSet.contains(TagConstants.MALE.getValue()) && tagsSet.contains("PrProstate")) {
            hbocReasoningStatement.add(checkForHBOCTag107(familyHistoryDetailsTagsDTOList, "HBOCTag107_A"));
            if (hbocReasoningStatement.contains(ReasoningStatementConstants.HBOCRS55.getValue())) {
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS55.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG107.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }

            hbocReasoningStatement.add(checkForHBOCTag108(familyHistoryDetailsTagsDTOList, "HBOCTag108_A"));
            if (hbocReasoningStatement.contains(ReasoningStatementConstants.HBOCRS56.getValue())) {
                ReasoningStatementAndTags reasoningStatementAndTags = new ReasoningStatementAndTags();
                reasoningStatementAndTags.setReasoningStatementId(ReasoningStatementIdConstant.HBOCRS56.getReasoningStatementId());
                Set<String> tagId = new HashSet<>();
                tagId.add(TagIdConstant.HBOCTAG108.getTagId());
                reasoningStatementAndTags.setTagIds(tagId);
                reasoningStatementAndTagsSet.add(reasoningStatementAndTags);
            }
        }


        hbocReasoningStatement.removeIf(String::isEmpty);
        reasoningStatementObjects.setReasoningStatementEn(hbocReasoningStatement);
        reasoningStatementObjects.setReasoningStatementAndTags(reasoningStatementAndTagsSet);

        log.info("Exit from getHbocReasoningStatement method of HbocReasoningStatementUtility : Check for NCCN HBOC logic considering of different individual criteria");
        return reasoningStatementObjects;
    }


    private String checkForHBOCTag29Or30(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 2) ? ReasoningStatementConstants.HBOCRS25.getValue() : "";
    }

    private String checkForHBOCTag31(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 2) ? ReasoningStatementConstants.HBOCRS25.getValue() : "";
    }

    private String checkForHBOCTag93(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 1) ? ReasoningStatementConstants.HBOCRS52.getValue() : "";
    }

    private String checkForHBOCTag107(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 1) ? ReasoningStatementConstants.HBOCRS55.getValue() : "";
    }

    private String checkForHBOCTag108(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagId) {
        return getValue(familyHistoryDetailsTagsDTOList, tagId, 1) ? ReasoningStatementConstants.HBOCRS56.getValue() : "";
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

    private Boolean checkForHBOCRS(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagIdA, String tagIdB, int countOfRelatives) {

        log.info("Entered into checkForHBOCRS method of HbocReasoningStatementUtility : Check for  HBOC reasoning statement");

        Boolean isCondition = false;
        Set<Integer> listA = new HashSet<>();
        Set<Integer> listB = new HashSet<>();

        AtomicReference<Integer> count = new AtomicReference<>(1);
        familyHistoryDetailsTagsDTOList.forEach(x -> {
            if (x.getTags().parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdA)).contains(t))) {
                listA.add(count.get());
            }
            if (x.getTags().parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdB)).contains(t))) {
                listB.add(count.get());
            }
            count.getAndSet(count.get() + 1);
        });
        if (!CollectionUtils.isEmpty(listA) && !CollectionUtils.isEmpty(listB)) {
            listA.addAll(listB);
            isCondition = listA.size() >= countOfRelatives;
        }
        log.info("Exit from checkForHBOCRS method of HbocReasoningStatementUtility : Check for  HBOC reasoning statement");
        return isCondition;
    }

    private Boolean checkForHBOCRS(List<FamilyHistoryDetailsTagsDTO> familyHistoryDetailsTagsDTOList, String tagIdA, String tagIdB, String tagIdC, int countOfRelatives) {

        Boolean isCondition = false;
        Set<Integer> listA = new HashSet<>();
        Set<Integer> listB = new HashSet<>();
        Set<Integer> listC = new HashSet<>();

        AtomicReference<Integer> count = new AtomicReference<>(1);
        familyHistoryDetailsTagsDTOList.forEach(x -> {
            if (x.getTags().parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdA)).contains(t))) {
                listA.add(count.get());
            }
            if (x.getTags().parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdB)).contains(t))) {
                listB.add(count.get());
            }

            if (x.getTags().parallelStream().anyMatch(t -> Arrays.asList(ConditionHolder.getInstance().getConditionMap().get(tagIdC)).contains(t))) {
                listC.add(count.get());
            }
            count.getAndSet(count.get() + 1);
        });
        if (!CollectionUtils.isEmpty(listA) && !CollectionUtils.isEmpty(listB) && !CollectionUtils.isEmpty(listC)) {
            listA.addAll(listB);
            listA.addAll(listC);
            isCondition = listA.size() >= countOfRelatives;
        }
        return isCondition;
    }


}

