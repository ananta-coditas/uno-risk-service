/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.risk;

import com.objex.progeny.risk.ChartBuilder;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.brca.Person;
import com.objex.risk.beans.brca.Risk;
import com.objex.risk.beans.charts.ChartVO;
import static com.objex.risk.beans.charts.ChartVO.Type.LINE;
import static com.objex.risk.beans.charts.ChartVO.Type.PIE;
import com.objex.risk.beans.charts.DataPoint;
import com.objex.risk.beans.charts.Series;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jason
 */
public class Utils {
    final static Logger log = LoggerFactory.getLogger(Utils.class);

    private static void addGermline(BaseRisk risk, RList germline) {
        
         for (int i=0; i< risk.getFamily().size(); i++) {
            BasePerson person = risk.getFamily().get(i);
            int res = 0;
            for (Object name : germline.names) {
                try
                {
                    res = (int)germline.at((String)name).asDoubles()[i];
                    if ("TestOrder".equals(name)) {
                        person.setTestOrder(res);
                    }
                    else  {
                        TestResult result = TestResult.NONE;
                        if (res == 1) {
                            result =  TestResult.POSITIVE;
                        } else if (res == 2) {
                            result =  TestResult.NEGATIVE;
                        }
                        person.setGermline(germCol(name), result);
                    }
                } catch (Exception e ) { log.error("Problem converting name:" + name + " row:" + i + " value:" + res); }
                  
            }
         }
    }
    
     /**
     * Build a chart based off of a risk result
     * @param result
     * @return 
     */

    public static ChartVO buildChart(BaseResult result) {
        
        ChartVO chart = ChartBuilder.instance().createChart(result.getChartCode(), result.getModel());
        for (Prediction pred : result.getPredictions()) {
            for (Series series : chart.getSeries())   {
                Double value = pred.getResult(series.getName());
                if (value == null) {
                    log.warn("No result for name " + series.getName() + " at age " + pred.getAge());
                } else {
                     Double risk =  value * 100; //charts use percent
                     series.add(new DataPoint(pred.getAge(), risk));
                }
            }         
        }
        return chart;
    }
    
        /**
     * will initialize empty fields from prototype based on context and chart code
     * @param chart 
     */
    public static void copyFromPrototype(ChartVO chart) {
        ChartVO prototype = ChartBuilder.instance().getPrototype(chart.getChartCode(), chart.getContext());
        prototype.copyChart(chart);
    }

    
    public static String germCol(Object name) {
        return (String)name;
    }
    
    public static void saveToFile(String filePath, BaseRisk risk)
    {
         try {
 
		java.io.File file = new java.io.File(filePath); //new java.io.File("C:\\temp\\converted.xml");
		javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(risk.getClass());
		javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
		// output pretty printed
		jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
               // jaxbMarshaller.setProperty("eclipselink.media-type", "application/json");
 
		jaxbMarshaller.marshal(risk, file);
		jaxbMarshaller.marshal(risk, System.out);
 
            } catch (javax.xml.bind.JAXBException e) {
              e.printStackTrace();
            }
    }
    public static void saveToWriter(Writer writer, BaseRisk risk)
    {
         try {
 
		javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(risk.getClass());
		javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
		// output pretty printed
		jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
               // jaxbMarshaller.setProperty("eclipselink.media-type", "application/json");
 
		jaxbMarshaller.marshal(risk, writer);
 
            } catch (javax.xml.bind.JAXBException e) {
              log.error("Problem saving to Writer");
            }
    }
    public static <T extends BaseRisk> T createRiskFromFile(String filePath, Class<T> riskClass) throws JAXBException {
        File file = new File(filePath);
	JAXBContext jaxbContext = JAXBContext.newInstance(riskClass);
	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	T risk = (T) jaxbUnmarshaller.unmarshal(file);
	return risk;   
    }
     public static String dataFrameToString(RList dataFrame) throws REXPMismatchException {
        StringBuilder frame = new StringBuilder("\n");
        for (Object name : dataFrame.names)
        {
            while (((String)name).length() < 22) {
                    name = name + " ";
            }
            frame.append(" " + name);
        }
        int rows = dataFrame.at(0).length();
        for (int i=0; i< rows; i++) {
            frame.append("\n");
            for (Object name : dataFrame.names) {
                    String res = dataFrame.at((String)name).asStrings()[i];
                    if (res == null)
                            res = "null";
                    while (res.length() < 24) {
                            res = res + " ";
                    }
              frame.append(res);
            }
        }
        frame.append("\n");
        return frame.toString();
    }
    public static void saveToBRCAFile(String filePath, RList fam) throws REXPMismatchException  {
       saveToFile(filePath,createBRCARiskWithFamily(fam));
    }
    public static void saveToBRCAFile(String filePath, RList fam, RList germline) throws REXPMismatchException  {
            Risk risk = createBRCARiskWithFamily(fam);
            addGermline(risk,germline);
            saveToFile(filePath,risk);
    }
    public static void saveToMMRFile(String filePath, RList fam) throws REXPMismatchException  {
       saveToFile(filePath,createMMRRiskWithFamily(fam));
    }
    
