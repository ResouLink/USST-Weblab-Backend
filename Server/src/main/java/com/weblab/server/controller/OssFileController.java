package com.weblab.server.controller;


import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.service.OssFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
@RequiredArgsConstructor
public class OssFileController {
    private final OssFileService ossFileService;

    @PostMapping
    public ApiResult upload(MultipartFile file){
        return ossFileService.uploadFile(file);
    }
    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Long id){
        return ossFileService.deleteFile(id);
    }
}
