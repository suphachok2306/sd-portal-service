package com.pcc.portalservice.response;

import lombok.Data;

@Data
public class ApiResponse {
    private String responseMessage;
    private ResponseData responseData;
}
