package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditEmployeeRequest {
    String empCode;
    String firstname;
    String lastname;
    String positionName;
    String email;
    String sectorName;
    String sectorCode;
    String deptName;
    String deptCode;
    String companyName;
}
