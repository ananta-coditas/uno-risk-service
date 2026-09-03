package com.genomebrain.backend.service.dto;

public enum NCCNTagConstants {

    //Personal Cancer Tags
    PR_BREAST("PrBreast"),
    PR_BREAST_GTE_51("PrBreast>=51"),
    PRPROSTATE("PrProstate"),

    PR_AD_POLYPS("PrAdPolyps"),
    PR_POLYPS_GT_20_DX("PrPolyp>20Dx"),
    PR_POLYPS_GT_10_DX("PrPolyp>10Dx"),
    FM_POLYPS_GT_10("FmPolyp>10"),

    PR_COLON("PrColon"),
    PR_BLADDER("PrBladder"),
    PR_GASTRIC("PrGastric"),
    PR_PANCREATIC("PrPancreatic"),
    PR_BRAIN("PrBrain"),
    PR_SM_BOWEL("PrSmBowel"),
    PR_BILIARY("PrBiliary"),
    PR_URETER("PrUreter"),
    PR_SEB_ADENO_CAR("PrSebAdenoCar"),
    PR_ENDOMETRIAL("PrEndometrial"),
    PR_OVARIAN("PrOvarian"),



    PR_COLON_LT_50("PrColon<50"),
    PR_ENDOMETRIAL_LT_50("PrEndometrial<50"),
    PR_GASTRIC_LT_50("PrGastric<50"),
    PR_OVARIAN_LT_50("PrOvarian<50"),
    PR_PANCREATIC_LT_50("PrPancreatic<50"),
    PR_BLADDER_LT_50("PrBladder<50"),
    PR_URETER_LT_50("PrUreter<50"),
    PR_BRAIN_LT_50("PrBrain<50"),
    PR_BILIARY_LT_50("PrBiliary<50"),
    PR_SMALL_BOWEL_LT_50("PrSmBowel<50"),
    PR_SEB_ADENO_CAR_LT_50("PrSebAdenoCar<50"),

    PR_GASTRIC_DIFFUSE("PrGastricDiffuse"),
    FM_GASTRIC_DIFFUSE("FmGastricDiffuse"),
    FM_GASTRIC_LTE_50("FmGastric<=50"),

    FM_GASTRIC_DIFFUSE_MAT_FDR("FmGastricDiffuseMatFDR"),
    FM_GASTRIC_DIFFUSE_MAT_SDR("FmGastricDiffuseMatSDR"),
    FM_GASTRIC_DIFFUSE_MAT_TDR("FmGastricDiffuseMatTDR"),
    FM_GASTRIC_DIFFUSE_PAT_FDR("FmGastricDiffusePatFDR"),
    FM_GASTRIC_DIFFUSE_PAT_SDR("FmGastricDiffusePatSDR"),
    FM_GASTRIC_DIFFUSE_PAT_TDR("FmGastricDiffusePatTDR"),

    FM_GASTRIC_LTE_50_MAT_FDR("FmGastric<=50MatFDR"),
    FM_GASTRIC_LTE_50_MAT_SDR("FmGastric<=50MatSDR"),
    FM_GASTRIC_LTE_50_MAT_TDR("FmGastric<=50MatTDR"),
    FM_GASTRIC_LTE_50_PAT_FDR("FmGastric<=50PatFDR"),
    FM_GASTRIC_LTE_50_PAT_SDR("FmGastric<=50PatSDR"),
    FM_GASTRIC_LTE_50_PAT_TDR("FmGastric<=50PatTDR"),

    //Condition for NCCN LYNCH Condition

