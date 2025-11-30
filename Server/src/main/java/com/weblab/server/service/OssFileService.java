package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import org.springframework.web.multipart.MultipartFile;

public interface OssFileService {
    ApiResult uploadFile(MultipartFile file);
    ApiResult deleteFile(long id);
}
