package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.repository.SectorRepository;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import com.pcc.portalservice.requests.CreateSectorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SectorService {
    private final SectorRepository sectorRepository;

    public Sector create(CreateSectorRequest createSectorRequest){
        Sector sector = Sector.builder()
                .sectorCode(createSectorRequest.getSectorCode())
                .sectorName(createSectorRequest.getSectorName())
                .build();
        return sectorRepository.save(sector);
    }

    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }
}