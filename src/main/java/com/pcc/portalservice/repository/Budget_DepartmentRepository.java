package com.pcc.portalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pcc.portalservice.model.Budget_Department;


@Repository
public interface Budget_DepartmentRepository extends JpaRepository<Budget_Department, Long> {

}
