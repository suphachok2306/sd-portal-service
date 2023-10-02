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
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final SectorRepository sectorRepository;
    private final EntityManager entityManager;

    public boolean isBudgetNull(Budget budget) {
        return budget == null || Objects.equals(budget.getAirAcc(), null) || Objects.equals(budget.getFee(), null) || budget.getYear().isEmpty() ;
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
                .class_name(createBudgetRequest.getClass_name())
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
        budget.setClass_name(createBudgetRequest.getClass_name());
        budget.setYear(createBudgetRequest.getYear());
        budget.setRemark(createBudgetRequest.getRemark());
        budget.setNumberOfPerson(createBudgetRequest.getNumberOfPerson());
        budget.setFee(createBudgetRequest.getFee());
        budget.setAirAcc(createBudgetRequest.getAirAcc());
        budget.setExp(totalExp(createBudgetRequest.getFee(),createBudgetRequest.getAirAcc()));

        return budgetRepository.save(budget);
    }

    public List<Map<String, Object>> total_exp(String year, long sector_id) {
        String jpql = "SELECT SUM(exp) AS total_exp FROM Budget WHERE sector_id = :sectorId AND year = :year";
    
        Query query = entityManager.createQuery(jpql);
        query.setParameter("sectorId", sector_id);
        query.setParameter("year", year);
    
        List<Object> resultList = query.getResultList();
    
        List<Map<String, Object>> result = new ArrayList<>();
    
        if (!resultList.isEmpty()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("total_exp", resultList.get(0));
            result.add(resultMap);
        }
    
        return result;
    }
    


}
