/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cts.iot.internal.transfer.objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 307132
 */
public class ResultsForFaces {

    private List<FacesResult> results = new ArrayList<>();

    public void addResult(FacesResult facesResult) {
        results.add(facesResult);
    }

    public List<FacesResult> getResults() {
        return results;
    }

    public void setResults(List<FacesResult> results) {
        this.results = results;
    }

}
