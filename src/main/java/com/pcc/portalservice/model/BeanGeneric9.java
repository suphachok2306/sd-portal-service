package com.pcc.portalservice.model;

import java.util.List;
import java.util.Objects;

public class BeanGeneric9 {

  public List<String> getCourse_names() {
    return course_names;
  }

  public void setCourse_names(List<String> course_names) {
    this.course_names = course_names;
  }

  public List<String> getResult1s() {
    return result1s;
  }

  public void setResult1s(List<String> result1s) {
    this.result1s = result1s;
  }

  public List<String> getResult2s() {
    return result2s;
  }

  public void setResult2s(List<String> result2s) {
    this.result2s = result2s;
  }

  public List<String> getResult3s() {
    return result3s;
  }

  public void setResult3s(List<String> result3s) {
    this.result3s = result3s;
  }

  public List<String> getResult4s() {
    return result4s;
  }

  public void setResult4s(List<String> result4s) {
    this.result4s = result4s;
  }

  public List<String> getResult5s() {
    return result5s;
  }

  public void setResult5s(List<String> result5s) {
    this.result5s = result5s;
  }

  public String getPositionname() {
    return positionname;
  }

  public void setPositionname(String positionname) {
    this.positionname = positionname;
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
  private List<String> result1s;
  private List<String> result2s;
  private List<String> result3s;
  private List<String> result4s;
  private List<String> result5s;
  private String positionname;
  private String name;
  private Long id;

  public BeanGeneric9() {}

  public BeanGeneric9(
    List<String> course_names,
    List<String> result1s,
    List<String> result2s,
    List<String> result3s,
    List<String> result4s,
    List<String> result5s,
    String positionname,
    String name,
    Long id
  ) {
    this.course_names = course_names;
    this.result1s = result1s;
    this.result2s = result2s;
    this.result3s = result3s;
    this.result4s = result4s;
    this.result5s = result5s;
    this.positionname = positionname;
    this.name = name;
    this.id = id;
  }
}
