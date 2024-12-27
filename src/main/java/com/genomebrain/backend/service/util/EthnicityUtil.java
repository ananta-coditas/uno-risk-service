package com.genomebrain.backend.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.genomebrain.backend.quesstionnaire.QuestionSet;
import com.genomebrain.backend.quesstionnaire.Questionnaire;
import com.genomebrain.backend.service.dto.DocumentType;
import com.genomebrain.backend.service.dto.Ethnicity;
import com.genomebrain.backend.service.dto.EthnicityInAP;
import com.genomebrain.backend.service.dto.OptionConstants;
import com.genomebrain.backend.service.dto.PersonalHistoryDetails;
import com.genomebrain.backend.service.dto.Program;
import com.genomebrain.backend.service.dto.RelativeConstants;
import com.genomebrain.backend.service.dto.TagConstants;
import com.genomebrain.backend.service.dto.WHQuestionAnswerRequestDTO;

@Service
public class EthnicityUtil {

    private static final String ETHNICITY_KEY = "ETHNICITY";
    private static final String SUB_CATEGORY_KEY = "SUB_CATEGORY";
    public static final String ANSWER_TEXT_KEY = ".answerText";
    public static final String DELIMITER = ", ";
    public static final String OPENING_BRACKET = " - (";
    public static final String CLOSING_BRACKET = ")";
    public static final  String ETHNICITY_QUESTION_CONSTANT = "AMBRY003";

    private static final Set<String> PARENT_ETHNICITY_CONSTANTS = new HashSet<>(Arrays.asList(
            "AMBRY003_001", "AMBRY003_002", "AMBRY003_003", "AMBRY003_004",
            "AMBRY003_005", "AMBRY003_006", "AMBRY003_007", "AMBRY003_008",
            "AMBRY003_009", "AMBRY003_010", "AMBRY003_011", "AMBRY003_012",
            "AMBRY003_013", "AMBRY003_014"
    ));

    // ASIAN_ETHNICITY_CONSTANT = "AMBRY003_002" WHITE_ETHNICITY_CONSTANT = "AMBRY003_003"
    private static final Set<String> ETHNICITIES_WITH_SUB_CATEGORIES = new HashSet<>(Arrays.asList("AMBRY003_002", "AMBRY003_003"));
    public static final String COLON = " : ";
    public static final String ASHKENAZI_JEWISH_MATERNAL_SIDE_CONSTANT = "AMBRY113_001";
    public static final String ASHKENAZI_JEWISH_PATERNAL_SIDE_CONSTANT = "AMBRY113_002";
    public static final String ASHKENAZI_JEWISH_SIDE_QUESTION_CONSTANT = "AMBRY113";

    private final Logger log = LoggerFactory.getLogger(EthnicityUtil.class);

    @Autowired
    private MessageSource messageSource;

    public static final String WHITE = "White";
    public static final String ASIAN = "Asian";
    public static final String CAUCASIAN = "Caucasian";

    /**
     * Retrieves a list of formatted ethnicity values with optional subcategories for display,
     * based on the specified document type.
     *
     * @param ethnicityConstants A list of ethnicity constants.
     * @param locale The locale for which to retrieve localized messages.
     * @param documentType The type of the document to determine value resolution strategy.
     * @return A list of formatted ethnicity values with subcategories, if applicable.
     */
    public List<String> getEthnicityListWithSubcategory(List<String> ethnicityConstants, Locale locale, DocumentType documentType, boolean useNewMapping) {
        List<String> formattedEthnicityList = new ArrayList<>();
        try {
            // Separate ethnicities and subcategories
            Map<String, List<String>> ethnicityMap = separateEthnicitiesAndSubCategories(ethnicityConstants);

            // Get ethnicities and create a map of subcategories under each ethnicity
            List<String> ethnicities = ethnicityMap.get(ETHNICITY_KEY);
            Map<String, List<String>> ethnicitySubCategoryMap = createEthnicitySubCategoryMap(ethnicityMap.getOrDefault(SUB_CATEGORY_KEY, Collections.emptyList()));

            ethnicities.forEach(ethnicity -> {
                // Sub categories were introduced in version 9 and above
                if (ETHNICITIES_WITH_SUB_CATEGORIES.contains(ethnicity) && useNewMapping) {
                    // Format ethnicity and subcategories if available
                    formattedEthnicityList.add(formatEthnicity(ethnicity, ethnicitySubCategoryMap, locale, documentType));
                } else {
                    // Ethnicity without subcategories
                    formattedEthnicityList.add(resolveConstantValue(ethnicity, locale, documentType, useNewMapping));
                }
            });
        } catch (Exception exception) {
            log.error("Error while parsing ethnicity, error {}", exception.getMessage());
            log.error("for constants {}, locale {}, document {}", ethnicityConstants, locale, documentType.getDocumentType());
        }
        return formattedEthnicityList;
    }

