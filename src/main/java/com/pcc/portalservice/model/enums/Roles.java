package com.pcc.portalservice.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    ADMIN,
    APPROVER,
    USER;

    public String getAuthority() {
        return name();
    }

}
