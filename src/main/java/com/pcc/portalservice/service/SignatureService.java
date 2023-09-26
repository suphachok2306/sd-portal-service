package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SignatureService {
    private final DepartmentRepository departmentRepository;
    private final SectorRepository sectorRepository;

    public boolean isDeptNull(Department department){
        if(department == null || department.getDeptName() == null || department.getDeptName().isEmpty()
                || department.getDeptCode() == null || department.getDeptCode().isEmpty()){
            return true;
        }
        return false;
    }

    public Department create(CreateDepartmentRequest createDepartmentRequest) {

        Sector sectorId = sectorRepository.findById(createDepartmentRequest.getSectorId())
                .orElseThrow(() -> new RuntimeException("sectorId not found: " + createDepartmentRequest.getSectorId()));

        Department department = Department.builder()
                .sector(sectorId)
                .deptName(createDepartmentRequest.getDeptName())
                .deptCode(createDepartmentRequest.getDeptCode())
                .build();
        return departmentRepository.save(department);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
