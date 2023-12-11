package com.pcc.portalservice.requests;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditEmployeeRequest {

  String empCode;
  String title;
  String firstname;
  String lastname;
  String positionName;
  String email;

  private List<Long> deptID;
  private List<Long> companiesID;
  private List<Long> sectorID;
  private List<String> roles;
}
