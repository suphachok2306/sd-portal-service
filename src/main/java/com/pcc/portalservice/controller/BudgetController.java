package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.*;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/createBudget")
    public ResponseEntity<ApiResponse> createBudget(@RequestBody CreateBudgetRequest createbudgetRequest) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(budgetService.isBudgetNull(createbudgetRequest)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Budget budget = budgetService.create(createbudgetRequest);
            data.setResult(budget);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createBudget").toUriString());
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
        return ResponseEntity.ok(budget);
    }


    @DeleteMapping("/deleteBudgetById")
    public ResponseEntity<ApiResponse> delete(@RequestParam Long budgetID) {
            ApiResponse response = new ApiResponse();
            ResponseData data = new ResponseData();
            Budget budget = budgetService.deleteData(budgetID);
            if (budget != null) {
                data.setResult(budget);
                response.setResponseMessage("ลบเรียบร้อย");
                response.setResponseData(data);
                return ResponseEntity.ok().body(response);
            } else {
                response.setResponseMessage("ไม่สามารถทำรายการได้");
                return ResponseEntity.badRequest().body(response);
            }
        } 


    @PutMapping("/editBudget")
    public ResponseEntity<ApiResponse> updateBudget(
        @RequestBody CreateBudgetRequest createbudgetRequest
    ) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if (budgetService.isBudgetNull(createbudgetRequest)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Budget budget = budgetService.editBudget(createbudgetRequest);
            data.setResult(budget);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/editEmployee").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }        

    @GetMapping("/findTotalBudget")
    public  LinkedHashMap<String, Object>findTotal(@RequestParam String Year,Long sectorId) {
        LinkedHashMap<String, Object> resultList = budgetService.total_exp(Year,sectorId);

        return resultList;
    }

    @GetMapping("/findRemainBudget")
    public  LinkedHashMap<String, Object> findRemain(@RequestParam int Year,Long sectorId) {
         LinkedHashMap<String, Object> resultList = budgetService.totalPriceRemaining(Year,sectorId);

        return resultList;
    }
    
}

