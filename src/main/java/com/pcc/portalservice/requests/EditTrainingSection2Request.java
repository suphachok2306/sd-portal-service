package com.pcc.portalservice.requests;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EditTrainingSection2Request {

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
  Date evaluationDate;
}
