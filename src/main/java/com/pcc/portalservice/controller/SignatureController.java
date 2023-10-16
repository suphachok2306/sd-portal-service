package com.pcc.portalservice.controller;

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


    @PostMapping(value = "/uploadSignatureImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSignatureImage(@RequestParam Long userId,@RequestParam("file") MultipartFile file) {
        try {
            signatureService.uploadSignature(userId, file);
            return ResponseEntity.ok("ลายเซ็นถูกอัพโหลดเรียบร้อยแล้ว");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("เกิดข้อผิดพลาดในการอัพโหลดลายเซ็น: " + e.getMessage());
        }
    }

    @GetMapping(value = "/getSignatureImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getSignatureImage(@RequestParam Long userId) {
        try {
            byte[] signatureImage = signatureService.getSignatureImage(userId);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
            //return new ResponseEntity<>(signatureImage, headers, HttpStatus.OK);
            return new ResponseEntity<>(signatureImage, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}