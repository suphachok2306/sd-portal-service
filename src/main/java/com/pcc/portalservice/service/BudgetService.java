package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.repository.BudgetRepository;
import com.pcc.portalservice.repository.CompanyRepository;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final DepartmentRepository departmentRepository;
  private final CompanyRepository companyRepository;
  private final EntityManager entityManager;

  public Budget saveOrUpdate(CreateBudgetRequest createBudgetRequest) {
    try {
      Long department_Id = createBudgetRequest.getDepartment_Id();
      String year = createBudgetRequest.getYear();
      Company company = companyRepository
        .findById(createBudgetRequest.getCompany_Id())
        .orElseThrow(() ->
          new RuntimeException(
            "companyName not found: " + createBudgetRequest.getCompany_Id()
          )
        );
      Department department = departmentRepository
        .findById(department_Id)
        .orElseThrow(() ->
          new RuntimeException("departmentId not found: " + department_Id)
        );

      Budget existingBudget = budgetRepository.findByYearAndDepartment(
        year,
        department
      );

      Budget budget;
      List<Float> trainingIds = getTrainingIds(year, department_Id, "สอบ");
      List<Float> trainingIdsTrain = getTrainingIds(
        year,
        department_Id,
        "อบรม"
      );
      List<Object> resultListBudgetCer = getBudget("สอบ", trainingIds);
      List<Object> resultListBudgetTraining = getBudget(
        "อบรม",
        trainingIdsTrain
      );
      if (existingBudget == null) {
        budget = new Budget();
        budget.setDepartment(department);
        budget.setCompany(company);
        budget.setYear(year);
        budget.setBudgetCer(createBudgetRequest.getBudgetCer());
        budget.setBudgetTraining(createBudgetRequest.getBudgetTraining());
        budget.setTotal_exp(
          totalExp(
            createBudgetRequest.getBudgetCer(),
            createBudgetRequest.getBudgetTraining()
          )
        );
        return budgetRepository.save(budget);
      } else {
        budget = existingBudget;
        if (
          createBudgetRequest.getBudgetCer() -
          (
            (
              resultListBudgetCer.get(0) != null
                ? ((Double) resultListBudgetCer.get(0)).doubleValue()
                : 0
            )
          ) >=
          0 &&
          createBudgetRequest.getBudgetTraining() -
          (
            (
              resultListBudgetTraining.get(0) != null
                ? ((Double) resultListBudgetTraining.get(0)).doubleValue()
                : 0
            )
          ) >=
          0
        ) {
          budget.setBudgetCer(createBudgetRequest.getBudgetCer());
          budget.setBudgetTraining(createBudgetRequest.getBudgetTraining());
          budget.setTotal_exp(
            totalExp(
              createBudgetRequest.getBudgetCer(),
              createBudgetRequest.getBudgetTraining()
            )
          );
          return budgetRepository.save(budget);
        } else {
          throw new RuntimeException("งบที่อัพเดตมีค่าน้อยกว่าที่ใช้ไปแล้ว");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("ไม่มีงบในระบบ", e);
    }
  }

  public float totalExp(float certificate, float training) {
    float total = certificate + training;

    return total;
  }

  public List<Budget> findAll() {
    return budgetRepository.findAll();
  }

  public Budget findById(Long id) {
    return budgetRepository
      .findById(id)
      .orElseThrow(() ->
        new EntityNotFoundException("ID " + id + " ไม่มีในระบบ")
      );
  }

  public boolean isBudgetNull(CreateBudgetRequest request) {
    return (
      request == null ||
      request.getYear() == null ||
      request.getYear().isEmpty()
    );
  }

  private void checkBudget(Long department_id, String year) {}

  public LinkedHashMap<String, Object> total_exp(
    String year,
    long department_id
  ) {
    checkBudget(department_id, year);

    String jpqlBudgetCer =
      "SELECT SUM(b.budgetCer) AS total_budget_cer " +
      "FROM Budget b " +
      "WHERE b.department.id = :department_id AND b.year = :year";

    String jpqlBudgetTraining =
      "SELECT SUM(b.budgetTraining) AS total_budget_training " +
      "FROM Budget b " +
      "WHERE b.department.id = :department_id AND b.year = :year";

    Query queryBudgetCer = entityManager.createQuery(jpqlBudgetCer);
    queryBudgetCer.setParameter("department_id", department_id);
    queryBudgetCer.setParameter("year", year);

    Query queryBudgetTraining = entityManager.createQuery(jpqlBudgetTraining);
    queryBudgetTraining.setParameter("department_id", department_id);
    queryBudgetTraining.setParameter("year", year);

    List<Object> resultListBudgetCer = queryBudgetCer.getResultList();
    List<Object> resultListBudgetTraining = queryBudgetTraining.getResultList();

    LinkedHashMap<String, Object> result = new LinkedHashMap<>();

    if (!resultListBudgetCer.isEmpty() && !resultListBudgetTraining.isEmpty()) {
      result.put("Year", year);
      result.put(
        "Company",
        companyRepository.findById(
          departmentRepository
            .findById(department_id)
            .get()
            .getSector()
            .getCompany()
            .getId()
        )
      );
      result.put("Department", departmentRepository.findById(department_id));

      BigDecimal budgetCer = new BigDecimal(
        resultListBudgetCer.get(0).toString()
      );
      BigDecimal budgetTraining = new BigDecimal(
        resultListBudgetTraining.get(0).toString()
      );

      result.put("budgetCer", budgetCer.setScale(2, RoundingMode.HALF_UP));
      result.put(
        "budgetTrain",
        budgetTraining.setScale(2, RoundingMode.HALF_UP)
      );
      result.put(
        "budgetTotal",
        budgetCer.add(budgetTraining).setScale(2, RoundingMode.HALF_UP)
      );
    }

    return result;
  }

  private List<Float> getTrainingIds(
    String year,
    long department_id,
    String courseType
  ) {
    String trainingIdsQuery =
      "SELECT DISTINCT t.id " +
      "FROM Training t " +
      "JOIN t.courses c " +
      "LEFT JOIN t.status s " +
      "JOIN t.user u " +
      "JOIN u.departments ud " +
      "WHERE ud.id = :departmentId " +
      "AND EXTRACT(YEAR FROM t.dateSave) = :year " +
      "AND c.active != 'ยกเลิก' " +
      "AND c.type = :courseType " +
      "AND (s.status IS NULL OR s.status != 'ยกเลิก') " +
      "AND (c.priceProject IS NULL OR c.priceProject = '')";

    Query query = entityManager.createQuery(trainingIdsQuery);
    query.setParameter("departmentId", department_id);
    query.setParameter("year", Integer.parseInt(year));
    query.setParameter("courseType", courseType);

    return query.getResultList();
  }

  private List<Object> getBudget(String courseType, List<Float> trainingIds) {
    String jpqlBudget =
      "SELECT SUM(c.price) " +
      "FROM Training t " +
      "JOIN t.courses c " +
      "WHERE t.id IN :trainingIds";

    Query queryBudget = entityManager.createQuery(jpqlBudget);
    queryBudget.setParameter("trainingIds", trainingIds);

    List<Object> resultListBudget = queryBudget.getResultList();

    return resultListBudget.isEmpty()
      ? Collections.emptyList()
      : resultListBudget;
  }

  public LinkedHashMap<String, Object> totalPriceRemaining(
    String year,
    long department_id
  ) {
    checkBudget(department_id, year);

    List<Float> trainingIds = getTrainingIds(year, department_id, "สอบ");
    List<Float> trainingIdsTrain = getTrainingIds(year, department_id, "อบรม");

    List<Object> resultListBudgetCer = getBudget("สอบ", trainingIds);
    List<Object> resultListBudgetTraining = getBudget("อบรม", trainingIdsTrain);

    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    try {
      LinkedHashMap<String, Object> all_budget = total_exp(year, department_id);

      BigDecimal budgetCer = (BigDecimal) all_budget.get("budgetCer");
      BigDecimal budgetTrain = (BigDecimal) all_budget.get("budgetTrain");

      BigDecimal certificate = budgetCer.subtract(
        (
          resultListBudgetCer.get(0) != null
            ? new BigDecimal(resultListBudgetCer.get(0).toString())
            : BigDecimal.ZERO
        )
      );
      BigDecimal train = budgetTrain.subtract(
        (
          resultListBudgetTraining.get(0) != null
            ? new BigDecimal(resultListBudgetTraining.get(0).toString())
            : BigDecimal.ZERO
        )
      );

      System.out.println(resultListBudgetTraining.get(0));
      System.out.println(resultListBudgetCer.get(0));

      BigDecimal total_use = certificate.add(train);

      if (
        !resultListBudgetCer.isEmpty() && !resultListBudgetTraining.isEmpty()
      ) {
        result.put("Year", year);
        result.put(
          "Company",
          companyRepository.findById(
            departmentRepository
              .findById(department_id)
              .get()
              .getSector()
              .getCompany()
              .getId()
          )
        );
        result.put("Department", departmentRepository.findById(department_id));
        result.put(
          "budgetCer",
          certificate.setScale(2, BigDecimal.ROUND_HALF_UP)
        );
        result.put("budgetTrain", train.setScale(2, BigDecimal.ROUND_HALF_UP));
        result.put(
          "budgetTotal",
          total_use.setScale(2, BigDecimal.ROUND_HALF_UP)
        );
      }

      return result;
    } catch (Exception e) {
        throw new RuntimeException("ไม่มีงบในระบบ", e);
    }
  }

  public List<LinkedHashMap<String, Object>> find_budget(
    Long company_id,
    String year,
    Long department_id
  ) {
    String jpql = "select * from budget where 1=1";

    if (year != null) {
      jpql += " and year = :year";
    }

    if (company_id != null) {
      jpql += " and company_id = :company_id";
    }

    if (department_id != null) {
      jpql += " and department_id = :department_id";
    }

    NativeQuery<Budget> query = (NativeQuery<Budget>) entityManager.createNativeQuery(
      jpql,
      Budget.class
    );

    if (year != null) {
      query.setParameter("year", year);
    }

    if (company_id != null) {
      query.setParameter("company_id", company_id);
    }

    if (department_id != null) {
      query.setParameter("department_id", department_id);
    }

    List<Budget> resultList = (List<Budget>) query.getResultList();

    List<LinkedHashMap<String, Object>> budgetMapList = new ArrayList<>();

    for (Budget budget : resultList) {
      LinkedHashMap<String, Object> budgetMap = new LinkedHashMap<>();

      budgetMap.put("id", budget.getId());
      budgetMap.put("year", budget.getYear());

      // Convert float values to BigDecimal and setScale for two decimal places
      BigDecimal budgetTraining = BigDecimal
        .valueOf(budget.getBudgetTraining())
        .setScale(2, BigDecimal.ROUND_HALF_UP);
      BigDecimal budgetCer = BigDecimal
        .valueOf(budget.getBudgetCer())
        .setScale(2, BigDecimal.ROUND_HALF_UP);
      BigDecimal total_exp = BigDecimal
        .valueOf(budget.getTotal_exp())
        .setScale(2, BigDecimal.ROUND_HALF_UP);

      budgetMap.put("budgetTraining", budgetTraining);
      budgetMap.put("budgetCer", budgetCer);
      budgetMap.put("total_exp", total_exp);
      budgetMap.put("company", budget.getCompany().getCompanyName());

      Map<String, Object> departmentMap = new HashMap<>();
      departmentMap.put("deptid", budget.getDepartment().getId());
      departmentMap.put("deptname", budget.getDepartment().getDeptName());
      departmentMap.put("deptcode", budget.getDepartment().getDeptCode());

      budgetMap.put("department", departmentMap);

      budgetMapList.add(budgetMap);
    }

    return budgetMapList;
  }

  public List<Map<String, Object>> findAlls() {
    List<Budget> budgets = budgetRepository.findAll();
    List<Map<String, Object>> result = new ArrayList<>();

    for (Budget budget : budgets) {
      if (!(budget.getBudgetCer() == 0 && budget.getBudgetTraining() == 0)) {
        Map<String, Object> budgetMap = new HashMap<>();
        budgetMap.put("id", budget.getId());
        budgetMap.put("year", budget.getYear());

        BigDecimal budgetTraining = BigDecimal
          .valueOf(budget.getBudgetTraining())
          .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal budgetCer = BigDecimal
          .valueOf(budget.getBudgetCer())
          .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal total_exp = BigDecimal
          .valueOf(budget.getTotal_exp())
          .setScale(2, BigDecimal.ROUND_HALF_UP);

        budgetMap.put("budgetTraining", budgetTraining);
        budgetMap.put("budgetCer", budgetCer);
        budgetMap.put("total_exp", total_exp);
        budgetMap.put("company", budget.getCompany().getCompanyName());

        Map<String, Object> departmentMap = new HashMap<>();
        departmentMap.put("deptid", budget.getDepartment().getId());
        departmentMap.put("deptname", budget.getDepartment().getDeptName());
        departmentMap.put("deptcode", budget.getDepartment().getDeptCode());

        budgetMap.put("department", departmentMap);

        result.add(budgetMap);
      }
    }
    return result;
  }
}
