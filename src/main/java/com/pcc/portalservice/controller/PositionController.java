package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Position;
import com.pcc.portalservice.requests.CreatePositionRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class PositionController {
    private final PositionService positionService;


    @PostMapping("/createPosition")
    public ResponseEntity<ApiResponse> createPosition(@RequestBody CreatePositionRequest createPositionRequest) {
        Position position = positionService.create(createPositionRequest);
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(positionService.isPositionNull(position)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            data.setResult(position);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createPosition").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping("/findAllPositions")
    public List<Position> getAllPositions() {
        return positionService.findAll();
    }
}
