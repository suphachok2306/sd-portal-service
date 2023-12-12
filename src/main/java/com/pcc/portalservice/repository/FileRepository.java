package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.TrainingFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<TrainingFiles, Long> {
}
