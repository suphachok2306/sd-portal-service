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

   /**
   * @สร้างCourse
   */
  public Department create(CreateDepartmentRequest createDepartmentRequest) {
    Sector sectorId = sectorRepository
      .findById(createDepartmentRequest.getSectorId())
      .orElseThrow(() ->
        new RuntimeException(
          "sectorId not found: " + createDepartmentRequest.getSectorId()
        )
      );

    Department department = Department
      .builder()
      .sector(sectorId)
      .deptName(createDepartmentRequest.getDeptName())
      .deptCode(createDepartmentRequest.getDeptCode())
      .build();
    return departmentRepository.save(department);
  }

   /**
   * @หาDepartmentทั้งหมด
   */
  public List<Department> findAll() {
    return departmentRepository.findAll();
  }

   /**
   * @หาDepartmentที่มีทำการJoinกับSectorและCompanyแล้ว
   */
  public List<Map<String, Object>> findAllJoinDepartments() {
    String jpql =
    "SELECT " +
    "c.company_name AS company_name, " +
    "s.sector_name AS sector_name, " +
    "s.sector_code AS sector_code, " +
    "d.id AS department_id, " +
    "d.dept_name AS department_name, " +
    "d.dept_code AS department_code, " +
    "p.id AS position_id, " +
    "p.position_name AS position_name " +
    "FROM department d " +
    "JOIN sector s ON d.sector_id = s.id " +
    "JOIN company c ON s.company_id = c.id " +
    "JOIN position p ON p.department_id = d.id;";

    List<Object[]> results = entityManager
      .createNativeQuery(jpql)
      .getResultList();

    List<Map<String, Object>> resultList = new ArrayList<>();

    Map<String, Map<String, Object>> companyMap = new HashMap<>();

    for (Object[] row : results) {
      String company = (String) row[0];
      String sectorname = (String) row[1];
      String sectorcode = (String) row[2];
      Long deptid = ((Number) row[3]).longValue();
      String deptname = (String) row[4];
      String deptcode = (String) row[5];
      Long positionId = ((Number) row[6]).longValue();
      String positionName = (String) row[7];

      Map<String, Object> resultMap;
      Map<String, Object> positionMap = new LinkedHashMap<>();

      if (companyMap.containsKey(company)) {
        resultMap = companyMap.get(company);
        List<Map<String, Object>> departments = (List<Map<String, Object>>) resultMap.get(
          "departments"
        );

        boolean departmentExists = false;
        for (Map<String, Object> department : departments) {
          if (department.get("deptname").equals(deptname)) {
            departmentExists = true;
            List<Map<String, Object>> positions = (List<Map<String, Object>>) department.get(
              "position"
            );
            positionMap.put("id", positionId.toString());
            positionMap.put("name", positionName);
            positions.add(positionMap);
            break;
          }
        }

        if (!departmentExists) {
          Map<String, Object> departmentMap = new LinkedHashMap<>();
          departmentMap.put("deptid", deptid);
          departmentMap.put("deptname", deptname);
          departmentMap.put("deptcode", deptcode);

          positionMap.put("id", positionId.toString());
          positionMap.put("name", positionName);

          List<Map<String, Object>> positions = new ArrayList<>();
          positions.add(positionMap);
          departmentMap.put("position", positions);

          departments.add(departmentMap);
        }
      } else {
        resultMap = new LinkedHashMap<>();
        resultMap.put("company", company);
        resultMap.put("sectorname", sectorname);
        resultMap.put("sectorcode", sectorcode);

        List<Map<String, Object>> departments = new ArrayList<>();

        Map<String, Object> departmentMap = new LinkedHashMap<>();
        departmentMap.put("deptid", deptid);
        departmentMap.put("deptname", deptname);
        departmentMap.put("deptcode", deptcode);

        positionMap.put("id", positionId.toString());
        positionMap.put("name", positionName);

        List<Map<String, Object>> positions = new ArrayList<>();
        positions.add(positionMap);
        departmentMap.put("position", positions);

        departments.add(departmentMap);

        resultMap.put("departments", departments);
        companyMap.put(company, resultMap);
        resultList.add(resultMap);
      }
    }

    return resultList;
  }

  public List<Map<String, Object>> findAllJoinDepartmentssector() {
    String jpql =
            "SELECT " +
                    "c.company_name AS company_name, " +
                    "s.sector_name AS sector_name, " +
                    "s.sector_code AS sector_code, " +
                    "d.id AS department_id, " +
                    "d.dept_name AS department_name, " +
                    "d.dept_code AS department_code " +
                    "FROM department d " +
                    "JOIN sector s ON d.sector_id = s.id " +
                    "JOIN company c ON s.company_id = c.id";

    List<Object[]> results = entityManager
            .createNativeQuery(jpql)
            .getResultList();

    List<Map<String, Object>> resultList = new ArrayList<>();

    for (Object[] row : results) {
      Map<String, Object> resultMap = new HashMap<>();

      resultMap.put("company", (String) row[0]);
      resultMap.put("sectorname", (String) row[1]);
      resultMap.put("sectorcode", (String) row[2]);
      resultMap.put("id", ((Number) row[3]).longValue());
      resultMap.put("deptname", (String) row[4]);
      resultMap.put("deptcode", (String) row[5]);

      resultList.add(resultMap);
    }

    return resultList;
  }

  /**
   * @หาDepartmentด้วยDeptNameและDeptCode
   */
  public Optional<Department> findByDeptCodeAndDeptName(
    String DeptCode,
    String DeptName
  ) {
    return departmentRepository.findByDeptCodeAndDeptName(DeptCode, DeptName);
  }

  /**
   * @เช็คNullของDepartment
   */
  public boolean isDeptNull(CreateDepartmentRequest request) {
    return (
      request == null ||
      request.getDeptName() == null ||
      request.getDeptName().isEmpty() ||
      request.getDeptCode() == null ||
      request.getDeptCode().isEmpty()
    );
  }
}
