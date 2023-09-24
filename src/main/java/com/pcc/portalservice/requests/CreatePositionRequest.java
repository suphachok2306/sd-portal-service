package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreatePositionRequest {
    String positionName;
    Long departmentId;
}
