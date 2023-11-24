package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditCourseRequest {

  private String courseName;
  private String startDate;
  private String endDate;
  private String hours;
  private String time;
  private String note;
  private float price;
  private String priceProject;
  private String place;
  private String institute;
  private String type;
}
