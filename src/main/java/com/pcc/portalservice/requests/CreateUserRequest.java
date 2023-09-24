package com.pcc.portalservice.requests;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateUserRequest {

    String email;
    String firstname;
    String lastname;
    String password;
    String telephone;
    private List<String> roles;

}
