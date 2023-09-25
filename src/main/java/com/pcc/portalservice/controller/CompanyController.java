package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import com.pcc.portalservice.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/creatCompany")
    public ResponseEntity<Company> createCompany(@RequestBody CreateCompanyRequest createCompanyRequest) {
        Company createdCompany = companyService.create(createCompanyRequest);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @GetMapping("/findAllCompany")
    public List<Company> getAllCompany() {return companyService.findAll();}
}
