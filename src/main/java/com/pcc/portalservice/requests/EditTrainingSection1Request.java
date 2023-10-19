package com.pcc.portalservice.requests;

import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.StatusApprove;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class EditTrainingSection1Request {
    
    //////////////////////////////
    Long userId;

    Date dateSave;
    int day;
    Long courseId;
    //Long approve1_id;
    User approve1_id;
    float budget;
}

