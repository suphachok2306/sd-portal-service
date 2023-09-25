package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Company;
import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.requests.CreateCompanyRequest;
import com.pcc.portalservice.requests.CreateSectorRequest;
import com.pcc.portalservice.service.SectorService;
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
public class SectorController {
    private final SectorService sectorService;

    @PostMapping("/creatSector")
    public ResponseEntity<Sector> createCompany(@RequestBody CreateSectorRequest createSectorRequest) {
        Sector createdSector = sectorService.create(createSectorRequest);
        return new ResponseEntity<>(createdSector, HttpStatus.CREATED);
    }

    @GetMapping("/findAllSector")
    public List<Sector> getAllSector() {return sectorService.findAll();}
}