    FM_COLON_PAT_FDR("FmColonPatFDR"),
    FM_COLON_PAT_SDR("FmColonPatSDR"),
    FM_BRAIN_PAT_FDR("FmBrainPatFDR"),
    FM_BRAIN_PAT_SDR("FmBrainPatSDR"),
    FM_OVARIAN_PAT_FDR("FmOvarianPatFDR"),
    FM_OVARIAN_PAT_SDR("FmOvarianPatSDR"),
    FM_PANCREATIC_PAT_FDR("FmPancreaticPatFDR"),
    FM_PANCREATIC_PAT_SDR("FmPancreaticPatSDR"),
    FM_ENDOMETRIAL_PAT_FDR("FmEndometrialPatFDR"),
    FM_ENDOMETRIAL_PAT_SDR("FmEndometrialPatSDR"),
    FM_BLADDER_PAT_FDR("FmBladderPatFDR"),
    FM_BLADDER_PAT_SDR("FmBladderPatSDR"),
    FM_GASTRIC_PAT_FDR("FmGastricPatFDR"),
    FM_GASTRIC_PAT_SDR("FmGastricPatSDR"),
    FM_GASTRIC_PAT_TDR("FmGastricPatTDR"),
    FM_SEB_ADENO_CAR_PAT_FDR("FmSebAdenoCarPatFDR"),
    FM_SEB_ADENO_CAR_PAT_SDR("FmSebAdenoCarPatSDR"),
    FM_SM_BOWEL_PAT_FDR("FmSmBowelPatFDR"),
    FM_SM_BOWEL_PAT_SDR("FmSmBowelPatSDR"),
    FM_BILIARY_PAT_FDR("FmBiliaryPatFDR"),
    FM_BILIARY_PAT_SDR("FmBiliaryPatSDR"),
    FM_URETER_PAT_FDR("FmUreterPatFDR"),
    FM_URETER_PAT_SDR("FmUreterPatSDR"),


    FM_COLON_MAT_FDR("FmColonMatFDR"),
    FM_COLON_MAT_SDR("FmColonMatSDR"),
    FM_BRAIN_MAT_FDR("FmBrainMatFDR"),
    FM_BRAIN_MAT_SDR("FmBrainMatSDR"),
    FM_OVARIAN_MAT_FDR("FmOvarianMatFDR"),
    FM_OVARIAN_MAT_SDR("FmOvarianMatSDR"),
    FM_PANCREATIC_MAT_FDR("FmPancreaticMatFDR"),
    FM_PANCREATIC_MAT_SDR("FmPancreaticMatSDR"),
    FM_ENDOMETRIAL_MAT_FDR("FmEndometrialMatFDR"),
    FM_ENDOMETRIAL_MAT_SDR("FmEndometrialMatSDR"),
    FM_BLADDER_MAT_FDR("FmBladderMatFDR"),
    FM_BLADDER_MAT_SDR("FmBladderMatSDR"),
    FM_GASTRIC_MAT_FDR("FmGastricMatFDR"),
    FM_GASTRIC_MAT_SDR("FmGastricMatSDR"),
    FM_GASTRIC_MAT_TDR("FmGastricMatTDR"),
    FM_SEB_ADENO_CAR_MAT_FDR("FmSebAdenoCarMatFDR"),
    FM_SEB_ADENO_CAR_MAT_SDR("FmSebAdenoCarMatSDR"),
    FM_SM_BOWEL_MAT_FDR("FmSmBowelMatFDR"),
    FM_SM_BOWEL_MAT_SDR("FmSmBowelMatSDR"),
    FM_BILIARY_MAT_FDR("FmBiliaryMatFDR"),
    FM_BILIARY_MAT_SDR("FmBiliaryMatSDR"),
    FM_URETER_MAT_FDR("FmUreterMatFDR"),
    FM_URETER_MAT_SDR("FmUreterMatSDR"),


    FM_COLON_LT_50_MAT_FDR("FmColon<50MatFDR"),
    FM_BLADDER_LT_50_MAT_FDR("FmBladder<50MatFDR"),
    FM_GASTRICS_LT_50_MAT_FDR("FmGastric<50MatFDR"),
    FM_PANCREATIC_LT_50_MAT_FDR("FmPancreatic<50MatFDR"),
    FM_BRAIN_LT_50_MAT_FDR("FmBrain<50MatFDR"),
    FM_SM_BOWEL_LT_50_MAT_FDR("FmSmBowel<50MatFDR"),
    FM_BILITARY_LT_50_MAT_FDR("FmBiliary<50MatFDR"),
    FM_URETER_LT_50_MAT_FDR("FmUreter<50MatFDR"),
    FM_ENDOMETRIAL_LT_50_MAT_FDR("FmEndometrial<50MatFDR"),
    FM_OVARIAN_LT_50_MAT_FDR("FmOvarian<50MatFDR"),
    FM_SEB_ADENO_CAR_LT_50_MAT_FDR("FmSebAdenoCar<50MatFDR"),
    FM_COLON_LT_50_MAT_SDR("FmColon<50MatSDR"),
    FM_BLADDER_LT_50_MAT_SDR("FmBladder<50MatSDR"),
    FM_GASTRICS_LT_50_MAT_SDR("FmGastric<50MatSDR"),
    FM_PANCREATIC_LT_50_MAT_SDR("FmPancreatic<50MatSDR"),
    FM_BRAIN_LT_50_MAT_SDR("FmBrain<50MatSDR"),
    FM_SM_BOWEL_LT_50_MAT_SDR("FmSmBowel<50MatSDR"),
    FM_BILITARY_LT_50_MAT_SDR("FmBiliary<50MatSDR"),
    FM_URETER_LT_50_MAT_SDR("FmUreter<50MatSDR"),
    FM_ENDOMETRIAL_LT_50_MAT_SDR("FmEndometrial<50MatSDR"),
    FM_OVARIAN_LT_50_MAT_SDR("FmOvarian<50MatSDR"),
    FM_SEB_ADENO_CAR_LT_50_MAT_SDR("FmSebAdenoCar<50MatSDR"),


