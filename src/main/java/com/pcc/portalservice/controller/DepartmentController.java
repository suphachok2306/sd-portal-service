package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class DepartmentController {

  private final DepartmentService departmentService;

  /**
   * @สร้างDepartment
   * @PostMapping
   */
  @PostMapping("/createDepartment")
  public ResponseEntity<ApiResponse> createPosition(
    @RequestBody CreateDepartmentRequest createDepartmentRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (departmentService.isDeptNull(createDepartmentRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Department department = departmentService.create(createDepartmentRequest);
      data.setResult(department);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/createDepartment")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  /**
   * @หาDepartmentทั้งหมด
   * @GetMapping
   */
  @GetMapping("/findAllDepartments")
  public List<Department> getAllDepartments() {
    return departmentService.findAll();
  }

  /**
   * @หาDepartmentที่มีทำการJoinกับSectorและCompanyแล้ว
   * @GetMapping
   */
  @GetMapping("/findAllJoinDepartments")
  public List<Map<String, Object>> getAllJoinDepartments() {
    return departmentService.findAllJoinDepartments();
  }

//  @GetMapping("/findAllJoinDepartmentssector")
//  public List<Map<String, Object>> getAllJoinDepartmentssector() {
//    return departmentService.findAllJoinDepartmentssector();
//  }

  @GetMapping("/findAllJoinDepartmentssector")
  public List<Map<String, Object>> getAllDepartmentssector() {
    List<Map<String, Object>> departments = departmentService.findAllJoinDepartmentssector();

    // Restructure the response
    List<Map<String, Object>> restructuredDepartments = new ArrayList<>();
    for (Map<String, Object> department : departments) {
      Map<String, Object> companySector = new HashMap<>();
      companySector.put("company", department.get("company"));
      companySector.put("sectorname", department.get("sectorname"));
      companySector.put("sectorcode", department.get("sectorcode"));

      Map<String, Object> departmentInfo = new HashMap<>();
      departmentInfo.put("id", department.get("id"));
      departmentInfo.put("deptcode", department.get("deptcode"));
      departmentInfo.put("deptname", department.get("deptname"));

      companySector.put("department", departmentInfo);
      restructuredDepartments.add(companySector);
    }

    return restructuredDepartments;
  }
}
