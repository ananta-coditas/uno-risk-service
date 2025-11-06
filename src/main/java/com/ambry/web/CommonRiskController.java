package com.ambry.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ambry.service.CommonRiskService;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@RestController
@RequestMapping({"/api","/risk"})
public class CommonRiskController {

    @Autowired
    private CommonRiskService commonRiskService;

    @PostMapping("/risk/models/calculate")
    public ResponseEntity<CombinedResult> calculateRiskModelsDeprecated(@RequestBody Risk risk) {
        return calculateRiskModelsV1(risk);
    }
    @PostMapping("/api/models/calculate")
    public ResponseEntity<CombinedResult> calculateRiskModels(@RequestBody Risk risk) {
        return calculateRiskModelsV1(risk);
    }
    @PostMapping("/v1/risk/models/calculate")
    public ResponseEntity<CombinedResult> calculateRiskModelsV1Deprecated(@RequestBody Risk risk) {
        CombinedResult combinedResult = commonRiskService.calculateRiskModels(risk);
        return ResponseEntity.ok(combinedResult);
    }
    @PostMapping("/api/v1/models/calculate")
    public ResponseEntity<CombinedResult> calculateRiskModelsV1(@RequestBody Risk risk) {
        CombinedResult combinedResult = commonRiskService.calculateRiskModels(risk);
        return ResponseEntity.ok(combinedResult);
    }

    @PostMapping("/risk/models/calculate1")
    public ResponseEntity<TestResult> calculateRiskModels1(@RequestBody Risk risk) {
        TestResult result = new TestResult();
        return ResponseEntity.ok(result);
    }
}
class TestResult extends BaseResult{

    @Override
    public String getModel() {
        // TODO Auto-generated method stub
        return "OK";
    }

    @Override
    public Map<String, String> getColumnToFields() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

}
