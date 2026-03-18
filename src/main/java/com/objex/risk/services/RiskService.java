/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.risk.services;

import com.ambry.config.ApplicationProperties;
import com.objex.risk.Utils;
import com.objex.risk.beans.*;
import com.objex.risk.beans.brca.BRCAResult;
import com.objex.risk.beans.brca.Marker;
import com.objex.risk.beans.brca.Person;
import com.objex.risk.beans.brca.Risk;
import com.objex.risk.beans.charts.ChartResult;
import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.mela.MelaResult;
import com.objex.risk.beans.mmr.MMRResult;
import com.objex.risk.beans.panc.PancResult;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * REST Web Service
 *
 * @author jason
 */
//@Path("/calculate")
@RestController
//@RequestMapping("/bayescalculate")
public class RiskService {

    final static Logger LOG = LoggerFactory.getLogger(RiskService.class);
    public static final String BAYES_VERSION = "BayesMendel v2.1.8 © 2020 DFCI. All Rights Reserved.";
    private static int maxConnections = 0;
    private static int connectionCount = 0;
    private final static Object conLock = new Object();
    private static List<RConnection> connections = Collections.synchronizedList(new ArrayList<RConnection>());
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static boolean connectionPooling = true;

    //@Context
    //private ServletContext context;
    private static boolean saveRequests = false;
    private static String savePath = null;
    private static boolean spawnServer = false;
    private static String spawnCommand;

    private final ApplicationProperties applicationProperties;

