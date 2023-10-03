package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Long> {

    Optional<Status> findById(Long statusId);
}
