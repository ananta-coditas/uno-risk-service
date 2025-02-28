/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.Germline;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.brca.Marker;
import com.objex.risk.beans.brca.Person;
import com.objex.risk.beans.brca.Risk;
import com.objex.risk.beans.brca.BRCAResult;
import com.objex.risk.beans.uno.Cancer;
import com.objex.risk.beans.uno.CancerDetails;
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
 *  changed
 * IMPUTRELATEVES is off in documentation.
 * @author jason
 */
public class TestUnoBRCA218Service extends TestCase {
    private Risk baseFamily = null;
    private Risk base2014Family = null;
    com.objex.risk.beans.uno.Risk unoFamily = null;
    static long completedAttempts;
    String url = "http://localhost:8086/";
    //private String url = "http://10.10.4.163:8080/RiskServiceWeb/services/";  //care uno server
  // private String url = "http://localhost:8088/RiskServiceWeb/services/";
     //private String url = "http://192.168.1.119:8080/RiskServiceWeb/services/";
    //private String url = "http://10.3.26.21:8080/RiskServiceWeb/services/"; //rocky test
    //private String url = "http://64.58.145.76:8080/RiskServiceWeb/services/";  //ambry test
   //private String url = "http://64.58.145.108:8080/RiskServiceWeb/services/";  //ambry prod
    //private String url = "http://10.3.29.22:80/RiskServiceWeb/services/";  //my ambry dev server
   // private String url = "http://52.91.83.98:8080/RiskServiceWeb/services/";
    
  // private String url = "http://bayes.riskcalculation.progenygenetics.com:8080/RiskServiceWeb/services/";
     
     //private String url = "http://riskcalculation.test.progenygenetics.com:8080/RiskServiceWeb/services/";
    //private String url = "http://10.3.26.18:8080/RiskServiceWeb/services/"; //ambry test
   // private String url = "http://10.3.26.11:8080/RiskServiceWeb/services/"; //ambry prod
    /**
     * this is the basefamily test.  the default for this has no changes from xml
     */
    
     public void testBaseFamily()
    {
        String br1 = "0.13316";
        String br2 = "0.15373";
        String predBr = "0.04633";
        String predOv = "0.02369";
        
        String lastpredBr = "0.19324";
        String lastpredOv = "0.10299";
        com.objex.risk.beans.uno.Risk fam = unoFamily; //new com.objex.risk.beans.uno.Risk();
        fam.setAgeTo(90);
        
         try {
            fam = Utils.createRiskFromFile("test/exampleBRCAFamily217.xml", com.objex.risk.beans.uno.Risk.class);
            com.objex.risk.beans.brca.Risk brcafam = Utils.createRiskFromFile("test/exampleBRCAFamily217.xml", com.objex.risk.beans.brca.Risk.class);
            for (int i =0; i < brcafam.getFamily().size(); i++) { 
                if (brcafam.getFamily().get(i).getAffectedBreastType() == 0) {
                    fam.getFamily().get(i).setAge(brcafam.getFamily().get(i).getDiagnoseBreastAge());
                } else {
                    fam.getFamily().get(i).setAge(brcafam.getFamily().get(i).getDiagnoseOvarianAge());
                }
            }
            fam.setSaveIncoming(true);
            
            fam.getFamily().get(1).addCancer(new Cancer(Cancer.CancerType.OVARIAN,1,69));
            fam.getFamily().get(6).addCancer(new Cancer(Cancer.CancerType.BREAST,1,45));
            fam.getFamily().get(7).addCancer(new Cancer(Cancer.CancerType.OVARIAN,1,65));
            fam.getFamily().get(13).addCancer(new Cancer(Cancer.CancerType.BREAST,1,1));
            Cancer bilateral = new Cancer(Cancer.CancerType.BREAST,1,40);
            CancerDetails details = new CancerDetails();
            details.setContralateral(AffectedType.ONE);
            details.setContralateralAge(45);
            bilateral.setCancerDetails(details);
            fam.getFamily().get(21).addCancer(bilateral);

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
        model.setName(ModelName.BRCAPRO);
        //model.setChartCode(1)
        fam.getRequestedModels().add(model);
        //baseAsserts(fam);.
        
             
        //Utils.saveToFile("C:\\temp\\comb.json", fam, false);
        CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        BRCAResult result = (BRCAResult) combresult.getResults().get(ModelName.BRCAPRO);
        //GailResult result = (GailResult) combresult.getGailResult();
        
         resultChecks(predBr, result, predOv, lastpredBr, lastpredOv, br1, br2);
    }

    protected void resultChecks(String predBr, BRCAResult result, String predOv, String lastpredBr, String lastpredOv, String br1, String br2) {
        double delta = .0001;
        assertEquals(Double.parseDouble(predBr),fmt(result.getPredictions().get(0).getResult("breastRisk"), 7),delta);
        assertEquals(Double.parseDouble(predOv),fmt(result.getPredictions().get(0).getResult("ovarianRisk"), 7),delta); 
        
        int lastIndex = result.getPredictions().size() - 1;
        assertEquals(Double.parseDouble(lastpredBr),fmt(result.getPredictions().get(lastIndex).getResult("breastRisk"), 7),delta);
        assertEquals(Double.parseDouble(lastpredOv),fmt(result.getPredictions().get(lastIndex).getResult("ovarianRisk"), 7),delta); 
        
        assertEquals(Double.parseDouble(br1),fmt(result.getProbabilityMutationBRCA1(), 7),delta);
        assertEquals(Double.parseDouble(br2),fmt(result.getProbabilityMutationBRCA2(), 7),delta);
    }
    
  
    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(risk.getAgeBy(), 5);
        assertEquals(risk.getFamily().size(), 25);
    }
    public double fmt(Double numb, int len)
     {
        if (numb != null && new BigDecimal(numb.doubleValue()).toPlainString().length() > len)
             return Double.parseDouble(new BigDecimal(numb.doubleValue()).toPlainString().substring(0,len));
        return numb.doubleValue();
    }
    public void setUp()
    {
        System.out.println("creating Base Family");
        try {
           baseFamily = Utils.createRiskFromFile("test/exampleBRCAFamily217.xml", Risk.class);
            base2014Family = Utils.createRiskFromFile("test/exampleBRCA2014Family.xml", Risk.class);
            unoFamily =  Utils.createRiskFromFile("test/exampleBRCAFamily217.xml", com.objex.risk.beans.uno.Risk.class);
            System.out.println(unoFamily);
           //base2014Family = Utils.createRiskFromFile("test/badBRCA.xml", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoBRCA218Service.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestUnoBRCA218Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
}
