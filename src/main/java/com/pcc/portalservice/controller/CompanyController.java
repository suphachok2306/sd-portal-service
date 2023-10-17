package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.CompanyService;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class CompanyController {

  private final CompanyService companyService;

  //สร้าง Company
  @PostMapping("/createCompany")
  public ResponseEntity<ApiResponse> createCompany(
    @RequestBody CreateCompanyRequest createCompanyRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (companyService.isCompanyNull(createCompanyRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Company company = companyService.create(createCompanyRequest);
      data.setResult(company);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/createCompany")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //หา Company
  @GetMapping("/findAllCompany")
  public List<Company> getAllCompany() {
    return companyService.findAll();
  }
}
