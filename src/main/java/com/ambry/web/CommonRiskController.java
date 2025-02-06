package com.ambry.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ambry.service.CommonRiskService;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Risk;

@RestController
@RequestMapping("/api")
public class CommonRiskController {

    @Autowired
    private CommonRiskService commonRiskService;
    
    @PostMapping("/risk/models/calculate")
    public ResponseEntity<CombinedResult> calculateRiskModels(@RequestBody Risk risk) {
        CombinedResult combinedResult = commonRiskService.calculateRiskModels(risk);
        return ResponseEntity.ok(combinedResult);
    }
}
