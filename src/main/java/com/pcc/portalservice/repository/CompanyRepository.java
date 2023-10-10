package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
  Optional<Company> findByCompanyName(String companyName);
}
