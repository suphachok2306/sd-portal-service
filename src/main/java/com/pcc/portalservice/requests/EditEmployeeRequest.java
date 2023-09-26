package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditEmployeeRequest {
    String firstname;
    String lastname;
    String positionName;
    String email;
}
