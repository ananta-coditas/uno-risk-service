package com.genomebrain.backend.service.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Class is used to get/set the fields of ConditionHolder
 */
public class ConditionHolder {

    private Map<String, String[]> conditionMap = new HashMap<String, String[]>();


    private static ConditionHolder instance;

    private ConditionHolder() {

    }

    public static synchronized ConditionHolder getInstance() {
        if (instance == null) {
            instance = new ConditionHolder();
            instance.populateConditionsMap();
        }
        return instance;
    }

    public Map<String, String[]> getConditionMap() {
        return conditionMap;
    }

    private void populateConditionsMap() {

        //Condition for NCCN LYNCH Condition
        //Condition For CRCRS11
        conditionMap.put("CRCTag22", new String[]{NCCNTagConstants.FM_COLON_PAT_FDR.getValue(), NCCNTagConstants.FM_COLON_PAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BRAIN_PAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_PAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_PAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BLADDER_PAT_FDR.getValue(), NCCNTagConstants.FM_BLADDER_PAT_SDR.getValue(),
                NCCNTagConstants.FM_GASTRIC_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_PAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_PAT_FDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_PAT_SDR.getValue(),
                NCCNTagConstants.FM_SM_BOWEL_PAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_PAT_SDR.getValue(), NCCNTagConstants.FM_BILIARY_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BILIARY_PAT_SDR.getValue(), NCCNTagConstants.FM_URETER_PAT_FDR.getValue(), NCCNTagConstants.FM_URETER_PAT_SDR.getValue(),
                NCCNTagConstants.FM_GALLBLADDER_PAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_PAT_SDR.getValue()});
        conditionMap.put("CRCTag21", new String[]{NCCNTagConstants.FM_COLON_MAT_FDR.getValue(), NCCNTagConstants.FM_COLON_MAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BRAIN_MAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_MAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_MAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_MAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_MAT_SDR.getValue(), NCCNTagConstants.FM_BLADDER_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BLADDER_MAT_SDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_SDR.getValue(),
                NCCNTagConstants.FM_SEB_ADENO_CAR_MAT_FDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_MAT_SDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_MAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BILIARY_MAT_FDR.getValue(), NCCNTagConstants.FM_BILIARY_MAT_SDR.getValue(), NCCNTagConstants.FM_URETER_MAT_FDR.getValue(), NCCNTagConstants.FM_URETER_MAT_SDR.getValue(),
                NCCNTagConstants.FM_GALLBLADDER_MAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_MAT_SDR.getValue()});

        //Condition For CRCRS10
        conditionMap.put("CRCTag19_A", new String[]{NCCNTagConstants.FM_COLON_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BLADDER_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BILITARY_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_URETER_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_OVARIAN_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_COLON_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BLADDER_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BRAIN_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_BILITARY_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_URETER_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_GALLBLADDER_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_MAT_SDR.getValue()});
        conditionMap.put("CRCTag19_B", new String[]{NCCNTagConstants.FM_OVARIAN_MAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_MAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_MAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BLADDER_MAT_FDR.getValue(), NCCNTagConstants.FM_BLADDER_MAT_SDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_SDR.getValue(),
                NCCNTagConstants.FM_SEB_ADENO_CAR_MAT_FDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_MAT_SDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_MAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BILIARY_MAT_FDR.getValue(), NCCNTagConstants.FM_BILIARY_MAT_SDR.getValue(), NCCNTagConstants.FM_URETER_MAT_FDR.getValue(), NCCNTagConstants.FM_URETER_MAT_SDR.getValue(),
                NCCNTagConstants.FM_COLON_MAT_FDR.getValue(), NCCNTagConstants.FM_COLON_MAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BRAIN_MAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_MAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_MAT_SDR.getValue()});

        conditionMap.put("CRCTag20_A", new String[]{NCCNTagConstants.FM_COLON_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BLADDER_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_PAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BILITARY_LT_50_PAT_FDR.getValue(),
                NCCNTagConstants.FM_URETER_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_COLON_LT_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BLADDER_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_SM_BOWEL_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_BILITARY_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_URETER_LT_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_ENDOMETRIAL_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_PAT_SDR.getValue()});
        conditionMap.put("CRCTag20_B", new String[]{NCCNTagConstants.FM_OVARIAN_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_PAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_PAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_PAT_SDR.getValue(), NCCNTagConstants.FM_BLADDER_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BLADDER_PAT_SDR.getValue(), NCCNTagConstants.FM_GASTRIC_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_PAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_PAT_FDR.getValue(),
                NCCNTagConstants.FM_SEB_ADENO_CAR_PAT_SDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_PAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_PAT_SDR.getValue(), NCCNTagConstants.FM_BILIARY_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BILIARY_PAT_SDR.getValue(), NCCNTagConstants.FM_URETER_PAT_FDR.getValue(), NCCNTagConstants.FM_URETER_PAT_SDR.getValue(), NCCNTagConstants.FM_COLON_PAT_FDR.getValue(),
                NCCNTagConstants.FM_COLON_PAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_PAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_PAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_PAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_PAT_SDR.getValue()});

        //Condition for CRCRS 7
        conditionMap.put("CRCTag15_Mat", new String[]{NCCNTagConstants.FM_OVARIAN_MAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_MAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_MAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_MAT_SDR.getValue(), NCCNTagConstants.FM_BLADDER_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BLADDER_MAT_SDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_MAT_FDR.getValue(),
                NCCNTagConstants.FM_SEB_ADENO_CAR_MAT_SDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_MAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_MAT_SDR.getValue(), NCCNTagConstants.FM_BILIARY_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BILIARY_MAT_SDR.getValue(), NCCNTagConstants.FM_URETER_MAT_FDR.getValue(), NCCNTagConstants.FM_URETER_MAT_SDR.getValue(), NCCNTagConstants.FM_COLON_MAT_FDR.getValue(),
                NCCNTagConstants.FM_COLON_MAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_MAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_MAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_MAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_MAT_SDR.getValue()});
        conditionMap.put("CRCTag15_Pat", new String[]{NCCNTagConstants.FM_OVARIAN_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_PAT_SDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_PAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_PAT_SDR.getValue(), NCCNTagConstants.FM_BLADDER_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BLADDER_PAT_SDR.getValue(), NCCNTagConstants.FM_GASTRIC_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_PAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_PAT_FDR.getValue(),
                NCCNTagConstants.FM_SEB_ADENO_CAR_PAT_SDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_PAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_PAT_SDR.getValue(), NCCNTagConstants.FM_BILIARY_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BILIARY_PAT_SDR.getValue(), NCCNTagConstants.FM_URETER_PAT_FDR.getValue(), NCCNTagConstants.FM_URETER_PAT_SDR.getValue(), NCCNTagConstants.FM_COLON_PAT_FDR.getValue(),
                NCCNTagConstants.FM_COLON_PAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_PAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_PAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_PAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_PAT_SDR.getValue()});

        //Following conditions for NCCN HBOC
        conditionMap.put("HBOCTag29_Mat", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_TDR.getValue()});
        conditionMap.put("HBOCTag29_Pat", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag30_Mat", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue()});
        conditionMap.put("HBOCTag30_Pat", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag31_Mat", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_TDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue()});
        conditionMap.put("HBOCTag31_Pat", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_TDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag34_Mat_A",new String[]{NCCNTagConstants.FM_BREAST_46_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_46_50_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag34_Mat_B",new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue(),
                NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_TDR.getValue(),
                NCCNTagConstants.FM_OVARIAN_EPITH_MAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_MAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_MAT_TDR.getValue(),
                NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_FDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_SDR.getValue(),
                NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_TDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_TDR.getValue()
        });

        conditionMap.put("HBOCTag34_Pat_A",new String[]{NCCNTagConstants.FM_BREAST_46_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_46_50_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag34_Pat_B",new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue(),
                NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_TDR.getValue(),
                NCCNTagConstants.FM_OVARIAN_EPITH_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_PAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_PAT_TDR.getValue(),
                NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_FDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_SDR.getValue(),
                NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_TDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag36_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
      conditionMap.put("HBOCTag36_B", new String[]{NCCNTagConstants.FM_BREAST_LTE_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_LTE_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_LTE_50_MAT_TDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_MAT_FDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_MAT_TDR.getValue()});

       conditionMap.put("HBOCTag37_B", new String[]{NCCNTagConstants.FM_BREAST_LTE_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_LTE_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_LTE_50_PAT_TDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_PAT_TDR.getValue()});
        conditionMap.put("HBOCTag37_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});

        conditionMap.put("HBOCTag40_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag40_B", new String[]{NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag41_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag41_B", new String[]{NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag42_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag42_B", new String[]{NCCNTagConstants.FM_PROSTATE_META_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag43_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag43_B", new String[]{NCCNTagConstants.FM_PROSTATE_META_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag67_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag67_B", new String[]{NCCNTagConstants.FM_PROSTATE_GL7_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag68_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag68_B", new String[]{NCCNTagConstants.FM_PROSTATE_GL7_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_PAT_TDR.getValue()});


        conditionMap.put("HBOCTag76_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag76_B", new String[]{NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_FDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_SDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag77_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag77_B", new String[]{NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_FDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_SDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag78_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag78_B", new String[]{NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_FDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_SDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag79_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag79_B", new String[]{NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_FDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_SDR.getValue(), NCCNTagConstants.FM_FALLO_PERITONEAL_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag53_Mat_A", new String[]{NCCNTagConstants.FM_BREAST_LTE_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_LTE_50_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_LTE_50_MAT_TDR.getValue()});
        conditionMap.put("HBOCTag53_Mat_B", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});

        conditionMap.put("HBOCTag53_Pat_A", new String[]{NCCNTagConstants.FM_BREAST_LTE_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_LTE_50_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_LTE_50_PAT_TDR.getValue()});
        conditionMap.put("HBOCTag53_Pat_B", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});

        conditionMap.put("HBOCTag54_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag54_B", new String[]{NCCNTagConstants.FM_OVARIAN_EPITH_MAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_MAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag55_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag55_B", new String[]{NCCNTagConstants.FM_OVARIAN_EPITH_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_PAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag56_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag56_B", new String[]{NCCNTagConstants.FM_PANCREATIC_MAT_FDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag57_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag57_B", new String[]{NCCNTagConstants.FM_PANCREATIC_PAT_FDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag58_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag58_B", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_TDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag59_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag59_B", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_TDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag38_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag38_B", new String[]{NCCNTagConstants.FM_OVARIAN_EPITH_MAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_MAT_SDR.getValue(),
                NCCNTagConstants.FM_OVARIAN_EPITH_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag39_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag39_B", new String[]{NCCNTagConstants.FM_OVARIAN_EPITH_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_EPITH_PAT_SDR.getValue(),
                NCCNTagConstants.FM_OVARIAN_EPITH_PAT_TDR.getValue()});


        conditionMap.put("HBOCTag64_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag64_B", new String[]{NCCNTagConstants.FM_PROSTATE_META_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_MAT_TDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_GL7_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_MAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag65_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag65_B", new String[]{NCCNTagConstants.FM_PROSTATE_META_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_META_PAT_TDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_GL7_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_PAT_SDR.getValue(), NCCNTagConstants.FM_PROSTATE_GL7_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag82_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MAT_TDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue()});
        conditionMap.put("HBOCTag82_B", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MAT_TDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag83_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_PAT_TDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue()});
        conditionMap.put("HBOCTag83_B", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_PAT_TDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag88_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag88_B", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MAT_TDR.getValue()});
        conditionMap.put("HBOCTag88_C", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag89_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag89_B", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_PAT_TDR.getValue()});
        conditionMap.put("HBOCTag89_C", new String[]{ NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_PROSTATE_PAT_TDR.getValue()});


        conditionMap.put("HBOCTag92", new String[]{TagConstants.AMBRY009_003.getValue(), NCCNTagConstants.PR_GASTRIC_DIFFUSE.getValue()});

        conditionMap.put("HBOCTag93_B", new String[]{NCCNTagConstants.FM_GASTRIC_DIFFUSE_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_DIFFUSE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_GASTRIC_DIFFUSE_MAT_TDR.getValue(),
                NCCNTagConstants.FM_GASTRIC_DIFFUSE_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_DIFFUSE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_GASTRIC_DIFFUSE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag94_B", new String[]{NCCNTagConstants.FM_BREAST_MALE_MAT_FDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_MAT_TDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_PAT_TDR.getValue()});


        conditionMap.put("HBOCTag95_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag95_B", new String[]{NCCNTagConstants.FM_BREAST_MALE_MAT_FDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag96_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag96_B", new String[]{NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag97_A", new String[]{NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag97_B", new String[]{NCCNTagConstants.FM_BREAST_BILAT_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_BILAT_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_BILAT_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag98_A", new String[]{NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(),
                NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag98_B", new String[]{NCCNTagConstants.FM_BREAST_BILAT_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_BILAT_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_BILAT_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag99_A", new String[]{NCCNTagConstants.FM_BREAST_BILAT_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_BILAT_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_BILAT_UNKNOWN_MAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_BILAT_UNKNOWN_MAT_FDR.getValue()});
        conditionMap.put("HBOCTag99_B", new String[]{NCCNTagConstants.FM_BREAST_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag100_A", new String[]{NCCNTagConstants.FM_BREAST_BILAT_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_BILAT_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_BILAT_UNKNOWN_PAT_SDR.getValue(), NCCNTagConstants.FM_BREAST_BILAT_UNKNOWN_PAT_FDR.getValue()});
        conditionMap.put("HBOCTag100_B", new String[]{NCCNTagConstants.FM_BREAST_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag107_A", new String[]{NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_TDR.getValue(),NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag108_A", new String[]{NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MALE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_PAT_TDR.getValue(),NCCNTagConstants.FM_BREAST_MALE_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MALE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_MAT_TDR.getValue()});

        conditionMap.put("HBOCTag109_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag109_B", new String[]{NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_TRP_NEG_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag110_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag110_B", new String[]{NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_TRP_NEG_MAT_TDR.getValue()});


        conditionMap.put("HBOCTag111_A", new String[]{NCCNTagConstants.FM_PROSTATE_PAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_PAT_SDR.getValue()});
        conditionMap.put("HBOCTag111_B", new String[]{NCCNTagConstants.FM_BREAST_MALE_PAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MALE_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_PAT_TDR.getValue()});

        conditionMap.put("HBOCTag112_A", new String[]{NCCNTagConstants.FM_PROSTATE_MAT_FDR.getValue(), NCCNTagConstants.FM_PROSTATE_MAT_SDR.getValue()});
        conditionMap.put("HBOCTag112_B", new String[]{NCCNTagConstants.FM_BREAST_MALE_MAT_FDR.getValue(), NCCNTagConstants.FM_BREAST_MALE_MAT_SDR.getValue(),
                NCCNTagConstants.FM_BREAST_MALE_MAT_TDR.getValue()});


        //CAR-753 v1.2026: colon/endometrial/gastric <50y now flag via their own any-age criteria (CRCCRIT15/16/17), so removed here to avoid double-counting
        conditionMap.put("CRCTag24", new String[]{  NCCNTagConstants.PR_OVARIAN_LT_50.getValue(),
                NCCNTagConstants.PR_PANCREATIC_LT_50.getValue(),
                NCCNTagConstants.PR_BLADDER_LT_50.getValue(),NCCNTagConstants.PR_URETER_LT_50.getValue(),NCCNTagConstants.PR_BRAIN_LT_50.getValue(),
                NCCNTagConstants.PR_BILIARY_LT_50.getValue(),NCCNTagConstants.PR_SMALL_BOWEL_LT_50.getValue(),
                NCCNTagConstants.PR_SEB_ADENO_CAR_LT_50.getValue(),NCCNTagConstants.PR_GALLBLADDER_LT_50.getValue()});

        //CAR-753 v1.2026: CRCCRIT7 - NCCN criteria v1.2026: "individual w/ CRC, endometrial, or gastric ca dx <50y" (FDR only)
        conditionMap.put("CRCTag16", new String[]{NCCNTagConstants.FM_COLON_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_COLON_LT_50_PAT_FDR.getValue(),
                NCCNTagConstants.FM_ENDOMETRIAL_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_LT_50_PAT_FDR.getValue(),
                NCCNTagConstants.FM_GASTRICS_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_PAT_FDR.getValue()});

        //CAR-753 v1.2026: new CRCCRIT18 - "≥3 FDR or SDR with gastric cancer regardless of age" on the same side of the family
        conditionMap.put("CRCTag33_Mat", new String[]{NCCNTagConstants.FM_GASTRIC_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_MAT_SDR.getValue()});
        conditionMap.put("CRCTag33_Pat", new String[]{NCCNTagConstants.FM_GASTRIC_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRIC_PAT_SDR.getValue()});

        conditionMap.put("CRCTag12", new String[]{ NCCNTagConstants.PR_COLON.getValue(),NCCNTagConstants.PR_BLADDER.getValue(),NCCNTagConstants.PR_GASTRIC.getValue(),NCCNTagConstants.PR_PANCREATIC.getValue(),
                NCCNTagConstants.PR_BRAIN.getValue(),NCCNTagConstants.PR_SM_BOWEL.getValue(),NCCNTagConstants.PR_BILIARY.getValue(),NCCNTagConstants.PR_URETER.getValue(),NCCNTagConstants.PR_SEB_ADENO_CAR.getValue(),
                NCCNTagConstants.PR_ENDOMETRIAL.getValue(),NCCNTagConstants.PR_OVARIAN.getValue(),NCCNTagConstants.PR_GALLBLADDER.getValue()});

        conditionMap.put("CRCTag8", new String[]{NCCNTagConstants.FM_COLON_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BLADDER_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_BILITARY_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_URETER_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_OVARIAN_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_MAT_FDR.getValue(), NCCNTagConstants.FM_COLON_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_BLADDER_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_GASTRICS_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_SM_BOWEL_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_BILITARY_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_URETER_LT_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_ENDOMETRIAL_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_MAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_MAT_FDR.getValue(),
                NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_MAT_SDR.getValue(),NCCNTagConstants.FM_COLON_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BLADDER_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_PAT_FDR.getValue(),
                NCCNTagConstants.FM_PANCREATIC_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_SM_BOWEL_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_BILITARY_LT_50_PAT_FDR.getValue(),
                NCCNTagConstants.FM_URETER_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_ENDOMETRIAL_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_OVARIAN_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_COLON_LT_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_BLADDER_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_GASTRICS_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_PANCREATIC_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_BRAIN_LT_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_SM_BOWEL_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_BILITARY_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_URETER_LT_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_ENDOMETRIAL_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_OVARIAN_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_GALLBLADDER_LT_50_PAT_SDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_PAT_FDR.getValue(), NCCNTagConstants.FM_SEB_ADENO_CAR_LT_50_PAT_SDR.getValue()});

        conditionMap.put("CRCTag26_A", new String[]{NCCNTagConstants.FM_GASTRIC_MAT_FDR.getValue(),NCCNTagConstants.FM_GASTRIC_MAT_SDR.getValue()});

        conditionMap.put("CRCTag26_B", new String[]{NCCNTagConstants.FM_GASTRIC_LTE_50_MAT_FDR.getValue(),NCCNTagConstants.FM_GASTRIC_LTE_50_MAT_SDR.getValue(),
                NCCNTagConstants.FM_GASTRIC_DIFFUSE_MAT_FDR.getValue(),NCCNTagConstants.FM_GASTRIC_DIFFUSE_MAT_SDR.getValue()});

        conditionMap.put("CRCTag27_A", new String[]{NCCNTagConstants.FM_GASTRIC_PAT_FDR.getValue(),NCCNTagConstants.FM_GASTRIC_PAT_SDR.getValue()});

        conditionMap.put("CRCTag27_B", new String[]{NCCNTagConstants.FM_GASTRIC_LTE_50_PAT_FDR.getValue(),NCCNTagConstants.FM_GASTRIC_LTE_50_PAT_SDR.getValue(),
                NCCNTagConstants.FM_GASTRIC_DIFFUSE_PAT_FDR.getValue(),NCCNTagConstants.FM_GASTRIC_DIFFUSE_PAT_SDR.getValue()});
    }

}
