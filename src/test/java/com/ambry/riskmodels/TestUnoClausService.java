/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.claus.Person;
import com.objex.risk.beans.claus.Risk;
import com.objex.risk.beans.AffectedType;
import com.objex.risk.beans.uno.Cancer;
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
public class TestUnoClausService extends TestCase {
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
    
   
    protected Risk simpleFDR(int probandAge, int fathAge, int momAge) {
        return simpleFDR(probandAge, fathAge, momAge,22);
    }

    protected Risk simpleFDR(int probandAge, int fathAge, int momAge, int motherAffectedAge) {
        Risk risk = new Risk();
        risk.setAgeTo(79);
        // risk.setSaveIncoming(true);
        Person person = new Person();
        person.setId(1);
        person.setFatherId(2);
        person.setMotherId(3);
        person.setGender(0);
        person.setAge(probandAge);
        risk.add(person);
        person = new Person();
        person.setId(2);
        person.setGender(1);
        person.setAffectedBreastType(0);
        person.setAge(fathAge);
        risk.add(person);
        person = new Person();
        person.setId(3);
        if (motherAffectedAge > 0) {
            person.setAffectedBreastType(1);
            person.setDiagnoseBreastAge(motherAffectedAge);
        }
        person.setAge(momAge);
        person.setGender(0);
        risk.add(person);
        return risk;
    }
    
    //aunt affected at 22
    
    protected Risk affectedAuntSDR(int probandAge, int fathAge, int momAge) {
        Risk risk = new Risk();
        risk.setAgeTo(79);
        // risk.setSaveIncoming(true);
        Person person = new Person();
        person.setId(1);
        person.setFatherId(2);
        person.setMotherId(3);
        person.setGender(0);
        person.setAge(probandAge);
        risk.add(person);
        
        person = new Person();
        person.setId(2);
        person.setGender(1);
        person.setAffectedBreastType(0);
        person.setAge(fathAge);
        person.setFatherId(4);
        person.setMotherId(5);
        risk.add(person);
        
        
        person = new Person();
        person.setId(3);
        person.setAge(momAge);
        person.setGender(0);
        risk.add(person);
        
        person = new Person();
        person.setId(4);
        person.setAge(81);
        person.setGender(0);
        risk.add(person);
        
         person = new Person();
        person.setId(5);
        person.setAge(82);
        person.setGender(0);
        risk.add(person);
        
         person = new Person();
        person.setId(6);
        person.setGender(0);
        person.setAffectedBreastType(1);
        person.setDiagnoseBreastAge(22);
        person.setAge(fathAge + 2);
        person.setFatherId(4);
        person.setMotherId(5);
        risk.add(person);
        
        return risk;
    }
    
  
    protected void baseAsserts(Risk risk) {
        assertNotNull(risk);
        assertEquals(risk.getAgeBy(), 5);
        assertEquals(risk.getFamily().size(), 21);
    }
    public String fmt(Double numb)
    {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(3, RoundingMode.HALF_UP); 
        return a_dec.toPlainString();
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

            
            
            
            baseFamily = Utils.createRiskFromFile("test/exampleClausFamily.xml", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoClausService.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestUnoClausService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
        public void testCombinedBaseFamily()
    {
      
        String predBr = "0.004";
        String lastpredBr = "0.115";
        com.objex.risk.beans.uno.Risk risk = simpleFDRCombined(29,62,60);
        
        
        risk.getFamily().get(2).getCancer(Cancer.CancerType.BREAST).setAffected(AffectedType.NONE.value);
        
        com.objex.risk.beans.uno.Person  person = new com.objex.risk.beans.uno.Person();
        person.setId(4);
        Cancer ovarian = new Cancer();
        ovarian.setCancerType(Cancer.CancerType.OVARIAN);
        ovarian.setAffected(1);
        ovarian.setDiagnoseAge(29);
        person.getCancers().add(ovarian);
        //person.setAffectedOvarianType(1);
        //person.setDiagnoseOvarianAge(29);
        person.setAge(42);
        person.setGender(0);
        person.setFatherId(2);
        person.setMotherId(3);
        risk.add(person);
       
       // com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        //com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
        //proband.setAge(38);
        //fam.getFamily().add(proband);
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(79);
        model.setName(ModelName.CLAUS);
        model.setChartCode(1);
        risk.getRequestedModels().add(model);
        
 
        
             
        Utils.saveToFile("C:\\temp\\comb.json", risk, false);
        CombinedResult combresult = UnoJerseyRiskCaller.callService(risk, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        ClausResult result = (ClausResult) combresult.getResults().get(ModelName.CLAUS);
       //ClausResult result = (ClausResult) combresult.getClausResult();
        
         assertEquals(predBr,fmt(result.getPredictions().get(0).getResult("breastRisk")));
         //assertEquals(lastpredBr,fmt(result.getPredictions().get(result.getPredictions().size() - 1).getResult("breastRisk"), 10));  
    }

    private com.objex.risk.beans.uno.Risk simpleFDRCombined(int probandAge, int fathAge, int momAge) {
        return simpleFDRCombined(probandAge, fathAge, momAge,22);
    }

    private com.objex.risk.beans.uno.Risk simpleFDRCombined(int probandAge, int fathAge, int momAge, int motherAffectedAge) {
        com.objex.risk.beans.uno.Risk risk = new com.objex.risk.beans.uno.Risk();
        risk.setAgeTo(79);

        // risk.setSaveIncoming(true);
        com.objex.risk.beans.uno.Person person = new com.objex.risk.beans.uno.Person();
        person.setId(1);
        person.setFatherId(2);
        person.setMotherId(3);
        person.setGender(0);
        person.setAge(probandAge);
        risk.add(person);
        person = new com.objex.risk.beans.uno.Person();
        person.setId(2);
        person.setGender(1);
        //person.setAffectedBreastType(0);
        person.setAge(fathAge);
        risk.add(person);
        person = new com.objex.risk.beans.uno.Person();
        person.setId(3);
        if (motherAffectedAge > 0) {
            Cancer cancer = new Cancer();
            cancer.setCancerType(Cancer.CancerType.BREAST);
            cancer.setAffected(1);
            cancer.setDiagnoseAge(motherAffectedAge);
           // person.setAffectedBreastType(1);
            //person.setDiagnoseBreastAge(motherAffectedAge);
        }
        person.setAge(momAge);
        person.setGender(0);
        risk.add(person);
        return risk;
    }
    
}
