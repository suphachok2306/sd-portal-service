package com.pcc.portalservice.model;

import java.util.List;
import java.util.Objects;

public class BeanHistroy {

  public List<String> getCourse_names() {
    return course_names;
  }

  public void setCourse_names(List<String> course_names) {
    this.course_names = course_names;
  }

  public List<String> getCourse_hours() {
    return course_hours;
  }

  public void setCourse_hours(List<String> course_hours) {
    this.course_hours = course_hours;
  }

  public List<String> getCourse_places() {
    return course_places;
  }

  public void setCourse_places(List<String> course_places) {
    this.course_places = course_places;
  }

  public List<Float> getCourse_prices() {
    return course_prices;
  }

  public void setCourse_prices(List<Float> course_prices) {
    this.course_prices = course_prices;
  }

  public List<String> getPriceProjects() {
    return priceProjects;
  }

  public void setPriceProjects(List<String> priceProjects) {
    this.priceProjects = priceProjects;
  }

  public List<String> getDates() {
    return dates;
  }

  public void setDates(List<String> dates) {
    this.dates = dates;
  }

  public Float getSums() {
    return sums;
  }

  public void setSums(Float sums) {
    this.sums = sums;
  }

  public Float getSumall() {
    return sumall;
  }

  public void setSumall(Float sumall) {
    this.sumall = sumall;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }



  private List<String> course_names;
  private List<String> course_hours;
  private List<String> course_places;
  private List<Float> course_prices;
  private List<String> priceProjects;
  private List<String> dates;
  private Float sums;
  private Float sumall;
  private String name;
  private Long id;


  public BeanHistroy(List<String> course_names, List<String> course_hours, List<String> course_places, List<Float> course_prices, List<String> priceProjects, List<String> dates, Float sums, Float sumall, String name, Long id) {
    this.course_names = course_names;
    this.course_hours = course_hours;
    this.course_places = course_places;
    this.course_prices = course_prices;
    this.priceProjects = priceProjects;
    this.dates = dates;
    this.sums = sums;
    this.sumall = sumall;
    this.name = name;
    this.id = id;
  }

}
