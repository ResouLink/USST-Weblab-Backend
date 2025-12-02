package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.service.OssFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oss")
public class OssFileController {

    private final OssFileService ossFileService;

    @PostMapping
    public ApiResult uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            return ApiResult.success(ossFileService.uploadFile(file));
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ApiResult.fail("文件上传失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteFile(@PathVariable long id) {
        try {
            return ossFileService.deleteFile(id);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return ApiResult.fail("文件删除失败: " + e.getMessage());
        }
    }
}
