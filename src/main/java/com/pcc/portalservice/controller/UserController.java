package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.EditEmployeeRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class UserController {

    private final UserService userService;


    @GetMapping("/findUserById")
    public ResponseEntity<ApiResponse> findUserById(@RequestParam Long userId) {
        User user = userService.findById(userId);
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



    @PostMapping("/createUser")
    public ResponseEntity<User> create(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.create(createUserRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/createEmployee")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody CreateEmployeeRequest createEmployeeRequest) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(userService.isEmpNull(createEmployeeRequest)){
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            User user = userService.createEmployee(createEmployeeRequest);
            data.setResult(user);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createEmployee").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e){
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }

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
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/editEmployee").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }

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

    @PostMapping("/addRoleToUser")
    public ResponseEntity<ApiResponse> addRoleToUser(@RequestParam Long userId, @RequestParam String roleName) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        Roles roleEnum = Roles.valueOf(roleName);
        if(userService.hasRole(userId, roleName)){
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            userService.addRoleToUser(userId, roleEnum);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @DeleteMapping("deleteById")
    public ResponseEntity<ApiResponse> delete(@RequestParam Long id) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        User user = userService.deleteData(id);
        if (user != null) {
            data.setResult(user);
            response.setResponseMessage("ลบเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } else {
            response.setResponseMessage("ไม่สามารถทำรายการได้");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/findAllEmployee")
    public List<User> getAllEmployee() {
        return userService.findAllEmployee();

    }

    @GetMapping("/findAllPersonnel")
    public List<User> getAllPersonnel() {
        return userService.findAllPersonnel();
    }

    @GetMapping("/findAllVicePresident")
    public List<User> getAllVicePresident() {
        return userService.findAllVicePresident();
    }

    @GetMapping("/findAllApprover")
    public List<User> getAllApprover() {
        return userService.findAllApprover();
    }

    @GetMapping("/findAllAdmin")
    public List<User> getAllAdmin() {
        return userService.findAllAdmin();
    }


}
