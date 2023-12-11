package com.pcc.portalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BeanHistroy {

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



}
