/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.tcuzick.TyrerResult;
import com.objex.risk.beans.tcuzick.Person;
import com.objex.risk.beans.tcuzick.Risk;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.Germline;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.charts.ChartResult;
import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.charts.Series;
import com.objex.risk.beans.tcuzick.BIRADS;
import com.objex.risk.beans.tcuzick.Person.HyperplasiaType;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;

/**
 *
 * @author jason
 */
public class TestUnoTyrerService extends TestCase {
    private Risk baseFamily = null;
    private String url = "http://localhost:8086/";
    //private String url = "http://localhost:8085/ProgenyRiskService/services/";
    //private String url = "http://claus.test.riskcalculation.progenygenetics.com:8080/ProgenyRiskService/services/";
    //private String url = "http://10.10.4.163:8080/ProgenyRiskService/services/";  //care uno server
    //private String url = "http://64.58.145.76:8080/ProgenyRiskService/services/";  //ambry test
   // private String url = "http://64.58.145.108:8080/ProgenyRiskService/services/";  //ambry prod
  // private String url = "http://52.91.83.98:8080/ProgenyRiskService/services/";
   // private String url = "http://10.3.26.11:8080/ProgenyRiskService/services/";
    
    static long completedAttempts;
    
   
    /**
     * mom affected at 22 proband is 39
     */
    
         /**
      * 35 no problems no family with problems
     */
    public void testBaseFamily()
    {
               String predBr = "0.00271960";
               String lastpredBr = "0.09299350";
               String br1 = "0.00050183";
        String br2 = "0.0011646";
        String prob = "0.0016665";
       

        //Risk fam = copyBase(); 
        com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
       // fam.setAgeTo(90);
        com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
      
        try {
            fam = Utils.createRiskFromFile("test/exampleTyrerFamily.xml", com.objex.risk.beans.uno.Risk.class);
            //proband.setAge(35);
            //proband.setMenarcheAge(13);
            //fam.getFamily().add(proband);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoTyrerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(85);
        model.setName(ModelName.TYRER);
        model.setChartCode(1);
        fam.getRequestedModels().add(model);
        
        fam.setSaveIncoming(true);
        
        fam.setAgeBy(5);
        fam.setChartCode(BaseRisk.DEFAULT_CHART);
        fam.setSaveIncoming(false);
        
         CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        TyrerResult result = (TyrerResult) combresult.getResults().get(Model.ModelName.TYRER);
        
        assertEquals(br1,fmt(result.getProbabilityMutationBRCA1(), 10));
        assertEquals(br2,fmt(result.getProbabilityMutationBRCA2(), 9));
        assertEquals(prob,fmt(result.getProbabilityCarrier(), 9));
        
        
         assertEquals(predBr,fmt(result.getPredictions().get(0).getResult("breastRisk"), 10));
         assertEquals(lastpredBr,fmt(result.getPredictions().get(result.getPredictions().size() - 1).getResult("breastRisk"), 10));  
    }
    
        public void setUp()
    {
        System.out.println("creating Base Family");
        try {
            
            
            
            baseFamily = Utils.createRiskFromFile("test/exampleTyrerFamily.xml", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoTyrerService.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestUnoTyrerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
     public String fmt(Double numb, int len)
    {
        if (numb == null) {
            return "NaN";
        }
        String resp = new BigDecimal(numb).toPlainString();
        if (resp.length() > len)
             return resp.substring(0,len);
        return resp;
    }
        public String fmt(Double numb)
    {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(3, RoundingMode.HALF_UP); 
        return a_dec.toPlainString();
    }
    
    
}
