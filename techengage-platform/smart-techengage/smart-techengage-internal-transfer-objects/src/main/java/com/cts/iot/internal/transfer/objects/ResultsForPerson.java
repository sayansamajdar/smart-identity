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
public class ResultsForPerson {

    private List<PersonResult> results = new ArrayList<>();

    public void addResult(PersonResult facesResult) {
        results.add(facesResult);
    }

    public List<PersonResult> getResults() {
        return results;
    }

    public void setResults(List<PersonResult> results) {
        this.results = results;
    }

}
