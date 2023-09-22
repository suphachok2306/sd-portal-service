package com.pcc.portalservice.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    Admin,
    Approver,
    VicePersident,
    Personnel,
    User;

    public String getAuthority() {
        return name();
    }

}
