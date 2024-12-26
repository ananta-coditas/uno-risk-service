/*
 */
package com.objex.progeny.risk;

import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.charts.Series;
import static com.objex.risk.beans.claus.ClausResult.CLAUS;
import static com.objex.risk.beans.gail.GailResult.GAIL;
import static com.objex.risk.beans.tcuzick.TyrerResult.TYRER;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jward
 */
public class ChartBuilder {
   protected static ChartBuilder builder = new ChartBuilder();
   protected Map<String,Map<Integer,ChartVO>> prototypes = new HashMap<>();
   
   
   protected ChartBuilder() {
       setup();
   }
   public static ChartBuilder instance() {
       return builder;
   }
/**
 * sets up the chart prototypes.  Could use an external file at some point if
 * want to support new charts without compilation
 */
    private void setup() {
        
       ChartVO chart = new ChartVO();
       chart.setChartType(ChartVO.Type.LINE);
       chart.setTitle("Risk By Age");
       chart.setBackgroundColor("#EEEEEE");
       chart.setAxisY("Risk (percent)");
       chart.setShowPoints(Boolean.TRUE);
       chart.setAxisX("Age (years)");
       chart.setSize(640, 480);
       Series breastSeries = chart.addSeries("breastRisk");
       breastSeries.setLabel("Breast Cancer Risk");
       breastSeries.setPrimaryColor("#008DCE");
       addPrototype(GAIL,1,chart);
       addPrototype(CLAUS,1,chart);
       
       chart = new ChartVO();
       chart.setChartType(ChartVO.Type.LINE);
       chart.setTitle("Risk By Age");
       chart.setBackgroundColor("#EEEEEE");
       chart.setAxisY("Risk (percent)");
       chart.setShowPoints(Boolean.TRUE);
       chart.setAxisX("Age (years)");
       chart.setSize(640, 480);
       
       breastSeries = chart.addSeries("breastRisk");
       breastSeries.setLabel("Breast Cancer Risk");
       breastSeries.setPrimaryColor("#008DCE");
       
       Series populationSeries = chart.addSeries("populationRisk");
       populationSeries.setLabel("Population Breast Risk");
       populationSeries.setPrimaryColor("#B7202F"); 
       addPrototype(TYRER,1,chart);
        
    }
    /**
     * add a new protoype by model and chart id
     * @param model
     * @param code
     * @param chart 
     */
    private void addPrototype(String model, Integer code,ChartVO chart) {
        if (prototypes.get(model) == null){
            prototypes.put(model, new HashMap<>());
        }
        prototypes.get(model).put(code,chart);
    }
    public ChartVO createChart(Integer chartCode, String model) {
        ChartVO chart = new ChartVO();
        ChartVO prototype = getPrototype(chartCode,model);
        if (prototype == null) {
            System.out.println("No prototype for model " + model + " code " + chartCode);
        } else {
            prototype.copyChart(chart);
        }
        chart.setChartCode(chartCode); //prototypes can be associated with many codes
        chart.setContext(model);//prototypes can be associated with many models
        return chart;
    }

    ChartVO getPrototype(Integer chartCode, String model) {
       ChartVO chart = null;
        if (prototypes.get(model) != null) {
           chart = prototypes.get(model).get(chartCode);
        }
        return chart;
    }
}
