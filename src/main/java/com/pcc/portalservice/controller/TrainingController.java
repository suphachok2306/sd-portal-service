package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Training> createTraining(@RequestBody CreateTrainingRequest createTrainingRequest) throws ParseException {
        Training training = trainingService.createTraining(createTrainingRequest);
        return ResponseEntity.ok(training);
    }

    @PostMapping("/editTraining")
    public ResponseEntity<Training> editTraining(@RequestParam Long trainingId, Long statusId, Long resultId,@RequestBody CreateTrainingRequest editTraining) throws ParseException {
        Training training = trainingService.editTraining(trainingId,statusId,resultId,editTraining);
        return ResponseEntity.ok(training);
    }

    @PutMapping("/setStatusToTraining")
    public ResponseEntity<Training> addStatusToTraining(@RequestParam Long trainingId, Long approveId , StatusApprove statusApprove) {
        Training training = trainingService.setStatusToTraining(trainingId,approveId,statusApprove);
        return ResponseEntity.ok(training);
    }

    @GetMapping("/findTrainingByTrainingId")
    public ResponseEntity<Training> findTrainingByTrainingId(@RequestParam Long trainingId) {
        Training training = trainingService.findById(trainingId);
        if (training != null) {
            return new ResponseEntity<>(training, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findTrainingByUserId")
    public ResponseEntity<List<Training>> findTrainingByUserId(@RequestParam Long userId) {
        List<Training> trainings = trainingService.findTrainingsByUserId(userId);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/findTrainingByApprove1Id")
    public ResponseEntity<List<Training>> findTrainingByApprove1Id(@RequestParam Long approve1Id) {
        List<Training> trainings = trainingService.findTrainingsByApprove1Id(approve1Id);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/findAllTraining")
    public ResponseEntity<List<Map<String, Object>>>findAllTraining() {
        List<Map<String, Object>> trainings = trainingService.findAllTraining();
        return ResponseEntity.ok(trainings);
    }


    @GetMapping("/findAllApprove")
    public ResponseEntity<List<Map<String, Object>>> findAllApprove(@RequestParam Long count) {
        List<Map<String, Object>> trainings = trainingService.findbyAllCountApprove(count);
        return ResponseEntity.ok(trainings);
    }




}
