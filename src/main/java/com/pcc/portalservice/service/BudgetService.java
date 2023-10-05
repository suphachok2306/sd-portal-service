package com.pcc.portalservice.service;
import javax.persistence.Query;
import com.pcc.portalservice.model.Budget;
import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.repository.BudgetRepository;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import lombok.RequiredArgsConstructor;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final SectorRepository sectorRepository;
    private final EntityManager entityManager;

    public boolean isBudgetNull(CreateBudgetRequest request) {
        return request == null || request.getYear() == null || request.getYear().isEmpty()
                || request.getClassName() == null || request.getClassName().isEmpty()
                || Objects.equals(request.getAirAcc(),null) || Objects.equals(request.getFee(),null);
    }

     public float totalExp(float fee,float airAcc) {
               
        float total = fee + airAcc;
        
        return total;
    }

    public Budget create(CreateBudgetRequest createBudgetRequest) {

        Sector sectorId = sectorRepository.findById(createBudgetRequest.getSectorId())
                .orElseThrow(() -> new RuntimeException("sectorId not found: " + createBudgetRequest.getSectorId()));
        

                
        Budget budget = Budget.builder()
                .sector(sectorId)
                .class_name(createBudgetRequest.getClassName())
                .remark(createBudgetRequest.getRemark())
                .year(createBudgetRequest.getYear())
                .numberOfPerson(createBudgetRequest.getNumberOfPerson())
                .fee(createBudgetRequest.getFee())
                .airAcc(createBudgetRequest.getAirAcc())
                .exp(totalExp(createBudgetRequest.getAirAcc(),createBudgetRequest.getFee()))
                .build();
                
        return budgetRepository.save(budget);
    }

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " ไม่มีในระบบ"));
    }

    public Budget deleteData(Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " ไม่มีในระบบ"));

        budgetRepository.delete(budget);

        return budget;
    }

    public Budget editBudget(CreateBudgetRequest createBudgetRequest) {
        Budget budget = findById(createBudgetRequest.getBudgetId());
        budget.setClass_name(createBudgetRequest.getClassName());
        budget.setYear(createBudgetRequest.getYear());
        budget.setRemark(createBudgetRequest.getRemark());
        budget.setNumberOfPerson(createBudgetRequest.getNumberOfPerson());
        budget.setFee(createBudgetRequest.getFee());
        budget.setAirAcc(createBudgetRequest.getAirAcc());
        budget.setExp(totalExp(createBudgetRequest.getFee(),createBudgetRequest.getAirAcc()));

        return budgetRepository.save(budget);
    }

    public  LinkedHashMap<String, Object> total_exp(String year, long sector_id) {
        String jpql = "SELECT SUM(b.exp) AS total_exp FROM Budget b WHERE b.sector.id = :sectorId AND b.year LIKE :year";
    
        Query query = entityManager.createQuery(jpql);
        query.setParameter("sectorId", sector_id);
        query.setParameter("year", year + "%");
    
        List<Object> resultList = query.getResultList();
    
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    
        if (!resultList.isEmpty()) {
            result.put("Year", year);
            result.put("Sector", sectorRepository.findById(sector_id));
            result.put("Total_exp", resultList.get(0));
        }
    
        return result;
    }
    

    public LinkedHashMap<String, Object> totalPriceRemaining(int year, long sector_id) {
        try {
            LinkedHashMap<String, Object> total_exp = total_exp(Integer.toString(year), sector_id);
    
            String jpql = "SELECT SUM(c.price) AS total_price " +
                                    "FROM training t " +
                                    "JOIN training_courses tc ON tc.training_id = t.id " +
                                    "JOIN course c ON c.id = tc.courses_id " +
                                    "JOIN users u ON u.id = t.user_id " +
                                    "WHERE u.sector_id = :sectorId AND EXTRACT(YEAR FROM t.date_save) = :year ";
    
            Query query = entityManager.createNativeQuery(jpql);
            query.setParameter("year", year);
            query.setParameter("sectorId", sector_id);
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
                        result.put("Sector", sectorRepository.findById(sector_id));
                        result.put("Remaining", difference);
                    }
                } catch (Exception e) {
                    System.out.println("Error calculating total price remaining: " + e.getMessage());
                    return new LinkedHashMap<>();
                }
            }
            return result;
        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }
    
    
}

    
    
    


