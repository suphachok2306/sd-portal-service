package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.repository.UserRepository;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class DepartmentController {

  private final DepartmentService departmentService;
  private final UserRepository userRepository;

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
    @GetMapping("/findDepartmentsByUser")
    public List<Object> findDepartmentsByUser(
            @RequestParam("userId") Long userId) {
    return Collections.singletonList(departmentService.findDepartmentsByUser(userRepository.findById(userId)));
}

  @GetMapping("/findAllJoinDepartmentssector")
  public List<Map<String, Object>> getAllDepartmentssector() {
    List<Map<String, Object>> departments = departmentService.findAllJoinDepartmentssector();

    // Restructure the response
    List<Map<String, Object>> restructuredDepartments = new ArrayList<>();
    for (Map<String, Object> department : departments) {
      Map<String, Object> companySector = new HashMap<>();
      companySector.put("company", department.get("company"));
      companySector.put("sectorName", department.get("sectorName"));
      companySector.put("sectorCode", department.get("sectorCode"));

      Map<String, Object> departmentInfo = new HashMap<>();
      departmentInfo.put("id", department.get("id"));
      departmentInfo.put("deptCode", department.get("deptCode"));
      departmentInfo.put("deptName", department.get("deptName"));

      companySector.put("department", departmentInfo);
      restructuredDepartments.add(companySector);
    }

    return restructuredDepartments;
  }


//  @GetMapping("/findAllJoinDepartmentssector")
//  public List<Map<String, Object>> getAllDepartmentssector() {
//    List<Map<String, Object>> departments = departmentService.findAllJoinDepartmentssector();
//
//    // Group departments by company
//    Map<String, List<Map<String, Object>>> companyMap = new HashMap<>();
//    for (Map<String, Object> department : departments) {
//      String company = (String) department.get("company");
//
//      // Create or get the list for the current company
//      companyMap.computeIfAbsent(company, k -> new ArrayList<>()).add(department);
//    }
//
//    // Restructure the response
//    List<Map<String, Object>> restructuredDepartments = new ArrayList<>();
//    for (List<Map<String, Object>> companyDepartments : companyMap.values()) {
//      Map<String, Object> firstDepartment = companyDepartments.get(0);
//
//      Map<String, Object> result = new HashMap<>();
//      result.put("sectorcode", firstDepartment.get("sectorcode"));
//      result.put("sectorname", firstDepartment.get("sectorname"));
//      result.put("company", firstDepartment.get("company"));
//
//      List<Map<String, Object>> departmentList = new ArrayList<>();
//      for (Map<String, Object> department : companyDepartments) {
//        Map<String, Object> departmentInfo = new HashMap<>();
//        departmentInfo.put("id", department.get("id"));
//        departmentInfo.put("deptcode", department.get("deptcode"));
//        departmentInfo.put("deptname", department.get("deptname"));
//        departmentList.add(departmentInfo);
//      }
//
//      result.put("department", departmentList);
//      restructuredDepartments.add(result);
//    }
//
//    return restructuredDepartments;
//  }
}
