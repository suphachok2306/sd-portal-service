package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCompanyRequest {
    Long companyId;
    String companyName;
}
