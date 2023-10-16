package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
  Optional<Status> findById(Long statusId);
  Optional<Status> findByTrainingId(Long trainingId);
  Optional<Status> findByTrainingIdAndApproveIdIsNull(Long trainingId);
}
