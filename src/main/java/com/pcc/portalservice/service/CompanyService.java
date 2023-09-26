package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.repository.CompanyRepository;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public boolean isCompanyNull(Company company) {
        if(company == null || company.getCompanyName() == null || company.getCompanyName().isEmpty()){
            return true;
        }
        return false;
    }

    public Company create(CreateCompanyRequest createCompanyRequest){
        Company company = Company.builder()
                .id(createCompanyRequest.getCompanyId())
                .companyName(createCompanyRequest.getCompanyName())
                .build();
        return companyRepository.save(company);
    }


    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}