    FM_COLON_LT_50_PAT_FDR("FmColon<50PatFDR"),
    FM_BLADDER_LT_50_PAT_FDR("FmBladder<50PatFDR"),
    FM_GASTRICS_LT_50_PAT_FDR("FmGastric<50PatFDR"),
    FM_PANCREATIC_LT_50_PAT_FDR("FmPancreatic<50PatFDR"),
    FM_BRAIN_LT_50_PAT_FDR("FmBrain<50PatFDR"),
    FM_SM_BOWEL_LT_50_PAT_FDR("FmSmBowel<50PatFDR"),
    FM_BILITARY_LT_50_PAT_FDR("FmBiliary<50PatFDR"),
    FM_URETER_LT_50_PAT_FDR("FmUreter<50PatFDR"),
    FM_ENDOMETRIAL_LT_50_PAT_FDR("FmEndometrial<50PatFDR"),
    FM_OVARIAN_LT_50_PAT_FDR("FmOvarian<50PatFDR"),
    FM_SEB_ADENO_CAR_LT_50_PAT_FDR("FmSebAdenoCar<50PatFDR"),
    FM_COLON_LT_50_PAT_SDR("FmColon<50PatSDR"),
    FM_BLADDER_LT_50_PAT_SDR("FmBladder<50PatSDR"),
    FM_GASTRICS_LT_50_PAT_SDR("FmGastric<50PatSDR"),
    FM_PANCREATIC_LT_50_PAT_SDR("FmPancreatic<50PatSDR"),
    FM_BRAIN_LT_50_PAT_SDR("FmBrain<50PatSDR"),
    FM_SM_BOWEL_LT_50_PAT_SDR("FmSmBowel<50PatSDR"),
    FM_BILITARY_LT_50_PAT_SDR("FmBiliary<50PatSDR"),
    FM_URETER_LT_50_PAT_SDR("FmUreter<50PatSDR"),
    FM_ENDOMETRIAL_LT_50_PAT_SDR("FmEndometrial<50PatSDR"),
    FM_OVARIAN_LT_50_PAT_SDR("FmOvarian<50PatSDR"),
    FM_SEB_ADENO_CAR_LT_50_PAT_SDR("FmSebAdenoCar<50PatSDR"),

    //Following conditions for NCCN HBOC

    FM_BREAST_MAT_FDR("FmBreastMatFDR"),
    FM_BREAST_MAT_SDR("FmBreastMatSDR"),
    FM_BREAST_MAT_TDR("FmBreastMatTDR"),
    FM_PROSTATE_MAT_FDR("FmProstateMatFDR"),
    FM_PROSTATE_MAT_SDR("FmProstateMatSDR"),
    FM_PROSTATE_MAT_TDR("FmProstateMatTDR"),


    FM_PROSTATE_META_MAT_FDR("FmProstateMetaMatFDR"),
    FM_PROSTATE_META_MAT_SDR("FmProstateMetaMatSDR"),
    FM_PROSTATE_META_MAT_TDR("FmProstateMetaMatTDR"),

    FM_PROSTATE_META_PAT_FDR("FmProstateMetaPatFDR"),
    FM_PROSTATE_META_PAT_SDR("FmProstateMetaPatSDR"),
    FM_PROSTATE_META_PAT_TDR("FmProstateMetaPatTDR"),

