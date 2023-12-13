package com.pcc.portalservice.requests;

import java.util.List;
import lombok.Builder;
import lombok.Data;

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
