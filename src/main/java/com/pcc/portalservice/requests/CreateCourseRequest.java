package com.pcc.portalservice.requests;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class CreateCourseRequest {
    private Long course_id;
    private String course_name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String time;
    private String note;
    private String objective;
    private float price;
    private float priceProject;
    private String place;
    private String institute;
}
