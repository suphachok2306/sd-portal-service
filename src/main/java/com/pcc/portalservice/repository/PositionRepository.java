package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Position;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
  Optional<Position> findByPositionName(String positionName);

  Optional<Position> findByPositionNameAndDepartment(
    String positionName,
    Department department
  );
}
