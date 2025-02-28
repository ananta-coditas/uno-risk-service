/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.gail.Person;
import com.objex.risk.beans.gail.Risk;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.warehouse.RiskInputs;
import com.objex.warehouse.RiskResults;
import com.objex.warehouse.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * the example family in this project does not match  the one in FHQ due to the age difference
 * of 90 to use the tool
 * @author jason
 */
public class TestUnoGailService extends TestCase {
    private Risk baseFamily = null;
    
     String url = "http://localhost:8086/";
    //private String url = "http://localhost:8085/ProgenyRiskService/services/";
    //private String url = "http://localhost:8085/progenyriskservice/v1/";
    
    
     //private String url = "http://64.58.145.76:8080/ProgenyRiskService/services/";
    //private String url = "http://64.58.145.76:8080/ProgenyRiskService/services/"; //ambry test
    //private String url = "http://64.58.145.108:8080/ProgenyRiskService/services/";  //ambry prod
   //private String url = "http://10.10.4.163:8080/ProgenyRiskService/services/";  //care uno server
    
//private String url = "http://gail.test.riskcalculation.progenygenetics.com:8080/ProgenyRiskService/services/";
    //private String url = "http://bayes.riskcalculation.progenygenetics.com:8080/ProgenyRiskService/services/";
     //private String url = "http://gail.riskcalculation.progenygenetics.com:8080/ProgenyRiskService/services/";
    
    public void testCombinedBaseFamily()
    {
        String predBr = "0.003";
        String lastpredBr = "0.11340920";
       
        com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        fam.setAgeTo(90);
        com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
        proband.setAge(35);
        proband.setMenarcheAge(13);
        proband.setRace(Person.Race.White);
        fam.getFamily().add(proband);
        fam.setSaveIncoming(true);
        
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(90);
        model.setName(ModelName.GAIL);
        model.setChartCode(1);
        fam.getRequestedModels().add(model);
        //baseAsserts(fam);.
        
             
        Utils.saveToFile("C:\\temp\\comb.json", fam, false);
        CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        GailResult result = (GailResult) combresult.getResults().get(ModelName.GAIL);
        //GailResult result = (GailResult) combresult.getGailResult();
        
         assertEquals(predBr,fmt(result.getPredictions().get(0).getResult("breastRisk")));
         assertEquals(lastpredBr,fmt(result.getPredictions().get(result.getPredictions().size() - 1).getResult("breastRisk"), 10));  
    }
    
   
    public String fmt(Double numb)
    {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(3, RoundingMode.HALF_UP); 
        return a_dec.toPlainString();
    }

  
    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(risk.getAgeBy(), 5);
        assertEquals(risk.getFamily().size(), 21);
    }
    public String fmt(Double numb, int len)
    {
        if (numb != null && numb.toString().length() > len)
             return numb.toString().substring(0,len);
        return numb.toString();
    }
    public void setUp()
    {
        System.out.println("creating Base Family");
        try {
              Risk risk = new Risk();
                risk.setSaveIncoming(true);
                Person person = new Person();
                person.setId(1);
                person.setFatherId(2);
                person.setMotherId(3);
                person.setGender(0);
                person.setAffectedBreastType(1);
                risk.add(person);

                person = new Person();
                person.setId(2);
                person.setGender(1);
                person.setAffectedBreastType(0);
               risk.add(person);

                 person = new Person();
                 person.setId(3);
                 person.setAffectedBreastType(1);
                
                person.setGender(0);
                risk.add(person);

                person = new Person();
                person.setId(4);
                person.setFatherId(2);
                person.setMotherId(3);
                risk.add(person);

            
            
            
            baseFamily = Utils.createRiskFromFile("test/exampleGailFamily.xml", Risk.class);
             Person prob = baseFamily.getFamily().get(0);
      
        prob.setRace(Person.Race.White);
            
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoGailService.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestUnoGailService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
}
