package com.example.jaikh.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductionCompany implements Serializable {

    private final static long serialVersionUID = 5842257756446828210L;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Long id;

    /**
     * No args constructor for use in serialization
     */
    public ProductionCompany() {
    }

    /**
     * @param id
     * @param name
     */
    public ProductionCompany(String name, Long id) {
        super();
        this.name = name;
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
    }

}
