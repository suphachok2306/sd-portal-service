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
        String jpql = "SELECT " +
                "c.company_name, " +
                "s.sector_name, " +
                "s.sector_code, " +
                "d.dept_name, " +
                "d.dept_code, " +
                "p.id, " +
                "p.position_name " +
                "FROM " +
                "department d " +
                "JOIN " +
                "sector s ON d.sector_id = s.id " +
                "JOIN " +
                "company c ON s.company_id = c.id " +
                "JOIN " +
                "position p ON p.department_id = d.id;";
    
        List<Object[]> results = entityManager.createNativeQuery(jpql).getResultList();
    
        Map<String, Map<String, Object>> departmentMap = new HashMap<>();
    
        for (Object[] row : results) {
            String departmentKey = row[4].toString();
    
            Map<String, Object> departmentInfo = departmentMap.computeIfAbsent(departmentKey, k -> {
                Map<String, Object> deptData = new LinkedHashMap<>();
                deptData.put("company", row[0]);
                deptData.put("sectorname", row[1]);
                deptData.put("sectorcode", row[2]);
                deptData.put("deptname", row[3]);
                deptData.put("deptcode", row[4]);
                deptData.put("positions", new ArrayList<Map<String, String>>());
                return deptData;
            });
    
            Map<String, String> positionMap = new LinkedHashMap<>();
            positionMap.put("id", row[5].toString());
            positionMap.put("name", row[6].toString());
    
            List<Map<String, String>> positions = (List<Map<String, String>>) departmentInfo.get("positions");
            positions.add(positionMap);
        }
    
        return new ArrayList<>(departmentMap.values());
    }
    
    
    
    
    
    
    
    
    public Optional<Department> findByDeptCodeAndDeptName(String DeptCode, String DeptName){
        return departmentRepository.findByDeptCodeAndDeptName(
                DeptCode,DeptName);
    }


}
