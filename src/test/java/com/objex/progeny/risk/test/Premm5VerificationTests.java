package com.objex.progeny.risk.test;

import com.objex.progeny.risk.Utils;
import com.objex.progeny.risk.premm.PremmDelegate;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.premm.Person;
import com.objex.risk.beans.premm.Risk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.objex.risk.beans.premm.Person.MLH1;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PremmDelegate}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PremmDelegate Unit Tests")
class Premm5VerificationTests {

    @InjectMocks
    private PremmDelegate premmDelegate;

    @BeforeEach
    void setUp() {
    }



    @Test
    @DisplayName("Verification test 1 colon cancer on proband")
    void testPremmVerificationCase1() throws IOException, JAXBException {
        File file  = ResourceUtils.getFile("classpath:test-input-data/risk/premmrisk_case1.xml");
        Risk fam = Utils.createRiskFromFile(file.getAbsolutePath(),Risk.class);
        //Utils.saveToFile("classpath:test-input-data/risk/premmriskout_case1.xml",new Risk());
        PREMMResult result = premmDelegate.calculate(fam);
        assertThat(result).isNotNull();
        assertThat(result.getProbabilityCarrier()).isEqualTo(.094);
    }
    @Test
    @DisplayName("Verirication case 3")
    void testPremmVerificationCase3() throws IOException, JAXBException {
        File file  = ResourceUtils.getFile("classpath:test-input-data/risk/premmrisk_case3.xml");
        Risk fam = Utils.createRiskFromFile(file.getAbsolutePath(),Risk.class);
        //Utils.saveToFile("classpath:test-input-data/risk/premmriskout_case1.xml",new Risk());
        PREMMResult result = premmDelegate.calculate(fam);
        assertThat(result).isNotNull();
        assertThat(result.getProbabilityCarrier()).isEqualTo(.351);
    }

    @Test
    @DisplayName("Verification case 4")
    void testPremmVerificationCase4() throws IOException, JAXBException {
        File file  = ResourceUtils.getFile("classpath:test-input-data/risk/premmrisk_case4.xml");
        Risk fam = Utils.createRiskFromFile(file.getAbsolutePath(),Risk.class);
        //Utils.saveToFile("classpath:test-input-data/risk/premmriskout_case1.xml",new Risk());
        PREMMResult result = premmDelegate.calculate(fam);
        assertThat(result).isNotNull();
        assertThat(result.getProbabilityCarrier()).isEqualTo(.429);
    }
    @Test
    @DisplayName("Verification case 5")
    void testPremmVerificationCase5() throws IOException, JAXBException {
        File file  = ResourceUtils.getFile("classpath:test-input-data/risk/premmrisk_case5.xml");
        Risk fam = Utils.createRiskFromFile(file.getAbsolutePath(),Risk.class);
        //Utils.saveToFile("classpath:test-input-data/risk/premmriskout_case1.xml",new Risk());
        PREMMResult result = premmDelegate.calculate(fam);
        assertThat(result).isNotNull();
        assertThat(result.getProbabilityCarrier()).isEqualTo(.122);
    }
    @Test
    @DisplayName("Verirication case 6")
    void testPremmVerificationCase6() throws IOException, JAXBException {
        File file  = ResourceUtils.getFile("classpath:test-input-data/risk/premmrisk_case6.xml");
        Risk fam = Utils.createRiskFromFile(file.getAbsolutePath(),Risk.class);
        //Utils.saveToFile("classpath:test-input-data/risk/premmriskout_case1.xml",new Risk());
        PREMMResult result = premmDelegate.calculate(fam);
        assertThat(result).isNotNull();
        assertThat(result.getProbabilityCarrier()).isEqualTo(.069);
    }

}

