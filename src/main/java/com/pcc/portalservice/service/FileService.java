package com.pcc.portalservice.service;


import com.pcc.portalservice.model.TrainingFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;


public abstract class FileService {

    public abstract LinkedHashMap<String, Object> getFile(Long Id);

    public abstract LinkedHashMap<String, Object> save(MultipartFile multipartFile);
}
