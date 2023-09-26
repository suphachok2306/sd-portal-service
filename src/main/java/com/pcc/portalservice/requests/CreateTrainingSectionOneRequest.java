package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
@Builder
@Data
public class CreateTrainingSectionOneRequest {
    String sectorName;
    String deptName;
    String deptCode;
    Timestamp dateSave;
    String topic;
    String objt;
    Timestamp startDate;
    Timestamp endDate;
    String time;
    String note;
    float price;
    float priceProject;
    String place;
    String institute;
}

