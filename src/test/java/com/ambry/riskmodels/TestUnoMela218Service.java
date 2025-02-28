/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.Germline;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.mela.Person;
import com.objex.risk.beans.mela.Risk;
import com.objex.risk.beans.mela.MelaResult;
import static com.objex.risk.beans.mela.MelaResult.MELANOMA_RISK;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.warehouse.TransType;
import com.objex.warehouse.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;

/**
 *
 * @author jason
 */
public class TestUnoMela218Service extends TestCase {
    private Risk baseFamily = null;
    private Risk base2014Family = null;
   // private String url = "http://localhost:8088/RiskServiceWeb/services/";
   //  private String url = "http://192.168.1.119:8080/RiskServiceWeb/services/";
    String url = "http://localhost:8086/";
     //private String url = "http://10.10.4.163:8080/RiskServiceWeb/services/";  //care uno server
    //private String url = "http://64.58.145.76:8080/RiskServiceWeb/services/"; //ambry test
   // private String url = "http://64.58.145.108:8080/RiskServiceWeb/services/";  //ambry prod
    
    
    
    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(5,risk.getAgeBy());
        assertEquals(28,risk.getFamily().size());
    }
       
    public void testBaseFamily()
    {
        String p16 = ".15420";
        String predMela = ".01403"; //NaN";
        String lastPredMela = ".13851"; //NaN";
       
        //baseAsserts(fam);
       // fam.setAgeBy(1);
     com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        //fam.setAgeBy(1);
        //fam.setAgeTo(95);
        
        try {
            fam = Utils.createRiskFromFile("test/exampleMelaFamily217.xml", com.objex.risk.beans.uno.Risk.class);
            Risk melafam = Utils.createRiskFromFile("test/exampleMelaFamily217.xml", com.objex.risk.beans.mela.Risk.class);
            for (int i =0; i < melafam.getFamily().size(); i++) { 
                fam.getFamily().get(i).setAge(melafam.getFamily().get(i).getDiagnoseSkinAge());
            }
             fam.getFamily().get(1).addCancer(new Cancer(Cancer.CancerType.SKIN,1,40));
            fam.getFamily().get(3).addCancer(new Cancer(Cancer.CancerType.SKIN,1,29));
            fam.getFamily().get(14).addCancer(new Cancer(Cancer.CancerType.SKIN,1,1));
             fam.getFamily().get(22).addCancer(new Cancer(Cancer.CancerType.SKIN,1,1));
             fam.getFamily().get(23).addCancer(new Cancer(Cancer.CancerType.SKIN,1,1));
           //proband.setAge(35);
            //proband.setMenarcheAge(13);
            //fam.getFamily().add(proband);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoTyrerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(85);
        model.setName(ModelName.MELAPRO);
        Instruction ins = new Instruction();
        ins.setName("impute");
        ins.setValue("true");
        model.getInstructions().add(ins);
        model.setChartCode(1);
        fam.getRequestedModels().add(model);
        //baseAsserts(fam);.
        
             
        //Utils.saveToFile("C:\\temp\\comb.json", fam, false);
        CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        MelaResult result = (MelaResult) combresult.getResults().get(ModelName.MELAPRO);
        //GailResult result = (GailResult) combresult.getGailResult();
          resultChecks(predMela, result, lastPredMela,p16);
    }
   
 public double fmt(Double numb, int len)
    {
        if (numb == null) {
            return Double.NaN;
        }
        if (new BigDecimal(numb.doubleValue()).toPlainString().length() > len)
             return Double.parseDouble(new BigDecimal(numb.doubleValue()).toPlainString().substring(0,len));
        return numb.doubleValue();
    }
    public void setUp()
    {
        System.out.println("creating Base Family");
        try {
            baseFamily = Utils.createRiskFromFile("test/exampleMelaFamily217.xml", Risk.class);
            base2014Family = Utils.createRiskFromFile("test/exampleMela2014Family.xml", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoMela218Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public Risk copyBase()
    {
        Risk fam = null;
        try {
            //Utils.
            // return (Risk) baseFamily.clone();
            fam =  (Risk)baseFamily.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(TestUnoMela218Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
     protected void resultChecks(String predMela, MelaResult result, String lastPredMela, String p16) {
        double delta = .00001;
        assertEquals(Double.parseDouble(predMela),fmt(result.getPredictions().get(0).getResult(MELANOMA_RISK), 7),delta);
        
        int lastIndex = result.getPredictions().size() - 1;
        assertEquals(Double.parseDouble(lastPredMela),fmt(result.getPredictions().get(lastIndex).getResult(MELANOMA_RISK), 7),delta);
         assertEquals(Double.parseDouble(p16),fmt(result.getProbabilityCarrier(), 7),delta);
     }
    
}
