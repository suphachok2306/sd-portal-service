package com.pcc.portalservice.model;

import java.util.List;
import java.util.Objects;

public class BeanGeneric9 {

  private List<String> course_names;
  private List<String> result1s;
  private List<String> result2s;
  private List<String> result3s;
  private List<String> result4s;
  private List<String> result5s;
  private List<String> result6s;
  private List<String> result7s;
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
    List<String> result6s,
    List<String> result7s,
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
    this.result6s = result6s;
    this.result7s = result7s;
    this.positionname = positionname;
    this.name = name;
    this.id = id;
  }

  public List<String> getCourse_names() {
    return this.course_names;
  }

  public void setCourse_names(List<String> course_names) {
    this.course_names = course_names;
  }

  public List<String> getResult1s() {
    return this.result1s;
  }

  public void setResult1s(List<String> result1s) {
    this.result1s = result1s;
  }

  public List<String> getResult2s() {
    return this.result2s;
  }

  public void setResult2s(List<String> result2s) {
    this.result2s = result2s;
  }

  public List<String> getResult3s() {
    return this.result3s;
  }

  public void setResult3s(List<String> result3s) {
    this.result3s = result3s;
  }

  public List<String> getResult4s() {
    return this.result4s;
  }

  public void setResult4s(List<String> result4s) {
    this.result4s = result4s;
  }

  public List<String> getResult5s() {
    return this.result5s;
  }

  public void setResult5s(List<String> result5s) {
    this.result5s = result5s;
  }

  public List<String> getResult6s() {
    return this.result6s;
  }

  public void setResult6s(List<String> result6s) {
    this.result6s = result6s;
  }

  public List<String> getResult7s() {
    return this.result7s;
  }

  public void setResult7s(List<String> result7s) {
    this.result7s = result7s;
  }

  public String getPositionname() {
    return this.positionname;
  }

  public void setPositionname(String positionname) {
    this.positionname = positionname;
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

  public BeanGeneric9 course_names(List<String> course_names) {
    setCourse_names(course_names);
    return this;
  }

  public BeanGeneric9 result1s(List<String> result1s) {
    setResult1s(result1s);
    return this;
  }

  public BeanGeneric9 result2s(List<String> result2s) {
    setResult2s(result2s);
    return this;
  }

  public BeanGeneric9 result3s(List<String> result3s) {
    setResult3s(result3s);
    return this;
  }

  public BeanGeneric9 result4s(List<String> result4s) {
    setResult4s(result4s);
    return this;
  }

  public BeanGeneric9 result5s(List<String> result5s) {
    setResult5s(result5s);
    return this;
  }

  public BeanGeneric9 result6s(List<String> result6s) {
    setResult6s(result6s);
    return this;
  }

  public BeanGeneric9 result7s(List<String> result7s) {
    setResult7s(result7s);
    return this;
  }

  public BeanGeneric9 positionname(String positionname) {
    setPositionname(positionname);
    return this;
  }

  public BeanGeneric9 name(String name) {
    setName(name);
    return this;
  }

  public BeanGeneric9 id(Long id) {
    setId(id);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof BeanGeneric9)) {
      return false;
    }
    BeanGeneric9 beanGeneric9 = (BeanGeneric9) o;
    return (
      Objects.equals(course_names, beanGeneric9.course_names) &&
      Objects.equals(result1s, beanGeneric9.result1s) &&
      Objects.equals(result2s, beanGeneric9.result2s) &&
      Objects.equals(result3s, beanGeneric9.result3s) &&
      Objects.equals(result4s, beanGeneric9.result4s) &&
      Objects.equals(result5s, beanGeneric9.result5s) &&
      Objects.equals(result6s, beanGeneric9.result6s) &&
      Objects.equals(result7s, beanGeneric9.result7s) &&
      Objects.equals(positionname, beanGeneric9.positionname) &&
      Objects.equals(name, beanGeneric9.name) &&
      Objects.equals(id, beanGeneric9.id)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      course_names,
      result1s,
      result2s,
      result3s,
      result4s,
      result5s,
      result6s,
      result7s,
      positionname,
      name,
      id
    );
  }

  @Override
  public String toString() {
    return (
      "{" +
      " course_names='" +
      getCourse_names() +
      "'" +
      ", result1s='" +
      getResult1s() +
      "'" +
      ", result2s='" +
      getResult2s() +
      "'" +
      ", result3s='" +
      getResult3s() +
      "'" +
      ", result4s='" +
      getResult4s() +
      "'" +
      ", result5s='" +
      getResult5s() +
      "'" +
      ", result6s='" +
      getResult6s() +
      "'" +
      ", result7s='" +
      getResult7s() +
      "'" +
      ", positionname='" +
      getPositionname() +
      "'" +
      ", name='" +
      getName() +
      "'" +
      ", id='" +
      getId() +
      "'" +
      "}"
    );
  }
}
