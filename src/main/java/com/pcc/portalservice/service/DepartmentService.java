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

  public Department create(CreateDepartmentRequest createDepartmentRequest) {
    Sector sectorId = sectorRepository
        .findById(createDepartmentRequest.getSectorId())
        .orElseThrow(() -> new RuntimeException(
            "sectorId not found: " + createDepartmentRequest.getSectorId()));
    
    Department department = Department
        .builder()
        .id(null)
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
            "c.company_name AS company_name, " +
            "d.sector_id AS sector_id, " +
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
            "JOIN position p ON p.department_id = d.id";

    List<Object[]> results = entityManager
        .createNativeQuery(jpql)
        .getResultList();

    List<Map<String, Object>> resultList = new ArrayList<>();

    Map<String, Map<String, Object>> companyMap = new HashMap<>();

    for (Object[] row : results) {
      String company = (String) row[0];
      Long sectorid = ((Number) row[1]).longValue();
      String sectorname = (String) row[2];
      String sectorcode = (String) row[3];
      Long deptid = ((Number) row[4]).longValue();
      String deptname = (String) row[5];
      String deptcode = (String) row[6];
      Long positionId = ((Number) row[7]).longValue();
      String positionName = (String) row[8];

      Map<String, Object> resultMap;
      Map<String, Object> positionMap = new LinkedHashMap<>();

      if (companyMap.containsKey(company)) {
        resultMap = companyMap.get(company);
        List<Map<String, Object>> sectors = (List<Map<String, Object>>) resultMap.get(
            "sectors");

        boolean sectorExists = false;
        for (Map<String, Object> sector : sectors) {
          if (sector.get("sectorname").equals(sectorname)) {
            sectorExists = true;
            List<Map<String, Object>> departments = (List<Map<String, Object>>) sector.get(
                "departments");

            boolean departmentExists = false;
            for (Map<String, Object> department : departments) {
              if (department.get("deptname").equals(deptname)) {
                departmentExists = true;
                List<Map<String, Object>> positions = (List<Map<String, Object>>) department.get(
                    "positions");
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
              departmentMap.put("positions", positions);

              departments.add(departmentMap);
            }

            break;
          }
        }

        if (!sectorExists) {
          Map<String, Object> sectorMap = new LinkedHashMap<>();
          sectorMap.put("sectorid", sectorid);
          sectorMap.put("sectorname", sectorname);
          sectorMap.put("sectorcode", sectorcode);

          List<Map<String, Object>> departments = new ArrayList<>();

          Map<String, Object> departmentMap = new LinkedHashMap<>();
          departmentMap.put("deptid", deptid);
          departmentMap.put("deptname", deptname);
          departmentMap.put("deptcode", deptcode);

          positionMap.put("id", positionId.toString());
          positionMap.put("name", positionName);

          List<Map<String, Object>> positions = new ArrayList<>();
          positions.add(positionMap);
          departmentMap.put("positions", positions);

          departments.add(departmentMap);

          sectorMap.put("departments", departments);
          sectors.add(sectorMap);
        }
      } else {
        resultMap = new LinkedHashMap<>();
        resultMap.put("company", company);

        List<Map<String, Object>> sectors = new ArrayList<>();

        Map<String, Object> sectorMap = new LinkedHashMap<>();
        sectorMap.put("sectorid", sectorid);
        sectorMap.put("sectorname", sectorname);
        sectorMap.put("sectorcode", sectorcode);

        List<Map<String, Object>> departments = new ArrayList<>();

        Map<String, Object> departmentMap = new LinkedHashMap<>();
        departmentMap.put("deptid", deptid);
        departmentMap.put("deptname", deptname);
        departmentMap.put("deptcode", deptcode);

        positionMap.put("id", positionId.toString());
        positionMap.put("name", positionName);

        List<Map<String, Object>> positions = new ArrayList<>();
        positions.add(positionMap);
        departmentMap.put("positions", positions);

        departments.add(departmentMap);
        sectorMap.put("departments", departments);

        sectors.add(sectorMap);
        resultMap.put("sectors", sectors);

        companyMap.put(company, resultMap);
        resultList.add(resultMap);
      }
    }

    return resultList;
  }

  public List<Map<String, Object>> findDepartmentsByUser(Long byId) {
    String jpql = "SELECT " +
        "c.company_name AS company_name, " +
        "s.sector_name AS sector_name, " +
        "s.sector_code AS sector_code, " +
        "d.id AS department_id, " +
        "d.dept_name AS department_name, " +
        "d.dept_code AS department_code " +
        "FROM sector s " +
        "JOIN company c ON s.company_id = c.id " +
        "JOIN department d ON s.id = d.sector_id " +
        "JOIN user_departments ud ON ud.department_id = d.id " +
        "JOIN users u ON ud.user_id = u.id " +
        "WHERE u.id = :id";

    List<Object[]> results = entityManager
        .createNativeQuery(jpql)
        .setParameter("id", byId)
        .getResultList();

    List<Map<String, Object>> resultList = new ArrayList<>();

    Map<String, Map<String, Object>> companyMap = new HashMap<>();

    for (Object[] row : results) {
      String company = (String) row[0];
      String sectorName = (String) row[1];
      String sectorCode = (String) row[2];
      Long deptId = ((Number) row[3]).longValue();
      String deptName = (String) row[4];
      String deptCode = (String) row[5];

      Map<String, Object> resultMap;
      Map<String, Object> departmentMap = new LinkedHashMap<>();

      if (companyMap.containsKey(company)) {
        resultMap = companyMap.get(company);
        List<Map<String, Object>> sectors = (List<Map<String, Object>>) resultMap.get(
            "sectors");

        boolean sectorExists = false;
        for (Map<String, Object> sector : sectors) {
          if (sector.get("sectorname").equals(sectorName)) {
            sectorExists = true;
            List<Map<String, Object>> departments = (List<Map<String, Object>>) sector.get(
                "departments");

            boolean departmentExists = false;
            for (Map<String, Object> department : departments) {
              if (department.get("deptname").equals(deptName)) {
                departmentExists = true;
                break;
              }
            }

            if (!departmentExists) {
              Map<String, Object> newDepartmentMap = new LinkedHashMap<>();
              newDepartmentMap.put("deptid", deptId);
              newDepartmentMap.put("deptname", deptName);
              newDepartmentMap.put("deptcode", deptCode);
              departments.add(newDepartmentMap);
            }

            break;
          }
        }

        if (!sectorExists) {
          Map<String, Object> sectorMap = new LinkedHashMap<>();
          sectorMap.put("sectorname", sectorName);
          sectorMap.put("sectorcode", sectorCode);

          List<Map<String, Object>> departments = new ArrayList<>();

          Map<String, Object> newDepartmentMap = new LinkedHashMap<>();
          newDepartmentMap.put("deptid", deptId);
          newDepartmentMap.put("deptname", deptName);
          newDepartmentMap.put("deptcode", deptCode);

          departments.add(newDepartmentMap);
          sectorMap.put("departments", departments);
          sectors.add(sectorMap);
        }
      } else {
        resultMap = new LinkedHashMap<>();
        resultMap.put("company", company);

        List<Map<String, Object>> sectors = new ArrayList<>();

        Map<String, Object> sectorMap = new LinkedHashMap<>();
        sectorMap.put("sectorname", sectorName);
        sectorMap.put("sectorcode", sectorCode);

        List<Map<String, Object>> departments = new ArrayList<>();

        Map<String, Object> newDepartmentMap = new LinkedHashMap<>();
        newDepartmentMap.put("deptid", deptId);
        newDepartmentMap.put("deptname", deptName);
        newDepartmentMap.put("deptcode", deptCode);

        departments.add(newDepartmentMap);
        sectorMap.put("departments", departments);

        sectors.add(sectorMap);
        resultMap.put("sectors", sectors);

        companyMap.put(company, resultMap);
        resultList.add(resultMap);
      }
    }

    return resultList;
  }

  public List<Map<String, Object>> findAllJoinDepartmentssector() {
    String jpql = "SELECT " +
        "c.company_name AS company_name, " +
        "s.sector_name AS sector_name, " +
        "s.sector_code AS sector_code, " +
        "d.id AS department_id, " +
        "d.dept_name AS department_name, " +
        "d.dept_code AS department_code " +
        "FROM " +
        "department d " +
        "JOIN sector s ON d.sector_id = s.id " +
        "JOIN company c ON s.company_id = c.id; ";

    List<Object[]> results = entityManager
        .createNativeQuery(jpql)
        .getResultList();

    List<Map<String, Object>> resultList = new ArrayList<>();

    for (Object[] row : results) {
      Map<String, Object> resultMap = new HashMap<>();

      resultMap.put("company", (String) row[0]);
      resultMap.put("sectorName", (String) row[1]);
      resultMap.put("sectorCode", (String) row[2]);
      resultMap.put("id", ((Number) row[3]).longValue());
      resultMap.put("deptName", (String) row[4]);
      resultMap.put("deptCode", (String) row[5]);

      resultList.add(resultMap);
    }

    return resultList;
  }

  public Optional<Department> findByDeptCodeAndDeptName(
      String DeptCode,
      String DeptName) {
    return departmentRepository.findByDeptCodeAndDeptName(DeptCode, DeptName);
  }

  public boolean isDeptNull(CreateDepartmentRequest request) {
    return (request == null ||
        request.getDeptName() == null ||
        request.getDeptName().isEmpty() ||
        request.getDeptCode() == null ||
        request.getDeptCode().isEmpty());
  }
}
