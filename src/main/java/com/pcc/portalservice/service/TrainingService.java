package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditTrainingSection1PersonRequest;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public Training createTraining(CreateTrainingRequest createTrainingRequest)
            throws ParseException {
        User user = userRepository
                .findById(createTrainingRequest.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approve1Id not found: " + createTrainingRequest.getApprove1_id()
                        )
                );
        User approve1 = userRepository
                .findById(createTrainingRequest.getApprove1_id())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approve1Id not found: " + createTrainingRequest.getApprove1_id()
                        )
                );
        Course course = courseRepository
                .findById(createTrainingRequest.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "CourseId not found: " + createTrainingRequest.getCourseId()
                        )
                );

        Role vicePresidentRole = approve1
                .getRoles()
                .stream()
                .filter(role -> role.getRole().equals(Roles.VicePresident))
                .findFirst()
                .orElse(null);

        Date startDate = course.getStartDate();
        Date endDate = course.getEndDate();
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        int daysDifference = (int) (
                differenceInMilliseconds / (1000 * 60 * 60 * 24)
        ) +
                1;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actionDateFormat = dateFormat.parse(
                createTrainingRequest.getActionDate()
        );

        Training training = Training
                .builder()
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
        Result result = Result
                .builder()
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

        if (vicePresidentRole != null) {
            Status status1 = Status
                    .builder()
                    .status(null)
                    .training(training)
                    .approveId(createTrainingRequest.getApprove1_id())
                    .active(1)
                    .build();

            Status status2 = Status
                    .builder()
                    .status(null)
                    .training(training)
                    .active(0)
                    .build();

            statusRepository.save(status1);
            statusRepository.save(status2);
            training.getStatus().add(status1);
            training.getStatus().add(status2);
        } else {
            Status status1 = Status
                    .builder()
                    .status(null)
                    .training(training)
                    .approveId(createTrainingRequest.getApprove1_id())
                    .active(1)
                    .build();

            Status status2 = Status
                    .builder()
                    .status(null)
                    .training(training)
                    .approveId(Long.valueOf(3))
                    .active(0)
                    .build();

            Status status3 = Status
                    .builder()
                    .status(null)
                    .training(training)
                    .active(0)
                    .build();

            statusRepository.save(status1);
            statusRepository.save(status2);
            statusRepository.save(status3);
            training.getStatus().add(status1);
            training.getStatus().add(status2);
            training.getStatus().add(status3);
        }

        Training savedTraining = trainingRepository.save(training);
        return savedTraining;
    }

    public Training editTrainingSection1(
            Long trainingId,
            Long statusId,
            EditTrainingSection1Request editTraining
    ) throws ParseException {
        Training training_id = trainingRepository
                .findById(trainingId)
                .orElseThrow(() ->
                        new RuntimeException("TrainingId not found: " + trainingId)
                );
        Status status_id = statusRepository
                .findById(statusId)
                .orElseThrow(() -> new RuntimeException("StatusId not found: " + statusId)
                );
        Course course_id = courseRepository
                .findById(editTraining.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "CourseId not found: " + editTraining.getCourseId()
                        )
                );
        User user_id = userRepository
                .findById(editTraining.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("UserId not found: " + editTraining.getUserId())
                );
        User approve1_id = userRepository
                .findById(editTraining.getApprove1_id())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approve1Id not found: " + editTraining.getCourseId()
                        )
                );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actionDateFormat = dateFormat.parse(editTraining.getActionDate());

        training_id.setUser(user_id);
        training_id.setDateSave(new Date());
        training_id.setAction(editTraining.getAction());
        training_id.setActionDate(actionDateFormat);
        training_id.getCourses().clear(); ///////ต้อง clear ก่อน
        training_id.getCourses().add(course_id);
        training_id.setApprove1(approve1_id);

        //        status_id.setStatus(editTraining.getStatus1());
        status_id.setApproveId(editTraining.getApprove1_id());
        statusRepository.save(status_id);
        Training updatedTraining = trainingRepository.save(training_id);
        return updatedTraining;
    }

    public Training editTrainingSection1Person(
            Long trainingId,
            EditTrainingSection1PersonRequest editTraining
    ) throws ParseException {
        Training training_id = trainingRepository
                .findById(trainingId)
                .orElseThrow(() ->
                        new RuntimeException("TrainingId not found: " + trainingId)
                );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actionDateFormat = dateFormat.parse(editTraining.getActionDate());

        training_id.setAction(editTraining.getAction());
        training_id.setActionDate(actionDateFormat);

        Training updatedTraining = trainingRepository.save(training_id);
        return updatedTraining;
    }

    public Result editTrainingSection2(
            Long resultId,
            EditTrainingSection2Request editTraining
    ) throws ParseException {
        Result result_id = resultRepository
                .findById(resultId)
                .orElseThrow(() -> new RuntimeException("ResultId not found: " + resultId)
                );
        User evaluator_id = userRepository
                .findById(editTraining.getEvaluatorId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "EvaluatorId not found: " + editTraining.getEvaluatorId()
                        )
                );

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

        Result updatedTraining = resultRepository.save(result_id);
        return updatedTraining;
    }

    public Training setStatusToTraining(
            Long trainingId,
            Long approveId,
            StatusApprove statusApprove
    ) {
        Training training = trainingRepository
                .findById(trainingId)
                .orElseThrow(() ->
                        new RuntimeException("Training not found with ID: " + trainingId)
                );
        Optional<Status> optionalStatus = training
                .getStatus()
                .stream()
                .filter(status -> approveId.equals(status.getApproveId()))
                .findFirst();

        if (optionalStatus.isPresent()) {
            if (optionalStatus.get().getActive() != 3) {
                Status existingStatus = optionalStatus.get();
                existingStatus.setStatus(statusApprove);
                existingStatus.setActive(2);
                statusRepository.save(existingStatus);

                Optional<Status> updateStatusNext = training
                        .getStatus()
                        .stream()
                        .filter(status ->
                                trainingId.equals(status.getTraining().getId()) &&
                                        status.getActive() != 2
                        )
                        .findFirst();

                if (updateStatusNext.isPresent()) {
                    if (updateStatusNext.get().getApproveId() != null) {
                        Status nextStatus = updateStatusNext.get();
                        nextStatus.setActive(1);
                        statusRepository.save(nextStatus);
                    } else {
                        Status nextStatus = updateStatusNext.get();
                        nextStatus.setActive(3);
                        statusRepository.save(nextStatus);
                    }
                }
            }
        } else {
            Optional<Status> updateStatus = training
                    .getStatus()
                    .stream()
                    .filter(status ->
                            trainingId.equals(status.getTraining().getId()) &&
                                    status.getApproveId() == null
                    )
                    .findFirst();
            Status UpdateStatus = updateStatus.get();
            UpdateStatus.setStatus(statusApprove);
            UpdateStatus.setApproveId(approveId);
            UpdateStatus.setActive(3);
            statusRepository.save(UpdateStatus);
        }

        return trainingRepository.save(training);
    }

    public Map<String, Object> findById(Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = builder.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        query.where(builder.equal(root.get("id"), id));

        Training training = entityManager.createQuery(query).getSingleResult();

        int approvedCount = 0;
        int disapprovedCount = 0;
        String result_status;

        for (Status status : training.getStatus()) {
            if (status.getStatus() != null) {
                if ("อนุมัติ".equals(status.getStatus().toString())) {
                    approvedCount++;
                } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
                    disapprovedCount++;
                }
            }
        }

        if (approvedCount == 3) {
            result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
            result_status = "ไม่อนุมัติ";
        } else {
            result_status = "รอประเมิน";
        }

        Map<String, Object> resultWithStatus = new HashMap<>();
        resultWithStatus.put("training", training);
        resultWithStatus.put("result_status", result_status);

        return resultWithStatus;
    }

    public List<Map<String, Object>> findTrainingsByUserId(Long userId) {
        String jpql = "SELECT t FROM Training t WHERE user_id = :id";

        TypedQuery<Training> query = entityManager.createQuery(
                jpql,
                Training.class
        );

        query.setParameter("id", userId);
        List<Training> resultList = query.getResultList();

        return calculateTrainingResultStatus(resultList);
    }


  public static List<Map<String, Object>> calculateTrainingResultStatus(
    List<Training> trainingList
  ) {
    List<Map<String, Object>> resultWithStatusList = new ArrayList<>();

    for (Training training : trainingList) {
      int approvedCount = 0;
      int disapprovedCount = 0;
      int cancalapprovedCount = 0;
      String result_status;
      int count = 0;

      for (Status status : training.getStatus()) {
        if (status.getStatus() != null) {
          if ("อนุมัติ".equals(status.getStatus().toString())) {
            approvedCount++;
          } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
            disapprovedCount++;
          } else if ("ยกเลิก".equals(status.getStatus().toString())) {
            cancalapprovedCount++;
          }
        }
        count++;
      }

     if (count == 3) {
        if (approvedCount == 3) {
            result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
            result_status = "ไม่อนุมัติ";
        } else if (cancalapprovedCount >= 1) {
            result_status = "ยกเลิก";
        } else {
            result_status = "รอประเมิน";
        }
    } else {
        if (approvedCount == 2) {
            result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
            result_status = "ไม่อนุมัติ";
        } else if (cancalapprovedCount >= 1) {
            result_status = "ยกเลิก";
        } else {
            result_status = "รอประเมิน";
        }
      }

      Map<String, Object> resultWithStatus = new HashMap<>();
      resultWithStatus.put("training", training);
      resultWithStatus.put("result_status", result_status);
      resultWithStatusList.add(resultWithStatus);
    }

    return resultWithStatusList;
  }

  public static List<Map<String, Object>> calculateTrainingResultStatus(
    List<Training> trainingList,
    long approve1Id,
    int active
  ) {
    List<Map<String, Object>> resultWithStatusList = new ArrayList<>();

    for (Training training : trainingList) {
      int approvedCount = 0;
      int disapprovedCount = 0;
      int cancalapprovedCount = 0;
      String result_status;
      String isDo = null;
      int count = 0;

      for (Status status : training.getStatus()) {
        if (status.getStatus() != null) {
          if ("อนุมัติ".equals(status.getStatus().toString())) {
            approvedCount++;
          } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
            disapprovedCount++;
          } else if ("ยกเลิก".equals(status.getStatus().toString())) {
            cancalapprovedCount++;
          }
        }
        if ((status.getActive() == active) && (status.getStatus() != null)) {
          if (status.getApproveId() == approve1Id) {
            if ("อนุมัติ".equals(status.getStatus().toString())) {
              isDo = "อนุมัติ";
            } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
              isDo = "ไม่อนุมัติ";
            } else if ("ยกเลิก".equals(status.getStatus().toString())) {
              isDo = "ยกเลิก";
            }
          }
        } else {
          isDo = "รอประเมิน";
        }
        count++;
      }


      if (count == 3) {
        if (approvedCount == 3) {
            result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
            result_status = "ไม่อนุมัติ";
        } else if (cancalapprovedCount >= 1) {
            result_status = "ยกเลิก";
        } else {
            result_status = "รอประเมิน";
        }
    } else {
        if (approvedCount == 2) {
            result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
            result_status = "ไม่อนุมัติ";
        } else if (cancalapprovedCount >= 1) {
            result_status = "ยกเลิก";
        } else {
            result_status = "รอประเมิน";
        }
    }    

      Map<String, Object> resultWithStatus = new HashMap<>();
      resultWithStatus.put("training", training);
      resultWithStatus.put("result_status", result_status);
      resultWithStatus.put("isDo", isDo);
      resultWithStatusList.add(resultWithStatus);
    }

    return resultWithStatusList;
  }
}
// public List<Map<String, Object>> findbyAllCountApprove(Long count) {
//     String jpql = "SELECT t FROM Training t " +
//                   "WHERE (SELECT COUNT(s) FROM Status s WHERE s.training = t AND s.status = 'อนุมัติ') = :count ";
//     TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);
//     query.setParameter("count", count);
//     List<Training> resultList = query.getResultList();
//     List<Map<String, Object>> resultWithStatusList = new ArrayList<>();
//     for (Training training : resultList) {
//         int approvedCount = 0;
//         int disapprovedCount = 0;
//         try {
//             for (Status status : training.getStatus()) {
//                 if ("อนุมัติ".equals(status.getStatus().toString())) {
//                     approvedCount++;
//                 } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
//                     disapprovedCount++;
//                 }
//             }
//             String result_status;
//             if (approvedCount == 3) {
//                 result_status = "อนุมัติ";
//             } else if (disapprovedCount >= 1) {
//                 result_status = "ไม่อนุมัติ";
//             } else {
//                 result_status = "รอประเมิน";
//             }
//             Map<String, Object> resultWithStatus = new HashMap<>();
//             resultWithStatus.put("training", training);
//             resultWithStatus.put("result_status", result_status);
//             resultWithStatusList.add(resultWithStatus);
//         }
//         catch(Exception e) {
//             continue;
//         }
//     }
//     return resultWithStatusList;
// }
// public List<Map<String, Object>> findNextApprove() {
//     String jpql = "SELECT DISTINCT s.training_id FROM Status s ORDER BY s.training_id";
//     Query query = entityManager.createNativeQuery(jpql);
//     List<Object> listOfTrainId = query.getResultList();
//     List<Map<String, Object>> resultWithStatusList = new ArrayList<>();
//     System.out.println(listOfTrainId);
//     for (Object training : listOfTrainId) {
//         String jpqlstatus = "SELECT approve_id,email,firstname,lastname FROM Status s join users u on approve_id = u.id  WHERE s.training_id = :training AND s.active = 0 LIMIT 1";
//         Query statusQuery = entityManager.createNativeQuery(jpqlstatus);
//         statusQuery.setParameter("training", training);
//         List<Status> statusList = statusQuery.getResultList();
//         Map<String, Object> resultWithStatus = new HashMap<>();
//         resultWithStatus.put("trainingId", training);
//         resultWithStatus.put("statusList", statusList);
//         resultWithStatusList.add(resultWithStatus);
//     }
//     return resultWithStatusList;
// }
