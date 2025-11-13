/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambry.riskmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.objex.risk.beans.BasePerson;
import com.objex.risk.beans.BaseRisk;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jason
 */
public class Utils {
    final static Logger log = LoggerFactory.getLogger(Utils.class);
    
     final static BasePerson NONE = new BasePerson() {

        @Override
        protected List<String> germLines() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
     };
    static{
        NONE.setFatherId(-1);
        NONE.setMotherId(-1);
    }
    
     public static void saveToFile(String filePath, byte[] data)
    {
        try (OutputStream stream = new FileOutputStream(filePath)) {
            stream.write(data);
        } catch ( Exception e) {
             log.error("Problem with save " + filePath, e);
        }          
    }
     
    /**
     * Save Risk object to file
     * @param filePath The path where to save the file
     * @param risk The risk object to save
     * @param usexml Whether to save as XML (true) or JSON (false)
     */
    public static void saveToFile(String filePath, BaseRisk risk, boolean usexml) {
        try {
            log.info("Saving to: {}", filePath);
            File file = new File(filePath);
            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(risk.getClass());
            javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            if (usexml) {
                jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            } else {
                ObjectMapper om = new ObjectMapper();
                ObjectWriter ow = om.writer();
                String json = ow.writeValueAsString(risk);
                log.debug("JSON content: {}", json);
                
                System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
                jaxbMarshaller.setProperty("eclipselink.media-type", "application/json");
            }

            jaxbMarshaller.marshal(risk, file);
        } catch (javax.xml.bind.JAXBException e) {
            log.error("Problem saving risk to file: {}", filePath, e);
            throw new RuntimeException("Failed to save risk to file: " + filePath, e);
        } catch (JsonProcessingException ex) {
            log.error("Problem converting risk to JSON for file: {}", filePath, ex);
            throw new RuntimeException("Failed to convert risk to JSON: " + filePath, ex);
        }
    }
    
    /**
     * Save result object to file as JSON
     * @param filePath The path where to save the file
     * @param result The result object to save
     */
    public static void saveToFile(String filePath, Object result) {
        try {
            log.info("Saving to: {}", filePath);
            File file = new File(filePath);
            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(result.getClass());
            javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(result, file);
        } catch (javax.xml.bind.JAXBException e) {
            log.error("Problem saving result to file: {}", filePath, e);
            throw new RuntimeException("Failed to save result to file: " + filePath, e);
        }
    }
    /**
     * Create a Risk object from an XML file
     * @param filePath The path to the XML file (can be relative path like "test/exampleTyrerFamily.xml" or absolute path)
     * @param riskClass The class of the risk object to create
     * @param <T> The type extending BaseRisk
     * @return The risk object created from the file
     * @throws JAXBException If there's an error parsing the XML file
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseRisk> T createRiskFromFile(String filePath, Class<T> riskClass) throws JAXBException {
        File file = null;
        java.io.InputStream inputStream = null;
        
        // First try as direct file path
        file = new File(filePath);
        if (!file.exists()) {
            // Try to load from classpath resources
            try {
                java.net.URL resourceUrl = Utils.class.getClassLoader().getResource(filePath);
                if (resourceUrl != null) {
                    if ("file".equals(resourceUrl.getProtocol())) {
                        file = new File(resourceUrl.getPath());
                    } else {
                        // For jar URLs, we need to use InputStream
                        inputStream = resourceUrl.openStream();
                    }
                } else {
                    // Try with test-input-data/risk/ prefix
                    String resourcePath = "test-input-data/risk/" + filePath.replace("test/", "");
                    resourceUrl = Utils.class.getClassLoader().getResource(resourcePath);
                    if (resourceUrl != null) {
                        if ("file".equals(resourceUrl.getProtocol())) {
                            file = new File(resourceUrl.getPath());
                        } else {
                            // For jar URLs, we need to use InputStream
                            inputStream = resourceUrl.openStream();
                        }
                    }
                }
            } catch (java.io.IOException e) {
                // If classpath loading fails, continue with original file
                log.debug("Could not load from classpath: {}", filePath, e);
            } catch (Exception e) {
                // If classpath loading fails, continue with original file
                log.debug("Could not load from classpath: {}", filePath, e);
            }
        }
        
        JAXBContext jaxbContext;
        try {
            // Try to create JAXBContext with just the risk class using default implementation
            jaxbContext = JAXBContext.newInstance(riskClass);
        } catch (com.sun.xml.bind.v2.runtime.IllegalAnnotationsException | RuntimeException e) {
            // If we get IllegalAnnotationsException due to duplicate annotations,
            // try using EclipseLink MOXy JAXBContextFactory which is more lenient
            log.warn("JAXB context creation failed with IllegalAnnotationsException, trying with EclipseLink MOXy: {}", e.getMessage());
            try {
                // Use EclipseLink MOXy JAXBContextFactory which handles duplicate annotations more gracefully
                // Use the standard property name for JAXBContextFactory
                System.setProperty("javax.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
                jaxbContext = JAXBContext.newInstance(riskClass);
                // Reset to default after successful creation
                System.clearProperty("javax.xml.bind.JAXBContextFactory");
            } catch (Exception e2) {
                // If MOXy also fails, try with related classes
                log.warn("EclipseLink MOXy context creation also failed, trying with related classes: {}", e2.getMessage());
                try {
                    Class<?>[] classes = new Class<?>[]{
                        riskClass,
                        com.objex.risk.beans.uno.Person.class,
                        com.objex.risk.beans.uno.Model.class
                    };
                    jaxbContext = JAXBContext.newInstance(classes);
                } catch (Exception e3) {
                    // Last resort: rethrow the original exception
                    log.error("All JAXB context creation attempts failed for class: {}", riskClass.getName(), e3);
                    // Reset property before throwing
                    System.clearProperty("javax.xml.bind.JAXBContextFactory");
                    throw new JAXBException("Failed to create JAXBContext for " + riskClass.getName() + 
                        ". Original error: " + e.getMessage() + 
                        ". Secondary error: " + e2.getMessage() + 
                        ". Tertiary error: " + e3.getMessage(), e);
                }
            }
        }
        
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        T risk;
        
        try {
            if (inputStream != null) {
                // Use InputStream for jar resources
                risk = (T) jaxbUnmarshaller.unmarshal(inputStream);
            } else if (file != null && file.exists()) {
                // Use File for file system resources
                risk = (T) jaxbUnmarshaller.unmarshal(file);
            } else {
                throw new JAXBException("File not found: " + filePath);
            }
        } finally {
            // Ensure inputStream is closed even if an exception occurs
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.io.IOException e) {
                    log.warn("Failed to close input stream for file: {}", filePath, e);
                }
            }
        }
        
        return risk;
    }
     
     public static boolean inRange( Integer age,int floor, int ceiling, int defaultValue) {
      return (age == defaultValue) || (age <= ceiling && age >= floor);
    }
   
    /**
     * Save MMR risk to file
     * @param filePath The path where to save the file
     * @param fam The family data as ArrayList
     */
    public static void saveToMMRFile(String filePath, ArrayList<?> fam) {
        saveToFile(filePath, createMMRRiskWithFamily(fam));
    }

