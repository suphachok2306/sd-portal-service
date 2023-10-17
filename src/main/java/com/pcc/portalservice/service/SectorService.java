package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.repository.CompanyRepository;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateSectorRequest;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class SectorService {

  private final SectorRepository sectorRepository;
  private final CompanyRepository companyRepository;

  /**
   * @สร้างSector
   */
  public Sector create(CreateSectorRequest createSectorRequest) {
    Company companyID = companyRepository
      .findById(createSectorRequest.getCompanyId())
      .orElseThrow(() ->
        new RuntimeException(
          "sectorId not found: " + createSectorRequest.getCompanyId()
        )
      );

    Sector sector = Sector
      .builder()
      .sectorCode(createSectorRequest.getSectorCode())
      .sectorName(createSectorRequest.getSectorName())
      .company(companyID)
      .build();
    return sectorRepository.save(sector);
  }

  /**
   * @หาSectorทั้งหมด
   */
  public List<Sector> findAll() {
    return sectorRepository.findAll();
  }


  /**
   * @หาSectorด้วยSectorCodeและSectorNameและCompanyName
   */
  public Optional<Sector> findBySectorCodeAndSectorNameAndCompanyName(
    String sectorCode,
    String sectorName,
    String companyName
  ) {
    return sectorRepository.findBySectorCodeAndSectorNameAndCompanyCompanyName(
      sectorCode,
      sectorName,
      companyName
    );
  }

  /**
   * @เช็คNullของSector
   */
  public boolean isSectorNull(CreateSectorRequest request) {
    return (
      request == null ||
      request.getSectorCode() == null ||
      request.getSectorCode().isEmpty() ||
      request.getSectorName() == null ||
      request.getSectorName().isEmpty()
    );
  }
}