    public RiskService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }


   /**
     * This is the entrypoint for the pancPro model program.  Passing it a risk populated
     * with necessary data will bridge to the bayesmendel  pancPro program and return
     * the predictions and carrier probabilities for pancPro.
     * @param risk
     * @return
     */
   // @POST
    //@Path("panc")
    //@Consumes({"application/xml",MediaType.APPLICATION_JSON})
    //@Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/panc", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
   // public Response calcPanc(com.objex.risk.beans.panc.Risk risk) {
     public PancResult calcPanc(@RequestBody com.objex.risk.beans.panc.Risk risk) {
        RConnection con = null;
        long time = System.currentTimeMillis();
        PancResult result = new PancResult();
        REXP x = null;
        try
        {
            StringBuilder inlineParms = new StringBuilder();
            checkForSaveOff(risk);
            validatePancData((com.objex.risk.beans.panc.Person) risk.getProband(), result, risk);
            if (result.hasErrors()) {
                result.setVersion(BAYES_VERSION);
                return result;
            }
            LOG.info("Requesting Connection to RServe.");
            con =  createConnection(spawnServer);
            if(verify(con)) {
                LOG.info("Connected to RServe.");
                long loadBegin = System.currentTimeMillis();
                con.voidEval("library('BayesMendel')");
                long loadTime = System.currentTimeMillis() - loadBegin;
                LOG.debug("Time to load models: " + loadTime + " milliseconds");

                String params = setupParams(risk);
                LOG.info("params: " + params);
                con.voidEval(params);

                con.assign("fam", RiskService.this.createFamilyFrame(risk));
                con.voidEval("result <- pancpro(family=fam,params=pancParams)");
                result = buildResult(con, result);
                result.setChartCode(risk.getChartCode());
                buildChartForResult(result);


                /*REPORTING AND DEBUGGING
                //RList fam  = con.eval("result@family").asList();
                RList fam  = con.eval("panc.fam").asList();
                //log.info("result family");
                Utils.saveToPancFile("C:/temp/pancfam.xml", fam);
                //LOG.info(Utils.dataFrameToString(fam)); */
            }
            long total = System.currentTimeMillis() - time;
            LOG.info("Panc Risk web Service time is " + total + " milliseconds");

        }
        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            throw new ServerErrorException(e.getMessage(), e); //return Response.serverError().build();
        }
        finally  {
            close(con);
        }
        return result; //Response.ok(result).build();
    }

     /**
     * This is the entrypoint for the melaPro model program.  Passing it a risk populated
     * with necessary data will bridge to the bayesmendel  melaPro program and return
     * the predictions and carrier probabilities for melaPro.
     * @param risk
     * @return
     */
    //@POST
    //@Path("mela")
   // @Consumes({"application/xml",MediaType.APPLICATION_JSON})
    //@Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/mela", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
   // public Response calcMela(com.objex.risk.beans.mela.Risk risk) {
    public MelaResult calcMela(@RequestBody com.objex.risk.beans.mela.Risk risk) {
        RConnection con = null;
        long time = System.currentTimeMillis();
        MelaResult result = new MelaResult();
        REXP x = null;
        try
        {
            StringBuilder inlineParms = new StringBuilder();
            checkForSaveOff(risk);
            validateMelaData((com.objex.risk.beans.mela.Person) risk.getProband(), result, risk);
            if (result.hasErrors()) {
                result.setVersion(BAYES_VERSION);
                return result;
            }
            LOG.info("Requesting Connection to RServe.");
            con =  createConnection(spawnServer);
            if(verify(con)) {
                LOG.info("Connected to RServe.");
                long loadBegin = System.currentTimeMillis();
                con.voidEval("library('BayesMendel')");
                //REMOVED when upgraded to 2.1-8 patch(con);
                long loadTime = System.currentTimeMillis() - loadBegin;
                LOG.debug("Time to load models: " + loadTime + " milliseconds");

                String params = setupParams(risk);
                LOG.info("params: " + params);
                con.voidEval(params);
                //con.eval("data(mela.fam)");  con.eval("fam <-mela.fam");

               con.assign("fam", RiskService.this.createFamilyFrame(risk));
               // RList fam  = con.eval("fam").asList();
                 //RList fam  = con.eval("result@family").asList();
                //LOG.info(Utils.dataFrameToString(fam));

                //Utils.saveToMelaFile("/temp/exampleMelanomaFamily_" + System.currentTimeMillis() + ".xml", fam);
                 //RList fam  = con.eval("mela.fam").asList();
               // Utils.saveToMelaFile("C:/temp/melafam.xml", fam);
                // con.eval("data(BayesMendelObjects_20140321)"); RList fam2  = con.eval("melaproFam").asList(); RList expgermline  = con.eval("mela.germline.testing").asList();
                //Utils.saveToMelaFile("/temp/exampleMela2014Family_" + System.currentTimeMillis() + ".xml", fam2,expgermline);

                RList germList = createGermlineMatrix(risk);
                checkIncludeGermline(germList, con, inlineParms);

                String inlineString = finishInlineParams(risk,risk.isImpute(),risk.isImputeRelatives(),inlineParms);
                LOG.info("inline params: " + inlineString);
                con.voidEval("result <- melapro(family=fam,params=melaParams" + inlineString + ")");

                result = buildResult(con, result);
                result.setChartCode(risk.getChartCode());
                buildChartForResult(result);

            }
            long total = System.currentTimeMillis() - time;
            LOG.info("MELA Risk web Service time  is " + total + "milliseconds");

        }
        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            //return Response.serverError().build();
            throw new ServerErrorException(e.getMessage(), e);
        }
        finally {
            close(con);
        }
        return result; //Response.ok(result).build();
    }

    protected static boolean verify(RConnection con) {
        if (connectionCount >= (maxConnections - 3)) {
            LOG.warn("***WARNING*** Connection count of:" + connectionCount + " is nearing MAXIMUM of:" + maxConnections);
        }
        return con != null && con.isConnected();
    }
    /**
     * This is the entrypoint for the mmr model program.  Passing it a risk populated
     * with necessary data will bridge to the bayesmendel  mmr program and return
     * the predictions and carrier probabilities for mmrpro.
     * @param risk
     * @return
     */
   // @POST
   // @Path("mmr")
   // @Consumes({"application/xml",MediaType.APPLICATION_JSON})
   // @Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/mmr", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    //public Response calcMMR(com.objex.risk.beans.mmr.Risk risk) {
    public MMRResult calcMMR(@RequestBody com.objex.risk.beans.mmr.Risk risk) {
        RConnection con = null;
        long time = System.currentTimeMillis();
        MMRResult result = new MMRResult();
        REXP x = null;
        try
        {
            StringBuilder inlineParms = new StringBuilder();
            checkForSaveOff(risk);
            validateMMRData((com.objex.risk.beans.mmr.Person) risk.getProband(), result, risk);
            if (result.hasErrors()) {
                result.setVersion(BAYES_VERSION);
                return result;
            }
            LOG.info("Requesting Connection to RServe.");
            con =  createConnection(spawnServer);
            if(verify(con)) {
                LOG.info("Connected to RServe.");
                long loadBegin = System.currentTimeMillis();
                con.voidEval("library('BayesMendel')");
                long loadTime = System.currentTimeMillis() - loadBegin;
                LOG.debug("Time to load models: " + loadTime + " milliseconds");

                String params = setupParams(risk);
                LOG.info("params: " + params);
                con.voidEval(params);
               // con.eval("data(MMR.fam)");  con.eval("fam <-MMR.fam");

                con.assign("fam", createFamilyFrame(risk));
                //RList fam  = con.eval("fam").asList();
                //log.info(Utils.dataFrameToString(fam));
                //con.eval("data(BayesMendelObjects_20140321)"); RList fam2  = con.eval("mmrproFam").asList(); RList expgermline  = con.eval("mmr.germline.testing").asList();
              //Utils.saveToMMRFile("/temp/exampleMMRFramily_" + System.currentTimeMillis() + ".xml", fam);

                RList markList = createMarkerMatrix(risk);
                checkIncludeMarker(markList, con, inlineParms);

                RList germList = createGermlineMatrix(risk);
                checkIncludeGermline(germList, con, inlineParms);

                String inlineString = finishInlineParams(risk,risk.isImpute(),risk.isImputeRelatives(),inlineParms);
                LOG.info("inline params: " + inlineString);
                //con.eval("MMRpro(family=fam,params=mmrParams" + inlineString + ")");
                con.voidEval("result <- MMRpro(family=fam,params=mmrParams" + inlineString + ")");

                result = buildResult(con, result);
                result.setChartCode(risk.getChartCode());
                buildChartForResult(result);


                //REPORTING AND DEBUGGING
                //RList fam  = con.eval("result@family").asList();
                //RList fam  = con.eval("MMR.fam").asList();
                //log.info("result family");
                 //Utils.saveToMMRFile("C:/temp/mmrfam.xml", fam);
                //LOG.info(Utils.dataFrameToString(fam)); //*/



            }
            long total = System.currentTimeMillis() - time;
            LOG.info("MMR Risk web Service time is " + total  + " milliseconds");

        }
        catch (REngineException e)
        {
            LOG.error("Risk Server Error trying to gather embedded error", e);
            try {
                 String rerror = con.parseAndEval("geterrmessage()").toString();
                LOG.error("error in R" + rerror);
            } catch (Exception ex) { //nothing more to do if cant get exception
            }
            throw new ServerErrorException(e.getMessage(), e); // return Response.serverError().build();
        }
        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            //return Response.serverError().build();
            throw new ServerErrorException(e.getMessage(), e);
        }
        finally
        {
            close(con);
        }
        return result; //Response.ok(result).build();
    }

    protected void checkIncludeGermline(RList germList, RConnection con, StringBuilder inlineParms) throws REXPMismatchException, RserveException {
        if (!germList.isEmpty()) {
            LOG.info("including germline data");
            con.assign("germline.testing",REXP.createDataFrame(germList));
            inlineParms.append(",germline.testing=germline.testing");
        }
    }

    protected void checkIncludeMarker(RList markList, RConnection con, StringBuilder inlineParms) throws REXPMismatchException, RserveException {
        if (!markList.isEmpty()) {
            LOG.info("including marker data");
            //  LOG.info(Utils.dataFrameToString(markList));
            con.assign("marker",REXP.createDataFrame(markList));
            inlineParms.append(",marker.testing=marker");
        }
    }
    /**
     * Method that runs the brca algorithm
     * @param risk
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    //@POST
    //@Path("brca")
    //@Consumes({"application/xml",MediaType.APPLICATION_JSON})
    //@Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/brca", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    //public Response calcBRCA(com.objex.risk.beans.brca.Risk risk) {
    public BRCAResult calcBRCA(@RequestBody com.objex.risk.beans.brca.Risk risk) {

        RConnection con = null;
        long time = System.currentTimeMillis();
        BRCAResult result = new BRCAResult();
        REXP x = null;
        try
        {
            StringBuilder inlineParms = new StringBuilder();
            checkForSaveOff(risk);
            validateBRCAData((Person) risk.getProband(), result, risk);
            if (result.hasErrors()) {
                result.setVersion(BAYES_VERSION);
                return result;
            }
            LOG.info("Requesting Connection to RServe.");

            con =  createConnection(spawnServer);
            if(verify(con)) {
                LOG.info("Connected to RServe.");
                long loadBegin = System.currentTimeMillis();
                con.voidEval("library('BayesMendel')");
                LOG.debug("Time to load models: " + (loadBegin - System.currentTimeMillis()) + " milliseconds");

                String params = setupParams(risk);
                LOG.info("params: " + params);
                con.voidEval(params);

                con.assign("fam", createFamilyFrame(risk));

                RList mastList = createMastectomyMatrix(risk);
                if (!mastList.isEmpty()) {
                     LOG.info("including masectomy data");
                     con.assign("mastectomy",REXP.createDataFrame(mastList));
                     inlineParms.append(",mastectomy=mastectomy");
                }
                RList oophList = createOophorectomyMatrix(risk);
                if (!oophList.isEmpty()) {
                     LOG.info("including oophorectomy data");
                    // LOG.info(Utils.dataFrameToString(oophList));
                     con.assign("oophorectomy",REXP.createDataFrame(oophList));
                     inlineParms.append(",oophorectomy=oophorectomy");
                }
                RList markList = createMarkerMatrix(risk);
                checkIncludeMarker(markList, con, inlineParms);

                RList germList = createGermlineMatrix(risk);
                checkIncludeGermline(germList, con, inlineParms);

                String inlineString = RiskService.this.finishInlineParams(risk,inlineParms);
                LOG.info("inline params: " + inlineString);
                long computeBegin = System.currentTimeMillis();
                RList fam  = con.eval("fam").asList(); //comment out
                LOG.info(Utils.dataFrameToString(fam));
                con.voidEval("result <- brcapro(family=fam,params=brcaParams" + inlineString + ")");
                LOG.debug("BRCA Risk Model compute time is " + (System.currentTimeMillis() - computeBegin)  + " milliseconds");

                result = buildResult(con, result);
                result.setChartCode(risk.getChartCode());
                buildChartForResult(result);


                /*REPORTING AND DEBUGGING
                //RList fam  = con.eval("result@family").asList();
                RList lfam  = con.eval("brca.fam").asList();
                //log.info("result family");
                Utils.saveToBRCAFile("C:/temp/brcafam.xml", lfam);
                //LOG.info(Utils.dataFrameToString(fam)); */


            }
            long total = System.currentTimeMillis() - time;
            LOG.info("BRCA Risk web Service time is " + total  + " milliseconds");

        }
        catch (REngineException e)
        {
            LOG.error("Risk Server Error trying to gather embedded error", e);
			StringWriter writer = new StringWriter();
            Utils.saveToWriter(writer, risk);
            LOG.error("Error Request " + writer.toString());
            try {
                 String rerror = con.parseAndEval("geterrmessage()").toString();
                LOG.error("error in R" + rerror);
            } catch (Exception ex) { //nothing more to do if cant get exception
            }
            throw new ServerErrorException(e.getMessage(), e); //Response.serverError().build();
        }
        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
           // return Response.serverError().build();
            throw new ServerErrorException(e.getMessage(), e);
        }
        finally
        {
            close(con);
        }
        return result; //Response.ok(result).build();
    }
       /** {
     *
     * if parameters are set, this is a way of saving off the incoming risk request
     * by setting the isSaveIncoming field on the risk or the save.all config setting.
     * this basically would be used for extreme debugging scenarios
     * @param risk
     */
    protected void checkForSaveOff(BaseRisk risk) {
        //options to save off incoming request if there is an unusual debugging need
        if ((risk.isSaveIncoming() || saveRequests)  && savePath != null) {
            String filename = savePath + File.separator + "risk_";
            filename = filename + System.currentTimeMillis() + ".txt";
            LOG.info("Saving file:" + filename);
            Utils.saveToFile(filename,risk);
        }
    }


    protected static void close(RConnection con) {
    	try {
	        if (connectionPooling) {
	            connections.add(con);
	        } else {
	            closeNonPool(con);
	        }
                conLock.notifyAll();
    	} catch (Exception e) { //dont blow up in finally

    	}
    }

    protected static synchronized void closeNonPool(RConnection con) {
        con.close();
        connectionCount--;
    }

        /**
     * this method takes a populated result and will build a populated chart object
     * with the associated chart code.
     * @param result
     */

    protected void buildChartForResult(BaseResult result) {
        if (result.getChartCode() == null || result.getPredictions().isEmpty()) {
            LOG.info("No info to chart for chartcode" + result.getChartCode() + " predictions:" + result.getPredictions().size());
            return;
        }
        ChartVO chart = Utils.buildChart(result);
        plotResultImageFromChart(result,chart);
    }

    /**
     *
     * @param <T>
     * @param c
     * @param result
     * @return
     * @throws REXPMismatchException
     * @throws RserveException
     */
    protected <T extends BaseResult> T buildResult(RConnection c, T result) throws REXPMismatchException, RserveException {
        result.setVersion(BAYES_VERSION);
        RList predictions = c.eval("result@predictions").asList();
       //if (LOG.isDebugEnabled())
         //   LOG.debug(Utils.dataFrameToString(predictions));

        int rows = predictions.at(0).length();
        for (int i=0; i< rows; i++) {
            Prediction prediction = result.addPrediction();
            for (Object name : predictions.names) {
                String value = predictions.at((String)name).asStrings()[i];
                try {
                     prediction.addRisk((String)name, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    LOG.info("Result value was not a number for name: " + name  + " value:" + value + " setting value=" + Double.NaN);
                    prediction.addRisk((String)name, Double.NaN);
                }
            }
        }

        RList probabilities = c.eval("result@probs").asList();

        //if (LOG.isDebugEnabled())
          //  LOG.debug(Utils.dataFrameToString(probabilities));

        for (Object name : probabilities.names) {
            String col = (String)name;
            result.addProbability(col, probabilities.at(col).asDoubles()[0]);
        }
        return result;
    }
    protected String setupParams(com.objex.risk.beans.brca.Risk brcaRisk) {
        StringBuilder params = new StringBuilder("brcaParams <-brcaparams(age.by=" + brcaRisk.getAgeBy());
        addParam("age.to",brcaRisk.getAgeTo(),params);
        addParam("myseed","1",params);
        addParam("sensitivity1",brcaRisk.getSensitivity1(),params);
        addParam("sensitivity2",brcaRisk.getSensitivity2(),params);
        addParam("specificity1",brcaRisk.getSpecificity1(),params);
        addParam("specificity2",brcaRisk.getSpecificity2(),params);
        params.append(")");
        return params.toString();
    }
     protected String setupParams(com.objex.risk.beans.mmr.Risk mmrRisk) {
        StringBuilder params = new StringBuilder("mmrParams <-MMRparams(age.by=" + mmrRisk.getAgeBy());
        addParam("age.to",mmrRisk.getAgeTo(),params);
        addParam("myseed","1",params);
        addParam("sensitivity1",mmrRisk.getSensitivity1(),params);
        addParam("sensitivity2",mmrRisk.getSensitivity2(),params);
        addParam("sensitivity3",mmrRisk.getSensitivity3(),params);
        addParam("specificity1",mmrRisk.getSpecificity1(),params);
        addParam("specificity2",mmrRisk.getSpecificity2(),params);
        addParam("specificity3",mmrRisk.getSpecificity3(),params);
        params.append(")");
        return params.toString();
    }
      protected String setupParams(com.objex.risk.beans.panc.Risk pancRisk) {
        StringBuilder params = new StringBuilder("pancParams <-pancparams(age.by=" + pancRisk.getAgeBy());
        addParam("age.to",pancRisk.getAgeTo(),params);
        addParam("myseed","1",params);
        params.append(")");
        return params.toString();
    }
    public void addParam(String name, Object value, StringBuilder params) {
        if (value != null) {
            params.append("," + name + "=" + value);
        }
    }
    protected String setupParams(com.objex.risk.beans.mela.Risk melanomaRisk) {
        StringBuilder params = new StringBuilder("melaParams <-melaparams(age.by=" + melanomaRisk.getAgeBy());
        addParam("age.to",melanomaRisk.getAgeTo(),params);
        addParam("myseed","1",params);
        addParam("sensitivity",melanomaRisk.getSensitivity(),params);
        addParam("specificity",melanomaRisk.getSpecificity(),params);
       // addParam("spm.lr.noncarrier","0.80", params);
        params.append(")");
        return params.toString();
    }
    private REXP createFamilyFrame(Risk brcaRisk) throws REXPMismatchException {

        RList family = new RList();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> genders = new ArrayList<>();
        ArrayList<Integer> fatherids = new ArrayList<>();
        ArrayList<Integer> motherids = new ArrayList<>();
        ArrayList<Integer> affectedBreast = new ArrayList<>();
        ArrayList<Integer> affectedsOvary = new ArrayList<>();
        ArrayList<Integer> ages = new ArrayList<>();
        ArrayList<Integer> agesOvary = new ArrayList<>();
        ArrayList<Integer> agesCont = new ArrayList<>();
        ArrayList<Integer> twins = new ArrayList<>();
        ArrayList<Integer> deaths = new ArrayList<>();
        ArrayList<Integer> deathAges = new ArrayList<>();
        ArrayList<String> ethnics = new ArrayList<>();
        HashMap usedGroups = new HashMap();
        for (Person person : brcaRisk.getFamily()) {
            addCommonFields(ids, person, genders, fatherids, motherids);
            affectedBreast.add(person.getAffectedBreastType());
            affectedsOvary.add(person.getAffectedOvarianType());
            ages.add(person.getDiagnoseBreastAge());
            if (person.getAffectedOvarianType() == 0 && person.isPerformedOophorectomy() && person.getDiagnoseOvarianAge() <= 1) {
                LOG.info("Estimating current age for use in Diagnose Ovarian");
                person.setDiagnoseOvarianAge(person.getAgeOophorectomy());
            }
            agesOvary.add(person.getDiagnoseOvarianAge());
            agesCont.add(person.getDiagnoseBreastContralateralAge());
            if (person.getTwin() == 0 ||  usedGroups.containsValue(person.getTwin())) {
                 twins.add(0);
            } else {
                 Integer group = person.getTwin();
                 twins.add(group);
                 if (usedGroups.containsKey(group)) {
                     usedGroups.put(group, group);
                 } else {
                     usedGroups.put(group, null);
                 }
            }
            ethnics.add(person.getEthnic().toString());
            deaths.add(person.getVitalStatus().value);
            deathAges.add(person.getAgeOfDeath()); //check this

        }

        Integer[] I = new Integer[0];
        family.put("ID", new REXPInteger(ArrayUtils.toPrimitive(ids.toArray(I))));
        family.put("Gender",new REXPInteger(ArrayUtils.toPrimitive(genders.toArray(I))));
        family.put("FatherID",new REXPInteger(ArrayUtils.toPrimitive(fatherids.toArray(I))));
        family.put("MotherID",new REXPInteger(ArrayUtils.toPrimitive(motherids.toArray(I))));
        family.put("AffectedBreast",new REXPInteger(ArrayUtils.toPrimitive(affectedBreast.toArray(I))));
        family.put("AffectedOvary", new REXPInteger(ArrayUtils.toPrimitive(affectedsOvary.toArray(I))));
        family.put("AgeBreast", new REXPInteger(ArrayUtils.toPrimitive(ages.toArray(I))));
        family.put("AgeOvary",new REXPInteger(ArrayUtils.toPrimitive(agesOvary.toArray(I))));
        family.put("AgeBreastContralateral",new REXPInteger(ArrayUtils.toPrimitive(agesCont.toArray(I))));
        family.put("Twins", new REXPInteger(ArrayUtils.toPrimitive(twins.toArray(I))));
        family.put("ethnic",new REXPString(ethnics.toArray(new String[0])));
        family.put("Death",new REXPInteger(ArrayUtils.toPrimitive(deaths.toArray(I))));
        family.put("AgeDeath",new REXPInteger(ArrayUtils.toPrimitive(deathAges.toArray(I))));

        return REXP.createDataFrame(family);
    }

    public static synchronized RConnection createConnection(boolean spawnServer) throws IOException, InterruptedException, RserveException
    {
        RConnection conn = null;
        String cmd = spawnCommand;
        REngineException returnEx = null;
        while (connectionCount >= maxConnections && connections.isEmpty()) {
            LOG.info("Sleeping to wait for connections to return");
            Thread.sleep(25);
        }
        //established above that connectionCount is not greater than maxconnection so feel free to create
        int retry = 0;
        while(conn == null && retry < 3)
        {
            try {
                if (connectionPooling == false || connections.isEmpty()) {
                    LOG.info("creating connection retry " + retry);
                    conn = new RConnection();
                    connectionCount++;
                }
                else {
                    conn = connections.remove(0);
                    verify(conn);
                    LOG.info("returning connection " + connections.size());
                }
                
            } catch (REngineException r) {
                    returnEx = r;
                    retry++;
                    Thread.sleep(2000);
            }
        }    
        if (conn == null)
        {
            LOG.error("Could not connect to RServ.  Spawning is " + spawnServer);
     
            if (spawnServer) {
                LOG.info("attempting to spawn with cmd: " + cmd);
                Process p = Runtime.getRuntime().exec(cmd);
                if (OS != null && !OS.startsWith("win")) {
                    p.waitFor();
                }
                Thread.sleep(3000);
                conn = new RConnection(); //createConnection(false, cmd); //avoid loop
                connectionCount = 1;
            }
            else {
                LOG.error("Connection build failed", returnEx);
            }
        }
        return conn;
    }

    private String finishInlineParams(Risk brcarisk, StringBuilder inlines) {
        inlines.append(",imputeAges=" + Boolean.toString(brcarisk.isImpute()).toUpperCase());
        inlines.append(",imputeRelatives=" + Boolean.toString(brcarisk.isImpute() && brcarisk.isImputeRelatives()).toUpperCase()); //nosupport for imputing  relaatives when not imputing ages
        inlines.append(",print=").append("TRUE");
        inlines.append(",race='").append(brcarisk.getRace()).append("'");
        return inlines.toString();
    }

     private String finishInlineParams(BaseRisk risk, boolean impute, boolean imputeRelatives,StringBuilder inlines) {
        inlines.append(",imputeAges=" + Boolean.toString(impute).toUpperCase());
        inlines.append(",imputeRelatives=" + Boolean.toString(impute && imputeRelatives).toUpperCase()); //nosupport for imputing  relaatives when not imputing ages
        //String print =  context.getInitParameter("print") == null || context.getInitParameter("print").equalsIgnoreCase("TRUE") ? "TRUE" : "FALSE";
        String print =  applicationProperties.getBayesMendel().getPrint() == null || applicationProperties.getBayesMendel().getPrint() .equalsIgnoreCase("TRUE") ? "TRUE" : "FALSE";
        inlines.append(",print=").append(print);
        return inlines.toString();
    }

    private RList createMastectomyMatrix(Risk risk) {
        RList mastectList = new RList();
        boolean hasData = false;

        ArrayList<Integer> mastectormies = new ArrayList<>();
        ArrayList<Integer> ages = new ArrayList<>();

        for (Person person : risk.getFamily()) {
            /* per bayes, cases where mastectomy age are greather thean contralateral diagnoses age are noninformative.  They
            also blow up the algorithm so adding informative flag to not count mastectomy in those cases.
            */
            boolean informative = person.getAgeMasectomy() == null || !((person.getAffectedBreastType() == 2) && (person.getAgeMasectomy() > person.getDiagnoseBreastContralateralAge()));
            hasData = hasData || (person.isPerformedMasectomy() && informative);
            mastectormies.add(person.isPerformedMasectomy()  && informative ? 1 : 0);
            ages.add(person.getAgeMasectomy());
        }
        if (hasData) {
            Integer[] I = new Integer[0];
            mastectList.put("Mastectomy", new REXPInteger(ArrayUtils.toPrimitive(mastectormies.toArray(I))));
            mastectList.put("AgeMastectomy",new REXPInteger(ArrayUtils.toPrimitive(ages.toArray(I))));
        }
        return mastectList;
    }

    private RList createOophorectomyMatrix(Risk risk) {
        RList oophoList = new RList();
        boolean hasData = false;

        ArrayList<Integer> oophorectomies = new ArrayList<>();
        ArrayList<Integer> ages = new ArrayList<>();

        for (Person person : risk.getFamily()) {
            //not including oopho for ovarians.  Bayes says it is noninformative and causes explosions
            //if diagnose Ov age is lower than Oopho age
            if (person.getAffectedOvarianType() == 1) {
                oophorectomies.add(0);
                ages.add(1);

            } else {
                hasData = hasData || person.isPerformedOophorectomy();
                oophorectomies.add(person.isPerformedOophorectomy()? 1 : 0);
                ages.add(person.getAgeOophorectomy());
            }
        }
        if (hasData) {
            Integer[] I = new Integer[0];
            oophoList.put("Oophorectomy", new REXPInteger(ArrayUtils.toPrimitive(oophorectomies.toArray(I))));
            oophoList.put("AgeOophorectomy",new REXPInteger(ArrayUtils.toPrimitive(ages.toArray(I))));
        }
        return oophoList;
    }

    private RList createMarkerMatrix(Risk brcaRisk) {
        RList markerList = new RList();
        boolean hasData = false;
        int famSize = brcaRisk.getFamily().size();
        int[][] markerMatrix = new int[5][famSize];

        for (int i = 0; i < famSize; i++) {
            Marker marker = brcaRisk.getFamily().get(i).getMarker();
            if (marker != null && !hasData) {
                hasData= true;
                Utils.fillMatrix(markerMatrix, 0);
            }
            if (marker != null && hasData) {
                markerMatrix[0][i] = marker.getER().value;
                markerMatrix[1][i] = marker.getCK14().value;
                markerMatrix[2][i] = marker.getCK5_6().value;
                markerMatrix[3][i] = marker.getPR().value;
                markerMatrix[4][i] = marker.getHER2().value;
            }

        }
        if (hasData) {
            markerList.put("ER", new REXPInteger(markerMatrix[0]));
            markerList.put("CK14",new REXPInteger(markerMatrix[1]));
            markerList.put("CK5.6",new REXPInteger(markerMatrix[2]));
            markerList.put("PR",new REXPInteger(markerMatrix[3]));
            markerList.put("HER2",new REXPInteger(markerMatrix[4]));
        }
        return markerList;
    }
     private RList createMarkerMatrix(com.objex.risk.beans.mmr.Risk mmrRisk) {
        RList markerList = new RList();
        boolean hasData = false;
        int famSize = mmrRisk.getFamily().size();
        int[][] markerMatrix = new int[5][famSize];

        for (int i = 0; i < famSize; i++) {
            com.objex.risk.beans.mmr.Marker marker = mmrRisk.getFamily().get(i).getMarker();
            if (marker != null && !hasData) {
                hasData= true;
                Utils.fillMatrix(markerMatrix, 0);
            }
            if (marker != null && hasData) {
                markerMatrix[0][i] = marker.getMsi().value;
                markerMatrix[1][i] = marker.getLocation().value;
            }

        }
        if (hasData) {
            markerList.put("MSI", new REXPInteger(markerMatrix[0]));
            markerList.put("location",new REXPInteger(markerMatrix[1]));
        }
        return markerList;
    }

    private RList createGermlineMatrix(BaseRisk risk) {
        RList germlineList = new RList();
        boolean hasInitialized = false;
        int famSize = risk.getFamily().size();
        BasePerson person = risk.getFamily().get(0);
        int germSize = person.getGermline().size();
       int[][] germlineMatrix = new int[germSize][famSize];
        List<String> testNames = new ArrayList(person.getGermline().keySet());
        for (int i = 0; i < famSize; i++) {
            person = risk.getFamily().get(i);
            if (person.hasGermline() && !hasInitialized) {
                hasInitialized= true;
                Utils.fillMatrix(germlineMatrix, 0);
            }
            if (person.hasGermline()) {
                for (int g = 0; g < person.getGermline().size(); g++) {
                    germlineMatrix[g][i] = person.getGermline(testNames.get(g)).value;
                }
            }

        }
        if (hasInitialized) {
            for (int g = 0; g < person.getGermline().size(); g++) {
                germlineList.put(testNames.get(g), new REXPInteger(germlineMatrix[g]));
            }
        }
        return germlineList;
    }

    @PostConstruct
    public void init() {
        try
        {
            if (savePath == null) { //only do this for the first attempt
                //saveRequests = ("true".equals(context.getInitParameter("save.all")));
            	saveRequests = ("true".equals(applicationProperties.getBayesMendel().getSaveAll()));

            }

            //savePath = context.getInitParameter("save.path");
            if (maxConnections == 0) {
                //String size = context.getInitParameter("max.connections");
            	String size = applicationProperties.getBayesMendel().getMaxConnections();
                maxConnections = size == null ? 5 : Integer.parseInt(size);
                LOG.info("initializing max connections to " + maxConnections);
            }
            /*is this obsolete */
           // if (context.getInitParameter("pool.connections") != null) {
           //     connectionPooling = "TRUE".equals(context.getInitParameter("pool.connections").toUpperCase());
          //  }
            //Can't reuse connections in Rserve on windows
            if (connectionPooling && OS.startsWith("win"))  {
                connectionPooling = false;
                LOG.warn("Connection pooling will be turned off.  It is not supported for OS " + OS);
                LOG.warn("Setting the pool.connections initParam to FALSE should remove this message");
            }
            if (OS.startsWith("win"))  {
            	savePath = applicationProperties.getBayesMendel().getWindows().getSavePath();
                //spawnCommand = context.getInitParameter("spawn.windows.command");
            	spawnCommand = applicationProperties.getBayesMendel().getWindows().getCommand();
            	spawnServer = applicationProperties.getBayesMendel().getWindows().getSpawn().equalsIgnoreCase("true");
            } else {
            	savePath = applicationProperties.getBayesMendel().getLinux().getSavePath();
                //spawnCommand = context.getInitParameter("spawn.linux.command");
            	spawnCommand = applicationProperties.getBayesMendel().getLinux().getCommand();
            	spawnServer = applicationProperties.getBayesMendel().getLinux().getSpawn().equalsIgnoreCase("true");
            }

           // if  (context.getInitParameter("spawn.server") != null && context.getInitParameter("spawn.server").equalsIgnoreCase("true")){
                //spawnServer = true;
            //spawnServer = applicationProperties.getBayesMendel().get
            //}

        } catch (Exception e) {
            LOG.error("could not init, setting parameters to defaults",e);
            maxConnections = 5;
        }
    }
    /**
     * this will create the dataframe for the mmrpro BayesMendel program
     * @param mmrRisk
     * @return
     * @throws REXPMismatchException
     */
    private REXP createFamilyFrame(com.objex.risk.beans.mmr.Risk mmrRisk) throws REXPMismatchException {
         RList family = new RList();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> genders = new ArrayList<>();
        ArrayList<Integer> fatherids = new ArrayList<>();
        ArrayList<Integer> motherids = new ArrayList<>();
        ArrayList<Integer> affectedColon = new ArrayList<>();
        ArrayList<Integer> affectedEndometrium = new ArrayList<>();
        ArrayList<Integer> agesColon = new ArrayList<>();
        ArrayList<Integer> agesEndos = new ArrayList<>();
        ArrayList<Integer> twins = new ArrayList<>();

         HashMap usedGroups = new HashMap();

        for (com.objex.risk.beans.mmr.Person person : mmrRisk.getFamily()) {
            addCommonFields(ids, person, genders, fatherids, motherids);
            affectedColon.add(person.getAffectedColonType());
            affectedEndometrium.add(person.getAffectedEndometriumType());
            agesColon.add(person.getDiagnoseColonAge());
            agesEndos.add(person.getDiagnoseEndometriumAge());
                        if (person.getTwin() == 0 ||  usedGroups.containsValue(person.getTwin())) {
                 twins.add(0);
            } else {
                 Integer group = person.getTwin();
                 twins.add(group);
                 if (usedGroups.containsKey(group)) {
                     usedGroups.put(group, group);
                 } else {
                     usedGroups.put(group, null);
                 }
            }
        }

        Integer[] I = new Integer[0];
        family.put("ID", new REXPInteger(ArrayUtils.toPrimitive(ids.toArray(I))));
        family.put("Gender",new REXPInteger(ArrayUtils.toPrimitive(genders.toArray(I))));
        family.put("FatherID",new REXPInteger(ArrayUtils.toPrimitive(fatherids.toArray(I))));
        family.put("MotherID",new REXPInteger(ArrayUtils.toPrimitive(motherids.toArray(I))));
        family.put("AffectedColon",new REXPInteger(ArrayUtils.toPrimitive(affectedColon.toArray(I))));
        family.put("AffectedEndometrium", new REXPInteger(ArrayUtils.toPrimitive(affectedEndometrium.toArray(I))));
        family.put("AgeColon", new REXPInteger(ArrayUtils.toPrimitive(agesColon.toArray(I))));
        family.put("AgeEndometrium",new REXPInteger(ArrayUtils.toPrimitive(agesEndos.toArray(I))));
        family.put("Twins", new REXPInteger(ArrayUtils.toPrimitive(twins.toArray(I))));
        return REXP.createDataFrame(family);
    }
    /**
     * this will create the dataframe for the pancPro BayesMendel program
     * @param pancRisk
     * @return
     * @throws REXPMismatchException
     */
     private REXP createFamilyFrame(com.objex.risk.beans.panc.Risk pancRisk) throws REXPMismatchException {
         RList family = new RList();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> genders = new ArrayList<>();
        ArrayList<Integer> fatherids = new ArrayList<>();
        ArrayList<Integer> motherids = new ArrayList<>();
        ArrayList<Integer> affectedPancreas = new ArrayList<>();
        ArrayList<Integer> agesPancreas = new ArrayList<>();
        ArrayList<Integer> twins = new ArrayList<>();

         HashMap usedGroups = new HashMap();

        for (com.objex.risk.beans.panc.Person person : pancRisk.getFamily()) {
            addCommonFields(ids, person, genders, fatherids, motherids);
            affectedPancreas.add(person.getAffectedPancreasType());
            agesPancreas.add(person.getDiagnosePancreasAge());
                        if (person.getTwin() == 0 ||  usedGroups.containsValue(person.getTwin())) {
                 twins.add(0);
            } else {
                 Integer group = person.getTwin();
                 twins.add(group);
                 if (usedGroups.containsKey(group)) {
                     usedGroups.put(group, group);
                 } else {
                     usedGroups.put(group, null);
                 }
            }
        }

        Integer[] I = new Integer[0];
        family.put("ID", new REXPInteger(ArrayUtils.toPrimitive(ids.toArray(I))));
        family.put("Gender",new REXPInteger(ArrayUtils.toPrimitive(genders.toArray(I))));
        family.put("FatherID",new REXPInteger(ArrayUtils.toPrimitive(fatherids.toArray(I))));
        family.put("MotherID",new REXPInteger(ArrayUtils.toPrimitive(motherids.toArray(I))));
        family.put("AffectedPancreas",new REXPInteger(ArrayUtils.toPrimitive(affectedPancreas.toArray(I))));
        family.put("AgePancreas", new REXPInteger(ArrayUtils.toPrimitive(agesPancreas.toArray(I))));
        family.put("Twins", new REXPInteger(ArrayUtils.toPrimitive(twins.toArray(I))));
        return REXP.createDataFrame(family);
    }
      /**
     * this will create the dataframe for the melaPro BayesMendel program
     * @param melaRisk
     * @return
     * @throws REXPMismatchException
     */
     private REXP createFamilyFrame(com.objex.risk.beans.mela.Risk melaRisk) throws REXPMismatchException {
         RList family = new RList();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> genders = new ArrayList<>();
        ArrayList<Integer> fatherids = new ArrayList<>();
        ArrayList<Integer> motherids = new ArrayList<>();
        ArrayList<Integer> affectedMelanomas = new ArrayList<>();
        ArrayList<Integer> agesMelanomas = new ArrayList<>();
        ArrayList<Integer> twins = new ArrayList<>();

        HashMap usedGroups = new HashMap();

        for (com.objex.risk.beans.mela.Person person : melaRisk.getFamily()) {
            addCommonFields(ids, person, genders, fatherids, motherids);
            affectedMelanomas.add(person.getAffectedSkinType());
            agesMelanomas.add(person.getDiagnoseSkinAge());
                        if (person.getTwin() == 0 ||  usedGroups.containsValue(person.getTwin())) {
                 twins.add(0);
            } else {
                 Integer group = person.getTwin();
                 twins.add(group);
                 if (usedGroups.containsKey(group)) {
                     usedGroups.put(group, group);
                 } else {
                     usedGroups.put(group, null);
                 }
            }
        }

        Integer[] I = new Integer[0];
        family.put("ID", new REXPInteger(ArrayUtils.toPrimitive(ids.toArray(I))));
        family.put("Gender",new REXPInteger(ArrayUtils.toPrimitive(genders.toArray(I))));
        family.put("FatherID",new REXPInteger(ArrayUtils.toPrimitive(fatherids.toArray(I))));
        family.put("MotherID",new REXPInteger(ArrayUtils.toPrimitive(motherids.toArray(I))));
        family.put("AffectedSkin",new REXPInteger(ArrayUtils.toPrimitive(affectedMelanomas.toArray(I))));
        family.put("AgeSkin", new REXPInteger(ArrayUtils.toPrimitive(agesMelanomas.toArray(I))));
        family.put("Twins", new REXPInteger(ArrayUtils.toPrimitive(twins.toArray(I))));
        return REXP.createDataFrame(family);
    }



    protected void addCommonFields(ArrayList<Integer> ids, BasePerson person, ArrayList<Integer> genders, ArrayList<Integer> fatherids, ArrayList<Integer> motherids) {
        ids.add(person.getId());
        genders.add(person.getGender());
        fatherids.add(person.getFatherId());
        motherids.add(person.getMotherId());
    }

     /**
     * Method that runs a command
     * @param command
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
   // @POST
    //@Path("runCommand")
    //@Consumes({"application/xml",MediaType.APPLICATION_JSON})
    //@Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/runCommand", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public RiskCommand runCommand(@RequestBody RiskCommand command) {
        long time = System.currentTimeMillis();
        try
        {
           String response = "command not understood";
           switch (command.getName()) {
               case "LIST" :
                   response = "LIST\nSAVE_REQUESTS";
                   break;
               case "SAVE_REQUESTS" :
                   if ("TRUE".equals(command.getValue().toUpperCase())) {
                       saveRequests = true;
                       LOG.info("turned request saving on");
                   } else {
                      saveRequests = false;
                       LOG.info("turned request saving off");
                   }
                   response = "save requests now set to " + saveRequests;
                   break;
           }
           command.setResponse(response);
            long total = System.currentTimeMillis() - time;
            LOG.info("Time for command " + command.getName() + " " + (total / 1000) + " seconds");

        }
        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            command.setResponse("Unknown failure");
            return command;
        }
        return command;
    }
    /**
     * This method applies the patch for a problem with small melapro pedigrees in 2.1-7.  It should be removed in 2.1-8
     * @param con
     * @throws RserveException
     */

    private void patch(RConnection con) throws RserveException {

       // con.voidEval(Patch.LYTE_FUNC);
        //con.voidEval(Patch.ENV_SET);
        //con.voidEval(Patch.ASS_NAMESPACE);
    }

    /**
 * This method takes a chart populated with data and will generate the image to set on the result
 * @param result
 * @param chart
 */
    protected void plotResultImageFromChart(BaseResult result, ChartVO chart) {
       byte[] chartBytes =  Utils.plotChartToPNG(chart);
       result.setChart(chartBytes);
    }

    //@POST
    //@Path("plotChart")
    //@Consumes({"application/xml",MediaType.APPLICATION_JSON})
    //@Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/plotChart", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    //public Response plotChartToResultImage(ChartVO chart) {
    public ChartResult plotChartToResultImage(@RequestBody ChartVO chart) {

        long time = System.currentTimeMillis();
        Utils.copyFromPrototype(chart);
        ChartResult result = new ChartResult();
        result.setContext(chart.getContext());
        try
        {
          // if (saveRequests) {
            //   risk.setSaveIncoming(true);
           //}
            result.setChartCode(chart.getChartCode());
            plotResultImageFromChart(result,chart);
            long total = System.currentTimeMillis() - time;
            LOG.info("Risk Request time for Charting Tyrer Cuzick is " + (total / 1000) + " seconds");

        }
        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            //return Response.serverError().build();
            throw new ServerErrorException(e.getMessage(), e);
        }
        //return Response.ok(result).build();
        return result;
    }
    //@POST
    //@Path("chartResult")
    //@Consumes({"application/xml",MediaType.APPLICATION_JSON})
    //@Produces({"application/xml",MediaType.APPLICATION_JSON})
    // @PostMapping(value = "/chartResult", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    //public Response plotResult(BaseResult result) {
    public BaseResult plotResult(@RequestBody BaseResult result) {

        long time = System.currentTimeMillis();
        try
        {
            buildChartForResult(result);
        }

        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            //return Response.serverError().build();
            throw new ServerErrorException(e.getMessage(), e);
        }
        long total = System.currentTimeMillis() - time;
        LOG.info("plot result time for Tyrer Cuzick is " + (total / 1000) + " seconds");
        return result;
        //return Response.ok(result).build();
    }
    /**
   * validate the incoming risk family data for BRCA
   * @param proband
   * @param result
   * @param risk
   */
  protected void validateBRCAData(Person proband, BRCAResult result, Risk risk) {

      if (proband.getAge() >= risk.getAgeTo()) {
          result.addError(new RiskError(proband.getId(),"Proband age is too high","ageTooHighForModelSettings"));
      }
      for (Person member : risk.getFamily()) {
          validateBRCAMemberData(member, result);
      }

  }

   /* validate the incoming risk family data for MMR
   * @param proband
   * @param result
   * @param risk
   */
  protected void validateMMRData(com.objex.risk.beans.mmr.Person proband, MMRResult result, com.objex.risk.beans.mmr.Risk risk) {
      if (proband.getAge() >= risk.getAgeTo()) {
          result.addError(new RiskError(proband.getId(),"Proband age is too high","ageTooHighForModelSettings"));
      }
      for (com.objex.risk.beans.mmr.Person member : risk.getFamily()) {
          validateMMRMemberData(member, result);
      }
  }
  /**
   * Member validations for MMR
   * @param member
   * @param result
   */
  private void validateMMRMemberData(com.objex.risk.beans.mmr.Person member, MMRResult result) {
      if (member.getAffectedColonType() != 0 && member.getDiagnoseColonAge() == 0) {
          result.addError(new RiskError(member.getId(),"Missing Age Colon","missingAgeColon"));
      }
      else if (member.getAffectedColonType() == 0 && member.getDiagnoseColonAge() != 0  && member.getDiagnoseColonAge() < member.getAge()) {
          result.addError(new RiskError(member.getId(),"Missing Affected For Diagnose Age Colon","missingAffectedForDiagnoseAgeColon"));
      }
      if (member.getAffectedEndometriumType() != 0 && member.getDiagnoseEndometriumAge() == 0) {
          result.addError(new RiskError(member.getId(),"Missing Age Endometrium","missingAgeEndometrium"));
      }
      else if (member.getAffectedEndometriumType() == 0 && member.getDiagnoseEndometriumAge() != 0  && member.getDiagnoseEndometriumAge() < member.getAge()) {
          result.addError(new RiskError(member.getId(),"Missing Affected For Diagnose Age Endometrium","missingAffectedForDiagnoseAgeEndometrium"));
      }
      if (member.getGender() == 1 && member.getAffectedEndometriumType() == 1) {
          result.addError(new RiskError(member.getId(),"Invalid Gender for Endometrium","invalidGenderForEndometrium"));
      }
      if (member.getAge() < 0) {
          result.addError(new RiskError(member.getId(),"Invalid Age","invalidAge"));
      }
  }

    /**
   * Member validations for BRCA
   * @param member
   * @param result
   * @param risk
   */
  private void validateBRCAMemberData(Person member, BRCAResult result) {
      if (member.getAffectedBreastType() == 1 && member.getDiagnoseBreastAge() == 0) {
          result.addError(new RiskError(member.getId(),"Missing Age Breast","missingAgeBreast"));
      }
      else if (member.getAffectedBreastType() == 2 && (member.getDiagnoseBreastAge() == 0 || member.getDiagnoseBreastContralateralAge() == 0)) {
         result.addError(new RiskError(member.getId(),"Missing Age Breast/Breast Contralateral","missingAgeBreastOrBreastContralateral"));
      }
      else if (member.getAffectedBreastType() == 0) {
    	  if ((member.getDiagnoseBreastAge() < member.getAge() && member.getDiagnoseBreastAge() != 0) || (member.getDiagnoseBreastContralateralAge() < member.getAge() && member.getDiagnoseBreastContralateralAge() != 0))
    	  {
    		  result.addError(new RiskError(member.getId(),"Missing Affected For Diagnose Age Breast","missingAffectedForDiagnoseAgeBreast"));
    	  }
      }
      else if (member.getAffectedBreastType() == 0 && (member.getDiagnoseBreastAge() != 0 && member.getDiagnoseBreastContralateralAge() != 0)) {
          result.addError(new RiskError(member.getId(),"Missing Affected For Diagnose Age Breast","missingAffectedForDiagnoseAgeBreast"));
      }
      if (member.getAffectedOvarianType() != 0 && member.getDiagnoseOvarianAge() == 0) {
         result.addError(new RiskError(member.getId(),"Missing Age Ovarian","missingAgeOvarian"));
      }
      else if (member.getAffectedOvarianType() == 0 && (member.getDiagnoseOvarianAge() < member.getAge() && member.getDiagnoseOvarianAge() > 0)) {
          result.addError(new RiskError(member.getId(),"Missing Affected For Diagnose Age Ovarian","missingAffectedForDiagnoseAgeOvarian"));
      }
      if (member.getAffectedBreastType() == 0 && member.getMarker() != null) {
          if (member.getMarker().getCK14() != TestResult.NONE || member.getMarker().getCK56()!= TestResult.NONE || member.getMarker().getER() != TestResult.NONE
                  || member.getMarker().getHER2() != TestResult.NONE || member.getMarker().getPR() != TestResult.NONE) {
              result.addError(new RiskError(member.getId(),"Tumor Markers Require Breast Cancer Diagnosis","tumorMarkersRequireBreastCancerDiagnosis"));
          }
      }

      if (member.isPerformedOophorectomy()) {
          if (member.getGender() == 1) {
              result.addError(new RiskError(member.getId(),"Invalid Gender for Oophorectomy","invalidGenderForOophorectomy"));
          } else if  (member.getAgeOophorectomy() == 0) {
              result.addError(new RiskError(member.getId(),"Mising Age for Oophorectomy","missingAgeOophorectomy"));
          } else if  (member.getAge() < member.getAgeOophorectomy()) {
              result.addError(new RiskError(member.getId(),"Current Age Less Than Oophorectomy Age","currentAgeIsLessThanOophorectomyAge"));
          }
      }

      if (member.isPerformedMasectomy() && member.getAgeMasectomy() == 0) {
        result.addError(new RiskError(member.getId(),"Missing Age Masectomy","missingAgeMasectomy"));
      }
      if (member.getGender() == 1 && member.getAffectedOvarianType() == 1) {
         result.addError(new RiskError(member.getId(),"Invalid Gender For Ovarian","invalidGenderForOvarian"));
      }

      if (member.getAge() < 0) {
         result.addError(new RiskError(member.getId(),"Invalid Age","invalideAge"));
      }
  }
  /**
   * Server side validations for melanoma
   * @param proband
   * @param result
   * @param risk
   */
  protected void validateMelaData(com.objex.risk.beans.mela.Person proband, MelaResult result, com.objex.risk.beans.mela.Risk risk) {
      if (proband.getAge() >= risk.getAgeTo()) {
          result.addError(new RiskError(proband.getId(),"Proband age is too high","ageTooHighForModelSettings"));
      }
      for (com.objex.risk.beans.mela.Person member : risk.getFamily()) {
          validateMelaMemberData(member, result);
      }

  }

    /**
   * Member validations for BRCA
   * @param member
   * @param result
   * @param risk
   */
  private void validateMelaMemberData(com.objex.risk.beans.mela.Person member, MelaResult result) {
      if (member.getAffectedSkinType() != 0 && member.getDiagnoseSkinAge() == 0) {
          result.addError(new RiskError(member.getId(),"Missing Age Skin","missingAgeSkin"));
      }
      else if (member.getAffectedSkinType() == 0 && member.getDiagnoseSkinAge() != 0 && member.getDiagnoseSkinAge() < member.getAge()) {
          result.addError(new RiskError(member.getId(),"Missing Affected for Skin Diagnose Age","missingAffectedForDiagnoseSkin"));
      }
      if (member.getAge() < 0) {
          result.addError(new RiskError(member.getId(),"Invalid Age","invalidAge"));
      }

  }
  protected void validatePancData(com.objex.risk.beans.panc.Person proband, PancResult result, com.objex.risk.beans.panc.Risk risk) {
      if (proband.getAge() >= risk.getAgeTo()) {
          result.addError(new RiskError(proband.getId(),"Proband age is too high","ageTooHighForModelSettings"));
      }
      for (com.objex.risk.beans.panc.Person member : risk.getFamily()) {
          validatePancMemberData(member, result);
      }

  }

    /**
   * Member validations for BRCA
   * @param member
   * @param result
   * @param risk
   */
  private void validatePancMemberData(com.objex.risk.beans.panc.Person member, PancResult result) {
      if (member.getAffectedPancreasType() != 0 && member.getDiagnosePancreasAge() == 0 ) {
          result.addError(new RiskError(member.getId(),"Missing Age Pancreas","missingAgePancreas"));
      }
      else if (member.getAffectedPancreasType() == 0 && member.getDiagnosePancreasAge() != 0 && member.getDiagnosePancreasAge() < member.getAge()) {
          result.addError(new RiskError(member.getId(),"Missing Affected for Diagnose Age Pancreas","missingAffectedForDiagnoseAgePancreas"));
      }
      if (member.getAge() < 0) {
          result.addError(new RiskError(member.getId(),"Invalid Age","invalidAge"));
      }
  }

}
