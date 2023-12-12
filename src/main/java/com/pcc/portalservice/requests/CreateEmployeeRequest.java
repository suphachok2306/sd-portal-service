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
  List<Long> deptID;
  List<Long> companyID;
  List<Long> sectorID;
  List<String> roles;
}
