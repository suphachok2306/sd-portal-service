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
      .totalExp(
        totalExp(
          createBudgetRequest.getBudgetCer(),
          createBudgetRequest.getBudgetTraining()
        )
      )
      .build();

    budget = budgetRepository.save(budget);

    checkBudget(createBudgetRequest.getDepartment_Id(),createBudgetRequest.getYear());

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
    budget.setTotalExp(
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
    float x = getTotalBudgetCer(departmentId,year);
    float y = getTotalBudgetTraining(departmentId,year);
    return x+y;
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

  private void checkBudget(Long department_id,String year) {
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
        Float x = getTotalBudgetCer(
         department_id,
         year
        );
        Float y = getTotalBudgetTraining(
          department_id,
         year
        );
        Float z = getTotalBudgetTotalExp(
         department_id,
         year
        );
        Department department = departmentRepository.findById(department_id).get();
        Company company = companyRepository.findById(department.getId()).get();
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
            company,
            department,
            year,
            "อบรม"
          );
          insertBudgetDepartment(
            y,
            company,
            department,
            year,
            "certificate"
          );
          insertBudgetDepartment(
            z,
            company,
            department,
            year,
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
    checkBudget(department_id,year);
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

    if (
      !resultListBudgetCer.isEmpty() &&
      !resultListBudgetTraining.isEmpty()
    ) {
      result.put("Year", year);
      result.put("Company",companyRepository.findById(departmentRepository.findById(department_id).get().getSector().getCompany().getId()));
      result.put("Department", departmentRepository.findById(department_id));
      result.put("งบ Certificate", resultListBudgetCer.get(0));
      result.put("งบ อบรม", resultListBudgetTraining.get(0));
      result.put("งบยอดรวม", (Double)resultListBudgetCer.get(0) + (Double) resultListBudgetTraining.get(0));
    }

    return result;
  }

  public LinkedHashMap<String, Object> totalPriceRemaining(
    String year,
    long department_id
  ) {
    checkBudget(department_id,year);
    String jpqlBudgetCer =
      "SELECT SUM(c.price) AS total_price FROM Training t JOIN t.courses c " +
      "WHERE t.user.department.id = :departmentId AND EXTRACT(YEAR FROM t.dateSave) = :year AND c.type = 'สอบ' ";

    String jpqlBudgetTraining =
      "SELECT SUM(c.price) AS total_price FROM Training t JOIN t.courses c " +
      "WHERE t.user.department.id = :departmentId AND EXTRACT(YEAR FROM t.dateSave) = :year AND c.type = 'course' ";

    Query queryBudgetCer = entityManager.createQuery(jpqlBudgetCer);
    queryBudgetCer.setParameter("departmentId", department_id);
    queryBudgetCer.setParameter("year", Integer.parseInt(year));

    Query queryBudgetTraining = entityManager.createQuery(jpqlBudgetTraining);
    queryBudgetTraining.setParameter("departmentId", department_id);
    queryBudgetTraining.setParameter("year", Integer.parseInt(year));

    List<Object> resultListBudgetCer = queryBudgetCer.getResultList();
    List<Object> resultListBudgetTraining = queryBudgetTraining.getResultList();

    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    try {
      LinkedHashMap<String, Object> all_budget = total_exp(year, department_id);

      Double certificate = (Double) all_budget.get("งบ Certificate") -
      (
        resultListBudgetCer.get(0) != null
          ? ((Double) resultListBudgetCer.get(0))
          : 0
      );
      Double train = (Double) all_budget.get("งบ อบรม") -
      (
        resultListBudgetTraining.get(0) != null
          ? ((Double) resultListBudgetTraining.get(0))
          : 0
      );
      Double total_use = certificate +train;
      if (
        !resultListBudgetCer.isEmpty() && !resultListBudgetTraining.isEmpty()
      ) {
        result.put("Year", year);
        result.put("Company",companyRepository.findById(departmentRepository.findById(department_id).get().getSector().getCompany().getId()));
        result.put("Department", departmentRepository.findById(department_id));
        result.put("งบ Certificate", certificate);
        result.put("งบ อบรม", train);
        result.put("งบยอดรวม", total_use);
      }

      return result;
    } catch (Exception e) {
      System.out.println(e);
    }
    return result;
  }
}
