package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Budget;
import com.pcc.portalservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

}
