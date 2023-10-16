package com.pcc.portalservice.requests;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CreateBudgetRequest {
    long budgetId;
    String year;
    String className;
    String remark;
    int numberOfPerson;
    float fee;
    float airAcc;
    long sectorId;
    long companyId;

}
