package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Sector;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
  Optional<Sector> findBySectorName(String sectorName);

  Optional<Sector> findBySectorCode(String sectorCode);

  Optional<Sector> findBySectorCodeAndSectorNameAndCompanyCompanyName(
    String sectorCode,
    String sectorName,
    String companyName
  );
}
