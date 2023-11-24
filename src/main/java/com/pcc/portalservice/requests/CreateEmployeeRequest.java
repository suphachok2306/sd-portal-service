package com.pcc.portalservice.requests;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmployeeRequest {

  String empCode;
  String title;
  String firstname;
  String lastname;
  String positionName;
  String email;
  private List<Long> deptID;
  private List<Long> companyID;
  private List<Long> sectorID;
  private List<String> roles;
}
