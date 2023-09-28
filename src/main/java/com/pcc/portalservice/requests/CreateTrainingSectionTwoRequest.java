package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTrainingSectionTwoRequest {
    String evaluatorName;
    String positionName;
    String deptName;
    String sectorName;
    String result1;
    String result2;
    String result3;
    String result4;
    String result5;
    String result6;
    String result7;
    String comment;
    String result;
    String cause;
    String plan;
}
