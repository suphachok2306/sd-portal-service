package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    

}
