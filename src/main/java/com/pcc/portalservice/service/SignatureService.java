package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Signature;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.repository.SignatureRepository;
import com.pcc.portalservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final UserRepository userRepository;

    //แบบเก็บเป็น byte

    public void uploadSignature(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("ไม่พบผู้ใช้ด้วย ID: " + userId));

//        String filename = userId + "_signature." + file.getOriginalFilename().split("\\.")[1];
//        String path = System.getProperty("user.home") + "/Downloads/uploads/" + filename;
//
//        try (FileOutputStream fos = new FileOutputStream(path)) {
//            fos.write(file.getBytes());
//        }

        byte[] signatureImage = file.getBytes();
        Signature signature = user.getSignature();

        if (signature == null) {
            signature = Signature.builder()
                    .user(user)
                    .image(signatureImage)
                    .build();
        } else {
            signature.setImage(signatureImage);
        }
        signatureRepository.save(signature);
    }

    public byte[] getSignatureImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("ไม่พบผู้ใช้ด้วย ID: " + userId));

        Signature signature = user.getSignature();

        if (signature != null) {
            return signature.getImage();
        } else {
            throw new EntityNotFoundException("ไม่พบลายเซ็นสำหรับผู้ใช้ด้วย ID: " + userId);
        }
    }


    //แบบเก็บเป็น string

//    public void uploadSignature(Long userId, MultipartFile file) throws IOException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("ไม่พบผู้ใช้ด้วย ID: " + userId));
//
//
//        String filename = userId + "_signature." + file.getOriginalFilename().split("\\.")[1];
//        String path = System.getProperty("user.home") + "/Downloads/uploads/" + filename;
//
//        try (FileOutputStream fos = new FileOutputStream(path)) {
//            fos.write(file.getBytes());
//        }
//        Signature signature = user.getSignature();
//
//        if (signature == null) {
//            signature = Signature.builder()
//                    .user(user)
//                    .image(filename)
//                    .build();
//        } else {
//            signature.setImage(filename);
//        }
//
//        signatureRepository.save(signature);
//    }

}

