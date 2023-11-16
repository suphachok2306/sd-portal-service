package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EditEmployeeRequest {
    String empCode;
    String title;
    String firstname;
    String lastname;
    String positionName;
    String email;
    String sectorName;
    String sectorCode;
    String companyName;

    private List<Long> deptID;
}
