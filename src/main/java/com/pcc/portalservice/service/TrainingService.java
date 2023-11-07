package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.InputStream;
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
  private final ResultRepository resultRepository;
  private final DepartmentRepository departmentRepository;
  private final SectorRepository sectorRepository;

  private final EntityManager entityManager;

  /**
   * @สร้างTraining
   */
  public Training createTraining(CreateTrainingRequest createTrainingRequest)
      throws ParseException {
    User user = userRepository
        .findById(createTrainingRequest.getUserId())
        .orElseThrow(() -> new RuntimeException(
            "Approve1Id not found: " + createTrainingRequest.getApprove1_id()));

    User approve1 = userRepository
        .findById(createTrainingRequest.getApprove1_id())
        .orElseThrow(() -> new RuntimeException(
            "Approve1Id not found: " + createTrainingRequest.getApprove1_id()));
    Course course = courseRepository
        .findById(createTrainingRequest.getCourseId())
        .orElseThrow(() -> new RuntimeException(
            "CourseId not found: " + createTrainingRequest.getCourseId()));

    Role vicePresidentRole = approve1
        .getRoles()
        .stream()
        .filter(role -> role.getRole().equals(Roles.VicePresident))
        .findFirst()
        .orElse(null);

    Date startDate = course.getStartDate();
    Date endDate = course.getEndDate();
    long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
    int daysDifference = (int) (differenceInMilliseconds / (1000 * 60 * 60 * 24)) +
        1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date actionDateFormat = dateFormat.parse(
        createTrainingRequest.getActionDate());

    Training training = Training
        .builder()
        .user(user)
        .dateSave(new Date())
        .day(daysDifference)
        .courses(Arrays.asList(course))
        .budget(createTrainingRequest.getBudget())
        .action(createTrainingRequest.getAction())
        .actionDate(actionDateFormat)
        // .action(null)
        // .actionDate(null)
        .approve1(approve1)
        .build();

    if (training.getResult() == null) {
      training.setResult(new ArrayList<>());
    }
    Result result = Result
        .builder()
        .training(training)
        // .evaluator(approve1)
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

      statusRepository.save(status1);
      training.getStatus().add(status1);
    } else {
      User ap1 = userRepository
          .findById(Long.valueOf(3))
          .orElseThrow(() -> new RuntimeException(
              "Approve1Id not found: " + createTrainingRequest.getApprove1_id()));
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

      statusRepository.save(status1);
      statusRepository.save(status2);
      training.getStatus().add(status1);
      training.getStatus().add(status2);
    }

    Training savedTraining = trainingRepository.save(training);
    return savedTraining;
  }

  /**
   * @EditTrainingSection1
   */
  public Training editTrainingSection1(
      Long trainingId,
      EditTrainingSection1Request editTraining) throws ParseException {
    // Training training_id = trainingRepository
    // .findById(trainingId)
    // .orElseThrow(() ->
    // new RuntimeException("TrainingId not found: " + trainingId)
    // );
    Training training_id = findByTrainingId(trainingId);

    Course course_id = courseRepository
        .findById(editTraining.getCourseId())
        .orElseThrow(() -> new RuntimeException(
            "CourseId not found: " + editTraining.getCourseId()));
    User user_id = userRepository
        .findById(editTraining.getUserId())
        .orElseThrow(() -> new RuntimeException("UserId not found: " + editTraining.getUserId()));
    User approve1_id = userRepository
        .findById(editTraining.getApprove1_id())
        .orElseThrow(() -> new RuntimeException(
            "Approve1Id not found: " + editTraining.getApprove1_id()));
    if (editTraining.getApprove1_id() != training_id.getApprove1().getId()) {
      System.out.println(editTraining.getApprove1_id());
      System.out.println(training_id.getApprove1().getId());
      changeApprover(editTraining, trainingId);
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date actionDateFormat = dateFormat.parse(editTraining.getActionDate());

    training_id.setUser(user_id);
    training_id.setDateSave(new Date());
    training_id.getCourses().clear();
    training_id.getCourses().add(course_id);
    training_id.setAction(editTraining.getAction());
    training_id.setActionDate(actionDateFormat);
    training_id.setApprove1(approve1_id);
    training_id.setBudget(editTraining.getBudget());

    Training updatedTraining = trainingRepository.save(training_id);
    return updatedTraining;
  }

 

  /**
   * @EditTrainingSection2
   */
  public Result editTrainingSection2(
      Long resultId,
      EditTrainingSection2Request editTraining) throws ParseException {
    Result result_id = resultRepository
        .findById(resultId)
        .orElseThrow(() -> new RuntimeException("ResultId not found: " + resultId));

    // result_id.setEvaluator(evaluator_id);
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
    result_id.setEvaluationDate(new Date());


    Result updatedTraining = resultRepository.save(result_id);
    return updatedTraining;
  }

  /**
   * @SetStatus
   */
  public Training setStatusToTraining(
      Long trainingId,
      Long approveId,
      StatusApprove statusApprove) {
    Training training_id = findByTrainingId(trainingId);

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
            existingStatus.setApprovalDate(new Date());
            statusRepository.save(existingStatus);

        Optional<Status> updateStatusNext = training_id
            .getStatus()
            .stream()
            .filter(status -> trainingId.equals(status.getTraining().getId()) &&
                status.getActive() != 2)
            .findFirst();

        if (updateStatusNext.isPresent()) {
          if (updateStatusNext.get().getApproveId() != null) {
            Status nextStatus = updateStatusNext.get();
            nextStatus.setActive(1);
            statusRepository.save(nextStatus);
          } 
        }
      } else {
        Status existingStatus = optionalStatus.get();
        existingStatus.setStatus(statusApprove);
        existingStatus.setApprovalDate(new Date());
        statusRepository.save(existingStatus);
      }
    }

    return trainingRepository.save(training_id);
  }

  public Training setCancelToTraining(Long trainingId) {
    String jpql = "UPDATE public.status SET active=2 ,status='ยกเลิก' WHERE training_id = :training_id ;";
    NativeQuery<Training> query = (NativeQuery<Training>) entityManager.createNativeQuery(
        jpql,
        Training.class);

    query.setParameter("training_id", trainingId);

    query.executeUpdate();

    return null;
  }

  /**
   * @หาTrainingด้วยId
   */

  public Training findByTrainingId(Long id) {
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
      List<Status> statusListCopy = new ArrayList<>(
          trainingList.get(0).getStatus());
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

      if (count == 2) {
        if (approvedCount == 2) {
          result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
          result_status = "ไม่อนุมัติ";
        } else if (cancalapprovedCount >= 1) {
          result_status = "ยกเลิก";
        } else {
          result_status = "รอประเมิน";
        }
      } else {
        if (approvedCount == 1) {
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

    TypedQuery<Training> query = entityManager.createQuery(
        jpql,
        Training.class);

    query.setParameter("id", userId);
    List<Training> resultList = query.getResultList();

    Map<Long, Training> uniqueTrainings = new HashMap<>();

    for (Training training : resultList) {
      uniqueTrainings.putIfAbsent(training.getId(), training);
    }

    List<Training> uniqueTrainingList = new ArrayList<>(
        uniqueTrainings.values());

    return calculateTrainingResultStatus(uniqueTrainingList);
  }

  /**
   * @หาTrainingด้วยApproveId
   */

  public List<Map<String, Object>> findTrainingsByApprove1Id(Long approve1Id) {
    String jpql = "SELECT DISTINCT t.id, action, action_date, date_save, day,budget," + //
        "approve1_id,user_id,active " + //
        "FROM status s " + //
        "JOIN training t ON training_id = t.id " + //
        "WHERE s.approve_id = :id and active != 0";

    NativeQuery<Training> query = (NativeQuery<Training>) entityManager.createNativeQuery(
        jpql,
        Training.class);

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

    TypedQuery<Training> query = entityManager.createQuery(
        jpql,
        Training.class);
    List<Training> trainingList = query.getResultList();

    return calculateTrainingResultStatus(trainingList);
  }

  /**
   * @หาTrainingด้วยPersonnelId
   */

  public List<Map<String, Object>> findTrainingByPersonnelId(Long approve1Id) {
    String jpql = "SELECT DISTINCT t.id, action, action_date, date_save, day,budget," + //
        "approve1_id,user_id,active " + //
        "FROM status s " + //
        "JOIN training t ON training_id = t.id " + //
        "WHERE (active = 3 and approve_id = :id) or (active = 3 and approve_id is null)";

    NativeQuery<Training> query = (NativeQuery<Training>) entityManager.createNativeQuery(
        jpql,
        Training.class);

    query.setParameter("id", approve1Id);
    List<Training> resultList = (List<Training>) query.getResultList();

    return calculateTrainingResultStatus(resultList, approve1Id, 3);
  }

  /**
   * @OutputForTraining1
   */

  public static List<Map<String, Object>> calculateTrainingResultStatus(
      List<Training> trainingList) {
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

      if (count == 2) {
        if (approvedCount == 2) {
          result_status = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
          result_status = "ไม่อนุมัติ";
        } else if (cancalapprovedCount >= 1) {
          result_status = "ยกเลิก";
        } else {
          result_status = "รอประเมิน";
        }
      } else {
        if (approvedCount == 1) {
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
      int active) {
    List<Map<String, Object>> resultWithStatusList = new ArrayList<>();

    for (Training training : trainingList) {
      int approvedCount = 0;
      int disapprovedCount = 0;
      int cancelApprovedCount = 0;
      String resultStatus = "รอประเมิน";
      String isDo = "รอประเมิน";
      String isDoResult = "ไม่";
      int count = 0;

      List<Status> uniqueStatusList = removeDuplicateStatus(
          training.getStatus());

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

        if (status.getActive() == active &&
            status.getApproveId() != null &&
            status.getApproveId().getId() == approve1Id) {
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

      if (count == 2) {
        if (approvedCount == 2) {
          resultStatus = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
          resultStatus = "ไม่อนุมัติ";
        } else if (cancelApprovedCount >= 1) {
          resultStatus = "ยกเลิก";
        } else {
          resultStatus = "รอประเมิน";
        }
      } else {
        if (approvedCount == 1) {
          resultStatus = "อนุมัติ";
        } else if (disapprovedCount >= 1) {
          resultStatus = "ไม่อนุมัติ";
        } else if (cancelApprovedCount >= 1) {
          resultStatus = "ยกเลิก";
        } else {
          resultStatus = "รอประเมิน";
        }
      }

      if (resultStatus.equals("อนุมัติ") &&
          training.getApprove1().getId() == approve1Id) {
        isDoResult = "ใช่";
      }

      if (!resultStatus.equals("ยกเลิก")) {
        training.setStatus(null);
        List<Status> statusListCopy = new ArrayList<>(uniqueStatusList);
        statusListCopy.sort(Comparator.comparing(Status::getId));
        training.setStatus(statusListCopy);
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
      } else {
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
      String courseName) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = builder.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);

    List<Predicate> predicates = new ArrayList<>();

    if (name != null) {
      Join<Training, User> userJoin = root.join("user");
      Expression<String> fullName = builder.concat(
          builder.concat(builder.lower(userJoin.get("firstname")), " "),
          builder.lower(userJoin.get("lastname")));
      predicates.add(
          builder.like(builder.lower(fullName), "%" + name.toLowerCase() + "%"));
    }

    if (position != null) {
      Join<Training, User> userJoin = root.join("user");
      Join<User, Position> positionJoin = userJoin.join("position");
      predicates.add(
          builder.like(
              builder.lower(positionJoin.get("positionName")),
              "%" + position.toLowerCase() + "%"));
    }

    if (department != null) {
      Join<Training, User> userJoin = root.join("user");
      Join<User, Department> departmentJoin = userJoin.join("department");
      predicates.add(
          builder.like(
              builder.lower(departmentJoin.get("deptName")),
              "%" + department.toLowerCase() + "%"));
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
              "%" + courseName.toLowerCase() + "%"));
    }

    if (name == null &&
        position == null &&
        department == null &&
        startDate == null &&
        endDate == null &&
        courseName == null) {
      return "ไม่พบรายการที่ต้องการค้นหา";
    }

    query.where(predicates.toArray(new Predicate[0]));
    root.fetch("status", JoinType.INNER);
    query.distinct(true);
    List<Training> trainings = entityManager.createQuery(query).getResultList();

    if (trainings.isEmpty()) {
      return "ไม่พบรายการที่ต้องการค้นหา";
    }
    return calculateTrainingResultStatus(trainings);
  }

  public static String convertByteToBase64(byte[] imageBytes) {
    return java.util.Base64.getEncoder().encodeToString(imageBytes);
  }

  public String printReport(
      Long trainId,
      Long userId1,
      Long userId2,
      Long userId3,
      Long userId4) {
    Training training_id = findByTrainingId(trainId);

    try {
      List<Map<String, Object>> dataList = new ArrayList<>();

      Map<String, Object> params = new HashMap<>();

      if (userId1 == null) {
        if (userId4 != null) {
          User evaluator4 = userRepository.findById(userId4).orElse(null);
          params.put(
              "approve1",
              convertByteToBase64(evaluator4.getSignature().getImage()));
        } else if (userId1 == null && userId4 == null) {
          User evaluator2 = userRepository.findById(userId2).orElse(null);
          params.put(
              "approve1",
              convertByteToBase64(evaluator2.getSignature().getImage()));
        } else if (userId1 == null && userId2 == null && userId4 == null) {
          User evaluator3 = userRepository.findById(userId3).orElse(null);
          params.put(
              "approve1",
              convertByteToBase64(evaluator3.getSignature().getImage()));
        }
      } else if (userId1 != null) {
        User evaluator1 = userRepository.findById(userId1).orElse(null);
        params.put(
            "approve1",
            convertByteToBase64(evaluator1.getSignature().getImage()));
      }

      if (userId1 != null) {
        User user_id1 = userRepository.findById(userId1).orElse(null);
        if (user_id1 != null) {
          params.put(
              "imageBase64User1",
              convertByteToBase64(user_id1.getSignature().getImage()));
          params.put("positionAp1", user_id1.getPosition().getPositionName());
          params.put(
              "date_saveUser1",
              training_id.getStatus().get(0).getApprovalDate());
        }
      }
      if (userId2 != null) {
        User user_id2 = userRepository.findById(userId2).orElse(null);
        if (user_id2 != null) {
          params.put(
              "imageBase64User2",
              convertByteToBase64(user_id2.getSignature().getImage()));
          params.put("positionAp2", user_id2.getPosition().getPositionName());
          if (training_id.getStatus().size() == 1) {
            params.put(
                "date_saveUser2",
                training_id.getStatus().get(0).getApprovalDate());
          } else if (training_id.getStatus().size() > 1) {
            params.put(
                "date_saveUser2",
                training_id.getStatus().get(1).getApprovalDate());
          }
        }
      }
      if (userId3 != null) {
        User user_id3 = userRepository.findById(userId3).orElse(null);
        if (user_id3 != null) {
          params.put(
              "imageBase64User3",
              convertByteToBase64(user_id3.getSignature().getImage()));
          if (training_id.getStatus().size() > 1) {
            params.put(
                "date_saveUser3",
                training_id.getStatus().get(1).getApprovalDate());
          } else if (training_id.getStatus().size() == 1) {
            params.put(
                "date_saveUser3",
                training_id.getStatus().get(0).getApprovalDate());
          }
        }
      }

      if (userId4 != null) {
        User user_id4 = userRepository.findById(userId4).orElse(null);
        if (user_id4 != null) {
          params.put(
              "imageBase64User4",
              convertByteToBase64(user_id4.getSignature().getImage()));
        }
      }

      params.put(
          "dept_code",
          training_id.getUser().getDepartment().getDeptCode());
      params.put(
          "sector_name",
          training_id.getUser().getSector().getSectorName());
      params.put(
          "dept_name",
          training_id.getUser().getDepartment().getDeptName());
      params.put("date_save", training_id.getDateSave());
      params.put(
          "course_name",
          training_id.getCourses().get(0).getCourseName());
      params.put("objective", training_id.getCourses().get(0).getObjective());
      params.put("start_date", training_id.getCourses().get(0).getStartDate());
      params.put("end_date", training_id.getCourses().get(0).getEndDate());
      params.put("time", training_id.getCourses().get(0).getTime());
      params.put("note", training_id.getCourses().get(0).getNote());

      params.put("price", training_id.getCourses().get(0).getPrice());
      params.put("institute", training_id.getCourses().get(0).getInstitute());
      params.put("place", training_id.getCourses().get(0).getPlace());
      params.put("budget", training_id.getBudget());

      params.put("emp_code", training_id.getUser().getEmpCode());
      params.put("firstname", training_id.getUser().getFirstname());
      params.put("lastname", training_id.getUser().getLastname());
      params.put(
          "position",
          training_id.getUser().getPosition().getPositionName());

      params.put("action", training_id.getAction());
      params.put("actionDate", training_id.getActionDate());

      // section2
      params.put("app_name", training_id.getApprove1().getFirstname());
      params.put("app_lastname", training_id.getApprove1().getLastname());
      params.put(
          "app_position",
          training_id.getApprove1().getPosition().getPositionName());
      params.put(
          "app_dept_name",
          training_id.getApprove1().getDepartment().getDeptName());
      params.put(
          "app_sector_name",
          training_id.getApprove1().getSector().getSectorName());
      params.put("result1", training_id.getResult().get(0).getResult1());
      params.put("result2", training_id.getResult().get(0).getResult2());
      params.put("result3", training_id.getResult().get(0).getResult3());
      params.put("result4", training_id.getResult().get(0).getResult4());
      params.put("result5", training_id.getResult().get(0).getResult5());
      params.put("result6", training_id.getResult().get(0).getResult6());
      params.put("result7", training_id.getResult().get(0).getResult7());
      params.put("comment", training_id.getResult().get(0).getComment());
      params.put("cause", training_id.getResult().get(0).getCause());
      params.put("plan", training_id.getResult().get(0).getPlan());
      params.put("result", training_id.getResult().get(0).getResult());
      params.put(
          "date_saveEvaluation",
          training_id.getResult().get(0).getEvaluationDate());
      dataList.add(params);

      // Load the JasperReport from a JRXML file
      InputStream reportInput = UserService.class.getClassLoader()
          .getResourceAsStream("report/OF1-report.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(
          reportInput);

      // Create a JRDataSource from the user data
      JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);

      // Fill the report with data
      JasperPrint jasperPrint = JasperFillManager.fillReport(
          jasperReport,
          params,
          dataSource);

//      File tempPdfFile = File.createTempFile("report", ".pdf");
//
//      // Export the report to the temporary PDF file
//      JasperExportManager.exportReportToPdfFile(jasperPrint, tempPdfFile.getAbsolutePath());
//
//      return tempPdfFile;

      // Export the report to PDF
      byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

      // Convert the byte array to Base64
      return Base64.encodeBase64String(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public String printReportHistoryTraining(
      String startDate,
      String endDate,
      Long deptID,
      Long sectorID
      //Long trainId
  ) throws Exception {
    //Training training_id = findByTrainingId(trainId);
    // Training user_id = findByTrainingId(deptID);
    // User user_id = fin
//    System.out.println("0");
//    Training department = findByTrainingId(deptID);

//      Training User = (Training) findAllTraining();
    LinkedHashMap<String, Object> ht = HistoryTraining(startDate, endDate, deptID, sectorID);
//    System.out.println("1");


      String spec = "report/SV1-training.jrxml";
      assert spec != null;
      System.out.println("0");
      if (spec == null) {
          throw new Exception("The spec variable is null");
      }
      //System.out.println("1");
    try {
      List<Map<String, Object>> dataList = new ArrayList<>();
      Map<String, Object> params = new HashMap<>();

      Optional<Sector> sector = sectorRepository.findById(sectorID);
      Optional<Department> depOptional = departmentRepository.findById(deptID);
      
      

      params.put("sector_name", sector.get().getSectorName());
      params.put("dept_name", depOptional.get().getDeptName());

      params.put("start_date", "2023-08-04");
      params.put("end_date", "2023-08-06");
//      params.put("sector", "1");
//      params.put("department", "2");
      // params.put("start_date",startDate);
      // params.put("end_date",endDate);
      //params.put("emp_code", );
      //params.put("dept_name", department.getUser().getTraining().getUser().getDepartment().getDeptName());
      //params.put("sector_name", sector.getUser().getTraining().getUser().getSector().getSectorName());
      //params.put("emp_code", User.getUser().getTraining().getUser().getEmpCode());
//      params.put("title", training_id.getUser().getTitle());
//      params.put("firstname", training_id.getUser().getFirstname());
//      params.put("lastname", training_id.getUser().getLastname());
//      for (int i = 0; i < dataList.size(); i++){
//      params.put("course_name", training_id.getUser().getTraining().getCourses().get(i).getCourseName());}
//      params.put("place", training_id.getUser().getTraining().getCourses().get(0).getPlace());
//      params.put("price", training_id.getUser().getTraining().getCourses().get(0).getPrice());
//      params.put("note", training_id.getUser().getTraining().getCourses().get(0).getNote());
//      params.put("start_date", training_id.getUser().getTraining().getCourses().get(0).getStartDate());
//      params.put("end_date", training_id.getUser().getTraining().getCourses().get(0).getEndDate());

      //params.put("emp_code", training_id.getUser().getEmpCode());

        //System.out.println("2");
      dataList.add(params);
        //System.out.println("3");
      // Load the JasperReport from a JRXML file
//      InputStream reportInput = UserService.class.getClassLoader()
//          .getResourceAsStream("SV1-training.jrxml");
//      JasperReport jasperReport = JasperCompileManager.compileReport(
//          reportInput);

////////////////////////////////////////////////////////////////////////////

      // Process the data in 'data' from 'ht' and add to params and dataList
      List<Map<String, Object>> data = (List<Map<String, Object>>) ht.get("data");
      for (Map<String, Object> userData : data) {
        // Create a new map for each user's data
        Map<String, Object> userParams = new HashMap<>();

        userParams.put("user_id", userData.get("user_id"));
        userParams.put("emp_code", userData.get("emp_code"));
        userParams.put("title", userData.get("title"));
        userParams.put("firstname", userData.get("firstname"));
        userParams.put("lastname", userData.get("lastname"));

        List<Map<String, Object>> courseList = (List<Map<String, Object>>) userData.get("course");
        // Process courses for the user
        for (int i = 0; i < courseList.size(); i++) {
          Map<String, Object> course = courseList.get(i);
          userParams.put("course_id" + i, course.get("course_id"));
          userParams.put("course_name" + i, course.get("course_name"));
          userParams.put("place" + i, course.get("place"));
          userParams.put("price" + i, course.get("price"));
          userParams.put("start_date" + i, course.get("start_date"));
          userParams.put("end_date" + i, course.get("end_date"));
          userParams.put("note" + i, course.get("note"));
        }

        dataList.add(userParams);
      }

///////////////////////////////////////////////////////////////////////////

        // Load the JRXML file
        InputStream reportInput = UserService.class.getClassLoader()
                .getResourceAsStream(spec);
        JasperReport jasperReport = JasperCompileManager.compileReport(
                reportInput);
        //System.out.println("4");
      // Create a JRDataSource from the user data
      JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);
        //System.out.println("5");
      // Fill the report with data
      JasperPrint jasperPrint = JasperFillManager.fillReport(
          jasperReport,
              params,
          dataSource);
        //System.out.println("6");
      // Export the report to PDF
      byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        //System.out.println("7");
      // Convert the byte array to Base64
      return Base64.encodeBase64String(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  

  public LinkedHashMap<String, Object> HistoryTraining(
      String startDate,
      String endDate,
      Long deptID,
      Long sectorID) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    System.out.println(startDate);
    System.out.println(endDate);
    try {
      Date parsedStartDate = dateFormat.parse(startDate);
      Date parsedEndDate = dateFormat.parse(endDate);
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> query = cb.createTupleQuery();
      Root<Training> trainingRoot = query.from(Training.class);
      query.multiselect(
          trainingRoot.get("user").get("id").alias("user_id"),
          trainingRoot.get("user").get("empCode").alias("emp_code"),
          trainingRoot.get("user").get("title").alias("title"),
          trainingRoot.get("user").get("firstname").alias("firstname"),
          trainingRoot.get("user").get("lastname").alias("lastname"),
          trainingRoot.join("courses").get("id").alias("course_id"),
          trainingRoot.join("courses").get("courseName").alias("course_name"),
          trainingRoot.join("courses").get("place").alias("place"),
          trainingRoot.join("courses").get("price").alias("price"),
          trainingRoot.join("courses").get("startDate").alias("start_date"),
          trainingRoot.join("courses").get("endDate").alias("end_date"),
          trainingRoot.join("courses").get("note").alias("note"));
          trainingRoot.join("courses").get("active").alias("active");
          trainingRoot.join("result").get("result").alias("result");
      Predicate startDatePredicate = cb.greaterThanOrEqualTo(trainingRoot.join("courses").get("startDate"), parsedStartDate);
      Predicate endDatePredicate = cb.lessThanOrEqualTo(trainingRoot.join("courses").get("endDate"), parsedEndDate);
      Predicate deptPredicate = cb.equal(trainingRoot.get("user").get("department").get("id"), deptID);
      Predicate sectorPredicate = cb.equal(trainingRoot.get("user").get("sector").get("id"), sectorID);
      Predicate cancelPredicate = cb.equal(trainingRoot.join("courses").get("active"),"ดำเนินการอยู่");
      Predicate passPredicate = cb.equal(trainingRoot.join("result").get("result"),"pass");
      query.where(
          cb.and(startDatePredicate, endDatePredicate, deptPredicate, sectorPredicate,cancelPredicate,passPredicate));
      query.orderBy(cb.asc(trainingRoot.get("user").get("id")));

      TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
      List<Tuple> resultListBudgetTraining = typedQuery.getResultList();
      LinkedHashMap<String, Object> result = new LinkedHashMap<>();
      List<LinkedHashMap<String, Object>> users = new ArrayList<>();
      LinkedHashMap<String, Object> currentUser = null;
      float totalAll = 0;

      for (Tuple row : resultListBudgetTraining) {
        if (currentUser == null || !currentUser.get("emp_code").equals(row.get("emp_code"))) {
          if (currentUser != null) {
            currentUser.put("total", totalAll);
          }
          currentUser = new LinkedHashMap<>();
          currentUser.put("user_id", row.get("user_id"));
          currentUser.put("emp_code", row.get("emp_code"));
          currentUser.put("title", row.get("title"));
          currentUser.put("firstname", row.get("firstname"));
          currentUser.put("lastname", row.get("lastname"));
          currentUser.put("course", new ArrayList<>());
          users.add(currentUser);
          totalAll = 0;
        }

        LinkedHashMap<String, Object> course = new LinkedHashMap<>();
        course.put("course_id", row.get("course_id"));
        course.put("course_name", row.get("course_name"));
        course.put("place", row.get("place"));
        course.put("price", row.get("price"));
        course.put("start_date", row.get("start_date"));
        course.put("end_date", row.get("end_date"));
        course.put("note", row.get("note"));
        ((List<LinkedHashMap<String, Object>>) currentUser.get("course")).add(course);

        totalAll += (Float) row.get("price");
      }

      if (currentUser != null) {
        currentUser.put("total", totalAll);
      }

      double totalAllValue = users.stream().mapToDouble(user -> (Float) user.get("total")).sum();
      result.put("total_All", totalAllValue);
      result.put("data", users);

      return result;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @เช็คNullของTraining1
   */
  public boolean isTrainingNull(CreateTrainingRequest request) {
    return (request == null ||
        request.getDateSave() == null ||
        request.getDateSave().toString().isEmpty());
  }

  /**
   * @เช็คNullของTraining2
   */
  public boolean isEditTrainingNull1(EditTrainingSection1Request request) {
    return (request == null ||
        request.getDateSave() == null ||
        request.getDateSave().toString().isEmpty());
  }

  /**
   * @เช็คNullของTraining3
   */
  public boolean isEditTrainingNull2(EditTrainingSection2Request request) {
    return (request == null ||
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
        request.getResult().isEmpty());
  }

  /**
   * @changeApprover
   */

  private void changeApprover(
      EditTrainingSection1Request editTraining,
      long trainingId) {
    System.out.println(trainingId);
    String jpql = "DELETE FROM Status s WHERE training_id = :training_id";
    entityManager
        .createQuery(jpql)
        .setParameter("training_id", trainingId)
        .executeUpdate();

    User approve1 = userRepository
        .findById(editTraining.getApprove1_id())
        .orElseThrow(() -> new RuntimeException(
            "Approve1Id not found: " + editTraining.getApprove1_id()));

    User approve3 = userRepository
        .findById(Long.valueOf(3))
        .orElseThrow(() -> new RuntimeException(
            "Approve1Id not found: " + editTraining.getApprove1_id()));

    Role vicePresidentRole = approve1
        .getRoles()
        .stream()
        .filter(role -> role.getRole().equals(Roles.VicePresident))
        .findFirst()
        .orElse(null);

    Training training_id = findByTrainingIdEdit(trainingId);
    if (training_id.getStatus() == null) {
      training_id.setStatus(new ArrayList<>());
    }
    if (vicePresidentRole != null) {
      Status status1 = Status
          .builder()
          .training(training_id)
          .status(null)
          .approveId(approve1)
          .active(1)
          .build();

      statusRepository.save(status1);

      training_id.getStatus().add(status1);
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

      statusRepository.save(status1);
      statusRepository.save(status2);

      training_id.getStatus().add(status1);
      training_id.getStatus().add(status2);
    }
  }

  public Training findByTrainingIdEdit(Long id) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = builder.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);
    query.where(builder.equal(root.get("id"), id));

    List<Training> trainingList = entityManager
        .createQuery(query)
        .getResultList();
    return trainingList.get(0);
  }
}
