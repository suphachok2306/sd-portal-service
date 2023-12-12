package com.pcc.portalservice.service.implement;

import com.pcc.portalservice.model.TrainingFiles;
import com.pcc.portalservice.repository.FileRepository;
import com.pcc.portalservice.repository.TrainingRepository;
import com.pcc.portalservice.service.FileService;
import com.pcc.portalservice.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class FileServiceImpl extends FileService {

    TrainingRepository trainingRepository;
    TrainingService trainingService;
    FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(TrainingRepository trainingRepository, TrainingService trainingService, FileRepository fileRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingService = trainingService;
        this.fileRepository = fileRepository;
    }

    @Value("${file.path}")
    private String filepath;

    @Override
    public LinkedHashMap<String, Object> save(MultipartFile multipartFile) {
        String dir = System.getProperty("user.dir") + "/" + filepath;
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        String name = formattedDate + "_" + multipartFile.getOriginalFilename();
        try {
            multipartFile.transferTo(new File(dir + name));
            TrainingFiles file = TrainingFiles
                    .builder()
                    .fileName(multipartFile.getOriginalFilename())
                    .filePath(dir + name)
                    .fileType(multipartFile.getContentType())
                    .build();
            TrainingFiles trainingFiles = fileRepository.save(file);
            result.put("responseMessage","อัพไฟล์เรียบร้อย");
            result.put("ID",trainingFiles.getId());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Fail to upload");
        }
    }

    public LinkedHashMap<String, Object> getFile(Long id) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        Optional<TrainingFiles> file = fileRepository.findById(id);
        if (file.isPresent()) {
            String filePath = file.get().getFilePath();
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            System.out.println(path);
            try {
                byte[] fileContent = Files.readAllBytes(path);
                result.put("id", file.get().getId());
                result.put("type", file.get().getFileType());
                result.put("file", fileContent);
                return result;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new RuntimeException("No file in db");
        }
    }
}
