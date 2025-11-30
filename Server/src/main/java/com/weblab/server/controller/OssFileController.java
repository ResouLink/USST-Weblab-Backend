package com.weblab.server.controller;


import com.weblab.common.core.domain.ApiResult;
import com.weblab.server.service.OssFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
@RequiredArgsConstructor
public class OssFileController {



    private final OssFileService ossFileService;

    /**
     * 文件上传
     */
    @PostMapping
    public ApiResult upload(@RequestPart("file") MultipartFile file) {
        try {
            return ossFileService.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 文件删除
     */
    @DeleteMapping(value = "/{id}")
    public ApiResult delete(@PathVariable Long id) {
        try {
            return ossFileService.deleteFile(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("文件删除失败: " + e.getMessage());
        }
    }
}
