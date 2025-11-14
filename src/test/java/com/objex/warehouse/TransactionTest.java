package com.objex.warehouse;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Transaction}.
 */
@DisplayName("Transaction Unit Tests")
class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
    }

    @Test
    @DisplayName("Should create Transaction with default values")
    void testDefaultConstructor() {
        assertThat(transaction).isNotNull();
        assertThat(transaction.getTransId()).isNull();
        assertThat(transaction.getCreateDate()).isNull();
        assertThat(transaction.getClinicCode()).isNull();
        assertThat(transaction.getFamilyId()).isNull();
        assertThat(transaction.getType()).isNull();
        assertThat(transaction.isTesting()).isFalse();
        assertThat(transaction.getInputs()).isNotNull();
        assertThat(transaction.getResults()).isNotNull();
    }

    @Test
    @DisplayName("Should set and get transId")
    void testTransId() {
        String transId = "TEST-123";
        transaction.setTransId(transId);
        assertThat(transaction.getTransId()).isEqualTo(transId);
    }

    @Test
    @DisplayName("Should set and get createDate")
    void testCreateDate() {
        Date createDate = new Date();
        transaction.setCreateDate(createDate);
        assertThat(transaction.getCreateDate()).isEqualTo(createDate);
    }

    @Test
    @DisplayName("Should set and get clinicCode")
    void testClinicCode() {
        String clinicCode = "CLINIC-001";
        transaction.setClinicCode(clinicCode);
        assertThat(transaction.getClinicCode()).isEqualTo(clinicCode);
    }

    @Test
    @DisplayName("Should set and get familyId")
    void testFamilyId() {
        String familyId = "FAMILY-001";
        transaction.setFamilyId(familyId);
        assertThat(transaction.getFamilyId()).isEqualTo(familyId);
    }

    @Test
    @DisplayName("Should set and get type")
    void testType() {
        TransType type = TransType.RISK;
        transaction.setType(type);
        assertThat(transaction.getType()).isEqualTo(type);
    }

    @Test
    @DisplayName("Should set and get testing flag")
    void testTesting() {
        transaction.setTesting(true);
        assertThat(transaction.isTesting()).isTrue();

        transaction.setTesting(false);
        assertThat(transaction.isTesting()).isFalse();
    }

    @Test
    @DisplayName("Should set and get inputs")
    void testInputs() {
        RiskInputs inputs = new RiskInputs();
        inputs.setTransId("INPUT-123");
        transaction.setInputs(inputs);
        assertThat(transaction.getInputs()).isEqualTo(inputs);
        assertThat(transaction.getInputs().getTransId()).isEqualTo("INPUT-123");
    }

    @Test
    @DisplayName("Should set and get results")
    void testResults() {
        RiskResults results = new RiskResults();
        results.setTransId("RESULT-123");
        transaction.setResults(results);
        assertThat(transaction.getResults()).isEqualTo(results);
        assertThat(transaction.getResults().getTransId()).isEqualTo("RESULT-123");
    }

    @Test
    @DisplayName("Should return formatted timestamp when createDate is set")
    void testGetTimestampWithDate() {
        Date createDate = new Date();
        transaction.setCreateDate(createDate);
        
        String timestamp = transaction.getTimestamp();
        assertThat(timestamp).isNotNull();
        assertThat(timestamp).isNotEmpty();
        
        // Verify format matches expected pattern
        SimpleDateFormat format = new SimpleDateFormat(Transaction.FORMAT);
        String expectedTimestamp = format.format(createDate);
        assertThat(timestamp).isEqualTo(expectedTimestamp);
    }

    @Test
    @DisplayName("Should return empty string when createDate is null")
    void testGetTimestampWithoutDate() {
        transaction.setCreateDate(null);
        String timestamp = transaction.getTimestamp();
        assertThat(timestamp).isEqualTo("");
    }

    @Test
    @DisplayName("Should handle all setters and getters together")
    void testAllProperties() {
        String transId = "TRANS-001";
        Date createDate = new Date();
        String clinicCode = "CLINIC-001";
        String familyId = "FAMILY-001";
        TransType type = TransType.RISK;
        RiskInputs inputs = new RiskInputs();
        RiskResults results = new RiskResults();

        transaction.setTransId(transId);
        transaction.setCreateDate(createDate);
        transaction.setClinicCode(clinicCode);
        transaction.setFamilyId(familyId);
        transaction.setType(type);
        transaction.setInputs(inputs);
        transaction.setResults(results);
        transaction.setTesting(true);

        assertThat(transaction.getTransId()).isEqualTo(transId);
        assertThat(transaction.getCreateDate()).isEqualTo(createDate);
        assertThat(transaction.getClinicCode()).isEqualTo(clinicCode);
        assertThat(transaction.getFamilyId()).isEqualTo(familyId);
        assertThat(transaction.getType()).isEqualTo(type);
        assertThat(transaction.getInputs()).isEqualTo(inputs);
        assertThat(transaction.getResults()).isEqualTo(results);
        assertThat(transaction.isTesting()).isTrue();
    }

    @Test
    @DisplayName("Should have correct FORMAT constant")
    void testFormatConstant() {
        assertThat(Transaction.FORMAT).isEqualTo("MM-dd-yyyy hh:mm:ss");
    }
}

