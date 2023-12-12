package com.pcc.portalservice.requests;

import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.StatusApprove;
import java.util.Date;
import java.util.List;

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
  Long approve2_id;
  Long approve3_id;
  Long approve4_id;
  float budget;
  List<Long> fileID;
}
