package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Budget;
import com.pcc.portalservice.requests.CreateBudgetRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.BudgetService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class BudgetController {

  private final BudgetService budgetService;

  /**
   * @สร้างBudget
   * @PostMapping
   */
  @PostMapping("/createBudget")
  public ResponseEntity<ApiResponse> createBudget(
    @RequestBody CreateBudgetRequest createbudgetRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (budgetService.isBudgetNull(createbudgetRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Budget budget = budgetService.create(createbudgetRequest);
      data.setResult(budget);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/createBudget")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  /**
   * @ลบBudgetด้วยId
   * @DeleteMapping
   */
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

  /**
   * @แก้ไขBudget
   * @PutMapping
   */
  @PutMapping("/editBudget")
  public ResponseEntity<ApiResponse> updateBudget(
    @RequestBody CreateBudgetRequest createbudgetRequest,
    @RequestParam Long budgetID
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (budgetService.isBudgetNull(createbudgetRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Budget budget = budgetService.editBudget(createbudgetRequest, budgetID);
      data.setResult(budget);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/editEmployee")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  /**
   * @หางบทั้งหมดของแต่ละYearและDepartment
   * @GetMapping
   */
//  @GetMapping("/findTotalBudget")
//  public LinkedHashMap<String, Object> findTotal(
//    @RequestParam String Year,
//    @RequestParam Long department_id
//  ) {
//    LinkedHashMap<String, Object> resultList = budgetService.total_exp(
//      Year,
//      department_id
//    );
//
//    return resultList;
//  }
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
      response.setResponseMessage(e.getMessage());
      if (e.getMessage().equals("Cannot invoke \"java.lang.Float.floatValue()\" because the return value of \"com.pcc.portalservice.service.BudgetService.getTotalBudgetCer(java.lang.Long, String)\" is null")) {
        response.setResponseMessage("ยังไม่มีงบในระบบ");
      }
      return ResponseEntity.badRequest().body(response);
    }
  }

  /**
   * @หางบทั้งหมดของแต่ละYearและDepartment
   * @GetMapping
   */
  @GetMapping("/findTotalRemain")
  public Object  findtotalPriceRemaining(
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
      response.setResponseMessage(e.getMessage());
      if (e.getMessage().equals("Cannot invoke \"java.lang.Float.floatValue()\" because the return value of \"com.pcc.portalservice.service.BudgetService.getTotalBudgetCer(java.lang.Long, String)\" is null")) {
        response.setResponseMessage("ยังไม่มีงบในระบบ");
      }
      return ResponseEntity.badRequest().body(response);
    }
  }

  // /**
  //  * @หางบท่ี่เหลือของแต่ละYearและDepartment
  //  * @GetMapping
  //  */
  // @GetMapping("/findRemainBudget")
  // public LinkedHashMap<String, Object> findRemain(
  //   @RequestParam int Year,
  //   @RequestParam Long department_id,
  //   @RequestParam String type
  // ) {
  //   LinkedHashMap<String, Object> resultList = budgetService.totalPriceRemaining(
  //     Year,
  //     department_id,
  //     type
  //   );

  //   return resultList;
  // }

  /**
   * @หาBudgetทั้งหมด
   * @GetMapping
   */
  @GetMapping("/findAllBudget")
  public ResponseEntity<List<Map<String, Object>>> getAllBudgets() {
    List<Map<String, Object>> budget = budgetService.findAlls();
    return ResponseEntity.ok(budget);
  }

  /**
   * @หาBudgetด้วยId
   * @GetMapping
   */
  @GetMapping("/findBudgetById")
  public ResponseEntity<Budget> findBudgetById(@RequestParam Long budgetID) {
    Budget budget = budgetService.findById(budgetID);
    return ResponseEntity.ok(budget);
  }

  /**
   * @หางบทั้งหมดของแต่ละYearและDepartment
   * @GetMapping
   */
  @GetMapping("/findBudget")
  public List<LinkedHashMap<String, Object>> findBudget(
    @RequestParam(required = false)String Year,
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
}
