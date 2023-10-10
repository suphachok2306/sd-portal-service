package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Department;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
  Optional<Department> findByDeptName(String deptName);

  Optional<Department> findByDeptCode(String deptCode);

  Optional<Department> findById(Long departmentId);

  Optional<Department> findByDeptCodeAndDeptName(
    String DeptCode,
    String DeptName
  );
}
