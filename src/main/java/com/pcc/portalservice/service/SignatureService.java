package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Signature;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.repository.SignatureRepository;
import com.pcc.portalservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final UserRepository userRepository;

    public static byte[] convertJPEGtoPNG(byte[] jpegImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(jpegImage));

        if (bufferedImage == null) {
            throw new IOException("ไม่สามารถอ่านรูปภาพจากข้อมูล JPEG");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    public Object uploadSignature(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("ไม่พบผู้ใช้ด้วย ID: " + userId));

        byte[] signatureImage = file.getBytes();
        byte[] pngImageData = convertJPEGtoPNG(signatureImage);
        Signature signature = user.getSignature();

        if (signature == null) {
            signature = Signature.builder()
                    .user(user)
                    .image(pngImageData)
                    .build();
        } else {
            signature.setImage(pngImageData);
        }
        return signatureRepository.save(signature);
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
}

