package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
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
  private final DepartmentRepository departmentRepository;
  private final SectorRepository sectorRepository;

  private final EntityManager entityManager;

  private Long findDeptByUserID(Long id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

    Root<User> userRoot = criteriaQuery.from(User.class);
    Join<User, Department> departmentJoin = userRoot.join("departments");

    Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
    Root<Department> subqueryDepartmentRoot = subquery.from(Department.class);
    subquery.select(subqueryDepartmentRoot.get("id"));
    subquery.where(
      criteriaBuilder.equal(subqueryDepartmentRoot, departmentJoin)
    );

    criteriaQuery
      .select(departmentJoin.get("id"))
      .distinct(true)
      .where(
        criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("id"), id))
      );

    return entityManager.createQuery(criteriaQuery).getSingleResult();
  }

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
    EditTrainingSection1Request editTraining
  ) throws ParseException {
    // Training training_id = trainingRepository
    // .findById(trainingId)
    // .orElseThrow(() ->
    // new RuntimeException("TrainingId not found: " + trainingId)
    // );
    Training training_id = findByTrainingId(trainingId);

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
    if (editTraining.getApprove1_id() != training_id.getApprove1().getId()) {
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
    EditTrainingSection2Request editTraining
  ) throws ParseException {
    Result result_id = resultRepository
      .findById(resultId)
      .orElseThrow(() -> new RuntimeException("ResultId not found: " + resultId)
      );

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
    StatusApprove statusApprove
  ) {
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
    String jpql =
      "UPDATE public.status SET active=2 ,status='ยกเลิก' WHERE training_id = :training_id ;";
    NativeQuery<Training> query = (NativeQuery<Training>) entityManager.createNativeQuery(
      jpql,
      Training.class
    );

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
        trainingList.get(0).getStatus()
      );
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
    String jpql =
      "SELECT t FROM Training t JOIN FETCH t.status s WHERE t.user.id = :id";

    TypedQuery<Training> query = entityManager.createQuery(
      jpql,
      Training.class
    );

    query.setParameter("id", userId);
    List<Training> resultList = query.getResultList();

    Map<Long, Training> uniqueTrainings = new HashMap<>();

    for (Training training : resultList) {
      uniqueTrainings.putIfAbsent(training.getId(), training);
    }

    List<Training> uniqueTrainingList = new ArrayList<>(
      uniqueTrainings.values()
    );

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
    String jpql =
      "SELECT DISTINCT t FROM Training t JOIN FETCH t.status " +
      "ORDER BY t.id";

    TypedQuery<Training> query = entityManager.createQuery(
      jpql,
      Training.class
    );
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

      List<Status> uniqueStatusList = removeDuplicateStatus(
        training.getStatus()
      );

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

        if (
          status.getActive() == active &&
          status.getApproveId() != null &&
          status.getApproveId().getId() == approve1Id
        ) {
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

      if (
        resultStatus.equals("อนุมัติ") &&
        training.getApprove1().getId() == approve1Id
      ) {
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
        uniqueStatusList.add(status);
        statusIds.add(status.getId());
      } else {
        continue;
      }
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
    Long userId4
  ) {
    Training training_id = findByTrainingId(trainId);
    Optional<Department> departmentOptional = departmentRepository.findById(
      findDeptByUserID(training_id.getUser().getId())
    );

    try {
      List<Map<String, Object>> dataList = new ArrayList<>();

      Map<String, Object> params = new HashMap<>();

      if (userId1 == null) {
        if (userId4 != null) {
          User evaluator4 = userRepository.findById(userId4).orElse(null);
          params.put(
            "approve1",
            convertByteToBase64(evaluator4.getSignature().getImage())
          );
        } else if (userId1 == null && userId4 == null) {
          User evaluator2 = userRepository.findById(userId2).orElse(null);
          params.put(
            "approve1",
            convertByteToBase64(evaluator2.getSignature().getImage())
          );
        } else if (userId1 == null && userId2 == null && userId4 == null) {
          User evaluator3 = userRepository.findById(userId3).orElse(null);
          params.put(
            "approve1",
            convertByteToBase64(evaluator3.getSignature().getImage())
          );
        }
      } else if (userId1 != null) {
        User evaluator1 = userRepository.findById(userId1).orElse(null);
        params.put(
          "approve1",
          convertByteToBase64(evaluator1.getSignature().getImage())
        );
      }

      if (userId1 != null) {
        User user_id1 = userRepository.findById(userId1).orElse(null);
        if (user_id1 != null) {
          params.put(
            "imageBase64User1",
            convertByteToBase64(user_id1.getSignature().getImage())
          );
          params.put("positionAp1", user_id1.getPosition().getPositionName());
          params.put(
            "date_saveUser1",
            training_id.getStatus().get(0).getApprovalDate()
          );
        }
      }
      if (userId2 != null) {
        User user_id2 = userRepository.findById(userId2).orElse(null);
        if (user_id2 != null) {
          params.put(
            "imageBase64User2",
            convertByteToBase64(user_id2.getSignature().getImage())
          );
          params.put("positionAp2", user_id2.getPosition().getPositionName());
          if (training_id.getStatus().size() == 1) {
            params.put(
              "date_saveUser2",
              training_id.getStatus().get(0).getApprovalDate()
            );
          } else if (training_id.getStatus().size() > 1) {
            params.put(
              "date_saveUser2",
              training_id.getStatus().get(1).getApprovalDate()
            );
          }
        }
      }
      if (userId3 != null) {
        User user_id3 = userRepository.findById(userId3).orElse(null);
        if (user_id3 != null) {
          params.put(
            "imageBase64User3",
            convertByteToBase64(user_id3.getSignature().getImage())
          );
          if (training_id.getStatus().size() > 1) {
            params.put(
              "date_saveUser3",
              training_id.getStatus().get(1).getApprovalDate()
            );
          } else if (training_id.getStatus().size() == 1) {
            params.put(
              "date_saveUser3",
              training_id.getStatus().get(0).getApprovalDate()
            );
          }
        }
      }

      if (userId4 != null) {
        User user_id4 = userRepository.findById(userId4).orElse(null);
        if (user_id4 != null) {
          params.put(
            "imageBase64User4",
            convertByteToBase64(user_id4.getSignature().getImage())
          );
        }
      }

      params.put("dept_code", departmentOptional.get().getDeptCode());
      params.put(
        "sector_name",
        training_id.getUser().getSector().getSectorName()
      );
      params.put("dept_name", departmentOptional.get().getDeptName());
      params.put("date_save", training_id.getDateSave());
      params.put(
        "course_name",
        training_id.getCourses().get(0).getCourseName()
      );
      params.put("objective", training_id.getCourses().get(0).getObjective());
      params.put("start_date", training_id.getCourses().get(0).getStartDate());
      params.put("end_date", training_id.getCourses().get(0).getEndDate());
      params.put("time", training_id.getCourses().get(0).getTime());
      params.put("note", training_id.getCourses().get(0).getNote());

      params.put("price", training_id.getCourses().get(0).getPrice());
      params.put("institute", training_id.getCourses().get(0).getInstitute());
      params.put("place", training_id.getCourses().get(0).getPlace());
      params.put("budget", training_id.getBudget());
      params.put(
        "priceProject",
        training_id.getCourses().get(0).getPriceProject()
      );

      params.put("emp_code", training_id.getUser().getEmpCode());
      params.put("firstname", training_id.getUser().getFirstname());
      params.put("lastname", training_id.getUser().getLastname());
      params.put(
        "position",
        training_id.getUser().getPosition().getPositionName()
      );

      params.put("action", training_id.getAction());
      params.put("actionDate", training_id.getActionDate());

      // section2
      params.put("app_name", training_id.getApprove1().getFirstname());
      params.put("app_lastname", training_id.getApprove1().getLastname());
      params.put(
        "app_position",
        training_id.getApprove1().getPosition().getPositionName()
      );
      params.put("app_dept_name", departmentOptional.get().getDeptName());
      params.put(
        "app_sector_name",
        training_id.getApprove1().getSector().getSectorName()
      );
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
        training_id.getResult().get(0).getEvaluationDate()
      );
      dataList.add(params);

      // Load the JasperReport from a JRXML file
      InputStream reportInput =
        UserService.class.getClassLoader()
          .getResourceAsStream("report/OF1-report.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(
        reportInput
      );

      // Create a JRDataSource from the user data
      JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);

      // Fill the report with data
      JasperPrint jasperPrint = JasperFillManager.fillReport(
        jasperReport,
        params,
        dataSource
      );

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

  private static JRDataSource getDataSource(LinkedHashMap<String, Object> ht) {
    Float sumall = 0.0f;
    Collection<BeanHistroy> coll = new ArrayList<BeanHistroy>();
    List<Map<String, Object>> data = (List<Map<String, Object>>) ht.get("data");

    for (Map<String, Object> userData : data) {
      List<String> course_names = new ArrayList<>();
      List<String> course_places = new ArrayList<>();
      List<Float> course_prices = new ArrayList<>();
      List<String> course_priceProjects = new ArrayList<>();
      List<String> dates = new ArrayList<>();
      Float sums = 0.0f;

      Long id = (Long) userData.get("user_id");
      String name =
        userData.get("emp_code") +
        " " +
        userData.get("title") +
        " " +
        userData.get("firstname") +
        " " +
        userData.get("lastname");
      List<Map<String, Object>> courseList = (List<Map<String, Object>>) userData.get(
        "course"
      );
      for (int i = 0; i < courseList.size(); i++) {
        Map<String, Object> course = courseList.get(i);

        course_names.add(
          course.get("course_name") != null
            ? course.get("course_name").toString()
            : ""
        );
        course_places.add(
          course.get("place") != null ? course.get("place").toString() : ""
        );
        course_prices.add(
          course.get("price") != null ? (float) course.get("price") : 0.0f
        );
        String startDate = course.get("start_date") != null
          ? course.get("start_date").toString()
          : "";
        String endDate = course.get("end_date") != null
          ? course.get("end_date").toString()
          : "";

        String formattedStartDate = "";
        String formattedEndDate = "";

        if (!startDate.isEmpty() && !endDate.isEmpty()) {
          try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateObj = dateFormat.parse(startDate);
            Date endDateObj = dateFormat.parse(endDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(
              "dd/MM/yyyy",
              new Locale("TH", "th")
            );
            formattedStartDate = outputDateFormat.format(startDateObj);
            formattedEndDate = outputDateFormat.format(endDateObj);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }

        String dateCombined = formattedStartDate + " - " + formattedEndDate;

        dates.add(dateCombined);
        course_priceProjects.add(
          course.get("priceProject") != null
            ? course.get("priceProject").toString()
            : ""
        );

        sums += (float) course.get("price");
        sumall += (float) course.get("price");
      }
      course_names.add("\t\t\t");
      course_places.add("\t\t\t รวม");
      course_prices.add(sums);
      dates.add("\t\t\t");
      course_priceProjects.add("\t\t\t");

      coll.add(
        new BeanHistroy(
          course_names,
          course_places,
          course_prices,
          course_priceProjects,
          dates,
          sums,
          sumall,
          name,
          id
        )
      );
    }
    return new JRBeanCollectionDataSource(coll);
  }

  private static JRDataSource getDataSourceGeneric9(
    LinkedHashMap<String, Object> ht
  ) {
    Collection<BeanGeneric9> coll = new ArrayList<>();
    List<Map<String, Object>> data = (List<Map<String, Object>>) ht.get("data");

    for (Map<String, Object> userData : data) {
      List<String> course_names = new ArrayList<>();
      List<Float> results = new ArrayList<>();
      String name =
        userData.get("emp_code") +
        " " +
        userData.get("title") +
        " " +
        userData.get("firstname") +
        " " +
        userData.get("lastname");

      List<Map<String, Object>> courseList = (List<Map<String, Object>>) userData.get(
        "course"
      );
      for (Map<String, Object> course : courseList) {
        course_names.add(
          course.get("course_name") != null
            ? course.get("course_name").toString()
            : ""
        );
        results.add(
          course.get("result1") != null ? (float) course.get("result1") : 0.0f
        );
        results.add(
          course.get("result2") != null ? (float) course.get("result2") : 0.0f
        );
        results.add(
          course.get("result3") != null ? (float) course.get("result3") : 0.0f
        );
        results.add(
          course.get("result4") != null ? (float) course.get("result4") : 0.0f
        );
        results.add(
          course.get("result5") != null ? (float) course.get("result5") : 0.0f
        );
        results.add(
          course.get("result6") != null ? (float) course.get("result6") : 0.0f
        );
        results.add(
          course.get("result7") != null ? (float) course.get("result7") : 0.0f
        );
      }

      coll.add(new BeanGeneric9(course_names, results, name));
    }

    return new JRBeanCollectionDataSource(coll);
  }

  public String printReportHistoryTraining(
    String startDate,
    String endDate,
    Long deptID,
    Long sectorID
  ) {
    try {
      String spec = "report/histroy-training.jrxml";
      Map<String, Object> params = new HashMap<String, Object>();
      Optional<Sector> sector = sectorRepository.findById(sectorID);
      Optional<Department> depOptional = departmentRepository.findById(deptID);
      LinkedHashMap<String, Object> ht = HistoryTraining(
        startDate,
        endDate,
        deptID,
        sectorID
      );

      params.put("sector_name", sector.get().getSectorName());
      params.put("dept_name", depOptional.get().getDeptName());

      params.put(
        "startdate",
        new SimpleDateFormat("dd/MM/yyyy", new Locale("TH", "th"))
          .format(new SimpleDateFormat("yyyy-MM-dd").parse(startDate))
      );
      params.put(
        "enddate",
        new SimpleDateFormat("dd/MM/yyyy", new Locale("TH", "th"))
          .format(new SimpleDateFormat("yyyy-MM-dd").parse(endDate))
      );

      InputStream reportInput =
        UserService.class.getClassLoader().getResourceAsStream(spec);
      JasperReport jasperReport = JasperCompileManager.compileReport(
        reportInput
      );
      JasperPrint jasperPrint = JasperFillManager.fillReport(
        jasperReport,
        params,
        getDataSource(ht)
      );
      byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
      return Base64.encodeBase64String(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String printReportGeneric9(String startDate, String endDate) {
    try {
      String spec = "report/Generic9.jrxml";
      Map<String, Object> params = new HashMap<String, Object>();
      //Optional<Sector> sector = sectorRepository.findById(sectorID);
      //Optional<Department> depOptional = departmentRepository.findById(deptID);
      LinkedHashMap<String, Object> ht = HistoryGeneric9(startDate, endDate);

      //params.put("sector_name", sector.get().getSectorName());
      //params.put("dept_name", depOptional.get().getDeptName());

      params.put(
        "startdate",
        new SimpleDateFormat("dd/MM/yyyy", new Locale("TH", "th"))
          .format(new SimpleDateFormat("yyyy-MM-dd").parse(startDate))
      );
      params.put(
        "enddate",
        new SimpleDateFormat("dd/MM/yyyy", new Locale("TH", "th"))
          .format(new SimpleDateFormat("yyyy-MM-dd").parse(endDate))
      );

      InputStream reportInput =
        UserService.class.getClassLoader().getResourceAsStream(spec);
      JasperReport jasperReport = JasperCompileManager.compileReport(
        reportInput
      );
      JasperPrint jasperPrint = JasperFillManager.fillReport(
        jasperReport,
        params,
        getDataSourceGeneric9(ht)
      );
      byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
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
    Long sectorID
  ) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date parsedStartDate = dateFormat.parse(startDate);
      Date parsedEndDate = dateFormat.parse(endDate);

      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> query = cb.createTupleQuery();
      Root<Training> trainingRoot = query.from(Training.class);

      Join<Training, Course> courseJoin = trainingRoot.join("courses");
      Join<Training, Status> statusJoin = trainingRoot.join("status");

      query
        .multiselect(
          trainingRoot.get("id").alias("train_id"),
          courseJoin.get("active").alias("active"),
          statusJoin.get("status").alias("status")
        )
        .distinct(true);

      Predicate startDatePredicate = cb.greaterThanOrEqualTo(
        courseJoin.get("startDate"),
        parsedStartDate
      );
      Predicate endDatePredicate = cb.lessThanOrEqualTo(
        courseJoin.get("endDate"),
        parsedEndDate
      );
      Predicate deptPredicate = cb.equal(
        trainingRoot.get("user").get("department").get("id"),
        deptID
      );
      Predicate sectorPredicate = cb.equal(
        trainingRoot.get("user").get("sector").get("id"),
        sectorID
      );
      Predicate cancelPredicate = cb.equal(
        courseJoin.get("active"),
        "ดำเนินการอยู่"
      );

      Predicate passPredicate = cb.or(
        cb.notEqual(statusJoin.get("status"), StatusApprove.ยกเลิก),
        cb.isNull(statusJoin.get("status"))
      );

      query.where(
        cb.and(
          startDatePredicate,
          endDatePredicate,
          deptPredicate,
          sectorPredicate,
          cancelPredicate,
          passPredicate
        )
      );
      TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
      List<Tuple> resultListBudgetTraining = typedQuery.getResultList();

      CriteriaBuilder cbOutput = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> queryOutput = cbOutput.createTupleQuery();
      Root<Training> trainingRootOutput = queryOutput.from(Training.class);
      queryOutput.multiselect(
        trainingRootOutput.get("user").get("id").alias("user_id"),
        trainingRootOutput.get("user").get("empCode").alias("emp_code"),
        trainingRootOutput.get("user").get("title").alias("title"),
        trainingRootOutput.get("user").get("firstname").alias("firstname"),
        trainingRootOutput.get("user").get("lastname").alias("lastname"),
        trainingRootOutput.join("courses").get("id").alias("course_id"),
        trainingRootOutput
          .join("courses")
          .get("courseName")
          .alias("course_name"),
        trainingRootOutput.join("courses").get("place").alias("place"),
        trainingRootOutput.join("courses").get("price").alias("price"),
        trainingRootOutput.join("courses").get("startDate").alias("start_date"),
        trainingRootOutput.join("courses").get("endDate").alias("end_date"),
        trainingRootOutput
          .join("courses")
          .get("priceProject")
          .alias("priceProject")
      );
      queryOutput.where(
        trainingRootOutput
          .get("id")
          .in(
            resultListBudgetTraining
              .stream()
              .map(tuple -> tuple.get("train_id", Long.class))
              .collect(Collectors.toList())
          )
      );
      queryOutput.orderBy(cb.asc(trainingRootOutput.get("user").get("id")));

      TypedQuery<Tuple> typedQueryOutput = entityManager.createQuery(
        queryOutput
      );
      List<Tuple> resultListBudgetTrainingOutput = typedQueryOutput.getResultList();

      LinkedHashMap<String, Object> result = new LinkedHashMap<>();
      List<LinkedHashMap<String, Object>> users = new ArrayList<>();
      LinkedHashMap<String, Object> currentUser = null;
      float totalAll = 0;

      for (Tuple row : resultListBudgetTrainingOutput) {
        if (
          currentUser == null ||
          !currentUser.get("emp_code").equals(row.get("emp_code"))
        ) {
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
        course.put("priceProject", row.get("priceProject"));
        ((List<LinkedHashMap<String, Object>>) currentUser.get("course")).add(
            course
          );
        totalAll += (float) row.get("price");
      }

      if (currentUser != null) {
        currentUser.put("total", (totalAll));
      }
      double totalAllValue = users
        .stream()
        .mapToDouble(user -> (Float) user.get("total"))
        .sum();

      result.put("total_All", totalAllValue);
      result.put("data", users);

      return result;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  //   public LinkedHashMap<String, Object> generic9(
  //     String startDate,
  //     String endDate
  //   ) {
  //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  //     try {
  //       Date parsedStartDate = dateFormat.parse(startDate);
  //       Date parsedEndDate = dateFormat.parse(endDate);
  //
  //       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
  //       CriteriaQuery<Tuple> query = cb.createTupleQuery();
  //       Root<Training> trainingRoot = query.from(Training.class);
  //
  //       Join<Training, Course> courseJoin = trainingRoot.join("courses");
  //       Join<Training, Status> statusJoin = trainingRoot.join("status");
  //
  //       query
  //         .multiselect(
  //           trainingRoot.get("id").alias("train_id"),
  //           courseJoin.get("active").alias("active"),
  //           statusJoin.get("status").alias("status")
  //         )
  //         .distinct(true);
  //
  //       Predicate startDatePredicate = cb.greaterThanOrEqualTo(
  //         courseJoin.get("startDate"),
  //         parsedStartDate
  //       );
  //       Predicate endDatePredicate = cb.lessThanOrEqualTo(
  //         courseJoin.get("endDate"),
  //         parsedEndDate
  //       );
  //       Predicate cancelPredicate = cb.equal(
  //         courseJoin.get("active"),
  //         "ดำเนินการอยู่"
  //       );
  //       Predicate passPredicate = cb.or(
  //         cb.notEqual(statusJoin.get("status"), StatusApprove.ยกเลิก),
  //         cb.isNull(statusJoin.get("status"))
  //       );
  //
  //       query.where(
  //         cb.and(
  //           startDatePredicate,
  //           endDatePredicate,
  //           cancelPredicate,
  //           passPredicate
  //         )
  //       );
  //       TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
  //       List<Tuple> resultListBudgetTraining = typedQuery.getResultList();
  //
  //       CriteriaBuilder cbOutput = entityManager.getCriteriaBuilder();
  //       CriteriaQuery<Tuple> queryOutput = cbOutput.createTupleQuery();
  //       Root<Training> trainingRootOutput = queryOutput.from(Training.class);
  //       queryOutput.multiselect(
  //         trainingRootOutput.get("user").get("id").alias("user_id"),
  //         trainingRootOutput.get("user").get("empCode").alias("emp_code"),
  //         trainingRootOutput.get("user").get("title").alias("title"),
  //         trainingRootOutput.get("user").get("firstname").alias("firstname"),
  //         trainingRootOutput.get("user").get("lastname").alias("lastname"),
  //         trainingRootOutput.join("courses").get("id").alias("course_id"),
  //         trainingRootOutput.join("result").get("result1").alias("result1"),
  //         trainingRootOutput.join("result").get("result2").alias("result2"),
  //         trainingRootOutput.join("result").get("result3").alias("result3"),
  //         trainingRootOutput.join("result").get("result4").alias("result4"),
  //         trainingRootOutput.join("result").get("result5").alias("result5"),
  //         trainingRootOutput.join("result").get("result6").alias("result6"),
  //         trainingRootOutput.join("result").get("result7").alias("result7"),
  //         trainingRootOutput
  //           .join("courses")
  //           .get("courseName")
  //           .alias("course_name")
  //       );
  //
  //       queryOutput.where(
  //         trainingRootOutput
  //           .get("id")
  //           .in(
  //             resultListBudgetTraining
  //               .stream()
  //               .map(tuple -> tuple.get("train_id", Long.class))
  //               .collect(Collectors.toList())
  //           )
  //       );
  //
  //       queryOutput.orderBy(cbOutput.asc(courseJoin.get("id")));
  //
  //       TypedQuery<Tuple> typedQueryOutput = entityManager.createQuery(
  //         queryOutput
  //       );
  //       List<Tuple> resultListBudgetTrainingOutput = typedQueryOutput.getResultList();
  //
  //       LinkedHashMap<String, Object> result = new LinkedHashMap<>();
  //       List<LinkedHashMap<String, Object>> courses = new ArrayList<>();
  //
  //       for (Tuple row : resultListBudgetTrainingOutput) {
  //         LinkedHashMap<String, Object> currentcourse = courses
  //           .stream()
  //           .filter(course ->
  //             course.get("course_name").equals(row.get("course_name"))
  //           )
  //           .findFirst()
  //           .orElse(null);
  //
  //         if (currentcourse == null) {
  //           currentcourse = new LinkedHashMap<>();
  //           currentcourse.put("course_name", row.get("course_name"));
  //           currentcourse.put("user", new ArrayList<>());
  //           courses.add(currentcourse);
  //         }
  //
  //         LinkedHashMap<String, Object> user = new LinkedHashMap<>();
  //         user.put("title", row.get("title"));
  //         user.put("firstname", row.get("firstname"));
  //         user.put("lastname", row.get("lastname"));
  //         user.put("result1",row.get("result1"));
  //         user.put("result2",row.get("result2"));
  //         user.put("result3",row.get("result3"));
  //         user.put("result4",row.get("result4"));
  //         user.put("result5",row.get("result5"));
  //         user.put("result6",row.get("result6"));
  //         user.put("result7",row.get("result7"));
  //
  //         ((List<LinkedHashMap<String, Object>>) currentcourse.get("user")).add(
  //             user
  //           );
  //       }
  //
  //       result.put("data", courses);
  //       return result;
  //
  //     } catch (ParseException e) {
  //       e.printStackTrace();
  //     }
  //     return null;
  //   }

  public LinkedHashMap<String, Object> HistoryGeneric9(
    String startDate,
    String endDate
  ) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date parsedStartDate = dateFormat.parse(startDate);
      Date parsedEndDate = dateFormat.parse(endDate);

      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> query = cb.createTupleQuery();
      Root<Training> trainingRoot = query.from(Training.class);

      Join<Training, Course> courseJoin = trainingRoot.join("courses");
      Join<Training, Status> statusJoin = trainingRoot.join("status");

      query
        .multiselect(
          trainingRoot.get("id").alias("train_id"),
          courseJoin.get("active").alias("active"),
          statusJoin.get("status").alias("status")
        )
        .distinct(true);

      Predicate startDatePredicate = cb.greaterThanOrEqualTo(
        courseJoin.get("startDate"),
        parsedStartDate
      );
      Predicate endDatePredicate = cb.lessThanOrEqualTo(
        courseJoin.get("endDate"),
        parsedEndDate
      );
      Predicate cancelPredicate = cb.equal(
        courseJoin.get("active"),
        "ดำเนินการอยู่"
      );

      Predicate passPredicate = cb.or(
        cb.notEqual(statusJoin.get("status"), StatusApprove.ยกเลิก),
        cb.isNull(statusJoin.get("status"))
      );

      query.where(
        cb.and(
          startDatePredicate,
          endDatePredicate,
          cancelPredicate,
          passPredicate
        )
      );
      TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
      List<Tuple> resultListBudgetTraining = typedQuery.getResultList();

      CriteriaBuilder cbOutput = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> queryOutput = cbOutput.createTupleQuery();
      Root<Training> trainingRootOutput = queryOutput.from(Training.class);
      queryOutput.multiselect(
        trainingRootOutput.get("user").get("id").alias("user_id"),
        trainingRootOutput.get("user").get("empCode").alias("emp_code"),
        trainingRootOutput.get("user").get("title").alias("title"),
        trainingRootOutput.get("user").get("firstname").alias("firstname"),
        trainingRootOutput.get("user").get("lastname").alias("lastname"),
        trainingRootOutput.join("courses").get("id").alias("course_id"),
        trainingRootOutput.join("result").get("result1").alias("result1"),
        trainingRootOutput.join("result").get("result2").alias("result2"),
        trainingRootOutput.join("result").get("result3").alias("result3"),
        trainingRootOutput.join("result").get("result4").alias("result4"),
        trainingRootOutput.join("result").get("result5").alias("result5"),
        trainingRootOutput.join("result").get("result6").alias("result6"),
        trainingRootOutput.join("result").get("result7").alias("result7"),
        trainingRootOutput
          .join("courses")
          .get("courseName")
          .alias("course_name")
      );
      queryOutput.where(
        trainingRootOutput
          .get("id")
          .in(
            resultListBudgetTraining
              .stream()
              .map(tuple -> tuple.get("train_id", Long.class))
              .collect(Collectors.toList())
          )
      );
      queryOutput.orderBy(cb.asc(trainingRootOutput.get("user").get("id")));

      TypedQuery<Tuple> typedQueryOutput = entityManager.createQuery(
        queryOutput
      );
      List<Tuple> resultListBudgetTrainingOutput = typedQueryOutput.getResultList();

      LinkedHashMap<String, Object> result = new LinkedHashMap<>();
      List<LinkedHashMap<String, Object>> users = new ArrayList<>();
      LinkedHashMap<String, Object> currentUser = null;

      for (Tuple row : resultListBudgetTrainingOutput) {
        if (
          currentUser == null ||
          !currentUser.get("emp_code").equals(row.get("emp_code"))
        ) {
          currentUser = new LinkedHashMap<>();
          currentUser.put("user_id", row.get("user_id"));
          currentUser.put("emp_code", row.get("emp_code"));
          currentUser.put("title", row.get("title"));
          currentUser.put("firstname", row.get("firstname"));
          currentUser.put("lastname", row.get("lastname"));
          currentUser.put("course", new ArrayList<>());
          users.add(currentUser);
        }

        LinkedHashMap<String, Object> course = new LinkedHashMap<>();
        course.put("course_name", row.get("course_name"));
        course.put("result1", row.get("result1"));
        course.put("result2", row.get("result2"));
        course.put("result3", row.get("result3"));
        course.put("result4", row.get("result4"));
        course.put("result5", row.get("result5"));
        course.put("result6", row.get("result6"));
        course.put("result7", row.get("result7"));
        ((List<LinkedHashMap<String, Object>>) currentUser.get("course")).add(
            course
          );
      }
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
    String jpql = "DELETE FROM Status s WHERE training_id = :training_id";
    entityManager
      .createQuery(jpql)
      .setParameter("training_id", trainingId)
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
