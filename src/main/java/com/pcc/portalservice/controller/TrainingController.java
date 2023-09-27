package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class TrainingController {
    private TrainingService trainingService;

    @PostMapping("/createSectionOne")
    public ResponseEntity<Training> create(@RequestBody CreateTrainingSectionOneRequest createTrainingSectionOneRequest) {
        Training training = trainingService.createSectionOne(createTrainingSectionOneRequest);
        return ResponseEntity.ok(training);
    }
}
