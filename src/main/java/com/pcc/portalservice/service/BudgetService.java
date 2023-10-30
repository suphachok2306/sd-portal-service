package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.repository.BudgetRepository;
import com.pcc.portalservice.repository.CompanyRepository;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final SectorRepository sectorRepository;
  private final DepartmentRepository departmentRepository;
  private final CompanyRepository companyRepository;
  private final EntityManager entityManager;

  /**
   * @Create
   */
  public Budget create(CreateBudgetRequest createBudgetRequest) {
    Department departmentId = departmentRepository
    .findById(createBudgetRequest.getDepartmentId())
    .orElseThrow(() ->
        new RuntimeException(
          "sectorId not found: " + createBudgetRequest.getDepartmentId()
        )
      );

    Company companyId = companyRepository
      .findById(createBudgetRequest.getCompanyId())
      .orElseThrow(() ->
        new RuntimeException(
          "companyId not found: " + createBudgetRequest.getCompanyId()
        )
      );

    Budget budget = Budget
      .builder()
      .type(createBudgetRequest.getType())
      .department(departmentId)
      .company(companyId)
      .class_name(createBudgetRequest.getClassName())
      .remark(createBudgetRequest.getRemark())
      .year(createBudgetRequest.getYear())
      .numberOfPerson(createBudgetRequest.getNumberOfPerson())
      .fee(createBudgetRequest.getFee())
      .airAcc(createBudgetRequest.getAirAcc())
      .exp(
        totalExp(createBudgetRequest.getAirAcc(), createBudgetRequest.getFee())
      )
      .build();

    return budgetRepository.save(budget);
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
  public Budget editBudget(CreateBudgetRequest createBudgetRequest,Long budgetID) {
    Company companyName = companyRepository
      .findById(createBudgetRequest.getCompanyId())
      .orElseThrow(() ->
        new RuntimeException(
          "companyName not found: " + createBudgetRequest.getCompanyId()
        )
      );

    Department departmentId = departmentRepository
    .findById(createBudgetRequest.getDepartmentId())
    .orElseThrow(() ->
        new RuntimeException(
          "sectorId not found: " + createBudgetRequest.getDepartmentId()
        )
      );



    Budget budget = findById(budgetID);
    budget.setType(createBudgetRequest.getType());
    budget.setDepartment(departmentId);
    budget.setCompany(companyName);
    budget.setClass_name(createBudgetRequest.getClassName());
    budget.setYear(createBudgetRequest.getYear());
    budget.setRemark(createBudgetRequest.getRemark());
    budget.setNumberOfPerson(createBudgetRequest.getNumberOfPerson());
    budget.setFee(createBudgetRequest.getFee());
    budget.setAirAcc(createBudgetRequest.getAirAcc());
    budget.setExp(
      totalExp(createBudgetRequest.getFee(), createBudgetRequest.getAirAcc())
    );

    return budgetRepository.save(budget);
  }

  /**
   * @หารวมงบทั้งหมดของแต่ละปี
   */
  public LinkedHashMap<String, Object> total_exp(
    String year,
    long department_id
  ) {
    String jpql =
      "SELECT SUM(b.exp) AS total_exp FROM Budget b WHERE b.department.id = :department_id AND b.year = :year";

    Query query = entityManager.createQuery(jpql);
    query.setParameter("department_id", department_id);
    query.setParameter("year", year);

    List<Object> resultList = query.getResultList();

    LinkedHashMap<String, Object> result = new LinkedHashMap<>();

    if (!resultList.isEmpty()) {
      result.put("Year", year);
      result.put("Department", departmentRepository.findById(department_id));
      result.put("Total_exp", resultList.get(0));
    }

    return result;
  }

  /**
   * @หางบที่เหลือ
   */
  public LinkedHashMap<String, Object> totalPriceRemaining(
    int year,
    long department_id
  ) {
    try {
      LinkedHashMap<String, Object> total_exp = total_exp(
        Integer.toString(year),
        department_id
      );

      String jpql =
        "SELECT SUM(c.price) AS total_price " +
        "FROM training t " +
        "JOIN training_courses tc ON tc.training_id = t.id " +
        "JOIN course c ON c.id = tc.courses_id " +
        "JOIN users u ON u.id = t.user_id " +
        "WHERE u.department_id = :departmentId AND EXTRACT(YEAR FROM t.date_save) = :year ";

      Query query = entityManager.createNativeQuery(jpql);
      query.setParameter("year", year);
      query.setParameter("departmentId", department_id);
      List<Object> resultList = query.getResultList();

      LinkedHashMap<String, Object> result = new LinkedHashMap<>();
      Object total_budget = total_exp.get("Total_exp");
      Object total_price = resultList.get(0);

      if (total_price != null) {
        try {
          double total_budget_double = (double) total_budget;
          float total_price_float = (float) total_price;

          double difference = total_budget_double - total_price_float;

          if (!resultList.isEmpty()) {
            result.put("Year", year);
            result.put(
              "Department",
              departmentRepository.findById(department_id)
            );
            result.put("Remaining", difference);
          }
        } catch (Exception e) {
          System.out.println(
            "Error calculating total price remaining: " + e.getMessage()
          );
          return new LinkedHashMap<>();
        }
      }
      return result;
    } catch (Exception e) {
      System.out.print(e);
    }
    return null;
  }

  /**
   * @คำนวณงบ
   */
  public float totalExp(float fee, float airAcc) {
    float total = fee + airAcc;

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
      request.getYear().isEmpty() ||
      request.getClassName() == null ||
      request.getClassName().isEmpty()
    );
  }
}