    public static void saveToMMRFile(String filePath, RList fam, RList germline) throws REXPMismatchException  {
            com.objex.risk.beans.mmr.Risk risk = createMMRRiskWithFamily(fam);
            addGermline(risk,germline);
            saveToFile(filePath,risk);
    }
        
     public static void saveToPancFile(String filePath, RList fam) throws REXPMismatchException  {
       saveToFile(filePath,createPancRiskWithFamily(fam));
    }
     
          public static void saveToFile(String filePath, byte[] data)
    {
        try (OutputStream stream = new FileOutputStream(filePath)) {
            stream.write(data);
        } catch ( Exception e) {
             log.error("Problem with save " + filePath, e);
        }          
    }
     
    public static void saveToMelaFile(String filePath, RList fam) throws REXPMismatchException  {
       saveToFile(filePath,createMelaRiskWithFamily(fam));
    }
    
    public static void saveToMelaFile(String filePath, RList fam, RList germline) throws REXPMismatchException  {
       com.objex.risk.beans.mela.Risk risk = createMelaRiskWithFamily(fam);
       addGermline(risk,germline);
       saveToFile(filePath,risk);
    }
    
    public static Risk createBRCARiskWithFamily(RList fam) throws REXPMismatchException {      
        Risk outRisk = new Risk();
        int rows = fam.at(0).length();
        for (int i=0; i< rows; i++) {
            Person person = new Person();
            outRisk.add(person);
            String res = null;
            for (Object name : fam.names) {
                try
                {
                    res = fam.at((String)name).asStrings()[i];
                    createCommon(name, person, res);
                    if ("AffectedBreast".equals(name)) {
                        person.setAffectedBreastType((int)Double.parseDouble(res));
                    }
                    else if ("AffectedOvary".equals(name)) {
                        person.setAffectedOvarianType((int)Double.parseDouble(res));
                    }
                    else if ("AgeBreast".equals(name)) {
                        person.setDiagnoseBreastAge((int)Double.parseDouble(res));
                    }
                    else if ("AgeOvary".equals(name)) {
                        person.setDiagnoseOvarianAge((int)Double.parseDouble(res));
                    }
                    else if ("AgeBreastContralateral".equals(name)) {
                        person.setDiagnoseBreastContralateralAge((int)Double.parseDouble(res));
                    }
                    else if ("ethnic".equals(name)) {
                        person.setEthnic(Person.Ethnic.valueOf(res));
                    }
                } catch (Exception e ) { log.error("Problem converting name:" + name + " row:" + i + " value:" + res); }
                  
            }
        }
        
    return outRisk;   
        
    }
    public static com.objex.risk.beans.mmr.Risk createMMRRiskWithFamily(RList fam) throws REXPMismatchException {      
        com.objex.risk.beans.mmr.Risk outRisk = new com.objex.risk.beans.mmr.Risk();
        int rows = fam.at(0).length();
        for (int i=0; i< rows; i++) {
            com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
            outRisk.add(person);
            String res = null;
            for (Object name : fam.names) {
                try
                {
                    res = fam.at((String)name).asStrings()[i];
                    createCommon(name, person, res);
                    if ("AffectedColon".equals(name)) {
                        person.setAffectedColonType((int)Double.parseDouble(res));
                    }
                    else if ("AffectedEndometrium".equals(name)) {
                        person.setAffectedEndometriumType((int)Double.parseDouble(res));
                    }
                    else if ("AgeColon".equals(name)) {
                        person.setDiagnoseColonAge((int)Double.parseDouble(res));
                    }
                    else if ("AgeEndometrium".equals(name)) {
                        person.setDiagnoseEndometriumAge((int)Double.parseDouble(res));
                    }
                } catch (Exception e ) { log.error("Problem converting name:" + name + " row:" + i + " value:" + res); }
                  
            }
        }
        
    return outRisk;   
    }
    public static com.objex.risk.beans.panc.Risk createPancRiskWithFamily(RList fam) throws REXPMismatchException {      
        com.objex.risk.beans.panc.Risk outRisk = new com.objex.risk.beans.panc.Risk();
        int rows = fam.at(0).length();
        for (int i=0; i< rows; i++) {
            com.objex.risk.beans.panc.Person person = new com.objex.risk.beans.panc.Person();
            outRisk.add(person);
            String res = null;
            for (Object name : fam.names) {
                try
                {
                    res = fam.at((String)name).asStrings()[i];
                    createCommon(name, person, res);
                    if ("AffectedPancreas".equals(name)) {
                        person.setAffectedPancreasType((int)Double.parseDouble(res));
                    }                   
                    else if ("AgePancreas".equals(name)) {
                        person.setDiagnosePancreasAge((int)Double.parseDouble(res));
                    }       
                } catch (Exception e ) { log.error("Problem converting name:" + name + " row:" + i + " value:" + res); }    
            }
        }
        
    return outRisk;      
    }
     public static com.objex.risk.beans.mela.Risk createMelaRiskWithFamily(RList fam) throws REXPMismatchException {      
        com.objex.risk.beans.mela.Risk outRisk = new com.objex.risk.beans.mela.Risk();
        int rows = fam.at(0).length();
        for (int i=0; i< rows; i++) {
            com.objex.risk.beans.mela.Person person = new com.objex.risk.beans.mela.Person();
            outRisk.add(person);
            String res = null;
            for (Object name : fam.names) {
                try
                {
                    res = fam.at((String)name).asStrings()[i];
                    createCommon(name, person, res);
                    if ("AffectedSkin".equals(name)) {
                        person.setAffectedSkinType((int)Double.parseDouble(res));
                    }                   
                    else if ("AgeSkin".equals(name)) {
                        person.setDiagnoseSkinAge((int)Double.parseDouble(res));
                    }       
                } catch (Exception e ) { log.error("Problem converting name:" + name + " row:" + i + " value:" + res); }    
            }
        }
        
    return outRisk;      
    }
    

