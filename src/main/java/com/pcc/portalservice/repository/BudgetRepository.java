package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Budget;
import com.pcc.portalservice.model.Department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Budget findByYearAndDepartment(String year, Department departmentId);
}
