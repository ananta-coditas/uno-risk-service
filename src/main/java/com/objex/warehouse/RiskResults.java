/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.warehouse;

import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.brca.BRCAResult;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.mela.MelaResult;
import com.objex.risk.beans.mmr.MMRResult;
import com.objex.risk.beans.panc.PancResult;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.tcuzick.TyrerResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jason
 */
@XmlRootElement(name = "RiskResults")
public class RiskResults {
    byte[] pedImage = null;
    MMRResult mmrResult = null;
    BRCAResult brcaResult = null;
    MelaResult melaResult = null;
    PancResult pancResult = null;
    PREMMResult premmResult = null;
    GailResult gailResult = null;
    ClausResult clausResult = null;
    TyrerResult tyrerResult = null;
    private String transId = null; 
   

    public RiskResults() {
    }
    
    public RiskResults(Transaction trans) {
        setTransId(trans.getTransId());
    }

    public GailResult getGailResult() {
        return gailResult;
    }

    public ClausResult getClausResult() {
        return clausResult;
    }
    
    public MelaResult getMelaResult() {
        return melaResult;
    }

    public PancResult getPancResult() {
        return pancResult;
    }

    public byte[] getPedImage() {
        return pedImage;
    }

    List<? extends BaseResult> getResults(List<String> supportedModels) {
        List results = new ArrayList();
        for (BaseResult result : asList()) {
            if (supportedModels.contains(result.getModel())) {
                results.add(result);
            }
        }
        return results;
    }

    public void setClausResult(ClausResult clausResult) {
        this.clausResult = clausResult;
    }

    public void setGailResult(GailResult gailResult) {
        this.gailResult = gailResult;
    }

    public void setMelaResult(MelaResult melaResult) {
        this.melaResult = melaResult;
    }

    public void setPancResult(PancResult pancResult) {
        this.pancResult = pancResult;
    }
    
    public void setPedImage(byte[] pedImage) {
        this.pedImage = pedImage;
    }

    public MMRResult getMMRResult() {
        return mmrResult;
    }

    public void setMMRResult(MMRResult mmrResult) {
        this.mmrResult = mmrResult;
    }

    public BRCAResult getBrcaResult() {
        return brcaResult;
    }

    public void setBrcaResult(BRCAResult brcaResult) {
        this.brcaResult = brcaResult;
    }
    public List<? extends BaseResult> asList() {
        List<BaseResult> resList = new ArrayList();
        for (BaseResult res : Arrays.asList(brcaResult, mmrResult, melaResult, pancResult,premmResult,gailResult, tyrerResult)) {
            if (res != null) {
                resList.add(res);
            }
        }
       return  resList;
    }
    
    public PREMMResult getPremmResult() {
        return premmResult;
    }

    public void setPremmResult(PREMMResult premmResult) {
       this.premmResult = premmResult;
    }
    public Set<String> getVersions() {
        Set versions = new HashSet();
        for (BaseResult res  : asList()) {
            versions.add(res.getVersion());
        }
        return versions;
    }
    
        public TyrerResult getTyrerResult() {
        return tyrerResult;
    }

    public void setTransId(String id) {
        transId = id;
    }
    
    public String getTransId() {
        return transId;
    }

    public void setTyrerResult(TyrerResult result) {
        tyrerResult = result;
    }
   
}
