package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Position;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.PositionRepository;
import com.pcc.portalservice.requests.CreatePositionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PositionService {
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public boolean isPositionNull(Position position){
        if(position == null || position.getPositionName() == null || position.getPositionName().isEmpty()
                || position.getDepartment() == null){
            return true;
        }
        return false;
    }

    public Position create(CreatePositionRequest createPositionRequest) {

        Department departmentId = departmentRepository.findById(createPositionRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("departmentId not found: " + createPositionRequest.getDepartmentId()));

        Position position = Position.builder()
                .positionName(createPositionRequest.getPositionName())
                .department(departmentId)
                .build();
        return positionRepository.save(position);
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
    }
}

