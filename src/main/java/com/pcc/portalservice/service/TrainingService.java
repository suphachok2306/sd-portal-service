package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.PositionRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class TrainingService {
    private final CourseService courseService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final SectorRepository sectorRepository;


    public Training createSectionOne(CreateTrainingSectionOneRequest createTrainingSectionOneRequest){
        Department departmentName = departmentRepository.findByDeptName(createTrainingSectionOneRequest.getDeptName())
                .orElseThrow(() -> new RuntimeException("departmentName not found: " + createTrainingSectionOneRequest.getDeptName()));

        Department departmentCode = departmentRepository.findByDeptCode(createTrainingSectionOneRequest.getDeptCode())
                .orElseThrow(() -> new RuntimeException("departmentCode not found: " + createTrainingSectionOneRequest.getDeptCode()));
    }
}
