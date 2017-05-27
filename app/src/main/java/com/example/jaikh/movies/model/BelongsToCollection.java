
package com.example.jaikh.movies.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BelongsToCollection implements Serializable
{

    private long id;
    private String name;
    private String posterPath;
    private String backdropPath;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1422065792396002208L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public BelongsToCollection() {
    }

    /**
     * 
     * @param id
     * @param posterPath
     * @param name
     * @param backdropPath
     */
    public BelongsToCollection(long id, String name, String posterPath, String backdropPath) {
        super();
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
