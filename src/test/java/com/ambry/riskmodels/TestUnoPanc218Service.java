/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.panc.Person;
import com.objex.risk.beans.panc.Risk;
import com.objex.risk.beans.panc.PancResult;
import static com.objex.risk.beans.panc.PancResult.PANCREATIC_RISK;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Instruction;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;

/**
 *
 * @author jason
 */
public class TestUnoPanc218Service extends TestCase {
    private Risk baseFamily = null;
    private Risk base2014Family = null;
   //private String url = "http://localhost:8088/RiskServiceWeb/services/";
   //  private String url = "http://192.168.1.119:8080/RiskServiceWeb/services/";
    String url = "http://localhost:8086/";
    //private String url = "http://10.10.4.163:8080/RiskServiceWeb/services/";  //care uno server
    //private String url = "http://64.58.145.76:8080/RiskServiceWeb/services/"; //test
    // private String url = "http://52.91.83.98:8080/RiskServiceWeb/services/";
    //private String url = "http://bayes.riskcalculation.progenygenetics.com:8080/RiskServiceWeb/services/";
    
    
    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        //assertEquals(risk.getAgeBy(), 1); currently test to match an installed R run rather than the docs due to myseed
        assertEquals(risk.getFamily().size(), 19);
    }
    public void testBaseFamily()
    {
        String prob = "0.453241";
        String predPanc = "0.018868";
        String lastPredPanc = "0.108990";
         com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        //fam.setAgeBy(1);
       // fam.setAgeTo(95);
        
        try {
            fam = Utils.createRiskFromFile("test/examplePancFamily217.xml", com.objex.risk.beans.uno.Risk.class);
            Risk pancfam = Utils.createRiskFromFile("test/examplePancFamily217.xml", com.objex.risk.beans.panc.Risk.class);
            for (int i =0; i < pancfam.getFamily().size(); i++) { 
                fam.getFamily().get(i).setAge(pancfam.getFamily().get(i).getDiagnosePancreasAge());
            }
            
            fam.getFamily().get(6).addCancer(new Cancer(CancerType.PANCREATIC,1,45));
            fam.getFamily().get(13).addCancer(new Cancer(CancerType.PANCREATIC,1,1));
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoTyrerService.class.getName()).log(Level.SEVERE, null, ex);
        }
       // fam.setSaveIncoming(true);
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(85);
        Instruction ins = new Instruction();
        ins.setName("impute");
        ins.setValue("true");
        model.getInstructions().add(ins);
        model.setName(ModelName.PANCPRO);
        //model.setChartCode(1)
        fam.getRequestedModels().add(model);
        //baseAsserts(fam);.
        
             
        //Utils.saveToFile("C:\\temp\\comb.json", fam, false);
        CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        PancResult result = (PancResult) combresult.getResults().get(ModelName.PANCPRO);
        //GailResult result = (GailResult) combresult.getGailResult();
        
         resultChecks(result, predPanc, lastPredPanc, prob); 
    }
     
     protected void resultChecks(PancResult result,String predPanc,  String lastPredPan, String prob) {
        double delta = .00001;
        assertEquals(Double.parseDouble(predPanc),fmt(result.getPredictions().get(0).getResult(PANCREATIC_RISK), 7),delta);
        
        int lastIndex = result.getPredictions().size() - 1;
        assertEquals(Double.parseDouble(lastPredPan),fmt(result.getPredictions().get(lastIndex).getResult(PANCREATIC_RISK), 7),delta);
         assertEquals(Double.parseDouble(prob),fmt(result.getProbabilityCarrier(), 7),delta);
     }
     public double fmt(Double numb, int len)
    {
        if (numb == null) {
            return Double.NaN;
        }
        if (new BigDecimal(numb).toPlainString().length() > len)
             return Double.parseDouble(new BigDecimal(numb).toPlainString().substring(0,len));
        return numb;
    }
    public void setUp()
    {
        System.out.println("creating Base Family");
        try {
            baseFamily = Utils.createRiskFromFile("test/examplePancFamily217.xml", Risk.class);
            base2014Family = Utils.createRiskFromFile("test/examplePanc2014Family.xml", Risk.class);
            //base2014Family = Utils.createRiskFromFile("test/livePancTesting.xml", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoPanc218Service.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestUnoPanc218Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
}
