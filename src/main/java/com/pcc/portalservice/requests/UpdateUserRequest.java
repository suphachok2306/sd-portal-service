package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserRequest {

    String username;
    String email;
    String firstname;
    String lastname;
    String telephone;

}
