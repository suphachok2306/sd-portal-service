package com.pcc.portalservice.requests;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCourseRequest {
    private Long courseId;
    private String courseName;
    private String startDate;
    private String endDate;
    private String time;
    private String note;
    private float price;
    private float priceProject;
    private String place;
    private String institute;
}
