package com.pcc.portalservice.controller;

import com.pcc.portalservice.service.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/signature")
public class SignatureController {

    private final SignatureService signatureService;


    @PostMapping(value = "/uploadSignature", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSignature(@RequestParam Long userId,@RequestParam("file") MultipartFile file) {
        try {
            signatureService.uploadSignature(userId, file);
            return ResponseEntity.ok("ลายเซ็นถูกอัพโหลดเรียบร้อยแล้ว");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("เกิดข้อผิดพลาดในการอัพโหลดลายเซ็น: " + e.getMessage());
        }
    }
}