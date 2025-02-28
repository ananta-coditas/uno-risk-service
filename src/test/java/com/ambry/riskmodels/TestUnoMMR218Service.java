/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.Germline;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.mmr.Person;
import com.objex.risk.beans.mmr.Risk;
import com.objex.risk.beans.mmr.MMRResult;
import static com.objex.risk.beans.mmr.MMRResult.ENDOMETRIAL_RISK;
import com.objex.risk.beans.mmr.Marker;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.Cancer.CancerType;
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
public class TestUnoMMR218Service extends TestCase {
    private Risk baseFamily = null;
    private Risk base2014Family = null;
    String url = "http://localhost:8086/";
 // private String url = "http://localhost:8088/RiskServiceWeb/services/";
    //private String url = "http://10.10.4.163:8080/RiskServiceWeb/services/";  //care uno server
   // private String url = "http://192.168.1.119:8080/RiskServiceWeb/services/";
  // private String url = "http://64.58.145.76:8080/RiskServiceWeb/services/"; //ambry test
    //private String url = "https://10.3.26.21/RiskServiceWeb/services/"; //ambry test
 // private String url = "http://64.58.145.108:8080/RiskServiceWeb/services/";  //ambry prod
     //private String url = "http://bayes.riskcalculation.progenygenetics.com:8080/RiskServiceWeb/services/"; 
    
    public void testBaseFamily()
    {
        String mlh = "0.008851";
        String msh2 = "0.0104141";
        String msh6 = "0.001365";
        String predCo = "0.0041157";
        String predEnd = "0.005778";
        String lastPredCol = "NaN";
        String lastPredEnd = "NaN";
          
        com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        //fam.setAgeBy(1);
       // fam.setAgeTo(95);
        

         try {
            fam = Utils.createRiskFromFile("test/exampleMMRFamily217.xml", com.objex.risk.beans.uno.Risk.class);
            fam.setSaveIncoming(true);
            Risk mmrfam = Utils.createRiskFromFile("test/exampleMMRFamily217.xml", com.objex.risk.beans.mmr.Risk.class);
            for (int i =0; i < mmrfam.getFamily().size(); i++) { 
                if (mmrfam.getFamily().get(i).getAffectedColonType() == 0) {
                    fam.getFamily().get(i).setAge(mmrfam.getFamily().get(i).getDiagnoseColonAge());
                } else {
                    fam.getFamily().get(i).setAge(mmrfam.getFamily().get(i).getDiagnoseEndometriumAge());
                }
            }
            fam.getFamily().get(2).addCancer(new Cancer(CancerType.COLORECTAL,1,57));
            fam.getFamily().get(13).addCancer(new Cancer(CancerType.COLORECTAL,1,1));
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoTyrerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(95);
        model.setName(ModelName.MMRPRO);
        //model.setChartCode(1);
        Instruction ins = new Instruction();
        ins.setName("impute");
        ins.setValue("true");
        model.getInstructions().add(ins);
        //model.setChartCode(1);
        fam.getRequestedModels().add(model);
        //baseAsserts(fam);.
        
             
        //Utils.saveToFile("C:\\temp\\comb.json", fam, false);
        CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        MMRResult result = (MMRResult) combresult.getResults().get(ModelName.MMRPRO);
        //GailResult result = (GailResult) combresult.getGailResult();
        
                resultChecks(result, predCo, predEnd, lastPredCol, lastPredEnd, mlh, msh2, msh6);
        }
    
    protected void resultChecks(MMRResult result,String predCol, String predEnd, String lastPredCol, String lastPredEnd, String mlh1, String msh2, String msh6) {
        double delta = .00001;
        
        assertEquals(Double.parseDouble(predCol),fmt(result.getPredictions().get(0).getResult(MMRResult.COLORECTAL_RISK), 9),delta);
        assertEquals(Double.parseDouble(predEnd),fmt(result.getPredictions().get(0).getResult(ENDOMETRIAL_RISK), 10),delta); 
        assertEquals(Double.parseDouble(lastPredCol),fmt(result.getPredictions().get(result.getPredictions().size() - 1).getResult(MMRResult.COLORECTAL_RISK), 10),delta); 
        assertEquals(Double.parseDouble(lastPredEnd),fmt(result.getPredictions().get(result.getPredictions().size() - 1).getResult(MMRResult.ENDOMETRIAL_RISK), 10),delta); 
        
        assertEquals(Double.parseDouble(mlh1),fmt(result.getProbabilityMutationMLH1(), 10),delta);
        assertEquals(Double.parseDouble(msh2),fmt(result.getProbabilityMutationMSH2(), 10),delta);
        assertEquals(Double.parseDouble(msh6),fmt(result.getProbabilityMutationMSH6(), 10),delta);


     }
     
 
    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(risk.getAgeBy(), 5);
        assertEquals(risk.getFamily().size(), 21);
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
            baseFamily = Utils.createRiskFromFile("test/exampleMMRFamily217.xml", Risk.class);
            base2014Family = Utils.createRiskFromFile("test/exampleMMR2014Family.xml", Risk.class);
              //base2014Family = Utils.createRiskFromFile("test/risk_1450902090104.txt", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoMMR218Service.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestUnoMMR218Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
}
