package com.pcc.portalservice.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Course")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long course_id;
    private String course_name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String time;
    private String note;
    private float price;
    private float priceProject;
    private String place;
    private String institute;
}

