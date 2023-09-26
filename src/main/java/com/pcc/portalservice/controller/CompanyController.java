package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class CompanyController {
    private final CompanyService companyService;


    @PostMapping("/createCompany")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CreateCompanyRequest createCompanyRequest) {
        Company company = companyService.create(createCompanyRequest);
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(companyService.isCompanyNull(company)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            data.setResult(company);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createCompany").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/findAllCompany")
    public List<Company> getAllCompany() {return companyService.findAll();}
}
