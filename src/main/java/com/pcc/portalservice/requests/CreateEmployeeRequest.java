package com.pcc.portalservice.requests;

import java.util.List;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Sector;
import lombok.Builder;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Builder
public class CreateEmployeeRequest {

  String empCode;
  String title;
  String firstname;
  String lastname;
  String positionName;
  String email;
  Long dept_actual;
  Long sector_actual;
  List<Long> deptID;
  List<Long> companyID;
  List<Long> sectorID;
  List<String> roles;
}
