package com.pcc.portalservice.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateSectorRequest {

  String sectorName;
  String sectorCode;
  Long companyId;
}