    /**
     * Create MMR Risk from family ArrayList
     * @param fam The family data as ArrayList
     * @return The MMR Risk object
     */
    public static com.objex.risk.beans.mmr.Risk createMMRRiskWithFamily(ArrayList<?> fam) {      
        com.objex.risk.beans.mmr.Risk outRisk = new com.objex.risk.beans.mmr.Risk();
        int rows = fam.size();
        for (int i=0; i< rows; i++) {
            com.objex.risk.beans.mmr.Person person = new com.objex.risk.beans.mmr.Person();
            outRisk.add(person);
            String res = null;
            for (Object name : fam) {
                try
                {
                    res = null;
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
    }
    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getParents(P person, BaseRisk risk) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : risk.getFamily()) {
            if (rel.isParent(person)) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }
    
    /**
     * removes a gender from the passed list.  
     * @param <P>
     * @param people
     * @param keepFemales  if true keeps only the females, if false keeps only males
     * @return 
     */
    
    public static <P extends BasePerson> List<P> filterSex(List<P> people, boolean keepFemales) {
        List<P> relatives = new ArrayList<>();
        int gender = keepFemales ? 0 : 1;
        for (BasePerson rel : people) {
            if (rel.getGender() == gender) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }

    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getSiblings(P person, BaseRisk risk) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : risk.getFamily()) {
            if (rel.isSibling(person)) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }

    public static <P extends BasePerson> List<P> getSiblings(P person, List<P> family) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : family) {
            if (rel.isSibling(person)) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }

    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getHalfSiblings(P person, BaseRisk risk) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : risk.getFamily()) {
            if (rel.isHalfSibling(person)) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }

