package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDeptName(String deptName);

    Optional<Department> findByDeptCode(String deptCode);

    Optional<Department> findById(Long departmentId);



}
