package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;

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
                .status(createTrainingSectionOneRequest.getStatus1())
                .training(training)
                .approveId(createTrainingSectionOneRequest.getApprove1_id())
                .build();
        statusRepository.save(status1);

        training.getStatus().add(status1);
        Training savedTraining = trainingRepository.save(training);

        return savedTraining;
    }
}