    public static <P extends BasePerson> List<P> getHalfSiblings(P person, P parent, BaseRisk risk) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : getHalfSiblings(person, risk)) {
            if (rel.isChild(parent)) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }

    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getChildren(P person, BaseRisk risk) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : risk.getFamily()) {
            if (rel.isChild(person)) {
                relatives.add((P) rel);
            }
        }
        return relatives;
    }
    /**
     * returns children of everyone in the list
     * @param <P>
     * @param parents
     * @param risk
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getChildren(List<P> parents, BaseRisk risk) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson rel : risk.getFamily()) {
            for (BasePerson parent : parents) {
                if (rel.isChild(parent)) {
                    relatives.add((P) rel);
                    break;
                }
            }
        }
        return relatives;
    }

    /**
     * Get first-degree relatives (parent, sibling, or child) of the proband
     */
    public static <P extends BasePerson> List<P> getFirstDegreeRelatives(BaseRisk risk, Class<P> clazz) {
        BasePerson proband = risk.getFamily().get(0);
        return getFirstDegreeRelatives(proband, risk, clazz);
    }

    /**
     * Get first-degree relatives (parent, sibling, or child) of a person
     */
    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getFirstDegreeRelatives(BasePerson person, BaseRisk risk, Class<P> clazz) {
        List<P> relatives = new ArrayList<>();
        for (BasePerson relative : risk.getFamily()) {
            if (person == relative) {
                continue;
            }
            if (relative.isParent(person) || relative.isChild(person) || relative.isSibling(person)) {
                relatives.add((P) relative);
            }
        }
        return relatives;
    }
    
    /**
     * A second-degree relative is defined as a blood relative which includes the individual's grandparents, 
     * grandchildren, aunts, uncles, nephews, nieces or half-siblings
     */
    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getSecondDegreeRelatives(BaseRisk risk, Class<P> clazz) {
        BasePerson father = NONE;
        BasePerson mother = NONE;
        ArrayList<Integer> children = new ArrayList<>();
        ArrayList<Integer> siblings = new ArrayList<>();
        List<P> relatives = new ArrayList<>();
        BasePerson proband = risk.getFamily().get(0);
        for (BasePerson person : risk.getFamily()) {
            if (proband == person) {
                continue;
            }
            if (person.isMother(proband)) {
                mother = person;
            } else if (person.isFather(proband)) {
                father = person;
            } else if (proband.isParent(person)) {
                children.add(person.getId());
            } else if (proband.isSibling(person)) {
                siblings.add(person.getId());
            }
        }
         for (BasePerson person : risk.getFamily()) { 
            if (proband == person) {
                continue;
            }
            if (person.isParent(father) || person.isParent(mother)) {
                relatives.add((P) person);   // grandparents
            } else if (person.isSibling(father) || person.isSibling(mother)) {
                relatives.add((P) person);   // aunts & uncles
            } else if (children.contains(person.getMotherId()) || children.contains(person.getFatherId())) {
                relatives.add((P) person);  // grandchild
            } else if (siblings.contains(person.getMotherId()) || siblings.contains(person.getFatherId())) {
                relatives.add((P) person);  // nieces nephews
            } else if (!siblings.contains(person.getId()) && ((proband.getMotherId() != 0 && proband.getMotherId().equals(person.getMotherId())) || (proband.getFatherId() != 0 && proband.getFatherId().equals(person.getFatherId())))) {
                relatives.add((P) person);   // halfsibling
            }
        }
        return relatives;
    }
    
    
    /**
     * A second-degree relative is defined as a blood relative which includes the individual's grandparents, 
     * grandchildren, aunts, uncles, nephews, nieces or half-siblings
     */
    @SuppressWarnings("unchecked")
    public static <P extends BasePerson> List<P> getSideOfFamilySecondDegreeRelatives(BaseRisk risk, Class<P> clazz, boolean maternal) {
        BasePerson father = NONE;
        BasePerson mother = NONE;
         BasePerson parent = NONE;
        Integer parentId = 0;
        ArrayList<Integer> children = new ArrayList<>();
        ArrayList<Integer> siblings = new ArrayList<>();
        List<P> relatives = new ArrayList<>();
        BasePerson proband = risk.getFamily().get(0);
        for (BasePerson person : risk.getFamily()) {
            if (proband == person) {
                continue;
            }
            if (person.isMother(proband)) {
                mother = person;
            } else if (person.isFather(proband)) {
                father = person;
            } else if (proband.isParent(person)) {
                children.add(person.getId());
            } else if (proband.isSibling(person)) {
                siblings.add(person.getId());
            }
        }
        
        if (maternal) {
            parent = mother;
        } else {
            parent = father;
        }
        
        if (parent != null){
            parentId = parent.getId();
        }
        
        for (BasePerson person : risk.getFamily()) { 
            if (proband == person) {
                continue;
            }
            if (person.isParent(parent)) {
                relatives.add((P) person);   // grandparents
            } else if (person.isSibling(parent)) {
                relatives.add((P) person);   // aunts & uncles
            } else if (children.contains(person.getMotherId()) || children.contains(person.getFatherId())) {
                relatives.add((P) person);  // grandchild
            } else if (siblings.contains(person.getMotherId()) || siblings.contains(person.getFatherId())) {
                relatives.add((P) person);  // nieces nephews
            } else if (!siblings.contains(person.getId()) && ((parentId != 0 && parentId.equals(person.getMotherId())) || (parentId.equals(person.getFatherId())))) {
                relatives.add((P) person);   // halfsibling
            }
        }
        return relatives;
    }
    
    /**
     * Build a chart based off of a risk result
     * NOTE: This method and related chart plotting methods are commented out as they require
     * additional dependencies (JFreeChart) that are not included in the test scope.
     * Uncomment and add dependencies if chart generation functionality is needed.
     */
    /*
    public static ChartVO buildChart(BaseResult result) {
        ChartVO chart = ChartBuilder.instance().createChart(result.getChartCode(), result.getModel());
        for (Prediction pred : result.getPredictions()) {
            for (Series series : chart.getSeries()) {
                Double risk = pred.getResult(series.getName()) * 100; //charts use percent
                series.add(new DataPoint(pred.getAge(), risk));
            }
        }
        return chart;
    }

    public static byte[] plotChartToPNG(ChartVO chart) {
        byte[] response = null;
        if (LINE == chart.getChartType()) {
            response = plotChartToLinePNG(chart, chart.isShowPoints());
        } else if (PIE == chart.getChartType()) {
            response = plotChartToPiePNG(chart, chart.isExplodePie());
        }
        return response;
    }

    public static void copyFromPrototype(ChartVO chart) {
        ChartVO prototype = ChartBuilder.instance().getPrototype(chart.getChartCode(), chart.getContext());
        prototype.copyChart(chart);
    }

    public static byte[] plotChartToPiePNG(ChartVO chartObject, boolean explode) {
        byte[] image = null;
        try {
            DefaultPieDataset pieData = new DefaultPieDataset();
            JFreeChart pieChartObject = ChartFactory.createPieChart(chartObject.getTitle(), pieData);
            PiePlot plot = (PiePlot) pieChartObject.getPlot();
            plot.setBackgroundPaint(Color.decode(chartObject.getBackgroundColor()));
            for (Series series : chartObject.getSeries()) {
                pieData.setValue(series.getLabel(), series.getDataMatrix()[0][0]);
                plot.setSectionPaint(series.getLabel(), Color.decode(series.getPrimaryColor()));
            }

            if (explode) {
                plot.setExplodePercent(chartObject.getSeries().get(0).getLabel(), 0.5);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(bos, pieChartObject, chartObject.getWidth(), chartObject.getHeight());
            image = bos.toByteArray();
        } catch (Exception i) {
            log.error("Problem generating image ", i);
        }
        return image;
    }

    public static byte[] plotChartToLinePNG(ChartVO chartObject, boolean showPoints) {
        byte[] image = null;
        try {
            DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
            DefaultXYDataset xydataset = new DefaultXYDataset();
            for (Series series : chartObject.getSeries()) {
                xydataset.addSeries(series.getLabel(), series.getDataMatrix());
                for (DataPoint point : series.getDataPoints()) {
                    Number y = point.getY();
                    categoryDataset.addValue(y, series.getLabel(), point.getX());
                }
            }

            JFreeChart lineChartObject = null;
            if (showPoints) {
                lineChartObject = ChartFactory.createXYLineChart(chartObject.getTitle(), chartObject.getAxisX(), chartObject.getAxisY(), xydataset, PlotOrientation.VERTICAL, true, true, false);
                XYPlot xyplot = (XYPlot) lineChartObject.getPlot();
                xyplot.setBackgroundPaint(Color.decode(chartObject.getBackgroundColor()));
                for (int i = 0; i < chartObject.getSeries().size(); i++) {
                    xyplot.getRenderer().setSeriesPaint(i, Color.decode(chartObject.getSeries().get(i).getPrimaryColor()));
                    ((XYLineAndShapeRenderer) xyplot.getRenderer()).setSeriesShapesVisible(i, true);
                }
            } else {
                lineChartObject = ChartFactory.createLineChart(chartObject.getTitle(), chartObject.getAxisX(), chartObject.getAxisY(), categoryDataset, PlotOrientation.VERTICAL, true, true, false);
                CategoryPlot lplot = (CategoryPlot) lineChartObject.getPlot();
                lplot.setBackgroundPaint(Color.decode(chartObject.getBackgroundColor()));
                for (int i = 0; i < chartObject.getSeries().size(); i++) {
                    lplot.getRenderer().setSeriesPaint(i, Color.decode(chartObject.getSeries().get(i).getPrimaryColor()));
                }
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(bos, lineChartObject, chartObject.getWidth(), chartObject.getHeight());
            image = bos.toByteArray();
        } catch (Exception i) {
            log.error("Problem generating image ", i);
        }
        return image;
    }
    */
}
