package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Status;
import com.pcc.portalservice.model.enums.StatusApprove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Long> {

    boolean existsByStatus(StatusApprove statusApprove);
}