    private Map<String, List<String>> separateEthnicitiesAndSubCategories(List<String> ethnicityConstants) {
        return ethnicityConstants.stream()
                .collect(Collectors.groupingBy(ethnicity -> PARENT_ETHNICITY_CONSTANTS.contains(ethnicity) ? ETHNICITY_KEY : SUB_CATEGORY_KEY));
    }

    private Map<String, List<String>> createEthnicitySubCategoryMap(List<String> subCategories){
        return subCategories.stream().collect(Collectors.groupingBy(
                this::getParentEthnicity,
                Collectors.mapping(tag -> tag, Collectors.toList())
        ));
    }

    private String getParentEthnicity(String ethnicity){
        return ethnicity.substring(0, ethnicity.lastIndexOf('_'));
    }

    private String formatEthnicity(String ethnicity, Map<String, List<String>> ethnicitySubCategoryMap, Locale locale, DocumentType documentType) {
        StringBuilder stringBuilder = new StringBuilder(resolveConstantValue(ethnicity, locale, documentType, true));

        List<String> subCategories = ethnicitySubCategoryMap.getOrDefault(ethnicity, Collections.emptyList());
        if (!subCategories.isEmpty()) {
            String formattedSubcategories = subCategories.stream()
                    .map(subCategory -> resolveConstantValue(subCategory, locale, documentType, true))
                    .collect(Collectors.joining(DELIMITER, OPENING_BRACKET, CLOSING_BRACKET));
            stringBuilder.append(formattedSubcategories);
        }
        return stringBuilder.toString();
    }

    private String resolveConstantValue(String constant, Locale locale, DocumentType documentType, boolean useNewMapping) {
        String value = "";
        try {
            if (!useNewMapping) {
                constant = constant + "_OLD";
            }

            if (DocumentType.CLINICAL_SUMMARY.equals(documentType)) {
                value = Ethnicity.getValueForName(constant);
            } else {
                // DocumentType.ASSESSMENT_TRANSCRIPT
                value = messageSource.getMessage(constant + ANSWER_TEXT_KEY, null, locale);
            }
        } catch (Exception exception) {
            log.error("Exception while resolving ethnicity constant {}, for document {}, locale {}", constant, documentType.getDocumentType(), locale);
            log.error("error: {}", exception.getMessage());
        }
        return value;
    }

    /**
     * Get ashkenazi jewish ethnicity side for HC if applicable
     * @param ethnicityList
     * @param questionnaire
     * @return
     */
    private String getAshkenaziJewishEthnicitySide(List<String> ethnicityList, Questionnaire questionnaire) {
        String ethnicitySide = null;
        if (isAshkenaziJewishEthnicitySelected(ethnicityList)) {
            QuestionSet questionSet = questionnaire.getDemographic().stream().filter(question -> ASHKENAZI_JEWISH_SIDE_QUESTION_CONSTANT.equalsIgnoreCase(question.getQuestionKey()))
                    .findFirst().orElse(null);
            if (Objects.nonNull(questionSet)) {
                ethnicitySide = getEthnicitySide(questionSet.getAnswerKey());
            }
        }
        return ethnicitySide;
    }

    private static boolean isAshkenaziJewishEthnicitySelected(List<String> ethnicityList) {
        return ethnicityList.contains(Ethnicity.AMBRY003_006.getValue()) || ethnicityList.contains(Ethnicity.AMBRY003_006_OLD.getValue());
    }

