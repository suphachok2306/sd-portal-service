package com.pcc.portalservice.model;

import java.util.List;

public class BeanGeneric9 {
    private List<String> course_names;
    private List<Float> results;
    private String name;
    private Long id;

    public BeanGeneric9(List<String> course_names, List<Float> results, String name) {
        this.course_names = course_names;
        this.results = results;
        this.name = name;
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<String> getCourse_names() {
        return course_names;
    }

    public void setCourse_names(List<String> course_names) {
        this.course_names = course_names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Float> getResults() {
        return results;
    }

    public void setResults(List<Float> results) {
        this.results = results;
    }
}
