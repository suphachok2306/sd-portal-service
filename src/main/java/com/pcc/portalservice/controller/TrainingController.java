package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class TrainingController {
    private TrainingService trainingService;

    @PostMapping("/createSectionOne")
    public ResponseEntity<Training> createSectionOne(@RequestBody CreateTrainingSectionOneRequest createTrainingSectionOneRequest) throws ParseException {
        Training training = trainingService.createSectionOne(createTrainingSectionOneRequest);
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
    public ResponseEntity<List<Training>> findAllTraining() {
        List<Training> trainings = trainingService.findAllTraining();
        return ResponseEntity.ok(trainings);
    }
}