    private static String getEthnicitySide(String answerKey) {
        String ethnicitySide;
        if (ASHKENAZI_JEWISH_MATERNAL_SIDE_CONSTANT.equalsIgnoreCase(answerKey)) {
            ethnicitySide = OptionConstants.AMBRY113_001.getValue();
        } else if (ASHKENAZI_JEWISH_PATERNAL_SIDE_CONSTANT.equalsIgnoreCase(answerKey)) {
            ethnicitySide = OptionConstants.AMBRY113_002.getValue();
        } else {
            // ASHKENAZI_JEWISH_BOTH_SIDES_CONSTANT
            ethnicitySide = OptionConstants.AMBRY113_003.getValue();
        }
        return ethnicitySide;
    }

    /**
     * add ashkenazi jewish ethnicity side and tags if applicable
     * @param personalHistoryDetails
     * @param tagSet
     * @param ethnicityList
     * @param questionnaire
     */
    public void applyAshkenaziTagAndSide(PersonalHistoryDetails personalHistoryDetails, Set<String> tagSet, List<String> ethnicityList, Questionnaire questionnaire) {
        if (isAshkenaziJewishEthnicitySelected(ethnicityList)) {
            String ethnicitySide = getAshkenaziJewishEthnicitySide(ethnicityList, questionnaire);
            if (OptionConstants.AMBRY113_001.getValue().equals(ethnicitySide)) {
                tagSet.add(TagConstants.AMBRY113_001.getValue());
            } else if (OptionConstants.AMBRY113_002.getValue().equals(ethnicitySide)) {
                tagSet.add(TagConstants.AMBRY113_002.getValue());

            } else if (OptionConstants.AMBRY113_003.getValue().equals(ethnicitySide)) {
                tagSet.add(TagConstants.AMBRY113_003.getValue());
            }
            personalHistoryDetails.setEthnicitySide(ethnicitySide);
        }
    }

    public String mapToMirthAPEthnicity(String ethnicity) {
        if (!StringUtils.isEmpty(ethnicity)) {
            switch (ethnicity) {
                case "African American":
                    ethnicity = Ethnicity.AMBRY003_001.getValue();
                    break;
                case "Caucasian":
                    ethnicity = Ethnicity.AMBRY003_003.getValue();
                    break;
                case "French Canadian":
                    ethnicity = Ethnicity.AMBRY003_004.getValue();
                    break;
                case "Finnish":
                    ethnicity = Ethnicity.AMBRY003_003_001.getValue();
                    break;
                case "Hispanic":
                    ethnicity = Ethnicity.AMBRY003_005.getValue();
                    break;
                case "Mediterranean":
                    ethnicity = Ethnicity.AMBRY003_013.getValue();
                    break;
                case "Middle Eastern":
                    ethnicity = Ethnicity.AMBRY003_008.getValue();
                    break;
                case "Ashkenazi Jewish":
                    ethnicity = Ethnicity.AMBRY003_006.getValue();
                    break;
                case "Asian":
                    ethnicity = Ethnicity.AMBRY003_002.getValue();
                    break;
                case "East Asian":
                    ethnicity = Ethnicity.AMBRY003_002_002.getValue();
                    break;
                case "South Asian":
                    ethnicity = Ethnicity.AMBRY003_002_007.getValue();
                    break;
                case "South-East Asian":
                    ethnicity = Ethnicity.AMBRY003_002_008.getValue();
                    break;
                case "Portuguese":
                    ethnicity = Ethnicity.AMBRY003_010.getValue();
                    break;
                case "Native American":
                    ethnicity = Ethnicity.AMBRY003_009.getValue();
                    break;
                case "Pacific Islander":
                    ethnicity = Ethnicity.AMBRY003_014.getValue();
                    break;
                case "Northern European":
                    ethnicity = Ethnicity.AMBRY003_003_002.getValue();
                    break;
                case "Southern European":
                    ethnicity = Ethnicity.AMBRY003_003_003.getValue();
                    break;
                default:
                    ethnicity = Ethnicity.AMBRY003_011.getValue();
                    break;
            }
        }
        return ethnicity;
    }
}
