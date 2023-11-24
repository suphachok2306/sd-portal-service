package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Budget_Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Budget_DepartmentRepository
  extends JpaRepository<Budget_Department, Long> {}
