package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EditTrainingSection1PersonRequest {
    String action;
    String actionDate;
}
