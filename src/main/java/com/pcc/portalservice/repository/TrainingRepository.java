package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
  Optional<Training> findById(Long trainingId);

  List<Training> findByUser(User user);

  List<Training> findByApprove1(User approve1);
}
