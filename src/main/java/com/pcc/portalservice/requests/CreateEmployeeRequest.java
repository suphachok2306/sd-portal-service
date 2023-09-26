package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateEmployeeRequest {
    String companyName;
    String sectorName;
    String sectorCode;
    String deptName;
    String deptCode;
    String empCode;
    String firstname;
    String lastname;
    String positionName;
    String email;
    private List<String> roles;
}
