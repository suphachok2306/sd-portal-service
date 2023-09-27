package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTrainingSectionOneRequest {
    String companyName;
    String sectorCode;
    String sectorName;
    String deptName;
    String deptCode;
    String dateSave;
    int day;
    String causeName;
    String startDate;
    String endDate;
    String time;
    String note;
    float price;
    float priceProject;
    String place;
    String institute;
    String empCode;
    String firstname;
    String lastname;
    String position;
    String action;
    String actionDate;
}

