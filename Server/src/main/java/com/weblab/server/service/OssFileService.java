package com.weblab.server.service;

import com.weblab.server.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

public interface OssFileService {
    FileVO uploadFile(MultipartFile file);
    void deleteFile(long id);
}
