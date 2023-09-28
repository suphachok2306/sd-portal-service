package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    Optional<Sector> findBySectorName(String sectorName);

    Optional<Sector> findBySectorCode(String sectorCode);

    Optional<Sector> findById(Long sectorId);

    Optional<Sector> findBySectorCodeAndSectorNameAndCompanyCompanyName(String sectorCode, String sectorName, String companyName);

}
