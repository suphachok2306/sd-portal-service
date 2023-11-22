package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.repository.BudgetRepository;
import com.pcc.portalservice.repository.Budget_DepartmentRepository;
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
  private final Budget_DepartmentRepository budget_DepartmentRepository;
  private final DepartmentRepository departmentRepository;
  private final CompanyRepository companyRepository;
  private final EntityManager entityManager;

  /**
   * @Create
   */
  public Budget create(CreateBudgetRequest createBudgetRequest) {
    autogen();
    Department departmentId = departmentRepository
      .findById(createBudgetRequest.getDepartment_Id())
      .orElseThrow(() ->
        new RuntimeException(
          "departmentId not found: " + createBudgetRequest.getDepartment_Id()
        )
      );
    Budget budget = Budget
      .builder()
      .department(departmentId)
      .company(departmentId.getSector().getCompany())
      .year(createBudgetRequest.getYear())
      .budgetCer(createBudgetRequest.getBudgetCer())
      .budgetTraining(createBudgetRequest.getBudgetTraining())
      .total_exp(
        totalExp(
          createBudgetRequest.getBudgetCer(),
          createBudgetRequest.getBudgetTraining()
        )
      )
      .build();
    budget = budgetRepository.save(budget);
    checkBudget(
      createBudgetRequest.getDepartment_Id(),
      createBudgetRequest.getYear()
    );

    return budget;
  }

  /**
   * @Delete
   */
  public String deleteData(Long id) {
    autogen();
    Optional<Budget> optionalBudget = budgetRepository.findById(id);
    if (optionalBudget.isPresent()) {
      budgetRepository.deleteById(id);
      return "ลบข้อมูลของ ID : " + id + " เรียบร้อย";
    } else {
      return null;
    }
  }

  /**
   * @Edit
   */
  public Budget editBudget(
    CreateBudgetRequest createBudgetRequest,
    Long budgetID
  ) {
    autogen();
    Company companyName = companyRepository
      .findById(createBudgetRequest.getCompany_Id())
      .orElseThrow(() ->
        new RuntimeException(
          "companyName not found: " + createBudgetRequest.getCompany_Id()
        )
      );

    Department departmentId = departmentRepository
      .findById(createBudgetRequest.getDepartment_Id())
      .orElseThrow(() ->
        new RuntimeException(
          "sectorId not found: " + createBudgetRequest.getDepartment_Id()
        )
      );

    Budget budget = findById(budgetID);
    budget.setDepartment(departmentId);
    budget.setCompany(companyName);
    budget.setYear(createBudgetRequest.getYear());
    budget.setBudgetCer(createBudgetRequest.getBudgetCer());
    budget.setBudgetTraining(createBudgetRequest.getBudgetTraining());
    budget.setTotal_exp(
      totalExp(
        createBudgetRequest.getBudgetCer(),
        createBudgetRequest.getBudgetTraining()
      )
    );

    return budgetRepository.save(budget);
  }

  public BigDecimal getTotalBudgetCer(Long departmentId, String year) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(
      BigDecimal.class
    );
    Root<Budget> bd = criteriaQuery.from(Budget.class);

    criteriaQuery.select(builder.sum(bd.get("budgetCer").as(BigDecimal.class)));
    criteriaQuery.where(
      builder.equal(bd.get("department").get("id"), departmentId),
      builder.equal(bd.get("year"), year)
    );

    TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(
      criteriaQuery
    );
    BigDecimal result = typedQuery.getSingleResult();

    result = result.setScale(2, RoundingMode.HALF_UP);

    return result;
  }

  public BigDecimal getTotalBudgetTraining(Long departmentId, String year) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(
      BigDecimal.class
    );
    Root<Budget> bd = criteriaQuery.from(Budget.class);

    criteriaQuery.select(
      builder.sum(bd.get("budgetTraining").as(BigDecimal.class))
    );
    criteriaQuery.where(
      builder.equal(bd.get("department").get("id"), departmentId),
      builder.equal(bd.get("year"), year)
    );

    TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(
      criteriaQuery
    );
    return typedQuery.getSingleResult();
  }

  public BigDecimal getTotalBudgetTotalExp(Long departmentId, String year) {
    BigDecimal x = getTotalBudgetCer(departmentId, year);
    BigDecimal y = getTotalBudgetTraining(departmentId, year);
    return x.add(y);
  }

  public void insertBudgetDepartment(
    BigDecimal x,
    Company company,
    Department department,
    String year,
    String type
  ) {
    Budget_Department budget_Department = Budget_Department
      .builder()
      .total_exp(x)
      .type(type)
      .year(year)
      .company(company)
      .department(department)
      .build();

    budget_DepartmentRepository.save(budget_Department);
  }

  public void UpdateBudgetDepartment(Long budgetId, BigDecimal y) {
    Budget_Department budget_Department = budget_DepartmentRepository
      .findById(budgetId)
      .orElseThrow(() -> new RuntimeException("budgetId not found: " + budgetId)
      );
    budget_Department.setTotal_exp(y);

    budget_DepartmentRepository.save(budget_Department);
  }

  /**
   * @คำนวณงบ
   */
  public float totalExp(float certificate, float training) {
    float total = certificate + training;

    return total;
  }

  /**
   * @หางบทั้งหมด
   */
  public List<Budget> findAll() {
    return budgetRepository.findAll();
  }

  /**
   * @หางบด้วยId
   */
  public Budget findById(Long id) {
    autogen();
    return budgetRepository
      .findById(id)
      .orElseThrow(() ->
        new EntityNotFoundException("ID " + id + " ไม่มีในระบบ")
      );
  }

  /**
   * @เช็คNullของBudget
   */
  public boolean isBudgetNull(CreateBudgetRequest request) {
    return (
      request == null ||
      request.getYear() == null ||
      request.getYear().isEmpty()
    );
  }

  private void checkBudget(Long department_id, String year) {
    if (department_id != null && year != null) {
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<Budget_Department> criteriaQuery = builder.createQuery(
        Budget_Department.class
      );
      Root<Budget_Department> bd = criteriaQuery.from(Budget_Department.class);

      Predicate yearPredicate = builder.equal(bd.get("year"), year);
      Predicate departmentPredicate = builder.equal(
        bd.get("department"),
        departmentRepository.findById(department_id).get()
      );

      Predicate predicate = builder.and(yearPredicate, departmentPredicate);
      criteriaQuery.where(predicate);

      TypedQuery<Budget_Department> typedQuery = entityManager.createQuery(
        criteriaQuery
      );

      if (typedQuery != null) {
        BigDecimal x = getTotalBudgetCer(department_id, year);
        BigDecimal y = getTotalBudgetTraining(department_id, year);
        BigDecimal z = getTotalBudgetTotalExp(department_id, year);
        Department department = departmentRepository
          .findById(department_id)
          .get();
        Optional<Company> company = companyRepository.findById(
          department.getSector().getCompany().getId()
        );
        List<Budget_Department> resultList = typedQuery.getResultList();
        resultList.sort(Comparator.comparing(Budget_Department::getId));
        if (resultList.size() == 3) {
          for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).getType().toString().equals("อบรม")) {
              UpdateBudgetDepartment(resultList.get(i).getId(), y);
            } else if (
              resultList.get(i).getType().toString().equals("certificate")
            ) {
              UpdateBudgetDepartment(resultList.get(i).getId(), x);
            } else if (
              resultList.get(i).getType().toString().equals("ยอดรวม")
            ) {
              UpdateBudgetDepartment(resultList.get(i).getId(), z);
            }
          }
        } else if (resultList.size() <= 0) {
          insertBudgetDepartment(x, company.get(), department, year, "อบรม");
          insertBudgetDepartment(
            y,
            company.get(),
            department,
            year,
            "certificate"
          );
          insertBudgetDepartment(z, company.get(), department, year, "ยอดรวม");
        }
      }
    }
  }

  public LinkedHashMap<String, Object> total_exp(
    String year,
    long department_id
  ) {
    autogen();
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
    autogen();
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
      System.out.println(e);
    }
    return result;
  }

  public List<LinkedHashMap<String, Object>> find_budget(
    Long company_id,
    String year,
    Long department_id
  ) {
    autogen();
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
    autogen();
    List<Budget> budgets = budgetRepository.findAll();
    List<Map<String, Object>> result = new ArrayList<>();

    for (Budget budget : budgets) {
      if (!(budget.getBudgetCer() == 0 || budget.getBudgetTraining() == 0)) {
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

  public void autogen() {
    List<Department> data = departmentRepository.findAll();
    int currentYear = LocalDate.now().getYear();

    try {
      Query query = entityManager.createNativeQuery(
        "SELECT DISTINCT bd.department_id FROM Budget_Department bd WHERE bd.year = :year ORDER BY bd.department_id"
      );
      query.setParameter("year", String.valueOf(currentYear));
      List<BigInteger> results = query.getResultList();
      List<Long> result_long = new ArrayList<>();
      for (BigInteger i : results) {
        result_long.add(i.longValue());
      }

      for (Department datas : data) {
        if (!result_long.contains(datas.getId())) {
          Optional<Department> department = departmentRepository.findById(
            datas.getId()
          );
          Company companyName = companyRepository
            .findById(department.get().getSector().getCompany().getId())
            .orElseThrow(() ->
              new RuntimeException(
                "companyName not found: " +
                department.get().getSector().getCompany().getId()
              )
            );
          Department departmentId = departmentRepository
            .findById(department.get().getId())
            .orElseThrow(() ->
              new RuntimeException(
                "departmentId not found: " + department.get().getId()
              )
            );
          Budget budget = Budget
            .builder()
            .department(departmentId)
            .company(companyName)
            .year(String.valueOf(currentYear))
            .budgetCer(0)
            .budgetTraining(0)
            .total_exp(totalExp(0, 0))
            .build();
          budget = budgetRepository.save(budget);
          checkBudget(departmentId.getId(), String.valueOf(currentYear));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
