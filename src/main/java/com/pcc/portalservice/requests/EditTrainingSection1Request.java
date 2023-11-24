package com.pcc.portalservice.requests;

import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.StatusApprove;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EditTrainingSection1Request {

  Long userId;
  Date dateSave;
  int day;
  String action;
  String actionDate;
  Long courseId;
  Long approve1_id;
  float budget;
}
