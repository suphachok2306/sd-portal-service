package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Position;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import com.pcc.portalservice.requests.CreatePositionRequest;
import com.pcc.portalservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/creatDepartment")
    public ResponseEntity<Department> createPosition(@RequestBody CreateDepartmentRequest createDepartmentRequest) {
        Department createdDepartment = departmentService.create(createDepartmentRequest);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @GetMapping("/findAllDepartments")
    public List<Department> getAllDepartments() {
        return departmentService.findAll();
    }
}
