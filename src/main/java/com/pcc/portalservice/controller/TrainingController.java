package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
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
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/editTraining")
    public ResponseEntity<ApiResponse> editTraining(@RequestParam Long trainingId, Long statusId, Long resultId,@RequestBody CreateTrainingRequest editTraining) throws ParseException {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if (trainingService.isEditTrainingNull(editTraining)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Training training = trainingService.editTraining(trainingId,statusId,resultId,editTraining);
            data.setResult(training);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/editTraining").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/setStatusToTraining")
    public ResponseEntity<ApiResponse> addStatusToTraining(@RequestParam Long trainingId, Long approveId , StatusApprove statusApprove) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        // if(trainingService.hasStatus(trainingId,statusApprove)){
        //     response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
        //     return ResponseEntity.badRequest().body(response);
        // }
        try {
            Training training = trainingService.setStatusToTraining(trainingId,approveId,statusApprove);
            data.setResult(training);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
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
    public List<Map<String, Object>> findAllApprove(@RequestParam Long count) {
        return trainingService.findbyAllCountApprove(count);
    }




}
