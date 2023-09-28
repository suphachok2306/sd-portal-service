package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingRepository extends JpaRepository<Training,Long> {
    Optional<Training> findById(Long trainingId);
}
