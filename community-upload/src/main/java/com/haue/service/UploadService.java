package com.haue.service;

import com.haue.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult upload(MultipartFile img);
}
