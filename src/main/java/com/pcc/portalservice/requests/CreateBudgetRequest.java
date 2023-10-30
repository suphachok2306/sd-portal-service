package com.pcc.portalservice.requests;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CreateBudgetRequest {
    String year;
    String className;
    String remark;
    int numberOfPerson;
    float fee;
    float airAcc;
    long departmentId;
    long companyId;
    String type;

}