    protected static void createCommon(Object name, BasePerson person, String res) throws NumberFormatException {
        if ("ID".equals(name)) {
            person.setId((int)Double.parseDouble(res));
        }
        else if ("Gender".equals(name)) {
            person.setGender((int)Double.parseDouble(res));
        }
        else if ("FatherID".equals(name)) {
            person.setFatherId((int)Double.parseDouble(res));
        }
        else if ("MotherID".equals(name)) {
            person.setMotherId((int)Double.parseDouble(res));
        }
            else if ("Twins".equals(name)) {
            person.setTwin((int)Double.parseDouble(res));
        }
    }
 /**
     * Create image with chart vo
     * @param chartObject
     * @param explode
     * @return 
     */
    public static byte[] plotChartToPiePNG(ChartVO chartObject,boolean explode) {
           byte[] image = null;
        try {
           DefaultPieDataset pieData = new DefaultPieDataset();
           JFreeChart pieChartObject = ChartFactory.createPieChart(chartObject.getTitle(),pieData);
           PiePlot plot = (PiePlot) pieChartObject.getPlot();
           plot.setBackgroundPaint(Color.decode(chartObject.getBackgroundColor()));
           for (Series series : chartObject.getSeries()) {
                  pieData.setValue(series.getLabel(),series.getDataMatrix()[0][0]);
                  plot.setSectionPaint(series.getLabel(),Color.decode(series.getPrimaryColor()));
            }
            
           if (explode) {
                plot.setExplodePercent(chartObject.getSeries().get(0).getLabel(), 0.5);
           }
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           ChartUtils.writeChartAsPNG(bos, pieChartObject, chartObject.getWidth(), chartObject.getHeight());
           image = bos.toByteArray(); 
         }  catch (Exception i)          {
           log.error("Problem generating image ", i);
         }
        return image;
}
     /**
     * turn chart vo into an image
     * @param chart
     * @return 
     */
    public static byte[] plotChartToPNG(ChartVO chart) {
        byte[] response = null;
        if (LINE == chart.getChartType()) {
            response = plotChartToLinePNG(chart,chart.isShowPoints());
        } else if (PIE == chart.getChartType()) {
            response = plotChartToPiePNG(chart,chart.isExplodePie());
        }
        return response;
    }
    /**
     * 
     * @param chartObject
     * @param showPoints
     * @return 
     */
    public static byte[] plotChartToLinePNG(ChartVO chartObject,boolean showPoints) {
         
        byte[] image = null;
        try {
            
           DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
           DefaultXYDataset xydataset = new DefaultXYDataset();
           for (Series series : chartObject.getSeries()) {
             xydataset.addSeries(series.getLabel(),series.getDataMatrix());
               for (DataPoint point : series.getDataPoints()) {
                   Number y = point.getY();
                  categoryDataset.addValue(y,series.getLabel(), point.getX());
                   
               }
           }

            JFreeChart lineChartObject=null;
            if (showPoints) {
                lineChartObject = ChartFactory.createXYLineChart(chartObject.getTitle(),chartObject.getAxisX(),chartObject.getAxisY(),xydataset,PlotOrientation.VERTICAL,true,true,false);                
                XYPlot xyplot = (XYPlot)lineChartObject.getPlot();
                xyplot.setBackgroundPaint(Color.decode(chartObject.getBackgroundColor()));
                for (int i = 0; i < chartObject.getSeries().size(); i++) {
                    xyplot.getRenderer().setSeriesPaint(i,Color.decode(chartObject.getSeries().get(i).getPrimaryColor()));
                    ((XYLineAndShapeRenderer)xyplot.getRenderer()).setSeriesShapesVisible(i, true);
                }
            }
            else {
                lineChartObject=ChartFactory.createLineChart(chartObject.getTitle(),chartObject.getAxisX(),chartObject.getAxisY(),categoryDataset,PlotOrientation.VERTICAL,true,true,false);
                CategoryPlot lplot = (CategoryPlot)lineChartObject.getPlot();
                lplot.setBackgroundPaint(Color.decode(chartObject.getBackgroundColor()));
                for (int i = 0; i < chartObject.getSeries().size(); i++) {
                    lplot.getRenderer().setSeriesPaint(i,Color.decode(chartObject.getSeries().get(i).getPrimaryColor()));
                }
            }
 
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(bos, lineChartObject, chartObject.getWidth(), chartObject.getHeight());
            image = bos.toByteArray();
         }
         catch (Exception i)
         {
              log.error("Problem generating image ", i);
         }
         return image;    
    }
    
    public static void fillMatrix(int[][] matrix, int value) {
        for (int i = 0; i < matrix.length; i++) {
            Arrays.fill(matrix[i], value);
        }
    }
}
