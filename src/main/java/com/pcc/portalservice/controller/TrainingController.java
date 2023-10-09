package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Result;
import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;
// import com.pcc.portalservice.requests.CreateTrainingSection2Request;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class TrainingController {
    private TrainingService trainingService;

    @PostMapping("/createTraining")
    public ResponseEntity<ApiResponse> createTraining(@RequestBody CreateTrainingRequest createTrainingRequest) throws ParseException {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if (trainingService.isTrainingNull(createTrainingRequest)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Training training = trainingService.createTraining(createTrainingRequest);
            data.setResult(training);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createTraining").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/editTrainingSection1")
    public ResponseEntity<ApiResponse> editTrainingSection1(@RequestParam Long trainingId,@RequestBody EditTrainingSection1Request editTraining) throws ParseException {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if (trainingService.isTrainingNull2(editTraining)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Training training = trainingService.editTrainingSection1(trainingId,editTraining);
            data.setResult(training);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/editTraining").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/editTrainingSection2")
    public ResponseEntity<ApiResponse> editTrainingSection2(@RequestParam Long resultId,@RequestBody EditTrainingSection2Request editTraining) throws ParseException {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if (trainingService.isEditTrainingNull2(editTraining)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Result training = trainingService.editTrainingSection2(resultId,editTraining);
            data.setResult(training);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/editTraining").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/setStatusToTraining")
    public ResponseEntity<ApiResponse> addStatusToTraining(@RequestParam Long trainingId, Long approveId , StatusApprove statusApprove) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            Training training = trainingService.setStatusToTraining(trainingId,approveId,statusApprove);
            data.setResult(training);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping("/findTrainingByTrainingId")
    public ResponseEntity<List<Map<String, Object>>> findTrainingByTrainingId(@RequestParam Long trainingId) {
        List<Map<String, Object>> training = trainingService.findById(trainingId);
        return ResponseEntity.ok(training);
    }
    

    @GetMapping("/findTrainingByUserId")
    public ResponseEntity<List<Map<String, Object>>> findTrainingByUserId(@RequestParam Long userId) {
        List<Map<String, Object>> trainings = trainingService.findTrainingsByUserId(userId);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/findTrainingByApprove1Id")
    public ResponseEntity<List<Map<String, Object>>>  findTrainingByApprove1Id(@RequestParam Long approve1Id) {
         List<Map<String, Object>> trainings = trainingService.findTrainingsByApprove1Id(approve1Id);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/findAllTraining")
    public List<Map<String, Object>> findAllTraining() {
        return trainingService.findAllTraining();
    }


    @GetMapping("/findAllApprove")
    public ResponseEntity<List<Map<String, Object>>> findAllApprove(@RequestParam Long count) {
        List<Map<String, Object>> training = trainingService.findbyAllCountApprove(count);
        if(count > 3){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(training);
    }


    @GetMapping("/searchTraining")
    public Object search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) String courseName
    ) {
        return trainingService.searchTraining(name,position,department,startDate,endDate,courseName);
    }


    @GetMapping("/findNextApprove")
    public List<Map<String, Object>> findNextApprove() {
        return trainingService.findNextApprove();
    }

}
