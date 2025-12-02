package com.weblab.server.service;

import com.weblab.common.result.ApiResult;
import com.weblab.server.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

public interface OssFileService {
    FileVO uploadFile(MultipartFile file);
    ApiResult deleteFile(long id);
}
