package com.ambry.web;

import com.objex.risk.beans.BaseResult;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jason
 */
@XmlRootElement(name = "GailResult")
public class Test{

    public static final String GAIL = "GAIL";
    
    protected static final Map<String,String> columnToFields = new HashMap<>();
 
    public Map<String, String> getColumnToFields() {
       return  columnToFields;
    }

    public String getModel() {
        return GAIL;
    }
    
}

