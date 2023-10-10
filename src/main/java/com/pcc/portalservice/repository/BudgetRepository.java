package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Budget;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {}
