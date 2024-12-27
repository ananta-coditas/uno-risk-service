package com.genomebrain.backend.service.dto;

public enum DocumentType {

    CLINICAL_SUMMARY("clinical_summary"),
    ASSESSMENT_TRANSCRIPT("assessment_transcript"),
    RESULT_DISCLOSURE_TRANSCRIPT("result_disclosure_transcript"),
    PRETEST_COUNSELLING_TRANSCRIPT("pretest_counselling_transcript"),
    AMBRY_OUTPUT("report"),
    GC_NOTES("gc_notes"),
    PRE_TEST_GC_NOTES("pre_test_gc_notes"),
    PRE_TEST_FAMILY_HISTORY_PEDIGREE("pre_test_family_history_pedigree"),
    PEDIGREE("pedigree"),
    CLINICAL_HISTORY("clinical_history"),
    PATIENT_CLINICAL_SUMMARY("patient_clinical_summary"),
    PROVIDER_CLINICAL_SUMMARY("provider_clinical_summary"),
    CLINICALS("clinicals"),
    FAMILY_MEMBER_TEST_RESULT("family_member_test_result"),
    INSURANCE_PREAUTHORIZATION("insurance_preauthorization"),
    LETTER_OF_MEDICAL_NECESSITY("letter_of_medical_necessity"),
    PATHOLOGY_REPORT("pathology_report"),
    PRE_TEST_COUNSELING_NOTE("pre_test_gc_notes"),
    POST_TEST_COUNSELING_NOTE("gc_notes"),
    TEST_RESULTS_REPORTS("test_results_reports"),
    OTHERS("other"),
    FACE_SHEET("face_sheet"),
    PAYOR_CORRESPONDENCE("payor_correspondence"),
    PHYSICIAN_REQUESTION_TEST_ORDER("physician_requestion_test_order"),
    SIGNATURE_CARD("signature_card"),

    INSURANCE_CARD("insurance_card");

    private final String documentType;

    DocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return documentType;
    }
}
