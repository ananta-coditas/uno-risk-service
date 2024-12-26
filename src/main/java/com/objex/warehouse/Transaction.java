/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.warehouse;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jason
 */
@XmlRootElement(name = "Transaction")
public class Transaction {
    
    public static final String FORMAT = "MM-dd-yyyy hh:mm:ss";
    private String transId = null;
    private Date createDate = null;
    private String clinicCode = null;
    private String familyId = null;
    private TransType type = null;
    private RiskInputs inputs = new RiskInputs();
    private RiskResults results =  new RiskResults();
    private boolean testing = false;

    public Date getCreateDate() {
        return createDate;
    }

    public RiskInputs getInputs() {
        return inputs;
    }

    public RiskResults getResults() {
        return results;
    }
   
    public String getTransId() {
        return transId;
    }

    public boolean isTesting() {
        return testing;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setInputs(RiskInputs inputs) {
        this.inputs = inputs;
    }
    
    

    public void setResults(RiskResults results) {
        this.results = results;
    }
    
    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }
    
    public String getTimestamp() {
        String stamp = "";
        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        if (getCreateDate() != null) {
            stamp = format.format(getCreateDate());
        }
        return stamp;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }


    public void setType(TransType transType) {
        type = transType;
    }
    public TransType getType() {
        return type;
    }
}