    FM_PROSTATE_GL7_MAT_FDR("FmProstateGL7MatFDR"),
    FM_PROSTATE_GL7_MAT_SDR("FmProstateGL7MatSDR"),
    FM_PROSTATE_GL7_MAT_TDR("FmProstateGL7MatTDR"),

    FM_PROSTATE_GL7_PAT_FDR("FmProstateGL7PatFDR"),
    FM_PROSTATE_GL7_PAT_SDR("FmProstateGL7PatSDR"),
    FM_PROSTATE_GL7_PAT_TDR("FmProstateGL7PatTDR"),

    FM_BREAST_PAT_FDR("FmBreastPatFDR"),
    FM_BREAST_PAT_SDR("FmBreastPatSDR"),
    FM_BREAST_PAT_TDR("FmBreastPatTDR"),
    FM_PROSTATE_PAT_FDR("FmProstatePatFDR"),
    FM_PROSTATE_PAT_SDR("FmProstatePatSDR"),
    FM_PROSTATE_PAT_TDR("FmProstatePatTDR"),


    FM_FALLO_PERITONEAL_MAT_FDR("FmFalloPeritonealMatFDR"),
    FM_FALLO_PERITONEAL_MAT_SDR("FmFalloPeritonealMatSDR"),
    FM_FALLO_PERITONEAL_MAT_TDR("FmFalloPeritonealMatTDR"),

    FM_FALLO_PERITONEAL_PAT_FDR("FmFalloPeritonealPatFDR"),
    FM_FALLO_PERITONEAL_PAT_SDR("FmFalloPeritonealPatSDR"),
    FM_FALLO_PERITONEAL_PAT_TDR("FmFalloPeritonealPatTDR"),

    FM_BREAST_LTE_50_MAT_FDR("FmBreast<=50MatFDR"),
    FM_BREAST_LTE_50_MAT_SDR("FmBreast<=50MatSDR"),
    FM_BREAST_LTE_50_MAT_TDR("FmBreast<=50MatTDR"),

    FM_BREAST_LTE_50_PAT_FDR("FmBreast<=50PatFDR"),
    FM_BREAST_LTE_50_PAT_SDR("FmBreast<=50PatSDR"),
    FM_BREAST_LTE_50_PAT_TDR("FmBreast<=50PatTDR"),

    FM_BREAST_GTE_51_MAT_FDR("FmBreast>=51MatFDR"),
    FM_BREAST_GTE_51_MAT_SDR("FmBreast>=51MatSDR"),
    FM_BREAST_GTE_51_MAT_TDR("FmBreast>=51MatTDR"),

    FM_BREAST_GTE_51_PAT_FDR("FmBreast>=51PatFDR"),
    FM_BREAST_GTE_51_PAT_SDR("FmBreast>=51PatSDR"),
    FM_BREAST_GTE_51_PAT_TDR("FmBreast<=51PatTDR"),

    FM_BREAST_UNKNOWN_MAT_FDR("FmBreastUnknownAgeMatFDR"),
    FM_BREAST_UNKNOWN_MAT_SDR("FmBreastUnknownAgeMatSDR"),
    FM_BREAST_UNKNOWN_MAT_TDR("FmBreastUnknownAgeMatTDR"),

    FM_BREAST_UNKNOWN_PAT_FDR("FmBreastUnknownAgePatFDR"),
    FM_BREAST_UNKNOWN_PAT_SDR("FmBreastUnknownAgePatSDR"),
    FM_BREAST_UNKNOWN_PAT_TDR("FmBreastUnknownAgePatTDR"),

    FM_OVARIAN_EPITH_MAT_FDR("FmOvarianEpithMatFDR"),
    FM_OVARIAN_EPITH_MAT_SDR("FmOvarianEpithMatSDR"),
    FM_OVARIAN_EPITH_MAT_TDR("FmOvarianEpithMatTDR"),
    FM_OVARIAN_EPITH_PAT_FDR("FmOvarianEpithPatFDR"),
    FM_OVARIAN_EPITH_PAT_SDR("FmOvarianEpithPatSDR"),
    FM_OVARIAN_EPITH_PAT_TDR("FmOvarianEpithPatTDR"),

