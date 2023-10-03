package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTrainingSectionTwoRequest {
    Long evaluatorId;
    Long resultId;
}
