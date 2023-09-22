package com.pcc.portalservice.requests;

import com.pcc.portalservice.model.enums.Roles;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Builder
@Data
public class CreateUserRequest {

    String username;
    String email;
    String firstname;
    String lastname;
    String password;
    String telephone;
    private List<String> roles;
//    @Builder.Default
//    private List<String> roles = Collections.singletonList(Roles.USER.name());

}
