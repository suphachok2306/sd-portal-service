package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final SectorRepository sectorRepository;
    private final EntityManager entityManager;

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

    public List<Map<String, Object>> findAllJoinDepartments() {
    String jpql = "SELECT c.company_name, s.sector_name, s.sector_code, d.dept_name, d.dept_code,p.id,p.position_name  FROM department d JOIN sector s ON d.sector_id = s.id JOIN company c ON s.company_id = c.id join position p on p.department_id = d.id ";

    List<Object[]> results = entityManager.createNativeQuery(jpql).getResultList();
    
    List<Map<String, Object>> resultList = new ArrayList<>();
    
    for (Object[] row : results) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("company", row[0]);
        resultMap.put("sectorname", row[1]);
        resultMap.put("sectorcode", row[2]);
        resultMap.put("deptname", row[3]);
        resultMap.put("deptcode", row[4]);

        List<Map<String, String>> positionList = new ArrayList<>();
        Map<String, String> positionMap = new LinkedHashMap<>();
        
        positionMap.put("id", row[5].toString());
        positionMap.put("name", row[6].toString());
        positionList.add(positionMap);

        resultMap.put("position", positionList);

        resultList.add(resultMap);
    }

    return resultList;
}
    public Optional<Department> findByDeptCodeAndDeptName(String DeptCode, String DeptName){
        return departmentRepository.findByDeptCodeAndDeptName(
                DeptCode,DeptName);
    }


}
