package com.pcc.portalservice.model;

import java.util.List;
import java.util.Objects;

public class BeanHistroy {
    private List<String> course_names;
    private List<String> course_places;
    private List<Float> course_prices;
    private List<String> priceProjects;
    private List<String> dates;
    private Float sums; 
    private Float sumall;
    private String name;
    private Long id;

    public BeanHistroy() {
    }

    public BeanHistroy(List<String> course_names, List<String> course_places, List<Float> course_prices, List<String> priceProjects, List<String> dates, Float sums, Float sumall, String name, Long id) {
        this.course_names = course_names;
        this.course_places = course_places;
        this.course_prices = course_prices;
        this.priceProjects = priceProjects;
        this.dates = dates;
        this.sums = sums;
        this.sumall = sumall;
        this.name = name;
        this.id = id;
    }

    public List<String> getCourse_names() {
        return this.course_names;
    }

    public void setCourse_names(List<String> course_names) {
        this.course_names = course_names;
    }

    public List<String> getCourse_places() {
        return this.course_places;
    }

    public void setCourse_places(List<String> course_places) {
        this.course_places = course_places;
    }

    public List<Float> getCourse_prices() {
        return this.course_prices;
    }

    public void setCourse_prices(List<Float> course_prices) {
        this.course_prices = course_prices;
    }

    public List<String> getPriceProjects() {
        return this.priceProjects;
    }

    public void setPriceProjects(List<String> priceProjects) {
        this.priceProjects = priceProjects;
    }

    public List<String> getDates() {
        return this.dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Float getSums() {
        return this.sums;
    }

    public void setSums(Float sums) {
        this.sums = sums;
    }

    public Float getSumall() {
        return this.sumall;
    }

    public void setSumall(Float sumall) {
        this.sumall = sumall;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BeanHistroy course_names(List<String> course_names) {
        setCourse_names(course_names);
        return this;
    }

    public BeanHistroy course_places(List<String> course_places) {
        setCourse_places(course_places);
        return this;
    }

    public BeanHistroy course_prices(List<Float> course_prices) {
        setCourse_prices(course_prices);
        return this;
    }

    public BeanHistroy priceProjects(List<String> priceProjects) {
        setPriceProjects(priceProjects);
        return this;
    }

    public BeanHistroy dates(List<String> dates) {
        setDates(dates);
        return this;
    }

    public BeanHistroy sums(Float sums) {
        setSums(sums);
        return this;
    }

    public BeanHistroy sumall(Float sumall) {
        setSumall(sumall);
        return this;
    }

    public BeanHistroy name(String name) {
        setName(name);
        return this;
    }

    public BeanHistroy id(Long id) {
        setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BeanHistroy)) {
            return false;
        }
        BeanHistroy beanHistroy = (BeanHistroy) o;
        return Objects.equals(course_names, beanHistroy.course_names) && Objects.equals(course_places, beanHistroy.course_places) && Objects.equals(course_prices, beanHistroy.course_prices) && Objects.equals(priceProjects, beanHistroy.priceProjects) && Objects.equals(dates, beanHistroy.dates) && Objects.equals(sums, beanHistroy.sums) && Objects.equals(sumall, beanHistroy.sumall) && Objects.equals(name, beanHistroy.name) && Objects.equals(id, beanHistroy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course_names, course_places, course_prices, priceProjects, dates, sums, sumall, name, id);
    }

    @Override
    public String toString() {
        return "{" +
            " course_names='" + getCourse_names() + "'" +
            ", course_places='" + getCourse_places() + "'" +
            ", course_prices='" + getCourse_prices() + "'" +
            ", priceProjects='" + getPriceProjects() + "'" +
            ", dates='" + getDates() + "'" +
            ", sums='" + getSums() + "'" +
            ", sumall='" + getSumall() + "'" +
            ", name='" + getName() + "'" +
            ", id='" + getId() + "'" +
            "}";
    }
   
}
