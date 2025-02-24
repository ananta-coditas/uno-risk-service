package com.ambry.transformer.core;

import java.util.Map;

import com.objex.risk.beans.BaseResult;

public class RiskModelErrorDto extends BaseResult{

    @Override
    public String getModel() {
        return null;
    }

    @Override
    public Map<String, String> getColumnToFields() {
        return null;
    }

}
