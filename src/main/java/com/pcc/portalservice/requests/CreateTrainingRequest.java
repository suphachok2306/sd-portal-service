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
    String action;
    String actionDate;
    int day;
    Long courseId;
    Long approve1_id;
    Float budget;
}

