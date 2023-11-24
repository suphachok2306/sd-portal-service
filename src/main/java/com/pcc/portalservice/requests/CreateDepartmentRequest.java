package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDepartmentRequest {

  String deptName;
  String deptCode;
  Long sectorId;
}
