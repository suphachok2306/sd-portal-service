package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Position;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.PositionRepository;
import com.pcc.portalservice.requests.CreatePositionRequest;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class PositionService {

  private final PositionRepository positionRepository;
  private final DepartmentRepository departmentRepository;

  public Position create(CreatePositionRequest createPositionRequest) {
    Department departmentId = departmentRepository
      .findById(createPositionRequest.getDepartmentId())
      .orElseThrow(() ->
        new RuntimeException(
          "departmentId not found: " + createPositionRequest.getDepartmentId()
        )
      );

    Position position = Position
      .builder()
      .positionName(createPositionRequest.getPositionName())
      .department(departmentId)
      .build();
    return positionRepository.save(position);
  }

  public List<Position> findAll() {
    return positionRepository.findAll();
  }

  public Optional<Position> findByPositionNameAndDepartment(
    String positionName,
    Department department
  ) {
    return positionRepository.findByPositionNameAndDepartment(
      positionName,
      department
    );
  }

  public boolean isPositionNull(CreatePositionRequest request) {
    return (
      request == null ||
      request.getPositionName() == null ||
      request.getPositionName().isEmpty()
    );
  }
}
