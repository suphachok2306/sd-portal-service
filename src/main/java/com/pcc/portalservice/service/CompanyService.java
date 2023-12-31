package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.repository.CompanyRepository;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class CompanyService {

  private final CompanyRepository companyRepository;

  public Company create(CreateCompanyRequest createCompanyRequest) {
    Company company = Company
      .builder()
      .companyName(createCompanyRequest.getCompanyName())
      .build();
    return companyRepository.save(company);
  }

  public boolean isCompanyNull(CreateCompanyRequest request) {
    return (
      request == null ||
      request.getCompanyName() == null ||
      request.getCompanyName().isEmpty()
    );
  }
}
