/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.premm.Person;
import com.objex.risk.beans.premm.Risk;
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
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 *
 * @author jason
 */
public class TestUnoPREMMService extends TestCase {
    private Risk baseFamily = null;
    //private String url = "http://localhost:8085/ProgenyRiskService/services/";
    //private String url = "http://localhost:8080/";
 //   private String url = "http://10.10.4.163:8080/ProgenyRiskService/services/";  //care uno server
    //private String url = "http://premm.riskcalculation.test.progenygenetics.com:8080/ProgenyRiskService/services/";
    //private String url = "http://premm.riskcalculation.progenygenetics.com:8080/ProgenyRiskService/services/";
    // private String url = "http://64.58.145.76:8080/ProgenyRiskService/services/";
    private String url = "http://localhost:8086/";
    //private String url = "http://64.58.145.76:8080/ProgenyRiskService/services/";  //ambry test
      //private String url = "http://64.58.145.108:8080/ProgenyRiskService/services/";  //ambry prod
     // private String url = "http://54.163.244.67/ProgenyRiskService/services/"; //test windows server
     //private String url = "http://52.91.83.98:8080/ProgenyRiskService/services/"; //prod
      //private String url = "http://52.91.159.138/ProgenyRiskService/services/"; //windows test
      //private String url = "http://10.3.26.12:8080/ProgenyRiskService/services/"; //ambry test
      // private String url = "http://10.3.26.11:8080/ProgenyRiskService/services/"; //ambry prod
    
     static long completedAttempts;
    
    public void testBaseFamily()
    {
        String prob = "0.68977281";
        String mlh = "0.44465775";
        String msh2 = "0.23920116";
        String msh6 = "0.00591389";
        //Risk fam = copyBase();
        
        com.objex.risk.beans.uno.Risk fam = new com.objex.risk.beans.uno.Risk();
        
        Model model = new Model();
        model.setAgeBy(5);
        model.setAgeTo(79);
        model.setName(Model.ModelName.PREMM);
        fam.getRequestedModels().add(model);
        //fam.setAgeTo(90);
        com.objex.risk.beans.uno.Person proband = new com.objex.risk.beans.uno.Person();
        proband.setFatherId(2);
        proband.setGender(1);
        proband.setId(1);
        proband.setAge(40);
        fam.add(proband);
        fam.setSaveIncoming(true);
        
       com.objex.risk.beans.uno.Person father = new com.objex.risk.beans.uno.Person();
       Cancer colon = new Cancer();
       father.setGender(1);
       colon.setCancerType(Cancer.CancerType.COLORECTAL);
       colon.setAffected(AffectedType.ONE.value);
       father.setId(2);
       colon.setDiagnoseAge(25);
      // father.setAffectedColonType(AffectedType.ONE);
       //father.setDiagnoseColonAge(25);
       father.getCancers().add(colon);
       
       proband.getCancers().add(colon);
       
       fam.add(father);
        //baseAsserts(fam);
        
              /*
        
                Risk risk = new Risk();
               // risk.setSaveIncoming(true);
                Person person = new Person();
                person.setId(1);
                person.setFatherId(2);
                person.setMotherId(3);
                person.setGender(1);
                person.setAffectedColonType(1);
                 person.setDiagnoseColonAge(25);
                risk.add(person);

                person = new Person();
                person.setId(2);
                person.setGender(1);
                  person.setAffectedColonType(1);
                person.setDiagnoseColonAge(25);
               risk.add(person);

                 person = new Person();
                 person.setId(3);
                 person.setAffectedColonType(0);
                person.setDiagnoseColonAge(0);
                person.setGender(0);
                risk.add(person);

                person = new Person();
                person.setId(4);
                person.setFatherId(2);
                person.setMotherId(3);
                risk.add(person);

               // result = (PREMMResult) callService(risk, REST_URI,"calculate/premm",PREMMResult.class);
                 */
        
        
         CombinedResult combresult = UnoJerseyRiskCaller.callService(fam, url,"api/risk/models/calculate",MediaType.APPLICATION_JSON);
        PREMMResult result = (PREMMResult) combresult.getResults().get(ModelName.PREMM);
       //PREMMResult result = (PREMMResult) combresult.getPremmResult();
        assertEquals(prob,fmt(result.getProbabilityCarrier(), 10));
        assertEquals(mlh,fmt(result.getProbabilityMutationMLH1(), 10));
        assertEquals(msh2,fmt(result.getProbabilityMutationMSH2(), 10));
        assertEquals(msh6,fmt(result.getProbabilityMutationMSH6(), 10));
        

       // assertEquals(predCo,fmt(result.getPredictions().get(0).getResult(MMRResult.COLORECTAL_RISK), 9));
        //assertEquals(predEnd,fmt(result.getPredictions().get(0).getResult(ENDOMETRIAL_RISK), 10)); 
    }    
       public void testLoad() throws JAXBException, InterruptedException
    {
                String predBr = "0.00271960";
               String lastpredBr = "0.09299350";
               String br1 = "0.00050183";
        String br2 = "0.0011646";
        String prob = "0.0016665";
    
       final int attempts =  100;
        Thread.sleep(1000); //give me time to switch contexts
        final long starttime = System.currentTimeMillis();
        for (int i = 0; i < attempts; i++) {
            final int attemptNum = i;
            Thread instance = new Thread() {
            final int myAttempt = attemptNum;
            final Risk risk =  baseFamily; ////Utils.createRiskFromFile("test/tyrerrisAJ.txt", Risk.class);
            public void run() {
                PREMMResult result = null;
                long time = System.currentTimeMillis();
                result = (PREMMResult)UnoJerseyRiskCaller.callService(risk, url,"calculate/premm",PREMMResult.class);
                long mytime = (System.currentTimeMillis() - time) /1000;
                if (mytime > 6)
                    System.out.println("****************************************long time " + mytime);

                System.out.println("time is " + mytime + " seconds for attempt:" + myAttempt);
                System.out.println(result.getProbabilityCarrier());
                completedAttempts++;
                if (completedAttempts  == attempts) {
                    long mytottime = (System.currentTimeMillis() - starttime);
                     System.out.println("total time including sleeps: " + (10 * attempts) + " is " + mytottime + " attempts:" + attempts);
                }
                
                
            }
            };
           Thread.sleep(10);
           instance.start();
        }
        
    }
     public String fmt(Double numb)
    {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(3, RoundingMode.HALF_UP); 
        return a_dec.toPlainString();
    }
      public String fmtE(Double numb)
    {
        if (numb == null) {
            return "NULL RESULT";
        }
        BigDecimal a_dec = new BigDecimal(numb).setScale(4, RoundingMode.HALF_UP); 
        return fmt(a_dec.doubleValue(),5);
        //return a_dec.toPlainString();
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
            baseFamily = Utils.createRiskFromFile("test/examplePREMMFamily.xml", Risk.class);
        } catch (JAXBException ex) {
            Logger.getLogger(TestUnoPREMMService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public Risk copyBase()
    {
        Risk fam = null;
        try {
            //Utils.
            // return (Risk) baseFamily.clone();
            fam =  new Risk(); //(Risk)baseFamily.clone();
       } catch (Exception ex) {
            Logger.getLogger(TestUnoPREMMService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fam;
    }
    
}
