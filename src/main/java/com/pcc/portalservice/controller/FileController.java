package com.pcc.portalservice.controller;


import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.FileService;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;


@RestController
@AllArgsConstructor
public class FileController {

    TrainingService trainingService;
    FileService fileService;

    @PostMapping(
            value = "/uploadFile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public LinkedHashMap<String, Object> uploadFile(
            @RequestParam MultipartFile file
    ) {

        return fileService.save(file);

    }

    @GetMapping("/getFile")
    public LinkedHashMap<String, Object> getFile(
            @RequestParam Long fileID
    ) throws Exception {
        LinkedHashMap<String, Object> base64FileContent = fileService.getFile(fileID);
        return base64FileContent;
    }
}

