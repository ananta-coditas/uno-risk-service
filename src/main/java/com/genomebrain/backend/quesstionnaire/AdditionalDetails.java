package com.genomebrain.backend.quesstionnaire;

public class AdditionalDetails {

    private  String probandAdditionalNotes;

    public String getProbandAdditionalNotes() {
        return probandAdditionalNotes;
    }

    public void setProbandAdditionalNotes(String probandAdditionalNotes) {
        this.probandAdditionalNotes = probandAdditionalNotes;
    }

    @Override
    public String toString() {
        return "AdditionalDetails{" +
                "probandAdditionalNotes='" + probandAdditionalNotes + '\'' +
                '}';
    }
}
