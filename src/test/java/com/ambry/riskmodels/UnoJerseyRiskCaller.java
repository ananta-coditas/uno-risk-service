/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.premm.Person;
import com.objex.risk.beans.premm.Risk;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.uno.CombinedResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import javax.xml.bind.JAXBException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

/**
 *
 * @author jason
 */
public class UnoJerseyRiskCaller {
   //   static final String REST_URI = "http://localhost:8085/ProgenyRiskService/services/";
   //  static String REST_URI = "http://192.168.1.119:8080/RiskServiceWeb/services/";
     //static final String REST_URI = "http://64.58.145.76:8080/RiskServiceWeb/services/";
    static final String REST_URI = "http://64.58.145.76:8080/ProgenyRiskService/services/";
      //static final String REST_URI = "http://54.146.150.253/ProgenyRiskService/services/
      //static final String REST_URI = "http://54.146.150.253/ProgenyRiskService/services/";
     
   
    public static void main(String[] args ) throws InterruptedException
    {
         int attempts =  200;
        Thread.sleep(1000); //give me time to switch contexts
        for (int i = 0; i < attempts; i++) {
            final int attemptNum = i;
            Thread instance = new Thread() {
            final int myAttempt = attemptNum;
            public void run() {
                PREMMResult result = null;
                long time = System.currentTimeMillis();
                System.out.println("starting calcBRCA");
                //WebClient client = WebClient.create(REST_URI + "?token=mydemoToken");
                //Client client = ClientBuilder.newClient();
                                //.register(JacksonFeature.class)

                Risk risk = new Risk();
                risk.setSaveIncoming(false);
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

                result = (PREMMResult) callService(risk, REST_URI,"calculate/premm",PREMMResult.class);
                long mytime = (System.currentTimeMillis() - time) /1000;
                if (mytime > 6)
                    System.out.println("****************************************long time " + mytime);

                System.out.println("time is " + mytime + " seconds for attempt:" + myAttempt);
                System.out.println(result.getProbabilityCarrier());
                System.out.println(result.getProbabilityMutationMLH1());
                System.out.println(result.getProbabilityMutationMSH2());
                System.out.println(result.getProbabilityMutationMSH6()); 
                
            }
            };
           Thread.sleep(10);
           instance.start();
        }
    }
    
        public static BaseResult callService(BaseRisk risk, String uri, String path,Class resultClass) {
        Client client = ClientBuilder.newClient();
        Response resp =  client.target(uri).path(path).request().post(Entity.entity(risk, MediaType.APPLICATION_JSON));
        
        //Response resp =  client.target(uri).path(path).request().accept(MediaType.APPLICATION_XML).post(Entity.entity(risk, MediaType.APPLICATION_XML));
        BaseResult result = (BaseResult)resp.readEntity(resultClass);
        return result;  
    }
    public static BaseResult callService(BaseRisk risk, String uri, String path,Class resultClass,String mediaType) {
        Client client = ClientBuilder.newClient();
        //Response resp =  client.target(uri).path(path).request().post(Entity.entity(risk, MediaType.APPLICATION_JSON));
        
        Response resp =  client.target(uri).path(path).request().accept(mediaType).post(Entity.entity(risk, mediaType));
        BaseResult result = (BaseResult)resp.readEntity(resultClass);
        return result;  
    }
    public static CombinedResult callService(BaseRisk risk, String uri, String path,String mediaType) {
        //Client client = ClientBuilder.newClient();
        //Response resp =  client.target(uri).path(path).request().post(Entity.entity(risk, MediaType.APPLICATION_JSON));
       RestClient defaultClient = RestClient.create();
       
           String json = new String();
        try {
            json = new ObjectMapper().writeValueAsString(risk);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(UnoJerseyRiskCaller.class.getName()).log(Level.SEVERE, null, ex);
        }

 //  and simple print it

    System.out.println(json);
       
       ResponseEntity<CombinedResult> response = defaultClient.post() 
  .uri(uri + path) 
  .contentType(org.springframework.http.MediaType.APPLICATION_JSON) 
  .body(risk).retrieve().toEntity(CombinedResult.class);
 
       CombinedResult result = response.getBody();
        //Response resp =  client.target(uri).path(path).request().accept(mediaType).post(Entity.entity(risk, mediaType));
        //CombinedResult result = resp.readEntity(CombinedResult.class);
        return result;  
    }
    public static BaseResult callService(BaseResult inresult, String uri, String path,Class resultClass) {
        Client client = ClientBuilder.newClient();
        Response resp =  client.target(uri).path(path).request().post(Entity.entity(inresult, MediaType.APPLICATION_JSON));
        BaseResult result = (BaseResult)resp.readEntity(resultClass);
        return result;  
    }
     public static BaseResult callService(ChartVO chart, String uri, String path,Class resultClass) {
        Client client = ClientBuilder.newClient();
        Response resp =  client.target(uri).path(path).request().post(Entity.entity(chart, MediaType.APPLICATION_JSON));
        BaseResult result = (BaseResult)resp.readEntity(resultClass);
        return result;  
    }
    public static BaseResult callService(ChartVO chart, String uri, String path,Class resultClass,  MediaType mediaType) {
        Client client = ClientBuilder.newClient();
        Response resp =  client.target(uri).path(path).request().post(Entity.entity(chart, mediaType));
        BaseResult result = (BaseResult)resp.readEntity(resultClass);
        return result;  
    }
    public static BaseResult callService(BaseRisk risk, String path, Class resultClass) {
        return callService(risk, REST_URI, path, resultClass);
    }
    
    public static <T extends BaseRisk> T createRisk(String fileName, Class<T> clazz) {
          try {
              return  Utils.createRiskFromFile(fileName, clazz);
          } catch (Exception ex) {
              Logger.getLogger(UnoJerseyRiskCaller.class.getName()).log(Level.SEVERE, null, ex);
          }
          return null;
    }
    
}
