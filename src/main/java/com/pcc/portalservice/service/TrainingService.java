package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;


    public Training createSectionOne(CreateTrainingSectionOneRequest createTrainingSectionOneRequest) throws ParseException {

        User user = userRepository.findById(createTrainingSectionOneRequest.getUserId()).orElseThrow(() -> new RuntimeException("UserId not found: " + createTrainingSectionOneRequest.getUserId()));
        User approve1 = userRepository.findById(createTrainingSectionOneRequest.getApprove1_id()).orElseThrow(() -> new RuntimeException("Approve1_id not found: " + createTrainingSectionOneRequest.getApprove1_id()));
        Course course = courseRepository.findById(createTrainingSectionOneRequest.getCourseId()).orElseThrow(() -> new RuntimeException("CourseId not found: " + createTrainingSectionOneRequest.getCourseId()));

        Date startDate = course.getStartDate();
        Date endDate = course.getEndDate();
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        int daysDifference = (int) (differenceInMilliseconds / (1000 * 60 * 60 * 24)) + 1;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actionDateFormat = dateFormat.parse(createTrainingSectionOneRequest.getActionDate());


        Training training = Training.builder()
                .user(user)
                .dateSave(new Date())
                .day(daysDifference)
                .courses(Arrays.asList(course))
                .action(createTrainingSectionOneRequest.getAction())
                .actionDate(actionDateFormat)
                .approve1(approve1)
                .build();

        if (training.getStatus() == null) {
            training.setStatus(new ArrayList<>());
        }

        Status status1 = Status.builder()
                //.status(createTrainingSectionOneRequest.getStatus1())
                .status(null)
                .training(training)
                .approveId(createTrainingSectionOneRequest.getApprove1_id())
                .build();
        statusRepository.save(status1);

        training.getStatus().add(status1);
        Training savedTraining = trainingRepository.save(training);

        return savedTraining;
    }


    public Training setStatusToTraining(Long trainingId, Long approveId, StatusApprove statusApprove) {

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        Optional<Status> optionalStatus = training.getStatus().stream()
                .filter(status -> status.getApproveId().equals(approveId))
                .findFirst();

        if (optionalStatus.isPresent()) {
            Status existingStatus = optionalStatus.get();
            existingStatus.setStatus(statusApprove);
            statusRepository.save(existingStatus);
        } else {

            Status status = Status.builder()
                    .status(statusApprove)
                    .training(training)
                    .approveId(approveId)
                    .build();
            statusRepository.save(status);
            training.getStatus().add(status);
        }

        return trainingRepository.save(training);
    }

    public Training findById(Long id) {
        return trainingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }


    public List<Training> findAllTraining() {
        return trainingRepository.findAll();
    }


}


