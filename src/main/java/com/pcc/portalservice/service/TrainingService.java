package com.pcc.portalservice.service;

//

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditGeneric9Result;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;
import java.io.ByteArrayOutputStream;
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
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
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
  private final PositionRepository positionRepository;
  private final ResultGeneric9Repository resultGeneric9Repository;

  private final EntityManager entityManager;

  private List<Long> findVicebySector(String sector) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);

    Root<User> userRoot = query.from(User.class);
    Join<User, Department> sectorJoin = userRoot.join("sectors");
    Join<User, Role> roleJoin = userRoot.join("roles");

    query
      .select(userRoot.get("id"))
      .where(
        cb.equal(sectorJoin.get("sectorName"), sector),
        cb.equal(roleJoin.get("role"), Roles.VicePresident)
      );

    List<Long> userIds = entityManager.createQuery(query).getResultList();
    return userIds;
  }

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

  private Long findsectorByUserID(Long id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

    Root<User> userRoot = criteriaQuery.from(User.class);
    Join<User, Sector> sectorJoin = userRoot.join("sectors");

    Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
    Root<Sector> subquerysectorRoot = subquery.from(Sector.class);
    subquery.select(subquerysectorRoot.get("id"));
    subquery.where(criteriaBuilder.equal(subquerysectorRoot, sectorJoin));

    criteriaQuery
      .select(sectorJoin.get("id"))
      .distinct(true)
      .where(
        criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("id"), id))
      );

    return entityManager.createQuery(criteriaQuery).getSingleResult();
  }

  public Training createTraining(CreateTrainingRequest createTrainingRequest)
    throws ParseException {
    User user = userRepository
      .findById(createTrainingRequest.getUserId())
      .orElseThrow(() ->
        new RuntimeException(
          "UserId not found: " + createTrainingRequest.getUserId()
        )
      );

    User approve1 = createTrainingRequest.getApprove1_id() != 0
      ? userRepository
        .findById(createTrainingRequest.getApprove1_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve1Id not found: " + createTrainingRequest.getApprove1_id()
          )
        )
      : null;

    User approve2 = createTrainingRequest.getApprove2_id() != 0
      ? userRepository
        .findById(createTrainingRequest.getApprove2_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve2Id not found: " + createTrainingRequest.getApprove2_id()
          )
        )
      : null;

    User approve3 = createTrainingRequest.getApprove3_id() != 0
      ? userRepository
        .findById(createTrainingRequest.getApprove3_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve3Id not found: " + createTrainingRequest.getApprove3_id()
          )
        )
      : null;

    User approve4 = createTrainingRequest.getApprove4_id() != 0
      ? userRepository
        .findById(createTrainingRequest.getApprove4_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve4Id not found: " + createTrainingRequest.getApprove4_id()
          )
        )
      : null;

    Optional<Sector> SectorOptional = sectorRepository.findById(
      findsectorByUserID(user.getId())
    );

    Sector sector = SectorOptional.get();

    User vice = findVicebySector(sector.getSectorName()).get(0) != 0
      ? userRepository
        .findById(findVicebySector(sector.getSectorName()).get(0))
        .orElseThrow(() ->
          new RuntimeException(
            "Vice not found: " + findVicebySector(sector.getSectorName()).get(0)
          )
        )
      : null;

    Course course = courseRepository
      .findById(createTrainingRequest.getCourseId())
      .orElseThrow(() ->
        new RuntimeException(
          "CourseId not found: " + createTrainingRequest.getCourseId()
        )
      );

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

    Training training = null;

    if (approve1 != null) {
      training =
        Training
          .builder()
          .user(user)
          .dateSave(new Date())
          .day(daysDifference)
          .courses(Arrays.asList(course))
          .budget(createTrainingRequest.getBudget())
          .action(createTrainingRequest.getAction())
          .actionDate(actionDateFormat)
          .approve1(approve1)
          .build();
    } else if (approve2 != null) {
      training =
        Training
          .builder()
          .user(user)
          .dateSave(new Date())
          .day(daysDifference)
          .courses(Arrays.asList(course))
          .budget(createTrainingRequest.getBudget())
          .action(createTrainingRequest.getAction())
          .actionDate(actionDateFormat)
          .approve1(approve2)
          .build();
    } else if (approve3 != null) {
      training =
        Training
          .builder()
          .user(user)
          .dateSave(new Date())
          .day(daysDifference)
          .courses(Arrays.asList(course))
          .budget(createTrainingRequest.getBudget())
          .action(createTrainingRequest.getAction())
          .actionDate(actionDateFormat)
          .approve1(approve3)
          .build();
    } else if (approve4 != null) {
      training =
        Training
          .builder()
          .user(user)
          .dateSave(new Date())
          .day(daysDifference)
          .courses(Arrays.asList(course))
          .budget(createTrainingRequest.getBudget())
          .action(createTrainingRequest.getAction())
          .actionDate(actionDateFormat)
          .approve1(approve4)
          .build();
    }

    if (training != null && training.getResult() == null) {
      training.setResult(new ArrayList<>());
      training.setResultGeneric9(new ArrayList<>());
    }
    Training savedTraining = trainingRepository.save(training);

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

    ResultGeneric9 resultGeneric9 = ResultGeneric9
      .builder()
      .result1(null)
      .result2(null)
      .result3(null)
      .result4(null)
      .result5(null)
      .training(training)
      .build();

    resultGeneric9Repository.save(resultGeneric9);
    training.getResultGeneric9().add(resultGeneric9);

    if (training != null && training.getStatus() == null) {
      training.setStatus(new ArrayList<>());
    }
    int active = 1;

    if (approve1 != null) {
      Status status1 = Status
        .builder()
        .status(null)
        .indexOfSignature(1)
        .training(training)
        .approveId(approve1)
        .active(active)
        .build();
      statusRepository.save(status1);
      training.getStatus().add(status1);
      active = 0;

      if (approve2 == null && approve3 == null) {
        Status viceStatus = Status
          .builder()
          .indexOfSignature(3)
          .status(null)
          .training(training)
          .approveId(vice)
          .active(0)
          .build();
        statusRepository.save(viceStatus);
        training.getStatus().add(viceStatus);
        active = 0;
      }
    }

    if (approve2 != null) {
      Status status2 = Status
        .builder()
        .status(null)
        .indexOfSignature(2)
        .training(training)
        .approveId(approve2)
        .active(active)
        .build();
      statusRepository.save(status2);
      training.getStatus().add(status2);
      if (approve3 == null) {
        Status viceStatus = Status
          .builder()
          .status(null)
          .indexOfSignature(3)
          .training(training)
          .approveId(vice)
          .active(0)
          .build();
        statusRepository.save(viceStatus);
        training.getStatus().add(viceStatus);
      }
      active = 0;
    }

    if (approve3 != null) {
      Status status3 = Status
        .builder()
        .status(null)
        .training(training)
        .approveId(approve3)
        .active(active)
        .indexOfSignature(3)
        .build();
      statusRepository.save(status3);
      training.getStatus().add(status3);
      active = 0;
    }

    if (approve4 != null) {
      Status status4 = Status
        .builder()
        .status(null)
        .training(training)
        .approveId(approve4)
        .active(active)
        .indexOfSignature(4)
        .build();
      statusRepository.save(status4);
      training.getStatus().add(status4);
    }
    return savedTraining;
  }

  public Training editTrainingSection1(
    Long trainingId,
    EditTrainingSection1Request editTraining
  ) throws ParseException {
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
    User approve1 = editTraining.getApprove1_id() != 0
      ? userRepository
        .findById(editTraining.getApprove1_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve1Id not found: " + editTraining.getApprove1_id()
          )
        )
      : null;

    User approve2 = editTraining.getApprove2_id() != 0
      ? userRepository
        .findById(editTraining.getApprove2_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve2Id not found: " + editTraining.getApprove2_id()
          )
        )
      : null;

    User approve3 = editTraining.getApprove3_id() != 0
      ? userRepository
        .findById(editTraining.getApprove3_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve3Id not found: " + editTraining.getApprove3_id()
          )
        )
      : null;

    User approve4 = editTraining.getApprove4_id() != 0
      ? userRepository
        .findById(editTraining.getApprove4_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve4Id not found: " + editTraining.getApprove4_id()
          )
        )
      : null;


    changeApprover(editTraining, trainingId);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date actionDateFormat = dateFormat.parse(editTraining.getActionDate());

    training_id.setUser(user_id);
    training_id.setDateSave(new Date());
    training_id.getCourses().clear();
    training_id.getCourses().add(course_id);
    training_id.setAction(editTraining.getAction());
    training_id.setActionDate(actionDateFormat);
    if (editTraining.getApprove1_id() != 0) {
      training_id.setApprove1(approve1);
    } else if (editTraining.getApprove2_id() != 0) {
      training_id.setApprove1(approve2);
    } else if (editTraining.getApprove3_id() != 0) {
      training_id.setApprove1(approve3);
    }
    training_id.setBudget(editTraining.getBudget());

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

  public Training findByTrainingId(Long id) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = builder.createQuery(Training.class);
    Root<Training> root = query.from(Training.class);

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

      if (approvedCount == count) {
        result_status = "อนุมัติ";
      } else if (disapprovedCount >= 1) {
        result_status = "ไม่อนุมัติ";
      } else if (cancalapprovedCount >= 1) {
        result_status = "ยกเลิก";
      } else {
        result_status = "รอประเมิน";
      }
      List<Status> statusListCopy = new ArrayList<>(training.getStatus());
      statusListCopy.sort(Comparator.comparing(Status::getId));
      training.setStatus(statusListCopy);

      resultWithStatus.put("training", training);
      resultWithStatus.put("result_status", result_status);
    }

    return resultWithStatus;
  }

  public List<Map<String, Object>> findTrainingsByUserId(Long userId) {
    String jpql =
      "SELECT t FROM Training t JOIN FETCH t.status s WHERE t.user.id = :id";

    TypedQuery<Training> query = entityManager.createQuery(
      jpql,
      Training.class
    );

    query.setParameter("id", userId);
    List<Training> resultList = query.getResultList();

    return calculateTrainingResultStatus(resultList);
  }

  public List<Map<String, Object>> findTrainingsByDept(Long userId) {
    String sql =
      "SELECT DISTINCT ud.id " +
      "FROM User u " +
      "JOIN u.departments ud " +
      "WHERE u.id = :id ";

    TypedQuery<Long> query = entityManager.createQuery(sql, Long.class);
    query.setParameter("id", userId);
    List<Long> departmentIds = query.getResultList();

    String jpql =
      "SELECT t " +
      "FROM Training t " +
      "JOIN User u ON u.id = t.user.id " +
      "JOIN u.departments ud " +
      "WHERE ud.id IN (:id)";

    TypedQuery<Training> querys = entityManager.createQuery(
      jpql,
      Training.class
    );

    querys.setParameter("id", departmentIds);
    List<Training> resultList = querys.getResultList();

    return calculateTrainingResultStatus(resultList);
  }

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
            cancalapprovedCount++;
          }
        }
        count++;
      }

      if (approvedCount == count) {
        result_status = "อนุมัติ";
      } else if (disapprovedCount >= 1) {
        result_status = "ไม่อนุมัติ";
      } else if (cancalapprovedCount >= 1) {
        result_status = "ยกเลิก";
      } else {
        result_status = "รอประเมิน";
      }

      List<Status> statusListCopy = new ArrayList<>(uniqueStatusList);
      statusListCopy.sort(Comparator.comparing(Status::getId));
      training.setStatus(statusListCopy);

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

      if (approvedCount == count) {
        resultStatus = "อนุมัติ";
      } else if (disapprovedCount >= 1) {
        resultStatus = "ไม่อนุมัติ";
      } else if (cancelApprovedCount >= 1) {
        resultStatus = "ยกเลิก";
      } else {
        resultStatus = "รอประเมิน";
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

  public Object searchTraining(
    String name,
    String position,
    String department,
    Date startDate,
    Date endDate,
    String courseName,
    String company
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
      Join<Training, User> userJoin = root.join("user").join("departments");
      //Join<User, Department> departmentJoin = userJoin.join("department");
      predicates.add(
        builder.like(
          builder.lower(userJoin.get("deptName")),
          "%" + department.toLowerCase() + "%"
        )
      );
    }

    if (startDate != null) {
      Join<Training, Course> courseJoin = root.join("courses");
      predicates.add(
        builder.greaterThanOrEqualTo(courseJoin.get("startDate"), startDate)
      );
    }

    if (endDate != null) {
      Join<Training, Course> courseJoin = root.join("courses");
      predicates.add(
        builder.lessThanOrEqualTo(courseJoin.get("endDate"), endDate)
      );
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
    if (company != null) {
      Join<Training, User> userJoin = root.join("user");
      Join<User, Company> companyJoin = userJoin.join("companys");

      predicates.add(
        builder.like(
          builder.lower(companyJoin.get("companyName")),
          "%" + company.toLowerCase() + "%"
        )
      );
    }

    if (
      name == null &&
      position == null &&
      department == null &&
      startDate == null &&
      endDate == null &&
      courseName == null &&
      company == null
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
    System.out.println(training_id.getUser().getCompanys());
    Optional<Department> departmentOptional = departmentRepository.findById(
      findDeptByUserID(training_id.getUser().getId())
    );

    Optional<Sector> SectorOptional = sectorRepository.findById(
      findsectorByUserID(training_id.getUser().getId())
    );

    try {
      List<Map<String, Object>> dataList = new ArrayList<>();

      Map<String, Object> params = new HashMap<>();

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
      params.put("sector_name", SectorOptional.get().getSectorName());
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

      if (training_id.getResult().get(0).getResult() != null) {
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
        params.put("app_name", training_id.getApprove1().getFirstname());
        params.put("app_lastname", training_id.getApprove1().getLastname());
        params.put(
          "app_position",
          training_id.getApprove1().getPosition().getPositionName()
        );
        params.put("app_dept_name", departmentOptional.get().getDeptName());
        params.put("app_sector_name", SectorOptional.get().getSectorName());
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
      }
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
      List<String> course_hours = new ArrayList<>();
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
        course_hours.add(
                course.get("course_hour") != null
                        ? course.get("course_hour").toString()
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
      course_hours.add("\t\t\t");
      dates.add("\t\t\t");
      course_priceProjects.add("\t\t\t");

      coll.add(
        new BeanHistroy(
          course_names,
          course_hours,
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
      List<String> result1 = new ArrayList<>();
      List<String> result2 = new ArrayList<>();
      List<String> result3 = new ArrayList<>();
      List<String> result4 = new ArrayList<>();
      List<String> result5 = new ArrayList<>();
      String position = (String) userData.get("position");
      Long id = (Long) userData.get("user_id");
      String title = userData.get("title").toString() ;
      String firstname = userData.get("firstname").toString();
      String lastname =   userData.get("lastname").toString();


      List<Map<String, Object>> courseList = (List<Map<String, Object>>) userData.get(
        "course"
      );

      for (Map<String, Object> course : courseList) {
        course_names.add(
          course.get("course_name") != null
            ? course.get("course_name").toString()
            : ""
        );
        result1.add(
          course.get("result1") != null ? (String) course.get("result1") : " "
        );
        result2.add(
          course.get("result2") != null ? (String) course.get("result2") : " "
        );
        result3.add(
          course.get("result3") != null ? (String) course.get("result3") : " "
        );
        result4.add(
          course.get("result4") != null ? (String) course.get("result4") : " "
        );
        result5.add(
          course.get("result5") != null ? (String) course.get("result5") : " "
        );
      }
      coll.add(
        new BeanGeneric9(
          course_names,
          result1,
          result2,
          result3,
          result4,
          result5,
          position,
          title,
          firstname,
          lastname,
          id
        )
      );
    }

    return new JRBeanCollectionDataSource(coll);
  }

  public String printReportHistoryTraining(
    String startDate,
    String endDate,
    Long deptID,
    Long courseID
  ) {
    LinkedHashMap<String, Object> ht = HistoryTraining(
            startDate,
            endDate,
            deptID,
            courseID
    );
    if(deptID == null && startDate == null && endDate == null && courseID!= null){
      try {
        String spec = "report/histroy-training_course.jrxml";
        Map<String, Object> params = new HashMap<String, Object>();
        Optional<Course> course = courseRepository.findById(courseID);
        params.put("name_course", course.get().getCourseName().toString());

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
        List<?> dataList = (List<?>) ht.get("data");

        if (dataList != null && !dataList.isEmpty()) {
          return Base64.encodeBase64String(bytes);
        } else {
          return "ไม่มีข้อมูล";
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
    else{
      try {
        String spec = "report/histroy-training.jrxml";
        Map<String, Object> params = new HashMap<String, Object>();
        Optional<Department> depOptional = departmentRepository.findById(deptID);
        params.put("company", depOptional.get().getSector().getCompany().getCompanyName());
        params.put("sector_name", depOptional.get().getSector().getSectorName());
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
        List<?> dataList = (List<?>) ht.get("data");

        if (dataList != null && !dataList.isEmpty()) {
          return Base64.encodeBase64String(bytes);
        } else {
          return "ไม่มีข้อมูล";
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
  }


  public Map<String, Object> printReportGeneric9(
    String startDate,
    String endDate
  ) {
    Map<String, Object> reports = new HashMap<>();

    try {
      String spec = "report/Generic9.jrxml";
      for (int i = 1; i < 3; i++) {
        Map<String, Object> params = new HashMap<String, Object>();
        LinkedHashMap<String, Object> ht = HistoryGeneric9(
          startDate,
          endDate,
          Long.valueOf(i)
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

        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporter.setExporterOutput(
          new SimpleOutputStreamExporterOutput(outputStream)
        );

        exporter.exportReport();
        byte[] xlsBytes = outputStream.toByteArray();
        List<?> dataList = (List<?>) ht.get("data");

        reports.put(
          (i == 1) ? "PCC_Jasper" : "Wisesoft_Jasper",
          (!dataList.isEmpty())
            ? Base64.encodeBase64String(xlsBytes)
            : "ไม่มีข้อมูล"
        );
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reports;
  }

  public LinkedHashMap<String, Object> HistoryTraining(
    String startDate,
    String endDate,
    Long deptID,
    Long courseID
  ) {
    if(deptID == null && startDate == null && endDate == null && courseID!= null){
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

      Predicate corusePredicate = cb.equal(courseJoin.get("id"), courseID);

      Predicate cancelPredicate = cb.equal(
              courseJoin.get("active"),
              "ดำเนินการอยู่"
      );

      Predicate passPredicate = cb.equal(
              statusJoin.get("status"),
              StatusApprove.อนุมัติ
      );

      Subquery<Long> approvedStatusCountSubquery = query.subquery(Long.class);
      Root<Status> statusRoot1 = approvedStatusCountSubquery.from(Status.class);
      approvedStatusCountSubquery.select(cb.count(statusRoot1));
      approvedStatusCountSubquery.where(
              cb.equal(statusRoot1.get("status"), StatusApprove.อนุมัติ),
              cb.equal(trainingRoot.get("id"), statusRoot1.get("training"))
      );

      Subquery<Long> totalStatusCountSubquery = query.subquery(Long.class);
      Root<Status> statusRoot2 = totalStatusCountSubquery.from(Status.class);
      totalStatusCountSubquery.select(cb.count(statusRoot2));
      totalStatusCountSubquery.where(
              cb.equal(trainingRoot.get("id"), statusRoot2.get("training"))
      );

      query.where(
              cb.and(
                      corusePredicate,
                      cancelPredicate,
                      passPredicate,
                      cb.equal(approvedStatusCountSubquery, totalStatusCountSubquery)
              )
      );
      TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
      List<Tuple> resultListIDTraining = typedQuery.getResultList();

      System.out.println(resultListIDTraining);

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
              trainingRootOutput.join("courses").get("hours").alias("hours"),
              trainingRootOutput
                      .join("courses")
                      .get("priceProject")
                      .alias("priceProject")
      );
      queryOutput.where(
              trainingRootOutput
                      .get("id")
                      .in(
                              resultListIDTraining
                                      .stream()
                                      .map(tuple -> tuple.get("train_id", Long.class))
                                      .collect(Collectors.toList())
                      )
      );
      queryOutput.orderBy(cb.asc(trainingRootOutput.get("user").get("id")));

      TypedQuery<Tuple> typedQueryOutput = entityManager.createQuery(
              queryOutput
      );
      List<Tuple> resultListIDTrainingOutput = typedQueryOutput.getResultList();

      LinkedHashMap<String, Object> result = new LinkedHashMap<>();
      List<LinkedHashMap<String, Object>> users = new ArrayList<>();
      LinkedHashMap<String, Object> currentUser = null;
      float totalAll = 0;

      for (Tuple row : resultListIDTrainingOutput) {
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
        course.put("course_hour",row.get("hours"));
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
    }
    else{
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

        Predicate predicate = cb.conjunction();

        if (courseID != null) {
          predicate = cb.and(predicate, cb.equal(courseJoin.get("id"), courseID));
        }

        if (startDate != null) {
          predicate = cb.and(predicate, cb.greaterThanOrEqualTo(courseJoin.get("startDate"), parsedStartDate));
        }

        if (endDate != null) {
          predicate = cb.and(predicate, cb.lessThanOrEqualTo(courseJoin.get("endDate"), parsedEndDate));
        }

        if (deptID != null) {
          Join<User, Department> departmentJoin = trainingRoot.join("user").join("departments");
          predicate = cb.and(predicate, cb.equal(departmentJoin.get("id"), deptID));
        }

        predicate = cb.and(
                predicate,
                cb.equal(courseJoin.get("active"), "ดำเนินการอยู่"),
                cb.equal(statusJoin.get("status"), StatusApprove.อนุมัติ)
        );

        Subquery<Long> approvedStatusCountSubquery = query.subquery(Long.class);
        Root<Status> statusRoot1 = approvedStatusCountSubquery.from(Status.class);
        approvedStatusCountSubquery.select(cb.count(statusRoot1));
        approvedStatusCountSubquery.where(
                cb.equal(statusRoot1.get("status"), StatusApprove.อนุมัติ),
                cb.equal(trainingRoot.get("id"), statusRoot1.get("training"))
        );

        Subquery<Long> totalStatusCountSubquery = query.subquery(Long.class);
        Root<Status> statusRoot2 = totalStatusCountSubquery.from(Status.class);
        totalStatusCountSubquery.select(cb.count(statusRoot2));
        totalStatusCountSubquery.where(
                cb.equal(trainingRoot.get("id"), statusRoot2.get("training"))
        );

        predicate = cb.and(predicate, cb.equal(approvedStatusCountSubquery, totalStatusCountSubquery));

        query.where(predicate);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        List<Tuple> resultListIDTraining = typedQuery.getResultList();
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
                trainingRootOutput.join("courses").get("hours").alias("hours"),
                trainingRootOutput
                        .join("courses")
                        .get("priceProject")
                        .alias("priceProject")
        );
        queryOutput.where(
                trainingRootOutput
                        .get("id")
                        .in(
                                resultListIDTraining
                                        .stream()
                                        .map(tuple -> tuple.get("train_id", Long.class))
                                        .collect(Collectors.toList())
                        )
        );
        queryOutput.orderBy(cb.asc(trainingRootOutput.get("user").get("id")));

        TypedQuery<Tuple> typedQueryOutput = entityManager.createQuery(
                queryOutput
        );
        List<Tuple> resultListIDTrainingOutput = typedQueryOutput.getResultList();

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> users = new ArrayList<>();
        LinkedHashMap<String, Object> currentUser = null;
        float totalAll = 0;

        for (Tuple row : resultListIDTrainingOutput) {
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
          course.put("course_hour",row.get("hours"));
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
  }

  public LinkedHashMap<String, Object> HistoryGeneric9(
    String startDate,
    String endDate,
    Long companyId
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
          statusJoin.get("status").alias("status"),
          trainingRoot
            .join("user")
            .join("companys")
            .get("id")
            .alias("company_id")
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

      Predicate passPredicate = cb.equal(
        statusJoin.get("status"),
        StatusApprove.อนุมัติ
      );

      Predicate companyPredicate = cb.equal(
        trainingRoot.join("user").join("companys").get("id"),
        companyId
      );

      Subquery<Long> approvedStatusCountSubquery = query.subquery(Long.class);
      Root<Status> statusRoot1 = approvedStatusCountSubquery.from(Status.class);
      approvedStatusCountSubquery.select(cb.count(statusRoot1));
      approvedStatusCountSubquery.where(
        cb.equal(statusRoot1.get("status"), StatusApprove.อนุมัติ),
        cb.equal(trainingRoot.get("id"), statusRoot1.get("training"))
      );

      Subquery<Long> totalStatusCountSubquery = query.subquery(Long.class);
      Root<Status> statusRoot2 = totalStatusCountSubquery.from(Status.class);
      totalStatusCountSubquery.select(cb.count(statusRoot2));
      totalStatusCountSubquery.where(
        cb.equal(trainingRoot.get("id"), statusRoot2.get("training"))
      );

      query.where(
        cb.and(
          startDatePredicate,
          endDatePredicate,
          cancelPredicate,
          passPredicate,
          companyPredicate,
          cb.equal(approvedStatusCountSubquery, totalStatusCountSubquery)
        )
      );
      TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
      List<Tuple> resultListIDTraining = typedQuery.getResultList();

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
          .get("user")
          .get("position")
          .get("id")
          .alias("position_id"),
        trainingRootOutput.join("resultGeneric9").get("result1").alias("result1"),
        trainingRootOutput.join("resultGeneric9").get("result2").alias("result2"),
        trainingRootOutput.join("resultGeneric9").get("result3").alias("result3"),
        trainingRootOutput.join("resultGeneric9").get("result4").alias("result4"),
        trainingRootOutput.join("resultGeneric9").get("result5").alias("result5"),
        trainingRootOutput
          .join("courses")
          .get("courseName")
          .alias("course_name")
      );
      queryOutput.where(
        trainingRootOutput
          .get("id")
          .in(
            resultListIDTraining
              .stream()
              .map(tuple -> tuple.get("train_id", Long.class))
              .collect(Collectors.toList())
          )
      );
      queryOutput.orderBy(cb.asc(trainingRootOutput.get("user").get("id")));

      TypedQuery<Tuple> typedQueryOutput = entityManager.createQuery(
        queryOutput
      );
      List<Tuple> resultListIDTrainingOutput = typedQueryOutput.getResultList();

      LinkedHashMap<String, Object> result = new LinkedHashMap<>();
      List<LinkedHashMap<String, Object>> users = new ArrayList<>();
      LinkedHashMap<String, Object> currentUser = null;

      for (Tuple row : resultListIDTrainingOutput) {
        Optional<Position> positionOptional = positionRepository.findById(
          (Long) row.get("position_id")
        );
        String positionString = positionOptional.get().getPositionName();
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
          currentUser.put("position", positionString);
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
        System.out.println(row.get("result1"));
        System.out.println(row.get("result2"));
        System.out.println(row.get("result3"));
        System.out.println(row.get("result4"));
        System.out.println(row.get("result5"));

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

  public boolean isTrainingNull(CreateTrainingRequest request) {
    return (
      request == null ||
      request.getDateSave() == null ||
      request.getDateSave().toString().isEmpty()
    );
  }

  public boolean isEditTrainingNull1(EditTrainingSection1Request request) {
    return (
      request == null ||
      request.getDateSave() == null ||
      request.getDateSave().toString().isEmpty()
    );
  }

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

  private void changeApprover(
    EditTrainingSection1Request editTraining,
    long trainingId
  ) {
    String jpql = "DELETE FROM Status s WHERE training_id = :training_id";
    entityManager
      .createQuery(jpql)
      .setParameter("training_id", trainingId)
      .executeUpdate();

    User user = userRepository
      .findById(editTraining.getUserId())
      .orElseThrow(() ->
        new RuntimeException("UserId not found: " + editTraining.getUserId())
      );

    User approve1 = editTraining.getApprove1_id() != 0
      ? userRepository
        .findById(editTraining.getApprove1_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve1Id not found: " + editTraining.getApprove1_id()
          )
        )
      : null;

    User approve2 = editTraining.getApprove2_id() != 0
      ? userRepository
        .findById(editTraining.getApprove2_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve2Id not found: " + editTraining.getApprove2_id()
          )
        )
      : null;

    User approve3 = editTraining.getApprove3_id() != 0
      ? userRepository
        .findById(editTraining.getApprove3_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve3Id not found: " + editTraining.getApprove3_id()
          )
        )
      : null;

    User approve4 = editTraining.getApprove4_id() != 0
      ? userRepository
        .findById(editTraining.getApprove4_id())
        .orElseThrow(() ->
          new RuntimeException(
            "Approve4Id not found: " + editTraining.getApprove4_id()
          )
        )
      : null;

    Optional<Sector> SectorOptional = sectorRepository.findById(
      findsectorByUserID(user.getId())
    );

    Sector sector = SectorOptional.get();

    User vice = findVicebySector(sector.getSectorName()).get(0) != 0
      ? userRepository
        .findById(findVicebySector(sector.getSectorName()).get(0))
        .orElseThrow(() ->
          new RuntimeException(
            "Vice not found: " + findVicebySector(sector.getSectorName()).get(0)
          )
        )
      : null;

    Training training_id = findByTrainingIdEdit(trainingId);
    if (training_id.getStatus() == null) {
      training_id.setStatus(new ArrayList<>());
    }
    int active = 1;

    if (approve1 != null) {
      Status status1 = Status
        .builder()
        .indexOfSignature(1)
        .status(null)
        .training(training_id)
        .approveId(approve1)
        .active(active)
        .build();
      statusRepository.save(status1);
      active = 0;

      if (approve2 == null && approve3 == null) {
        Status viceStatus = Status
          .builder()
          .status(null)
          .indexOfSignature(3)
          .training(training_id)
          .approveId(vice)
          .active(0)
          .build();
        statusRepository.save(viceStatus);
        active = 0;
      }
    }

    if (approve2 != null) {
      Status status2 = Status
        .builder()
        .indexOfSignature(2)
        .status(null)
        .training(training_id)
        .approveId(approve2)
        .active(active)
        .build();
      statusRepository.save(status2);
      if (approve3 == null) {
        Status viceStatus = Status
          .builder()
          .indexOfSignature(3)
          .status(null)
          .training(training_id)
          .approveId(vice)
          .active(0)
          .build();
        statusRepository.save(viceStatus);
      }
      active = 0;
    }

    if (approve3 != null) {
      Status status3 = Status
        .builder()
        .indexOfSignature(3)
        .status(null)
        .training(training_id)
        .approveId(approve3)
        .active(active)
        .build();
      statusRepository.save(status3);
      active = 0;
    }

    if (approve4 != null) {
      Status status4 = Status
        .builder()
        .indexOfSignature(4)
        .status(null)
        .training(training_id)
        .approveId(approve4)
        .active(active)
        .build();
      statusRepository.save(status4);
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

  public ResultGeneric9 editGeneric9(
    Long gen9ID,
    EditGeneric9Result editTraining
  ) throws ParseException {
    ResultGeneric9 result_id = resultGeneric9Repository
      .findById(gen9ID)
      .orElseThrow(() -> new RuntimeException("ResultId not found: " + gen9ID));

    result_id.setResult1(editTraining.getResult1());
    result_id.setResult2(editTraining.getResult2());
    result_id.setResult3(editTraining.getResult3());
    result_id.setResult4(editTraining.getResult4());
    result_id.setResult5(editTraining.getResult5());

    ResultGeneric9 updatedTraining = resultGeneric9Repository.save(result_id);
    return updatedTraining;
  }
}