    FM_PANCREATIC_MAT_TDR("FmPancreaticMatTDR"),
    FM_PANCREATIC_PAT_TDR("FmPancreaticPatTDR"),

    FM_BREAST_MALE_MAT_FDR("FmBreastMaleMatFDR"),
    FM_BREAST_MALE_MAT_SDR("FmBreastMaleMatSDR"),
    FM_BREAST_MALE_MAT_TDR("FmBreastMaleMatTDR"),

    FM_BREAST_MALE_PAT_FDR("FmBreastMalePatFDR"),
    FM_BREAST_MALE_PAT_SDR("FmBreastMalePatSDR"),
    FM_BREAST_MALE_PAT_TDR("FmBreastMalePatTDR"),


    FM_BREAST_TRP_NEG_MAT_FDR("FmBreastTrpNegMatFDR"),
    FM_BREAST_TRP_NEG_MAT_SDR("FmBreastTrpNegMatSDR"),
    FM_BREAST_TRP_NEG_MAT_TDR("FmBreastTrpNegMatTDR"),

    FM_BREAST_TRP_NEG_PAT_FDR("FmBreastTrpNegPatFDR"),
    FM_BREAST_TRP_NEG_PAT_SDR("FmBreastTrpNegPatSDR"),
    FM_BREAST_TRP_NEG_PAT_TDR("FmBreastTrpNegPatTDR"),

    FM_BREAST_BILAT_MAT_FDR("FmBreastBilatMatFDR"),
    FM_BREAST_BILAT_MAT_SDR("FmBreastBilatMatSDR"),
    FM_BREAST_BILAT_MAT_TDR("FmBreastBilatMatTDR"),

    FM_BREAST_BILAT_PAT_FDR("FmBreastBilatPatFDR"),
    FM_BREAST_BILAT_PAT_SDR("FmBreastBilatPatSDR"),
    FM_BREAST_BILAT_PAT_TDR("FmBreastBilatPatTDR"),

    FM_BREAST_BILAT_GTE_51_MAT_FDR("FmBreastBilat>=51MatFDR"),
    FM_BREAST_BILAT_GTE_51_MAT_SDR("FmBreastBilat>=51MatSDR"),
    FM_BREAST_BILAT_GTE_51_MAT_TDR("FmBreastBilat>=51MatTDR"),

    FM_BREAST_BILAT_GTE_51_PAT_FDR("FmBreastBilat>=51PatFDR"),
    FM_BREAST_BILAT_GTE_51_PAT_SDR("FmBreastBilat>=51PatSDR"),
    FM_BREAST_BILAT_GTE_51_PAT_TDR("FmBreastBilat>=51PatTDR"),

    FM_BREAST_BILAT_UNKNOWN_MAT_FDR("FmBreastBilatUnknownAgeMatFDR"),
    FM_BREAST_BILAT_UNKNOWN_MAT_SDR("FmBreastBilatUnknownAgeMatSDR"),
    FM_BREAST_BILAT_UNKNOWN_MAT_TDR("FmBreastBilatUnknownAgeMatTDR"),

    FM_BREAST_BILAT_UNKNOWN_PAT_FDR("FmBreastBilatUnknownAgePatFDR"),
    FM_BREAST_BILAT_UNKNOWN_PAT_SDR("FmBreastBilatUnknownAgePatSDR"),
    FM_BREAST_BILAT_UNKNOWN_PAT_TDR("FmBreastBilatUnknownAgePatTDR"),

    FM_BREAST_46_50_MAT_FDR("FmBreast46-50MatFDR"),
    FM_BREAST_46_50_MAT_SDR("FmBreast46-50MatSDR"),
    FM_BREAST_46_50_MAT_TDR("FmBreast46-50MatTDR"),

    FM_BREAST_46_50_PAT_FDR("FmBreast46-50PatFDR"),
    FM_BREAST_46_50_PAT_SDR("FmBreast46-50PatSDR"),
    FM_BREAST_46_50_PAT_TDR("FmBreast46-50PatTDR"),

    FM_POSITIVE_GENE_MUTATION("FmPositiveGeneMutation");


    private String value;

    public String getValue() {
        return this.value;
    }

    NCCNTagConstants(String value) {
        this.value = value;
    }
}
