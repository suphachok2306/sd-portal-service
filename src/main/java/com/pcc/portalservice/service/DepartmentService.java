package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department create(CreateDepartmentRequest createDepartmentRequest) {
        Department department = Department.builder()
                .deptName(createDepartmentRequest.getDeptName())
                .deptCode(createDepartmentRequest.getDeptCode())
                .build();
        return departmentRepository.save(department);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
