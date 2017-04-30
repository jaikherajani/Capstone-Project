package com.example.jaikh.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Videos implements Serializable {

    private final static long serialVersionUID = 245403597863113748L;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    /**
     * No args constructor for use in serialization
     */
    public Videos() {
    }

    /**
     * @param results
     */
    public Videos(List<Result> results) {
        super();
        this.results = results;
    }

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}
