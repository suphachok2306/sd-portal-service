package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.repository.BudgetRepository;
import com.pcc.portalservice.repository.Budget_DepartmentRepository;
import com.pcc.portalservice.repository.CompanyRepository;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final SectorRepository sectorRepository;
  private final Budget_DepartmentRepository budget_DepartmentRepository;
  private final DepartmentRepository departmentRepository;
  private final CompanyRepository companyRepository;
  private final EntityManager entityManager;

  /**
   * @Create
   */
  public Budget create(CreateBudgetRequest createBudgetRequest) {
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
          "departmentId not found: " + createBudgetRequest.getDepartment_Id()
        )
      );
    Budget budget = Budget
      .builder()
      .department(departmentId)
      .company(companyName)
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

    checkBudget(budget);

    return budget;
  }

  /**
   * @Delete
   */
  public String deleteData(Long id) {
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

  public Float getTotalBudgetCer(Long departmentId, String year) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Float> criteriaQuery = builder.createQuery(Float.class);
    Root<Budget> bd = criteriaQuery.from(Budget.class);

    criteriaQuery.select(builder.sum(bd.get("budgetCer").as(Float.class)));
    criteriaQuery.where(
      builder.equal(bd.get("department").get("id"), departmentId),
      builder.equal(bd.get("year"), year)
    );

    TypedQuery<Float> typedQuery = entityManager.createQuery(criteriaQuery);
    return typedQuery.getSingleResult();
  }

  public Float getTotalBudgetTraining(Long departmentId, String year) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Float> criteriaQuery = builder.createQuery(Float.class);
    Root<Budget> bd = criteriaQuery.from(Budget.class);

    criteriaQuery.select(builder.sum(bd.get("budgetTraining").as(Float.class)));
    criteriaQuery.where(
      builder.equal(bd.get("department").get("id"), departmentId),
      builder.equal(bd.get("year"), year)
    );

    TypedQuery<Float> typedQuery = entityManager.createQuery(criteriaQuery);
    return typedQuery.getSingleResult();
  }

  public Float getTotalBudgetTotalExp(Long departmentId, String year) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Float> criteriaQuery = builder.createQuery(Float.class);
    Root<Budget> bd = criteriaQuery.from(Budget.class);

    criteriaQuery.select(builder.sum(bd.get("total_exp").as(Float.class)));
    criteriaQuery.where(
      builder.equal(bd.get("department").get("id"), departmentId),
      builder.equal(bd.get("year"), year)
    );

    TypedQuery<Float> typedQuery = entityManager.createQuery(criteriaQuery);
    return typedQuery.getSingleResult();
  }

  public void insertBudgetDepartment(
    Float x,
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

  public void UpdateBudgetDepartment(Long budgetId, Float value) {
    Budget_Department budget_Department = budget_DepartmentRepository
      .findById(budgetId)
      .orElseThrow(() -> new RuntimeException("budgetId not found: " + budgetId)
      );

    budget_Department.setTotal_exp(value);

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

  private void checkBudget(Budget budget) {
    if (budget != null) {
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<Budget_Department> criteriaQuery = builder.createQuery(
        Budget_Department.class
      );
      Root<Budget_Department> bd = criteriaQuery.from(Budget_Department.class);

      Predicate yearPredicate = builder.equal(bd.get("year"), budget.getYear());
      Predicate departmentPredicate = builder.equal(
        bd.get("department"),
        budget.getDepartment()
      );

      Predicate predicate = builder.and(yearPredicate, departmentPredicate);
      criteriaQuery.where(predicate);

      TypedQuery<Budget_Department> typedQuery = entityManager.createQuery(
        criteriaQuery
      );
      if (typedQuery != null) {
        Float x = getTotalBudgetCer(
          budget.getDepartment().getId(),
          budget.getYear()
        );
        Float y = getTotalBudgetTraining(
          budget.getDepartment().getId(),
          budget.getYear()
        );
        Float z = getTotalBudgetTotalExp(
          budget.getDepartment().getId(),
          budget.getYear()
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
          insertBudgetDepartment(
            x,
            budget.getCompany(),
            budget.getDepartment(),
            budget.getYear(),
            "อบรม"
          );
          insertBudgetDepartment(
            y,
            budget.getCompany(),
            budget.getDepartment(),
            budget.getYear(),
            "certificate"
          );
          insertBudgetDepartment(
            z,
            budget.getCompany(),
            budget.getDepartment(),
            budget.getYear(),
            "ยอดรวม"
          );
        }
      }
    }
  }

  public LinkedHashMap<String, Object> total_exp(
    String year,
    long department_id
  ) {
    String jpqlBudgetCer =
      "SELECT SUM(b.budgetCer) AS total_budget_cer " +
      "FROM Budget b " +
      "WHERE b.department.id = :department_id AND b.year = :year";

    String jpqlBudgetTraining =
      "SELECT SUM(b.budgetTraining) AS total_budget_training " +
      "FROM Budget b " +
      "WHERE b.department.id = :department_id AND b.year = :year";

    String jpqlTotalExp =
      "SELECT SUM(b.total_exp) AS total_total_exp " +
      "FROM Budget b " +
      "WHERE b.department.id = :department_id AND b.year = :year";

    Query queryBudgetCer = entityManager.createQuery(jpqlBudgetCer);
    queryBudgetCer.setParameter("department_id", department_id);
    queryBudgetCer.setParameter("year", year);

    Query queryBudgetTraining = entityManager.createQuery(jpqlBudgetTraining);
    queryBudgetTraining.setParameter("department_id", department_id);
    queryBudgetTraining.setParameter("year", year);

    Query queryTotalExp = entityManager.createQuery(jpqlTotalExp);
    queryTotalExp.setParameter("department_id", department_id);
    queryTotalExp.setParameter("year", year);

    List<Object> resultListBudgetCer = queryBudgetCer.getResultList();
    List<Object> resultListBudgetTraining = queryBudgetTraining.getResultList();
    List<Object> resultListTotalExp = queryTotalExp.getResultList();

    LinkedHashMap<String, Object> result = new LinkedHashMap<>();

    if (
      !resultListBudgetCer.isEmpty() &&
      !resultListBudgetTraining.isEmpty() &&
      !resultListTotalExp.isEmpty()
    ) {
      result.put("Year", year);
      result.put("Department", departmentRepository.findById(department_id));
      result.put("งบ Certificate", resultListBudgetCer.get(0));
      result.put("งบ อบรม", resultListBudgetTraining.get(0));
      result.put("งบยอดรวม", resultListTotalExp.get(0));
    }

    return result;
  }

  public LinkedHashMap<String, Object> totalPriceRemaining(
    String year,
    long department_id
  ) {
    String jpqlBudgetCer = " SELECT SUM(c.price) AS total_price FROM training t JOIN training_courses tc ON tc.training_id = t.id "+
    "JOIN course c ON c.id = tc.courses_id JOIN users u ON u.id = t.user_id "+ 
    "WHERE u.department_id = :departmentId AND EXTRACT(YEAR FROM t.date_save) = :year and c.type = 'สอบ' ";
      
    String jpqlBudgetTraining =
    " SELECT SUM(c.price) AS total_price FROM training t JOIN training_courses tc ON tc.training_id = t.id "+
    "JOIN course c ON c.id = tc.courses_id JOIN users u ON u.id = t.user_id "+ 
    "WHERE u.department_id = :departmentId AND EXTRACT(YEAR FROM t.date_save) = :year and c.type = 'course' ";

    String jpqlTotalExp =
      "SELECT SUM(b.total_exp) AS total_total_exp " +
      "FROM Budget b " +
      "WHERE b.department.id = :department_id AND b.year = :year";

    Query queryBudgetCer = entityManager.createQuery(jpqlBudgetCer);
    queryBudgetCer.setParameter("department_id", department_id);
    queryBudgetCer.setParameter("year", year);

    Query queryBudgetTraining = entityManager.createQuery(jpqlBudgetTraining);
    queryBudgetTraining.setParameter("department_id", department_id);
    queryBudgetTraining.setParameter("year", year);

    Query queryTotalExp = entityManager.createQuery(jpqlTotalExp);
    queryTotalExp.setParameter("department_id", department_id);
    queryTotalExp.setParameter("year", year);

    List<Object> resultListBudgetCer = queryBudgetCer.getResultList();
    List<Object> resultListBudgetTraining = queryBudgetTraining.getResultList();
    List<Object> resultListTotalExp = queryTotalExp.getResultList();

    LinkedHashMap<String, Object> result = new LinkedHashMap<>();

    if (
      !resultListBudgetCer.isEmpty() &&
      !resultListBudgetTraining.isEmpty() &&
      !resultListTotalExp.isEmpty()
    ) {
      result.put("Year", year);
      result.put("Department", departmentRepository.findById(department_id));
      result.put("งบ Certificate", resultListBudgetCer.get(0));
      result.put("งบ อบรม", resultListBudgetTraining.get(0));
      result.put("งบยอดรวม", resultListTotalExp.get(0));
    }

    return result;
  }
}
