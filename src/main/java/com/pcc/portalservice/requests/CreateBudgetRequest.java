package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateBudgetRequest {

  String year;
  Long department_Id;
  Long company_Id;
  float budgetTraining;
  float budgetCer;
}
