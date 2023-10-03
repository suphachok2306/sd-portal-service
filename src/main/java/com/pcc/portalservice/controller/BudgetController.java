package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Budget;
import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Course;
import com.pcc.portalservice.repository.DepartmentRepository;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import com.pcc.portalservice.requests.CreateCourseRequest;
import com.pcc.portalservice.requests.CreateDepartmentRequest;
import com.pcc.portalservice.requests.CreatePositionRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.BudgetService;
import com.pcc.portalservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class BudgetController {

    private final BudgetService budgetService;


    @PostMapping("/createBudget")
    public ResponseEntity<ApiResponse> createBudget(@RequestBody CreateBudgetRequest createbudgetRequest) {
        Budget budget = budgetService.create(createbudgetRequest);
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(budgetService.isBudgetNull(budget)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            data.setResult(budget);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createBuget").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/findAllBudget")
    public List<Budget> getAllBudgets() {return budgetService.findAll();}

    @GetMapping("/findBudgetById")
    public ResponseEntity<Budget> findBudgetById(@RequestParam Long budgetID) {
        Budget budget= budgetService.findById(budgetID);
        if (budget != null) {
            return new ResponseEntity<>(budget, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/deleteBudgetById")
    public ResponseEntity<String> delete(
         @RequestParam Long budgetID
        ) {
            Budget budget = budgetService.deleteData(budgetID);
            if (budget != null) {
                return new ResponseEntity<>("ลบเรียบร้อย", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 


    @PutMapping("/editBudget")
    public ResponseEntity<Budget> updateBudget(
        @RequestBody CreateBudgetRequest createbudgetRequest
    ) {
        Budget budgetCourse = budgetService.editBudget(createbudgetRequest);
        if (budgetCourse != null) {
            return new ResponseEntity<>(budgetCourse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }        

    @GetMapping("/findTotalBudget")
    public List<Map<String, Object>> findTotal(@RequestParam String Year,Long sectorId) {
        List<Map<String, Object>> resultList = budgetService.total_exp(Year,sectorId);

        return resultList;
    }

    @GetMapping("/findRemainBudget")
    public List<Map<String, Object>> findReamin(@RequestParam String Year,Long sectorId) {
        List<Map<String, Object>> resultList = budgetService.totalPriceRemaining(Year,sectorId);

        return resultList;
    }
    
}

