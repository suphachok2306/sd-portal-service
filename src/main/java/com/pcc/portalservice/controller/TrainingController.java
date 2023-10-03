package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import com.pcc.portalservice.requests.CreateTrainingSectionTwoRequest;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
}
