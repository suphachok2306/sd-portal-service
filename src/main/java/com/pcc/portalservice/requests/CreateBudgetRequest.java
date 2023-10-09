package com.pcc.portalservice.requests;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Sector;

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
