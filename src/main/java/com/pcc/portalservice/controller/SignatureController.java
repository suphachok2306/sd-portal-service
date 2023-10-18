package com.pcc.portalservice.controller;
import com.pcc.portalservice.service.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
        try {
            signatureService.uploadSignature(userId, file);
            return ResponseEntity.ok("ลายเซ็นถูกอัพโหลดเรียบร้อยแล้ว");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("เกิดข้อผิดพลาดในการอัพโหลดลายเซ็น: " + e.getMessage());
        }
    }

    //หา Signature
    @GetMapping(value = "/getSignatureImage", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Object> getSignatureImage(@RequestParam Long userId) {
        try {
            byte[] signatureImage = signatureService.getSignatureImage(userId);
            return new ResponseEntity<>(signatureImage, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>("ไม่พบลายเซ็นของผู้ใช้", headers, HttpStatus.NOT_FOUND);
        }
    }
}