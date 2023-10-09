package com.pcc.portalservice.requests;

import com.pcc.portalservice.model.enums.StatusApprove;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class CreateTrainingRequest {
    Long userId;

    Date dateSave;
    int day;
    Long courseId;
    String action;
    String actionDate;

    Long approve1_id;
    StatusApprove status1;

    //////////////////////////////
    Long evaluatorId;
    String result1;
    String result2;
    String result3;
    String result4;
    String result5;
    String result6;
    String result7;
    String result;
    String comment;
    String cause;
    String plan;







}

