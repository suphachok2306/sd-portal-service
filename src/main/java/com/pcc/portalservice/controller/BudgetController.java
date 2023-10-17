package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.BudgetService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
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

@RestController
@AllArgsConstructor
@BasePathAwareController
public class BudgetController {

    private final BudgetService budgetService;

    //สร้าง Budget
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
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    //หา Bugdet ทั้งหมด
    @GetMapping("/findAllBudget")
    public List<Budget> getAllBudgets() {return budgetService.findAll();}

    //หา Bugdet ด้วย Id
    @GetMapping("/findBudgetById")
    public ResponseEntity<Budget> findBudgetById(@RequestParam Long budgetID) {
        Budget budget= budgetService.findById(budgetID);
        return ResponseEntity.ok(budget);
    }

    //ลบ Bugdet ด้วย Id
    @DeleteMapping("/deleteBudgetById")
    public ResponseEntity<ApiResponse> delete(@RequestParam Long budgetID) {
            ApiResponse response = new ApiResponse();
            ResponseData data = new ResponseData();
            String budget = budgetService.deleteData(budgetID);
            if (budget != null) {
                data.setResult(budget);
                response.setResponseMessage("ลบข้อมูลเรียบร้อย");
                response.setResponseData(data);
                return ResponseEntity.ok().body(response);
            } else {
                response.setResponseMessage("ไม่สามารถทำรายการได้");
                return ResponseEntity.badRequest().body(response);
            }
        } 

    //แก้ไข Bugdet
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
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }        

    //หางบทั้งหมดของแต่ละ Year และ Department
    @GetMapping("/findTotalBudget")
    public  LinkedHashMap<String, Object>findTotal(@RequestParam String Year,Long department_id) {
        LinkedHashMap<String, Object> resultList = budgetService.total_exp(Year,department_id);

        return resultList;
    }

    //หางบท่ี่เหลือของแต่ละ Year และ Department
    @GetMapping("/findRemainBudget")
    public  LinkedHashMap<String, Object> findRemain(@RequestParam int Year,Long department_id) {
         LinkedHashMap<String, Object> resultList = budgetService.totalPriceRemaining(Year,department_id);

        return resultList;
    }
    
}