package com.pcc.portalservice.service;
import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditTrainingSection1PersonRequest;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.query.NativeQuery;
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

  /**
   * @สร้างTraining
   */
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

    Training training = Training
      .builder()
      .user(user)
      .dateSave(new Date())
      .day(daysDifference)
      .courses(Arrays.asList(course))
      .budget(createTrainingRequest.getBudget())
      //      .action(createTrainingRequest.getAction())
      //      .actionDate(actionDateFormat)
      .action(null)
      .actionDate(null)
      .approve1(approve1)
      .build();

    if (training.getResult() == null) {
      training.setResult(new ArrayList<>());
    }
    Result result = Result
      .builder()
      .training(training)
      //.evaluator(approve1)
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
        .approveId(approve1)
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
      User ap1 = userRepository
              .findById(Long.valueOf(3))
              .orElseThrow(() ->
                      new RuntimeException(
                              "Approve1Id not found: " + createTrainingRequest.getApprove1_id()
                      )
              );
      Status status1 = Status
        .builder()
        .status(null)
        .training(training)
        .approveId(approve1)
        .active(1)
        .build();

      Status status2 = Status
        .builder()
        .status(null)
        .training(training)
        .approveId(ap1)
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

  /**
   * @EditTrainingSection1
   */
  public Training editTrainingSection1(
    Long trainingId,
    EditTrainingSection1Request editTraining
  ) throws ParseException {
//    Training training_id = trainingRepository
//      .findById(trainingId)
//      .orElseThrow(() ->
//        new RuntimeException("TrainingId not found: " + trainingId)
//      );
    Training training_id = findByTrainingIdByMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(trainingId);

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
          "Approve1Id not found: " + editTraining.getApprove1_id()
        )
      );

    training_id.setUser(user_id);
    training_id.setDateSave(new Date());
    training_id.getCourses().clear();
    training_id.getCourses().add(course_id);
    training_id.setApprove1(approve1_id);
    training_id.setBudget(editTraining.getBudget());
    if(editTraining.getApprove1_id() != training_id.getApprove1().getId()){
      changeApprover(editTraining, trainingId);
   }
    Training updatedTraining = trainingRepository.save(training_id);
    return updatedTraining;
  }

  /**
   * @EditTrainingSection3
   */
  public Training editTrainingSection1Person(
    Long trainingId,
    EditTrainingSection1PersonRequest editTraining
  ) throws ParseException {
//    Training training_id = trainingRepository
//      .findById(trainingId)
//      .orElseThrow(() ->
//        new RuntimeException("TrainingId not found: " + trainingId)
//      );
    Training training_id = findByTrainingIdByMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(trainingId);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date actionDateFormat = dateFormat.parse(editTraining.getActionDate());

    training_id.setAction(editTraining.getAction());
    training_id.setActionDate(actionDateFormat);

    Training updatedTraining = trainingRepository.save(training_id);
    return updatedTraining;
  }

  /**
   * @EditTrainingSection2
   */
  public Result editTrainingSection2(
    Long resultId,
    EditTrainingSection2Request editTraining
  ) throws ParseException {
    Result result_id = resultRepository
      .findById(resultId)
      .orElseThrow(() -> new RuntimeException("ResultId not found: " + resultId)
      );
//    User evaluator_id = userRepository
//      .findById(editTraining.getEvaluatorId())
//      .orElseThrow(() ->
//        new RuntimeException(
//          "EvaluatorId not found: " + editTraining.getEvaluatorId()
//        )
//      );

    //result_id.setEvaluator(evaluator_id);
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

  /**
   * @SetStatus
   */
  public Training setStatusToTraining(
    Long trainingId,
    Long approveId,

    StatusApprove statusApprove
  ) {
//    Training training = trainingRepository
//      .findById(trainingId)
//      .orElseThrow(() ->
//        new RuntimeException("Training not found with ID: " + trainingId)
//      );
    Training training_id = findByTrainingIdByMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(trainingId);

    User approve = userRepository
            .findById(approveId)
            .orElseThrow(() ->
                    new RuntimeException("Training not found with ID: " + trainingId)
            );
    Optional<Status> optionalStatus = training_id
            .getStatus()
            .stream()
            .filter(status -> {
              User approveIdObj = status.getApproveId();
              return approveIdObj != null && approveId.equals(approveIdObj.getId());
            })
            .findFirst();

    if (optionalStatus.isPresent()) {
      if (optionalStatus.get().getActive() != 3) {
        Status existingStatus = optionalStatus.get();
        existingStatus.setStatus(statusApprove);
        existingStatus.setActive(2);
        statusRepository.save(existingStatus);

        Optional<Status> updateStatusNext = training_id
          .getStatus()
          .stream()
          .filter(status ->
            trainingId.equals(status.getTraining().getId()) &&
            status.getActive() != 2 &&
            status.getActive() != 3
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
      } else {
        Status existingStatus = optionalStatus.get();
        existingStatus.setStatus(statusApprove);
        statusRepository.save(existingStatus);
      }
    } else {
      Optional<Status> updateStatus = training_id
        .getStatus()
        .stream()
        .filter(status ->
          trainingId.equals(status.getTraining().getId()) &&
          status.getApproveId() == null
        )
        .findFirst();
      Status UpdateStatus = updateStatus.get();
      UpdateStatus.setStatus(statusApprove);
      UpdateStatus.setApproveId(approve);
      UpdateStatus.setActive(3);
      statusRepository.save(UpdateStatus);
    }

    return trainingRepository.save(training_id);
  }

  /**
   * @หาTrainingด้วยId
   */

  public Training findByTrainingIdByMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(Long id) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = builder.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);

    // Fetch statuses without duplicates
    root.fetch("status", JoinType.INNER);

    query.where(builder.equal(root.get("id"), id));

    List<Training> trainingList = entityManager
            .createQuery(query)
            .getResultList();

    if (!trainingList.isEmpty()) {
      List<Status> statusListCopy = new ArrayList<>(trainingList.get(0).getStatus());
      statusListCopy.sort(Comparator.comparing(Status::getId));
      trainingList.get(0).setStatus(statusListCopy);

      return trainingList.get(0);
    } else {
      return null;
    }
  }



  public Map<String, Object> findById(Long id) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = builder.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);

    root.fetch("status", JoinType.INNER);

    query.where(builder.equal(root.get("id"), id));

    List<Training> trainingList = entityManager
            .createQuery(query)
            .getResultList();

    Map<String, Object> resultWithStatus = new HashMap<>();

    if (!trainingList.isEmpty()) {
      Training training = trainingList.get(0);
      int approvedCount = 0;
      int disapprovedCount = 0;
      int cancalapprovedCount = 0;
      String result_status;
      int count = 0;

      for (Status status : training.getStatus()) {
        System.out.println(count);
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

      List<Status> statusListCopy = new ArrayList<>(training.getStatus());
      statusListCopy.sort(Comparator.comparing(Status::getId));
      training.setStatus(statusListCopy);

      resultWithStatus.put("training", training);
      resultWithStatus.put("result_status", result_status);
    }

    return resultWithStatus;
  }




  /**
   * @หาTrainingด้วยUserId
   */
  public List<Map<String, Object>> findTrainingsByUserId(Long userId) {
    String jpql = "SELECT t FROM Training t JOIN FETCH t.status s WHERE t.user.id = :id";

    TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);

    query.setParameter("id", userId);
    List<Training> resultList = query.getResultList();

    Map<Long, Training> uniqueTrainings = new HashMap<>();

    for (Training training : resultList) {
      uniqueTrainings.putIfAbsent(training.getId(), training);
    }

    List<Training> uniqueTrainingList = new ArrayList<>(uniqueTrainings.values());

    return calculateTrainingResultStatus(uniqueTrainingList);
  }



  /**
   * @หาTrainingด้วยApproveId
   */

  public List<Map<String, Object>> findTrainingsByApprove1Id(Long approve1Id) {
    String jpql =
      "SELECT DISTINCT t.id, action, action_date, date_save, day,budget," + //
      "approve1_id,user_id,active " + //
      "FROM status s " + //
      "JOIN training t ON training_id = t.id " + //
      "WHERE s.approve_id = :id and active != 0";

    NativeQuery<Training> query = (NativeQuery<Training>) entityManager.createNativeQuery(
      jpql,
      Training.class
    );

    query.setParameter("id", approve1Id);
    List<Training> resultList = (List<Training>) query.getResultList();

    return calculateTrainingResultStatus(resultList, approve1Id, 2);
  }

  /**
   * @หาTrainingทั้งหมด
   */
  public List<Map<String, Object>> findAllTraining() {
    String jpql = "SELECT DISTINCT t FROM Training t JOIN FETCH t.status " +
            "ORDER BY t.id";

    TypedQuery<Training> query = entityManager.createQuery(jpql, Training.class);
    List<Training> trainingList = query.getResultList();

    return calculateTrainingResultStatus(trainingList);
  }


  /**
   * @หาTrainingด้วยPersonnelId
   */

   public List<Map<String, Object>> findTrainingByPersonnelId(Long approve1Id) {
    String jpql =
      "SELECT DISTINCT t.id, action, action_date, date_save, day,budget," + //
      "approve1_id,user_id,active " + //
      "FROM status s " + //
      "JOIN training t ON training_id = t.id " + //
      "WHERE (active = 3 and approve_id = :id) or (active = 3 and approve_id is null)";

    NativeQuery<Training> query = (NativeQuery<Training>) entityManager.createNativeQuery(
      jpql,
      Training.class
    );

    query.setParameter("id", approve1Id);
    List<Training> resultList = (List<Training>) query.getResultList();

    return calculateTrainingResultStatus(resultList, approve1Id, 3);
  }


  /**
   * @OutputForTraining1
   */

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

      List<Status> statusListCopy = new ArrayList<>(training.getStatus());
      statusListCopy.sort(Comparator.comparing(Status::getId));
      training.setStatus(statusListCopy);

      Map<String, Object> resultWithStatus = new HashMap<>();
      resultWithStatus.put("training", training);
      resultWithStatus.put("result_status", result_status);
      resultWithStatusList.add(resultWithStatus);
    }

    return resultWithStatusList;
  }

  /**
   * @OutputForTraining2
   */

   public static List<Map<String, Object>> calculateTrainingResultStatus(
    List<Training> trainingList,
    long approve1Id,
    int active
) {
    List<Map<String, Object>> resultWithStatusList = new ArrayList<>();

    for (Training training : trainingList) {
        int approvedCount = 0;
        int disapprovedCount = 0;
        int cancelApprovedCount = 0;
        String resultStatus = "รอประเมิน";
        String isDo = "รอประเมิน";
        String isDoResult = "ไม่";
        int count = 0;

        
        List<Status> uniqueStatusList = removeDuplicateStatus(training.getStatus());

        for (Status status : uniqueStatusList) {
            if (status.getStatus() != null) {
                if ("อนุมัติ".equals(status.getStatus().toString())) {
                    approvedCount++;
                } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
                    disapprovedCount++;
                } else if ("ยกเลิก".equals(status.getStatus().toString())) {
                    cancelApprovedCount++;
                }
            }

            if (status.getActive() == active && status.getApproveId().getId() == approve1Id) {
                if ("อนุมัติ".equals(status.getStatus().toString())) {
                    isDo = "อนุมัติ";
                } else if ("ไม่อนุมัติ".equals(status.getStatus().toString())) {
                    isDo = "ไม่อนุมัติ";
                } else if ("ยกเลิก".equals(status.getStatus().toString())) {
                    isDo = "ยกเลิก";
                }
            }
            count++;
        }

        if (count == 3) {
        if (approvedCount == 3) {
          resultStatus = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
          resultStatus = "ไม่อนุมัติ";
        } else if (cancelApprovedCount >= 1) {
          resultStatus = "ยกเลิก";
        } else {
          resultStatus = "รอประเมิน";
        }
      } else {
        if (approvedCount == 2) {
          resultStatus = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
          resultStatus = "ไม่อนุมัติ";
        } else if (cancelApprovedCount >= 1) {
          resultStatus = "ยกเลิก";
        } else {
          resultStatus = "รอประเมิน";
        }
      }

        if (resultStatus.equals("อนุมัติ") && training.getApprove1().getId() == approve1Id) {
            isDoResult = "ใช่";
        }

        if (!resultStatus.equals("ยกเลิก")) {
            Map<String, Object> resultWithStatus = new HashMap<>();
            resultWithStatus.put("training", training);
            resultWithStatus.put("result_status", resultStatus);
            resultWithStatus.put("isDo", isDo);
            resultWithStatus.put("isDoResult", isDoResult);
            resultWithStatusList.add(resultWithStatus);
        }
    }

    return resultWithStatusList;
}

