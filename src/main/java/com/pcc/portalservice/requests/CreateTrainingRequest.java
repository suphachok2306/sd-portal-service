package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class CreateTrainingRequest {

  Long userId;
  Date dateSave;
  String action;
  String actionDate;
  int day;
  Long courseId;
  Float budget;
  Long approve1_id;
  Long approve2_id;
  Long approve3_id;
  Long approve4_id;
  List<Long> fileID;


}
