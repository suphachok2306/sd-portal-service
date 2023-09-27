package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Position;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import com.pcc.portalservice.requests.CreatePositionRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class DepartmentController {
    private final DepartmentService departmentService;


    @PostMapping("/createDepartment")
    public ResponseEntity<ApiResponse> createPosition(@RequestBody CreateDepartmentRequest createDepartmentRequest) {
        Department department = departmentService.create(createDepartmentRequest);
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(departmentService.isDeptNull(department)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            data.setResult(department);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createDepartment").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping("/findAllDepartments")
    public List<Department> getAllDepartments() {
        return departmentService.findAll();
    }

    @GetMapping("/findAllJoinDepartments")
    public List<Map<String, Object>> getAllJoinDepartments() {
        return departmentService.findAllJoinDepartments();
    }

}
