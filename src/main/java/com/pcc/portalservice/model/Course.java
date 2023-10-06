package com.pcc.portalservice.model;

import java.sql.Timestamp;
import java.util.Date;

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
    private long id;
    private String courseName;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    private String time;
    private String note;
    private String objective;
    private float price;
    private float priceProject;
    private String place;
    private String institute;
}

