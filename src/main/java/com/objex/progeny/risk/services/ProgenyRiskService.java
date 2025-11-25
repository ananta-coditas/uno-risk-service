/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.progeny.risk.services;

import com.ambry.config.ApplicationProperties;
import com.objex.progeny.risk.Utils;
import com.objex.progeny.risk.claus.ClausDelegate;
import com.objex.progeny.risk.gail.GailDelegate;
import com.objex.progeny.risk.premm.PremmDelegate;
import com.objex.progeny.risk.tycuz.TyrerDelegate;
import com.objex.risk.beans.*;
import com.objex.risk.beans.charts.ChartResult;
import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.tcuzick.TyrerResult;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import java.util.ArrayList;
import java.util.Map;

/**
 * REST Web Service
 *
 * @author jason
 */
@RestController
//@RequestMapping("/calculate")
public class ProgenyRiskService {

    final static Logger LOG = LoggerFactory.getLogger(ProgenyRiskService.class);
    protected static boolean saveRequests = false;
    protected static String savePath = null;

    private static String OS = System.getProperty("os.name").toLowerCase();
    private String tyrerCommand;
    private final ApplicationProperties applicationProperties;

    public ProgenyRiskService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Method that runs the premm algorithm
     *
     * @param risk
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    // @PostMapping(value = "/premm", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    //         "application/json" })
    public PREMMResult computePREMM(@RequestBody com.objex.risk.beans.premm.Risk risk) {
        PremmDelegate delegate = new PremmDelegate();

        long time = System.currentTimeMillis();
        PREMMResult result = null;
        try {
            checkForSaveOff(risk);
            result = delegate.calculate(risk);
            long total = System.currentTimeMillis() - time;
            LOG.info("Risk Request time for PREMM is " + (total / 1000) + " seconds");

        } catch (Exception e) {
            LOG.error(" Risk Server Error", e);
           throw new ServerErrorException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method that runs the gail algorithm
     *
     * @param risk
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    // @PostMapping(value = "/gail", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public @ResponseBody GailResult computeGail(@RequestBody com.objex.risk.beans.gail.Risk risk) {

        long time = System.currentTimeMillis();
        GailResult result = null;
        GailDelegate delegate = new GailDelegate();
        try {
            checkForSaveOff(risk);
            result = delegate.calculate(risk);
            result.setChartCode(risk.getChartCode());
            buildChartForResult(result);
            long total = System.currentTimeMillis() - time;
            LOG.info("Risk Request time for Gail is " + (total / 1000) + " seconds");

        } catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            throw new ServerErrorException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * this method takes a populated result and will build a populated chart object
     * with the associated chart code.
     *
     * @param result
     */

    protected void buildChartForResult(BaseResult result) {
        if (result.getChartCode() == null || result.getPredictions().isEmpty()) {
            LOG.info("No info to chart for chartcode" + result.getChartCode() + " predictions:"
                    + result.getPredictions().size());
            return;
        }
        ChartVO chart = Utils.buildChart(result);
        plotResultImageFromChart(result, chart);
    }

    /**
     * Method that runs the claus algorithm
     *
     * @param risk
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    // @PostMapping(value = "/claus", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public ClausResult computeClaus(@RequestBody com.objex.risk.beans.claus.Risk risk) {

        long time = System.currentTimeMillis();
        ClausResult result = null;
        ClausDelegate delegate = new ClausDelegate();
        try {
            checkForSaveOff(risk);

            result = delegate.calculate(risk);
            result.setChartCode(risk.getChartCode());
            buildChartForResult(result);
            long total = System.currentTimeMillis() - time;
            LOG.info("Risk Request time for Clasue is " + (total / 1000) + " seconds");

        } catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            throw new ServerErrorException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method that runs the tyrer algorithm
     *
     * @param risk
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    // @PostMapping(value = "/tyrer", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public TyrerResult computeTyrer(@RequestBody com.objex.risk.beans.tcuzick.Risk risk) {
        // String cmd = "C:\\temp\\tyrer\\tcuzick-wincmd.exe";
        long time = System.currentTimeMillis();
        TyrerResult result = null;
        TyrerDelegate delegate = new TyrerDelegate(tyrerCommand);
        try {
            checkForSaveOff(risk);
            if (saveRequests) {
                risk.setSaveIncoming(true);
            }

            result = delegate.calculate(risk);
            result.setChartCode(risk.getChartCode());
            buildChartForResult(result);
            long total = System.currentTimeMillis() - time;
            LOG.info("Risk Request time for Tyrer Cuzick is " + (total / 1000) + " seconds");

        } catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            throw new ServerErrorException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method that runs a command
     *
     * @param command
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    // @PostMapping(value = "/runCommand", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public RiskCommand runCommand(@RequestBody RiskCommand command) {
        long time = System.currentTimeMillis();
        try {
            String response = "command not understood";
            switch (command.getName()) {
            case "LIST":
                response = "LIST\nSAVE_REQUESTS";
                break;
            case "SAVE_REQUESTS":
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

        } catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            command.setResponse("Unknown failure");
            return command;
        }
        return command;
    }

    /**
     * {
     *
     * if parameters are set, this is a way of saving off the incoming risk request
     * by setting the isSaveIncoming field on the risk or the save.all config
     * setting. this basically would be used for extreme debugging scenarios
     *
     * @param risk
     */
    protected void checkForSaveOff(BaseRisk risk) {
        // This will be handling via Service Mesh
    }

    /**
     *
     * @param <T>
     * @param c
     * @param result
     * @return
     */
    protected <T extends BaseResult> T buildResult(Map<String, String> predictions, Map<String, Double> probabilities,
            T result) {
        int rows = predictions.size();
        for (int i = 0; i < rows; i++) {
            Prediction prediction = result.addPrediction();
            for (Object name : predictions.keySet()) {
                String value = predictions.get(name);
                try {
                    prediction.addRisk((String) name, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    LOG.info("Result value was not a number for name: " + name + " value:" + value + " setting value="
                            + Double.NaN);
                    prediction.addRisk((String) name, Double.NaN);
                }
            }
        }

        for (Object name : probabilities.keySet()) {
            String col = (String) name;
            result.addProbability(col, probabilities.get(col));
        }
        return result;
    }

    protected void addCommonFields(ArrayList<Integer> ids, BasePerson person, ArrayList<Integer> genders,
            ArrayList<Integer> fatherids, ArrayList<Integer> motherids) {
        ids.add(person.getId());
        genders.add(person.getGender());
        fatherids.add(person.getFatherId());
        motherids.add(person.getMotherId());
    }

    @PostConstruct
    public void init() {
        try {
            if (OS.startsWith("win")) {
                tyrerCommand = applicationProperties.getTyrer().getWindows().getCommand();
            } else {
                tyrerCommand = applicationProperties.getTyrer().getLinux().getCommand();
            }
        } catch (Exception e) {
            LOG.error("could not init, setting parameters to defaults", e);
        }
    }

    /**
     * This method takes a chart populated with data and will generate the image to
     * set on the result
     *
     * @param result
     * @param chart
     */
    protected void plotResultImageFromChart(BaseResult result, ChartVO chart) {
        byte[] chartBytes = Utils.plotChartToPNG(chart);
        result.setChart(chartBytes);
    }

    // @PostMapping(value = "/plotChart", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public ChartResult plotChartToResultImage(@RequestBody ChartVO chart) {

        long time = System.currentTimeMillis();
        Utils.copyFromPrototype(chart);
        ChartResult result = new ChartResult();
        result.setContext(chart.getContext());
        try {
            // if (saveRequests) {
            // risk.setSaveIncoming(true);
            // }
            result.setChartCode(chart.getChartCode());
            plotResultImageFromChart(result, chart);
            long total = System.currentTimeMillis() - time;
            LOG.info("Risk Request time for Charting Tyrer Cuzick is " + (total / 1000) + " seconds");

        } catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            throw new ServerErrorException(e.getMessage(), e);
        }
        return result;
    }

    // @PostMapping(value = "/chartResult", consumes = { "application/xml", "application/json" }, produces = { "application/xml",
    // "application/json" })
    public BaseResult plotResult(@RequestBody BaseResult result) {

        long time = System.currentTimeMillis();
        try {
            buildChartForResult(result);
        }

        catch (Exception e) {
            LOG.error(" Risk Server Error", e);
            throw new ServerErrorException(e.getMessage(), e);
        }
        long total = System.currentTimeMillis() - time;
        LOG.info("plot result time for Tyrer Cuzick is " + (total / 1000) + " seconds");
        return result;
    }
}
