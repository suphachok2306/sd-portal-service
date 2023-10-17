package com.pcc.portalservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusUser;
import com.pcc.portalservice.repository.UserRepository;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.EditEmployeeRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.UserService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class UserController {

  private final UserService userService;
  private final UserRepository userRepository;

  @GetMapping("/findUserById")
  public ResponseEntity<ApiResponse> findUserById(@RequestParam Long userId) {
    Optional<User> user = userRepository.findById(userId);
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (user != null) {
      data.setResult(user);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      return ResponseEntity.ok().body(response);
    } else {
      response.setResponseMessage("ไม่สามารถทำรายการได้");
      return ResponseEntity.badRequest().body(response);
    }
  }

  //สร้าง User
  @PostMapping("/createUser")
  public ResponseEntity<ApiResponse> create(
    @RequestBody CreateUserRequest createUserRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (userService.isUserNull(createUserRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      User user = userService.create(createUserRequest);
      data.setResult(user);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //สร้างพนักงาน,Employee
  @PostMapping("/createEmployee")
  public ResponseEntity<ApiResponse> createEmployee(
    @RequestBody CreateEmployeeRequest createEmployeeRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (userService.isEmpNull(createEmployeeRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      User user = userService.createEmployee(createEmployeeRequest);
      data.setResult(user);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/createEmployee")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //แก้ไขพนักงาน , edit Employee
  @PutMapping("/editEmployee")
  public ResponseEntity<ApiResponse> editEmployee(
    @RequestParam Long userId,
    @RequestBody EditEmployeeRequest editEmployeeRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (userService.isEditEmpNull(editEmployeeRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      User user = userService.editUser(userId, editEmployeeRequest);
      data.setResult(user);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/editEmployee")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //สร้างยศ , สร้าง Role
  @PostMapping("/createRole")
  public ResponseEntity<ApiResponse> createRole(@RequestBody String roleName) {
    Roles roleEnum = Roles.valueOf(roleName);
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    try {
      Role role = userService.createRole(roleEnum);
      if (role == null) {
        response.setResponseMessage("Role นี้มีอยู่ในระบบอยู่แล้ว " + roleName);
        return ResponseEntity.badRequest().body(response);
      }
      data.setResult(roleEnum);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      return ResponseEntity.ok().body(response);
    } catch (IllegalArgumentException e) {
      response.setResponseMessage("Invalid role name: " + roleName);
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //เพิ่ม Role ให้ User
  @PostMapping("/addRoleToUser")
  public ResponseEntity<ApiResponse> addRoleToUser(
    @RequestParam Long userId,
    @RequestParam String roleName
  ) {
    ApiResponse response = new ApiResponse();
    Roles roleEnum = Roles.valueOf(roleName);
    if (userService.hasRole(userId, roleName)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      userService.addRoleToUser(userId, roleEnum);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //ลบ User ด้วย Id
  @DeleteMapping("deleteById")
  public ResponseEntity<ApiResponse> delete(@RequestParam Long id) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    String user = userService.deleteData(id);
    if (user != null) {
      data.setResult(user);
      response.setResponseMessage("ลบข้อมูลเรียบร้อย");
      response.setResponseData(data);
      return ResponseEntity.ok().body(response);
    } else {
      response.setResponseMessage("ไม่พบข้อมูลที่ตรงกับ ID ที่ระบุ");
      return ResponseEntity.badRequest().body(response);
    }
  }

  //หา User ด้วย empCode,name,position,email,deptName,deptCode,company
  @GetMapping("/searchUser")
  public Object search(@RequestParam(required = false) String empCode,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String position,
                       @RequestParam(required = false) String email,
                       //@RequestParam(required = false) String department,
                       @RequestParam(required = false) String deptName,
                       @RequestParam(required = false) String deptCode,
                       @RequestParam(required = false) String company
                       ) throws JsonProcessingException {
    return userService.searchUser(empCode,name,position,email,deptName,deptCode,company);
  }

  //หาพนักงานทั้งหมด , หา Employee ทั้งหมด 
  @GetMapping("/findAllEmployee")
  public List<User> getAllEmployee() {
    return userService.findAllEmployee();
  }

  //หา Personnel ทั้งหมด
  @GetMapping("/findAllPersonnel")
  public List<User> getAllPersonnel() {
    return userService.findAllPersonnel();
  }

  //หา VicePresident ทั้งหมด
  @GetMapping("/findAllVicePresident")
  public List<User> getAllVicePresident() {
    return userService.findAllVicePresident();
  }

  //หา Approver ทั้งหมด
  @GetMapping("/findAllApprover")
  public List<User> getAllApprover() {
    return userService.findAllApprover();
  }

  //หา Admin ทั้งหมด
  @GetMapping("/findAllAdmin")
  public List<User> getAllAdmin() {
    return userService.findAllAdmin();
  }

  @GetMapping("/findAllVicePresidentAndApprover")
  public List<User> getAllVicePresidentAndApprover() {
      List<User> vicePresidents = userService.findAllVicePresident();
      List<User> approvers = userService.findAllApprover();

      List<User> allVicePresidentAndApprover = new ArrayList<>();
      allVicePresidentAndApprover.addAll(vicePresidents);
      allVicePresidentAndApprover.addAll(approvers);

      return allVicePresidentAndApprover;
  }

  //set Status ให้ User
  @PutMapping("/setStatusToUser")
    public ResponseEntity<ApiResponse> addStatusToUser(@RequestParam Long User_id, StatusUser statusUser) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            User user = userService.setStatusToUser(User_id,statusUser);
            data.setResult(user);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }

}
