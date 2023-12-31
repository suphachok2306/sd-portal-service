package com.pcc.portalservice.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
  Admin,
  Approver,
  VicePresident,
  Personnel,
  User,
  President,
  Manager;

  public String getAuthority() {
    return name();
  }
}
