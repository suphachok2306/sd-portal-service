package com.pcc.portalservice.requests;

import com.pcc.portalservice.model.enums.StatusApprove;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

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
