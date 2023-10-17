package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Sector;
import com.pcc.portalservice.requests.CreateSectorRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.SectorService;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class SectorController {

  private final SectorService sectorService;

  //สร้าง Sector
  @PostMapping("/creatSector")
  public ResponseEntity<ApiResponse> createCompany(
    @RequestBody CreateSectorRequest createSectorRequest
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (sectorService.isSectorNull(createSectorRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Sector sector = sectorService.create(createSectorRequest);
      data.setResult(sector);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/createSector")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  //หา Sector ทั้งหมด
  @GetMapping("/findAllSector")
  public List<Sector> getAllSector() {
    return sectorService.findAll();
  }
}
