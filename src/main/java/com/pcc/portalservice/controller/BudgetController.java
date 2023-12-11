package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Budget;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.BudgetService;
import java.net.URI;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class BudgetController {

  private final BudgetService budgetService;

  // @PostMapping("/createBudget")
  // public ResponseEntity<ApiResponse> createBudget(
  //   @RequestBody CreateBudgetRequest createbudgetRequest
  // ) {
  //   ApiResponse response = new ApiResponse();
  //   ResponseData data = new ResponseData();
  //   if (budgetService.isBudgetNull(createbudgetRequest)) {
  //     response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
  //     return ResponseEntity.badRequest().body(response);
  //   }
  //   try {
  //     Budget budget = budgetService.create(createbudgetRequest);
  //     data.setResult(budget);
  //     response.setResponseMessage("ทำรายการเรียบร้อย");
  //     response.setResponseData(data);
  //     URI uri = URI.create(
  //       ServletUriComponentsBuilder
  //         .fromCurrentContextPath()
  //         .path("/createBudget")
  //         .toUriString()
  //     );
  //     return ResponseEntity.created(uri).body(response);
  //   } catch (Exception e) {
  //     response.setResponseMessage(e.getMessage());
  //     return ResponseEntity.internalServerError().body(response);
  //   }
  // }

  // @PutMapping("/editBudget")
  // public ResponseEntity<ApiResponse> updateBudget(
  //   @RequestBody CreateBudgetRequest createbudgetRequest,
  //   @RequestParam Long budgetID
  // ) {
  //   ApiResponse response = new ApiResponse();
  //   ResponseData data = new ResponseData();
  //   if (budgetService.isBudgetNull(createbudgetRequest)) {
  //     response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
  //     return ResponseEntity.badRequest().body(response);
  //   }
  //   try {
  //     Budget budget = budgetService.editBudget(createbudgetRequest, budgetID);
  //     data.setResult(budget);
  //     response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
  //     response.setResponseData(data);
  //     URI uri = URI.create(
  //       ServletUriComponentsBuilder
  //         .fromCurrentContextPath()
  //         .path("/editEmployee")
  //         .toUriString()
  //     );
  //     return ResponseEntity.created(uri).body(response);
  //   } catch (Exception e) {
  //     response.setResponseMessage(e.getMessage());
  //     return ResponseEntity.internalServerError().body(response);
  //   }
  // }

  @GetMapping("/findTotalBudget")
  public Object findTotal(
    @RequestParam String Year,
    @RequestParam Long department_id
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();

    try {
      LinkedHashMap<String, Object> resultList = budgetService.total_exp(
        Year,
        department_id
      );

      if (resultList == null) {
        // department_id เกินจากฐานข้อมูลที่มีอยู่
        response.setResponseMessage("department_id เกินจากฐานข้อมูลที่มีอยู่");
        response.setResponseData((ResponseData) Collections.emptyList());
        return response;
      }

      data.setResult(resultList);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);

      return resultList;
    } catch (Exception e) {
      response.setResponseMessage("ยังไม่มีงบในระบบ");
      return ResponseEntity.badRequest().body(response);
    }
  }

  @GetMapping("/findTotalRemain")
  public Object findtotalPriceRemaining(
    @RequestParam String Year,
    @RequestParam Long department_id
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    try {
      LinkedHashMap<String, Object> resultList = budgetService.totalPriceRemaining(
        Year,
        department_id
      );

      if (resultList == null) {
        // department_id เกินจากฐานข้อมูลที่มีอยู่
        response.setResponseMessage("department_id เกินจากฐานข้อมูลที่มีอยู่");
        response.setResponseData((ResponseData) Collections.emptyList());
        return response;
      }

      data.setResult(resultList);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);

      return resultList;
    } catch (Exception e) {
      response.setResponseMessage("ยังไม่มีงบในระบบ");
    }
    return ResponseEntity.badRequest().body(response);
  }

  @GetMapping("/findAllBudget")
  public ResponseEntity<List<Map<String, Object>>> getAllBudgets() {
    List<Map<String, Object>> budget = budgetService.findAlls();
    return ResponseEntity.ok(budget);
  }

  @GetMapping("/findBudgetById")
  public ResponseEntity<Budget> findBudgetById(@RequestParam Long budgetID) {
    Budget budget = budgetService.findById(budgetID);
    return ResponseEntity.ok(budget);
  }

  @GetMapping("/findBudget")
  public List<LinkedHashMap<String, Object>> findBudget(
    @RequestParam(required = false) String Year,
    @RequestParam(required = false) Long department_id,
    @RequestParam(required = false) Long company_id
  ) {
    List<LinkedHashMap<String, Object>> resultList = budgetService.find_budget(
      company_id,
      Year,
      department_id
    );

    return resultList;
  }

  @PostMapping("/createbudget")
  public ResponseEntity<ApiResponse> createOrUpdateBudget(
      @RequestBody CreateBudgetRequest createBudgetRequest){
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();

    if (budgetService.isBudgetNull(createBudgetRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }

    try {
      Budget budget = budgetService.saveOrUpdate(createBudgetRequest);
      data.setResult(budget);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);

      URI uri = URI.create(
          ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path("/api/budgets/budget")
              .queryParam("budgetID", budget.getId())
              .toUriString());

      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

}