public static List<Status> removeDuplicateStatus(List<Status> statusList) {
  Set<Long> statusIds = new HashSet<>();
  List<Status> uniqueStatusList = new ArrayList<>();
  for (Status status : statusList) {
      if (!statusIds.contains(status.getId())) {
          System.out.println(status.getId());
          uniqueStatusList.add(status);
          statusIds.add(status.getId());
      }
      else{
        continue;
      }
      System.out.println(statusIds);
  }

  return uniqueStatusList;
}


  /**
   * @หาtrainingด้วยName,Position,Department,StartDate,Enddate,CourseName
   */

  public Object searchTraining(
    String name,
    String position,
    String department,
    Date startDate,
    Date endDate,
    String courseName
  ) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = builder.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);


    List<Predicate> predicates = new ArrayList<>();

    if (name != null) {
      Join<Training, User> userJoin = root.join("user");
      Expression<String> fullName = builder.concat(
        builder.concat(builder.lower(userJoin.get("firstname")), " "),
        builder.lower(userJoin.get("lastname"))
      );
      predicates.add(
        builder.like(builder.lower(fullName), "%" + name.toLowerCase() + "%")
      );
    }

    if (position != null) {
      Join<Training, User> userJoin = root.join("user");
      Join<User, Position> positionJoin = userJoin.join("position");
      predicates.add(
        builder.like(
          builder.lower(positionJoin.get("positionName")),
          "%" + position.toLowerCase() + "%"
        )
      );
    }

    if (department != null) {
      Join<Training, User> userJoin = root.join("user");
      Join<User, Department> departmentJoin = userJoin.join("department");
      predicates.add(
        builder.like(
          builder.lower(departmentJoin.get("deptName")),
          "%" + department.toLowerCase() + "%"
        )
      );
    }

    if (startDate != null) {
      Join<Training, Course> courseJoin = root.join("courses");
      predicates.add(builder.equal(courseJoin.get("startDate"), startDate));
    }

    if (endDate != null) {
      Join<Training, Course> courseJoin = root.join("courses");
      predicates.add(builder.equal(courseJoin.get("endDate"), endDate));
    }

    if (courseName != null) {
      Join<Training, Course> courseJoin = root.join("courses");
      predicates.add(
        builder.like(
          builder.lower(courseJoin.get("courseName")),
          "%" + courseName.toLowerCase() + "%"
        )
      );
    }

    if (
      name == null &&
      position == null &&
      department == null &&
      startDate == null &&
      endDate == null &&
      courseName == null
    ) {
      return "ไม่พบรายการที่ต้องการค้นหา";
    }

    query.where(predicates.toArray(new Predicate[0]));
    root.fetch("status", JoinType.INNER);
    query.distinct(true);
    List<Training> trainings = entityManager.createQuery(query).getResultList();

    if (trainings.isEmpty()) {
      return "ไม่พบรายการที่ต้องการค้นหา";
    }

    List<Map<String, Object>> results = new ArrayList<>();
    for (Training training : trainings) {
      Map<String, Object> result = new HashMap<>();
      result.put("training", training);
      results.add(result);
    }

    return results;
  }

  public static String convertByteToBase64(byte[] imageBytes) {
    return java.util.Base64.getEncoder().encodeToString(imageBytes);
  }


  public String printReport(Long trainId) {
    try {
//      Training training_id = trainingRepository.findById(trainId)
//              .orElseThrow(() -> new RuntimeException("TrainId not found: " + trainId));

      Training training_id = findByTrainingIdByMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(trainId);


      if (training_id == null) {
        return null;
      }
      List<Map<String, Object>> dataList = new ArrayList<>();

      Map<String, Object> params = new HashMap<>();
      String imageBase64Ap1 = convertByteToBase64(training_id.getStatus().get(0).getApproveId().getSignature().getImage());
      String imageBase64Ap2 = convertByteToBase64(training_id.getStatus().get(1).getApproveId().getSignature().getImage());
      String imageBase64Ap3 = convertByteToBase64(training_id.getStatus().get(2).getApproveId().getSignature().getImage());

      params.put("dept_code", training_id.getUser().getDepartment().getDeptCode());
      params.put("dept_name", training_id.getUser().getDepartment().getDeptName());
      params.put("date_save", training_id.getDateSave());

      params.put("course_name", training_id.getCourses().get(0).getCourseName());
      params.put("objective", training_id.getCourses().get(0).getObjective());
      params.put("start_date",training_id.getCourses().get(0).getStartDate());
      params.put("end_date",training_id.getCourses().get(0).getEndDate());
      params.put("price", training_id.getCourses().get(0).getPrice());
      params.put("institute", training_id.getCourses().get(0).getInstitute());
      params.put("place", training_id.getCourses().get(0).getPlace());
      //checkbox budget

      params.put("emp_code", training_id.getUser().getEmpCode());
      params.put("firstname", training_id.getUser().getFirstname());
      params.put("lastname", training_id.getUser().getLastname());
      params.put("position", training_id.getUser().getPosition().getPositionName());

      //approve1
      params.put("imageBase64Ap1", imageBase64Ap1);
      params.put("positionAp1", training_id.getStatus().get(0).getApproveId().getPosition().getPositionName());
      //approve2
      params.put("imageBase64Ap2", imageBase64Ap2);
      params.put("positionAp2", training_id.getStatus().get(1).getApproveId().getPosition().getPositionName());
      //approve3
      params.put("imageBase64Ap3", imageBase64Ap3);

      params.put("action", training_id.getAction());
      params.put("actionDate", training_id.getActionDate());

      dataList.add(params);

      // Load the JasperReport from a JRXML file
      InputStream reportInput = UserService.class.getClassLoader().getResourceAsStream("report/OF1-report.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(reportInput);

      // Create a JRDataSource from the user data
      JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);

      // Fill the report with data
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

      // Export the report to PDF
      byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

      // Convert the byte array to Base64
      return Base64.encodeBase64String(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * @เช็คNullของTraining1
   */
  public boolean isTrainingNull(CreateTrainingRequest request) {
    return (
      request == null ||
      request.getDateSave() == null ||
      request.getDateSave().toString().isEmpty()
    );
  }

  /**
   * @เช็คNullของTraining2
   */
  public boolean isEditTrainingNull1(EditTrainingSection1Request request) {
    return (
      request == null ||
      request.getDateSave() == null ||
      request.getDateSave().toString().isEmpty()
    );
  }

  /**
   * @เช็คNullของTraining3
   */
  public boolean isEditTrainingNull2(EditTrainingSection2Request request) {
    return (
      request == null ||
      request.getResult1() == null ||
      request.getResult1().isEmpty() ||
      request.getResult2() == null ||
      request.getResult2().isEmpty() ||
      request.getResult3() == null ||
      request.getResult3().isEmpty() ||
      request.getResult4() == null ||
      request.getResult4().isEmpty() ||
      request.getResult5() == null ||
      request.getResult5().isEmpty() ||
      request.getResult6() == null ||
      request.getResult6().isEmpty() ||
      request.getResult7() == null ||
      request.getResult7().isEmpty() ||
      request.getResult() == null ||
      request.getResult().isEmpty()
    );
  }
  /**
   * @changeApprover
   */

   private void changeApprover(
    EditTrainingSection1Request editTraining,
    long trainingId
  ) {

      String sql = "DELETE FROM Status WHERE training_id = :trainingId";
       entityManager
      .createNativeQuery(sql)
      .setParameter("trainingId", trainingId)
      .executeUpdate();
    
    User approve1 = userRepository
      .findById(editTraining.getApprove1_id())
      .orElseThrow(() ->
        new RuntimeException(
          "Approve1Id not found: " + editTraining.getApprove1_id()
        )
      );
     
    User approve3 = userRepository
      .findById(Long.valueOf(3))
      .orElseThrow(() ->
        new RuntimeException(
          "Approve1Id not found: " + editTraining.getApprove1_id()
        )
      );

   
    Role vicePresidentRole = approve1
      .getRoles()
      .stream()
      .filter(role -> role.getRole().equals(Roles.VicePresident))
      .findFirst()
      .orElse(null);

//    Training training = trainingRepository
//      .findById(trainingId)
//      .orElseThrow(() ->
//        new RuntimeException("TrainingId not found: " + trainingId)
//      );

    Training training_id = findByTrainingIdByMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(trainingId);

    if (vicePresidentRole != null) {
      Status status1 = Status
        .builder()
        .status(null)
        .training(training_id)
        .approveId(approve1)
        .active(1)
        .build();

      Status status2 = Status
        .builder()
        .status(null)
        .training(training_id)
        .active(0)
        .build();

      statusRepository.save(status1);
      statusRepository.save(status2);
      training_id.getStatus().add(status1);
      training_id.getStatus().add(status2);
    } else {


      Status status1 = Status
        .builder()
        .status(null)
        .training(training_id)
        .approveId(approve1)
        .active(1)
        .build();

      Status status2 = Status
        .builder()
        .status(null)
        .training(training_id)
        .approveId(approve3)
        .active(0)
        .build();

      Status status3 = Status
        .builder()
        .status(null)
        .training(training_id)
        .active(0)
        .build();

      statusRepository.save(status1);
      statusRepository.save(status2);
      statusRepository.save(status3);
      training_id.getStatus().add(status1);
      training_id.getStatus().add(status2);
      training_id.getStatus().add(status3);
    }
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
