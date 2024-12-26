/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.warehouse;

import com.objex.risk.beans.BaseRisk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jason
 */
@XmlRootElement(name = "RiskInputs")
public class RiskInputs {
    private com.objex.risk.beans.mmr.Risk mmrRisk = null;
    private com.objex.risk.beans.brca.Risk brcaRisk = null;
    private com.objex.risk.beans.mela.Risk melaRisk = null;
    private com.objex.risk.beans.panc.Risk pancRisk = null;
    private com.objex.risk.beans.premm.Risk premmRisk = null;
    private com.objex.risk.beans.gail.Risk gailRisk = null;
    private com.objex.risk.beans.claus.Risk clausRisk = null;
    private com.objex.risk.beans.tcuzick.Risk tyrerRisk = null;
    private String transId = null;

    public RiskInputs(Transaction trans) {
        trans.setInputs(this);
        setTransId(trans.getTransId());
    }
    
    public RiskInputs() {
    }

    public String getTransId() {
        return transId;
    }

   
    public com.objex.risk.beans.mmr.Risk getMmrRisk() {
        return mmrRisk;
    }
    
    public void setTransId(String id) {
        this.transId = id;
    }

    public void setMmrRisk(com.objex.risk.beans.mmr.Risk mmrRisk) {
        this.mmrRisk = mmrRisk;
    }

    public com.objex.risk.beans.brca.Risk getBrcaRisk() {
        return brcaRisk;
    }

    public void setBrcaRisk(com.objex.risk.beans.brca.Risk brcaRisk) {
        this.brcaRisk = brcaRisk;
    }

    public com.objex.risk.beans.mela.Risk getMelaRisk() {
        return melaRisk;
    }

    public void setMelaRisk(com.objex.risk.beans.mela.Risk melaRisk) {
        this.melaRisk = melaRisk;
    }

    public com.objex.risk.beans.panc.Risk getPancRisk() {
        return pancRisk;
    }

    public void setPancRisk(com.objex.risk.beans.panc.Risk pancRisk) {
        this.pancRisk = pancRisk;
    }

    public com.objex.risk.beans.premm.Risk getPremmRisk() {
        return premmRisk;
    }

    public void setPremmRisk(com.objex.risk.beans.premm.Risk premmRisk) {
        this.premmRisk = premmRisk;
    }

    public com.objex.risk.beans.gail.Risk getGailRisk() {
        return gailRisk;
    }

    public void setGailRisk(com.objex.risk.beans.gail.Risk gailRisk) {
        this.gailRisk = gailRisk;
    }

    public com.objex.risk.beans.claus.Risk getClausRisk() {
        return clausRisk;
    }

    public void setClausRisk(com.objex.risk.beans.claus.Risk clausRisk) {
        this.clausRisk = clausRisk;
    }

    public com.objex.risk.beans.tcuzick.Risk getTyrerRisk() {
        return tyrerRisk;
    }

    public void setTyrerRisk(com.objex.risk.beans.tcuzick.Risk tyrerRisk) {
        this.tyrerRisk = tyrerRisk;
    }
    
    public int getFamilySize() {
        int count = 0;
        for (BaseRisk risk : asList()) {
            count = risk.getFamily().size();
            break;
        }
        return count;
    }
    
    public List<? extends BaseRisk> asList() {
        List<BaseRisk> resList = new ArrayList();
        for (BaseRisk res : Arrays.asList(brcaRisk, mmrRisk, melaRisk, pancRisk,premmRisk,gailRisk, clausRisk,tyrerRisk)) {
            if (res != null) {
                resList.add(res);
            }
        }
       return  resList;
    }
    
    
    
}
