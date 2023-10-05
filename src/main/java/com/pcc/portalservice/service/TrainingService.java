package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor
@Service
@Transactional
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final ResultRepository resultRepository;

    private final EntityManager entityManager;


    public Training createTraining(CreateTrainingRequest createTrainingRequest) throws ParseException {

        User user = userRepository.findById(createTrainingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("UserId not found: " + createTrainingRequest.getUserId()));
        User approve1 = userRepository.findById(createTrainingRequest.getApprove1Id())
                .orElseThrow(() -> new RuntimeException("Approve1Id not found: " + createTrainingRequest.getApprove1Id()));
        Course course = courseRepository.findById(createTrainingRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("CourseId not found: " + createTrainingRequest.getCourseId()));


        Date startDate = course.getStartDate();
        Date endDate = course.getEndDate();
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        int daysDifference = (int) (differenceInMilliseconds / (1000 * 60 * 60 * 24)) + 1;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actionDateFormat = dateFormat.parse(createTrainingRequest.getActionDate());


        Training training = Training.builder()
                .user(user)
                .dateSave(new Date())
                .day(daysDifference)
                .courses(Arrays.asList(course))
                .action(createTrainingRequest.getAction())
                .actionDate(actionDateFormat)
                .approve1(approve1)
                .build();

        if (training.getResult() == null) {
            training.setResult(new ArrayList<>());
        }
        Result result = Result.builder()
                .training(training)
                .evaluator(null)
                .result1(null)
                .result2(null)
                .result3(null)
                .result4(null)
                .result5(null)
                .result6(null)
                .result7(null)
                .result(null)
                .comment(null)
                .cause(null)
                .plan(null)
                .build();
        resultRepository.save(result);
        training.getResult().add(result);

        if (training.getStatus() == null) {
            training.setStatus(new ArrayList<>());
        }
        Status status1 = Status.builder()
                .status(null)
                .training(training)
                .approveId(createTrainingRequest.getApprove1Id())
                .build();
        statusRepository.save(status1);

        training.getStatus().add(status1);
        Training savedTraining = trainingRepository.save(training);
        return savedTraining;
    }

    public Training editTraining(Long trainingId, Long resultId, Long statusId,CreateTrainingRequest editTraining) throws ParseException {

        Training training_id = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("TrainingId not found: " + trainingId));
        Result result_id = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("ResultId not found: " + resultId));
        Status status_id = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("StatusId not found: " + statusId));
        Course course_id = courseRepository.findById(editTraining.getCourseId())
                .orElseThrow(() -> new RuntimeException("CourseId not found: " + editTraining.getCourseId()));
        User user_id = userRepository.findById(editTraining.getUserId())
                .orElseThrow(() -> new RuntimeException("UserId not found: " + editTraining.getUserId()));
        User approve1_id = userRepository.findById(editTraining.getApprove1Id())
                .orElseThrow(() -> new RuntimeException("Approve1Id not found: " + editTraining.getCourseId()));
        User evaluator_id = userRepository.findById(editTraining.getEvaluatorId())
                .orElseThrow(() -> new RuntimeException("EvaluatorId not found: " + editTraining.getEvaluatorId()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actionDateFormat = dateFormat.parse(editTraining.getActionDate());


        training_id.setUser(user_id);
        training_id.setDateSave(new Date());
        training_id.setAction(editTraining.getAction());
        training_id.setActionDate(actionDateFormat);
        training_id.getCourses().clear();                       ///////ต้อง clear ก่อน
        training_id.getCourses().add(course_id);
        //training_id.setCourses(Arrays.asList(course_id));     ////ใช้แบบนี้ไม่ได้
        training_id.setApprove1(approve1_id);

        result_id.setEvaluator(evaluator_id);
        result_id.setResult1(editTraining.getResult1());
        result_id.setResult2(editTraining.getResult2());
        result_id.setResult3(editTraining.getResult3());
        result_id.setResult4(editTraining.getResult4());
        result_id.setResult5(editTraining.getResult5());
        result_id.setResult6(editTraining.getResult6());
        result_id.setResult7(editTraining.getResult7());
        result_id.setResult(editTraining.getResult());
        result_id.setComment(editTraining.getComment());
        result_id.setCause(editTraining.getCause());
        result_id.setPlan(editTraining.getPlan());

        status_id.setStatus(editTraining.getStatus1());

        Training updatedTraining = trainingRepository.save(training_id);
        resultRepository.save(result_id);
        statusRepository.save(status_id);
        return updatedTraining;
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


    public List<Training> findTrainingsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserId not found: " + userId));
        return trainingRepository.findByUser(user);
    }

    public List<Training> findTrainingsByApprove1Id(Long approve1Id) {
        User approve1 = userRepository.findById(approve1Id)
                .orElseThrow(() -> new RuntimeException("Approve1Id not found: " + approve1Id));
        return trainingRepository.findByApprove1(approve1);
    }

    public List<Training> findAllTraining() {
        return trainingRepository.findAll();
    }

    public List<Training> findbyAllCountApprove(Long count) {
    String jpql = "SELECT t FROM Training t " +
                  "WHERE (SELECT COUNT(s) FROM Status s WHERE s.training = t AND s.status = 'อนุมัติ') = :count";
    
    TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);

    query.setParameter("count", count);
    
    List<Training> resultList = query.getResultList();
    
    return resultList;
    }

    

}


