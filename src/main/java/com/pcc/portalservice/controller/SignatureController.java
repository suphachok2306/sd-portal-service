package com.pcc.portalservice.controller;

import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@RestController
@AllArgsConstructor
public class SignatureController {

    private final SignatureService signatureService;

    //อัพโหลด Signature,สร้าง Signature
    @PostMapping(value = "/uploadSignatureImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSignatureImage(@RequestParam Long userId,@RequestParam("file") MultipartFile file) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            Object result = signatureService.uploadSignature(userId, file);
            data.setResult(result);
            response.setResponseMessage("ลายเซ็นถูกอัพโหลดเรียบร้อยแล้ว");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            response.setResponseMessage("เกิดข้อผิดพลาดในการอัพโหลดลายเซ็น: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //หา Signature
    @GetMapping(value = "/getSignatureImage", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getSignatureImage(@RequestParam Long userId) {
        try {
            byte[] signatureImage = signatureService.getSignatureImage(userId);
            return new ResponseEntity<>(signatureImage, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}